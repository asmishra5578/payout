package io.asktech.payout.service.neocred.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.service.neocred.model.NeocredModel;





public interface NeocredModelRepo extends JpaRepository<NeocredModel, String>{
    NeocredModel findByOrderId(String orderId);
    
}
