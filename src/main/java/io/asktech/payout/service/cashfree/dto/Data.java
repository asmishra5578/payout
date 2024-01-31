package io.asktech.payout.service.cashfree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    private String utr;
    private String nameAtBank;
    private String city;
    private String nameMatchScore;
    private String micr;
    private String amountDeposited;
    private String bankName;
    private String refId;
    private String branch;
}
