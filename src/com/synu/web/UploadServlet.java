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
			//1.�ϴ��ļ�
			String upload = this.getServletContext().getRealPath("WEB-INF/upload");
			String temp = this.getServletContext().getRealPath("WEB-INF/temp");
			Map pmap = new HashMap();
			pmap.put("ip", request.getRemoteAddr());
			
			//--�������������ڴ滺�����Ĵ�С����ʱ�ļ��е�λ��
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*100);
			factory.setRepository(new File(temp));
			
			//--��ȡ�ļ��ϴ�������,����ļ�������/�����ļ���С����
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setHeaderEncoding("utf-8");
			fileUpload.setFileSizeMax(1024*1024*100);
			fileUpload.setSizeMax(1024*1024*200);
			
			//--����Ƿ�����ȷ���ļ��ϴ���
			if(!fileUpload.isMultipartContent(request)){
				throw new RuntimeException("��ʹ����ȷ�ı������ϴ�!");
			}
			
			//--����request
			List<FileItem> list = fileUpload.parseRequest(request);
			
			//--����list,��ȡFileItem���н���
			for(FileItem item : list){
				if(item.isFormField()){//��ͨ�ֶ���
					String name = item.getFieldName();
					String value = item.getString("utf-8");
					pmap.put(name, value);
				}else{//�ļ��ϴ�
					//--uuidname��ֹ�ļ����ظ�
					String realname = item.getName();
					String uuidname = UUID.randomUUID().toString()+"_"+realname;
					pmap.put("realname", realname);
					pmap.put("uuidname", uuidname);
					
					//--��ȡ������
					InputStream in = item.getInputStream();
					
					//--��Ŀ¼�洢��ֹһ���ļ������ļ�����
					String hash = Integer.toHexString(uuidname.hashCode());
					String savepath = "/WEB-INF/upload";
					for(char c : hash.toCharArray()){
						upload+="/"+c;
						savepath+="/"+c;
					}
					new File(upload).mkdirs();
					pmap.put("savepath", savepath);
					//--��ȡ�����
					OutputStream out = new FileOutputStream(new File(upload,uuidname));
					
					//--���Խ��ϴ�
					IOUtils.In2Out(in, out);
					IOUtils.close(in, out);
					
					//--ɾ����ʱ�ļ�
					item.delete();
				}
				
			}
			
			//2.�����ݿ��в�������
			Resource r = new Resource();
			BeanUtils.populate(r, pmap);
			
			String sql ="insert into netdisk values(null,?,?,?,?,null,?)"; 
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			runner.update(sql,r.getUuidname(),r.getRealname(),r.getSavepath(),r.getIp(),r.getDescription());
		
			
			//3.�ض������ҳ
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
