<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css" >
<title>测试</title>
</head>

<body>
<%
	String ret = "";
	String[] cmd = { "/bin/sh", "-c", "ps -aef | grep java" };
	Process p = Runtime.getRuntime().exec(cmd);
	InputStream is = p.getInputStream();
	byte [] bt = new byte[1024];
	int i=0;
	while(i != -1){
		i = is.read(bt);
		if(i>0) ret += new String(bt,0,i);
	}
	is.close();
	int exitVal = p.waitFor() ;
	ret=ret.replace("\n","<BR>");
	ret=ret.replace(" ","&nbsp;");
%>
<%=exitVal%><br>
<%=ret%>

<%
	/*
	Console cmd = System.console();
	if(cmd != null){
		System.out.println("AAA");
		PrintWriter writer = cmd.writer();
		writer.println("dir");
		writer.flush();
		writer.close();
		int i = 0;
		StringBuffer ret = new StringBuffer();
		char [] ch = new char[8192];
		Reader reader = cmd.reader();
		while( (i=reader.read(ch))!=-1 ){
			ret.append(ch,0,i);
		}
		reader.close();
		System.out.println(ret);
	}*/
%>
</body>

</html>
