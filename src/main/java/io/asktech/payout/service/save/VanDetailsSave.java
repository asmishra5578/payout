package io.asktech.payout.service.save;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.asktech.payout.dto.reqres.VanCreationReq;
import io.asktech.payout.dto.reqres.VanUpdateReq;
import io.asktech.payout.dto.van.VanWebhookReq;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.van.VanCreationDetails;
import io.asktech.payout.modal.van.VanCreationRequest;
import io.asktech.payout.modal.van.VanCreationResponse;
import io.asktech.payout.modal.van.VanWebhookReqModel;
import io.asktech.payout.paytm.requests.dto.PayTMVanCreationRes;
import io.asktech.payout.repository.van.VanCreationDetailsRepo;
import io.asktech.payout.repository.van.VanCreationRequestRepo;
import io.asktech.payout.repository.van.VanCreationResponseRepo;
import io.asktech.payout.repository.van.VanWebhookReqRepo;

@Component
public class VanDetailsSave {

	@Autowired
	VanWebhookReqRepo vanWebhookReqRepo;

	public boolean VanWebhookSave(VanWebhookReq dto) {
		System.out.println("Van Webhook Save");
		try {
			VanWebhookReqModel vanWebhookReqModel = new VanWebhookReqModel();
			vanWebhookReqModel.setEvent_tracking_id(dto.getEvent_tracking_id());
			vanWebhookReqModel.setCa_id(dto.getCa_id());
			if (dto.getData() != null) {
				vanWebhookReqModel.setStatus(dto.getData().getStatus());
				vanWebhookReqModel.setAmount(dto.getData().getAmount());
				vanWebhookReqModel.setVanNumber(dto.getData().getVanNumber());
				vanWebhookReqModel.setBeneficiaryAccountNumber(dto.getData().getBeneficiaryAccountNumber());
				vanWebhookReqModel.setBeneficiaryIfsc(dto.getData().getBeneficiaryIfsc());
				vanWebhookReqModel.setRemitterAccountNumber(dto.getData().getRemitterAccountNumber());
				vanWebhookReqModel.setRemitterIfsc(dto.getData().getRemitterIfsc());
				vanWebhookReqModel.setRemitterName(dto.getData().getRemitterName());
				vanWebhookReqModel.setBankTxnIdentifier(dto.getData().getBankTxnIdentifier());
				vanWebhookReqModel.setTransactionRequestId(dto.getData().getTransactionRequestId());
				vanWebhookReqModel.setTransferMode(dto.getData().getTransferMode());
				vanWebhookReqModel.setResponseCode(dto.getData().getResponseCode());
				vanWebhookReqModel.setTransactionDate(dto.getData().getTransactionDate());
				vanWebhookReqModel.setTransactionType(dto.getData().getTransactionType());
				vanWebhookReqModel.setParentUtr(dto.getData().getParentUtr());
				if (dto.getData().getMeta() != null) {
					vanWebhookReqModel.setBranch(dto.getData().getMeta().getBranch());
					vanWebhookReqModel.setSection(dto.getData().getMeta().getSection());
				}
			}
			System.out.println("Van Webhook Save Success");
			vanWebhookReqRepo.save(vanWebhookReqModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	@Autowired
	VanCreationRequestRepo vanCreationRequestRepo;

	// ------------VAN Creation -----------------
	public boolean VanCreationRequestSave(VanCreationReq dto) throws ValidationExceptions {
		VanCreationRequest vanCreationRequest = new VanCreationRequest(null,dto.getOrderID(), dto.getMobile(),
				dto.getName(), dto.getMerchantID(),"");		
		
		try {
			vanCreationRequestRepo.save(vanCreationRequest);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("SQL Exception", FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;

	}

	@Autowired
	VanCreationResponseRepo vanCreationResponseRepo;

	public boolean VanCreationResponseSave(PayTMVanCreationRes dto, String merchantId, String orderId)
			throws ValidationExceptions {
		VanCreationResponse vanCreationResponse = new VanCreationResponse();
		vanCreationResponse.setMerchantId(merchantId);
		vanCreationResponse.setOrderId(orderId);
		if (!Objects.isNull(dto.getResponse())) {
			vanCreationResponse.setReferenceId(dto.getResponse().getReferenceId());
			vanCreationResponse.setActive(dto.getResponse().getActive());
			vanCreationResponse.setBeneficiaryName(dto.getResponse().getBeneficiaryName());
			vanCreationResponse.setBankName(dto.getResponse().getBankName());
		} else {
			vanCreationResponse.setErrorCode(dto.getErrorCode());
			vanCreationResponse.setErrorDetails(dto.getErrorMessage());
		}
		vanCreationResponse.setRequestId(dto.getRequestId());
		vanCreationResponse.setSuccess(dto.getSuccess());
		vanCreationResponse.setCode(dto.getCode());
		vanCreationResponse.setMessage(dto.getMessage());
		try {
			vanCreationResponseRepo.save(vanCreationResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("SQL Exception", FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;
	}

	@Autowired
	VanCreationDetailsRepo vanCreationDetailsRepo;

	public boolean VanCreationDetailsSave(PayTMVanCreationRes dto, String merchantId, String orderId)
		throws ValidationExceptions {
		VanCreationDetails vanCreationDetails = new VanCreationDetails();
		vanCreationDetails.setMerchantId(merchantId);
		vanCreationDetails.setOrderId(orderId);
		vanCreationDetails.setReferenceId(dto.getResponse().getReferenceId());
		vanCreationDetails.setActive(dto.getResponse().getActive());
		vanCreationDetails.setBeneficiaryName(dto.getResponse().getBeneficiaryName());
		vanCreationDetails.setIfscCode(dto.getResponse().getIfscCode());
		vanCreationDetails.setBankName(dto.getResponse().getBankName());
		vanCreationDetails.setRequestId(dto.getRequestId());
		try {
			vanCreationDetailsRepo.save(vanCreationDetails);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("SQL Exception", FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;
	}

	// ------------VAN Updation -----------------

	public boolean VanUpdateRequestSave(VanUpdateReq dto) throws ValidationExceptions {

		return true;
	}

}
