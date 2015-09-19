<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>
<%request.setCharacterEncoding("GB2312");%>
<%
	String path = request.getParameter("path");
	String fdir = new String(Base64.decodeBase64(path.getBytes()));
	if(FileUpload.isMultipartContent(request)){
		DiskFileUpload fu = new DiskFileUpload();
		List items = fu.parseRequest(request);
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (!item.isFormField() && item.getSize()>0){

				File tempFile = new File(item.getName());
				String fileName = tempFile.getName();
				item.write(new File(fdir+System.getProperty("file.separator")+fileName));
			}
		}
	}
	response.sendRedirect("index.jsp?path="+path);
%>