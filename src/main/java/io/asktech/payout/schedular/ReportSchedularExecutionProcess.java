package io.asktech.payout.schedular;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.asktech.payout.reports.customInterface.DataRepository;
import io.asktech.payout.reports.dto.ReportData;
import io.asktech.payout.reports.enums.ReportStatus;
import io.asktech.payout.reports.enums.ReportType;
import io.asktech.payout.reports.model.ReportTransactionDetails;
import io.asktech.payout.reports.repository.ReportTransactionDetailsRepository;
import io.asktech.payout.reports.util.CSVExporterWithFileCreate;
import io.asktech.payout.reports.util.ExcelExporterWithFileCreate;
import io.asktech.payout.utils.Utility;



@Service
public class ReportSchedularExecutionProcess {

	static Logger logger = LoggerFactory.getLogger(ReportSchedularExecutionProcess.class);

	@Value("${reportArgument.reportPath}")
	private String reportFolderPath;
	@Value("${reportArgument.reportFileExpiry}")
	private int reportFileExpiry;

	@Autowired
	ReportTransactionDetailsRepository reportTransactionDetailsRepository;
	@Autowired
	ExcelExporterWithFileCreate excelExporterWithFileCreate;
	@Autowired
	CSVExporterWithFileCreate cSVExporterWithFileCreate;
	@Autowired
	DataRepository dataRepository;

	public void reportExecution() throws ParseException, IOException {

		List<ReportTransactionDetails> listReportTransactionDetails = reportTransactionDetailsRepository
				.findAllByReportExecuteStatusAndReportType(ReportStatus.PENDING.toString(),
						ReportType.SCHEDULE.toString());

		for (ReportTransactionDetails reportTransactionDetails : listReportTransactionDetails) {

			try {
				logger.info("Report Name :: " + reportTransactionDetails.getReportName());
				List<ReportData> datas = dataRepository.executeQuery(reportTransactionDetails.getReportQuery());
				
				if(reportTransactionDetails.getReportExportType().contains("EXCEL")){

					if(datas.size() <=500) {
						
						ExcelExporterWithFileCreate excelExporterWithFileCreate = new ExcelExporterWithFileCreate(datas);
						String reportFileName = reportFolderPath + reportTransactionDetails.getReportName()
								+ Utility.getEpochTIme() + ".xlsx";
		
						excelExporterWithFileCreate.export(reportTransactionDetails.getReportHeader(), reportFileName,
								reportTransactionDetails.getReportName());
						updateReportTransaction(reportTransactionDetails, ReportStatus.COMPLETED.toString(), reportFileName);
					}else {
						
						String reportFileName = reportTransactionDetails.getReportName() + Utility.getEpochTIme() + ".csv";
						logger.info(" reportFileName :: "+reportFileName);
						cSVExporterWithFileCreate.exportCSVFiles(datas, reportFileName, reportTransactionDetails.getReportHeader(), reportFolderPath);
						
						updateReportTransaction(reportTransactionDetails, ReportStatus.COMPLETED.toString(), reportFileName);
					}
				}else if(reportTransactionDetails.getReportExportType().contains("CSV")){
					
					String reportFileName = reportTransactionDetails.getReportName() + Utility.getEpochTIme() + ".csv";
					cSVExporterWithFileCreate.exportCSVFiles(datas, reportFileName, reportTransactionDetails.getReportHeader(), reportFolderPath);
					
					updateReportTransaction(reportTransactionDetails, ReportStatus.COMPLETED.toString(), reportFileName);
				}
				
			} catch (Exception e) {
				
				logger.error("Exception :: "+e);
				
				updateReportTransaction(reportTransactionDetails, ReportStatus.FAILED.toString(), null);
			}
			
		}

	}

	private void updateReportTransaction(ReportTransactionDetails reportTransactionDetails, String status,
			String reportFilePath) {
		reportTransactionDetails.setReportExecuteStatus(status);
		reportTransactionDetails.setReportPath(reportFilePath);
		reportTransactionDetailsRepository.save(reportTransactionDetails);

	}

}
