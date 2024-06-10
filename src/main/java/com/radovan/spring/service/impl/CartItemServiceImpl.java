package com.radovan.spring.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.exceptions.BookQuantityException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.LoyaltyCardService;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private LoyaltyCardService cardService;

	@Autowired
	private BookService bookService;

	@Override
	@Transactional
	public CartItemDto addCartItem(CartItemDto cartItem) {
		// TODO Auto-generated method stub
		CartItemDto returnValue = null;
		Integer bookId = cartItem.getBookId();
		CustomerDto customer = customerService.getCurrentCustomer();
		CartDto cart = cartService.getCartById(customer.getCartId());
		Optional<Integer> loyaltyCardIdOption = Optional.ofNullable(customer.getLoyaltyCardId());
		BookDto book = bookService.getBookById(bookId);
		List<CartItemDto> allCartItems = listAllByCartId(cart.getId());
		Integer tempQuantity = 0;
		for (CartItemDto tempCartItem : allCartItems) {
			tempQuantity = tempQuantity + tempCartItem.getQuantity();
		}

		if (tempQuantity + cartItem.getQuantity() > 50) {
			Error error = new Error("Maximum 50 books allowed");
			throw new BookQuantityException(error);
		}

		if (loyaltyCardIdOption.isPresent()) {
			LoyaltyCardDto loyaltyCard = cardService.getCardById(loyaltyCardIdOption.get());
			Integer discount = loyaltyCard.getDiscount();

			Optional<CartItemEntity> existingCartItemOpt = cartItemRepository.findByCartIdAndBookId(cart.getId(),
					bookId);
			if (existingCartItemOpt.isPresent()) {
				cartItem.setId(existingCartItemOpt.get().getId());
				cartItem.setCartId(cart.getId());
				cartItem.setQuantity(existingCartItemOpt.get().getQuantity() + cartItem.getQuantity());
				if (cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				if (discount == 0) {
					cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				} else {
					Float cartPrice = (book.getPrice() * cartItem.getQuantity());
					cartPrice = cartPrice - ((cartPrice * discount) / 100);
					cartItem.setPrice(cartPrice);
				}
				CartItemEntity storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(cartItem));
				returnValue = tempConverter.cartItemEntityToDto(storedItem);
				cartService.refreshCartState(cart.getId());
			} else {
				cartItem.setQuantity(cartItem.getQuantity());
				if (cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				if (discount == 0) {
					cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				} else {
					Float cartPrice = (book.getPrice() * cartItem.getQuantity());
					cartPrice = cartPrice - ((cartPrice * discount) / 100);
					cartItem.setPrice(cartPrice);
				}
				cartItem.setCartId(cart.getId());
				CartItemEntity storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(cartItem));
				returnValue = tempConverter.cartItemEntityToDto(storedItem);
				cartService.refreshCartState(cart.getId());
			}

		} else {

			Optional<CartItemEntity> existingCartItemOpt = cartItemRepository.findByCartIdAndBookId(cart.getId(),
					bookId);
			if (existingCartItemOpt.isPresent()) {
				cartItem.setId(existingCartItemOpt.get().getId());
				cartItem.setCartId(cart.getId());
				cartItem.setQuantity(existingCartItemOpt.get().getQuantity() + cartItem.getQuantity());
				if (cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				CartItemEntity storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(cartItem));
				returnValue = tempConverter.cartItemEntityToDto(storedItem);
				cartService.refreshCartState(cart.getId());
			} else {
				cartItem.setQuantity(cartItem.getQuantity());
				if (cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				cartItem.setCartId(cart.getId());
				cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				CartItemEntity storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(cartItem));
				returnValue = tempConverter.cartItemEntityToDto(storedItem);
				cartService.refreshCartState(cart.getId());
			}

		}
		return returnValue;

	}

	@Override
	@Transactional
	public void removeCartItem(Integer itemId) {
		// TODO Auto-generated method stub
		CartItemDto item = getItemById(itemId);
		cartItemRepository.removeCartItem(itemId);
		cartItemRepository.flush();
		cartService.refreshCartState(item.getCartId());

	}

	@Override
	@Transactional
	public void eraseAllCartItems(Integer cartId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByCartId(cartId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CartItemDto> listAllByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		List<CartItemEntity> cartItems = cartItemRepository.findAllByCartId(cartId);
		return cartItems.stream().map(tempConverter::cartItemEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CartItemDto getCartItemByCartIdAndBookId(Integer cartId, Integer bookId) {
		// TODO Auto-generated method stub
		CartItemEntity itemEntity = cartItemRepository.findByCartIdAndBookId(cartId, bookId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The item has not been found!")));

		return tempConverter.cartItemEntityToDto(itemEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getBookQuantity(Integer cartId) {
		// TODO Auto-generated method stub
		return cartItemRepository.findBookQuantity(cartId).orElse(0);

	}

	@Override
	@Transactional
	public void eraseAllByBookId(Integer bookId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByBookId(bookId);
		cartItemRepository.flush();
		cartService.refreshAllCarts();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean hasDiscount(Integer itemId) {
		// TODO Auto-generated method stub
		Boolean returnValue = false;
		CartItemDto cartItem = getItemById(itemId);
		CartDto cart = cartService.getCartById(cartItem.getCartId());
		CustomerDto customer = customerService.getCustomerById(cart.getCustomerId());
		Optional<Integer> cardIdOptional = Optional.ofNullable(customer.getLoyaltyCardId());

		if (cardIdOptional.isPresent()) {
			Integer cardId = cardIdOptional.get();
			LoyaltyCardDto card = cardService.getCardById(cardId);
			Integer discount = card.getDiscount();
			if (discount > 0) {
				returnValue = true;
			}
		}

		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public CartItemDto getItemById(Integer itemId) {
		// TODO Auto-generated method stub
		CartItemEntity itemEntity = cartItemRepository.findById(itemId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The item has not been found!")));

		return tempConverter.cartItemEntityToDto(itemEntity);
	}

	@Override
	public Integer calculateTotalQuantity(Integer cartId) {
		// TODO Auto-generated method stub
		List<CartItemDto> allItems = listAllByCartId(cartId);
		return allItems.stream().mapToInt(CartItemDto::getQuantity).sum();
	}

}
