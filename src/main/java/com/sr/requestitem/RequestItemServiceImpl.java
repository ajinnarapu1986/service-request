package com.sr.requestitem;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestItemServiceImpl implements RequestItemService {

	private final RequestItemRepository requestItemRepository;

	private final RequestItemMapper mapper;

	@Override
	public DataTablesOutput<RequestItemDto> findAll(DataTablesInput input) {

		DataTablesOutput<RequestItem> output = requestItemRepository.findAll(input);

		DataTablesOutput<RequestItemDto> dto = new DataTablesOutput<>();

		dto.setDraw(output.getDraw());
		dto.setError(output.getError());
		dto.setRecordsFiltered(output.getRecordsFiltered());
		dto.setRecordsTotal(output.getRecordsTotal());
		dto.setData(output.getData().stream().map(row -> mapper.toDto(row)).collect(Collectors.toList()));
		return dto;
	}

	@Override
	public RequestItemDto save(RequestItemDto requestItemDto) {
		log.info(":: requestInfo = {} ::", requestItemDto);
		RequestItem requestItem = mapper.toEntity(requestItemDto);
		requestItem = requestItemRepository.save(requestItem);
		return mapper.toDto(requestItem);
	}

	@Override
	public RequestItemDto findOne(Long id) {

		Optional<RequestItem> optional = requestItemRepository.findOne(RequestItemSpecifications.hasId(id));

		return optional.isPresent() ? mapper.toDto(optional.get()) : null;
	}

}
