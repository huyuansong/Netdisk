package com.synu.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.synu.domain.Resource;
import com.synu.util.DaoUtils;
import com.synu.util.IOUtils;

public class UploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			//1.上传文件
			String upload = this.getServletContext().getRealPath("WEB-INF/upload");
			String temp = this.getServletContext().getRealPath("WEB-INF/temp");
			Map pmap = new HashMap();
			pmap.put("ip", request.getRemoteAddr());
			
			//--创建工厂设置内存缓冲区的大小和临时文件夹的位置
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*100);
			factory.setRepository(new File(temp));
			
			//--获取文件上传核心类,解决文件名乱码/设置文件大小限制
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setHeaderEncoding("utf-8");
			fileUpload.setFileSizeMax(1024*1024*100);
			fileUpload.setSizeMax(1024*1024*200);
			
			//--检查是否是正确的文件上传表单
			if(!fileUpload.isMultipartContent(request)){
				throw new RuntimeException("请使用正确的表单进行上传!");
			}
			
			//--解析request
			List<FileItem> list = fileUpload.parseRequest(request);
			
			//--遍历list,获取FileItem进行解析
			for(FileItem item : list){
				if(item.isFormField()){//普通字段项
					String name = item.getFieldName();
					String value = item.getString("utf-8");
					pmap.put(name, value);
				}else{//文件上传
					//--uuidname防止文件名重复
					String realname = item.getName();
					String uuidname = UUID.randomUUID().toString()+"_"+realname;
					pmap.put("realname", realname);
					pmap.put("uuidname", uuidname);
					
					//--获取输入流
					InputStream in = item.getInputStream();
					
					//--分目录存储防止一个文件夹中文件过多
					String hash = Integer.toHexString(uuidname.hashCode());
					String savepath = "/WEB-INF/upload";
					for(char c : hash.toCharArray()){
						upload+="/"+c;
						savepath+="/"+c;
					}
					new File(upload).mkdirs();
					pmap.put("savepath", savepath);
					//--获取输出流
					OutputStream out = new FileOutputStream(new File(upload,uuidname));
					
					//--流对接上传
					IOUtils.In2Out(in, out);
					IOUtils.close(in, out);
					
					//--删除临时文件
					item.delete();
				}
				
			}
			
			//2.向数据库中插入数据
			Resource r = new Resource();
			BeanUtils.populate(r, pmap);
			
			String sql ="insert into netdisk values(null,?,?,?,?,null,?)"; 
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			runner.update(sql,r.getUuidname(),r.getRealname(),r.getSavepath(),r.getIp(),r.getDescription());
		
			
			//3.重定向回主页
			response.sendRedirect(request.getContextPath()+"/index.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
