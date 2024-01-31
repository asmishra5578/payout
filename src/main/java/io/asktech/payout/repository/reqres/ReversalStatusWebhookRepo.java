package io.asktech.payout.repository.reqres;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.reqres.ReversalStatusWebhookModal;

public interface ReversalStatusWebhookRepo extends JpaRepository<ReversalStatusWebhookModal, String>{

}
