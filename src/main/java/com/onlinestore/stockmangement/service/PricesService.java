package com.onlinestore.stockmangement.service;

import java.time.LocalDateTime;

import com.onlinestore.stockmangement.model.PriceDTO;

public interface PricesService {

	PriceDTO findFinalPrice(Integer brandId, Long productId, LocalDateTime date);

}
