package com.ruiqi.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class YouHuiZheKouInfo implements Serializable {
	
	private String annex;
	private String comment;
	private String id;
	private float money;
	private float price;
	private String promotionsn;
	private String status;
	private String time_created;
	private String time_end;
	private String time_start;
	private String title;
	private String type;
	public String getAnnex() {
		return annex;
	}
	public void setAnnex(String annex) {
		this.annex = annex;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getPromotionsn() {
		return promotionsn;
	}
	public void setPromotionsn(String promotionsn) {
		this.promotionsn = promotionsn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime_created() {
		return time_created;
	}
	public void setTime_created(String time_created) {
		this.time_created = time_created;
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
	

}
