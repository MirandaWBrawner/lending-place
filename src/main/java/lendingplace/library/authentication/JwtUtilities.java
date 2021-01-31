package lendingplace.library.authentication;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lendingplace.library.service.LibrarianDetails;

@Component
public class JwtUtilities {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtilities.class);
	
	@Autowired
	private JwtPropertiesContainer propContainer;
	
	JwtPropertiesContainer getJwtProperties() {
		return propContainer;
	}
	
	public String generateToken(Authentication authentication) {
		LibrarianDetails details = (LibrarianDetails) authentication.getPrincipal();
		Date now = new Date();
		Date expirationDate = new Date(new Date().getTime() + getJwtProperties().sessionLength());
		return Jwts.builder().setSubject(details.getUsername())
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, getJwtProperties().getKey())
				.compact();
	}
	
	public String getUsernameFromToken(String jwt) {
		return Jwts.parser().setSigningKey(propContainer.getKey())
				.parseClaimsJws(jwt).getBody().getSubject();
	}
	
	public boolean validateToken(String jwt) {
		if (jwt == null) return false;
		try {
			Jwts.parser().setSigningKey(propContainer.getKey()).parseClaimsJws(jwt);
			return true;
		} catch (SignatureException exception) {
			logger.info("Invalid JWT signature. " + exception.getMessage());
		} catch (MalformedJwtException exception) {
			logger.info("Invalid Json Web Token. " + exception.getMessage());
		} catch (ExpiredJwtException exception) {
			logger.info("Token has expired. " + exception.getMessage());
		} catch (UnsupportedJwtException exception) {
			logger.info("Unsupported Json Web Token " + exception.getMessage());
		} catch (IllegalArgumentException exception) {
			logger.info("The token is invalid because the claims string is empty. " + exception.getMessage());
		}
		return false;
	}
}
