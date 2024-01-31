package io.asktech.payout.service;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.constants.PayTMConstants;
import io.asktech.payout.dto.error.ErrorNodalDto;
import io.asktech.payout.dto.nodal.MerNodalBalTransReq;
import io.asktech.payout.dto.nodal.MerNodalCheckTXNstatusReq;
import io.asktech.payout.dto.nodal.MerchantUpdateAccountRequest;
import io.asktech.payout.dto.nodal.NodalUpdateAccountRequest;
import io.asktech.payout.dto.nodal.NodalUpdateAccountRequestupdate;
import io.asktech.payout.dto.nodal.PayTmNodalBalTransReq;
import io.asktech.payout.dto.nodal.PayTmNodalBalTransRes;
import io.asktech.payout.dto.nodal.PayTmNodalBenefManagementReq;
import io.asktech.payout.dto.nodal.PayTmNodalCheckTXNstatusReq;
import io.asktech.payout.dto.nodal.PayTmNodalCheckTXNstatusRes;
import io.asktech.payout.dto.nodal.PayTmNodalFetchAccBalRes;
import io.asktech.payout.dto.webhooks.NodalWebhookReq;
import io.asktech.payout.dto.webhooks.NodalWebhookRes;
import io.asktech.payout.dto.webhooks.NodalWebhookResData;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.nodal.NodalBalTransDetail;
import io.asktech.payout.modal.nodal.NodalBalTransReq;
import io.asktech.payout.modal.nodal.NodalBalTransRes;
import io.asktech.payout.modal.nodal.NodalBenefManagementDetailModal;
import io.asktech.payout.modal.nodal.NodalBenefManagementReq;
import io.asktech.payout.modal.nodal.NodalBenefManagementRes;
import io.asktech.payout.modal.nodal.NodalUpdateBeneficiaryReq;
import io.asktech.payout.modal.nodal.PayTmNodalDelMercbybenrefidRes;
import io.asktech.payout.modal.nodal.PaytmVodanAccountDeleteReq;
import io.asktech.payout.repository.nodal.NodalBenefManagementDetailRepo;
import io.asktech.payout.repository.nodal.NodalBenefManagementReqRepo;
import io.asktech.payout.repository.nodal.NodalBenefManagementResRepo;
import io.asktech.payout.repository.nodal.NodalUpdateBeneficiaryReqRepo;
import io.asktech.payout.repository.nodal.NodalbaltransferDetailRepo;
import io.asktech.payout.repository.nodal.NodalbaltransferReqRepo;
import io.asktech.payout.repository.nodal.NodalbaltransferResRepo;
import io.asktech.payout.repository.nodal.PayTmNodalDelMercbybenrefidResRepo;
import io.asktech.payout.repository.nodal.PaytmNodanAccountDeleteReqRepo;
import io.asktech.payout.security.TokenGenerationPaytm;
import io.asktech.payout.service.save.NodalDetailSave;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

@Service
public class PayoutNodalService implements PayTMConstants, ErrorValues {
	@Autowired
	NodalDetailSave nodalDetailSave;
	@Autowired
	NodalbaltransferDetailRepo nodalbaltransferDetailRepo;
	@Autowired
	NodalbaltransferReqRepo nodalbaltransferReqRepo;
	@Autowired
	NodalbaltransferResRepo nodalbaltransferResRepo;
	@Autowired
	NodalBenefManagementDetailRepo nodalBenefManagementDetailRepo;
	@Autowired
	NodalBenefManagementReqRepo nodalBenefManagementReqRepo;
	@Autowired
	NodalBenefManagementResRepo nodalBenefManagementResRepo;
	@Autowired
	PaytmNodanAccountDeleteReqRepo paytmVodanAccountDeleteReqRepo;
	@Autowired
	PayTmNodalDelMercbybenrefidResRepo payTmNodalDelMercbybenrefidResRepo;
	@Autowired
	NodalUpdateBeneficiaryReqRepo nodalUpdateBeneficiaryReqRepo;

	static Logger logger = LoggerFactory.getLogger(PayoutNodalService.class);

	@Value("${payTmEndPoints.payTMNodealGetBalance}")
	String payTMNodealGetBalance;
	@Value("${payTmEndPoints.payTMNodalBalTransfer}")
	String payTMNodalBalTransfer;
	@Value("${payTmEndPoints.payTMNodalchecktrStatus}")
	String payTMNodalchecktrStatus;
	@Value("${payTmEndPoints.payTMNodalAddBeneficiary}")
	String payTMNodalAddBeneficiary;
	@Value("${payTmEndPoints.payTMNodalDeleteBeneficiary}")
	String payTMNodalDeleteBeneficiary;
	@Value("${payTmEndPoints.payTMNSearchByNickName}")
	String payTMNSearchByNickName;
	@Value("${payTmEndPoints.payTMNSearchByBenId}")
	String payTMNSearchByBenId;
	@Value("${payTmCredentials.nodel.secretId}")
	String nodalSecretId;
	@Value("${payTmCredentials.nodel.partnerId}")
	String nodalPartnerId;
			

	public NodalWebhookRes SaveWebhook(NodalWebhookReq dto) throws ValidationExceptions {
		NodalWebhookRes nodalWebhookRes = new NodalWebhookRes();
		nodalWebhookRes.setEvent_tracking_id(dto.getEvent_tracking_id());
		if (nodalDetailSave.saveWebhook(dto)) {
			nodalWebhookRes.setResponse_code("SL_2000");
			if (dto.getData() != null) {
				NodalWebhookResData nodalWebhookResData = new NodalWebhookResData();
				nodalWebhookResData.setClientRequestId(dto.getData().getClientRequestId());
				nodalWebhookResData.setTransactionRequestId(dto.getData().getTransactionRequestId());
				nodalWebhookResData.setTransactionType(dto.getData().getTransactionType());
				nodalWebhookRes.setData(nodalWebhookResData);
			}
		}
		return nodalWebhookRes;

	}

	public Object getBalanceInfo(String ca) throws JsonProcessingException, ParseException {

		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<PayTmNodalFetchAccBalRes> payTmNodalFetchAccBalRes = Unirest.get(payTMNodealGetBalance + ca)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(nodalSecretId,nodalPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE).asObject(PayTmNodalFetchAccBalRes.class);

		ErrorNodalDto er = payTmNodalFetchAccBalRes.mapError(ErrorNodalDto.class);

		logger.info("Status Code ::" + payTmNodalFetchAccBalRes.getStatus());

		if (payTmNodalFetchAccBalRes.getStatus() != 200) {
			if (er == null) {
				er = Utility.populateExceptionResponse(String.valueOf(payTmNodalFetchAccBalRes.getStatus()),
						payTmNodalFetchAccBalRes.getStatusText());
			}
			er.setResponse_code(String.valueOf(payTmNodalFetchAccBalRes.getStatus()));
			logger.error("Nodal GetBalance Error :: " + er);
			return er;
		}

		logger.info(
				"Nodal GetBalance Response :: " + Utility.convertDTO2JsonString(payTmNodalFetchAccBalRes.getBody()));
		return payTmNodalFetchAccBalRes.getBody();
	}

	public Object doBalanceTransfer(MerNodalBalTransReq merNodalBalTransReq)
			throws JsonProcessingException, ParseException, ValidationExceptions {

		NodalBalTransReq nodalBalTransDetail = nodalbaltransferReqRepo
				.findByOrderId(merNodalBalTransReq.getOrderId());
		if (nodalBalTransDetail != null) {
			throw new ValidationExceptions(DUPLICATE_ORDERID, FormValidationExceptionEnums.DUPLICATE_ORDERID);
		}
		
		String txnReqId = "ND_" + Utility.getEpochTIme();

		NodalBalTransReq nodalBalTransReq = new NodalBalTransReq();
		nodalBalTransReq.setAmount(merNodalBalTransReq.getAmount());
		nodalBalTransReq.setBenAccNo(merNodalBalTransReq.getBenAccNo());
		nodalBalTransReq.setBeneficiaryIfsc(merNodalBalTransReq.getBenIfsc());
		nodalBalTransReq.setBenIfsc(merNodalBalTransReq.getBenIfsc());
		nodalBalTransReq.setBenName(merNodalBalTransReq.getBenName());
		nodalBalTransReq.setChannel(merNodalBalTransReq.getChannel());
		nodalBalTransReq.setMerchantId(merNodalBalTransReq.getMerchantId());
		nodalBalTransReq.setOrderId(merNodalBalTransReq.getOrderId());
		nodalBalTransReq.setTxnReqId(txnReqId);
		nodalBalTransReq.setTransactionType(merNodalBalTransReq.getTransactionType());
		nodalbaltransferReqRepo.save(nodalBalTransReq);

		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<PayTmNodalBalTransRes> payTmNodalBalTransRes = Unirest.post(payTMNodalBalTransfer)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(nodalSecretId,nodalPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.body(populatePayTMNodalBLRequest(merNodalBalTransReq,txnReqId)).asObject(PayTmNodalBalTransRes.class);
		
		ErrorNodalDto er = payTmNodalBalTransRes.mapError(ErrorNodalDto.class);

		logger.info("Status Code ::" + payTmNodalBalTransRes.getStatus());

		if (payTmNodalBalTransRes.getStatus() != 200) {
			if (er == null) {
				er = Utility.populateExceptionResponse(String.valueOf(payTmNodalBalTransRes.getStatus()),
						payTmNodalBalTransRes.getStatusText());
			}
			er.setResponse_code(String.valueOf(payTmNodalBalTransRes.getStatus()));
			logger.error("Nodal GetBalance Error :: " + Utility.convertDTO2JsonString(er));
			return er;
		}

		logger.info("Nodal BalanceTransfer Response :: " + Utility.convertDTO2JsonString(payTmNodalBalTransRes.getBody()));
		
		populateResponseNodalBT(payTmNodalBalTransRes.getBody(),merNodalBalTransReq);
		populateTransactionDetailsBT(payTmNodalBalTransRes.getBody(),merNodalBalTransReq);

		return payTmNodalBalTransRes.getBody();
	}

	public void populateTransactionDetailsBT(PayTmNodalBalTransRes payTmNodalBalTransRes, MerNodalBalTransReq merNodalBalTransReq) {
		
		logger.info("Inside populateTransactionDetailsBT()");
		NodalBalTransDetail nodalBalTransDetail = new NodalBalTransDetail();
		nodalBalTransDetail.setAmount(payTmNodalBalTransRes.getAmount());
		nodalBalTransDetail.setBenAccNo(merNodalBalTransReq.getBenAccNo());
		nodalBalTransDetail.setBeneficiaryIfsc(payTmNodalBalTransRes.getExtra_info().getBeneficiaryIfsc());
		nodalBalTransDetail.setBeneficiaryName(payTmNodalBalTransRes.getExtra_info().getBeneficiaryName());
		nodalBalTransDetail.setBenIfsc(merNodalBalTransReq.getBenIfsc());
		nodalBalTransDetail.setBenName(merNodalBalTransReq.getBenName());
		nodalBalTransDetail.setChannel(merNodalBalTransReq.getChannel());
		nodalBalTransDetail.setMerchantId(merNodalBalTransReq.getMerchantId());
		nodalBalTransDetail.setOrderId(merNodalBalTransReq.getOrderId());
		nodalBalTransDetail.setRemarks(merNodalBalTransReq.getRemarks());
		nodalBalTransDetail.setResponse_code(payTmNodalBalTransRes.getResponse_code());
		nodalBalTransDetail.setTarget_account(payTmNodalBalTransRes.getTarget_account());
		nodalBalTransDetail.setTxn_id(payTmNodalBalTransRes.getTxn_id());
		nodalBalTransDetail.setStatus(payTmNodalBalTransRes.getStatus());
		nodalbaltransferDetailRepo.save(nodalBalTransDetail);
		logger.info("NodalBalTransDetail table data has been populated.");
		
	}
	
	public void populateResponseNodalBT(PayTmNodalBalTransRes payTmNodalBalTransRes, MerNodalBalTransReq merNodalBalTransReq) {
		
		NodalBalTransRes nodalBalTransRes = new NodalBalTransRes();
		nodalBalTransRes.setAmount(payTmNodalBalTransRes.getAmount());
		nodalBalTransRes.setBeneficiaryIfsc(payTmNodalBalTransRes.getExtra_info().getBeneficiaryIfsc());
		nodalBalTransRes.setBeneficiaryName(payTmNodalBalTransRes.getExtra_info().getBeneficiaryName());
		nodalBalTransRes.setMerchantId(merNodalBalTransReq.getMerchantId());
		nodalBalTransRes.setOrderId(merNodalBalTransReq.getOrderId());
		nodalBalTransRes.setResponse_code(payTmNodalBalTransRes.getResponse_code());
		nodalBalTransRes.setStatus(payTmNodalBalTransRes.getStatus());
		nodalBalTransRes.setTarget_account(payTmNodalBalTransRes.getTarget_account());
		nodalBalTransRes.setTxn_id(payTmNodalBalTransRes.getTxn_id());
		nodalbaltransferResRepo.save(nodalBalTransRes);
	}

	public PayTmNodalBalTransReq populatePayTMNodalBLRequest(MerNodalBalTransReq merNodalBalTransReq, String tnxReqId)
			throws ParseException {

		PayTmNodalBalTransReq payTmNodalBalTransReq = new PayTmNodalBalTransReq();

		payTmNodalBalTransReq.setTxnReqId(tnxReqId);
		payTmNodalBalTransReq.setAmount(merNodalBalTransReq.getAmount());
		payTmNodalBalTransReq.setBenAccNo(merNodalBalTransReq.getBenAccNo());
		payTmNodalBalTransReq.setBenIfsc(merNodalBalTransReq.getBenIfsc());
		payTmNodalBalTransReq.setBenName(merNodalBalTransReq.getBenName());
		payTmNodalBalTransReq.setChannel(merNodalBalTransReq.getChannel());
		payTmNodalBalTransReq.setRemarks(merNodalBalTransReq.getRemarks());
		payTmNodalBalTransReq.setTransactionType(merNodalBalTransReq.getTransactionType());

		return payTmNodalBalTransReq;
	}

	public Object getTXNStatusFromMerchant(MerNodalCheckTXNstatusReq merNodalCheckTXNstatusReq) throws JsonProcessingException, ParseException {
		
		logger.info("Inside getTXNStatus Request From Merchants :: "+Utility.convertDTO2JsonString(merNodalCheckTXNstatusReq));
		PayTmNodalCheckTXNstatusReq payTmNodalCheckTXNstatusReq = new PayTmNodalCheckTXNstatusReq();
		payTmNodalCheckTXNstatusReq.setChannel(merNodalCheckTXNstatusReq.getChannel());
		payTmNodalCheckTXNstatusReq.setTransactionType(merNodalCheckTXNstatusReq.getTransactionType());
		payTmNodalCheckTXNstatusReq.setTxnReqId(merNodalCheckTXNstatusReq.getTxnReqId());
		
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<PayTmNodalCheckTXNstatusRes> payTmNodalCheckTXNstatusRes = Unirest.post(payTMNodalchecktrStatus)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(nodalSecretId,nodalPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.body(payTmNodalCheckTXNstatusReq).asObject(PayTmNodalCheckTXNstatusRes.class);
		
		logger.info(payTmNodalCheckTXNstatusRes.getParsingError().toString());
		
		ErrorNodalDto er = payTmNodalCheckTXNstatusRes.mapError(ErrorNodalDto.class);

		logger.info("Status Code ::" + payTmNodalCheckTXNstatusRes.getStatus());

		if (payTmNodalCheckTXNstatusRes.getStatus() != 200) {
			if (er == null) {
				er = Utility.populateExceptionResponse(String.valueOf(payTmNodalCheckTXNstatusRes.getStatus()),
						payTmNodalCheckTXNstatusRes.getStatusText());
			}
			er.setResponse_code(String.valueOf(payTmNodalCheckTXNstatusRes.getStatus()));
			logger.error("Nodal GetBalance Error :: " + Utility.convertDTO2JsonString(er));
			return er;
		}

		logger.info("Nodal Check transaction Status Response :: " + Utility.convertDTO2JsonString(payTmNodalCheckTXNstatusRes.getBody()));
		return payTmNodalCheckTXNstatusRes.getBody();
	}
	
	public Object addBeneficiary(NodalBenefManagementReq merNodalBenefManagementReq) throws JsonProcessingException, ValidationExceptions, ParseException {
		
		logger.info("Inside addBeneficiary Request From Merchants :: "+Utility.convertDTO2JsonString(merNodalBenefManagementReq));
		
		NodalBenefManagementReq nodalBenefManagementReq = nodalBenefManagementReqRepo.findByOrderId(merNodalBenefManagementReq.getOrderId());
		if(nodalBenefManagementReq !=null) {
			populateNodalAddBenefRes(merNodalBenefManagementReq, DUPLICATE_ORDERID);
			throw new ValidationExceptions(DUPLICATE_ORDERID, FormValidationExceptionEnums.DUPLICATE_ORDERID);
			
		}
		
		nodalBenefManagementReqRepo.save(merNodalBenefManagementReq);
		
		NodalBenefManagementDetailModal nodalBenefManagementDetailModal = 
				nodalBenefManagementDetailRepo.findByMerchantIdAndBeneficiaryNickName(
						merNodalBenefManagementReq.getMerchantId(),merNodalBenefManagementReq.getBeneficiaryNickName());
		if(nodalBenefManagementDetailModal != null) {
			populateNodalAddBenefRes(merNodalBenefManagementReq, MERCHANT_NICK_NAME_EXITS);
			throw new ValidationExceptions(MERCHANT_NICK_NAME_EXITS, FormValidationExceptionEnums.MERCHANT_NICK_NAME_EXITS);
		}
		
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<NodalBenefManagementRes> nodalBenefManagementRes = Unirest.post(payTMNodalAddBeneficiary)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(nodalSecretId,nodalPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.body(populateNodalAddBeneficiary(merNodalBenefManagementReq)).asObject(NodalBenefManagementRes.class);
		
		ErrorNodalDto er = nodalBenefManagementRes.mapError(ErrorNodalDto.class);

		logger.info("Status Code ::" + nodalBenefManagementRes.getStatus());

		if (nodalBenefManagementRes.getStatus() != 200) {
			if (er == null) {
				er = Utility.populateExceptionResponse(String.valueOf(nodalBenefManagementRes.getStatus()),
						nodalBenefManagementRes.getStatusText());
			}
			er.setResponse_code(String.valueOf(nodalBenefManagementRes.getStatus()));
			logger.error("Nodal GetBalance Error :: " + Utility.convertDTO2JsonString(er));
			return er;
		}
		
		populateAddNebeficiaryDetails(merNodalBenefManagementReq,nodalBenefManagementRes.getBody());
		
		NodalBenefManagementRes nodalBenefManagementReponse = nodalBenefManagementRes.getBody();
		nodalBenefManagementReponse.setMerchantId(merNodalBenefManagementReq.getMerchantId());
		nodalBenefManagementReponse.setOrderId(merNodalBenefManagementReq.getOrderId());
				
		return nodalBenefManagementResRepo.save(nodalBenefManagementReponse);
	}
	
	public void populateNodalAddBenefRes(NodalBenefManagementReq merNodalBenefManagementReq, String message) {
		
		NodalBenefManagementRes nodalBenefManagementRes = new NodalBenefManagementRes();
		nodalBenefManagementRes.setMerchantId(merNodalBenefManagementReq.getMerchantId());
		nodalBenefManagementRes.setMessage(message);
		nodalBenefManagementRes.setOrderId(merNodalBenefManagementReq.getOrderId());
		nodalBenefManagementRes.setStatus("FAILED");
		nodalBenefManagementResRepo.save(nodalBenefManagementRes);
		
		
	}
	
	public void populateAddNebeficiaryDetails(NodalBenefManagementReq merNodalBenefManagementReq,
			NodalBenefManagementRes  nodalBenefManagementRes) {
	
		NodalBenefManagementDetailModal nodalBenefManagementDetailModal = new NodalBenefManagementDetailModal();
		nodalBenefManagementDetailModal.setBeneficiaryAccountNumber(merNodalBenefManagementReq.getBeneficiaryAccountNumber());
		nodalBenefManagementDetailModal.setBeneficiaryAccountType(merNodalBenefManagementReq.getBeneficiaryAccountType());
		nodalBenefManagementDetailModal.setBeneficiaryEmail(merNodalBenefManagementReq.getBeneficiaryEmail());
		nodalBenefManagementDetailModal.setBeneficiaryIfsc(merNodalBenefManagementReq.getBeneficiaryIfsc());
		nodalBenefManagementDetailModal.setBeneficiaryMobile(merNodalBenefManagementReq.getBeneficiaryMobile());
		nodalBenefManagementDetailModal.setBeneficiaryName(merNodalBenefManagementReq.getBeneficiaryName());
		nodalBenefManagementDetailModal.setBeneficiaryNickName(merNodalBenefManagementReq.getBeneficiaryNickName());
		nodalBenefManagementDetailModal.setErrorCode(nodalBenefManagementRes.getErrorCode());
		nodalBenefManagementDetailModal.setMerchantId(merNodalBenefManagementReq.getMerchantId());
		nodalBenefManagementDetailModal.setMessage(nodalBenefManagementRes.getMessage());
		nodalBenefManagementDetailModal.setResponse(nodalBenefManagementRes.getResponse());
		nodalBenefManagementDetailModal.setStatus(nodalBenefManagementRes.getStatus());
		nodalBenefManagementDetailModal.setOrderId(merNodalBenefManagementReq.getOrderId());
		
		nodalBenefManagementDetailRepo.save(nodalBenefManagementDetailModal);
		
	}
	
	public PayTmNodalBenefManagementReq populateNodalAddBeneficiary(NodalBenefManagementReq merNodalBenefManagementReq) throws JsonProcessingException {
		
		PayTmNodalBenefManagementReq payTmNodalBenefManagementReq = new PayTmNodalBenefManagementReq();
		payTmNodalBenefManagementReq.setBeneficiaryAccountNumber(merNodalBenefManagementReq.getBeneficiaryAccountNumber());
		payTmNodalBenefManagementReq.setBeneficiaryAccountType(merNodalBenefManagementReq.getBeneficiaryAccountType());
		payTmNodalBenefManagementReq.setBeneficiaryEmail(merNodalBenefManagementReq.getBeneficiaryEmail());
		payTmNodalBenefManagementReq.setBeneficiaryIfsc(merNodalBenefManagementReq.getBeneficiaryIfsc());
		payTmNodalBenefManagementReq.setBeneficiaryMobile(merNodalBenefManagementReq.getBeneficiaryMobile());
		payTmNodalBenefManagementReq.setBeneficiaryName(merNodalBenefManagementReq.getBeneficiaryName());
		payTmNodalBenefManagementReq.setBeneficiaryNickName(merNodalBenefManagementReq.getBeneficiaryNickName());
		payTmNodalBenefManagementReq.setBeneficiaryUserType(merNodalBenefManagementReq.getBeneficiaryUserType());
		
		logger.info("Input for Request :: "+Utility.convertDTO2JsonString(payTmNodalBenefManagementReq));
		
		return payTmNodalBenefManagementReq;
	}
	
	public Object deleteBeneficiary(PaytmVodanAccountDeleteReq beneficiaryRefId) throws ValidationExceptions, JsonProcessingException, ParseException {
		
		PaytmVodanAccountDeleteReq paytmVodanAccountDeleteReq = paytmVodanAccountDeleteReqRepo.findByOrderId(beneficiaryRefId.getOrderId());
		if(paytmVodanAccountDeleteReq !=null) {
			paytmVodanAccountDeleteReqRepo.save(beneficiaryRefId);
			payTmNodalDelMercbybenrefidResRepo.save(populateDeleteBeneficiaryResponse(beneficiaryRefId, 
					FormValidationExceptionEnums.DUPLICATE_ORDERID.toString(), "FAILED", DUPLICATE_ORDERID));
			throw new ValidationExceptions(DUPLICATE_ORDERID, FormValidationExceptionEnums.DUPLICATE_ORDERID);
		}
		
		NodalBenefManagementDetailModal nodalBenefManagementDetailModal = nodalBenefManagementDetailRepo.findByResponse(beneficiaryRefId.getBeneficiaryRefId());
		if(nodalBenefManagementDetailModal == null) {
			paytmVodanAccountDeleteReqRepo.save(beneficiaryRefId);
			payTmNodalDelMercbybenrefidResRepo.save(populateDeleteBeneficiaryResponse(beneficiaryRefId, 
					FormValidationExceptionEnums.ACC_NOT_FOUND_WITH_BENEFICIARYID.toString(), "FAILED", ACC_NOT_FOUND_WITH_BENEFICIARYID));
			throw new ValidationExceptions(ACC_NOT_FOUND_WITH_BENEFICIARYID, FormValidationExceptionEnums.ACC_NOT_FOUND_WITH_BENEFICIARYID);
		}
		
		if(nodalBenefManagementDetailModal.getStatus().equalsIgnoreCase("DELETED")) {
			paytmVodanAccountDeleteReqRepo.save(beneficiaryRefId);
			payTmNodalDelMercbybenrefidResRepo.save(populateDeleteBeneficiaryResponse(beneficiaryRefId, 
					FormValidationExceptionEnums.ACC_NOT_ACTIVE_STATE.toString(), "FAILED", ACC_NOT_ACTIVE_STATE));
			throw new ValidationExceptions(ACC_NOT_ACTIVE_STATE, FormValidationExceptionEnums.ACC_NOT_ACTIVE_STATE);
		}
		
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<PayTmNodalDelMercbybenrefidRes> payTmNodalDelMercbybenrefidRes = Unirest.put(payTMNodalDeleteBeneficiary)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(nodalSecretId,nodalPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.body(beneficiaryRefId).asObject(PayTmNodalDelMercbybenrefidRes.class);
		
		ErrorNodalDto er = payTmNodalDelMercbybenrefidRes.mapError(ErrorNodalDto.class);
		if (payTmNodalDelMercbybenrefidRes.getStatus() != 200) {
			if (er == null) {
				er = Utility.populateExceptionResponse(String.valueOf(payTmNodalDelMercbybenrefidRes.getStatus()),
						payTmNodalDelMercbybenrefidRes.getStatusText());
			}
			er.setResponse_code(String.valueOf(payTmNodalDelMercbybenrefidRes.getStatus()));
			logger.error("Nodal GetBalance Error :: " + Utility.convertDTO2JsonString(er));
			return er;
		}
		
		payTmNodalDelMercbybenrefidRes.getBody().setOrderId(beneficiaryRefId.getOrderId());
		payTmNodalDelMercbybenrefidResRepo.save(payTmNodalDelMercbybenrefidRes.getBody());
		
		nodalBenefManagementDetailModal.setStatus("DELETED");
		nodalBenefManagementDetailRepo.save(nodalBenefManagementDetailModal);
		
		return payTmNodalDelMercbybenrefidRes.getBody();
	}
	
	public PayTmNodalDelMercbybenrefidRes populateDeleteBeneficiaryResponse(PaytmVodanAccountDeleteReq beneficiaryRefId, 
			String errorCode, String status , String message) {
		
		PayTmNodalDelMercbybenrefidRes payTmNodalDelMercbybenrefidRes = new PayTmNodalDelMercbybenrefidRes();
		payTmNodalDelMercbybenrefidRes.setErrorCode(errorCode);
		payTmNodalDelMercbybenrefidRes.setOrderId(beneficiaryRefId.getOrderId());
		payTmNodalDelMercbybenrefidRes.setStatus(status);
		payTmNodalDelMercbybenrefidRes.setMessage(message);
		
		
		return payTmNodalDelMercbybenrefidRes;
	}
	
	public Object updateBeneficiary(MerchantUpdateAccountRequest updalUpdateAccountRequest) throws JsonProcessingException {
		
		logger.info("Inside Nodal Update Block :: "+Utility.convertDTO2JsonString(updalUpdateAccountRequest));
		populateUpdateRequestBeneficiary(updalUpdateAccountRequest);
		logger.info("Request has been persists in DB ..");
		
		/*HttpResponse<PayTmNodalDelMercbybenrefidRes> payTmNodalDelMercbybenrefidRes = Unirest.put(payTMNodalDeleteBeneficiary)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken()).header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.body(beneficiaryRefId).asObject(PayTmNodalDelMercbybenrefidRes.class);*/
		
		return null;
	}
	
	public NodalUpdateAccountRequest populateUpdateBeneficiaryForAPI(MerchantUpdateAccountRequest updalUpdateAccountRequest) {
		
		NodalUpdateAccountRequest nodalUpdateAccountRequest = new NodalUpdateAccountRequest();
		nodalUpdateAccountRequest.setBeneficiaryRefId(updalUpdateAccountRequest.getBeneficiaryRefId());
		nodalUpdateAccountRequest.setUpdatedBy(updalUpdateAccountRequest.getUpdatedBy());
		
		NodalUpdateAccountRequestupdate nodalUpdateAccountRequestupdate = new NodalUpdateAccountRequestupdate();
		nodalUpdateAccountRequestupdate.setBeneficiaryNickName(updalUpdateAccountRequest.getUpdate().getBeneficiaryNickName());
		nodalUpdateAccountRequestupdate.setBeneficiaryUserType(updalUpdateAccountRequest.getUpdate().getBeneficiaryUserType());
		nodalUpdateAccountRequestupdate.setEmail(updalUpdateAccountRequest.getUpdate().getEmail());
		nodalUpdateAccountRequestupdate.setIfsc(updalUpdateAccountRequest.getUpdate().getIfsc());
		nodalUpdateAccountRequestupdate.setMobile(updalUpdateAccountRequest.getUpdate().getMobile());
		nodalUpdateAccountRequestupdate.setName(updalUpdateAccountRequest.getUpdate().getName());		
		nodalUpdateAccountRequest.setUpdate(nodalUpdateAccountRequestupdate);
		
		return nodalUpdateAccountRequest;
	}
	
	public NodalUpdateBeneficiaryReq populateUpdateRequestBeneficiary(MerchantUpdateAccountRequest updalUpdateAccountRequest) {
		
		NodalUpdateBeneficiaryReq nodalUpdateBeneficiaryReq = new NodalUpdateBeneficiaryReq();
		nodalUpdateBeneficiaryReq.setBeneficiaryNickName(updalUpdateAccountRequest.getUpdate().getBeneficiaryNickName());
		nodalUpdateBeneficiaryReq.setBeneficiaryRefId(updalUpdateAccountRequest.getBeneficiaryRefId());
		nodalUpdateBeneficiaryReq.setBeneficiaryUserType(updalUpdateAccountRequest.getUpdate().getBeneficiaryUserType());
		nodalUpdateBeneficiaryReq.setEmail(updalUpdateAccountRequest.getUpdate().getEmail());
		nodalUpdateBeneficiaryReq.setIfsc(updalUpdateAccountRequest.getUpdate().getIfsc());
		nodalUpdateBeneficiaryReq.setMerchantId(updalUpdateAccountRequest.getMerchantId());
		nodalUpdateBeneficiaryReq.setMobile(updalUpdateAccountRequest.getUpdate().getMobile());
		nodalUpdateBeneficiaryReq.setName(updalUpdateAccountRequest.getUpdate().getName());
		nodalUpdateBeneficiaryReq.setOrderId(updalUpdateAccountRequest.getOrderId());
		nodalUpdateBeneficiaryReq.setUpdateBy(updalUpdateAccountRequest.getUpdatedBy());
		
		return nodalUpdateBeneficiaryReqRepo.save(nodalUpdateBeneficiaryReq);
	}
	
	
	public Object getDetailsByNickName(String nickName) throws ParseException {
		
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<JsonNode> payTMSearByNickName = Unirest.get(payTMNSearchByNickName + nickName)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(nodalSecretId,nodalPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE).asJson();
		
		ErrorNodalDto er = payTMSearByNickName.mapError(ErrorNodalDto.class);

		logger.info("Status Code ::" + payTMSearByNickName.getStatus());
		logger.info("Response :: "+payTMSearByNickName.getBody());

		if (payTMSearByNickName.getStatus() != 200) {
			if (er == null) {
				er = Utility.populateExceptionResponse(String.valueOf(payTMSearByNickName.getStatus()),
						payTMSearByNickName.getStatusText());
			}
			er.setResponse_code(String.valueOf(payTMSearByNickName.getStatus()));
			logger.error("Nodal getDetailsByNickName Error :: " + er);
			return er;
		}
		
		return payTMSearByNickName.getBody().toPrettyString();
	}

	public Object getDetailsByRefId(String beneficiaryRefId) throws ParseException {
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<JsonNode> payTMSearByBenId = Unirest.get(payTMNSearchByBenId.replace("{beneficiaryRefId}", beneficiaryRefId))
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(nodalSecretId,nodalPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE).asJson();
		
		ErrorNodalDto er = payTMSearByBenId.mapError(ErrorNodalDto.class);

		logger.info("Status Code ::" + payTMSearByBenId.getStatus());
		logger.info("Response :: "+payTMSearByBenId.getBody());

		if (payTMSearByBenId.getStatus() != 200) {
			if (er == null) {
				er = Utility.populateExceptionResponse(String.valueOf(payTMSearByBenId.getStatus()),
						payTMSearByBenId.getStatusText());
			}
			er.setResponse_code(String.valueOf(payTMSearByBenId.getStatus()));
			logger.error("Nodal getDetailsByRefId Error :: " + er);
			return er;
		}
		
		return payTMSearByBenId.getBody().toPrettyString();
	}

}
