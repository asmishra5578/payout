package io.asktech.payout.service.razor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.service.razor.dto.RazorResponseDto;
import io.asktech.payout.service.razor.dto.req.RazorBankAccountRequestDto;
import io.asktech.payout.service.razor.dto.req.RazorStatusResponse;
import io.asktech.payout.service.razor.models.RazorReqRes;
import io.asktech.payout.service.razor.repo.RazorReqResReposistory;
import io.asktech.payout.utils.Utility;

@Component
public class SaveRazorDetails {
    @Autowired
    private RazorReqResReposistory razorReqResReposistory;

    @Async
    public void storeAccountReqRes(RazorBankAccountRequestDto razoreRequest,
            RazorResponseDto razorResponseDto, String orderId) throws JsonProcessingException {
        RazorReqRes razorReqRes = null;

        if (razoreRequest != null) {
            razorReqRes = razorReqResReposistory.findByOrderId(orderId);
            if (razorReqRes == null) {
                razorReqRes = new RazorReqRes();
                razorReqRes.setOrderId(orderId);
            }
            razorReqRes.setReqString(Utility.convertDTO2JsonString(razoreRequest));
            razorReqRes.setAccount_number(razoreRequest.getAccount_number());
            razorReqRes.setReference_id(razoreRequest.getReference_id());

            razorReqRes.setAccount_type(razoreRequest.getFund_account().getAccount_type());
            razorReqRes.setAmount(razoreRequest.getAmount());
            razorReqRes.setMode(razoreRequest.getMode());
            razorReqRes.setReqString(Utility.convertDTO2JsonString(razoreRequest));
            razorReqRes.setStatusString("INITIATED");
            if (razoreRequest.getFund_account().getVpa() != null) {
                razorReqRes.setVpaId(razoreRequest.getFund_account().getVpa().getAddress());
            }
            if (razoreRequest.getFund_account().getBank_Account() != null) {
                razorReqRes.setAccount_number(razoreRequest.getFund_account().getBank_Account().getAccount_number());
                razorReqRes.setIfsc(razoreRequest.getFund_account().getBank_Account().getIfsc());
            }
        }
        if (razorResponseDto != null) {
            // logger.info("razorResponseDto::" +
            // Utility.convertDTO2JsonString(razorResponseDto));
            razorReqRes = razorReqResReposistory.findByOrderId(orderId);
            if (razorReqRes == null) {
                razorReqRes = new RazorReqRes();
                razorReqRes.setOrderId(orderId);
            }
            razorReqRes.setResString(Utility.convertDTO2JsonString(razorResponseDto));
            if (razorResponseDto.getId() != null) {
                razorReqRes.setMode(razorResponseDto.getMode());
                razorReqRes.setAmount(razorResponseDto.getAmount());
                razorReqRes.setReference_id(razorResponseDto.getReference_id());
                razorReqRes.setRazorId(razorResponseDto.getId());
                razorReqRes.setStatusTrx(razorResponseDto.getStatus());
                razorReqRes.setStatusString("COMPLETED");

            } else {
                if (razorResponseDto.getError() != null) {
                    razorReqRes.setStatusTrx("ERROR");
                    razorReqRes.setStatusString(razorResponseDto.getError().getReason());
                }
            }

        }
        razorReqResReposistory.save(razorReqRes);
    }

    @Async
    public void statusUpdateRazor(RazorStatusResponse razoreRequest, String orderId) throws JsonProcessingException {
        RazorReqRes razorReqRes = new RazorReqRes();

        if (razoreRequest != null) {
            razorReqRes = razorReqResReposistory.findByOrderId(orderId);
            if (razoreRequest.getId() != null) {
                if (razorReqRes == null) {
                    razorReqRes = new RazorReqRes();
                    razorReqRes.setOrderId(orderId);
                }
                razorReqRes.setReference_id(razoreRequest.getReference_id());
                razorReqRes.setAccount_type(razoreRequest.getMode());
                razorReqRes.setAmount(razoreRequest.getAmount());

                razorReqRes.setMode(razoreRequest.getMode());
                // Fund_account fund_account=null;
                razorReqRes.setRazorFundAccountId(razoreRequest.getFund_account_id());
                razorReqRes.setStatusString(Utility.convertDTO2JsonString(razoreRequest));
                razorReqRes.setStatusTrx(razoreRequest.getStatus());
                razorReqRes.setUtrid(razoreRequest.getUtr());

            } else {
                razorReqRes.setStatusString(Utility.convertDTO2JsonString(razoreRequest));
                razorReqRes.setStatusTrx("ERROR");
            }
            razorReqResReposistory.save(razorReqRes);
        }

    }

    public String getReferId(String orderId) {
        RazorReqRes razorReqRes = razorReqResReposistory.findByOrderId(orderId);
        if (razorReqRes != null) {
            return razorReqRes.getReference_id();
        }
        return null;
    }

}
