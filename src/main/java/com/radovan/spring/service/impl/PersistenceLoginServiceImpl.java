package com.radovan.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.PersistenceLoginDto;
import com.radovan.spring.entity.PersistenceLoginEntity;
import com.radovan.spring.repository.PersistenceLoginRepository;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.PersistenceLoginService;
import com.radovan.spring.service.UserService;

@Service
public class PersistenceLoginServiceImpl implements PersistenceLoginService {

	@Autowired
	private PersistenceLoginRepository persistenceRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public PersistenceLoginDto addPersistenceLogin() {
		// TODO Auto-generated method stub

		if (userService.isAdmin()) {
			return null;

		} else {

			CustomerDto customer = customerService.getCurrentCustomer();
			PersistenceLoginEntity persistence = new PersistenceLoginEntity();
			persistence.setCustomer(tempConverter.customerDtoToEntity(customer));
			persistence.setCreateTime(tempConverter.getCurrentUTCTimestamp());
			PersistenceLoginEntity storedPersistence = persistenceRepository.save(persistence);
			return tempConverter.persistenceEntityToDto(storedPersistence);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public PersistenceLoginDto getLastLogin(Integer customerId) {
		// TODO Auto-generated method stub
		PersistenceLoginDto returnValue = null;
		CustomerDto customer = customerService.getCustomerById(customerId);
		PersistenceLoginEntity persistenceLogin = persistenceRepository.findLastLogin(customer.getId()).orElse(null);
		if (persistenceLogin != null) {
			returnValue = tempConverter.persistenceEntityToDto(persistenceLogin);
		}

		return returnValue;

	}

}
