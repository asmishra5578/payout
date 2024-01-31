package io.asktech.payout.service.paytm.requests;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.constants.PayTMConstants;
import io.asktech.payout.dto.reqres.VanCreationReq;
import io.asktech.payout.paytm.requests.dto.PayTMVanCreationRes;
import io.asktech.payout.paytm.requests.dto.PaytmVanRequestDto;
import io.asktech.payout.utils.Token;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.asktech.payout.dto.reqres.VanCreationResError;

@Component
public class PayTMVanRequests implements PayTMConstants{
	
	static Logger logger = LoggerFactory.getLogger(PayTMVanRequests.class);
	
	public PayTMVanCreationRes createVanAccount(VanCreationReq vandto, String seq) throws JsonProcessingException {
		logger.info("PayTMVanRequests - PayTMVanCreationRes()");
		PaytmVanRequestDto paytmVanRequestDto = new PaytmVanRequestDto(vandto.getMobile(), 
				vandto.getName(), 
				PARTNERID+seq,
				PREFIXVAL+PARTNERID+seq, //899990002430001
				PREFIXVAL, VANSOLUTION, STRATEGY);	
	//	System.out.println(Utility.convertDTO2JsonString((paytmVanRequestDto)));
		logger.info(Utility.convertDTO2JsonString((paytmVanRequestDto)));
		HttpResponse<PayTMVanCreationRes> vanCreationRes =  Unirest.post(VANbaseURL)
					.header(CONTENTTYPE, APPTYPE)
					.header("Authorization",Token.getJWT())
					.body(paytmVanRequestDto)
					.asObject(PayTMVanCreationRes.class).ifFailure(VanCreationResError.class, 
							r -> {
			                    VanCreationResError e = r.getBody();
			                    
			           });
		return vanCreationRes.getBody();
	}
}
