package com.radovan.spring.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.radovan.spring.dto.BookImageDto;

public interface BookImageService {

	BookImageDto addImage(MultipartFile file, Integer bookId);

	void deleteImage(Integer imageId);

	List<BookImageDto> listAll();
	
	BookImageDto getImageById(Integer imageId);
}
