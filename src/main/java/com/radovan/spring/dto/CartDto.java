package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

public class CartDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer customerId;
	private List<Integer> cartItemsIds;
	private Float cartPrice;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public List<Integer> getCartItemsIds() {
		return cartItemsIds;
	}

	public void setCartItemsIds(List<Integer> cartItemsIds) {
		this.cartItemsIds = cartItemsIds;
	}

	public Float getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(Float cartPrice) {
		this.cartPrice = cartPrice;
	}

}
