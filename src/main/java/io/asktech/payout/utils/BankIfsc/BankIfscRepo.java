package io.asktech.payout.utils.BankIfsc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankIfscRepo extends JpaRepository<BankIfsc, String>{
    List<BankIfsc> findByIfsc(String ifsc);
}
