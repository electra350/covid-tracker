package io.opencodehub.covidtracker.services;

import com.google.json.JsonSanitizer;
import io.opencodehub.covidtracker.models.LocationStats;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CovidTrackerDataService
{
    @Autowired
    private final RestTemplate restTemplate;

    String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats ()
    {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchData () throws
        IOException
    {
        fetchData(new Date());
    }

    private void fetchData (Date date) throws IOException
    {
        try {
            List<LocationStats> newStats = new ArrayList<>();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            String queryDateString = df.format(date);
            ResponseEntity<String> response = restTemplate.exchange(
                VIRUS_DATA_URL + queryDateString + ".csv",
                HttpMethod.GET,
                entity,
                String.class);

            StringReader csvBodyReader = new StringReader(response.getBody().toString());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(
                csvBodyReader);
            for (CSVRecord record : records) {
                LocationStats stats = new LocationStats();
                stats.setCountry(record.get("Country_Region"));
                stats.setState(record.get("Province_State"));
                stats.setLatestTotalCases(Integer.parseInt(record.get("Confirmed")));
                newStats.add(stats);
            }
            this.allStats = newStats;
            populatePreviousDayData(this.allStats,
                Date.from(date.toInstant().minus(Duration.ofDays(1))));
        }
        catch (HttpClientErrorException.NotFound ex) {
            fetchData(Date.from(date.toInstant().minus(Duration.ofDays(1))));
        }
    }

    private void populatePreviousDayData (List<LocationStats> allStats, Date date) throws
        IOException
    {
        List<LocationStats> newStats = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String queryDateString = df.format(date);
        ResponseEntity<String> response = restTemplate.exchange(
            VIRUS_DATA_URL + queryDateString + ".csv",
            HttpMethod.GET,
            entity,
            String.class);

        StringReader csvBodyReader = new StringReader(response.getBody().toString());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(
            csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats stats = new LocationStats();
            stats.setCountry(record.get("Country_Region"));
            stats.setState(record.get("Province_State"));
            Optional<LocationStats> locationStat = allStats.stream().filter(p -> p.equals(
                stats)).findFirst();
            if (locationStat.isPresent()) {
                LocationStats location = locationStat.get();
                location.setNewCases(
                    location.getLatestTotalCases() - Integer.parseInt(record.get(
                        "Confirmed")));
            }
        }
    }

    public ResponseEntity<String> getCleanedResponseBody (ResponseEntity<String> response)
    {
        String responseBody = response.getBody();
        if (responseBody == null) {
            return new ResponseEntity<>(response.getStatusCode());
        }
        else if (responseBody.isEmpty()) {
            return new ResponseEntity<>("", response.getStatusCode());
        }
        String cleanResponseBody = JsonSanitizer.sanitize(responseBody);

        return new ResponseEntity<>(cleanResponseBody, response.getStatusCode());
    }
}
