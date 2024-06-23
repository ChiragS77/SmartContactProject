package com.SmartContactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.SmartContactManager.dao.UserRepository;
import com.SmartContactManager.entities.User;

public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
	User user = userRepository.findByEmail(email);
	System.out.println(user);
	
	if(user==null) {
		throw new UsernameNotFoundException("could not found user  !!");
	}
		
	
		return new CustomUserDetail(user);
	}

	
	
	
	
	
}
