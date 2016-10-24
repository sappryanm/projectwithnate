package com.techelevator;

import java.util.List;

public interface NationalParkDAO {
	
	public default List<NationalPark> getAllParksInSystem() {
		// TODO Auto-generated method stub
		return null;
	}
	public void displayParkInfo(List<NationalPark> campgroundResults);
	NationalPark getParkById(int parkId);
	
	
	
}
