package io.asktech.payout.reports.customInterface;

import java.util.List;

import io.asktech.payout.reports.dto.ReportData;

public interface DataRepository {

	List<ReportData> executeQuery(String query);
	
	
}
