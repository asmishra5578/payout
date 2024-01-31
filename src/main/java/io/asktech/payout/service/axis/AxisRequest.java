package io.asktech.payout.service.axis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.axis.dto.BeneficiaryResponseDto;
import io.asktech.payout.service.axis.dto.BenificaryDto;
import io.asktech.payout.service.axis.dto.FundtransferDto;
import io.asktech.payout.service.axis.dto.StatusRequestDto;
import io.asktech.payout.service.axis.dto.StatusResponseDto;
// import io.asktech.payout.service.axis.dto.TransferPaymentResponse;
import io.asktech.payout.service.axis.dto.TransferPaymentResponseMain;
// import io.asktech.payout.service.payg.dto.FundTransferDto;
// import io.asktech.payout.utils.ApiReqResLogger;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;




@Component
public class AxisRequest {
  
    @Value("${axisCredentials.beneUrl}")
    String beneUrl;
    @Value("${axisCredentials.fundUrl}")
    String fundUrl;
    @Value("${axisCredentials.statusUrl}")
    String statusUrl;
    // PROVIDE EXCEUTABLE PERMISSION TO KEY FOLDER ADN
    @Value("${axisCredentials.p12Path}")
    String p12Path;

    static Logger logger = LoggerFactory.getLogger(AxisRequest.class);
    // BeneficiaryResponseDto
    public  BeneficiaryResponseDto addBeneficiaryRequest(BenificaryDto beneficaryRequest, PgDetails pg ) throws JsonProcessingException{
        Unirest.config().clientCertificateStore(p12Path, "");
        logger.info("Beneficiary request is called now : "+pg.getPgId() + "\n" + pg.getPgConfigSecret() );
        
        
        HttpResponse<kong.unirest.JsonNode> response = Unirest.post(beneUrl)
        .header("X-IBM-Client-Id", pg.getPgConfigKey())
                .header("X-IBM-Client-Secret", pg.getPgConfigSecret())
                // .header("X-IBM-Client-Id", "a6793d69-6e75-458b-9c00-c00febd8b437")
                // .header("X-IBM-Client-Secret", "tE3kK3kO0rX8jL4sH7mP3eW7aT7tP3qV7fB2uJ0vW4eB6rB7pC")

                .header("Content-Type", "application/json")
                .body(Utility.convertDTO2JsonString(beneficaryRequest)).asJson();


                String data = response.getBody().toString();
                logger.info("Beneficiary response "+ data );
        
                ObjectMapper objectMapper = new ObjectMapper();
                BeneficiaryResponseDto beneficiaryResponseDto = objectMapper.readValue(data,
                BeneficiaryResponseDto.class);
                logger.info(Utility.convertDTO2JsonString(beneficiaryResponseDto));
        
                return beneficiaryResponseDto;

    }

    public TransferPaymentResponseMain fundTransferRequest(FundtransferDto fundTransfer, PgDetails pg) throws JsonProcessingException{
        Unirest.config().clientCertificateStore(p12Path, "");
        HttpResponse<kong.unirest.JsonNode> response = Unirest.post(fundUrl)
                // .header("X-IBM-Client-Id", "a6793d69-6e75-458b-9c00-c00febd8b437")
                // .header("X-IBM-Client-Secret", "tE3kK3kO0rX8jL4sH7mP3eW7aT7tP3qV7fB2uJ0vW4eB6rB7pC")
                .header("X-IBM-Client-Id", pg.getPgConfigKey())
                .header("X-IBM-Client-Secret", pg.getPgConfigSecret())
                .header("Content-Type", "application/json")
                .body(Utility.convertDTO2JsonString(fundTransfer)).asJson();

                String data = response.getBody().toString();
                logger.info("Beneficiary response "+ data );
        
                ObjectMapper objectMapper = new ObjectMapper();
                TransferPaymentResponseMain beneficiaryResponseDto = objectMapper.readValue(data,
                TransferPaymentResponseMain.class);
                logger.info(Utility.convertDTO2JsonString(beneficiaryResponseDto));
                // return response.getBody();
                    return beneficiaryResponseDto;
    }


    public StatusResponseDto getStatusRequest(StatusRequestDto request, PgDetails pg) throws JsonProcessingException{

        logger.info("Satus get the Request : " + Utility.convertDTO2JsonString(request));

        Unirest.config().clientCertificateStore(p12Path, "");
        HttpResponse<kong.unirest.JsonNode> response = Unirest.post(statusUrl)
                // .header("X-IBM-Client-Id", "a6793d69-6e75-458b-9c00-c00febd8b437")
                // .header("X-IBM-Client-Secret", "tE3kK3kO0rX8jL4sH7mP3eW7aT7tP3qV7fB2uJ0vW4eB6rB7pC")
                .header("X-IBM-Client-Id", pg.getPgConfigKey())
                .header("X-IBM-Client-Secret", pg.getPgConfigSecret())
                .header("Content-Type", "application/json")
                .body(Utility.convertDTO2JsonString(request)).asJson();

                String data = response.getBody().toString();
                logger.info("Status Response : "+ data );
        
                ObjectMapper objectMapper = new ObjectMapper();
                StatusResponseDto beneficiaryResponseDto = objectMapper.readValue(data,
                StatusResponseDto.class);
                logger.info(Utility.convertDTO2JsonString(beneficiaryResponseDto));
                // return response.getBody();
                    return beneficiaryResponseDto;
    }

}
