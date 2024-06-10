package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.OrderDto;

public interface OrderService {

	OrderDto addOrder();

	List<OrderDto> listAll();

	OrderDto getOrderById(Integer orderId);

	void deleteOrder(Integer orderId);
	
	List<OrderDto> listAllByCustomerId(Integer customerId);

	Float calculateOrderTotal(Integer orderId);
	
	Float calculateOrdersValue(Integer cartId);
	
}
