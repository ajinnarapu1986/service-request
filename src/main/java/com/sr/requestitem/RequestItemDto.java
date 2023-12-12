package com.sr.requestitem;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestItemDto {

	private Long id;

	private String item;

	private String description;

	private String createdBy;

	private LocalDateTime createdDate;

	private String modifiedBy;

	private LocalDateTime modifiedDate;

}
