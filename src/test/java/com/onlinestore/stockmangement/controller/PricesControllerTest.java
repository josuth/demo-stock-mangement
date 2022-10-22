package com.onlinestore.stockmangement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PricesControllerTest {

	@Autowired
	MockMvc mvc;
	
	@Test
	void givenCorrectArguments_whenGetRequest_thenSuccessReturns() throws Exception	{
		Integer brandId = 1;
		Long productId = 1L;
		String date = "2020-06-14%2015%3A00%3A00";
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	void givenNotNumericBrandId_whenGetRequest_thenErrorReturns() throws Exception	{
		String brandId = "abc";
		Long productId = 1L;
		String date = "2020-06-14%2015%3A00%3A00";
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(400, response.getStatus());
	}
	
	@Test
	void givenBrandIdExceeds_whenGetRequest_thenErrorReturns() throws Exception	{
		Integer brandId = 99999;
		Long productId = 1L;
		String date = "2020-06-14%2015%3A00%3A00";
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(400, response.getStatus());
	}
	
	@Test
	void givenNotNumericProductId_whenGetRequest_thenErrorReturns() throws Exception	{
		Integer brandId = 1;
		String productId = "abc";
		String date = "2020-06-14%2015%3A00%3A00";
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(400, response.getStatus());
	}
	
	@Test
	void givenProductIdExceeds_whenGetRequest_thenErrorReturns() throws Exception	{
		Integer brandId = 1;
		String productId = "abc";
		String date = "2020-06-14%2015%3A00%3A00";
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(400, response.getStatus());
	}
	
	@Test
	void givenIncorrectFormatDate_whenGetRequest_thenSuccessReturns() throws Exception	{
		Integer brandId = 1;
		Long productId = 1L;
		String date = "2020/06/14-15.00";
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(404, response.getStatus());
	}
	
}
