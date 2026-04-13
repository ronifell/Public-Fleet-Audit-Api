package com.example.seagri.infra.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.Maintenance;
import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.model.VehicleTravel;
import com.example.seagri.infra.service.MaintenanceService;
import com.example.seagri.infra.service.SupplyService;
import com.example.seagri.infra.service.VehicleTravelService;


/**
 * Endpoints de verificação de integridade SHA-256.
 *
 * Permite auditar se um registro de telemetria foi alterado
 * após sua criação, comparando o hash armazenado com o hash
 * recalculado a partir dos dados atuais.
 */
@RestController
@RequestMapping("/integrity")
public class IntegrityController {

    @Autowired
    private HashService hashService;

    @Autowired
    private SupplyService supplyService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private VehicleTravelService travelService;

    /** Verifica a integridade de um registro de abastecimento. */
    @GetMapping("/supply/{id}")
    public ResponseEntity<Map<String, Object>> verifySupply(@PathVariable Long id) {
        Supply supply = supplyService.getById(id);
        if (supply == null) {
            return ResponseEntity.notFound().build();
        }
        return buildResponse(id, "supply", supply.getIntegrityHash(),
                hashService.verify(supply, supply.getIntegrityHash()));
    }

    /** Verifica a integridade de um registro de manutenção. */
    @GetMapping("/maintenance/{id}")
    public ResponseEntity<Map<String, Object>> verifyMaintenance(@PathVariable Long id) {
        Maintenance m = maintenanceService.getById(id);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        return buildResponse(id, "maintenance", m.getIntegrityHash(),
                hashService.verify(m, m.getIntegrityHash()));
    }

    /** Verifica a integridade de um registro de viagem. */
    @GetMapping("/travel/{id}")
    public ResponseEntity<Map<String, Object>> verifyTravel(@PathVariable Long id) {
        VehicleTravel travel = travelService.getEntityById(id);
        if (travel == null) {
            return ResponseEntity.notFound().build();
        }
        return buildResponse(id, "travel", travel.getIntegrityHash(),
                hashService.verify(travel, travel.getIntegrityHash()));
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            Long id, String type, String storedHash, boolean intact) {
        return ResponseEntity.ok(Map.of(
                "id",          id,
                "type",        type,
                "storedHash",  storedHash != null ? storedHash : "SEM HASH",
                "intact",      intact,
                "status",      intact ? "ÍNTEGRO" : "ADULTERADO"
        ));
    }
}
