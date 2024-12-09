package com.radovan.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.entity.LoyaltyCardRequestEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.LoyaltyCardRepository;
import com.radovan.spring.repository.LoyaltyCardRequestRepository;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.LoyaltyCardService;

@Service
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

	@Autowired
	private LoyaltyCardRequestRepository requestRepository;

	@Autowired
	private LoyaltyCardRepository cardRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CustomerService customerService;

	@Override
	@Transactional(readOnly = true)
	public List<LoyaltyCardRequestDto> listAllCardRequests() {
		// TODO Auto-generated method stub

		List<LoyaltyCardRequestEntity> allRequests = requestRepository.findAll();
		return allRequests.stream().map(tempConverter::cardRequestEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void authorizeRequest(Integer cardRequestId) {
		// TODO Auto-generated method stub
		LoyaltyCardRequestEntity cardRequestEntity = requestRepository.findById(cardRequestId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("Card request has not been found!")));
		LoyaltyCardDto card = new LoyaltyCardDto();
		card.setDiscount(0);
		card.setPoints(0);
		CustomerDto customer = customerService.getCustomerById(cardRequestEntity.getCustomer().getId());
		card.setCustomerId(customer.getId());
		LoyaltyCardEntity storedCard = cardRepository.save(tempConverter.loyaltyCardDtoToEntity(card));
		customer.setLoyaltyCardId(storedCard.getId());
		customerService.updateCustomer(customer.getId(), customer);

		requestRepository.deleteById(cardRequestId);
		requestRepository.flush();
	}

	@Override
	@Transactional
	public void rejectRequest(Integer cardRequestId) {
		// TODO Auto-generated method stub
		requestRepository.deleteById(cardRequestId);
		requestRepository.flush();
	}

	@Override
	@Transactional
	public LoyaltyCardRequestDto addCardRequest() {
		// TODO Auto-generated method stub
		CustomerDto currentCustomer = customerService.getCurrentCustomer();
		LoyaltyCardRequestDto cardRequest = new LoyaltyCardRequestDto();
		cardRequest.setCustomerId(currentCustomer.getId());
		LoyaltyCardRequestEntity storedRequest = requestRepository
				.save(tempConverter.cardRequestDtoToEntity(cardRequest));
		return tempConverter.cardRequestEntityToDto(storedRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public LoyaltyCardRequestDto getRequestByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub

		LoyaltyCardRequestEntity cardRequestEntity = requestRepository.findByCustomerId(customerId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("Card request has not been found!")));
		return tempConverter.cardRequestEntityToDto(cardRequestEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<LoyaltyCardDto> listAllLoyaltyCards() {
		// TODO Auto-generated method stub

		List<LoyaltyCardEntity> allCards = cardRepository.findAll();
		return allCards.stream().map(tempConverter::loyaltyCardEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public LoyaltyCardDto getCardById(Integer cardId) {
		// TODO Auto-generated method stub

		LoyaltyCardEntity cardEntity = cardRepository.findById(cardId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The card has not been found!")));
		return tempConverter.loyaltyCardEntityToDto(cardEntity);
	}

	@Override
	@Transactional
	public LoyaltyCardDto updateLoyaltyCard(Integer cardId, LoyaltyCardDto card) {
		// TODO Auto-generated method stub
		getCardById(cardId);
		card.setId(cardId);
		LoyaltyCardEntity updatedCard = cardRepository.saveAndFlush(tempConverter.loyaltyCardDtoToEntity(card));
		return tempConverter.loyaltyCardEntityToDto(updatedCard);

	}

}
