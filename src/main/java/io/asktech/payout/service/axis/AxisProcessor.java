package io.asktech.payout.service.axis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.IProcessor;
import io.asktech.payout.service.axis.dto.FundtransferDto;
import io.asktech.payout.service.axis.dto.FundtransferDtoD;
import io.asktech.payout.service.axis.dto.GetStatusRequestD;
import io.asktech.payout.service.axis.dto.GetStatusResponseBody;
import io.asktech.payout.service.axis.dto.GetStatusResponseD;
import io.asktech.payout.service.axis.dto.StatusRequestDto;
import io.asktech.payout.service.axis.dto.StatusRequestDtoD;
import io.asktech.payout.service.axis.dto.StatusResponseDto;
import io.asktech.payout.service.axis.dto.StatusResponseDtoD;
import io.asktech.payout.service.axis.dto.TransferPaymentRequestD;
import io.asktech.payout.service.axis.dto.TransferPaymentResponseBody;
import io.asktech.payout.service.axis.dto.TransferPaymentResponseD;
import io.asktech.payout.service.axis.dto.TransferPaymentResponseMain;
import io.asktech.payout.service.axis.dto.TransferPaymentResponseMainD;
import io.asktech.payout.service.axis.model.AxisBeneficiary;
import io.asktech.payout.service.axis.model.AxisUtilityModel;
import io.asktech.payout.service.axis.repo.AxisBeneficaryRepo;
import io.asktech.payout.service.axis.repo.AxisUtilityRepo;
import io.asktech.payout.utils.Utility;

@Service
public class AxisProcessor implements IProcessor {

    static Logger logger = LoggerFactory.getLogger(AxisProcessor.class);

    @Autowired
    AxisUtility axisUtility;
    @Autowired
    AxisRequest axisRequest;
    @Autowired
    AxisBeneficaryRepo axisBeneficaryRepo;
    @Autowired
    AxisUtilityRepo axisUtilityRepo;

    @Override
    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg) {
        // TODO Auto-generated method stub

        logger.info("new benificiary will be add\n some data neded\n");

        AxisUtilityModel axisUtilityModel = new AxisUtilityModel();
        // AxisUtilityModel axisUtilityModel =
        // axisUtilityRepo.findByOrderId(dto.getOrderid());

        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        ObjectMapper objectMapper = new ObjectMapper();
        axisUtilityModel.setAccessKey(pg.getPgId());
        axisUtilityModel.setAccessSecret(pg.getPgConfigSecret());
        axisUtilityModel.setOrderId(dto.getOrderid());
        axisUtilityModel.setMerchantId(merchantid);
        axisUtilityRepo.save(axisUtilityModel);
        try {

            // CHECK WHETHER A BENEFICIARY ALREADY EXISTS
            logger.info("checking if data already exists\n");
            AxisBeneficiary axisBeneficarymodel = axisBeneficaryRepo.findByBeneBankAccount(dto.getBankaccount());

            if (axisBeneficarymodel == null) {
                logger.info("new benificiary will be add");
                String customerUniqueId = axisUtility.createBeneCode(merchantid);
                logger.info("new benificiary code " + customerUniqueId);
                AxisBeneficiary axisBeneficarymodel1 = new AxisBeneficiary();
                axisBeneficarymodel1.setBeneCode(customerUniqueId);
                axisBeneficarymodel1.setBeneBankAccount(dto.getBankaccount());
                axisBeneficarymodel1.setBeneName(dto.getBeneficiaryName());
                axisBeneficarymodel1.setBeneIfscCode(dto.getIfsc());

                axisBeneficaryRepo.save(axisBeneficarymodel1);
                // CREATE THE BENEFICIARY REQUEST
                // BenificaryDto beneficaryRequest = axisUtility.addBenificary(dto, merchantid,
                // pg, customerUniqueId);
                // logger.info("new benificiary create request" +
                // Utility.convertDTO2JsonString(beneficaryRequest));
                // axisUtilityModel.setBenificaryRequest(Utility.convertDTO2JsonString(beneficaryRequest));

                // ADD THE REQUEST
                // BeneficiaryDtoDcrypt beneDcryptRequest = new BeneficiaryDtoDcrypt();
                // BeneficiaryRegistrationRequestD beneficiaryRegistrationRequestD = new
                // BeneficiaryRegistrationRequestD();
                // beneficiaryRegistrationRequestD
                // .setSubHeader(beneficaryRequest.getBeneficiaryRegistrationRequest().getSubHeader());
                // String decryptedBody = beneficaryRequest.getBeneficiaryRegistrationRequest()
                // .getBeneficiaryRegistrationRequestBodyEncrypted();
                // decryptedBody = axisUtility.decryptCallBack(pg.getPgConfig1(),
                // decryptedBody);
                // BeneficiaryRegistrationRequestBody beneficiaryDecryptedRequest =
                // objectMapper.readValue(decryptedBody,
                // BeneficiaryRegistrationRequestBody.class);

                // beneficiaryRegistrationRequestD.setBeneficiaryRegistrationRequestBody(beneficiaryDecryptedRequest);
                // beneDcryptRequest.setBeneficiaryRegistrationRequestD(beneficiaryRegistrationRequestD);
                // axisUtilityModel.setBenificaryRequestDecrypt(Utility.convertDTO2JsonString(beneDcryptRequest));

                // MAKE THE REQUEST
                // BeneficiaryResponseDto beneficiaryResponse =
                // axisRequest.addBeneficiaryRequest(beneficaryRequest, pg);
                // axisUtilityModel.setBenificaryResponse(Utility.convertDTO2JsonString(beneficiaryResponse));
                // // DECRYPT THE RESPONSE
                // logger.info("Beneficary Resposne" +
                // Utility.convertDTO2JsonString(beneficiaryResponse));

                // decryptedBody = beneficiaryResponse.getBeneficiaryRegistrationResponse()
                // .getBeneficiaryRegistrationResponseBodyEncrypted();

                // decryptedBody = axisUtility.decryptCallBack(pg.getPgConfig1(),
                // decryptedBody);
                // BenficiaryRegistrationRespBody beneficiaryDecryptedResponse =
                // objectMapper.readValue(decryptedBody,
                // BenficiaryRegistrationRespBody.class);
                // logger.info("new benificiary create response::"
                // + Utility.convertDTO2JsonString(beneficiaryDecryptedResponse));

                // BeneficiaryResponseDtoD beneResponseDtoDcrypt = new
                // BeneficiaryResponseDtoD();
                // BeneficiaryRegistrationResponseD beneRegistrationRes = new
                // BeneficiaryRegistrationResponseD();
                // beneRegistrationRes
                // .setSubHeader(beneficiaryResponse.getBeneficiaryRegistrationResponse().getSubHeader());
                // beneRegistrationRes.setBeneficiaryRegistrationResponseBody(decryptedBody);
                // beneResponseDtoDcrypt.setBeneficiaryRegistrationResponseD(beneRegistrationRes);

                // axisUtilityModel.setBenificaryResponseDcrypt(Utility.convertDTO2JsonString(beneResponseDtoDcrypt));
                // axisUtilityRepo.save(axisUtilityModel);
                // if (beneficiaryDecryptedResponse.getStatus().equals("F")) {
                // logger.info("Benificary add is FAILURE");

                // walletTransferRes.setStatus("PENDING");
                // walletTransferRes.setStatusMessage("PENDING");
                // walletTransferRes.setErrorMsg(beneficiaryDecryptedResponse.getMessage());
                // return walletTransferRes;
                // }

            }

            axisBeneficarymodel = axisBeneficaryRepo.findByBeneBankAccount(dto.getBankaccount());

            if (axisBeneficarymodel == null) {
                logger.info("Benificary add is FAILURE");
                walletTransferRes.setStatus("PENDING");
                walletTransferRes.setStatusMessage("PENDING");
                walletTransferRes.setErrorMsg("beneCode is not created");
                return walletTransferRes;

            }
            // axisUtilityModel.setBeneCode(axisBeneficarymodel.getBeneCode());

            // NOW FUND TRANSFER REQUEST
            FundtransferDto transferPaymentRequest = axisUtility.fundTransferRequest(axisBeneficarymodel.getBeneCode(),
                    dto, pg);

            FundtransferDtoD transferPaymentRequestD = new FundtransferDtoD();
            TransferPaymentRequestD transferPaymentD = new TransferPaymentRequestD();
            transferPaymentD.setSubHeader(transferPaymentRequest.getTransferPaymentRequest().getSubHeader());
            transferPaymentD.setTransferPaymentRequestBody(axisUtility.decryptCallBack(pg.getPgConfig1(),
                    transferPaymentRequest.getTransferPaymentRequest().getTransferPaymentRequestBodyEncrypted()));
            transferPaymentRequestD.setTransferPaymentRequest(transferPaymentD);

            axisUtilityModel.setPaymentRequestDcrypt(Utility.convertDTO2JsonString(transferPaymentRequestD));
            axisUtilityModel.setPaymentRequest(Utility.convertDTO2JsonString(transferPaymentRequest));
            logger.info(Utility.convertDTO2JsonString(transferPaymentRequest));
            // MAKE THE PAYMENT REQUEST
            TransferPaymentResponseMain transferPaymentResponse = axisRequest
                    .fundTransferRequest(transferPaymentRequest, pg);
            logger.info(Utility.convertDTO2JsonString(transferPaymentResponse));

            axisUtilityModel.setPaymentResponse(Utility.convertDTO2JsonString(transferPaymentResponse));

            String decryptedBody = transferPaymentResponse.getTransferPaymentResponse()
                    .getTransferPaymentResponseBodyEncrypted();
            decryptedBody = axisUtility.decryptCallBack(pg.getPgConfig1(), decryptedBody);

            TransferPaymentResponseMainD transferPaymentResponseMainD = new TransferPaymentResponseMainD();
            TransferPaymentResponseD transferPaymentResponseD = new TransferPaymentResponseD();
            transferPaymentResponseD.setSubHeader(transferPaymentResponse.getTransferPaymentResponse().getSubHeader());
            transferPaymentResponseD.setTransferPaymentResponseBody(decryptedBody);
            transferPaymentResponseMainD.setTransferPaymentResponse(transferPaymentResponseD);
            axisUtilityModel.setPaymentResponseDcrypt(Utility.convertDTO2JsonString(transferPaymentResponseMainD));
            axisUtilityRepo.save(axisUtilityModel);

            logger.info("some error will be after that");
            // MAP INTO THE OBJECT
            TransferPaymentResponseBody fundDecryptedResponse = objectMapper.readValue(decryptedBody,
                    TransferPaymentResponseBody.class);
            logger.info(Utility.convertDTO2JsonString(fundDecryptedResponse));

            axisUtilityRepo.save(axisUtilityModel);
            // CHECKING THE RESPONSE
            if (fundDecryptedResponse.getStatus().equals("F")) {
                axisUtilityModel.setStatus("FAILURE");
                axisUtilityRepo.save(axisUtilityModel);
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes.setStatusMessage("FAILURE");
                walletTransferRes.setErrorMsg(fundDecryptedResponse.getMessage());
                return walletTransferRes;
            }

            // Checking the status before updating the table

            // create Status Request

            StatusRequestDto statusRequest = axisUtility.createStatusRequest(dto.getOrderid(), pg);
            logger.info("status request : " + Utility.convertDTO2JsonString(statusRequest));
            axisUtilityModel.setStatusRequest(Utility.convertDTO2JsonString(statusRequest));

            StatusRequestDtoD statusRequestDtoD = new StatusRequestDtoD();
            GetStatusRequestD getStatusRequestD = new GetStatusRequestD();
            getStatusRequestD.setSubHeader(statusRequest.getGetStatusRequest().getSubHeader());
            getStatusRequestD.setGetStatusRequestBody(axisUtility.decryptCallBack(pg.getPgConfig1(),
                    statusRequest.getGetStatusRequest().getGetStatusRequestBodyEncrypted()));
            statusRequestDtoD.setGetStatusRequest(getStatusRequestD);

            axisUtilityModel.setStatusRequestDcrypt(Utility.convertDTO2JsonString(statusRequestDtoD));

            StatusResponseDto statusResponse = axisRequest.getStatusRequest(statusRequest, pg);
            logger.info("status Response : " + Utility.convertDTO2JsonString(statusResponse));
            axisUtilityModel.setStatusResponse(Utility.convertDTO2JsonString(statusResponse));

            decryptedBody = statusResponse.getGetStatusResponse().getGetStatusResponseBodyEncrypted();
            decryptedBody = axisUtility.decryptCallBack(pg.getPgConfig1(), decryptedBody);
            logger.info("Decrypted StatusBody : " + decryptedBody);

            StatusResponseDtoD statusResponseDtoD = new StatusResponseDtoD();
            GetStatusResponseD getStatusResponseD = new GetStatusResponseD();

            GetStatusResponseBody statusResponseBody = objectMapper.readValue(decryptedBody,
                    GetStatusResponseBody.class);
            getStatusResponseD.setSubHeader(statusResponse.getGetStatusResponse().getSubHeader());
            getStatusResponseD.setGetStatusRequestBody(statusResponseBody);
            statusResponseDtoD.setGetStatusResponse(getStatusResponseD);

            axisUtilityModel.setStatusResponseDcrypt(Utility.convertDTO2JsonString(statusResponseDtoD));

            logger.info("Another Satus body : " + Utility.convertDTO2JsonString(statusResponseBody));

            logger.info(
                    "some sarrys : " + Utility.convertDTO2JsonString(statusResponseBody.getData().getCUR_TXN_ENQ()));

            axisUtilityModel.setUtrId(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getUtrNo());
            axisUtilityModel.setStatus(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getTransactionStatus());
            axisUtilityModel
                    .setStatusMessage(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
            logger.info("status description : "
                    + statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
            axisUtilityRepo.save(axisUtilityModel);

            if (statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getTransactionStatus().equals("REJECTED")) {
                logger.info("Transaction is faled");
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes
                        .setStatusMessage(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                walletTransferRes
                        .setErrorMsg(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                return walletTransferRes;
            } else if (statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getTransactionStatus().equals("PENDING")) {
                logger.info("Transaction is Pendding");
                walletTransferRes.setStatus("PENDING");
                walletTransferRes.setStatusMessage("PENDING");
                walletTransferRes
                        .setErrorMsg(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                return walletTransferRes;
            } else if (statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getTransactionStatus()
                    .equals("PROCESSED") /* && statusResponseBody does not contai the amount */) {
                logger.info("Transaction is Success");
                walletTransferRes.setStatus("PENDING");
                walletTransferRes
                        .setStatusMessage(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                // walletTransferRes.setErrorMsg(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                return walletTransferRes;
            }

        }

        catch (Exception e) {
            logger.error("AXIS ERROR::" + e.getMessage());
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage("PENDING");
            walletTransferRes.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }

        axisUtilityRepo.save(axisUtilityModel);
        return walletTransferRes;
    }

    @Override
    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) {
        // TODO Auto-generated method stub

        TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
        try {

            StatusRequestDto statusRequest = axisUtility.createStatusRequest(dto.getOrderId(), pg);
            StatusResponseDto statusResponse = axisRequest.getStatusRequest(statusRequest, pg);

            String decryptedBody = statusResponse.getGetStatusResponse().getGetStatusResponseBodyEncrypted();
            decryptedBody = axisUtility.decryptCallBack(pg.getPgConfig1(), decryptedBody);

            ObjectMapper objectMapper = new ObjectMapper();
            StatusResponseDtoD statusResponseDtoD = new StatusResponseDtoD();

            GetStatusResponseBody statusResponseBody = objectMapper.readValue(decryptedBody,
                    GetStatusResponseBody.class);
            GetStatusResponseD responseStatusDecrpt = new GetStatusResponseD();
            responseStatusDecrpt.setSubHeader(statusResponse.getGetStatusResponse().getSubHeader());
            responseStatusDecrpt.setGetStatusRequestBody(statusResponseBody);
            statusResponseDtoD.setGetStatusResponse(responseStatusDecrpt);

            AxisUtilityModel axisUtilityModel = axisUtilityRepo.findByOrderId(dto.getOrderId());
            if (axisUtilityModel != null) {
                if ((statusResponseBody.getData().getCUR_TXN_ENQ()).size() > 0) {
                    axisUtilityModel.setStatusResponseDcrypt(Utility.convertDTO2JsonString(statusResponseDtoD));
                    axisUtilityModel
                            .setStatus(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getTransactionStatus());
                    axisUtilityModel
                            .setStatusMessage(
                                    statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                    axisUtilityModel.setUtrId(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getUtrNo());
                    axisUtilityRepo.save(axisUtilityModel);
                }
            }

            if (statusResponse != null) {
                transactionResponseMerRes = responseStatusUpdate(transactionResponseMerRes, responseStatusDecrpt);
            }
            logger.info("transactionResponseMerRes::" + Utility.convertDTO2JsonString(transactionResponseMerRes));
            if (statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription() != null) {
                transactionResponseMerRes
                        .setMessage(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                transactionResponseMerRes.setStatusMessage(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
            }
            logger.info("transactionResponseMerRes::" + Utility.convertDTO2JsonString(transactionResponseMerRes));
            transactionResponseMerRes.setAmount(transactionDetails.getAmount());
            if (statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getUtrNo() != null) {
                transactionResponseMerRes.setUtrId(statusResponseBody.getData().getCUR_TXN_ENQ().get(0).getUtrNo());
            } else {
                transactionResponseMerRes.setStatus("PENDING");
            }
            logger.info("transactionResponseMerRes::" + Utility.convertDTO2JsonString(transactionResponseMerRes));
            transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
            logger.info("axisStatusResponse::" + Utility.convertDTO2JsonString(responseStatusDecrpt));
        } catch (Exception e) {
            transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
            transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
            transactionResponseMerRes.setAmount(transactionDetails.getAmount());
            transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
            if (transactionDetails.getUtrid() != null) {
                transactionResponseMerRes.setUtrId(transactionDetails.getUtrid());
            }
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return transactionResponseMerRes;
    }

    @Override
    public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg) {
        // TODO Auto-generated method stub
        return null;
    }

    private TransactionResponseMerRes responseStatusUpdate(TransactionResponseMerRes walletTransferRes,
            GetStatusResponseD res) {

        walletTransferRes
                .setMessage(res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
        if (res.getGetStatusRequestBody().getStatus().equals("S")) {
            if (res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getUtrNo() == null) {
                walletTransferRes.setStatus("PENDING");
                return walletTransferRes;
            } else {

                if (res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getTransactionStatus()
                        .equals("REJECTED")) {
                    walletTransferRes.setStatus("FAILURE");
                    walletTransferRes.setMessage(
                            res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                    walletTransferRes.setStatusMessage(res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getStatusDescription());
                } else {
                    if ((res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getUtrNo().length() > 5)
                            && (res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getTransactionStatus()
                                    .equals("PROCESSED"))) {

                        walletTransferRes.setStatus("SUCCESS");
                    } else {
                        walletTransferRes.setStatus("PENDING");
                    }
                    walletTransferRes
                            .setUtrId(res.getGetStatusRequestBody().getData().getCUR_TXN_ENQ().get(0).getUtrNo());
                }

            }
            return walletTransferRes;
        } else {
            walletTransferRes.setStatus("PENDING");
            return walletTransferRes;
        }

    }

}
