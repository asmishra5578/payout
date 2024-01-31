package io.asktech.payout.repository.reqres;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.reqres.AddFundDetailModal;

public interface AddFundDetailRepo extends JpaRepository<AddFundDetailModal, String> {

}
