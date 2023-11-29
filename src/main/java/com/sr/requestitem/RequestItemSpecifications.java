package com.sr.requestitem;

import org.springframework.data.jpa.domain.Specification;

public class RequestItemSpecifications {

	public static Specification<RequestItem> hasId(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}

	public static Specification<RequestItem> hasPriceGreaterThan(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), id);
	}
}
