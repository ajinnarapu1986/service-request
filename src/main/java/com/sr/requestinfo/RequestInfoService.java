package com.sr.requestinfo;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface RequestInfoService {
	
	DataTablesOutput<RequestInfoDto> findAll(DataTablesInput input);
	
	RequestInfoDto save(RequestInfoDto requestInfoDto);

	RequestInfoDto findOne(Long id);
}
