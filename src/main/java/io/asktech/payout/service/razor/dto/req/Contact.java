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
public class Contact {
   @JsonProperty("id")
   private String id;
   @JsonProperty("entity")
   private String entity;
   @JsonProperty("name")
   private String name;
   @JsonProperty("contact")
   private String contact;
   @JsonProperty("email")
   private String email;
   @JsonProperty("type")
   private String type;
   @JsonProperty("reference_id")
   private String reference_id;
   @JsonProperty("batch_id")
   private String batch_id;
  
   @JsonProperty("active")
   private String active;
   @JsonProperty("notes")
   private Notes notes;
   @JsonProperty("created_at")
   private String created_at;

}
