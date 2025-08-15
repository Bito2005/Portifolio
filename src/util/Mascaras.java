package util;

import javax.swing.text.MaskFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.JFormattedTextField;
import java.text.ParseException;

/**
 * Classe utilitária para aplicar máscaras em campos de entrada
 * Máscaras obrigatórias: CPF, CEP, Telefone, Placa, Data
 */
public class Mascaras {
    
    // Constantes das máscaras
    public static final String MASCARA_CPF = "###.###.###-##";
    public static final String MASCARA_CEP = "#####-###";
    public static final String MASCARA_TELEFONE = "(##) #####-####";
    public static final String MASCARA_TELEFONE_FIXO = "(##) ####-####";
    public static final String MASCARA_PLACA = "UUU-####";
    public static final String MASCARA_DATA = "##/##/####";
    public static final String MASCARA_HORA = "##:##";
    
    /**
     * Cria um MaskFormatter para CPF
     */
    public static MaskFormatter criarMascaraCPF() {
        try {
            MaskFormatter formatter = new MaskFormatter(MASCARA_CPF);
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(true);
            return formatter;
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara CPF: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cria um MaskFormatter para CEP
     */
    public static MaskFormatter criarMascaraCEP() {
        try {
            MaskFormatter formatter = new MaskFormatter(MASCARA_CEP);
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(true);
            return formatter;
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara CEP: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cria um MaskFormatter para telefone celular
     */
    public static MaskFormatter criarMascaraTelefone() {
        try {
            MaskFormatter formatter = new MaskFormatter(MASCARA_TELEFONE);
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(true);
            return formatter;
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara telefone: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cria um MaskFormatter para telefone fixo
     */
    public static MaskFormatter criarMascaraTelefoneFixo() {
        try {
            MaskFormatter formatter = new MaskFormatter(MASCARA_TELEFONE_FIXO);
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(true);
            return formatter;
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara telefone fixo: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cria um MaskFormatter para placa de veículo
     */
    public static MaskFormatter criarMascaraPlaca() {
        try {
            MaskFormatter formatter = new MaskFormatter(MASCARA_PLACA);
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(true);
            return formatter;
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara placa: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cria um MaskFormatter para data
     */
    public static MaskFormatter criarMascaraData() {
        try {
            MaskFormatter formatter = new MaskFormatter(MASCARA_DATA);
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(true);
            return formatter;
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara data: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cria um MaskFormatter para hora
     */
    public static MaskFormatter criarMascaraHora() {
        try {
            MaskFormatter formatter = new MaskFormatter(MASCARA_HORA);
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(true);
            return formatter;
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara hora: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cria um JFormattedTextField com máscara de CPF
     */
    public static JFormattedTextField criarCampoCPF() {
        MaskFormatter formatter = criarMascaraCPF();
        return formatter != null ? new JFormattedTextField(formatter) : new JFormattedTextField();
    }
    
    /**
     * Cria um JFormattedTextField com máscara de CEP
     */
    public static JFormattedTextField criarCampoCEP() {
        MaskFormatter formatter = criarMascaraCEP();
        return formatter != null ? new JFormattedTextField(formatter) : new JFormattedTextField();
    }
    
    /**
     * Cria um JFormattedTextField com máscara de telefone
     */
    public static JFormattedTextField criarCampoTelefone() {
        MaskFormatter formatter = criarMascaraTelefone();
        return formatter != null ? new JFormattedTextField(formatter) : new JFormattedTextField();
    }
    
    /**
     * Cria um JFormattedTextField com máscara de placa
     */
    public static JFormattedTextField criarCampoPlaca() {
        MaskFormatter formatter = criarMascaraPlaca();
        return formatter != null ? new JFormattedTextField(formatter) : new JFormattedTextField();
    }
    
    /**
     * Cria um JFormattedTextField com máscara de data
     */
    public static JFormattedTextField criarCampoData() {
        MaskFormatter formatter = criarMascaraData();
        return formatter != null ? new JFormattedTextField(formatter) : new JFormattedTextField();
    }
    
    /**
     * Aplica máscara em um campo existente
     */
    public static void aplicarMascara(JFormattedTextField campo, MaskFormatter mascara) {
        if (campo != null && mascara != null) {
            campo.setFormatterFactory(new DefaultFormatterFactory(mascara));
        }
    }
    
    /**
     * Remove caracteres especiais de uma string formatada
     */
    public static String removerFormatacao(String texto) {
        if (texto == null) return "";
        return texto.replaceAll("[^0-9A-Za-z]", "");
    }
    
    /**
     * Aplica formatação de CPF em uma string
     */
    public static String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9);
    }
    
    /**
     * Aplica formatação de CEP em uma string
     */
    public static String formatarCEP(String cep) {
        if (cep == null || cep.length() != 8) return cep;
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }
    
    /**
     * Aplica formatação de telefone em uma string
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null) return telefone;
        
        String numeros = telefone.replaceAll("[^0-9]", "");
        
        if (numeros.length() == 11) {
            return "(" + numeros.substring(0, 2) + ") " + 
                   numeros.substring(2, 7) + "-" + 
                   numeros.substring(7);
        } else if (numeros.length() == 10) {
            return "(" + numeros.substring(0, 2) + ") " + 
                   numeros.substring(2, 6) + "-" + 
                   numeros.substring(6);
        }
        
        return telefone;
    }
    
    /**
     * Aplica formatação de placa em uma string
     */
    public static String formatarPlaca(String placa) {
        if (placa == null || placa.length() != 7) return placa;
        return placa.substring(0, 3).toUpperCase() + "-" + placa.substring(3);
    }
    
    /**
     * Remove máscara de uma string, mantendo apenas números e letras
     */
    public static String removerMascara(String valor) {
        if (valor == null) return "";
        return valor.replaceAll("[^0-9A-Za-z]", "");
    }
}
