package io.asktech.payout.utils.BankIfsc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepo extends JpaRepository<BankModel, String> {
    List<BankModel> findByIfsc(String ifsc);
}
