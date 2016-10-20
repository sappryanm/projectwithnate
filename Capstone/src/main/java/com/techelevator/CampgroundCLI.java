package com.techelevator;


import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class CampgroundCLI {

	private NationalParkDAO nationalParkDAO;
	private ParkMenu menu;
	
	private static final String MAIN_MENU_HEADING = "Welcome to the National Park Campground Finder";
	private static final String MAIN_SEARCH_FOR_ALL_PARKS = "Search for all parks in the system";
	private static final String MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK = "Search for camgrounds by park";
	private static final String MAIN_MENU_EXIT_APP = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_HEADING, MAIN_SEARCH_FOR_ALL_PARKS, MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK, MAIN_MENU_EXIT_APP };

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		this.menu = new ParkMenu(System.in, System.out);
		nationalParkDAO = new JDBCNationalParkDAO(dataSource);
	}
	
	public void run() {
		displayBanner();
		
		while(true) {
			printHeading(MAIN_MENU_OPTIONS);
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice == MAIN_SEARCH_FOR_ALL_PARKS) {
				List<NationalPark> parkSearchList = nationalParkDAO.getAllParksInSystem();
				System.out.println();
				for (NationalPark park : parkSearchList) {
					System.out.println(park.getName());
				}
			}
			else if (choice == MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK) {
				List<NationalPark> campgroundSearchList = nationalParkDAO.getAllParksInSystem();
				String[] parksInSystem = new String[campgroundSearchList.size() + 1];
				parksInSystem[0] = "Which park would you like to see the campgrounds for?";
				for (int i = 1; i <= campgroundSearchList.size(); i++) {
					parksInSystem[i] = (campgroundSearchList.get(i - 1)).getName();
				}
				printHeading(parksInSystem);
				String parkChoice = (String)menu.getChoiceFromOptions(parksInSystem);
			}
		}
	}
	 
	
	
	
	
	
	private void printHeading(String[] optionsMenu) {
		System.out.println("\n" + optionsMenu[0]);
		for(int i = 0; i < optionsMenu[0].length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
	
	private void displayBanner() {
		System.out.println();
		System.out.println(",--.  ,--.          ,--.  ,--.                       ,--.    ,------.                ,--.         ,---.                              ,--.");      
		System.out.println("|  ,'.|  | ,--,--.,-'  '-.`--' ,---. ,--,--,  ,--,--.|  |    |  .--. ' ,--,--.,--.--.|  |,-.     '   .-'  ,---.  ,--,--.,--.--. ,---.|  ,---.");  
		System.out.println("|  |' '  |' ,-.  |'-.  .-',--.| .-. ||      \\' ,-.  ||  |    |  '--' |' ,-.  ||  .--'|     /     `.  `-. | .-. :' ,-.  ||  .--'| .--'|  .-.  |"); 
		System.out.println("|  | `   |\\ '-'  |  |  |  |  |' '-' '|  ||  |\\ '-'  ||  |    |  | --' \\ '-'  ||  |   |  \\  \\     .-'    |\\   --.\\ '-'  ||  |   \\ `--.|  | |  | ");
		System.out.println("`--'  `--' `--`--'  `--'  `--' `---' `--''--' `--`--'`--'    `--'      `--`--'`--'   `--'`--'    `-----'  `----' `--`--'`--'    `---'`--' `--' ");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
        
	}
}