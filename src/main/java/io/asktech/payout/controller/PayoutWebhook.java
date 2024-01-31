package io.asktech.payout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.asktech.payout.service.axis.AxisUtility;
import io.asktech.payout.service.axis.dto.CallBackDto;
import io.asktech.payout.service.payg.dto.webhook.PayGwebhook;





@RestController
public class PayoutWebhook {

@Autowired
AxisUtility axisUtility;



    @PostMapping(value = "/web/payg")
	public ResponseEntity<?> PageWebhook(@RequestBody PayGwebhook dto){
			return ResponseEntity.ok().body(dto);
	}
	@PostMapping(value = "/web/axis")
	public ResponseEntity<?> AxisWebhook(@RequestBody CallBackDto dto) throws JsonMappingException, JsonProcessingException{

		String data =  axisUtility.decryptCallBack("1F4DC4B07AA34418A15ED37C56B64B64", dto.getGetStatusResponseBodyEncrypted());
		axisUtility.callBackApi(data);
		return ResponseEntity.ok().body(data);
}
}
