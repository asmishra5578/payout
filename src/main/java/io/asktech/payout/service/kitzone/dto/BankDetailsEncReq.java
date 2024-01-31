package io.asktech.payout.service.kitzone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankDetailsEncReq {
    public String bankAccount;
    public String ifsc;
    public String name;
    public String email;
    public String phone;
    public String vpa;
    public String address1;
}