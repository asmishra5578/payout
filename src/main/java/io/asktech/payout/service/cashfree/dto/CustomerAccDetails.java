package io.asktech.payout.service.cashfree.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAccDetails {
    private String name;
    private String phone;
    private String bankAccount;
    private String ifsc;
}
