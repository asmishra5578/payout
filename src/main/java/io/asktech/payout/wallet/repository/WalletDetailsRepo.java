package io.asktech.payout.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.wallet.modal.WalletDetails;

public interface WalletDetailsRepo extends JpaRepository<WalletDetails, String>{
	public WalletDetails findByWalletid(String walletid);
	
	public List<WalletDetails> findByMainWalletid(String mainWalletid);
	
	public WalletDetails findByMerchantid(String merchantid);
}
