package util;

import domain.*;
import persistence.GerenciadorArquivos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// iText imports
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Image;

/**
 * Classe utilitária para exportação de relatórios em PDF
 * Usa a biblioteca iText para gerar PDFs profissionais
 */
public class PDFExporter {
    
    private static final String DIRETORIO_EXPORT = "export";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_ARQUIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    // Fontes para o PDF
    private static final Font FONTE_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
    private static final Font FONTE_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
    private static final Font FONTE_CABECALHO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    private static final Font FONTE_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
    private static final Font FONTE_FOOTER = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.DARK_GRAY);
    
    // Cores para o PDF
    private static final BaseColor COR_CABECALHO = new BaseColor(31, 78, 121); // Azul escuro
    private static final BaseColor COR_LINHA_ALTERNADA = new BaseColor(240, 240, 240); // Cinza claro
    
    /**
     * Inicializa o diretório de export se não existir
     */
    private static void inicializarDiretorio() {
        File diretorio = new File(DIRETORIO_EXPORT);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
    
    /**
     * Cria cabeçalho padrão para os relatórios em PDF
     */
    private static void adicionarCabecalhoPDF(Document documento, String tituloRelatorio) 
            throws DocumentException {
        // Logo (se existir)
        try {
            Image logo = Image.getInstance("icons/autofacil.png");
            logo.scaleToFit(100, 50);
            logo.setAlignment(Element.ALIGN_CENTER);
            documento.add(logo);
        } catch (Exception e) {
            // Ignora se não encontrar o logo
        }
        
        // Título
        Paragraph titulo = new Paragraph("AutoFácil - Sistema de Locação", FONTE_TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        
        // Subtítulo
        Paragraph subtitulo = new Paragraph(tituloRelatorio, FONTE_SUBTITULO);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(subtitulo);
        
        // Data de geração
        Paragraph data = new Paragraph("Gerado em: " + 
            LocalDateTime.now().format(FORMATO_DATA_HORA), FONTE_SUBTITULO);
        data.setAlignment(Element.ALIGN_CENTER);
        documento.add(data);
        
        // Espaço
        documento.add(new Paragraph(" "));
    }
    
    /**
     * Adiciona rodapé ao documento PDF
     */
    private static void adicionarRodapePDF(Document documento) throws DocumentException {
        documento.add(new Paragraph(" "));
        Paragraph rodape = new Paragraph("AutoFácil Java Edition - Documento gerado automaticamente", FONTE_FOOTER);
        rodape.setAlignment(Element.ALIGN_CENTER);
        documento.add(rodape);
    }
    
    /**
     * Exporta relatório de clientes para PDF
     */
    public static String exportarRelatorioClientes() {
        inicializarDiretorio();
        String nomeArquivo = "relatorio_clientes_" + LocalDateTime.now().format(FORMATO_ARQUIVO) + ".pdf";
        String caminhoCompleto = DIRETORIO_EXPORT + File.separator + nomeArquivo;
        
        Document documento = new Document(PageSize.A4, 36, 36, 54, 36);
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(caminhoCompleto));
            documento.open();
            
            // Cabeçalho
            adicionarCabecalhoPDF(documento, "RELATÓRIO DE CLIENTES");
            
            // Carregar dados
            GerenciadorArquivos<Cliente> gerenciador = new GerenciadorArquivos<>("clientes.json", Cliente.class);
            List<Cliente> clientes = gerenciador.carregar();
            
            // Criar tabela
            PdfPTable tabela = new PdfPTable(6);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1, 3, 2, 3, 2, 2});
            
            // Cabeçalho da tabela
            addCelulaCabecalho(tabela, "ID");
            addCelulaCabecalho(tabela, "Nome");
            addCelulaCabecalho(tabela, "CPF");
            addCelulaCabecalho(tabela, "E-mail");
            addCelulaCabecalho(tabela, "Telefone");
            addCelulaCabecalho(tabela, "Data Cad.");
            
            // Dados dos clientes
            boolean linhaPar = false;
            for (Cliente cliente : clientes) {
                addCelula(tabela, cliente.getId(), linhaPar);
                addCelula(tabela, limitarTexto(cliente.getNome(), 30), linhaPar);
                addCelula(tabela, cliente.getCpf(), linhaPar);
                addCelula(tabela, limitarTexto(cliente.getEmail(), 30), linhaPar);
                addCelula(tabela, cliente.getTelefone(), linhaPar);
                addCelula(tabela, cliente.getDataCadastro().format(FORMATO_DATA), linhaPar);
                linhaPar = !linhaPar;
            }
            
            documento.add(tabela);
            
            // Estatísticas adicionais
            documento.add(new Paragraph(" "));
            
            Paragraph estatisticas = new Paragraph();
            estatisticas.add(new Phrase("Total de clientes: " + clientes.size() + "\n", FONTE_NORMAL));
            
            long clientesAtivos = clientes.stream().filter(Cliente::isAtivo).count();
            estatisticas.add(new Phrase("Clientes ativos: " + clientesAtivos + "\n", FONTE_NORMAL));
            estatisticas.add(new Phrase("Clientes inativos: " + (clientes.size() - clientesAtivos), FONTE_NORMAL));
            
            documento.add(estatisticas);
            
            // Rodapé
            adicionarRodapePDF(documento);
            
            return caminhoCompleto;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar PDF de clientes: " + e.getMessage());
            return null;
        } finally {
            documento.close();
        }
    }
    
    /**
     * Exporta relatório de veículos para PDF
     */
    public static String exportarRelatorioVeiculos() {
        inicializarDiretorio();
        String nomeArquivo = "relatorio_veiculos_" + LocalDateTime.now().format(FORMATO_ARQUIVO) + ".pdf";
        String caminhoCompleto = DIRETORIO_EXPORT + File.separator + nomeArquivo;
        
        Document documento = new Document(PageSize.A4, 36, 36, 54, 36);
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(caminhoCompleto));
            documento.open();
            
            // Cabeçalho
            adicionarCabecalhoPDF(documento, "RELATÓRIO DE VEÍCULOS");
            
            // Carregar dados
            GerenciadorArquivos<Veiculo> gerenciador = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
            List<Veiculo> veiculos = gerenciador.carregar();
            
            // Criar tabela
            PdfPTable tabela = new PdfPTable(7);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1, 2, 2, 2, 1, 1.5f, 1.5f});
            
            // Cabeçalho da tabela
            addCelulaCabecalho(tabela, "ID");
            addCelulaCabecalho(tabela, "Modelo");
            addCelulaCabecalho(tabela, "Marca");
            addCelulaCabecalho(tabela, "Placa");
            addCelulaCabecalho(tabela, "Ano");
            addCelulaCabecalho(tabela, "Categoria");
            addCelulaCabecalho(tabela, "Status");
            
            // Dados dos veículos
            boolean linhaPar = false;
            for (Veiculo veiculo : veiculos) {
                addCelula(tabela, veiculo.getId(), linhaPar);
                addCelula(tabela, veiculo.getModelo(), linhaPar);
                addCelula(tabela, veiculo.getMarca(), linhaPar);
                addCelula(tabela, veiculo.getPlaca(), linhaPar);
                addCelula(tabela, String.valueOf(veiculo.getAno()), linhaPar);
                addCelula(tabela, veiculo.getCategoria().toString(), linhaPar);
                addCelula(tabela, veiculo.getStatus().toString(), linhaPar);
                linhaPar = !linhaPar;
            }
            
            documento.add(tabela);
            
            // Estatísticas adicionais
            documento.add(new Paragraph(" "));
            
            Paragraph estatisticas = new Paragraph();
            estatisticas.add(new Phrase("Total de veículos: " + veiculos.size() + "\n", FONTE_NORMAL));
            
            // Contagem por status
            Map<Veiculo.Status, Long> contagemStatus = veiculos.stream()
                .collect(Collectors.groupingBy(Veiculo::getStatus, Collectors.counting()));
                
            for (Map.Entry<Veiculo.Status, Long> entry : contagemStatus.entrySet()) {
                estatisticas.add(new Phrase(
                    "Veículos " + entry.getKey() + ": " + entry.getValue() + "\n", 
                    FONTE_NORMAL
                ));
            }
            
            documento.add(estatisticas);
            
            // Rodapé
            adicionarRodapePDF(documento);
            
            return caminhoCompleto;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar PDF de veículos: " + e.getMessage());
            return null;
        } finally {
            documento.close();
        }
    }
    
    /**
     * Exporta relatório de aluguéis para PDF
     */
    public static String exportarRelatorioAlugueis() {
        inicializarDiretorio();
        String nomeArquivo = "relatorio_alugueis_" + LocalDateTime.now().format(FORMATO_ARQUIVO) + ".pdf";
        String caminhoCompleto = DIRETORIO_EXPORT + File.separator + nomeArquivo;
        
        Document documento = new Document(PageSize.A4.rotate(), 36, 36, 54, 36); // Paisagem
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(caminhoCompleto));
            documento.open();
            
            // Cabeçalho
            adicionarCabecalhoPDF(documento, "RELATÓRIO DE ALUGUÉIS");
            
            // Carregar dados
            GerenciadorArquivos<Aluguel> gerenciadorAluguel = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
            List<Aluguel> alugueis = gerenciadorAluguel.carregar();
            
            GerenciadorArquivos<Cliente> gerenciadorCliente = new GerenciadorArquivos<>("clientes.json", Cliente.class);
            List<Cliente> clientes = gerenciadorCliente.carregar();
            
            GerenciadorArquivos<Veiculo> gerenciadorVeiculo = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
            List<Veiculo> veiculos = gerenciadorVeiculo.carregar();
            
            // Mapear IDs para facilitar a busca
            Map<String, Cliente> mapaClientes = clientes.stream()
                .collect(Collectors.toMap(Cliente::getId, cliente -> cliente));
                
            Map<String, Veiculo> mapaVeiculos = veiculos.stream()
                .collect(Collectors.toMap(Veiculo::getId, veiculo -> veiculo));
            
            // Criar tabela
            PdfPTable tabela = new PdfPTable(8);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1, 2, 2, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
            
            // Cabeçalho da tabela
            addCelulaCabecalho(tabela, "ID");
            addCelulaCabecalho(tabela, "Cliente");
            addCelulaCabecalho(tabela, "Veículo");
            addCelulaCabecalho(tabela, "Data Início");
            addCelulaCabecalho(tabela, "Data Fim");
            addCelulaCabecalho(tabela, "Valor Total");
            addCelulaCabecalho(tabela, "Status");
            addCelulaCabecalho(tabela, "Data Devolução");
            
            // Dados dos aluguéis
            boolean linhaPar = false;
            for (Aluguel aluguel : alugueis) {
                Cliente cliente = mapaClientes.get(aluguel.getClienteId());
                Veiculo veiculo = mapaVeiculos.get(aluguel.getVeiculoId());
                
                String nomeCliente = cliente != null ? cliente.getNome() : "Cliente não encontrado";
                String infoVeiculo = veiculo != null ? veiculo.getMarca() + " " + veiculo.getModelo() : "Veículo não encontrado";
                
                addCelula(tabela, aluguel.getId(), linhaPar);
                addCelula(tabela, nomeCliente, linhaPar);
                addCelula(tabela, infoVeiculo, linhaPar);
                addCelula(tabela, aluguel.getDataInicio().format(FORMATO_DATA), linhaPar);
                addCelula(tabela, aluguel.getDataFimPrevista().format(FORMATO_DATA), linhaPar);
                addCelula(tabela, "R$ " + aluguel.getValorTotal().toString(), linhaPar);
                addCelula(tabela, aluguel.getStatus().toString(), linhaPar);
                addCelula(tabela, aluguel.getDataFimReal() != null ? 
                    aluguel.getDataFimReal().format(FORMATO_DATA) : "-", linhaPar);
                linhaPar = !linhaPar;
            }
            
            documento.add(tabela);
            
            // Estatísticas adicionais
            documento.add(new Paragraph(" "));
            
            Paragraph estatisticas = new Paragraph();
            estatisticas.add(new Phrase("Total de aluguéis: " + alugueis.size() + "\n", FONTE_NORMAL));
            
            // Contagem por status
            Map<Aluguel.StatusAluguel, Long> contagemStatus = alugueis.stream()
                .collect(Collectors.groupingBy(Aluguel::getStatus, Collectors.counting()));
                
            for (Map.Entry<Aluguel.StatusAluguel, Long> entry : contagemStatus.entrySet()) {
                estatisticas.add(new Phrase(
                    "Aluguéis " + entry.getKey() + ": " + entry.getValue() + "\n", 
                    FONTE_NORMAL
                ));
            }
            
            // Cálculo de receita total
            BigDecimal receitaTotal = alugueis.stream()
                .filter(a -> a.getStatus() != Aluguel.StatusAluguel.CANCELADO)
                .map(Aluguel::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
            estatisticas.add(new Phrase("Receita total: R$ " + receitaTotal, FONTE_NORMAL));
            
            documento.add(estatisticas);
            
            // Rodapé
            adicionarRodapePDF(documento);
            
            return caminhoCompleto;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar PDF de aluguéis: " + e.getMessage());
            return null;
        } finally {
            documento.close();
        }
    }
    
    /**
     * Exporta relatório de funcionários para PDF
     */
    public static String exportarRelatorioFuncionarios() {
        inicializarDiretorio();
        String nomeArquivo = "relatorio_funcionarios_" + LocalDateTime.now().format(FORMATO_ARQUIVO) + ".pdf";
        String caminhoCompleto = DIRETORIO_EXPORT + File.separator + nomeArquivo;
        
        Document documento = new Document(PageSize.A4, 36, 36, 54, 36);
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(caminhoCompleto));
            documento.open();
            
            // Cabeçalho
            adicionarCabecalhoPDF(documento, "RELATÓRIO DE FUNCIONÁRIOS");
            
            // Carregar dados
            GerenciadorArquivos<Funcionario> gerenciador = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class);
            List<Funcionario> funcionarios = gerenciador.carregar();
            
            // Criar tabela
            PdfPTable tabela = new PdfPTable(7);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1, 3, 2, 2, 2, 1.5f, 1.5f});
            
            // Cabeçalho da tabela
            addCelulaCabecalho(tabela, "ID");
            addCelulaCabecalho(tabela, "Nome");
            addCelulaCabecalho(tabela, "CPF");
            addCelulaCabecalho(tabela, "E-mail");
            addCelulaCabecalho(tabela, "Telefone");
            addCelulaCabecalho(tabela, "Tipo");
            addCelulaCabecalho(tabela, "Data Adm.");
            
            // Dados dos funcionários
            boolean linhaPar = false;
            for (Funcionario funcionario : funcionarios) {
                addCelula(tabela, funcionario.getId(), linhaPar);
                addCelula(tabela, limitarTexto(funcionario.getNome(), 30), linhaPar);
                addCelula(tabela, funcionario.getCpf(), linhaPar);
                addCelula(tabela, limitarTexto(funcionario.getEmail(), 30), linhaPar);
                addCelula(tabela, funcionario.getTelefone(), linhaPar);
                addCelula(tabela, funcionario.getTipo().toString(), linhaPar);
                addCelula(tabela, funcionario.getDataAdmissao().format(FORMATO_DATA), linhaPar);
                linhaPar = !linhaPar;
            }
            
            documento.add(tabela);
            
            // Estatísticas adicionais
            documento.add(new Paragraph(" "));
            
            Paragraph estatisticas = new Paragraph();
            estatisticas.add(new Phrase("Total de funcionários: " + funcionarios.size() + "\n", FONTE_NORMAL));
            
            // Contagem por tipo
            Map<Funcionario.TipoFuncionario, Long> contagemTipos = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getTipo, Collectors.counting()));
                
            for (Map.Entry<Funcionario.TipoFuncionario, Long> entry : contagemTipos.entrySet()) {
                estatisticas.add(new Phrase(
                    "Funcionários " + entry.getKey() + ": " + entry.getValue() + "\n", 
                    FONTE_NORMAL
                ));
            }
            
            // Contagem por status (ativo/inativo)
            long funcionariosAtivos = funcionarios.stream().filter(Funcionario::isAtivo).count();
            estatisticas.add(new Phrase("Funcionários ativos: " + funcionariosAtivos + "\n", FONTE_NORMAL));
            estatisticas.add(new Phrase("Funcionários inativos: " + (funcionarios.size() - funcionariosAtivos), FONTE_NORMAL));
            
            documento.add(estatisticas);
            
            // Rodapé
            adicionarRodapePDF(documento);
            
            return caminhoCompleto;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar PDF de funcionários: " + e.getMessage());
            return null;
        } finally {
            documento.close();
        }
    }

    /**
     * Método auxiliar para adicionar célula de cabeçalho à tabela
     */
    private static void addCelulaCabecalho(PdfPTable tabela, String texto) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, FONTE_CABECALHO));
        celula.setBackgroundColor(COR_CABECALHO);
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setPadding(5);
        tabela.addCell(celula);
    }
    
    /**
     * Método auxiliar para adicionar célula à tabela
     */
    private static void addCelula(PdfPTable tabela, String texto, boolean destacar) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, FONTE_NORMAL));
        if (destacar) {
            celula.setBackgroundColor(COR_LINHA_ALTERNADA);
        }
        celula.setPadding(4);
        tabela.addCell(celula);
    }
    
    /**
     * Limita o tamanho de um texto para exibição
     */
    private static String limitarTexto(String texto, int tamanhoMaximo) {
        if (texto == null) return "";
        return texto.length() > tamanhoMaximo ? texto.substring(0, tamanhoMaximo - 3) + "..." : texto;
    }
}
