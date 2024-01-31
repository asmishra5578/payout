

package io.asktech.payout.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.asktech.payout.constants.PayTMConstants;
import io.asktech.payout.dataInterfaces.IMaxAcc;
import io.asktech.payout.dto.reqres.VanCreationReq;
import io.asktech.payout.dto.reqres.VanUpdateReq;
import io.asktech.payout.dto.van.PayTmVanUpdateReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.paytm.requests.dto.PayTMVanCreationRes;
import io.asktech.payout.repository.van.VanCreationDetailsRepo;
import io.asktech.payout.service.paytm.requests.PayTMVanRequests;
import io.asktech.payout.service.save.VanDetailsSave;
import io.asktech.payout.service.validations.VANValidation;
import io.asktech.payout.utils.Utility;

@Service
public class PayoutAdminService implements PayTMConstants {

	@Autowired
	VANValidation vANValidation;

	@Autowired
	PayTMVanRequests payTMVanRequests;

	@Autowired
	VanDetailsSave vanDetailsSave;

	@Autowired
	VanCreationDetailsRepo  vanCreationDetailsRepo;
	
	public PayTMVanCreationRes getVanCreated(VanCreationReq dto)
			throws NoSuchAlgorithmException, IOException, ValidationExceptions {
		System.out.println(Utility.convertDTO2JsonString(dto));

		PayTMVanCreationRes vanCreationRes = null;
		if (vANValidation.validateCreateVAN(dto)) {
			vanDetailsSave.VanCreationRequestSave(dto);
			long accno = 10001;
			IMaxAcc acc = vanCreationDetailsRepo.getMaxAccno();
			if(acc != null) {
				accno = acc.getAccno()+1;
			}
			vanCreationRes = payTMVanRequests.createVanAccount(dto,Long.toString(accno));
			System.out.println(Utility.convertDTO2JsonString(vanCreationRes));
			vanDetailsSave.VanCreationResponseSave(vanCreationRes, dto.getMerchantID(), dto.getOrderID());
			if (Objects.isNull(vanCreationRes.getErrorCode())) {
				vanDetailsSave.VanCreationDetailsSave(vanCreationRes, dto.getMerchantID(), dto.getOrderID());
			}
		}

		return vanCreationRes;
	}

	public PayTmVanUpdateReq VanUpdate(VanUpdateReq Dto) {
		if (vANValidation.validateUpdateVan(Dto)) {
			
		}
		return null;

	}

}
