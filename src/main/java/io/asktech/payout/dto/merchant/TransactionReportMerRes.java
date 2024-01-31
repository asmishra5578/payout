package io.asktech.payout.dto.merchant;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionReportMerRes {
	private String status;
	private String statusMessage;
	private List<TransactionReportMerReqResult> result;
}
