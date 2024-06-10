package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.exceptions.ExistingInstanceException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.DeliveryAddressRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.repository.WishListRepository;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.UserService;
import com.radovan.spring.utils.RegistrationForm;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private DeliveryAddressRepository addressRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Override
	@Transactional(readOnly = true)
	public List<CustomerDto> listAll() {
		// TODO Auto-generated method stub
		List<CustomerEntity> allCustomers = customerRepository.findAll();
		return allCustomers.stream().map(tempConverter::customerEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCustomerById(Integer id) {
		// TODO Auto-generated method stub
		CustomerEntity customerEntity = customerRepository.findById(id)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The customer has not been found!")));
		return tempConverter.customerEntityToDto(customerEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCustomerByUserId(Integer userId) {
		// TODO Auto-generated method stub
		CustomerEntity customerEntity = customerRepository.findByUserId(userId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The customer has not been found!")));
		return tempConverter.customerEntityToDto(customerEntity);

	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCustomerByCartId(Integer cartId) {
		// TODO Auto-generated method stub

		CustomerEntity customerEntity = customerRepository.findByCartId(cartId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The customer has not been found!")));
		return tempConverter.customerEntityToDto(customerEntity);
	}

	@Override
	@Transactional
	public CustomerDto updateCustomer(Integer customerId, CustomerDto customer) {
		// TODO Auto-generated method stub

		getCustomerById(customerId);
		customer.setId(customerId);
		CustomerEntity updatedCustomer = customerRepository.saveAndFlush(tempConverter.customerDtoToEntity(customer));
		return tempConverter.customerEntityToDto(updatedCustomer);
	}

	@Override
	@Transactional
	public CustomerDto storeCustomer(RegistrationForm form) {
		// TODO Auto-generated method stub
		UserDto userDto = form.getUser();
		Optional<UserEntity> testUser = userRepository.findByEmail(userDto.getEmail());
		if (testUser.isPresent()) {
			throw new ExistingInstanceException(new Error("This email already exists!"));
		}

		RoleEntity roleEntity = roleRepository.findByRole("ROLE_USER").orElse(null);
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userDto.setEnabled((short) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(roleEntity);
		UserEntity userEntity = tempConverter.userDtoToEntity(userDto);
		userEntity.setRoles(roles);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedUser);
		if(roleEntity!=null){
			roleEntity.setUsers(users);
		}
		roleRepository.saveAndFlush(roleEntity);

		CartEntity cartEntity = new CartEntity();
		cartEntity.setCartPrice(0f);
		CartEntity storedCart = cartRepository.save(cartEntity);

		WishListEntity wishListEntity = new WishListEntity();
		WishListEntity storedWishList = wishListRepository.save(wishListEntity);

		DeliveryAddressDto deliveryAddress = form.getAddress();
		DeliveryAddressEntity deliveryAddressEntity = tempConverter.deliveryAddressDtoToEntity(deliveryAddress);
		DeliveryAddressEntity storedAddress = addressRepository.save(deliveryAddressEntity);

		CustomerDto customerDto = form.getCustomer();
		customerDto.setUserId(storedUser.getId());
		customerDto.setCartId(storedCart.getId());
		customerDto.setWishListId(storedWishList.getId());
		customerDto.setDeliveryAddressId(storedAddress.getId());

		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customerDto);
		customerEntity.setRegistrationTime(tempConverter.getCurrentUTCTimestamp());
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);

		storedCart.setCustomer(storedCustomer);
		cartRepository.saveAndFlush(storedCart);

		storedWishList.setCustomer(storedCustomer);
		wishListRepository.saveAndFlush(storedWishList);

		storedAddress.setCustomer(storedCustomer);
		addressRepository.saveAndFlush(storedAddress);

		return tempConverter.customerEntityToDto(storedCustomer);

	}

	@Override
	@Transactional
	public CustomerDto addCustomer(CustomerDto customer) {
		// TODO Auto-generated method stub

		CustomerEntity storedCustomer = customerRepository.save(tempConverter.customerDtoToEntity(customer));
		return tempConverter.customerEntityToDto(storedCustomer);
	}

	@Override
	@Transactional
	public void deleteCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		getCustomerById(customerId);
		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		allOrders.forEach((order) -> {
			orderService.deleteOrder(order.getId());
		});
		customerRepository.deleteById(customerId);
		customerRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCurrentCustomer() {
		// TODO Auto-generated method stub
		UserDto authUser = userService.getCurrentUser();
		return getCustomerByUserId(authUser.getId());
	}

}
