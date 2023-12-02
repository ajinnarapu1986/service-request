package com.sr.requestinfo;

import org.springframework.data.jpa.domain.Specification;

public class RequestInfoSpecifications {

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Specification<RequestInfo> hasPriceGreaterThan(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), id);
	}

	/**
	 * 
	 * @param status
	 * @return
	 */
	public static Specification<RequestInfo> withStatus(String status) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
	}
}
