package com.radovan.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.LoyaltyCardService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private BookService bookService;

	@Autowired
	private LoyaltyCardService loyaltyCardService;

	@PostMapping(value = "/addToCart")
	public String addCartItem(@ModelAttribute("cartItem") CartItemDto cartItem) {
		cartItemService.addCartItem(cartItem);
		return "fragments/homePage :: fragmentContent";

	}

	@GetMapping(value = "/addItemCompleted")
	public String addItemCompleted() {
		return "fragments/itemAdded :: fragmentContent";
	}

	@GetMapping(value = "/getCart")
	public String cartDetails(ModelMap map) {
		CustomerDto customer = customerService.getCurrentCustomer();
		CartDto cart = cartService.getCartById(customer.getCartId());
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(customer.getCartId());
		List<BookDto> allBooks = bookService.listAll();
		Optional<Integer> loyaltyCardIdOpt = Optional.ofNullable(customer.getLoyaltyCardId());
		if (loyaltyCardIdOpt.isPresent()) {
			LoyaltyCardDto loyaltyCard = loyaltyCardService.getCardById(loyaltyCardIdOpt.get());
			map.put("discount", loyaltyCard.getDiscount());
		} else {
			map.put("discount", 0);
		}
		map.put("allCartItems", allCartItems);
		map.put("allBooks", allBooks);
		map.put("cart", cart);
		return "fragments/cart :: fragmentContent";

	}

	@GetMapping(value = "/deleteItem/{itemId}")
	public String deleteCartItem(@PathVariable("itemId") Integer itemId) {
		cartItemService.removeCartItem(itemId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/deleteAllItems/{cartId}")
	public String deleteAllCartItems(@PathVariable("cartId") Integer cartId) {
		cartItemService.eraseAllCartItems(cartId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/invalidCart")
	public String invalidCart() {
		return "fragments/invalidCart :: fragmentContent";
	}

}
