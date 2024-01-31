package io.asktech.payout.service.safex.models;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(indexes = { 
    @Index(columnList = "orderRefNo"),
    @Index(columnList = "payoutId")
})
public class SafexPayloadBean extends AbstractTimeStampAndId {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String accountHolderName;
    private String payoutDate;
    @Column(length = 80)
    private String orderRefNo;
    private String payoutTime;
    private String txnStatus;
    private String accountNo;
    private String customerId;
    private String txn_InitiateDate;
    private String merid;
    private String ifscCode;
    private String bankTransactionRefNo;
    private String bankRefNo;
    private String statusDesc;
    private String accountType;
    private String count;
    private String aggregatorId;
    private String mobileNo;
    @Column(length = 80)
    private String payoutId;
    private String customerName;
    private String bankStatus;
    private String aggregtorName;
    private String bankResponseCode;
    private String txnAmount;
    private String txnDate;
    private String statusCode;
    private String bankName;
    private String txnType;
}
