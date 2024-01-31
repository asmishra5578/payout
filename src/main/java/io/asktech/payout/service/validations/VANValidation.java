package io.asktech.payout.service.validations;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.asktech.payout.dto.reqres.VanCreationReq;
import io.asktech.payout.dto.reqres.VanUpdateReq;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;


@Component
public class VANValidation {
	public boolean validateCreateVAN(VanCreationReq dto) throws ValidationExceptions {
		if (!StringUtils.hasLength(dto.getMobile())) {		
			throw new ValidationExceptions("Mobille Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}
		if (!StringUtils.hasLength(dto.getName())) {
			throw new ValidationExceptions("Name Field can't be empty", FormValidationExceptionEnums.FIELED_NOT_FOUND);
		}
		
		return true;
	}
	public boolean validateUpdateVan(VanUpdateReq Dto) {
		return true;
	}
}


