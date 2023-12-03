package com.sr.requestinfo;

import static com.sr.requestinfo.RequestStatus.DISPATCH;
import static com.sr.requestinfo.RequestStatus.PENDING_L1;
import static com.sr.requestinfo.RequestStatus.PENDING_L2;
import static com.sr.role.RoleEnum.ROLE_DISPATCH;
import static com.sr.role.RoleEnum.ROLE_L1_APPROVE;
import static com.sr.role.RoleEnum.ROLE_L2_APPROVE;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sr.exception.ApplicationException;
import com.sr.utility.MailUtility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestInfoServiceImpl implements RequestInfoService {

	private final RequestInfoRepository requestInfoRepository;
	private final MailUtility mailUtility;
	private final RequestInfoMapper mapper;

	/**
	 * 
	 */
	@Override
	public DataTablesOutput<RequestInfoDto> findAll(DataTablesInput input) throws ApplicationException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		DataTablesOutput<RequestInfo> response = null;
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ROLE_L1_APPROVE.name()))) {
			response = requestInfoRepository.findAll(input, RequestInfoSpecifications.withStatus(PENDING_L1.name()));
		} else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ROLE_L2_APPROVE.name()))) {
			response = requestInfoRepository.findAll(input, RequestInfoSpecifications.withStatus(PENDING_L2.name()));
		} else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ROLE_DISPATCH.name()))) {
			response = requestInfoRepository.findAll(input, RequestInfoSpecifications.withStatus(DISPATCH.name()));
		} else {
			response = requestInfoRepository.findAll(input);
		}

		DataTablesOutput<RequestInfoDto> responseDto = new DataTablesOutput<>();

		responseDto.setDraw(response.getDraw());
		responseDto.setError(response.getError());
		responseDto.setRecordsFiltered(response.getRecordsFiltered());
		responseDto.setRecordsTotal(response.getRecordsTotal());
		responseDto.setData(response.getData().stream().map(row -> mapper.toDto(row)).collect(Collectors.toList()));
		return responseDto;
	}

	/**
	 * @throws ApplicationException
	 * 
	 */
	@Override
	public RequestInfoDto save(RequestInfoDto requestInfoDto) throws ApplicationException {
		RequestInfo requestInfo = null;
		try {
			requestInfo = mapper.toEntity(requestInfoDto);
			requestInfo = requestInfoRepository.save(requestInfo);

			log.info(":: requestInfo = {} ::", requestInfo);
			if (null != requestInfo.getId()) {
				mailUtility.sendApprovalMail(requestInfo);
			}
		} catch (ApplicationException appException) {
			log.error(":: Exception while saving Request Info = {} ::", appException);
			throw new ApplicationException(appException);
		} catch (Exception e) {
			log.error(":: Exception while saving Request Info = {} ::", e);
			throw new ApplicationException("Exception while saving Request Info", e);
		}
		return mapper.toDto(requestInfo);
	}

	/**
	 * 
	 */
	@Override
	public RequestInfoDto findOne(Long id) throws ApplicationException {

		Optional<RequestInfo> optional = Optional.empty();
		try {
			optional = requestInfoRepository.findById(id);
		} catch (Exception e) {
			log.error(":: RequestInfo not found with id = {} ::", id);
			throw new ApplicationException("RequestInfo not found with id.", e);
		}

		return optional.isPresent() ? mapper.toDto(optional.get()) : null;
	}

	/**
	 * 
	 */
	@Override
	public List<RequestInfo> findByStatusAndL1EscalationMailSentNull(String status) throws ApplicationException {

		return requestInfoRepository.findByStatusAndL1EscalationMailSentNull(status);
	}

	/**
	 * 
	 */
	@Override
	public List<RequestInfo> findByStatusAndL2EscalationMailSentNullAndL1ApprovedDateNotNull(String status)
			throws ApplicationException {

		return requestInfoRepository.findByStatusAndL2EscalationMailSentNullAndL1ApprovedDateNotNull(status);
	}

	@Override
	public RequestInfo save(RequestInfo requestInfo) throws ApplicationException {
		RequestInfo requestInfo1 = null;
		try {
			requestInfo1 = requestInfoRepository.save(requestInfo);
		} catch (Exception e) {
			log.error(":: Error while saving the Request Info = {} ::", requestInfo);
			throw new ApplicationException("Error while saving the Request Info.", e);
		}
		return requestInfo1;
	}

}
