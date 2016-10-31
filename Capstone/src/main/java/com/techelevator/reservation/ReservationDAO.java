package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.campground.Campground;
import com.techelevator.site.Site;

public interface ReservationDAO {
	public List<Reservation> reservationListByCampground(Campground campground);
	public List<Reservation> getAllReservationsBySite(Site site);
	public boolean reservationAvailable(List<Reservation> reservationList, LocalDate[] dateRange);
}
