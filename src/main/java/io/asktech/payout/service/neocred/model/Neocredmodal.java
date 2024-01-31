package io.asktech.payout.service.neocred.model;
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
    
    @Index(columnList = "orderId")
})
public class Neocredmodal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50)
    private String merchantId;
    @Column(length = 100)
    private String amount;
    @Column(length = 100)
    private String orderId;
    @Column(length = 50)
    private String username;
    @Column(length = 50)
    private String password;
    @Column(length = 100)
    private String pgOrderId;
    @Column(length = 100)
    private String pgTransId;
    @Column(length = 100)
    private String pgCustId;
    @Column(length = 100)
    private String token;
    @Column(columnDefinition = "LONGTEXT")
    private String Req;

    @Column(columnDefinition = "LONGTEXT")
    private String Res;
    @Column(columnDefinition = "LONGTEXT")
    private String timestamp;
   }
