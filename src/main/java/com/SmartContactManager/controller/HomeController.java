package com.SmartContactManager.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.SmartContactManager.dao.UserRepository;
import com.SmartContactManager.entities.User;
import com.SmartContactManager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
    PasswordEncoder encoder;	
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {	
		model.addAttribute("title", "Home -  Smart Contact Manager");	
		return "home";
	}
	
	
	@RequestMapping("/about")
	public String about(Model model) {	
		model.addAttribute("title", "About -  Smart Contact Manager");	
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {	
		model.addAttribute("title", "Register -  Smart Contact Manager");	
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(value="/do_register" , method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user")User user,BindingResult result1, @RequestParam(value = "agreement" , defaultValue = "false")boolean agreement  
			,  Model model, HttpSession session ) {
		
		try {
			
			
			if(!agreement) {
				throw new Exception("you have not agreed the terms and conditions ");
				
			}
			
			if(result1.hasErrors()) {
				System.out.println("ERROR..."+result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
					
			
			user.setRole("ROLE_USER"); //change
			user.setEnable(true);
			user.setImageUrl("default.jpg");
			
            encoder.encode(user.getPassword())	;		
			
			User result = userRepository.save(user);

			
			model.addAttribute("user" ,new User());
			session.setAttribute("message", new Message(" Successfully register !!", "alert-success"));
			
			System.out.println(agreement);
			System.out.println("User: "+user);
			
			return "signup";
			
			
		} catch (Exception e) {
			
			model.addAttribute("user" ,user);
			session.setAttribute("message", new Message("Something went wrong !!", "alert-danger"));
			e.printStackTrace();
			return "signup";

		}
		
	}

	
	//login user
	@RequestMapping("/signin")
	public String loginPage(Model model, Principal principle) {
		
		model.addAttribute("title","Login form");
		
		
		
		return "normal/login";
		
	}
	
}
