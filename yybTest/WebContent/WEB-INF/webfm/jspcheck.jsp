<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.naming.*" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css" >
<title>JSP̽��</title>
</head>

<body style="margin-left:50px;margin-right:50px;">
<center style="color:#FF6600;font-size:24px;font-weight:bold;">���JSP̽��</center>
<a href="<%=request.getContextPath()%>">��ҳ</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="index.jsp">WEB�ļ�������</a>
<hr>
<center>ϵͳ��Ŀ¼</center>
<%
	File[] fs = File.listRoots();
	for(int i=0;i<fs.length;i++){
%>
<%=fs[i].getCanonicalPath()%>&nbsp;
<%
	}
%>
<hr>
<center>ϵͳ����</center>
<%
	Properties pro = System.getProperties();
	Enumeration e = pro.propertyNames();
	while(e.hasMoreElements()){
		String name = (String)e.nextElement();
		String value = pro.getProperty(name);
%>
<font color="blue"><%=name%>&nbsp;��&nbsp;</font><font color="red"><%=value%></font><br>
<%
	}
%>
<hr>
<center>��������</center>
<%
	Map env = System.getenv();
	Iterator it = env.keySet().iterator();
	while(it.hasNext()){
		String name = (String)it.next();
		String value= (String)env.get(name);
%>
<font color="blue"><%=name%>&nbsp;��&nbsp;</font><font color="red"><%=value%></font><br>
<%
	}
%>
<hr>
<center>JVM�ڴ�</center>
�ڴ�������<%=Runtime.getRuntime().totalMemory()/1024/1024%>MB<br>
����ڴ棺<%=Runtime.getRuntime().maxMemory()/1024/1024%>MB<br>
�����ڴ棺<%=Runtime.getRuntime().freeMemory()/1024/1024%>MB<br>
</body>

</html>


