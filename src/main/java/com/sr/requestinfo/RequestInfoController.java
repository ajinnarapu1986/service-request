package com.sr.requestinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/request-info/")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class RequestInfoController {

	private final RequestInfoService requestInfoService;

	/**
	 * 
	 * @return
	 */
	@GetMapping("/list")
	public String listRequestInfo(Model model) {

		List<String> statusList = Stream.of(RequestStatus.values()).map(Enum::name).collect(Collectors.toList());

		model.addAttribute("statusList", statusList);

		List<String> requestTypeList = new ArrayList<>();
		requestTypeList.add("Laptop");
		requestTypeList.add("Desktop Req");
		requestTypeList.add("ABC Req");

		model.addAttribute("requestTypeList", requestTypeList);

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setStatus(RequestStatus.PENDING_L1.name());
		model.addAttribute("requestInfo", requestInfo);
		return "requestInfo";
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	@GetMapping(value = "/dt")
	@ResponseBody
	public DataTablesOutput<RequestInfoDto> listPOST(@Valid DataTablesInput input) {
		return requestInfoService.findAll(input);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/get")
	@ResponseBody
	public String getRequestInfo(@RequestParam Long id) {
		log.info(":: Entered into getRequestInfo() :: {}", id);
		String json = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.findAndRegisterModules();
			json = mapper.writeValueAsString(requestInfoService.findOne(id));
		} catch (Exception e) {
			log.error(":: Exception occured in getRequestInfo() ::", e);
		}
		return json;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@PostMapping(value = "/approve")
	@PreAuthorize("hasRole('ROLE_L1_APPROVE') or hasRole('ROLE_L2_APPROVE')")
	public String approve(RequestInfoDto requestInfoDto, RedirectAttributes redirectAttrs) {
		Long id = requestInfoDto.getId();
		log.info(":: Entered into approve() :: {}", id);

		String message = "";
		String alertCss = "";
		try {
			log.info(":: requestInfoDto ::" + requestInfoDto);

			RequestInfoDto dbObj = requestInfoService.findOne(id);
			dbObj.setStatus(RequestStatus.PENDING_L2.name());

			requestInfoDto = requestInfoService.save(dbObj);
			message = "Request updated successfully.";
			alertCss = "alert-success";
		} catch (Exception e) {
			log.error(":: Exception occured while saving Record. ::", e);
			message = "Internal Error occured while saving Data.";
			alertCss = "alert-danger";
		} finally {
			log.info(":: requestInfoDto :: {}", requestInfoDto);
			redirectAttrs.addFlashAttribute("message", message);
			redirectAttrs.addFlashAttribute("alertCss", alertCss);
		}
		return "redirect:/request-info/list";
	}

	/**
	 * 
	 * @param RequestType
	 * @return
	 */
	@PostMapping("save")
	//@PreAuthorize("hasRole('ROLE_USER')")
	public String saveRequestInfo(RequestInfoDto requestInfoDto, RedirectAttributes redirectAttrs) {
		String message = "";
		String alertCss = "";
		try {
			log.info(":: requestInfoDto ::" + requestInfoDto);
			requestInfoDto.setStatus(RequestStatus.PENDING_L1.name());
			requestInfoDto = requestInfoService.save(requestInfoDto);
			message = "Request saved successfully.";
			alertCss = "alert-success";
		} catch (Exception e) {
			log.error(":: Exception occured while saving Record. ::", e);
			message = "Internal Error occured while saving Data.";
			alertCss = "alert-danger";
		} finally {
			log.info(":: requestInfoDto :: {}", requestInfoDto);
			redirectAttrs.addFlashAttribute("message", message);
			redirectAttrs.addFlashAttribute("alertCss", alertCss);
		}
		return "redirect:/request-info/list";
	}

}
