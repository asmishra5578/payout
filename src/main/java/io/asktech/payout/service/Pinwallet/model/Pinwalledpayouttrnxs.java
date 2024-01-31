package io.asktech.payout.service.Pinwallet.model;
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
    // @Index(columnList = "beneBankAccount"),
    @Index(columnList = "orderId")
})
public class Pinwalledpayouttrnxs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50)
    private String merchantId;
    @Column(length = 50)
    private String orderId;
    @Column(length = 100)
    private String amount;
    @Column(length = 100)
    private String pgOrderId;
    @Column(length = 100)
    private String pgTransId;
    @Column(length = 100)
    private String pgCustId;
    @Column(length = 100)
    private String userName;
    @Column(length = 300)
    private String Password;
    @Column(length = 100)
    private String trxType;
    @Column(length = 100)
    private String utrId;
    @Column(length = 300)
    private String status;
    @Column(length = 300)
    private String statusMessage;
    @Column(columnDefinition = "LONGTEXT")
    private String authRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String authResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String  paymentRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String  paymentResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String  statustRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String  statusResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String pinwallettransactionId;
    @Column(columnDefinition = "LONGTEXT")
    private String CallBack;

}
