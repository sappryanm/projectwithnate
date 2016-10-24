package com.techelevator;
import java.time.LocalDate;
import java.util.List;

public interface ReservationInsertDAO {

	public Reservation insertReservationIntoDatabase(int site_id, String name, LocalDate from_date, LocalDate to_date, LocalDate create_date);
	public List<Reservation> getReservationsBySiteId(int site_Id);
	public Reservation getReservationById(int reservationId);
	public void saveReservation(Reservation r);
	int getCurrentReservationId();
	public List<Site> getAvailableSitesByCampground(int campgroundId, LocalDate arrivalDate, LocalDate departureDate);
//	public Reservation createReservation(int parseInt, String nameReserved, LocalDate arrivalDate,
//			LocalDate departureDate);
}