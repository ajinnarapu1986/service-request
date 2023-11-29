package com.sr.requestitem;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface RequestItemService {
	
	DataTablesOutput<RequestItemDto> findAll(DataTablesInput input);
	
	RequestItemDto save(RequestItemDto requestItemDto);

	RequestItemDto findOne(Long id);
}
