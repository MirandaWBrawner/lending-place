package lendingplace.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LendingPlaceApplication {

	public static void main(String[] args) {
		/*
		Logger logger = LoggerFactory.getLogger(LendingPlaceApplication.class);
		String logFilePath = "src/main/resources/logging/serverLog.log";
		File logFile = new File(logFilePath);
		try {
			PrintStream logStream = new PrintStream(logFile);
			System.setOut(logStream);
		} catch (FileNotFoundException exception) {
			logger.error("The log file could not be found. Using standard output instead.");
		} catch (SecurityException exception) {
			logger.error("Unable to redirect the standard output to the log file.");
		} */
		SpringApplication.run(LendingPlaceApplication.class, args);
	}

}
