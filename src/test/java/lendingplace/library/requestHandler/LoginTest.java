package lendingplace.library.requestHandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import lendingplace.library.LendingPlaceApplication;
import lendingplace.library.authentication.JwtUtilities;
import lendingplace.library.authentication.ResponseWithJwt;
import lendingplace.library.request.LoginRequest;
import lendingplace.library.service.LibrarianDetailsService;

class LoginTest {
	
	private static ApplicationContext context;
	private static String startingLanguage = "sw";
	private static String[] usernames = {"TestUser1184198057", "TestUser-670503797", 
			"TestUser-421434768", "TestUser1496407591"};
	private String[] requestLanguages = {"hi", "es", "fr", null};

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		context = SpringApplication.run(LendingPlaceApplication.class);
		LibrarianDetailsService detailsService = context.getBean(LibrarianDetailsService.class);
		for (int index = 0; index < usernames.length; index++ ) {
			detailsService.updateLanguage(usernames[index], startingLanguage);
		}
	}

	@Test
	void testSuccessfulLogin() {
		UserRequestHandler requestHandler = context.getBean(UserRequestHandler.class);
		String password = "passwordpasswordpassword";
		for (int index = 0; index < usernames.length; index++ ) {
			LoginRequest request = new LoginRequest(
					requestLanguages[index], usernames[index], password);
			ResponseEntity<?> response = requestHandler.login(request);
			Assertions.assertNotNull(response, "The login method should not return null.");
			int expectedHttpStatus = 200;
			Assertions.assertEquals(expectedHttpStatus, response.getStatusCodeValue(),
					String.format("Login failed for user %s. A login with the "
							+ "correct username and password should return an "
							+ "http status of %d.",
							usernames[index], expectedHttpStatus));
			Assertions.assertTrue(response.hasBody(), "A login with the correct "
					+ "username and password should not return an empty response.");
			Object responseContent = response.getBody();
			Assertions.assertNotNull(responseContent, "The content of a response "
					+ "to a successful login should not be null.");
			Assertions.assertTrue(responseContent instanceof ResponseWithJwt);
			ResponseWithJwt jwtResponse = (ResponseWithJwt) responseContent;
			Assertions.assertEquals("Bearer", jwtResponse.getTokenType());
			Assertions.assertEquals(usernames[index], jwtResponse.getUsername());
			if (requestLanguages[index] == null) {
				Assertions.assertEquals(startingLanguage, jwtResponse.getLanguage(),
						"The language returned in the response should match the one "
						+ "in the database if the request does not provide one.");
			} else {
				Assertions.assertEquals(requestLanguages[index], jwtResponse.getLanguage(), 
						"The language returned in the response should match the one "
						+ "in the request if the request includes a non-null language.");
			}
			String token = jwtResponse.getJwt();
			JwtUtilities util = context.getBean(JwtUtilities.class);
			Assertions.assertTrue(util.validateToken(token), "The json web token should be valid.");
		}
	}
	
	@Test
	void testIncorrectPassword() {
		String password = "wrongpassword";
		for (int index = 0; index < usernames.length; index++) {
			String language = requestLanguages[index];
			String username = usernames[index];
			LoginRequest request = new LoginRequest(language, username, password);
			UserRequestHandler requestHandler = context.getBean(UserRequestHandler.class);
			ResponseEntity<?> response = requestHandler.login(request);
			Assertions.assertNotNull(response, "The login method should not return null.");
			int expectedHttpStatus = 404;
			String message = String.format("Login for user %s went unexpectedly. A login with "
					+ "the wrong password should return an http status of %d", 
					username, expectedHttpStatus);
			Assertions.assertEquals(expectedHttpStatus, response.getStatusCodeValue(), message);
		}
	}
	
	@Test
	void testNoSuchUser() {
		String[] namesNotInDB = {"a", "b", "c", "d"};
		String password = "passwordpasswordpassword";
		for (int index = 0; index < namesNotInDB.length; index++) {
			String language = requestLanguages[index];
			String username = namesNotInDB[index];
			LoginRequest request = new LoginRequest(language, username, password);
			UserRequestHandler requestHandler = context.getBean(UserRequestHandler.class);
			ResponseEntity<?> response = requestHandler.login(request);
			Assertions.assertNotNull(response, "The login method should not return null.");
			int expectedHttpStatus = 404;
			String message = String.format("Login for user %s went unexpectedly. A login with "
					+ "the wrong password should return an http status of %d", 
					username, expectedHttpStatus);
			Assertions.assertEquals(expectedHttpStatus, response.getStatusCodeValue(), message);
		}
	}

}
