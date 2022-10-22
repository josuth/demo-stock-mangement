package com.onlinestore.stockmangement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onlinestore.stockmangement.entity.Price;

@Repository
public interface PricesRepository extends JpaRepository<Price, Long> {

	@Query("from Price p " +
           "where p.brandId=:brandId and p.productId=:productId " +
           "  and p.startDate <= :date and p.endDate >= :date")
	List<Price> findPvP(
			@Param("brandId") Integer brandId, 
			@Param("productId") Long productId, 
			@Param("date") LocalDateTime date);
	
}
