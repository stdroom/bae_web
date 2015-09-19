<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>
<%request.setCharacterEncoding("GB2312");%>
<%
	String ps = System.getProperty("file.separator");
	String path = request.getParameter("path");
	String fdir = new String(Base64.decodeBase64(path.getBytes()));
	String name = request.getParameter("name");
	String content = request.getParameter("content");
	name = name.trim();
	if(!name.equals("")){
		FileOutputStream fos = new FileOutputStream(fdir+ps+name);
		fos.write(content.getBytes());
		fos.close();

	}

	response.sendRedirect("index.jsp?path="+path);
%>