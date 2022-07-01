package com.example.userService.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtUtil {

	private String secret;
	private int jwtExpirationInMs;
	
	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${jwt.secret}")
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Value("${jwt.jwtExpirationInMs}")
	public void setJwtExpirationInMs(int jwtExpirationInMs) {
		this.jwtExpirationInMs = jwtExpirationInMs;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_AUTHOR"))) {
			claims.put("isAuthor", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_MENTOR"))) {
			claims.put("isMentor", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_TRAINEE"))) {
			claims.put("isTrainee", true);
		}
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			throw new BadCredentialsException("Invalid credentials", e);
		} catch (ExpiredJwtException e) {
			throw e;
		}
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		List<SimpleGrantedAuthority> roles = null;

		Boolean isAdmin = claims.get("isAdmin", Boolean.class);

		Boolean isAuthor = claims.get("isAuthor", Boolean.class);

		Boolean isMentor = claims.get("isMentor", Boolean.class);

		Boolean isTrainee = claims.get("isTrainee", Boolean.class);

		if (isAdmin != null && isAdmin) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));

		}

		if (isAuthor != null && isAuthor) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_AUTHOR"));
		}

		if (isMentor != null && isMentor) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_MENTOR"));
		}
		if (isTrainee != null && isTrainee) {
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_TRAINEE"));
		}
		return roles;
	}
}
