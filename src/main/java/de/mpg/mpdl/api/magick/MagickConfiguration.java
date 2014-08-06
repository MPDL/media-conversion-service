package de.mpg.mpdl.api.magick;

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
public class MagickConfiguration {

	private static final String PROPERTIES_FILENAME = "magick-service.properties";
	private Properties properties = new Properties();

	public MagickConfiguration() {
		load();
	}

	/**
	 * Return the home directory for imagemagick (important for windows)
	 * 
	 * @return
	 */
	public String getImageMagickHome() {
		if (properties.containsKey("imagemagick.home"))
			return (String) properties.get("imagemagick.home");
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
