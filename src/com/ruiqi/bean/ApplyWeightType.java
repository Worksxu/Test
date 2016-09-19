package com.ruiqi.bean;
/**
 * 领取重瓶的种类
 * @author Administrator
 *
 */
public class ApplyWeightType {
	private String type;
	private String num;
	public ApplyWeightType(String type, String num) {
		super();
		this.type = type;
		this.num = num;
	}
	public ApplyWeightType() {
		super();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "ApplyWeightType [type=" + type + ", num=" + num + "]";
	}
	
}
