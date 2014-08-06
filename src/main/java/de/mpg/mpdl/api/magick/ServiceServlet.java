package de.mpg.mpdl.api.magick;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServiceServlet extends HttpServlet {

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
		try {
			URL url = URI.create(req.getParameter("url")).toURL();
			URLConnection conn = url.openConnection();
			magick.convert(conn.getInputStream(), resp.getOutputStream(),
					url.toString(), req.getParameter("format"),
					req.getParameter("size"), req.getParameter("params"));
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			magick.convert(req.getInputStream(), resp.getOutputStream(), "tmp",
					req.getParameter("format"), req.getParameter("size"),
					req.getParameter("params"));
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
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

}
