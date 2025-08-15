package ui;

import domain.*;
import persistence.GerenciadorArquivos;
import util.Tema;
import util.PDFExporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela de relatórios e gráficos do sistema
 */
public class TelaRelatorios extends JPanel {
    
    private JTabbedPane abas;
    private JPanel painelRelatorios;
    private JPanel painelGraficos;
    
    private JComboBox<String> comboTipoRelatorio;
    private JButton botaoGerar;
    private JButton botaoExportar;
    
    private JPanel painelGraficoAtual;
    private JComboBox<String> comboTipoGrafico;
    private JButton botaoAtualizarGrafico;
    
    private String ultimoRelatorioGerado;
    
    public TelaRelatorios() {
        setLayout(new BorderLayout());
        Tema.configurarPainel(this);
        
        // Criar componentes
        inicializarComponentes();
        
        // Configurar layout
        configurarLayout();
        
        // Configurar eventos
        configurarEventos();
        
        // Mostrar gráfico inicial
        atualizarGrafico();
    }
    
    private void inicializarComponentes() {
        // Abas principais
        abas = new JTabbedPane();
        Tema.configurarTabbedPane(abas);
        
        // Painel de relatórios
        painelRelatorios = new JPanel(new BorderLayout());
        Tema.configurarPainel(painelRelatorios);
        
        // Painel de gráficos
        painelGraficos = new JPanel(new BorderLayout());
        Tema.configurarPainel(painelGraficos);
        
        // Componentes painel relatórios
        JPanel painelControlesRelatorios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Tema.configurarPainel(painelControlesRelatorios);
        
        JLabel labelTipoRelatorio = new JLabel("Tipo de Relatório:");
        comboTipoRelatorio = new JComboBox<>(new String[] {
            "Clientes", "Veículos", "Aluguéis", "Funcionários"
        });
        Tema.configurarComboBox(comboTipoRelatorio);
        
        botaoGerar = new JButton("Gerar Relatório");
        botaoGerar.setPreferredSize(new Dimension(140, 30));
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoGerar, Tema.AZUL_ESCURO, Color.WHITE);
        
        botaoExportar = new JButton("Exportar PDF");
        botaoExportar.setPreferredSize(new Dimension(120, 30));
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoExportar, Tema.VERDE, Color.WHITE);
        botaoExportar.setEnabled(false);
        
        painelControlesRelatorios.add(labelTipoRelatorio);
        painelControlesRelatorios.add(comboTipoRelatorio);
        painelControlesRelatorios.add(botaoGerar);
        painelControlesRelatorios.add(botaoExportar);
        
        // Área de exibição do relatório
        JPanel painelPreview = new JPanel(new BorderLayout());
        Tema.configurarPainel(painelPreview);
        painelPreview.setBorder(BorderFactory.createTitledBorder("Pré-visualização"));
        
        JTextArea areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollRelatorio = new JScrollPane(areaRelatorio);
        painelPreview.add(scrollRelatorio, BorderLayout.CENTER);
        
        painelRelatorios.add(painelControlesRelatorios, BorderLayout.NORTH);
        painelRelatorios.add(painelPreview, BorderLayout.CENTER);
        
        // Componentes painel gráficos
        JPanel painelControlesGraficos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Tema.configurarPainel(painelControlesGraficos);
        
        JLabel labelTipoGrafico = new JLabel("Tipo de Gráfico:");
        comboTipoGrafico = new JComboBox<>(new String[] {
            "Status dos Veículos", 
            "Veículos por Categoria", 
            "Aluguéis por Mês",
            "Receita por Período"
        });
        Tema.configurarComboBox(comboTipoGrafico);
        
        botaoAtualizarGrafico = new JButton("Atualizar Gráfico");
        botaoAtualizarGrafico.setPreferredSize(new Dimension(150, 30));
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoAtualizarGrafico, Tema.AZUL_ESCURO, Color.WHITE);
        
        painelControlesGraficos.add(labelTipoGrafico);
        painelControlesGraficos.add(comboTipoGrafico);
        painelControlesGraficos.add(botaoAtualizarGrafico);
        
        // Área de exibição do gráfico
        JPanel painelGraficoContainer = new JPanel(new BorderLayout());
        Tema.configurarPainel(painelGraficoContainer);
        painelGraficoContainer.setBorder(BorderFactory.createTitledBorder("Gráfico"));
        
        // Gráfico inicial
        painelGraficoAtual = new JPanel(new BorderLayout());
        Tema.configurarPainel(painelGraficoAtual);
        
        painelGraficoContainer.add(painelGraficoAtual, BorderLayout.CENTER);
        
        painelGraficos.add(painelControlesGraficos, BorderLayout.NORTH);
        painelGraficos.add(painelGraficoContainer, BorderLayout.CENTER);
    }
    
    private void configurarLayout() {
        // Adicionar abas
        abas.addTab("Relatórios", new ImageIcon("icons/relatorio.png"), painelRelatorios);
        abas.addTab("Gráficos", new ImageIcon("icons/relatorio.png"), painelGraficos);
        
        add(abas, BorderLayout.CENTER);
    }
    
    private void configurarEventos() {
        // Botão gerar relatório
        botaoGerar.addActionListener(e -> gerarRelatorio());
        
        // Botão exportar PDF
        botaoExportar.addActionListener(e -> exportarPDF());
        
        // Botão atualizar gráfico
        botaoAtualizarGrafico.addActionListener(e -> atualizarGrafico());
    }
    
    private void gerarRelatorio() {
        String tipoRelatorio = (String) comboTipoRelatorio.getSelectedItem();
        JTextArea areaRelatorio = (JTextArea) ((JScrollPane) ((JPanel) painelRelatorios.getComponent(1)).getComponent(0)).getViewport().getView();
        
        try {
            // Limpar área de relatório
            areaRelatorio.setText("Gerando relatório...");
            
            // Gerar conteúdo conforme tipo selecionado
            switch (tipoRelatorio) {
                case "Clientes":
                    gerarRelatorioClientes(areaRelatorio);
                    break;
                case "Veículos":
                    gerarRelatorioVeiculos(areaRelatorio);
                    break;
                case "Aluguéis":
                    gerarRelatorioAlugueis(areaRelatorio);
                    break;
                case "Funcionários":
                    gerarRelatorioFuncionarios(areaRelatorio);
                    break;
            }
            
            // Habilitar botão de exportação
            botaoExportar.setEnabled(true);
            
        } catch (Exception ex) {
            areaRelatorio.setText("Erro ao gerar relatório: " + ex.getMessage());
            botaoExportar.setEnabled(false);
            ex.printStackTrace();
        }
    }
    
    private void gerarRelatorioClientes(JTextArea area) {
        try {
            GerenciadorArquivos<Cliente> gerenciador = new GerenciadorArquivos<>("clientes.json", Cliente.class);
            List<Cliente> clientes = gerenciador.carregar();
            
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("===========================================================\n");
            relatorio.append("                RELATÓRIO DE CLIENTES                      \n");
            relatorio.append("===========================================================\n\n");
            relatorio.append(String.format("%-10s %-30s %-15s %-12s\n", "ID", "Nome", "CPF", "Data Cad."));
            relatorio.append("-----------------------------------------------------------\n");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Cliente cliente : clientes) {
                relatorio.append(String.format("%-10s %-30s %-15s %-12s\n",
                    cliente.getId(),
                    limitarTexto(cliente.getNome(), 30),
                    cliente.getCpf(),
                    cliente.getDataCadastro().format(formatter)
                ));
            }
            
            relatorio.append("-----------------------------------------------------------\n");
            relatorio.append("Total de clientes: ").append(clientes.size()).append("\n\n");
            
            long clientesAtivos = clientes.stream().filter(Cliente::isAtivo).count();
            relatorio.append("Clientes ativos: ").append(clientesAtivos).append("\n");
            relatorio.append("Clientes inativos: ").append(clientes.size() - clientesAtivos).append("\n");
            
            // Exibir na área de texto
            area.setText(relatorio.toString());
            ultimoRelatorioGerado = "clientes";
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de clientes: " + e.getMessage(), e);
        }
    }
    
    private void gerarRelatorioVeiculos(JTextArea area) {
        try {
            GerenciadorArquivos<Veiculo> gerenciador = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
            List<Veiculo> veiculos = gerenciador.carregar();
            
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("===========================================================\n");
            relatorio.append("                RELATÓRIO DE VEÍCULOS                      \n");
            relatorio.append("===========================================================\n\n");
            relatorio.append(String.format("%-10s %-15s %-15s %-10s %-10s %-10s\n", 
                "ID", "Modelo", "Marca", "Placa", "Ano", "Status"));
            relatorio.append("-----------------------------------------------------------\n");
            
            for (Veiculo veiculo : veiculos) {
                relatorio.append(String.format("%-10s %-15s %-15s %-10s %-10d %-10s\n",
                    veiculo.getId(),
                    limitarTexto(veiculo.getModelo(), 15),
                    limitarTexto(veiculo.getMarca(), 15),
                    veiculo.getPlaca(),
                    veiculo.getAno(),
                    veiculo.getStatus()
                ));
            }
            
            relatorio.append("-----------------------------------------------------------\n");
            relatorio.append("Total de veículos: ").append(veiculos.size()).append("\n\n");
            
            // Contagem por status
            relatorio.append("Por status:\n");
            veiculos.stream()
                .map(Veiculo::getStatus)
                .distinct()
                .forEach(status -> {
                    long count = veiculos.stream()
                        .filter(v -> v.getStatus() == status)
                        .count();
                    relatorio.append(status).append(": ").append(count).append("\n");
                });
            
            // Exibir na área de texto
            area.setText(relatorio.toString());
            ultimoRelatorioGerado = "veiculos";
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de veículos: " + e.getMessage(), e);
        }
    }
    
    private void gerarRelatorioAlugueis(JTextArea area) {
        try {
            GerenciadorArquivos<Aluguel> gerenciadorAluguel = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
            List<Aluguel> alugueis = gerenciadorAluguel.carregar();
            
            GerenciadorArquivos<Cliente> gerenciadorCliente = new GerenciadorArquivos<>("clientes.json", Cliente.class);
            List<Cliente> clientes = gerenciadorCliente.carregar();
            
            GerenciadorArquivos<Veiculo> gerenciadorVeiculo = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
            List<Veiculo> veiculos = gerenciadorVeiculo.carregar();
            
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("===========================================================\n");
            relatorio.append("                RELATÓRIO DE ALUGUÉIS                      \n");
            relatorio.append("===========================================================\n\n");
            relatorio.append(String.format("%-10s %-20s %-15s %-12s %-12s %-10s %-10s\n", 
                "ID", "Cliente", "Veículo", "Data Início", "Data Fim", "Valor", "Status"));
            relatorio.append("-----------------------------------------------------------\n");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Aluguel aluguel : alugueis) {
                // Buscar nome do cliente
                String nomeCliente = clientes.stream()
                    .filter(c -> c.getId().equals(aluguel.getClienteId()))
                    .findFirst()
                    .map(Cliente::getNome)
                    .orElse("Não encontrado");
                
                // Buscar info do veículo
                String infoVeiculo = veiculos.stream()
                    .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
                    .findFirst()
                    .map(v -> v.getMarca() + " " + v.getModelo())
                    .orElse("Não encontrado");
                
                relatorio.append(String.format("%-10s %-20s %-15s %-12s %-12s %-10s %-10s\n",
                    aluguel.getId(),
                    limitarTexto(nomeCliente, 20),
                    limitarTexto(infoVeiculo, 15),
                    aluguel.getDataInicio().format(formatter),
                    aluguel.getDataFimPrevista().format(formatter),
                    "R$ " + aluguel.getValorTotal(),
                    aluguel.getStatus()
                ));
            }
            
            relatorio.append("-----------------------------------------------------------\n");
            relatorio.append("Total de aluguéis: ").append(alugueis.size()).append("\n\n");
            
            // Contagem por status
            relatorio.append("Por status:\n");
            alugueis.stream()
                .map(Aluguel::getStatus)
                .distinct()
                .forEach(status -> {
                    long count = alugueis.stream()
                        .filter(a -> a.getStatus() == status)
                        .count();
                    relatorio.append(status).append(": ").append(count).append("\n");
                });
            
            // Exibir na área de texto
            area.setText(relatorio.toString());
            ultimoRelatorioGerado = "alugueis";
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de aluguéis: " + e.getMessage(), e);
        }
    }
    
    private void gerarRelatorioFuncionarios(JTextArea area) {
        try {
            GerenciadorArquivos<Funcionario> gerenciador = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class);
            List<Funcionario> funcionarios = gerenciador.carregar();
            
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("===========================================================\n");
            relatorio.append("               RELATÓRIO DE FUNCIONÁRIOS                   \n");
            relatorio.append("===========================================================\n\n");
            relatorio.append(String.format("%-10s %-25s %-15s %-15s %-12s\n", 
                "ID", "Nome", "CPF", "Tipo", "Data Contr."));
            relatorio.append("-----------------------------------------------------------\n");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Funcionario funcionario : funcionarios) {
                relatorio.append(String.format("%-10s %-25s %-15s %-15s %-12s\n",
                    funcionario.getId(),
                    limitarTexto(funcionario.getNome(), 25),
                    funcionario.getCpf(),
                    funcionario.getTipo(),
                    funcionario.getDataAdmissao().format(formatter)
                ));
            }
            
            relatorio.append("-----------------------------------------------------------\n");
            relatorio.append("Total de funcionários: ").append(funcionarios.size()).append("\n\n");
            
            // Contagem por tipo
            relatorio.append("Por tipo:\n");
            funcionarios.stream()
                .map(Funcionario::getTipo)
                .distinct()
                .forEach(tipo -> {
                    long count = funcionarios.stream()
                        .filter(f -> f.getTipo() == tipo)
                        .count();
                    relatorio.append(tipo).append(": ").append(count).append("\n");
                });
            
            // Exibir na área de texto
            area.setText(relatorio.toString());
            ultimoRelatorioGerado = "funcionarios";
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de funcionários: " + e.getMessage(), e);
        }
    }
    
    private void exportarPDF() {
        try {
            String caminhoArquivo = null;
            
            switch (ultimoRelatorioGerado) {
                case "clientes":
                    caminhoArquivo = PDFExporter.exportarRelatorioClientes();
                    break;
                case "veiculos":
                    caminhoArquivo = PDFExporter.exportarRelatorioVeiculos();
                    break;
                case "alugueis":
                    caminhoArquivo = PDFExporter.exportarRelatorioAlugueis();
                    break;
                case "funcionarios":
                    caminhoArquivo = PDFExporter.exportarRelatorioFuncionarios();
                    break;
            }
            
            if (caminhoArquivo != null) {
                File arquivo = new File(caminhoArquivo);
                
                if (arquivo.exists()) {
                    JOptionPane.showMessageDialog(this, 
                        "Relatório exportado com sucesso para:\n" + caminhoArquivo,
                        "Exportação", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Tentar abrir o arquivo
                    try {
                        Desktop.getDesktop().open(arquivo);
                    } catch (Exception ex) {
                        // Ignorar se não conseguir abrir
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Erro: Arquivo não foi gerado corretamente.",
                        "Exportação", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao exportar relatório para PDF.",
                    "Exportação", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao exportar: " + e.getMessage(),
                "Exportação", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void atualizarGrafico() {
        int tipoGrafico = comboTipoGrafico.getSelectedIndex();
        
        // Remover gráfico atual
        painelGraficoAtual.removeAll();
        
        // Criar novo gráfico conforme seleção
        JPanel novoGrafico;
        
        try {
            // Implementação direta dos gráficos
            switch (tipoGrafico) {
                case 0: // Status dos Veículos (Pizza)
                    // Obter dados de veículos
                    GerenciadorArquivos<Veiculo> gerenciadorVeiculos = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
                    List<Veiculo> veiculos = gerenciadorVeiculos.carregar();
                    
                    // Gerar gráfico de pizza (implementação básica)
                    novoGrafico = new JPanel(new BorderLayout());
                    
                    // Contar status
                    int disponiveis = 0, alugados = 0, manutencao = 0, inativos = 0;
                    for (Veiculo v : veiculos) {
                        switch (v.getStatus()) {
                            case DISPONIVEL: disponiveis++; break;
                            case ALUGADO: alugados++; break;
                            case MANUTENCAO: manutencao++; break;
                            case INATIVO: inativos++; break;
                            default: break;
                        }
                    }
                    
                    // Exibir informações simples
                    JTextArea info = new JTextArea();
                    info.setEditable(false);
                    info.append("Status de Veículos:\n\n");
                    info.append("- Disponíveis: " + disponiveis + "\n");
                    info.append("- Alugados: " + alugados + "\n");
                    info.append("- Em Manutenção: " + manutencao + "\n");
                    info.append("- Inativos: " + inativos + "\n");
                    info.append("\nTotal: " + veiculos.size() + " veículos");
                    
                    novoGrafico.add(new JScrollPane(info), BorderLayout.CENTER);
                    break;
                    
                case 1: // Veículos por Categoria (Barras)
                    // Obter dados de veículos
                    gerenciadorVeiculos = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
                    veiculos = gerenciadorVeiculos.carregar();
                    
                    // Gerar gráfico de barras (implementação básica)
                    novoGrafico = new JPanel(new BorderLayout());
                    
                    // Contar categorias
                    int economico = 0, intermediario = 0, executivo = 0, luxo = 0, suv = 0, compacto = 0, pickup = 0;
                    for (Veiculo v : veiculos) {
                        switch (v.getCategoria()) {
                            case ECONOMICO: economico++; break;
                            case INTERMEDIARIO: intermediario++; break;
                            case EXECUTIVO: executivo++; break;
                            case LUXO: luxo++; break;
                            case SUV: suv++; break;
                            case COMPACTO: compacto++; break;
                            case PICKUP: pickup++; break;
                            default: break;
                        }
                    }
                    
                    // Exibir informações simples
                    info = new JTextArea();
                    info.setEditable(false);
                    info.append("Categorias de Veículos:\n\n");
                    info.append("- Econômico: " + economico + "\n");
                    info.append("- Intermediário: " + intermediario + "\n");
                    info.append("- Executivo: " + executivo + "\n");
                    info.append("- Luxo: " + luxo + "\n");
                    info.append("- SUV: " + suv + "\n");
                    info.append("- Compacto: " + compacto + "\n");
                    info.append("- Pickup: " + pickup + "\n");
                    info.append("\nTotal: " + veiculos.size() + " veículos");
                    
                    novoGrafico.add(new JScrollPane(info), BorderLayout.CENTER);
                    break;
                    
                case 2: // Aluguéis por Mês (Barras)
                    // Obter dados de aluguéis
                    GerenciadorArquivos<Aluguel> gerenciadorAlugueis = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
                    List<Aluguel> alugueis = gerenciadorAlugueis.carregar();
                    
                    // Gerar gráfico de barras (implementação básica)
                    novoGrafico = new JPanel(new BorderLayout());
                    
                    // Exibir informações simples (últimos 6 meses)
                    info = new JTextArea();
                    info.setEditable(false);
                    info.append("Aluguéis por Mês (Últimos 6 meses):\n\n");
                    
                    // Agrupar por mês (simplificado)
                    int[] aluguelPorMes = new int[6];
                    String[] nomesMeses = new String[6];
                    
                    // Preencher nomes dos meses
                    LocalDate hoje = LocalDate.now();
                    for (int i = 0; i < 6; i++) {
                        LocalDate mesAnterior = hoje.minusMonths(i);
                        nomesMeses[i] = mesAnterior.getMonth().name() + "/" + mesAnterior.getYear();
                    }
                    
                    // Contar aluguéis por mês
                    for (Aluguel a : alugueis) {
                        for (int i = 0; i < 6; i++) {
                            LocalDate mesAnterior = hoje.minusMonths(i);
                            if (a.getDataInicio().getMonth() == mesAnterior.getMonth() && 
                                a.getDataInicio().getYear() == mesAnterior.getYear()) {
                                aluguelPorMes[i]++;
                                break;
                            }
                        }
                    }
                    
                    // Exibir dados
                    for (int i = 0; i < 6; i++) {
                        info.append("- " + nomesMeses[i] + ": " + aluguelPorMes[i] + " aluguéis\n");
                    }
                    
                    info.append("\nTotal: " + alugueis.size() + " aluguéis");
                    
                    novoGrafico.add(new JScrollPane(info), BorderLayout.CENTER);
                    break;
                    
                case 3: // Receita por Período (Linha)
                    // Obter dados de aluguéis
                    gerenciadorAlugueis = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
                    alugueis = gerenciadorAlugueis.carregar();
                    
                    // Gerar gráfico de linha (implementação básica)
                    novoGrafico = new JPanel(new BorderLayout());
                    
                    // Exibir informações simples (últimos 6 meses)
                    info = new JTextArea();
                    info.setEditable(false);
                    info.append("Receita por Mês (Últimos 6 meses):\n\n");
                    
                    // Agrupar por mês (simplificado)
                    double[] receitaPorMes = new double[6];
                    nomesMeses = new String[6];
                    
                    // Preencher nomes dos meses
                    hoje = LocalDate.now();
                    for (int i = 0; i < 6; i++) {
                        LocalDate mesAnterior = hoje.minusMonths(i);
                        nomesMeses[i] = mesAnterior.getMonth().name() + "/" + mesAnterior.getYear();
                    }
                    
                    // Calcular receita por mês
                    for (Aluguel a : alugueis) {
                        if (a.getStatus() != Aluguel.StatusAluguel.CANCELADO) {
                            for (int i = 0; i < 6; i++) {
                                LocalDate mesAnterior = hoje.minusMonths(i);
                                if (a.getDataInicio().getMonth() == mesAnterior.getMonth() && 
                                    a.getDataInicio().getYear() == mesAnterior.getYear()) {
                                    receitaPorMes[i] += a.getValorTotal().doubleValue();
                                    break;
                                }
                            }
                        }
                    }
                    
                    // Exibir dados
                    for (int i = 0; i < 6; i++) {
                        info.append("- " + nomesMeses[i] + ": R$ " + String.format("%.2f", receitaPorMes[i]) + "\n");
                    }
                    
                    // Calcular total
                    double totalReceita = 0;
                    for (double r : receitaPorMes) {
                        totalReceita += r;
                    }
                    
                    info.append("\nTotal: R$ " + String.format("%.2f", totalReceita));
                    
                    novoGrafico.add(new JScrollPane(info), BorderLayout.CENTER);
                    break;
                    
                default:
                    novoGrafico = new JPanel();
                    novoGrafico.add(new JLabel("Gráfico não disponível"));
            }
        } catch (Exception e) {
            novoGrafico = new JPanel();
            novoGrafico.add(new JLabel("Erro ao gerar gráfico: " + e.getMessage()));
            e.printStackTrace();
        }
        
        // Adicionar novo gráfico
        painelGraficoAtual.add(novoGrafico, BorderLayout.CENTER);
        
        // Atualizar interface
        painelGraficoAtual.revalidate();
        painelGraficoAtual.repaint();
    }
    
    private String limitarTexto(String texto, int tamanhoMaximo) {
        if (texto == null) return "";
        return texto.length() > tamanhoMaximo ? texto.substring(0, tamanhoMaximo - 3) + "..." : texto;
    }
    
    /**
     * Aplicar correções de botões quando o tema é alterado
     */
    public void aplicarTema() {
        // Corrigir cores em todos os botões da tela de forma recursiva
        util.CorrecaoTema.corrigirBotoesRecursivamente(this);
    }
}
