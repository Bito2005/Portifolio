package ui;

import auth.GerenciadorAutenticacao;
import domain.Cliente;
import util.GeradorID;
import util.Tema;
import util.CorrecaoTema;
import util.CampoTextoPersonalizado;
import util.CampoSenhaPersonalizado;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Tela de login do sistema AutoFácil
 */
public class TelaLogin extends JFrame {
    
    private CampoTextoPersonalizado campoUsuario;
    private CampoSenhaPersonalizado campoSenha;
    private JButton botaoLogin;
    private JButton botaoCadastrar;
    private JButton botaoSair;
    private JLabel labelTentativas;
    private JLabel labelStatus;
    
    private GerenciadorAutenticacao gerenciadorAuth;
    
    public TelaLogin() {
        gerenciadorAuth = GerenciadorAutenticacao.getInstance();
        
        // Aplicar correção Nimbus específica para campos de texto
        aplicarCorrecaoNimbusCampoTexto();
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarJanela();
        
        // Aplicar configuração avançada aos botões
        reconfigurarBotoes();
    }
    
    private void inicializarComponentes() {
        // Campos de entrada personalizados com aparência consistente
        campoUsuario = new CampoTextoPersonalizado(20);
        campoSenha = new CampoSenhaPersonalizado(20);
        
        // Botões
        botaoLogin = new JButton("Entrar");
        botaoCadastrar = new JButton("Cadastrar");
        botaoSair = new JButton("Sair");
        
        // Labels
        labelTentativas = new JLabel("");
        labelStatus = new JLabel("");
        
        // Definir tamanhos preferenciais
        Dimension tamanhoCampo = new Dimension(250, 35);
        Dimension tamanhoBotao = new Dimension(120, 40);
        
        // Configurar campos com dimensão fixa
        campoUsuario.setPreferredSize(tamanhoCampo);
        campoUsuario.setMinimumSize(tamanhoCampo);
        campoUsuario.setMaximumSize(tamanhoCampo);
        campoUsuario.setSize(tamanhoCampo);
        
        campoSenha.setPreferredSize(tamanhoCampo);
        campoSenha.setMinimumSize(tamanhoCampo);
        campoSenha.setMaximumSize(tamanhoCampo);
        campoSenha.setSize(tamanhoCampo);
        
        // Aplicar tema
        Tema.configurarCampoTexto(campoUsuario);
        Tema.configurarCampoTexto(campoSenha);
        
        // Usar nossa solução específica para os botões
        // Botão de login (verde)
        CorrecaoTema.corrigirBotao(botaoLogin, Tema.VERDE, Color.WHITE);
        
        // Botão de cadastro (azul)
        CorrecaoTema.corrigirBotao(botaoCadastrar, Tema.AZUL_ESCURO, Color.WHITE);
        
        // Botão de sair (vermelho)
        CorrecaoTema.corrigirBotao(botaoSair, Tema.VERMELHO, Color.WHITE);
        
        // Configurar fonte e tamanho
        Font fonteGrande = new Font("Arial", Font.BOLD, 14);
        campoUsuario.setFont(fonteGrande);
        campoSenha.setFont(fonteGrande);
        botaoLogin.setFont(fonteGrande);
        botaoCadastrar.setFont(fonteGrande);
        botaoSair.setFont(fonteGrande);
        
        botaoLogin.setPreferredSize(tamanhoBotao);
        botaoCadastrar.setPreferredSize(tamanhoBotao);
        botaoSair.setPreferredSize(tamanhoBotao);
        
        atualizarTentativas();
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        Tema.configurarPainel(painelPrincipal);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel titulo = new JLabel("AutoFácil Java Edition");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Tema.AZUL_ESCURO);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelPrincipal.add(titulo, gbc);
        
        // Subtítulo
        JLabel subtitulo = new JLabel("Sistema de Locadora de Veículos");
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitulo.setForeground(Tema.CINZA_ESCURO);
        gbc.gridy = 1;
        painelPrincipal.add(subtitulo, gbc);
        
        // Espaçamento
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        painelPrincipal.add(new JLabel(""), gbc);
        
        // Campo usuário
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JLabel labelUsuario = new JLabel("Usuário:");
        labelUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        Tema.configurarLabel(labelUsuario);
        painelPrincipal.add(labelUsuario, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        painelPrincipal.add(campoUsuario, gbc);
        
        // Campo senha
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("Arial", Font.BOLD, 12));
        Tema.configurarLabel(labelSenha);
        painelPrincipal.add(labelSenha, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        painelPrincipal.add(campoSenha, gbc);
        
        // Status
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        labelStatus.setForeground(Color.RED);
        labelStatus.setFont(new Font("Arial", Font.BOLD, 14));
        labelStatus.setPreferredSize(new Dimension(250, 25));
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(labelStatus, gbc);
        
        // Tentativas
        gbc.gridy = 6;
        labelTentativas.setForeground(Tema.LARANJA);
        painelPrincipal.add(labelTentativas, gbc);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        Tema.configurarPainel(painelBotoes);
        painelBotoes.add(botaoLogin);
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoSair);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 10, 10, 10);
        painelPrincipal.add(painelBotoes, gbc);
        
        // Instruções
        JPanel painelInstrucoes = new JPanel(new GridLayout(0, 1, 5, 5));
        Tema.configurarPainel(painelInstrucoes);
        painelInstrucoes.setBorder(Tema.criarBordaTitulo("Informações de Login"));
        
        JLabel instrucao1 = new JLabel("• Admin: admin / admin");
        JLabel instrucao2 = new JLabel("• Funcionário: usar usuário e senha");
        JLabel instrucao3 = new JLabel("• Cliente: usar CPF (sem formatação) e senha");
        
        Tema.configurarLabel(instrucao1);
        Tema.configurarLabel(instrucao2);
        Tema.configurarLabel(instrucao3);
        
        painelInstrucoes.add(instrucao1);
        painelInstrucoes.add(instrucao2);
        painelInstrucoes.add(instrucao3);
        
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelInstrucoes, BorderLayout.SOUTH);
    }
    
    private void configurarEventos() {
        // Botão login
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // Botão cadastrar
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaCadastroCliente();
            }
        });
        
        // Botão sair
        botaoSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Enter nos campos
        KeyListener enterListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        };
        
        campoUsuario.addKeyListener(enterListener);
        campoSenha.addKeyListener(enterListener);
        
        // Limpar status ao digitar apenas em certos casos
        KeyListener limparStatusListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                String mensagemAtual = labelStatus.getText();
                
                // Se não for a mensagem de senha incorreta, limpar ao digitar
                if (mensagemAtual.length() > 0 && 
                    !mensagemAtual.equals("Senha incorreta")) {
                    labelStatus.setText("");
                    labelStatus.setFont(new Font("Arial", Font.BOLD, 14));
                }
                
                // Garantir que os campos mantenham seu tamanho
                resetarCampos();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        };
        
        campoUsuario.addKeyListener(limparStatusListener);
        campoSenha.addKeyListener(limparStatusListener);
        
        // Adicionar listeners de foco para garantir que os campos mantenham a aparência correta
        campoUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Dimension tamanhoCampo = new Dimension(250, 35);
                campoUsuario.setBackground(Tema.BRANCO);
                campoUsuario.setForeground(Tema.PRETO);
                campoUsuario.setFont(new Font("Arial", Font.BOLD, 14));
                campoUsuario.setBorder(BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
                campoUsuario.setPreferredSize(tamanhoCampo);
                campoUsuario.setMinimumSize(tamanhoCampo);
                campoUsuario.setMaximumSize(tamanhoCampo);
                campoUsuario.setSize(tamanhoCampo);
                
                // Forçar atualização
                SwingUtilities.invokeLater(() -> {
                    revalidate();
                    repaint();
                });
            }
        });
        
        campoSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Dimension tamanhoCampo = new Dimension(250, 35);
                campoSenha.setBackground(Tema.BRANCO);
                campoSenha.setForeground(Tema.PRETO);
                campoSenha.setFont(new Font("Arial", Font.BOLD, 14));
                campoSenha.setBorder(BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
                campoSenha.setPreferredSize(tamanhoCampo);
                campoSenha.setMinimumSize(tamanhoCampo);
                campoSenha.setMaximumSize(tamanhoCampo);
                campoSenha.setSize(tamanhoCampo);
                
                // Forçar atualização
                SwingUtilities.invokeLater(() -> {
                    revalidate();
                    repaint();
                });
            }
        });
    }
    
    private void configurarJanela() {
        setTitle("AutoFácil - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        
        // Icone da janela (se disponivel)
        try {
            ImageIcon icone = new ImageIcon("icons/autofacil.png");
            if (icone.getIconWidth() > 0) {
                setIconImage(icone.getImage());
            }
        } catch (Exception e) {
            // Ignorar se não conseguir carregar o ícone
        }
        
        // Focar no campo usuário
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                campoUsuario.requestFocus();
            }
        });
    }
    
    private void realizarLogin() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            String mensagem = "";
            if (usuario.isEmpty() && senha.isEmpty()) {
                mensagem = "Preencha todos os campos!";
            } else if (usuario.isEmpty()) {
                mensagem = "Digite o usuário!";
            } else {
                mensagem = "Digite a senha!";
            }
            
            labelStatus.setText(mensagem);
            labelStatus.setForeground(Color.RED);
            
            // Restaurar a aparência dos campos
            resetarCampos();
            forcarTamanhoCampos();
            
            if (usuario.isEmpty()) {
                campoUsuario.requestFocus();
            } else {
                campoSenha.requestFocus();
            }
            revalidate();
            repaint();
            return;
        }
        
        // Desabilitar botão durante processamento
        botaoLogin.setEnabled(false);
        botaoLogin.setText("Verificando...");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GerenciadorAutenticacao.ResultadoLogin resultado = gerenciadorAuth.login(usuario, senha);
                
                if (resultado.isSucesso()) {
                    labelStatus.setText(resultado.getMensagem());
                    labelStatus.setForeground(Tema.VERDE);
                    
                    // Aguardar um pouco e abrir sistema principal
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            abrirSistemaPrincipal();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    // Para login com senha incorreta, sempre mostrar "Senha incorreta"
                    // independentemente da mensagem do gerenciador de autenticação
                    if (usuario.length() > 0 && senha.length() > 0) {
                        labelStatus.setText("Senha incorreta");
                    } else {
                        labelStatus.setText(resultado.getMensagem());
                    }
                    labelStatus.setForeground(Color.RED);
                    labelStatus.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    // Limpar senha
                    campoSenha.setText("");
                    
                    // Redefinir a aparência dos campos para manter consistência visual
                    resetarCampos();
                    
                    // Adicionar um pequeno atraso para garantir que a atualização ocorra após
                    // quaisquer mudanças automáticas do Swing/Nimbus
                    Timer timerResetCampos = new Timer(50, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            resetarCampos();
                            forcarTamanhoCampos();
                            // Focar no campo de senha para facilitar nova tentativa
                            campoSenha.requestFocus();
                        }
                    });
                    timerResetCampos.setRepeats(false);
                    timerResetCampos.start();
                    
                    // Garantir que a mensagem seja exibida corretamente
                    revalidate();
                    repaint();
                    
                    if (gerenciadorAuth.tentativasEsgotadas()) {
                        Timer timer = new Timer(2000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
                
                botaoLogin.setEnabled(true);
                botaoLogin.setText("Entrar");
                atualizarTentativas();
            }
        });
    }
    
    private void atualizarTentativas() {
        int tentativas = gerenciadorAuth.getTentativasRestantes();
        if (tentativas < 3) {
            labelTentativas.setText("Tentativas restantes: " + tentativas);
            labelTentativas.setForeground(tentativas == 1 ? Color.RED : Tema.LARANJA);
        } else {
            labelTentativas.setText("");
        }
    }
    
    private void abrirSistemaPrincipal() {
        dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }
    
    /**
     * Reconfigura os botões e campos de texto para garantir que a cor de fundo seja aplicada corretamente
     */
    private void reconfigurarBotoes() {
        // Usar a solução extrema para os botões - isso deve funcionar em todos os casos
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoLogin, Tema.VERDE, Color.WHITE);
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoCadastrar, Tema.AZUL_ESCURO, Color.WHITE);
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(botaoSair, Tema.VERMELHO, Color.WHITE);
        
        // Também redefinir os campos de texto para manter consistência visual
        resetarCampos();
    }
    
    /**
     * Abre a tela de cadastro de cliente
     */
    private void abrirTelaCadastroCliente() {
        DialogoCliente dialogo = new DialogoCliente(this, "Novo Cadastro de Cliente", null);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            Cliente clienteCadastrado = dialogo.getCliente();
            if (clienteCadastrado != null) {
                // Gerar ID para o cliente
                clienteCadastrado.setId(util.GeradorID.gerarIDCliente());
                
                // Salvar o cliente no sistema
                List<Cliente> clientes = gerenciadorAuth.getGerenciadorClientes().carregar();
                clientes.add(clienteCadastrado);
                gerenciadorAuth.getGerenciadorClientes().salvar(clientes);
                
                // Mostrar mensagem de sucesso
                JOptionPane.showMessageDialog(
                    this,
                    "Cliente cadastrado com sucesso!\nVocê já pode fazer login usando seu CPF e senha.",
                    "Cadastro Realizado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Preencher o campo de usuário com o CPF
                campoUsuario.setText(clienteCadastrado.getCpf().replaceAll("[^0-9]", ""));
                campoSenha.requestFocus();
            }
        }
    }
    
    /**
     * Redefine a aparência visual dos campos de texto para o padrão
     */
    private void resetarCampos() {
        // Forçar a atualização dos campos garantindo tamanhos fixos
        SwingUtilities.invokeLater(() -> {
            // Reconfigurar campo de usuário com estilo completo
            campoUsuario.setBackground(Tema.BRANCO);
            campoUsuario.setForeground(Tema.PRETO);
            campoUsuario.setFont(new Font("Arial", Font.BOLD, 14));
            campoUsuario.setBorder(BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
            
            Dimension tamanhoCampo = new Dimension(250, 35);
            campoUsuario.setPreferredSize(tamanhoCampo);
            campoUsuario.setMinimumSize(tamanhoCampo);
            campoUsuario.setMaximumSize(tamanhoCampo);
            campoUsuario.setSize(tamanhoCampo);
            
            // Reconfigurar campo de senha com estilo completo
            campoSenha.setBackground(Tema.BRANCO);
            campoSenha.setForeground(Tema.PRETO);
            campoSenha.setFont(new Font("Arial", Font.BOLD, 14));
            campoSenha.setBorder(BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
            
            campoSenha.setPreferredSize(tamanhoCampo);
            campoSenha.setMinimumSize(tamanhoCampo);
            campoSenha.setMaximumSize(tamanhoCampo);
            campoSenha.setSize(tamanhoCampo);
            
            // Atualizar o visual
            revalidate();
            repaint();
        });
    }
    
    /**
     * Aplica correção específica para campos de texto no Nimbus Look and Feel
     */
    private void aplicarCorrecaoNimbusCampoTexto() {
        try {
            // Garantir a aparência correta dos campos com Nimbus
            UIManager.put("TextField.background", Tema.BRANCO);
            UIManager.put("TextField.foreground", Tema.PRETO);
            UIManager.put("TextField.caretForeground", Tema.PRETO);
            UIManager.put("TextField.selectionBackground", Tema.AZUL_CLARO);
            UIManager.put("TextField.selectionForeground", Tema.PRETO);
            UIManager.put("TextField.border", BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
            
            UIManager.put("PasswordField.background", Tema.BRANCO);
            UIManager.put("PasswordField.foreground", Tema.PRETO);
            UIManager.put("PasswordField.caretForeground", Tema.PRETO);
            UIManager.put("PasswordField.selectionBackground", Tema.AZUL_CLARO);
            UIManager.put("PasswordField.selectionForeground", Tema.PRETO);
            UIManager.put("PasswordField.border", BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
            
            // Aplicar as mudanças aos componentes existentes
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Erro ao aplicar correções Nimbus: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        // Definir look and feel nativo
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }
    
    /**
     * Força a aplicação do tamanho correto para os campos após erro de senha
     * Este método garante que os campos não fiquem pequenos após um erro
     */
    private void forcarTamanhoCampos() {
        SwingUtilities.invokeLater(() -> {
            Dimension tamanhoCampo = new Dimension(250, 35);
            
            // Forçar tamanho do campo usuário
            campoUsuario.setPreferredSize(tamanhoCampo);
            campoUsuario.setMinimumSize(tamanhoCampo);
            campoUsuario.setMaximumSize(tamanhoCampo);
            campoUsuario.setSize(tamanhoCampo);
            
            // Forçar tamanho do campo senha
            campoSenha.setPreferredSize(tamanhoCampo);
            campoSenha.setMinimumSize(tamanhoCampo);
            campoSenha.setMaximumSize(tamanhoCampo);
            campoSenha.setSize(tamanhoCampo);
            
            // Atualizar o layout e repainting
            invalidate();
            validate();
            repaint();
        });
    }
}
