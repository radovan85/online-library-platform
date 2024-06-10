package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.PersistenceLoginEntity;

@Repository
public interface PersistenceLoginRepository extends JpaRepository<PersistenceLoginEntity, Integer> {

	@Query(value = "select * from persistence_logins where customer_id = :customerId order by create_time desc limit 1",nativeQuery = true)
	Optional<PersistenceLoginEntity> findLastLogin(@Param ("customerId") Integer customerId);

}
