package com.techelevator;

import java.util.List;

public interface CampgroundDAO {
	public List<Campground> getAllCampgrounds();
	public List<Campground> findCampgroundByPark(String parkChoice);
	public void displayCampgroundInfo(List<Campground> campgroundResults);
}
