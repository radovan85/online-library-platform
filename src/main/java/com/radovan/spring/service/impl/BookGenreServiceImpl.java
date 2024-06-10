package com.radovan.spring.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.entity.BookGenreEntity;
import com.radovan.spring.exceptions.ExistingInstanceException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.BookGenreRepository;
import com.radovan.spring.service.BookGenreService;
import com.radovan.spring.service.BookService;

@Service
public class BookGenreServiceImpl implements BookGenreService {

	@Autowired
	private BookGenreRepository genreRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private BookService bookService;

	@Override
	@Transactional
	public BookGenreDto addGenre(BookGenreDto genre) {
		// TODO Auto-generated method stub
		String name = genre.getName();
		Optional<BookGenreEntity> genreOptional = genreRepository.findByName(name);
		if (genreOptional.isPresent()) {
			throw new ExistingInstanceException(new Error("This genre exists already!"));
		}
		BookGenreEntity genreEntity = tempConverter.bookGenreDtoToEntity(genre);
		BookGenreEntity storedGenre = genreRepository.save(genreEntity);
		return tempConverter.bookGenreEntityToDto(storedGenre);

	}

	@Override
	@Transactional(readOnly = true)
	public BookGenreDto getGenreById(Integer genreId) {
		// TODO Auto-generated method stub

		BookGenreEntity genreEntity = genreRepository.findById(genreId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The genre has not been found!")));

		return tempConverter.bookGenreEntityToDto(genreEntity);

	}

	@Override
	@Transactional(readOnly = true)
	public BookGenreDto getGenreByName(String name) {
		// TODO Auto-generated method stub

		BookGenreEntity genreEntity = genreRepository.findByName(name)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The genre has not been found!")));
		return tempConverter.bookGenreEntityToDto(genreEntity);
	}

	@Override
	@Transactional
	public void deleteGenre(Integer genreId) {
		// TODO Auto-generated method stub
		getGenreById(genreId);
		List<BookDto> allBooks = bookService.listAllByGenreId(genreId);
		allBooks.forEach((book) -> {
			bookService.deleteBook(book.getId());
		});
		genreRepository.deleteById(genreId);
		genreRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookGenreDto> listAll() {
		// TODO Auto-generated method stub
		List<BookGenreEntity> allGenres = genreRepository.findAll();
		return allGenres.stream().map(tempConverter::bookGenreEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public BookGenreDto updateGenre(BookGenreDto genre, Integer genreId) {
		// TODO Auto-generated method stub
		BookGenreDto tempGenre = getGenreById(genreId);
		System.out.println(tempGenre.toString());
		System.out.println(genre.toString());
		String name = genre.getName();
		Optional<BookGenreEntity> genreOptional = genreRepository.findByName(name);
		if (genreOptional.isPresent()) {
			if (genreOptional.get().getId() != tempGenre.getId()) {
				throw new ExistingInstanceException(new Error("This genre exists already!"));
			}
		}

		genre.setId(genreId);
		BookGenreEntity updatedGenre = genreRepository.saveAndFlush(tempConverter.bookGenreDtoToEntity(genre));
		return tempConverter.bookGenreEntityToDto(updatedGenre);
	}

}
