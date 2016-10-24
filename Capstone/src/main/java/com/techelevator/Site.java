package com.techelevator;

public class Site {

		private int site_id;
		private int campground_id;
		private int site_number;
		private int max_occupancy;
		private boolean accessible;
		private int max_rv_length;
		private boolean utilities;
		private Object[] toArray;
		

		public int getSite_id() {
			return site_id;
		}
		public void setSite_id(int site_id) {
			this.site_id = site_id;
		}
		public int getCampground_id() {
			return campground_id;
		}
		public void setCampground_id(int campground_id) {
			this.campground_id = campground_id;
		}
		public int getSite_number() {
			return site_number;
		}
		public void setSite_number(int siteNumber) {
			this.site_number = siteNumber;
		}
		public int getMax_occupancy() {
			return max_occupancy;
		}
		public void setMax_occupancy(int maxOccupancy) {
			this.max_occupancy = maxOccupancy;
		}
		public boolean isAccessible() {
			return accessible;
		}
		public void setAccessible(boolean accessible) {
			this.accessible = accessible;
		}
		public int getMax_rv_length() {
			return max_rv_length;
		}
		public void setMax_rv_length(int max_rv_length) {
			this.max_rv_length = max_rv_length;
		}
		public boolean isUtilities() {
			return utilities;
		}
		public void setUtilities(boolean utilities) {
			this.utilities = utilities;
		}
		public Object[] getToArray() {
			return toArray;
		}
		public void setToArray(Object[] toArray) {
			this.toArray = toArray;
		}

		
	
}
