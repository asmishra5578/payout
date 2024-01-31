package io.asktech.payout.service.razor.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bank_Account {
    @JsonProperty("name")
    private String name;
    @JsonProperty("ifsc")
    private String ifsc;
    @JsonProperty("account_number")
    private String account_number;

}
