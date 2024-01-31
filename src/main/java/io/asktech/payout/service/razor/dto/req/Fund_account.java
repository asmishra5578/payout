package io.asktech.payout.service.razor.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fund_account {
   @JsonProperty("id")
   private String id;
   @JsonProperty("entity")
   private String entity;
   @JsonProperty("contact_id")
   private String contact_id;
   @JsonProperty("contact")
   private Contact contact;   
   @JsonProperty("account_type")
   private String account_type;
   @JsonProperty("bank_account")
   private Bank_Account bank_Account;
   @JsonProperty("vpa")
   private Vpa vpa;
   @JsonProperty("batch_id")
   private String batch_id;
   @JsonProperty("active")
   private Boolean active;
   @JsonProperty("created_at")
   private String created_at;




}