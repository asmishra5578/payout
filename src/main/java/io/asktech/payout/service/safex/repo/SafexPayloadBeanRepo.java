package io.asktech.payout.service.safex.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.safex.models.SafexPayloadBean;

public interface SafexPayloadBeanRepo extends JpaRepository<SafexPayloadBean, String>{
    SafexPayloadBean findByOrderRefNo(String eazyOrderId);
    
}
