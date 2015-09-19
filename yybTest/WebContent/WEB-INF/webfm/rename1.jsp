<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>
<%request.setCharacterEncoding("GB2312");%>
<%
	String newfname = request.getParameter("newfname");
	String fname = request.getParameter("fname");
	fname = new String(Base64.decodeBase64(fname.getBytes()));
	File f = new File(fname);
	File nf = new File(f.getParentFile().getAbsolutePath()+System.getProperty("file.separator")+newfname);
	System.out.println(f.getAbsolutePath());
	System.out.println(nf.getAbsolutePath());
	f.renameTo(nf);

	response.sendRedirect("index.jsp?path="+new String(Base64.encodeBase64(f.getParentFile().getAbsolutePath().getBytes())));
%>