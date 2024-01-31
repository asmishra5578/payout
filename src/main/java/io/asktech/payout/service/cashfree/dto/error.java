package io.asktech.payout.service.cashfree.dto;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class error {
    private Integer subCode;
    private String message;
    private String status;
 
}
