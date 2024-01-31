package io.asktech.payout.service.neocred;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.IProcessor;
import io.asktech.payout.service.neocred.Request.AuthRequest;
import io.asktech.payout.service.neocred.dto.AuthRequestDto;
import io.asktech.payout.service.neocred.dto.AuthResponseDto;
import io.asktech.payout.service.neocred.dto.PaymentResDto;
import io.asktech.payout.service.neocred.dto.SettlementReqDto;
import io.asktech.payout.service.neocred.dto.SettlementResDto;
import io.asktech.payout.service.neocred.dto.StatusNResDto;
import io.asktech.payout.service.neocred.dto.StatusReq;
import io.asktech.payout.service.neocred.dto.StatusRes;
import io.asktech.payout.service.neocred.model.NeocredModel;
import io.asktech.payout.service.neocred.repo.NeocredModelRepo;
import io.asktech.payout.utils.Utility;

@Service
public class NeoCredProcessor implements IProcessor {

    static Logger logger = LoggerFactory.getLogger(NeoCredProcessor.class);

    @Autowired
    AuthRequest authRequest;
    @Autowired
    NeocredModelRepo neocredModelRepo;

    @Autowired
    NeocredModel neocredModel;
    @Autowired
    NeoCredUtils neoCredUtils;

    @Override
    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException {
        // TODO Auto-generated method stub

        logger.info("\n\nnNeoCred transaction\n\n");

        logger.info("\n neocread accoutnt trandsfer intiated\n");
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        walletTransferRes.setStatus("PENDING");
        walletTransferRes.setStatusMessage("PENDING");
        NeocredModel neocredModel = new NeocredModel();
        neocredModel.setAmount(dto.getAmount());
        neocredModel.setMerchantId(merchantid);
        neocredModel.setAccessKey(pg.getPgConfigKey());
        neocredModel.setAccessSecret(pg.getPgConfigSecret());
        neocredModel.setOrderId(dto.getInternalOrderid());

        neocredModelRepo.save(neocredModel);
        /* new payment transfer */

        // PaymentResDto paymentResDto = neoCredUtils.doPayment(dto, merchantid, pg,
        // neocredModel);

        // logger.info("\n\n\nrewsponse " + Utility.convertDTO2JsonString(paymentResDto)
        // +"\n\n\n");

        // if (paymentResDto.getData() != null){
        // walletTransferRes.setReferenceId(paymentResDto.getData().getReferenceId());

        // neocredModel.setPgOrderId(paymentResDto.getData().getTransferId());
        // neocredModelRepo.save(neocredModel);
        // }
        // else{
        // //walletTransferRes.setReferenceId(paymentResDto.getData().getReferenceId());
        // if (paymentResDto.getStatus().equals("ERROR")){
        // walletTransferRes.setStatus("FAILURE");
        // }
        // walletTransferRes.setErrorMsg(paymentResDto.getMessage());
        // walletTransferRes.setStatusMessage(paymentResDto.getStatus());
        // // walletTransferRes.
        // }
        // walletTransferRes.setStatus("PENDING");
        // walletTransferRes.setErrorMsg(paymentResDto.getMessage());
        // walletTransferRes.setStatusMessage(paymentResDto.getMessage());
        // neocredModelRepo.save(neocredModel);

        try {
            AuthRequestDto authReqdto = new AuthRequestDto();
            authReqdto.setUsername(pg.getPgConfigKey());
            authReqdto.setPassword(pg.getPgConfigSecret());
            neocredModel.setAuthRequest(authRequest.convertDTO2JsonString(authReqdto));
            AuthResponseDto authResponseDto = authRequest.getToken(authReqdto);
            logger.info("neocred auth request : " + Utility.convertDTO2JsonString(authReqdto) + "\n AuthResponse : "
                    + Utility.convertDTO2JsonString(authResponseDto));
            if (authResponseDto == null) {
                walletTransferRes.setErrorMsg("Authentication FAILURE");
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes.setStatusMessage("FAILURE");
                logger.info("neocred auth request FAILURE");
                neocredModel.setAuthResponse("Auth FAILURE");
                neocredModelRepo.save(neocredModel);
                return walletTransferRes;
            }
            neocredModel.setAuthResponse(authRequest.convertDTO2JsonString(authRequest.getToken(authReqdto)));
            SettlementReqDto settlementReqDto = new SettlementReqDto();
            settlementReqDto.setBankAccountHolderName(dto.getBeneficiaryName());
            settlementReqDto.setBankAccountNumber(dto.getBankaccount());
            settlementReqDto.setBankIfscCode(dto.getIfsc());
            settlementReqDto.setTransferType(dto.getRequestType());
            settlementReqDto.setCreditorInfoRemarks("Fund Transfer");
            logger.info("settelement request : " + Utility.convertDTO2JsonString(settlementReqDto));
            SettlementResDto settlementResDto = authRequest.paySettlement(settlementReqDto, pg.getPgConfig1(),
                    dto.getAmount(), authResponseDto.getData());
            logger.info("settelement response : " + Utility.convertDTO2JsonString(settlementResDto));

            neocredModel.setPaymentRequest(authRequest.convertDTO2JsonString(settlementReqDto));

            if (settlementResDto == null) {
                walletTransferRes.setErrorMsg("Transfer Req FAILURE");
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes.setStatusMessage("FAILURE");
                logger.info("neocred auth request FAILURE");
                neocredModel.setPaymentResponse("Transfer FAILURE");
                neocredModelRepo.save(neocredModel);
                return walletTransferRes;
            }
            neocredModel.setPaymentResponse(Utility.convertDTO2JsonString(settlementResDto));
            neocredModelRepo.save(neocredModel);

            if (settlementResDto.getStatus().equals("CREATED")) {
                neocredModel.setPgTransId(settlementResDto.getData().getTxnRefNo());
                neocredModel.setTrxType(settlementReqDto.getTransferType());
                neocredModelRepo.save(neocredModel);
                walletTransferRes.setStatusMessage(settlementResDto.getMessage());
                walletTransferRes.setReferenceId(settlementResDto.getData().getTxnRefNo());
            } else {
                walletTransferRes.setErrorMsg(authRequest.convertDTO2JsonString(settlementResDto));
            }
            neocredModel.setPaymentResponse(authRequest.convertDTO2JsonString(settlementResDto));
        } catch (Exception e) {
            logger.error("NeoCred Exceptions ERROR::" + e.getMessage());
            walletTransferRes.setErrorMsg(authRequest.convertDTO2JsonString(e.getMessage()));
            neocredModel.setPaymentResponse(authRequest.convertDTO2JsonString(e.getMessage()));
        }

        // STATUS APIS
        return walletTransferRes;
    }

    @Override
    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) throws JsonProcessingException {

        // TODO Auto-generated method stub

        AuthRequestDto authReqdto = new AuthRequestDto();
        authReqdto.setUsername(pg.getPgConfigKey());
        authReqdto.setPassword(pg.getPgConfigSecret());
        logger.info("transaction details : " + Utility.convertDTO2JsonString(transactionDetails));

        TransactionResponseMerRes response = new TransactionResponseMerRes();
        response.setOrderid(transactionDetails.getInternalOrderId());
        response.setAmount(transactionDetails.getAmount());
        response.setStatus("PENDING");
        if (transactionDetails.getReferenceId() == null) {
            return response;
        }

        AuthResponseDto authResponseDto = authRequest.getToken(authReqdto);
        logger.info("neocred auth request : " + Utility.convertDTO2JsonString(authReqdto) + "\n AuthResponse : "
                + Utility.convertDTO2JsonString(authResponseDto));
        if (authResponseDto == null) {
            return response;
        }
        StatusReq statusReq = new StatusReq();
        statusReq.setClientHashId(pg.getPgConfig1());

        statusReq.setRrn(transactionDetails.getReferenceId());
        logger.info(transactionDetails.getReferenceId());
        SettlementResDto settlementResDto = authRequest.getStatus(statusReq, pg.getPgConfig1(),
                "Bearer " + authResponseDto.getData());
        logger.info("status response : " + Utility.convertDTO2JsonString(settlementResDto));
        if (settlementResDto == null) {
            return response;
        }

        NeocredModel neocredModel = neocredModelRepo.findByOrderId(transactionDetails.getInternalOrderId());

       // StatusRes statusRes = neoCredUtils.statusCheck(dto, merchantid, transactionDetails, pg, neocredModel);

        if (neocredModel != null) {
            neocredModel.setStatusRequest(Utility.convertDTO2JsonString(statusReq));
            neocredModel.setStatusResponse(Utility.convertDTO2JsonString(settlementResDto));
            if (settlementResDto.getData() != null) {
                neocredModel.setStatusMessage(settlementResDto.getData().getStatus());
            }
            neocredModelRepo.save(neocredModel);

            if (settlementResDto.getStatus() != null) {
                if (settlementResDto.getStatus().equals("SUCCESS")) {
                    if (settlementResDto.getData().getRrn() != null) {
                        logger.info("156");
                        if ((settlementResDto.getData().getRrn().length() > 3)
                                && (settlementResDto.getData().getStatus().equalsIgnoreCase("SUCCESS"))) {
                            response.setStatus("SUCCESS");
                        } else if (settlementResDto.getData().getStatus().equalsIgnoreCase("FAILURE")) {
                            response.setStatus("FAILURE");
                        } else {
                            response.setStatus("PENDING");
                        }
                    } else if (settlementResDto.getData().getStatus().equalsIgnoreCase("FAILURE")) {
                        response.setStatus("FAILURE");
                    } else {
                        response.setStatus("PENDING");
                    }
                    response.setMessage(settlementResDto.getMessage());
                    response.setUtrId(settlementResDto.getData().getRrn());
                    response.setStatusMessage(settlementResDto.getData().getTxnRefNo());
                } else if (settlementResDto.getStatus().equals("FAILURE")) {
                    response.setStatus("FAILURE");
                }

            } else {
                if (settlementResDto.getDetails() != null) {
                    if (settlementResDto.getDetails().getStatus().equalsIgnoreCase("SettlementInProcess")) {
                        response.setStatus("PENDING");
                        response.setStatusMessage(settlementResDto.getDetails().getTxnRefNo());
                    }
                    if (settlementResDto.getDetails().getStatus().equalsIgnoreCase("SettlementReversed")) {
                        response.setStatus("FAILURE");
                        response.setStatusMessage(settlementResDto.getDetails().getTxnRefNo());
                    }
                    if (settlementResDto.getDetails().getStatus().equalsIgnoreCase("Pending")) {
                        response.setStatus("PENDING");
                        response.setStatusMessage(settlementResDto.getDetails().getTxnRefNo());
                    }
                    if (settlementResDto.getDetails().getStatus().equalsIgnoreCase("FAILURE")) {
                        response.setStatus("FAILURE");
                        response.setStatusMessage(settlementResDto.getDetails().getTxnRefNo());
                    }
                }

            }

            // if (statusRes.getStatus() != null) {
            // if (statusRes.getStatus().equals("SUCCESS")) {
            // if (statusRes.getData().getUtr() != null) {
            // logger.info("156");
            // if ((statusRes.getData().getUtr().length() > 3)
            // && (statusRes.getStatus().equalsIgnoreCase("SUCCESS"))) {
            // response.setStatus("SUCCESS");
            // } else if (statusRes.getStatus().contains("FAILED")) {
            // response.setStatus("FAILURE");
            // } else {
            // response.setStatus("PENDING");
            // }
            // } else if (statusRes.getStatus().contains("FAILED")) {
            // response.setStatus("FAILURE");
            // } else {
            // response.setStatus("PENDING");
            // }
            // response.setMessage(statusRes.getMessage());
            // response.setUtrId(statusRes.getData().getUtr());
            // response.setStatusMessage(statusRes.getMessage());
            // }

            // } else {
            // if (statusRes.getData() != null) {
            // if (statusRes.getStatus().equalsIgnoreCase("SUCCESS")) {
            // response.setStatus("PENDING");
            // response.setStatusMessage(statusRes.getData().getTransferId());
            // }
            // if (statusRes.getStatus().contains("FAILED")) {
            // response.setStatus("FAILURE");
            // response.setStatusMessage(statusRes.getData().getTransferId());
            // }
            // if (statusRes.getStatus().equalsIgnoreCase("PENDING")) {
            // response.setStatus("PENDING");
            // response.setStatusMessage(statusRes.getData().getTransferId());
            // }
            // if (statusRes.getStatus().contains("FAILED")) {
            // response.setStatus("FAILURE");
            // response.setStatusMessage(statusRes.getData().getTransferId());
            // }
            // }

            // else{
            // if (statusRes.getStatus().contains("FAILED")) {
            // response.setStatus("FAILURE");
            // response.setMessage(statusRes.getMessage());
            // } else {
            // response.setStatus("PENDING");
            // }
            // }
            // }
        }
        return response;

    }

    @Override
    public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg) {
        // TODO Auto-generated method stub

        return null;
    }

}
