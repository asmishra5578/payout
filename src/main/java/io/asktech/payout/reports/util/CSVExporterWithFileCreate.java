package io.asktech.payout.reports.util;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVWriter;

import io.asktech.payout.reports.dto.ReportData;

@Service
public class CSVExporterWithFileCreate  {
    
	static Logger logger = LoggerFactory.getLogger(CSVExporterWithFileCreate.class);
	@Transactional
	@Async
    public void exportCSVFiles(List<ReportData> datas , String fileName , String reportHeader, String reportFolderPath) 
    {
    	
    	try {
    		logger.info("Inside CSV wirter ...");
    			logger.info("Path :: "+reportFolderPath+fileName);
		        CSVWriter writer = new CSVWriter(new FileWriter(reportFolderPath+fileName));
		        List<String[]> list = new ArrayList<>();
		        
		        String[] headers = reportHeader.split("\\^");
				logger.info("headers :: "+headers[0],headers[1],headers[2],headers[3],headers[4],headers[5],headers[6],headers[7],headers[8],headers[9],headers[10],headers[11],headers[12]);
		        list.add(headers);
		
		        for (ReportData reportData : datas) {
					
					String[] arrOfStr = reportData.getDatas().split("\\^");
					logger.info("arrOfStr :: "+arrOfStr[0],arrOfStr[1],arrOfStr[2],arrOfStr[3],arrOfStr[4],arrOfStr[5],arrOfStr[6],arrOfStr[7],arrOfStr[8],arrOfStr[9],arrOfStr[10],arrOfStr[11],arrOfStr[12]);
					list.add(arrOfStr);
					//writer.writeAll(list);
		        }
		        writer.writeAll(list);
				writer.close();
		        logger.info("End CSV wirter ...");
				
			}catch(Exception e) {
				logger.error("Exception in CSV fie generated");
			}
        
    }
}
