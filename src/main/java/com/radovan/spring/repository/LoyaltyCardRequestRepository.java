package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.LoyaltyCardRequestEntity;

@Repository
public interface LoyaltyCardRequestRepository extends JpaRepository<LoyaltyCardRequestEntity, Integer> {

	@Query(value = "select * from card_requests where customer_id = :customerId",nativeQuery = true)
	Optional<LoyaltyCardRequestEntity> findByCustomerId(@Param ("customerId") Integer customerId);
}
