package com.onlinestore.stockmangement.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinestore.stockmangement.entity.Price;
import com.onlinestore.stockmangement.errors.ConflictPricesException;
import com.onlinestore.stockmangement.errors.PriceNotFoundException;
import com.onlinestore.stockmangement.model.PriceDTO;
import com.onlinestore.stockmangement.repository.PricesRepository;
import com.onlinestore.stockmangement.service.PricesService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
		
		log.info("price with the highest priority");
		return buildDto(op.get().get(0));
		
	}

	private void checkPriceNotFound(Integer brandId, Long productId, LocalDateTime date, Optional<List<Price>> op) {
		log.info("checking if price exists..."); 
		if (op.isEmpty())	{
			log.error("price with parameters passed not found. brandId={}, productId={}, date={}",
					brandId, productId, date);
			throw PriceNotFoundException.builder().brandId(brandId).productId(productId).date(date).build();
		}
	}

	private void checkMultiplePrices(Integer brandId, Long productId, LocalDateTime date,
			List<Price> list) {
		log.info("checking if multiple prices are found...");
		if (list.size() > 1)	{
			// Items are ordered by priority (desc). If the first two have the same priority... we have a problem
			if (list.get(0).getPriority() == list.get(1).getPriority())	{
				// It cannot select only one
				log.error("multiple prices with the same priority. brandId={}, productId={}, date={}",
						brandId, productId, date);
				throw ConflictPricesException.builder().brandId(brandId).productId(productId).date(date).build();	
			}
		}
		if (log.isDebugEnabled())	{
			log.debug("prices found {}", list.size());
		}
	}

	private Optional<List<Price>> getPrices(Integer brandId, Long productId, LocalDateTime date)	{
		log.info("retrieving data from db...");
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
