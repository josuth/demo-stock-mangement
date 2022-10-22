package com.onlinestore.stockmangement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.onlinestore.stockmangement.model.PriceDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PricesController {

	@GetMapping("/prices/brandId/{brandId}/productId/{productId}/date/{date}")
	ResponseEntity<PriceDTO> getFinalPrice(@PathVariable Integer brandId, @PathVariable Long productId, @PathVariable String date)	{
		log.info("solicitando precio artículo {}", productId);
		
		log.info("devolviendo precio artículo {}", productId);
		return ResponseEntity.ok(PriceDTO.builder().build());
	}
}
