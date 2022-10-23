package com.onlinestore.stockmangement.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.onlinestore.stockmangement.model.PriceDTO;
import com.onlinestore.stockmangement.service.PricesService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Validated
public class PricesController {

	@Autowired
	PricesService service;
	
	@GetMapping("/prices/brandId/{brandId}/productId/{productId}/date/{date}")
	ResponseEntity<PriceDTO> getFinalPrice(
			@Valid @PathVariable("brandId")
			@Min(value = 1, message = "brandId must be greater than 1") 
			@Max(value = 999, message = "brandId must be less than 999") 
			Integer brandId,
	
			@Valid @PathVariable("productId")
			@Min(value = 1, message = "productId must be greater than 1") 
			@Max(value = 999999, message = "productId must be less than 999999")
			Long productId,
	
			@Valid @PathVariable("date") 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
			LocalDateTime date)	{
		
		log.info("solicitando precio artículo {}", productId);
		
		PriceDTO dto = service.findFinalPrice(brandId, productId, date);
		
		log.info("devolviendo precio artículo {}", productId);
		return ResponseEntity.ok(dto);
	}
	
}
