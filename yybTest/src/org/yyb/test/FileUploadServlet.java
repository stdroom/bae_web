package org.yyb.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.yyb.utils.ConstantGloble;
import org.yyb.utils.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FileUploadServlet extends HttpServlet{
	String tag1 = "";
	String tag2 = "";
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HashMap param_hm = new HashMap();
        try {
                // jsp file encode utf-8 must
                request.setCharacterEncoding("utf-8");
                RequestContext requestContext = new ServletRequestContext(request);
                if (FileUpload.isMultipartContent(requestContext)) {
                        DiskFileItemFactory factory = new DiskFileItemFactory();
                        File temp_file = new File(ConstantGloble.webPath+"WEB-INF/");
                        temp_file.mkdir();
                        factory.setRepository(temp_file);
                        ServletFileUpload upload = new ServletFileUpload(factory);
                        upload.setHeaderEncoding("utf-8");
                        upload.setSizeMax(-1);
                        List items = new ArrayList();
                        items = upload.parseRequest(request);
                        Iterator it = items.iterator();
                        while (it.hasNext()) {
                                FileItem fileItem = (FileItem) it.next();
                                if (fileItem.isFormField()) {
                                        param_hm.put(fileItem.getFieldName(), new String(
                                                        fileItem.getString().getBytes("ISO-8859-1"),
                                                        "utf-8"));
                                } else {
                                        if (fileItem.getName() != null
                                                        && fileItem.getSize() != 0) {
                                                File fullFile = new File(fileItem.getName());
                                                File newFile = new File(ConstantGloble.webPath
                                                                + "WEB-INF/"+fullFile.getName());
                                                if (!newFile.exists()){
                                                	File tempFile = new File(ConstantGloble.webPath+ "WEB-INF/");
                                                	tempFile.mkdirs();
                                                	tag1 = "不存在";
                                                }else {
                                                	tag1 = "存在";
                                                }
                                                fileItem.write(newFile);
                                                tag2 = "��";
                                        } else {
                                                System.out.println("文件shit");
                                        }
                                }
                        }
                }
                String versionCode = (String)param_hm.get("versionCode");
                String versionLog = (String)param_hm.get("versionLog");
                String dowloadUrl = (String)param_hm.get("downloadUrl");
                UpdateBean bean  = new UpdateBean();
                if(dowloadUrl!=null && !"".equals(dowloadUrl)){
                	bean.setDownloadUrl(dowloadUrl);
                }else{
                	bean.setDownloadUrl("http://www.pgyer.com/enjoyread");
                }
                bean.setUpdateLog(versionLog);
                bean.setVersionCode(versionCode);
                bean.setVersionName("android");
                if (!"".equals(versionCode)){
                	ResultsBean results = new ResultsBean();
                	results.results = bean;
	                FileUtils.saveFileCache(JSON.toJSONString(results).getBytes("utf-8"),
	                		ConstantGloble.webPath+ "WEB-INF/", "versionCode.txt");
                }
        } catch (Exception e2) {
        	response.setContentType("text/html;charset=utf-8");
    		PrintWriter out = response.getWriter();
    		
    		out.println(tag1);
    		out.println(tag2);
    		out.println(ConstantGloble.webPath);
    		out.println(e2);
    		out.println(e2.getStackTrace());
    		out.close();
                e2.printStackTrace();
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
	    ConstantGloble.webPath = this.getClass().getResource("/").getPath().replaceAll("^\\/", "");  
	    ConstantGloble.webPath = ConstantGloble.webPath.replaceAll("[\\\\\\/]WEB-INF[\\\\\\/]classes[\\\\\\/]?", "/");  
	    ConstantGloble.webPath = ConstantGloble.webPath.replaceAll("[\\\\\\/]+", "/");  
	    ConstantGloble.webPath = ConstantGloble.webPath.replaceAll("%20", " ");  
	  
	    if (ConstantGloble.webPath.matches("^[a-zA-Z]:.*?$")) {  
	  
	    } else {  
	    	ConstantGloble.webPath = "/" + ConstantGloble.webPath;  
	    }  
	  
	    ConstantGloble.webPath += "/";  
	    ConstantGloble.webPath = ConstantGloble.webPath.replaceAll("[\\\\\\/]+", "/");  
	  
	    return ConstantGloble.webPath;  
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
