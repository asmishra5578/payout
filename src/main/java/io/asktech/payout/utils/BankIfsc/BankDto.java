package io.asktech.payout.utils.BankIfsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDto {
    private String Ifsc;
    private String BankName;
    private String Office;
    private String Address;
    private String District;
    private String City;
    private String State;
    private String Phone;
}
