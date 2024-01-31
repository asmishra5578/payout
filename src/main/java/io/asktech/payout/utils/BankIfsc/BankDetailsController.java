package io.asktech.payout.utils.BankIfsc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.utils.Utility;
import io.asktech.payout.utils.RandomNameGenerator.RandomName;

@RestController
@RequestMapping("/utility")
public class BankDetailsController {
    @Autowired
    BankService bankService;
    static Logger logger = LoggerFactory.getLogger(BankDetailsController.class);
    @PostMapping(value = "/bankDetails")
    public ResponseEntity<?> saveBankDetails(@RequestBody BankDto dto) throws JsonProcessingException
    {   
        logger.info(Utility.convertDTO2JsonString(dto));
		return ResponseEntity.ok().body(bankService.setBankDetails(dto));
	}

    @GetMapping(value = "/bankDetails/{ifsc}")
    public ResponseEntity<?> getBankDetails(@PathVariable String ifsc)
    {   
		return ResponseEntity.ok().body(bankService.getBankDetails(ifsc));
	}
    @Autowired
    RandomName randomName;

    @GetMapping(value = "/getRandomName")
    public String getRandomName()
    {   
		return randomName.getRandomName();
	}

}
