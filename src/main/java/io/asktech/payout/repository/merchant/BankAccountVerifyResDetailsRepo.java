package io.asktech.payout.repository.merchant;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.BankAccountVerifyResDetails;

public interface BankAccountVerifyResDetailsRepo extends JpaRepository<BankAccountVerifyResDetails, String>{

}
