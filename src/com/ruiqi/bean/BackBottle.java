package com.ruiqi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 退瓶订单的详情类
 * @author Administrator
 *
 */
public class BackBottle implements Serializable{
	private String depositsn;
	
	private String money;
	private String doormoney;
	private String productmoney;
	private String shouldmoney;
	private String username;
	private String time;
	private String mobile;
	
	private String id;
	private String address;
	
	private String status;
	private String kid;
	
	private String status_name;
	
	private List<Bottle> list;
	public List<Bottle> getList() {
		return list;
	}
	public void setList(List<Bottle> list) {
		this.list = list;
	}
	public BackBottle(String depositsn, String money, String doormoney,
			String productmoney, String shouldmoney, String username,
			String time, String mobile, String id, String address,
			String status, String kid, String status_name, List<Bottle> list) {
		super();
		this.depositsn = depositsn;
		this.money = money;
		this.doormoney = doormoney;
		this.productmoney = productmoney;
		this.shouldmoney = shouldmoney;
		this.username = username;
		this.time = time;
		this.mobile = mobile;
		this.id = id;
		this.address = address;
		this.status = status;
		this.kid = kid;
		this.status_name = status_name;
		this.list = list;
	}
	public String getStatus_name() {
		return status_name;
	}
	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}
	public BackBottle(String depositsn, String money, String doormoney,
			String productmoney, String shouldmoney, String username,
			String time, String mobile, String id, String address,
			String status, String kid, String status_name) {
		super();
		this.depositsn = depositsn;
		this.money = money;
		this.doormoney = doormoney;
		this.productmoney = productmoney;
		this.shouldmoney = shouldmoney;
		this.username = username;
		this.time = time;
		this.mobile = mobile;
		this.id = id;
		this.address = address;
		this.status = status;
		this.kid = kid;
		this.status_name = status_name;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public BackBottle(String depositsn, String money, String doormoney,
			String productmoney, String shouldmoney, String username,
			String time, String mobile, String id, String address,
			String status, String kid) {
		super();
		this.depositsn = depositsn;
		this.money = money;
		this.doormoney = doormoney;
		this.productmoney = productmoney;
		this.shouldmoney = shouldmoney;
		this.username = username;
		this.time = time;
		this.mobile = mobile;
		this.id = id;
		this.address = address;
		this.status = status;
		this.kid = kid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BackBottle(String depositsn, String money, String doormoney,
			String productmoney, String shouldmoney, String username,
			String time, String mobile, String id, String address, String status) {
		super();
		this.depositsn = depositsn;
		this.money = money;
		this.doormoney = doormoney;
		this.productmoney = productmoney;
		this.shouldmoney = shouldmoney;
		this.username = username;
		this.time = time;
		this.mobile = mobile;
		this.id = id;
		this.address = address;
		this.status = status;
	}
	@Override
	public String toString() {
		return "BackBottle [depositsn=" + depositsn + ", money=" + money
				+ ", doormoney=" + doormoney + ", productmoney=" + productmoney
				+ ", shouldmoney=" + shouldmoney + ", username=" + username
				+ ", time=" + time + ", mobile=" + mobile + ", id=" + id
				+ ", address=" + address + ", status=" + status + ", kid="
				+ kid + ", status_name=" + status_name + ", list=" + list + "]";
	}
	public String getDepositsn() {
		return depositsn;
	}
	public void setDepositsn(String depositsn) {
		this.depositsn = depositsn;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDoormoney() {
		return doormoney;
	}
	public void setDoormoney(String doormoney) {
		this.doormoney = doormoney;
	}
	public String getProductmoney() {
		return productmoney;
	}
	public void setProductmoney(String productmoney) {
		this.productmoney = productmoney;
	}
	public String getShouldmoney() {
		return shouldmoney;
	}
	public void setShouldmoney(String shouldmoney) {
		this.shouldmoney = shouldmoney;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BackBottle() {
		super();
	}
	public BackBottle(String depositsn, String money, String doormoney,
			String productmoney, String shouldmoney, String username,
			String time, String mobile, String id, String address) {
		super();
		this.depositsn = depositsn;
		this.money = money;
		this.doormoney = doormoney;
		this.productmoney = productmoney;
		this.shouldmoney = shouldmoney;
		this.username = username;
		this.time = time;
		this.mobile = mobile;
		this.id = id;
		this.address = address;
	}
	
}
