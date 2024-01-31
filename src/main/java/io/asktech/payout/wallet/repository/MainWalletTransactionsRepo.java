package io.asktech.payout.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.wallet.modal.MainWalletTransactions;

public interface MainWalletTransactionsRepo extends JpaRepository<MainWalletTransactions, String> {

}
