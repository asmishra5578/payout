package io.asktech.payout.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.BankAccountVerifyRequest;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.service.PayoutMerchant;

@RestController
public class AccountVerifyCustomer {

	static Logger logger = LoggerFactory.getLogger(AccountVerifyCustomer.class);
	@Autowired
	PayoutMerchant payoutMerchant;

	@PostMapping(value = "/merchant/accountVerify")
	public ResponseEntity<?> bankAccountVerify(@RequestBody BankAccountVerifyRequest dto)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		logger.info("Insde Payout accountVerify () :: ");
		
		return ResponseEntity.ok().body(payoutMerchant.bankAccountVerify(dto));
	}
	
	@PostMapping(value = "/merchant/callBackApi")
	public ResponseEntity<?> callBackApi(@RequestBody String strCallBack)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		logger.info("Insde Payout PayTm CallBack API for AccountVerify :: "+strCallBack);
		return ResponseEntity.ok().body(strCallBack);
	}
}
