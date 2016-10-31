package com.techelevator.campground;

import java.time.LocalDate;
import java.util.List;

public interface CampgroundDAO {
	public List<Campground> getAllCampgrounds();
	public List<Campground> findCampgroundByPark(String parkChoice);
	public void printCampgroundInformation(Campground campground);
	public Campground findCampgroundByName(String parkName);
	public boolean isValidStartDate(LocalDate startDate, Campground campground);
	public boolean isValidEndDate(LocalDate endDate, Campground campground);
	public LocalDate getOpenDate(LocalDate startDate, Campground campground);
	public LocalDate getCloseDate(LocalDate endDate, Campground campground);
}
