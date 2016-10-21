package com.techelevator;


import java.util.List;

public interface SiteSearchDAO {

	public List<Site> showAvailableSites(int siteId, String beginDate, String endDate);
	
}