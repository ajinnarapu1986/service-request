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
	List<RequestInfo> findByStatusAndL1EscalationMailSent(String status, Boolean l1MailSent);

	/**
	 * 
	 * @param status
	 * @return
	 */
	List<RequestInfo> findByStatusAndL2EscalationMailSentAndL1ApprovedDateNotNull(String status, Boolean l2MailSent);

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
