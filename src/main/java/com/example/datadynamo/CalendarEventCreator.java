package com.example.datadynamo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class CalendarEventCreator {

    // Get access token
    public  String getAccessToken() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Prepare the request body
      
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());
            String accessToken = json.get("access_token").asText();
            System.out.println("Access token retrieved successfully.");
            return accessToken;
        } else {
            System.out.println("Failed to retrieve access token.");
            System.out.println(response.statusCode() + ": " + response.body());
            return null;
        }
    }

    // Helper method to calculate the next business day
    LocalDateTime getNextBusinessDay(LocalDateTime dateTime, int businessDays) {
        LocalDateTime nextBusinessDay = dateTime;
        int addedDays = 0;

        while (addedDays < businessDays) {
            nextBusinessDay = nextBusinessDay.plusDays(1);
            if (!(nextBusinessDay.getDayOfWeek().getValue() == 6 || nextBusinessDay.getDayOfWeek().getValue() == 7)) {
                addedDays++;
            }
        }
        return nextBusinessDay;
    }

    // Create an event
    public  void createEvent(String accessToken, String subject, LocalDateTime startDate, String attendeeEmail) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Define start and end times
        String startDateTime = startDate.format(formatter);
        String endDateTime = startDate.plusHours(1).format(formatter); // 1-hour duration

        String eventJson = String.format("""
                {
                    "subject": "%s",
                    "start": {
                        "dateTime": "%s",
                        "timeZone": "Pacific Standard Time"
                    },
                    "end": {
                        "dateTime": "%s",
                        "timeZone": "Pacific Standard Time"
                    },
                    "location": {
                        "displayName": "Conference Room 1"
                    },
                    "attendees": [
                        {
                            "emailAddress": {
                                "address": "%s",
                                "name": "Client"
                            },
                            "type": "required"
                        }
                    
                       
                    ]
                }
                """, subject, startDateTime, endDateTime,attendeeEmail);

        // Create the POST request for the event
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://graph.microsoft.com/v1.0/me/events"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(eventJson))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            System.out.println("Event created successfully.");
            System.out.println("Response: " + response.body());
        } else {
            System.out.println("Failed to create event.");
            System.out.println(response.statusCode() + ": " + response.body());
        }
    }

//    public static void main(String[] args) {
//        try {
//            // Step 1: Get access token
//            String accessToken = getAccessToken();
//
//            if (accessToken != null) {
//                LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Los_Angeles")).with(LocalTime.of(8, 0));
//
//                // Create event for +1 business day
//                LocalDateTime oneBusinessDayLater = getNextBusinessDay(now, 1);
//                createEvent(accessToken, "Contact Client", oneBusinessDayLater);
//
//                // Create event for +5 business days
//                LocalDateTime fiveBusinessDaysLater = getNextBusinessDay(now, 5);
//                createEvent(accessToken, "Complete Client Intake Interview", fiveBusinessDaysLater);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
