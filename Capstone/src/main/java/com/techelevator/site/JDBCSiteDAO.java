package com.techelevator.site;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Campground;

public class JDBCSiteDAO implements SiteDAO {

	JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Site> searchForSitesByCampground(Campground campgroundChoice) {
		List<Site> sitesByCampground = new ArrayList<Site>();
		
		String query = "SELECT * " +
				       "FROM site s " +
				       "INNER JOIN campground cg ON cg.campground_id = s.campground_id " +
				       "WHERE cg.campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, campgroundChoice.getId());
		while (results.next()) {
			sitesByCampground.add(saveDataAsSite(results));
		}
		return sitesByCampground;
	}
	
	@Override
	public List<Site> searchForAllSitesWithNoReservations(Campground campground) {
		List<Site> sitesWithNoReservations = new ArrayList<Site>();
		
		String query = "SELECT s.* " + 
				       "FROM site s " +
				       "INNER JOIN campground cg ON cg.campground_id = s.campground_id " +
				       "LEFT JOIN reservation r ON s.site_id = r.site_id " +
				       "WHERE r.site_id IS NULL AND cg.campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, campground.getId());
		while (results.next()) {
			sitesWithNoReservations.add(saveDataAsSite(results));
		}
		return sitesWithNoReservations;
	}
	
	private Site saveDataAsSite(SqlRowSet results) {
		Site site = new Site();
		site.setSiteId(results.getInt("site_id"));
		site.setCampgroundId(results.getInt("campground_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccessible(results.getBoolean("accessible"));
		site.setMaxRvLength(results.getInt("max_rv_length"));
		site.setUtilities(results.getBoolean("utilities"));
		return site;
	}

	
}
