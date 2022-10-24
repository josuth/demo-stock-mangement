package com.onlinestore.stockmangement.service;

import com.onlinestore.stockmangement.model.PriceDTO;
import com.onlinestore.stockmangement.model.SearchParams;

public interface PricesService {

	PriceDTO findFinalPrice(SearchParams params);

}
