package com.techelevator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO {

	JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(SingleConnectionDataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Campground> getAllCampgroundsByPark() { 
		List<Campground> campgroundList = new ArrayList<Campground>();
		
		String query = "SELECT * FROM campground " + 
					   "ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while (results.next()) {
			campgroundList.add(saveDataAsCampground(results));
		}
		return campgroundList;
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
	
	
}
