package io.asktech.payout.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.wallet.dto.NofityResponse2Merchant;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Service
public class NotiFyURLService2Merchant {

	static Logger logger = LoggerFactory.getLogger(NotiFyURLService2Merchant.class);
	@Async	
	public void sendNotifyDetails2Merchant(TransactionDetails transactionDetails) {
		logger.info("NotiFyURLService2Merchant");
		NofityResponse2Merchant nofityResponse2Merchant = new NofityResponse2Merchant();
		nofityResponse2Merchant.setAmount(transactionDetails.getAmount());
		nofityResponse2Merchant.setBankAccount(transactionDetails.getBankaccount());
		nofityResponse2Merchant.setBeneficiaryName(transactionDetails.getBeneficiaryName());
		nofityResponse2Merchant.setBeneficiaryVPA(transactionDetails.getBeneficiaryVPA());
		nofityResponse2Merchant.setCreationDate(transactionDetails.getCreated().toString());
		nofityResponse2Merchant.setIfscCode(transactionDetails.getIfsc());
		nofityResponse2Merchant.setMerchantOrderId(transactionDetails.getOrderId());
		nofityResponse2Merchant.setPgOrderId(transactionDetails.getInternalOrderId());
		nofityResponse2Merchant.setPhonenumber(transactionDetails.getPhonenumber());
		nofityResponse2Merchant.setPurpose(transactionDetails.getPurpose());
		nofityResponse2Merchant.setTransactionMessage(transactionDetails.getTransactionMessage());
		nofityResponse2Merchant.setTransactionStatus(transactionDetails.getTransactionStatus());
		nofityResponse2Merchant.setTransactionType(transactionDetails.getTransactionType());
		nofityResponse2Merchant.setUtrid(String.valueOf(transactionDetails.getUtrid()));
	  
		logger.info("Utr Id:\t"+ nofityResponse2Merchant.getUtrid());
		try {
			logger.info("\n\nNotiFy Payout Callback:\t"+ Utility.convertDTO2JsonString(nofityResponse2Merchant));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

		try {
			HttpResponse<Object> alertUrlCreateResponse = Unirest.post(transactionDetails.getCallBackURL().trim())
					.header("Content-Type", "application/json").header("Accept", "*/*").body(nofityResponse2Merchant)
					.asObject(Object.class);

		    
			logger.info("ALERT URL Response from Merchant Side ::" + alertUrlCreateResponse.getBody().toString());

		} catch (Exception e) {

			logger.error("Exception in nofity URL , please check with Merchant-"+e.getMessage());

			e.printStackTrace();

		}
	}
}
