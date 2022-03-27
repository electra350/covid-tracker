package io.opencodehub.covidtracker.controllers;

import io.opencodehub.covidtracker.models.LocationStats;
import io.opencodehub.covidtracker.services.CovidTrackerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController
{
    @Autowired
    CovidTrackerDataService covidTrackerDataService;

    @GetMapping("/")
    public String home(Model model)
    {
        List<LocationStats> allStats = covidTrackerDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getNewCases()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}
