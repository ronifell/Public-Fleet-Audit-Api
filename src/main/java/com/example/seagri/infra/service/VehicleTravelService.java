package com.example.seagri.infra.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.dto.ChecklistDTO;
import com.example.seagri.infra.dto.TravelDTO;
import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.Checklist;
import com.example.seagri.infra.model.VehicleTravel;
import com.example.seagri.infra.repository.ChecklistRepository;
import com.example.seagri.infra.repository.VehicleTravelRepository;

@Service
public class VehicleTravelService {

    @Autowired
    private VehicleTravelRepository repository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private HashService hashService;

    public List<TravelDTO> getAll() {
        List<VehicleTravel> travels = repository.findAll();
        return travels.stream().map(TravelDTO::DTOFromEntity).toList();
    }

    @SuppressWarnings("null")
    public Page<VehicleTravel> getPage(Pageable page) {
        return repository.findAll(page);
    }

    @SuppressWarnings("null")
    public TravelDTO getById(Long id) {
        return TravelDTO.DTOFromEntity(repository.findById(id).orElse(null));
    }

    @SuppressWarnings("null")
    public VehicleTravel getEntityById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<VehicleTravel> postSave(List<VehicleTravel> objeto) {
        for (VehicleTravel t : objeto) {
            if (t.getIntegrityHash() == null) {
                t.setIntegrityHash(hashService.generate(t));
            }
        }
        @SuppressWarnings("null")
        List<VehicleTravel> travels = repository.saveAll(objeto);
        List<Checklist> checklists = new ArrayList<Checklist>();

        for(VehicleTravel travel : travels) {
            Checklist travelChecklist = new Checklist();
            travelChecklist.setId(travel.getId());
            travelChecklist.setTravel(travel);
            checklists.add(travelChecklist);
        }
        checklistRepository.saveAll(checklists);
        
        return travels;

    }

    public VehicleTravel postSave(VehicleTravel objeto) {
        if (objeto.getIntegrityHash() == null) {
            objeto.setIntegrityHash(hashService.generate(objeto));
        }
        @SuppressWarnings("null")
        VehicleTravel travel = repository.save(objeto);
        Checklist travelChecklist = new Checklist();
        
        travelChecklist.setId(travel.getId());
        travelChecklist.setTravel(travel);
        travelChecklist = checklistRepository.save(travelChecklist);

        objeto.setChecklist(travelChecklist);
        return travel;

    }

    @SuppressWarnings("null")
    public VehicleTravel updateSave(VehicleTravel objeto) {
        checklistRepository.save(objeto.getChecklist());
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public VehicleTravel saveWithSignatures(ChecklistDTO dto) {
        byte[] imagem1;
        byte[] imagem2;
        Checklist check = dto.travel().getChecklist();
        if(!dto.assinaturaResponsavel().equals("")){
            imagem1 = Base64.getDecoder().decode(dto.assinaturaResponsavel());
            check.setAssinaturaResponsavel(imagem1);
        }
        if(!dto.assinaturaMotorista().equals("")){
            imagem2 = Base64.getDecoder().decode(dto.assinaturaMotorista());
            check.setAssinaturaMotorista(imagem2);
        }
        checklistRepository.save(check);
        return repository.save(dto.travel());
        // return dto.travel();
    }

    @SuppressWarnings("null")
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
