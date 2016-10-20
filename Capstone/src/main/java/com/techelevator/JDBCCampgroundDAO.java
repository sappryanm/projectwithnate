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
		campground.setId(results.getInt("park_id"));
		campground.setName(results.getString("name"));
		campground.setOpenFrom(results.getString("open_from_mm"));
		campground.setOpenTo(results.getString("open_to_mm"));
		campground.setDailyFee((results.getBigDecimal("daily_fee"))); 
		return campground;
	}
 
	public void displayCampgroundInfo(List<Campground> foundByName) {
			for (Campground i: foundByName){
				System.out.println(i.getName());
				System.out.println(i.getId());
				System.out.println(i.getOpenFrom());
				System.out.println(i.getOpenTo());
				System.out.println(i.getDailyFee());
			}
			
	}
	
}
