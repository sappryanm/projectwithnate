package com.techelevator;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.nationalpark.JDBCNationalParkDAO;
import com.techelevator.nationalpark.NationalParkDAO;

public class JDBCNationalParkDAOIntegrationTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private NationalParkDAO dao;
	public final String PARK_DESCRIPTION = "A national park is a park in use for conservation purposes. Often it is a reserve of natural, semi-natural," + 
										   "or developed land that a sovereign state declares or owns.";
	
	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections 
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
	}
	 
	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		String insertPark = "INSERT INTO park ( name, location, establish_date, area, visitors, description ) " +
							"VALUES (?, ?, ?, ?, ?, ?)";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(insertPark, "park", "columbus", LocalDate.of(1990,  1,  1), 1000, 1000, PARK_DESCRIPTION);
		dao = new JDBCNationalParkDAO(dataSource);
	}
	
	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	/* This method provides access to the DataSource for subclasses so that 
	 * they can instantiate a DAO for testing */
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	@Test
	public void get_all_parks_in_system_from_database() {
		int beforeCount = (dao.getAllParksInSystem()).size();
		String insertPark = "INSERT INTO park ( name, location, establish_date, area, visitors, description ) " +
				"VALUES (?, ?, ?, ?, ?, ?)";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(insertPark, "park2", "cleveland", LocalDate.of(1990,  1,  1), 1000, 1000, PARK_DESCRIPTION);
		int afterCount = (dao.getAllParksInSystem()).size();
		Assert.assertEquals(afterCount, beforeCount + 1);
	}
	 
	
	
}
