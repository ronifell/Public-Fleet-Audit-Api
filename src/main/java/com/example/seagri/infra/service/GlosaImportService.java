package com.example.seagri.infra.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.seagri.infra.dto.GlosaTransactionInputDTO;
import com.example.seagri.infra.dto.GlosaTransactionInputDTO.CoordenadaDTO;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Converte arquivos CSV ou Excel (.xlsx) em lista de GlosaTransactionInputDTO
 * para processamento pelo Motor de Glosa.
 *
 * Colunas esperadas (em qualquer ordem, detectadas pelo cabeçalho):
 *   transacao_id | timestamp | placa | posto_cnpj | posto_lat | posto_lng |
 *   combustivel  | volume_litros | odometro_informado | valor_total |
 *   veiculo_lat  | veiculo_lng
 *
 * As colunas veiculo_lat, veiculo_lng e valor_total são opcionais.
 */
@Service
public class GlosaImportService {

    // ── API pública ──────────────────────────────────────────────────────────

    public List<GlosaTransactionInputDTO> parseCsv(MultipartFile file) throws IOException {
        List<String[]> rows = readCsvRows(file);
        if (rows.size() < 2) return List.of();

        int[] idx = mapHeaders(rows.get(0));
        List<GlosaTransactionInputDTO> result = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            GlosaTransactionInputDTO dto = buildDto(row, idx);
            if (dto != null) result.add(dto);
        }
        return result;
    }

    public List<GlosaTransactionInputDTO> parseExcel(MultipartFile file) throws IOException {
        List<String[]> rows = readExcelRows(file);
        if (rows.size() < 2) return List.of();

        int[] idx = mapHeaders(rows.get(0));
        List<GlosaTransactionInputDTO> result = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            GlosaTransactionInputDTO dto = buildDto(row, idx);
            if (dto != null) result.add(dto);
        }
        return result;
    }

    // ── Parsing de arquivo ───────────────────────────────────────────────────

    private List<String[]> readCsvRows(MultipartFile file) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVReader csv = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(detectSeparator(file)).build())
                     .build()) {
            String[] line;
            while ((line = csv.readNext()) != null) {
                if (!isBlankRow(line)) rows.add(line);
            }
        } catch (Exception e) {
            throw new IOException("Erro ao ler CSV: " + e.getMessage(), e);
        }
        return rows;
    }

    private List<String[]> readExcelRows(MultipartFile file) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            int lastCol = 0;

            // Descobrir a quantidade de colunas pelo cabeçalho
            Row header = sheet.getRow(sheet.getFirstRowNum());
            if (header != null) lastCol = header.getLastCellNum();

            for (Row row : sheet) {
                if (row == null) continue;
                String[] cells = new String[lastCol];
                for (int c = 0; c < lastCol; c++) {
                    cells[c] = cellToString(row.getCell(c));
                }
                if (!isBlankRow(cells)) rows.add(cells);
            }
        }
        return rows;
    }

    // ── Mapeamento de colunas ────────────────────────────────────────────────

    /**
     * Índices de cada coluna conhecida. -1 = coluna ausente no arquivo.
     * Ordem: [transacaoId, timestamp, placa, postoCnpj, postoLat, postoLng,
     *         combustivel, volumeLitros, odomteroInformado, valorTotal,
     *         veiculoLat, veiculoLng]
     */
    private int[] mapHeaders(String[] headers) {
        int[] idx = new int[12];
        java.util.Arrays.fill(idx, -1);

        for (int i = 0; i < headers.length; i++) {
            String h = headers[i].trim().toLowerCase()
                    .replace(" ", "_").replace("-", "_");
            switch (h) {
                case "transacao_id", "id_transacao", "transacaoid"     -> idx[0] = i;
                case "timestamp", "data_hora", "data"                  -> idx[1] = i;
                case "placa", "placa_veiculo", "license_plate"         -> idx[2] = i;
                case "posto_cnpj", "cnpj_posto", "cnpj"               -> idx[3] = i;
                case "posto_lat", "lat_posto", "latitude_posto"        -> idx[4] = i;
                case "posto_lng", "lng_posto", "longitude_posto"       -> idx[5] = i;
                case "combustivel", "tipo_combustivel", "fuel"         -> idx[6] = i;
                case "volume_litros", "litros", "quantidade_litros"    -> idx[7] = i;
                case "odometro_informado", "odometro", "hodometro"     -> idx[8] = i;
                case "valor_total", "valor", "preco_total"             -> idx[9] = i;
                case "veiculo_lat", "lat_veiculo", "gps_lat"           -> idx[10] = i;
                case "veiculo_lng", "lng_veiculo", "gps_lng"           -> idx[11] = i;
                default -> { /* coluna desconhecida — ignorada */ }
            }
        }
        return idx;
    }

    // ── Conversão de linha em DTO ────────────────────────────────────────────

    private GlosaTransactionInputDTO buildDto(String[] row, int[] idx) {
        try {
            String transacaoId    = get(row, idx[0]);
            String timestamp      = normalizeTimestamp(get(row, idx[1]));
            String placa          = get(row, idx[2]);
            String postoCnpj      = get(row, idx[3]);
            Double postoLat       = toDouble(get(row, idx[4]));
            Double postoLng       = toDouble(get(row, idx[5]));
            String combustivel    = get(row, idx[6]);
            Double volumeLitros   = toDouble(get(row, idx[7]));
            Integer odometro      = toInteger(get(row, idx[8]));
            Double valorTotal     = toDouble(get(row, idx[9]));
            Double veiculoLat     = toDouble(get(row, idx[10]));
            Double veiculoLng     = toDouble(get(row, idx[11]));

            // Campos obrigatórios
            if (transacaoId == null || placa == null || timestamp == null || volumeLitros == null) {
                return null;
            }

            CoordenadaDTO postoCoordenadas = (postoLat != null && postoLng != null)
                    ? new CoordenadaDTO(postoLat, postoLng) : null;
            CoordenadaDTO veiculoCoordenadas = (veiculoLat != null && veiculoLng != null)
                    ? new CoordenadaDTO(veiculoLat, veiculoLng) : null;

            return new GlosaTransactionInputDTO(
                    transacaoId, timestamp, placa, postoCnpj,
                    postoCoordenadas, combustivel, volumeLitros,
                    odometro, valorTotal, veiculoCoordenadas);

        } catch (Exception e) {
            return null; // linha malformada é pulada silenciosamente
        }
    }

    // ── Utilitários ──────────────────────────────────────────────────────────

    private char detectSeparator(MultipartFile file) throws IOException {
        // Lê a primeira linha para detectar se o separador é , ou ;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String first = br.readLine();
            if (first != null && first.chars().filter(c -> c == ';').count()
                    > first.chars().filter(c -> c == ',').count()) {
                return ';';
            }
        }
        return ',';
    }

    private String get(String[] row, int idx) {
        if (idx < 0 || idx >= row.length) return null;
        String v = row[idx].trim();
        return v.isEmpty() ? null : v;
    }

    private Double toDouble(String v) {
        if (v == null) return null;
        try { return Double.parseDouble(v.replace(",", ".")); }
        catch (NumberFormatException e) { return null; }
    }

    private Integer toInteger(String v) {
        if (v == null) return null;
        try { return Integer.parseInt(v.replaceAll("[^0-9]", "")); }
        catch (NumberFormatException e) { return null; }
    }

    /**
     * Aceita formatos comuns de data e converte para ISO-8601 UTC.
     * Ex: "27/03/2026 10:15:00" → "2026-03-27T10:15:00Z"
     *     "2026-03-27T10:15:00Z" → (sem alteração)
     */
    private String normalizeTimestamp(String v) {
        if (v == null) return null;
        if (v.contains("T")) return v.endsWith("Z") ? v : v + "Z";
        // dd/MM/yyyy HH:mm:ss ou dd/MM/yyyy
        try {
            String[] parts = v.split("[ T]");
            String date = parts[0]; // dd/MM/yyyy ou yyyy-MM-dd
            String time = parts.length > 1 ? parts[1] : "00:00:00";
            if (date.contains("/")) {
                String[] d = date.split("/");
                date = d[2] + "-" + d[1] + "-" + d[0];
            }
            return date + "T" + time + "Z";
        } catch (Exception e) {
            return v;
        }
    }

    private String cellToString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toString()
                    : String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try { yield String.valueOf(cell.getNumericCellValue()); }
                catch (Exception e) { yield cell.getStringCellValue(); }
            }
            default -> "";
        };
    }

    private boolean isBlankRow(String[] row) {
        for (String cell : row) {
            if (cell != null && !cell.isBlank()) return false;
        }
        return true;
    }
}
