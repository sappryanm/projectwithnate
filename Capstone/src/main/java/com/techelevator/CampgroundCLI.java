package com.techelevator;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.Campground;
import com.techelevator.campground.CampgroundDAO;
import com.techelevator.campground.JDBCCampgroundDAO;
import com.techelevator.nationalpark.JDBCNationalParkDAO;
import com.techelevator.nationalpark.NationalPark;
import com.techelevator.nationalpark.NationalParkDAO;
import com.techelevator.reservation.JDBCReservationDAO;
import com.techelevator.reservation.Reservation;
import com.techelevator.reservation.ReservationDAO;
import com.techelevator.site.JDBCSiteDAO;
import com.techelevator.site.Site;
import com.techelevator.site.SiteDAO;

public class CampgroundCLI {

	private NationalParkDAO nationalParkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;
	private ParkMenu menu;
	
	private static final String MAIN_MENU_HEADING = "Welcome to the National Park Campground Finder";
	private static final String MAIN_SEARCH_FOR_ALL_PARKS = "Search for all parks in the system";
	private static final String MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK = "Search for camgrounds by park";
	private static final String MAIN_SEARCH_FOR_CAMPGROUND_AVAILABILITY = "Search for availabiltiy by campground";
	private static final String MAIN_MENU_EXIT_APP = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_HEADING, 
			                                                         MAIN_SEARCH_FOR_ALL_PARKS, 
			                                                         MAIN_SEARCH_FOR_CAMPGROUNDS_BY_PARK, 
			                                                         MAIN_SEARCH_FOR_CAMPGROUND_AVAILABILITY, 
			                                                         MAIN_MENU_EXIT_APP };

	DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US);
	
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
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
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
				String[] parksInSystem = allParksToChoiceArray("Which park would you like to see the campgrounds for?");
				printHeading(parksInSystem);
				String parkChoice = (String)menu.getChoiceFromOptions(parksInSystem);
				List<Campground> campgroundList = campgroundDAO.findCampgroundByPark(parkChoice);
				for (Campground campground : campgroundList) {
					System.out.println();
					campgroundDAO.printCampgroundInformation(campground);
				}
			}
			else if (choice == MAIN_SEARCH_FOR_CAMPGROUND_AVAILABILITY) {
				String[] parksInSystem = allParksToChoiceArray("Which park would you like to see the campgrounds for?");
				printHeading(parksInSystem);
				String parkChoice = (String)menu.getChoiceFromOptions(parksInSystem);
				
				String[] campgroundsInPark = campgroundsByParkToChoiceArray(parkChoice, "Choose a campground to check availability for");
				printHeading(campgroundsInPark);
				String campgroundChoice = (String)menu.getChoiceFromOptions(campgroundsInPark);
				Campground campgroundToSearch = campgroundDAO.findCampgroundByName(campgroundChoice);
				LocalDate[] dateRange = getDateRange(campgroundToSearch);
				
//				first get list of all sites without reservations at all
				List<Site> availableSites = siteDAO.searchForAllSitesWithNoReservations(campgroundToSearch);
				
//				if that is null, pulls in list of reservations by campground
				if (availableSites.isEmpty()) {
					Campground campground = campgroundDAO.findCampgroundByName(parkChoice);
					availableSites = siteDAO.searchForSitesByCampground(campground);
					LocalDate openDate = campgroundDAO.getOpenDate(dateRange[0], campground);
					LocalDate closeDate = campgroundDAO.getCloseDate(dateRange[1], campground);
		
					for (Site site : availableSites) {
						List<Reservation> reservationList = reservationDAO.getAllReservationsBySite(site);
						
						if ((dateRange[0].isAfter(openDate)) && dateRange[0].isBefore((reservationList.get(0)).getFromDate())) {
							availableSites.add(site);
						}
						
						for (int i = 0; i < reservationList.size() - 2; i++) {
							if (dateRange[0].isAfter(reservationList.get(i).getToDate()) && dateRange[1].isBefore(reservationList.get(i + 1).getFromDate())) {
								availableSites.add(site);
								break;
							}
						}
						if ((dateRange[1].isBefore(closeDate)) && dateRange[1].isAfter((reservationList.get(reservationList.size() - 1)).getToDate())) {
							availableSites.add(site);
						}
					}	
				}
				
				if (availableSites.size() < 5) {
					for (int i = 0; i < availableSites.size(); i++) {
						System.out.println(availableSites.get(i));						
					}
				}
				else {
					for (int i = 0; i < 5; i++) {
						System.out.println((availableSites.get(i)).getSiteNumber());
					}
				}
			}
			
			else if (choice == MAIN_MENU_EXIT_APP) {
				System.exit(0);
			}
		}
	}
	
	public String[] allParksToChoiceArray(String choiceToMake) {
		List<NationalPark> parkList = nationalParkDAO.getAllParksInSystem();
		String[] parksInSystem = new String[parkList.size() + 1];
		parksInSystem[0] = choiceToMake;
		for (int i = 1; i <= parkList.size(); i++) {
			parksInSystem[i] = (parkList.get(i - 1)).getName();
		}
		return parksInSystem;
	}
	
	public String[] campgroundsByParkToChoiceArray(String natParkChoice, String choiceToMake) {
		List<Campground> campgroundByPark = campgroundDAO.findCampgroundByPark(natParkChoice);
		String[] choiceArray = new String[campgroundByPark.size() + 1];
		choiceArray[0] = choiceToMake;
		for (int i = 1; i <= campgroundByPark.size(); i++) {
			choiceArray[i] = (campgroundByPark.get(i - 1)).getName();
		}
		return choiceArray;
	}
	
//	insures we will only get a valid date range
	public LocalDate[] getDateRange(Campground campgroundToSearch) {
		LocalDate[] dateRange = new LocalDate[2];
		boolean fromIsValid = false;
		boolean toIsValid = false;
		
		while (true) {
			while (fromIsValid == false) {
				String startDate = getUserInput("Enter arrival date in yyyy-mm-dd format");
				if (startDate.length() != 10) {
					System.out.println("Invalid date format, please try again");
				}
				else {
					dateRange[0] = LocalDate.parse(startDate, localDateFormatter);
				}
				
				if (campgroundDAO.isValidStartDate(dateRange[0], campgroundToSearch)) {
					fromIsValid = true;
				}
				else {
					System.out.println("This campground will not be open on that start date, please try again");
				}
			}

			while (toIsValid == false) {
				String endDate = getUserInput("Enter departure date in yyyy-mm-dd format");
				if (endDate.length() != 10) {
					System.out.println("Invalid date format, please try again");
				}
				else {
					dateRange[1] = LocalDate.parse(endDate, localDateFormatter);				
				}
				
				if (campgroundDAO.isValidEndDate(dateRange[1], campgroundToSearch)) {
					toIsValid = true;
				}
				else {
					System.out.println("This campground will be closed by that end date, please try again");
				}
			}
			if (dateRange[0].isAfter(dateRange[1])) {
				System.out.println("End date must be after start date, please try again");
			}
			else {
				return dateRange;
			}
		}
	}
	
	private void printSiteInformation(List<Site> availableSites) {
		for (Site site : availableSites) {
			System.out.println("Site number: " + site.getSiteNumber());
			System.out.println("Length of stay: " + );
			System.out.println("Cost for visit: " + );
			System.out.println();
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