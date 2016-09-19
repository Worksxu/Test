package com.ruiqi.chai;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChaiSafeInfo implements Serializable {
	
	private String admin_user_id;
	private String admin_user_name;
	private String comment;
	private String id;
	private String listorder;
	private String reportsn;
	private String status;
	private String title;
	private String type;
	public String getAdmin_user_id() {
		return admin_user_id;
	}
	public void setAdmin_user_id(String admin_user_id) {
		this.admin_user_id = admin_user_id;
	}
	public String getAdmin_user_name() {
		return admin_user_name;
	}
	public void setAdmin_user_name(String admin_user_name) {
		this.admin_user_name = admin_user_name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getListorder() {
		return listorder;
	}
	public void setListorder(String listorder) {
		this.listorder = listorder;
	}
	public String getReportsn() {
		return reportsn;
	}
	public void setReportsn(String reportsn) {
		this.reportsn = reportsn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
