package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.result.CalculateUserTime;
import org.example.result.Result;
import org.example.result.ResultData;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            // Calculate user time and retrieve the result
            List<Result> resultList = new CalculateUserTime().calculateUserUsedTime().getResult();
            ResultData resultData = new ResultData(resultList);

            // Convert ResultData to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult = objectMapper.writeValueAsString(resultData);

            // Print the JSON result
            System.out.println("JSON Result: " + jsonResult);

            // Replace "http://localhost:8080" with your actual base URL

            // Prepare HTTP connection
            URL url = new URL("http://server:8080/v1/result");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write the JSON data to the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonResult.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Handle the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Check what response: " + responseCode);
            } else {
                System.out.println(responseCode);
                System.out.println("Run the docker-compose up to start this container!");
            }

            // Close the connection
            connection.disconnect();

        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}
