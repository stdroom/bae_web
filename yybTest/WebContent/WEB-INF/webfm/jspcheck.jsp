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
<title>JSP探针</title>
</head>

<body style="margin-left:50px;margin-right:50px;">
<center style="color:#FF6600;font-size:24px;font-weight:bold;">火狐JSP探针</center>
<a href="<%=request.getContextPath()%>">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="index.jsp">WEB文件管理器</a>
<hr>
<center>系统根目录</center>
<%
	File[] fs = File.listRoots();
	for(int i=0;i<fs.length;i++){
%>
<%=fs[i].getCanonicalPath()%>&nbsp;
<%
	}
%>
<hr>
<center>系统参数</center>
<%
	Properties pro = System.getProperties();
	Enumeration e = pro.propertyNames();
	while(e.hasMoreElements()){
		String name = (String)e.nextElement();
		String value = pro.getProperty(name);
%>
<font color="blue"><%=name%>&nbsp;〓&nbsp;</font><font color="red"><%=value%></font><br>
<%
	}
%>
<hr>
<center>环境变量</center>
<%
	Map env = System.getenv();
	Iterator it = env.keySet().iterator();
	while(it.hasNext()){
		String name = (String)it.next();
		String value= (String)env.get(name);
%>
<font color="blue"><%=name%>&nbsp;〓&nbsp;</font><font color="red"><%=value%></font><br>
<%
	}
%>
<hr>
<center>JVM内存</center>
内存总量：<%=Runtime.getRuntime().totalMemory()/1024/1024%>MB<br>
最大内存：<%=Runtime.getRuntime().maxMemory()/1024/1024%>MB<br>
空闲内存：<%=Runtime.getRuntime().freeMemory()/1024/1024%>MB<br>
</body>

</html>


