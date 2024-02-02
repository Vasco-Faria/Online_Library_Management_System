package pt.ua.deti.ies.ReadEase.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.AlertService;

import pt.ua.deti.ies.ReadEase.model.Alert;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/today")
    public ResponseEntity<List<Alert>> getAlertsForCurrentDay() {
        try {
            List<Alert> currentDayAlerts = alertService.getAlertsForCurrentDay();
            return ResponseEntity.ok(currentDayAlerts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

