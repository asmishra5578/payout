package io.asktech.payout.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenGenerationPaytm {
	
	//static String SECRET_KEY = "ExgLbR-mmdFRhNLln9Sx5d78zN50W7kmS0leh4RAwgs=";

	public static void main(String[] args) {

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		System.out.println(nowMillis);
		String secretId = "4zTmmKYw6a2jxKLbrNcQBqpNkiDWTv-RxuI5FUVs6vQ=";

		Map<String, Object> claims = new HashMap<>();
		claims.put("iss", "PAYTM");
		claims.put("timestamp", nowMillis);
		claims.put("partnerId", "VAN_ANA_000243");
		claims.put("requestReferenceId", "req1246");

		Map<String, Object> header = new HashMap<>();

		header.put("typ", "JWT");
		header.put("alg", "HS256");

		String jwtToken = createJWT(claims, header,secretId);
		System.out.println("Token Info :: "+jwtToken);
		
		//System.out.println(decodeJWT(jwtToken).get("partnerId"));

	}
	
	public static String createPaytmJWTToken(String secretId , String partnerId, String referenceId) {
		
		long nowMillis = System.currentTimeMillis();
		//Date now = new Date(nowMillis);
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("iss", "PAYTM");
		claims.put("timestamp", nowMillis);
		claims.put("partnerId", partnerId);
		claims.put("requestReferenceId", referenceId);

		Map<String, Object> header = new HashMap<>();

		header.put("typ", "JWT");
		header.put("alg", "HS256");
		
		return createJWT(claims, header,secretId);
	}

	public static String createJWT(Map<String, Object> claims, Map<String, Object> header, String secretId) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		

		byte[] apiKeySecretBytes = secretId.getBytes();

		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setHeader(header).setClaims(claims).signWith(signatureAlgorithm,signingKey);
		return builder.compact();
	}
	
	/*
	public static Claims decodeJWT(String jwt) {
	    //This line will throw an exception if it is not a signed JWS (as expected)
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = SECRET_KEY.getBytes();
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
	    Claims claims = Jwts.parser()
	            .setSigningKey(signingKey)
	            .parseClaimsJws(jwt).getBody();
	    return claims;
	}
	*/
}
