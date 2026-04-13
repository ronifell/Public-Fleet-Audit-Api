package com.example.seagri.infra.integrity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerar e verificar hashes SHA-256
 * de integridade para os registros de telemetria.
 */
@Service
public class HashService {

    /**
     * Gera um hash SHA-256 hexadecimal a partir de uma string.
     */
    public String generate(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível na JVM", e);
        }
    }

    /**
     * Gera o hash de uma entidade Hashable.
     */
    public String generate(Hashable entity) {
        return generate(entity.toHashString());
    }

    /**
     * Verifica se o hash armazenado em uma entidade ainda é válido,
     * ou seja, se os dados não foram alterados desde a criação.
     *
     * @param entity    A entidade a verificar
     * @param storedHash O hash armazenado no banco de dados
     * @return true se íntegro, false se houve adulteração
     */
    public boolean verify(Hashable entity, String storedHash) {
        if (storedHash == null || storedHash.isBlank()) {
            return false;
        }
        return generate(entity).equals(storedHash);
    }
}
