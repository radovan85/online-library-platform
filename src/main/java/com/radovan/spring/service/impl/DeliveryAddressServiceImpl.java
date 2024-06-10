package com.radovan.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.DeliveryAddressRepository;
import com.radovan.spring.service.DeliveryAddressService;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

	@Autowired
	private DeliveryAddressRepository addressRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	@Transactional(readOnly = true)
	public DeliveryAddressDto getAddressById(Integer addressId) {
		// TODO Auto-generated method stub
		DeliveryAddressEntity addressEntity = addressRepository.findById(addressId).orElseThrow(
				() -> new InstanceUndefinedException(new Error("The delivery address has not been found!")));
		return tempConverter.deliveryAddressEntityToDto(addressEntity);
	}

	@Override
	@Transactional
	public DeliveryAddressDto createAddress(DeliveryAddressDto address) {
		// TODO Auto-generated method stub
		DeliveryAddressEntity storedAddress = addressRepository.save(tempConverter.deliveryAddressDtoToEntity(address));
		return tempConverter.deliveryAddressEntityToDto(storedAddress);

	}

}
