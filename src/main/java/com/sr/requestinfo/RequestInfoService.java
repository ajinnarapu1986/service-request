package com.sr.requestinfo;

import java.util.List;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.sr.exception.ApplicationException;

public interface RequestInfoService {

	/**
	 * 
	 * @param input
	 * @return
	 */
	DataTablesOutput<RequestInfoDto> findAll(DataTablesInput input) throws ApplicationException;

	/**
	 * 
	 * @param status
	 * @return
	 */
	List<RequestInfo> findByStatusAndL1EscalationMailSentNull(String status) throws ApplicationException;

	/**
	 * 
	 * @param status
	 * @return
	 */
	List<RequestInfo> findByStatusAndL2EscalationMailSentNullAndL1ApprovedDateNotNull(String status) throws ApplicationException;

	/**
	 * 
	 * @param requestInfoDto
	 * @return
	 */
	RequestInfoDto save(RequestInfoDto requestInfoDto) throws ApplicationException ;

	/**
	 * 
	 * @param requestInfo
	 * @return
	 */
	RequestInfo save(RequestInfo requestInfo) throws ApplicationException;

	/**
	 * 
	 * @param id
	 * @return
	 */
	RequestInfoDto findOne(Long id) throws ApplicationException;
}
