package com.radovan.spring.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.WishListDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.WishListRepository;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.WishListService;
import com.radovan.spring.utils.RandomStringUtil;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private RandomStringUtil randomStringUtil;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private WishListService wishListService;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	@Transactional
	public BookDto addBook(BookDto book) {
		// TODO Auto-generated method stub
		Optional<Integer> bookIdOpt = Optional.ofNullable(book.getId());
		if (bookIdOpt.isPresent()) {
			Integer bookId = bookIdOpt.get();
			Float avgRating = bookRepository.calculateAverageRating(bookId).orElse(null);
			if (avgRating != null) {
				book.setAverageRating(avgRating);
			}
		}
		Optional<String> isbnOptional = Optional.ofNullable(book.getISBN());
		if (!isbnOptional.isPresent()) {
			book.setISBN(randomStringUtil.getAlphaNumericString(13).toUpperCase());
		}

		BookEntity bookEntity = tempConverter.bookDtoToEntity(book);
		BookEntity storedBook = bookRepository.save(bookEntity);
		BookDto returnValue = tempConverter.bookEntityToDto(storedBook);

		if (bookIdOpt.isPresent()) {
			List<CartItemEntity> allCartItems = cartItemRepository.findAllByBookId(returnValue.getId());
			if (!allCartItems.isEmpty()) {
				allCartItems.forEach((itemEntity) -> {
					Float itemPrice = returnValue.getPrice() * itemEntity.getQuantity();
					if (cartItemService.hasDiscount(itemEntity.getId())) {
						itemPrice = itemPrice - ((itemPrice / 100) * 35);
					}

					itemPrice = Float.valueOf(decfor.format(itemPrice));
					itemEntity.setPrice(itemPrice);
					cartItemRepository.saveAndFlush(itemEntity);
				});

				cartService.refreshAllCarts();
			}
		}
		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public BookDto getBookById(Integer bookId) {
		// TODO Auto-generated method stub

		BookEntity bookEntity = bookRepository.findById(bookId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The book has not been found!")));
		return tempConverter.bookEntityToDto(bookEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public BookDto getBookByISBN(String isbn) {
		// TODO Auto-generated method stub

		BookEntity bookEntity = bookRepository.findByISBN(isbn)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The book has not been found!")));

		return tempConverter.bookEntityToDto(bookEntity);
	}

	@Override
	@Transactional
	public void deleteBook(Integer bookId) {
		// TODO Auto-generated method stub
		removeBookFromAllWishlist(bookId);
		cartItemService.eraseAllByBookId(bookId);
		bookRepository.deleteById(bookId);
		bookRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> listAll() {
		// TODO Auto-generated method stub
		List<BookEntity> allBooks = bookRepository.findAll();
		return allBooks.stream().map(tempConverter::bookEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> listAllByGenreId(Integer genreId) {
		// TODO Auto-generated method stub

		List<BookEntity> allBooks = bookRepository.findAllByGenreId(genreId);
		return allBooks.stream().map(tempConverter::bookEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> search(String keyword) {
		// TODO Auto-generated method stub
		List<BookEntity> allBooks = bookRepository.findAllByKeyword(keyword);
		return allBooks.stream().map(tempConverter::bookEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void addToWishList(Integer bookId) {
		// TODO Auto-generated method stub
		CustomerDto customer = customerService.getCurrentCustomer();
		WishListDto wishlist = wishListService.getWishListById(customer.getWishListId());
		BookDto book = getBookById(bookId);
		List<Integer> booksIds = wishlist.getBooksIds();
		if (!booksIds.contains(book.getId())) {
			booksIds.add(book.getId());
			wishlist.setBooksIds(booksIds);
			wishListRepository.saveAndFlush(tempConverter.wishListDtoToEntity(wishlist));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> listAllFromWishList() {
		// TODO Auto-generated method stub
		CustomerDto customer = customerService.getCurrentCustomer();
		WishListDto wishList = wishListService.getWishListById(customer.getWishListId());
		List<BookDto> returnValue = new ArrayList<>();
		wishList.getBooksIds().forEach((bookId) -> {
			returnValue.add(getBookById(bookId));
		});
		return returnValue;
	}

	@Override
	@Transactional
	public void removeFromWishList(Integer bookId) {
		// TODO Auto-generated method stub
		CustomerDto customer = customerService.getCurrentCustomer();
		WishListDto wishList = wishListService.getWishListById(customer.getWishListId());
		List<Integer> booksIds = wishList.getBooksIds();
		booksIds.remove(bookId);
		wishList.setBooksIds(booksIds);
		wishListRepository.saveAndFlush(tempConverter.wishListDtoToEntity(wishList));
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> listAllByBookId() {
		// TODO Auto-generated method stub

		List<BookEntity> allBooks = bookRepository.findAllSortedById();
		return allBooks.stream().map(tempConverter::bookEntityToDto).collect(Collectors.toList());

	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> listAllByRating() {
		// TODO Auto-generated method stub

		List<BookEntity> allBooks = bookRepository.findAllSortedByRating();
		return allBooks.stream().map(tempConverter::bookEntityToDto).collect(Collectors.toList());

	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> listAllByPrice() {
		// TODO Auto-generated method stub

		List<BookEntity> allBooks = bookRepository.findAllSortedByPrice();
		return allBooks.stream().map(tempConverter::bookEntityToDto).collect(Collectors.toList());

	}

	@Override
	@Transactional
	public void removeBookFromAllWishlist(Integer bookId) {
		// TODO Auto-generated method stub
		bookRepository.eraseBookFromAllWishlists(bookId);
		bookRepository.flush();
	}

	@Override
	@Transactional
	public void refreshAvgRating() {
		// TODO Auto-generated method stub
		List<BookDto> allBooks = listAll();

		allBooks.forEach((book) -> {
			Float avgRating = bookRepository.calculateAverageRating(book.getId()).orElse(null);
			book.setAverageRating(avgRating);
			bookRepository.saveAndFlush(tempConverter.bookDtoToEntity(book));
		});

	}

}
