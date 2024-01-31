package io.asktech.payout.dto.admin;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PgUpdateRequest implements Serializable{
	/**
	 * @author abhimanyu-kumar
	 * @Take request for update PG details
	 */
	private static final long serialVersionUID = -8235187394882336398L;
	private String pgId;
    private String pgName;
    private String pgStatus;
    private String pgConfigKey;
    private String pgConfigSecret;
    private String pgConfig1;
    private String pgConfig2;
    private String pgConfig3;

}
