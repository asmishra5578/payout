package io.asktech.payout.service.razor.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bank_accountResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("ifsc")
    private String ifsc;
    @JsonProperty("account_number")
    private String account_number;
    @JsonProperty("bank_name")
    private String bank_name;
    @JsonProperty("notes")
    private List<String> notes;
}
