package lendingplace.library.requestHandler;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import lendingplace.library.LendingPlaceApplication;
import lendingplace.library.dao.UserDao;
import lendingplace.library.model.User;
import lendingplace.library.request.SignupRequest;
import lendingplace.library.service.UserRoleService;

class SignupTest {
	
	private static ApplicationContext context;
	private static UserRoleService roleService;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		context = SpringApplication.run(LendingPlaceApplication.class);
		roleService = context.getBean(UserRoleService.class);
	}
	
	@Test
	void testGetBean() {
		UserRequestHandler requestHandler = null;
		try {
			requestHandler = context.getBean(UserRequestHandler.class);
		} catch (Exception exception) {
			Assertions.fail(exception.toString());
		}
		Assertions.assertNotNull(requestHandler, "The UserRequestHandler bean "
				+ "did not load.");
	}
	
	@Test
	void testUsernameAlreadyTaken() {
		String nameAlreadyTaken = "username";
		Set<String> roleNames = Set.of("a", "b", "c");
		SignupRequest request = new SignupRequest(
				"fr", nameAlreadyTaken, "password", "email", roleNames);
		UserRequestHandler requestHandler = context.getBean(UserRequestHandler.class);
		int expectedHttpStatus = 400; // bad request
		Assertions.assertEquals(expectedHttpStatus, requestHandler.signup(request)
				.getStatusCodeValue(),
				"A signup request where the username is already in use "
				+ "should return an http status of 400.");
	}
	
	@Test
	void testSuccessfulSignupDefaultRole() {
		for (int i = 0; i < 5; i++) {
			UserDao userDao = context.getBean(UserDao.class);
			Random random = new Random();
			String username = "TestUser";
			while (userDao.existsByUsername(username)) {
				username = "TestUser" + random.nextInt();
			} 
			String password = "passwordpasswordpassword";
			SignupRequest request = new SignupRequest(
					"sw", username, password, "theEmail", null);
			UserRequestHandler requestHandler = context.getBean(UserRequestHandler.class);
			int expectedHttpStatus = 200; // success
			Assertions.assertFalse(userDao.existsByUsername(username),
					"To test the signup, the user should not already be registered. "
					+ "The existsByUsername method should return false.");
			Assertions.assertEquals(expectedHttpStatus, requestHandler.signup(request)
					.getStatusCodeValue(),
					"A valid signup request should return an http status of 200.");
			Assertions.assertTrue(userDao.existsByUsername(username),
					"After a valid signup request, the new user's username should "
					+ "be retrievable from the database.");
			Optional<User> optionalUser = userDao.findByUsername(username);
			Assertions.assertTrue(optionalUser.isPresent(), "The user could not be retrieved "
					+ "from the database.");
			User user = optionalUser.get();
			Assertions.assertTrue(roleService.userHasRoleNamed(user, "User"), 
					"The default set of roles "
					+ "should include one with the name 'User'.");
			Assertions.assertFalse(roleService.userHasRoleNamed(user, "Librarian"),
					"The default set of roles "
					+ "should not include 'Librarian'.");
		}
	}
	
	@Test
	void testSuccessfulSignupLibrarianRole() {
		for (int i = 0; i < 5; i++) {
			UserDao userDao = context.getBean(UserDao.class);
			Random random = new Random();
			String username = "TestUser";
			while (userDao.existsByUsername(username)) {
				username = "TestUser" + random.nextInt();
			} 
			String password = "passwordpasswordpassword";
			SignupRequest request = new SignupRequest(
					"sw", username, password, "theEmail", Set.of("Librarian"));
			UserRequestHandler requestHandler = context.getBean(UserRequestHandler.class);
			int expectedHttpStatus = 200; // success
			Assertions.assertFalse(userDao.existsByUsername(username),
					"To test the signup, the user should not already be registered. "
					+ "The existsByUsername method should return false.");
			Assertions.assertEquals(expectedHttpStatus, requestHandler.signup(request)
					.getStatusCodeValue(),
					"A valid signup request should return an http status of 200.");
			Assertions.assertTrue(userDao.existsByUsername(username),
					"After a valid signup request, the new user's username should "
					+ "be retrievable from the database.");
			Optional<User> optionalUser = userDao.findByUsername(username);
			Assertions.assertTrue(optionalUser.isPresent(), "The user could not be retrieved "
					+ "from the database.");
			User user = optionalUser.get();
			Assertions.assertTrue(roleService.userHasRoleNamed(user, "User"), 
					"The set of roles should include one with the name 'User'.");
			Assertions.assertTrue(roleService.userHasRoleNamed(user, "Librarian"), 
					"The set of roles should include one with the name 'Librarian'.");
		}
	}

}
