package io.asktech.payout.service;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.constants.PaytmContantsVAN;
import io.asktech.payout.dataInterfaces.IMaxAcc;
import io.asktech.payout.dto.error.ErrorVANDto;
import io.asktech.payout.dto.van.PayTmVanUpdateReq;
import io.asktech.payout.dto.van.PayTmVanUpdateRes;
import io.asktech.payout.dto.van.VANRequestDto;
import io.asktech.payout.dto.van.VANResponseDto;
import io.asktech.payout.dto.van.VanWebhookReq;
import io.asktech.payout.dto.van.VanWebhookRes;
import io.asktech.payout.dto.van.VanWebhookResData;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.van.MerVanUpdateReq;
import io.asktech.payout.modal.van.PayTmVanUpdateRequest;
import io.asktech.payout.modal.van.PayTmVanUpdateResonse;
import io.asktech.payout.modal.van.VanCreationDetails;
import io.asktech.payout.modal.van.VanCreationRequest;
import io.asktech.payout.modal.van.VanCreationResponse;
import io.asktech.payout.repository.van.MerVanUpdateReqRepo;
import io.asktech.payout.repository.van.PayTmVanUpdateReqRepo;
import io.asktech.payout.repository.van.PayTmVanUpdateResRepo;
import io.asktech.payout.repository.van.VanCreationDetailsRepo;
import io.asktech.payout.repository.van.VanCreationRequestRepo;
import io.asktech.payout.repository.van.VanCreationResponseRepo;
import io.asktech.payout.security.TokenGenerationPaytm;
import io.asktech.payout.service.save.VanDetailsSave;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

@Service
public class PayoutVanService implements PaytmContantsVAN, ErrorValues  {
	
	static Logger logger = LoggerFactory.getLogger(PayoutVanService.class);
	
	@Autowired
	VanDetailsSave vanDetailsSave;
	@Autowired
	VanCreationRequestRepo vanCreationRequestRepo;
	@Autowired
	VanCreationDetailsRepo vanCreationDetailsRepo;
	@Autowired
	VanCreationResponseRepo vanCreationResponseRepo;
	
	@Autowired
	MerVanUpdateReqRepo merVanUpdateReqRepo;
	@Autowired
	PayTmVanUpdateReqRepo payTmVanUpdateReqRepo;
	@Autowired
	PayTmVanUpdateResRepo payTmVanUpdateResRepo;
	
	
	
	@Value("${payTmCredentials.van.secretId}")
	String vanSecretId;
	@Value("${payTmCredentials.van.partnerId}")
	String vanPartnerId;
	@Value("${payTmEndPoints.payTMVANCreate}")
	String payTMVanCreate;
	@Value("${payTmEndPoints.payTMVANStatusUpdate}")
	String payTMVANStatusUpdate;
	@Value("${payTmEndPoints.payTMVANAllAccountDetails}")
	String payTMVANAllAccountDetails;
	@Value("${payTmEndPoints.payTMVANGetVanDetails}")
	String payTMVANGetVanDetails;
	
	public VanWebhookRes SaveWebhook(VanWebhookReq dto) {
		System.out.println("Van Webhook Service");
		vanDetailsSave.VanWebhookSave(dto);
		VanWebhookRes vanWebhookRes = new VanWebhookRes();
		vanWebhookRes.setEvent_tracking_id(dto.getEvent_tracking_id());
		vanWebhookRes.setResponse_code("CL_2000");
		VanWebhookResData vanWebhookResData = new VanWebhookResData();
		if (dto.getData() != null) {
			vanWebhookResData.setBankTxnIdentifier(dto.getData().getBankTxnIdentifier());
			vanWebhookResData.setStatus(dto.getData().getStatus());
			vanWebhookResData.setRemitterIfsc(dto.getData().getRemitterIfsc());
			vanWebhookRes.setData(vanWebhookResData);
		}
		return vanWebhookRes;
		
	}

	public Object createVAN(VanCreationRequest vanCreationRequest) throws ValidationExceptions, ParseException, JsonProcessingException {
		logger.info("Inside createVAN method ::");
		
		VanCreationResponse vanCreationResponse = new VanCreationResponse();
		
		if(vanCreationRequestRepo.findByOrderId(vanCreationRequest.getOrderId()) != null) {
			
			vanCreationRequest.setErrorDetails(DUPLICATE_ORDERID);
			vanCreationRequestRepo.save(vanCreationRequest);
			throw new ValidationExceptions(DUPLICATE_ORDERID, FormValidationExceptionEnums.DUPLICATE_ORDERID);
		}
		
		/*if(vanCreationDetailsRepo.findByMerchantId(vanCreationRequest.getMerchantID()) !=null) {
			vanCreationRequest.setErrorDetails(MERCHANT_EXITS);
			vanCreationRequestRepo.save(vanCreationRequest);
			throw new ValidationExceptions(MERCHANT_EXITS, FormValidationExceptionEnums.MERCHANT_EXITS);
		}*/
		
		
		vanCreationRequestRepo.save(vanCreationRequest);
		logger.info("after Van creation repo method ::");
		IMaxAcc iMaxAcc = vanCreationDetailsRepo.getMaxAccno();
		String seq = "00000".substring(String.valueOf(iMaxAcc.getAccno()+1).length()) + String.valueOf(iMaxAcc.getAccno()+1);
		String vanRefId = String.valueOf(Utility.getEpochTIme());
		logger.info("random no assigned for van");
		VANRequestDto vanRequestDto = new VANRequestDto(
				vanCreationRequest.getMobile(),
				vanCreationRequest.getName(),
				PARTNERID+seq,
				vanRefId,				
				PREFIXVAL,
				VANSOLUTION,
				STRATEGY);
		
		logger.info("Input String to VAN :: "+Utility.convertDTO2JsonString(vanRequestDto));
		
		String referenceId = String.valueOf(Utility.getEpochTIme());
		logger.info("TOKEN :: "+TokenGenerationPaytm.createPaytmJWTToken(vanSecretId,vanPartnerId,referenceId));
		HttpResponse<VANResponseDto> payTmVANCreateRes = Unirest.post(payTMVanCreate)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(vanSecretId,vanPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.body(vanRequestDto).asObject(VANResponseDto.class);
		
		ErrorVANDto er = payTmVANCreateRes.mapError(ErrorVANDto.class);

		
		logger.info("Status Code ::" + payTmVANCreateRes.getStatus());
		logger.info("Response :: "+payTmVANCreateRes.getBody().toString());
		logger.info("Error DTO :: "+Utility.convertDTO2JsonString(er));

		if (payTmVANCreateRes.getStatus() != 200) {
			
			if (er == null) {
				er = Utility.populateExceptionResponseVan(String.valueOf(payTmVANCreateRes.getStatus()),
						payTmVANCreateRes.getStatusText());
			}
			
			vanCreationResponse.setMerchantId(vanCreationRequest.getMerchantID());
			vanCreationResponse.setOrderId(vanCreationRequest.getOrderId());
			vanCreationResponse.setReferenceId(referenceId);
			vanCreationResponse.setErrorCode(er.getErrorCode());
			vanCreationResponse.setErrorDetails(er.getErrorMessage());
			vanCreationResponseRepo.save(vanCreationResponse);
			
			er.setResponseCode(String.valueOf(payTmVANCreateRes.getStatus()));
			logger.error("VAN createVAN Error :: " + Utility.convertDTO2JsonString(er));
			return er;
		}
		
		vanCreationResponse.setActive(payTmVANCreateRes.getBody().getResponse().getActive());
		vanCreationResponse.setBankName(payTmVANCreateRes.getBody().getResponse().getBankName());
		vanCreationResponse.setBeneficiaryName(payTmVANCreateRes.getBody().getResponse().getBeneficiaryName());
		vanCreationResponse.setCode(payTmVANCreateRes.getBody().getCode());
		vanCreationResponse.setIfscCode(payTmVANCreateRes.getBody().getResponse().getIfscCode());
		vanCreationResponse.setMerchantId(vanCreationRequest.getMerchantID());
		vanCreationResponse.setMessage(payTmVANCreateRes.getBody().getMessage());
		vanCreationResponse.setOrderId(vanCreationRequest.getOrderId());
		vanCreationResponse.setReferenceId(referenceId);
		vanCreationResponse.setRequestId(vanRequestDto.getPartnerRefId());
		vanCreationResponse.setSuccess(payTmVANCreateRes.getBody().getSuccess());
		vanCreationResponseRepo.save(vanCreationResponse);
		
		
		VanCreationDetails vanCreationDetails =new VanCreationDetails();
				
		vanCreationDetails.setMerchantId(vanCreationRequest.getMerchantID()); 
		vanCreationDetails.setOrderId(vanCreationRequest.getOrderId()); 
		vanCreationDetails.setReferenceId(payTmVANCreateRes.getBody().getResponse().getReferenceId()); 
		vanCreationDetails.setActive(payTmVANCreateRes.getBody().getResponse().getActive()); 
		vanCreationDetails.setBeneficiaryName(payTmVANCreateRes.getBody().getResponse().getBeneficiaryName()); 
		vanCreationDetails.setIfscCode(payTmVANCreateRes.getBody().getResponse().getIfscCode()); 
		vanCreationDetails.setBankName(payTmVANCreateRes.getBody().getResponse().getBankName());		
		vanCreationDetails.setReferenceId(payTmVANCreateRes.getBody().getResponse().getReferenceId()); 
		vanCreationDetails.setAccoutNumber(vanCreationRequest.getMobile()+seq);	
		vanCreationDetails.setRequestId(payTmVANCreateRes.getBody().getRequestId());
		vanCreationDetails.setSequenceNo(Integer.parseInt(seq));
		vanCreationDetails.setVanInput(vanRequestDto.getPartnerRefId());
		
		return vanCreationDetailsRepo.save(vanCreationDetails);
	}

	public Object vanAccountStatusUpdate(MerVanUpdateReq meerVanUpdateReq) throws ValidationExceptions, ParseException, JsonProcessingException {

		logger.info("Inside vanAccountStatusUpdate method ::");
		
		if(merVanUpdateReqRepo.findByOrderId(meerVanUpdateReq.getOrderId()) !=null) {
			meerVanUpdateReq.setErrorDetails(DUPLICATE_ORDERID);
			merVanUpdateReqRepo.save(meerVanUpdateReq);
			throw new ValidationExceptions(DUPLICATE_ORDERID, FormValidationExceptionEnums.DUPLICATE_ORDERID);
		}
		
		VanCreationDetails vanCreationDetails = vanCreationDetailsRepo.findByReferenceId(meerVanUpdateReq.getId());
		if(vanCreationDetails ==null) {
			meerVanUpdateReq.setErrorDetails(INPUT_ID_NOT_FOUND);
			merVanUpdateReqRepo.save(meerVanUpdateReq);
			throw new ValidationExceptions(INPUT_ID_NOT_FOUND, FormValidationExceptionEnums.INPUT_ID_NOT_FOUND);
		}
		
		PayTmVanUpdateReq payTmVanUpdateReq = new PayTmVanUpdateReq();
		payTmVanUpdateReq.setId(meerVanUpdateReq.getId());
		payTmVanUpdateReq.setStatus(meerVanUpdateReq.getStatus());
		payTmVanUpdateReq.setType(VANUPDATETYPE);	
		
		PayTmVanUpdateRequest payTmVanUpdateRequest = new PayTmVanUpdateRequest();
		payTmVanUpdateRequest.setMerchantId(meerVanUpdateReq.getMerchantId());
		payTmVanUpdateRequest.setOrderId(meerVanUpdateReq.getOrderId());
		payTmVanUpdateRequest.setStatus(meerVanUpdateReq.getStatus());
		payTmVanUpdateRequest.setType(VANUPDATETYPE);
		payTmVanUpdateRequest.setId(meerVanUpdateReq.getId());
		payTmVanUpdateReqRepo.save(payTmVanUpdateRequest);
		
		PayTmVanUpdateResonse payTmVanUpdateResonse = new PayTmVanUpdateResonse(); 
		
		logger.info("Output Response before API Call:: ");
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<PayTmVanUpdateRes> payTmVANUpdateVan = Unirest.put(payTMVANStatusUpdate)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(vanSecretId,vanPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.body(payTmVanUpdateReq).asObject(PayTmVanUpdateRes.class);
		logger.info("Output Response After API Call:: ");
		logger.info("Output Response :: "+Utility.convertDTO2JsonString(payTmVANUpdateVan.getBody()));
		
		ErrorVANDto er = payTmVANUpdateVan.mapError(ErrorVANDto.class);
		if (payTmVANUpdateVan.getStatus() != 200) {
			
			if (er == null) {
				er = Utility.populateExceptionResponseVan(String.valueOf(payTmVANUpdateVan.getStatus()),
						payTmVANUpdateVan.getStatusText());
			}		
			
			payTmVanUpdateResonse.setMerchantId(meerVanUpdateReq.getMerchantId());
			payTmVanUpdateResonse.setCode(er.getErrorCode());
			payTmVanUpdateResonse.setMessage(er.getErrorMessage());
			payTmVanUpdateResonse.setOrderId(meerVanUpdateReq.getOrderId());
			payTmVanUpdateResonse.setId(meerVanUpdateReq.getId());	
			payTmVanUpdateResRepo.save(payTmVanUpdateResonse);
			
			er.setResponseCode(String.valueOf(payTmVANUpdateVan.getStatus()));
			logger.error("VAN createVAN Error :: " + Utility.convertDTO2JsonString(er));
			return er;
		}
		
		payTmVanUpdateResonse.setMerchantId(meerVanUpdateReq.getMerchantId());
		payTmVanUpdateResonse.setOrderId(meerVanUpdateReq.getOrderId());
		payTmVanUpdateResonse.setId(meerVanUpdateReq.getId());	
		payTmVanUpdateResonse.setCode(payTmVANUpdateVan.getBody().getCode());
		payTmVanUpdateResonse.setMessage(payTmVANUpdateVan.getBody().getMessage());
		payTmVanUpdateResonse.setSuccess(String.valueOf(payTmVANUpdateVan.getBody().isSuccess()));		
		
		if(payTmVANUpdateVan.getBody().isSuccess()) {
			vanCreationDetails.setActive("ACTIVE");
			vanCreationDetailsRepo.save(vanCreationDetails);
		}		
		
		return payTmVanUpdateResRepo.save(payTmVanUpdateResonse);
	}

	public Object listAllVanDetails() throws ParseException {
		
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<JsonNode> listAllVanDetails = Unirest.get(payTMVANAllAccountDetails+"?fetchStrategy="+PRODUCT_NUMBER+"&limit=100&offset=0&productNumber="+NODAL_ACCOUNT_NUMBER)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(vanSecretId,vanPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.asJson();
		logger.info("Response :: "+listAllVanDetails.getStatus());
		logger.info("Response :: "+listAllVanDetails.getStatusText());
		logger.info("Response :: "+listAllVanDetails.getBody());
		
		return listAllVanDetails.getBody().toPrettyString();				
	}

	public Object getVanDetails(String vanId) throws ParseException {
		String referenceId = String.valueOf(Utility.getEpochTIme());
		HttpResponse<JsonNode> listAllVanDetails = Unirest.get(payTMVANAllAccountDetails+"?fetchStrategy="+PRODUCT_NUMBER)
				.header(AUTHORIZATION, TokenGenerationPaytm.createPaytmJWTToken(vanSecretId,vanPartnerId,referenceId))
				.header(CONTENTTYPE, APPTYPE)
				.header(ACCEPT, APPTYPE).header(CACHE_CONTROL, NO_CACHE)
				.asJson();
		logger.info("Response :: "+listAllVanDetails.getStatus());
		logger.info("Response :: "+listAllVanDetails.getStatusText());
		logger.info("Response :: "+listAllVanDetails.getBody());
		
		return listAllVanDetails.getBody().toPrettyString();	
	}
}
