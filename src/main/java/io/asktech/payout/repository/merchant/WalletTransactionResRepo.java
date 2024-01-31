package io.asktech.payout.repository.merchant;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.WalletTransactionRes;

public interface WalletTransactionResRepo extends JpaRepository<WalletTransactionRes, String>{

}
