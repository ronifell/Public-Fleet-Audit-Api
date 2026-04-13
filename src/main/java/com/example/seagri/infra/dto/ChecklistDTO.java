package com.example.seagri.infra.dto;

import com.example.seagri.infra.model.VehicleTravel;

public record ChecklistDTO (
    VehicleTravel travel,
    String assinaturaResponsavel,
    String assinaturaMotorista
) {

    public ChecklistDTO DTOFromPost(VehicleTravel travel, String assinaturaResponsavel, String assinaturaMotorista){
        ChecklistDTO dto = new ChecklistDTO(
            travel,
            assinaturaResponsavel,
            assinaturaMotorista
        );        
        return dto;
    }
    
}
