package com.sr.requestinfo;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RequestInfoMapper {

	RequestInfoMapper MAPPER = Mappers.getMapper(RequestInfoMapper.class);
	
	RequestInfoDto mapToRequestInfoDto(RequestInfo requestInfo);

	RequestInfo mapToRequestInfo(RequestInfoDto requestInfoDto);

}
