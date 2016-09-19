package com.ruiqi.bean;

import java.io.Serializable;

/**
 * 订单的一些基础信息
 * @author Administrator
 *
 */
public class TableInfo implements Serializable{
	private String orderNum;//订单号
	
	private String orderMoney;//订单金额
	
	private String orderStatus;//订单状态
	
	private String orderTime;//下单时间 
	private String beizhu;//备注
	
	/**
	 * 两列的构造器
	 */
	
	public TableInfo(String orderNum, String orderMoney) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
	}
	

	
	/**
	 * 3列的构造器
	 * @param orderNum
	 * @param orderMoney
	 * @param orderStatus
	 */
	public TableInfo(String orderNum, String orderMoney, String orderStatus) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
		this.orderStatus = orderStatus;
	}


	/**
	 * 4列的构造器
	 * @param orderNum
	 * @param orderMoney
	 * @param orderStatus
	 * @param orderTime
	 */
	public TableInfo(String orderNum, String orderMoney, String orderStatus,String orderTime) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
	}
	/**
	 * 5列的构造器
	 * @param orderNum
	 * @param orderMoney
	 * @param orderStatus
	 * @param orderTime
	 * @param beizhu
	 */
	public TableInfo(String orderNum, String orderMoney, String orderStatus,String orderTime,String beizhu) {
		super();
		this.orderNum = orderNum;
		this.orderMoney = orderMoney;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		this.beizhu=beizhu;
	}

	public TableInfo() {
		super();
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	
	public String getBeizhu() {
		return beizhu;
	}



	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}



	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	@Override
	public String toString() {
		return "OrderInfo [orderNum=" + orderNum + ", orderMoney=" + orderMoney
				+ ", orderStatus=" + orderStatus + ", orderTime=" + orderTime
				+ "]";
	}
	
	
}
