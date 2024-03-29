package io.asktech.payout.dto.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionChangeResponce {
    private String orderIds;
    private String status;
    private int totalCount;
    private String comment;
    private int failCount;
    private int sucessCount;
    private String rechargeAgent;
    private String uuid;
    private List<TransactionChangeResponceList> transactionChangeResponceList;

}
