package com.onlinestore.stockmangement.controller;

import static com.onlinestore.stockmangement.GlobalTestingHelper.throwNotFoundException;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.onlinestore.stockmangement.service.PricesService;

@SpringBootTest
@AutoConfigureMockMvc
public class PricesControllerTest {

	@Autowired
	MockMvc mvc;
	
	@MockBean
	PricesService service;
	
	@Test
	void givenCorrectArguments_whenGetRequest_thenSuccessReturns() throws Exception	{
		Integer brandId = 1;
		Long productId = 1L;
		String date = "2020-06-14T15:00:00";
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	void givenPriceDoesntExist_whenGetRequest_thenNotFoundCodeReturns() throws Exception	{
		Integer brandId = 1;
		Long productId = 1L;
		LocalDateTime date = now();
		when(service.findFinalPrice(any())).thenThrow(throwNotFoundException(brandId, productId, date));
		
		MockHttpServletResponse response = mvc.perform(
				get("/prices/brandId/" + brandId + "/productId/" + productId + "/date/" + date)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertNotNull(response);
		assertEquals(204, response.getStatus());
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
