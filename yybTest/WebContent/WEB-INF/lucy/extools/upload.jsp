<%@ page language="java" contentType="text/html;charset=gbk"%>
<%@ page import="java.io.File,
java.util.*,
java.text.SimpleDateFormat,
com.jspsmart.upload.*,
org.apache.commons.fileupload.*,
java.net.URLEncoder;"
%>
<%!
/*** 设置 ***/
private static final String UPLOAD_FILENAME = "upload.jsp";
//jspsmartupload
//是否使用每个文件上传最大size限制 true=使用, false=不使用
private static final boolean JSM_USE_SINGLEMAXSIZEE = true;
//private static final boolean JSM_USE_SINGLEMAXSIZEE = false;
//设置每个文件最大的size,单位是字节
private static final long JSM_SINGLEMAXSIZE = 1048576;//1M=1024X1024=1048576
//commons-fileupload
//是否使用总共文件上传最大size限制 true=使用, false=不使用
private static final boolean CFU_USE_TOTALMAXSIZE = true;
//设置总共文件最大的size,单位是字节
private static final long CFU_TOTALMAXSIZE = 5*1024*1024;//
//----------------------------------------------------------------------------------
/*
pj_getDrivers()获得驱动器列表,返回字符串(<a href="...">)
PJ 2008.2
*/
private static String pj_getDrivers(){
	StringBuffer driversSB=new StringBuffer();
	File roots[]=File.listRoots();
	for(int i=0;i<roots.length;i++){
	driversSB.append("<a href='"+UPLOAD_FILENAME+"?path="+URLEncoder.encode(roots[i].toString())+"'>"+roots[i]+"</a>&nbsp;");
	}
	return driversSB.toString();
}
/*
pj_unicodeToChinese(String s),将iso8859-1转换为gbk
参数s:要转换的字符串(iso8859-1)
返回:转换后的字符串(gbk)
错误:(1),如果s为空,返回"",(2)当捕获错误时,返回s原样
PJ 2008.2
*/
private static String pj_unicodeToChinese(String s){
	try{
		if(s==null||s.equals("")) return "";
		String newstring=new String(s.getBytes("ISO8859_1"),"GBK");
		return newstring;
	}catch(Exception e){return s;}
}
/*
pj_path2link(String path),将路径转换为<a href="...">
参数path:要转换的path字符串
返回:转换后的字符串(<a href="...">)
PJ 2008.2
*/
private static String pj_path2link(String path) {
	File f = new File(path);
	StringBuffer buf = new StringBuffer();
	String encPath=null;
	if(!f.canRead()){
		return path;
	}else{
		while (f.getParentFile() != null) {
		encPath = URLEncoder.encode(f.getAbsolutePath());
		buf.insert(0,"<a href=\""+UPLOAD_FILENAME+"?path="+encPath+"\">"+f.getName()+File.separator+"</a>");
		f = f.getParentFile();
	}
		encPath = URLEncoder.encode(f.getAbsolutePath());
		buf.insert(0,"<a href=\""+UPLOAD_FILENAME+"?path="+encPath+"\">"+f.getAbsolutePath()+"</a>");
	}
	return buf.toString();
}
/*
pj_convertFileSize(long fileSize),将byte为单位的文件大小转换windows的文件大小格式
参数fileSize:要转换的文件大小
返回:转换后的字符串,如10KB
PJ 2008.2
*/
private static String pj_convertFileSize(long fileSize) {
	String fileSizeOut=null;//size
	if(fileSize<1024){
		fileSizeOut=fileSize+"byte";
	}else if(fileSize>=(1024*1024*1024)){
		fileSize=fileSize/(1024*1024*1024);
		fileSizeOut=fileSize +"GB";
	}else if(fileSize>=(1024*1024)){
		fileSize=fileSize/(1024*1024);
		fileSizeOut=fileSize +"MB";
	}else if(fileSize>=1024){
		fileSize=fileSize/1024;
		fileSizeOut=fileSize+"KB";
	}
	return fileSizeOut;
}
%>
<% 
request.setCharacterEncoding("gbk");
response.setHeader("Pragma","No-cache");//HTTP 1.1
response.setHeader("Cache-Control","no-cache");//HTTP 1.0
response.setHeader("Expires","0");//防止被proxy
%>
<html>
<head>
<title>upload</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<style type="text/css">
<!--
body{margin:6px;font-size:8pt;font-family:"Verdana","Arial","Helvetica","sans-serif";letter-spacing:1px}
#title{margin-bottom:0px;font-size:10pt;font-family:Verdana;font-weight:bold;color:#666666}
hr{border-top:1px solid #aaaaaa;border-bottom:0px solid #aaaaaa;height: 0px;}
td{font-size:8pt;font-family:"Verdana","Arial","Helvetica","sans-serif";letter-spacing:1px;padding-left:2px;}
form{margin:0px;display: inline;}
input{border:1px solid gray;font-size:8pt;font-family:"Verdana";}
.button{background-color: #cecece; color: #666666;border: 1px solid #999999;}
a:link{text-decoration:none;color: #0033FF;}
a:visited{text-decoration:none;color: #0033FF;}
a:hover{text-decoration:underline;color: #800000;}
a:active{text-decoration:none;}
-->
</style>
</head>
<body>
<span id="title">Upload tools</span>
<hr>
<%
/***路径***/
String path=request.getParameter("path");
String use=request.getParameter("use");
String del=request.getParameter("del");
String result="";
String sysError="";
if(path==null || path.length()<1){
path=request.getRealPath("/");
}
//如果有得到路径就将path解码,如果接收del参数,则从del参数制作路径
if(path!=null) path=pj_unicodeToChinese(path);
//如果没有获得use
if(use==null && (request.getParameter("submit")!=null)){
result="<div style='background-color:#FFA2A2'>未指定上传组件</div>";
out.print(result);
}
/***上传***/
else if(use!=null && use.equals("su")){
String FileName=null;
if(!path.trim().endsWith(File.separator)) path=path+File.separator;
File ioFile=null;
try{
//新建一个SmartUpload对象
SmartUpload su=new SmartUpload();
//上传初始化
su.initialize(pageContext);
//设定上传限制
//限制每个上传文件的最大长度(字节)
if(JSM_USE_SINGLEMAXSIZEE) su.setMaxFileSize(JSM_SINGLEMAXSIZE);
//上传文件
su.upload();
//如果总大小为0
if(su.getFiles().getSize()==0){
result="<div style='background-color:#FFA2A2'>上传的文件总大小为0字节</div>";
}else{
//循环获得所有文件
for (int i=0;i<su.getFiles().getCount();i++){
//getFiles,取全部上传文件，以Files对象形式返回，可以利用Files类的操作方法来获得上传文件的数目等信息。 
com.jspsmart.upload.File myFile = su.getFiles().getFile(i);
if(myFile.isMissing()) continue;//若文件不存在则,重新开始一次for循环,获取下一个文件
else{
//获得文件名(abc.zip)
FileName=myFile.getFileName();
//判断文件是否存在
ioFile=new File(path+FileName);
if(ioFile.exists()){
result +="<div style='background-color:#FFA2A2'>1个文件未上传,["+path+FileName+"],文件已存在</div>";   
}else{
myFile.saveAs(path+FileName);
result +="<div style='background-color:#CFEAB7'>1个文件已上传,["+path+FileName+"],size:["+pj_convertFileSize(myFile.getSize())+"]</div>";
}
}
}//for的结尾
}
}catch(Exception jsmULex){
sysError="<div style='background-color:#FFA2A2'>Exception"+jsmULex.getMessage()+"</div>";
}
out.print(result+sysError);
}

//commons-fileupload
else if(use!=null && use.equals("fu")){
String FileName=null;
if(!path.trim().endsWith(File.separator)) path=path+File.separator;
File ioFile=null;
try{
//创建新对象
DiskFileUpload fu = new DiskFileUpload();
//设置最大文件尺寸，这里是4MB
if(CFU_USE_TOTALMAXSIZE) fu.setSizeMax(CFU_TOTALMAXSIZE);
//设置缓冲区大小，这里是4kb
fu.setSizeThreshold(4096);
//设置临时目录：当前目录
fu.setRepositoryPath(".");
//得到所有的文件：
List fileItems = fu.parseRequest(request);
Iterator i = fileItems.iterator();
//依次处理每一个文件：
while(i.hasNext()) {
FileItem fi = (FileItem)i.next();
//获得文件名，这个文件名包括路径：
FileName=fi.getName();
//如果没有filename,即没有选定文件,重新一次循环
if(FileName=="" || FileName==null) continue;
else{
FileName=FileName.substring(FileName.lastIndexOf(File.separator)+1);
//判断文件是否存在
ioFile=new File(path+FileName);
if(ioFile.exists()){
result +="<div style='background-color:#FFA2A2'>1个文件未上传,["+path+FileName+"],文件已存在</div>";   
}else{
//写入文件
fi.write(new File(path+FileName));
result +="<div style='background-color:#CFEAB7'>1个文件已上传,["+path+FileName+"],size:["+pj_convertFileSize(fi.getSize())+"]</div>";
}
}
}//while的结尾
}catch(Exception culULex) {
sysError="<div style='background-color:#FFA2A2'>Exception"+culULex.getMessage()+"</div>";
}
out.print(result+sysError);
}

/***删除***/
else if(del!=null){
try{
del=pj_unicodeToChinese(del);
File f=new File(del);
path=f.getParent();
if(!f.delete()) result="<div style='background-color:#FFA2A2'>0个文件已删除.不能删除:["+del+"]</div>";
else result="<div style='background-color:#CFEAB7'>1个文件已删除:["+del+"]</div>";
}catch(Exception delFileex){
sysError="<div style='background-color:#FFA2A2'>IOException:"+delFileex.getMessage()+"</div>";
}
out.print(result+sysError);
}
/*** 开始创建文件列表 ***/
File objFile=new File(path);
%>
<script language="javascript">
var Button_Array = Array(
Array('Div_su'),
Array('Div_fu')
);
/***隐藏所有div***/
function disableAll(){
for (i = 0; i < Button_Array.length; i++){
document.getElementById(Button_Array[i]).style.display = 'none';}
}
/***显示某一指定div***/
function showDiv(cmd){
var nowStyle=document.getElementById(cmd).style.display;
if(nowStyle=='none'){
disableAll();
document.getElementById(cmd).style.display='block';
}else{
document.getElementById(cmd).style.display='none';}
}
</script>
驱动器:<%=pj_getDrivers()%><br>
当前路径:<%=pj_path2link(path)%><br>
<%
if(objFile.getParent()==null){out.print("<a href='#' disabled>已是最顶层目录</a> |");}
else{out.print("<a href='"+UPLOAD_FILENAME+"?path="+URLEncoder.encode(objFile.getParent())+"'>上一级目录</a>  |");}
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d H:mm");
String bgColor="#eeeeee";
int a=0;
int b=0;
%>
<a href='#' onClick="history.go(-1);">后退</a> |
<a href='#' onClick="history.go(1);">前进</a> |
<a href="<%=UPLOAD_FILENAME%>?path=<%=URLEncoder.encode(path)%>">刷新</a> |
<a href="<%=UPLOAD_FILENAME%>">Home</a>
<div style="background-color:#DEDEDE">
<table width="100%" cellpadding="2" cellspacing="1">
<thead>
<tr bgcolor="#CCCCCC">
<td width="60%">Name:</td>
<td width="12%">Size:</td>
<td width="21%">Last modified:</td>
<td width="7%">Delete:</td>
</tr>
</thead>
<%
File fileList[]=objFile.listFiles();
if(fileList==null){
out.print("<tr><td colspan='4' bgcolor='#FFA2A2'>路径错误或其他错误,不能创建文件列表!</td></tr></table>");
}else{
String fileAbsolutePath=null;
for(int i=0;i<fileList.length;i++){
if(fileList[i].isDirectory()){
bgColor=bgColor.equals("#eeeeee") ? "#ffffff" : "#eeeeee";
a++;
fileAbsolutePath=fileList[i].getAbsolutePath();
%>
<tr bgcolor="<%=bgColor%>"><td>
<a href="<%=UPLOAD_FILENAME%>?path=<%=URLEncoder.encode(fileAbsolutePath)%>">[<%=fileList[i].getName()%>]</a>
</td>
<td>文件夹</td>
<td><%=formatter.format(fileList[i].lastModified())%></td>
<td>
<a href="<%=UPLOAD_FILENAME%>?del=<%=URLEncoder.encode(fileAbsolutePath)%>" onclick="return confirm('你确定要删除<%=fileList[i].getName()%>?此操作不可恢复');">
delete
</a>
</td>
</tr>
<%
}//end if
}//end for
for(int i=0;i<fileList.length;i++){
if(fileList[i].isFile()){ 
bgColor=bgColor.equals("#eeeeee") ? "#ffffff" : "#eeeeee";
b++;
fileAbsolutePath=fileList[i].getAbsolutePath();
%>
<tr bgcolor="<%=bgColor%>"><td>
<a href="#">
<%=fileList[i].getName()%></a>
</td>
<td><%=pj_convertFileSize(fileList[i].length())%></td>
<td><%=formatter.format(fileList[i].lastModified())%></td>
<td>
<a href="<%=UPLOAD_FILENAME%>?del=<%=URLEncoder.encode(fileAbsolutePath)%>"
 onclick="return confirm('你确定要删除<%=fileList[i].getName()%>?此操作不可恢复');">
delete
</a>
</td>
</tr>
<%
}//end if
}//end for
%>
</table>
</div>
<table width="100%" cellspacing="0" cellpadding="2">
<thead>
<tr><td bgcolor="#CCCCCC">
Total: [folder(s)]:<%=a%>, file(s):<%=b%>
</td></tr>
</thead>
</table>
<%
}
%>

<br>
<input type="button" class="button" onclick="javascript:showDiv('Div_su');" value='jspSmartUpload'>
<input type="button" class="button" onclick="javascript:showDiv('Div_fu');" value='commons-fileupload'>
<!---jspSmartUpload--->
<div id="Div_su" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>使用jspSmartUpload组件上传文件</b>(支持中文)<br>
jspsmartupload是由www.jspsmart.com网站开发的一个可免费使用的全功能的文件上传下载组件<br>
你将上传文件到:<font color=red><%=path%></font><br>
<%
if(JSM_USE_SINGLEMAXSIZEE) 
out.print("你设置了单个文件最大size限制<font color=red>"+pj_convertFileSize(JSM_SINGLEMAXSIZE)+"</font>,("+JSM_SINGLEMAXSIZE+" 字节)");
else 
out.print("你没有设置单个文件最大size限制</font>");
%>
<br>
<form method="POST" action="<%=UPLOAD_FILENAME%>?use=su&path=<%=URLEncoder.encode(path)%>" enctype="multipart/form-data">
<input type="file" name="su1" size="50"><br>
<input type="file" name="su2" size="50"><br>
<input type="file" name="su3" size="50"><br>
<input type="file" name="su4" size="50"><br>
<input type="file" name="su5" size="50"><br>
<input type="submit" value="Upload">
</form>
</div>
<!------>
<!---commons-fileupload--->
<div id="Div_fu" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>使用commons-fileupload组件上传文件</b>(支持中文)<br>
common-fileupload组件是apache的开源组件之一<br>
你将上传文件到:<font color=red><%=path%></font><br>
<%
if(CFU_USE_TOTALMAXSIZE) 
out.print("你设置了总共文件最大size限制<font color=red>"+pj_convertFileSize(CFU_TOTALMAXSIZE)+"</font>,("+CFU_TOTALMAXSIZE+" 字节)");
else 
out.print("你没有设置单个文件最大size限制</font>");
%>
<br>
<form method="POST" action="<%=UPLOAD_FILENAME%>?use=fu&path=<%=URLEncoder.encode(path)%>" enctype="multipart/form-data">
<input type="file" name="fu1" size="50"><br>
<input type="file" name="fu2" size="50"><br>
<input type="file" name="fu3" size="50"><br>
<input type="file" name="fu4" size="50"><br>
<input type="file" name="fu5" size="50"><br>
<input type="submit" value="Upload">
</form>
</div>
<!------>
</body>
</html>