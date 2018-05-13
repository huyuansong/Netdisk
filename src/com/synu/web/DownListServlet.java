package com.synu.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.synu.domain.Resource;
import com.synu.util.DaoUtils;

public class DownListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.�������ݿ��ҳ����еĿɹ����ص���Դ��Ϣ
		String sql = "select * from netdisk";
		QueryRunner runner = new QueryRunner(DaoUtils.getSource());
		List<Resource> list = null;
		try {
			list = runner.query(sql, new BeanListHandler<Resource>(Resource.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2.����request���д���jspҳ�����չʾ
		request.setAttribute("list", list);
		request.getRequestDispatcher("/downlist.jsp").forward(request, response);
	}
	

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
