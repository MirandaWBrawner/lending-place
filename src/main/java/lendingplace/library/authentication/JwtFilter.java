package lendingplace.library.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lendingplace.library.service.LibrarianDetailsService;

public class JwtFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	
	@Autowired
	private JwtUtilities utils;
	
	@Autowired
	private LibrarianDetailsService detailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if (jwt != null && utils.validateToken(jwt)) {
				String username = utils.getUsernameFromToken(jwt);
				UserDetails details = detailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
						details, null, details.getAuthorities());
				upaToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upaToken);
			}
		} catch (Exception exception) {
			logger.info("Cannot set user authentication. Exception caught: " + exception.getMessage());
		}
		filterChain.doFilter(request, response);
	}
	
	/** Returns the json web token if the request has one in the Authentication header.
	 The header must start with the string "Bearer " followed by a space. 
	 The rest of the Authentication header is the token. 
	 Returns null if any of these conditions are not met. */
	private String parseJwt(HttpServletRequest request) {
		String authenticationHeader = request.getHeader("Authentication");
		
		// Check that the header for authentication is present and starts with Bearer followed by a space
		if (StringUtils.hasText(authenticationHeader) && authenticationHeader.startsWith("Bearer ")) {
			
			// The rest of the header, after the word Bearer, is the jwt
			return authenticationHeader.substring(7);
		}
		return null;
	}

}
