package com.ruiqi.bean;

import java.io.Serializable;

public class NopayDetail implements Serializable {
	
	private String ctime;
	private String money;
	private String order_sn;
	private String time_list;
	private String username;
	public NopayDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NopayDetail(String ctime, String money, String order_sn,
			String time_list, String username) {
		super();
		this.ctime = ctime;
		this.money = money;
		this.order_sn = order_sn;
		this.time_list = time_list;
		this.username = username;
	}
	public NopayDetail(String time_list, String money, String order_sn,
			 String username) {
		super();
		this.time_list = time_list;
		this.money = money;
		this.order_sn = order_sn;
		this.username = username;
	}
	public NopayDetail(String time_list, String money, String order_sn) {
		super();
		this.time_list = time_list;
		this.money = money;
		this.order_sn = order_sn;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getTime_list() {
		return time_list;
	}
	public void setTime_list(String time_list) {
		this.time_list = time_list;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
