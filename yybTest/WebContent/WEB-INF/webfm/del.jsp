<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>
<%request.setCharacterEncoding("GB2312");%>
<%
	String path = request.getParameter("path");
	String [] fname = request.getParameterValues("fname");
	if(fname != null){
		for(int i=0;i<fname.length;i++){
			fname[i] = new String(Base64.decodeBase64(fname[i].getBytes()));

				File f = new File(fname[i]);
				if(f.exists()) f.delete();


		}
	}
	response.sendRedirect("index.jsp?path="+path);
%>