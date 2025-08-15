package util;

import javax.swing.*;
import java.awt.*;
import util.CorrecaoTema;

/**
 * Gerenciador de temas do sistema
 * Cores obrigatórias conforme especificação
 */
public class Tema {
    
    // Cores obrigatórias do sistema
    public static final Color AZUL_ESCURO = new Color(31, 78, 121);      // #1F4E79
    public static final Color AZUL_CLARO = new Color(220, 230, 241);     // #DCE6F1
    public static final Color VERDE = new Color(76, 175, 80);            // #4CAF50
    public static final Color BRANCO = new Color(255, 255, 255);         // #FFFFFF
    public static final Color CINZA_CLARO = new Color(245, 245, 245);    // #F5F5F5
    public static final Color CINZA_ESCURO = new Color(51, 51, 51);      // #333333
    public static final Color PRETO = new Color(0, 0, 0);               // #000000
    
    // Cores de status
    public static final Color VERMELHO = new Color(244, 67, 54);         // #F44336
    public static final Color AMARELO = new Color(255, 235, 59);         // #FFEB3B
    public static final Color LARANJA = new Color(255, 152, 0);          // #FF9800
    
    // Tema atual
    private static boolean temaEscuro = false;
    
    /**
     * Alterna entre tema claro e escuro
     */
    public static void alternarTema() {
        temaEscuro = !temaEscuro;
        aplicarTema();
    }
    
    /**
     * Define o tema como escuro
     */
    public static void definirTemaEscuro(boolean escuro) {
        temaEscuro = escuro;
        aplicarTema();
    }
    
    /**
     * Verifica se o tema atual é escuro
     */
    public static boolean isTemaEscuro() {
        return temaEscuro;
    }
    
    /**
     * Aplica o tema atual ao sistema
     */
    private static void aplicarTema() {
        try {
            if (temaEscuro) {
                // Tema escuro
                UIManager.put("Panel.background", CINZA_ESCURO);
                UIManager.put("Label.foreground", BRANCO);
                UIManager.put("TextField.background", CINZA_ESCURO);
                UIManager.put("TextField.foreground", BRANCO);
                UIManager.put("Button.background", AZUL_ESCURO);
                UIManager.put("Button.foreground", BRANCO);
                UIManager.put("Table.background", CINZA_ESCURO);
                UIManager.put("Table.foreground", BRANCO);
                UIManager.put("ScrollPane.background", CINZA_ESCURO);
                UIManager.put("ComboBox.background", CINZA_ESCURO);
                UIManager.put("ComboBox.foreground", BRANCO);
            } else {
                // Tema claro
                UIManager.put("Panel.background", BRANCO);
                UIManager.put("Label.foreground", PRETO);
                UIManager.put("TextField.background", BRANCO);
                UIManager.put("TextField.foreground", PRETO);
                UIManager.put("Button.background", AZUL_ESCURO);
                UIManager.put("Button.foreground", BRANCO);
                UIManager.put("Table.background", BRANCO);
                UIManager.put("Table.foreground", PRETO);
                UIManager.put("ScrollPane.background", BRANCO);
                UIManager.put("ComboBox.background", BRANCO);
                UIManager.put("ComboBox.foreground", PRETO);
            }
        } catch (Exception e) {
            System.err.println("Erro ao aplicar tema: " + e.getMessage());
        }
    }
    
    /**
     * Inicializa o tema padrão do sistema
     * Deve ser chamado no início da aplicação
     */
    public static void inicializar() {
        // Definir tema inicial (claro por padrão)
        temaEscuro = false;
        
        // Aplicar as correções de tema primeiro
        CorrecaoTema.aplicarCorrecoes();
        
        // Aplicar o tema ao sistema
        aplicarTema();
        
        // Configurar look and feel
        try {
            // Usar o look and feel do sistema operacional
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Aplicar correções específicas após definir o Look and Feel
            CorrecaoTema.aplicarCorrecoes();
            
            // Customizações adicionais para o tema
            if (temaEscuro) {
                // Customizações para tema escuro
                UIManager.put("TabbedPane.selected", AZUL_ESCURO);
                UIManager.put("TabbedPane.background", CINZA_ESCURO);
                UIManager.put("TabbedPane.foreground", BRANCO);
                UIManager.put("TabbedPane.focus", AZUL_ESCURO);
                UIManager.put("TabbedPane.highlight", AZUL_ESCURO);
                UIManager.put("TabbedPane.light", CINZA_ESCURO);
                UIManager.put("TabbedPane.shadow", CINZA_ESCURO);
                UIManager.put("TabbedPane.tabAreaBackground", CINZA_ESCURO);
            } else {
                // Customizações para tema claro
                UIManager.put("TabbedPane.selected", AZUL_CLARO);
                UIManager.put("TabbedPane.background", BRANCO);
                UIManager.put("TabbedPane.foreground", PRETO);
                UIManager.put("TabbedPane.focus", AZUL_ESCURO);
                UIManager.put("TabbedPane.highlight", AZUL_ESCURO);
                UIManager.put("TabbedPane.light", AZUL_CLARO);
                UIManager.put("TabbedPane.shadow", CINZA_CLARO);
                UIManager.put("TabbedPane.tabAreaBackground", BRANCO);
            }
        } catch (Exception e) {
            System.err.println("Erro ao configurar Look and Feel: " + e.getMessage());
        }
    }
    
    /**
     * Retorna a cor de fundo principal do tema atual
     */
    public static Color getCorFundo() {
        return temaEscuro ? CINZA_ESCURO : BRANCO;
    }
    
    /**
     * Retorna a cor do texto principal do tema atual
     */
    public static Color getCorTexto() {
        return temaEscuro ? BRANCO : PRETO;
    }
    
    /**
     * Retorna a cor de fundo secundária do tema atual
     */
    public static Color getCorFundoSecundario() {
        return temaEscuro ? PRETO : CINZA_CLARO;
    }
    
    /**
     * Configura um botão com as cores padrão
     */
    public static void configurarBotao(JButton botao) {
        CorrecaoTema.corrigirBotao(botao, AZUL_ESCURO, BRANCO);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    /**
     * Configura um botão de confirmação (verde)
     */
    public static void configurarBotaoConfirmacao(JButton botao) {
        CorrecaoTema.corrigirBotao(botao, VERDE, BRANCO);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    /**
     * Configura um botão de cancelamento (vermelho)
     */
    public static void configurarBotaoCancelamento(JButton botao) {
        CorrecaoTema.corrigirBotao(botao, VERMELHO, BRANCO);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    /**
     * Configura um botão personalizado com cores específicas
     * Esta é a abordagem mais robusta para solucionar o problema de cores nos botões
     */
    public static void configurarBotaoPersonalizado(JButton botao, Color corFundo, Color corTexto) {
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, corFundo, corTexto);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    /**
     * Configura um painel com as cores do tema
     */
    public static void configurarPainel(JPanel painel) {
        painel.setBackground(getCorFundo());
        painel.setForeground(getCorTexto());
    }
    
    /**
     * Configura um label com as cores do tema
     */
    public static void configurarLabel(JLabel label) {
        label.setForeground(getCorTexto());
        label.setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    /**
     * Configura um campo de texto com as cores do tema
     */
    public static void configurarCampoTexto(JTextField campo) {
        campo.setBackground(getCorFundo());
        campo.setForeground(getCorTexto());
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createLineBorder(AZUL_ESCURO, 1));
    }
    
    /**
     * Configura uma tabela com as cores do tema
     */
    public static void configurarTabela(JTable tabela) {
        tabela.setBackground(getCorFundo());
        tabela.setForeground(getCorTexto());
        tabela.setSelectionBackground(AZUL_CLARO);
        tabela.setSelectionForeground(PRETO);
        tabela.setGridColor(AZUL_ESCURO);
        tabela.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Configurar cabeçalho
        if (tabela.getTableHeader() != null) {
            tabela.getTableHeader().setBackground(AZUL_ESCURO);
            tabela.getTableHeader().setForeground(BRANCO);
            tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        }
    }
    
    /**
     * Configura um ComboBox com as cores do tema
     */
    public static void configurarComboBox(JComboBox<?> combo) {
        combo.setBackground(getCorFundo());
        combo.setForeground(getCorTexto());
        combo.setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    /**
     * Configura a aparência de um JTabbedPane conforme o tema atual
     */
    public static void configurarTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setBackground(getCorFundo());
        tabbedPane.setForeground(getCorTexto());
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        if (temaEscuro) {
            tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
                @Override
                protected void installDefaults() {
                    super.installDefaults();
                    lightHighlight = CINZA_ESCURO;
                    shadow = CINZA_ESCURO;
                    darkShadow = CINZA_ESCURO;
                    focus = AZUL_ESCURO;
                }
                
                @Override
                protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, 
                                           int x, int y, int w, int h, boolean isSelected) {
                    g.setColor(isSelected ? AZUL_ESCURO : CINZA_ESCURO);
                    switch (tabPlacement) {
                        case TOP:
                            g.drawLine(x, y, x + w - 1, y);
                            g.drawLine(x, y, x, y + h - 1);
                            g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
                            break;
                    }
                }
                
                @Override
                protected void paintTabBackground(Graphics g, int tabPlacement, 
                                               int tabIndex, int x, int y, int w, int h, 
                                               boolean isSelected) {
                    g.setColor(isSelected ? AZUL_ESCURO : CINZA_ESCURO);
                    g.fillRect(x, y, w, h);
                }
                
                @Override
                protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                    int width = tabPane.getWidth();
                    int height = tabPane.getHeight();
                    
                    g.setColor(AZUL_ESCURO);
                    g.drawLine(0, 0, width - 1, 0);
                    g.drawLine(0, 0, 0, height - 1);
                    g.drawLine(width - 1, 0, width - 1, height - 1);
                    g.drawLine(0, height - 1, width - 1, height - 1);
                }
            });
        } else {
            tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
                @Override
                protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, 
                                           int x, int y, int w, int h, boolean isSelected) {
                    g.setColor(isSelected ? AZUL_ESCURO : CINZA_CLARO);
                    switch (tabPlacement) {
                        case TOP:
                            g.drawLine(x, y, x + w - 1, y);
                            g.drawLine(x, y, x, y + h - 1);
                            g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
                            break;
                    }
                }
                
                @Override
                protected void paintTabBackground(Graphics g, int tabPlacement, 
                                               int tabIndex, int x, int y, int w, int h, 
                                               boolean isSelected) {
                    g.setColor(isSelected ? AZUL_CLARO : CINZA_CLARO);
                    g.fillRect(x, y, w, h);
                }
                
                @Override
                protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                    int width = tabPane.getWidth();
                    int height = tabPane.getHeight();
                    
                    g.setColor(AZUL_ESCURO);
                    g.drawLine(0, 0, width - 1, 0);
                    g.drawLine(0, 0, 0, height - 1);
                    g.drawLine(width - 1, 0, width - 1, height - 1);
                    g.drawLine(0, height - 1, width - 1, height - 1);
                }
            });
        }
    }
    
    /**
     * Aplica o tema a um componente e seus filhos recursivamente
     */
    public static void aplicarTemaRecursivo(Container container) {
        // Primeiro aplicar a correção nos botões
        CorrecaoTema.corrigirBotoesRecursivamente(container);
        
        // Continuar com as configurações normais
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                // Os botões já foram configurados pela correção acima
            } else if (component instanceof JPanel) {
                configurarPainel((JPanel) component);
            } else if (component instanceof JLabel) {
                configurarLabel((JLabel) component);
            } else if (component instanceof JTextField) {
                configurarCampoTexto((JTextField) component);
            } else if (component instanceof JTable) {
                configurarTabela((JTable) component);
            } else if (component instanceof JComboBox) {
                configurarComboBox((JComboBox<?>) component);
            } else if (component instanceof JTabbedPane) {
                configurarTabbedPane((JTabbedPane) component);
            }
            
            // Aplicar recursivamente se for um container
            if (component instanceof Container) {
                aplicarTemaRecursivo((Container) component);
            }
        }
    }
    
    /**
     * Cria uma borda padrão para painéis
     */
    public static javax.swing.border.Border criarBordaPadrao() {
        return BorderFactory.createLineBorder(AZUL_ESCURO, 1);
    }
    
    /**
     * Cria uma borda com título
     */
    public static javax.swing.border.Border criarBordaTitulo(String titulo) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AZUL_ESCURO, 1),
            titulo,
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            getCorTexto()
        );
    }
    
    /**
     * Configura o tema para diálogos
     */
    public static void configurarDialog(JDialog dialog) {
        dialog.getContentPane().setBackground(getCorFundo());
        aplicarTemaRecursivo(dialog);
    }
}
