package com.test.api.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.test.api.utils.Constants;

/**
 * External Config class implemented to load multiple .properties file which can
 * be used throughout the application
 * 
 * @author HMarkam
 *
 */
public class ExternalConfig implements FilenameFilter {

	private static final Log log = LogFactory.getLog(ExternalConfig.class);

	private String fileName;

	private Map<String, String> fileMaps;

	private Map<String, Properties> externalProperties;

	/**
	 * Method to return property value per key
	 * 
	 * @return externalProperties
	 */
	public Map<String, Properties> getExternalProperties() {
		return externalProperties;
	}

	/**
	 * Method call to load property files and return property object which contains
	 * key and value
	 * 
	 * @param key
	 * @return Properties
	 */
	public Properties getProperties(String key) {
		if (getExternalProperties() == null || getExternalProperties().get(key) == null) {
			loadProperties(key);
		}
		return getExternalProperties().get(key);
	}

	/**
	 * Method to add multiple property file names in a map
	 */
	public void updateData() {
		if (fileMaps == null) {
			fileMaps = new HashMap<>();
		}

		fileMaps.put(Constants.BASE_CONFIG_FILE_KEY, "config.properties");
		fileMaps.put(Constants.MAIL_CONFIG_FILE_KEY, "mail.properties");
	}

	/**
	 * Method to load the property file with its key and value
	 * 
	 * @param key
	 */
	public void loadProperties(String key) {
		String configDir = Constants.USER_DIRECTORY + "\\config\\properties\\";

		updateData();

		String propFileName = getFileMaps().get(key);

		filenameFilterCaseIgnore(propFileName);

		String[] files = new File(configDir).list();
		File propFile = null;

		if (files != null && files.length > 0) {
			propFile = new File(configDir + File.separator + propFileName);

			if (propFile.exists() && propFile.isFile()) {
				Properties props = new Properties();

				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(propFile);
					props.load(fileInputStream);

					if (externalProperties == null) {
						externalProperties = new HashMap<>();
					}
					getExternalProperties().put(key, props);
				} catch (Exception ex) {
					log.error("Error in context initialisation:", ex);
				}
			}
		}

	}

	public Map<String, String> getFileMaps() {
		return fileMaps;
	}

	public void setFileMaps(Map<String, String> fileMaps) {
		this.fileMaps = fileMaps;
	}

	public void filenameFilterCaseIgnore(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.equalsIgnoreCase(fileName);
	}
	
	public Object getInstance(){
		return null;
	}
}
