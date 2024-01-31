package io.asktech.payout.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Token {

	public static void main (String []args)
	{
					
		}
	
	

	
	public static String getJWT() {
		
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		System.out.println(nowMillis);
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("iss", "PAYTM");
		claims.put("timestamp", nowMillis);
		claims.put("partnerId", "VAN_ANA_000243");
		claims.put("requestReferenceId", "req121212");
		
		Map<String, Object> header = new HashMap<>();
		
		header.put("typ", "JWT");
		header.put("alg", "HS256");
		
	return	createJWT(claims,header);
		
		
	}
	public static String createJWT (Map<String, Object> claims,Map<String, Object> header) {
		  
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);

	   String SECRET_KEY ="4zTmmKYw6a2jxKLbrNcQBqpNkiDWTv-RxuI5FUVs6vQ=";
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes =SECRET_KEY.getBytes();
	    
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	    JwtBuilder builder = Jwts.builder()
	    		.setHeader(header)
	            .setClaims(claims)
	            .signWith(signatureAlgorithm, signingKey);
	  

	    return builder.compact();
	}
}
