package io.asktech.payout.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.asktech.payout.wallet.modal.MainWalletDetails;


public interface MainWalletDetailsRepo extends JpaRepository<MainWalletDetails, String>{
public MainWalletDetails findByWalletid(String walletid);

@Query(value = "select walletid from MainWalletDetails",nativeQuery = true)
public List<String> getMainWalletIdList();
}
