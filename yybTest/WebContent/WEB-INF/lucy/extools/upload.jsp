<%@ page language="java" contentType="text/html;charset=gbk"%>
<%@ page import="java.io.File,
java.util.*,
java.text.SimpleDateFormat,
com.jspsmart.upload.*,
org.apache.commons.fileupload.*,
java.net.URLEncoder;"
%>
<%!
/*** ���� ***/
private static final String UPLOAD_FILENAME = "upload.jsp";
//jspsmartupload
//�Ƿ�ʹ��ÿ���ļ��ϴ����size���� true=ʹ��, false=��ʹ��
private static final boolean JSM_USE_SINGLEMAXSIZEE = true;
//private static final boolean JSM_USE_SINGLEMAXSIZEE = false;
//����ÿ���ļ�����size,��λ���ֽ�
private static final long JSM_SINGLEMAXSIZE = 1048576;//1M=1024X1024=1048576
//commons-fileupload
//�Ƿ�ʹ���ܹ��ļ��ϴ����size���� true=ʹ��, false=��ʹ��
private static final boolean CFU_USE_TOTALMAXSIZE = true;
//�����ܹ��ļ�����size,��λ���ֽ�
private static final long CFU_TOTALMAXSIZE = 5*1024*1024;//
//----------------------------------------------------------------------------------
/*
pj_getDrivers()����������б�,�����ַ���(<a href="...">)
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
pj_unicodeToChinese(String s),��iso8859-1ת��Ϊgbk
����s:Ҫת�����ַ���(iso8859-1)
����:ת������ַ���(gbk)
����:(1),���sΪ��,����"",(2)���������ʱ,����sԭ��
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
pj_path2link(String path),��·��ת��Ϊ<a href="...">
����path:Ҫת����path�ַ���
����:ת������ַ���(<a href="...">)
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
pj_convertFileSize(long fileSize),��byteΪ��λ���ļ���Сת��windows���ļ���С��ʽ
����fileSize:Ҫת�����ļ���С
����:ת������ַ���,��10KB
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
response.setHeader("Expires","0");//��ֹ��proxy
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
/***·��***/
String path=request.getParameter("path");
String use=request.getParameter("use");
String del=request.getParameter("del");
String result="";
String sysError="";
if(path==null || path.length()<1){
path=request.getRealPath("/");
}
//����еõ�·���ͽ�path����,�������del����,���del��������·��
if(path!=null) path=pj_unicodeToChinese(path);
//���û�л��use
if(use==null && (request.getParameter("submit")!=null)){
result="<div style='background-color:#FFA2A2'>δָ���ϴ����</div>";
out.print(result);
}
/***�ϴ�***/
else if(use!=null && use.equals("su")){
String FileName=null;
if(!path.trim().endsWith(File.separator)) path=path+File.separator;
File ioFile=null;
try{
//�½�һ��SmartUpload����
SmartUpload su=new SmartUpload();
//�ϴ���ʼ��
su.initialize(pageContext);
//�趨�ϴ�����
//����ÿ���ϴ��ļ�����󳤶�(�ֽ�)
if(JSM_USE_SINGLEMAXSIZEE) su.setMaxFileSize(JSM_SINGLEMAXSIZE);
//�ϴ��ļ�
su.upload();
//����ܴ�СΪ0
if(su.getFiles().getSize()==0){
result="<div style='background-color:#FFA2A2'>�ϴ����ļ��ܴ�СΪ0�ֽ�</div>";
}else{
//ѭ����������ļ�
for (int i=0;i<su.getFiles().getCount();i++){
//getFiles,ȡȫ���ϴ��ļ�����Files������ʽ���أ���������Files��Ĳ�������������ϴ��ļ�����Ŀ����Ϣ�� 
com.jspsmart.upload.File myFile = su.getFiles().getFile(i);
if(myFile.isMissing()) continue;//���ļ���������,���¿�ʼһ��forѭ��,��ȡ��һ���ļ�
else{
//����ļ���(abc.zip)
FileName=myFile.getFileName();
//�ж��ļ��Ƿ����
ioFile=new File(path+FileName);
if(ioFile.exists()){
result +="<div style='background-color:#FFA2A2'>1���ļ�δ�ϴ�,["+path+FileName+"],�ļ��Ѵ���</div>";   
}else{
myFile.saveAs(path+FileName);
result +="<div style='background-color:#CFEAB7'>1���ļ����ϴ�,["+path+FileName+"],size:["+pj_convertFileSize(myFile.getSize())+"]</div>";
}
}
}//for�Ľ�β
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
//�����¶���
DiskFileUpload fu = new DiskFileUpload();
//��������ļ��ߴ磬������4MB
if(CFU_USE_TOTALMAXSIZE) fu.setSizeMax(CFU_TOTALMAXSIZE);
//���û�������С��������4kb
fu.setSizeThreshold(4096);
//������ʱĿ¼����ǰĿ¼
fu.setRepositoryPath(".");
//�õ����е��ļ���
List fileItems = fu.parseRequest(request);
Iterator i = fileItems.iterator();
//���δ���ÿһ���ļ���
while(i.hasNext()) {
FileItem fi = (FileItem)i.next();
//����ļ���������ļ�������·����
FileName=fi.getName();
//���û��filename,��û��ѡ���ļ�,����һ��ѭ��
if(FileName=="" || FileName==null) continue;
else{
FileName=FileName.substring(FileName.lastIndexOf(File.separator)+1);
//�ж��ļ��Ƿ����
ioFile=new File(path+FileName);
if(ioFile.exists()){
result +="<div style='background-color:#FFA2A2'>1���ļ�δ�ϴ�,["+path+FileName+"],�ļ��Ѵ���</div>";   
}else{
//д���ļ�
fi.write(new File(path+FileName));
result +="<div style='background-color:#CFEAB7'>1���ļ����ϴ�,["+path+FileName+"],size:["+pj_convertFileSize(fi.getSize())+"]</div>";
}
}
}//while�Ľ�β
}catch(Exception culULex) {
sysError="<div style='background-color:#FFA2A2'>Exception"+culULex.getMessage()+"</div>";
}
out.print(result+sysError);
}

/***ɾ��***/
else if(del!=null){
try{
del=pj_unicodeToChinese(del);
File f=new File(del);
path=f.getParent();
if(!f.delete()) result="<div style='background-color:#FFA2A2'>0���ļ���ɾ��.����ɾ��:["+del+"]</div>";
else result="<div style='background-color:#CFEAB7'>1���ļ���ɾ��:["+del+"]</div>";
}catch(Exception delFileex){
sysError="<div style='background-color:#FFA2A2'>IOException:"+delFileex.getMessage()+"</div>";
}
out.print(result+sysError);
}
/*** ��ʼ�����ļ��б� ***/
File objFile=new File(path);
%>
<script language="javascript">
var Button_Array = Array(
Array('Div_su'),
Array('Div_fu')
);
/***��������div***/
function disableAll(){
for (i = 0; i < Button_Array.length; i++){
document.getElementById(Button_Array[i]).style.display = 'none';}
}
/***��ʾĳһָ��div***/
function showDiv(cmd){
var nowStyle=document.getElementById(cmd).style.display;
if(nowStyle=='none'){
disableAll();
document.getElementById(cmd).style.display='block';
}else{
document.getElementById(cmd).style.display='none';}
}
</script>
������:<%=pj_getDrivers()%><br>
��ǰ·��:<%=pj_path2link(path)%><br>
<%
if(objFile.getParent()==null){out.print("<a href='#' disabled>�������Ŀ¼</a> |");}
else{out.print("<a href='"+UPLOAD_FILENAME+"?path="+URLEncoder.encode(objFile.getParent())+"'>��һ��Ŀ¼</a>  |");}
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d H:mm");
String bgColor="#eeeeee";
int a=0;
int b=0;
%>
<a href='#' onClick="history.go(-1);">����</a> |
<a href='#' onClick="history.go(1);">ǰ��</a> |
<a href="<%=UPLOAD_FILENAME%>?path=<%=URLEncoder.encode(path)%>">ˢ��</a> |
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
out.print("<tr><td colspan='4' bgcolor='#FFA2A2'>·���������������,���ܴ����ļ��б�!</td></tr></table>");
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
<td>�ļ���</td>
<td><%=formatter.format(fileList[i].lastModified())%></td>
<td>
<a href="<%=UPLOAD_FILENAME%>?del=<%=URLEncoder.encode(fileAbsolutePath)%>" onclick="return confirm('��ȷ��Ҫɾ��<%=fileList[i].getName()%>?�˲������ɻָ�');">
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
 onclick="return confirm('��ȷ��Ҫɾ��<%=fileList[i].getName()%>?�˲������ɻָ�');">
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
<b>ʹ��jspSmartUpload����ϴ��ļ�</b>(֧������)<br>
jspsmartupload����www.jspsmart.com��վ������һ�������ʹ�õ�ȫ���ܵ��ļ��ϴ��������<br>
�㽫�ϴ��ļ���:<font color=red><%=path%></font><br>
<%
if(JSM_USE_SINGLEMAXSIZEE) 
out.print("�������˵����ļ����size����<font color=red>"+pj_convertFileSize(JSM_SINGLEMAXSIZE)+"</font>,("+JSM_SINGLEMAXSIZE+" �ֽ�)");
else 
out.print("��û�����õ����ļ����size����</font>");
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
<b>ʹ��commons-fileupload����ϴ��ļ�</b>(֧������)<br>
common-fileupload�����apache�Ŀ�Դ���֮һ<br>
�㽫�ϴ��ļ���:<font color=red><%=path%></font><br>
<%
if(CFU_USE_TOTALMAXSIZE) 
out.print("���������ܹ��ļ����size����<font color=red>"+pj_convertFileSize(CFU_TOTALMAXSIZE)+"</font>,("+CFU_TOTALMAXSIZE+" �ֽ�)");
else 
out.print("��û�����õ����ļ����size����</font>");
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