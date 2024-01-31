package io.asktech.payout.repository.van;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.van.VanCreationResponse;

public interface VanCreationResponseRepo extends JpaRepository<VanCreationResponse, String> {

}
