package io.asktech.payout.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.wallet.modal.BalanceProcessorQueue;

public interface BalanceProcessorQueueRepository extends JpaRepository<BalanceProcessorQueue, String>{

	List<BalanceProcessorQueue> findByStatusAndMerchantThreadFlagOrderBySnoAsc(String string, int merchantThreadFlag);

}
