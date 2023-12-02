package com.sr.requestinfo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface RequestInfoRepository extends DataTablesRepository<RequestInfo, Long> {	
	
	Optional<RequestInfo> findById(Long id);
	
	List<RequestInfo> findByStatusAndL1EscalationMailSent(String status, Boolean l1EscalationMailSent);
	
	List<RequestInfo> findByStatusAndL2EscalationMailSentAndL1ApprovedDateNotNull(String status, Boolean l2EscalationMailSent);
}
