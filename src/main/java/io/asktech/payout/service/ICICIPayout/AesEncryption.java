package io.asktech.payout.service.ICICIPayout;
import javax.crypto.Cipher;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;  
import java.security.InvalidAlgorithmParameterException;  
import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;  
import java.util.Base64;
import javax.crypto.BadPaddingException;  
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



@Component
public class AesEncryption 
    {  
    /* Private variable declaration */  
    /* Encryption Method */  
public static Logger logger = LoggerFactory.getLogger(AesEncryption.class);

    public String encrypt(String strToEncrypt, String SECRET_KEY,  String SALTVALUE) throws InvalidKeySpecException, InvalidAlgorithmParameterException   
    {  
		 try   
     {  
    //   /* Declare a byte array. */  
    //   byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};  
    //   IvParameterSpec ivspec = new IvParameterSpec(iv);   
	//   String salt = "";     
    //   /* Create factory for secret keys. */  
    //   SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");  
    //   /* PBEKeySpec class implements KeySpec interface. */  
    //   KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);  
    //   SecretKey tmp = factory.generateSecret(spec);  
    //   SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");  
    //   Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7padding");  
    //   cipher.init(Cipher.ENCRYPT_MODE, secretKey);  
    //   /* Retruns encrypted value. */  
	// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    // SecureRandom rnd = new SecureRandom();
    // byte[] iv = new byte[16];
    // rnd.nextBytes(iv);
    // IvParameterSpec ivParams = new IvParameterSpec(iv);

    // cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.ISO_8859_1), "AES"), ivParams);

    // // byte[] ciphertext = cipher.doFinal(strToEncrypt.getBytes());
    //   return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_16LE)));  
SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16])); // 0-byte IV
        byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
     
}   
// public static String calculateAES(String jsonString) {
// 		String data = "";
// 		try {
// 			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
// 			IvParameterSpec ivspec = new IvParameterSpec(iv);
// 			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
// 			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
// 			SecretKey tmp = factory.generateSecret(spec);
// 			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
// 			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
// 			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
// 			data = Base64.getEncoder().encodeToString(cipher.doFinal(jsonString.getBytes("UTF-8")));
// 		} catch (Exception e) {
// 			System.out.println("Error while encrypting: " + e.toString());
// 		}
// 		return data;
// 	}
    catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e)   
    {  
      System.out.println("Error occured during encryption: " + e.toString());  
    }  
    return null;  
    } 
    
     public static String decrypt(String encryptedBase64, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16])); // 0-byte IV
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}