package org.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.event.Event;
import org.example.event.EventData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Get {
    // Your URL here
    private final String apiUrl = "http://localhost:8080/v1/dataset";

    /**
     * Retrieves a list of events from an external service.
     *
     * @return The list of events.
     * @throws IOException If an error occurs during the retrieval process.
     */
    public List<Event> getServiceResponse() throws IOException {
        List<Event> eventList = new ArrayList<>();

        try {
            // Define the URL
            URL url = new URL(apiUrl);

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse JSON response and populate eventList using Jackson
                ObjectMapper objectMapper = new ObjectMapper();

                // Use EventData class if your JSON has a root object with an "events" field
                EventData eventData = objectMapper.readValue(response.toString(), EventData.class);
                eventList = eventData.getEvents();

                // Alternatively, if your JSON is an array of events without a root object
                // eventList = objectMapper.readValue(response.toString(), new TypeReference<List<Event>>() {});

            } else {
                throw new IOException("Failed to get data from the external service. Response code: " + responseCode);
            }

        } catch (IOException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw new IOException("Error during the retrieval process", e);
        }

        return eventList;
    }
}
