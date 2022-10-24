package com.onlinestore.stockmangement.service;

import static com.onlinestore.stockmangement.GlobalTestingHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.onlinestore.stockmangement.errors.ConflictPricesException;
import com.onlinestore.stockmangement.errors.PriceNotFoundException;
import com.onlinestore.stockmangement.model.PriceDTO;
import com.onlinestore.stockmangement.model.SearchParams;
import com.onlinestore.stockmangement.repository.PricesRepository;
import com.onlinestore.stockmangement.service.impl.PricesServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

	@InjectMocks
	PricesServiceImpl service;
	
	@Mock
	PricesRepository repository;
	
	@Test
	void givenPriceExists_whenfindFinalPrice_thenPriceReturns()	{
		Integer brandId = 1;
		Long productId = 1L;
		LocalDateTime date = LocalDateTime.now();
		Mockito.when(repository.findOrderedPrices(any(), any(), any())).thenReturn(buildPriceEntity());
		
		PriceDTO returned = service.findFinalPrice(SearchParams.builder()
				.brandId(brandId)
				.productId(productId)
				.date(date)
				.build());
		
		assertNotNull(returned);
		assertEquals(1, returned.getBrandId());
		assertEquals(1, returned.getProductId());
		assertEquals(1, returned.getAppliedRate());
		assertTrue(returned.getStartDate().isBefore(date));
		assertTrue(returned.getEndDate().isAfter(date));
		assertEquals((float)12.34, returned.getPrice());
		
	}
	
	@Test
	void givenPriceDoesntExist_whenfindFinalPrice_thenNotFoundException()	{
		Integer brandId = 1;
		Long productId = 1L;
		LocalDateTime date = LocalDateTime.now();
		
		PriceNotFoundException ex = assertThrows(PriceNotFoundException.class, () -> {
			service.findFinalPrice(SearchParams.builder()
					.brandId(brandId)
					.productId(productId)
					.date(date)
					.build());
		});

		assertNotNull(ex);
		assertEquals(1, ex.getBrandId());
		assertEquals(1, ex.getProductId());
		assertEquals(date, ex.getDate());
		
	}
	
	@Test
	void givenPriceWithTwoFares_whenfindFinalPrice_thenPriceGreaterPriorityIsReturned()	{
		Integer brandId = 1;
		Long productId = 2L;
		LocalDateTime date = LocalDateTime.now();
		when(repository.findOrderedPrices(any(), any(), any())).thenReturn(buildListPricesEntity());
		
		PriceDTO returned = service.findFinalPrice(SearchParams.builder()
				.brandId(brandId)
				.productId(productId)
				.date(date)
				.build());
		
		assertNotNull(returned);
		assertEquals(1, returned.getBrandId());
		assertEquals(2, returned.getProductId());
		assertEquals(1, returned.getAppliedRate());
		assertTrue(returned.getStartDate().isBefore(date));
		assertTrue(returned.getEndDate().isAfter(date));
		assertEquals((float)22.22, returned.getPrice());
		
	}
	
	@Test
	void givenPriceWithTwoFaresAndSamePriority_whenfindFinalPrice_thenConflictPriceExcep()	{
		Integer brandId = 1;
		Long productId = 3L;
		LocalDateTime date = LocalDateTime.now();
		when(repository.findOrderedPrices(any(), any(), any())).thenReturn(buildListCnflictPricesEntity());
		
		ConflictPricesException ex = assertThrows(ConflictPricesException.class, () -> {
			service.findFinalPrice(SearchParams.builder()
					.brandId(brandId)
					.productId(productId)
					.date(date)
					.build());
		});

		assertNotNull(ex);
		assertEquals(1, ex.getBrandId());
		assertEquals(3, ex.getProductId());
		assertEquals(date, ex.getDate());
		
	}
	
}
