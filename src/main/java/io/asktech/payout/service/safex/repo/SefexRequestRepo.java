package io.asktech.payout.service.safex.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.safex.models.SafexRequests;

public interface SefexRequestRepo extends JpaRepository<SafexRequests, String>{
    SafexRequests findByOrderId(String eazyOrderId);
}

