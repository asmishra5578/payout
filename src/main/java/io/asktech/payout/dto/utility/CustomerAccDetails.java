package io.asktech.payout.dto.utility;

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
