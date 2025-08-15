package util;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe utilitária para validações de dados
 * Validações obrigatórias: CPF, CEP, Telefone, Placa, Data, Email
 */
public class Validador {
    
    // Padrões regex para validação
    private static final Pattern PADRAO_CPF = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    private static final Pattern PADRAO_CEP = Pattern.compile("\\d{5}-\\d{3}");
    private static final Pattern PADRAO_TELEFONE = Pattern.compile("\\(\\d{2}\\) \\d{4,5}-\\d{4}");
    private static final Pattern PADRAO_PLACA = Pattern.compile("[A-Z]{3}-\\d{4}");
    private static final Pattern PADRAO_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Valida CPF no formato ###.###.###-##
     */
    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        // Verificar formato
        if (!PADRAO_CPF.matcher(cpf).matches()) {
            return false;
        }
        
        // Remover formatação para validação numérica
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");
        
        // Verificar se todos os dígitos são iguais (CPF inválido)
        if (cpfNumeros.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validação do algoritmo do CPF
        return validarDigitosCPF(cpfNumeros);
    }
    
    /**
     * Valida CPF no formato numérico (apenas números)
     */
    public static boolean validarCPFNumerico(String cpf) {
        if (cpf == null || cpf.trim().isEmpty() || cpf.length() != 11) {
            return false;
        }
        
        // Verificar se é composto apenas por números
        if (!cpf.matches("\\d+")) {
            return false;
        }
        
        // Verificar se todos os dígitos são iguais (CPF inválido)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validação do algoritmo do CPF
        return validarDigitosCPF(cpf);
    }
    
    private static boolean validarDigitosCPF(String cpf) {
        // Calcular primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Integer.parseInt(cpf.substring(i, i + 1)) * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;
        
        // Calcular segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Integer.parseInt(cpf.substring(i, i + 1)) * (11 - i);
        }
        resto = soma % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;
        
        // Verificar se os dígitos calculados coincidem com os fornecidos
        return cpf.charAt(9) == (char) (digito1 + '0') && 
               cpf.charAt(10) == (char) (digito2 + '0');
    }

    /**
     * Valida CEP no formato #####-###
     */
    public static boolean validarCEP(String cep) {
        return cep != null && PADRAO_CEP.matcher(cep).matches();
    }

    /**
     * Valida telefone no formato (##) #####-#### ou (##) ####-####
     * Também aceita formato numérico (apenas números): 10 ou 11 dígitos
     */
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        
        // Se estiver no formato com máscara, validar com regex
        if (telefone.contains("(") || telefone.contains(")") || telefone.contains("-")) {
            return PADRAO_TELEFONE.matcher(telefone).matches();
        }
        
        // Se for apenas números, verificar se tem 10 ou 11 dígitos
        String numeroLimpo = telefone.replaceAll("[^0-9]", "");
        return numeroLimpo.length() == 10 || numeroLimpo.length() == 11;
    }

    /**
     * Valida placa no formato AAA-0000
     */
    public static boolean validarPlaca(String placa) {
        return placa != null && PADRAO_PLACA.matcher(placa.toUpperCase()).matches();
    }

    /**
     * Valida email
     */
    public static boolean validarEmail(String email) {
        return email != null && PADRAO_EMAIL.matcher(email).matches();
    }

    /**
     * Valida data no formato dd/MM/yyyy
     */
    public static boolean validarData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate.parse(data, FORMATO_DATA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Converte string de data para LocalDate
     */
    public static LocalDate converterData(String data) {
        if (!validarData(data)) {
            throw new IllegalArgumentException("Data inválida: " + data);
        }
        return LocalDate.parse(data, FORMATO_DATA);
    }

    /**
     * Converte LocalDate para string formatada
     */
    public static String formatarData(LocalDate data) {
        return data != null ? data.format(FORMATO_DATA) : "";
    }

    /**
     * Valida se uma string não está vazia
     */
    public static boolean validarTextoObrigatorio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Valida se um número é positivo
     */
    public static boolean validarNumeroPositivo(double numero) {
        return numero > 0;
    }

    /**
     * Valida se um ano é válido (entre 1900 e ano atual + 1)
     */
    public static boolean validarAno(int ano) {
        int anoAtual = LocalDate.now().getYear();
        return ano >= 1900 && ano <= anoAtual + 1;
    }

    /**
     * Remove formatação de CPF, mantendo apenas números
     */
    public static String limparCPF(String cpf) {
        return cpf != null ? cpf.replaceAll("[^0-9]", "") : "";
    }

    /**
     * Remove formatação de CEP, mantendo apenas números
     */
    public static String limparCEP(String cep) {
        return cep != null ? cep.replaceAll("[^0-9]", "") : "";
    }

    /**
     * Remove formatação de telefone, mantendo apenas números
     */
    public static String limparTelefone(String telefone) {
        return telefone != null ? telefone.replaceAll("[^0-9]", "") : "";
    }

    /**
     * Remove formatação de placa, mantendo apenas letras e números
     */
    public static String limparPlaca(String placa) {
        return placa != null ? placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase() : "";
    }

    /**
     * Valida força da senha (mínimo 6 caracteres)
     */
    public static boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 6;
    }

    /**
     * Gera mensagem de erro personalizada para cada tipo de validação
     */
    public static String obterMensagemErro(String campo, String valor) {
        switch (campo.toLowerCase()) {
            case "cpf":
                return "CPF inválido. Use o formato: 000.000.000-00";
            case "cep":
                return "CEP inválido. Use o formato: 00000-000";
            case "telefone":
                return "Telefone inválido. Use o formato: (00) 00000-0000";
            case "placa":
                return "Placa inválida. Use o formato: AAA-0000";
            case "email":
                return "Email inválido. Verifique o formato.";
            case "data":
                return "Data inválida. Use o formato: dd/MM/yyyy";
            case "senha":
                return "Senha deve ter pelo menos 6 caracteres.";
            default:
                return "Campo '" + campo + "' é obrigatório.";
        }
    }
}
