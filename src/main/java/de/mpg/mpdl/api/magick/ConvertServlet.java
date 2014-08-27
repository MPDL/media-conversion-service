package de.mpg.mpdl.api.magick;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.mpg.mpdl.api.magick.MagickFacade.Priority;

/**
 * Web Service called for using the service
 * 
 * @author saquet
 *
 */
public class ConvertServlet extends HttpServlet {

	private static final long serialVersionUID = -1284139955167964710L;
	private MagickFacade magick;

	@Override
	public void init() throws ServletException {
		super.init();
		magick = new MagickFacade();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream in = getInputStream(req);
		try {
			magick.convert(in, resp.getOutputStream(), "tmp",
					readParam(req, "format"), readParam(req, "size"),
					readParam(req, "crop"),
					Priority.nonNullValueOf(readParam(req, "priority")),
					readParam(req, "params1"), readParam(req, "params2"));
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			e.printStackTrace();
		} finally {
			resp.getOutputStream().close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if (ServletFileUpload.isMultipartContent(req)) {
				magick.convert(getUploadedFiles(req), resp.getOutputStream());
			} else {
				magick.convert(req.getInputStream(), resp.getOutputStream(),
						"tmp", readParam(req, "format"),
						readParam(req, "size"), readParam(req, "crop"),
						Priority.nonNullValueOf(readParam(req, "priority")),
						readParam(req, "params1"), readParam(req, "params2"));
			}
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			e.printStackTrace();
		} finally {
			resp.getOutputStream().close();
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	/**
	 * Read Multipart files from {@link HttpServletRequest}
	 * 
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws FileUploadException
	 */
	private List<FileItem> getUploadedFiles(HttpServletRequest req)
			throws IOException, FileUploadException {
		File repository = File.createTempFile("servlet", null).getParentFile();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(repository);
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		return fileUpload.parseRequest(req);
	}

	/**
	 * Read a parameter from the request
	 * 
	 * @param req
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String readParam(HttpServletRequest req, String name)
			throws UnsupportedEncodingException {
		String value = req.getParameter(name);
		if ("crop".equals(name) && value != null) {
			String notEncodedvalue = repareCropParam(value);
			if (!notEncodedvalue.equals(value))
				return notEncodedvalue;
		}
		return value == null ? "" : URLDecoder.decode(value, "UTF-8");
	}

	/**
	 * When the crop parameter is not encoded, the + are interpreted as a white
	 * space in the url
	 * 
	 * @param crop
	 * @return
	 */
	private String repareCropParam(String crop) {
		return crop.trim().replace(" ", "+");
	}

	/**
	 * Read the input in the request and return it as a stream
	 * 
	 * @param req
	 * @return
	 * @throws IOException
	 */
	private InputStream getInputStream(HttpServletRequest req)
			throws IOException {
		if (!readParam(req, "url").equals("")) {
			URL url = URI.create(req.getParameter("url")).toURL();
			return url.openConnection().getInputStream();
		} else if (!readParam(req, "inputFile").equals("")) {
			return new FileInputStream(new File(readParam(req, "inputFile")));
		} else
			throw new RuntimeException("No file to transform");
	}

}
