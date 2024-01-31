package io.asktech.payout.repository.merchant;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.PayoutApiUserDetails;



public interface PayoutApiUserDetailsRepo extends JpaRepository<PayoutApiUserDetails, String>{

}
