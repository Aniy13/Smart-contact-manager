package com.smart.entity;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smart.dao.UserRepository;
@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<com.smart.entity.User>	user =  userRepo.findByUName(username);
	
	if (user.isPresent()) {
		var userObj = user.get();
		return User.withUsername(userObj.getuName()).
				password(userObj.getUpwd())
				.roles(getRoles(userObj)).build();
	}else {
		throw new UsernameNotFoundException(username);
	}
	}

	private String[] getRoles(com.smart.entity.User user) {
		if(user.getRole()==null) {
			return new String[]{"USER"};
		}
		return user.getRole().split(",");
	}

}
