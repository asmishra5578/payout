package io.asktech.payout.reports.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.dto.utility.SuccessResponseDto;
import io.asktech.payout.enums.SuccessCode;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.reports.dto.ReportRequestDTO;
import io.asktech.payout.reports.model.ReportMaster;
import io.asktech.payout.reports.model.ReportTransactionDetails;
import io.asktech.payout.reports.service.ReportService;


@RestController
public class ReportController implements ErrorValues {

	
	@Autowired
	ReportService reportService;


	@PostMapping(value = "report/transactionReport/{uuid}")
	
	public ResponseEntity<?> reportTransaction(@PathVariable("uuid") String uuid,
 			@RequestBody ReportRequestDTO reportRequestDTO) throws ParseException, IOException,
			NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ValidationExceptions {

		// jwtUserValidator.validatebyJwtAdminDetails(uuid);
		String fileName = reportService.transactionReportService(reportRequestDTO, uuid);
		SuccessResponseDto sdto = new SuccessResponseDto();
		sdto.getMsg().add("Request Processed Successfully !");
		sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		sdto.getExtraData().put("file Download Link", fileName);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(sdto);
	}

	@RequestMapping("/download")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("fileName") String fileName)
			throws IOException, ValidationExceptions {

		File file = reportService.fileDownloadUtility(fileName);
		// jwtUserValidator.validatebyJwtAdminDetails(uuid);
		// get the mimetype
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			// unknown mimetype so set the mimetype to application/octet-stream
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());

	}
	

	@GetMapping(value = "report/getAllPayoutReportDetails")
	public ResponseEntity<?> getAllReportDetails(){
		return ResponseEntity.ok().body(reportService.getAllReportDetails());	
	}

	@GetMapping(value = "report/getAllPayoutReportTransactionDownloadLinkList")
	public ResponseEntity<?> getAllReportTransactionDownloadLinkList(){

		return ResponseEntity.ok().body(reportService.getAllReportTransactionDownloadLinkList());	
	}
	@GetMapping(value = "report/getAllReportTrList/{reportName}")
	public ResponseEntity<?> getAllReportTrList(@PathVariable("reportName") String reportName){

		return ResponseEntity.ok().body(reportService.getAllReportTrList(reportName));	
	}
}
