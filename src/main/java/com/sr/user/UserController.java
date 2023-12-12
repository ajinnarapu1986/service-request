package com.sr.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// Go to Registration Page
	@GetMapping("/register")
	public String register() {
		log.info("register()");
		return "registerUser";
	}

	// Read Form data to save into DB
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, Model model) {
		Integer id = userService.saveUser(user);
		String message = "User '" + id + "' saved successfully !";
		model.addAttribute("msg", message);
		return "registerUser";
	}
}
