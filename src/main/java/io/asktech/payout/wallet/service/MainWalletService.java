package io.asktech.payout.wallet.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.mainwallet.dto.MainWalletCreateReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletCreateResDto;
import io.asktech.payout.mainwallet.dto.MainWalletRechargeReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletRechargeResDto;
import io.asktech.payout.mainwallet.dto.MainWalletReversalReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletReversalResDto;
import io.asktech.payout.mainwallet.dto.MainWalletTransactionReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletUpdateReqDto;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.wallet.modal.MainWalletDetails;
import io.asktech.payout.wallet.modal.MainWalletTransactions;
import io.asktech.payout.wallet.modal.WalletDetails;
import io.asktech.payout.wallet.repository.MainWalletDetailsRepo;
import io.asktech.payout.wallet.repository.MainWalletTransactionsRepo;
import io.asktech.payout.wallet.repository.WalletDetailsRepo;

@Service
public class MainWalletService {
	static Logger logger = LoggerFactory.getLogger(MainWalletService.class);

	@Autowired
	MainWalletDetailsRepo mainWalletDetailsRepo;

	@Autowired
	MainWalletTransactionsRepo mainWalletTransactionsRepo;
	@Autowired
	private WalletDetailsRepo walletDetailsRepo;

	public MainWalletCreateResDto walletCreate(MainWalletCreateReqDto walletCreateReqDto) {
		String amount = "0.00";
		MainWalletDetails mainWalletDetails = new MainWalletDetails();
		// TODO Name Validation
		mainWalletDetails.setName(walletCreateReqDto.getName());
		// TODO Status Validation with Enum
		mainWalletDetails.setStatus(walletCreateReqDto.getStatus());
		String walletid = UUID.randomUUID().toString();
		mainWalletDetails.setWalletid(walletid);
		if (walletCreateReqDto.getAmount() != null) {
			amount = walletCreateReqDto.getAmount();
		}
		// TODO Amount Validation
		mainWalletDetails.setAmount(amount);
		mainWalletDetailsRepo.save(mainWalletDetails);
		MainWalletCreateResDto mainWalletCreateResDto = new MainWalletCreateResDto();
		mainWalletCreateResDto.setWalletId(walletid);
		mainWalletCreateResDto.setWalletStatus(mainWalletDetails.getStatus());
		mainWalletCreateResDto.setResponseStatus("SUCCESS");
		mainWalletCreateResDto.setName(mainWalletDetails.getName());
		mainWalletCreateResDto.setAmount(amount);
		return mainWalletCreateResDto;

	}

	public MainWalletDetails updateWalletStatus(MainWalletCreateReqDto walletCreateReqDto, String walletid)
			throws ValidationExceptions {
		MainWalletDetails mainWalletDetails = mainWalletDetailsRepo.findByWalletid(walletid);
		if (mainWalletDetails == null) {
			throw new ValidationExceptions("WALLET NOT FOUND", FormValidationExceptionEnums.WALLET_NOT_FOUND);
		}

		// if (mainWalletDetails.getStatus().equalsIgnoreCase("BLOCKED")) {
		// throw new ValidationExceptions("Wallet has been BLOCKED! Contact
		// administrator.", FormValidationExceptionEnums.WALLET_STATUS_ERROR);
		// }

		mainWalletDetails.setStatus(walletCreateReqDto.getStatus());

		return mainWalletDetailsRepo.save(mainWalletDetails);

	}

	public String walletUpdate(MainWalletUpdateReqDto walletUpdateReqDto, String walletId) {

		return null;
	}

	public MainWalletRechargeResDto walletAddBalance(MainWalletRechargeReqDto walletRechargeReqDto, String walletId)
			throws ValidationExceptions {
		MainWalletDetails mainWalletDetails = checkMainWalletExists(walletId);
		MainWalletTransactions mainWalletTransactions = new MainWalletTransactions();
		mainWalletTransactions.setOpeningBalance(Utility.doubleFormat(mainWalletDetails.getAmount()));
		HashMap<String, String> trResult = walletTransaction(mainWalletDetails, walletRechargeReqDto.getAmount(),
				"CREDIT");
		String TotalAmt = trResult.get("TotalAmt");
		mainWalletTransactions.setTransactionType(walletRechargeReqDto.getTransactionType());

		mainWalletTransactions.setAmount(Utility.doubleFormat(walletRechargeReqDto.getAmount()));
		mainWalletTransactions.setClosingBalance(Utility.doubleFormat(TotalAmt));
		mainWalletTransactions.setCredit_debit("CREDIT");
		String trid = UUID.randomUUID().toString().replace("-", "");
		mainWalletTransactions.setTransactionId(trid);
		mainWalletTransactions.setWalletid(walletId);
		mainWalletTransactions.setRemarks(walletRechargeReqDto.getRemarks());
		mainWalletTransactions.setPurpose(walletRechargeReqDto.getPurpose());
		mainWalletTransactions.setTransactionStatus(trResult.get("status"));
		mainWalletTransactions.setStatusRemarks(trResult.get("remark"));
		mainWalletTransactionsRepo.save(mainWalletTransactions);
		MainWalletRechargeResDto mainWalletRechargeResDto = new MainWalletRechargeResDto();
		mainWalletRechargeResDto.setAmount(walletRechargeReqDto.getAmount());
		mainWalletRechargeResDto.setStatus(trResult.get("status"));
		mainWalletRechargeResDto.setPurpose(walletRechargeReqDto.getPurpose());
		mainWalletRechargeResDto.setCredit_debit("CREDIT");
		mainWalletRechargeResDto.setTransactionId(trid);
		mainWalletRechargeResDto.setClosingBalance(Utility.doubleFormat(TotalAmt));
		mainWalletRechargeResDto.setRemark(trResult.get("remark"));
		return mainWalletRechargeResDto;
	}

	public MainWalletReversalResDto walletDeductBalance(MainWalletReversalReqDto dto, String walletId)
			throws ValidationExceptions {
		MainWalletDetails mainWalletDetails = checkMainWalletExists(walletId);
		MainWalletTransactions mainWalletTransactions = new MainWalletTransactions();
		mainWalletTransactions.setOpeningBalance(Utility.doubleFormat(mainWalletDetails.getAmount()));
		HashMap<String, String> trResult = walletTransaction(mainWalletDetails, dto.getAmount(), "DEBIT");
		String TotalAmt = trResult.get("TotalAmt");
		mainWalletTransactions.setTransactionType(dto.getTransactionType());
		mainWalletTransactions.setAmount(Utility.doubleFormat(dto.getAmount()));
		mainWalletTransactions.setClosingBalance(Utility.doubleFormat(TotalAmt));
		mainWalletTransactions.setCredit_debit("DEBIT");
		String trid = UUID.randomUUID().toString().replace("-", "");
		mainWalletTransactions.setTransactionId(trid);
		mainWalletTransactions.setWalletid(walletId);
		mainWalletTransactions.setRemarks(dto.getRemarks());
		mainWalletTransactions.setPurpose(dto.getPurpose());
		mainWalletTransactions.setTransactionStatus(trResult.get("status"));
		mainWalletTransactions.setStatusRemarks(trResult.get("remark"));
		mainWalletTransactionsRepo.save(mainWalletTransactions);
		MainWalletReversalResDto mainWalletReversalResDto = new MainWalletReversalResDto();
		mainWalletReversalResDto.setAmount(Utility.doubleFormat(dto.getAmount()));
		mainWalletReversalResDto.setStatus(trResult.get("status"));
		mainWalletReversalResDto.setPurpose(dto.getPurpose());
		mainWalletReversalResDto.setCredit_debit("DEBIT");
		mainWalletReversalResDto.setTransactionId(trid);
		mainWalletReversalResDto.setClosingBalance(Utility.doubleFormat(TotalAmt));
		mainWalletReversalResDto.setRemark(trResult.get("remark"));
		return mainWalletReversalResDto;

	}

	public MainWalletDetails mainWalletBalanceByMerchantId(String merchantId) {
		WalletDetails wallet = walletDetailsRepo.findByMerchantid(merchantId);
		MainWalletDetails mDetails = mainWalletDetailsRepo.findByWalletid(wallet.getMainWalletid());
		return mDetails;
	}

	public MainWalletDetails getWalletDetails(String walletid) throws ValidationExceptions {
		return checkMainWalletExists(walletid);
	}

	public List<MainWalletDetails> getMainWalletList() throws ValidationExceptions {
		return mainWalletDetailsRepo.findAll();
	}

	public String getWalletTransactions(MainWalletTransactionReqDto walletTransactionReqDto, String merchantId) {
		return null;
	}

	public String walletRefundBalance(MainWalletRechargeReqDto walletRechargeReqDto, String merchantId) {
		return null;
	}

	private MainWalletDetails checkMainWalletExists(String mainWalletid) throws ValidationExceptions {
		MainWalletDetails mainWalletDetails = null;
		DecimalFormat df = new DecimalFormat("#.##");

		try {
			mainWalletDetails = mainWalletDetailsRepo.findByWalletid(mainWalletid);
		} catch (Exception e) {
			logger.info("Wallet Exception");
			throw new ValidationExceptions("WALLET NOT FOUND", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		if (mainWalletDetails == null) {
			logger.info("Wallet Not Found");
			throw new ValidationExceptions("WALLET NOT FOUND", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}

		MainWalletDetails res = new MainWalletDetails();
		res.setCreated(mainWalletDetails.getCreated());
		res.setUpdated(mainWalletDetails.getUpdated());
		res.setId(mainWalletDetails.getId());
		res.setName(mainWalletDetails.getName());
		res.setStatus(mainWalletDetails.getStatus());
		res.setWalletid(mainWalletDetails.getWalletid());
		res.setAmount(String.valueOf(df.format(Double.parseDouble(mainWalletDetails.getAmount()))));

		return res;
	}

	public boolean checkMainWalletBalanceExists(String mainWalletid, String amount) throws ValidationExceptions {
		MainWalletDetails mainWalletDetails = checkMainWalletExists(mainWalletid);
		Double walletbal = Double.parseDouble(mainWalletDetails.getAmount());
		Double amt = Double.parseDouble(amount);
		Double deducted = walletbal - amt;
		if (deducted >= 0) {
			return true;
		}
		return false;
	}

	private HashMap<String, String> walletTransaction(MainWalletDetails mainWalletDetails, String amt,
			String transactionType) {
		if (mainWalletDetails.getStatus().equals("ACTIVE")) {
			HashMap<String, String> map = new HashMap<String, String>();
			Double amount = Double.parseDouble(mainWalletDetails.getAmount());
			if (Double.parseDouble(amt) < 1.00) {
				map.put("status", "FAILED");
				map.put("remark", "Invalid Amount. Cannot be less than 1");
				map.put("TotalAmt", mainWalletDetails.getAmount());
				return map;
			}
			if (transactionType.equals("CREDIT")) {
				String TotalAmt = String.valueOf(Double.parseDouble(amt) + amount);
				mainWalletDetails.setAmount(Utility.doubleFormat((TotalAmt)));
				mainWalletDetailsRepo.save(mainWalletDetails);
				map.put("status", "SUCCESS");
				map.put("TotalAmt", TotalAmt);
				map.put("remark", "Credit Success");
				return map;
			} else if (transactionType.equals("DEBIT")) {
				Double am = amount - Double.parseDouble(amt);
				String TotalAmt = String.valueOf(am);
				if (am >= 0) {
					mainWalletDetails.setAmount(Utility.doubleFormat(TotalAmt));
					mainWalletDetailsRepo.save(mainWalletDetails);
					map.put("status", "SUCCESS");
					map.put("remark", "Debit Success");
					map.put("TotalAmt", TotalAmt);
				} else {
					map.put("status", "FAILED");
					map.put("remark", "Low Balance");
					map.put("TotalAmt", mainWalletDetails.getAmount());
				}

				return map;
			}
			map.put("status", "FAILED");
			map.put("remark", "Invalid Wallet");
			map.put("TotalAmt", Utility.doubleFormat(mainWalletDetails.getAmount()));
			return map;
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("status", "FAILED");
			map.put("remark", "Wallet is Inactive");
			map.put("TotalAmt", Utility.doubleFormat(mainWalletDetails.getAmount()));
			return map;
		}
	}
}
