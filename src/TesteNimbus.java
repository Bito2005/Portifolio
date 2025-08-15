import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Teste específico para botões no Nimbus Look and Feel
 * Esta classe demonstra várias abordagens para aplicar cores de fundo em botões
 */
public class TesteNimbus {
    
    // Cores do teste (mesmas do projeto)
    private static final Color AZUL_ESCURO = new Color(31, 78, 121);   // #1F4E79
    private static final Color VERDE = new Color(76, 175, 80);         // #4CAF50
    private static final Color VERMELHO = new Color(244, 67, 54);      // #F44336
    
    public static void main(String[] args) {
        try {
            // Definir Nimbus Look and Feel diretamente
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            
            // Configurações globais para o Nimbus
            UIManager.put("Button.contentMargins", new Insets(6, 14, 6, 14));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        // Criar janela de teste
        JFrame frame = new JFrame("Teste de Botões com Nimbus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Painel com diferentes tipos de botões
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 3, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 1. Botão com setBackground básico
        JButton btnBasic = new JButton("Botão Básico");
        btnBasic.setBackground(AZUL_ESCURO);
        btnBasic.setForeground(Color.WHITE);
        buttonsPanel.add(btnBasic);
        
        // 2. Botão com setOpaque
        JButton btnOpaque = new JButton("Botão Opaque");
        btnOpaque.setBackground(AZUL_ESCURO);
        btnOpaque.setForeground(Color.WHITE);
        btnOpaque.setOpaque(true);
        buttonsPanel.add(btnOpaque);
        
        // 3. Botão sem borda
        JButton btnNoBorder = new JButton("Sem Borda");
        btnNoBorder.setBackground(AZUL_ESCURO);
        btnNoBorder.setForeground(Color.WHITE);
        btnNoBorder.setOpaque(true);
        btnNoBorder.setBorderPainted(false);
        buttonsPanel.add(btnNoBorder);
        
        // 4. Botão com propriedades Nimbus
        JButton btnNimbus = new JButton("Propriedades Nimbus");
        btnNimbus.setBackground(VERDE);
        btnNimbus.setForeground(Color.WHITE);
        btnNimbus.setOpaque(true);
        btnNimbus.setBorderPainted(false);
        btnNimbus.putClientProperty("Nimbus.Overrides", createColorUIResource(VERDE));
        btnNimbus.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
        buttonsPanel.add(btnNimbus);
        
        // 5. Botão com painter customizado
        JButton btnCustomPainter = new JButton("Custom Painter");
        btnCustomPainter.setBackground(VERMELHO);
        btnCustomPainter.setForeground(Color.WHITE);
        btnCustomPainter.setOpaque(true);
        btnCustomPainter.setBorderPainted(false);
        
        UIDefaults defaults = new UIDefaults();
        defaults.put("Button.background", VERMELHO);
        defaults.put("Button[Enabled].backgroundPainter", createCustomPainter(VERMELHO));
        defaults.put("Button[MouseOver].backgroundPainter", createCustomPainter(VERMELHO.brighter()));
        defaults.put("Button[Pressed].backgroundPainter", createCustomPainter(VERMELHO.darker()));
        btnCustomPainter.putClientProperty("Nimbus.Overrides", defaults);
        
        buttonsPanel.add(btnCustomPainter);
        
        // 6. Botão com gradiente 
        JButton btnGradient = new JButton("Gradiente");
        buttonsPanel.add(btnGradient);
        btnGradient.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                int w = c.getWidth();
                int h = c.getHeight();
                
                // Desenhar gradiente
                GradientPaint gp = new GradientPaint(
                    0, 0, AZUL_ESCURO,
                    0, h, AZUL_ESCURO.darker()
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                
                g2d.dispose();
                super.paint(g, c);
            }
        });
        btnGradient.setForeground(Color.WHITE);
        
        // 7. Botão com borda de cor
        JButton btnColorBorder = new JButton("Borda Colorida");
        btnColorBorder.setBackground(AZUL_ESCURO);
        btnColorBorder.setForeground(Color.WHITE);
        btnColorBorder.setBorder(BorderFactory.createLineBorder(AZUL_ESCURO, 4));
        buttonsPanel.add(btnColorBorder);
        
        // 8. Abordagem combinada
        JButton btnCombined = new JButton("Abordagem Combinada");
        configurarBotaoCombinado(btnCombined, VERDE);
        buttonsPanel.add(btnCombined);
        
        // 9. JLabel como botão
        JLabel lblAsButton = new JLabel("JLabel como Botão", SwingConstants.CENTER);
        lblAsButton.setOpaque(true);
        lblAsButton.setBackground(AZUL_ESCURO);
        lblAsButton.setForeground(Color.WHITE);
        lblAsButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonsPanel.add(lblAsButton);
        
        // 10. Botão com cor de fundo forte
        JButton btnStrongColor = new JButton("Cor Forte");
        btnStrongColor.setBackground(Color.RED);
        btnStrongColor.setForeground(Color.WHITE);
        btnStrongColor.setOpaque(true);
        btnStrongColor.setBorderPainted(false);
        btnStrongColor.setContentAreaFilled(true);
        buttonsPanel.add(btnStrongColor);
        
        // 11. Outro teste com JPanel em vez de botão
        JPanel panelButton = new JPanel(new BorderLayout());
        panelButton.setBackground(AZUL_ESCURO);
        JLabel labelInPanel = new JLabel("JPanel como Botão", SwingConstants.CENTER);
        labelInPanel.setForeground(Color.WHITE);
        panelButton.add(labelInPanel, BorderLayout.CENTER);
        buttonsPanel.add(panelButton);
        
        // 12. JButton com look and feel sobrescrito
        JButton btnOverrideLAF = new JButton("Sobrescrever LAF");
        btnOverrideLAF.setUI(new javax.swing.plaf.metal.MetalButtonUI());
        btnOverrideLAF.setBackground(VERMELHO);
        btnOverrideLAF.setForeground(Color.WHITE);
        btnOverrideLAF.setOpaque(true);
        buttonsPanel.add(btnOverrideLAF);
        
        // Adicionar o painel de botões ao painel principal
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Adicionar painel de instruções
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        JTextArea instructions = new JTextArea(
            "Teste de diferentes abordagens para aplicar cores de fundo em botões com Nimbus Look and Feel.\n" +
            "Compare os resultados e veja qual abordagem funciona melhor.\n" +
            "A abordagem combinada (#8) é a mais completa e deve funcionar em todos os casos."
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(240, 240, 240));
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        instructionsPanel.add(instructions, BorderLayout.CENTER);
        mainPanel.add(instructionsPanel, BorderLayout.NORTH);
        
        // Configurar a janela
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Abordagem combinada - usa múltiplas técnicas para garantir que a cor seja aplicada
     */
    private static void configurarBotaoCombinado(JButton botao, Color corFundo) {
        // Configuração básica
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setFocusPainted(false);
        
        // Remover borda padrão e adicionar borda com a cor de fundo
        botao.setBorderPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(corFundo, 2));
        
        // Propriedades específicas para Nimbus
        UIDefaults defaults = new UIDefaults();
        defaults.put("Button.background", corFundo);
        defaults.put("Button[Enabled].backgroundPainter", createCustomPainter(corFundo));
        defaults.put("Button[MouseOver].backgroundPainter", createCustomPainter(corFundo.brighter()));
        defaults.put("Button[Pressed].backgroundPainter", createCustomPainter(corFundo.darker()));
        botao.putClientProperty("Nimbus.Overrides", defaults);
        botao.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
    }
    
    /**
     * Cria um ColorUIResource a partir de uma cor
     */
    private static Object createColorUIResource(Color color) {
        try {
            // Tentar criar um ColorUIResource diretamente
            Class<?> colorUIResourceClass = Class.forName("javax.swing.plaf.ColorUIResource");
            return colorUIResourceClass.getConstructor(Color.class).newInstance(color);
        } catch (Exception e) {
            // Fallback se não conseguir criar o objeto
            return color;
        }
    }
    
    /**
     * Interface Painter para desenho personalizado
     */
    @FunctionalInterface
    private interface Painter<T> {
        void paint(Graphics2D g, T object, int width, int height);
    }
    
    /**
     * Cria um Painter customizado
     */
    private static Object createCustomPainter(final Color color) {
        return new javax.swing.UIDefaults.LazyValue() {
            @Override
            public Object createValue(UIDefaults table) {
                return new Painter<JComponent>() {
                    @Override
                    public void paint(Graphics2D g, JComponent c, int w, int h) {
                        g.setColor(color);
                        g.fillRect(0, 0, w, h);
                    }
                };
            }
        };
    }
}
