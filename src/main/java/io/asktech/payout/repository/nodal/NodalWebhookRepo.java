package io.asktech.payout.repository.nodal;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.nodal.NodalWebhook;

public interface NodalWebhookRepo extends JpaRepository<NodalWebhook, String>{

}
