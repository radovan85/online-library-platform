package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.CartItemDto;

public interface CartItemService {

	CartItemDto addCartItem(CartItemDto cartItem);

	void removeCartItem(Integer itemId);

	void eraseAllCartItems(Integer cartId);

	List<CartItemDto> listAllByCartId(Integer cartId);

	CartItemDto getItemById(Integer itemId);

	CartItemDto getCartItemByCartIdAndBookId(Integer cartId, Integer bookId);

	Integer getBookQuantity(Integer cartId);

	void eraseAllByBookId(Integer bookId);

	Boolean hasDiscount(Integer itemId);
	
	Integer calculateTotalQuantity(Integer cartId);
}
