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
htmldata +="<li>���������:"+navigator.appName+"</li>";
htmldata +="<li>������汾��:"+navigator.appVersion+"</li>";
htmldata +="<li>����ƽ̨:"+navigator.platform+"</li>";
htmldata +="<li>�Ƿ�֧��cookie:"+navigator.cookieEnabled+"</li>"; 
document.write(htmldata);
}
</script>
</head>
<body>
<div id="header">System Information</div>
<ul>
<li><B>������</B></li>
<ul>
<li>����������:<%=request.getServerName()%></li>
<li>�������˿ں�:<%=request.getServerPort()%></li>
<li>�������������:<%=getServletContext().getServerInfo()%></li>
</ul><br>
<li><B>�ͻ���</B></li>
<ul>
<li>�ͻ��˵�������:<%=request.getRemoteHost()%></li>
<li>�ͻ��˵�IP��ַ:<%=request.getRemoteAddr()%></li>
<li>�ͻ��˵Ķ˿�:<%=request.getRemotePort()%></li>
</ul><br>
<li><B>��http����ͷ��ȡ���������Ϣ</B></li>
<ul>
<li>֧�ֵ�MIME����:<%=request.getHeader("Accept")%></li>
<li>֧�ֵı��뷽ʽ:<%=request.getHeader("Accept-Encoding")%></li>
<li>֧�ֵ���������:<%=request.getHeader("Accept-Language")%></li>
<li>֧�ֵ����ӷ�ʽ:<%=request.getHeader("Connection")%></li>
<li>Cookie:<%=request.getHeader("Cookie")%></li>
<li>http�����Ŀ������:<%=request.getHeader("Host")%></li>
<li>If-Modified-Since(��ʾ����ҳ����ָ��������֮�󱻸Ķ���ʱ�ͻ���ϣ����ø�ҳ��):
<%=request.getHeader("If-Modified-Since")%></li>
<li>����ҳ���URL(Referer):<%=request.getHeader("Referer")%></li>
<li>���������(User-Agent):<%=request.getHeader("User-Agent")%></li>
</ul><br>
<li><B>��javascript��ȡ���������Ϣ</B></li>
<ul>
<script language="javascript">browser();</script>
</ul>
</ul>
<div id="about">System information jsp page - version 1.0 - PJ produce 2008.4.30</div>
</body></html>