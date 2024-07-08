package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.smart.entity.CustomUserDetailService;



@Configuration
@EnableWebSecurity
public class MyConfig {
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
//   @Bean
//   public UserDetailsService userDetailsService() {
//	   UserDetails normalUser =User.withUsername("user").
//				password(encoder().encode("user")).roles("user").build();
//		
//		UserDetails adminUser = User.withUsername("admin").password(encoder().encode("admin"))
//				.roles("admin").build();
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(normalUser,adminUser);
//		return manager;}
	@Bean
	public UserDetailsService userDetailsService() {
		return customUserDetailService;
	}
   @Bean
   public PasswordEncoder encoder() {
	   return new BCryptPasswordEncoder();
   }
   @Bean
   public AuthenticationProvider authenticationProvider() {
	   DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	   provider.setUserDetailsService(this.customUserDetailService);
	   provider.setPasswordEncoder(encoder());
	   
	   return provider;
   }
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	  return  httpSecurity
			 .csrf(CsrfConfigurer<HttpSecurity>::disable) 
		   .authorizeHttpRequests(registry-> {
		  registry.requestMatchers("/admin").hasRole("ADMIN");
		  registry.requestMatchers("/user/**").hasRole("USER");
		  registry.requestMatchers("/**").permitAll();
		  
	  })       
			  .formLogin(httpsSecurityFormLoginConfigurer-> httpsSecurityFormLoginConfigurer.loginPage("/signin").loginProcessingUrl("/do_login").defaultSuccessUrl("/user/index").permitAll())
			  
			  .build(); 
   }
   
   
}
