package com.radovan.spring.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.DeliveryAddressService;
import com.radovan.spring.service.PersistenceLoginService;
import com.radovan.spring.service.UserService;
import com.radovan.spring.utils.RegistrationForm;

@Controller
public class MainController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private DeliveryAddressService addressService;

	@Autowired
	private PersistenceLoginService persistenceLoginService;

	@GetMapping(value = "/")
	public String sayIndex() {

		return "index";
	}

	@GetMapping(value = "/home")
	public String home() {

		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/login")
	public String login() {
		return "fragments/login :: fragmentContent";
	}

	@GetMapping(value = "/userRegistration")
	public String register(ModelMap map) {

		RegistrationForm tempForm = new RegistrationForm();
		map.put("tempForm", tempForm);
		return "fragments/registration :: fragmentContent";
	}

	@PostMapping(value = "/userRegistration")
	public String storeUser(@ModelAttribute("tempForm") RegistrationForm form) {
		customerService.storeCustomer(form);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/registerComplete")
	public String registrationCompl() {
		return "fragments/registration_completed :: fragmentContent";
	}

	@GetMapping(value = "/registerFail")
	public String registrationFail() {
		return "fragments/registration_failed :: fragmentContent";
	}

	@PostMapping(value = "/loggedout")
	public String logout() {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(null);
		SecurityContextHolder.clearContext();
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/loginErrorPage")
	public String logError(ModelMap map) {
		map.put("alert", "Invalid username or password!");
		return "fragments/login :: fragmentContent";
	}

	@PostMapping(value = "/loginPassConfirm")
	public String confirmLoginPass(Principal principal) {
		Optional<Principal> authPrincipal = Optional.ofNullable(principal);
		if (!authPrincipal.isPresent()) {
			throw new InstanceUndefinedException(new Error("Invalid user!"));
		}

		persistenceLoginService.addPersistenceLogin();

		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/aboutUs")
	public String aboutPage() {
		return "fragments/about :: fragmentContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/accountInfo")
	public String userAccountInfo(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		DeliveryAddressDto address = addressService.getAddressById(customer.getDeliveryAddressId());
		map.put("authUser", authUser);
		map.put("address", address);
		return "fragments/accountDetails :: fragmentContent";
	}

}
