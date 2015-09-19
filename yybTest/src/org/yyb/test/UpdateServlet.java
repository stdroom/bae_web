package org.yyb.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yyb.utils.ConstantGloble;
import org.yyb.utils.FileUtils;


import com.alibaba.fastjson.JSON;

public class UpdateServlet extends HttpServlet{
	int i = 0;
	UpdateBean updateBean = new UpdateBean();
	public void init(ServletConfig config) throws ServletException {
		String tempStr = config.getInitParameter("versionCode");
        if (tempStr != null && !tempStr.equals("")) {
            updateBean.setVersionCode(tempStr);
        }
        tempStr = config.getInitParameter("versionName");
        if (tempStr != null && !tempStr.equals("")) {
            updateBean.setVersionName(tempStr);
        }
        tempStr = config.getInitParameter("downloadUrl");
        if (tempStr != null && !tempStr.equals("")) {
        	updateBean.setDownloadUrl(tempStr);
        }
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		System.out.println(req.getHeader("m_os_version"));
		System.out.println(req.getHeader("m_hd_type"));
		System.out.println(req.getHeader("m_client_type"));
		System.out.println(req.getHeader("m_hd_type"));
		System.out.println(req.getHeader("lang"));
		PrintWriter out = resp.getWriter();

		//利用PrintWriter对象的方法将数据发送给客户端 　　
		ResultsBean results = new ResultsBean();
		String versionCode = FileUtils.readFile(ConstantGloble.webPath+ "WEB-INF/versionCode.txt");
		if(!"".equals(
				versionCode)&& !"null".equals(versionCode)){
			out.println(versionCode);
			out.close();
		}else{
			results.results = updateBean;
			out.println(JSON.toJSONString(results));
			out.close();
		}
	}
	
	
	
}
