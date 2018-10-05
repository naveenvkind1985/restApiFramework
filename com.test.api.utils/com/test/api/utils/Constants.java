package com.test.api.utils;

import java.time.Duration;

/**
 * Constant values used throughout the application
 * @author HMarkam
 *
 */
public class Constants {
	
	/** Constants for External Config  files */
	
	public static final String MAIL_CONFIG_FILE_KEY = "mail";
	public static final String BASE_CONFIG_FILE_KEY = "base";
	
	/** Default wait */
	public static final int DEFAULT_WAIT_4_ELEMENT = 20;
	public static final int DEFAULT_WAIT_4_PAGE = 20;
	
	/** Duration fluent wait **/
	public static final Duration SECONDS = Duration.ofSeconds(30);
	public static final Duration MILISECONDS = Duration.ofMillis(1);
	
	/**
	 * Defines the pattern for numeric
	 */
	public static final String NUMERIC_PATTERN = "\\d+";
	
	/**
	 * Regex to define the pattern for a valid string of characters only
	 */
	public static final String CHARACTER_PATTERN = "[a-zA-Z ]+";

	/**
	 * Regex to define the alphanumeric code
	 */
	public static final String ALPHANUMERIC_PATTERN = "^[0-9a-zA-Z ]+$";
	
	/**
	 * Regex to define the pattern for a valid date
	 */
	public static final String DATE_PATTERN = "^(?:(?:(?:0[1-9]|[12]\\d|3[01])/(?:0[13578]|1[02])|(?:0[1-9]|[12]\\d|30)/(?:0[469]|11)|(?:0[1-9]|1\\d|2[0-8])/02)/(?!0000)\\d{4}|(?:(?:0[1-9]|[12]\\d)/02/(?:(?!0000)(?:[02468][048]|[13579][26])00|(?!..00)\\d{2}(?:[02468][048]|[13579][26]))))$";

	/**
	 * Regex to define the pattern for a valid password
	 */
	public static final String PASSWORD_PATTERN = "(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$!]).{8,16}";

	/**
	 * Regex to define the pattern for a valid email
	 */
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9\\+]+(\\.[_A-Za-z0-9]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	/**
	 * Defines the user directory of current project
	 */
	public static final String USER_DIRECTORY=System.getProperty("user.dir");
	

}
