package com.onlinestore.stockmangement.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.onlinestore.stockmangement.entity.Price;
import com.onlinestore.stockmangement.errors.ConflictPricesException;
import com.onlinestore.stockmangement.errors.PriceNotFoundException;
import com.onlinestore.stockmangement.model.PriceDTO;
import com.onlinestore.stockmangement.repository.PricesRepository;
import com.onlinestore.stockmangement.service.PricesService;

public class PricesServiceImpl implements PricesService {

	@Autowired
	PricesRepository repository;
	
	@Override
	public PriceDTO findFinalPrice(Integer brandId, Long productId, LocalDateTime date) {
		
		// Retrieve data from db
		Optional<List<Price>> op = getPrices(brandId, productId, date);
		
		// Check if price is found
		checkPriceNotFound(brandId, productId, date, op);
		
		// if there are several prices, filter less priority prices
		checkMultiplePrices(brandId, productId, date, op.get());
		
		// Returns the highest priority
		return buildDto(op.get().get(0));
		
	}

	private void checkPriceNotFound(Integer brandId, Long productId, LocalDateTime date, Optional<List<Price>> op) {
		if (op.isEmpty())	{
			throw PriceNotFoundException.builder().brandId(brandId).productId(productId).date(date).build();
		}
	}

	private void checkMultiplePrices(Integer brandId, Long productId, LocalDateTime date,
			List<Price> list) {
		if (list.size() > 1)	{
			// Items are ordered by priority (desc). If the first two have the same priority... we have a problem
			if (list.get(0).getPriority() == list.get(1).getPriority())	{
				// It cannot select only one
				throw ConflictPricesException.builder().brandId(brandId).productId(productId).date(date).build();	
			}
		}
	}

	private Optional<List<Price>> getPrices(Integer brandId, Long productId, LocalDateTime date)	{
		List<Price> list = repository.findOrderedPrices(brandId, productId, date);
		if (null == list || list.isEmpty())	{
			return Optional.empty();
		}
		return Optional.of(list);
	}

	private PriceDTO buildDto(Price price) {
		return PriceDTO.builder()
				.brandId(price.getBrandId())
				.productId(price.getProductId())
				.appliedRate(price.getPriceList())
				.startDate(price.getStartDate())
				.endDate(price.getEndDate())
				.price(price.getPrice())
				.build();
	}
}
