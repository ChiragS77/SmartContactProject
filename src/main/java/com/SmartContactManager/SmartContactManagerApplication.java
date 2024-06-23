package com.SmartContactManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.SmartContactManager.config.UserDetailServiceImpl;
import com.SmartContactManager.dao.UserRepository;
import com.SmartContactManager.entities.User;

@SpringBootApplication
public class SmartContactManagerApplication {
	

	public static void main(String[] args) {
		ApplicationContext context =  SpringApplication.run(SmartContactManagerApplication.class, args);
		
		
		
		System.out.println("run successfully...");
		
		
		
		
		
		
	}

}
