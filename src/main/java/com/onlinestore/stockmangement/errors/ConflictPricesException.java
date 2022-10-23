package com.onlinestore.stockmangement.errors;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class ConflictPricesException extends RuntimeException {

	private static final long serialVersionUID = 186349974223535975L;
	
	Integer brandId;
	Long productId;
	LocalDateTime date;

}
