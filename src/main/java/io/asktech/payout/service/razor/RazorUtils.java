package io.asktech.payout.service.razor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.razor.dto.req.Bank_Account;
import io.asktech.payout.service.razor.dto.req.Contact;
import io.asktech.payout.service.razor.dto.req.Fund_account;
import io.asktech.payout.service.razor.dto.req.Notes;
import io.asktech.payout.service.razor.dto.req.RazorBankAccountRequestDto;
import io.asktech.payout.service.razor.dto.req.Vpa;
import io.asktech.payout.service.razor.models.RazorReqRes;
import io.asktech.payout.service.razor.repo.RazorReqResReposistory;
import io.asktech.payout.utils.Utility;

@Component
public class RazorUtils {

    private static final String NUMBER_ERROR = "Invalid Amount";
    static Logger logger = LoggerFactory.getLogger(RazorUtils.class);

    public RazorBankAccountRequestDto generateUpiTransferRequest(AccountTransferUPIMerReq dto, String merchantid,
            PgDetails pg)
            throws ValidationExceptions, JsonProcessingException {
        RazorBankAccountRequestDto razorRequestDto = new RazorBankAccountRequestDto();
        razorRequestDto.setAccount_number(pg.getPgConfig1());
        String amt = dto.getAmount();
        if (amt.contains(".")) {
            amt = amt.split("\\.")[0];
        }
        int number = 0;
        try {
            number = Integer.parseInt(amt);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new ValidationExceptions(NUMBER_ERROR, FormValidationExceptionEnums.NUMBER_ERROR);
        }
        number = number * 100;
        amt = String.valueOf(number);
        razorRequestDto.setAmount(amt);
        razorRequestDto.setCurrency("INR");
        razorRequestDto.setMode("UPI");
        razorRequestDto.setPurpose("payout");

        Fund_account fund_account = new Fund_account();
        fund_account.setAccount_type("vpa");
        Vpa vpa = new Vpa();
        vpa.setAddress(dto.getBeneficiaryVPA());
        fund_account.setVpa(vpa);

        Contact contact = new Contact();
        contact.setContact(dto.getPhonenumber());
        contact.setEmail("technologysys@gmail.com");
        contact.setName("Amit");
        contact.setType("self");
        contact.setReference_id("CON" + dto.getInternalOrderid());

        Notes notes = new Notes();
        notes.setNotes_key_1(dto.getInternalOrderid());
        notes.setNotes_key_2(dto.getOrderid());

        contact.setNotes(notes);
        fund_account.setContact(contact);
        razorRequestDto.setFund_account(fund_account);

        razorRequestDto.setQueue_if_low_balance(false);
        razorRequestDto.setReference_id(dto.getInternalOrderid());
        razorRequestDto.setNarration("payout");
        razorRequestDto.setNotes(notes);
        logger.info("UPI Generated Request::" + Utility.convertDTO2JsonString(razorRequestDto));
        return razorRequestDto;
    }

    public RazorBankAccountRequestDto generateBankTransferRequest(AccountTransferMerReq dto, String merchantid,
            PgDetails pg) throws ValidationExceptions, JsonProcessingException {
        RazorBankAccountRequestDto razorRequestDto = new RazorBankAccountRequestDto();
        String account_number = pg.getPgConfig1();
        razorRequestDto.setAccount_number(account_number);
        razorRequestDto.setCurrency("INR");
        String amt = dto.getAmount();
        if (amt.contains(".")) {
            amt = amt.split("\\.")[0];
        }
        int number = 0;
        try {
            number = Integer.parseInt(amt);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new ValidationExceptions(NUMBER_ERROR, FormValidationExceptionEnums.NUMBER_ERROR);
        }
        number = number * 100;
        amt = String.valueOf(number);

        razorRequestDto.setAmount(amt);
        razorRequestDto.setCurrency("INR");
        razorRequestDto.setMode(dto.getRequestType());
        razorRequestDto.setPurpose("payout");

        Fund_account fund_account = new Fund_account();
        fund_account.setAccount_type("bank_account");

        Bank_Account bank_Account = new Bank_Account();
        bank_Account.setAccount_number(dto.getBankaccount());
        bank_Account.setIfsc(dto.getIfsc());
        bank_Account.setName(dto.getBeneficiaryName());
        fund_account.setBank_Account(bank_Account);

        // set contact
        Contact contact = new Contact();
        contact.setContact(dto.getPhonenumber());
        contact.setEmail("technologysys@gmail.com");
        contact.setName("Amit");
        contact.setType("self");
        contact.setReference_id("CON" + dto.getInternalOrderid());
        Notes notes = new Notes();
        notes.setNotes_key_1(dto.getInternalOrderid());
        notes.setNotes_key_2(dto.getOrderid());

        contact.setNotes(notes);
        fund_account.setContact(contact);
        razorRequestDto.setFund_account(fund_account);
        razorRequestDto.setQueue_if_low_balance(false);
        razorRequestDto.setReference_id(dto.getInternalOrderid());
        razorRequestDto.setNarration("payout");
        razorRequestDto.setNotes(notes);
        logger.info("ACCOUNT Generated Request::" + Utility.convertDTO2JsonString(razorRequestDto));

        return razorRequestDto;
    }

    @Autowired
    RazorReqResReposistory razorReqResReposistory;

    public String getRazorId(String orderId) {
        RazorReqRes razorReqRes = razorReqResReposistory.findByOrderId(orderId);
        if (razorReqRes != null) {
            return razorReqRes.getRazorId();
        }
        return null;
    }

}
