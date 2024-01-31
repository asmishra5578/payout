package io.asktech.payout.wallet.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.BalanceCheckMerRes;
import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.mainwallet.dto.MainWalletCreateReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletCreateResDto;
import io.asktech.payout.mainwallet.dto.MainWalletRechargeReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletRechargeResDto;
import io.asktech.payout.mainwallet.dto.MainWalletReversalReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletReversalResDto;
import io.asktech.payout.modal.admin.MerchantRecharge;
import io.asktech.payout.service.PayoutMerchant;
import io.asktech.payout.wallet.dto.WalletCreateReqDto;
import io.asktech.payout.wallet.dto.WalletRechargeReqDto;
import io.asktech.payout.wallet.dto.WalletRechargeRequest;
import io.asktech.payout.wallet.dto.WalletRechargeResDto;
import io.asktech.payout.wallet.dto.WalletReversalReqDto;
import io.asktech.payout.wallet.dto.WalletReversalResDto;
import io.asktech.payout.wallet.dto.WalletUpdateReqDto;
import io.asktech.payout.wallet.modal.MainWalletDetails;
import io.asktech.payout.wallet.service.MainWalletService;
import io.asktech.payout.wallet.service.WalletManagement;
import io.asktech.payout.wallet.service.WalletService;

@RestController
@RequestMapping("/admin/wallet")
public class WalletControllerAdmin {

	@Autowired
	MainWalletService mainWalletService;

	@Autowired
	WalletService walletService;

	@Autowired
	PayoutMerchant payoutMerchant;

	static Logger logger = LoggerFactory.getLogger(WalletControllerAdmin.class);

	@PostMapping(value = "/mainWalletCreation")
	public ResponseEntity<?> mainWalletRecharge(@RequestBody MainWalletCreateReqDto dto)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		if (dto != null) {
			MainWalletCreateResDto mainWalletCreateResDto = mainWalletService.walletCreate(dto);
			return ResponseEntity.ok().body(mainWalletCreateResDto);
		}
		return null;

	}

	@PutMapping(value = "/updateMainWalletStatus/{walletid}")
	public ResponseEntity<?> updateMainWalletStatus(@RequestBody MainWalletCreateReqDto dto,
			@PathVariable String walletid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		if (dto != null) {
			MainWalletDetails mainWalletCreateResDto = mainWalletService.updateWalletStatus(dto, walletid);
			return ResponseEntity.ok().body(mainWalletCreateResDto);
		}
		return null;

	}

	@PostMapping(value = "/mainWalletRecharge/{walletid}")
	public ResponseEntity<?> mainWalletRecharge(@RequestBody MainWalletRechargeReqDto dto,
			@PathVariable String walletid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		if (dto != null) {
			MainWalletRechargeResDto mainWalletRechargeResDto = mainWalletService.walletAddBalance(dto, walletid);
			return ResponseEntity.ok().body(mainWalletRechargeResDto);
		}
		return null;

	}

	@PostMapping(value = "/mainWalletReversal/{walletid}")
	public ResponseEntity<?> mainWalletReversal(@RequestBody MainWalletReversalReqDto dto,
			@PathVariable String walletid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		if (dto != null) {
			MainWalletReversalResDto mainWalletReversalResDto = mainWalletService.walletDeductBalance(dto, walletid);
			return ResponseEntity.ok().body(mainWalletReversalResDto);
		}
		return null;
	}

	@GetMapping(value = "/mainWalletTransaction/{walletid}")
	public ResponseEntity<?> mainWalletTransaction(@RequestBody WalletTransferMerReq dto,
			@PathVariable String merchantid) throws ValidationExceptions, JsonProcessingException, ParseException {
		return null;
	}

	@GetMapping(value = "/mainWalletBalance/{walletid}")
	public ResponseEntity<?> mainWalletBalance(@PathVariable String walletid)
			throws ValidationExceptions{
		return ResponseEntity.ok().body(mainWalletService.getWalletDetails(walletid));
	}

	@GetMapping(value = "/mainWalletBalanceByMerchantId/{merchantid}")
	public ResponseEntity<?> mainWalletBalanceByMerchantId(@PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		return ResponseEntity.ok().body(mainWalletService.mainWalletBalanceByMerchantId(merchantid));
	}

	@GetMapping(value = "/mainWalletList")
	public ResponseEntity<?> mainWalletList()
			throws ValidationExceptions, JsonProcessingException, ParseException {
		return ResponseEntity.ok().body(mainWalletService.getMainWalletList());

	}

	@PostMapping(value = "/walletRecharge/{merchantid}")
	public ResponseEntity<?> walletRecharge(@RequestBody WalletRechargeReqDto dto, @PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		return ResponseEntity.ok().body(walletService.walletAddBalance(dto, merchantid));

	}

	@PostMapping(value = "/walletCreation/{merchantid}")
	public ResponseEntity<?> walletCreation(@RequestBody WalletCreateReqDto dto, @PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		return ResponseEntity.ok().body(walletService.walletCreate(dto, merchantid));

	}

	@PostMapping(value = "/walletUpdate/{merchantid}")
	public ResponseEntity<?> walletUpdate(@RequestBody WalletUpdateReqDto dto, @PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		return ResponseEntity.ok().body(walletService.walletUpdate(dto, merchantid));

	}
	//Update Status and hold amount
	@PutMapping(value="/walletUpdateStatusAndHoldAmount")
	public ResponseEntity<?> walletUpdateStatusAndHoldAmount( @RequestBody WalletUpdateReqDto walletUpdateReqDto)
	throws ValidationExceptions {
		return ResponseEntity.ok().body(walletService.walletUpdateStatusAndHoldAmount(walletUpdateReqDto));
	}


	@PostMapping(value = "/walletRefund/{merchantid}")
	public ResponseEntity<?> walletRefund(@RequestBody WalletRechargeReqDto dto, @PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		return ResponseEntity.ok().body(walletService.walletRefundBalance(dto, merchantid));

	}

	@PostMapping(value = "/walletReversal/{merchantid}")
	public ResponseEntity<?> walletReversal(@RequestBody WalletReversalReqDto dto, @PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		WalletReversalResDto walletReversalResDto = new WalletReversalResDto();
		WalletRechargeResDto walletDeduction = walletService.walletDeductBalance(dto, merchantid);
		walletReversalResDto.setAmount(walletDeduction.getAmount());
		walletReversalResDto.setClosingBalance(walletDeduction.getClosingBalance());
		walletReversalResDto.setPurpose(dto.getPurpose());
		walletReversalResDto.setTransactionId(walletDeduction.getTransactionId());
		walletReversalResDto.setCredit_debit("DEBIT");
		walletReversalResDto.setRemark(dto.getRemarks());
		walletReversalResDto.setMainWalletId(walletDeduction.getMainWalletId());
		if (walletDeduction.getStatus().equals("SUCCESS")) {
			MainWalletRechargeReqDto mainDto = new MainWalletRechargeReqDto();
			mainDto.setAmount(walletDeduction.getAmount());
			mainDto.setPurpose("WALLET REVERSAL");
			mainDto.setRemarks(dto.getRemarks());
			mainDto.setTransactionType(walletReversalResDto.getTransactionId());
			MainWalletRechargeResDto mainWalletRechargeResDto = mainWalletService.walletAddBalance(mainDto,
					walletDeduction.getMainWalletId());
			if (mainWalletRechargeResDto.getStatus().equals("SUCCESS")) {
				walletReversalResDto.setStatus("SUCCESS");
				return ResponseEntity.ok().body(walletReversalResDto);
			} else {
				walletReversalResDto.setStatus(mainWalletRechargeResDto.getStatus());
				walletReversalResDto.setRemark(mainWalletRechargeResDto.getRemark());
				return ResponseEntity.ok().body(walletReversalResDto);
			}
		} else {
			walletReversalResDto.setStatus(walletDeduction.getStatus());
			walletReversalResDto.setRemark(walletDeduction.getRemark());
			return ResponseEntity.ok().body(walletReversalResDto);
		}

	}

	@GetMapping(value = "/walletTransaction/{merchantid}")
	public ResponseEntity<?> walletTransaction(@RequestBody TransactionReportMerReq dto,
			@PathVariable String merchantid)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		return ResponseEntity.ok().body(payoutMerchant.walletReport(dto, merchantid));
	}

	////// get y merchantId
	@GetMapping(value = "/walletBalance/{merchantid}")
	public ResponseEntity<?> walletBalance(@PathVariable String merchantid)
			throws ValidationExceptions  {
		BalanceCheckMerRes balanceCheckMerRes = payoutMerchant.balanceCheck(merchantid);
		return ResponseEntity.ok().body(balanceCheckMerRes);

	}

	@RequestMapping(value = "/weebhookTest", method = RequestMethod.POST)
	public ResponseEntity<?> weebhookTest(@RequestBody String responseFormData)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		logger.info("Mock Call Back URI for Merchant:: " + responseFormData.toString());

		return ResponseEntity.ok().body(responseFormData.toString());
	}

	@Autowired
	WalletManagement walletManagement;

	@PostMapping(value = "/rechargeRequest")
	public ResponseEntity<?> rechargeRequest(@RequestBody WalletRechargeRequest dto)
			throws ValidationExceptions, ParseException, JsonProcessingException {
		return ResponseEntity.ok().body(walletManagement.walletRecharge(dto));

	}

	@GetMapping(value = "/MerchantRechargeByMerchantId/{merchantid}")
	public List<MerchantRecharge> getByMerchantRechargeByMerchantId(@PathVariable String merchantid) {
		return walletManagement.getByMerchantRechargeByMerchantId(merchantid);
	}

	@GetMapping(value = "/getAllByMerchantRecharge")
	public List<MerchantRecharge> getAllByMerchantRecharge() {
		return walletManagement.getAllByMerchantRecharge();
	}

	@GetMapping(value = "/MerchantRechargeByUtrid/{utrid}")
	public MerchantRecharge getByMerchantRechargeByUtrid(@PathVariable String utrid) {
		return walletManagement.getByMerchantRechargeByUtrid(utrid);
	}

	@GetMapping(value = "/getMerchantRechargeDateRange/{dateFrom}/{dateTo}")
	public List<MerchantRecharge> getMerchantRechargeDateRange(@PathVariable String dateFrom,
			@PathVariable String dateTo) {
		return walletManagement.getMerchantRechargeDateRange(dateFrom, dateTo);
	}
	//
	// @PostMapping(value = "/transactionServiceCommission")
	// public ResponseEntity<?> transactionCommission(@RequestBody
	// TransactionChargeCommission dto)
	// throws ValidationExceptions, JsonProcessingException, ParseException {
	// return ResponseEntity.ok().body(walletService.transactionCommission(dto));
	//
	// }
	//
	// @GetMapping(value = "/getTransactionCommissionList")
	// public ResponseEntity<?> getTransactionCommissionList()
	// throws ValidationExceptions, JsonProcessingException, ParseException {
	// return ResponseEntity.ok().body(walletService.getTransactionComList());
	//
	// }
	//
	// @GetMapping(value = "/getRechargeCommissionList")
	// public ResponseEntity<?> getRechargeCommissionList()
	// throws ValidationExceptions, JsonProcessingException, ParseException {
	// return ResponseEntity.ok().body(walletService.getRechargeComList());
	//
	// }
	//
	// @GetMapping(value = "/getTransactionCommissionListMerchantwise/{merchantid}")
	// public ResponseEntity<?>
	// getTransactionCommissionListMerchantwise(@PathVariable String merchantid)
	// throws ValidationExceptions, JsonProcessingException, ParseException {
	// return
	// ResponseEntity.ok().body(walletService.getTransactionComListMerchantwise(merchantid));
	//
	// }
	//
	// @GetMapping(value = "/getRechargeCommissionListMerchantwise/{merchantid}")
	// public ResponseEntity<?> getRechargeCommissionListMerchantwise(@PathVariable
	// String merchantid)
	// throws ValidationExceptions, JsonProcessingException, ParseException {
	// return
	// ResponseEntity.ok().body(walletService.getRechargeComListMerchantwise(merchantid));
	//
	// }
	//
	// @PutMapping(value = "/updateTransactionServiceCommission")
	// public ResponseEntity<?> updateTransactionServiceCom(@RequestBody
	// TransactionChargeCommission dto)
	// throws ValidationExceptions, JsonProcessingException, ParseException {
	// return
	// ResponseEntity.ok().body(walletService.updateTransactionCommission(dto));
	//
	// }
	//
	// @PutMapping(value = "/updateRechargeServiceCommission")
	// public ResponseEntity<?> updateRechargeServiceCommission(@RequestBody
	// RechargeServiceCommission dto)
	// throws ValidationExceptions, JsonProcessingException, ParseException {
	// return ResponseEntity.ok().body(walletService.updateRechargeCommission(dto));
	//
	// }
	//
	//// @GetMapping(value = "/rechargeServiceCommissionfunction")
	//// public ResponseEntity<?> rechargeCommissionfunction(@RequestParam String
	// amount, @RequestParam String merchantid)
	//// throws ValidationExceptions, JsonProcessingException, ParseException {
	//// return ResponseEntity.ok().body(walletService.commCreditAmt(amount,
	// merchantid));
	////
	//// }
	////
	//// @GetMapping(value = "/transactionServiceCommissionfunction")
	//// public ResponseEntity<?> transactionCommissionfunction(@RequestParam String
	// amount,@RequestParam String type, @RequestParam String gateway, @RequestParam
	// String merchantid)
	//// throws ValidationExceptions, JsonProcessingException, ParseException {
	//// return ResponseEntity.ok().body(walletService.commDeductionAmt(amount,
	// type, gateway, merchantid));
	////
	//// }

}
