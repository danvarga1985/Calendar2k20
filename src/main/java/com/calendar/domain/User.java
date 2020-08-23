package com.calendar.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "tbl_user")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	private LocalDateTime registrationTime;
	
	private boolean onlyActiveProjects;

	private String validateToken;

	private boolean validated;

	private boolean deleted;

	public User() {

	}

	public User(String email, String password, String validateToken) {
		this.email = email;
		this.password = password;
		this.validateToken = validateToken;

		this.registrationTime = LocalDateTime.now();
		
		this.onlyActiveProjects = false;
		validated = false;
		deleted = false;
	}
	
	public boolean isOnlyActiveProjects() {
		return onlyActiveProjects;
	}

	public void setOnlyActiveProjects(boolean onlyActiveProjects) {
		this.onlyActiveProjects = onlyActiveProjects;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getRegistrationTime() {
		return registrationTime;
	}

	public String getValidateToken() {
		return validateToken;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !deleted;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !deleted;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !deleted;
	}

	@Override
	public boolean isEnabled() {
		return !deleted;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

}
