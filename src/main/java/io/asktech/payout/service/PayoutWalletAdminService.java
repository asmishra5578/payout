package io.asktech.payout.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.constants.PayTMConstants;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.BalanceCheckMerRes;
import io.asktech.payout.dto.merchant.BankAccountVerifyRequest;
import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.dto.merchant.TransactionReportMerReqResult;
import io.asktech.payout.dto.merchant.TransactionReportMerRes;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.paytm.requests.dto.PaytmAccountVerifyResponse;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.service.ICICIPayout.IciciProcessor;
import io.asktech.payout.service.Pinwallet.Pinprocessor;
import io.asktech.payout.service.axis.AxisProcessor;
import io.asktech.payout.service.cashfree.requests.CashfreeWalletRequests;
import io.asktech.payout.service.kitzone.KitzoneProessor;
import io.asktech.payout.service.neocred.NeoCredProcessor;
import io.asktech.payout.service.payg.PaygProcessor;
import io.asktech.payout.service.payoutUtils.PaymentUtils;
import io.asktech.payout.service.paytm.requests.PaytmWalletRequests;
import io.asktech.payout.service.razor.RazorProcessor;
import io.asktech.payout.service.safex.SafexProcessor;
import io.asktech.payout.service.sark.SarkProcessor;
import io.asktech.payout.service.validations.WalletValidations;
import io.asktech.payout.utils.Utility;

@Service
public class PayoutWalletAdminService implements PayTMConstants {
	// @Value("${PayoutTransfers.Account}")
	// String accountTransfer;
	// @Value("${PayoutTransfers.Status}")
	// String statusTransfer;
	// @Value("${PayoutTransfers.Upi}")
	// String upiTransfer;

	static Logger logger = LoggerFactory.getLogger(PayoutWalletAdminService.class);
	@Autowired
	WalletValidations walletValidation;

	@Autowired
	PaytmWalletRequests paytmwalletRequests;
	@Autowired
	CashfreeWalletRequests cashfreeWalletRequests;
	@Autowired
	TransactionDetailsRepo transactionDetailsRepository;
	@Autowired
	PaytmWalletRequests paytmWalletRequests;
	private WalletTransferRes wallettransferRes;

	@Autowired
	TransactionDetailsRepo transactionDetailsRepo;
	@Autowired
	PayoutUtils payoutUtils;
	@Autowired
	PaygProcessor paygProcessor;
	@Autowired
	SafexProcessor safexProcessor;
	@Autowired
	SarkProcessor sarkProcessor;

	@Autowired
	RazorProcessor razorProcessor;

	@Autowired
	AxisProcessor axisProcessor ;
	@Autowired
	NeoCredProcessor neoCredProcessor;
	@Autowired
	Pinprocessor pinprocessor;
	@Autowired
	KitzoneProessor kitzoneProessor;
	@Autowired
	IciciProcessor iciciProcessor;

	public WalletTransferRes getWalletTransfer(WalletTransferMerReq dto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {

		wallettransferRes = null;
		if (walletValidation.validateWalletTransfer(dto)) {
			logger.info("Validation done before submit to Paytm. :::");
			// wallettransferRes = paytmwalletRequests.doWalletTransfer(dto, merchantId);
		}
		wallettransferRes.setStatus("PENDING");
		wallettransferRes.setStatusMessage("Service temporarily down");
		return wallettransferRes;
	}

	public TransactionResponseMerRes getTransferStatus(TransferStatusReq dto, String merchantid) throws Exception {

		TransactionResponseMerRes transferStatusRes = null;
		if (walletValidation.validateWalletTransferStatus(dto)) {
			TransactionDetails transactionDetails = transactionDetailsRepo.findByOrderId(dto.getOrderId());
			PgDetails gatewayDetails = payoutUtils.getByPgId(transactionDetails.getPgId());
			if (gatewayDetails.getPgName().toUpperCase().contains("CASHFREE")) {
				transferStatusRes = cashfreeWalletRequests.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			} else if (gatewayDetails.getPgName().toUpperCase().contains("PAYG")) {
				transferStatusRes = paygProcessor.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			} else if (gatewayDetails.getPgName().toUpperCase().contains("SAFEX")) {
				transferStatusRes = safexProcessor.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			} else if (gatewayDetails.getPgName().toUpperCase().contains("SARK")) {
				transferStatusRes = sarkProcessor.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			} else if (gatewayDetails.getPgName().toUpperCase().contains("RAZOR")) {
				transferStatusRes = razorProcessor.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			} else if (gatewayDetails.getPgName().toUpperCase().contains("NEOCRED")) {
				transferStatusRes = neoCredProcessor.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			}else if (gatewayDetails.getPgName().toUpperCase().contains("KITZONE")) {
				transferStatusRes = kitzoneProessor.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			}else if (gatewayDetails.getPgName().toUpperCase().contains("ICICI")) {
				transferStatusRes = iciciProcessor.doWalletTransferStatus(dto, merchantid, transactionDetails,
						gatewayDetails);
			}   
			else {
				transferStatusRes = paytmwalletRequests.doWalletTransferStatus(dto, merchantid);

			}
		}
		return transferStatusRes;

	}

	public WalletTransferRes getAccountTransfer(AccountTransferMerReq dto, String merchantId, PgDetails gatewayDetails)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		WalletTransferRes wallettransferRes = null;

		if (gatewayDetails.getPgName().toUpperCase().contains("CASHFREE")) {
			wallettransferRes = cashfreeWalletRequests.doAccountTransfer(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("PAYG")) {
			wallettransferRes = paygProcessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("SAFEX")) {
			wallettransferRes = safexProcessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("SARK")) {
			wallettransferRes = sarkProcessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("RAZOR")) {
			wallettransferRes = razorProcessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		}else if (gatewayDetails.getPgName().toUpperCase().contains("AXIS")) {
			wallettransferRes = axisProcessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		}else if (gatewayDetails.getPgName().toUpperCase().contains("PIN")) {
			wallettransferRes = pinprocessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		}else if (gatewayDetails.getPgName().toUpperCase().contains("NEOCRED")) {
			wallettransferRes = neoCredProcessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("KITZONE")) {
			wallettransferRes = kitzoneProessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("ICICI")) {
			wallettransferRes = iciciProcessor.doAccountTransfer(dto, merchantId, gatewayDetails);
		} 
		else {
			wallettransferRes = paytmwalletRequests.doAccountTransfer(dto, merchantId);
		}
		logger.info("getAccountTransfer :: " + Utility.convertDTO2JsonString(wallettransferRes));

		return wallettransferRes;
	}

	public WalletTransferRes getAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantId,
			PgDetails gatewayDetails)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		logger.info("getAccountTransfer Request:: " + Utility.convertDTO2JsonString(dto));

		WalletTransferRes wallettransferRes = null;
		if (gatewayDetails.getPgName().toUpperCase().contains("CASHFREE")) {
			wallettransferRes = cashfreeWalletRequests.doAccountTransferUPI(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("RAZOR")) {
			wallettransferRes = razorProcessor.doAccountTransferUPI(dto, merchantId, gatewayDetails);
		} else if (gatewayDetails.getPgName().toUpperCase().contains("SARK")) {
			wallettransferRes = sarkProcessor.doAccountTransferUPI(dto, merchantId, gatewayDetails);
		} else {
			
			wallettransferRes = paytmwalletRequests.doAccountTransferUPI(dto, merchantId);
		}

		logger.info("getAccountTransfer :: " + Utility.convertDTO2JsonString(wallettransferRes));

		return wallettransferRes;
	}

	public BalanceCheckMerRes getBalance(String merchantid) throws JsonProcessingException, ValidationExceptions {
		BalanceCheckMerRes balanceCheckMerRes = paytmwalletRequests.getBalance(merchantid);
		return balanceCheckMerRes;
	}

	@Autowired
	PaymentUtils paymentUtils;

	public TransactionResponseMerRes getWalletTransferStatus(TransferStatusReq dto, String merchantid)
			throws JsonProcessingException, ValidationExceptions {
		logger.info("Orderid:" + dto.getOrderId() + "-MerchantId::" + merchantid);
		List<TransactionDetails> transactionDetails = transactionDetailsRepository
				.findByOrderIdAndMerchantId(dto.getOrderId(), merchantid);
		logger.info(Utility.convertDTO2JsonString(transactionDetails));
		if (transactionDetails.size() > 0) {
			String pgname = transactionDetails.get(0).getPgname();

			if (pgname == null) {
				TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
				transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
				transactionResponseMerRes.setStatusMessage(transactionDetails.get(0).getTransactionMessage());
				transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
				transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
				transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
				if (transactionDetails.get(0).getUtrid() != null) {
					transactionResponseMerRes.setUtrId(transactionDetails.get(0).getUtrid());
				}
				return transactionResponseMerRes;
			}
			// TransactionDetails transactionDetails =
			// transactionDetailsRepo.findByOrderId(dto.getOrderId());
			if (transactionDetails.get(0).getPgId() == null) {
				TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
				transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
				transactionResponseMerRes.setStatusMessage(transactionDetails.get(0).getTransactionMessage());
				transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
				transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
				transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
				if (transactionDetails.get(0).getUtrid() != null) {
					transactionResponseMerRes.setUtrId(transactionDetails.get(0).getUtrid());
				}
				return transactionResponseMerRes;
			}
			PgDetails gatewayDetails = payoutUtils.getByPgId(transactionDetails.get(0).getPgId());
			logger.info("gatewayDetails ::" + Utility.convertDTO2JsonString(gatewayDetails));
			if (gatewayDetails == null) {
				TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
				transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
				transactionResponseMerRes.setStatusMessage(transactionDetails.get(0).getTransactionMessage());
				transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
				transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
				transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
				if (transactionDetails.get(0).getUtrid() != null) {
					transactionResponseMerRes.setUtrId(transactionDetails.get(0).getUtrid());
				}
				return transactionResponseMerRes;
			}
			if (transactionDetails.get(0).getTransactionStatus().equalsIgnoreCase("ACCEPTED")) {
				logger.info("ACCEPTED::REQUEST");
				TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
				transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
				transactionResponseMerRes.setStatusMessage("Waiting for Transaction");
				transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
				transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
				transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
				return transactionResponseMerRes;
			}
			if (transactionDetails.get(0).getTransactionStatus().equalsIgnoreCase("SUCCESS")) {
				logger.info("SUCCESS::REQUEST");
				TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
				transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
				transactionResponseMerRes.setStatusMessage(transactionDetails.get(0).getTransactionMessage());
				transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
				transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
				transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
				if (transactionDetails.get(0).getUtrid() != null) {
					transactionResponseMerRes.setUtrId(transactionDetails.get(0).getUtrid());
				}
				return transactionResponseMerRes;
			}
			if (transactionDetails.get(0).getTransactionStatus().equalsIgnoreCase("FAILURE")
					|| transactionDetails.get(0).getTransactionStatus().equalsIgnoreCase("REVERSED")) {
				logger.info(transactionDetails.get(0).getTransactionStatus() + " ::REQUEST");
				TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
				transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
				transactionResponseMerRes.setStatusMessage(transactionDetails.get(0).getTransactionMessage());
				transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
				transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
				transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
				if (transactionDetails.get(0).getUtrid() != null) {
					transactionResponseMerRes.setUtrId(transactionDetails.get(0).getUtrid());
				}
				return transactionResponseMerRes;
			}
			try {
				if (Utility.midnightBlackout("23:50:00", "00:10:00")) {
					logger.info("midnightBlackout:: 23:50:00 00:10:00");
					TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
					transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
					transactionResponseMerRes.setStatusMessage(transactionDetails.get(0).getTransactionMessage());
					transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
					transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
					transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
					if (transactionDetails.get(0).getUtrid() != null) {
						transactionResponseMerRes.setUtrId(transactionDetails.get(0).getUtrid());
					}
					return transactionResponseMerRes;
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
				TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
				transactionResponseMerRes.setStatus(transactionDetails.get(0).getTransactionStatus());
				transactionResponseMerRes.setStatusMessage(transactionDetails.get(0).getTransactionMessage());
				transactionResponseMerRes.setOrderid(transactionDetails.get(0).getOrderId());
				transactionResponseMerRes.setAmount(transactionDetails.get(0).getAmount());
				transactionResponseMerRes.setMessage(transactionDetails.get(0).getTransactionMessage());
				if (transactionDetails.get(0).getUtrid() != null) {
					transactionResponseMerRes.setUtrId(transactionDetails.get(0).getUtrid());
				}
				return transactionResponseMerRes;
				// TODO Auto-generated catch block

			}
			if (gatewayDetails.getPgName().toUpperCase().contains("CASHFREE")) {
				try {
					TransactionResponseMerRes trstatus = cashfreeWalletRequests.doWalletTransferStatus(dto, merchantid,
							transactionDetails.get(0),
							gatewayDetails);
					if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
						transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
						transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
						paymentUtils.sendNotification(transactionDetails.get(0));
					}
					return trstatus;
				} catch (Exception e) {
					TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
					transactionResponseMerRes.setStatus("ERROR");
					transactionResponseMerRes.setStatusMessage(e.getMessage());
					return transactionResponseMerRes;
				}
			} else if (gatewayDetails.getPgName().toUpperCase().contains("PAYG")) {
				logger.info("PAYG Status API");
				try {
					TransactionResponseMerRes trstatus = paygProcessor.doWalletTransferStatus(dto, merchantid,
							transactionDetails.get(0),
							gatewayDetails);
					if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
						transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
						transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
						paymentUtils.sendNotification(transactionDetails.get(0));
					}
					return trstatus;
				} catch (Exception e) {
					TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
					transactionResponseMerRes.setStatus("ERROR");
					transactionResponseMerRes.setStatusMessage(e.getMessage());
					return transactionResponseMerRes;
				}
			} else if (gatewayDetails.getPgName().toUpperCase().contains("SAFEX")) {
				logger.info("SAFEX Status API");
				try {
					TransactionResponseMerRes trstatus = safexProcessor.doWalletTransferStatus(dto, merchantid,
							transactionDetails.get(0),
							gatewayDetails);
					if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
						transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
						transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
						paymentUtils.sendNotification(transactionDetails.get(0));
					}
					return trstatus;
				} catch (Exception e) {
					TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
					transactionResponseMerRes.setStatus("ERROR");
					transactionResponseMerRes.setStatusMessage(e.getMessage());
					return transactionResponseMerRes;
				}
			} else if (gatewayDetails.getPgName().toUpperCase().contains("SARK")) {
				logger.info("SARK Status API");
				try {
					TransactionResponseMerRes trstatus = sarkProcessor.doWalletTransferStatus(dto, merchantid,
							transactionDetails.get(0),
							gatewayDetails);
					String initialStatus = transactionDetails.get(0).getTransactionStatus();
					if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
						transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());

						if (!(trstatus.getMessage() == null) || !(trstatus.getMessage().isEmpty())) {
							transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
						}

						paymentUtils.sendNotification(transactionDetails.get(0));
					}
					logger.info("SARK  Start");
					logger.info("SARK::" + Utility.convertDTO2JsonString(trstatus));
					logger.info("SARK::" + Utility.convertDTO2JsonString(transactionDetails));
					if (!trstatus.getStatus().equalsIgnoreCase(initialStatus)) {
						logger.info("SARK Revesal Status Change");
						if (trstatus.getStatus().equals("FAILURE") && initialStatus.equals("PENDING")) {
							logger.info("SARK Revesal Request");

							paymentUtils.doReversal(transactionDetails.get(0));
						}
						// if (trstatus.getStatus().equals("SUCCESS") &&
						// initialStatus.equals("FAILURE")) {
						// logger.info("SARK reverse reversal");

						// //paymentUtils.doReversal(transactionDetails.get(0));
						// }
					}
					return trstatus;
				} catch (Exception e) {
					TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
					transactionResponseMerRes.setStatus("ERROR");
					transactionResponseMerRes.setStatusMessage(e.getMessage());
					return transactionResponseMerRes;
				}
			} else if (gatewayDetails.getPgName().toUpperCase().contains("RAZOR")) {
				TransactionResponseMerRes trstatus = razorProcessor.doWalletTransferStatus(dto, merchantid,
						transactionDetails.get(0),
						gatewayDetails);
				String initialStatus = transactionDetails.get(0).getTransactionStatus();
				if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
					transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
					transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
					paymentUtils.sendNotification(transactionDetails.get(0));
				}
				logger.info("RAZOR Revesal Start");
				logger.info("RAZOR::" + Utility.convertDTO2JsonString(trstatus));
				logger.info("RAZOR::" + Utility.convertDTO2JsonString(transactionDetails));
				if (!trstatus.getStatus().equalsIgnoreCase(initialStatus)) {
					logger.info("RAZOR Revesal Status Change");
					if (trstatus.getStatus().equals("FAILURE") && initialStatus.equals("PENDING")) {
						logger.info("RAZOR Revesal Request");
						paymentUtils.doReversal(transactionDetails.get(0));
					}
				}
				return trstatus;
			} else if (gatewayDetails.getPgName().toUpperCase().contains("AXIS")) {
				TransactionResponseMerRes trstatus = axisProcessor.doWalletTransferStatus(dto, merchantid,
						transactionDetails.get(0),
						gatewayDetails);
				String initialStatus = transactionDetails.get(0).getTransactionStatus();
				if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
					transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
					transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
					paymentUtils.sendNotification(transactionDetails.get(0));
				}
				logger.info("Axis Revesal Start");
				logger.info("Axis::" + Utility.convertDTO2JsonString(trstatus));
				logger.info("Axis::" + Utility.convertDTO2JsonString(transactionDetails));
				if (!trstatus.getStatus().equalsIgnoreCase(initialStatus)) {
					logger.info("Axis Revesal Status Change");
					if (trstatus.getStatus().equals("FAILURE") && initialStatus.equals("PENDING")) {
						logger.info("Axis Revesal Request");
						paymentUtils.doReversal(transactionDetails.get(0));
					}
				}
				return trstatus;
			} else if (gatewayDetails.getPgName().toUpperCase().contains("NEOCRED")) {
				TransactionResponseMerRes trstatus = neoCredProcessor.doWalletTransferStatus(dto, merchantid,
						transactionDetails.get(0),
						gatewayDetails);
				logger.info("NEOCRED ::" + Utility.convertDTO2JsonString(trstatus));
				String initialStatus = transactionDetails.get(0).getTransactionStatus();
				if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
					transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
					transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
					paymentUtils.sendNotification(transactionDetails.get(0));
				}
				logger.info("NEOCRED Revesal Start");
				//logger.info("NEOCRED ::" + Utility.convertDTO2JsonString(trstatus));
				logger.info("NEOCRED ::" + Utility.convertDTO2JsonString(transactionDetails));
				if (!trstatus.getStatus().equalsIgnoreCase(initialStatus)) {
					logger.info("NEOCRED Revesal Status Change");
					if (trstatus.getStatus().equals("FAILURE") && initialStatus.equals("PENDING")) {
						logger.info("NEOCRED Revesal Request");
						paymentUtils.doReversal(transactionDetails.get(0));
					}
				}
				return trstatus;
			
			}else if (gatewayDetails.getPgName().toUpperCase().contains("PIN")) {
				TransactionResponseMerRes trstatus = pinprocessor.doWalletTransferStatus(dto, merchantid,
						transactionDetails.get(0),
						gatewayDetails);
				logger.info("PINWALLET ::" + Utility.convertDTO2JsonString(trstatus));
				String initialStatus = transactionDetails.get(0).getTransactionStatus();
				if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
					transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
					transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
					paymentUtils.sendNotification(transactionDetails.get(0));
				}
				logger.info("PINWALLET Revesal Start");
				//logger.info("NEOCRED ::" + Utility.convertDTO2JsonString(trstatus));
				logger.info("PINWALLET ::" + Utility.convertDTO2JsonString(transactionDetails));
				if (!trstatus.getStatus().equalsIgnoreCase(initialStatus)) {
					logger.info("PINWALLET Revesal Status Change");
					if (trstatus.getStatus().equals("FAILURE") && initialStatus.equals("PENDING")) {
						logger.info("PINWALLET Revesal Request");
						paymentUtils.doReversal(transactionDetails.get(0));
					}
				}
				return trstatus;
			
			}else if (gatewayDetails.getPgName().toUpperCase().contains("KITZONE")) {
				TransactionResponseMerRes trstatus = kitzoneProessor.doWalletTransferStatus(dto, merchantid,
						transactionDetails.get(0),
						gatewayDetails);
				logger.info("KITZONE ::" + Utility.convertDTO2JsonString(trstatus));
				String initialStatus = transactionDetails.get(0).getTransactionStatus();
				if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
					transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
					transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
					paymentUtils.sendNotification(transactionDetails.get(0));
				}
				logger.info("KITZONE Revesal Start");
				//logger.info("NEOCRED ::" + Utility.convertDTO2JsonString(trstatus));
				logger.info("KITZONE ::" + Utility.convertDTO2JsonString(transactionDetails));
				if (!trstatus.getStatus().equalsIgnoreCase(initialStatus)) {
					logger.info("KITZONE Revesal Status Change");
					if (trstatus.getStatus().equals("FAILURE") && initialStatus.equals("PENDING")) {
						logger.info("KITZONE Revesal Request");
						paymentUtils.doReversal(transactionDetails.get(0));
					}
				}
				return trstatus;
			
			}else if (gatewayDetails.getPgName().toUpperCase().contains("ICICI")) {
				TransactionResponseMerRes trstatus = iciciProcessor.doWalletTransferStatus(dto, merchantid,
						transactionDetails.get(0),
						gatewayDetails);
				logger.info("ICICI ::" + Utility.convertDTO2JsonString(trstatus));
				String initialStatus = transactionDetails.get(0).getTransactionStatus();
				if (!trstatus.getStatus().equalsIgnoreCase(transactionDetails.get(0).getTransactionStatus())) {
					transactionDetails.get(0).setTransactionStatus(trstatus.getStatus());
					transactionDetails.get(0).setTransactionMessage(trstatus.getMessage());
					paymentUtils.sendNotification(transactionDetails.get(0));
				}
				logger.info("ICICI Revesal Start");
				//logger.info("NEOCRED ::" + Utility.convertDTO2JsonString(trstatus));
				logger.info("ICICI ::" + Utility.convertDTO2JsonString(transactionDetails));
				if (!trstatus.getStatus().equalsIgnoreCase(initialStatus)) {
					logger.info("ICICI Revesal Status Change");
					if (trstatus.getStatus().equals("FAILURE") && initialStatus.equals("PENDING")) {
						logger.info("ICICI Revesal Request");
						paymentUtils.doReversal(transactionDetails.get(0));
					}
				}
				return trstatus;
			
			} else {
				try {
					return paytmWalletRequests.doWalletTransferStatus(dto, merchantid);
				} catch (Exception e) {
					TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
					transactionResponseMerRes.setStatus("ERROR");
					transactionResponseMerRes.setStatusMessage("Status Not Found");
					return transactionResponseMerRes;
				}
			}

		} else {
			TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
			transactionResponseMerRes.setStatus("FAILURE");
			transactionResponseMerRes.setStatusMessage("Order Id not found");
			return transactionResponseMerRes;
		}
	}


	
	public TransactionReportMerRes getTransactionReport(TransactionReportMerReq dto, String merchantid) {
		TransactionReportMerRes transactionReportMerRes = new TransactionReportMerRes();

		List<TransactionDetails> trdetails = transactionDetailsRepository.getByMerchantId(merchantid);
		List<TransactionReportMerReqResult> transactionReportMerReqResults = new ArrayList<TransactionReportMerReqResult>();
		for (TransactionDetails rs : trdetails) {
			TransactionReportMerReqResult transactionReportMerReqResult = new TransactionReportMerReqResult();
			transactionReportMerReqResult.setUtrId(rs.getUtrid());
			transactionReportMerReqResult.setBeneficiary(rs.getBankaccount());
			transactionReportMerReqResult.setCreditOrDebit(rs.getPurpose());
			transactionReportMerReqResult.setTxnId(rs.getOrderId());
			transactionReportMerReqResult.setMerchantId(merchantid);
			transactionReportMerReqResult.setMerchantTxnType(rs.getTransactionType());
			transactionReportMerReqResult.setOrderId(rs.getOrderId());
			transactionReportMerReqResult.setTxnAmount(rs.getAmount());
			transactionReportMerReqResult.setTxnDate(rs.getCreated().toString());
			transactionReportMerReqResult.setTxnType(rs.getTransactionType());
			transactionReportMerReqResult.setMerchantTxnType(rs.getTransactionType());
			transactionReportMerReqResult.setTxnStatus(rs.getTransactionStatus());
			transactionReportMerReqResults.add(transactionReportMerReqResult);
		}
		transactionReportMerRes.setStatus("SUCCESS");
		transactionReportMerRes.setStatusMessage("FETCH SUCCESS");
		transactionReportMerRes.setResult(transactionReportMerReqResults);

		return transactionReportMerRes;
	}

	/*
	 * public TransferFundsRes getTransferFunds (TransferFundsReq dto) throws
	 * Exception{
	 * 
	 * TransferFundsRes transferFunds = null; if
	 * (walletValidation.validateTransferFunds(dto)) { transferFunds =
	 * paytmwalletRequests.doTransferFunds(dto); } return transferFunds; }
	 */
	public WalletTransferRes getWalletTransfer(WalletTransferReq dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public PaytmAccountVerifyResponse verifyBankAccount(BankAccountVerifyRequest dto)
			throws JsonProcessingException, ValidationExceptions {

		PaytmAccountVerifyResponse paytmAccountVerifyResponse = paytmwalletRequests.verifyBankAccount(dto);
		return paytmAccountVerifyResponse;
	}

	public void updateWebhookResponse() {

	}

}
