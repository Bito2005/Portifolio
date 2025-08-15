package ui;

import domain.Funcionario;
import persistence.GerenciadorArquivos;
import util.GeradorID;
import util.Mascaras;
import util.Tema;
import util.Validador;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Diálogo para cadastro e edição de funcionários
 */
public class DialogoFuncionario extends JDialog {

    private JTextField campoNome;
    private JFormattedTextField campoCpf;
    private JTextField campoEmail;
    private JFormattedTextField campoTelefone;
    private JTextField campoEndereco;
    private JTextField campoSalario;
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JComboBox<Funcionario.TipoFuncionario> comboTipo;
    private JCheckBox checkAtivo;
    
    private Funcionario funcionario;
    private boolean confirmado = false;
    private GerenciadorArquivos<Funcionario> gerenciador;

    public DialogoFuncionario(JFrame parent, Funcionario funcionario) {
        super(parent, funcionario == null ? "Novo Funcionário" : "Editar Funcionário", true);
        this.funcionario = funcionario;
        this.gerenciador = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class);
        
        inicializarComponentes();
        configurarLayouts();
        configurarEventos();
        aplicarTema();
        preencherCampos();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        // Campos de texto
        campoNome = new JTextField(20);
        campoEmail = new JTextField(20);
        campoEndereco = new JTextField(20);
        campoSalario = new JTextField(20);
        campoUsuario = new JTextField(20);
        campoSenha = new JPasswordField(20);
        campoConfirmarSenha = new JPasswordField(20);
        
        // Campos formatados
        try {
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            campoCpf = new JFormattedTextField(mascaraCpf);
            campoCpf.setColumns(15);
            
            MaskFormatter mascaraTelefone = new MaskFormatter("(##) #####-####");
            mascaraTelefone.setPlaceholderCharacter('_');
            campoTelefone = new JFormattedTextField(mascaraTelefone);
            campoTelefone.setColumns(15);
        } catch (Exception e) {
            campoCpf = new JFormattedTextField();
            campoTelefone = new JFormattedTextField();
        }
        
        // ComboBox
        comboTipo = new JComboBox<>(Funcionario.TipoFuncionario.values());
        
        // CheckBox
        checkAtivo = new JCheckBox("Funcionário Ativo");
        checkAtivo.setSelected(true);
    }

    private void configurarLayouts() {
        setLayout(new BorderLayout());

        // Painel principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        painelPrincipal.add(new JLabel("Nome*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoNome, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("CPF*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoCpf, gbc);

        // E-mail
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("E-mail*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoEmail, gbc);

        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Telefone*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoTelefone, gbc);

        // Endereço
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoEndereco, gbc);

        // Salário
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Salário*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoSalario, gbc);

        // Usuário
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Usuário*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoUsuario, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Senha*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoSenha, gbc);

        // Confirmar Senha
        gbc.gridx = 0; gbc.gridy = 8; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Confirmar Senha*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoConfirmarSenha, gbc);

        // Tipo
        gbc.gridx = 0; gbc.gridy = 9; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Tipo*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(comboTipo, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        painelPrincipal.add(checkAtivo, gbc);

        add(painelPrincipal, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setPreferredSize(new Dimension(100, 35));
        btnSalvar.addActionListener(e -> salvarFuncionario());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> dispose());

        Tema.configurarBotao(btnSalvar);
        Tema.configurarBotaoCancelamento(btnCancelar);
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        // Configurar validação em tempo real
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

        campoSalario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoSalario();
            }
        });
        
        // Adicionar validação para CPF
        campoCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoCpf();
            }
        });
        
        // Adicionar validação para telefone
        campoTelefone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoTelefone();
            }
        });
        
        // Adicionar validação para usuário
        campoUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoUsuario();
            }
        });
        
        // Adicionar validação para senha e confirmação
        campoSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarCampoSenha();
            }
        });
        
        campoConfirmarSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validarConfirmacaoSenha();
            }
        });
    }

    private void validarCampoNome() {
        if (campoNome.getText().trim().length() < 2) {
            campoNome.setBackground(new Color(255, 230, 230));
        } else {
            campoNome.setBackground(Color.WHITE);
        }
    }

    private void validarCampoEmail() {
        if (!Validador.validarEmail(campoEmail.getText())) {
            campoEmail.setBackground(new Color(255, 230, 230));
        } else {
            campoEmail.setBackground(Color.WHITE);
        }
    }

    private void validarCampoSalario() {
        try {
            Double.parseDouble(campoSalario.getText().replace(",", "."));
            campoSalario.setBackground(Color.WHITE);
        } catch (NumberFormatException e) {
            campoSalario.setBackground(new Color(255, 230, 230));
        }
    }
    
    private void validarCampoCpf() {
        String cpf = Mascaras.removerMascara(campoCpf.getText());
        
        // Verifica se o campo está completo
        if (cpf.length() != 11) {
            campoCpf.setBackground(new Color(255, 230, 230));
            return;
        }
        
        // Verifica se o CPF é válido usando o validador
        if (!Validador.validarCPFNumerico(cpf)) {
            campoCpf.setBackground(new Color(255, 230, 230));
            return;
        }
        
        // Verifica se o CPF já existe (exceto para o próprio funcionário)
        List<Funcionario> funcionarios = gerenciador.carregar();
        boolean cpfExiste = funcionarios.stream()
            .anyMatch(f -> {
                String cpfFunc = Mascaras.removerMascara(f.getCpf());
                return cpfFunc.equals(cpf) && 
                       (funcionario == null || !f.getId().equals(funcionario.getId()));
            });
        
        if (cpfExiste) {
            campoCpf.setBackground(new Color(255, 230, 230));
            return;
        }
        
        campoCpf.setBackground(Color.WHITE);
    }
    
    private void validarCampoTelefone() {
        String telefone = Mascaras.removerMascara(campoTelefone.getText());
        
        // Verifica se o campo está completo (11 dígitos para celular, 10 para fixo)
        if (telefone.length() < 10 || telefone.length() > 11) {
            campoTelefone.setBackground(new Color(255, 230, 230));
            return;
        }
        
        // Verifica se o telefone contém apenas dígitos
        if (!telefone.matches("\\d+")) {
            campoTelefone.setBackground(new Color(255, 230, 230));
            return;
        }
        
        campoTelefone.setBackground(Color.WHITE);
    }

    private void validarCampoUsuario() {
        String usuario = campoUsuario.getText().trim();
        
        // Verificar se o usuário tem pelo menos 3 caracteres
        if (usuario.length() < 3) {
            campoUsuario.setBackground(new Color(255, 230, 230));
            return;
        }
        
        // Verificar se o nome de usuário contém apenas letras, números, pontos, traços e sublinhados
        if (!usuario.matches("[a-zA-Z0-9._-]+")) {
            campoUsuario.setBackground(new Color(255, 230, 230));
            return;
        }
        
        // Verificar se o usuário já existe (exceto para o próprio funcionário)
        List<Funcionario> funcionarios = gerenciador.carregar();
        boolean usuarioExiste = funcionarios.stream()
            .anyMatch(f -> f.getUsuario() != null && 
                     f.getUsuario().equalsIgnoreCase(usuario) && 
                     (funcionario == null || !f.getId().equals(funcionario.getId())));
        
        if (usuarioExiste) {
            campoUsuario.setBackground(new Color(255, 230, 230));
            return;
        }
        
        campoUsuario.setBackground(Color.WHITE);
    }
    
    private void validarCampoSenha() {
        String senha = new String(campoSenha.getPassword());
        
        // Verificar se a senha tem pelo menos 6 caracteres
        if (senha.length() < 6) {
            campoSenha.setBackground(new Color(255, 230, 230));
            return;
        }
        
        campoSenha.setBackground(Color.WHITE);
        
        // Validar novamente a confirmação de senha
        validarConfirmacaoSenha();
    }
    
    private void validarConfirmacaoSenha() {
        String senha = new String(campoSenha.getPassword());
        String confirmacao = new String(campoConfirmarSenha.getPassword());
        
        // Verificar se as senhas coincidem
        if (!senha.equals(confirmacao)) {
            campoConfirmarSenha.setBackground(new Color(255, 230, 230));
            return;
        }
        
        campoConfirmarSenha.setBackground(Color.WHITE);
    }
    
    private void preencherCampos() {
        if (funcionario != null) {
            campoNome.setText(funcionario.getNome());
            campoCpf.setText(funcionario.getCpf());
            campoEmail.setText(funcionario.getEmail());
            campoTelefone.setText(funcionario.getTelefone());
            campoEndereco.setText(funcionario.getEndereco());
            campoSalario.setText(String.valueOf(funcionario.getSalario()));
            campoUsuario.setText(funcionario.getUsuario());
            // Não preencher senha para edição - exigir nova senha ou manter atual
            comboTipo.setSelectedItem(funcionario.getTipo());
            checkAtivo.setSelected(funcionario.isAtivo());
            
            // Desabilitar campos de senha na edição para mostrar que não serão alterados
            // a menos que o usuário insira novos valores
            campoSenha.setEnabled(false);
            campoConfirmarSenha.setEnabled(false);
            
            // Adicionar um KeyListener para habilitar os campos de senha se o usuário quiser alterar
            campoSenha.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    campoSenha.setEnabled(true);
                    campoConfirmarSenha.setEnabled(true);
                }
            });
            
            // Clique do mouse também habilita os campos
            campoSenha.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    campoSenha.setEnabled(true);
                    campoConfirmarSenha.setEnabled(true);
                }
            });
        }
    }

    private void salvarFuncionario() {
        if (!validarFormulario()) {
            return;
        }

        try {
            String nome = campoNome.getText().trim();
            String cpf = Mascaras.removerMascara(campoCpf.getText());
            String email = campoEmail.getText().trim();
            String telefone = Mascaras.removerMascara(campoTelefone.getText());
            String endereco = campoEndereco.getText().trim();
            double salario = Double.parseDouble(campoSalario.getText().replace(",", "."));
            String usuario = campoUsuario.getText().trim();
            String senha = new String(campoSenha.getPassword());
            Funcionario.TipoFuncionario tipo = (Funcionario.TipoFuncionario) comboTipo.getSelectedItem();
            boolean ativo = checkAtivo.isSelected();

            List<Funcionario> funcionarios = gerenciador.carregar();

            if (funcionario == null) {
                // Novo funcionário
                String id = GeradorID.gerarIDFuncionario();
                funcionario = new Funcionario(id, nome, cpf, email, telefone, endereco, 
                                            salario, tipo, LocalDate.now());
                // Definir usuário e senha para novo funcionário
                funcionario.setUsuario(usuario);
                funcionario.setSenha(senha);
                funcionario.setAtivo(ativo);
                funcionarios.add(funcionario);
            } else {
                // Editar funcionário existente
                funcionario.setNome(nome);
                funcionario.setCpf(cpf);
                funcionario.setEmail(email);
                funcionario.setTelefone(telefone);
                funcionario.setEndereco(endereco);
                funcionario.setSalario(salario);
                funcionario.setTipo(tipo);
                funcionario.setAtivo(ativo);
                funcionario.setUsuario(usuario);
                
                // Atualizar senha somente se uma nova senha for fornecida
                if (senha != null && !senha.isEmpty() && campoSenha.isEnabled()) {
                    funcionario.setSenha(senha);
                }
            }

            gerenciador.salvar(funcionarios);
            confirmado = true;
            
            JOptionPane.showMessageDialog(this,
                "Funcionário salvo com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
                
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar funcionário: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();

        // Validar nome
        if (campoNome.getText().trim().length() < 2) {
            erros.append("- Nome deve ter pelo menos 2 caracteres\n");
        }

        // Validar CPF
        String cpf = Mascaras.removerMascara(campoCpf.getText());
        if (!Validador.validarCPFNumerico(cpf)) {
            erros.append("- CPF inválido\n");
        } else {
            // Verificar se CPF já existe (exceto para o próprio funcionário)
            List<Funcionario> funcionarios = gerenciador.carregar();
            boolean cpfExiste = funcionarios.stream()
                .anyMatch(f -> {
                    String cpfFunc = Mascaras.removerMascara(f.getCpf());
                    return cpfFunc.equals(cpf) && 
                           (funcionario == null || !f.getId().equals(funcionario.getId()));
                });
            
            if (cpfExiste) {
                erros.append("- CPF já cadastrado\n");
            }
        }

        // Validar e-mail
        if (!Validador.validarEmail(campoEmail.getText())) {
            erros.append("- E-mail inválido\n");
        }

        // Validar telefone
        String telefone = Mascaras.removerMascara(campoTelefone.getText());
        if (telefone.length() < 10 || telefone.length() > 11 || !Validador.validarTelefone(telefone)) {
            erros.append("- Telefone inválido (deve ter 10 ou 11 dígitos)\n");
        }
        
        // Validar usuário
        String usuario = campoUsuario.getText().trim();
        if (usuario.length() < 3) {
            erros.append("- Usuário deve ter pelo menos 3 caracteres\n");
        } else if (!usuario.matches("[a-zA-Z0-9._-]+")) {
            erros.append("- Usuário deve conter apenas letras, números, pontos, traços e sublinhados\n");
        } else {
            // Verificar se o usuário já existe (exceto para o próprio funcionário)
            List<Funcionario> funcionariosCheck = gerenciador.carregar();
            boolean usuarioExiste = funcionariosCheck.stream()
                .anyMatch(f -> f.getUsuario() != null && 
                         f.getUsuario().equalsIgnoreCase(usuario) && 
                         (funcionario == null || !f.getId().equals(funcionario.getId())));
            
            if (usuarioExiste) {
                erros.append("- Nome de usuário já existe\n");
            }
        }
        
        // Validar senha para novos funcionários ou quando alterando senha
        String senhaValor = new String(campoSenha.getPassword());
        String confirmacaoSenha = new String(campoConfirmarSenha.getPassword());
        
        if (funcionario == null || (senhaValor.length() > 0 && campoSenha.isEnabled())) {
            if (senhaValor.length() < 6) {
                erros.append("- Senha deve ter pelo menos 6 caracteres\n");
            } else if (!senhaValor.equals(confirmacaoSenha)) {
                erros.append("- Senhas não coincidem\n");
            }
        }

        // Validar salário
        try {
            double salario = Double.parseDouble(campoSalario.getText().replace(",", "."));
            if (salario <= 0) {
                erros.append("- Salário deve ser maior que zero\n");
            }
        } catch (NumberFormatException e) {
            erros.append("- Salário inválido\n");
        }

        // Validar usuário
        if (campoUsuario.getText().trim().length() < 3) {
            erros.append("- Usuário deve ter pelo menos 3 caracteres\n");
        }

        // Validar senha
        String senha = new String(campoSenha.getPassword());
        if (senha.length() < 6) {
            erros.append("- Senha deve ter pelo menos 6 caracteres\n");
        }

        // Validar confirmação de senha
        String confirmacao = new String(campoConfirmarSenha.getPassword());
        if (!senha.equals(confirmacao)) {
            erros.append("- A confirmação da senha não coincide\n");
        }

        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Corrija os seguintes erros:\n\n" + erros.toString(),
                "Dados Inválidos",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void aplicarTema() {
        Tema.configurarDialog(this);
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
