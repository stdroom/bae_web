package org.yyb.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userName = req.getParameter("usn");
		String password = req.getParameter("psd");
		if (userName.equals("leixun")
				&& password.equals("leixun")){
			
		} else {
			
		}
	}

	
}
