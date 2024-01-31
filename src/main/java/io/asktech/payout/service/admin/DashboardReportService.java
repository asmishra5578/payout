package io.asktech.payout.service.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.asktech.payout.dto.admin.PayOutCallBackRequest;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;

@Service
public class DashboardReportService {

    private static final String MERCHANT_ID_NOT_FOUND = null;
    private static final String DATE_PARAMETER_IS_MANDATORY = null;
    private static final String DATE_FORMAT = null;
    @Autowired
    TransactionDetailsRepo transactionDetailsRepository;

    static Logger logger = LoggerFactory.getLogger(DashboardReportService.class);

    public Object getByMerchantWisePgWiseSum(String start_date, String end_date, String status) {
        return transactionDetailsRepository.getByMerchantWisePgWiseSumQuery(start_date, end_date, status);
    }

    public Object getPgTypeAndCountByStatusAndDate(String start_date, String end_date, String status) {
        return transactionDetailsRepository.getPgTypeAndCountByStatusAndDate(start_date, end_date, status);
    }

    public Object getHourandCountStatusAndDate(String start_date, String status) {
        return transactionDetailsRepository.getHourandCountStatusAndDate(start_date, status);
    }

    public Object getMinuteandCountByStatus(String status) {
        return transactionDetailsRepository.getMinuteandCountByStatus(status);
    }

    public Object getStatusCount(String start_date, String end_date) {
        return transactionDetailsRepository.getStatusCount(start_date, end_date);
    }

    public Object getLastTrxMerchList(String start_date) {
        return transactionDetailsRepository.getLastTrxMerchList(start_date);
    }

    public Object getStatusAndMinuteWiseCount() {
        return transactionDetailsRepository.getStatusAndMinuteWiseCount();
    }

    public Object getHourandStatusWiseCountAndDate(String start_date) {
        return transactionDetailsRepository.getHourandStatusWiseCountAndDate(start_date);
    }

    public boolean txnParam(String val) {
        if (val == null || val.isBlank() || val.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean validateDateFormat(String dateToValdate) throws ValidationExceptions {

        if (dateToValdate.isBlank() || dateToValdate.isEmpty()) {
            throw new ValidationExceptions(DATE_PARAMETER_IS_MANDATORY,
                    FormValidationExceptionEnums.DATE_PARAMETER_IS_MANDATORY);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateToValdate);
            // System.out.println("++validated DATE TIME ++"+formatter.format(parsedDate));

        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public void dateWiseValidation(String start_date, String end_date) throws ValidationExceptions {
        boolean checkStartFormat = validateDateFormat(start_date);
        boolean checkEndFormat = validateDateFormat(end_date);

        if (checkStartFormat == false || checkEndFormat == false || start_date.trim().contains(" ")
                || end_date.trim().contains(" ") || start_date.matches(".*[a-zA-Z]+.*")
                || end_date.matches(".*[a-zA-Z]+.*")) {
            throw new ValidationExceptions(DATE_FORMAT, FormValidationExceptionEnums.DATE_FORMAT);
        }

    }

    public List<TransactionDetails> txnWithParameters(String merchant_id, String pg_id, String start_date,
            String end_date, String oder_id, String trx_msg, String utrid, String internalOrderId,
            String internalOrderIds, String utrids)
            throws ValidationExceptions {
        // logger.info("oderId ::::: 1 "+oder_id);
        if (txnParam(merchant_id) == true) {
            if (transactionDetailsRepository.findAllByMerchantId(merchant_id).isEmpty()) {
                throw new ValidationExceptions(MERCHANT_ID_NOT_FOUND, FormValidationExceptionEnums.MERCHANT_NOT_FOUND);
            }
        }

        if (txnParam(start_date) == true && txnParam(end_date) == true) {
            dateWiseValidation(start_date, end_date);
        }

        if (txnParam(merchant_id) == true && txnParam(pg_id) == true) {
            return transactionDetailsRepository.findAllByMerchantIdAndPgId(merchant_id, pg_id);
        } else if (txnParam(merchant_id) == true && txnParam(internalOrderId) == true && txnParam(pg_id) == false) {
            return transactionDetailsRepository.findAllByMerchantIdAndInternalOrderId(merchant_id, internalOrderId);
        } else if (txnParam(merchant_id) == true && txnParam(internalOrderId) == true && txnParam(pg_id) == true) {
            return transactionDetailsRepository.findAllByMerchantIdAndInternalOrderIdAndPgId(merchant_id,
                    internalOrderId, pg_id);
        } else if (txnParam(merchant_id) == false && txnParam(internalOrderId) == true && txnParam(pg_id) == true) {
            return transactionDetailsRepository.findAllByInternalOrderIdAndPgId(internalOrderId, pg_id);
        } else if (txnParam(merchant_id) == false && txnParam(internalOrderId) == true && txnParam(pg_id) == false) {
            return transactionDetailsRepository.findAllByInternalOrderId(internalOrderId);
        } else if (txnParam(merchant_id) == false && txnParam(pg_id) == true
                && txnParam(internalOrderId) == false
                && txnParam(start_date) == true && txnParam(end_date) == true) {
            return transactionDetailsRepository.getTransactionByPgId(pg_id, start_date, end_date);
        } else if (txnParam(merchant_id) == true
                && txnParam(internalOrderId) == true && txnParam(pg_id) == true
                && start_date != null && end_date != null) {
            return transactionDetailsRepository.findAllByMerIdAndInterrnalOrAndPgId(merchant_id, internalOrderId, pg_id,
                    start_date, end_date);
        } else if (txnParam(pg_id) == true) {
            return transactionDetailsRepository.getTransactionByPgIdWithOutDate(pg_id);
        } else if (txnParam(internalOrderIds) == true) {

            List<String> oderIdsListInString = new ArrayList<>(Arrays.asList(internalOrderIds.split(",")));
            List<String> oderIdsListInString2 = new ArrayList<>();
            for (String string1 : oderIdsListInString) {
                if (string1.trim().length() > 5) {
                    oderIdsListInString2.add(string1);
                }

            }
            return transactionDetailsRepository.findByinternalOrderIdIn(oderIdsListInString2);
            // return
            // transactionDetailsRepository.findAllByMerchantOrderId(merchant_order_id);
        } else if (txnParam(start_date) == true && txnParam(end_date) == true) {
            return transactionDetailsRepository.findAllByTrDate(start_date, end_date);
        } else if (txnParam(start_date) == true && txnParam(end_date) == false) {
            validateDateFormat(start_date);
            return transactionDetailsRepository.getDataStartDate(start_date);
        } else if (txnParam(start_date) == false && txnParam(end_date) == true) {
            validateDateFormat(end_date);
            return transactionDetailsRepository.getDataEndDate(end_date);
        } else if (txnParam(start_date) == true && txnParam(end_date) == true && txnParam(trx_msg) == true) {
            validateDateFormat(end_date);
            return transactionDetailsRepository.findAllByTrx_msg_Like(trx_msg, start_date, end_date);
        } else if (txnParam(oder_id) == true) {
            List<String> oderIdsListInString = new ArrayList<>(Arrays.asList(oder_id.split(",")));
            List<String> oderIdsListInString2 = new ArrayList<>();
            for (String string1 : oderIdsListInString) {
                if (string1.trim().length() > 5) {
                    oderIdsListInString2.add(string1);
                }

            }
            return transactionDetailsRepository.findByorderIdIn(oderIdsListInString2);
            // return transactionDetailsRepository.findByOrderID(oder_id);
        } else if (txnParam(utrids) == true) {
            List<String> oderIdsListInString = new ArrayList<>(Arrays.asList(utrids.split(",")));
            List<String> oderIdsListInString2 = new ArrayList<>();
            for (String string1 : oderIdsListInString) {
                if (string1.trim().length() > 5) {
                    oderIdsListInString2.add(string1);
                }

            }
            return transactionDetailsRepository.findByutridIn(oderIdsListInString2);
            // return transactionDetailsRepository.findByOrderID(oder_id);
        }

        return null;

    }

    public void updateCallBackFlag(PayOutCallBackRequest validOrderId) {
        logger.info("valid order Ids for update call back: "+validOrderId.toString());
        validOrderId.getValidOrderIds().forEach(o -> {
            TransactionDetails transactionDetails = transactionDetailsRepository.findByinternalOrderId(o);
            if (transactionDetails != null) {
                transactionDetails.setCallBackFlag("true");
                transactionDetailsRepository.save(transactionDetails);
            }
        });
    }

}
