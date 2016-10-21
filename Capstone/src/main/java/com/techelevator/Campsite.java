package com.techelevator;
import java.math.BigDecimal;

public class Campsite {

	private long campgroundId;
	private long parkId;
	private String name;
	private String openFromM;
	private String openToM;
	private BigDecimal dailyFee;
	private int site_number;
	
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public long getParkId() {
		return parkId;
	}
	public void setParkId(long parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenFrom() {
		return openFromM;
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
		return openToM;
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
	public int getSite_number() {
		return site_number;
	}
	public void setSite_number(int site_number) {
		this.site_number = site_number;
	}
	
}
