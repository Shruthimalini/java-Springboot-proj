package com.example.coronavirus_tracker;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.io.StringReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoronaVirusDataService {

    private static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/refs/heads/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allStats = new ArrayList<>();
    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct
    public void fetchVirusData() throws IOException, InterruptedException {
    	List<LocationStats> newStats = new ArrayList<>();
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(VIRUS_DATA_URL))
            .build();

    HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
    StringReader csvBody = new StringReader(httpResponse.body());
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build()
            .parse(csvBody);
    for (CSVRecord record : records) {
        LocationStats locStat = new LocationStats();
        locStat.setState(record.get("Province/State"));
        locStat.setCountry(record.get("Country/Region"));
        int latestCases=Integer.parseInt(record.get(record.size() - 1));
        int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
        locStat.setLatestTotalCases(latestCases);  
        locStat.setDiffFromPrevDays(latestCases - prevDayCases);  
        newStats.add(locStat);
        }
    this.allStats = newStats;
    }
    
        @Scheduled(cron = "* * * * * *")
        public void scheduleTask() throws IOException, InterruptedException {
            fetchVirusData();
    
    }    
 
    
}