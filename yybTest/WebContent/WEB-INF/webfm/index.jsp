<%@ page contentType="text/html; charset=gbk" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css" >
<title>WEB�ļ�������</title>
</head>
<BODY>
<%
	InitialContext ctx = new InitialContext();
	String rootPath = request.getParameter("rootPath");
	String path = request.getParameter("path");
	if(rootPath == null) rootPath = "";
	rootPath = rootPath.trim();
	if(rootPath.equals("")){
		rootPath = application.getRealPath("/");
	}

	if(path == null) {
		path = rootPath;
	}else{
		path = new String(Base64.decodeBase64(path.getBytes()));
	}
	File fpath = new File(path);
%>
<hr>
<center style="color:#FF6600;font-size:18px;font-weight:bolder;">���WEB�ļ�������</center>
<center><a href="<%=request.getContextPath()%>">��ҳ</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="jspcheck.jsp">JSP̽��</a></center>
<hr>
<table border="0" width="800"  cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td style="border-right:2px solid black;" valign="top">
<form method="POST" action="<%=request.getRequestURI()%>">
ת��Ŀ¼��<input type="text" name="rootPath" size="20"><input type="submit" value="ȷ��" name="submit">
</form>
<table border="0" width="400" align="center" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%">
<u><b><font color="#FF6600">��ǰ·����<%=fpath.getAbsolutePath()%></font></b></u><br>
<%
	if(fpath.getParentFile() != null){
%>
<a href="<%=request.getRequestURI()%>?path=<%=new String(Base64.encodeBase64(fpath.getParentFile().getAbsolutePath().getBytes()))%>"><img border="0" src="<%=request.getContextPath()%>/img/parentdir.gif"><font color="#3399FF"><b>[&nbsp;�ϼ�Ŀ¼&nbsp;]</b></font></a>
<%
	}else{
		File[] fs = File.listRoots();
		for(int i=0;i<fs.length;i++){

%>
[<a href="<%=request.getRequestURI()%>?path=<%=new String(Base64.encodeBase64(fs[i].getAbsolutePath().getBytes()))%>"><%=fs[i].getAbsolutePath()%></a>]&nbsp;&nbsp;
<%
		}
	}
%>
<form method="POST" action="del.jsp">

<table border="0" width="100%" cellspacing="0" cellpadding="0">
<%
	File f = new File(path);
	if(f.isDirectory()){
		File[] fs	= f.listFiles();
		for(int i=0;i<fs.length;i++){
%>
  <tr>
    <td>
    <input type="checkbox" name="fname" value="<%=new String(Base64.encodeBase64(fs[i].getAbsolutePath().getBytes()))%>">
    &nbsp;<a href="rename.jsp?fname=<%=new String(Base64.encodeBase64(fs[i].getAbsolutePath().getBytes()))%>">������</a>&nbsp;
    <%if(fs[i].isDirectory()){%><a href="<%=request.getContextPath()%>/<%=request.getRequestURI()%>?path=<%=new String(Base64.encodeBase64(fs[i].getAbsolutePath().getBytes()))%>"><IMG border="0" align="ABSMIDDLE" src="<%=request.getContextPath()%>/img/folder.gif"></a><%}%>
    <%if(fs[i].isFile()){%><a target="_blank" href="<%=request.getContextPath()%>/file/save.jsp?path=<%=new String(Base64.encodeBase64(fpath.getAbsolutePath().getBytes()))%>&name=<%=new String(Base64.encodeBase64(fs[i].getAbsolutePath().getBytes()))%>"><IMG border="0" align="ABSMIDDLE" src="<%=request.getContextPath()%>/img/file.gif"></a><%}%>
    <%=fs[i].getName()%>
    </td>
  </tr>
<%
		}
	}
%>
</table>
<input type="hidden" name="path" size="20" value="<%=new String(Base64.encodeBase64(path.getBytes()))%>">
<center><input type="submit" value="ɾ��" name="submit"></center>
ע�⣺Ŀ¼Ϊ�գ�����ɾ������Ŀ¼���ļ���ɾ�����������棬һ��ɾ�����ɻָ�����
</form>
    </td>
  </tr>
</table>

    </td>
    <td valign="top">

<table border="0" width="100%" align="left">
  <tr>
    <td width="100%"><b>������Ŀ¼</b></td>
  </tr>
  <tr>
    <td width="100%">
<form method="POST" action="mkdir.jsp">
<input type="hidden" name="path" size="20" value="<%=new String(Base64.encodeBase64(path.getBytes()))%>">
  Ŀ¼���ƣ�<input type="text" name="dirname" size="16"><input type="submit" value="����" name="submit">
</form>
    </td>
  </tr>
</table><br>
<table border="0" width="100%" align="left" style="border-top:1px dashed black;">
  <tr>
    <td width="100%"><b>���ر����ļ�</b></td>
  </tr>
  <tr>
    <td width="100%">
<form method="POST" action="upload.jsp?path=<%=new String(Base64.encodeBase64(path.getBytes()))%>" ENCTYPE="multipart/form-data">
  ѡ���ļ���<input type="file" name="filename" size="16"><input type="submit" value="����" name="submit">
</form>
    </td>
  </tr>
</table>

<form method="POST" action="writetext.jsp?path=<%=new String(Base64.encodeBase64(path.getBytes()))%>">
  <table border="0"  width="400" align="left" style="border-top:1px dashed black;">
  <tr>
    <td width="400" colspan="2" align="left"><b>д�ı��ļ�</b></td>
  </tr>
    <tr>
      <td width="65" valign="top" align="left">�ļ����ƣ�</td>
      <td width="335" valign="top" align="left"><input type="text" name="name" size="20"><input type="submit" value="�ύ" name="submit"></td>
    </tr>
    <tr>
      <td width="65" valign="top" align="left">�ļ����ݣ�</td>
      <td width="335" valign="top" align="left"><textarea rows="6" name="content" cols="30"></textarea></td>
    </tr>
  </table>
</form>
<center><img align="absmiddle" src="<%=request.getContextPath()%>/img/logo.gif"></center>
    </td>
  </tr>
</table>
<center><a href="http://www.163jsp.com"><SPAN  style="color:#FF6600;font-size:16px;font-weight:bolder;">���������Ƽ����޹�˾ �ṩרҵJSP��������</SPAN></a></center>
</BODY>
</html>


