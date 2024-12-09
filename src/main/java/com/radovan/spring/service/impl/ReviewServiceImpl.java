package com.radovan.spring.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.entity.ReviewEntity;
import com.radovan.spring.exceptions.ExistingInstanceException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.ReviewRepository;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private BookService bookService;

	@Override
	@Transactional
	public ReviewDto addReview(ReviewDto review) {
		// TODO Auto-generated method stub
		CustomerDto customer = customerService.getCurrentCustomer();
		Optional<ReviewEntity> reviewOptional = reviewRepository.findByCustomerIdAndBookId(customer.getId(),
				review.getBookId());
		if (reviewOptional.isPresent()) {
			throw new ExistingInstanceException(new Error("You have reviewed this book already!"));
		}

		review.setAuthorId(customer.getId());
		review.setAuthorized((short) 0);
		ReviewEntity reviewEntity = tempConverter.reviewDtoToEntity(review);
		reviewEntity.setCreateTime(tempConverter.getCurrentUTCTimestamp());
		ReviewEntity storedReview = reviewRepository.save(reviewEntity);
		return tempConverter.reviewEntityToDto(storedReview);

	}

	@Override
	@Transactional(readOnly = true)
	public ReviewDto getReviewById(Integer reviewId) {
		// TODO Auto-generated method stub
		ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The review has not been found!")));
		return tempConverter.reviewEntityToDto(reviewEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewDto> listAll() {
		// TODO Auto-generated method stub
		List<ReviewEntity> allReviews = reviewRepository.findAll();
		return allReviews.stream().map(tempConverter::reviewEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewDto> listAllByBookId(Integer bookId) {
		// TODO Auto-generated method stub
		List<ReviewEntity> allReviews = reviewRepository.findAllAuthorizedByBookId(bookId);
		return allReviews.stream().map(tempConverter::reviewEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteReview(Integer reviewId) {
		// TODO Auto-generated method stub
		reviewRepository.deleteById(reviewId);
		reviewRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewDto> listAllAuthorized() {
		// TODO Auto-generated method stub
		List<ReviewEntity> allReviews = reviewRepository.findAllAuthorized();
		return allReviews.stream().map(tempConverter::reviewEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewDto> listAllOnHold() {
		// TODO Auto-generated method stub
		List<ReviewEntity> allReviews = reviewRepository.findAllOnHold();
		return allReviews.stream().map(tempConverter::reviewEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void authorizeReview(Integer reviewId) {
		// TODO Auto-generated method stub
		ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The review has not been found!")));
		reviewEntity.setAuthorized((byte) 1);
		reviewRepository.saveAndFlush(reviewEntity);
		bookService.refreshAvgRating();
	}

}
