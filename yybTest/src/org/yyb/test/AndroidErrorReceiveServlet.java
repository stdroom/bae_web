/**
 * 工程名: yybTest
 * 文件名: AndroidErrorReceiveServlet.java
 * 包名: org.yyb.test
 * 日期: 2014-12-11下午3:57:41
 * Copyright (c) 2014, 北京巨翔科技有限公司 All Rights Reserved.
 * 官网：http://www.wjuxiang.com/
 *
*/

package org.yyb.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;

/**
 * 类名: AndroidErrorReceiveServlet <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2014-12-11 下午3:57:41 <br/>
 *
 * @author   leixun
 * @email leixun33@163.com
 * @version  	 
 */
public class AndroidErrorReceiveServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println(req.getParameterValues("userName"));
		System.out.println(req.getParameter("userName"));
		System.out.println(req.getParameterMap().get("userName"));
		try {
		if(ServletFileUpload.isMultipartContent(req)){
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
	        List items = null;
				items = upload.parseRequest(req);
			
	        for (int i = 0; i < items.size(); i++) {
	            FileItem item = (FileItem) items.get(i);
	            if (!item.isFormField()) {
	            	item.getName();
	            	System.out.println("item:"+item.getName());
	            	String filename = "\\YybLog\\"+item.getName();
	            	File file = new File(filename);
	            	item.write(file);
	            } else {
	                //普通表单数据
//	            	System.out.println(item.getString("type"));
	            }
	        }
	        PrintWriter pw = resp.getWriter();
	        HashMap<String,Object> respR = new HashMap<String,Object>();
	        respR.put("status", "1");
	        respR.put("results","success");
	        pw.print(JSONObject.toJSON(respR));
	        pw.close();
		}else{
			
		}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		}
		

	
}

