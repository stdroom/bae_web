package org.yyb.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public class DownloadServlet extends HttpServlet{
	private String contentType = "application/x-msdownload";
    private String enc = "utf-8";
    private String fileRoot = "";
    private String webPath = "";

    /**
     * ��ʼ��contentType��enc��fileRoot
     */
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	init();
        String tempStr = config.getInitParameter("contentType");
        if (tempStr != null && !tempStr.equals("")) {
            contentType = tempStr;
        }
        tempStr = config.getInitParameter("enc");
        if (tempStr != null && !tempStr.equals("")) {
            enc = tempStr;
        }
        tempStr = config.getInitParameter("fileRoot");
        if (tempStr != null && !tempStr.equals("")) {
            fileRoot = tempStr;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filepath = request.getParameter("file");
        System.out.println(fileRoot);
        
        String fullFilePath =  webPath +"WEB-INF/"+ filepath;
        System.out.println(fullFilePath);
        System.out.println(new Date());
        /*��ȡ�ļ�*/
        File file = new File(fullFilePath);
        
        /*����ļ�����*/
        if (file.exists()) {
            String filename = URLEncoder.encode(file.getName(), enc);
            response.reset();
            response.setContentType(contentType);
            response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            int fileLength = (int) file.length();
            response.setContentLength(fileLength);
            /*����ļ����ȴ���0*/
            if (fileLength != 0) {
                /*����������*/
                InputStream inStream = new FileInputStream(file);
                byte[] buf = new byte[4096];
                /*���������*/
                ServletOutputStream servletOS = response.getOutputStream();
                int readLength;
                while (((readLength = inStream.read(buf)) != -1)) {
                    servletOS.write(buf, 0, readLength);
                }
                inStream.close();
                servletOS.flush();
                servletOS.close();
            }
        }
    }
    
	/** 
	 * ͨ����������ȡ����·�� 
	 *  
	 * @return 
	 * @throws Exception 
	 */  
	private String getAbsolutePathByContext() throws Exception {  
	    String webPath = this.getServletContext().getRealPath("/");  
	  
	    webPath = webPath.replaceAll("[\\\\\\/]WEB-INF[\\\\\\/]classes[\\\\\\/]?", "/");  
	    webPath = webPath.replaceAll("[\\\\\\/]+", "/");  
	    webPath = webPath.replaceAll("%20", " ");  
	  
	    if (webPath.matches("^[a-zA-Z]:.*?$")) {  
	  
	    } else {  
	        webPath = "/" + webPath;  
	    }  
	  
	    webPath += "/";  
	    webPath = webPath.replaceAll("[\\\\\\/]+", "/");  
	    return webPath;  
	  
	}  
	  
	/** 
	 * ͨ����·����ȡ����·�� 
	 *  
	 * @return 
	 * @throws Exception 
	 */  
	private String getAbsolutePathByClass() throws Exception {  
	    webPath = this.getClass().getResource("/").getPath().replaceAll("^\\/", "");  
	    webPath = webPath.replaceAll("[\\\\\\/]WEB-INF[\\\\\\/]classes[\\\\\\/]?", "/");  
	    webPath = webPath.replaceAll("[\\\\\\/]+", "/");  
	    webPath = webPath.replaceAll("%20", " ");  
	  
	    if (webPath.matches("^[a-zA-Z]:.*?$")) {  
	  
	    } else {  
	        webPath = "/" + webPath;  
	    }  
	  
	    webPath += "/";  
	    webPath = webPath.replaceAll("[\\\\\\/]+", "/");  
	  
	    return webPath;  
	}  
	private String getAbsolutePathByResource() throws Exception {  
	    URL url = this.getServletContext().getResource("/");  
	    String path = new File(url.toURI()).getAbsolutePath();  
	          if (!path.endsWith("\\") && !path.endsWith("/")) {  
	        path += File.separator;  
	    }  
	    return path;  
	}  
	  
	public void init() throws ServletException {  
	    String webPath = null;  
	    try {  
	        webPath = getAbsolutePathByContext();  
	    } catch (Exception e) {  
	    }  
	  
	    // ��weblogic 11g �Ͽ����޷���������ȡ����������·�������Ը�Ϊ�����  
	    if (webPath == null) {  
	        try {  
	            webPath = getAbsolutePathByClass();  
	        } catch (Exception e) {  
	        }  
	    }  
	  
	    if (webPath == null) {  
	        try {  
	            webPath = getAbsolutePathByResource();  
	        } catch (Exception e) {  
	        }  
	    }  
	  
	    System.out.println(webPath);  
	}  
}
