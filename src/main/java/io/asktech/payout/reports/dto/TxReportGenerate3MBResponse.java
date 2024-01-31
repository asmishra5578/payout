package io.asktech.payout.reports.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TxReportGenerate3MBResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6106553116664321260L;
	private String reportLocation;
	private String fileName;
	private String fileType;
	private int status;
	private String message;

}
