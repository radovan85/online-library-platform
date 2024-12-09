package com.radovan.spring.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookImageDto;
import com.radovan.spring.entity.BookImageEntity;
import com.radovan.spring.exceptions.FileUploadException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.BookImageRepository;
import com.radovan.spring.service.BookImageService;
import com.radovan.spring.service.BookService;
import com.radovan.spring.utils.FileValidator;

@Service
public class BookImageServiceImpl implements BookImageService {

	@Autowired
	private BookImageRepository imageRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private FileValidator fileValidator;

	@Autowired
	private BookService bookService;

	@Override
	@Transactional
	public BookImageDto addImage(MultipartFile file, Integer bookId) {
		// TODO Auto-generated method stub
		BookDto book = bookService.getBookById(bookId);
		fileValidator.validateFile(file);
		Optional<BookImageEntity> imageOptional = imageRepository.findByBookId(bookId);
		if (imageOptional.isPresent()) {
			deleteImage(imageOptional.get().getId());
		}

		try {
			BookImageDto image = new BookImageDto();
			image.setBookId(bookId);
			image.setName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
			image.setContentType(file.getContentType());
			image.setSize(file.getSize());
			image.setData(file.getBytes());

			Optional<Integer> imageIdOptional = Optional.ofNullable(book.getImageId());
			if (imageIdOptional.isPresent()) {
				image.setId(imageIdOptional.get());
			}

			BookImageEntity imageEntity = tempConverter.imageDtoToEntity(image);
			BookImageEntity storedImage = imageRepository.save(imageEntity);

			return tempConverter.imageEntityToDto(storedImage);
		} catch (Exception e) {
			throw new FileUploadException(new Error("Failed to upload file: " + e.getMessage()));
		}
	}

	@Override
	@Transactional
	public void deleteImage(Integer imageId) {
		imageRepository.deleteById(imageId);
		imageRepository.flush();

	}

	@Override
	@Transactional(readOnly = true)
	public List<BookImageDto> listAll() {
		List<BookImageEntity> allImages = imageRepository.findAll();
		return allImages.stream().map(tempConverter::imageEntityToDto).collect(Collectors.toList());
	}

	@Override
	public BookImageDto getImageById(Integer imageId) {
		// TODO Auto-generated method stub
		BookImageEntity imageEntity = imageRepository.findById(imageId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The image has not been found!")));
		
		return tempConverter.imageEntityToDto(imageEntity);
	}
	
	

}
