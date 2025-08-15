package ui;

import domain.Cliente;
import persistence.GerenciadorArquivos;
import util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de gestão de clientes
 */
public class TelaClientes extends JPanel {
    
    private GerenciadorArquivos<Cliente> gerenciadorClientes;
    
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    private JTextField campoBusca;
    
    public TelaClientes() {
        gerenciadorClientes = new GerenciadorArquivos<>("clientes.json", Cliente.class);
        
        inicializarComponentes();
        carregarClientes();
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
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        // Título
        JLabel titulo = new JLabel("Gerenciamento de Clientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        painel.add(titulo, BorderLayout.WEST);

        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBusca.add(new JLabel("Buscar:"));
        
        campoBusca = new JTextField(20);
        campoBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarClientes();
            }
        });
        painelBusca.add(campoBusca);

        painel.add(painelBusca, BorderLayout.EAST);

        return painel;
    }
    
    private void criarTabela() {
        String[] colunas = {
            "ID", "Nome", "CPF", "Email", "Telefone", 
            "CEP", "Endereço", "Status"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaClientes = new JTable(modeloTabela);
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaClientes.getTableHeader().setReorderingAllowed(false);
        
        // Configurar larguras das colunas
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(150); // ID
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(120); // CPF
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(180); // Email
        tabelaClientes.getColumnModel().getColumn(4).setPreferredWidth(120); // Telefone
        tabelaClientes.getColumnModel().getColumn(5).setPreferredWidth(100); // CEP
        tabelaClientes.getColumnModel().getColumn(6).setPreferredWidth(200); // Endereço
        tabelaClientes.getColumnModel().getColumn(7).setPreferredWidth(80);  // Status
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        Tema.configurarPainel(painel);

        // Botão Novo Cliente (azul)
        JButton btnNovo = new JButton("Novo Cliente");
        btnNovo.setPreferredSize(new Dimension(140, 35));
        btnNovo.addActionListener(e -> abrirDialogoCliente(null));
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnNovo, Tema.AZUL_ESCURO, Color.WHITE);

        // Botão Editar (azul)
        JButton btnEditar = new JButton("Editar");
        btnEditar.setPreferredSize(new Dimension(100, 35));
        btnEditar.addActionListener(e -> editarClienteSelecionado());
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnEditar, Tema.AZUL_ESCURO, Color.WHITE);

        // Botão Excluir (vermelho)
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setPreferredSize(new Dimension(100, 35));
        btnExcluir.addActionListener(e -> excluirClienteSelecionado());
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnExcluir, Tema.VERMELHO, Color.WHITE);

        // Botão Atualizar (verde)
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 35));
        btnAtualizar.addActionListener(e -> carregarClientes());
        util.CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnAtualizar, Tema.VERDE, Color.WHITE);

        painel.add(btnNovo);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnAtualizar);

        return painel;
    }
    
    private void carregarClientes() {
        modeloTabela.setRowCount(0);
        List<Cliente> clientes = gerenciadorClientes.carregar();

        for (Cliente cliente : clientes) {
            Object[] linha = {
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCep(),
                cliente.getEndereco(),
                cliente.isAtivo() ? "Ativo" : "Inativo"
            };
            modeloTabela.addRow(linha);
        }
    }
    
    private void filtrarClientes() {
        String termo = campoBusca.getText().toLowerCase().trim();
        
        if (termo.isEmpty()) {
            carregarClientes();
            return;
        }

        modeloTabela.setRowCount(0);
        List<Cliente> clientes = gerenciadorClientes.carregar();

        for (Cliente cliente : clientes) {
            if (cliente.getNome().toLowerCase().contains(termo) ||
                cliente.getCpf().contains(termo) ||
                cliente.getEmail().toLowerCase().contains(termo)) {
                
                Object[] linha = {
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getEmail(),
                    cliente.getTelefone(),
                    cliente.getCep(),
                    cliente.getEndereco(),
                    cliente.isAtivo() ? "Ativo" : "Inativo"
                };
                modeloTabela.addRow(linha);
            }
        }
    }
    
    private void abrirDialogoCliente(Cliente cliente) {
        DialogoCliente dialogo = new DialogoCliente(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            cliente == null ? "Novo Cliente" : "Editar Cliente", 
            cliente
        );
        
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            carregarClientes();
        }
    }

    private void editarClienteSelecionado() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um cliente para editar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        List<Cliente> clientes = gerenciadorClientes.carregar();
        
        Cliente cliente = clientes.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (cliente != null) {
            abrirDialogoCliente(cliente);
        }
    }
    
    private void excluirClienteSelecionado() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um cliente para excluir.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este cliente?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            List<Cliente> clientes = gerenciadorClientes.carregar();
            
            // Opção 1: Remover permanentemente
            clientes.removeIf(c -> c.getId().equals(id));
            
            // Opção 2: Marcar como inativo (se preferir)
            /*
            for (Cliente cliente : clientes) {
                if (cliente.getId().equals(id)) {
                    cliente.setAtivo(false);
                    break;
                }
            }
            */
            
            gerenciadorClientes.salvar(clientes);
            carregarClientes();

            JOptionPane.showMessageDialog(this,
                "Cliente excluído com sucesso!",
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
