package com.example.datadynamo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChartController {

    @Autowired
    private ClientIntakeService clientIntakeService;

    @GetMapping("/api/chartData")
    public List<ClientIntake> getChartData() {
        return clientIntakeService.getAllClientIntakes();
    }


}
