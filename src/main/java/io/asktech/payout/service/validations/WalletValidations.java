package io.asktech.payout.service.validations;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.dto.reqres.TransferFundsReq;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.VanCreationReq;
import io.asktech.payout.dto.reqres.WalletTransferReq;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;

@Component
public class WalletValidations {
	
	public boolean validateWalletTransfer(WalletTransferMerReq dto) throws ValidationExceptions {
		if (!StringUtils.hasLength(dto.getPhonenumber())) {		
			throw new ValidationExceptions("Mobille Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}
		/*
		if (dto.getAmount() < 2) {
			throw new ValidationExceptions("Amount Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}
		*/
		return true;
	}
	
	public boolean validateWalletTransferStatus(TransferStatusReq dto) throws ValidationExceptions{
		
		if (!StringUtils.hasLength(dto.getOrderId())) {		
			throw new ValidationExceptions("OrderId Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}

		return true;
		
		
	}
	
	public boolean validateTransferFunds (TransferFundsReq dto) throws ValidationExceptions{
		
		if (!StringUtils.hasLength(dto.getAmount())) {		
			throw new ValidationExceptions("Amount Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}
		if (!StringUtils.hasLength(dto.getRequestId())) {		
			throw new ValidationExceptions("RequestId Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}
		if (!StringUtils.hasLength(dto.getMerchantId())) {		
			throw new ValidationExceptions("Merchant Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}
		return true;
	}

}
