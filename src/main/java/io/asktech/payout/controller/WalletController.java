package io.asktech.payout.controller;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.admin.UpdateTransactionDetailsRequestDto;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferMerRes;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.BalanceCheckMerRes;
import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.dto.merchant.TransactionReportMerRes;
import io.asktech.payout.dto.merchant.TransactionRequestFilterMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.dto.merchant.WalletTransferMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.service.PayoutMerchant;
import io.asktech.payout.utils.Utility;

@RestController
public class WalletController {

	static Logger logger = LoggerFactory.getLogger(WalletController.class);
	@Autowired
	PayoutMerchant payoutMerchant;

	@PostMapping(value = "/merchant/walletTransfer/{merchantid}")
	public ResponseEntity<?> WalletTransfer(@RequestBody WalletTransferMerReq dto, @PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		logger.info("Insde Payout WalletTransfer () :: ");

		WalletTransferMerRes walletTransferMerRes = payoutMerchant.walletTransfer(dto, merchantid);
		logger.info("Response from  WalletTransfer :: " + Utility.convertDTO2JsonString(walletTransferMerRes));
		return ResponseEntity.ok().body(walletTransferMerRes);
	}

	@PostMapping(value = "/merchant/accountTransfer/{merchantid}")
	public ResponseEntity<?> accountTransfer(@RequestBody AccountTransferMerReq dto, @PathVariable String merchantid)

			throws JsonProcessingException, ValidationExceptions, ParseException {
		AccountTransferMerRes accountTransferMerRes = payoutMerchant.accountTransfer(dto, merchantid);
		logger.info("Wallet controller AccountTransfer:" + Utility.convertDTO2JsonString(accountTransferMerRes));
		return ResponseEntity.ok().body(accountTransferMerRes);
	}

	@PostMapping(value = "/merchant/bulk/accountTransfer/{merchantid}")
	public void accountTransferUsingBulkFile(@RequestBody List<AccountTransferMerReq> dto, @PathVariable String merchantid)
			throws JsonProcessingException, ValidationExceptions, ParseException {
		logger.info("Wallet controller AccountTransfer:" + dto.toString());
		payoutMerchant.accountTransferUsingBulkFile(dto, merchantid);
	}

	@GetMapping(value = "/merchant/balanceCheck/{merchantid}")
	public ResponseEntity<?> balanceCheck(@PathVariable String merchantid)
			throws JsonProcessingException, ValidationExceptions {
		BalanceCheckMerRes balanceCheckMerRes = payoutMerchant.balanceCheck(merchantid);
		return ResponseEntity.ok().body(balanceCheckMerRes);
	}

	
	@PostMapping(value = "/merchant/transactionReport/{merchantid}")
	public ResponseEntity<?> transactionReport(@PathVariable String merchantid,
			@RequestBody TransactionReportMerReq dto) throws JsonProcessingException, ValidationExceptions {
		TransactionReportMerRes transactionReportMerRes = payoutMerchant.transactionReport(dto, merchantid);
		return ResponseEntity.ok().body(transactionReportMerRes);
	}

	@PostMapping(value = "/merchant/transactionStatus/{merchantid}")
	public ResponseEntity<?> getStatus(@PathVariable String merchantid, @RequestBody TransferStatusReq dto)
			throws ValidationExceptions, JsonProcessingException {
		TransactionResponseMerRes transactionResponseMerRes = payoutMerchant.transactionStatus(dto, merchantid);
		return ResponseEntity.ok().body(transactionResponseMerRes);
	}

	@PostMapping(value = "/merchant/accountTransferUPI/{merchantid}")
	public ResponseEntity<?> accountTransferUPI(@RequestBody AccountTransferUPIMerReq dto,
			@PathVariable String merchantid) throws JsonProcessingException, ValidationExceptions, ParseException {
		logger.info("accountTransferUPI");
		AccountTransferMerRes accountTransferMerRes = null;
		accountTransferMerRes = payoutMerchant.accountTransferUPI(dto, merchantid);
		return ResponseEntity.ok().body(accountTransferMerRes);
	}

	@PostMapping(value = "/merchant/bulk/accountTransferUPI/{merchantid}")
	public void accountTransferUPIUsingBulkFile(@RequestBody List<AccountTransferUPIMerReq> dto,
			@PathVariable String merchantid) throws JsonProcessingException, ValidationExceptions, ParseException {
		logger.info("accountTransferUPI" + dto.toString());
		payoutMerchant.accountTransferUPIUsingBulkFile(dto, merchantid);
	}

	
	@PostMapping(value = "/merchant/transactionReportWithFilter/{merchantid}")
	public ResponseEntity<?> transactionReportWithFilters(@PathVariable String merchantid,
			@RequestBody TransactionRequestFilterMerReq dto)
			throws JsonProcessingException, ValidationExceptions, ParseException {
		logger.info("transactionReportWithFilter");
		Object transactionReportMerRes = payoutMerchant.transactionReportwithFilter(dto, merchantid);
		return ResponseEntity.ok().body(transactionReportMerRes);
	}
	
	@PostMapping(value = "/merchant/walletReport/{merchantid}")
	public ResponseEntity<?> walletReport(@PathVariable String merchantid,
			@RequestBody TransactionReportMerReq dto)
			throws JsonProcessingException, ValidationExceptions, ParseException {
		logger.info("walletReport");
		Object transactionReportMerRes = payoutMerchant.walletReport(dto, merchantid);
		return ResponseEntity.ok().body(transactionReportMerRes);
	}
	
	
//	@GetMapping(value = "/merchant/walletReport7days/{merchantid}")
//	public ResponseEntity<?> walletReport7Days(@PathVariable String merchantid)
//			throws JsonProcessingException, ValidationExceptions, ParseException {
//		logger.info("walletReport");
//		Object transactionReportMerRes = payoutMerchant.wallet7DaysReport( merchantid);
//		return ResponseEntity.ok().body(transactionReportMerRes);
//	}

	
	/*
	 * @PostMapping(value = "/merchant/accountVerify") public ResponseEntity<?>
	 * accountVerify(@RequestBody BankAccountVerifyRequest dto, @PathVariable String
	 * merchantid) throws JsonProcessingException, ValidationExceptions,
	 * ParseException { BankAccountVerfiyResponse bankAccountVerfiyResponse =
	 * payoutMerchant.bankAccountVerify(dto); return
	 * ResponseEntity.ok().body(bankAccountVerfiyResponse); }
	 */
}
