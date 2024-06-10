package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.BookImageEntity;

@Repository
public interface BookImageRepository extends JpaRepository<BookImageEntity, Integer> {

	@Query(value = "select * from book_images where book_id = :bookId", nativeQuery = true)
	Optional<BookImageEntity> findByBookId(@Param("bookId") Integer bookId);
}
