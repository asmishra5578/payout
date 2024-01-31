package io.asktech.payout.service.kitzone.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferNew {
    public String referenceId;
    public String beneId;
    public String amount;
    public String status;
    public String addedOn;
    public String processedOn;
    public Object reason;
    public String transferMode;
    public int acknowledged; 
}
