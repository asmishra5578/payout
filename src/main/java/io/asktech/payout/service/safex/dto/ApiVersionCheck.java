package io.asktech.payout.service.safex.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiVersionCheck {
    
   private Header header;
   private Transaction transaction;
}
