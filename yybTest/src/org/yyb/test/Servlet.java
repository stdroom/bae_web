/**
 * 工程名: yybTest
 * 文件名: Servlet.java
 * 包名: org.yyb.test
 * 日期: 2014-12-9下午6:14:11
 * Copyright (c) 2014, 北京巨翔科技有限公司 All Rights Reserved.
 * 官网：http://www.wjuxiang.com/
 *
*/

package org.yyb.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类名: Servlet <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2014-12-9 下午6:14:11 <br/>
 *
 * @author   leixun
 * @email leixun33@163.com
 * @version  	 
 */
public class Servlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//这条语句指明了向客户端发送的内容格式和采用的字符编码． 　　
		resp.setContentType("text/html;charset=GB2312");

		PrintWriter out = resp.getWriter();

		//利用PrintWriter对象的方法将数据发送给客户端 　　
		out.println("how are you");
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}

