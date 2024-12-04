package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    // List all regions
    public List<Region> listAllRegions() {
        return regionRepository.findAll();
    }

    // Add a new region by name
    public Region addRegionByName(String name) {
        Region region = new Region(); // Create a new Region object
        region.setName(name); // Set the name from the input
        return regionRepository.save(region); // Save the region to the database
    }

    // Remove a region by ID
    public boolean removeRegionById(String id) {
        Optional<Region> region = regionRepository.findById(id);
        if (region.isPresent()) {
            regionRepository.deleteById(id);
            return true;
        }
        return false; // Region not found
    }

    // Remove a region by name
    public boolean removeRegionByName(String name) {
        List<Region> regions = regionRepository.findAll();
        if (regions.stream().anyMatch(region -> region.getName().equalsIgnoreCase(name))) {
            regionRepository.deleteByName(name);
            return true;
        }
        return false; // Region not found
    }
}
