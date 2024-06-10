package com.radovan.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.WishListDto;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.WishListRepository;
import com.radovan.spring.service.WishListService;

@Service
public class WishListServiceImpl implements WishListService {

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	@Transactional(readOnly = true)
	public WishListDto getWishListById(Integer id) {
		// TODO Auto-generated method stub
		WishListEntity wishListEntity = wishListRepository.findById(id)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The wishist has not been found!")));

		return tempConverter.wishListEntityToDto(wishListEntity);
	}

}
