package com.ruiqi.chai;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ArrearsList implements Serializable {

	private String contractno;
	private String is_return;
	private String money;
	private String shipper_id;
	private String type;

	public String getContractno() {
		return contractno;
	}

	public void setContractno(String contractno) {
		this.contractno = contractno;
	}

	public String getIs_return() {
		return is_return;
	}

	public void setIs_return(String is_return) {
		this.is_return = is_return;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getShipper_id() {
		return shipper_id;
	}

	public void setShipper_id(String shipper_id) {
		this.shipper_id = shipper_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
