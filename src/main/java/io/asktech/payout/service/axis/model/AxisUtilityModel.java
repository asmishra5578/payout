package io.asktech.payout.service.axis.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
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
    // @Index(columnList = "beneBankAccount"),
    @Index(columnList = "orderId")
})
public class AxisUtilityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50)
    private String merchantId;
    @Column(length = 50)
    private String beneCode;
    @Column(length = 50, unique = true)
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
    private String BenificaryRequest;

    @Column(columnDefinition = "LONGTEXT")
    private String BenificaryRequestDecrypt;
    @Column(columnDefinition = "LONGTEXT")
    private String BenificaryResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String BenificaryResponseDcrypt; 
    @Column(columnDefinition = "LONGTEXT")
    private String paymentRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String paymentResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String paymentRequestDcrypt;
    @Column(columnDefinition = "LONGTEXT")
    private String paymentResponseDcrypt;
    
    @Column(columnDefinition = "LONGTEXT")
    private String statusRequest;
    @Column(columnDefinition = "LONGTEXT")
    private String statusResponse;
    @Column(columnDefinition = "LONGTEXT")
    private String statusRequestDcrypt;
    @Column(columnDefinition = "LONGTEXT")
    private String statusResponseDcrypt;

    @Column(columnDefinition = "LONGTEXT")
    private String callBackDcrypt;
    // @Column(columnDefinition = "LONGTEXT")
    // private String callBackDcrypt;




}
