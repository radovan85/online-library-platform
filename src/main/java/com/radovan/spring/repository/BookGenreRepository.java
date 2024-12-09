package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.BookGenreEntity;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenreEntity, Integer> {

	Optional<BookGenreEntity> findByName(@Param("name") String name);

}
