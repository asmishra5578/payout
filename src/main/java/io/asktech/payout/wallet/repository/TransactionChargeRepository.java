package io.asktech.payout.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.wallet.modal.TransactionChargeCommission;


public interface TransactionChargeRepository extends JpaRepository<TransactionChargeCommission, String>{

	List<TransactionChargeCommission> findByMerchantIdAndGatewayAndTransactionType(String merchantId, String gateway, String type);
	List<TransactionChargeCommission> findByMerchantId(String merchantId);
	TransactionChargeCommission findByMerchantIdAndGatewayAndTransactionTypeAndSlab(String merchantId, String gateway, String type, String slab);
	TransactionChargeCommission findByMerchantIdAndGatewayAndTransactionTypeAndSlabAndCommissionType(String merchantId, String gateway, String type, String slab, String CommissionType);
}
