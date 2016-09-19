package com.ruiqi.chai;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChaiYouHui implements Serializable {
	/**
	 * //液化气类型
	 */
	private String good_type;
	/**
	 * //使用条件
	 */
	private String money;
	/**
	 * //数量
	 */
	private String num;
	/**
	 * //价格
	 */
	private String price;
	private String time_end;
	private String time_start;
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 优惠券名称
	 */
	private String title;
	/**
	 * 优惠类型 1优惠 2折扣
	 */
	private String type;
	/**
	 * 优惠券编码
	 */
	private String yhsn;
	public String getGood_type() {
		return good_type;
	}
	public void setGood_type(String good_type) {
		this.good_type = good_type;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
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
	public String getYhsn() {
		return yhsn;
	}
	public void setYhsn(String yhsn) {
		this.yhsn = yhsn;
	}
	
	

}
