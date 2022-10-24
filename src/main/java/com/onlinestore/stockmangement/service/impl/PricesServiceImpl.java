package com.onlinestore.stockmangement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinestore.stockmangement.entity.Price;
import com.onlinestore.stockmangement.errors.ConflictPricesException;
import com.onlinestore.stockmangement.errors.PriceNotFoundException;
import com.onlinestore.stockmangement.model.PriceDTO;
import com.onlinestore.stockmangement.model.SearchParams;
import com.onlinestore.stockmangement.repository.PricesRepository;
import com.onlinestore.stockmangement.service.PricesService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PricesServiceImpl implements PricesService {

	@Autowired
	PricesRepository repository;
	
	@Override
	public PriceDTO findFinalPrice(SearchParams params) {
		
		// Retrieve data from db
		Optional<List<Price>> op = getPrices(params);
		
		// Check if price is found
		checkPriceNotFound(params, op);
		
		// if there are several prices, filter less priority prices
		checkMultiplePrices(params, op.get());
		
		log.info("price with the highest priority");
		return buildDto(op.get().get(0));
		
	}

	private void checkPriceNotFound(SearchParams p, Optional<List<Price>> op) {
		log.info("checking if price exists..."); 
		if (op.isEmpty())	{
			log.error("price with parameters passed not found. {}", p);
			throw PriceNotFoundException.builder()
					.brandId(p.getBrandId())
					.productId(p.getProductId())
					.date(p.getDate())
					.build();
		}
	}

	private void checkMultiplePrices(SearchParams p, List<Price> list) {
		log.info("checking if multiple prices are found...");
		if (list.size() > 1)	{
			// Items are ordered by priority (desc). If the first two have the same priority... we have a problem
			if (list.get(0).getPriority() == list.get(1).getPriority())	{
				// It cannot select only one
				log.error("multiple prices with the same priority. {}", p);
				throw ConflictPricesException.builder()
					.brandId(p.getBrandId())
					.productId(p.getProductId())
					.date(p.getDate())
					.build();
			}
		}
		if (log.isDebugEnabled())	{
			log.debug("prices found {}", list.size());
		}
	}

	private Optional<List<Price>> getPrices(SearchParams p)	{
		log.info("retrieving data from db...");
		List<Price> list = repository.findOrderedPrices(p.getBrandId(), p.getProductId(), p.getDate());
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
