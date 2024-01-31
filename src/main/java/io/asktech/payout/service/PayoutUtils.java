package io.asktech.payout.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.asktech.payout.modal.merchant.MerchantPgConfig;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.repository.merchant.MerchantPgConfigRepo;
import io.asktech.payout.repository.merchant.PgDetailsRepo;
import io.asktech.payout.utils.Utility;

@Component
public class PayoutUtils {
    @Autowired
    PgDetailsRepo pgDetailsRepo;
    @Autowired
    MerchantPgConfigRepo merchantPgConfigRepo;
    static Logger logger = LoggerFactory.getLogger(PayoutUtils.class);

    public PgDetails getMerchantPgDetails(String merchantId, String service) throws JsonProcessingException {
        logger.info("getMerchantPgDetails::"+merchantId+"|"+service);
        MerchantPgConfig configs = merchantPgConfigRepo.findByMerchantIdAndServiceAndStatus(merchantId, service,
                "ACTIVE");

        logger.info("MerchantPgConfig::"+Utility.convertDTO2JsonString(configs));
        if (configs == null) {
            return null;
        }
        PgDetails pgDetails =  pgDetailsRepo.findByPgIdAndPgStatus(configs.getPgId(), "ACTIVE");
        logger.info("pgDetails::"+Utility.convertDTO2JsonString(pgDetails));
        return pgDetails;

    }

    public PgDetails getByPgId(String pgName) {
        return pgDetailsRepo.findByPgId(pgName.toUpperCase());

    }
}
