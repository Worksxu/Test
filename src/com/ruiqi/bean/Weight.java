package com.ruiqi.bean;

import java.io.Serializable;

/**
 * 重瓶的 芯片号，类型。以及状态
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class Weight implements Serializable{
	private String xinpian;
	
	private String type;
	
	private String status;
	private String number;
	private String type_name;
	
	public String getXuliehao() {
		return xuliehao;
	}

	public void setXuliehao(String xuliehao) {
		this.xuliehao = xuliehao;
	}

	private String xuliehao;

	@Override
	public String toString() {
		return "Weight [xinpian=" + xinpian + ", type=" + type + ", status="
				+ status + "]";
	}

	public String getXinpian() {
		return xinpian;
	}

	public void setXinpian(String xinpian) {
		this.xinpian = xinpian;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Weight(String xinpian, String type, String status) {
		super();
		this.xinpian = xinpian;
		this.type = type;
		this.status = status;
	}

	/**
	 * 
	 * @param xinpian 芯片号
	 * @param type 类型id
	 * @param status 
	 * @param number 钢印号
	 * @param type_name 类型名称
	 */
	public Weight(String xinpian, String type, String status, String number,
			String type_name) {
		super();
		this.xinpian = xinpian;
		this.type = type;
		this.status = status;
		this.number = number;
		this.type_name = type_name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public Weight() {
		super();
	}
}
