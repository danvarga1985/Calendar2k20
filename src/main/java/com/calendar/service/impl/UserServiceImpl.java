package com.calendar.service.impl;

import java.security.SecureRandom;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.calendar.domain.User;
import com.calendar.exceptions.EmailAlreadyExistsException;
import com.calendar.exceptions.UserDeletedException;
import com.calendar.repository.UserRepository;
import com.calendar.requestdto.RegistrationDto;
import com.calendar.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
        if (user.isDeleted() == false) {
            return user;
        } else {
            throw new UserDeletedException("accountDeleted");
        }
    }
		
	@Override
	@Transactional
	public void createUser(RegistrationDto regDto) {
		if (userRepository.existsByEmail(regDto.getEmail())) {
			throw new EmailAlreadyExistsException("Email already exists");
		}
		User user = new User(
				regDto.getEmail(), 
				passwordEncoder.encode(regDto.getPassword()), 
				validationTokenGeneration());
		
		userRepository.save(user);
	}
	
	@Override
	public String validationTokenGeneration() {
		SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();
        return token;
	}

	@Override
	public User getFullUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
	
	@Override
	public void deleteUser(int id) {
		User user = getFullUser();
//		if (user.getAuthority() == Authority.ADMIN) {
//
//            User userById = userRepository.findById(id)
//                    .orElseThrow(() -> new IdNotFoundException(""));
//
//            if (!userById.isDeleted()) {
//                userById.setIsDeleted(true);
//            } else {
//                userById.setIsDeleted(false);
//            }
//        } else {
//            throw new AccessDeniedException("");
//        }
		
	}
}