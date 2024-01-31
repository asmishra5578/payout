package io.asktech.payout.service.razor.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.razor.models.RazorReqRes;

public interface RazorReqResReposistory extends JpaRepository<RazorReqRes, Long> {
    RazorReqRes findByOrderId(String txnId);
    
}
