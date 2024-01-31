package io.asktech.payout.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.wallet.modal.RechargeServiceCommission;


public interface RechargeServiceRepository extends JpaRepository<RechargeServiceCommission, String>{

	//RechargeServiceCommission findByMerchantIdAndGatewayAndType(String merchantId, String gateway, String type);
	RechargeServiceCommission findByMerchantId(String merchantId);
	RechargeServiceCommission findByMerchantIdAndCommissionType(String merchantId, String unitType);
}
