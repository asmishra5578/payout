package io.asktech.payout.service.kitzone.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.kitzone.model.KitzoneModel;

public interface KitzoneRepo extends JpaRepository<KitzoneModel, String> {

    KitzoneModel  findByOrderId(String orderId);
    Optional<KitzoneModel>  findByPgTransId(String pgTransId);;
}
