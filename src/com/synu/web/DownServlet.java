package com.synu.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.synu.domain.Resource;
import com.synu.util.DaoUtils;
import com.synu.util.IOUtils;

public class DownServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		//1.��ȡҪ���ص���Դid
		String id = request.getParameter("id");
		//2.����id������Դ
		Resource r = null;
		try {
			String sql = "select * from netdisk where id = ?";
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			r = runner.query(sql, new BeanHandler<Resource>(Resource.class), id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(r==null){
			//3.�����Դ��������ʾ
			response.getWriter().write("�Ҳ�������Դ!!!");
			return;
		}else{
			//4.��������ṩ����
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(r.getRealname(),"UTF-8"));
			response.setContentType(this.getServletContext().getMimeType(r.getRealname()));
			
			String filePath = this.getServletContext().getRealPath(r.getSavepath()+"/"+r.getUuidname());
			InputStream in = new FileInputStream(filePath);
			OutputStream out = response.getOutputStream();
			
			IOUtils.In2Out(in, out);
			IOUtils.close(in, null);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
