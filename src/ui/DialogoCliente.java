package ui;

import domain.Cliente;
import persistence.GerenciadorArquivos;
import util.*;
import util.CorrecaoTema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Diálogo para cadastro/edição de clientes
 */
public class DialogoCliente extends JDialog {
    
    private JTextField campoNome;
    private JFormattedTextField campoCpf;
    private JTextField campoEmail;
    private JFormattedTextField campoTelefone;
    private JFormattedTextField campoCep;
    private JTextField campoEndereco;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JCheckBox checkAtivo;
    
    private JButton botaoSalvar;
    private JButton botaoCancelar;
    
    private Cliente cliente;
    private boolean confirmado = false;
    private boolean edicao = false;
    
    public DialogoCliente(Frame parent, String titulo, Cliente cliente) {
        super(parent, titulo, true);
        this.cliente = cliente;
        this.edicao = (cliente != null);
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarJanela();
        
        if (edicao) {
            preencherCampos();
        }
        
        // Aplicar tema
        Tema.aplicarTemaRecursivo(this);
    }
    
    private void inicializarComponentes() {
        // Campos de texto
        campoNome = new JTextField(20);
        campoCpf = Mascaras.criarCampoCPF();
        campoEmail = new JTextField(20);
        campoTelefone = Mascaras.criarCampoTelefone();
        campoCep = Mascaras.criarCampoCEP();
        campoEndereco = new JTextField(20);
        campoSenha = new JPasswordField(20);
        campoConfirmarSenha = new JPasswordField(20);
        checkAtivo = new JCheckBox("Cliente Ativo", true);
        
        // Botões
        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setPreferredSize(new Dimension(100, 35));
        
        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(100, 35));
        
        // Configurações visuais
        Tema.configurarBotao(botaoSalvar);
        Tema.configurarBotaoCancelamento(botaoCancelar);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Nome*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoNome, gbc);
        
        // CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("CPF*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoCpf, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Email*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoEmail, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Telefone*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoTelefone, gbc);
        
        // CEP
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("CEP*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoCep, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoEndereco, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Senha*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoSenha, gbc);
        
        // Confirmar senha
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Confirmar Senha*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoConfirmarSenha, gbc);
        
        // Status ativo (apenas para edição)
        if (edicao) {
            gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
            painelPrincipal.add(checkAtivo, gbc);
        }
        
        add(painelPrincipal, BorderLayout.CENTER);
        
        // Painel de botões (usando o mesmo estilo dos outros diálogos)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);
        
        add(painelBotoes, BorderLayout.SOUTH);
        
        // Nota sobre campos obrigatórios na parte superior
        JPanel painelNota = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel notaCamposObrigatorios = new JLabel("* Campos obrigatórios");
        notaCamposObrigatorios.setFont(new Font("Arial", Font.ITALIC, 10));
        notaCamposObrigatorios.setForeground(Tema.VERMELHO);
        painelNota.add(notaCamposObrigatorios);
        
        add(painelNota, BorderLayout.NORTH);
    }
    
    // Método removido: criarLabel
    
    private void configurarEventos() {
        botaoSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarCliente();
            }
        });
        
        botaoCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Enter para salvar
        KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKey, "salvar");
        getRootPane().getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarCliente();
            }
        });
        
        // Escape para cancelar
        KeyStroke escapeKey = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKey, "cancelar");
        getRootPane().getActionMap().put("cancelar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Validação em tempo real
        campoNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoNome();
            }
        });
        
        campoEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoEmail();
            }
        });
        
        campoSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoSenha();
            }
        });
        
        campoConfirmarSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoConfirmarSenha();
            }
        });
    }
    
    private void configurarJanela() {
        // Aplicar tema geral ao diálogo
        Tema.aplicarTemaRecursivo(this);
        
        // Aplicar correções específicas para os botões no tema Nimbus
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoSalvar, Tema.VERDE, Color.WHITE);
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoCancelar, Tema.VERMELHO, Color.WHITE);
        
        // Configurações da janela
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
        
        // Carregar ícone
        try {
            ImageIcon icone = new ImageIcon("icons/cliente.png");
            if (icone.getIconWidth() > 0) {
                setIconImage(icone.getImage());
            }
        } catch (Exception e) {
            // Ignorar se não conseguir carregar o ícone
        }
        
        // Focar no primeiro campo
        SwingUtilities.invokeLater(() -> campoNome.requestFocus());
    }
    
    private void preencherCampos() {
        if (cliente != null) {
            campoNome.setText(cliente.getNome());
            campoCpf.setText(cliente.getCpf());
            campoEmail.setText(cliente.getEmail());
            campoTelefone.setText(cliente.getTelefone());
            campoCep.setText(cliente.getCep());
            campoEndereco.setText(cliente.getEndereco());
            // Não preencher senha por segurança
            checkAtivo.setSelected(cliente.isAtivo());
        }
    }
    
    private void salvarCliente() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }
        
        try {
            // Criar ou atualizar cliente
            if (cliente == null) {
                cliente = new Cliente();
            }
            
            cliente.setNome(campoNome.getText().trim());
            cliente.setCpf(campoCpf.getText());
            cliente.setEmail(campoEmail.getText().trim());
            cliente.setTelefone(campoTelefone.getText());
            cliente.setCep(campoCep.getText());
            cliente.setEndereco(campoEndereco.getText().trim());
            cliente.setSenha(new String(campoSenha.getPassword()));
            
            if (edicao) {
                cliente.setAtivo(checkAtivo.isSelected());
            } else {
                cliente.setDataCadastro(LocalDate.now());
                cliente.setAtivo(true);
            }
            
            confirmado = true;
            
            // Mostrar mensagem de sucesso se for cadastro (não mostrar se for chamado da tela de login)
            if (!edicao && getOwner() instanceof TelaPrincipal) {
                JOptionPane.showMessageDialog(this,
                    "Cliente salvo com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar cliente: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        StringBuilder erros = new StringBuilder();
        
        // Nome
        if (!Validador.validarTextoObrigatorio(campoNome.getText())) {
            erros.append("- Nome é obrigatório\n");
        }
        
        // CPF
        String cpf = campoCpf.getText();
        if (!Validador.validarCPF(cpf)) {
            erros.append("- ").append(Validador.obterMensagemErro("cpf", cpf)).append("\n");
        } else {
            // Verificar se CPF já existe (exceto para o próprio cliente)
            try {
                GerenciadorArquivos<Cliente> gerenciador = new GerenciadorArquivos<>("clientes.json", Cliente.class);
                List<Cliente> clientes = gerenciador.carregar();
                
                boolean cpfExiste = clientes.stream()
                    .anyMatch(c -> c.getCpf().replaceAll("[^0-9]", "").equals(cpf.replaceAll("[^0-9]", "")) && 
                             (cliente == null || !c.getId().equals(cliente.getId())));
                
                if (cpfExiste) {
                    erros.append("- CPF já cadastrado\n");
                }
            } catch (Exception e) {
                erros.append("- Erro ao verificar CPF: ").append(e.getMessage()).append("\n");
            }
        }
        
        // Email
        String email = campoEmail.getText();
        if (!Validador.validarEmail(email)) {
            erros.append("- ").append(Validador.obterMensagemErro("email", email)).append("\n");
        }
        
        // Telefone
        String telefone = campoTelefone.getText();
        if (!Validador.validarTelefone(telefone)) {
            erros.append("- ").append(Validador.obterMensagemErro("telefone", telefone)).append("\n");
        }
        
        // CEP
        String cep = campoCep.getText();
        if (!Validador.validarCEP(cep)) {
            erros.append("- ").append(Validador.obterMensagemErro("cep", cep)).append("\n");
        }
        
        // Senha
        String senha = new String(campoSenha.getPassword());
        if (!Validador.validarSenha(senha)) {
            erros.append("- ").append(Validador.obterMensagemErro("senha", senha)).append("\n");
        }
        
        // Confirmar senha
        String confirmarSenha = new String(campoConfirmarSenha.getPassword());
        if (!senha.equals(confirmarSenha)) {
            erros.append("- As senhas não coincidem\n");
        }
        
        // Verificar se há erros
        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Corrija os seguintes erros:\n\n" + erros.toString(),
                "Dados Inválidos",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    // Método removido: mostrarErro
    
    // Getters
    public Cliente getCliente() {
        return cliente;
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
    
    /**
     * Valida o campo nome em tempo real
     */
    private void validarCampoNome() {
        if (campoNome.getText().trim().length() < 3) {
            campoNome.setBackground(new Color(255, 230, 230));
        } else {
            campoNome.setBackground(Color.WHITE);
        }
    }
    
    /**
     * Valida o campo email em tempo real
     */
    private void validarCampoEmail() {
        if (!Validador.validarEmail(campoEmail.getText())) {
            campoEmail.setBackground(new Color(255, 230, 230));
        } else {
            campoEmail.setBackground(Color.WHITE);
        }
    }
    
    /**
     * Valida o campo senha em tempo real
     */
    private void validarCampoSenha() {
        String senha = new String(campoSenha.getPassword());
        if (!Validador.validarSenha(senha)) {
            campoSenha.setBackground(new Color(255, 230, 230));
        } else {
            campoSenha.setBackground(Color.WHITE);
        }
    }
    
    /**
     * Valida se a confirmação de senha corresponde à senha
     */
    private void validarCampoConfirmarSenha() {
        String senha = new String(campoSenha.getPassword());
        String confirmarSenha = new String(campoConfirmarSenha.getPassword());
        
        if (!senha.equals(confirmarSenha)) {
            campoConfirmarSenha.setBackground(new Color(255, 230, 230));
        } else {
            campoConfirmarSenha.setBackground(Color.WHITE);
        }
    }
}
