package com.mo.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtils {

	private String SECRET_KEY = "lsdfsadhfukashfgiauydstioftuywgefbsdmvaisetfgiauwetriajsdbvkcjasgdfiutayd";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String extractRole(String token) {
		return extractClaim(token, Claims::getId);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String generateJwtToken(UserDetails userDetails, String role) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername(), role);

	}

	private String createToken(Map<String, Object> claims, String subject, String role) {

		long now = System.currentTimeMillis();

		long expirationTime = 7 * 24 * 60 * 60 * 1000; // set expiry time as 7 days

		return Jwts.builder()
				.setId(role)
				.setSubject(subject)
				.setClaims(claims)
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + expirationTime))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

	}
}
