package com.onlinestore.stockmangement.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchParams {
	Integer brandId; 
	Long productId;
	LocalDateTime date;
	
	@Override
	public String toString()	{
		return String.format("brandId=%d, productId=%d, date=%s", 
				this.brandId, this.productId, this.date);
	}
}
