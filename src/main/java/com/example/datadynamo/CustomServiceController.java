package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
public class CustomServiceController {

    @Autowired
    private CustomServiceService customServiceService;

    @PostMapping("/api/save-custom-service")
    public CustomService saveCustomService(@RequestBody CustomService customService) {
        return customServiceService.saveCustomService(customService);
    }

    @PutMapping("/api/updateService/{id}")
    public ResponseEntity<CustomService> updateService(
            @PathVariable String id,
            @RequestBody CustomService updatedService) throws ServiceNotFoundException {
        CustomService updated = customServiceService.editCustomService(id, updatedService);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/api/custom-services")
    public List<CustomService> getAllServices() {
        return customServiceService.getAllServices();
    }

    @GetMapping("/api/getServiceByName/{serviceName}")
    public CustomService getServiceByName(@PathVariable String serviceName) {
        return customServiceService.getCustomServiceByName(serviceName);
    }

    @GetMapping("/api/getServiceById/{id}")
    public Optional<CustomService> getServiceById(@PathVariable String id) {
        return customServiceService.getServiceById(id);
    }


}
