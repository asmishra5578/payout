package io.asktech.payout.service.validations;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.constants.PaytmWalletConstants;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.BankAccountVerifyRequest;
import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.utils.Utility;

@Component
public class PayoutMerchantReqValidation implements ErrorValues {
	public boolean WalletTransferValidation(WalletTransferMerReq dto)
			throws ValidationExceptions, JsonProcessingException {

		if (!(dto.getPhonenumber().length() == 10)) {

			throw new ValidationExceptions(PHONE_VAIDATION_FILED_E0107, FormValidationExceptionEnums.E0107);
		}
		return true;
	}

	public boolean accountTransferValidation(AccountTransferMerReq dto) throws ValidationExceptions {
		if (!(dto.getPhonenumber().length() == 10)) {
			throw new ValidationExceptions(PHONE_VAIDATION_FILED_E0107, FormValidationExceptionEnums.E0107);
		}
		if (!Utility.checkNumericValue(dto.getPhonenumber())) {
			throw new ValidationExceptions(PHONE_VAIDATION_FILED_E0107, FormValidationExceptionEnums.E0107);
		}
		if (!Utility.checkNumericValue(dto.getBankaccount())) {
			throw new ValidationExceptions(BANK_ACCOUNT_VAIDATION_FILED_E0108, FormValidationExceptionEnums.E0108);
		}
		return true;

	}

	public boolean accountTransferValidation(AccountTransferUPIMerReq dto) throws ValidationExceptions {
		
		if (!(dto.getPhonenumber().length() == 10)) {
			throw new ValidationExceptions(PHONE_VAIDATION_FILED_E0107, FormValidationExceptionEnums.E0107);
		}
		if (dto.getBeneficiaryName() != null) {
			if (dto.getBeneficiaryName().length() < 1) {
				throw new ValidationExceptions("Baneficiary Name is Required", FormValidationExceptionEnums.E0107);
			}
		} else {
			throw new ValidationExceptions("Baneficiary Name is Required", FormValidationExceptionEnums.E0107);
		}
		if (dto.getBeneficiaryVPA() != null) {
			if (dto.getBeneficiaryVPA().length() < 1) {
				throw new ValidationExceptions("Baneficiary VPA is Required", FormValidationExceptionEnums.E0107);
			}
		} else {
			throw new ValidationExceptions("Baneficiary VPA is Required", FormValidationExceptionEnums.E0107);
		}
		if (dto.getAmount() != null) {
			if (dto.getAmount().length() < 1) {
				throw new ValidationExceptions("Amount is Required", FormValidationExceptionEnums.E0107);
			}
		} else {
			throw new ValidationExceptions("Amount is Required", FormValidationExceptionEnums.E0107);
		}
		if (!Utility.checkNumericValue(dto.getPhonenumber())) {
			throw new ValidationExceptions(PHONE_VAIDATION_FILED_E0107, FormValidationExceptionEnums.E0107);
		}

		return true;

	}

	public boolean statusReportValidation(TransactionReportMerReq dto) throws ValidationExceptions {

		if (!Utility.dateValidator(dto.getFromDate(), PaytmWalletConstants.DATE_FORMAT)) {
			throw new ValidationExceptions(DATE_VALIDATION_ERROR_E0105, FormValidationExceptionEnums.E0105);
		}

		if (!Utility.dateValidator(dto.getToDate(), PaytmWalletConstants.DATE_FORMAT)) {
			throw new ValidationExceptions(DATE_VALIDATION_ERROR_E0105, FormValidationExceptionEnums.E0105);
		}

		return true;
	}

	public boolean bankAccountVerfiyValidation(BankAccountVerifyRequest dto) throws ValidationExceptions {

		if (dto.getBeneficiaryVPA().length() == 0 || dto.getBeneficiaryVPA() == "") {
			if (!Utility.checkNumericValue(dto.getBeneficiaryAccountNo())) {
				throw new ValidationExceptions(BANK_ACCOUNT_VAIDATION_FILED_E0108, FormValidationExceptionEnums.E0108);
			}

			if (!Utility.validateIFSCCode(dto.getBeneficiaryIFSCCode())) {
				throw new ValidationExceptions(BANK_IFSC_VALIDATION_FAILED_E0109, FormValidationExceptionEnums.E0109);
			}
		} else {
			if (!Utility.validateIUpiID(dto.getBeneficiaryVPA())) {
				throw new ValidationExceptions(BANK_UPI_VALIDATION_FAILED_E0110, FormValidationExceptionEnums.E0110);
			}
		}

		return true;
	}

}
