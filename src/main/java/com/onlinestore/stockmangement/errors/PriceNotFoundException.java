package com.onlinestore.stockmangement.errors;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class PriceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7136596525198395946L;

	Integer brandId;
	Long productId;
	LocalDateTime date;
	
}
