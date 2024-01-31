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
        @Index(columnList = "eazyPayOrderId")
})
public class PaygContact extends AbstractTimeStampAndId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userDefined8;
    private String userDefined9;
    private String userDefined6;
    private String email;
    private String userDefined7;
    private String address;
    private String mobileNo2;
    private String userDefined1;
    private String userDefined4;
    private String requestUniqueId;
    private String userDefined5;
    private String userDefined2;
    private String userDefined3;
    private String companyName;
    private String email2;
    private String userDefined10;
    private String merchantKeyId;
    private String mobileNo;
    private String firstName;
    private String zipCode;
    private String customerId;
    private String city;
    private String state;
    private String country;
    private String lastName;
    private String payoutCustomerkeyId;
    private String updatedDatetime;
    @Column(length = 80)
    private String eazyMerchantId;
    @Column(unique = true, length = 80)
    private String eazyPayOrderId;
}
