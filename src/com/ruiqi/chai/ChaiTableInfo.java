package com.ruiqi.chai;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChaiTableInfo implements Serializable {
	
	private String title1;
	private String title2;
	private String title3;
	private String title4;
	
	private String title5;
	private int number;
	
	
	
	
	public ChaiTableInfo(String title1, String title2) {
		super();
		this.title1 = title1;
		this.title2 = title2;
	}
	public ChaiTableInfo(String title1, String title2, String title3) {
		super();
		this.title1 = title1;
		this.title2 = title2;
		this.title3 = title3;
	}
	/**
	 * 
	 * @param title1
	 * @param title2
	 * @param number 数量
	 */
	public ChaiTableInfo(String title1, String title2, int number) {
		super();
		this.title1 = title1;
		this.title2 = title2;
		this.number = number;
	}
	public ChaiTableInfo(String title1, String title2, String title3,
			String title4) {
		super();
		this.title1 = title1;
		this.title2 = title2;
		this.title3 = title3;
		this.title4 = title4;
	}
	
	public ChaiTableInfo(String title1, String title2, String title3,
			String title4, String title5) {
		super();
		this.title1 = title1;
		this.title2 = title2;
		this.title3 = title3;
		this.title4 = title4;
		this.title5 = title5;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getTitle1() {
		return title1;
	}
	public void setTitle1(String title1) {
		this.title1 = title1;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getTitle3() {
		return title3;
	}
	public void setTitle3(String title3) {
		this.title3 = title3;
	}
	public String getTitle4() {
		return title4;
	}
	public void setTitle4(String title4) {
		this.title4 = title4;
	}
	public String getTitle5() {
		return title5;
	}
	public void setTitle5(String title5) {
		this.title5 = title5;
	}
	
	
	

}
