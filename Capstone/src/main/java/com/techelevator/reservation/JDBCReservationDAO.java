package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Campground;
import com.techelevator.site.Site;

public class JDBCReservationDAO implements ReservationDAO {
	
	JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Reservation> reservationListByCampground(Campground campground) {
		List<Reservation> reservationList = new ArrayList<Reservation>();
		
		String query = "SELECT * " + 
				       "FROM reservation r " + 
				       "INNER JOIN site s ON s.site_id = r.site_id " +
				       "INNER JOIN campground cg ON cg.campground_id = s.campground_id " + 
				       "WHERE cg.campground_id = ? " + 
				       "ORDER BY r.from_date";
				       
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, campground.getId());
		while (results.next()) {
			reservationList.add(saveDataAsReservation(results));
		}
		return reservationList;
	}
	
	@Override
	public boolean reservationAvailable(List<Reservation> reservationList, LocalDate[] dateRange) {
		
		for (int i = 0; i < reservationList.size(); i++) {
			
		}
		
		return true;
	}
	
	public List<Reservation> getAllReservationsBySite(Site site) {
		List<Reservation> reservationsBySite = new ArrayList<Reservation>();
		
		String query = "SELECT r.* " + 
				       "FROM reservation r " + 
				       "INNER JOIN site s ON s.site_id = r.site_id " +
				       "WHERE s.site_id = ? " +
				       "ORDER BY r.from_date";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, site.getSiteId());
		while (results.next()) {
			reservationsBySite.add(saveDataAsReservation(results));
		}
		return reservationsBySite;
	}
	
	@Override
	public long getLengthOfStay(Reservation reservation) {
		return ChronoUnit.DAYS.between(reservation.getFromDate(), reservation.getToDate());
	}
	
	@Override
	public void finishReservation(List<Site> availableSites) {
		
	}

	private Reservation saveDataAsReservation(SqlRowSet results) {
		Reservation reservation = new Reservation();
		reservation.setReservationId(results.getInt("reservation_id"));
		reservation.setSiteId(results.getInt("site_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		reservation.setCreateDate(results.getDate("create_date").toLocalDate());
		return reservation; 
	}
	
}
