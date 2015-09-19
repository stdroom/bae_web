<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>
<%request.setCharacterEncoding("GB2312");%>
<%
	String path = request.getParameter("path");
	String fdir = path;
	String dirname = request.getParameter("dirname");
	if(fdir == null) {
		fdir = application.getRealPath("/");
	}else{
		fdir = new String(Base64.decodeBase64(fdir.getBytes()));
	}
	fdir += System.getProperty("file.separator");
	fdir += dirname;
	File f = new File(fdir);
	if(!f.exists()) f.mkdir();
	response.sendRedirect("index.jsp?path="+path);
%>