package io.asktech.payout.repository.van;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.van.VanCreationRequest;

public interface VanCreationRequestRepo extends JpaRepository<VanCreationRequest, String> {

	VanCreationRequest findByOrderId(String orderId);

	VanCreationRequest findByMerchantID(String merchantID);

}
