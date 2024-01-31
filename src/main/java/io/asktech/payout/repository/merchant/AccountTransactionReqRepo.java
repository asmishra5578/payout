package io.asktech.payout.repository.merchant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.asktech.payout.modal.merchant.AccountTransactionReq;


public interface AccountTransactionReqRepo extends JpaRepository<AccountTransactionReq, String>{

	List<AccountTransactionReq> findByMerchantIdAndOrderId(String merchantId, String orderid);
	List<AccountTransactionReq> findByRequestStatus(String reqStatus);
	
	List<AccountTransactionReq> findByRequestStatusAndThreadFlag(String reqStatus,int threadFlag);
	
    @Query(value = "UPDATE AccountTransactionReq SET requestStatus = :setStatus where requestStatus = :reqStatus")
    @Modifying
    int updateAccountTransactionReqStatusByStatus(String setStatus, String reqStatus);
	AccountTransactionReq findByInternalOrderId(String orderid);
	List<AccountTransactionReq> findByRequestStatusAndMerchantThreadFlag(String string, int merchantThreadFlag);

}
