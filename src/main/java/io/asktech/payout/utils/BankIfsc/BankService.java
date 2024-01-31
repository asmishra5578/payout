package io.asktech.payout.utils.BankIfsc;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.utils.Utility;

@Component
public class BankService {
    @Autowired
    BankRepo bankRepo;
    static Logger logger = LoggerFactory.getLogger(BankService.class);

    public List<BankModel> getBankDetails(String ifsc) {
        logger.info("getBankDetails::" + ifsc);
        return bankRepo.findByIfsc(ifsc);
    }

    public BankModel setBankDetails(BankDto bankDto) throws JsonProcessingException {
        logger.info("setBankDetails::" + Utility.convertDTO2JsonString(bankDto));
        ModelMapper modelMapper = new ModelMapper();
        BankModel bankModel = modelMapper.map(bankDto, BankModel.class);
        return bankRepo.save(bankModel);
    }

    @Autowired
    BankIfscRepo bankIfscRepo;

    public String getBankNameByIFSCInitial(String ifsc) {
        String ifs = ifsc.substring(0, 4);
        List<BankIfsc> bankIfs = bankIfscRepo.findByIfsc(ifs);

        if (bankIfs.size() > 0) {
            logger.info("BANK NAME::"+bankIfs.get(0).getBankName());
            return bankIfs.get(0).getBankName();
        }
        logger.info("NO BANK::"+"KOTAK");
        return "KOTAK";
    }

    public String getBankName(String ifsc){
        List<BankModel> bnk =  getBankDetails(ifsc);
        System.out.println(bnk);
        if(bnk.size() > 0){
            return bnk.get(0).getBankName();
        }
        String b = getBankNameByIFSCInitial(ifsc);
        if(b != null){
            return b;
        }
        return null;
    }
}
