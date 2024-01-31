package io.asktech.payout.service.payg.modals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.asktech.payout.modal.van.AbstractTimeStampAndId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Table;
import javax.persistence.Index;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(indexes = { 
    @Index(columnList = "eazyMerchantId"),
    @Index(columnList = "eazyOrderId"),
    @Index(columnList = "uniqueRequestId")
})
public class PaygResponseDetails extends AbstractTimeStampAndId{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    private String status;
    private String totalFeeAmount;
    private String buyAmount;
    private String transactionType;
    private String responseCode;
    @Column(length = 80)
    private String uniqueRequestId;
    private String amount;
    @Column(length = 80)
    private String payoutCustomerkeyId;
    private String gSTAmount;
    private String batchId;
    private String payOutKeyId;
    private String payOutBeneficiaryKeyId;
    private String payOutType;
    private String feeCode;
    private String transactionId;
    private String payOutDate;
    private String additionalFee;
    private String buyGSTAmount;
    private String paymentType;
    private String responseText;
    private String updatedDateTime;
    private String gSTPercentage;
    @Column(length = 80)
    private String merchantKeyId;
    private String feeAmount;
    @Column(length = 80)
    private String eazyMerchantId;
    @Column(unique = true, length = 80)
    private String eazyOrderId;
}
