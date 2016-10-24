package com.techelevator;


import java.util.List;

public interface SiteSearchDAO {

	public List<Site> showAvailableSites(int site_Id, String beginDate, String endDate);
	public Site getSiteById(int site_Id);
}