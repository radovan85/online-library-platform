package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.PersistenceLoginEntity;

@Repository
public interface PersistenceLoginRepository extends JpaRepository<PersistenceLoginEntity, Integer> {

	@Query(value = "SELECT * FROM persistence_logins WHERE customer_id = :customerId ORDER BY create_time DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	Optional<PersistenceLoginEntity> findLastLogin(@Param("customerId") Integer customerId);

}
