package io.asktech.payout.wallet.service;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.mainwallet.dto.MainWalletRechargeReqDto;
import io.asktech.payout.modal.admin.MerchantRecharge;
import io.asktech.payout.repository.admin.MerchantRechargeRepo;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.wallet.dto.WalletRechargeReqDto;
import io.asktech.payout.wallet.dto.WalletRechargeRequest;
import io.asktech.payout.wallet.dto.WalletRechargeResDto;
import io.asktech.payout.wallet.dto.WalletRechargeResponse;
import io.asktech.payout.wallet.modal.WalletDetails;
import io.asktech.payout.wallet.repository.WalletDetailsRepo;

@Service
public class WalletManagement {

        @Autowired
        private MerchantRechargeRepo merchantRechargeRepo;

        @Autowired
        private MainWalletService mainWalletService;

        @Autowired
        private WalletService walletService;
        @Autowired
        private WalletDetailsRepo walletDetailsRepo;
        static Logger logger = LoggerFactory.getLogger(WalletService.class);

        public WalletRechargeResponse walletRecharge(WalletRechargeRequest walletRechargeRequest)
                        throws  ValidationExceptions, ParseException, JsonProcessingException {

                 if (walletRechargeRequest.getRechargeType().equals("BANK TRANSFER")) {

                MerchantRecharge merchantRecharge2 = merchantRechargeRepo
                                .findByUtrid(walletRechargeRequest.getUtrid());
                if (merchantRecharge2 != null) {
                        throw new ValidationExceptions("Merchant Recharge ERROR (Check  UTRID )",
                                        FormValidationExceptionEnums.FATAL_EXCEPTION);
                }

                WalletDetails walletDetails = walletDetailsRepo
                                .findByMerchantid(walletRechargeRequest.getMerchantId());
                if (walletDetails == null) {
                        logger.info("WalletDetails Exception not findByMerchantid");
                        throw new ValidationExceptions("WalletDetails ERROR (Check  Merchantid )",
                                        FormValidationExceptionEnums.FATAL_EXCEPTION);
                }

                System.out.println("\n\n" + "wallet Details   " + walletDetails.getWalletid() + "\n\n");

                WalletRechargeResponse walletRechargeResponse = new WalletRechargeResponse();
                MerchantRecharge merchantRecharge = new MerchantRecharge();
                merchantRecharge.setAmount(walletRechargeRequest.getAmount());
                merchantRecharge.setMerchantId(walletRechargeRequest.getMerchantId());
                merchantRecharge.setBankName(walletRechargeRequest.getBankName());
                merchantRecharge.setCommission(walletRechargeRequest.getCommission());
                merchantRecharge.setNote1(walletRechargeRequest.getNote1());
                merchantRecharge.setNote2(walletRechargeRequest.getNote2());
                merchantRecharge.setNote3(walletRechargeRequest.getNote3());
                merchantRecharge.setRechargeAgent(walletRechargeRequest.getRechargeAgent());
                merchantRecharge.setReferenceId(walletRechargeRequest.getReferenceId());
                merchantRecharge.setReferenceName(walletRechargeRequest.getReferenceName());
                merchantRecharge.setUtrid(walletRechargeRequest.getUtrid());
                merchantRecharge.setRechargeAgentName(walletRechargeRequest.getRechargeAgentName());
                merchantRecharge.setWalletId(walletDetails.getWalletid());
                merchantRecharge.setStatus("PENDING");
                merchantRecharge.setRechargeType(walletRechargeRequest.getRechargeType());

                merchantRecharge.setRechargeId(Utility.getRandomId());

                merchantRecharge.setMainWalletId(walletDetails.getMainWalletid());

                merchantRecharge = merchantRechargeRepo.save(merchantRecharge);
                MainWalletRechargeReqDto mainWalletRechargeReqDto = new MainWalletRechargeReqDto();
                mainWalletRechargeReqDto.setAmount(merchantRecharge.getAmount());
                mainWalletRechargeReqDto
                                .setPurpose(merchantRecharge.getBankName() + "|" + merchantRecharge.getNote2());
                mainWalletRechargeReqDto.setRemarks(merchantRecharge.getNote1());
                mainWalletRechargeReqDto.setTransactionType("BANK TRANSFER");

                // MainWalletRechargeResDto mainWalletRechargeResDto =
                mainWalletService.walletAddBalance(mainWalletRechargeReqDto, walletDetails.getMainWalletid());

                double total = Double.parseDouble(merchantRecharge.getAmount())
                                - Double.parseDouble(merchantRecharge.getCommission());
                System.out.println(total);
                if (total < 0) {
                        throw new ValidationExceptions("Check Amount And Msak",
                                        FormValidationExceptionEnums.FATAL_EXCEPTION);
                }
                logger.info(merchantRecharge.getAmount() + "MR And c" + merchantRecharge.getCommission()
                                + "total"
                                + total);

                WalletRechargeReqDto walletRechargeReqDto = new WalletRechargeReqDto();
                walletRechargeReqDto.setAmount(total + "");
                walletRechargeReqDto
                                .setPurpose(merchantRecharge.getBankName() + "|" + merchantRecharge.getNote2());
                walletRechargeReqDto.setReferenceId(merchantRecharge.getReferenceId());
                walletRechargeReqDto.setRemarks(merchantRecharge.getNote1());
                walletRechargeReqDto.setTransactionType("BANK TRANSFER");

                System.out.println("\n\n" + merchantRecharge.getMerchantId() + "\n\n"); // Error Code

                WalletRechargeResDto walletRechargeResDto = walletService.walletAddBalance(walletRechargeReqDto,
                                merchantRecharge.getMerchantId());

                System.out.println("\n\n" + walletRechargeResDto + "\n\n");

                merchantRecharge.setAmount(walletRechargeResDto.getAmount());
                merchantRecharge.setStatus(walletRechargeResDto.getStatus());
                merchantRecharge.setMainWalletId(walletRechargeResDto.getMainWalletId());
                // Update the value like status amount
                merchantRecharge = merchantRechargeRepo.save(merchantRecharge);
                // set the responce
                walletRechargeResponse.setAmount(merchantRecharge.getAmount());
                walletRechargeResponse.setBankName(merchantRecharge.getBankName());
                walletRechargeResponse.setMerchantId(merchantRecharge.getMerchantId());
                walletRechargeResponse.setReferenceId(merchantRecharge.getReferenceId());
                walletRechargeResponse.setStatus(merchantRecharge.getStatus());
                walletRechargeResponse.setMainWalletId(merchantRecharge.getMainWalletId());
                walletRechargeResponse.setReferenceName(merchantRecharge.getReferenceName());
                walletRechargeResponse.setRechargeAgentName(merchantRecharge.getRechargeAgentName());
        
                return walletRechargeResponse;

                } else if (walletRechargeRequest.getRechargeType().equals("WALLET")) {
                // if (walletRechargeRequest.getUtrid() != null) {
                // throw new ValidationExceptions("Check UTRID, NOT REQUIRED IN WALLET",
                // FormValidationExceptionEnums.NOT_A_FIELED);
                // }
                WalletRechargeResponse walletRechargeResponse = new WalletRechargeResponse();
                WalletDetails walletDetails = walletDetailsRepo
                .findByMerchantid(walletRechargeRequest.getMerchantId());
                if (walletDetails == null) {
                logger.info("WalletDetails Exception not findByMerchantid");
                throw new ValidationExceptions("WalletDetails ERROR (Check Merchantid )",
                FormValidationExceptionEnums.FATAL_EXCEPTION);
                }

                System.out.println("\n\n" + "wallet Details " + walletDetails.getWalletid() +
                "\n\n");

                MerchantRecharge merchantRecharge = new MerchantRecharge();
                merchantRecharge.setAmount(walletRechargeRequest.getAmount());
                merchantRecharge.setMerchantId(walletRechargeRequest.getMerchantId());
                merchantRecharge.setBankName(walletRechargeRequest.getBankName());
                merchantRecharge.setCommission(walletRechargeRequest.getCommission());
                merchantRecharge.setNote1(walletRechargeRequest.getNote1());
                merchantRecharge.setNote2(walletRechargeRequest.getNote2());
                merchantRecharge.setNote3(walletRechargeRequest.getNote3());
                merchantRecharge.setRechargeAgent(walletRechargeRequest.getRechargeAgent());
                merchantRecharge.setReferenceId(walletRechargeRequest.getReferenceId());
                merchantRecharge.setReferenceName(walletRechargeRequest.getReferenceName());
                merchantRecharge.setRechargeAgentName(walletRechargeRequest.getRechargeAgentName());
                merchantRecharge.setRechargeType(walletRechargeRequest.getRechargeType());
                merchantRecharge.setWalletId(walletDetails.getWalletid());
                merchantRecharge.setStatus("PENDING");

                merchantRecharge.setRechargeId(Utility.getRandomId());

                merchantRecharge.setMainWalletId(walletDetails.getMainWalletid());

                merchantRecharge = merchantRechargeRepo.save(merchantRecharge);
                // MainWalletRechargeReqDto mainWalletRechargeReqDto = new
                // MainWalletRechargeReqDto();
                // mainWalletRechargeReqDto.setAmount(merchantRecharge.getAmount());
                // mainWalletRechargeReqDto
                // .setPurpose(merchantRecharge.getBankName() + "|" +
                // merchantRecharge.getNote2());
                // mainWalletRechargeReqDto.setRemarks(merchantRecharge.getNote1());
                // mainWalletRechargeReqDto.setTransactionType("BANK TRANSFER");

                // // MainWalletRechargeResDto mainWalletRechargeResDto =
                // mainWalletService.walletAddBalance(mainWalletRechargeReqDto,
                // walletDetails.getMainWalletid());

                double total = Double.parseDouble(merchantRecharge.getAmount())
                - Double.parseDouble(merchantRecharge.getCommission());
                System.out.println(total);
                if (total < 0) {
                throw new ValidationExceptions("Check Amount And Msak",
                FormValidationExceptionEnums.FATAL_EXCEPTION);
                }
                logger.info(merchantRecharge.getAmount() + "MR And c" +
                merchantRecharge.getCommission()
                + "total"
                + total);

                WalletRechargeReqDto walletRechargeReqDto = new WalletRechargeReqDto();
                walletRechargeReqDto.setAmount(total + "");
                walletRechargeReqDto
                .setPurpose(merchantRecharge.getBankName() + "|" +
                merchantRecharge.getNote2());
                walletRechargeReqDto.setReferenceId(merchantRecharge.getReferenceId());
                walletRechargeReqDto.setRemarks(merchantRecharge.getNote1());
                walletRechargeReqDto.setTransactionType("WALLET");

                System.out.println("\n\n" + merchantRecharge.getMerchantId() + "\n\n"); //
               // Error Code

                WalletRechargeResDto walletRechargeResDto =
                walletService.walletAddBalance(walletRechargeReqDto,
                merchantRecharge.getMerchantId());

                System.out.println("\n\n" + walletRechargeResDto + "\n\n");

                merchantRecharge.setAmount(walletRechargeResDto.getAmount());
                merchantRecharge.setStatus(walletRechargeResDto.getStatus());
                merchantRecharge.setMainWalletId(walletRechargeResDto.getMainWalletId());
                // Update the value like status amount
                merchantRecharge = merchantRechargeRepo.save(merchantRecharge);
                // set the responce
                walletRechargeResponse.setAmount(merchantRecharge.getAmount());
                walletRechargeResponse.setBankName(merchantRecharge.getBankName());
                walletRechargeResponse.setMerchantId(merchantRecharge.getMerchantId());
                walletRechargeResponse.setReferenceId(merchantRecharge.getReferenceId());
                walletRechargeResponse.setStatus(merchantRecharge.getStatus());
                walletRechargeResponse.setMainWalletId(merchantRecharge.getMainWalletId());
                walletRechargeResponse.setReferenceName(merchantRecharge.getReferenceName());
                walletRechargeResponse.setRechargeAgentName(merchantRecharge.getRechargeAgentName());
                return walletRechargeResponse;

                } else {
                throw new ValidationExceptions("CHECK PAYMENT_TYPE WALLET OR BANK TRANSFER)",
                FormValidationExceptionEnums.PAYMENT_TYPE_INPUT_ERROR);
                }
        }

        public MerchantRecharge getByMerchantRechargeByUtrid(String utrid) {

                return merchantRechargeRepo.findByUtrid(utrid);
        }

        public List<MerchantRecharge> getByMerchantRechargeByMerchantId(String merchantId) {
                return merchantRechargeRepo.findAllByMerchantId(merchantId);
        }

        public List<MerchantRecharge> getMerchantRechargeDateRange(String dateFrom, String dateTo) {
                return merchantRechargeRepo.getMerchantRechargeDateRange(dateFrom, dateTo);
        }

        public List<MerchantRecharge> getAllByMerchantRecharge() {
                return merchantRechargeRepo.findAll();
        }
}
