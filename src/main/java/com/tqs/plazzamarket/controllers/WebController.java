package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.utils.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class WebController {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProducerRepository producerRepository;

	@GetMapping(value = "/")
	public String login(Model model) {
		return "login";
	}

	@GetMapping(value = "/register")
	public String register(Model model) {
		return "register";
	}

	@GetMapping(value = "/createproduct")
	public String createProduct(Model model) {
		return "createproduct";
	}

	@GetMapping(value = "/listproduct")
	public String listProduct(Model model, HttpSession httpSession) {
		BaseUser user = (BaseUser) httpSession.getAttribute("user");
		model.addAttribute("user", user);
		if (user.getClass()== Consumer.class) {
			model.addAttribute("products", productRepository.findAll());
			return "listproduct";
		} else {
			model.addAttribute("products", producerRepository.getOne(user.getUsername()).getProducts());
			return "listProducerProducts";
		}
	}

	@GetMapping(value = "/home")
	public String homepage(Model model) {
		model.addAttribute("produtos", productRepository.findAll());
    	return "listproducts";
	}
}