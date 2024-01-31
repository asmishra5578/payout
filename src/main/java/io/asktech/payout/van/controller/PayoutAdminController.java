package io.asktech.payout.van.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.asktech.payout.constants.PayTMConstants;
import io.asktech.payout.dto.error.ErrorResponseDto;
import io.asktech.payout.dto.reqres.VanCreationReq;
import io.asktech.payout.dto.van.VANRequestDto;
import io.asktech.payout.dto.van.VANResponseDto;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.paytm.requests.dto.PayTMVanCreationRes;
import io.asktech.payout.service.PayoutAdminService;
import io.asktech.payout.utils.Utility;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@RestController
public class PayoutAdminController {
	
	@Autowired
	PayoutAdminService payoutAdminService;
	
	@PostMapping(value = "/admin/vanCreation")
	//@ApiOperation(value = "Admin van creation api", authorizations = { @Authorization(value = "apiKey") })
	public ResponseEntity<?> VanCreate(@RequestBody VanCreationReq dto) throws 
			NoSuchAlgorithmException, IOException, ValidationExceptions, jdk.jshell.spi.ExecutionControl.UserException {
		

		PayTMVanCreationRes vanres = payoutAdminService.getVanCreated(dto);

		return ResponseEntity.ok().body(vanres);
	}
	
	@PostMapping(value = "/admin/vanUpdation")
	//@ApiOperation(value = "Admin van creation api", authorizations = { @Authorization(value = "apiKey") })
	public ResponseEntity<?> VanUpdation(@RequestBody VanCreationReq dto) throws 
			NoSuchAlgorithmException, IOException, ValidationExceptions, jdk.jshell.spi.ExecutionControl.UserException {
		

		PayTMVanCreationRes vanres = payoutAdminService.getVanCreated(dto);

		return ResponseEntity.ok().body(vanres);
	}
	
	@PostMapping(value = "/admin/vanCallback")
	//@ApiOperation(value = "Admin van creation api", authorizations = { @Authorization(value = "apiKey") })
	public ResponseEntity<?> VanCallBack(@RequestHeader Map<String, String> headers,@RequestBody Map<String, Object> bodydata) throws 
			Exception {
		
		
		System.out.println(bodydata);
		String[] chunks = headers.get("authorization").split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		String header = new String(decoder.decode(chunks[0]));
		String payload = new String(decoder.decode(chunks[1]));
		
		System.out.println(header);
		System.out.println(payload);
		
		SignatureAlgorithm sa = HS256;
		SecretKeySpec secretKeySpec = new SecretKeySpec(PayTMConstants.secretKey.getBytes(), sa.getJcaName());
		DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
		String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        if (!validator.isValid(tokenWithoutSignature, signature)) {
            throw new Exception("Could not verify JWT token integrity!");
        }else {
        	System.out.println("Valid Token");
        }
        String data = "HEADER:"+payload + "|BODY:"+new JSONObject(bodydata)+"\n";
        File file = new File("LoadData.txt");
        FileWriter fr = new FileWriter(file, true);
        fr.write(data);
        fr.close();
		headers.forEach((key, value) -> {
	        System.out.println(String.format("Header '%s' = %s", key, value));
	    });

	    return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

}
