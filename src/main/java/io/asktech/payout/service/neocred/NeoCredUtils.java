package io.asktech.payout.service.neocred;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.neocred.Request.AuthRequest;
import io.asktech.payout.service.neocred.dto.BeneDetails;
import io.asktech.payout.service.neocred.dto.PaymentDto;
import io.asktech.payout.service.neocred.dto.PaymentResDto;
import io.asktech.payout.service.neocred.dto.StatusNReqDto;
import io.asktech.payout.service.neocred.dto.StatusRes;
import io.asktech.payout.service.neocred.model.NeocredModel;
import io.asktech.payout.service.neocred.repo.NeocredModelRepo;
import io.asktech.payout.utils.Utility;

@Component
public class NeoCredUtils {

    static Logger logger = LoggerFactory.getLogger(NeoCredUtils.class);

    @Autowired
    AuthRequest authRequest;
    @Autowired
    NeocredModelRepo neocredModelRepo;

    @Autowired
    NeocredModel neocredModel;

    public PaymentResDto doPayment(AccountTransferMerReq dto, String merchantid, PgDetails pg,
            NeocredModel neocredModel)
            throws JsonProcessingException {

        // FILL THE PAYMENT DTO


        // NeocredModel neocredModel =
        // neocredModelRepo.findByOrderId(dto.getInternalOrderid());
        // if (neocredModel == null) {

        // logger.info("\n\n\n model is coming null\n\n\n");
        // return null;
        // }

        logger.info("\n\n\neocred mode  " + Utility.convertDTO2JsonString(neocredModel) + "\n\n\n");
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setAmount(dto.getAmount());
        paymentDto.setTransferMode(dto.getRequestType().toLowerCase());
        paymentDto.setRemarks(dto.getTrRemark());


        BeneDetails benedetails = new BeneDetails();
        benedetails.setBankAccount(dto.getBankaccount());
        benedetails.setIfsc(dto.getIfsc());
        benedetails.setName(dto.getBeneficiaryName());
        benedetails.setEmail("amitshukla@gmail.com");
        benedetails.setPhone(dto.getPhonenumber());
        paymentDto.setBeneDetails(benedetails);
        

        PaymentResDto paymentRes = authRequest.paymentRequest(paymentDto, pg);
        neocredModel.setPaymentRequest(Utility.convertDTO2JsonString(paymentDto));
        neocredModel.setPaymentResponse(Utility.convertDTO2JsonString(paymentRes));

        logger.info("\n\n\nrewsponse " + Utility.convertDTO2JsonString(paymentRes) + "\n\n\n");
        // TODO CHECK IF SUCESS

        if (paymentRes.getData() != null) {
            neocredModel.setPgTransId(paymentRes.getData().getReferenceId());
        }

        neocredModelRepo.save(neocredModel);

        return paymentRes;

    }

    public StatusRes statusCheck(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg, NeocredModel neocredModel) throws JsonProcessingException {


        logger.info("\n\n\n\nset orderId  :  "+ Utility.convertDTO2JsonString(dto) +"\n\n\n");
        logger.info("\n\n\n\nset model   :  "+ Utility.convertDTO2JsonString(neocredModel) +"\n\n\n");


        if (neocredModel == null) {
            return null;
        }

        StatusNReqDto statusReq = new StatusNReqDto();
        statusReq.setReferenceId(neocredModel.getPgTransId());

        StatusRes statusRes = authRequest.getNStatus(statusReq, pg);

        // neocredModel.setStatusRequest(Utility.convertDTO2JsonString(statusReq));
        // neocredModel.setStatusResponse(Utility.convertDTO2JsonString(statusRes));

        logger.info("Status request : "+ Utility.convertDTO2JsonString(statusReq));


        logger.info("Status response : "+ Utility.convertDTO2JsonString(statusRes));


        // neocredModelRepo.save(neocredModel);

        return statusRes;
    }

    

}
