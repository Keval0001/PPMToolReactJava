package io.intelligence.ppmtool.web;

import javax.validation.Valid;

import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.intellegience.ppmtool.payload.JWTLoginResponse;
import io.intellegience.ppmtool.payload.LoginRequest;
import io.intelligence.ppmtool.domain.User;
import io.intelligence.ppmtool.security.JwtTokenProvider;
import io.intelligence.ppmtool.services.MapValidationErrorService;
import io.intelligence.ppmtool.services.UserService;
import io.intelligence.ppmtool.validator.UserValidator;

import static io.intelligence.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<?> AuthenticatedUserRealm(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX+ tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JWTLoginResponse(true, jwt));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
		// Validate passwords match
		userValidator.validate(user, result);
		
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null)return errorMap;
		
		User newUser = userService.saveUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}
}

















