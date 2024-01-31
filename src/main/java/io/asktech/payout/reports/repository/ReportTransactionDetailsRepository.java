package io.asktech.payout.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.asktech.payout.reports.model.ReportTransactionDetails;

@Repository
public interface ReportTransactionDetailsRepository extends JpaRepository<ReportTransactionDetails, String>{

	List<ReportTransactionDetails> findAllByReportExecuteStatusAndReportType(String string, String string2);

	ReportTransactionDetails findById(long reportId);

	ReportTransactionDetails findByReportPath(String fileName);
	
	List<ReportTransactionDetails> findAllByReportName(String reportName);
	
}
