package com.sr.requestinfo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface RequestInfoRepository extends DataTablesRepository<RequestInfo, Long> {	
	
	Optional<RequestInfo> findById(Long id);
	
	List<RequestInfo> findByStatusAndL1EscalationMailSentNull(String status);
	
	List<RequestInfo> findByStatusAndL2EscalationMailSentNullAndL1ApprovedDateNotNull(String status);
}
