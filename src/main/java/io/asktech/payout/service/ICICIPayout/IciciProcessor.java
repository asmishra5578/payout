package io.asktech.payout.service.ICICIPayout;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.service.IProcessor;
import io.asktech.payout.service.ICICIPayout.Dto.FundTransferres;
import io.asktech.payout.service.ICICIPayout.Dto.TransactionStatusres;
import io.asktech.payout.service.ICICIPayout.model.IcicipayoutTransactiondetails;
import io.asktech.payout.service.ICICIPayout.repo.Icicirepository;

@Service
public class IciciProcessor implements IProcessor{

     Logger logger = LoggerFactory.getLogger(IciciProcessor.class);

     @Autowired
     IciciUtility iciciUtility;
     @Autowired
     Icicirepository icicirepository;
     @Autowired
     TransactionDetailsRepo transactionDetailsRepo;

      @Override
      public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException {
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        walletTransferRes.setStatus("PENDING");
        walletTransferRes.setStatusMessage("PENDING");
        logger.info("\n INSIDE THE ICICI PAYOUT\n");

      IcicipayoutTransactiondetails icicipayoutTransactiondetails = new IcicipayoutTransactiondetails();
      
      FundTransferres fundTransferres = iciciUtility.makePayload(dto, merchantid, pg);

        if (fundTransferres == null) {
            logger.info("\n Something went wrong\n");
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setErrorMsg("SOME ERROR in MAKE PAYMENT");
        } else {
            logger.info("\n ALL is fine here\n");
            walletTransferRes.setErrorMsg(fundTransferres.getMessage());
            walletTransferRes.setReferenceId(fundTransferres.getData().getOrderId());
            walletTransferRes.setStatusMessage(fundTransferres.getMessage());
            walletTransferRes.setStatus("PENDING");
            if (fundTransferres.getData().getStatus().equals("SUCCESS")) {
                walletTransferRes.setStatus("PENDING");
            }
        }

        return walletTransferRes;

    }

     @Override
    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) throws JsonProcessingException {
       
                transactionDetails = transactionDetailsRepo.findByOrderId(dto.getOrderId());
               TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
         try {
            TransactionStatusres transactionStatusres = iciciUtility.gettransactionstatus( transactionDetails.getInternalOrderId(), merchantid, pg, merchantid);
              IcicipayoutTransactiondetails icicipayoutTransactiondetails = icicirepository.findByOrderId(transactionDetails.getInternalOrderId());
               
            if (icicipayoutTransactiondetails == null) {
            logger.info("\n  order id not found in kitzone :" + dto.getOrderId() + " \n");
            // walletTransferRes.set("ORDER ID NOT FOUND IN KITZOne");
            transactionResponseMerRes.setStatus("PENDING");
            transactionResponseMerRes.setErrorMsg("order id not found in ICICI");
            
        }
        if (transactionStatusres == null) {
            transactionResponseMerRes.setStatus("PENDING");
           }
        if (transactionStatusres.getData().getStatus().equalsIgnoreCase("Received")) {
            transactionResponseMerRes.setStatus("PENDING");
             icicipayoutTransactiondetails.setTx_status(transactionResponseMerRes.getStatus());
             icicipayoutTransactiondetails.setStatus(transactionStatusres.getData().getStatus());
                        // if (.getAdditionalDetails() != null) {
            //     if (res.getStatus() == 2) {
            //         AdditionalDetails additionalDetails = obMapper.readValue(res.getAdditionalDetails(),
            //                 AdditionalDetails.class);
            //         if (additionalDetails.getErrorMessage() != null) {
            //             modelOp.setErrorString(additionalDetails.getErrorMessage());
            //             walletTransferRes.setStatusMessage(additionalDetails.getErrorMessage());
            //             walletTransferRes.setStatus("PENDING");
            //             walletTransferRes.setErrorMsg(additionalDetails.getErrorMessage());
            //         }
               }
                if (transactionStatusres.getData().getStatus().equalsIgnoreCase("Completed")) {

                int statusapiamount = Integer.parseInt(transactionStatusres.getData().getAmount());
                int txdetailsamount = Integer.parseInt(String.valueOf(transactionDetails.getAmount()));
                logger.info("\n\nAMOUNT VALIDATION:\t"+ statusapiamount+"\n"+txdetailsamount+"\n\n");
                if((statusapiamount - txdetailsamount) == 0){
                    icicipayoutTransactiondetails.setStatusMessage((transactionStatusres.getMessage()));
                        transactionResponseMerRes.setStatusMessage(transactionStatusres.getMessage());
                        transactionResponseMerRes.setStatus("SUCCESS");
                        transactionResponseMerRes.setUtrId(transactionStatusres.getData().getUtr());
                        icicipayoutTransactiondetails.setTx_status(transactionResponseMerRes.getStatus());
                        icicipayoutTransactiondetails.setStatus(transactionStatusres.getData().getStatus());
                        icicipayoutTransactiondetails.setVpayappError("AMOUNT VALIDATION SUCCESSFUL");
                        icicirepository.save(icicipayoutTransactiondetails);
                         }else {icicipayoutTransactiondetails.setStatusMessage((transactionStatusres.getMessage()));
                        transactionResponseMerRes.setStatusMessage(transactionStatusres.getMessage());
                        transactionResponseMerRes.setStatus("PENDING");
                        icicipayoutTransactiondetails.setTx_status(transactionResponseMerRes.getStatus());
                        icicipayoutTransactiondetails.setStatus(transactionStatusres.getData().getStatus());
                        icicipayoutTransactiondetails.setVpayappError("AMOUNT VALIDATION FAILED and STATUS GETTING SUCCESS");
                         }
                        }
            
                icicirepository.save(icicipayoutTransactiondetails);
                return transactionResponseMerRes;
            } 

         catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }

 @Override
    public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException {

        throw new UnsupportedOperationException("Unimplemented method 'doAccountTransferUPI'");
    }
}


    
