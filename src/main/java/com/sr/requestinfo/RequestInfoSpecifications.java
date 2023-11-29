package com.sr.requestinfo;

import org.springframework.data.jpa.domain.Specification;

public class RequestInfoSpecifications {

	public static Specification<RequestInfo> hasId(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}

	public static Specification<RequestInfo> hasPriceGreaterThan(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), id);
	}
}
