package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Controller
public class RecordClient {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    SessionService sessionService;


    @Autowired
    private CalendarEventCreator calendarEventCreator;

    @Autowired
    private ClientIntakeService clientIntakeService; // Inject ClientIntakeService

    @Autowired
    EventService eventService;

    @Autowired
    TaskService taskService;

    @Autowired
    CustomServiceService customServiceService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

    @PostMapping("/api/submitTask")
    public ResponseEntity<String> submitTask(
            @RequestParam("service") String service,
            @RequestParam("lastName") String lastName,
            @RequestParam("firstName") String firstName,
            @RequestParam("referenceNumber") String referenceNumber,
            @RequestParam("referralDate") String referralDate,
            @RequestParam("dob") String dob,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("region") String region,
            @RequestParam("staff") String staff,
            @RequestParam("startDate") String startDate,
            Model model) {

        try {
            // Step 1: Parse input data
            LocalDate referralDateParsed = parseDate(referralDate);
            LocalDate dobParsed = parseDate(dob);
            LocalDate startDateParsed = parseDate(startDate);

            // Convert LocalDate to Date
            Date referralDateConverted = Date.from(referralDateParsed.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dobConverted = Date.from(dobParsed.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date startDateConverted = Date.from(startDateParsed.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Step 2: Create and save ClientIntake object
            ClientIntake clientIntake = new ClientIntake();
            clientIntake.setService(service);
            clientIntake.setLastName(lastName);
            clientIntake.setFirstName(firstName);
            clientIntake.setReferenceNumber(referenceNumber);
            clientIntake.setReferralDate(referralDateConverted);
            clientIntake.setDob(dobConverted);
            clientIntake.setAddress(address);
            clientIntake.setPhone(phone);
            clientIntake.setEmail(email);
            clientIntake.setRegion(region);
            clientIntake.setStaff(staff);
            clientIntake.setStartDate(startDateConverted);

            // Save the client intake details
            clientIntakeService.saveClientIntake(clientIntake);

            // Step 3: Add tasks based on service
            addTasks(clientIntake);

            // Step 4: Get staff email
            String staffEmail = sessionService.getLoggedInEmail();
            if (staffEmail == null) {
                staffEmail = "sarah.insight@outlook.com";
            }

            // Step 5: Get access token for Google Calendar
            String accessToken = calendarEventCreator.getAccessToken();
            if (accessToken != null) {
                LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Los_Angeles")).with(LocalTime.of(8, 0));

                // Fetch CustomService details based on the client's service name
                CustomService customService = customServiceService.getCustomServiceByName(clientIntake.getService());
                if (customService == null) {
                    return ResponseEntity.status(400).body("Invalid service name: " + clientIntake.getService());
                }

                // Loop through tasks for the service and dynamically create events
                for (Stask task : customService.getTaskDetails()) {
                    LocalDateTime taskDueDate;
                    String reminder;

                    // Handle specific tasks like "Contact Client Monthly"
                    if ("Contact Client Monthly".equalsIgnoreCase(task.getTaskType())) {
                        Date serviceStartDate = clientIntake.getStartDate();
                        taskDueDate = serviceStartDate.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                                .plusMonths(1); // Monthly task due one month later

                        calendarEventCreator.createEvent(accessToken, "Monthly Client Check-In for " + firstName + " " + lastName, taskDueDate, staffEmail);
                        Event newEvent = new Event(taskDueDate, "Monthly Client Check-In for " + firstName + " " + lastName, clientIntake.getStaff(), firstName + " " + lastName);
                        eventService.saveEvent(newEvent);
                    } else {
                        taskDueDate = calendarEventCreator.getNextBusinessDay(now, task.getBusinessDays());
                        if (task.getTaskType().contains("Contact Client")) {
                            reminder = "Contact " + firstName + " " + lastName;
                            calendarEventCreator.createEvent(accessToken, reminder, taskDueDate, staffEmail);
                        } else if (task.getTaskType().contains("Interview")) {
                            reminder = "Complete " + task.getTaskType() + " for " + firstName + " " + lastName;
                            calendarEventCreator.createEvent(accessToken, reminder, taskDueDate, staffEmail);
                        } else {
                            reminder = task.getTaskType() + " for " + firstName + " " + lastName;
                            calendarEventCreator.createEvent(accessToken, reminder, taskDueDate, staffEmail);
                        }

                        // Create and save event in database
                        Event newEvent = new Event(taskDueDate, reminder, clientIntake.getStaff(), firstName + " " + lastName);
                        eventService.saveEvent(newEvent);
                    }
                }
            }

            return ResponseEntity.ok("Client details submitted and email sent successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send email or create event.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to process the task.");
        }
    }

    private void addTasks(ClientIntake clientIntake) {
        String fullname = clientIntake.getFirstName() + " " + clientIntake.getLastName();
        String taskStatus = "Pending";
        String service = clientIntake.getService();
        String assignedEmployee = clientIntake.getStaff();

        Task contactTask = new Task(fullname, "Contact " + fullname + " within 1 business day", taskStatus, service, assignedEmployee);
        Task interviewTask = new Task(fullname, "Complete Intake Interview with " + fullname + " within 2 weeks", taskStatus, service, assignedEmployee);
        Task actionTask = new Task(fullname, "Complete Employment Action Plan for " + fullname, taskStatus, service, assignedEmployee);
        Task monthlyTask = new Task(fullname, "Monthly Client Check-In for " + fullname, taskStatus, service, assignedEmployee);

        taskService.saveTask(contactTask);
        taskService.saveTask(interviewTask);
        taskService.saveTask(actionTask);
        taskService.saveTask(monthlyTask);
    }

    public LocalDate parseDate(String dateString) {
        try {
            // Parse the input date string to LocalDate
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            System.err.println("Invalid date format: " + dateString);
            return null;
        }
    }

}
