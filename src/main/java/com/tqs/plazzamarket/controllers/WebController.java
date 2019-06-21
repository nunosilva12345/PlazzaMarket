package com.tqs.plazzamarket.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping(value = "/register")
	public String register(Model model) {
		return "register";
	}
}