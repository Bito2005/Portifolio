package ui;

import domain.Funcionario;
import persistence.GerenciadorArquivos;
import util.Tema;
import util.Validador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela para gerenciamento de funcionários
 */
public class TelaFuncionarios extends JPanel {

    private JTable tabelaFuncionarios;
    private DefaultTableModel modeloTabela;
    private GerenciadorArquivos<Funcionario> gerenciador;
    private JTextField campoBusca;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaFuncionarios() {
        this.gerenciador = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class);
        inicializarComponentes();
        carregarFuncionarios();
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
        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Funcionários"));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        // Título
        JLabel titulo = new JLabel("Gerenciamento de Funcionários");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        painel.add(titulo, BorderLayout.WEST);

        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBusca.add(new JLabel("Buscar:"));
        
        campoBusca = new JTextField(20);
        campoBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarFuncionarios();
            }
        });
        painelBusca.add(campoBusca);

        painel.add(painelBusca, BorderLayout.EAST);

        return painel;
    }

    private void criarTabela() {
        String[] colunas = {
            "ID", "Nome", "CPF", "E-mail", "Telefone", 
            "Tipo", "Status", "Data Cadastro"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaFuncionarios = new JTable(modeloTabela);
        tabelaFuncionarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaFuncionarios.getTableHeader().setReorderingAllowed(false);
        
        // Configurar larguras das colunas
        tabelaFuncionarios.getColumnModel().getColumn(0).setPreferredWidth(150); // ID
        tabelaFuncionarios.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
        tabelaFuncionarios.getColumnModel().getColumn(2).setPreferredWidth(120); // CPF
        tabelaFuncionarios.getColumnModel().getColumn(3).setPreferredWidth(180); // E-mail
        tabelaFuncionarios.getColumnModel().getColumn(4).setPreferredWidth(120); // Telefone
        tabelaFuncionarios.getColumnModel().getColumn(5).setPreferredWidth(100); // Tipo
        tabelaFuncionarios.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        tabelaFuncionarios.getColumnModel().getColumn(7).setPreferredWidth(100); // Data
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnNovo = new JButton("Novo Funcionário");
        btnNovo.setPreferredSize(new Dimension(160, 35));
        btnNovo.addActionListener(e -> abrirDialogoFuncionario(null));

        JButton btnEditar = new JButton("Editar");
        btnEditar.setPreferredSize(new Dimension(100, 35));
        btnEditar.addActionListener(e -> editarFuncionarioSelecionado());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setPreferredSize(new Dimension(100, 35));
        btnExcluir.addActionListener(e -> excluirFuncionarioSelecionado());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 35));
        btnAtualizar.addActionListener(e -> carregarFuncionarios());

        // Aplicar tema aos botões
        Tema.configurarBotao(btnNovo);
        Tema.configurarBotao(btnEditar);
        Tema.configurarBotaoCancelamento(btnExcluir);
        Tema.configurarBotao(btnAtualizar);

        painel.add(btnNovo);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnAtualizar);

        return painel;
    }

    private void carregarFuncionarios() {
        modeloTabela.setRowCount(0);
        List<Funcionario> funcionarios = gerenciador.carregar();

        for (Funcionario funcionario : funcionarios) {
            Object[] linha = {
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getCpf(),
                funcionario.getEmail(),
                funcionario.getTelefone(),
                funcionario.getTipo().getDescricao(),
                funcionario.isAtivo() ? "Ativo" : "Inativo",
                funcionario.getDataCadastro().format(FORMATO_DATA)
            };
            modeloTabela.addRow(linha);
        }
    }

    private void filtrarFuncionarios() {
        String termo = campoBusca.getText().toLowerCase().trim();
        
        if (termo.isEmpty()) {
            carregarFuncionarios();
            return;
        }

        modeloTabela.setRowCount(0);
        List<Funcionario> funcionarios = gerenciador.carregar();

        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getNome().toLowerCase().contains(termo) ||
                funcionario.getCpf().contains(termo) ||
                funcionario.getEmail().toLowerCase().contains(termo)) {
                
                Object[] linha = {
                    funcionario.getId(),
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getEmail(),
                    funcionario.getTelefone(),
                    funcionario.getTipo().getDescricao(),
                    funcionario.isAtivo() ? "Ativo" : "Inativo",
                    funcionario.getDataCadastro().format(FORMATO_DATA)
                };
                modeloTabela.addRow(linha);
            }
        }
    }

    private void abrirDialogoFuncionario(Funcionario funcionario) {
        DialogoFuncionario dialogo = new DialogoFuncionario((JFrame) SwingUtilities.getWindowAncestor(this), funcionario);
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            carregarFuncionarios();
        }
    }

    private void editarFuncionarioSelecionado() {
        int linhaSelecionada = tabelaFuncionarios.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um funcionário para editar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        List<Funcionario> funcionarios = gerenciador.carregar();
        
        Funcionario funcionario = funcionarios.stream()
            .filter(f -> f.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (funcionario != null) {
            abrirDialogoFuncionario(funcionario);
        }
    }

    private void excluirFuncionarioSelecionado() {
        int linhaSelecionada = tabelaFuncionarios.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um funcionário para excluir.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        
        // Verificar se é o admin (não pode ser excluído)
        if (id.equals("ADMIN-001")) {
            JOptionPane.showMessageDialog(this,
                "O usuário administrador não pode ser excluído.",
                "Operação não permitida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este funcionário?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            List<Funcionario> funcionarios = gerenciador.carregar();
            
            funcionarios.removeIf(f -> f.getId().equals(id));
            gerenciador.salvar(funcionarios);
            carregarFuncionarios();

            JOptionPane.showMessageDialog(this,
                "Funcionário excluído com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aplicarTema() {
        Tema.configurarPainel(this);
    }
}
