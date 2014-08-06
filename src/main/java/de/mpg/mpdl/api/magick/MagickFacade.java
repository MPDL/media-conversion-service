package de.mpg.mpdl.api.magick;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * Facade implementing the calling imagemagick
 * 
 * @author saquet
 *
 */
public class MagickFacade {
	private static String CONVERT_CMD = "convert";
	private Runtime runtime;
	private static final String DEFAULT_FORMAT = "png";

	/**
	 * Constructor: Initialize the runtime and the configuration
	 */
	public MagickFacade() {
		runtime = Runtime.getRuntime();
		MagickConfiguration config = new MagickConfiguration();
		CONVERT_CMD = FilenameUtils.concat(config.getImageMagickHome(),
				CONVERT_CMD);
	}

	/**
	 * Do convert from an inputstream to an outputstream
	 * 
	 * @param in
	 * @param out
	 * @param name
	 * @param format
	 * @param size
	 * @param params
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void convert(InputStream in, OutputStream out, String name,
			String format, String size, String params) throws IOException,
			InterruptedException {
		File temp = File.createTempFile("magick",
				"".equals(FilenameUtils.getExtension(name)) ? "" : "."
						+ FilenameUtils.getExtension(name));
		IOUtils.copy(in, new FileOutputStream(temp));
		IOUtils.copy(new FileInputStream(convert(temp, format, size, params)),
				out);
	}

	/**
	 * Convert a File to the specified format with the specified size
	 * 
	 * @param input
	 * @param format
	 * @param size
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public File convert(File input, String format, String size, String params)
			throws IOException, InterruptedException {
		File output = createOutputFile(format);
		String cmd = generateCommand(input, output, format, size, params);
		Process p = runtime.exec(cmd);
		// Wait until the process is done
		p.waitFor();
		return output;
	}

	/**
	 * Create the file where the result of the transformation will be written
	 * 
	 * @param format
	 * @return
	 * @throws IOException
	 */
	private File createOutputFile(String format) throws IOException {
		if (format == null || "".equals(format))
			format = DEFAULT_FORMAT;
		return File.createTempFile("magick_result", "." + format);
	}

	/**
	 * Generate an Imagemagick command Line following the template:<br/>
	 * convert -define jpeg:size=SIZE INPUT PARAMS OUTPUT
	 * 
	 * @param input
	 * @param output
	 * @param format
	 * @param size
	 * @param cmd
	 * @return
	 */
	private String generateCommand(File input, File output, String format,
			String size, String params) {

		params = params != null ? params : "";
		return CONVERT_CMD + " " + getSizeAsParam(size) + " "
				+ input.getAbsolutePath() + " " + params + " "
				+ output.getAbsolutePath();
	}

	/**
	 * Return the size param as following: -define jpeg:size=SIZE
	 * 
	 * @param size
	 * @return
	 */
	private String getSizeAsParam(String size) {
		if (size == null || "".equals(size))
			return "";
		return "-define jpeg:size=" + size;
	}

	public static void main(String[] args) {
		MagickFacade m = new MagickFacade();
		try {
			m.convert(new File("C:\\Users\\saquet\\Pictures\\Jellyfish.jpg"),
					"png", "30", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
