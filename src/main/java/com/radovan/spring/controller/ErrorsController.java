package com.radovan.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import com.radovan.spring.exceptions.BookQuantityException;
import com.radovan.spring.exceptions.DataNotValidatedException;
import com.radovan.spring.exceptions.ExistingInstanceException;
import com.radovan.spring.exceptions.FileUploadException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.exceptions.SuspendedUserException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorsController {

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<String> handleMultipartException(HttpServletRequest request, Exception e) {
		return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<String> handleFileUploadException(Error error) {
		return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(InvalidCartException.class)
	public ResponseEntity<String> handleInvalidCartException(Error error) {
		return new ResponseEntity<>(error.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(SuspendedUserException.class)
	public ResponseEntity<String> handleSuspendedUserException(Error error) {
		SecurityContextHolder.clearContext();
		return new ResponseEntity<>(error.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
	}

	@ExceptionHandler(BookQuantityException.class)
	public ResponseEntity<String> handleBookQuantityException(Error error) {
		return new ResponseEntity<>(error.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(ExistingInstanceException.class)
	public ResponseEntity<String> handleExistingInstanceException(Error error) {
		return new ResponseEntity<String>(error.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InstanceUndefinedException.class)
	public ResponseEntity<String> handleInstanceUndefinedException(Error error) {
		return new ResponseEntity<String>(error.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(DataNotValidatedException.class)
	public ResponseEntity<String> handleDataNotValidatedException(Error error) {
		return new ResponseEntity<String>(error.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
}
