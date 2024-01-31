package io.asktech.payout.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.TransactionFilterReq;
import io.asktech.payout.dto.merchant.WalletFilterReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.service.PayoutMerchant;
import io.asktech.payout.service.admin.DashboardReportService;
import io.asktech.payout.wallet.service.WalletService;

@RestController
public class PayOutReportController {
	
	static Logger logger = LoggerFactory.getLogger(WalletController.class);
	@Autowired
	PayoutMerchant payoutMerchant;
	
	@Autowired
	WalletService walletService;

	

	@PostMapping(value = "/merchant/transactionFilter")
	public ResponseEntity<?> transactionFilter(@RequestBody TransactionFilterReq dto)
			throws  ValidationExceptions, ParseException{
		logger.info("transactionReportWithFilter");
		Object res = payoutMerchant.transactionFilter(dto);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping(value = "/merchant/walletFilter")
	public ResponseEntity<?> walletFilter(@RequestBody WalletFilterReq dto)
			throws  ValidationExceptions, ParseException {
		logger.info("walletReportWithFilter");
		Object res = payoutMerchant.walletReportFilter(dto);
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping(value = "/merchant/walletlist")
	public ResponseEntity<?> walletlist()
			throws ValidationExceptions{
		return ResponseEntity.ok().body(walletService.walletList());
	}
	
	@GetMapping(value = "/merchant/walletlist/{merchantid}")
	public ResponseEntity<?> walletlistByMerchant(@PathVariable String merchantid)
			throws ValidationExceptions {
		return ResponseEntity.ok().body(walletService.walletListByMerchantId(merchantid));
	}
	
	@GetMapping(value = "/merchant/mainWalletAssociation/{mainWalletid}")
	public ResponseEntity<?> walletlistBywallId(@PathVariable String mainWalletid)
			throws ValidationExceptions {
		return ResponseEntity.ok().body(walletService.walletListByMainWalletId(mainWalletid));
	}
	
//	@GetMapping(value = "/merchant/AlltransactionReport")
//	public ResponseEntity<?> transactionReport()
//			throws JsonProcessingException, ValidationExceptions, ParseException {
//		logger.info("transactionReportWithFilter");
//		Object res = payoutMerchant.AllTransactionReport();
//		return ResponseEntity.ok().body(res);
//	}
//	
//	@GetMapping(value = "/merchant/AllwalletReport")
//	public ResponseEntity<?> walletReport()
//			throws JsonProcessingException, ValidationExceptions, ParseException {
//		logger.info("walletReportWithFilter");
//		Object res = payoutMerchant.AllWalletReport();
//		return ResponseEntity.ok().body(res);
//	}
}
