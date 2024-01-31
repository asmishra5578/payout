package io.asktech.payout.service.cashfree.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthDetails {
    private String ClientId;
    private String ClientSecret;
}
