package com.onlinestore.stockmangement.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.onlinestore.stockmangement.entity.Price;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PricesRepositoryTest {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private PricesRepository pricesRepository;

	@Test
	void componentInjection() {
		assertNotNull(dataSource);
		assertNotNull(entityManager);
		assertNotNull(pricesRepository);
	}
	
	@Test
	void givenBrandProductDate_whenFind_thenPriceReturn()	{
		int brandId = 1;
		long productId = 35455L;
		LocalDateTime aplicationDate = LocalDateTime.of(2020, Month.JUNE, 14, 11, 0);
		
		List<Price> list = pricesRepository.findPvP(brandId, productId, aplicationDate);
		
		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	@Test
	void givenBrandProductDate_whenFind_thenListPricesReturn()	{
		int brandId = 1;
		long productId = 35455L;
		LocalDateTime aplicationDate = LocalDateTime.of(2020, Month.JUNE, 14, 16, 0);
		
		List<Price> list = pricesRepository.findPvP(brandId, productId, aplicationDate);
		
		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	@Test
	void givenBrandProductDate_whenFind_thenNothingReturns()	{
		int brandId = 1;
		long productId = 35455L;
		LocalDateTime aplicationDate = LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0);
		
		List<Price> list = pricesRepository.findPvP(brandId, productId, aplicationDate);
		
		assertNotNull(list);
		assertEquals(0, list.size());
	}
}
