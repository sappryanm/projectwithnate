package com.techelevator;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.nationalpark.NationalPark;

public class NationalParkTest {

	NationalPark testPark;
	
	@Before
	public void setUp() {
		testPark = new NationalPark();
	}
	
	@Test
	public void getters_and_setters_for_nat_park() {
		testPark.setId(1);
		testPark.setName("name");
		testPark.setLocation("Columbus");
		testPark.setEstablishedDate(LocalDate.of(1990, 1, 1));
		testPark.setArea(1000);
		testPark.setVisitors(1000);
		testPark.setDescription("this is a test");
		
		int id = testPark.getId();
		String name = testPark.getName();
		String location = testPark.getLocation();
		LocalDate estabDate = testPark.getEstablishedDate();
		int area = testPark.getArea();
		int visitors = testPark.getVisitors();
		String description = testPark.getDescription();
		
		Assert.assertEquals(1, id);
		Assert.assertEquals("name", name);
		Assert.assertEquals("Columbus", location);
		Assert.assertEquals(LocalDate.of(1990, 1, 1), estabDate);
		Assert.assertEquals(1000, area);
		Assert.assertEquals(1000, visitors);
		Assert.assertEquals("this is a test", description);
	}

}
