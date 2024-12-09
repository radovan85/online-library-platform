package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.PersistenceLoginDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.BookGenreService;
import com.radovan.spring.service.BookImageService;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.DeliveryAddressService;
import com.radovan.spring.service.LoyaltyCardService;
import com.radovan.spring.service.OrderAddressService;
import com.radovan.spring.service.OrderItemService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.PersistenceLoginService;
import com.radovan.spring.service.ReviewService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookGenreService genreService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private LoyaltyCardService loyaltyCardService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private DeliveryAddressService addressService;

	@Autowired
	private PersistenceLoginService persistenceService;

	@Autowired
	private OrderAddressService orderAddressService;

	@Autowired
	private BookImageService imageService;

	@GetMapping(value = "/createBook")
	public String renderBookForm(ModelMap map) {
		map.put("book", new BookDto());
		map.put("allGenres", genreService.listAll());
		return "fragments/bookForm :: fragmentContent";
	}

	@PostMapping(value = "/createBook")
	public String createBook(@ModelAttribute("book") BookDto book) {

		bookService.addBook(book);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/deleteBook/{bookId}")
	public String deleteBook(@PathVariable("bookId") Integer bookId) {
		bookService.deleteBook(bookId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/updateBook/{bookId}")
	public String renderUpdateForm(@PathVariable("bookId") Integer bookId, ModelMap map) {
		map.put("book", new BookDto());
		map.put("currentBook", bookService.getBookById(bookId));
		map.put("allGenres", genreService.listAll());
		return "fragments/bookUpdateForm :: ajaxLoadedContent";
	}

	@GetMapping(value = "/createGenre")
	public String renderGenreForm(ModelMap map) {
		map.put("genre", new BookGenreDto());
		return "fragments/genreForm :: fragmentContent";
	}

	@PostMapping(value = "/createGenre")
	public String createGenre(@ModelAttribute("genre") BookGenreDto genre) {
		genreService.addGenre(genre);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allGenres")
	public String listAllGenres(ModelMap map) {
		map.put("allGenres", genreService.listAll());
		map.put("recordsPerPage", 6);
		return "fragments/genreList :: fragmentContent";
	}

	@GetMapping(value = "/deleteGenre/{genreId}")
	public String removeGenre(@PathVariable("genreId") Integer genreId) {
		genreService.deleteGenre(genreId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/updateGenre/{genreId}")
	public String updateGenre(@PathVariable("genreId") Integer genreId, ModelMap map) {
		map.put("genre", new BookGenreDto());
		map.put("currentGenre", genreService.getGenreById(genreId));
		return "fragments/genreUpdateForm :: fragmentContent";
	}

	@PostMapping(value = "/updateGenre/{genreId}")
	public String updateGenre(@ModelAttribute("genre") BookGenreDto genre, @PathVariable("genreId") Integer genreId) {
		genreService.updateGenre(genre, genreId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allReviews")
	public String reviewList(ModelMap map) {
		map.put("approvedReviews", reviewService.listAllAuthorized());
		map.put("allReviewRequests", reviewService.listAllOnHold());
		map.put("allBooks", bookService.listAll());
		map.put("allCustomers", customerService.listAll());
		map.put("allUsers", userService.listAllUsers());
		map.put("recordsPerPage", 6);
		return "fragments/reviewList :: fragmentContent";
	}

	@GetMapping(value = "/allRequestedReviews")
	public String requestedReviewsList(ModelMap map) {
		List<ReviewDto> pendingReviews = reviewService.listAllOnHold();
		List<BookDto> allBooks = bookService.listAll();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("pendingReviews", pendingReviews);
		map.put("allBooks", allBooks);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/reviewRequestsList :: fragmentContent";
	}

	@GetMapping(value = "/pendingReviewDetails/{reviewId}")
	public String getPendingReview(@PathVariable("reviewId") Integer reviewId, ModelMap map) {
		ReviewDto currentReview = reviewService.getReviewById(reviewId);
		CustomerDto tempCustomer = customerService.getCustomerById(currentReview.getAuthorId());
		UserDto tempUser = userService.getUserById(tempCustomer.getUserId());
		BookDto currentBook = bookService.getBookById(currentReview.getBookId());
		map.put("currentReview", currentReview);
		map.put("tempUser", tempUser);
		map.put("currentBook", currentBook);
		return "fragments/pendingReviewDetails :: fragmentContent";
	}

	@GetMapping(value = "/reviewDetails/{reviewId}")
	public String getReviewDetails(@PathVariable("reviewId") Integer reviewId, ModelMap map) {
		ReviewDto currentReview = reviewService.getReviewById(reviewId);
		CustomerDto tempCustomer = customerService.getCustomerById(currentReview.getAuthorId());
		UserDto tempUser = userService.getUserById(tempCustomer.getUserId());
		BookDto currentBook = bookService.getBookById(currentReview.getBookId());
		map.put("currentReview", currentReview);
		map.put("tempUser", tempUser);
		map.put("currentBook", currentBook);
		return "fragments/reviewDetails :: fragmentContent";
	}

	@PostMapping(value = "/authorizeReview/{reviewId}")
	public String reviewAuthorization(@PathVariable("reviewId") Integer reviewId) {
		reviewService.authorizeReview(reviewId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/rejectReview/{reviewId}")
	public String rejectReview(@PathVariable("reviewId") Integer reviewId) {
		reviewService.deleteReview(reviewId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/checkCardRequests")
	public String allCardRequests(ModelMap map) {
		List<LoyaltyCardRequestDto> allRequests = loyaltyCardService.listAllCardRequests();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allRequests", allRequests);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/cardRequestList :: fragmentContent";
	}

	@GetMapping(value = "/getAllCards")
	public String getAllCards(ModelMap map) {
		List<LoyaltyCardDto> allCards = loyaltyCardService.listAllLoyaltyCards();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		List<LoyaltyCardRequestDto> allCardRequests = loyaltyCardService.listAllCardRequests();
		map.put("allCards", allCards);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("allCardRequests", allCardRequests);
		map.put("recordsPerPage", 6);
		return "fragments/loyaltyCardList :: fragmentContent";
	}

	@GetMapping(value = "/authorizeCard/{cardRequestId}")
	public String authorizeLoyaltyCard(@PathVariable("cardRequestId") Integer cardRequestId, ModelMap map) {
		loyaltyCardService.authorizeRequest(cardRequestId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/rejectCard/{cardRequestId}")
	public String rejectLoyaltyCard(@PathVariable("cardRequestId") Integer cardRequestId, ModelMap map) {
		loyaltyCardService.rejectRequest(cardRequestId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allOrders")
	public String listAllOrders(ModelMap map) {

		List<OrderDto> allOrders = orderService.listAll();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/orderList :: fragmentContent";
	}

	@GetMapping(value = "/allOrders/{customerId}")
	public String allOrdersByCustomerId(@PathVariable("customerId") Integer customerId, ModelMap map) {
		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		CustomerDto customer = customerService.getCustomerById(customerId);
		UserDto user = userService.getUserById(customer.getUserId());
		map.put("allOrders", allOrders);
		map.put("tempUser", user);
		map.put("recordsPerPage", 6);
		return "fragments/customerOrderList :: fragmentContent";
	}

	@GetMapping(value = "/deleteOrder/{orderId}")
	public String deleteOrder(@PathVariable("orderId") Integer orderId) {
		orderService.deleteOrder(orderId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/getOrder/{orderId}")
	public String orderDetails(@PathVariable("orderId") Integer orderId, ModelMap map) {

		OrderDto order = orderService.getOrderById(orderId);
		OrderAddressDto address = orderAddressService.getAddressById(order.getAddressId());
		Float orderPrice = orderService.calculateOrderTotal(orderId);
		List<OrderItemDto> orderedItems = orderItemService.listAllByOrderId(orderId);
		map.put("order", order);
		map.put("address", address);
		map.put("orderPrice", orderPrice);
		map.put("orderedItems", orderedItems);
		return "fragments/orderDetails :: fragmentContent";
	}

	@GetMapping(value = "/allCustomers")
	public String customerList(ModelMap map) {

		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/customerList :: fragmentContent";

	}

	@GetMapping(value = "/deleteCustomer/{customerId}")
	public String removeCustomer(@PathVariable("customerId") Integer customerId) {
		customerService.deleteCustomer(customerId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/getCustomer/{customerId}")
	public String getCustomer(@PathVariable("customerId") Integer customerId, ModelMap map) {

		CustomerDto customer = customerService.getCustomerById(customerId);
		UserDto tempUser = userService.getUserById(customer.getUserId());
		DeliveryAddressDto address = addressService.getAddressById(customer.getDeliveryAddressId());
		PersistenceLoginDto persistence = persistenceService.getLastLogin(customerId);
		Float ordersValue = orderService.calculateOrdersValue(customer.getCartId());
		map.put("tempCustomer", customer);
		map.put("tempUser", tempUser);
		map.put("address", address);
		map.put("persistence", persistence);
		map.put("ordersValue", ordersValue);
		return "fragments/customerDetails :: fragmentContent";
	}

	@GetMapping(value = "/suspendUser/{userId}")
	public String suspendUser(@PathVariable("userId") Integer userId) {
		userService.suspendUser(userId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/reactivateUser/{userId}")
	public String reactivateUser(@PathVariable("userId") Integer userId) {
		userService.clearSuspension(userId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/invalidPath")
	public String invalidImagePath() {
		return "fragments/invalidImagePath :: fragmentContent";
	}

	@GetMapping(value = "/genreError")
	public String fireGenreExc() {
		return "fragments/genreError :: fragmentContent";
	}

	@GetMapping(value = "/addImage/{bookId}")
	public String renderImageForm(@PathVariable("bookId") Integer bookId, ModelMap map) {
		map.put("bookId", bookId);
		return "fragments/imageForm :: fragmentContent";
	}

	@PostMapping(value = "/storeImage/{bookId}")
	public String storeImage(@RequestPart("file") MultipartFile file, @PathVariable("bookId") Integer bookId) {

		imageService.addImage(file, bookId);
		return "fragments/homePage :: fragmentContent";
	}
}
