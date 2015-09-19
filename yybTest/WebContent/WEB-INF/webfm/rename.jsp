<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css" >
<title>WEB文件管理器</title>
</head>
<BODY>
<%request.setCharacterEncoding("GB2312");%>
<%
	String fname = request.getParameter("fname");
	fname = new String(Base64.decodeBase64(fname.getBytes()));
	File f = new File(fname);
%>
<table border="0" width="100%" height="100%">
  <tr>
	<td width="35%">&nbsp;</td>
    <td width="30%">
修改文件夹或者文件的名称<br>
<form method="POST" action="rename1.jsp">
原名称：<%=fname%><br>
<input type="hidden" name="fname" size="20" value="<%=new String(Base64.encodeBase64(fname.getBytes()))%>"><br>
新名称：<input type="text" name="newfname" size="20" value="<%=f.getName()%>"><br>
<input type="submit" value="OK" name="submit">
</form>
    </td>
    <td width="35%">&nbsp;</td>
  </tr>
</table>
</BODY>
</html>