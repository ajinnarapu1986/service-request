package com.sr.requestinfo;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sr.role.RoleEnum;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestInfoServiceImpl implements RequestInfoService {

	private final RequestInfoRepository requestInfoRepository;

	private final JavaMailSender mailSender;

	private final RequestInfoMapper mapper;

	@Value("${spring.mail.username}")
	private String fromAddress;

	private static final boolean html = true;

	@Override
	public DataTablesOutput<RequestInfoDto> findAll(DataTablesInput input) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		DataTablesOutput<RequestInfo> response = null;
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(RoleEnum.ROLE_L1_APPROVE.name()))) {
			Specification<RequestInfo> authoritySpec = RequestInfoSpecifications
					.withStatus(RequestStatus.PENDING_L1.name());
			response = requestInfoRepository.findAll(input, authoritySpec);
		} else if (auth.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(RoleEnum.ROLE_L2_APPROVE.name()))) {
			Specification<RequestInfo> authoritySpec = RequestInfoSpecifications
					.withStatus(RequestStatus.PENDING_L2.name());
			response = requestInfoRepository.findAll(input, authoritySpec);
		} else if (auth.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(RoleEnum.ROLE_DISPATCH.name()))) {
			Specification<RequestInfo> authoritySpec = RequestInfoSpecifications
					.withStatus(RequestStatus.DISPATCH.name());
			response = requestInfoRepository.findAll(input, authoritySpec);
		} else {
			requestInfoRepository.findAll(input);
		}

		DataTablesOutput<RequestInfoDto> responseDto = new DataTablesOutput<>();

		responseDto.setDraw(response.getDraw());
		responseDto.setError(response.getError());
		responseDto.setRecordsFiltered(response.getRecordsFiltered());
		responseDto.setRecordsTotal(response.getRecordsTotal());
		responseDto.setData(response.getData().stream().map(row -> mapper.toDto(row)).collect(Collectors.toList()));
		return responseDto;
	}

	@Override
	public RequestInfoDto save(RequestInfoDto requestInfoDto) {

		RequestInfo requestInfo = mapper.toEntity(requestInfoDto);
		requestInfo = requestInfoRepository.save(requestInfo);

		log.info(":: requestInfo = {} ::", requestInfo);
		if ( null != requestInfo.getId() ) {
			this.sendHTMLMail(requestInfo.getId());
		}

		return mapper.toDto(requestInfo);
	}

	@SuppressWarnings("unused")
	private void sendHTMLMail(Long requestInfoId) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setSubject("Approval Request : Request Info Id - " + requestInfoId);
			helper.setFrom(fromAddress);
			helper.setTo(fromAddress);

			helper.setText("<b>Hey guys</b>,<br><i>Welcome to my new home</i>", html);

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RequestInfoDto findOne(Long id) {

		Optional<RequestInfo> optional = requestInfoRepository.findOne(RequestInfoSpecifications.hasId(id));

		return optional.isPresent() ? mapper.toDto(optional.get()) : null;
	}

}
