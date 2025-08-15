package ui;

import domain.Veiculo;
import persistence.GerenciadorArquivos;
import util.Tema;
import util.Validador;
import util.Mascaras;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Tela para gerenciamento de veículos
 */
public class TelaVeiculos extends JPanel {

    private JTable tabelaVeiculos;
    private DefaultTableModel modeloTabela;
    private GerenciadorArquivos<Veiculo> gerenciador;
    private JTextField campoBusca;

    public TelaVeiculos() {
        this.gerenciador = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
        inicializarComponentes();
        carregarVeiculos();
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
        JScrollPane scrollPane = new JScrollPane(tabelaVeiculos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Veículos"));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        // Título
        JLabel titulo = new JLabel("Gerenciamento de Veículos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        painel.add(titulo, BorderLayout.WEST);

        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBusca.add(new JLabel("Buscar:"));
        
        campoBusca = new JTextField(20);
        campoBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarVeiculos();
            }
        });
        painelBusca.add(campoBusca);

        painel.add(painelBusca, BorderLayout.EAST);

        return painel;
    }

    private void criarTabela() {
        String[] colunas = {
            "ID", "Marca", "Modelo", "Placa", "Ano", "Cor", 
            "Categoria", "Valor Diária", "Status", "Quilometragem"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaVeiculos = new JTable(modeloTabela);
        tabelaVeiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaVeiculos.getTableHeader().setReorderingAllowed(false);
        
        // Configurar larguras das colunas
        tabelaVeiculos.getColumnModel().getColumn(0).setPreferredWidth(150); // ID
        tabelaVeiculos.getColumnModel().getColumn(1).setPreferredWidth(100); // Marca
        tabelaVeiculos.getColumnModel().getColumn(2).setPreferredWidth(100); // Modelo
        tabelaVeiculos.getColumnModel().getColumn(3).setPreferredWidth(100); // Placa
        tabelaVeiculos.getColumnModel().getColumn(4).setPreferredWidth(60);  // Ano
        tabelaVeiculos.getColumnModel().getColumn(5).setPreferredWidth(80);  // Cor
        tabelaVeiculos.getColumnModel().getColumn(6).setPreferredWidth(120); // Categoria
        tabelaVeiculos.getColumnModel().getColumn(7).setPreferredWidth(100); // Valor
        tabelaVeiculos.getColumnModel().getColumn(8).setPreferredWidth(100); // Status
        tabelaVeiculos.getColumnModel().getColumn(9).setPreferredWidth(120); // Quilometragem
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        Tema.configurarPainel(painel);

        // Botão Novo Veículo (azul)
        JButton btnNovo = new JButton("Novo Veículo");
        btnNovo.setPreferredSize(new Dimension(140, 35));
        btnNovo.addActionListener(e -> abrirDialogoVeiculo(null));
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnNovo, Tema.AZUL_ESCURO, Color.WHITE);

        // Botão Editar (azul)
        JButton btnEditar = new JButton("Editar");
        btnEditar.setPreferredSize(new Dimension(100, 35));
        btnEditar.addActionListener(e -> editarVeiculoSelecionado());
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnEditar, Tema.AZUL_ESCURO, Color.WHITE);

        // Botão Excluir (vermelho)
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setPreferredSize(new Dimension(100, 35));
        btnExcluir.addActionListener(e -> excluirVeiculoSelecionado());
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnExcluir, Tema.VERMELHO, Color.WHITE);

        // Botão Atualizar (verde)
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 35));
        btnAtualizar.addActionListener(e -> carregarVeiculos());
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnAtualizar, Tema.VERDE, Color.WHITE);

        painel.add(btnNovo);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnAtualizar);

        return painel;
    }

    private void carregarVeiculos() {
        modeloTabela.setRowCount(0);
        List<Veiculo> veiculos = gerenciador.carregar();

        for (Veiculo veiculo : veiculos) {
            Object[] linha = {
                veiculo.getId(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getPlaca(),
                veiculo.getAno(),
                veiculo.getCor(),
                veiculo.getCategoria().getDescricao(),
                "R$ " + veiculo.getValorDiaria(),
                veiculo.getStatus().getDescricao(),
                veiculo.getQuilometragem() + " km"
            };
            modeloTabela.addRow(linha);
        }
    }

    private void filtrarVeiculos() {
        String termo = campoBusca.getText().toLowerCase().trim();
        
        if (termo.isEmpty()) {
            carregarVeiculos();
            return;
        }

        modeloTabela.setRowCount(0);
        List<Veiculo> veiculos = gerenciador.carregar();

        for (Veiculo veiculo : veiculos) {
            if (veiculo.getMarca().toLowerCase().contains(termo) ||
                veiculo.getModelo().toLowerCase().contains(termo) ||
                veiculo.getPlaca().toLowerCase().contains(termo) ||
                veiculo.getCor().toLowerCase().contains(termo)) {
                
                Object[] linha = {
                    veiculo.getId(),
                    veiculo.getMarca(),
                    veiculo.getModelo(),
                    veiculo.getPlaca(),
                    veiculo.getAno(),
                    veiculo.getCor(),
                    veiculo.getCategoria().getDescricao(),
                    "R$ " + veiculo.getValorDiaria(),
                    veiculo.getStatus().getDescricao(),
                    veiculo.getQuilometragem() + " km"
                };
                modeloTabela.addRow(linha);
            }
        }
    }

    private void abrirDialogoVeiculo(Veiculo veiculo) {
        DialogoVeiculo dialogo = new DialogoVeiculo((JFrame) SwingUtilities.getWindowAncestor(this), veiculo);
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            carregarVeiculos();
        }
    }

    private void editarVeiculoSelecionado() {
        int linhaSelecionada = tabelaVeiculos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo para editar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        List<Veiculo> veiculos = gerenciador.carregar();
        
        Veiculo veiculo = veiculos.stream()
            .filter(v -> v.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (veiculo != null) {
            abrirDialogoVeiculo(veiculo);
        }
    }

    private void excluirVeiculoSelecionado() {
        int linhaSelecionada = tabelaVeiculos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo para excluir.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este veículo?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            List<Veiculo> veiculos = gerenciador.carregar();
            
            veiculos.removeIf(v -> v.getId().equals(id));
            gerenciador.salvar(veiculos);
            carregarVeiculos();

            JOptionPane.showMessageDialog(this,
                "Veículo excluído com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void aplicarTema() {
        Tema.configurarPainel(this);
        
        // Corrigir cores em todos os botões da tela de forma recursiva
        util.CorrecaoTema.corrigirBotoesRecursivamente(this);
    }
}
