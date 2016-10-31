package com.techelevator.campground;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO {

	JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override 
	public List<Campground> getAllCampgrounds() { 
		List<Campground> campgroundList = new ArrayList<Campground>();
		
		String query = "SELECT * FROM campground " + 
					   "ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while (results.next()) {
			campgroundList.add(saveDataAsCampground(results));
		}
		return campgroundList;
	}
	
	@Override
	public Campground findCampgroundByName(String parkName) {
		List<Campground> searchCampgroundList = getAllCampgrounds();
		for (Campground campground : searchCampgroundList) {
			if ((campground.getName()).equals(parkName)) {
				return campground;
			}
		}
		System.out.println("Campground not found!");
		return null;
	}
	
	@Override
	public List<Campground> findCampgroundByPark(String parkChoice) {
		List<Campground> foundByName = new ArrayList<Campground>();
		String query = "SELECT * " + 
					   "FROM campground cg " +
					   "INNER JOIN park p ON p.park_id = cg.park_id " +
					   "WHERE p.name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, parkChoice);
		while (results.next()) {
			foundByName.add(saveDataAsCampground(results));
		}
		return foundByName;
	}
	
	@Override
	public void printCampgroundInformation(Campground campground) {
		System.out.println("Name: \t\t" + campground.getName());
		System.out.println("ID: \t\t" + campground.getId());
		System.out.println("Open Month: \t" + parseOpenAndCloseMonths(campground.getOpenFrom()));
		System.out.println("Close Month: \t" + parseOpenAndCloseMonths(campground.getOpenTo()));
		System.out.println("Daily Fee: \t" + NumberFormat.getCurrencyInstance().format(campground.getDailyFee()));
	}
	
	@Override
	public boolean isValidStartDate(LocalDate startDate, Campground campground) {
		if (startDate == null) {
			return false;
		}
		int openDay = 1;
		int openMonth = Integer.parseInt(campground.getOpenFrom());
		int openYear = startDate.getYear();
		LocalDate campgroundOpensOn = LocalDate.of(openYear, openMonth, openDay);
		if (startDate.isAfter(campgroundOpensOn)) {
			return true;
		}
		else {
			return false;
		}
	} 
	
	@Override
	public boolean isValidEndDate(LocalDate endDate, Campground campground) {
		if (endDate == null) {
			return false;
		}
		int closeYear = endDate.getYear();
		int closeMonth = Integer.parseInt(campground.getOpenTo());
		int closeDay = 1;
		LocalDate closeDate = LocalDate.of(closeYear, closeMonth, closeDay);
		
		Month closeMonthObject = Month.from(closeDate);	
		closeDay = closeMonthObject.maxLength();
		closeDate = LocalDate.of(closeYear, closeMonth, closeDay);
		
		if (endDate.isBefore(closeDate)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public LocalDate getOpenDate(LocalDate startDate, Campground campground) {
		int openDay = 1;
		int openMonth = Integer.parseInt(campground.getOpenFrom());
		int openYear = startDate.getYear();
		LocalDate campgroundOpensOn = LocalDate.of(openYear, openMonth, openDay);
		return campgroundOpensOn;
	}
	
	@Override
	public LocalDate getCloseDate(LocalDate endDate, Campground campground) {
		int closeYear = endDate.getYear();
		int closeMonth = Integer.parseInt(campground.getOpenTo());
		int closeDay = 1;
		LocalDate closeDate = LocalDate.of(closeYear, closeMonth, closeDay);
		
		Month closeMonthObject = Month.from(closeDate);	
		closeDay = closeMonthObject.maxLength();
		closeDate = LocalDate.of(closeYear, closeMonth, closeDay);
		return closeDate;
	}
	
	private Campground saveDataAsCampground(SqlRowSet results) {
		Campground campground = new Campground();
		campground.setId(results.getInt("park_id"));
		campground.setName(results.getString("name"));
		campground.setOpenFrom(results.getString("open_from_mm"));
		campground.setOpenTo(results.getString("open_to_mm"));
		campground.setDailyFee((results.getBigDecimal("daily_fee"))); 
		return campground;
	}
		
	private String parseOpenAndCloseMonths(String monthString) {
		switch (monthString) {
		    case "01": return "January";
		    case "02": return "February";
		    case "03": return "March";
		    case "04": return "April";
		    case "05": return "May";
		    case "06": return "June";
		    case "07": return "July";
		    case "08": return "August";
		    case "09": return "September";
		    case "10": return "October";
		    case "11": return "November";
		    case "12": return "December";
	        default: return "No month specified";
		}
	}

 
	
	
}
