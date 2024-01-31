package io.asktech.payout.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.admin.ConfigPgMerchantDto;
import io.asktech.payout.dto.admin.PayOutCallBackRequest;
import io.asktech.payout.dto.admin.TrxnWithParameter;
import io.asktech.payout.dto.admin.UpdateTransactionDetailsRequestDto;
import io.asktech.payout.dto.admin.PgConfigResponse;
import io.asktech.payout.dto.admin.PgCreationDto;
import io.asktech.payout.dto.admin.PgResponse;
import io.asktech.payout.dto.admin.PgUpdateRequest;
import io.asktech.payout.dto.admin.TransactionChangeRequestDto;
import io.asktech.payout.dto.admin.TransactionReversalRequest;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.MerchantPgConfig;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.PayoutMerchant;
import io.asktech.payout.service.admin.ConfigPgMerchant;
import io.asktech.payout.service.admin.DashboardReportService;
import io.asktech.payout.service.admin.PgService;
import io.asktech.payout.service.admin.TransactionChangeRequestService;
import io.asktech.payout.service.admin.TransactionManagement;
import unirest.shaded.org.apache.http.ParseException;

@RestController
@RequestMapping("/controller")
public class PayoutAdmin {

    @Autowired
    PgService pgService;

    @Autowired
    DashboardReportService dashboardReportService;

    static Logger logger = LoggerFactory.getLogger(PayoutAdmin.class);

    @PostMapping(value = "/pgCreation")
    public ResponseEntity<?> pgCreation(@RequestBody PgCreationDto dto)
            throws ValidationExceptions {
        PgDetails pgres = pgService.createPg(dto);

        return ResponseEntity.ok().body(new PgResponse("SUCCESS", pgres));
    }

    @PostMapping(value = "/pgUpdate")
    public ResponseEntity<?> pgUpdate(@RequestBody PgUpdateRequest pgUpdateRequest)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        PgDetails pgres = pgService.updatePg(pgUpdateRequest);
        return ResponseEntity.ok().body(new PgResponse("SUCCESS", pgres));
    }

    @GetMapping(value = "/getAllPg")
    public ResponseEntity<?> getAllPg() {
        return ResponseEntity.ok().body(pgService.getAllPg());
    }

    @Autowired
    ConfigPgMerchant configPgMerchant;

    @PostMapping(value = "/configPgMerchant")
    public ResponseEntity<?> linkPgMerchant(@RequestBody ConfigPgMerchantDto dto)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        MerchantPgConfig pgres = configPgMerchant.createConfigPgMerchant(dto);

        return ResponseEntity.ok().body(new PgConfigResponse("SUCCESS", pgres));
    }

    @PostMapping(value = "/updateConfigPgMerchant")
    public ResponseEntity<?> updateLinkPgMerchant(@RequestBody ConfigPgMerchantDto dto)
            throws ValidationExceptions {
        MerchantPgConfig pgres = configPgMerchant.updateConfigPgMerchant(dto);

        return ResponseEntity.ok().body(new PgConfigResponse("SUCCESS", pgres));
    }

    @GetMapping(value = "/getAllConfigPgMerchant")
    public ResponseEntity<?> getAllLinkPgMerchant()
            throws ValidationExceptions {
        return ResponseEntity.ok().body(configPgMerchant.getAllMerchantPgLinks());
    }

    @GetMapping(value = "/getAllConfigPgNameMerchant")
    public ResponseEntity<?> getAllConfigPgNameMerchant()
            throws ValidationExceptions {
        return ResponseEntity.ok().body(configPgMerchant.getAllConfigPgNameMerchant());
    }

    @Autowired
    TransactionManagement transactionManagement;

    @PostMapping(value = "/transactionReversal")
    public ResponseEntity<?> transactionReversal(@RequestBody TransactionReversalRequest dto)
            throws JsonProcessingException {
        return ResponseEntity.ok().body(transactionManagement.transactionReversal(dto));
    }

    @PostMapping(value = "/walletRecharge")
    public ResponseEntity<?> walletRecharge(@RequestBody TransactionReversalRequest dto)
            throws JsonProcessingException {
        return ResponseEntity.ok().body(transactionManagement.transactionReversal(dto));
    }

    @Autowired
    private TransactionChangeRequestService transactionChangeRequestService;

    @PutMapping(value = "/updateTransactionStatus")
    public ResponseEntity<?> updateTransactionStatus(
            @RequestBody TransactionChangeRequestDto transactionChangeRequestDto)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok()
                .body(transactionChangeRequestService.updateTransactionStatus(transactionChangeRequestDto));
    }

    @PutMapping(value = "/updateBulkFileTransactionStatus")
    public void updateBulkFileTransactionStatus(
            @RequestBody TransactionChangeRequestDto transactionChangeRequestDto)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        transactionChangeRequestService.updateBulkFileTransactionStatus(transactionChangeRequestDto);
    }

    @GetMapping(value = "/getallTransactionChangeRequest")
    public ResponseEntity<?> getallTransactionChangeRequest()
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok().body(transactionChangeRequestService.getallTransactionChangeRequest());
    }

    // Admin Dash Bard Reports
    @GetMapping(value = "/getByMerchantWisePgWiseSum/{start_date}/{end_date}/{status}")
    public ResponseEntity<?> getByMerchantWisePgWiseSum(@PathVariable("start_date") String start_date,
            @PathVariable("end_date") String end_date, @PathVariable("status") String status)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok()
                .body(dashboardReportService.getByMerchantWisePgWiseSum(start_date, end_date, status));
    }

    @GetMapping(value = "/getPgTypeAndCountByStatusAndDate/{start_date}/{end_date}/{status}")
    public ResponseEntity<?> getPgTypeAndCountByStatusAndDate(@PathVariable("start_date") String start_date,
            @PathVariable("end_date") String end_date, @PathVariable("status") String status)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok()
                .body(dashboardReportService.getPgTypeAndCountByStatusAndDate(start_date, end_date, status));
    }

    @GetMapping(value = "/getHourandCountStatusAndDate/{start_date}/{status}")
    public ResponseEntity<?> getHourandCountStatusAndDate(@PathVariable("start_date") String start_date,
            @PathVariable("status") String status)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok().body(dashboardReportService.getHourandCountStatusAndDate(start_date, status));
    }

    @GetMapping(value = "/getMinuteandCountByStatus/{status}")
    public ResponseEntity<?> getMinuteandCountByStatus(@PathVariable("status") String status)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok().body(dashboardReportService.getMinuteandCountByStatus(status));
    }

    @GetMapping(value = "/getStatusCount/{start_date}/{end_date}")
    public ResponseEntity<?> getStatusCount(@PathVariable("start_date") String start_date,
            @PathVariable("end_date") String end_date)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok().body(dashboardReportService.getStatusCount(start_date, end_date));
    }

    @GetMapping(value = "/getLastTrxMerchList/{start_date}")
    public ResponseEntity<?> getLastTrxMerchList(@PathVariable("start_date") String start_date)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok().body(dashboardReportService.getLastTrxMerchList(start_date));
    }

    @GetMapping(value = "/getStatusAndMinuteWiseCount")
    public ResponseEntity<?> getStatusAndMinuteWiseCount()
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok().body(dashboardReportService.getStatusAndMinuteWiseCount());
    }

    @GetMapping(value = "/getHourandStatusWiseCountAndDate/{start_date}")
    public ResponseEntity<?> getHourandStatusWiseCountAndDate(@PathVariable("start_date") String start_date)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok().body(dashboardReportService.getHourandStatusWiseCountAndDate(start_date));
    }

    @PostMapping(value = "/txnWithParameters")
    public ResponseEntity<?> txnWithParameters(@RequestBody TrxnWithParameter dto)
            throws JsonProcessingException, ValidationExceptions, ParseException {
        return ResponseEntity.ok()
                .body(dashboardReportService.txnWithParameters(dto.getMerchant_id(), dto.getPg_id(),
                        dto.getStart_date(), dto.getEnd_date(), dto.getOder_id(), dto.getTrx_msg(), dto.getUtrid(),
                        dto.getInternalOrderId(), dto.getInternalOrderIds(), dto.getUtrids()));
    }

    @PutMapping(value = "/updateCallBackFlag")
    public void updateCallBackFlag(
            @RequestBody PayOutCallBackRequest validOrder) {
        logger.info("calling.. dashboard service for update call back: ");
        dashboardReportService.updateCallBackFlag(validOrder);
    }
}
