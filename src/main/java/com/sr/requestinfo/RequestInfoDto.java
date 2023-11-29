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

	private String createdBy;
	
	private LocalDateTime createdDate;

	private String modifiedBy;
	
	private LocalDateTime modifiedDate;
	
}
