package io.asktech.payout.repository.van;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.van.MerVanUpdateReq;

public interface MerVanUpdateReqRepo extends JpaRepository<MerVanUpdateReq, String> {

	MerVanUpdateReq findByOrderId(String orderId);

}
