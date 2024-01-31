package io.asktech.payout.repository.nodal;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.nodal.PaytmVodanAccountDeleteReq;

public interface PaytmNodanAccountDeleteReqRepo extends JpaRepository<PaytmVodanAccountDeleteReq, String>{

	PaytmVodanAccountDeleteReq findByOrderId(String orderId);

}
