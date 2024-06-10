package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

public class OrderDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Float orderPrice;
	private String createTime;
	private Integer cartId;
	private Integer addressId;
	private Integer bookQuantity;
	private Integer discount;
	private List<Integer> orderedItemsIds;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getBookQuantity() {
		return bookQuantity;
	}

	public void setBookQuantity(Integer bookQuantity) {
		this.bookQuantity = bookQuantity;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public List<Integer> getOrderedItemsIds() {
		return orderedItemsIds;
	}

	public void setOrderedItemsIds(List<Integer> orderedItemsIds) {
		this.orderedItemsIds = orderedItemsIds;
	}

}
