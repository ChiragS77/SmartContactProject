package com.SmartContactManager.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.SmartContactManager.entities.User;

public class CustomUserDetail implements UserDetails{
	
	
	private User user;
	
	

	public CustomUserDetail(User user) {
		super();
		this.user = user;
	}

	//here we make the collection of authority that should be provide to  user (role--> user or admin)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		    SimpleGrantedAuthority simpleGrantedAuthority  =  new SimpleGrantedAuthority(user.getRole());
		       System.out.println(List.of(simpleGrantedAuthority));
		
		return Arrays.asList(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
