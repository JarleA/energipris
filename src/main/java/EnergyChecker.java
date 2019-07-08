import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class EnergyChecker {
    public static void main(String[] args) throws Exception {

       /*
       Use Tibber API to get power data.
        - Talk to https endpoint https://api.tibber.com/v1-beta/gql
            - Use POST request
            - Use Json data
        - send query to endpoint

       then print requested values.
        Authorization: Bearer d1007ead2dc84a2b82f0de19451c5fb22112f7ae11d19bf2bedb224a003ff74a
        */


        URL url = new URL("https://api.tibber.com/v1-beta/gql");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer d1007ead2dc84a2b82f0de19451c5fb22112f7ae11d19bf2bedb224a003ff74a");
        con.setDoOutput(true);
        String jsonInputString = "{ \"query\": \"{viewer {homes {currentSubscription {priceInfo {current {total energy tax startsAt }}}}}}\" }";


        // Sending
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Receive
        InputStream is;
        String response;
        if (con.getResponseCode() >= 400) {
            String error = readStream(con.getErrorStream());
            throw new Exception(error);
        } else {
            response = readStream(con.getInputStream());
        }

        JSONObject jsonObject = new JSONObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject viewer = data.getJSONObject("viewer");
        JSONArray homes = viewer.getJSONArray("homes");
        JSONObject home = homes.getJSONObject(0);
        System.out.println(home);
        //JSONObject currentSubscription = jsonObject.getJSONObject("currentSubscription");
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
}