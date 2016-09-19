package com.ruiqi.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 气罐规格的属性类
 * @author Administrator
 *
 */
public class Type implements Serializable{
	private String price;
	private String weight;
	private String num;
	
	private String id;
	private String norm_id;
	private String type;
	private String bottle_name;
	
	private String name;
	private String yj_price;
	
	
	private String gpnum;
	
	public Type(String price, String weight, String num, String id,String norm_id, String type, String bottle_name, String name,String yj_price,String gpnum) {
		super();
		this.price = price;
		this.weight = weight;
		this.num = num;
		this.id = id;
		this.norm_id = norm_id;
		this.type = type;
		this.bottle_name = bottle_name;
		this.name = name;
		this.yj_price = yj_price;
		this.gpnum=gpnum;
	}
	
	public String getGpnum() {
		return gpnum;
	}
	public void setGpnum(String gpnum) {
		this.gpnum = gpnum;
	}
	public String getYj_price() {
		return yj_price;
	}
	public void setYj_price(String yj_price) {
		this.yj_price = yj_price;
	}
	public Type(String price, String weight, String num, String id,String norm_id, String type, String bottle_name, String name,String yj_price) {
		super();
		this.price = price;
		this.weight = weight;
		this.num = num;
		this.id = id;
		this.norm_id = norm_id;
		this.type = type;
		this.bottle_name = bottle_name;
		this.name = name;
		this.yj_price = yj_price;
		
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type(String price, String weight, String num, String id,
			String norm_id, String type, String bottle_name, String name) {
		super();
		this.price = price;
		this.weight = weight;
		this.num = num;
		this.id = id;
		this.norm_id = norm_id;
		this.type = type;
		this.bottle_name = bottle_name;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNorm_id() {
		return norm_id;
	}
	public void setNorm_id(String norm_id) {
		this.norm_id = norm_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBottle_name() {
		return bottle_name;
	}
	public void setBottle_name(String bottle_name) {
		this.bottle_name = bottle_name;
	}
	public Type(String price, String weight, String num, String id,
			String norm_id, String type, String bottle_name) {
		super();
		this.price = price;
		this.weight = weight;
		this.num = num;
		this.id = id;
		this.norm_id = norm_id;
		this.type = type;
		this.bottle_name = bottle_name;
	}
	public Type(String price, String weight, String num) {
		super();
		this.price = price;
		this.weight = weight;
		this.num = num;
	}
	public Type() {
		super();
	}
	
	public Type(String price, String weight) {
		super();
		this.price = price;
		this.weight = weight;
	}
	public String getPrice() {
		return price;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "Type [price=" + price + ", weight=" + weight + ", num=" + num
				+ ", id=" + id + ", norm_id=" + norm_id + ", type=" + type
				+ ", bottle_name=" + bottle_name + ", name=" + name
				+ ", yj_price=" + yj_price + "]";
	}
	
	
	
}
