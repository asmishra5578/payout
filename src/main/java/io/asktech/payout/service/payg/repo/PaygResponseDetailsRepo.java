package io.asktech.payout.service.payg.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.payg.modals.PaygResponseDetails;

public interface PaygResponseDetailsRepo extends JpaRepository<PaygResponseDetails, String> {
    PaygResponseDetails findByEazyOrderId(String eazyOrderId);
}
