package io.asktech.payout.service.sark.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.sark.models.SarkReqRes;

public interface SarkReqResRepo extends JpaRepository<SarkReqRes, String> {
    SarkReqRes findByOrderId(String txnId);
}
