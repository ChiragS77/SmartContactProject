package com.SmartContactManager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SmartContactManager.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
//	@Query("from Contact as	c where c.user.id =:userId")
//	public List<Contact> findContactByUser(int userId);
	
	
	//current-page
	//contact per page
	@Query("from Contact as	c where c.user.id =:userId")
	public Page<Contact> findContactByUser(int userId, Pageable pageable);

}
