package com.onlinestore.stockmangement;

import static java.time.LocalDateTime.now;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import com.onlinestore.stockmangement.entity.Price;
import com.onlinestore.stockmangement.errors.PriceNotFoundException;

public class GlobalTestingHelper {

	public static List<Price> buildPriceEntity() {
		return List.of(
				// Product 1, will be used regularly 
				Price.builder()
					.priceId(BigInteger.valueOf(1L))
					.brandId(1)
					.productId(1L)
					.priceList(1)
					.priority(0)
					.startDate(now().minusDays(1))
					.endDate(now().plusDays(1))
					.price(12.34F)
					.currency("EUR")
					.build(),
				// Product 2 (2 fares, distinct priority), overlap in time, will be used for testing priority
				Price.builder()
					.priceId(BigInteger.valueOf(2L))
					.brandId(1)
					.productId(2L)
					.priceList(1)
					.priority(0)
					.startDate(now().minusDays(2))
					.endDate(now().plusDays(1))
					.price(22.22F)
					.currency("EUR")
					.build(),			
				Price.builder()
					.priceId(BigInteger.valueOf(3L))
					.brandId(1)
					.productId(2L)
					.priceList(3)
					.priority(1)
					.startDate(now().minusDays(1))
					.endDate(now().plusDays(2))
					.price(33.33F)
					.currency("EUR")
					.build());
	}
	
	public static Throwable throwNotFoundException(Integer brandId, Long productId, LocalDateTime date) {
		return PriceNotFoundException.builder()
				.brandId(brandId)
				.productId(productId)
				.date(date)
				.build();
	}
}
