package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

public class CustomerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String dateOfBirth;
	private String registrationTime;
	private Integer userId;
	private Integer cartId;
	private Integer wishListId;
	private Integer loyaltyCardId;
	private Integer deliveryAddressId;
	private List<Integer> reviewsIds;
	private List<Integer> persistenceLoginsIds;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(String registrationTime) {
		this.registrationTime = registrationTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	

	public Integer getWishListId() {
		return wishListId;
	}

	public void setWishListId(Integer wishListId) {
		this.wishListId = wishListId;
	}

	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}

	public Integer getDeliveryAddressId() {
		return deliveryAddressId;
	}

	public void setDeliveryAddressId(Integer deliveryAddressId) {
		this.deliveryAddressId = deliveryAddressId;
	}

	public List<Integer> getReviewsIds() {
		return reviewsIds;
	}

	public void setReviewsIds(List<Integer> reviewsIds) {
		this.reviewsIds = reviewsIds;
	}

	public List<Integer> getPersistenceLoginsIds() {
		return persistenceLoginsIds;
	}

	public void setPersistenceLoginsIds(List<Integer> persistenceLoginsIds) {
		this.persistenceLoginsIds = persistenceLoginsIds;
	}

}
