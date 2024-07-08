package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder encoder;
	@GetMapping("/test")
	@ResponseBody
	public String test() {
		User user = new User();
		user.setuName("Aniket Chopade");
		user.setuEmail("aniyket13@gmail.com");
		
		Contact contact = new Contact();
		
		user.getContacts().add(contact);
		userRepo.save(user);
				
		
		return "working";
	}
	
	
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home | SCM");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "about | SCM");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register | SCM");
		model.addAttribute("user", new User());
		return "signup";
	}
		@PostMapping("/do_register")
		public String registerUser(@Valid @ModelAttribute("user") User user,@RequestParam(value="aggr",defaultValue = "false") boolean aggr,Model model, BindingResult result) {
			try {
				if(!aggr) {
					  model.addAttribute("message", new Message("alert-danger", "You have not agreed to terms and conditions"));
		                return "signup";
				}
				if(result.hasErrors()) {
					System.out.println("Error :"+result.toString());
					model.addAttribute("user", user);
					return "signup";
				}
				
				user.setRole("USER");
				user.setEnabled(true);
				user.setImageUrl("default.png");
				user.setUpwd(encoder.encode(user.getUpwd()));
				System.out.println("agrement: "+aggr);
				System.out.println("User: "+user);
				
				 
			    User result1	= this.userRepo.save(user);
			    
				model.addAttribute("user",new User());
				model.addAttribute("message", new Message("alert-success","Successfully registered !!"));	
			
				 return "signup"; 
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("user", user);
				model.addAttribute("message", new Message("alert-danger","Something went wrong !! "+e.getMessage()));
	            return "signup";
			}
			
		 }
		@GetMapping("/signin")
		public String login(Model model) {
			model.addAttribute("title", "Login | SCM");
			return "login";
		}

}
