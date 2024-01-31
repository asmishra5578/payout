package io.asktech.payout.schedular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PayoutSchedularService {

	static Logger logger = LoggerFactory.getLogger(PayoutSchedularService.class);

	@Autowired
	PayoutStatusAPIWallet PayoutStatusAPIWallet;
	@Autowired
	PayoutPaymentAPI payoutPaymentAPI;

	// @Scheduled(cron = "0 0/1 * * * ?")
	// public void paytmSchedularProcess() throws Exception {
	// 	logger.info("Status check Schedular will initiate the Processing ....");
	// 	PayoutStatusAPIWallet.updateWalletTransactionStatus();
	// 	logger.info("Status check Schedular END for this cycle ....");
	// }

	@Scheduled(cron = "0 0/1 * * * ?")
	public void paytmSchedularProcess2() throws Exception {
	logger.info("Send Callback Schedular will initiate the Processing ....");
	PayoutStatusAPIWallet.sendFlaggedCallback();
	logger.info("Send Callback Schedular END for this cycle ....");
	}

	// @Scheduled(cron = "0 0/1 * * * ?")
	// public void paytmSchedularProcessHourly() throws Exception {
	// logger.info("Hourly status check Schedular will initiate the Processing....");
	// PayoutStatusAPIWallet.updateWalletTransactionStatusHourly();
	// logger.info("Hourly status check Schedular END for this cycle ....");
	// }

	// //
	// ============================================================================================
	// // Process start for Payout only to process request and deduction

	/* Deduction/ Refund API Schedular thread */

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor1() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(1);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor2() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(2);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor3() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(3);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor4() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(4);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor5() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(5);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor6() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(6);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor7() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(7);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor8() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(8);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor9() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(9);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runBalanceProcessor10() throws Exception {
		logger.info("BalanceDeduction Schedular will initiate the Processing ....");

		payoutPaymentAPI.balanceDeductionProcessor(10);
		logger.info("BalanceDeduction Schedular END for this cycle ....");
	}
	// // // // user_session

	// // /* Deduction / REFUND API Schedular thread END */

	// // /* Transfer API API Schedular thread */

	@Scheduled(fixedDelay = 3000)
	public void runAccountTransfer1() throws Exception {
		logger.info("AccountTransfer1 Schedular will initiate the Processing ....");

		payoutPaymentAPI.accountTransferProcessor(1);
		logger.info("AccountTransfer1 Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runAccountTransfer2() throws Exception {
		logger.info("AccountTransfer2 Schedular will initiate the Processing ....");

		payoutPaymentAPI.accountTransferProcessor(2);
		logger.info("AccountTransfer2 Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runAccountTransfer3() throws Exception {
		logger.info("AccountTransfer3 Schedular will initiate the Processing ....");

		payoutPaymentAPI.accountTransferProcessor(3);
		logger.info("AccountTransfer3 Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runAccountTransfer4() throws Exception {
		logger.info("AccountTransfer4 Schedular will initiate the Processing ....");

		payoutPaymentAPI.accountTransferProcessor(4);
		logger.info("AccountTransfer4 Schedular END for this cycle ....");
	}

	@Scheduled(fixedDelay = 3000)
	public void runAccountTransfer5() throws Exception {
		logger.info("AccountTransfer5 Schedular will initiate the Processing ....");

		payoutPaymentAPI.accountTransferProcessor(5);
		logger.info("AccountTransfer5 Schedular END for this cycle ....");
	}

	// @Scheduled(fixedDelay = 3000)
	// public void runUPITransfer1() throws Exception {
	// logger.info("UPI Schedular will initiate the Processing ....");
	// payoutPaymentAPI.upiTransferProcessor(1);
	// logger.info("UPI Schedular END for this cycle ....");
	// }

	// @Scheduled(fixedDelay = 3000)
	// public void runUPITransfer2() throws Exception {
	// logger.info("UPI Schedular will initiate the Processing ....");
	// payoutPaymentAPI.upiTransferProcessor(2);
	// logger.info("UPI Schedular END for this cycle ....");
	// }

	// @Scheduled(fixedDelay = 3000)
	// public void runUPITransfer3() throws Exception {
	// logger.info("UPI Schedular will initiate the Processing ....");
	// payoutPaymentAPI.upiTransferProcessor(3);
	// logger.info("UPI Schedular END for this cycle ....");
	// }

	// @Scheduled(fixedDelay = 3000)
	// public void runUPITransfer4() throws Exception {
	// logger.info("UPI Schedular will initiate the Processing ....");
	// payoutPaymentAPI.upiTransferProcessor(4);
	// logger.info("UPI Schedular END for this cycle ....");
	// }

	// @Scheduled(fixedDelay = 3000)
	// public void runUPITransfer5() throws Exception {
	// logger.info("UPI Schedular will initiate the Processing ....");
	// payoutPaymentAPI.upiTransferProcessor(5);
	// logger.info("UPI Schedular END for this cycle ....");
	// }

}
