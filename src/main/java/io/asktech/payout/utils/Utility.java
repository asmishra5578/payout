package io.asktech.payout.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.error.ErrorNodalDto;
import io.asktech.payout.dto.error.ErrorResponseDto;
import io.asktech.payout.dto.error.ErrorVANDto;
import io.asktech.payout.enums.FormValidationExceptionEnums;

public class Utility {

	public static String getRandomPhn() {
		Random random = new Random();
		String phoneNumber = String.format("%d%09d",
				random.nextInt(9 - 5) + 6, random.nextInt(1000000000));
		System.out.println("Phone Number =  " + phoneNumber);
		return phoneNumber;

	}

	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	public static boolean validateJavaDateFormat(String strDate){
	 	
		/*Set preferred date format,For example yyyy-mm-dd*/
			   
		   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
		   simpleDateFormat.setLenient(false);
		   //Create Date object parse the string into date
		   //If a string will be converted into a date object then string is valid and contain a valid date value in desire format
		   try {
			   Date javaDate = simpleDateFormat.parse(strDate.trim());
			   System.out.println(strDate + " is valid date format  : "+ javaDate);
		   }catch(Exception e) {
			   e.printStackTrace();
			   return false;
		   }
   
	   
		   return true;
		}
	public static Instant getTimestamp() {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Instant instant = timestamp.toInstant();

		return instant;
	}

	public static String convertDTO2JsonString(Object json) throws JsonProcessingException {
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(json);
		return jsonStr;
	}

	public static boolean validateBalance(int fromBalabce, int toBalance) {

		if (fromBalabce >= toBalance) {
			return true;
		}

		return false;
	}

	public static Long getEpochTIme() throws ParseException {
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
		String currentTime = crunchifyFormat.format(today);
		Date date = crunchifyFormat.parse(currentTime);
		long epochTime = date.getTime();
		return epochTime;
	}

	public static String getRandomId() throws ParseException  {
		String str = String.valueOf(getEpochTIme()) + UUID.randomUUID().toString().split("-")[0];
		return str.toUpperCase();
	}

	public static String beautifyJson(String strJson) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(strJson);
		return null;
	}

	public static String getJsonFileCreate(JSONObject jsonObject, String requestType) throws ParseException {

		// JSONObject jsonObject = new JSONObject(jsonStr);
		String fileName = requestType + "_" + getEpochTIme() + ".json";
		System.out.println("File Name :: " + fileName);
		try (FileWriter file = new FileWriter("/home/asktech/AskTech/Webhook/" + fileName)) {
			// try (FileWriter file = new
			// FileWriter("/home/asktech/AskTech/Webhook/fromPostMan/"+fileName)) {
			file.write(jsonObject.toString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public static String shuffle(String input) {
		List<Character> characters = new ArrayList<Character>();
		for (char c : input.toCharArray()) {
			characters.add(c);
		}
		StringBuilder output = new StringBuilder(input.length());
		while (characters.size() != 0) {
			int randPicker = (int) (Math.random() * characters.size());
			output.append(characters.remove(randPicker));
		}
		return output.toString();
	}

	private static int inc = 0;

	public static long getMerchantsID() {
		// 12 digits.

		long id = Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(1, 13));
		inc = (inc + 1) % 10;
		return id;
	}

	public static String generateAppId() {

		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString().replace("-", "");

		return uuidAsString;
	}

	public static ErrorResponseDto populateErrorDto(FormValidationExceptionEnums fieledNotFound,
			Map<String, Object> extraData, String msg, boolean status, int statusCode) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.getMsg().add(msg);
		errorResponseDto.setStatus(status);
		errorResponseDto.setStatusCode(statusCode);

		return errorResponseDto;
	}

	public static String convertDatetoMySqlDateFormat(String dateIn) throws ParseException {

		DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = originalFormat.parse(dateIn);
		String formattedDate = targetFormat.format(date);

		return formattedDate;
	}

	public static boolean dateValidator(String dateIn, String format) {
		DateFormat originalFormat = new SimpleDateFormat(format);
		try {
			Date date = originalFormat.parse(dateIn);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Integer randomNumberForOtp(int sizeOfOtp) {

		int rand = (new Random()).nextInt(90000000) + 10000000;
		return rand;
	}

	public static boolean checkNumericValue(String strValue) {
		String regex = "[0-9]+";
		return strValue.matches(regex);
	}

	public static ErrorNodalDto populateExceptionResponse(String status, String statusText) {

		ErrorNodalDto er = new ErrorNodalDto();
		er.setMessage(statusText);
		er.setStatus(status);

		return er;

	}

	public static ErrorVANDto populateExceptionResponseVan(String errorCode, String errorMessage) {

		ErrorVANDto er = new ErrorVANDto();
		er.setErrorCode(errorCode);
		er.setErrorMessage(errorMessage);

		return er;

	}

	public static boolean validateIFSCCode(String strIFCS) {
		String regex = "^[A-Z]{4}0[A-Z0-9]{6}$";
		return strIFCS.matches(regex);
	}

	public static int randomInteger() {

		int randomInt = 1;
		for (int i = 0; i < 10; i++) {
			randomInt = getRandomNumberInRange(1, 2);
		}

		return randomInt;
	}

	public static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public static boolean validateIUpiID(String strUPIId) {
		final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^(.+)@(.+)$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(strUPIId);
		return matcher.find();
	}

	public static void main(String args[]) {
		try {
			System.out.println(midnightBlackout("23:53:00", "00:07:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean midnightBlackout(String startMin, String endMin) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String today = formatter.format(date);
		if (isTimeBetween(today + " " + startMin, today + " 23:59:59")) {
			System.out.println(startMin);
			return true;
		}
		if (isTimeBetween(today + " " + "00:00:00", today + " " + endMin)) {
			System.out.println(endMin);
			return true;
		}
		return false;
	}

	public static boolean checkTimePassed(String val, Long timeInSeconds) {
		long dt = (Long.parseLong(val) / 1000);
		long curtime = Instant.now().getEpochSecond();		
		if (curtime - dt > timeInSeconds) {			
			return true;
		}
		return false;
	}

	public static boolean isTimeBetween(String beforeTime, String afterTime) throws ParseException {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String strDate = sdf.format(date);
		// System.out.println(strDate);
		Date date1 = sdf.parse(beforeTime);
		Date date2 = sdf.parse(afterTime);
		Date date3 = sdf.parse(strDate);

		if (date3.before(date1)) {
			return false;
		} else if (date3.after(date2)) {
			return false;
		} else {
			return true;
		}
	}

	public static String doubleFormat(String input) {
		DecimalFormat df = new DecimalFormat("#.##");
		return String.valueOf(df.format(Double.parseDouble(input)));
	}

	public static String base64Encoder(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes());
	}
}
