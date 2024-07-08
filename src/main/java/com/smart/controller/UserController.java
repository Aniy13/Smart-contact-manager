package com.smart.controller;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.ContactRepo;
import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	 private UserRepository userRepo;
	@Autowired
	private ContactRepo contactRepo;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	// Handle for adding common data to respomse
	   @ModelAttribute
	   public void addCommonData(Model model,Principal principal) {
		   String name = principal.getName();
			  System.out.println(name);
//			  Get user email from nAME
			  
			  User user=userRepo.getUserByUserName(name);
			  model.addAttribute("user", user);
			  System.out.println(user);
	    	  
	   }
	 //Dashboard Home
	  @RequestMapping("/index")
      public String dashboard(Model model,Principal principal){
		  model.addAttribute("title", "Dashboard | SCM");
    	  return "normal/user_dashboard";
      }
	  // open add contact handler
	  @GetMapping("/add-contact")
	  public String addContact(Model model) {
		  model.addAttribute("title", "Add Contact | SCM");
		  model.addAttribute("contact", new Contact());
		  return "normal/add_contact_form";
		  
	  }
	  
	  //handler for procssing contact form
	  @PostMapping("/process-contact")
	  public String processContact(@ModelAttribute @Valid Contact contact,BindingResult bindingResult, @RequestParam("profileImg") MultipartFile file ,Principal principal, Model model) {
		  
		  if (bindingResult.hasErrors()) {
		        // Handle server-side validation errors
			  StringBuilder errorMsg = new StringBuilder("Error in filling form : ");
		        if (bindingResult.hasFieldErrors("cName")) {
		            errorMsg.append("Name error: ").append(bindingResult.getFieldError("cName").getDefaultMessage()).append(". ");
		        }
		        if (bindingResult.hasFieldErrors("cEmail")) {
		            errorMsg.append("Email error: ").append(bindingResult.getFieldError("cEmail").getDefaultMessage()).append(". ");
		        }
		        model.addAttribute("message", new Message("alert-danger", errorMsg.toString()));
		        return "normal/add_contact_form";
		  }
		  try {
		   String name = principal.getName();
		   User user = this.userRepo.getUserByUserName(name);
		   
		   contact.setUser(user);
		   user.getContacts().add(contact);
		   
		   
		   
		   
//		    uploadibg file
		   if(file.isEmpty()) {
			   //message
			   contact.setImageUrl("contacts_icon.svg.png");
			   model.addAttribute("message", new Message("alert-success","Successfully added contact!!"));	
		   }else {
			   // 
			   contact.setImageUrl(file.getOriginalFilename());
			   File saveFile = new ClassPathResource("/static/img").getFile();
			   Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			   Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			   System.out.println("Image Uploaded");
			   model.addAttribute("message", new Message("alert-success","Successfully added contact!!"));	
		   }
		   
		   
		   
		   this.userRepo.save(user);
			System.out.println("Data :"+contact);
			System.out.println("Successfully added contact to database");
			
		  }
		  catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", new Message("alert-danger","Something went wrong !! "+e.getMessage()));
		}
		  return "normal/add_contact_form";
		  
	  }
	  
	  
	  // pagination
	  //per page 5 contacts
	  //current page number 0
	  @RequestMapping("/show-contact/{page}")
      public String showContacts(@PathVariable("page") Integer page, Model model,Principal principal){
		  model.addAttribute("title", "Contacts | SCM");
		  
		  	String name = principal.getName();
		  	User user = this.userRepo.getUserByUserName(name);
		  	Pageable pageable = PageRequest.of(page, 3);
		  	Page<Contact> contactsByUser = this.contactRepo.findContactsByUser(user.getuId(),pageable);
//		  List<Contact> contacts = user.getContacts();
		  	model.addAttribute("contacts", contactsByUser);
		  	model.addAttribute("currentPage",page);
		  	model.addAttribute("totalPages",contactsByUser.getTotalPages());
		  
		  //we need to add contact repo
		  
		  
    	  return "normal/show_contacts";
      }
	  
	  
	  //show particular contacts
	  @RequestMapping("/show-contact/contact/{cId}")
	  public String showContact(@PathVariable("cId") Integer cId, Model model,Principal principal) {
		  System.out.println(cId);
		  model.addAttribute("title", "Contacts | SCM");
		  
		  Optional<Contact> conOpt = contactRepo.findById(cId);
		  Contact contact = conOpt.get();
		  
		  String name = principal.getName();
		  User user = this.userRepo.getUserByUserName(name);
		  
		  if(user.getuId()==contact.getUser().getuId()) {
			  model.addAttribute("contact", contact);
		  }
		  
		  
		  return "normal/contact_details";
	  }
	  
	  @RequestMapping("/delete/{cId}")
	  public String delete(@PathVariable("cId") Integer cId, Model model, Principal principal, RedirectAttributes redirectAttributes) {
	      System.out.println("Delete method called for contact ID: " + cId);
	      Optional<Contact> contactOpt = contactRepo.findById(cId);
	      
	      if (contactOpt.isPresent()) {
	          Contact contact = contactOpt.get();
	          String name = principal.getName();
	          User user = userRepo.getUserByUserName(name);
	          
	          if (user.getuId()==(contact.getUser().getuId())) {
	              user.getContacts().remove(contact);
	              this.userRepo.save(user);
	              System.out.println(contact.getcId() + " deleted");
	              // Add message to RedirectAttributes
	              redirectAttributes.addFlashAttribute("message", new Message("alert-success", "Successfully deleted contact!!"));
	          } 
	      } 

	      return "redirect:/user/show-contact/0";

	  }
	  
	  
	  //handler for creating profile

	  @GetMapping("/profile")
	  public String profile(Model model) {
		  model.addAttribute("title", "Profile | SCM");
		  return "/normal/profile";
	  }
	  
	  @GetMapping("/settings")
	  public String settings(Model model) {
		  model.addAttribute("title", "Settings | SCM");
		  return "/normal/setting";
	  }
	  
	  
	  //chnage password handler
	  @PostMapping("/change-password")
	  public String changepassword(@RequestParam("oldpwd") String oldPwd,@RequestParam("newpwd") String newPwd, Principal principal,RedirectAttributes redirectAttributes) {
			/*
			 * System.out.println(oldPwd); System.out.println(newPwd);
			 */
		  String name = principal.getName();
		  User user = this.userRepo.getUserByUserName(name);
		  if(this.encoder.matches(oldPwd,user.getUpwd())) {
			  //change password
			  user.setUpwd(this.encoder.encode(newPwd));
			  this.userRepo.save(user);
			  redirectAttributes.addFlashAttribute("message", new Message("alert-success", "Password deleted Successfully !!"));	
		  }
		  else {
			  redirectAttributes.addFlashAttribute("message", new Message("alert-danger", "Please enter old passeord correct!!"));
			  return "redirect:/user/settings";
		  }
		  
		  return "redirect:/user/index";
	  }
}
	    
	  
