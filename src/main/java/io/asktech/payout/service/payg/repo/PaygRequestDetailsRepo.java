package io.asktech.payout.service.payg.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.payg.modals.PaygRequestDetails;

public interface PaygRequestDetailsRepo extends JpaRepository<PaygRequestDetails, String>{
    PaygRequestDetails findByEazyOrderId(String eazyOrderId);
}
