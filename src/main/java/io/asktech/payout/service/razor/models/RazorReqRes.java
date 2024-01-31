package io.asktech.payout.service.razor.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import io.asktech.payout.modal.van.AbstractTimeStampAndId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(columnList = "orderId"),
        @Index(columnList = "razorId")
})
public class RazorReqRes extends AbstractTimeStampAndId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String mode;
    private String account_number;
    private String amount;
    private String reference_id;
    private String ifsc;
    private String vpaId;
    private String account_type;
    @Column(columnDefinition = "TEXT")
    private String ReqString;
    @Column(columnDefinition = "TEXT")
    private String ResString;
    @Column(unique = true, length = 80)
    private String orderId;
    private String utrid;
    @Column(columnDefinition = "TEXT")
    private String statusString;
    private String razorId;
    private String razorFundAccountId;
    private String statusTrx;
    private String razorMerchant_id;
}
