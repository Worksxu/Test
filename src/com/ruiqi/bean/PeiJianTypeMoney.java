package com.ruiqi.bean;

import java.io.Serializable;

public class PeiJianTypeMoney implements Serializable{
	//配件库存 字段 对应  字段  goods_id(goods_type)->norm_id  goods_kind->id type->type  
	private String id;
	private String name;
	
	private String type;
	
	private String norm_id;
	
	private String typename;
	private String price;
	
	private String num;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public PeiJianTypeMoney(String id, String name, String type,
			String norm_id, String typename, String price, String num) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.norm_id = norm_id;
		this.typename = typename;
		this.price = price;
		this.num = num;
	}
	@Override
	public String toString() {
		return "PeiJianTypeMoney [id=" + id + ", name=" + name + ", type="
				+ type + ", norm_id=" + norm_id + ", typename=" + typename
				+ ", price=" + price + ", num=" + num + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public PeiJianTypeMoney(String id, String num) {
		super();
		this.id = id;
		this.num = num;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNorm_id() {
		return norm_id;
	}
	public void setNorm_id(String norm_id) {
		this.norm_id = norm_id;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public PeiJianTypeMoney(String id, String name, String type,
			String norm_id, String typename, String price) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.norm_id = norm_id;
		this.typename = typename;
		this.price = price;
	}
	public PeiJianTypeMoney() {
		super();
	}
	
	
	
}
