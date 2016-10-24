package com.techelevator;

import java.math.BigDecimal;

public class Campground {
	private int parkId;
	private String name;
	private String openFrom;
	private String openTo;
	private String openFromM;
	private String openToM;
	private BigDecimal dailyFee;
	private int campgroundId;
	
	public int getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int park_id) {
		this.parkId = parkId;
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
	public void setOpenFrom(long openFrom) {
		if (openFrom == 1){
			this.openFromM = "January";
		}else if (openFrom == 2){
			this.openFromM = "February";
		}else if (openFrom == 3){
			this.openFromM = "March";
		}else if (openFrom == 4){
			this.openFromM = "April";
		}else if (openFrom == 5){
			this.openFromM = "May";
		}else if (openFrom == 6){
			this.openFromM = "June";
		}else if (openFrom == 7){
			this.openFromM = "July";
		}else if (openFrom == 8){
			this.openFromM = "August";
		}else if (openFrom == 9){
			this.openFromM = "September";
		}else if (openFrom == 10){
			this.openFromM = "October";
		}else if (openFrom == 11){
			this.openFromM = "November";
		}else if (openFrom == 12){
			this.openFromM = "December";
		}
	}
	public String getOpenTo() {
		return openTo;
	}
	public void setOpenTo(long openFrom) {
		if (openFrom == 1){
			this.openToM = "January";
		}else if (openFrom == 2){
			this.openToM = "February";
		}else if (openFrom == 3){
			this.openToM = "March";
		}else if (openFrom == 4){
			this.openToM = "April";
		}else if (openFrom == 5){
			this.openToM = "May";
		}else if (openFrom == 6){
			this.openToM = "June";
		}else if (openFrom == 7){
			this.openToM = "July";
		}else if (openFrom == 8){
			this.openToM = "August";
		}else if (openFrom == 9){
			this.openToM = "September";
		}else if (openFrom == 10){
			this.openToM = "October";
		}else if (openFrom == 11){
			this.openToM = "November";
		}else if (openFrom == 12){
			this.openToM = "December";
		}
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
