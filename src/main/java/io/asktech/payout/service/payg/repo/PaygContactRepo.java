package io.asktech.payout.service.payg.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.payg.modals.PaygContact;

public interface PaygContactRepo extends JpaRepository<PaygContact, String> {
    
    PaygContact findByEazyPayOrderId(String eazyPayOrderId);
    //PaygContact findByEazyOrderId(String EazyOrderId);
}
