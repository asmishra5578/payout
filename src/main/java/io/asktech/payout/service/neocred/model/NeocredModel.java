package io.asktech.payout.service.neocred.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import io.asktech.payout.service.neocred.dto.SettlementReqDto;
import io.asktech.payout.service.neocred.dto.SettlementResDto;
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
public class NeocredModel {
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
    private String accessKey;
    @Column(length = 300)
    private String accessSecret;
    @Column(length = 100)
    private String trxType;
    @Column(length = 100)
    private String utrId;
    @Column(length = 300)
    private String status;
    @Column(length = 300)
    private String statusMessage;
    @Column(columnDefinition = "LONGTEXT")
    private String AuthRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String AuthResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String  PaymentRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String PaymentResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String StatusRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String StatusResponse;
}
