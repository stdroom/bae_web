<%@ page contentType="text/html;charset=gbk"%>
<%@ page language="java" %>
<% request.setCharacterEncoding("gbk");%>
<%!
/*-----------------------
 Lucy Login
 ʹ��session��֤,Ҫʹ������ļ�,����Ҫ��Lucy��include����ļ�
 session�����ڴ���(�����)�رյ�ʱ��ʧЧ
 ��������:Lucy 1.0
 PJ produce 2008.4
 email:isafile@gmail.com 
-----------------------*/
private static final String LUCY_FILENAME="index.jsp";
private static final String LUCY_LOGIN="login.jsp";
private static final String[][] UsnAndPsd={{"test","test"},{"onlypj","123456"}};
%>
<% 
request.setCharacterEncoding("gbk");
response.setHeader("Pragma","No-cache");//HTTP 1.1
response.setHeader("Cache-Control","no-cache");//HTTP 1.0
response.setHeader("Expires","0");//��ֹ��proxy
//����Ѿ���session,��ʲôҲ����,
//ע��include file = "login.jsp"����˼�ǽ������ļ��ϲ���һ���ļ�
//ʲôҲ�������Ǽ���ִ��������ļ�
if((session.getAttribute("name"))!=null){
out.print("<div id='login'>&nbsp;&nbsp;�ѵ�½:"+session.getAttribute("name")+"&nbsp;&nbsp;</div>");
}else{
%>
<html>
<head>
<title>Lucy Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<style type="text/css">
<!--
html,body {font-size:12px;font-family:Verdana, sans-serif;}
hr{border-top:1px solid #aaaaaa;border-bottom:0px solid #aaaaaa;height: 0px;}
div{font-size:12px;font-family:Verdana, sans-serif;color:#333333;}
form{margin:0px;display: inline;}
#outer{width:100%;}
#header{margin-left:25px;}
#header h1{font-size:2em;font-weight:bold;margin-left:0px;margin-bottom:2px;color:#333333;}
#loginresult{margin-left:50px;margin-top:25px;color:#FF0000;}
#loginbox{margin-left:50px;margin-top:15px;margin-bottom:25px;width:320px;}
#loginbox label {display:block;height:2.5em;width:100%;}
#usn,#psd{float:right;width:225px;}
#lb{float:right;}
#footer{margin-left:25px;}
#footer p{margin:2px;}
-->
</style>
<script language="javascript">
var _d=document;
function $id(id){return _d.getElementById(id);}
function check(){
var usn=$id("usn").value;
var psd=$id("psd").value;
$id("loginresult").innerHTML="";
if(!usn || usn.length<1 ){$id("loginresult").innerHTML="Please enter Username";return false;}
else if(!psd || psd.length<1 ){$id("loginresult").innerHTML="Please enter Password";return false;}
else{return true;}
}
function wrong(){$id("loginresult").innerHTML="Invalid Username or Password";}
</script>
</head><body>
<%
String usn=request.getParameter("usn");
String psd=request.getParameter("psd");
boolean right=false;
String name=null;
String script="";
if(usn!=null && psd!=null){
//ѭ����ȡ����
for(int i=0;i<UsnAndPsd.length;i++){
if(usn.equals(UsnAndPsd[i][0]) && psd.equals(UsnAndPsd[i][1])){
	right=true;
	name=UsnAndPsd[i][0];
	break;//����ѭ��
  }
} //for�Ľ�β 
}
if(right){
session.setAttribute("name",name);
//�����right�ͼ���ִ��lucy�Ĵ���,
//����û���ҵ��ĸ�������������,����ֻ���������
response.sendRedirect(LUCY_FILENAME);
}
//������ǵ�һ�δ�
else if(!right && usn!=null){script="<script language=\"javascript\">wrong();</script>";}
%>
<div id="outer">
<div id="header"><h1>Lucy Login</h1><hr width="400px" align="left"></div>
<div id="loginresult">&nbsp;</div><form name="login" method="POST" action="<%=LUCY_LOGIN%>">
<div id="loginbox">
<label><input type="text" size="25" name="usn" id="usn" /> Username:</label>
<label><input type="text" size="25" name="psd" id="psd" /> Password:</label>
<label><input type="submit" name="submit" value="Login" id="lb" onClick="return check();" /></label>
</div></form><div id="footer" ><hr width="400px" align="left">
<p>PJ produce 2008.1.16 &nbsp;&nbsp; email:isafile@gmail.com</p>
</div></div></body></html>
<%
out.print(script);
return;//��һ�����Ҫ,ִֹͣ���������
//����include������login.jsp��lucy�ϲ���һ���ļ�,���㿴�����������½��,������lucy
}
%>