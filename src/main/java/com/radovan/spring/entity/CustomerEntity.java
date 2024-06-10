package com.radovan.spring.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class CustomerEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "date_of_birth",nullable = false)
	private LocalDate dateOfBirth;

	@Column(name = "registration_time",nullable = false)
	private Timestamp registrationTime;

	@OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
	@JoinColumn(name = "user_id",nullable = false)
	private UserEntity user;

	@OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
	@JoinColumn(name = "cart_id",nullable = false)
	private CartEntity cart;

	@OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
	@JoinColumn(name = "wishlist_id",nullable = false)
	private WishListEntity wishList;

	@OneToOne(fetch = FetchType.EAGER,orphanRemoval = true,mappedBy = "customer")
	private LoyaltyCardEntity loyaltyCard;

	@OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
	@JoinColumn(name = "delivery_address_id")
	private DeliveryAddressEntity deliveryAddress;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "author", orphanRemoval = true)
	private List<ReviewEntity> reviews;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "customer",orphanRemoval = true)
	private List<PersistenceLoginEntity> persistenceLogins;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Timestamp getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Timestamp registrationTime) {
		this.registrationTime = registrationTime;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public CartEntity getCart() {
		return cart;
	}

	public void setCart(CartEntity cart) {
		this.cart = cart;
	}

	public WishListEntity getWishList() {
		return wishList;
	}

	public void setWishList(WishListEntity wishList) {
		this.wishList = wishList;
	}

	public LoyaltyCardEntity getLoyaltyCard() {
		return loyaltyCard;
	}

	public void setLoyaltyCard(LoyaltyCardEntity loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}

	public DeliveryAddressEntity getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddressEntity deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public List<ReviewEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewEntity> reviews) {
		this.reviews = reviews;
	}

	public List<PersistenceLoginEntity> getPersistenceLogins() {
		return persistenceLogins;
	}

	public void setPersistenceLogins(List<PersistenceLoginEntity> persistenceLogins) {
		this.persistenceLogins = persistenceLogins;
	}

	

}
