package com.radovan.spring.utils;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookImageDto;
import com.radovan.spring.service.BookImageService;
import com.radovan.spring.service.BookService;

@Component
public class ThymeleafUtils {

	@Autowired
	private BookImageService imageService;

	@Autowired
	private BookService bookService;

	public String displayImage(Integer bookId) {
		String returnValue = null;
		BookDto book = bookService.getBookById(bookId);
		Optional<Integer> imageIdOptional = Optional.ofNullable(book.getImageId());
		if (imageIdOptional.isPresent()) {
			BookImageDto image = imageService.getImageById(imageIdOptional.get());
			returnValue = Base64.getEncoder().encodeToString(image.getData());
		}

		return returnValue;
	}
}
