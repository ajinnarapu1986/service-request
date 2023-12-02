package com.sr.requestinfo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "request_info")
public class RequestInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "status")
	private String status;

	@Column(name = "description")
	private String description;
	
	@Column(name = "l1_approved_date", insertable = false)
	private LocalDateTime l1ApprovedDate;
	
	@Column(name = "l1_escalation_mail_sent", nullable = false, insertable = false, columnDefinition = "boolean default false")
	private Boolean l1EscalationMailSent;
	
	@Column(name = "l2_approved_date", insertable = false)
	private LocalDateTime l2ApprovedDate;
	
	@Column(name = "l2_escalation_mail_sent", nullable = false, insertable = false, columnDefinition = "boolean default false")
	private Boolean l2EscalationMailSent;

	@Column(name = "created_by", nullable = false, updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@Column(name = "modified_by", insertable = false)
	@LastModifiedBy
	private String modifiedBy;

	@Column(name = "modified_date", insertable = false)
	@LastModifiedDate
	private LocalDateTime modifiedDate;

}
