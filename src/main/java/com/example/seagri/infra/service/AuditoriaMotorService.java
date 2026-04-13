package com.example.seagri.infra.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

/**
 * Reads and caches the contents of DB.json (Motor de Auditoria SEAGRI)
 * located in the Backend project root.
 */
@Service
public class AuditoriaMotorService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Object> fullData = Collections.emptyMap();

    @PostConstruct
    public void init() {
        reload();
    }

    /**
     * Reloads DB.json from the file system.
     * Tries multiple locations: project root, classpath, and working directory.
     */
    public void reload() {
        try {
            InputStream is = resolveDbJson();
            if (is != null) {
                fullData = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB.json: " + e.getMessage(), e);
        }
    }

    private InputStream resolveDbJson() throws IOException {
        Path[] candidates = {
                Paths.get("DB.json"),
                Paths.get(".", "DB.json"),
                Paths.get(System.getProperty("user.dir"), "DB.json"),
        };
        for (Path p : candidates) {
            if (Files.isRegularFile(p)) {
                return Files.newInputStream(p);
            }
        }
        InputStream cp = getClass().getClassLoader().getResourceAsStream("DB.json");
        if (cp != null) {
            return cp;
        }
        throw new IOException(
                "DB.json not found. Searched paths: " + java.util.Arrays.toString(candidates));
    }

    public Map<String, Object> getFullData() {
        return fullData;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAbastecimentos() {
        Object val = fullData.get("abastecimentos");
        return val instanceof List ? (List<Map<String, Object>>) val : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getResultadosMotorGlosa() {
        Object val = fullData.get("resultados_motor_glosa");
        return val instanceof List ? (List<Map<String, Object>>) val : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getResumoDashboard() {
        Object val = fullData.get("resumo_dashboard");
        return val instanceof Map ? (Map<String, Object>) val : Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTrilhaAuditoria() {
        Object val = fullData.get("trilha_auditoria");
        return val instanceof List ? (List<Map<String, Object>>) val : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBensPatrimonio() {
        Object val = fullData.get("bens_patrimonio");
        return val instanceof List ? (List<Map<String, Object>>) val : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTransferenciasPatrimonio() {
        Object val = fullData.get("transferencias_patrimonio");
        return val instanceof List ? (List<Map<String, Object>>) val : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCadeiaCustodiaPatrimonio() {
        Object val = fullData.get("cadeia_custodia_patrimonio");
        return val instanceof List ? (List<Map<String, Object>>) val : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getResumoPatrimonio() {
        Object val = fullData.get("resumo_patrimonio");
        return val instanceof Map ? (Map<String, Object>) val : Collections.emptyMap();
    }
}
