package lendingplace.library.authentication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import lendingplace.library.LendingPlaceApplication;

class JwtPropertiesTests {
	
	private int hours = 12;
	private int minutes = 1;
	private int seconds = 55;
	private String key = "mGsj3sfm2qXf3nEIm294nsZgYj";
	private static ApplicationContext context;
	
	@BeforeAll
	static void setup() {
		context = SpringApplication.run(LendingPlaceApplication.class);
	}
	
	private void checkSingleProperty(Object expected, Object actual, String propertyName) {
		String message = String.format("The field '%s' in JwtPropertiesContainer should match "
				+ "application.properties. Check application.properties to see if "
				+ "the %s property is set to %s.", propertyName, propertyName, expected);
		Assertions.assertEquals(expected, actual, message);
	}
	
	private void checkJwtProperties(JwtPropertiesContainer container, 
			int expectedHours, int expectedMinutes, int expectedSeconds, 
			String expectedKey) {
		checkSingleProperty(expectedHours, container.getHours(), "hours");
		checkSingleProperty(expectedMinutes, container.getMinutes(), "minutes");
		checkSingleProperty(expectedSeconds, container.getSeconds(), "seconds");
		checkSingleProperty(expectedKey, container.getKey(), "key");
	}
	
	@Test
	void testJwtPropertiesWithAppContext() {
		JwtPropertiesContainer container = context.getBean(JwtPropertiesContainer.class);
		checkJwtProperties(container, hours, minutes, seconds, key);
	}
	
	@Test
	void testSessionLength() {
		JwtPropertiesContainer container = context.getBean(JwtPropertiesContainer.class);
		long expLength = 1000 * (hours * 3600 + minutes * 60 + seconds);
		Assertions.assertEquals(expLength, container.sessionLength(), "The session length "
				+ "returned by JwtPropertiesContainer did not match the length specified "
				+ "in the application.properties file.");
	}
}
