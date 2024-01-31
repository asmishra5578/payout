package io.asktech.payout.repository.merchant;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.BankAccountVerifyReqDetails;

public interface BankAccountVerifyReqDetailsRepo extends JpaRepository<BankAccountVerifyReqDetails, String > {

	BankAccountVerifyReqDetails findByOrderId(String orderid);

}
