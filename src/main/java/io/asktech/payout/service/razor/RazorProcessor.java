package io.asktech.payout.service.razor;

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
import io.asktech.payout.service.razor.dto.RazorResponseDto;
import io.asktech.payout.service.razor.dto.req.RazorBankAccountRequestDto;
import io.asktech.payout.service.razor.dto.req.RazorStatusResponse;
import io.asktech.payout.utils.Utility;

@Service
public class RazorProcessor implements IProcessor {
    @Autowired
    private RazorRequests razorRequests;
    @Autowired
    private RazorUtils razorUtils;
    @Autowired
    private SaveRazorDetails saveRazorDetails;
    static Logger logger = LoggerFactory.getLogger(RazorProcessor.class);

    @Override
    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg) {
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        try {
            RazorBankAccountRequestDto razorRequestDto = razorUtils.generateBankTransferRequest(dto, merchantid, pg);
            saveRazorDetails.storeAccountReqRes(razorRequestDto, null, dto.getInternalOrderid());
            RazorResponseDto razorResponseDto = razorRequests.accountTransfer(razorRequestDto, merchantid, pg);
            saveRazorDetails.storeAccountReqRes(null, razorResponseDto, dto.getInternalOrderid());
            if (razorResponseDto.getId() != null) {
                walletTransferRes.setReferenceId(razorResponseDto.getReference_id());
                walletTransferRes.setUtr(razorResponseDto.getUtr());
                if (razorResponseDto.getStatus().equals("processed") && razorResponseDto.getUtr().length() > 5) {
                    walletTransferRes.setStatus("SUCCESS");
                } else {
                    walletTransferRes.setStatus("PENDING");
                }
                walletTransferRes.setStatusMessage(razorResponseDto.getStatus_details().getReason());
            } else {
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes.setStatusMessage(razorResponseDto.getError().getReason());
            }
        } catch (JsonProcessingException e) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage("Transfer Api ERROR");

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage("Transfer Api ERROR");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return walletTransferRes;
    }

    @Override
    public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg) {
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        try {
            RazorBankAccountRequestDto razorRequestDto = razorUtils.generateUpiTransferRequest(dto, merchantid, pg);
            saveRazorDetails.storeAccountReqRes(razorRequestDto, null, dto.getInternalOrderid());
            RazorResponseDto razorResponseDto = razorRequests.upiTransfer(razorRequestDto, merchantid, pg);
            saveRazorDetails.storeAccountReqRes(null, razorResponseDto, dto.getInternalOrderid());
            if (razorResponseDto.getId() != null) {
                walletTransferRes.setReferenceId(razorResponseDto.getReference_id());
                walletTransferRes.setUtr(razorResponseDto.getUtr());
                if (razorResponseDto.getStatus().equals("processed") && razorResponseDto.getUtr().length() > 5) {
                    walletTransferRes.setStatus("SUCCESS");
                } else {
                    walletTransferRes.setStatus("PENDING");
                }
                walletTransferRes.setStatusMessage(razorResponseDto.getStatus_details().getReason());
            } else {
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes.setStatusMessage(razorResponseDto.getError().getReason());
            }
        } catch (JsonProcessingException e) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage("Transfer Api ERROR");

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage("Transfer Api ERROR");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return walletTransferRes;
    }

    @Override
    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) {
        String razorId = razorUtils.getRazorId(transactionDetails.getInternalOrderId());
        TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
        transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
        transactionResponseMerRes.setAmount(transactionDetails.getAmount());
        if (razorId != null) {
            try {
                RazorStatusResponse razorStatusResponse = razorRequests
                        .transferStatusApi(razorId, merchantid, pg);
                saveRazorDetails.statusUpdateRazor(razorStatusResponse, transactionDetails.getOrderId());
                logger.info("RAZOR::" + Utility.convertDTO2JsonString(razorStatusResponse));

                if (razorStatusResponse.getStatus().equals("processed") && razorStatusResponse.getUtr().length() > 5) {
                    transactionResponseMerRes.setStatus("SUCCESS");
                } else {
                    transactionResponseMerRes.setStatus("PENDING");
                }
                transactionResponseMerRes.setMessage(razorStatusResponse.getStatus_details().getDescription());
                transactionResponseMerRes.setStatusMessage(razorStatusResponse.getStatus_details().getReason());
                transactionResponseMerRes.setUtrId(razorStatusResponse.getUtr());
            } catch (Exception e) {
                transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
                transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
                transactionResponseMerRes.setStatusMessage(transactionDetails.getTransactionMessage());
                e.printStackTrace();
            }

        } else {
            try {
                logger.info(Utility.convertDTO2JsonString(transactionDetails));
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!transactionDetails.getTransactionStatus().equals("ACCEPTED")) {
                transactionResponseMerRes.setStatus("FAILURE");
                transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
                transactionResponseMerRes.setStatusMessage(transactionDetails.getTransactionMessage());
            } else {
                transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
                transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
                transactionResponseMerRes.setStatusMessage(transactionDetails.getTransactionMessage());
            }
        }
        try {
            logger.info("RAZOR::" + Utility.convertDTO2JsonString(transactionResponseMerRes));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return transactionResponseMerRes;
    }

}
