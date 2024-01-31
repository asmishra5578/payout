package io.asktech.payout.service.sark.models;

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
public class SarkReqRes extends AbstractTimeStampAndId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition="TEXT")
    private String reqString;
    @Column(columnDefinition="TEXT")
    private String resString;
    private String txnTime;
    private String message;
    private String status;
    private String txnId;
    private String uniqueRefId;
    @Column(unique=true)
    private String orderId;
    private String bankRef;
    @Column(columnDefinition="TEXT")
    private String statusString;
}
