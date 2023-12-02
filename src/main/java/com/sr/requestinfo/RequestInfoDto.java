package com.sr.requestinfo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfoDto {

	private Long id;

	private String type;

	private String status;

	private String description;
	
	private LocalDateTime l1ApprovedDate;
	
	private LocalDateTime l1EscalationMailSent;	
	
	private LocalDateTime l2ApprovedDate;
	
	private LocalDateTime l2EscalationMailSent;

	private String createdBy;
	
	private LocalDateTime createdDate;

	private String modifiedBy;
	
	private LocalDateTime modifiedDate;
	
}
