package io.asktech.payout.repository.merchant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.asktech.payout.modal.merchant.MerchantPgConfig;

public interface MerchantPgConfigRepo extends JpaRepository<MerchantPgConfig, String> {
    List<MerchantPgConfig> findByMerchantId(String merchantid);

    MerchantPgConfig findByMerchantIdAndService(String merchantId, String service);

    MerchantPgConfig findByMerchantIdAndServiceAndStatus(String merchantId, String service, String status);
}
