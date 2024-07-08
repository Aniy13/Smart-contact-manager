package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.smart.entity.Contact;
import com.smart.entity.User;
import java.util.List;


public interface ContactRepo extends CrudRepository<Contact, Integer>{
	
	// for pegination we created this , although we can view contact using User
	@Query("from Contact as c where c.user.uId=:userId")
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable );

}
