package io.asktech.payout.dto.admin;

import io.asktech.payout.modal.merchant.PgDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PgResponse {
    private String status;
    private PgDetails pgres;
}
