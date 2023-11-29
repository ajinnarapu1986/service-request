package com.sr.requestitem;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RequestItemMapper {

	RequestItemMapper MAPPER = Mappers.getMapper(RequestItemMapper.class);
	
	RequestItemDto toDto(RequestItem requestItem);

	RequestItem toEntity(RequestItemDto requestItemDto);

}
