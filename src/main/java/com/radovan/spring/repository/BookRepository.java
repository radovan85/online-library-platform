package com.radovan.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

	Optional<BookEntity> findByISBN(String isbn);

	@Query(value = "select * from books where genre_id = :genreId", nativeQuery = true)
	List<BookEntity> findAllByGenreId(@Param("genreId") Integer genreId);

	@Query(value = "SELECT * FROM books WHERE LOWER(isbn) LIKE '%' || LOWER(:keyword) || '%'"
			+ " OR LOWER(publisher) LIKE '%' || LOWER(:keyword) || '%'"
			+ " OR LOWER(author) LIKE '%' || LOWER(:keyword) || '%'"
			+ " OR LOWER(description) LIKE '%' || LOWER(:keyword) || '%'"
			+ " OR LOWER(name) LIKE '%' || LOWER(:keyword) || '%'", nativeQuery = true)
	List<BookEntity> findAllByKeyword(@Param("keyword") String keyword);

	@Query(value = "select cast(avg(rating) as decimal(4,2)) from reviews where book_id = :bookId and authorized=1", nativeQuery = true)
	Optional<Float> calculateAverageRating(@Param("bookId") Integer bookId);

	@Query(value = "select * from books order by id asc", nativeQuery = true)
	List<BookEntity> findAllSortedById();

	@Query(value = "select * from books order by average_rating desc", nativeQuery = true)
	List<BookEntity> findAllSortedByRating();

	@Query(value = "select * from books order by price asc", nativeQuery = true)
	List<BookEntity> findAllSortedByPrice();

	@Modifying
	@Query(value = "delete from books_wishlists where book_id = :bookId", nativeQuery = true)
	void eraseBookFromAllWishlists(@Param("bookId") Integer bookId);

}
