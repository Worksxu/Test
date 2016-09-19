package com.ruiqi.bean;

import java.io.Serializable;

public class Nopay implements Serializable{
	
	private String kehu_type;
	private String kid;
	private String total;
	private String username;
	public Nopay(String kehu_type, String kid, String total, String username) {
		super();
		this.kehu_type = kehu_type;
		this.kid = kid;
		this.total = total;
		this.username = username;
	}
	public Nopay() {
		super();
	}
	public String getKehu_type() {
		return kehu_type;
	}
	public void setKehu_type(String kehu_type) {
		this.kehu_type = kehu_type;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

}
