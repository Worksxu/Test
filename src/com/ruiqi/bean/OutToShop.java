package com.ruiqi.bean;
/**
 * 上缴金额给门店的订单详情
 * @author Administrator
 *
 */
public class OutToShop {
	private String money;
	private String shop;//门店收款人
	private String time;
	public OutToShop(String money, String shop, String time) {
		super();
		this.money = money;
		this.shop = shop;
		this.time = time;
	}
	public OutToShop() {
		super();
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "OutToShop [money=" + money + ", shop=" + shop + ", time="
				+ time + "]";
	}
	
	
}
