package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomServiceService {

    @Autowired
    private CustomServiceRepository customServiceRepository;

    public CustomService saveCustomService(CustomService customService) {
        return customServiceRepository.save(customService);
    }

    public CustomService getCustomServiceByName(String serviceName) {
        return customServiceRepository.findByServiceName(serviceName);
    }

    public CustomService editCustomService(String id, CustomService updatedService) throws ServiceNotFoundException {
        // Find the existing service by its ID
        CustomService existingService = customServiceRepository.findById(id).orElse(null);
            // Update the fields of the existing service
            existingService.setServiceName(updatedService.getServiceName());
            existingService.setTaskDetails(updatedService.getTaskDetails());

            // Save the updated service
            return customServiceRepository.save(existingService);

    }



    public List<CustomService> getAllServices() {
        return customServiceRepository.findAll();
    }
    public Optional<CustomService> getServiceById(String id) {
        return customServiceRepository.findById(id);
    }




}
