<%@ page contentType="text/html;charset=gbk"%>
<%@ page language="java" %>
<% request.setCharacterEncoding("gbk");%>
<%!
/*-----------------------
 Lucy Login2
 使用session+cookie验证,cookie保存1小时
 可以用于:Lucy 1.0
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
response.setHeader("Expires","0");//防止被proxy
StringBuffer logininfo=new StringBuffer();
//如果已经有session,就什么也不做,
//注意include file = "login.jsp"的意思是将两个文件合并成一个文件
//什么也不做就是继续执行下面的文件
if((session.getAttribute("name"))!=null){
String s="<div id='login'><span id='loginsuccess'>&nbsp;已登陆:"+session.getAttribute("name")+"&nbsp;</span>|&nbsp;"
+"<a href='#' onclick='clearCookie()'>退出</a></div>";
out.print(s);
%>
<script language="javascript"> 
function clearCookie(){ 
　 var strCookie = document.cookie;
　 var arrCookie = strCookie.split(";");
　 var i;
　 var expires = new Date(); 
　 expires.setDate(expires.getDate() - 1); 
　 for(i=0; i<arrCookie.length; i++){
　 　 document.cookie = arrCookie[i].split('=')[0]+'=null;expires=' + expires.toGMTString(); 
　 }
   alert('log out successfully!');
   window.location.href="<%=LUCY_FILENAME%>";
}
</script>
<%
}else{
//out.print("没有session,检查cookie...<br>");
logininfo.append("没有session,检查cookie...<br>");
boolean cookie_right=false;
//1.检查cookie
if(request.getCookies()!=null){
Cookie theCookies[]=request.getCookies(); 
//out.print("发现有cookie<br>");
logininfo.append("发现有cookie<br>");
//获得cookie
String cookie_value=null;
//获得cookie的值
for(int i=0;i<theCookies.length;i++ ){
if(theCookies[i].getName().equals("name")){
cookie_value=theCookies[i].getValue();
//out.print("已经获得cookie的值:"+cookie_value+"<br>");
logininfo.append("已经获得cookie的值:"+cookie_value+"<br>");
break;
}
}
//
if(cookie_value!=null){
//检查cookie
for(int i=0;i<UsnAndPsd.length;i++){
if(cookie_value.equals(UsnAndPsd[i][0])){
cookie_right=true;
cookie_value=UsnAndPsd[i][0];
break;//跳出循环
}
}
}
if(cookie_right){
session.setAttribute("name",cookie_value);
//如果有right就继续执行lucy的代码,
//不过没有找到哪个语句可以这样做,所以只能用这个了
response.sendRedirect(LUCY_FILENAME);
}else{
//out.print("检查cookie的值不正确<br>");
logininfo.append("检查cookie的值不正确:"+cookie_value+"<br>");
}
//如果连cookie都没有
}else{
//out.print("没有cookie<br>");
logininfo.append("没有cookie<br>");
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
//循环读取数组
for(int i=0;i<UsnAndPsd.length;i++){
if(usn.equals(UsnAndPsd[i][0]) && psd.equals(UsnAndPsd[i][1])){
	right=true;
	name=UsnAndPsd[i][0];
	break;//跳出循环
  }
} //for的结尾 
}
if(right){
session.setAttribute("name",name);
//out.print("用户名密码正确,保存到session<br>");
logininfo.append("用户名密码正确,保存到session<br>");

//保存到cookie
if(al!=null && al.equals("yes")){
//out.print("你选择了将来信息保存到cookie<br>");
logininfo.append("你选择了将来信息保存到cookie<br>");
Cookie myCookie=new Cookie("name",name);
myCookie.setMaxAge(60*60); //保存一小时
response.addCookie(myCookie);
//out.print("已保存到cookie<br>");
logininfo.append("已保存到cookie<br>");
}
//如果有right就继续执行lucy的代码,
//不过没有找到哪个语句可以这样做,所以只能用这个了
response.sendRedirect(LUCY_FILENAME);
}
//如果不是第一次打开
else if(!right && usn!=null){script="<script language=\"javascript\">wrong();</script>";logininfo.append("用户名密码不正确<br>");}
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
return;//这一句很重要,停止执行下面代码
//否则include将来把login.jsp和lucy合并成一个文件,那你看到的是上面登陆框,下面是lucy
}
%>