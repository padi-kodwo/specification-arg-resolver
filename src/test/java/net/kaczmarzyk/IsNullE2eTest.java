/**
 * Copyright 2014-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kaczmarzyk;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.kaczmarzyk.spring.data.jpa.Customer;
import net.kaczmarzyk.spring.data.jpa.CustomerRepository;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;


public class IsNullE2eTest extends E2eTestBase {
	
	@Controller
	public static class TestController {
		
		@Autowired
		CustomerRepository customerRepo;
		
		@RequestMapping("/customersWithoutNickname")
		@ResponseBody
		public Object findCharactersWithoutNickname(
				@Spec(path = "nickName", spec = IsNull.class) Specification<Customer> spec) {
			return customerRepo.findAll(spec);
		}
	}
	
	@Test
	public void findsEntitiesWithNullAttributeValue() throws Exception {
		mockMvc.perform(get("/customersWithoutNickname")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$[0].firstName").value("Marge"))
			.andExpect(jsonPath("$[1].firstName").value("Lisa"))
			.andExpect(jsonPath("$[2].firstName").value("Maggie"))
			.andExpect(jsonPath("$[3].firstName").value("Moe"))
			.andExpect(jsonPath("$[4]").doesNotExist());
	}
}
