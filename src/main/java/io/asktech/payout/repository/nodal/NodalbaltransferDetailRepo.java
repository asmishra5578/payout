package io.asktech.payout.repository.nodal;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.nodal.NodalBalTransDetail;

public interface NodalbaltransferDetailRepo extends JpaRepository<NodalBalTransDetail, String> {

	NodalBalTransDetail findByOrderId(String orderId);

}
