<%@ page contentType="text/html;charset=gbk"%>
<%@ page language="java" %>
<% request.setCharacterEncoding("gbk");%>
<%!
/*-----------------------
 Lucy Login2
 ʹ��session+cookie��֤,cookie����1Сʱ
 ��������:Lucy 1.0
 PJ produce 2008.4
 email:isafile@gmail.com 
-----------------------*/
private static final String LUCY_FILENAME="index.jsp";
private static final String LUCY_LOGIN="login2.jsp";
private static final String[][] UsnAndPsd={{"test","test"},{"onlypj","123456"}};
%>
<% 
request.setCharacterEncoding("gbk");
response.setHeader("Pragma","No-cache");//HTTP 1.1
response.setHeader("Cache-Control","no-cache");//HTTP 1.0
response.setHeader("Expires","0");//��ֹ��proxy
StringBuffer logininfo=new StringBuffer();
//����Ѿ���session,��ʲôҲ����,
//ע��include file = "login.jsp"����˼�ǽ������ļ��ϲ���һ���ļ�
//ʲôҲ�������Ǽ���ִ��������ļ�
if((session.getAttribute("name"))!=null){
String s="<div id='login'><span id='loginsuccess'>&nbsp;�ѵ�½:"+session.getAttribute("name")+"&nbsp;</span>|&nbsp;"
+"<a href='#' onclick='clearCookie()'>�˳�</a></div>";
out.print(s);
%>
<script language="javascript"> 
function clearCookie(){ 
�� var strCookie = document.cookie;
�� var arrCookie = strCookie.split(";");
�� var i;
�� var expires = new Date(); 
�� expires.setDate(expires.getDate() - 1); 
�� for(i=0; i<arrCookie.length; i++){
�� �� document.cookie = arrCookie[i].split('=')[0]+'=null;expires=' + expires.toGMTString(); 
�� }
   alert('log out successfully!');
   window.location.href="<%=LUCY_FILENAME%>";
}
</script>
<%
}else{
//out.print("û��session,���cookie...<br>");
logininfo.append("û��session,���cookie...<br>");
boolean cookie_right=false;
//1.���cookie
if(request.getCookies()!=null){
Cookie theCookies[]=request.getCookies(); 
//out.print("������cookie<br>");
logininfo.append("������cookie<br>");
//���cookie
String cookie_value=null;
//���cookie��ֵ
for(int i=0;i<theCookies.length;i++ ){
if(theCookies[i].getName().equals("name")){
cookie_value=theCookies[i].getValue();
//out.print("�Ѿ����cookie��ֵ:"+cookie_value+"<br>");
logininfo.append("�Ѿ����cookie��ֵ:"+cookie_value+"<br>");
break;
}
}
//
if(cookie_value!=null){
//���cookie
for(int i=0;i<UsnAndPsd.length;i++){
if(cookie_value.equals(UsnAndPsd[i][0])){
cookie_right=true;
cookie_value=UsnAndPsd[i][0];
break;//����ѭ��
}
}
}
if(cookie_right){
session.setAttribute("name",cookie_value);
//�����right�ͼ���ִ��lucy�Ĵ���,
//����û���ҵ��ĸ�������������,����ֻ���������
response.sendRedirect(LUCY_FILENAME);
}else{
//out.print("���cookie��ֵ����ȷ<br>");
logininfo.append("���cookie��ֵ����ȷ:"+cookie_value+"<br>");
}
//�����cookie��û��
}else{
//out.print("û��cookie<br>");
logininfo.append("û��cookie<br>");
}
%>
<html>
<head>
<title>Lucy Login2</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<style type="text/css">
<!--
html,body {font-size:12px;font-family:Verdana, sans-serif;}
hr{border-top:1px solid #aaaaaa;border-bottom:0px solid #aaaaaa;height: 0px;}
div{font-size:12px;font-family:Verdana, sans-serif;color:#333333;}
form{margin:0px;display: inline;}
#outer{width:450px;}
#header{margin-left:25px;}
#header h1{font-size:2em;font-weight:bold;margin-left:0px;margin-bottom:2px;color:#333333;}
#loginresult{margin-left:50px;margin-top:25px;color:#FF0000;}
#loginbox{margin-left:50px;margin-top:15px;margin-bottom:25px;width:320px;}
#loginbox label {display:block;height:2.5em;width:100%;}
#usn,#psd{float:right;width:225px;font-size:12px;font-family:Verdana, sans-serif;}
#loginsave{float:right;}
#lb{float:right;}
#footer{margin-left:25px;}
#footer p{margin:2px;}
#logininfolist{position:absolute;left:500px;top:50px;color:#a1a1a1;}
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
String al=request.getParameter("al");
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
//out.print("�û���������ȷ,���浽session<br>");
logininfo.append("�û���������ȷ,���浽session<br>");

//���浽cookie
if(al!=null && al.equals("yes")){
//out.print("��ѡ���˽�����Ϣ���浽cookie<br>");
logininfo.append("��ѡ���˽�����Ϣ���浽cookie<br>");
Cookie myCookie=new Cookie("name",name);
myCookie.setMaxAge(60*60); //����һСʱ
response.addCookie(myCookie);
//out.print("�ѱ��浽cookie<br>");
logininfo.append("�ѱ��浽cookie<br>");
}
//�����right�ͼ���ִ��lucy�Ĵ���,
//����û���ҵ��ĸ�������������,����ֻ���������
response.sendRedirect(LUCY_FILENAME);
}
//������ǵ�һ�δ�
else if(!right && usn!=null){script="<script language=\"javascript\">wrong();</script>";logininfo.append("�û������벻��ȷ<br>");}
%>
<div id="outer">
<div id="header"><h1>Lucy Login2</h1><hr width="400px" align="left"></div>
<div id="loginresult">&nbsp;</div><form name="login" method="POST" action="<%=LUCY_LOGIN%>">
<div id="loginbox">
<label><input type="text" size="25" name="usn" id="usn" /> Username:</label>
<label><input type="text" size="25" name="psd" id="psd" /> Password:</label>
<label><span id="loginsave"><input type="checkbox" name="al" value="yes"> Remember me</span></label>
<label><input type="submit" name="submit" value="Login" id="lb" onClick="return check();" /></label>
</div></form><div id="footer" ><hr width="400px" align="left">
<p>PJ produce 2008.1.16 &nbsp;&nbsp;email:isafile@gmail.com</p>
</div></div>
<%
out.println(script);
out.print("<div id='logininfolist'>"+logininfo.toString()+"</div>");
%>
</body></html>
<%
return;//��һ�����Ҫ,ִֹͣ���������
//����include������login.jsp��lucy�ϲ���һ���ļ�,���㿴�����������½��,������lucy
}
%>