package lendingplace.library.authentication;

import java.util.Date;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lendingplace.library.LendingPlaceApplication;
import lendingplace.library.service.LibrarianDetails;

class JwtUtilitiesTests {
	
	private static String key = "mGsj3sfm2qXf3nEIm294nsZgYj";
	
	/* Spaces are not necessarily preserved when run the username
	is added to a token and then returned. Tokens do not have to
	have a username. */
	private static String[] usernameArray = {"abc", "1234", 
			"&#789j98jd9oj3w8784fns", "a a",
			"./.fs./efifmefwe\\wa\'a\"wa/w", "", "\s\n\s"
	};
	private static ApplicationContext context;
	
	@BeforeAll
	static void setup() {
		context = SpringApplication.run(LendingPlaceApplication.class);
	}
	
	@Test
	void testGetBean() {
		JwtUtilities util = null;
		try {
			util = context.getBean(JwtUtilities.class);
		} catch (Exception exception) {
			Assertions.fail(exception.toString());
		}
		Assertions.assertNotNull(util, "The JwtUtilities bean is null.");
	}
	
	@Test
	void testGenerateToken() {
		String inputUsername = "ExampleUsername";
		LibrarianDetails details = Mockito.mock(LibrarianDetails.class);
		Mockito.when(details.getUsername()).thenReturn(inputUsername);
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getPrincipal()).thenReturn(details);
		JwtUtilities util = context.getBean(JwtUtilities.class);
		String token = null;
		try {
			token = util.generateToken(authentication);
		} catch (Exception exception) {
			Assertions.fail(exception.toString());
		}
		Assertions.assertNotNull(token, "The token is null.");
		Assertions.assertNotEquals("", token, "The token is empty");
	}
	
	private String nullToEmpty(String input) {
		if (input == null) return "";
		return input;
	}
	
	@Test
	void testGetUsername() {
		for(String inputUsername: usernameArray) {
			LibrarianDetails details = Mockito.mock(LibrarianDetails.class);
			Mockito.when(details.getUsername()).thenReturn(inputUsername);
			Authentication authentication = Mockito.mock(Authentication.class);
			Mockito.when(authentication.getPrincipal()).thenReturn(details);
			JwtUtilities util = context.getBean(JwtUtilities.class);
			String token = util.generateToken(authentication);
			String retrievedUsername = util.getUsernameFromToken(token);
			Assertions.assertEquals(inputUsername.trim(), 
					nullToEmpty(retrievedUsername).trim(), 
					"The username returned by getUsernameFromToken should "
					+ "match the username provided in the authentication "
					+ "that was passed to generateToken.");
		}
	}
	
	@Test
	void testValidateTokenSuccess() {
		for(String inputUsername: usernameArray) {
			LibrarianDetails details = Mockito.mock(LibrarianDetails.class);
			Mockito.when(details.getUsername()).thenReturn(inputUsername);
			Authentication authentication = Mockito.mock(Authentication.class);
			Mockito.when(authentication.getPrincipal()).thenReturn(details);
			JwtUtilities util = context.getBean(JwtUtilities.class);
			String token = util.generateToken(authentication);
			Assertions.assertTrue(util.validateToken(token), "The validateToken "
					+ "method rejected a token with the username " + inputUsername);
		}
	}

	
	private String truncateToLength(String content, int maxLength) {
		if (content == null) return null;
		if (content.length() > maxLength && maxLength >= 1) {
			return content.substring(0, maxLength);
		}
		return content;
	}
	
	@Test
	void testImproperToken() {
		String[] improperTokens = {"abcde", "eyJhbGciOiJUzUxMiJ9.eyJhIjoiYiJ9.YgFr8lomYyB"
				+ "6aoQNIe-vSGsqIih4Ny91zETfZ7NSmwxYtaH24UGO6IIfTbgKG5XH60MgEMoqgVFaXBYszQehLBw",
				null, "", "ey.a.b"};
		for (String token: improperTokens) {
			JwtUtilities util = context.getBean(JwtUtilities.class);
			Assertions.assertFalse(util.validateToken(token), 
					String.format("The validateToken method marked the immproper token '%s' "
							+ "as valid.", truncateToLength(token, 15)));
		}
	}
	
	private String createToken(long expirationDateMillis, String tempKey) {
		if (tempKey == null || tempKey.isEmpty()) {
			return null;
		}
		Date issueDate = new Date(System.currentTimeMillis() - 1_000_000_000);
		return Jwts.builder().setSubject("ExampleUsername")
		.setIssuedAt(issueDate)
		.setExpiration(new Date(expirationDateMillis))
		.signWith(SignatureAlgorithm.HS512, tempKey)
		.compact();
	}
	
	@Test
	void testTokenExpirationDateAndKey() {
		long[] times = { -100_000_000, -1000, 0, 1_000_000_000, -10_000, 10_000_000_000L };
		String[] keyArray = {null, "", key, "wrongKey"};
		for (long timeDifference: times) {
			for (String tempKey: keyArray) {
				long expiration = System.currentTimeMillis() + timeDifference;
				boolean expectedResult = timeDifference > 0 && Objects.equals(tempKey, key);
				String token = createToken(expiration, tempKey);
				JwtUtilities util = context.getBean(JwtUtilities.class);
				Assertions.assertEquals(expectedResult, util.validateToken(token), 
						String.format("The validateToken method returned the wrong "
								+ "result for a key of %s and an expiration date of "
								+ "the current time + %s milliseconds.", tempKey,
								expiration));
			}
		}
	}
}
