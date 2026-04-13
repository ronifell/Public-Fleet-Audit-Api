package com.example.seagri.infra.controller;

import java.io.FileNotFoundException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.config.UserProfile;
import com.example.seagri.infra.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService service;

    private String currentUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserProfile up) {
            return up.user.getUserName();
        }
        return "system";
    }

    @SuppressWarnings("null")
    @GetMapping("/")
    public ResponseEntity<byte[]> getAll(
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate
    ) throws FileNotFoundException, JRException {

        byte[] response = this.service.getAll(startDate, endDate, currentUserLogin());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=teste.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF).body(response);
    }

    @SuppressWarnings("null")
    @GetMapping("/vehicle")
    public ResponseEntity<byte[]> getByVehicle(
        @RequestParam("vehiclePlate") String vehiclePlate,
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate
    ) throws FileNotFoundException, JRException {
        
        byte[] response = this.service.getByVehicle(vehiclePlate, startDate, endDate, currentUserLogin());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=teste.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF).body(response);
    }

    @SuppressWarnings("null")
    @GetMapping("/type")
    public ResponseEntity<byte[]> getByVehicleType(
        @RequestParam("typeId") Integer typeId,
        @RequestParam("typeName") String typeName,
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate
    ) throws FileNotFoundException, JRException {
        byte[] response = service.getByType(typeId, typeName, startDate, endDate, currentUserLogin());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=teste.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF).body(response);
    }

}
