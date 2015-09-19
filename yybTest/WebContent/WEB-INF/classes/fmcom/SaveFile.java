package fmcom;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.codec.binary.*;
public class SaveFile extends HttpServlet{
  	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		String path = request.getParameter("path");
		String name = request.getParameter("name");

		String fpath = new String(Base64.decodeBase64(name.getBytes()));
		File file = new File(fpath);
		if(file.isFile()){
			response.setContentType("application/x-unknown;charset=GB2312");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(),"UTF8"));
			response.setContentLength((int)file.length());
			int i = 0;
			byte [] bt = new byte[8192];
			FileInputStream fis = new FileInputStream(file);
			ServletOutputStream sos = response.getOutputStream();
			while( (i = fis.read(bt)) != -1){
				sos.write(bt,0,i);
			}
			sos.flush();
			sos.close();
			sos = null;
			fis.close();
		}
	}
}