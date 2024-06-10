package com.radovan.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.service.BookGenreService;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.ReviewService;
import com.radovan.spring.service.UserService;
import com.radovan.spring.utils.ThymeleafUtils;

@Controller
@RequestMapping(value = "/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookGenreService genreService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ThymeleafUtils thymeleafUtils;

	@GetMapping(value = "/allBooks")
	public String allBooksList(ModelMap map) {
		map.put("allBooks", bookService.listAll());
		map.put("recordsPerPage", 5);
		map.put("utils", thymeleafUtils);
		return "fragments/bookList :: fragmentContent";
	}

	@GetMapping(value = "/allBooksById")
	public String allBooksListSortedById(ModelMap map) {
		map.put("allBooks", bookService.listAllByBookId());
		map.put("recordsPerPage", 5);
		map.put("utils", thymeleafUtils);
		return "fragments/bookList :: fragmentContent";
	}

	@GetMapping(value = "/allBooksByRating")
	public String allBooksListSortedByRating(ModelMap map) {
		map.put("allBooks", bookService.listAllByRating());
		map.put("recordsPerPage", 5);
		map.put("utils", thymeleafUtils);
		return "fragments/bookList :: fragmentContent";
	}

	@GetMapping(value = "/allBooksByPrice")
	public String allBooksListSortedByPrice(ModelMap map) {
		map.put("allBooks", bookService.listAllByPrice());
		map.put("recordsPerPage", 5);
		map.put("utils", thymeleafUtils);
		return "fragments/bookList :: fragmentContent";
	}

	@GetMapping(value = "/bookDetails/{bookId}")
	public String getBookDetails(@PathVariable("bookId") Integer bookId, ModelMap map) {
		map.put("currentBook", bookService.getBookById(bookId));
		map.put("allGenres", genreService.listAll());
		map.put("allReviews", reviewService.listAllByBookId(bookId));
		map.put("allUsers", userService.listAllUsers());
		map.put("allCustomers", customerService.listAll());
		map.put("recordsPerPage", 5);
		map.put("utils", thymeleafUtils);
		return "fragments/bookDetails :: fragmentContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@PostMapping(value = "/addToWishList/{bookId}")
	public String toWishlist(@PathVariable("bookId") Integer bookId) {
		bookService.addToWishList(bookId);
		return "fragments/homePage :: fragmentContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/getWishList")
	public String getWishListBooks(ModelMap map) {
		map.put("allBooks", bookService.listAllFromWishList());
		map.put("recordsPerPage", 5);
		map.put("utils", thymeleafUtils);
		return "fragments/wishlist :: fragmentContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/deleteFromWishList/{bookId}")
	public String deleteFromWishList(@PathVariable("bookId") Integer bookId) {
		bookService.removeFromWishList(bookId);
		return "fragments/homePage :: fragmentContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/addToCart/{bookId}")
	public String addBookToCart(@PathVariable("bookId") Integer bookId, ModelMap map) {
		map.put("selectedBook", bookService.getBookById(bookId));
		map.put("cartItem", new CartItemDto());
		map.put("utils", thymeleafUtils);
		return "fragments/cartItemForm :: fragmentContent";
	}

	@GetMapping(value = "/searchBooks/{keyword}")
	public String searchBooks(@PathVariable("keyword") String keyword, ModelMap map) {
		map.put("allBooks", bookService.search(keyword));
		map.put("recordsPerPage", 5);
		map.put("utils", thymeleafUtils);
		return "fragments/searchList :: fragmentContent";
	}
}
