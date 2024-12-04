package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientIntakeController {

    @Autowired
    private ClientIntakeRepository clientIntakeRepository;

    @Autowired
    private ClientIntakeService clientIntakeService;




    @GetMapping("/api/clients")
    public ResponseEntity<List<ClientIntake>> getClientsByFilters(
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String staff) {

        List<ClientIntake> clients;

        if (service != null && staff != null) {
            // Filter by both service and staff
            clients = clientIntakeRepository.findByServiceAndStaff(service, staff);
        } else if (service != null) {
            // Filter by service only
            clients = clientIntakeRepository.findByService(service);
        } else if (staff != null) {
            // Filter by staff only
            clients = clientIntakeRepository.findByStaff(staff);
        } else {
            // No filters, return all clients
            clients = clientIntakeRepository.findAll();
        }

        return ResponseEntity.ok(clients);
    }

    @GetMapping("/api/myclients")
    public ResponseEntity<List<ClientIntake>> getClientsByStaff(
            @RequestParam(required = false) String staff) {

        List<ClientIntake> clients;
        clients = clientIntakeRepository.findByStaff(staff);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/api/search")
    public List<ClientIntake> searchClients(
            @RequestParam String searchText,
            @RequestParam String staff
    ) {
        return clientIntakeRepository.keywordSearch(searchText, staff);
    }
    @GetMapping("/api/recentClients")
    public ResponseEntity<List<ClientIntake>> getRecentClientsByStaff(@RequestParam String staff) {
        // Fetch clients sorted by startDate with staff filter
        List<ClientIntake> recentClients = clientIntakeRepository.findByStaffSortedByStartDate(staff);
        // Limit to top 5 results
        return ResponseEntity.ok(recentClients.stream().limit(5).toList());
    }
    @GetMapping("/api/filterClients")
    public List<ClientIntake> getFilteredClients(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String staff,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String startDate) {

        return clientIntakeService.getFilteredClients(region, staff, service, startDate);
    }

}
