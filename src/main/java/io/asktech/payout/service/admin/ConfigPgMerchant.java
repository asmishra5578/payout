package io.asktech.payout.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.asktech.payout.dto.admin.ConfigPgMerchantDto;
import io.asktech.payout.dto.admin.ConfigPgNameMerchant;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.MerchantPgConfig;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.repository.merchant.MerchantPgConfigRepo;
import io.asktech.payout.repository.merchant.PgDetailsRepo;

@Service
public class ConfigPgMerchant {
    private static final String DETAILS_REQUIRED = "MerchantID, PG ID, Service cannot be blank";
    private static final String DETAILS_EXISTS = "Details already exists";
    private static final String PG_NOT_EXISTS = "PgID does not Exists";
    private static final String MERCHANT_DETAILS_REQUIRED = "MerchantId is required";
    private static final String DETAILS_NOT_EXISTS = "Merchant details not found";
    @Autowired
    MerchantPgConfigRepo merchantPgConfigRepo;
    @Autowired
    PgDetailsRepo pgDetailsRepo;

    public MerchantPgConfig createConfigPgMerchant(ConfigPgMerchantDto dto) throws ValidationExceptions {
        if (dto.getMerchantId()==null)  {
    throw new ValidationExceptions(DETAILS_REQUIRED, FormValidationExceptionEnums.MERCHANT_NOT_FOUND);
}
if (dto.getService()==null)  {
    throw new ValidationExceptions(DETAILS_REQUIRED, FormValidationExceptionEnums.PG_SERVICE_NOT_FOUND);
}
        if (!StringUtils.hasText(dto.getMerchantId()) && !StringUtils.hasText(dto.getService())
                && !StringUtils.hasText(dto.getPgId())) {
            throw new ValidationExceptions(DETAILS_REQUIRED, FormValidationExceptionEnums.PG_EXISTS);
        }
        if (pgDetailsRepo.findByPgId(dto.getPgId()) == null) {
            throw new ValidationExceptions(PG_NOT_EXISTS, FormValidationExceptionEnums.PG_EXISTS);
        }
        MerchantPgConfig merchantPgConfig = merchantPgConfigRepo.findByMerchantIdAndService(dto.getMerchantId(),
                dto.getService());
        if (merchantPgConfig == null) {
            merchantPgConfig = new MerchantPgConfig();
            merchantPgConfig.setMerchantId(dto.getMerchantId());
            merchantPgConfig.setPgId(dto.getPgId());
            merchantPgConfig.setService(dto.getService());
            if (StringUtils.hasText(dto.getStatus())) {
                merchantPgConfig.setStatus(dto.getStatus());
            } else {
                merchantPgConfig.setStatus("ACTIVE");
            }
            return merchantPgConfigRepo.save(merchantPgConfig);
        } else {
            throw new ValidationExceptions(DETAILS_EXISTS, FormValidationExceptionEnums.PG_EXISTS);
        }

    }

    public MerchantPgConfig updateConfigPgMerchant(ConfigPgMerchantDto dto) throws ValidationExceptions {
        if (!StringUtils.hasText(dto.getMerchantId())) {
            throw new ValidationExceptions(MERCHANT_DETAILS_REQUIRED, FormValidationExceptionEnums.PG_EXISTS);
        }
        if (pgDetailsRepo.findByPgId(dto.getPgId()) == null) {
            throw new ValidationExceptions(PG_NOT_EXISTS, FormValidationExceptionEnums.PG_EXISTS);
        }
        MerchantPgConfig merchantPgConfig = merchantPgConfigRepo.findByMerchantIdAndService(dto.getMerchantId(),
                dto.getService());
        if (merchantPgConfig != null) {
            if (StringUtils.hasText(dto.getPgId())) {
                merchantPgConfig.setPgId(dto.getPgId());
            }
            if (StringUtils.hasText(dto.getService())) {
                merchantPgConfig.setService(dto.getService());
            }
            if (StringUtils.hasText(dto.getStatus())) {
                merchantPgConfig.setStatus(dto.getStatus());
            }

            return merchantPgConfigRepo.save(merchantPgConfig);
        } else {
            throw new ValidationExceptions(DETAILS_NOT_EXISTS, FormValidationExceptionEnums.PG_EXISTS);
        }

    }

    public List<MerchantPgConfig> getAllMerchantPgLinks(){
       return merchantPgConfigRepo.findAll();
    }

public List<ConfigPgNameMerchant> getAllConfigPgNameMerchant(){
    List<ConfigPgNameMerchant> configPgNameMerchantList=new ArrayList<>();

    
      
       List<MerchantPgConfig> merchantPgConfigs=merchantPgConfigRepo.findAll();

       for (MerchantPgConfig merchantPgConfig : merchantPgConfigs) {
        ConfigPgNameMerchant configPgNameMerchant=new ConfigPgNameMerchant();
        PgDetails pf=     pgDetailsRepo.findByPgId(merchantPgConfig.getPgId());
        if(pf==null){
            continue;
        }
        configPgNameMerchant.setId(merchantPgConfig.getId());
        configPgNameMerchant.setMerchantId(merchantPgConfig.getMerchantId());
        configPgNameMerchant.setEnableRandomName(merchantPgConfig.getEnableRandomName());
        configPgNameMerchant.setPgId(merchantPgConfig.getPgId());
        configPgNameMerchant.setService(merchantPgConfig.getService());
        configPgNameMerchant.setPgName(pf.getPgName());
        configPgNameMerchant.setStatus(merchantPgConfig.getStatus());
        configPgNameMerchantList.add(configPgNameMerchant);
     
       }




return configPgNameMerchantList;
}

}
