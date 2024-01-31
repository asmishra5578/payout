package io.asktech.payout.modal.admin;

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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRecharge extends AbstractTimeStampAndId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String amount;
    @Column(unique = true)
    private String utrid;

    private String merchantId;
    private String rechargeType;
    private String commission;
    private String referenceId;
    private String rechargeId;
    private String bankName;
    private String referenceName;
    private String walletId;
    private String mainWalletId;
    private String note1;
    private String note2;
    private String note3;
    private String status;
    private String rechargeAgent;
    private String rechargeAgentName;
}
