package com.example.coronavirus_tracker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Controller
	public class HomeController {
	@Autowired
    private CoronaVirusDataService coronaVirusDataService;
	@GetMapping("/")  
	  public String home(Model model)
	{
		 List<LocationStats> allStats = coronaVirusDataService.getAllStats();
	        int totalReportedCases = allStats.stream()
	                                          .mapToInt(LocationStats::getLatestTotalCases)
	                                          .sum();
	        

	        model.addAttribute("locationStats", allStats);  
	        model.addAttribute("totalReportedCases", totalReportedCases);
		
		  return"home";
		  
	  }

}
