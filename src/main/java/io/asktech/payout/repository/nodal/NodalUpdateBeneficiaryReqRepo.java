package io.asktech.payout.repository.nodal;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.nodal.NodalUpdateBeneficiaryReq;

public interface NodalUpdateBeneficiaryReqRepo extends JpaRepository<NodalUpdateBeneficiaryReq, String > {

}
