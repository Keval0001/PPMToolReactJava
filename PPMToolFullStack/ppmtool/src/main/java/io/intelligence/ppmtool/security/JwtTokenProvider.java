package io.intelligence.ppmtool.security;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.intelligence.ppmtool.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static io.intelligence.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static io.intelligence.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
	
	//Generate the token
	
	public String generateToken(Authentication authentication) {
		User user = (User)authentication.getPrincipal();
		Date now = new Date(System.currentTimeMillis());
		Date expiryDate = new Date(now.getTime()+EXPIRATION_TIME);
		String userId = Long.toString(user.getId());
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("id",(Long.toString(user.getId())));
		claims.put("username", user.getUsername());
		claims.put("fullName", user.getFullname());
		//roles as well if we have to follow it
		return Jwts.builder()
				.setSubject(userId)
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
	}
	
	//Validate the token
	//Get user Id from token

}
