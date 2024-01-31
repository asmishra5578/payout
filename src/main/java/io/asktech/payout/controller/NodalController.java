package io.asktech.payout.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.nodal.MerNodalBalTransReq;
import io.asktech.payout.dto.nodal.MerNodalCheckTXNstatusReq;
import io.asktech.payout.dto.nodal.MerchantUpdateAccountRequest;
import io.asktech.payout.dto.webhooks.NodalWebhookReq;
import io.asktech.payout.dto.webhooks.NodalWebhookRes;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.nodal.NodalBenefManagementReq;
import io.asktech.payout.modal.nodal.PaytmVodanAccountDeleteReq;
import io.asktech.payout.service.PayoutNodalService;

@RestController
@RequestMapping("/nodal")
public class NodalController {
	
	@Autowired
	PayoutNodalService payoutNodalService;
	
	static Logger logger = LoggerFactory.getLogger(NodalController.class);
	
	@PostMapping(value = "/webhook")
	public ResponseEntity<?> nodalwebhook(@RequestBody NodalWebhookReq dto) throws ValidationExceptions, JsonProcessingException {
		
		NodalWebhookRes nodalWebhookRes=payoutNodalService.SaveWebhook(dto);
		
		return ResponseEntity.ok().body(nodalWebhookRes);
	}	
	
	@GetMapping(value = "/getBalance")
	public ResponseEntity<?> getAccountBalance(@RequestParam("account_type") String ca) throws JsonProcessingException, ParseException  {
		
		logger.info("Inside Controller");
		return ResponseEntity.ok().body(payoutNodalService.getBalanceInfo(ca));
	}
	
	@PostMapping(value = "/balancetransfer")
	public ResponseEntity<?> doBalanceTransfer(@RequestBody MerNodalBalTransReq merNodalBalTransReq) 
			throws JsonProcessingException, ParseException, ValidationExceptions  {
		
		logger.info("Inside Controller");
		return ResponseEntity.ok().body(payoutNodalService.doBalanceTransfer(merNodalBalTransReq));
	}
	
	@PostMapping(value = "/txnStatus")
	public ResponseEntity<?> getTXNStatus(@RequestBody MerNodalCheckTXNstatusReq merNodalCheckTXNstatusReq) throws ParseException, Exception {

		logger.info("Inside Controller");
		return ResponseEntity.ok().body(payoutNodalService.getTXNStatusFromMerchant(merNodalCheckTXNstatusReq));
	}
	
	@PostMapping(value = "/addBeneficiary")
	public ResponseEntity<?> addBeneficiary(@RequestBody NodalBenefManagementReq addBeneficiaryRequest) throws ParseException, Exception {

		logger.info("Inside Controller");
		return ResponseEntity.ok().body(payoutNodalService.addBeneficiary(addBeneficiaryRequest));
	}
	
	@PutMapping(value = "/deleteBeneficiary")
	public ResponseEntity<?> deleteBeneficiary(@RequestBody PaytmVodanAccountDeleteReq beneficiaryRefId) throws ParseException, Exception {

		logger.info("Inside deleteBeneficiary Controller ::");
		return ResponseEntity.ok().body(payoutNodalService.deleteBeneficiary(beneficiaryRefId));
	}
	
	@PutMapping(value = "/updateBeneficiary")
	public ResponseEntity<?> updateBeneficiary(@RequestBody MerchantUpdateAccountRequest nodalUpdateAccountRequest) throws ParseException, Exception {

		logger.info("Inside updateBeneficiary Controller ::");
		return ResponseEntity.ok().body(payoutNodalService.updateBeneficiary(nodalUpdateAccountRequest));
	}
	
	@GetMapping(value = "/getDetailsByNickName")
	public ResponseEntity<?> getDetailsByNickName(@RequestParam("BeneficiaryNickName") String nickName) throws JsonProcessingException, ParseException  {
		
		logger.info("Inside Controller getDetailsByNickName()");
		//logger.info(payoutNodalService.getDetailsByNickName(nickName).toString());
		return ResponseEntity.ok().body(payoutNodalService.getDetailsByNickName(nickName));
	}
	
	@GetMapping(value = "/getDetailsByRefId/{beneficiaryRefId}")
	public ResponseEntity<?> getDetailsByRefId(@PathVariable(value="beneficiaryRefId") String beneficiaryRefId) throws JsonProcessingException, ParseException  {
		
		logger.info("Inside Controller getDetailsByNickName()");
		//logger.info(payoutNodalService.getDetailsByNickName(nickName).toString());
		return ResponseEntity.ok().body(payoutNodalService.getDetailsByRefId(beneficiaryRefId));
	}
	
}
