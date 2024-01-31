package io.asktech.payout.service.payg.dto.webhook;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderPaymentCustomerData {
    private String MobileNo;
    private String Email;
    private String Address;
    private String FirstName;
    private String State;
    private String ZipCode;
    private String UserId;
    private String Country;
    private String IpAddress;
    private String LastName;
    private String City;
}
