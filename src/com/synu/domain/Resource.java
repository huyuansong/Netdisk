package com.synu.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Resource implements Serializable{
	private int id;
	private String uuidname;  //�ϴ��ļ������ƣ��ļ���uuid��
	private String realname; //�ϴ��ļ�����ʵ����
	private String savepath;     //��ס�ļ���λ��
	private String ip;
	private Timestamp uploadtime;     //�ļ����ϴ�ʱ��
	private String description;  //�ļ�������
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUuidname() {
		return uuidname;
	}
	public void setUuidname(String uuidname) {
		this.uuidname = uuidname;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getSavepath() {
		return savepath;
	}
	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Timestamp getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(Timestamp uploadtime) {
		this.uploadtime = uploadtime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
