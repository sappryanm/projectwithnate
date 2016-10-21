package com.techelevator;

import java.math.BigDecimal;

public class Campground {
	private int id;
	private String name;
	private String openFrom;
	private String openTo;
	private BigDecimal dailyFee;
	private int campgroundId;
	
	public int getCampgroundId() {
		return campgroundId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(String openFrom) {
		this.openFrom = openFrom;
	}
	public String getOpenTo() {
		return openTo;
	}
	public void setOpenTo(String openTo) {
		this.openTo = openTo;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	@Override 
	public String toString() {
		return name;
	}
}
