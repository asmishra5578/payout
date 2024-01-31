package io.asktech.payout.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.van.VanWebhookReq;
import io.asktech.payout.dto.van.VanWebhookRes;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.van.MerVanUpdateReq;
import io.asktech.payout.modal.van.VanCreationRequest;
import io.asktech.payout.service.PayoutVanService;
import io.asktech.payout.utils.Utility;

@RestController
@RequestMapping("/van")
public class VanController {

	@Autowired
	PayoutVanService payoutVanService;
	
	static Logger logger = LoggerFactory.getLogger(PayoutVanService.class);

	@PostMapping(value = "/webhook")
	public ResponseEntity<?> vanwebhook(@RequestBody VanWebhookReq dto)
			throws ValidationExceptions, JsonProcessingException {

		System.out.println("Van Webhook");
		System.out.println(Utility.convertDTO2JsonString(dto));
		VanWebhookRes vanWebhookRes = payoutVanService.SaveWebhook(dto);

		return ResponseEntity.ok().body(vanWebhookRes);
	}
	
	@PostMapping(value = "/createVanAccount")
	public ResponseEntity<?> createVanAccount(@RequestBody VanCreationRequest dto)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		logger.info("Inside createVanAccount controller");
		logger.info("Input Details :: "+Utility.convertDTO2JsonString(dto));
		
		return ResponseEntity.ok().body(payoutVanService.createVAN(dto));
	}
	
	@PutMapping(value = "/vanAccountStatusUpdate")
	public ResponseEntity<?> vanAccountStatusUpdate(@RequestBody MerVanUpdateReq merVanUpdateReq)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		logger.info("Inside createVanAccount controller");
		logger.info("Input Details vanAccountStatusUpdate :: "+Utility.convertDTO2JsonString(merVanUpdateReq));
		
		return ResponseEntity.ok().body(payoutVanService.vanAccountStatusUpdate(merVanUpdateReq));
	}
	
	@GetMapping(value="/listAllVanDetails")
	public ResponseEntity<?> listAllVanDetails()
			throws ValidationExceptions, JsonProcessingException, ParseException {
		
		return ResponseEntity.ok().body(payoutVanService.listAllVanDetails());
	}
	@GetMapping(value="/getVanDetails")
	public ResponseEntity<?> listAllVanDetails(@RequestParam String vanId)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		
		return ResponseEntity.ok().body(payoutVanService.getVanDetails(vanId));
	}
	
}
