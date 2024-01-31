package io.asktech.payout.repository.merchant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.WalletTransactionReq;


public interface WalletTransactionReqRepo extends JpaRepository<WalletTransactionReq, String>{

	List<WalletTransactionReq> findByMerchantIdAndOrderId(String merchantId, String orderid);

}
