package ui;

import domain.Aluguel;
import domain.Cliente;
import domain.Veiculo;
import persistence.GerenciadorArquivos;
import util.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela para gerenciamento de aluguéis
 */
public class TelaAlugueis extends JPanel {

    private JTable tabelaAlugueis;
    private DefaultTableModel modeloTabela;
    private GerenciadorArquivos<Aluguel> gerenciador;
    private GerenciadorArquivos<Cliente> gerenciadorClientes;
    private GerenciadorArquivos<Veiculo> gerenciadorVeiculos;
    private JTextField campoBusca;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaAlugueis() {
        this.gerenciador = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
        this.gerenciadorClientes = new GerenciadorArquivos<>("clientes.json", Cliente.class);
        this.gerenciadorVeiculos = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Aluguéis"));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        // Título
        JLabel titulo = new JLabel("Gerenciamento de Aluguéis");
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
            "ID", "Cliente", "Veículo", "Data Início", "Data Fim",
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
        tabelaAlugueis.getColumnModel().getColumn(1).setPreferredWidth(150); // Cliente
        tabelaAlugueis.getColumnModel().getColumn(2).setPreferredWidth(150); // Veículo
        tabelaAlugueis.getColumnModel().getColumn(3).setPreferredWidth(100); // Data Início
        tabelaAlugueis.getColumnModel().getColumn(4).setPreferredWidth(100); // Data Fim
        tabelaAlugueis.getColumnModel().getColumn(5).setPreferredWidth(100); // Valor Diário
        tabelaAlugueis.getColumnModel().getColumn(6).setPreferredWidth(100); // Valor Total
        tabelaAlugueis.getColumnModel().getColumn(7).setPreferredWidth(100); // Status
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnNovo = new JButton("Novo Aluguel");
        btnNovo.setPreferredSize(new Dimension(150, 35));
        btnNovo.addActionListener(e -> abrirDialogoAluguel(null));

        JButton btnEditar = new JButton("Editar");
        btnEditar.setPreferredSize(new Dimension(100, 35));
        btnEditar.addActionListener(e -> editarAluguelSelecionado());

        JButton btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setPreferredSize(new Dimension(100, 35));
        btnFinalizar.addActionListener(e -> finalizarAluguelSelecionado());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> cancelarAluguelSelecionado());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 35));
        btnAtualizar.addActionListener(e -> carregarAlugueis());

        // Aplicar tema aos botões
        Tema.configurarBotao(btnNovo);
        Tema.configurarBotao(btnEditar);
        Tema.configurarBotao(btnFinalizar);
        Tema.configurarBotaoCancelamento(btnCancelar);
        Tema.configurarBotao(btnAtualizar);

        painel.add(btnNovo);
        painel.add(btnEditar);
        painel.add(btnFinalizar);
        painel.add(btnCancelar);
        painel.add(btnAtualizar);

        return painel;
    }

    private void carregarAlugueis() {
        modeloTabela.setRowCount(0);
        List<Aluguel> alugueis = gerenciador.carregar();
        List<Cliente> clientes = gerenciadorClientes.carregar();
        List<Veiculo> veiculos = gerenciadorVeiculos.carregar();

        for (Aluguel aluguel : alugueis) {
            // Buscar cliente
            String nomeCliente = clientes.stream()
                .filter(c -> c.getId().equals(aluguel.getClienteId()))
                .map(Cliente::getNome)
                .findFirst()
                .orElse("Cliente não encontrado");

            // Buscar veículo
            String descricaoVeiculo = veiculos.stream()
                .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
                .map(v -> v.getMarca() + " " + v.getModelo() + " (" + v.getPlaca() + ")")
                .findFirst()
                .orElse("Veículo não encontrado");

            Object[] linha = {
                aluguel.getId(),
                nomeCliente,
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
        List<Aluguel> alugueis = gerenciador.carregar();
        List<Cliente> clientes = gerenciadorClientes.carregar();
        List<Veiculo> veiculos = gerenciadorVeiculos.carregar();

        for (Aluguel aluguel : alugueis) {
            // Buscar cliente
            Cliente cliente = clientes.stream()
                .filter(c -> c.getId().equals(aluguel.getClienteId()))
                .findFirst()
                .orElse(null);

            // Buscar veículo
            Veiculo veiculo = veiculos.stream()
                .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
                .findFirst()
                .orElse(null);

            boolean incluir = aluguel.getId().toLowerCase().contains(termo) ||
                             aluguel.getStatus().getDescricao().toLowerCase().contains(termo);

            if (cliente != null) {
                incluir = incluir || cliente.getNome().toLowerCase().contains(termo);
            }

            if (veiculo != null) {
                incluir = incluir || veiculo.getMarca().toLowerCase().contains(termo) ||
                         veiculo.getModelo().toLowerCase().contains(termo) ||
                         veiculo.getPlaca().toLowerCase().contains(termo);
            }

            if (incluir) {
                String nomeCliente = cliente != null ? cliente.getNome() : "Cliente não encontrado";
                String descricaoVeiculo = veiculo != null ? 
                    veiculo.getMarca() + " " + veiculo.getModelo() + " (" + veiculo.getPlaca() + ")" :
                    "Veículo não encontrado";

                Object[] linha = {
                    aluguel.getId(),
                    nomeCliente,
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

    private void abrirDialogoAluguel(Aluguel aluguel) {
        DialogoAluguel dialogo = new DialogoAluguel((JFrame) SwingUtilities.getWindowAncestor(this), aluguel);
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            carregarAlugueis();
        }
    }

    private void editarAluguelSelecionado() {
        int linhaSelecionada = tabelaAlugueis.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um aluguel para editar.", 
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
            if (aluguel.getStatus() == Aluguel.StatusAluguel.FINALIZADO ||
                aluguel.getStatus() == Aluguel.StatusAluguel.CANCELADO) {
                JOptionPane.showMessageDialog(this,
                    "Não é possível editar um aluguel finalizado ou cancelado.",
                    "Operação não permitida",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            abrirDialogoAluguel(aluguel);
        }
    }

    private void finalizarAluguelSelecionado() {
        int linhaSelecionada = tabelaAlugueis.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um aluguel para finalizar.", 
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
            if (aluguel.getStatus() != Aluguel.StatusAluguel.ATIVO) {
                JOptionPane.showMessageDialog(this,
                    "Apenas aluguéis ativos podem ser finalizados.",
                    "Operação não permitida",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja finalizar este aluguel?",
                "Confirmar Finalização",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                // Solicitar quilometragem final
                String quilometragem = JOptionPane.showInputDialog(this,
                    "Informe a quilometragem final do veículo:",
                    "Finalizar Aluguel",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (quilometragem != null && !quilometragem.trim().isEmpty()) {
                    try {
                        int kmFinal = Integer.parseInt(quilometragem.trim());
                        aluguel.finalizar(java.time.LocalDate.now(), kmFinal);
                        
                        // Liberar veículo
                        List<Veiculo> veiculos = gerenciadorVeiculos.carregar();
                        veiculos.stream()
                            .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
                            .findFirst()
                            .ifPresent(v -> v.setStatus(Veiculo.Status.DISPONIVEL));
                        gerenciadorVeiculos.salvar(veiculos);
                        
                        gerenciador.salvar(alugueis);
                        carregarAlugueis();

                        JOptionPane.showMessageDialog(this,
                            "Aluguel finalizado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this,
                            "Quilometragem inválida!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void cancelarAluguelSelecionado() {
        int linhaSelecionada = tabelaAlugueis.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um aluguel para cancelar.", 
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
            if (aluguel.getStatus() == Aluguel.StatusAluguel.FINALIZADO ||
                aluguel.getStatus() == Aluguel.StatusAluguel.CANCELADO) {
                JOptionPane.showMessageDialog(this,
                    "Este aluguel já foi finalizado ou cancelado.",
                    "Operação não permitida",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja cancelar este aluguel?",
                "Confirmar Cancelamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                aluguel.setStatus(Aluguel.StatusAluguel.CANCELADO);
                
                // Liberar veículo
                List<Veiculo> veiculos = gerenciadorVeiculos.carregar();
                veiculos.stream()
                    .filter(v -> v.getId().equals(aluguel.getVeiculoId()))
                    .findFirst()
                    .ifPresent(v -> v.setStatus(Veiculo.Status.DISPONIVEL));
                gerenciadorVeiculos.salvar(veiculos);
                
                gerenciador.salvar(alugueis);
                carregarAlugueis();

                JOptionPane.showMessageDialog(this,
                    "Aluguel cancelado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void aplicarTema() {
        Tema.configurarPainel(this);
    }
}
