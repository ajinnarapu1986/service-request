package com.sr.requestinfo;

import java.time.LocalDateTime;
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
import com.sr.exception.ApplicationException;
import com.sr.requestitem.RequestItemDto;
import com.sr.requestitem.RequestItemService;

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
	private final RequestItemService requestItemService;

	/**
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String listRequestInfo(Model model) {
		List<String> statusList = new ArrayList<>();
		List<String> itemList = new ArrayList<>();
		try {
			statusList = Stream.of(RequestStatus.values()).map(Enum::name).collect(Collectors.toList());

			DataTablesInput input = new DataTablesInput();
			DataTablesOutput<RequestItemDto> data = requestItemService.findAll(input);
			itemList = data.getData().stream().map(RequestItemDto::getItem).collect(Collectors.toList());

			RequestInfo requestInfo = new RequestInfo();
			requestInfo.setStatus(RequestStatus.PENDING_L1.name());

			model.addAttribute("statusList", statusList);
			model.addAttribute("requestTypeList", itemList);
			model.addAttribute("requestInfo", requestInfo);
		} catch (Exception e) {
			log.error(":: Exception occured in listRequestInfo() {}::", e);
		} finally {
			log.info(":: Exited from listRequestInfo ::");
		}
		return "requestInfo";
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws ApplicationException 
	 */
	@GetMapping(value = "/dt")
	@ResponseBody
	public DataTablesOutput<RequestInfoDto> listPOST(@Valid DataTablesInput input) throws ApplicationException {
		return requestInfoService.findAll(input);
	}

	/**
	 * 
	 * @param id
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
	 * @param requestInfoDto
	 * @param redirectAttrs
	 * @return
	 */
	@PostMapping(value = "/approve")
	@PreAuthorize("hasRole('ROLE_L1_APPROVE') or hasRole('ROLE_L2_APPROVE')")
	public String approve(RequestInfoDto requestInfoDto, RedirectAttributes redirectAttrs) {
		log.info(":: Entered into approve() :: requestInfoDto = {}", requestInfoDto);

		String message = "";
		String alertCss = "";
		try {
			RequestInfoDto dbObj = requestInfoService.findOne(requestInfoDto.getId());
			if (dbObj.getStatus().equalsIgnoreCase(RequestStatus.PENDING_L1.name())) {
				dbObj.setL1ApprovedDate(LocalDateTime.now());
				dbObj.setStatus(RequestStatus.PENDING_L2.name());
			} else if (dbObj.getStatus().equalsIgnoreCase(RequestStatus.PENDING_L2.name())) {
				dbObj.setL2ApprovedDate(LocalDateTime.now());
				dbObj.setStatus(RequestStatus.DISPATCH.name());
			}

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
	 * @param requestInfoDto
	 * @param redirectAttrs
	 * @return
	 */
	@PostMapping("save")
	// @PreAuthorize("hasRole('ROLE_USER')")
	public String saveRequestInfo(RequestInfoDto requestInfoDto, RedirectAttributes redirectAttrs) {
		String message = "";
		String alertCss = "";
		try {
			log.info(":: requestInfoDto ::" + requestInfoDto);
			requestInfoDto.setStatus(RequestStatus.PENDING_L1.name());

			// Saving to the Database
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
