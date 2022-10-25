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

	PricesRepository repository;
	
	@Autowired
	public PricesServiceImpl(PricesRepository repository) {
		this.repository = repository;
	}

	@Override
	public PriceDTO findFinalPrice(SearchParams params) {
		
		List<Price> list = getPrices(params);

		Integer highestPriority = getHighestPriority(params, list);
			
		Price price = getSelectedPrice(params, list, highestPriority);
		
		return buildDto(price);
		
	}
	
	private List<Price> getPrices(SearchParams p)	{
		log.info("retrieving data from db...");
		List<Price> list = 
				Optional.ofNullable(repository.findOrderedPrices(p.getBrandId(), p.getProductId(), p.getDate()))
						.orElseThrow(() -> buildPriceNotFound(p));
		
		return list;
	}

	private Integer getHighestPriority(SearchParams p, List<Price> list) {
		return list.stream()				
				.mapToInt(Price::getPriority)
				.max()
				.orElseThrow(() -> buildPriceNotFound(p));
	}
	
	private Price getSelectedPrice(SearchParams p, List<Price> list, Integer higherPriority) {
		log.info("getting highest priority price...");
		return list.stream()
				.filter(i -> i.getPriority() == higherPriority)
				.reduce((a, b) ->{ throw buildConflictPricesExc(p); })
				.get();
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
	
	private PriceNotFoundException buildPriceNotFound(SearchParams p) {
		return PriceNotFoundException.builder()
				.brandId(p.getBrandId())
				.productId(p.getProductId())
				.date(p.getDate())
				.build();
	}
	
	private ConflictPricesException buildConflictPricesExc(SearchParams p) {
		return ConflictPricesException.builder()
				.brandId(p.getBrandId())
				.productId(p.getProductId())
				.date(p.getDate())
				.build();
	}
}
