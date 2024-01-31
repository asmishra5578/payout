package io.asktech.payout.dto.admin;
import io.asktech.payout.modal.merchant.MerchantPgConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PgConfigResponse {
    private String status;
    private MerchantPgConfig pgres;
}
