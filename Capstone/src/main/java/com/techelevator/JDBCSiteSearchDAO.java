package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;



public class JDBCSiteSearchDAO implements SiteSearchDAO {

	private JdbcTemplate jdbcTemplate;
	private List<Reservation> resList;
	private List<Site> availableSiteList;


	public JDBCSiteSearchDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		}
	
		
	@Override
	public List<Site> showAvailableSites(int campgroundId, String beginDate, String endDate) {
		
		resList  = new ArrayList<>();
		availableSiteList = new ArrayList<>();
	
		String sqlAllReservations = "SELECT * " +
									"FROM site " +
									"LEFT JOIN reservation ON site.site_id = reservation.site_id " +
									"WHERE site.campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllReservations, campgroundId);
		
		while(results.next()) {
						
				int reservation_id = results.getInt("reservation_id");
			
				if (reservation_id == 0) {
					Site availableSite = new Site();
					
					int site_id = results.getInt("site_id");
					int campground_id = results.getInt("campground_id");
					int site_number = results.getInt("site_number");
					int max_occupancy = results.getInt("max_occupancy");
					boolean accessible = results.getBoolean("accessible");
					int max_rv_length = results.getInt("max_rv_length");
					boolean utilities = results.getBoolean("utilities");

					availableSite.setSite_id(site_id);
					availableSite.setCampground_id(campground_id);
					availableSite.setSite_number(site_number);
					availableSite.setMax_occupancy(max_occupancy);
					availableSite.setAccessible(accessible);
					availableSite.setMax_rv_length(max_rv_length);
					availableSite.setUtilities(utilities);
					
					System.out.println("added Sites to List");
					
					availableSiteList.add(availableSite);
				
			} else {
			
				Reservation res = new Reservation();
				
					int site_id = results.getInt("site_id");
					String name = results.getString("name");
					String from_date = results.getString("from_date");
						LocalDate fromLD = LocalDate.parse(from_date);
					String to_date = results.getString("to_date");
						LocalDate toLD = LocalDate.parse(to_date);
					String create_date = results.getString("create_date");
					LocalDate create_dateLD = LocalDate.parse(create_date);
					
					res.setReservation_id(reservation_id);
					res.setSite_id(site_id);
					res.setName(name);
					res.setFrom_date(fromLD);
					res.setTo_date(toLD);
					res.setTo_date(create_dateLD);
				
					resList.add(res);
					
		
		boolean isSiteAvailable;
		
		for(Reservation r : resList) {
			LocalDate fromeDate = r.getFrom_date();
			LocalDate toDate = r.getTo_date();
			LocalDate beginDateLD = LocalDate.parse(beginDate);
			LocalDate endDateLD = LocalDate.parse(endDate);
			
			if (fromeDate.isAfter(beginDateLD) && fromeDate.isBefore(endDateLD)){
				isSiteAvailable = false;
				System.out.println("1 - it's false");

			}
			else if (toDate.isAfter(beginDateLD) && toDate.isBefore(endDateLD)){
				isSiteAvailable = false;
				System.out.println("2 - it's false");

			}
			else if (fromeDate.isBefore(beginDateLD) && toDate.isAfter(endDateLD)) {
				isSiteAvailable = false;
				System.out.println("3 - it's false");

			}
			else {
				isSiteAvailable = true;
				
				Site availableSite = new Site();
						
				int siteId = results.getInt("site_id");
				int campground_id = results.getInt("campground_id");
				int site_number = results.getInt("site_number");
				int max_occupancy = results.getInt("max_occupancy");
				boolean accessible = results.getBoolean("accessible");
				int max_rv_length = results.getInt("max_rv_length");
				boolean utilities = results.getBoolean("utilities");

				availableSite.setSite_id(siteId);
				availableSite.setCampground_id(campground_id);
				availableSite.setSite_number(site_number);
				availableSite.setMax_occupancy(max_occupancy);
				availableSite.setAccessible(accessible);
				availableSite.setMax_rv_length(max_rv_length);
				availableSite.setUtilities(utilities);
				
				System.out.println("4 - it's true");
				
				availableSiteList.add(availableSite);
			}
			}
		}
		}

		return availableSiteList;

	}
//	public Item getItem(String itemKey) {
//		return itemMap.get(itemKey);
//	}

	@Override
	public Site getSiteById(int site_Id) {
		String sqlGetSiteById = "SELECT * " +
								"FROM site "+
								"WHERE site_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetSiteById, site_Id);
		Site s = new Site();
		
		if (result.next()) {
			s.setSite_id(result.getInt("site_id"));
			s.setCampground_id(result.getInt("campground_id"));
			s.setSite_number(result.getInt("site_number"));
			s.setMax_occupancy(result.getInt("max_occupancy"));
			s.setAccessible(result.getBoolean("accessible"));
			s.setMax_rv_length(result.getInt("max_rv_length"));
			s.setUtilities(result.getBoolean("utilities"));
		}
		return s;
	}
}