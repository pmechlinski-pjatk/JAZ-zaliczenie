package com.pjatk.nbp.project.service;

import org.json.*;
import com.pjatk.nbp.project.model.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RestService {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    DateService date;

    public <Optional>Entry getNbpData(String currency, LocalDate startDate, LocalDate endDate) {
        String currencyResourceUrl = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/" + startDate + "/" + endDate + "/?format=json";
        ResponseEntity<String> response = restTemplate.getForEntity(currencyResourceUrl, String.class);
        if (! response.getStatusCode().is2xxSuccessful()) {
            System.out.println("[DB_LOG][ERROR] " + date.getTime() + " GET rest query failed with a code: " + response.getStatusCode());
            return null;
        }
        String responseString = response.getBody().toString();
        System.out.println(responseString);
        JSONObject responseJSON = new JSONObject(responseString);
        JSONArray dailyEntries = responseJSON.getJSONArray("rates");

        // Create a hashtable
        Map<Double, LocalDate> ratesArray = new Hashtable();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < dailyEntries.length(); i++) {
            ratesArray.put(dailyEntries.getJSONObject(i).getDouble("mid"), LocalDate.parse(dailyEntries.getJSONObject(i).getString("effectiveDate"), formatter));
        }
        System.out.println("ratesArray:" + ratesArray);

        // Sort hashtable
        Set<Double> keys = ratesArray.keySet();
        Iterator<Double> itr = keys.iterator();
        List<Double> sortedDayMids = new ArrayList<>();

        while (itr.hasNext()) {
            Double i = itr.next();
            sortedDayMids.add(i);
        }
        Collections.sort(sortedDayMids);
        System.out.println("sortedDayMids: " + sortedDayMids);
        double median;
        if (sortedDayMids.size()%2 == 1) {
            median = (sortedDayMids.get(sortedDayMids.size() / 2) + sortedDayMids.get(sortedDayMids.size() / 2 - 1))/2;
        } else {
            median = sortedDayMids.get(sortedDayMids.size() / 2);
        }
        System.out.println("Median: "+median);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new Entry(currency, startDate, endDate, median, timestamp);
    }

    @Override
    public String toString() {
        JSONObject entry = new JSONObject(this);
        return entry.toString();
    }

}
