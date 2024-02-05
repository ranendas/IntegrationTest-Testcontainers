package com.testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.testcontainers.controller.EmployeeController;
import com.testcontainers.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationtestTestcontainersApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");


	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@BeforeAll
	static void beforeAll() {
		mySQLContainer.start();
	}

	@AfterAll
	static void afterAll() {
		mySQLContainer.stop();
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(EmployeeController.class)
				.build();
	}


	@Test
	void contextLoads() {
	}

	@Test
	public void addEmployeeTest() throws Exception {
		Employee emp = Employee.builder().firstName("Rohan").lastName("Das").age("10").build();
		//call the API end point
		mockMvc.perform(MockMvcRequestBuilders.
				post("/employee").
				contentType("application/json").
				content(getJsonString(emp)).
				accept("application/json")).
				andExpect(status().isOk()).
				andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}

	@Test
	public void findAllTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.get("/employee")
						.accept("application/json")
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1));
	}

	private String getJsonString(Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
