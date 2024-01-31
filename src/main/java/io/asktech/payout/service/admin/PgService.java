package io.asktech.payout.service.admin;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.asktech.payout.dto.admin.PgCreationDto;
import io.asktech.payout.dto.admin.UpdateTransactionDetailsRequestDto;
import io.asktech.payout.dto.admin.PgUpdateRequest;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.PgDetailsRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.constants.ErrorValues;

@Service
public class PgService implements ErrorValues{
    private static final String PG_ALREADY_EXISTS = "PG Already Exists";
    private static final String BLANK_PG_NAME = "Blank PG Name";
    private static final String PG_NOT_FOUND = "PG Does not exists";
    @Autowired
    PgDetailsRepo pgDetailsRepo;
    
    @Autowired
	TransactionDetailsRepo transactionDetailsRepository;

    public PgDetails createPg(PgCreationDto dto) throws ValidationExceptions {
        PgDetails pgDetails = pgDetailsRepo.findByPgName(dto.getPgName());
        if (pgDetails == null) {
            pgDetails = new PgDetails();
            if (StringUtils.hasText(dto.getPgName())) {
                pgDetails.setPgName(dto.getPgName());
            } else {
                throw new ValidationExceptions(BLANK_PG_NAME, FormValidationExceptionEnums.PG_BLANK);
            }
            if (StringUtils.hasText(dto.getPgConfigKey())) {
                pgDetails.setPgConfigKey(dto.getPgConfigKey());
            }
            if (StringUtils.hasText(dto.getPgConfigSecret())) {
                pgDetails.setPgConfigSecret(dto.getPgConfigSecret());
            }
            if (StringUtils.hasText(dto.getPgStatus())) {
                pgDetails.setPgStatus(dto.getPgStatus());
            }
            if (StringUtils.hasText(dto.getPgConfig1())) {
                pgDetails.setPgConfig1(dto.getPgConfig1());
            }
            if (StringUtils.hasText(dto.getPgConfig2())) {
                pgDetails.setPgConfig2(dto.getPgConfig2());
            }
            if (StringUtils.hasText(dto.getPgConfig3())) {
                pgDetails.setPgConfig3(dto.getPgConfig3());
            }
            pgDetails.setPgId(UUID.randomUUID().toString());
            return pgDetailsRepo.save(pgDetails);
        } else {
            throw new ValidationExceptions(PG_ALREADY_EXISTS, FormValidationExceptionEnums.PG_EXISTS);
        }

    }

    public PgDetails updatePg(PgUpdateRequest pgUpdateRequest) throws ValidationExceptions {
    	
    	//PgDetails pgDetails = pgDetailsRepo.findByPgId(pgUpdateRequest.getPgId());
    	PgDetails pgDetails = pgDetailsRepo.findByPgIdAndPgName(pgUpdateRequest.getPgId(), pgUpdateRequest.getPgName());
        PgDetails pgDetails2 = pgDetailsRepo.findByPgName(pgUpdateRequest.getPgName());
        if (pgDetails != null) {
            if (StringUtils.hasText(pgUpdateRequest.getPgConfigKey())) {
                pgDetails.setPgConfigKey(pgUpdateRequest.getPgConfigKey());
            }
            if (StringUtils.hasText(pgUpdateRequest.getPgConfigSecret())) {
                pgDetails.setPgConfigSecret(pgUpdateRequest.getPgConfigSecret());
            }
            if (StringUtils.hasText(pgUpdateRequest.getPgStatus())) {
                pgDetails.setPgStatus(pgUpdateRequest.getPgStatus());
            }
            if (StringUtils.hasText(pgUpdateRequest.getPgConfig1())) {
                pgDetails.setPgConfig1(pgUpdateRequest.getPgConfig1());
            }
            if (StringUtils.hasText(pgUpdateRequest.getPgConfig2())) {
                pgDetails.setPgConfig2(pgUpdateRequest.getPgConfig2());
            }
            if (StringUtils.hasText(pgUpdateRequest.getPgConfig3())) {
                pgDetails.setPgConfig3(pgUpdateRequest.getPgConfig3());
            }
            return pgDetailsRepo.save(pgDetails);
        } else {
            throw new ValidationExceptions(PG_NOT_FOUND, FormValidationExceptionEnums.PG_NOT_EXISTS);
        }
    }

    public List<PgDetails> getAllPg() {
        List<PgDetails> pgDetails = pgDetailsRepo.findAll();
        return pgDetails;
    }
}
