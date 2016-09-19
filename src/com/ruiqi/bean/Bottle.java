package com.ruiqi.bean;

import java.io.Serializable;

public class Bottle implements Serializable{
	private String xinpian;
	private String weight;
	private String type;
	public String getXinpian() {
		return xinpian;
	}
	public void setXinpian(String xinpian) {
		this.xinpian = xinpian;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Bottle(String xinpian, String weight, String type) {
		super();
		this.xinpian = xinpian;
		this.weight = weight;
		this.type = type;
	}
	public Bottle() {
		super();
	}
	@Override
	public String toString() {
		return "Bottle [xinpian=" + xinpian + ", weight=" + weight + ", type="
				+ type + "]";
	}
	
	
}
