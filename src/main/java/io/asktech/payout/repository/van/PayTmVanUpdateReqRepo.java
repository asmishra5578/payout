package io.asktech.payout.repository.van;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.van.PayTmVanUpdateRequest;

public interface PayTmVanUpdateReqRepo extends JpaRepository<PayTmVanUpdateRequest, String> {

}
