package io.asktech.payout.schedular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PayoutReportSchedular {

	static Logger logger = LoggerFactory.getLogger(PayoutReportSchedular.class);

	@Autowired
	ReportSchedularExecutionProcess reportSchedularExecutionProcess;

	@Scheduled(cron="0 0/1 * * * ?")
	public void schedularReportExcution() throws Exception {
	logger.info("Schedular will execute the pending report generation ....");
	reportSchedularExecutionProcess.reportExecution();
	logger.info("Report Execution Schedular END for this cycle ....");
	}

}
