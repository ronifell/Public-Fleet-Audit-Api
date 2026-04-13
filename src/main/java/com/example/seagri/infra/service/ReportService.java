package com.example.seagri.infra.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.seagri.infra.dto.MaintenanceDTO;
import com.example.seagri.infra.dto.SupplyDTO;
import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.Maintenance;
import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.repository.MaintenanceRepository;
import com.example.seagri.infra.repository.SupplyRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;


@Service
public class ReportService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private HashService hashService;

    public byte[] generateReport(List<Supply> supplies, List<Maintenance> maintenances,
                                 Map<String, Object> parameters, String generatedBy) throws JRException, FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:reportsFile/mainReport.jrxml");
        String path = file.getParent();
        
        JasperReport jasperReport = JasperCompileManager.compileReport(path + "/mainReport.jrxml");
        JRSaver.saveObject(jasperReport, path + "/mainReport.jasper");

        @SuppressWarnings("null")
        Double suppliesAmount = supplies.stream()
                    .reduce(0.0, 
                        (subtotal, supply) -> subtotal + supply.getEmission_value().doubleValue(), Double::sum);

        @SuppressWarnings("null")                        
        Double maintenancesAmount = maintenances.stream()
                    .reduce(0.0, 
                        (subtotal, maintenance) -> subtotal + maintenance.getValorTotal().doubleValue(), Double::sum);

        Double totalAmount = maintenancesAmount + suppliesAmount;
        List<SupplyDTO> suppliesDTOs = new ArrayList<SupplyDTO>();
        // essa primeira linha adicionando um abastecimento - ou mais futuramente uma manutenção - vazio é necessária devido 
        // a uma propriedade do jasper que não estava imprimindo a primeira linha quando usando um sub-relatório
        // para mais informações: 
        // https://stackoverflow.com/questions/67637363/jasper-report-the-first-line-from-a-list-is-not-displayed
        // https://itecnote.com/tecnote/java-jasper-report-missing-first-row/
        suppliesDTOs.add(new SupplyDTO());
        supplies.stream().forEach(supply -> suppliesDTOs.add(new SupplyDTO(supply)));

        JRSaver.saveObject(JasperCompileManager.compileReport(path + "/suppliesReports.jrxml"), path + "/suppliesReports.jasper");
        
        List<MaintenanceDTO> maintenancesDTOs = new ArrayList<MaintenanceDTO>();
        maintenancesDTOs.add(new MaintenanceDTO());
        maintenances.stream().forEach(maintenance -> maintenancesDTOs.add(new MaintenanceDTO(maintenance)));

        JRSaver.saveObject(JasperCompileManager.compileReport(path + "/maintenancesReports.jrxml"), path + "/maintenancesReports.jasper");

        JRBeanCollectionDataSource suppliesDataSource = new JRBeanCollectionDataSource(suppliesDTOs);
        JRBeanCollectionDataSource maintenancesDataSource = new JRBeanCollectionDataSource(maintenancesDTOs);


        String sealTimestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Hash do selo: combina os dados do relatório para provar que o conteúdo é autêntico
        String sealInput = String.join("|",
                parameters.getOrDefault("periodString", "").toString(),
                parameters.getOrDefault("searchedBy", "").toString(),
                String.format("%.2f", totalAmount),
                String.valueOf(supplies.size()),
                String.valueOf(maintenances.size()),
                generatedBy != null ? generatedBy : "system",
                sealTimestamp
        );
        String sealHash = hashService.generate(sealInput);

        parameters.put("suppliesDataSet", suppliesDataSource);
        parameters.put("maintenancesDataSet", maintenancesDataSource);
        parameters.put("path", path);
        parameters.put("generationDate", new Date().toString());
        parameters.put("totalValueSupplies", String.format("%.2f", suppliesAmount));
        parameters.put("totalValueMaintenances", String.format("%.2f", maintenancesAmount));
        parameters.put("totalValue", String.format("%.2f", totalAmount));
        parameters.put("sealHash", sealHash);
        parameters.put("sealTimestamp", sealTimestamp);
        parameters.put("sealGeneratedBy", generatedBy != null ? generatedBy : "system");

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        // JasperExportManager.exportReportToPdfFile(print, "Report.pdf");
        return JasperExportManager.exportReportToPdf(print);
    }

    public byte[] getAll(LocalDate startDate, LocalDate endDate, String generatedBy) throws FileNotFoundException, JRException {

        LocalDateTime date1 = LocalDateTime.of(startDate, LocalDateTime.MIN.toLocalTime());
        LocalDateTime date2 = LocalDateTime.of(endDate, LocalDateTime.MAX.toLocalTime());
        String periodString = turnDateString(startDate) + " - " + turnDateString(endDate);

        List<Supply> supplies = supplyRepository.getAllSupplies(date1, date2);
        List<Maintenance> maintenances = maintenanceRepository.getAllMaintenances(date1, date2);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("searchedBy", "-");
        parameters.put("periodString", periodString);

        return generateReport(supplies, maintenances, parameters, generatedBy);
    }

    public byte[] getByVehicle(String vehiclePlate, LocalDate startDate, LocalDate endDate, String generatedBy) throws FileNotFoundException, JRException {
        LocalDateTime date1 = LocalDateTime.of(startDate, LocalDateTime.MIN.toLocalTime());
        LocalDateTime date2 = LocalDateTime.of(endDate, LocalDateTime.MAX.toLocalTime());
        String periodString = turnDateString(startDate) + " - " + turnDateString(endDate);

        List<Supply> supplies = supplyRepository.getVehicleSupplies(vehiclePlate, date1, date2);
        List<Maintenance> maintenances = maintenanceRepository.getVehicleMaintenances(vehiclePlate, date1, date2);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("searchedBy", vehiclePlate);
        parameters.put("periodString", periodString);

        return generateReport(supplies, maintenances, parameters, generatedBy);
    }

    public byte[] getByType(Integer vehicleType, String typeName, LocalDate startDate, LocalDate endDate, String generatedBy) throws FileNotFoundException, JRException {
        LocalDateTime date1 = LocalDateTime.of(startDate, LocalDateTime.MIN.toLocalTime());
        LocalDateTime date2 = LocalDateTime.of(endDate, LocalDateTime.MAX.toLocalTime());
        String periodString = turnDateString(startDate) + " - " + turnDateString(endDate);

        List<Supply> supplies = supplyRepository.getVehicleTypeSupplies(vehicleType, date1, date2);
        List<Maintenance> maintenances = maintenanceRepository.getVehicleTypeMaintenance(vehicleType, date1, date2);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("searchedBy", typeName);
        parameters.put("periodString", periodString);

        return generateReport(supplies, maintenances, parameters, generatedBy);
    }

    public String turnDateString(LocalDate dateTime){        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
          .withResolverStyle(ResolverStyle.STRICT);

        String date = formatter.format(dateTime).toString(); 
        return date;
    }


}
