package de.mpg.mpdl.service.rest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

/**
 * Properties of the service
 * 
 * @author saquet
 *
 */
public class ServiceConfiguration {

	public static final String SERVICE_NAME = "magick";
	private static final String PROPERTIES_FILENAME = "magick-service.properties";
	private Properties properties = new Properties();

	public ServiceConfiguration() {
		load();
	}

	public String getServiceUrl() {
		if (properties.containsKey("service.url"))
			return (String) properties.get("service.url");
		return "http://localhost:8080/" + SERVICE_NAME;
	}

	/**
	 * Return the home directory for imagemagick (important for windows)
	 * 
	 * @return
	 */
	public String getImageMagickConvertBin() {
		if (properties.containsKey("imagemagick.convert.bin"))
			return (String) properties.get("imagemagick.convert.bin");
		return null;
	}

	/**
	 * Load the properties
	 */
	private void load() {
		if (getPropertyFileLocation() != null) {
			try {
				properties.load(new FileInputStream(new File(
						getPropertyFileLocation())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Return the location of the property file according to the server
	 * 
	 * @return
	 */
	private String getPropertyFileLocation() {
		String loc = "";
		if (System.getProperty("jboss.server.config.dir") != null) {
			loc = System.getProperty("jboss.server.config.dir");
		} else if (System.getProperty("catalina.home") != null) {
			loc = System.getProperty("catalina.home") + "/conf";
		}
		return FilenameUtils.concat(loc, PROPERTIES_FILENAME);
	}

}
