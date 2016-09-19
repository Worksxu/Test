package com.ruiqi.bean;

import java.io.Serializable;

/**
 * 订单种类的详情信息
 * @author Administrator
 *
 */
public class Orderdeail implements Serializable{
	private String title;
	private String num;
	private String goods_kind;
	private String goods_price;
	private String norm_id;
	private String goods_id;
	private String pay_money;
	private String good_zhekou;
	private String goods_type;
	public Orderdeail(String title, String num, String goods_kind,
			String goods_price) {
		super();
		this.title = title;
		this.num = num;
		this.goods_kind = goods_kind;
		this.goods_price = goods_price;
	}
	public Orderdeail(String title, String num, String goods_kind,
			String goods_price,String norm_id,String goods_id) {
		super();
		this.title = title;
		this.num = num;
		this.goods_kind = goods_kind;
		this.goods_price = goods_price;
		this.norm_id=norm_id;
		this.goods_id=goods_id;
	}
	public Orderdeail(String title, String num, String goods_kind,
			String goods_price,String norm_id,String goods_id,String good_zhekou,String pay_money,String goods_type) {
		super();
		this.title = title;
		this.num = num;
		this.goods_kind = goods_kind;
		this.goods_price = goods_price;
		this.norm_id=norm_id;
		this.goods_id=goods_id;
		this.good_zhekou=good_zhekou;
		this.pay_money=pay_money;
		this.goods_type=goods_type;
	}
	
	
	public String getGoods_type() {
		return goods_type;
	}
	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}
	public String getGood_zhekou() {
		return good_zhekou;
	}
	public void setGood_zhekou(String good_zhekou) {
		this.good_zhekou = good_zhekou;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getNorm_id() {
		return norm_id;
	}
	public void setNorm_id(String norm_id) {
		this.norm_id = norm_id;
	}
	public Orderdeail() {
		super();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getGoods_kind() {
		return goods_kind;
	}
	public void setGoods_kind(String goods_kind) {
		this.goods_kind = goods_kind;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	@Override
	public String toString() {
		return "Orderdeail [title=" + title + ", num=" + num + ", goods_kind="
				+ goods_kind + ", goods_price=" + goods_price + "]";
	}
	
	
}
