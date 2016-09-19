package com.ruiqi.addreselector;

public class MyListItem {
	private String name;
	private String pcode;
	private String code;
	public MyListItem(String code,String pcode,String name){
		this.name=name;
		this.pcode=pcode;
		this.code=code;
	}
	public MyListItem(String code,String name){
		this.name=name;
		this.code=code;
		
	}
	public MyListItem(){}
	public String getName(){
		return name;
	}
	public String getPcode(){
		return pcode;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPcode(String pcode){
		this.pcode=pcode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
