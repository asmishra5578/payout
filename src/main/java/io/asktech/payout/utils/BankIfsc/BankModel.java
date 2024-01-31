package io.asktech.payout.utils.BankIfsc;

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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "ifsc"),
        @Index(columnList = "bankName")
})
public class BankModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 80)
    private String ifsc;
    @Column(length = 150)
    private String BankName;
    @Column(columnDefinition = "text")
    private String Office;
    @Column(columnDefinition = "text")
    private String Address;
    private String District;
    private String City;
    private String State;
    private String Phone;
}
