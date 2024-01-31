package io.asktech.payout.service.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.asktech.payout.dto.nodal.MerNodalBenefManagementReq;
import io.asktech.payout.dto.nodal.MerNodalBenefManagementRes;
import io.asktech.payout.dto.nodal.NodalBenefManagementDetail;
import io.asktech.payout.dto.webhooks.NodalWebhookReq;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.nodal.NodalBenefManagementDetailModal;
import io.asktech.payout.modal.nodal.NodalBenefManagementReq;
import io.asktech.payout.modal.nodal.NodalBenefManagementRes;
import io.asktech.payout.modal.nodal.NodalWebhook;
import io.asktech.payout.repository.nodal.NodalBenefManagementDetailRepo;
import io.asktech.payout.repository.nodal.NodalBenefManagementReqRepo;
import io.asktech.payout.repository.nodal.NodalBenefManagementResRepo;
import io.asktech.payout.repository.nodal.NodalWebhookRepo;

@Component
public class NodalDetailSave {

	@Autowired
	NodalWebhookRepo nodalWebhookRepo;

	public boolean saveWebhook(NodalWebhookReq dto) throws ValidationExceptions {
		NodalWebhook nodalWebhook = new NodalWebhook();
		nodalWebhook.setEvent_tracking_id(dto.getEvent_tracking_id());
		nodalWebhook.setCa_id(dto.getCa_id());
		if (dto.getData() != null) {
			nodalWebhook.setTransactionType(dto.getData().getTransactionType());
			nodalWebhook.setAmount(dto.getData().getAmount());
			nodalWebhook.setResponse_code(dto.getData().getResponse_code());
			nodalWebhook.setClientRequestId(dto.getData().getClientRequestId());
			nodalWebhook.setTransactionRequestId(dto.getData().getTransactionRequestId());
			nodalWebhook.setTransactionDate(dto.getData().getTransactionDate());
			nodalWebhook.setStatus(dto.getData().getStatus());
			nodalWebhook.setTransfer_mode(dto.getData().getExtra_info().getTransfer_mode());
			nodalWebhook.setExternalTransactionId(dto.getData().getExtra_info().getExternalTransactionId());
		}

		try {
			nodalWebhookRepo.save(nodalWebhook);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("Unable to save webhook.", FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;
	}

	@Autowired
	NodalBenefManagementReqRepo nodalBenefManagementReqRepo;

	public boolean SaveBenefManagementReq(MerNodalBenefManagementReq dto) throws ValidationExceptions {
		NodalBenefManagementReq nodalBenefManagementReq = new NodalBenefManagementReq();
		nodalBenefManagementReq.setMerchantId(dto.getMerchantId());
		nodalBenefManagementReq.setOrderId(dto.getOrderId());
		nodalBenefManagementReq.setBeneficiaryAccountNumber(dto.getBeneficiaryAccountNumber());
		nodalBenefManagementReq.setBeneficiaryIfsc(dto.getBeneficiaryIfsc());
		nodalBenefManagementReq.setBeneficiaryAccountType(dto.getBeneficiaryAccountType());
		nodalBenefManagementReq.setBeneficiaryUserType(dto.getBeneficiaryUserType());
		nodalBenefManagementReq.setBeneficiaryName(dto.getBeneficiaryName());
		nodalBenefManagementReq.setBeneficiaryEmail(dto.getBeneficiaryEmail());
		nodalBenefManagementReq.setBeneficiaryMobile(dto.getBeneficiaryMobile());
		nodalBenefManagementReq.setBeneficiaryNickName(dto.getBeneficiaryNickName());
		try {
			nodalBenefManagementReqRepo.save(nodalBenefManagementReq);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("Unable to create account.",
					FormValidationExceptionEnums.SQLERROR_EXCEPTION);
		}
		return true;
	}

	@Autowired
	NodalBenefManagementResRepo nodalBenefManagementResRepo;
	
	public boolean SavebenefManagementResRepo(MerNodalBenefManagementRes dto) throws ValidationExceptions {
		NodalBenefManagementRes nodalBenefManagementRes =new NodalBenefManagementRes();
		nodalBenefManagementRes.setMerchantId(dto.getMerchantId());
		nodalBenefManagementRes.setOrderId(dto.getOrderId());
		nodalBenefManagementRes.setResponse(dto.getResponse());
		nodalBenefManagementRes.setSucces(dto.getSucces());
		nodalBenefManagementRes.setCode(dto.getCode());
		nodalBenefManagementRes.setErrorCode(dto.getErrorCode());
		nodalBenefManagementRes.setStatus(dto.getStatus());
		nodalBenefManagementRes.setMessage(dto.getMessage());
		
				
		try {
			nodalBenefManagementResRepo.save(nodalBenefManagementRes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("Unable to create account.",
					FormValidationExceptionEnums.SQLERROR_EXCEPTION);
		}
		return true;
	}
	
	@Autowired
	NodalBenefManagementDetailRepo nodalBenefManagementDetailRepo;
	public boolean SavenodalBenefManagementDetailRepo(NodalBenefManagementDetail dto)throws ValidationExceptions {
		NodalBenefManagementDetailModal nodalBenefManagementDetailModal= new NodalBenefManagementDetailModal();
		nodalBenefManagementDetailModal.setMerchantId(dto.getMerchantId());
		nodalBenefManagementDetailModal.setBeneficiaryAccountNumber(dto.getBeneficiaryAccountNumber());
		nodalBenefManagementDetailModal.setBeneficiaryIfsc(dto.getBeneficiaryIfsc());
		nodalBenefManagementDetailModal.setBeneficiaryAccountType(dto.getBeneficiaryAccountType());
		nodalBenefManagementDetailModal.setBeneficiaryName(dto.getBeneficiaryName());
		nodalBenefManagementDetailModal.setBeneficiaryEmail(dto.getBeneficiaryEmail());
		nodalBenefManagementDetailModal.setBeneficiaryMobile(dto.getBeneficiaryMobile());
		nodalBenefManagementDetailModal.setBeneficiaryNickName(dto.getBeneficiaryNickName());
		nodalBenefManagementDetailModal.setResponse(dto.getResponse());
		nodalBenefManagementDetailModal.setErrorCode(dto.getErrorCode());
		nodalBenefManagementDetailModal.setStatus(dto.getStatus());
		nodalBenefManagementDetailModal.setMessage(dto.getMessage());
		try {
			nodalBenefManagementDetailRepo.save(nodalBenefManagementDetailModal);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("Unable to create account.",
					FormValidationExceptionEnums.SQLERROR_EXCEPTION);
		}
		
		return true;
	}
	}
	

