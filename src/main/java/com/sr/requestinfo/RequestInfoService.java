package com.sr.requestinfo;

import java.util.List;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface RequestInfoService {

	/**
	 * 
	 * @param input
	 * @return
	 */
	DataTablesOutput<RequestInfoDto> findAll(DataTablesInput input);

	/**
	 * 
	 * @param status
	 * @return
	 */
	List<RequestInfo> findByStatusAndL1EscalationMailSentNull(String status);

	/**
	 * 
	 * @param status
	 * @return
	 */
	List<RequestInfo> findByStatusAndL2EscalationMailSentNullAndL1ApprovedDateNotNull(String status);

	/**
	 * 
	 * @param requestInfoDto
	 * @return
	 */
	RequestInfoDto save(RequestInfoDto requestInfoDto);

	/**
	 * 
	 * @param requestInfo
	 * @return
	 */
	RequestInfo save(RequestInfo requestInfo);

	/**
	 * 
	 * @param id
	 * @return
	 */
	RequestInfoDto findOne(Long id);
}
