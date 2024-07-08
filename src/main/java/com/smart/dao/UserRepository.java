 package com.smart.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.smart.entity.User;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User,Integer> {
   Optional<User> findByUName(String uName);
   @Query("SELECT u FROM User u WHERE u.uName = ?1")
   public User getUserByUserName(String userName);
}
