package io.asktech.payout.service.payg.dto;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddContactErrorResponse {
    private String ResponseCode;
    private String MoreInfoUrl;
    private String Message;
    private String Code;
    private String FieldName;
    private String RequestUniqueId;
    private String DeveloperMessage;
}
