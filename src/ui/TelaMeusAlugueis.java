package ui;

import domain.Aluguel;
import domain.Veiculo;
import persistence.GerenciadorArquivos;
import auth.GerenciadorAutenticacao;
import util.Tema;
import util.CorrecaoTema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tela para visualização de aluguéis do cliente logado
 */
public class TelaMeusAlugueis extends JPanel {

    private JTable tabelaAlugueis;
    private DefaultTableModel modeloTabela;
    private GerenciadorArquivos<Aluguel> gerenciador;
    private GerenciadorArquivos<Veiculo> gerenciadorVeiculos;
    private GerenciadorAutenticacao gerenciadorAuth;
    private JTextField campoBusca;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private String clienteId;

    public TelaMeusAlugueis() {
        this.gerenciador = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
        this.gerenciadorVeiculos = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
        this.gerenciadorAuth = GerenciadorAutenticacao.getInstance();
        
        // Obter ID do cliente logado
        this.clienteId = gerenciadorAuth.getClienteLogadoId();
        
        inicializarComponentes();
        carregarAlugueis();
        aplicarTema();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel superior
        JPanel painelSuperior = criarPainelSuperior();
        add(painelSuperior, BorderLayout.NORTH);

        // Tabela
        criarTabela();
        JScrollPane scrollPane = new JScrollPane(tabelaAlugueis);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Meus Aluguéis"));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        // Título
        JLabel titulo = new JLabel("Meus Aluguéis");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        painel.add(titulo, BorderLayout.WEST);

        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBusca.add(new JLabel("Buscar:"));
        
        campoBusca = new JTextField(20);
        campoBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarAlugueis();
            }
        });
        painelBusca.add(campoBusca);

        painel.add(painelBusca, BorderLayout.EAST);

        return painel;
    }

    private void criarTabela() {
        String[] colunas = {
            "ID", "Veículo", "Data Início", "Data Fim",
            "Valor Diário", "Valor Total", "Status"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAlugueis = new JTable(modeloTabela);
        tabelaAlugueis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaAlugueis.getTableHeader().setReorderingAllowed(false);
        
        // Configurar larguras das colunas
        tabelaAlugueis.getColumnModel().getColumn(0).setPreferredWidth(150); // ID
        tabelaAlugueis.getColumnModel().getColumn(1).setPreferredWidth(180); // Veículo
        tabelaAlugueis.getColumnModel().getColumn(2).setPreferredWidth(100); // Data Início
        tabelaAlugueis.getColumnModel().getColumn(3).setPreferredWidth(100); // Data Fim
        tabelaAlugueis.getColumnModel().getColumn(4).setPreferredWidth(100); // Valor Diário
        tabelaAlugueis.getColumnModel().getColumn(5).setPreferredWidth(100); // Valor Total
        tabelaAlugueis.getColumnModel().getColumn(6).setPreferredWidth(120); // Status
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        Tema.configurarPainel(painel);

        // Botão Novo Aluguel
        JButton btnNovoAluguel = new JButton("Novo Aluguel");
        btnNovoAluguel.setPreferredSize(new Dimension(140, 35));
        btnNovoAluguel.addActionListener(e -> abrirDialogoNovoAluguel());
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnNovoAluguel, Tema.AZUL_ESCURO, Color.WHITE);

        // Botão Detalhes
        JButton btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.setPreferredSize(new Dimension(140, 35));
        btnDetalhes.addActionListener(e -> verDetalhesAluguelSelecionado());
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnDetalhes, Tema.AZUL_ESCURO, Color.WHITE);

        // Botão Atualizar
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 35));
        btnAtualizar.addActionListener(e -> carregarAlugueis());
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnAtualizar, Tema.VERDE, Color.WHITE);

        painel.add(btnNovoAluguel);
        painel.add(btnDetalhes);
        painel.add(btnAtualizar);

        return painel;
    }
    
    private void carregarAlugueis() {
        modeloTabela.setRowCount(0);
        List<Aluguel> todosAlugueis = gerenciador.carregar();
        List<Veiculo> veiculos = gerenciadorVeiculos.carregar();
        
        // Filtrar apenas os aluguéis do cliente logado
        List<Aluguel> meusAlugueis = todosAlugueis.stream()
                .filter(a -> a.getClienteId().equals(clienteId))
                .collect(Collectors.toList());

        for (Aluguel aluguel : meusAlugueis) {
            // Buscar veículo
            String descricaoVeiculo = veiculos.stream()
                .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
                .map(v -> v.getMarca() + " " + v.getModelo() + " (" + v.getPlaca() + ")")
                .findFirst()
                .orElse("Veículo não encontrado");

            Object[] linha = {
                aluguel.getId(),
                descricaoVeiculo,
                aluguel.getDataInicio().format(FORMATO_DATA),
                aluguel.getDataFimReal() != null ? aluguel.getDataFimReal().format(FORMATO_DATA) : 
                    aluguel.getDataFimPrevista().format(FORMATO_DATA),
                String.format("R$ %.2f", aluguel.getValorDiaria()),
                String.format("R$ %.2f", aluguel.getValorTotal()),
                aluguel.getStatus().getDescricao()
            };
            modeloTabela.addRow(linha);
        }
    }

    private void filtrarAlugueis() {
        String termo = campoBusca.getText().toLowerCase().trim();
        
        if (termo.isEmpty()) {
            carregarAlugueis();
            return;
        }

        modeloTabela.setRowCount(0);
        List<Aluguel> todosAlugueis = gerenciador.carregar();
        List<Veiculo> veiculos = gerenciadorVeiculos.carregar();

        // Filtrar apenas os aluguéis do cliente logado
        List<Aluguel> meusAlugueis = todosAlugueis.stream()
                .filter(a -> a.getClienteId().equals(clienteId))
                .collect(Collectors.toList());

        for (Aluguel aluguel : meusAlugueis) {
            // Buscar veículo
            Veiculo veiculo = veiculos.stream()
                .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
                .findFirst()
                .orElse(null);

            boolean incluir = aluguel.getId().toLowerCase().contains(termo) ||
                             aluguel.getStatus().getDescricao().toLowerCase().contains(termo);

            if (veiculo != null) {
                incluir = incluir || veiculo.getMarca().toLowerCase().contains(termo) ||
                         veiculo.getModelo().toLowerCase().contains(termo) ||
                         veiculo.getPlaca().toLowerCase().contains(termo);
            }

            if (incluir) {
                String descricaoVeiculo = veiculo != null ? 
                    veiculo.getMarca() + " " + veiculo.getModelo() + " (" + veiculo.getPlaca() + ")" :
                    "Veículo não encontrado";

                Object[] linha = {
                    aluguel.getId(),
                    descricaoVeiculo,
                    aluguel.getDataInicio().format(FORMATO_DATA),
                    aluguel.getDataFimReal() != null ? aluguel.getDataFimReal().format(FORMATO_DATA) : 
                        aluguel.getDataFimPrevista().format(FORMATO_DATA),
                    String.format("R$ %.2f", aluguel.getValorDiaria()),
                    String.format("R$ %.2f", aluguel.getValorTotal()),
                    aluguel.getStatus().getDescricao()
                };
                modeloTabela.addRow(linha);
            }
        }
    }

    private void verDetalhesAluguelSelecionado() {
        int linhaSelecionada = tabelaAlugueis.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um aluguel para ver os detalhes.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        List<Aluguel> alugueis = gerenciador.carregar();
        
        Aluguel aluguel = alugueis.stream()
            .filter(a -> a.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (aluguel != null) {
            mostrarDetalhesAluguel(aluguel);
        }
    }
    
    private void mostrarDetalhesAluguel(Aluguel aluguel) {
        // Buscar informações do veículo
        List<Veiculo> veiculos = gerenciadorVeiculos.carregar();
        Veiculo veiculo = veiculos.stream()
            .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
            .findFirst()
            .orElse(null);
        
        // Criar mensagem com detalhes
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("ID do Aluguel: ").append(aluguel.getId()).append("\n\n");
        
        if (veiculo != null) {
            detalhes.append("VEICULO\n");
            detalhes.append("Marca/Modelo: ").append(veiculo.getMarca()).append(" ").append(veiculo.getModelo()).append("\n");
            detalhes.append("Placa: ").append(veiculo.getPlaca()).append("\n");
            detalhes.append("Categoria: ").append(veiculo.getCategoria().getDescricao()).append("\n");
            detalhes.append("Ano: ").append(veiculo.getAno()).append("\n\n");
        }
        
        detalhes.append("DADOS DO ALUGUEL\n");
        detalhes.append("Data Início: ").append(aluguel.getDataInicio().format(FORMATO_DATA)).append("\n");
        detalhes.append("Data Fim Prevista: ").append(aluguel.getDataFimPrevista().format(FORMATO_DATA)).append("\n");
        
        if (aluguel.getDataFimReal() != null) {
            detalhes.append("Data Devolução: ").append(aluguel.getDataFimReal().format(FORMATO_DATA)).append("\n");
        }
        
        detalhes.append("Valor Diária: R$ ").append(String.format("%.2f", aluguel.getValorDiaria())).append("\n");
        detalhes.append("Valor Total: R$ ").append(String.format("%.2f", aluguel.getValorTotal())).append("\n");
        detalhes.append("Status: ").append(aluguel.getStatus().getDescricao()).append("\n");
        
        if (aluguel.getObservacoes() != null && !aluguel.getObservacoes().isEmpty()) {
            detalhes.append("\nObservações: ").append(aluguel.getObservacoes()).append("\n");
        }
        
        // Exibir detalhes em uma caixa de diálogo
        JTextArea textArea = new JTextArea(detalhes.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detalhes do Aluguel", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Abre o diálogo para criar um novo aluguel para o cliente logado
     */
    private void abrirDialogoNovoAluguel() {
        try {
            // Cria um novo diálogo de aluguel configurado para o cliente logado
            DialogoAluguel dialogo = new DialogoAluguel(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                null  // Novo aluguel
            );
            
            // O diálogo de aluguel precisa ser modificado para usar o cliente logado
            // em vez de permitir a seleção de qualquer cliente
            dialogo.configurarClienteLogado(clienteId);
            
            dialogo.setVisible(true);
            
            // Recarregar aluguéis após fechar o diálogo
            if (dialogo.isConfirmado()) {
                carregarAlugueis();
                JOptionPane.showMessageDialog(this, 
                    "Aluguel criado com sucesso!",
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao abrir diálogo de aluguel: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void aplicarTema() {
        Tema.configurarPainel(this);
        
        // Corrigir cores em todos os botões da tela de forma recursiva
        CorrecaoTema.corrigirBotoesRecursivamente(this);
    }
}
