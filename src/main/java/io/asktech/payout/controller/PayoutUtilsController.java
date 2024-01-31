package io.asktech.payout.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.service.payoutUtils.PaymentUtils;
import unirest.shaded.org.apache.http.ParseException;

@RestController
@RequestMapping("/util")
public class PayoutUtilsController {
    @Autowired
    PaymentUtils paymentUtils;
    @PostMapping(value = "/accountValidation/{merchantid}")
    public ResponseEntity<?> accountValidation(@PathVariable String merchantid,
            @RequestBody TransactionReportMerReq dto)
            throws JsonProcessingException, ValidationExceptions, ParseException {

        return ResponseEntity.ok().body("");
    }

    @PostMapping(value = "/upiValidation/{merchantid}")
    public ResponseEntity<?> upiValidation(@PathVariable String merchantid,
            @RequestBody TransactionReportMerReq dto)
            throws JsonProcessingException, ValidationExceptions, ParseException {

        return ResponseEntity.ok().body("");
    }
}
