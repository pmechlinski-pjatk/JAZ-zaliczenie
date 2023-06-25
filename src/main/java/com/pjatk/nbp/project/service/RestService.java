package com.pjatk.nbp.project.service;

import org.json.*;
import com.pjatk.nbp.project.model.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Service
public class RestService {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    DateService date;

    public <Optional>Entry getNbpData(String currency, int daysRange) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1 * daysRange);
        String currencyResourceUrl = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/last/" + daysRange + "/?format=json";
        ResponseEntity<String> response = restTemplate.getForEntity(currencyResourceUrl, String.class);
        if (! response.getStatusCode().is2xxSuccessful()) {
            System.out.println("[DB_LOG][ERROR] " + date.getTime() + " GET rest query failed with a code: " + response.getStatusCode());
            return null;
        }
        String responseString = response.getBody().toString();
        System.out.println(responseString);
        JSONObject responseJSON = new JSONObject(responseString);
        JSONArray dailyEntries = responseJSON.getJSONArray("rates");
        double sum = 0;
        for (int i = 0; i < dailyEntries.length(); i++){
            sum += dailyEntries.getJSONObject(i).getDouble("mid");
        }
        double average = sum / Double.valueOf(daysRange);

        return new Entry(currency, daysRange, average, cal.getTime());
    }
}
