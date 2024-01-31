package io.asktech.payout.repository.reqres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.ApiRequestResponseLogger;


public interface ApiRequestResponseLoggerRepo extends JpaRepository<ApiRequestResponseLogger, String> {
    List<ApiRequestResponseLogger> getByRequestIdAndMerchantId(String orderid, String merchantid);
}
