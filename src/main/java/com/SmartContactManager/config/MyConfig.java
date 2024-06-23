package com.SmartContactManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig  {

	@Bean
	public UserDetailsService getUserDetailService() {
		
		
		return new UserDetailServiceImpl();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		
		authenticationProvider.setUserDetailsService(this.getUserDetailService());
		authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		
		return authenticationProvider;
	}
	
	
	//configure method
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
	  SecurityFilterChain sfc = http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/user/**").hasRole("USER").anyRequest().permitAll()
		.and().formLogin().loginPage("/signin").loginProcessingUrl("/dologin")
		.defaultSuccessUrl("/user/index")
		.failureUrl("/login-fail")
		.and().csrf().disable().build();
		
		return sfc;
	}
	
	
}
