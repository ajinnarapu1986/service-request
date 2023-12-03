package com.sr.requestitem;

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
@RequestMapping("/request-item/")
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RequestItemController {

	private final RequestItemService requestItemService;

	/**
	 * 
	 * @return
	 */
	@GetMapping("/list")
	public String listRequestInfo(Model model) {

		RequestItemDto requestItemDto = new RequestItemDto();
		model.addAttribute("requestItem", requestItemDto);
		return "requestItem";
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	@GetMapping(value = "/dt")
	@ResponseBody
	public DataTablesOutput<RequestItemDto> listPOST(@Valid DataTablesInput input) {
		return requestItemService.findAll(input);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/get")
	@ResponseBody
	public String getRequestItem(@RequestParam Long id) {
		log.info(":: Entered into getRequestItem() :: {}", id);
		String json = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.findAndRegisterModules();
			json = mapper.writeValueAsString(requestItemService.findOne(id));
		} catch (Exception e) {
			log.error(":: Exception occured in getRequestItem() ::", e);
		}
		return json;
	}	

	/**
	 * 
	 * @param RequestType
	 * @return
	 */
	@PostMapping("save")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String saveRequestInfo(RequestItemDto requestItemDto, RedirectAttributes redirectAttrs) {
		String message = "";
		String alertCss = "";
		try {
			log.info(":: requestItemDto ::" + requestItemDto);
			
			requestItemDto = requestItemService.save(requestItemDto);
			message = "Request saved successfully.";
			alertCss = "alert-success";
		} catch (Exception e) {
			log.error(":: Exception occured while saving Record. ::", e);
			message = "Internal Error occured while saving Data.";
			alertCss = "alert-danger";
		} finally {
			log.info(":: requestItemDto :: {}", requestItemDto);
			redirectAttrs.addFlashAttribute("message", message);
			redirectAttrs.addFlashAttribute("alertCss", alertCss);
		}
		return "redirect:/request-item/list";
	}

}
