/**
 * ������: yybTest
 * �ļ���: Servlet.java
 * ����: org.yyb.test
 * ����: 2014-12-9����6:14:11
 * Copyright (c) 2014, ��������Ƽ����޹�˾ All Rights Reserved.
 * ������http://www.wjuxiang.com/
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
 * ����: Servlet <br/>
 * ����: TODO ��ӹ�������. <br/>
 * ����: 2014-12-9 ����6:14:11 <br/>
 *
 * @author   leixun
 * @email leixun33@163.com
 * @version  	 
 */
public class Servlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//�������ָ������ͻ��˷��͵����ݸ�ʽ�Ͳ��õ��ַ����룮 ����
		resp.setContentType("text/html;charset=GB2312");

		PrintWriter out = resp.getWriter();

		//����PrintWriter����ķ��������ݷ��͸��ͻ��� ����
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

