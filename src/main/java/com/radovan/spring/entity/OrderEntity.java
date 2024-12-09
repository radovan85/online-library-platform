package com.radovan.spring.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "order_price",nullable = false)
	private Float orderPrice;

	@Column(name = "created_time", nullable = false)
	private Timestamp createTime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cart_id",nullable = false)
	private CartEntity cart;

	@OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
	@JoinColumn(name = "address_id",nullable = false)
	private OrderAddressEntity address;

	@Column(name = "book_quantity",nullable = false)
	private Integer bookQuantity;

	@Column(nullable = false)
	private Integer discount;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "order",orphanRemoval = true)
	private List<OrderItemEntity> orderedItems;

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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public CartEntity getCart() {
		return cart;
	}

	public void setCart(CartEntity cart) {
		this.cart = cart;
	}

	public OrderAddressEntity getAddress() {
		return address;
	}

	public void setAddress(OrderAddressEntity address) {
		this.address = address;
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

	public List<OrderItemEntity> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(List<OrderItemEntity> orderedItems) {
		this.orderedItems = orderedItems;
	}

	

}
