<%@ page language="java" contentType="text/html;charset=gbk"%>
<html>
<head>
<title>System Information</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<style type="text/css">
<!--
body{margin:0px;font-size:12px;font-family:"Verdana","Arial","Helvetica","sans-serif";letter-spacing:1px}
#header{background-color:#767676;color:#ffffff;padding-left:8px;padding-top:7px;padding-bottom:5px;font-size:16pt;font-family:Verdana;font-weight:bold;}
#about{position:absolute;right:8px;bottom:6px;color:#999999;font-size:8pt;}
-->
</style>
<script language="javascript">
function browser(){
var htmldata="";
htmldata +="<li>浏览器名称:"+navigator.appName+"</li>";
htmldata +="<li>浏览器版本号:"+navigator.appVersion+"</li>";
htmldata +="<li>运行平台:"+navigator.platform+"</li>";
htmldata +="<li>是否支持cookie:"+navigator.cookieEnabled+"</li>"; 
document.write(htmldata);
}
</script>
</head>
<body>
<div id="header">System Information</div>
<ul>
<li><B>服务器</B></li>
<ul>
<li>服务器名称:<%=request.getServerName()%></li>
<li>服务器端口号:<%=request.getServerPort()%></li>
<li>服务器软件名称:<%=getServletContext().getServerInfo()%></li>
</ul><br>
<li><B>客户端</B></li>
<ul>
<li>客户端的主机名:<%=request.getRemoteHost()%></li>
<li>客户端的IP地址:<%=request.getRemoteAddr()%></li>
<li>客户端的端口:<%=request.getRemotePort()%></li>
</ul><br>
<li><B>从http请求头获取的浏览器信息</B></li>
<ul>
<li>支持的MIME类型:<%=request.getHeader("Accept")%></li>
<li>支持的编码方式:<%=request.getHeader("Accept-Encoding")%></li>
<li>支持的语言种类:<%=request.getHeader("Accept-Language")%></li>
<li>支持的连接方式:<%=request.getHeader("Connection")%></li>
<li>Cookie:<%=request.getHeader("Cookie")%></li>
<li>http请求的目标主机:<%=request.getHeader("Host")%></li>
<li>If-Modified-Since(标示仅当页面在指定的日期之后被改动过时客户才希望获得该页面):
<%=request.getHeader("If-Modified-Since")%></li>
<li>引用页面的URL(Referer):<%=request.getHeader("Referer")%></li>
<li>浏览器类型(User-Agent):<%=request.getHeader("User-Agent")%></li>
</ul><br>
<li><B>从javascript获取的浏览器信息</B></li>
<ul>
<script language="javascript">browser();</script>
</ul>
</ul>
<div id="about">System information jsp page - version 1.0 - PJ produce 2008.4.30</div>
</body></html>