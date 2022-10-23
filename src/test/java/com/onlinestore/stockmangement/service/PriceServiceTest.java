package com.onlinestore.stockmangement.service;

import static com.onlinestore.stockmangement.GlobalTestingHelper.buildPriceEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

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
import com.onlinestore.stockmangement.repository.PricesRepository;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

	@InjectMocks
	PricesService service;
	
	@Mock
	PricesRepository repository;
	
	@Test
	void givenPriceExists_whenfindFinalPrice_thenPriceReturns() throws PriceNotFoundException	{
		Integer brandId = 1;
		Long productId = 1L;
		LocalDateTime date = LocalDateTime.now();
		Mockito.when(repository.findPvP(any(), any(), any())).thenReturn(buildPriceEntity());
		
		PriceDTO returned = service.findFinalPrice(brandId, productId, date);
		
		assertNotNull(returned);
		assertEquals(1, returned.getBrandId());
		assertEquals(1, returned.getProductId());
		assertEquals(1, returned.getAppliedRate());
		assertTrue(returned.getStartDate().isBefore(date));
		assertTrue(returned.getEndDate().isAfter(date));
		assertEquals((float)12.34, returned.getPrice());
		
	}
	
	@Test
	void givenPriceDoesntExist_whenfindFinalPrice_thenNotFoundException() throws PriceNotFoundException	{
		Integer brandId = 1;
		Long productId = 1L;
		LocalDateTime date = LocalDateTime.now();
		
		PriceNotFoundException ex = assertThrows(PriceNotFoundException.class, () -> {
			service.findFinalPrice(brandId, productId, date);
		});

		assertNotNull(ex);
		assertEquals(1, ex.getBrandId());
		assertEquals(1, ex.getProductId());
		assertEquals(date, ex.getDate());
		
	}
	
	@Test
	void givenPriceWithTwoFares_whenfindFinalPrice_thenPriceGreaterPriorityIsReturned() throws PriceNotFoundException	{
		Integer brandId = 1;
		Long productId = 2L;
		LocalDateTime date = LocalDateTime.now();
		Mockito.when(repository.findPvP(any(), any(), any())).thenReturn(buildPriceEntity());
		
		PriceDTO returned = service.findFinalPrice(brandId, productId, date);
		
		assertNotNull(returned);
		assertEquals(1, returned.getBrandId());
		assertEquals(2, returned.getProductId());
		assertEquals(1, returned.getAppliedRate());
		assertTrue(returned.getStartDate().isBefore(date));
		assertTrue(returned.getEndDate().isAfter(date));
		assertEquals((float)33.33, returned.getPrice());
		
	}
	
	@Test
	void givenPriceWithTwoFaresAndSamePriority_whenfindFinalPrice_thenConflictPriceExcep() throws PriceNotFoundException	{
		Integer brandId = 1;
		Long productId = 1L;
		LocalDateTime date = LocalDateTime.now();
		
		ConflictPricesException ex = assertThrows(ConflictPricesException.class, () -> {
			service.findFinalPrice(brandId, productId, date);
		});

		assertNotNull(ex);
		assertEquals(1, ex.getBrandId());
		assertEquals(2, ex.getProductId());
		assertEquals(date, ex.getDate());
		
	}
	
}
