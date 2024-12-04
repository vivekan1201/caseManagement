package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ClientIntakeService {

    @Autowired
    private ClientIntakeRepository clientIntakeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public ClientIntake saveClientIntake(ClientIntake clientIntake) {
        return clientIntakeRepository.save(clientIntake);
    }

    public List<ClientIntake> getAllClientIntakes() {
        return clientIntakeRepository.findAll();
    }

    // New method to filter by service type
    public List<ClientIntake> getClientIntakesByService(String service) {
        return clientIntakeRepository.findByService(service);
    }

    // New method to filter by staff
    public List<ClientIntake> getClientIntakesByStaff(String staff) {
        return clientIntakeRepository.findByStaff(staff);
    }

    // New method to filter by both service type and staff
    public List<ClientIntake> getClientIntakesByServiceAndStaff(String service, String staff) {
        return clientIntakeRepository.findByServiceAndStaff(service, staff);
    }

    public List<ClientIntake> getFilteredClients(String region, String staff, String service, String startDate) {
        Date startDateFilter = null;
        Date endDateFilter = null;

        // If "All Dates" is passed, we don't apply any date filter
        if (startDate != null && !startDate.isEmpty() && !startDate.equalsIgnoreCase("All Dates")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
            try {
                Date monthStartDate = dateFormat.parse(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(monthStartDate);

                // Set the start of the month
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDateFilter = calendar.getTime();

                // Set the end of the month
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                endDateFilter = calendar.getTime();
            } catch (ParseException e) {
                e.printStackTrace(); // Handle invalid date format
            }
        }

        // If "All Regions" or "All Services" is passed, set the respective parameters to null
        if ("All Regions".equalsIgnoreCase(region)) {
            region = null;
        }
        if ("All Services".equalsIgnoreCase(service)) {
            service = null;
        }

        // Build query dynamically
        Query query = new Query(); // This will create a query object

        // Add filters based on parameters if they are not null
        if (region != null) {
            query.addCriteria(Criteria.where("region").is(region));
        }
        if (staff != null) {
            query.addCriteria(Criteria.where("staff").is(staff));
        }
        if (service != null) {
            query.addCriteria(Criteria.where("service").is(service));
        }
        if (startDateFilter != null && endDateFilter != null) {
            query.addCriteria(Criteria.where("startDate").gte(startDateFilter).lte(endDateFilter));
        }

        // Execute the query
        return mongoTemplate.find(query, ClientIntake.class);
    }


}
