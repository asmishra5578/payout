package io.asktech.payout.dto.admin;

public interface IPgTypeAndCountByStatusAndDate {
    String getTransactionType();
    String getCnt();
    String getTotalAmt();
    String getPgname();
    String getTransactionStatus();
    
}
