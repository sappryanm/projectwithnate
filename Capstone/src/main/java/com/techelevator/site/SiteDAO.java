package com.techelevator.site;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.techelevator.campground.Campground;

public interface SiteDAO {
    public List<Site> searchForSitesByCampground(Campground campgroundChoice);
    public List<Site> searchForAllSitesWithNoReservations(Campground campground);
    public BigDecimal getDailyFeeBySiteId(Site site);
}
