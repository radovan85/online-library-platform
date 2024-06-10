package com.radovan.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

	@Query(value = "select * from orders where cart_id = :cartId", nativeQuery = true)
	List<OrderEntity> findAllByCartId(@Param("cartId") Integer cartId);

	@Query(value="select sum(order_price) from orders where cart_id = :cartId",nativeQuery = true)
	Optional<Float> getOrdersValue(@Param("cartId") Integer cartId);

}
