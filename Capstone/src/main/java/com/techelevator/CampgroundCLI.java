package com.techelevator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;



public class CampgroundCLI {

	private NationalParkDAO nationalParkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteSearchDAO siteSearchDAO;
	private ReservationInsertDAO reservationInsertDAO;
	private ParkMenu menu;
	private Scanner scanner;
	
	private static final String MAIN_MENU_HEADING = "Welcome to the National Park Campground Finder not using";
	private static final String MAIN_SEARCH_FOR_ALL_PARKS = "Search for all parks in the system not using";
	private static final String MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK = "Search for camgrounds by park not using";
	private static final String MAIN_MENU_EXIT_APP = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_SEARCH_FOR_ALL_PARKS, MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK, MAIN_MENU_EXIT_APP };
	
	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";
	
	private static final String CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation not working";
	private static final String CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUNDS, 
	                                                                        CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION, 
	                                                                        CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN };
	private static final String RESERVATION_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION = "Search for Available Reservation working on";
	private static final String RESERVATION_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private static final String[] RESERVATION_MENU_OPTIONS = new String[] { RESERVATION_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION,
	                                                                        RESERVATION_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN };
	private static final String SITE_MENU_OPTIONS_SEARCH_FOR_AVAILABLE_RESERVATIONS = "Search for Available Options test";
	private static final String SITE_MENU_OPTIONS_MAKE_RESERVATIONS = "Make Reservations Screen";
	private static final String SITE_MENU_OPTIONS_RETURN_TO_CAMPSITE_INFO = "Return to Previous Screen";

	private static final String[] SITE_MENU_OPTIONS = new String[] { SITE_MENU_OPTIONS_SEARCH_FOR_AVAILABLE_RESERVATIONS,
																		SITE_MENU_OPTIONS_MAKE_RESERVATIONS,
																		MENU_OPTION_RETURN_TO_MAIN};
	
	public static void main(String[] args) throws IOException  {
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
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		reservationInsertDAO = new JDBCReservationInsertDAO(dataSource);
	}
	
	public void run() {
		displayBanner();
		while(true) {
			NationalPark park= handleSelectParks();
			showParkDetails(park);
//			printHeading("Main Menu");
//			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
//			if(choice.equals(MAIN_SEARCH_FOR_ALL_PARKS)) {
//				handleSelectPark();
//			} else if(choice.equals(MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK)) {
//				handleCampgroundSearch();
//			} else if(choice.equals(MAIN_MENU_EXIT_APP)) {
//				System.exit(0);
//			}
		}
	}
	private NationalPark handleSelectParks() {
		List<NationalPark> parkDisplayList = nationalParkDAO.getAllParksInSystem();
		NationalPark park = (NationalPark)menu.getChoiceFromOptions(parkDisplayList.toArray());
		System.out.println("You have chosesn" + park.getName());
		nationalParkDAO.displayParkInfo(parkDisplayList);
		return park;
	}
	private void showParkDetails(NationalPark park) {
	    System.out.println();
	    System.out.println("Park Information Screen");
	    System.out.println(park.getName() + " National Park");
	    System.out.println("Location: " + park.getLocation());
	    System.out.println("Established: " + park.getEstablishedDate());//Need to format date
	    System.out.println("Area: " + park.getArea());
	    System.out.println("Annual Visitors: " + park.getVisitors());
	    System.out.println();
	    System.out.println(park.getDescription());
	    System.out.println();

	    printHeading("Select a Command");
	    String choice = (String)menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
	    if(choice.equals(CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUNDS)) {
	        handleViewCampgrounds(park);
	    } else if(choice.equals(CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
	        handleSearchForAvailableReservation(park);
	    } else if(choice.equals(CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
	    	handleSelectParks();//END OF PARK INFO SCREEN
	    } 
	}
	private void handleViewCampgrounds(NationalPark park) {
		List<Campground> allCampgrounds= campgroundDAO.getCampgroundsByParkIdNoString(park.getId());

		    if (allCampgrounds.size() > 0) {
		        System.out.println(park.getName() + " National Park Campgrounds");
		        System.out.println();
		        System.out.println("Name" + "\t\t" + "Open" + "\t" + "Close" + "\t" + "Daily Fee");
		        for(Campground campground : allCampgrounds) {
		            System.out.println( campground.getName() + "\t" + campground.getOpenFrom() + "\t" + campground.getOpenTo() + "\t" + "$"+ campground.getDailyFee()); 
		        }
		        System.out.println();
		        String choice = (String)menu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS);
		        if(choice.equals(RESERVATION_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
		        	handleViewCampground(park);
		        } else if(choice.equals(RESERVATION_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
		            showParkDetails(park);
		        } 
		        
		    } else {
		        System.out.println("\n*** No results ***");
		    }
		}
//	private void handleSearchForCampgroundByPark() {
//		List<NationalPark> campgroundSearchList = nationalParkDAO.getAllParksInSystem();
//		NationalPark park = (NationalPark)menu.getChoiceFromOptions(campgroundSearchList.toArray());
//		String[] parksInSystem = new String[campgroundSearchList.size() + 1];
//		parksInSystem[0] = "Which park would you like to see the campgrounds for?";
//		for (int i = 1; i <= campgroundSearchList.size(); i++) {
//			parksInSystem[i] = (campgroundSearchList.get(i - 1)).getName();
//		}
//		//1
//		//printHeading(parksInSystem);
//		String parkChoice = (String)menu.getChoiceFromOptions(parksInSystem);
//		//campgroundDAO.findCampgroundByPark(parkChoice);
//		List<Campground> campgroundResults = campgroundDAO.findCampgroundByPark(parkChoice);
//		campgroundDAO.displayCampgroundInfo(campgroundResults);
//		
//		String siteChoice = (String)menu.getChoiceFromOptions(SITE_MENU_OPTIONS);
//		handleCampgroundSearch();
//	}	
	private void handleCampgroundSearch(NationalPark park) {
		printHeading("Campground");
		String choice = (String)menu.getChoiceFromOptions(SITE_MENU_OPTIONS);
		if(choice.equals(SITE_MENU_OPTIONS_SEARCH_FOR_AVAILABLE_RESERVATIONS)) {
			handleCampgroundSearchBySiteId();
		} else if(choice.equals(SITE_MENU_OPTIONS_MAKE_RESERVATIONS)) {
			handleSearchForAvailableReservation(park);	
		}
	}
	
	private void handleCampgroundSearchBySiteId(){
		String newSiteId = getUserInput("Enter Campground Site ID");
		int siteIdEnterToInt = Integer.parseInt(newSiteId);
		//campgroundResults.get(siteIdEnterToInt);
		Site selectedSite = getSiteSelectionFromUser((Campground)campgroundDAO.getCampgroundsByParkIdNoString(siteIdEnterToInt));
		//LocalDate arrivalDate = LocalDate.parse(arrivalString, formatter);
		//LocalDate departureDate = LocalDate.parse(departureString, formatter);
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("What is the arrival date? __/__/____");
	   
	    String arrivalString = scanner.nextLine();
	    System.out.println("What is the departure date? __/__/____");
	    String departureString = scanner.nextLine();
	    
		List<Site> site = siteSearchDAO.showAvailableSites((siteIdEnterToInt), arrivalString, departureString);
		if (site>0)
		System.out.println(site);
//
//	    if (site.size() > 0) {
//	        System.out.println(site.getName() + " National Park Campgrounds");
//	        System.out.println();
//	        System.out.println("Name" + "\t\t" + "Open" + "\t" + "Close" + "\t" + "Daily Fee");
//	        for(Campground campground : site) {
//	            System.out.println( campground.getName() + "\t" + campground.getOpenFrom() + "\t" + campground.getOpenTo() + "\t" + "$"+ campground.getDailyFee()); 
//	        }
//	        System.out.println();
	}
//	private void handleShowAvailableReservations(){
//		String newSiteID = getUserInput("Enter Site ID");
//		
//
//	}
	private NationalPark handleSelectPark() {
	    printHeading("View National Parks");
	    List<NationalPark> allParks= nationalParkDAO.getAllParksInSystem();
	    if(allParks.size() > 0) {
	        System.out.println("\n** Select a park fordetails **");
	        NationalPark choice = (NationalPark)menu.getChoiceFromOptions(allParks.toArray());
	        return choice;
	    } else {
	        System.out.println("\n*** No results ***");
	        return null;
	  }
	}
	private void handleViewCampground(NationalPark park) {
		List<Campground> allCampgrounds= campgroundDAO.getCampgroundsByParkIdNoString(park.getId());
	
	    if (allCampgrounds.size() > 0) {
	        System.out.println(park.getName() + " National Park Campgrounds List");
	        System.out.println();
	        System.out.println("Name" + "\t\t" + "Open Date" + "\t" + "Close Date" + "\t" + "Daily Fee");
	        for(Campground campground : allCampgrounds) {
	            System.out.println( campground.getName() + "\t" + campground.getOpenFrom() + "\t" + campground.getOpenTo() + "\t" + "$"+ campground.getDailyFee()); 
	        }
	        System.out.println();
	        String choice = (String)menu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS);
	        if(choice.equals(RESERVATION_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
	            handleSearchForAvailableReservation(park);
	        } else if(choice.equals(RESERVATION_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
	            showParkDetails(park);
	        } 
	        
	    } else {
	        System.out.println("\n*** No results ***");
	    }
	}
	
	private void handleSearchForAvailableReservation(NationalPark park) {
	    List<Campground> campgroundList = campgroundDAO.getCampgroundsByParkIdNoString(park.getId());
	    Campground[] campgrounds = new Campground[campgroundList.size()];
	    campgroundList.toArray(campgrounds);
	    Campground campground = (Campground) menu.getChoiceFromOptions(campgrounds);
	    
	    System.out.println("What is the arrival date? __/__/____");
	    Scanner scanner = new Scanner(System.in);
	    String arrivalString = scanner.nextLine();
	    System.out.println("What is the departure date? __/__/____");
	    String departureString = scanner.nextLine();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");    
	    LocalDate arrivalDate = LocalDate.parse(arrivalString, formatter);
	    LocalDate departureDate = LocalDate.parse(departureString, formatter);
	    long amountOfDaysReserved= ChronoUnit.DAYS.between(arrivalDate, departureDate);
	    long dailyFeeLong = campground.getDailyFee().longValueExact();
	    List<Site> availableSites = reservationInsertDAO.getAvailableSitesByCampground(campground.getCampgroundId(), arrivalDate, departureDate);
	    for (Site sites : availableSites) {
	        System.out.println("Site No." + "\t" + "Max Occup." + "\t" + "Accessible" + "\t" + "Max RV length" + "\t" + "Utility" + "\t\t" + "Cost");
	        System.out.println(Integer.toString(sites.getSite_id()) + "\t\t" + sites.getMax_occupancy() + "\t\t" + sites.isAccessible() 
	                + "\t\t" + sites.getMax_rv_length() + "\t\t" + sites.isUtilities() + "\t\t" + dailyFeeLong * (long)amountOfDaysReserved);
	        
	    System.out.println();
	    System.out.println("Which site should be reserved (enter 0 to cancel)?_");
	    String siteReserved= scanner.nextLine();
	    System.out.println("What name will you use for reservation?_");
	    String nameReserved= scanner.nextLine();
	    LocalDate create_date = LocalDate.now();
	    Reservation reservedSite= new Reservation();
	    reservedSite= reservationInsertDAO.insertReservationIntoDatabase( sites.getSite_id(), nameReserved, arrivalDate, departureDate, create_date);
	    
	    System.out.println("You are confirmed and booked for:" + sites.getSite_id());
	    }
	}
//		while(true) {
//			System.out.println(MAIN_MENU_HEADING);
//			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
//			if (choice == MAIN_SEARCH_FOR_ALL_PARKS) {
//				List<NationalPark> parkSearchList = nationalParkDAO.getAllParksInSystem();
//				NationalPark park = (NationalPark)menu.getChoiceFromOptions(parkSearchList.toArray());
//				System.out.println("You have chosesn" + park.getName());
////				System.out.println();
////				for (NationalPark park : parkSearchList) {
////					System.out.println(park.getName());
////				}
//
//			}
//			else if (choice == MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK) {
//				List<NationalPark> campgroundSearchList = nationalParkDAO.getAllParksInSystem();
//				String[] parksInSystem = new String[campgroundSearchList.size() + 1];
//				parksInSystem[0] = "Which park would you like to see the campgrounds for?";
//				for (int i = 1; i <= campgroundSearchList.size(); i++) {
//					parksInSystem[i] = (campgroundSearchList.get(i - 1)).getName();
//				}
//				//printHeading(parksInSystem);
//				String parkChoice = (String)menu.getChoiceFromOptions(parksInSystem);
//				//campgroundDAO.findCampgroundByPark(parkChoice);
//				List<Campground> campgroundResults = campgroundDAO.findCampgroundByPark(parkChoice);
//				campgroundDAO.displayCampgroundInfo(campgroundResults);
//				
//				//printHeading(SITE_MENU);
//				Scanner input = new Scanner(System.in);
//				String siteChoice = (String)menu.getChoiceFromOptions(SITE_MENU_OPTIONS);
//				if (choice == SITE_MENU_OPTIONS_SEARCH_FOR_AVAILABLE_RESERVATIONS) {
//					String newSiteId = getUserInput(null);
//					int siteIdEnterToInt = Integer.parseInt(newSiteId);
//					//campgroundResults.get(siteIdEnterToInt);
//					Site selectedSite = getSiteSelectionFromUser((Campground)campgroundDAO.getCampgroundsByParkIdNoString(siteIdEnterToInt));
//					System.out.println("You Site Max Occupancy" + selectedSite.getMax_occupancy());
//					System.out.println("Your Site Number" + selectedSite.getSite_number());
//					System.out.println("You have chosesn" + selectedSite.getCampground_id());
//					//Site campgroundSite = siteSearchDAO.getSiteById(siteIdEnterToInt);
//					//	Site newSite = newSite.getCampground_id();
//					
//					//campgroundResults.get(siteIdEnterToInt);
//					//int campID = campground.getCampgroundId();
//					//s = campground.getCampgroundId();
//				//	s.getSiteSelectionFromUser(campground);
//					//s.getSiteSelectionFromUser(campground);
//					//siteSearchDAO.getSiteById(siteIdEnterToInt);
//			
//				} else if(choice.equals(SITE_MENU_OPTIONS_MAKE_RESERVATIONS)) {
//					
//				}else if(choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
//					
//				}
//			}

		


	
	private Site getSiteSelectionFromUser(Campground campground) {
		System.out.println("Choose a Site:");
		System.out.println("Please Insert Site ID");

		//String newSiteId = getUserInput(null);
		//int siteIdEnterToInt = Integer.parseInt(newSiteId);
		Site allSitesForPark = siteSearchDAO.getSiteById(campground.getCampgroundId());
		return (Site)menu.getChoiceFromOptions(allSitesForPark.getToArray());
		//return allSitesForPark;
	}


	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	private void printHeadingSubMenu(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
//		System.out.println("\n" + optionsMenu[0]);
//		for(int i = 0; i < optionsMenu[0].length(); i++) {
//			System.out.print("-");
//		}
//		System.out.println();
//	}
//	private void printHeading(String[] mainSearchForAllParks) {
//	System.out.println("\n" + mainSearchForAllParks[0]);
//	for(int i = 0; i < mainSearchForAllParks[i].length(); i++) {
//		System.out.print("-");
//	}
//	System.out.println();
//	}
	
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