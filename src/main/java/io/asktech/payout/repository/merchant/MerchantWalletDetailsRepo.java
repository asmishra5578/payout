package io.asktech.payout.repository.merchant;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.MerchantWalletDetails;

public interface MerchantWalletDetailsRepo extends JpaRepository<MerchantWalletDetails, String>{

	public MerchantWalletDetails  findByMerchantId(String merchantId);

}
