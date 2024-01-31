package io.asktech.payout.service.payg.modals;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.asktech.payout.modal.van.AbstractTimeStampAndId;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
public class PaygRequestDetails extends AbstractTimeStampAndId {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    private String bankName;
    private String productData;
    private String beneficiaryVerification;
    @Column(length = 80)
    private String uniqueRequestId;
    private String bankCountry;
    private String amount;
    @Column(length = 80)
    private String payoutCustomerkeyId;
    private String beneficiaryName;
    private String bankCode;
    private String payOutType;
    private String accountNumber;
    private String payOutDate;
    private String paymentType;
    private String branchName;
    @Column(length = 80)
    private String merchantKeyId;
    private String kycData;
    @Column(length = 80)
    private String eazyMerchantId;
    @Column(unique = true, length = 80)
    private String eazyOrderId;
}
