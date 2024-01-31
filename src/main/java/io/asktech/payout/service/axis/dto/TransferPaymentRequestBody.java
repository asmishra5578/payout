package io.asktech.payout.service.axis.dto;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferPaymentRequestBody {
    public String channelId;
    public String corpCode;
    public List<PaymentDetail> paymentDetails;
    public String checksum;

// public String createChecsum(){
//     String cheksum  = channelId+corpCode+paymentDetails.get(0).getTxnPaymode()+paymentDetails.get(0).getCustUniqRef() +paymentDetails.get(0).getCorpAccNum() +paymentDetails.get(0).getValueDate() +paymentDetails.get(0).getTxnAmount();
//     return createChecsum();
// }

}
