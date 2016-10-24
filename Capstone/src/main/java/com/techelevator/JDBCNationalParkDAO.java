package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;
 
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCNationalParkDAO implements NationalParkDAO {
 
	JdbcTemplate jdbcTemplate;
	
	public JDBCNationalParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	//HashMap<String,NationalParkDAO > itemMap = new HashMap<String, NationalParkDAO >();
	@Override
	public List<NationalPark> getAllParksInSystem() { 
		List<NationalPark> parkList = new ArrayList<NationalPark>();
		
		String query = "SELECT * FROM park " + 
					   "ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while (results.next()) {
			NationalPark p = new NationalPark();
			
			p.setId(results.getInt("park_id"));
			p.setName(results.getString("name"));
			p.setLocation(results.getString("location"));
			p.setArea(results.getInt("area"));
			p.setVisitors(results.getInt("visitors"));
			p.setDescription(results.getString("description"));
			
			LocalDate estDate = (results.getDate("establish_date").toLocalDate());
			//LocalDate establishDate = estDate.toLocalDate();
			p.setEstablishedDate(estDate);
			
			//Attempteding to try to to do  Hashmap So I can use String Value
//			NationalPark item = new NationalPark(slot[0], slot[1], new DollarAmount(priceInPennies), 5);
//            itemMap.put(slot[0], item);
			parkList.add(p);
			//parkList.add(saveDataAsPark(results));
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
	@Override
	public NationalPark getParkById(int parkId) {
		NationalPark p = new NationalPark();
		String sqlSelectParkById = "SELECT * "+
									"FROM park "+
									"WHERE park_id = ?";
		
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlSelectParkById, parkId);
		
		if(result.next()) {
			p.setId(result.getInt("park_id"));
			p.setName(result.getString("name"));
			p.setLocation(result.getString("location"));
			p.setArea(result.getInt("area"));
			p.setVisitors(result.getInt("visitors"));
			p.setDescription(result.getString("description"));
			
			//Date estDate = result.getDate("establish_date");
			LocalDate estDate = (result.getDate("establish_date").toLocalDate());
			p.setEstablishedDate(estDate);
			
		}
		return p;
	}
	public void displayParkInfo(List<NationalPark> foundByName) {
		for (NationalPark i: foundByName){
			System.out.println("_________CampSite_______________");
			System.out.println("name    " +i.getName());
			System.out.println("ID    " +i.getId());
			System.out.println("Begin Date     " +i.getVisitors());
			System.out.println("Closing date   " +i.getArea());
			System.out.println("Fee   " +i.getDescription());
		}
		
}
	
}
