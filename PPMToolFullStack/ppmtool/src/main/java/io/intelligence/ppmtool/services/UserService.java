package io.intelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.intelligence.ppmtool.domain.User;
import io.intelligence.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.intelligence.ppmtool.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			
			//Username has to be unique (custom exception)
			
			newUser.setUsername(newUser.getUsername());
			//Make sure that password and confirm password match
			newUser.setConfirmPassword("");
			//Dont persist or show the confirmPassword
			return userRepository.save(newUser);
		} catch (Exception e) {
			 throw new UsernameAlreadyExistsException("Username "+ newUser.getUsername()+" already exists.");
		}
	}
}
