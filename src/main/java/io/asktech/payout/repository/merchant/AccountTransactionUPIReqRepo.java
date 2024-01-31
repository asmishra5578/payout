package io.asktech.payout.repository.merchant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.AccountTransactionUPIReq;


public interface AccountTransactionUPIReqRepo extends JpaRepository<AccountTransactionUPIReq, String>{

	List<AccountTransactionUPIReq> findByMerchantIdAndOrderId(String merchantId, String orderId);
	
List<AccountTransactionUPIReq> findByRequestStatus(String reqStatus);
List<AccountTransactionUPIReq> findByRequestStatusAndThreadFlag(String reqStatus,int threadFlag);
	
AccountTransactionUPIReq findByInternalOrderId(String orderid);

List<AccountTransactionUPIReq> findByRequestStatusAndMerchantThreadFlag(String string, int merchantThreadFlag);

}
