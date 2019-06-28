package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.*;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.repositories.ReceiptRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import com.tqs.plazzamarket.utils.BaseUser;
import com.tqs.plazzamarket.utils.Cart;
import com.tqs.plazzamarket.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebController {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProducerRepository producerRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private ReceiptRepository receiptRepository;

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
			Map<Integer, Double[]> items = ((Cart) httpSession.getAttribute("cart")).getItems();
			model.addAttribute("products", productRepository.findAll());
			model.addAttribute("items", items.entrySet().stream().collect(Collectors.toMap(entry -> productRepository.getOne(entry.getKey()), entry -> entry.getValue())));
			model.addAttribute("totalCart", ((Cart) httpSession.getAttribute("cart")).getTotal());
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

	@GetMapping(value = "/pendingreservations")
	public String pendingReservations(Model model, HttpSession session) {
		BaseUser user = (BaseUser) session.getAttribute("user");
		List<Sale> sales = new ArrayList<>();
		for(Sale sale : saleRepository.findAll()) {
			if(sale.getProduct().getProducer().getUsername().equals(user.getUsername()) && sale.getStatus()== Status.PROCESSING) {
				sales.add(sale);
			}
		}
		model.addAttribute("sales", sales);
		return "listPendingReservations";
	}

	@GetMapping(value = "/historyshopping")
	public String historyShopping(Model model, HttpSession session) {
		Consumer user = (Consumer) session.getAttribute("user");
		List<Receipt> receipts = new ArrayList<>();
		for(Receipt receipt : receiptRepository.findAll()) {
			if(receipt.getConsumer().getUsername().equals(user.getUsername()))
				receipts.add(receipt);
		}
		model.addAttribute("receipts", receipts);
		return "historyShopping";
	}

	@GetMapping(value = "/historysales")
	public String historySales(Model model, HttpSession session) {
		Producer user = (Producer) session.getAttribute("user");
		List<Receipt> receipts = new ArrayList<>();
		for(Receipt receipt : receiptRepository.findAll()) {
			if(receipt.getProducer().getUsername().equals(user.getUsername()))
				receipts.add(receipt);
		}
		model.addAttribute("receipts", receipts);
		return "historySales";
	}

}