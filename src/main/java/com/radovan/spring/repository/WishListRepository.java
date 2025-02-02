package com.radovan.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.WishListEntity;

@Repository
public interface WishListRepository extends JpaRepository<WishListEntity, Integer> {


}
