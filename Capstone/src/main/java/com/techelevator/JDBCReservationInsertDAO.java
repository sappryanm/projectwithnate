package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;






public class JDBCReservationInsertDAO implements ReservationInsertDAO {

	private JdbcTemplate jdbcTemplate;
	private Reservation reservation;


	public JDBCReservationInsertDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	
		}
	
	
	@Override
	public Reservation insertReservationIntoDatabase(int site_id, String name, LocalDate from_date,
		LocalDate to_date, LocalDate create_date) {
		
		reservation = new Reservation();

		String sqlNextResId = "SELECT nextval('reservation_reservation_id_seq')";
		SqlRowSet resultID = jdbcTemplate.queryForRowSet(sqlNextResId);
		resultID.next();
		int nextResID = resultID.getInt(1);
		
		String sqlReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) " +
								"VALUES (?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sqlReservation, nextResID, site_id, name, from_date, to_date, create_date);
		
		reservation.setReservation_id((int)(nextResID));
		reservation.setSite_id((int)(site_id));
		reservation.setName(name);
		reservation.setFrom_date(from_date);
		reservation.setTo_date(to_date);
		reservation.setCreate_date(create_date);
		
		return reservation;

	}
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation r;
		
		r = new Reservation();
		r.setReservation_id(results.getInt("reservation_id"));
		r.setSite_id(results.getInt("site_id"));
		r.setName(results.getString("name"));
		
		Date fd = results.getDate("from_date");
		r.setFrom_date(fd);
		
		Date td = results.getDate("to_date");
		r.setTo_date(td);
		
		Date cd = results.getDate("create_date");
		r.setCreate_date(cd);
		
		return r;
	}


	@Override
	public Reservation getReservationById(int reservationId) {
		Reservation r = new Reservation();
		String sqlGetReservationById = "SELECT * " +
									   "FROM reservation " +
									   "WHERE reservation_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetReservationById, reservationId);
		if (result.next()) {
			r.setReservation_id(result.getInt("reservation_id"));
			r.setSite_id(result.getInt("site_id"));
			r.setName(result.getString("name"));
			
			Date fd = result.getDate("from_date");
			r.setFrom_date(fd);
			
			Date td = result.getDate("to_date");
			r.setTo_date(td);
			
			Date cd = result.getDate("create_date");
			r.setCreate_date(cd);
		}
		return r;
	}

	public List<Reservation> getReservationsBySiteId(int site_Id) {
		ArrayList<Reservation> reservationList = new ArrayList<>();
		String sqlGetReservationsBySiteId = "SELECT * " +
									   "FROM reservation " +
									   "WHERE site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationsBySiteId, site_Id);
		while (results.next()) {
			Reservation r = new Reservation();
			r.setReservation_id(results.getInt("reservation_id"));
			r.setSite_id(results.getInt("site_id"));
			r.setName(results.getString("name"));
			
			Date fd = results.getDate("from_date");

			r.setFrom_date(fd);
			
			Date td = results.getDate("to_date");

			r.setTo_date(td);
			
			Date cd = results.getDate("create_date");

			r.setCreate_date(cd);
			
			reservationList.add(r);
		}
		return reservationList;
	}



	@Override
	public void saveReservation(Reservation r) {
		String sqlInsertReservation = "INSERT INTO reservation(reservation_id, site_id, name, from_date, to_date) " +
									  "VALUES(?, ?, ?, ?, ?)";
		r.setReservation_id(getNextReservationId());
		jdbcTemplate.update(sqlInsertReservation, r.getReservation_id(),
												  r.getSite_id(),
												  r.getName(),
												  r.getFrom_date(),
												  r.getTo_date());
		
	}



	@Override
	public int getCurrentReservationId() {
		String sqlGetCurrentReservationId = "SELECT * FROM RESERVATION WHERE reservation_id = ?;";
		SqlRowSet currentReservationId = jdbcTemplate.queryForRowSet(sqlGetCurrentReservationId);
		if (currentReservationId.next()) {
			System.out.println(currentReservationId);
			return currentReservationId.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong getting the last reservation id.");
		}
	}
	
	private int getNextReservationId() {
		SqlRowSet nextReservationId = jdbcTemplate.queryForRowSet("SELECT * FROM RESERVATION WHERE reservation_id = ?+1;");
		if(nextReservationId.next()) {
			System.out.println(nextReservationId.getInt(1));
			return nextReservationId.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong getting the next reservation id.");
		}
	}


	@Override
	public List<Site> getAvailableSitesByCampground(int campgroundId, LocalDate arrivalDate, LocalDate departureDate) {
		// TODO Auto-generated method stub
		return null;
	}




}
