package io.asktech.payout.reports.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.reports.customInterface.DataRepository;
import io.asktech.payout.reports.dto.ReportData;
import io.asktech.payout.reports.dto.ReportDownloadTrx;
import io.asktech.payout.reports.dto.ReportRequestDTO;
import io.asktech.payout.reports.dto.ReportTransactionSuccessRequest;
import io.asktech.payout.reports.dto.TransactionReportRequest;
import io.asktech.payout.reports.enums.ReportStatus;
import io.asktech.payout.reports.enums.ReportType;
import io.asktech.payout.reports.model.ReportMaster;
import io.asktech.payout.reports.model.ReportTransactionDetails;
import io.asktech.payout.reports.repository.ReportMasterRepository;
import io.asktech.payout.reports.repository.ReportTransactionDetailsRepository;
import io.asktech.payout.reports.util.CSVExporterWithFileCreate;
import io.asktech.payout.reports.util.ExcelExporterWithFileCreate;
import io.asktech.payout.reports.util.GeneralUtils;
import io.asktech.payout.utils.Utility;



@Service
public class ReportService implements ErrorValues {

	static Logger logger = LoggerFactory.getLogger(ReportService.class);

	@Value("${reportArgument.reportPath}")
	private String reportFolderPath;
	@Value("${reportArgument.reportFileExpiry}")
	private int reportFileExpiry;
	@Value("${apiReportEndPoint}")
	private String apiReportEndPoint;

	@Autowired
	ReportMasterRepository reportMasterRepository;
	@Autowired
	ExcelExporterWithFileCreate excelExporterWithFileCreate;
	@Autowired
	DataRepository dataRepository;
	@Autowired
	ReportTransactionDetailsRepository reportTransactionDetailsRepository;
	@Autowired
	CSVExporterWithFileCreate cSVExporterWithFileCreate;

	private String sql_query = "";
	private String reportFileName = "";

	public String transactionReportService(ReportRequestDTO reportRequestDTO, String uuid)
	throws ValidationExceptions, ParseException, IOException, NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {


// check for report file name is 
ReportMaster reportMaster = null;
if(reportRequestDTO.getReportName().isEmpty() && reportRequestDTO.getReportName() == null) {
	throw new ValidationExceptions(REPORT_NAME_EMPTY_NULL, FormValidationExceptionEnums.REPORT_NAME_EMPTY_NULL);
}else if(reportRequestDTO.getReportParam1().isEmpty() && reportRequestDTO.getReportParam1() == null ){
	
	throw new ValidationExceptions(START_DATE_ERROR, FormValidationExceptionEnums.START_DATE_ERROR);
}else if(reportRequestDTO.getReportParam2().isEmpty() && reportRequestDTO.getReportParam2() == null) {
	throw new ValidationExceptions(END_DATE_ERROR, FormValidationExceptionEnums.END_DATE_ERROR);
	
}else if(!Utility.validateJavaDateFormat(reportRequestDTO.getReportParam1())){
	throw new ValidationExceptions(DATE_FORMAT, FormValidationExceptionEnums.DATE_FORMAT);
}else if(!Utility.validateJavaDateFormat(reportRequestDTO.getReportParam2())){
	throw new ValidationExceptions(DATE_FORMAT, FormValidationExceptionEnums.DATE_FORMAT);
}

reportMaster = reportMasterRepository.findByReportName(reportRequestDTO.getReportName());

Object obj = reportRequestDTO;
if(reportMaster != null) {
	sql_query = sqlBuilder(obj, reportMaster);
}else {
	throw new ValidationExceptions(REPORT_NOT_FOUND, FormValidationExceptionEnums.REPORT_NOT_FOUND); 
}


return reportExecutionProcess(reportMaster, uuid, sql_query,reportRequestDTO);
}

	public String transactionReportSuccessService(ReportTransactionSuccessRequest reportTransactionSuccessRequest,
			String uuid) throws ValidationExceptions, ParseException, IOException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		ReportMaster reportMaster = reportMasterRepository
				.findByReportName(reportTransactionSuccessRequest.getReportName());

		if (reportMaster == null) {
			throw new ValidationExceptions(REPORT_NOT_FOUND, FormValidationExceptionEnums.REPORT_NOT_FOUND);
		}

		Object obj = reportTransactionSuccessRequest;

		sql_query = sqlBuilder(obj, reportMaster);

		return reportExecutionProcess(reportMaster, uuid, sql_query,null);
	}
		
	
	private String reportExecutionProcess(ReportMaster reportMaster, String uuid, String sql_query,ReportRequestDTO reportRequestDTO) throws ParseException, IOException {

		ReportTransactionDetails reportTransactionDetails = createReportTransaction(uuid,ReportStatus.PENDING.toString(), reportMaster.getReportName(), reportMaster.getReportType(),
				reportFolderPath, sql_query, reportMaster.getHeaderNames(),reportRequestDTO.getReportParam1(),reportRequestDTO.getReportParam2(),reportRequestDTO.getReportParam3(),reportRequestDTO.getReportParam4(),reportRequestDTO.getReportParam5(),reportRequestDTO.getReportParam6(),reportMaster.getReportExportType());

		if (reportMaster.getReportType().contains(ReportType.DOWNLOAD.toString())) {

			List<ReportData> datas = dataRepository.executeQuery(sql_query);
			
			
			
			if(reportMaster.getReportExportType().contains("EXCEL")){

				if(datas.size() >500) {
					
					reportFileName = reportMaster.getReportName() + Utility.getEpochTIme() + ".csv";
					cSVExporterWithFileCreate.exportCSVFiles(datas, reportFileName, reportMaster.getHeaderNames(), reportFolderPath);
					
					updateReportTransaction(reportTransactionDetails.getId(), ReportStatus.COMPLETED.toString(), reportFileName);
					
				}else {
					ExcelExporterWithFileCreate excelExporterWithFileCreate = new ExcelExporterWithFileCreate(datas);
					reportFileName = reportMaster.getReportName() + Utility.getEpochTIme() + ".xlsx";
	
					excelExporterWithFileCreate.export(reportMaster.getHeaderNames(), reportFolderPath + reportFileName, reportMaster.getReportName());
					updateReportTransaction(reportTransactionDetails.getId(), ReportStatus.COMPLETED.toString(), reportFileName);
				}
				
			}else if(reportMaster.getReportExportType().contains("CSV")){
				
				reportFileName = reportMaster.getReportName() + Utility.getEpochTIme() + ".csv";
				cSVExporterWithFileCreate.exportCSVFiles(datas, reportFileName, reportMaster.getHeaderNames(), reportFolderPath);
				
				updateReportTransaction(reportTransactionDetails.getId(), ReportStatus.COMPLETED.toString(), reportFileName);
			}

			return apiReportEndPoint + reportFileName;
		}

		return "The report has been schedule , once executed it will be reflected in your account.";

	}
	private String sqlBuilder(Object obj, ReportMaster reportMaster) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, ValidationExceptions {

		String[] arrOfManParameter = reportMaster.getMandatoryParameter().split("\\^");
		String[] arrOfManParameterType = reportMaster.getMandatoryPrType().split("\\^");
		String[] arrOfParameters = reportMaster.getParameters().split("\\^");
		String[] arrOfCondisons = reportMaster.getQueryFilter().split("\\^");
		logger.info(reportMaster.getReportQuery()+"************");

		boolean flagForOperator = false;
		logger.info(sql_query+"************");

		for (int i = 0; i < arrOfManParameter.length; i++) {
			if (arrOfManParameterType[i].equalsIgnoreCase("DATE")) {

				Method m = obj.getClass().getMethod("get" + GeneralUtils.convertString("reportParam"+String.valueOf(i+1)));
				if (!GeneralUtils.dateValidator(String.valueOf(m.invoke(obj)))) {
					throw new ValidationExceptions(DATE_FORMAT, FormValidationExceptionEnums.DATE_FORMAT);
				}
			}
		}
		logger.info(reportMaster.getReportQuery()+"***r2*********");
		logger.info(sql_query+"******2******");

		// SQLQuery Builder
		sql_query = reportMaster.getReportQuery();
		logger.info(sql_query+"******3******");
		logger.info(arrOfParameters.length+"******3******");
		for (int i = 0; i < arrOfParameters.length; i++) {

			Method m = obj.getClass().getMethod("get" + GeneralUtils.convertString("reportParam"+String.valueOf(i+1)));
			String value = String.valueOf(m.invoke(obj));
			if (value == "" || value == null || value.equalsIgnoreCase("null")) {
				
			} else {
				
				String condisionUpdate = arrOfCondisons[i];
				logger.info("Inside arrOfParameters else block :: "+condisionUpdate);
				
				if(!flagForOperator) {
					logger.info(" Previous Query IF :: "+ sql_query);
					//sql_query = sql_query+" " +arrOfParameters[i] + "=" +condisionUpdate.replace(":" + arrOfParameters[i], "'" + value + "'");
					sql_query = sql_query+" " +condisionUpdate.replace(":" + arrOfParameters[i], "'" + value + "'");
					flagForOperator=true;
				}else {
					if(arrOfParameters[i].contains("Date") || arrOfParameters[i].contains("date")) {
						sql_query = sql_query+" and " +condisionUpdate.replace(":" + arrOfParameters[i], "'" + value + "'");
					}else {
						logger.info(" Previous Query else :: "+ sql_query);
						sql_query = sql_query+" and "+arrOfParameters[i] + "=" +condisionUpdate.replace(":" + arrOfParameters[i], "'" + value + "'");
						//sql_query = sql_query+" and " +condisionUpdate.replace(":" + arrOfParameters[i], "'" + value + "'");
					}
				}
			}
		}
		logger.info(sql_query + " :::: ******4******");

		if(!StringUtils.isEmpty(reportMaster.getAdditionalPartQuery())) {
			sql_query = sql_query+reportMaster.getAdditionalPartQuery();
		}
		
		logger.info("sql_query :: "+sql_query);
		return sql_query;
	}

	
	private ReportTransactionDetails createReportTransaction(String uuid, String status, String reportName,
			String reportType, String folderName, String reportQuery, String reportHeader ,String reportParam1,String reportParam2,String reportParam3,String reportParam4,String reportParam5,String reportParam6,String reportExportType) {
		ReportTransactionDetails reportTransactionDetails = new ReportTransactionDetails();

		reportTransactionDetails.setCreatedBy(uuid);
		reportTransactionDetails.setReportExecuteStatus(status);
		reportTransactionDetails.setReportName(reportName);
		reportTransactionDetails.setReportType(reportType);
		reportTransactionDetails.setReportValidity(reportFileExpiry);
		reportTransactionDetails.setFolderName(folderName);
		reportTransactionDetails.setReportQuery(reportQuery);
		reportTransactionDetails.setReportHeader(reportHeader);
		reportTransactionDetails.setReportParam1(reportParam1);
		reportTransactionDetails.setReportParam2(reportParam2);
		reportTransactionDetails.setReportParam3(reportParam3);
		reportTransactionDetails.setReportParam4(reportParam4);
		reportTransactionDetails.setReportParam5(reportParam5);
		reportTransactionDetails.setReportParam6(reportParam6);
		reportTransactionDetails.setReportExportType(reportExportType);
		

		return reportTransactionDetailsRepository.save(reportTransactionDetails);
	}

	private void updateReportTransaction(long reportId, String status, String reportFilePath) {

		ReportTransactionDetails reportTransactionDetails = reportTransactionDetailsRepository.findById(reportId);

		reportTransactionDetails.setReportExecuteStatus(status);
		reportTransactionDetails.setReportPath(reportFilePath);
		reportTransactionDetailsRepository.save(reportTransactionDetails);

	}

	public File fileDownloadUtility(String fileName) throws ValidationExceptions {
		ReportTransactionDetails reportTransactionDetails = reportTransactionDetailsRepository
				.findByReportPath(fileName);
		File file = new File(reportTransactionDetails.getFolderName() + fileName);
		if (!file.exists()) {
			throw new ValidationExceptions(FILE_NOT_FOUND, FormValidationExceptionEnums.FILE_NOT_FOUND);
		}
		return file;
	}

	public static void main(String[] args) throws Exception {
		Object o = populateValue();
		Method m = o.getClass().getMethod("getOrderId");
		System.out.println(m.invoke(o));
	}

	public static TransactionReportRequest populateValue() {
		TransactionReportRequest transactionReportRequest = new TransactionReportRequest();
		transactionReportRequest.setFromDate("2022-08-10");

		return transactionReportRequest;

	}

	public List<ReportMaster> getAllReportDetails(){
		return reportMasterRepository.findAll();
	}
		public List<ReportTransactionDetails> getAllReportTransactionDownloadLinkList(){
		return reportTransactionDetailsRepository.findAll();
	}
	
	
	public List<ReportDownloadTrx> getAllReportTrList(String reportName){
		List<ReportTransactionDetails> rtx= reportTransactionDetailsRepository.findAllByReportName(reportName);
		List<ReportDownloadTrx> rdtList=new ArrayList<>();
for (ReportTransactionDetails reportTransactionDetails : rtx) {
	ReportDownloadTrx rt=new ReportDownloadTrx();
   rt.setCreatedBy(reportTransactionDetails.getCreatedBy());
   rt.setReportExecuteStatus(reportTransactionDetails.getReportExecuteStatus());
   rt.setReportName(reportTransactionDetails.getReportName());
   rt.setReportPath(reportTransactionDetails.getReportPath());
   rt.setFolderName(reportTransactionDetails.getFolderName());
   rt.setFolderName(reportTransactionDetails.getFolderName());
   rt.setReportParam1(reportTransactionDetails.getReportParam1());
   rt.setReportParam2(reportTransactionDetails.getReportParam2());
   rt.setReportParam3(reportTransactionDetails.getReportParam3());
   rt.setReportParam4(reportTransactionDetails.getReportParam4());
   rt.setReportParam5(reportTransactionDetails.getReportParam5());
   rt.setReportType(reportTransactionDetails.getReportType());
   rt.setUpdatedBy(reportTransactionDetails.getUpdatedBy());
   rdtList.add(rt);
}
return rdtList; 
	}
}
