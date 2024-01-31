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
    @Index(columnList = "orderId")
})
public class SafexRequests extends AbstractTimeStampAndId{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    @Column(columnDefinition="TEXT")
    private String reqFundTransferPayload;
    @Column(columnDefinition="TEXT")
    private String resFundTransferPayload;
    @Column(columnDefinition="TEXT")
    private String reqSessionApiPayload;
    @Column(columnDefinition="TEXT")
    private String resSessionApiPayload;
    @Column(columnDefinition="TEXT")
    private String reqStatusApiPayload;
    @Column(columnDefinition="TEXT")
    private String resStatusApiPayload;
    @Column(length = 80)
    private String orderId;
    private String responseFundTransferId;
    private String responseApiSessionId;
    private String responseStatusApiSessionId;
    private String sessionKey;
    private String trxStatus;
}
