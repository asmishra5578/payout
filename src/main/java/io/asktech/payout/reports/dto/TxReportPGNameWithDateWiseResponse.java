package io.asktech.payout.reports.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TxReportPGNameWithDateWiseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 898852269808827623L;
	private String reportLocation;
	private String fileName;
	private String fileType;
	private int status;
	private String message;
	private String pgName;
	private String fromDate;
	private String upToDate;
}
