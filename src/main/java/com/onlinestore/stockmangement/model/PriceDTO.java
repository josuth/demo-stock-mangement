package com.onlinestore.stockmangement.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDTO {
Integer brandId;
	Long productId;
	Integer appliedRate;
	LocalDateTime startDate;
	LocalDateTime endDate;
	Float price;
}
