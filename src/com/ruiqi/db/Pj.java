package com.ruiqi.db;

public class Pj {
	private String id ;
	private String name ;
	private String type ;
	private String norm_id ;
	private String typename ;
	private String price ;
	public Pj(String id, String name, String type, String norm_id,
			String typename, String price) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.norm_id = norm_id;
		this.typename = typename;
		this.price = price;
	}
	public Pj() {
		super();
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
	@Override
	public String toString() {
		return "Pj [id=" + id + ", name=" + name + ", type=" + type
				+ ", norm_id=" + norm_id + ", typename=" + typename
				+ ", price=" + price + "]";
	}
	
	
}
