package io.asktech.payout.van.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.service.PayoutWalletAdminService;

@RestController
public class PayoutWalletAdminController {
	
	@Autowired
	PayoutWalletAdminService payoutadminwalletservice;
	
	@PostMapping(value = "/user/transfertowallet")
	public ResponseEntity<?> TransferCreate(@RequestBody WalletTransferReq dto) throws 
			Exception {
		

		WalletTransferRes transferres = payoutadminwalletservice.getWalletTransfer(dto);

		return ResponseEntity.ok().body(transferres);
	}
	
	@PostMapping(value = "/user/transferstatuscheck")
	public ResponseEntity<?> TransferStatus(@RequestBody TransferStatusReq dto) throws 
			Exception {
		

		TransactionResponseMerRes statusres = payoutadminwalletservice.getTransferStatus(dto, null);

		return ResponseEntity.ok().body(statusres);
	}
	/*
	@PostMapping(value = "/user/transferfundstodis")
	public ResponseEntity<?> TransferFunds(@RequestBody TransferFundsReq dto) throws 
			Exception {
		

		TransferFundsRes fundsres = payoutadminwalletservice.getTransferFunds(dto);

		return ResponseEntity.ok().body(fundsres);
	}
	*/

}
