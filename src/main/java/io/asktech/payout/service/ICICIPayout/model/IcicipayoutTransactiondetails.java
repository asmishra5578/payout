package io.asktech.payout.service.ICICIPayout.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
@Entity
@Table(indexes = {
    @Index(columnList = "orderId")
})
public class IcicipayoutTransactiondetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50)
    private String merchantId;
    @Column(length = 50)
    private String orderId;
    @Column(length = 50)
    private String internalOrderId;
    @Column(length = 100)
    private String amount;
    @Column(length = 100)
    private String pgOrderId;
    @Column(length = 100)
    private String pgTransId;
    @Column(length = 100)
    private String userId;
    @Column(length = 200)
    private String debitaccount;
    @Column(length = 300)
    private String accessSecret;
    @Column(length = 100)
    private String trxType;
    @Column(length = 100)
    private String utrId;
    @Column(length = 300)
    private String status;
     @Column(length = 300)
    private String tx_status;
    @Column(length = 300)
    private String statusMessage;
    @Column(columnDefinition = "LONGTEXT")
    private String errorString;
    @Column(columnDefinition = "LONGTEXT")
    private String encryptPaymentReq;
     @Column(columnDefinition = "LONGTEXT")
    private String tokenreq;
    @Column(columnDefinition = "LONGTEXT")
    private String token;
    @Column(columnDefinition = "LONGTEXT")
    private String PaymentReq;
    @Column(columnDefinition = "LONGTEXT")
    private String PaymentRes; 
    // @Column(columnDefinition = "LONGTEXT")
    // private String paymentRequest;
    // @Column(columnDefinition = "LONGTEXT")
    // private String paymentResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String EncryptStatusReq;
    @Column(columnDefinition = "LONGTEXT")
    private String statusReq;
    @Column(columnDefinition = "LONGTEXT")
    private String statusRes;
    @Column(columnDefinition = "LONGTEXT")
    private String callBackDcrypt;
     @Column(name = "VPAYAPP_ERROR", columnDefinition = "LONGTEXT")
    private String vpayappError;
 }
