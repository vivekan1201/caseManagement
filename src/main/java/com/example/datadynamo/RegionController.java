package com.example.datadynamo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions") // Base path for all region-related APIs
public class RegionController {

    @Autowired
    private RegionService regionService;

    // List all regions
    @GetMapping
    public ResponseEntity<List<Region>> getAllRegions() {
        List<Region> regions = regionService.listAllRegions();
        return ResponseEntity.ok(regions);
    }

    // Add a new region by name
    @PostMapping
    public ResponseEntity<Region> createRegion(@RequestParam String name) {
        Region createdRegion = regionService.addRegionByName(name);
        return ResponseEntity.ok(createdRegion);
    }

    // Remove a region by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegionById(@PathVariable String id) {
        boolean isDeleted = regionService.removeRegionById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Region deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Region not found.");
        }
    }

    // Remove a region by name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteRegionByName(@PathVariable String name) {
        boolean isDeleted = regionService.removeRegionByName(name);
        if (isDeleted) {
            return ResponseEntity.ok("Region deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Region not found.");
        }
    }
}
