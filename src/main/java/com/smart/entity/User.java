package com.smart.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="USER")
public class User {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
     private int uId;
	 
	 @NotBlank(message = "Name field must be required!")
	 @Size(min = 3,max = 20, message = "min 3 and max 20 characters are allowed")
     private String uName;
	 
     @Column(unique = true )
     @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
     private String uEmail;
     
     
     private String upwd;
     private String role;
     private boolean enabled;
     @Column(length = 300)
     private String about;
     private String imageUrl;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "user", orphanRemoval = true)
	private List<Contact> contacts= new ArrayList<>();
	
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uNmaeString) {
		this.uName = uNmaeString;
	}
	public String getuEmail() {
		return uEmail;
	}
	public void setuEmail(String uEmail) {
		this.uEmail = uEmail;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Override
	public String toString() {
		return "User [uId=" + uId + ", uName=" + uName + ", uEmail=" + uEmail + ", upwd=" + upwd + ", role=" + role
				+ ", enabled=" + enabled + ", about=" + about + ", imageUrl=" + imageUrl + ", contacts=" + contacts
				+ "]";
	}	
	
     
     
}
