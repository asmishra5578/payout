package io.asktech.payout.repository.van;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.van.VanWebhookReqModel;

public interface VanWebhookReqRepo extends JpaRepository<VanWebhookReqModel, String>{

}
