package lendingplace.library.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtPropertiesContainer {

	@Value("${lendingplace.library.key}")
	private String key;
	
	@Value("${lendingplace.library.sessionLengthHours}")
	private int hours;
	
	@Value("${lendingplace.library.sessionLengthMinutes}")
	private int minutes;
	
	@Value("${lendingplace.library.sessionLengthSeconds}")
	private int seconds;
	
	String getKey() {
		return key;
	}
	int getHours() {
		return hours;
	}
	int getMinutes() {
		return minutes;
	}
	int getSeconds() {
		return seconds;
	}
	long sessionLength() {
		return getHours() * 3_600_000 + getMinutes() * 60_000 + getSeconds() * 1000; 
	}
	
	
}
