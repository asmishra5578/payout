package io.asktech.payout.repository.nodal;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.nodal.NodalBalTransReq;

public interface NodalbaltransferReqRepo extends JpaRepository<NodalBalTransReq, String>{

	NodalBalTransReq findByOrderId(String orderId);

}











