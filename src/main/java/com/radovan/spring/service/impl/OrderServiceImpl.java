package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.DeliveryAddressService;
import com.radovan.spring.service.LoyaltyCardService;
import com.radovan.spring.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Autowired
	private DeliveryAddressService deliveryAddressService;

	@Autowired
	private LoyaltyCardService cardService;

	@Override
	@Transactional
	public OrderDto addOrder() {
		// TODO Auto-generated method stub
		OrderDto order = new OrderDto();
		order.setDiscount(0);
		CustomerDto customer = customerService.getCurrentCustomer();
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		CartDto cart = cartService.getCartById(customer.getCartId());
		cartService.validateCart(cart.getId());
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cart.getId());
		DeliveryAddressDto deliveryAddress = deliveryAddressService.getAddressById(customer.getDeliveryAddressId());
		OrderAddressDto orderAddress = tempConverter.addressToOrderAddress(deliveryAddress);
		OrderAddressEntity storedAddress = orderAddressRepository
				.save(tempConverter.orderAddressDtoToEntity(orderAddress));
		Integer bookQuantity = cartItemService.calculateTotalQuantity(cart.getId());
		Optional<Integer> cardIdOptional = Optional.ofNullable(customer.getLoyaltyCardId());
		if (cardIdOptional.isPresent()) {
			LoyaltyCardDto card = cardService.getCardById(cardIdOptional.get());
			order.setDiscount(card.getDiscount());
		}
		order.setAddressId(storedAddress.getId());
		order.setCartId(cart.getId());
		order.setOrderPrice(cart.getCartPrice());
		order.setBookQuantity(bookQuantity);
		OrderEntity orderEntity = tempConverter.orderDtoToEntity(order);
		orderEntity.setCreateTime(tempConverter.getCurrentUTCTimestamp());
		OrderEntity storedOrder = orderRepository.save(orderEntity);

		for (CartItemDto cartItem : allCartItems) {
			OrderItemDto orderItem = tempConverter.cartItemToOrderItem(cartItem);
			orderItem.setOrderId(storedOrder.getId());
			OrderItemEntity storedItem = orderItemRepository.save(tempConverter.orderItemDtoToEntity(orderItem));
			orderedItems.add(storedItem);
		}

		storedOrder.getOrderedItems().clear();
		storedOrder.getOrderedItems().addAll(orderedItems);
		storedOrder = orderRepository.saveAndFlush(storedOrder);

		cartItemService.eraseAllCartItems(cart.getId());
		cartService.refreshCartState(cart.getId());

		return tempConverter.orderEntityToDto(storedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderEntity> allOrders = orderRepository.findAll();
		return allOrders.stream().map(tempConverter::orderEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Float calculateOrderTotal(Integer orderId) {
		// TODO Auto-generated method stub
		return orderItemRepository.calculateGrandTotal(orderId).orElse(0f);

	}

	@Override
	@Transactional(readOnly = true)
	public OrderDto getOrderById(Integer orderId) {
		// TODO Auto-generated method stub
		OrderEntity orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The order has not been found!")));
		return tempConverter.orderEntityToDto(orderEntity);

	}

	@Override
	@Transactional
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		getOrderById(orderId);
		orderRepository.deleteById(orderId);
		orderRepository.flush();
	}

	

	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> listAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		CustomerDto customer = customerService.getCustomerById(customerId);
		List<OrderEntity> allOrders = orderRepository.findAllByCartId(customer.getCartId());
		return allOrders.stream().map(tempConverter::orderEntityToDto).collect(Collectors.toList());

	}
	
	@Override
	@Transactional(readOnly = true)
	public Float calculateOrdersValue(Integer cartId) {
		// TODO Auto-generated method stub
		
		return orderRepository.getOrdersValue(cartId).orElse(0f);
		
	}

}
