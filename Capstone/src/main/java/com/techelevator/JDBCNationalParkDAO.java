package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
 
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCNationalParkDAO implements NationalParkDAO {
 
	JdbcTemplate jdbcTemplate;
	
	public JDBCNationalParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<NationalPark> getAllParksInSystem() { 
		List<NationalPark> parkList = new ArrayList<NationalPark>();
		
		String query = "SELECT * FROM park " + 
					   "ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while (results.next()) {
			parkList.add(saveDataAsPark(results));
		}
		return parkList;
	}
	 
	private NationalPark saveDataAsPark(SqlRowSet results) {
		NationalPark park = new NationalPark();
		park.setId(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishedDate((results.getDate("establish_date")).toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		return park;
	}

	
}
