package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.techelevator.campground.Campground;

public class CampgroundTest {

	private Campground testCampground;
	
	@Before
	public void setup() {
		testCampground = new Campground();
	}
	
	@Test
	public void test_getters_and_setters() {
		testCampground.setId(1);
		testCampground.setName("test");
		testCampground.setOpenFrom("01");
		testCampground.setOpenTo("12");
		testCampground.setDailyFee(new BigDecimal(1.00));
		
		int id = testCampground.getId();
		String name = testCampground.getName();
		String openDate = testCampground.getOpenFrom();
		String closeDate = testCampground.getOpenTo();
		BigDecimal feeMoney = testCampground.getDailyFee(); 
	}
	

}
