package util;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gerador de IDs únicos para as entidades
 */
public class GeradorID {
    
    private static final DateTimeFormatter FORMATO_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * Gera um ID único usando UUID
     */
    public static String gerarID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Gera um ID sequencial baseado em timestamp + sufixo aleatório
     */
    public static String gerarIDSequencial(String prefixo) {
        String timestamp = LocalDateTime.now().format(FORMATO_TIMESTAMP);
        String sufixo = String.valueOf((int)(Math.random() * 1000));
        return prefixo.toUpperCase() + "-" + timestamp + "-" + sufixo;
    }
    
    /**
     * Gera ID para cliente (CLI-AAAAMMDDHHMMSS-XXX)
     */
    public static String gerarIDCliente() {
        return gerarIDSequencial("CLI");
    }
    
    /**
     * Gera ID para veículo (VEI-AAAAMMDDHHMMSS-XXX)
     */
    public static String gerarIDVeiculo() {
        return gerarIDSequencial("VEI");
    }
    
    /**
     * Gera ID para funcionário (FUN-AAAAMMDDHHMMSS-XXX)
     */
    public static String gerarIDFuncionario() {
        return gerarIDSequencial("FUN");
    }
    
    /**
     * Gera ID para aluguel (ALU-AAAAMMDDHHMMSS-XXX)
     */
    public static String gerarIDAluguel() {
        return gerarIDSequencial("ALU");
    }
    
    /**
     * Gera um número de protocolo para documentos
     */
    public static String gerarProtocolo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
    
    /**
     * Verifica se um ID tem formato válido
     */
    public static boolean validarID(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        // Formato UUID padrão
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            // Não é UUID, verificar formato sequencial
        }
        
        // Formato sequencial: XXX-AAAAMMDDHHMMSS-XXX
        String[] partes = id.split("-");
        return partes.length == 3 && 
               partes[0].length() == 3 && 
               partes[1].length() == 14 && 
               partes[2].length() >= 1;
    }
    
    /**
     * Extrai o prefixo de um ID sequencial
     */
    public static String extrairPrefixo(String id) {
        if (id != null && id.contains("-")) {
            return id.split("-")[0];
        }
        return "";
    }
    
    /**
     * Extrai o timestamp de um ID sequencial
     */
    public static String extrairTimestamp(String id) {
        if (id != null && id.contains("-")) {
            String[] partes = id.split("-");
            if (partes.length >= 2) {
                return partes[1];
            }
        }
        return "";
    }
}
