package com.techelevator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
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
	  
	private Campground saveDataAsCampground(SqlRowSet results) {
		Campground campground = new Campground();
		campground.setParkId(results.getInt("park_id"));
		campground.setName(results.getString("name"));
		campground.setOpenFrom(results.getString("open_from_mm"));
		campground.setOpenTo(results.getString("open_to_mm"));
		campground.setDailyFee((results.getBigDecimal("daily_fee"))); 
		return campground;
	}
 
	public void displayCampgroundInfo(List<Campground> foundByName) {
			for (Campground i: foundByName){
				System.out.println("_________CampSite_______________");
				System.out.println("name    " +i.getName());
				System.out.println("ID    " +i.getParkId());
				System.out.println("Begin Date     " +i.getOpenFrom());
				System.out.println("Closing date   " +i.getOpenTo());
				System.out.println("Fee   " +i.getDailyFee());
			}
			
	}
	@Override
	public List<Campground> getCampgroundsByParkIdNoString(int parkId) {
		ArrayList<Campground> campgroundList = new ArrayList<>();
		String sqlSelectCampgroundsByParkId = "SELECT * "+
												"FROM campground "+
												"WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectCampgroundsByParkId, parkId);
		
		while(results.next()) {
			Campground c = new Campground();
			c.setCampgroundId(results.getInt("campground_id"));
			c.setParkId(results.getInt("park_id"));
			c.setName(results.getString("name"));
			c.setOpenFrom(results.getString("open_from_mm"));
			c.setOpenTo(results.getString("open_to_mm"));
			c.setDailyFee(results.getBigDecimal("daily_fee"));
			
			campgroundList.add(c);
		}
		return campgroundList;
	}
//	@Override
//	public Campsite showCampsgroundName(int campgroundId) {
//		Campsite campGround = new Campsite();
//		
//		String sqlAllCampsites = "SELECT * " +
//								 "FROM campground " +
//								 "WHERE campground_id = ?";
//				
//		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllCampsites, campgroundId);
//		results.next();
//		int campId = results.getInt("campground_id");
//		String name = results.getString("name");
//
//		campGround.setCampgroundId(campId);
//		campGround.setName(name);
//
//		return campGround;
//	}
	
}
