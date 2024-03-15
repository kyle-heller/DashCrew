package com.rollcall.web.services.external;

import com.rollcall.web.services.GeolocationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;


@Service
public class GeolocationServiceImpl implements GeolocationService {

    @Override
    public List<Integer> findClosestZips(int zip) {
        List<Integer> closestZips = new ArrayList<>();
        String urlStr = "http://api.geonames.org/findNearbyPostalCodesJSON?country=US&radius=20&maxRows=100&username=sentientkeying&postalcode=" + zip;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray postalCodes = jsonResponse.getJSONArray("postalCodes");
            for (int i = 0; i < postalCodes.length(); i++) {
                JSONObject postalCode = postalCodes.getJSONObject(i);
                // Assuming the postal code is returned as a string, convert it to an integer.
                // You might need to adjust this part depending on the actual structure and data type.
                int code = Integer.parseInt(postalCode.getString("postalCode"));
                closestZips.add(code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return closestZips;
    }

}
