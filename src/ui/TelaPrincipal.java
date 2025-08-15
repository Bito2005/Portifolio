package ui;

import auth.GerenciadorAutenticacao;
import util.Tema;
import util.CorrecaoTema;
import util.GeradorDadosExemplo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

/**
 * Tela principal do sistema AutoFácil
 * Contém sidebar e área de conteúdo
 */
public class TelaPrincipal extends JFrame {
    
    private GerenciadorAutenticacao gerenciadorAuth;
    private JPanel painelSidebar;
    private JPanel painelConteudo;
    private JLabel labelUsuario;
    private JButton botaoLogout;
    private CardLayout cardLayout;
    
    // Painéis das telas
    private JPanel painelInicio;
    private JPanel painelClientes;
    private JPanel painelVeiculos;
    private JPanel painelFuncionarios;
    private JPanel painelAlugueis;
    private JPanel painelMeusAlugueis;
    private JPanel painelRelatorios;
    
    // Estado da sidebar
    private boolean sidebarExpandida = false; // Inicia fechada
    private final int LARGURA_SIDEBAR_EXPANDIDA = 250;
    private final int LARGURA_SIDEBAR_RECOLHIDA = 60;
    
    // Animação para expansão/recolhimento
    private Timer timerAnimacao;
    private final int DURACAO_ANIMACAO = 180; // Duração ligeiramente maior para suavidade
    private final int PASSOS_ANIMACAO = 18;   // Mais passos para uma animação mais suave
    
    public TelaPrincipal() {
        gerenciadorAuth = GerenciadorAutenticacao.getInstance();
        
        // Gerar dados de exemplo se necessário
        GeradorDadosExemplo.gerarDadosSeNecessario();
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarJanela();
        
        // Mostrar tela de boas-vindas
        mostrarBoasVindas();
    }
    
    private void inicializarComponentes() {
        // Sidebar
        painelSidebar = new JPanel();
        painelSidebar.setLayout(new BoxLayout(painelSidebar, BoxLayout.Y_AXIS));
        painelSidebar.setBackground(Tema.AZUL_ESCURO);
        painelSidebar.setPreferredSize(new Dimension(LARGURA_SIDEBAR_RECOLHIDA, 600));
        
        // Area de conteudo
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        Tema.configurarPainel(painelConteudo);
        
        // Cabeçalho da sidebar
        criarCabecalhoSidebar();
        
        // Menu da sidebar
        criarMenuSidebar();
        
        // Rodapé da sidebar
        criarRodapeSidebar();
        
        // Painéis de conteúdo
        criarPaineisConteudo();
    }
    
    private void criarCabecalhoSidebar() {
        JPanel cabecalho = new JPanel();
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setBackground(Tema.AZUL_ESCURO);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Logo/Título
        JLabel titulo = new JLabel("AutoFácil");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecalho.add(titulo);
        
        // Versão
        JLabel versao = new JLabel("Java Edition");
        versao.setFont(new Font("Arial", Font.PLAIN, 12));
        versao.setForeground(Tema.AZUL_CLARO);
        versao.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecalho.add(versao);
        
        // Espaçamento
        cabecalho.add(Box.createVerticalStrut(10));
        
        // Usuário logado
        labelUsuario = new JLabel(gerenciadorAuth.getNomeUsuarioLogado());
        labelUsuario.setFont(new Font("Arial", Font.PLAIN, 11));
        labelUsuario.setForeground(Color.WHITE);
        labelUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecalho.add(labelUsuario);
        
        // Tipo de usuário
        String tipoUsuario = gerenciadorAuth.isAdmin() ? "Administrador" : 
                           gerenciadorAuth.isFuncionario() ? "Funcionário" : "Cliente";
        JLabel labelTipo = new JLabel("(" + tipoUsuario + ")");
        labelTipo.setFont(new Font("Arial", Font.ITALIC, 10));
        labelTipo.setForeground(Tema.AZUL_CLARO);
        labelTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecalho.add(labelTipo);
        
        painelSidebar.add(cabecalho);
    }
    
    private void criarMenuSidebar() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(Tema.AZUL_ESCURO);
        menu.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        
        // Botão Início
        adicionarBotaoMenu(menu, "Início", "inicio.png", "inicio");
        
        // Separador
        menu.add(Box.createVerticalStrut(10));
        
        // Botões principais
        if (gerenciadorAuth.isFuncionario()) {
            adicionarBotaoMenu(menu, "Clientes", "cliente.png", "clientes");
            adicionarBotaoMenu(menu, "Veículos", "veiculo.png", "veiculos");
            adicionarBotaoMenu(menu, "Aluguéis", "aluguel.png", "alugueis");
            adicionarBotaoMenu(menu, "Relatórios", "relatorio.png", "relatorios");
            
            if (gerenciadorAuth.isAdmin()) {
                adicionarBotaoMenu(menu, "Funcionários", "funcionario.png", "funcionarios");
            }
        }
        
        if (gerenciadorAuth.isCliente()) {
            adicionarBotaoMenu(menu, "Meus Aluguéis", "aluguel.png", "meus_alugueis");
        }
        
        painelSidebar.add(menu);
        
        // Espaçamento flexível
        painelSidebar.add(Box.createVerticalGlue());
    }
    
    private void criarRodapeSidebar() {
        JPanel rodape = new JPanel();
        rodape.setLayout(new BoxLayout(rodape, BoxLayout.Y_AXIS));
        rodape.setBackground(Tema.AZUL_ESCURO);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 5, 20, 5));
        
        // Botão sobre
        JButton botaoSobre = criarBotaoSidebar("Sobre", "sobre.png");
        botaoSobre.addActionListener(e -> mostrarSobre());
        rodape.add(botaoSobre);
        
        // Botão logout
        botaoLogout = criarBotaoSidebar("Sair", "sair.png");
        botaoLogout.addActionListener(e -> realizarLogout());
        rodape.add(botaoLogout);
        
        painelSidebar.add(rodape);
    }
    
    private void adicionarBotaoMenu(JPanel menu, String texto, String icone, String comando) {
        JButton botao = criarBotaoSidebar(texto, icone);
        botao.addActionListener(e -> mostrarTela(comando));
        menu.add(botao);
        menu.add(Box.createVerticalStrut(5));
    }
    
    private JButton criarBotaoSidebar(String texto, String nomeIcone) {
        // Criar botão especial para sidebar com alinhamento à esquerda
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.LEFT_ALIGNMENT);
        botao.setMaximumSize(new Dimension(240, 35));
        botao.setPreferredSize(new Dimension(240, 35));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Definir cores
        Color corFundo = Tema.AZUL_ESCURO;
        Color corTexto = Color.WHITE;
        
        // Carregar ícone antes de aplicar correção para que ele seja usado corretamente
        try {
            ImageIcon icone = new ImageIcon("icons/" + nomeIcone);
            if (icone.getIconWidth() > 0) {
                // Redimensionar ícone se necessário
                Image img = icone.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                botao.setIcon(new ImageIcon(img));
                
                // Guardar o ícone nas propriedades do botão para uso posterior
                botao.putClientProperty("icone", new ImageIcon(img));
            }
        } catch (Exception e) {
            // Ignorar se não conseguir carregar o ícone
        }
        
        // Aplicar solução extrema com UI personalizada
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Usar anti-aliasing para melhor qualidade
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Preencher o fundo do botão
                g2d.setColor(botao.getBackground());
                g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
                
                // Verificar se deve mostrar o texto (baseado no estado da sidebar)
                String text = botao.getText();
                if (text != null && !text.isEmpty()) {
                    // Desenhar texto com recuo para o ícone
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX;
                    int iconPadding = 30; // Espaço para o ícone + padding
                    textX = iconPadding; // Alinhar à esquerda com padding para o ícone
                    
                    int textY = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    
                    g2d.setColor(botao.getForeground());
                    g2d.drawString(text, textX, textY);
                }
                
                // Desenhar ícone se existir
                if (botao.getIcon() != null) {
                    int iconX = 5; // Padding à esquerda
                    int iconY = (c.getHeight() - botao.getIcon().getIconHeight()) / 2;
                    botao.getIcon().paintIcon(c, g2d, iconX, iconY);
                }
                
                g2d.dispose();
            }
        });
        
        // Configurações adicionais
        botao.setOpaque(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        
        // Efeito hover com UI personalizado
        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Configurações para hover
                SwingUtilities.invokeLater(() -> {
                    botao.setBackground(Tema.AZUL_CLARO);
                    botao.setForeground(Tema.AZUL_ESCURO);
                    botao.repaint();
                });
                
                // Expandir a sidebar ao passar o mouse sobre qualquer botão
                if (!sidebarExpandida) {
                    SwingUtilities.invokeLater(() -> expandirSidebar());
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                // Restaurar configurações originais
                SwingUtilities.invokeLater(() -> {
                    botao.setBackground(Tema.AZUL_ESCURO);
                    botao.setForeground(Color.WHITE);
                    botao.repaint();
                });
            }
        });
        
        return botao;
    }
    
    /**
     * Cria painel de relatórios
     */
    private JPanel criarPainelRelatorios() {
        return new TelaRelatorios();
    }
    
    private void criarPaineisConteudo() {
        // Painel de início
        painelInicio = criarPainelInicio();
        painelConteudo.add(painelInicio, "inicio");
        
        // Telas principais para funcionários
        if (gerenciadorAuth.isFuncionario()) {
            painelClientes = new TelaClientes();
            painelConteudo.add(painelClientes, "clientes");
            
            painelVeiculos = new TelaVeiculos();
            painelConteudo.add(painelVeiculos, "veiculos");
            
            painelAlugueis = new TelaAlugueis();
            painelConteudo.add(painelAlugueis, "alugueis");
            
            // Tela de relatórios
            painelRelatorios = criarPainelRelatorios();
            painelConteudo.add(painelRelatorios, "relatorios");
        }
        
        // Tela específica para clientes
        if (gerenciadorAuth.isCliente()) {
            painelMeusAlugueis = new TelaMeusAlugueis();
            painelConteudo.add(painelMeusAlugueis, "meus_alugueis");
        }
        
        // Tela específica para administradores
        if (gerenciadorAuth.isAdmin()) {
            painelFuncionarios = new TelaFuncionarios();
            painelConteudo.add(painelFuncionarios, "funcionarios");
        }
    }
    
    private JPanel criarPainelInicio() {
        JPanel painel = new JPanel(new BorderLayout());
        Tema.configurarPainel(painel);
        
        // Cabeçalho
        JPanel cabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Tema.configurarPainel(cabecalho);
        
        JLabel titulo = new JLabel("Bem-vindo ao AutoFácil!");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Tema.AZUL_ESCURO);
        cabecalho.add(titulo);
        
        painel.add(cabecalho, BorderLayout.NORTH);
        
        // Conteúdo central
        JPanel centro = new JPanel(new GridLayout(2, 2, 20, 20));
        Tema.configurarPainel(centro);
        centro.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Cards informativos
        centro.add(criarCardInfo("Sistema", "AutoFácil Java Edition", "Locadora de Veículos"));
        centro.add(criarCardInfo("Usuário", gerenciadorAuth.getNomeUsuarioLogado(), 
                               gerenciadorAuth.isAdmin() ? "Administrador" : 
                               gerenciadorAuth.isFuncionario() ? "Funcionário" : "Cliente"));
        centro.add(criarCardInfo("Data", java.time.LocalDate.now().toString(), "Hoje"));
        centro.add(criarCardInfo("Status", "Sistema Operacional", "Pronto para uso"));
        
        painel.add(centro, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarCardInfo(String titulo, String valor, String descricao) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Tema.AZUL_CLARO);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        labelTitulo.setForeground(Tema.AZUL_ESCURO);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(labelTitulo);
        
        card.add(Box.createVerticalStrut(10));
        
        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Arial", Font.BOLD, 16));
        labelValor.setForeground(Tema.PRETO);
        labelValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(labelValor);
        
        card.add(Box.createVerticalStrut(5));
        
        JLabel labelDescricao = new JLabel(descricao);
        labelDescricao.setFont(new Font("Arial", Font.ITALIC, 12));
        labelDescricao.setForeground(Tema.CINZA_ESCURO);
        labelDescricao.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(labelDescricao);
        
        return card;
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // Sidebar
        add(painelSidebar, BorderLayout.WEST);
        
        // Conteúdo principal
        add(painelConteudo, BorderLayout.CENTER);
        
        // Barra superior
        JPanel barraSuperior = criarBarraSuperior();
        add(barraSuperior, BorderLayout.NORTH);
    }
    
    private JPanel criarBarraSuperior() {
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(Tema.AZUL_ESCURO);
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Lado esquerdo - Info do sistema
        JPanel painelEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelEsquerdo.setBackground(Tema.AZUL_ESCURO);
        
        JLabel labelSistema = new JLabel("AutoFácil Java Edition");
        labelSistema.setFont(new Font("Arial", Font.BOLD, 14));
        labelSistema.setForeground(Color.WHITE);
        painelEsquerdo.add(labelSistema);
        
        JLabel labelSeparador = new JLabel(" | ");
        labelSeparador.setForeground(Tema.AZUL_CLARO);
        painelEsquerdo.add(labelSeparador);
        
        JLabel labelUsuario = new JLabel("Usuário: " + gerenciadorAuth.getNomeUsuarioLogado());
        labelUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        labelUsuario.setForeground(Tema.AZUL_CLARO);
        painelEsquerdo.add(labelUsuario);
        
        barraSuperior.add(painelEsquerdo, BorderLayout.WEST);
        
        // Lado direito - Controles
        JPanel painelDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelDireito.setBackground(Tema.AZUL_ESCURO);
        
        // Data e hora atual
        JLabel labelData = new JLabel(java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        labelData.setFont(new Font("Arial", Font.PLAIN, 11));
        labelData.setForeground(Tema.AZUL_CLARO);
        painelDireito.add(labelData);
        
        // Botão de logout
        JButton btnLogout = new JButton("Sair");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 11));
        btnLogout.setPreferredSize(new Dimension(50, 25));
        CorrecaoTema.corrigirBotao(btnLogout, Tema.VERMELHO, Color.WHITE);
        btnLogout.addActionListener(e -> realizarLogout());
        painelDireito.add(btnLogout);
        
        barraSuperior.add(painelDireito, BorderLayout.EAST);
        
        return barraSuperior;
    }
    
    private void configurarEventos() {
        // Declarando uma variável de membro para o timer global
        final Timer[] timerGlobal = new Timer[1];
        
        // Timer global para verificar o estado do mouse na sidebar
        timerGlobal[0] = new Timer(30, new ActionListener() { // Verificação mais frequente (30ms)
            // Variáveis para controle de estado
            private boolean mouseEstavaForaDaSidebar = true;
            private boolean mouseEstavaEmTransicao = false;
            private long ultimaVerificacao = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                long tempoAtual = System.currentTimeMillis();
                
                // Limitar a frequência de verificação para evitar sobrecarga
                if (tempoAtual - ultimaVerificacao < 15) { // Evita verificações excessivamente frequentes
                    return;
                }
                ultimaVerificacao = tempoAtual;
                
                // Obter a posição atual do mouse
                Point mousePos = MouseInfo.getPointerInfo().getLocation();
                
                // Converter para coordenadas da janela
                SwingUtilities.convertPointFromScreen(mousePos, TelaPrincipal.this);
                
                // Verificar se a posição do mouse está na área da sidebar
                boolean mouseNaSidebar = mousePos.x >= 0 && 
                                        mousePos.x <= (sidebarExpandida ? LARGURA_SIDEBAR_EXPANDIDA : LARGURA_SIDEBAR_RECOLHIDA + 10) && 
                                        mousePos.y >= 0 && 
                                        mousePos.y <= getHeight();
                                        
                // Implementação com histerese para evitar oscilações
                if (mouseNaSidebar && mouseEstavaForaDaSidebar && !mouseEstavaEmTransicao) {
                    // Mouse entrou na área da sidebar - expandir
                    mouseEstavaEmTransicao = true;
                    expandirSidebar();
                    // Programar reset do estado de transição após a animação
                    new Timer(DURACAO_ANIMACAO + 50, evt -> {
                        mouseEstavaEmTransicao = false;
                        mouseEstavaForaDaSidebar = false;
                        ((Timer)evt.getSource()).stop();
                    }).start();
                }
                else if (!mouseNaSidebar && !mouseEstavaForaDaSidebar && !mouseEstavaEmTransicao) {
                    // Mouse saiu da área da sidebar - recolher
                    mouseEstavaEmTransicao = true;
                    recolherSidebar();
                    // Programar reset do estado de transição após a animação
                    new Timer(DURACAO_ANIMACAO + 50, evt -> {
                        mouseEstavaEmTransicao = false;
                        mouseEstavaForaDaSidebar = true;
                        ((Timer)evt.getSource()).stop();
                    }).start();
                }
            }
        });
        
        // Iniciar o timer
        timerGlobal[0].start();
        
        // Adicionar um WindowListener para parar o timer quando a janela for fechada
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Parar o timer global
                if (timerGlobal[0] != null) {
                    timerGlobal[0].stop();
                }
                
                // Parar o timer de animação se existir
                if (timerAnimacao != null) {
                    timerAnimacao.stop();
                }
            }
        });
    }
    
    private void configurarJanela() {
        setTitle("AutoFácil - Java Edition");
        setSize(1024, 768);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            ImageIcon icone = new ImageIcon("icons/autofacil.png");
            setIconImage(icone.getImage());
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone da aplicação: " + e.getMessage());
        }
        
        // Inicializar sidebar como fechada, ocultando textos dos botões
        atualizarVisibilidadeTextosBotoes(false);
        
        // Exibir a janela
        setVisible(true);
    }
    
    private void mostrarTela(String tela) {
        cardLayout.show(painelConteudo, tela);
    }
    
    private void expandirSidebar() {
        if (sidebarExpandida) return;
        
        // Parar qualquer animação em andamento
        if (timerAnimacao != null && timerAnimacao.isRunning()) {
            timerAnimacao.stop();
        }
        
        final int larguraInicial = painelSidebar.getWidth();
        final int larguraFinal = LARGURA_SIDEBAR_EXPANDIDA;
        final double totalVariacao = larguraFinal - larguraInicial;
        final int atraso = DURACAO_ANIMACAO / PASSOS_ANIMACAO;
        
        // Garantir que todos os componentes da sidebar estejam visíveis no início da animação
        // Usar o EDT (Event Dispatch Thread) para evitar problemas de concorrência
        SwingUtilities.invokeLater(() -> atualizarVisibilidadeTextosBotoes(true));
        
        final long tempoInicio = System.currentTimeMillis();
        
        timerAnimacao = new Timer(atraso, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long tempoDecorrido = System.currentTimeMillis() - tempoInicio;
                
                // Calcular progresso usando uma função de suavização
                double progresso = Math.min(1.0, (double)tempoDecorrido / DURACAO_ANIMACAO);
                
                // Função de suavização cúbica (easeInOutCubic) para uma animação mais natural
                double progressoSuavizado;
                if (progresso < 0.5) {
                    progressoSuavizado = 4 * progresso * progresso * progresso;
                } else {
                    double f = ((2 * progresso) - 2);
                    progressoSuavizado = 0.5 * (f * f * f + 2);
                }
                
                // Calcular nova largura baseada no progresso
                final int novaLargura = larguraInicial + (int)(totalVariacao * progressoSuavizado);
                
                if (progresso >= 1.0) {
                    sidebarExpandida = true;
                    timerAnimacao.stop();
                    
                    // Definir a largura final após a animação
                    SwingUtilities.invokeLater(() -> {
                        painelSidebar.setPreferredSize(new Dimension(larguraFinal, getHeight()));
                        painelSidebar.revalidate();
                        painelSidebar.repaint();
                    });
                } else {
                    // Atualizar a UI de forma segura
                    SwingUtilities.invokeLater(() -> {
                        painelSidebar.setPreferredSize(new Dimension(novaLargura, getHeight()));
                        painelSidebar.revalidate();
                        painelSidebar.repaint();
                    });
                }
            }
        });
        
        timerAnimacao.start();
    }
    
    private void recolherSidebar() {
        if (!sidebarExpandida) return;
        
        // Parar qualquer animação em andamento
        if (timerAnimacao != null && timerAnimacao.isRunning()) {
            timerAnimacao.stop();
        }
        
        final int larguraInicial = painelSidebar.getWidth();
        final int larguraFinal = LARGURA_SIDEBAR_RECOLHIDA;
        final double totalVariacao = larguraInicial - larguraFinal;
        final int atraso = DURACAO_ANIMACAO / PASSOS_ANIMACAO;
        
        // Ocultar textos imediatamente para uma transição mais suave
        atualizarVisibilidadeTextosBotoes(false);
        
        final long tempoInicio = System.currentTimeMillis();
        
        timerAnimacao = new Timer(atraso, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long tempoDecorrido = System.currentTimeMillis() - tempoInicio;
                
                // Calcular progresso usando uma função de suavização
                double progresso = Math.min(1.0, (double)tempoDecorrido / DURACAO_ANIMACAO);
                
                // Função de suavização cúbica (easeInOutCubic) para uma animação mais natural
                double progressoSuavizado;
                if (progresso < 0.5) {
                    progressoSuavizado = 4 * progresso * progresso * progresso;
                } else {
                    double f = ((2 * progresso) - 2);
                    progressoSuavizado = 0.5 * (f * f * f + 2);
                }
                
                // Calcular nova largura baseada no progresso
                final int novaLargura = larguraInicial - (int)(totalVariacao * progressoSuavizado);
                
                if (progresso >= 1.0) {
                    sidebarExpandida = false;
                    timerAnimacao.stop();
                    
                    // Definir a largura final após a animação
                    SwingUtilities.invokeLater(() -> {
                        painelSidebar.setPreferredSize(new Dimension(larguraFinal, getHeight()));
                        painelSidebar.revalidate();
                        painelSidebar.repaint();
                    });
                } else {
                    // Atualizar a UI de forma segura
                    SwingUtilities.invokeLater(() -> {
                        painelSidebar.setPreferredSize(new Dimension(novaLargura, getHeight()));
                        painelSidebar.revalidate();
                        painelSidebar.repaint();
                    });
                }
            }
        });
        
        timerAnimacao.start();
    }
    
    /**
     * Atualiza a visibilidade dos textos nos botões da sidebar
     */
    private void atualizarVisibilidadeTextosBotoes(boolean mostrarTexto) {
        // Usar SwingUtilities.invokeLater para garantir que as alterações de UI ocorram na EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Percorrer todos os componentes da sidebar para atualizar a visibilidade do texto
                    percorrerComponentesRecursivamente(painelSidebar, component -> {
                        if (component instanceof JButton) {
                            JButton botao = (JButton) component;
                            if (!mostrarTexto) {
                                // Só salvar o texto se ainda não tiver sido salvo
                                if (botao.getClientProperty("textoOriginal") == null && 
                                    botao.getText() != null && !botao.getText().isEmpty()) {
                                    botao.putClientProperty("textoOriginal", botao.getText());
                                    botao.setText("");
                                }
                            } else {
                                Object textoOriginal = botao.getClientProperty("textoOriginal");
                                if (textoOriginal != null) {
                                    botao.setText(textoOriginal.toString());
                                }
                            }
                            
                            // Atualizar layout imediatamente para evitar atrasos visuais
                            botao.invalidate();
                            botao.revalidate();
                            botao.repaint();
                        }
                    });
                    
                    // Forçar a atualização imediata da UI para maior fluidez
                    painelSidebar.invalidate();
                    painelSidebar.revalidate();
                    painelSidebar.repaint();
                } catch (Exception e) {
                    // Proteger contra possíveis exceções durante a atualização da UI
                    System.err.println("Erro ao atualizar textos dos botões: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Percorre componentes recursivamente aplicando uma ação
     */
    private void percorrerComponentesRecursivamente(Container container, Consumer<Component> acao) {
        for (Component component : container.getComponents()) {
            acao.accept(component);
            if (component instanceof Container) {
                percorrerComponentesRecursivamente((Container) component, acao);
            }
        }
    }
    
    private void mostrarSobre() {
        String sobre = "AutoFácil Java Edition\n\n" +
                      "Sistema de Gerenciamento de Locadora de Veículos\n\n" +
                      "Características:\n" +
                      "• Interface moderna e responsiva\n" +
                      "• Controle de acesso por perfil\n" +
                      "• Validações completas\n" +
                      "• Relatórios e gráficos\n" +
                      "• Exportação em PDF\n" +
                      "• Persistência local em JSON\n\n" +
                      "Desenvolvido em Java Swing\n" +
                      "Ano: 2025";
        
        JOptionPane.showMessageDialog(this, sobre, "Sobre o Sistema", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarBoasVindas() {
        String mensagem = "Bem-vindo ao AutoFácil!\n\n" +
                         "Usuário: " + gerenciadorAuth.getNomeUsuarioLogado() + "\n" +
                         "Perfil: " + (gerenciadorAuth.isAdmin() ? "Administrador" : 
                                     gerenciadorAuth.isFuncionario() ? "Funcionário" : "Cliente") + "\n\n" +
                         "O sistema está pronto para uso!";
        
        JOptionPane.showMessageDialog(this, mensagem, "Boas-vindas", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void realizarLogout() {
        int opcao = JOptionPane.showConfirmDialog(this,
            "Deseja realmente sair do sistema?",
            "Logout", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcao == JOptionPane.YES_OPTION) {
            gerenciadorAuth.logout();
            dispose();
            SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
        }
    }
}