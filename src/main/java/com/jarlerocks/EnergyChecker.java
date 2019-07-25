package com.jarlerocks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

public class EnergyChecker {
    public static JSONObject requestTibbers(String auth, String query) throws Exception {
        URL url = new URL("https://api.tibber.com/v1-beta/gql");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", auth);
        con.setDoOutput(true);

        String requestJson = "{ \"query\": \"" + query + "\" }";

        // Sending
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Receive
        String response;
        if (con.getResponseCode() >= 400) {
            String error = readStream(con.getErrorStream());
            throw new Exception(error);
        } else {
            response = readStream(con.getInputStream());
        }

        return new JSONObject(response);
    }

    public static JSONObject getHome() throws Exception {

       /*
       Use Tibber API to get power data.
        - Talk to https endpoint https://api.tibber.com/v1-beta/gql
            - Use POST request
            - Use Json data
        - send query to endpoint

       then print requested values.
        Authorization: Bearer d1007ead2dc84a2b82f0de19451c5fb22112f7ae11d19bf2bedb224a003ff74a
        */

       JSONObject response = requestTibbers("Bearer d1007ead2dc84a2b82f0de19451c5fb22112f7ae11d19bf2bedb224a003ff74a",
                                      "{viewer {homes {currentSubscription {priceInfo {current {total energy tax startsAt }}}}}}");

       // int i = 1;
       // int n = i + 4;
       // int a = add(n, i);
       // PricePoint[] prices = getLowestPrices(a);

        JSONObject data = response.getJSONObject("data");
        JSONObject viewer = data.getJSONObject("viewer");
        JSONArray homes = viewer.getJSONArray("homes");

        // TODO Are they all the same? - Is it always the first?
        return homes.getJSONObject(0);
    }

    public static PricePoint[] getLowestPrices(int count) throws Exception {
        //sort the prices
        // get best prices
        JSONObject response = requestTibbers("Bearer d1007ead2dc84a2b82f0de19451c5fb22112f7ae11d19bf2bedb224a003ff74a",
                "{viewer{homes{consumption(resolution:HOURLY,last:24){nodes{from to cost unitPrice unitPriceVAT consumption consumptionUnit}}}}}");


        // int i = 1;
        // int n = i + 4;
        // int a = add(n, i);
        // PricePoint[] prices = getLowestPrices(a);


        JSONObject data = response.getJSONObject("data");
        JSONObject viewer = data.getJSONObject("viewer");
        JSONArray homes = viewer.getJSONArray("homes");
        JSONObject home = homes.getJSONObject(0);
        JSONObject consumption = home.getJSONObject("consumption");
        JSONArray nodes = consumption.getJSONArray("nodes");


        PricePoint[] pricePoints = new PricePoint[nodes.length()];

        for(int i = 0; i < nodes.length(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            String time =(node.getString("from"));
            float price =  (node.getFloat("unitPrice"));
            PricePoint pricepoint = new PricePoint(time,price);

            pricePoints[i] = pricepoint;
        }

        Arrays.sort(pricePoints);
        return Arrays.copyOfRange(pricePoints, 0, count);
    }

    private static String readStream(InputStream is) throws Exception {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }


    private static int add(int x, int y) {
        return x + y;
    }
}
