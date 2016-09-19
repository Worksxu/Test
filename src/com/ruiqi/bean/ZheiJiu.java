package com.ruiqi.bean;
/**
 * 折旧的瓶的信息
 * @author Administrator
 *
 */
public class ZheiJiu {
	private String weight;
	private String num;
	private String id;
	private String price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public ZheiJiu(String weight, String num, String id, String price) {
		super();
		this.weight = weight;
		this.num = num;
		this.id = id;
		this.price = price;
	}

	public ZheiJiu(String weight, String num) {
		super();
		this.weight = weight;
		this.num = num;
	}

	public ZheiJiu() {
		super();
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "ZheiJiu [weight=" + weight + ", num=" + num + ", id=" + id
				+ ", price=" + price + "]";
	}
	
	
	
}
