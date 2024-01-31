package io.asktech.payout.service.Pinwallet.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import io.asktech.payout.service.Pinwallet.model.Pinwalledpayouttrnxs;

public interface Pinwalletrepository extends JpaRepository<Pinwalledpayouttrnxs, String>{
    Pinwalledpayouttrnxs findByOrderId(String orderId);
   // Pinwalledpayouttrnxs findByPinwallettransactionId(String PinwallettransactionId);

}
