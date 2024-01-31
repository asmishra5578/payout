package io.asktech.payout.repository.nodal;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.nodal.NodalBenefManagementReq;


public interface NodalBenefManagementReqRepo extends JpaRepository<NodalBenefManagementReq, String>{

	NodalBenefManagementReq findByOrderId(String orderId);

}


