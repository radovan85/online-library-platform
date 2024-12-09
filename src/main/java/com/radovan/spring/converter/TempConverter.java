package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.dto.BookImageDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.PersistenceLoginDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.dto.WishListDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.BookGenreEntity;
import com.radovan.spring.entity.BookImageEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.entity.LoyaltyCardRequestEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.PersistenceLoginEntity;
import com.radovan.spring.entity.ReviewEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.repository.BookGenreRepository;
import com.radovan.spring.repository.BookImageRepository;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.DeliveryAddressRepository;
import com.radovan.spring.repository.LoyaltyCardRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.PersistenceLoginRepository;
import com.radovan.spring.repository.ReviewRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.repository.WishListRepository;

@Component
public class TempConverter {

	@Autowired
	private BookGenreRepository genreRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private LoyaltyCardRepository loyaltyCardRepository;

	@Autowired
	private DeliveryAddressRepository addressRepository;

	@Autowired
	private PersistenceLoginRepository persistenceRepository;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Autowired
	private BookImageRepository bookImageRepository;

	@Autowired
	private ModelMapper mapper;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private ZoneId zoneId = ZoneId.of("UTC");

	public BookDto bookEntityToDto(BookEntity bookEntity) {
		BookDto returnValue = mapper.map(bookEntity, BookDto.class);
		Optional<BookGenreEntity> genreOptional = Optional.ofNullable(bookEntity.getGenre());
		if (genreOptional.isPresent()) {
			returnValue.setGenreId(genreOptional.get().getId());
		}

		Optional<List<ReviewEntity>> reviewsOptional = Optional.ofNullable(bookEntity.getReviews());
		List<Integer> reviewsIds = new ArrayList<Integer>();
		if (!reviewsOptional.isEmpty()) {
			reviewsOptional.get().forEach((review) -> {
				reviewsIds.add(review.getId());
			});
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		returnValue.setReviewsIds(reviewsIds);

		List<Integer> wishListsIds = new ArrayList<>();
		Optional<List<WishListEntity>> wishListsOptional = Optional.ofNullable(bookEntity.getWishLists());
		if (!wishListsOptional.isEmpty()) {
			wishListsOptional.get().forEach((wishList) -> {
				wishListsIds.add(wishList.getId());
			});
		}

		returnValue.setWishListsIds(wishListsIds);

		Optional<BookImageEntity> imageOptional = Optional.ofNullable(bookEntity.getImage());
		if (imageOptional.isPresent()) {
			returnValue.setImageId(imageOptional.get().getId());
		}

		return returnValue;
	}

	public BookEntity bookDtoToEntity(BookDto book) {
		BookEntity returnValue = mapper.map(book, BookEntity.class);
		Optional<Integer> genreIdOptional = Optional.ofNullable(book.getGenreId());
		if (genreIdOptional.isPresent()) {
			Integer genreId = genreIdOptional.get();
			BookGenreEntity genre = genreRepository.findById(genreId).orElse(null);
			if (genre != null) {
				returnValue.setGenre(genre);
			}
		}

		Optional<List<Integer>> reviewsIdsOptional = Optional.ofNullable(book.getReviewsIds());
		List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();
		if (!reviewsIdsOptional.isEmpty()) {
			reviewsIdsOptional.get().forEach((reviewId) -> {
				ReviewEntity review = reviewRepository.findById(reviewId).orElse(null);
				if (review != null) {
					reviews.add(review);
				}
			});
		}

		returnValue.setReviews(reviews);
		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);

		Optional<List<Integer>> wishListsIdsOptional = Optional.ofNullable(book.getWishListsIds());
		List<WishListEntity> wishLists = new ArrayList<>();
		if (!wishListsIdsOptional.isEmpty()) {
			wishListsIdsOptional.get().forEach((wishListsId) -> {
				WishListEntity wishListEntity = wishListRepository.findById(wishListsId).orElse(null);
				if (wishListEntity != null) {
					wishLists.add(wishListEntity);
				}
			});
		}

		returnValue.setWishLists(wishLists);

		Optional<Integer> imageIdOptional = Optional.ofNullable(book.getImageId());
		if (imageIdOptional.isPresent()) {
			Integer imageId = imageIdOptional.get();
			BookImageEntity imageEntity = bookImageRepository.findById(imageId).orElse(null);
			if (imageEntity != null) {
				returnValue.setImage(imageEntity);
			}
		}
		return returnValue;
	}

	public BookGenreDto bookGenreEntityToDto(BookGenreEntity genreEntity) {
		BookGenreDto returnValue = mapper.map(genreEntity, BookGenreDto.class);
		Optional<List<BookEntity>> booksOptional = Optional.ofNullable(genreEntity.getBooks());
		List<Integer> booksIds = new ArrayList<Integer>();
		if (!booksOptional.isEmpty()) {
			booksOptional.get().forEach((book) -> {
				booksIds.add(book.getId());
			});
		}

		returnValue.setBooksIds(booksIds);
		return returnValue;

	}

	public BookGenreEntity bookGenreDtoToEntity(BookGenreDto genre) {
		BookGenreEntity returnValue = mapper.map(genre, BookGenreEntity.class);
		Optional<List<Integer>> booksIdsOptional = Optional.ofNullable(genre.getBooksIds());
		List<BookEntity> books = new ArrayList<BookEntity>();
		if (!booksIdsOptional.isEmpty()) {
			booksIdsOptional.get().forEach((bookId) -> {
				BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
				if (bookEntity != null) {
					books.add(bookEntity);
				}

			});
		}

		returnValue.setBooks(books);
		return returnValue;
	}
	
	public BookImageDto imageEntityToDto(BookImageEntity image) {
		BookImageDto returnValue = mapper.map(image, BookImageDto.class);
		Optional<BookEntity> bookOptional = Optional.ofNullable(image.getBook());
		if(bookOptional.isPresent()) {
			returnValue.setBookId(bookOptional.get().getId());
		}
		
		return returnValue;
	}
	
	public BookImageEntity imageDtoToEntity(BookImageDto image) {
		BookImageEntity returnValue = mapper.map(image, BookImageEntity.class);
		Optional<Integer> bookIdOptional = Optional.ofNullable(image.getBookId());
		if(bookIdOptional.isPresent()) {
			Integer bookId = bookIdOptional.get();
			BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
			if(bookEntity!=null) {
				returnValue.setBook(bookEntity);
			}
		}
		
		return returnValue;
	}

	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);

		Optional<CustomerEntity> customerOptional = Optional.ofNullable(cartEntity.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getId());
		}

		List<Integer> itemsIds = new ArrayList<>();
		Optional<List<CartItemEntity>> cartItemsOptional = Optional.ofNullable(cartEntity.getCartItems());
		if (!cartItemsOptional.isEmpty()) {
			cartItemsOptional.get().forEach((itemEntity) -> {
				Integer itemId = itemEntity.getId();
				itemsIds.add(itemId);
			});
		}

		returnValue.setCartItemsIds(itemsIds);
		Float price = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(price);
		return returnValue;

	}

	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);

		Optional<Integer> customerIdOptional = Optional.ofNullable(cartDto.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).orElse(null);
			if (customerEntity != null) {
				returnValue.setCustomer(customerEntity);
			}

		}

		List<CartItemEntity> cartItems = new ArrayList<>();
		Optional<List<Integer>> itemsIdsOptional = Optional.ofNullable(cartDto.getCartItemsIds());

		if (!itemsIdsOptional.isEmpty()) {
			itemsIdsOptional.get().forEach((itemId) -> {
				CartItemEntity itemEntity = cartItemRepository.findById(itemId).orElse(null);
				if (itemEntity != null) {
					cartItems.add(itemEntity);
				}

			});
		}

		returnValue.setCartItems(cartItems);
		Float price = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(price);
		return returnValue;
	}

	public CartItemDto cartItemEntityToDto(CartItemEntity cartItemEntity) {
		CartItemDto returnValue = mapper.map(cartItemEntity, CartItemDto.class);
		Optional<BookEntity> bookOpt = Optional.ofNullable(cartItemEntity.getBook());
		if (bookOpt.isPresent()) {
			returnValue.setBookId(bookOpt.get().getId());
		}

		Optional<CartEntity> cartOpt = Optional.ofNullable(cartItemEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getId());
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);

		return returnValue;
	}

	public CartItemEntity cartItemDtoToEntity(CartItemDto cartItemDto) {
		CartItemEntity returnValue = mapper.map(cartItemDto, CartItemEntity.class);
		Optional<Integer> cartIdOptional = Optional.ofNullable(cartItemDto.getCartId());
		if (cartIdOptional.isPresent()) {
			Integer cartId = cartIdOptional.get();
			CartEntity cartEntity = cartRepository.findById(cartId).orElse(null);
			if (cartEntity != null) {
				returnValue.setCart(cartEntity);
			}

		}

		Optional<Integer> bookIdOptional = Optional.ofNullable(cartItemDto.getBookId());
		if (bookIdOptional.isPresent()) {
			Integer bookId = bookIdOptional.get();
			BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
			if (bookEntity != null) {
				returnValue.setBook(bookEntity);
			}
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);
		Optional<UserEntity> userOptional = Optional.ofNullable(customerEntity.getUser());
		if (userOptional.isPresent()) {
			returnValue.setUserId(userOptional.get().getId());
		}

		Optional<CartEntity> cartOptional = Optional.ofNullable(customerEntity.getCart());
		if (cartOptional.isPresent()) {
			returnValue.setCartId(cartOptional.get().getId());
		}

		Optional<WishListEntity> wishListOptional = Optional.ofNullable(customerEntity.getWishList());
		if (wishListOptional.isPresent()) {
			returnValue.setWishListId(wishListOptional.get().getId());
		}

		Optional<LoyaltyCardEntity> loyaltyCardOptional = Optional.ofNullable(customerEntity.getLoyaltyCard());
		if (loyaltyCardOptional.isPresent()) {
			returnValue.setLoyaltyCardId(loyaltyCardOptional.get().getId());
		}

		Optional<DeliveryAddressEntity> addressOptional = Optional.ofNullable(customerEntity.getDeliveryAddress());
		if (addressOptional.isPresent()) {
			returnValue.setDeliveryAddressId(addressOptional.get().getId());
		}

		Optional<List<ReviewEntity>> reviewsOptional = Optional.ofNullable(customerEntity.getReviews());
		List<Integer> reviewsIds = new ArrayList<Integer>();
		if (reviewsOptional.isPresent()) {
			reviewsOptional.get().forEach((review) -> {
				reviewsIds.add(review.getId());
			});
		}

		returnValue.setReviewsIds(reviewsIds);

		List<Integer> persistenceLoginsIds = new ArrayList<Integer>();
		Optional<List<PersistenceLoginEntity>> persistenceLoginsOptional = Optional
				.ofNullable(customerEntity.getPersistenceLogins());
		if (!persistenceLoginsOptional.isEmpty()) {
			persistenceLoginsOptional.get().forEach((persistence) -> {
				persistenceLoginsIds.add(persistence.getId());
			});
		}

		returnValue.setPersistenceLoginsIds(persistenceLoginsIds);

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Optional<LocalDate> dateOfBirthOptional = Optional.ofNullable(customerEntity.getDateOfBirth());
		if (dateOfBirthOptional.isPresent()) {
			String dateOfBirth = dateOfBirthOptional.get().format(dateFormatter);
			returnValue.setDateOfBirth(dateOfBirth);
		}

		Optional<Timestamp> registrationTimeOptional = Optional.ofNullable(customerEntity.getRegistrationTime());
		if (registrationTimeOptional.isPresent()) {
			String registrationTimeStr = registrationTimeOptional.get().toLocalDateTime().atZone(zoneId)
					.format(formatter);
			returnValue.setRegistrationTime(registrationTimeStr);
		}
		return returnValue;
	}

	public CustomerEntity customerDtoToEntity(CustomerDto customer) {
		CustomerEntity returnValue = mapper.map(customer, CustomerEntity.class);
		Optional<Integer> userIdOptional = Optional.ofNullable(customer.getUserId());
		if (userIdOptional.isPresent()) {
			Integer userId = userIdOptional.get();
			UserEntity userEntity = userRepository.findById(userId).orElse(null);
			if (userEntity != null) {
				returnValue.setUser(userEntity);
			}

		}

		Optional<Integer> cartIdOptional = Optional.ofNullable(customer.getCartId());
		if (cartIdOptional.isPresent()) {
			Integer cartId = cartIdOptional.get();
			CartEntity cartEntity = cartRepository.findById(cartId).orElse(null);
			if (cartEntity != null) {
				returnValue.setCart(cartEntity);
			}
		}

		Optional<Integer> wishListIdOptional = Optional.ofNullable(customer.getWishListId());
		if (wishListIdOptional.isPresent()) {
			Integer wishListId = wishListIdOptional.get();
			WishListEntity wishListEntity = wishListRepository.findById(wishListId).orElse(null);
			if (wishListEntity != null) {
				returnValue.setWishList(wishListEntity);
			}

		}

		Optional<Integer> loyaltyCardIdOptional = Optional.ofNullable(customer.getLoyaltyCardId());
		if (loyaltyCardIdOptional.isPresent()) {
			Integer loyaltyCardId = loyaltyCardIdOptional.get();
			LoyaltyCardEntity cardEntity = loyaltyCardRepository.findById(loyaltyCardId).orElse(null);
			if (cardEntity != null) {
				returnValue.setLoyaltyCard(cardEntity);
			}

		}

		Optional<Integer> delieryAddressIdOptional = Optional.ofNullable(customer.getDeliveryAddressId());
		if (delieryAddressIdOptional.isPresent()) {
			Integer deliveryAddressId = delieryAddressIdOptional.get();
			DeliveryAddressEntity addressEntity = addressRepository.findById(deliveryAddressId).orElse(null);
			if (addressEntity != null) {
				returnValue.setDeliveryAddress(addressEntity);
			}
		}

		Optional<List<Integer>> reviewsIdsOptional = Optional.ofNullable(customer.getReviewsIds());
		List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();
		if (!reviewsIdsOptional.isEmpty()) {
			reviewsIdsOptional.get().forEach((reviewId) -> {
				ReviewEntity reviewEntity = reviewRepository.findById(reviewId).get();
				reviews.add(reviewEntity);
			});
		}

		returnValue.setReviews(reviews);

		Optional<List<Integer>> persistenceLoginsIdsOptional = Optional.ofNullable(customer.getPersistenceLoginsIds());
		List<PersistenceLoginEntity> persistenceLogins = new ArrayList<PersistenceLoginEntity>();
		if (!persistenceLoginsIdsOptional.isEmpty()) {
			persistenceLoginsIdsOptional.get().forEach((persistenceId) -> {
				PersistenceLoginEntity persistence = persistenceRepository.findById(persistenceId).get();
				persistenceLogins.add(persistence);
			});
		}

		returnValue.setPersistenceLogins(persistenceLogins);

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Optional<String> dateOfBirthStrOptional = Optional.ofNullable(customer.getDateOfBirth());
		if (dateOfBirthStrOptional.isPresent()) {
			LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStrOptional.get(), dateFormatter);
			returnValue.setDateOfBirth(dateOfBirth);
		}

		return returnValue;
	}

	public LoyaltyCardDto loyaltyCardEntityToDto(LoyaltyCardEntity card) {
		LoyaltyCardDto returnValue = mapper.map(card, LoyaltyCardDto.class);
		Optional<CustomerEntity> customerOptional = Optional.ofNullable(card.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getId());
		}

		return returnValue;
	}

	public LoyaltyCardEntity loyaltyCardDtoToEntity(LoyaltyCardDto card) {
		LoyaltyCardEntity returnValue = mapper.map(card, LoyaltyCardEntity.class);
		Optional<Integer> customerIdOptional = Optional.ofNullable(card.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).orElse(null);
			if (customerEntity != null) {
				returnValue.setCustomer(customerEntity);
			}

		}

		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);

		Optional<OrderAddressEntity> addressOptional = Optional.ofNullable(orderEntity.getAddress());
		if (addressOptional.isPresent()) {
			returnValue.setAddressId(addressOptional.get().getId());
		}

		Optional<List<OrderItemEntity>> orderedItemsOptional = Optional.ofNullable(orderEntity.getOrderedItems());
		List<Integer> orderedItemsIds = new ArrayList<Integer>();
		if (!orderedItemsOptional.isEmpty()) {
			orderedItemsOptional.get().forEach((item) -> {
				orderedItemsIds.add(item.getId());
			});
		}

		returnValue.setOrderedItemsIds(orderedItemsIds);

		Optional<Timestamp> createTimeOptional = Optional.ofNullable(orderEntity.getCreateTime());
		if (createTimeOptional.isPresent()) {
			ZonedDateTime createTime = createTimeOptional.get().toLocalDateTime().atZone(zoneId);
			returnValue.setCreateTime(createTime.format(formatter));
		}

		Optional<CartEntity> cartOptional = Optional.ofNullable(orderEntity.getCart());
		if (cartOptional.isPresent()) {
			returnValue.setCartId(cartOptional.get().getId());
		}

		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto order) {
		OrderEntity returnValue = mapper.map(order, OrderEntity.class);

		Optional<Integer> addressIdOptional = Optional.ofNullable(order.getAddressId());
		if (addressIdOptional.isPresent()) {
			Integer addressId = addressIdOptional.get();
			OrderAddressEntity addressEntity = orderAddressRepository.findById(addressId).orElse(null);
			if (addressEntity != null) {
				returnValue.setAddress(addressEntity);
			}

		}

		Optional<List<Integer>> orderedItemsIdsOptional = Optional.ofNullable(order.getOrderedItemsIds());
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		if (!orderedItemsIdsOptional.isEmpty()) {
			orderedItemsIdsOptional.get().forEach((itemId) -> {
				OrderItemEntity item = orderItemRepository.findById(itemId).get();
				orderedItems.add(item);
			});
		}

		returnValue.setOrderedItems(orderedItems);

		Optional<Integer> cartIdOptional = Optional.ofNullable(order.getCartId());
		if (cartIdOptional.isPresent()) {
			Integer cartId = cartIdOptional.get();
			CartEntity cartEntity = cartRepository.findById(cartId).orElse(null);
			if (cartEntity != null) {
				returnValue.setCart(cartEntity);
			}
		}
		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);

		Optional<OrderEntity> orderOptional = Optional.ofNullable(itemEntity.getOrder());
		if (orderOptional.isPresent()) {
			returnValue.setOrderId(orderOptional.get().getId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);

		Optional<Integer> orderIdOptional = Optional.ofNullable(itemDto.getOrderId());
		if (orderIdOptional.isPresent()) {
			Integer orderId = orderIdOptional.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
			if (orderEntity != null) {
				returnValue.setOrder(orderEntity);
			}

		}

		return returnValue;
	}

	public ReviewDto reviewEntityToDto(ReviewEntity reviewEntity) {
		ReviewDto returnValue = mapper.map(reviewEntity, ReviewDto.class);
		Optional<CustomerEntity> authorOptional = Optional.ofNullable(reviewEntity.getAuthor());
		if (authorOptional.isPresent()) {
			returnValue.setAuthorId(authorOptional.get().getId());
		}

		Optional<BookEntity> bookOptional = Optional.ofNullable(reviewEntity.getBook());
		if (bookOptional.isPresent()) {
			returnValue.setBookId(bookOptional.get().getId());
		}

		Optional<Timestamp> createTimeOptional = Optional.ofNullable(reviewEntity.getCreateTime());
		if (createTimeOptional.isPresent()) {
			ZonedDateTime createTime = createTimeOptional.get().toLocalDateTime().atZone(zoneId);
			returnValue.setCreateTime(createTime.format(formatter));
		}

		return returnValue;
	}

	public ReviewEntity reviewDtoToEntity(ReviewDto review) {
		ReviewEntity returnValue = mapper.map(review, ReviewEntity.class);
		Optional<Integer> authorIdOptional = Optional.ofNullable(review.getAuthorId());
		if (authorIdOptional.isPresent()) {
			Integer authorId = authorIdOptional.get();
			CustomerEntity author = customerRepository.findById(authorId).orElse(null);
			if (author != null) {
				returnValue.setAuthor(author);
			}
		}

		Optional<Integer> bookIdOptional = Optional.ofNullable(review.getBookId());
		if (bookIdOptional.isPresent()) {
			Integer bookId = bookIdOptional.get();
			BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
			if (bookEntity != null) {
				returnValue.setBook(bookEntity);
			}

		}

		return returnValue;
	}

	public WishListDto wishListEntityToDto(WishListEntity wishList) {
		WishListDto returnValue = mapper.map(wishList, WishListDto.class);
		Optional<List<BookEntity>> booksOptional = Optional.ofNullable(wishList.getBooks());
		List<Integer> booksIds = new ArrayList<Integer>();
		if (!booksOptional.isEmpty()) {
			booksOptional.get().forEach((book) -> {
				booksIds.add(book.getId());
			});
		}

		returnValue.setBooksIds(booksIds);

		Optional<CustomerEntity> customerOptional = Optional.ofNullable(wishList.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getId());
		}

		return returnValue;
	}

	public WishListEntity wishListDtoToEntity(WishListDto wishList) {
		WishListEntity returnValue = mapper.map(wishList, WishListEntity.class);
		Optional<List<Integer>> booksIdsOptional = Optional.ofNullable(wishList.getBooksIds());
		List<BookEntity> books = new ArrayList<BookEntity>();
		if (!booksIdsOptional.isEmpty()) {
			booksIdsOptional.get().forEach((bookId) -> {
				BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
				if (bookEntity != null) {
					books.add(bookEntity);
				}

			});
		}

		returnValue.setBooks(books);

		Optional<Integer> customerIdOptional = Optional.ofNullable(wishList.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).orElse(null);
			if (customerEntity != null) {
				returnValue.setCustomer(customerEntity);
			}

		}

		return returnValue;
	}

	public LoyaltyCardRequestDto cardRequestEntityToDto(LoyaltyCardRequestEntity request) {
		LoyaltyCardRequestDto returnValue = mapper.map(request, LoyaltyCardRequestDto.class);
		Optional<CustomerEntity> customerOptional = Optional.ofNullable(request.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getId());
		}
		return returnValue;
	}

	public LoyaltyCardRequestEntity cardRequestDtoToEntity(LoyaltyCardRequestDto request) {
		LoyaltyCardRequestEntity returnValue = mapper.map(request, LoyaltyCardRequestEntity.class);
		Optional<Integer> customerIdOptional = Optional.ofNullable(request.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).orElse(null);
			if (customerEntity != null) {
				returnValue.setCustomer(customerEntity);
			}

		}
		return returnValue;
	}

	public DeliveryAddressDto deliveryAddressEntityToDto(DeliveryAddressEntity address) {
		DeliveryAddressDto returnValue = mapper.map(address, DeliveryAddressDto.class);
		Optional<CustomerEntity> customerOptional = Optional.ofNullable(address.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getId());
		}
		return returnValue;
	}

	public DeliveryAddressEntity deliveryAddressDtoToEntity(DeliveryAddressDto address) {
		DeliveryAddressEntity returnValue = mapper.map(address, DeliveryAddressEntity.class);
		Optional<Integer> customerIdOptional = Optional.ofNullable(address.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).orElse(null);
			if (customerEntity != null) {
				returnValue.setCustomer(customerEntity);
			}

		}
		return returnValue;
	}

	public OrderItemDto cartItemToOrderItem(CartItemDto cartItem) {
		OrderItemDto returnValue = mapper.map(cartItem, OrderItemDto.class);

		Optional<Integer> bookIdOptional = Optional.ofNullable(cartItem.getBookId());
		if (bookIdOptional.isPresent()) {
			Integer bookId = bookIdOptional.get();
			BookEntity bookEntity = bookRepository.findById(bookId).orElse(null);
			if (bookEntity != null) {
				returnValue.setBookName(bookEntity.getName());
				returnValue.setBookPrice(bookEntity.getPrice());
			}

		}

		returnValue.setId(null);
		return returnValue;
	}

	public PersistenceLoginDto persistenceEntityToDto(PersistenceLoginEntity persistence) {
		PersistenceLoginDto returnValue = mapper.map(persistence, PersistenceLoginDto.class);
		Optional<CustomerEntity> customerOptional = Optional.ofNullable(persistence.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getId());
		}

		Optional<Timestamp> createTimeOptional = Optional.ofNullable(persistence.getCreateTime());
		if (createTimeOptional.isPresent()) {
			ZonedDateTime createTime = createTimeOptional.get().toLocalDateTime().atZone(zoneId);
			returnValue.setCreateTime(createTime.format(formatter));
		}

		return returnValue;
	}

	public PersistenceLoginEntity persistenceDtoToEntity(PersistenceLoginDto persistence) {
		PersistenceLoginEntity returnValue = mapper.map(persistence, PersistenceLoginEntity.class);
		Optional<Integer> customerIdOptional = Optional.ofNullable(persistence.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).orElse(null);
			if (customerEntity != null) {
				returnValue.setCustomer(customerEntity);
			}
		}

		return returnValue;
	}

	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderOptional = Optional.ofNullable(address.getOrder());
		if (orderOptional.isPresent()) {
			returnValue.setOrderId(orderOptional.get().getId());
		}

		return returnValue;
	}

	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderIdOptional = Optional.ofNullable(address.getOrderId());
		if (orderIdOptional.isPresent()) {
			Integer orderId = orderIdOptional.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
			if (orderEntity != null) {
				returnValue.setOrder(orderEntity);
			}

		}

		return returnValue;
	}

	public OrderAddressDto addressToOrderAddress(DeliveryAddressDto address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		returnValue.setId(null);
		return returnValue;
	}

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		Optional<Byte> enabledOptional = Optional.ofNullable(userEntity.getEnabled());
		if (enabledOptional.isPresent()) {
			returnValue.setEnabled(Short.valueOf(enabledOptional.get()));
		}

		Optional<List<RoleEntity>> rolesOptional = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOptional.isEmpty()) {
			rolesOptional.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOptional = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOptional.isEmpty()) {
			rolesIdsOptional.get().forEach((roleId) -> {
				RoleEntity role = roleRepository.findById(roleId).get();
				roles.add(role);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		Optional<List<UserEntity>> usersOptional = Optional.ofNullable(roleEntity.getUsers());
		List<Integer> userIds = new ArrayList<>();

		if (!usersOptional.isEmpty()) {
			usersOptional.get().forEach((user) -> {
				userIds.add(user.getId());
			});
		}

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		Optional<List<Integer>> usersIdsOptional = Optional.ofNullable(roleDto.getUsersIds());
		List<UserEntity> users = new ArrayList<>();

		if (!usersIdsOptional.isEmpty()) {
			usersIdsOptional.get().forEach((userId) -> {
				UserEntity userEntity = userRepository.findById(userId).get();
				users.add(userEntity);
			});
		}

		returnValue.setUsers(users);
		return returnValue;
	}

	public Timestamp getCurrentUTCTimestamp() {
		ZonedDateTime currentTime = Instant.now().atZone(zoneId);
		return Timestamp.valueOf(currentTime.toLocalDateTime());
	}
}
