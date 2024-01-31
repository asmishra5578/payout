package io.asktech.payout.service.axis.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetail {
    public String txnPaymode;
    public String custUniqRef;
    public String corpAccNum;
    public String valueDate;
    public String txnAmount;
    public String beneLEI;
    public String beneName;
    public String beneCode;
    public String beneAccNum;
    public String beneAcType;
    public String beneAddr1;
    public String beneAddr2;
    public String beneAddr3;
    public String beneCity;
    public String beneState;
    public String benePincode;
    public String beneIfscCode;
    public String beneBankName;
    public String baseCode;
    public String chequeNumber;
    public String chequeDate;
    public String payableLocation;
    public String printLocation;
    public String beneEmailAddr1;
    public String beneMobileNo;
    public String productCode;
    public String txnType;
    public String enrichment1;
    public String enrichment2;
    public String enrichment3;
    public String enrichment4;
    public String enrichment5;
    public String senderToReceiverInfo;
}
