package com.onlinestore.stockmangement.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRICES")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Price {

	@Id
	BigInteger priceId; 
	
	Integer brandId;
	
	Long productId;
	
	Integer priceList;
	
	Integer priority;
	
	LocalDateTime startDate;
	
	LocalDateTime endDate;
	
	Float price;
	
	String currency;
	
}
