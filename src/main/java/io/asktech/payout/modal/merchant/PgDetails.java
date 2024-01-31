package io.asktech.payout.modal.merchant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;

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
    @Index(columnList = "pgId"),
    @Index(columnList = "pgStatus"),
    @Index(columnList = "pgName")
})
public class PgDetails {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    @Column(unique = true)
	private String pgId;
    @Column(unique = true)
    private String pgName;
    private String pgStatus;
    private String pgConfigKey;
    private String pgConfigSecret;
    private String pgConfig1;
    private String pgConfig2;
    private String pgConfig3;
}
