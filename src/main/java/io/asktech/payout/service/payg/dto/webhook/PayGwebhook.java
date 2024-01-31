package io.asktech.payout.service.payg.dto.webhook;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayGwebhook {
    private String PaymentTransactionId;
    private String UniqueRequestId;
    private String OrderNotes;
    private String OrderKeyId;
    private String PaymentDateTime;
    private OrderPaymentCustomerData OrderPaymentCustomerData;
    private String OrderStatus;
    private String OrderType;
    private String PaymentApprovalCode;
    private String OrderPaymentTransactionDetail;
    private String UpiLink;
    private String OrderPaymentStatus;
    private String OrderAmount;
    private String MerchantKeyId;
    private String OrderPaymentStatusText;
    private String PaymentStatus;
    private String PaymentTransactionRefNo;
    private UserDefinedData UserDefinedData;
    private String PaymentAccount;
    private String PaymentResponseCode;
    private String PaymentProcessUrl;
    private String OrderId;
    private String PaymentResponseText;
    private String PaymentMethod;
    private String UpdatedDateTime;
}
