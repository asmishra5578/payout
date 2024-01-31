package io.asktech.payout.service.axis.model;
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
    @Index(columnList = "beneBankAccount"),
    // @Index(columnList = "pgOrderId")
})
public class AxisBeneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String beneCode;
    private String beneBankAccount;
    private String beneName;
    private String beneIfscCode;
    private String userId;
    

}
