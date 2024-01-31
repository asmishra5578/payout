package io.asktech.payout.wallet.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.utility.SuccessResponseDto;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.enums.SuccessCode;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.mainwallet.dto.MainWalletReversalReqDto;
import io.asktech.payout.mainwallet.dto.MainWalletReversalResDto;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.wallet.dto.TrDetails;
import io.asktech.payout.wallet.dto.WalletCreateReqDto;
import io.asktech.payout.wallet.dto.WalletCreateResDto;
import io.asktech.payout.wallet.dto.WalletRechargeReqDto;
import io.asktech.payout.wallet.dto.WalletRechargeResDto;
import io.asktech.payout.wallet.dto.WalletReversalReqDto;
import io.asktech.payout.wallet.dto.WalletUpdateReqDto;
import io.asktech.payout.wallet.dto.WalletUpdateResDto;
import io.asktech.payout.wallet.modal.WalletDetails;
import io.asktech.payout.wallet.repository.RechargeServiceRepository;
import io.asktech.payout.wallet.repository.TransactionChargeRepository;
import io.asktech.payout.wallet.repository.WalletDetailsRepo;
import io.asktech.payout.wallet.repository.WalletTransactionsRepo;

@Service
public class WalletService {
	static Logger logger = LoggerFactory.getLogger(WalletService.class);

	@Autowired
	WalletDetailsRepo walletDetailsRepo;

	@Autowired
	TransactionChargeRepository transactionChargeRepository;
	@Autowired
	RechargeServiceRepository rechargeServiceRepository;

	@Autowired
	WalletTransactionsRepo walletTransactionsRepo;

	@Autowired
	MainWalletService mainWalletService;

	@Autowired
	UpdateWallet updateWallet;

	ObjectMapper mapper = new ObjectMapper();

	public WalletCreateResDto walletCreate(WalletCreateReqDto walletCreateReqDto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		String amount = "0.00";
		if (checkWalletExists(merchantId) != null) {
			throw new ValidationExceptions("WALLET Already Exists", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		WalletDetails walletDetails = new WalletDetails();
		// TODO Name Validation
		walletDetails.setName(walletCreateReqDto.getName());
		// TODO Status Validation with Enum
		walletDetails.setStatus(walletCreateReqDto.getStatus());
		String walletid = UUID.randomUUID().toString();
		walletDetails.setWalletid(walletid);
		if (mainWalletService.getWalletDetails(walletCreateReqDto.getMainWalletid()) == null) {
			logger.info("Wallet Not Found");
			throw new ValidationExceptions("WALLET NOT FOUND", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		walletDetails.setMainWalletid(walletCreateReqDto.getMainWalletid());
		walletDetails.setMerchantid(merchantId);
		walletDetails.setWalletCallBackAPI(walletCreateReqDto.getWalletCallBackAPI());

		walletDetails.setAmount(amount);
		walletDetails.setMerchantThreadFlag(Utility.getRandomNumberInRange(1, 10));
		walletDetailsRepo.save(walletDetails);
		if (walletCreateReqDto.getAmount() != null) {
			amount = walletCreateReqDto.getAmount();
		}
		// TODO Amount Validation
		if (Double.parseDouble(amount) > 0) {
			WalletRechargeReqDto walletRechargeReqDto = new WalletRechargeReqDto();
			walletRechargeReqDto.setAmount(amount);
			walletRechargeReqDto.setPurpose("WALLET CREATION");
			walletRechargeReqDto.setTransactionType("WALLET TRANSFER");
			walletRechargeReqDto.setRemarks("Wallet Creation Transfer");
			walletAddBalance(walletRechargeReqDto, merchantId);
		}
		WalletCreateResDto walletCreateResDto = new WalletCreateResDto();
		walletCreateResDto.setWalletId(walletid);
		walletCreateResDto.setWalletStatus(walletDetails.getStatus());
		walletCreateResDto.setResponseStatus("SUCCESS");
		walletCreateResDto.setName(walletDetails.getName());
		walletCreateResDto.setAmount(amount);
		walletCreateResDto.setMainWalletId(walletCreateReqDto.getMainWalletid());
		return walletCreateResDto;

	}

	public SuccessResponseDto walletList() {
		SuccessResponseDto sdto = new SuccessResponseDto();
		sdto.getMsg().add("Request Processed Successfully !");
		sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		sdto.getExtraData().put("walletlist", walletDetailsRepo.findAll());
		return sdto;
	}

	public WalletDetails walletListByMerchantId(String merchantid) {
		// SuccessResponseDto sdto = new SuccessResponseDto();
		// sdto.getMsg().add("Request Processed Successfully !");
		// sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		// sdto.getExtraData().put("walletlist",
		// walletDetailsRepo.findByMerchantid(merchantid));
		// return sdto;
		return walletDetailsRepo.findByMerchantid(merchantid);
	}

	public SuccessResponseDto walletListByMainWalletId(String mainWalletid) {
		SuccessResponseDto sdto = new SuccessResponseDto();
		sdto.getMsg().add("Request Processed Successfully !");
		sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		sdto.getExtraData().put("walletlist", walletDetailsRepo.findByMainWalletid(mainWalletid));
		return sdto;
	}

	public WalletUpdateResDto walletUpdate(WalletUpdateReqDto walletUpdateReqDto, String merchantId)
			throws ValidationExceptions {

		WalletDetails walletDetails = checkWalletExists(merchantId);

		if (walletDetails == null) {
			throw new ValidationExceptions("WALLET Not Exists", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		WalletUpdateResDto walletUpdateResDto = new WalletUpdateResDto();
		walletDetails.setWalletCallBackAPI(walletUpdateReqDto.getWalletCallBackAPI());
		walletDetails.setStatus(walletUpdateReqDto.getStatus());
			walletDetailsRepo.save(walletDetails);	
		walletUpdateResDto.setName(walletDetails.getName());
		walletUpdateResDto.setResponseStatus("SUCCESS");
		walletUpdateResDto.setWalletCallBackAPI(walletDetails.getWalletCallBackAPI());
		walletUpdateResDto.setWalletId(walletDetails.getWalletid());
		walletUpdateResDto.setWalletStatus(walletDetails.getStatus());

		return walletUpdateResDto;
	}

	public WalletUpdateResDto walletUpdateStatusAndHoldAmount(WalletUpdateReqDto walletUpdateReqDto)
	throws ValidationExceptions {
		WalletDetails walletDetails = new WalletDetails();
    walletDetails = checkWalletExists(walletUpdateReqDto.getMerchantId());

if (walletDetails == null) {
	throw new ValidationExceptions("WALLET Not Exists", FormValidationExceptionEnums.FATAL_EXCEPTION);
}
System.out.println("status befor "+walletDetails.getStatus());
WalletUpdateResDto walletUpdateResDto = new WalletUpdateResDto();
System.out.println(walletUpdateReqDto.getStatus()!=null);
System.out.println(walletUpdateReqDto.getStatus()+"  ssdto");
if (walletUpdateReqDto.getStatus()!=null) {

	walletDetails.setStatus(walletUpdateReqDto.getStatus());

	walletDetails=walletDetailsRepo.save(walletDetails);	
	System.out.println("status after "+walletDetails.getStatus());
walletUpdateResDto.setName(walletDetails.getName());
walletUpdateResDto.setResponseStatus("SUCCESS");
walletUpdateResDto.setWalletCallBackAPI(walletDetails.getWalletCallBackAPI());
walletUpdateResDto.setWalletId(walletDetails.getWalletid());
walletUpdateResDto.setWalletStatus(walletDetails.getStatus());
return walletUpdateResDto;
}
walletDetails.setWalletCallBackAPI(walletUpdateReqDto.getWalletCallBackAPI());
walletDetails.setInstantReversal(walletUpdateReqDto.getInstantReversal());
walletDetails.setWalletHoldAmount(walletUpdateReqDto.getWalletHoldAmount());
walletDetails=walletDetailsRepo.save(walletDetails);	
walletUpdateResDto.setName(walletDetails.getName());
walletUpdateResDto.setResponseStatus("SUCCESS");
walletUpdateResDto.setWalletCallBackAPI(walletDetails.getWalletCallBackAPI());
walletUpdateResDto.setWalletId(walletDetails.getWalletid());
walletUpdateResDto.setWalletStatus(walletDetails.getStatus());

return walletUpdateResDto;
}

	public WalletRechargeResDto walletAddBalance(WalletRechargeReqDto walletRechargeReqDto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("walletAddBalance ::" + Utility.convertDTO2JsonString(walletRechargeReqDto) + "|" + merchantId);
		WalletDetails walletDetails = checkWalletExists(merchantId);
		if (walletDetails == null) {
			throw new ValidationExceptions("WALLET NOT FOUND", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		DecimalFormat df = new DecimalFormat("#.##");
		TrDetails TrDetails = new TrDetails();
		TrDetails.setOpeningBalance(walletDetails.getAmount());
		HashMap<String, String> trResult = walletTransaction(walletDetails, walletRechargeReqDto.getAmount(), "CREDIT");
		String TotalAmt = trResult.get("TotalAmt");
		TrDetails.setClosingBalance(TotalAmt);
		TrDetails.setCreditDebit("CREDIT");
		TrDetails.setWalletId(walletDetails.getWalletid());
		TrDetails.setTransactionStatus(trResult.get("status"));
		TrDetails.setStatusRemarks(trResult.get("remark"));
		TrDetails.setMainWalletId(walletDetails.getMainWalletid());
		/*
		 * if (trResult.get("status").equals("SUCCESS")) {
		 * TrDetails.setReferenceId(trResult.get("refId")); }
		 */
		TrDetails.setReferenceId(walletRechargeReqDto.getReferenceId());
		updateWallet.insertTrasactionDetails(walletRechargeReqDto, merchantId, TrDetails);
		WalletRechargeResDto walletRechargeResDto = new WalletRechargeResDto();
		walletRechargeResDto.setAmount(String.valueOf(df.format(Double.parseDouble(walletRechargeReqDto.getAmount()))));
		walletRechargeResDto.setStatus(trResult.get("status"));
		walletRechargeResDto.setPurpose(walletRechargeReqDto.getPurpose());
		walletRechargeResDto.setCredit_debit("CREDIT");
		walletRechargeResDto.setClosingBalance(TotalAmt);
		walletRechargeResDto.setRemark(trResult.get("remark"));
		walletRechargeResDto.setMainWalletId(walletDetails.getMainWalletid());
		walletRechargeResDto.setCallBackURL(walletDetails.getWalletCallBackAPI());
		return walletRechargeResDto;
	}

	public WalletRechargeResDto walletDeductBalance(WalletReversalReqDto walletReversalReqDto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("WALLET DEDUCTION ::" + Utility.convertDTO2JsonString(walletReversalReqDto) + "|" + merchantId);
		WalletDetails walletDetails = checkWalletExists(merchantId);
		if (walletDetails == null) {
			throw new ValidationExceptions("WALLET NOT FOUND", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		DecimalFormat df = new DecimalFormat("#.##");
		TrDetails TrDetails = new TrDetails();
		TrDetails.setOpeningBalance(walletDetails.getAmount());
		HashMap<String, String> trResult = walletTransaction(walletDetails, walletReversalReqDto.getAmount(), "DEBIT");
		String TotalAmt = trResult.get("TotalAmt");
		TrDetails.setClosingBalance(TotalAmt);
		TrDetails.setCreditDebit("DEBIT");
		TrDetails.setWalletId(walletDetails.getWalletid());
		TrDetails.setTransactionStatus(trResult.get("status"));
		TrDetails.setStatusRemarks(trResult.get("remark"));
		TrDetails.setMainWalletId(walletDetails.getMainWalletid());
		TrDetails.setReferenceId(walletReversalReqDto.getReferenceId());
		logger.info(Utility.convertDTO2JsonString(TrDetails));
		updateWallet.insertTrasactionDetails(walletReversalReqDto, merchantId, TrDetails);
		WalletRechargeResDto walletRechargeResDto = new WalletRechargeResDto();
		walletRechargeResDto.setAmount(String.valueOf(df.format(Double.parseDouble(walletReversalReqDto.getAmount()))));
		walletRechargeResDto.setStatus(trResult.get("status"));
		walletRechargeResDto.setPurpose(walletReversalReqDto.getPurpose());
		walletRechargeResDto.setCredit_debit("DEBIT");
		walletRechargeResDto.setClosingBalance(TotalAmt);
		walletRechargeResDto.setRemark(trResult.get("remark"));
		walletRechargeResDto.setMainWalletId(walletDetails.getMainWalletid());
		walletRechargeResDto.setCallBackURL(walletDetails.getWalletCallBackAPI());
		return walletRechargeResDto;

	}

	public WalletDetails getWalletDetails(String merchantId) throws ValidationExceptions {
		return checkWalletExists(merchantId);

	}

	public WalletRechargeResDto walletRefundBalance(WalletRechargeReqDto walletRechargeReqDto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("REFUND ::" + Utility.convertDTO2JsonString(walletRechargeReqDto) + "|" + merchantId);
		WalletDetails walletDetails = checkWalletExists(merchantId);
		if (walletDetails == null) {
			throw new ValidationExceptions("WALLET NOT FOUND", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		DecimalFormat df = new DecimalFormat("#.##");
		TrDetails TrDetails = new TrDetails();
		TrDetails.setOpeningBalance(walletDetails.getAmount());
		HashMap<String, String> trResult = walletTransaction(walletDetails, walletRechargeReqDto.getAmount(), "REFUND");
		String TotalAmt = trResult.get("TotalAmt");
		TrDetails.setClosingBalance(TotalAmt);
		TrDetails.setCreditDebit("CREDIT");
		TrDetails.setWalletId(walletDetails.getWalletid());
		TrDetails.setTransactionStatus(trResult.get("status"));
		TrDetails.setStatusRemarks(trResult.get("remark"));
		TrDetails.setMainWalletId(walletDetails.getMainWalletid());
		TrDetails.setReferenceId(walletRechargeReqDto.getReferenceId());
		walletRechargeReqDto.setPurpose("REVERSAL");
		updateWallet.insertTrasactionDetails(walletRechargeReqDto, merchantId, TrDetails);
		WalletRechargeResDto walletRechargeResDto = new WalletRechargeResDto();
		walletRechargeResDto.setAmount(String.valueOf(df.format(Double.parseDouble(walletRechargeReqDto.getAmount()))));
		walletRechargeResDto.setStatus(trResult.get("status"));
		walletRechargeResDto.setPurpose("REVERSAL");
		walletRechargeResDto.setCredit_debit("CREDIT");
		walletRechargeResDto.setClosingBalance(TotalAmt);
		walletRechargeResDto.setRemark(trResult.get("remark"));
		walletRechargeResDto.setMainWalletId(walletDetails.getMainWalletid());
		walletRechargeResDto.setCallBackURL(walletDetails.getWalletCallBackAPI());
		return walletRechargeResDto;
	}

	public boolean checkWalletBalanceExists(String merchantid, String amount) throws ValidationExceptions {
		WalletDetails walletDetails = checkWalletExists(merchantid);
		if (walletDetails != null) {
			try {
				logger.debug("BAL AMT::"+amount+"|"+Utility.convertDTO2JsonString(walletDetails));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Double walletbal = Double.parseDouble(walletDetails.getAmount());
			Double amt = Double.parseDouble(amount);
			logger.debug("BAL AMT 1" + walletDetails.getAmount() );
			if (walletDetails.getWalletHoldAmount() != null) {
				logger.debug("BAL AMT 2"+walletDetails.getWalletHoldAmount());
				Double holdAmount = Double.parseDouble(walletDetails.getWalletHoldAmount());
				logger.debug("BAL AMT 2"+walletDetails.getWalletHoldAmount());
				if (walletbal <= holdAmount) {
					return false;
				}
			}
			logger.info("Balance Returned" + "  " + amt + "    " + walletbal);
			double deducted = walletbal - amt;
			logger.info("deducted:: " + deducted);
			if (deducted >= 0.0) {

				logger.info("deducted:: " + "True");
				return true;
			}
			return false;
		}
		return false;
	}

	private WalletDetails checkWalletExists(String merchantid) throws ValidationExceptions {
		WalletDetails walletDetails = null;
		try {
			walletDetails = walletDetailsRepo.findByMerchantid(merchantid);
		} catch (Exception e) {
			logger.info("Wallet Exception");
			throw new ValidationExceptions("WALLET ERROR", FormValidationExceptionEnums.FATAL_EXCEPTION);
		}
		return walletDetails;
	}

	private HashMap<String, String> walletTransaction(WalletDetails walletDetails, String amt, String transactionType)
			throws ValidationExceptions {
		DecimalFormat df = new DecimalFormat("#.##");
		if (walletDetails.getStatus().equals("ACTIVE")) {
			HashMap<String, String> map = new HashMap<>();
			Double amount = Double.parseDouble(walletDetails.getAmount());
			if (Double.parseDouble(amt) < 1.00) {
				map.put("status", "FAILED");
				map.put("remark", "Invalid Amount. Cannot be less than 1");
				map.put("TotalAmt", walletDetails.getAmount());
				return map;
			}
			if (transactionType.equals("CREDIT")) {
				String mainWalletId = walletDetails.getMainWalletid();
				if (!mainWalletService.checkMainWalletBalanceExists(mainWalletId, amt)) {
					map.put("status", "FAILED");
					map.put("remark", "Low Main Wallet Balance");
					map.put("TotalAmt", walletDetails.getAmount());
					return map;
				}

				MainWalletReversalReqDto dto = new MainWalletReversalReqDto();
				dto.setAmount(amt);
				dto.setPurpose("WALLET TRANSFER");
				dto.setRemarks("Wallet Creation " + walletDetails.getWalletid() + "Merchant Id: "
						+ walletDetails.getMerchantid());
				dto.setTransactionType("WALLET TRANSFER");
				MainWalletReversalResDto ResDto = mainWalletService.walletDeductBalance(dto,
						walletDetails.getMainWalletid());
				if (!ResDto.getStatus().equals("SUCCESS")) {
					map.put("status", "FAILED");
					map.put("remark", "Transfer Failed from Main Wallet|" + ResDto.getRemark());
					map.put("TotalAmt", walletDetails.getAmount());
					return map;
				}

				String TotalAmt = String.valueOf(df.format(Double.parseDouble(amt) + amount));
				walletDetails.setAmount(TotalAmt);
				walletDetailsRepo.save(walletDetails);
				map.put("status", "SUCCESS");
				map.put("TotalAmt", TotalAmt);
				map.put("remark", "Credit Success");
				map.put("refId", ResDto.getTransactionId());
				return map;
			} else if (transactionType.equals("DEBIT")) {
				Double am = amount - Double.parseDouble(amt);
				String TotalAmt = String.valueOf(df.format(am));
				if (am >= 0) {
					walletDetails.setAmount(TotalAmt);
					walletDetailsRepo.save(walletDetails);
					map.put("status", "SUCCESS");
					map.put("remark", "Debit Success");
					map.put("TotalAmt", TotalAmt);
				} else {
					map.put("status", "FAILED");
					map.put("remark", "Low Balance");
					map.put("TotalAmt", walletDetails.getAmount());
				}

				return map;
			} else if (transactionType.equals("REFUND")) {
				String TotalAmt = String.valueOf(df.format(Double.parseDouble(amt) + amount));
				walletDetails.setAmount(TotalAmt);
				walletDetailsRepo.save(walletDetails);
				map.put("status", "SUCCESS");
				map.put("TotalAmt", TotalAmt);
				map.put("remark", "Refund Success");
				return map;
			}
			map.put("status", "FAILED");
			map.put("remark", "Invalid Wallet");
			map.put("TotalAmt", walletDetails.getAmount());
			return map;
		} else {
			HashMap<String, String> map = new HashMap<>();
			map.put("status", "FAILED");
			map.put("remark", "Wallet is Inactive");
			map.put("TotalAmt", walletDetails.getAmount());
			return map;
		}
	}

	// public void commissionValidation(TransactionChargeCommission data) throws
	// ValidationExceptions {
	// if (!Validator.containsEnum(GatewayType.class, data.getGateway())) {
	// throw new ValidationExceptions("Entered gateway does not found in the
	// system!", FormValidationExceptionEnums.GATEWAY_INPUT_ERROR);
	// }
	//
	// if (!Validator.containsEnum(ComTransactionType.class,
	// data.getTransactionType())) {
	// throw new ValidationExceptions("Entered transaction type not found in the
	// system!", FormValidationExceptionEnums.TRANSACTION_TYPE_INPUT_ERROR);
	// }
	//
	// if (!Validator.containsEnum(UnitTypes.class, data.getCommissionType())) {
	// throw new ValidationExceptions("Entered commission type not found in the
	// system!", FormValidationExceptionEnums.COMMISSION_TYPE_INPUT_ERROR);
	// }
	//
	// if(!data.getSlab().equalsIgnoreCase("1_10000")) {
	// if(!data.getSlab().equalsIgnoreCase("10001_25000")) {
	// if(!data.getSlab().equalsIgnoreCase("25001_")) {
	// throw new ValidationExceptions("Entered slab does not found in the system!",
	// FormValidationExceptionEnums.SLAB_INPUT_ERROR);
	// }
	// }
	// }
	//// if (!Validator.containsEnum(SlabType.class, data.getSlab())) {
	//// throw new ValidationExceptions("Entered slab does not found in the
	// system!", FormValidationExceptionEnums.SLAB_INPUT_ERROR);
	//// }
	// }
	//
	// public TransactionChargeCommission
	// transactionCommission(TransactionChargeCommission data) throws
	// JsonProcessingException, ValidationExceptions {
	// commissionValidation(data);
	// TransactionChargeCommission chargeCom =
	// transactionChargeRepository.findByMerchantIdAndGatewayAndTransactionTypeAndSlab(data.getMerchantId(),
	// data.getGateway(), data.getTransactionType(), data.getSlab());
	//
	// if(chargeCom != null) {
	// throw new ValidationExceptions("Merchant Information already exists with
	// entered input! You can opt to update the information.",
	// FormValidationExceptionEnums.INFORMATION_DOES_NOT_EXISTS);
	// }
	//
	// String val = data.getCommissionValue();
	//
	// if(val.equals("")) {
	// val = "0.0";
	// }
	//
	// TransactionChargeCommission taxCom = new TransactionChargeCommission();
	//
	// taxCom.setMerchantId(data.getMerchantId());
	// taxCom.setGateway(data.getGateway());
	// taxCom.setCommissionType(data.getCommissionType());
	// taxCom.setSlab(data.getSlab());
	// taxCom.setTransactionType(data.getTransactionType());
	// taxCom.setCommissionValue(val);
	// transactionChargeRepository.save(taxCom);
	// return taxCom;
	//
	// }
	//
	// public RechargeServiceCommission rechargeCommission(RechargeServiceCommission
	// data) throws JsonProcessingException, ValidationExceptions {
	// if (!Validator.containsEnum(UnitTypes.class, data.getCommissionType())) {
	// throw new ValidationExceptions("Entered commission type not found in the
	// system!", FormValidationExceptionEnums.COMMISSION_TYPE_INPUT_ERROR);
	// }
	//
	// RechargeServiceCommission chargeCom =
	// rechargeServiceRepository.findByMerchantId(data.getMerchantId());
	// if(chargeCom != null) {
	// throw new ValidationExceptions("Merchant Information already exists with
	// entered input! You can opt to update the information.",
	// FormValidationExceptionEnums.INFORMATION_DOES_NOT_EXISTS);
	// }
	//
	// String val = data.getCommissionValue();
	//
	// if(val.equals("")) {
	// val = "0.0";
	// }
	//
	// RechargeServiceCommission taxCom = new RechargeServiceCommission();
	// taxCom.setMerchantId(data.getMerchantId());
	// taxCom.setCommissionType(data.getCommissionType());
	// taxCom.setCommissionValue(val);
	// rechargeServiceRepository.save(taxCom);
	// return taxCom;
	//
	// }
	//
	// public Object getRechargeComList() {
	// return rechargeServiceRepository.findAll();
	// }
	//
	//
	// public List<TransactionChargeCommission> getTransactionComList() {
	// return transactionChargeRepository.findAll();
	// }
	//
	// public Object getRechargeComListMerchantwise(String merchantId) {
	// return rechargeServiceRepository.findByMerchantId(merchantId);
	// }
	//
	//
	// public List<TransactionChargeCommission>
	// getTransactionComListMerchantwise(String merchantId) {
	// return transactionChargeRepository.findByMerchantId(merchantId);
	// }
	//
	// public TransactionChargeCommission
	// updateTransactionCommission(TransactionChargeCommission dto) throws
	// ValidationExceptions {
	// List<TransactionChargeCommission> commer =
	// transactionChargeRepository.findByMerchantId(dto.getMerchantId());
	// if(commer.isEmpty()) {
	// throw new ValidationExceptions("Merchant Information not found for
	// commission!", FormValidationExceptionEnums.MERCHANT_NOT_FOUND);
	// }
	//
	// commissionValidation(dto);
	// TransactionChargeCommission taxCom =
	// transactionChargeRepository.findByMerchantIdAndGatewayAndTransactionTypeAndSlab(dto.getMerchantId(),
	// dto.getGateway(), dto.getTransactionType(), dto.getSlab());
	// if(taxCom == null) {
	// throw new ValidationExceptions("Merchant Information not found with entered
	// input!", FormValidationExceptionEnums.INFORMATION_DOES_NOT_EXISTS);
	// }
	//
	// taxCom.setMerchantId(dto.getMerchantId());
	// taxCom.setGateway(dto.getGateway());
	// taxCom.setCommissionType(dto.getCommissionType());
	// taxCom.setSlab(dto.getSlab());
	// taxCom.setTransactionType(dto.getTransactionType());
	// taxCom.setCommissionValue(dto.getCommissionValue());
	//
	// return transactionChargeRepository.save(taxCom);
	// }
	//
	// public RechargeServiceCommission
	// updateRechargeCommission(RechargeServiceCommission dto) throws
	// ValidationExceptions {
	// RechargeServiceCommission taxCom =
	// rechargeServiceRepository.findByMerchantId(dto.getMerchantId());
	// if(taxCom == null) {
	// throw new ValidationExceptions("Merchant Information not found for
	// commission!", FormValidationExceptionEnums.MERCHANT_NOT_FOUND);
	// }
	//
	// if (!Validator.containsEnum(UnitTypes.class, dto.getCommissionType())) {
	// throw new ValidationExceptions("Entered commission type not found in the
	// system!", FormValidationExceptionEnums.COMMISSION_TYPE_INPUT_ERROR);
	// }
	//
	//// RechargeServiceCommission taxCom =
	// rechargeServiceRepository.findByMerchantIdAndCommissionType(dto.getMerchantId(),
	// dto.getCommissionType());
	//// if(taxCom == null) {
	//// throw new ValidationExceptions("Merchant Information not found with entered
	// input!", FormValidationExceptionEnums.INFORMATION_DOES_NOT_EXISTS);
	//// }
	//
	// taxCom.setMerchantId(dto.getMerchantId());
	// taxCom.setCommissionType(dto.getCommissionType());
	// taxCom.setCommissionValue(dto.getCommissionValue());
	//
	// return rechargeServiceRepository.save(taxCom);
	// }
	//
	// public String commDeductionAmt(String amount, String trType, String gateway,
	// String merchantId) throws JsonProcessingException, ValidationExceptions {
	// logger.info(amount + "|" + trType + "|" + gateway + "|" + merchantId);
	//
	// double amt = Double.parseDouble(amount);
	// double gstcal = 0.18;
	// double adder = 0.0;
	// double commission = 0.0;
	//
	// List<TransactionChargeCommission> chargeCom =
	// transactionChargeRepository.findByMerchantIdAndGatewayAndTransactionType(merchantId,
	// gateway, trType);
	// if(chargeCom.isEmpty()) {
	// throw new ValidationExceptions("Merchant information not found for commission
	// process!", FormValidationExceptionEnums.INFORMATION_DOES_NOT_EXISTS);
	// }
	//
	// CommissionFieldRes res = new CommissionFieldRes();
	// if (chargeCom.size() > 0) {
	// if (amt < 10001) {
	// res = searchListForTransactionCommission(chargeCom, "10000");
	// } else if ((amt > 10000) && (amt < 25000)) {
	// res = searchListForTransactionCommission(chargeCom, "25000");
	// } else if (amt > 25000) {
	// res = searchListForTransactionCommission(chargeCom, "25001");
	// }
	// }
	//
	// commission = Double.parseDouble(res.getAdderValue());
	//
	//// if(res.getCommType().equalsIgnoreCase("UNIT")) {
	//// adder = commission;
	//// }
	//// adder = commission/100;
	////
	//// double comVal1 = (adder*gstcal);
	//// double comVal2 = comVal1 + adder;
	//
	// double comVal1 = 0.0;
	// double comVal2 = 0.0;
	// if(res.getCommType().equalsIgnoreCase("UNIT")) {
	// adder = commission;
	// comVal1 = (adder*gstcal);
	// comVal2 = comVal1 + adder;
	// }
	// else {
	// adder = commission/100;
	// comVal1 = ((adder*amt)*gstcal);
	// comVal2 = comVal1 + (adder*amt);
	// }
	//
	// double commValue = (comVal2 + amt);
	// logger.info("ADDER::" + adder);
	// if (commValue<=0) {
	// throw new ValidationExceptions("Insufficient balance for the transaction
	// process!", FormValidationExceptionEnums.E0100);
	// }
	// return String.valueOf(commValue);
	//
	// }
	//
	// public String commCreditAmt(String amount, String merchantId) throws
	// JsonProcessingException, ValidationExceptions {
	// logger.info(amount + "|" + merchantId);
	//
	// double amt = Double.parseDouble(amount);
	// double gstcal = 0.18;
	// double adder = 0.0;
	// double mercom = 0.0;
	//
	// RechargeServiceCommission chargeCom =
	// rechargeServiceRepository.findByMerchantId(merchantId);
	// if(chargeCom == null) {
	// throw new ValidationExceptions("Merchant information not found for commission
	// process!", FormValidationExceptionEnums.INFORMATION_DOES_NOT_EXISTS);
	// }
	//
	//// mercom = Double.parseDouble(chargeCom.getCommissionValue());
	//// if(chargeCom.getCommissionType().equalsIgnoreCase("UNIT")) {
	//// adder = mercom;
	//// }
	//// adder = mercom/100;
	////
	//// double comVal1 = ((amt*adder)*gstcal);
	//// double comVal2 = (comVal1) + (amt*adder);
	//
	// double comVal1 = 0.0;
	// double comVal2 = 0.0;
	// mercom = Double.parseDouble(chargeCom.getCommissionValue());
	// if(chargeCom.getCommissionType().equalsIgnoreCase("UNIT")) {
	// adder = mercom;
	// comVal2 = (adder*gstcal);
	// }
	// else {
	// adder = mercom/100;
	// comVal1 = ((amt*adder)*gstcal);
	// comVal2 = (comVal1) + (amt*adder);
	// }
	//
	// double finalCom = amt - comVal2 ;
	// if (finalCom<=0) {
	// throw new ValidationExceptions("Insufficient balance in wallet.",
	// FormValidationExceptionEnums.E0100);
	// }
	// return String.valueOf(finalCom);
	//
	// }
	//
	// private CommissionFieldRes
	// searchListForTransactionCommission(List<TransactionChargeCommission> l,
	// String s) {
	// CommissionFieldRes res = new CommissionFieldRes();
	// for (TransactionChargeCommission ls : l) {
	//
	// if (ls.getSlab().contains(s)) {
	// res.setAdderValue(ls.getCommissionValue());
	// res.setCommType(ls.getCommissionType());
	// return res;
	// }
	// }
	// return null;
	// }

}
