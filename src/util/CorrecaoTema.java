package util;

import javax.swing.*;
import java.awt.*;

/**
 * Correção para resolver o problema da cor de fundo dos botões no sistema AutoFácil
 */
public class CorrecaoTema {
    
    /**
     * Configura um botão para garantir que a cor de fundo seja aplicada corretamente
     * Este método resolve problemas de incompatibilidade entre diferentes Look and Feels
     * usando uma abordagem simplificada que sobrescreve o UI do botão.
     */
    public static void corrigirBotao(JButton botao, Color corFundo, Color corTexto) {
        // Configuração básica
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        
        // Para todas as versões do Look and Feel, vamos usar a abordagem extrema que é mais confiável
        aplicarCorrecaoExtremaNimbus(botao, corFundo, corTexto);
        
        // Forçar repaint para garantir que as mudanças sejam aplicadas
        botao.revalidate();
        botao.repaint();
    }
    
    /**
     * Configura o UIManager para garantir que os botões exibam as cores corretamente
     * Deve ser chamado antes de aplicar o tema ao sistema
     */
    public static void configurarUIManager() {
        try {
            // Desabilitar propriedades específicas do Look and Feel que podem afetar as cores
            UIManager.put("Button.background", null);
            UIManager.put("Button.select", null);
            
            // Forçar o uso das cores definidas diretamente nos componentes
            UIManager.put("Button.useUIDefaultBackground", Boolean.FALSE);
            
            // Habilitar rendering personalizado para botões
            UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
            
            // Essas propriedades são específicas para Nimbus Look and Feel
            if (isNimbusLookAndFeel()) {
                UIManager.put("Nimbus.Button.contentMargins", new Insets(6, 14, 6, 14));
                UIManager.put("Nimbus.Button.forceOpaque", Boolean.TRUE);
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao configurar UIManager: " + e.getMessage());
        }
    }
    
    /**
     * Aplica a correção de cor a todos os botões no sistema
     * Percorre recursivamente todos os componentes em um container
     */
    public static void corrigirBotoesRecursivamente(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton botao = (JButton) component;
                Color corFundo = botao.getBackground();
                Color corTexto = botao.getForeground();
                corrigirBotao(botao, corFundo, corTexto);
            }
            
            if (component instanceof Container) {
                corrigirBotoesRecursivamente((Container) component);
            }
        }
    }
    
    /**
     * Verifica se o Look and Feel atual é o Nimbus
     */
    public static boolean isNimbusLookAndFeel() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        return laf != null && laf.getName().contains("Nimbus");
    }
    
    /**
     * Verifica se o Look and Feel atual é o Windows
     */
    public static boolean isWindowsLookAndFeel() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        return laf != null && laf.getName().contains("Windows");
    }
    
    /**
     * Verifica se o Look and Feel atual é o Metal
     */
    public static boolean isMetalLookAndFeel() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        return laf != null && laf.getName().contains("Metal");
    }
    
    /**
     * Aplica a correção específica para o Look and Feel atual
     */
    public static void aplicarCorrecoes() {
        configurarUIManager();
        
        // Configurações específicas para cada Look and Feel
        if (isNimbusLookAndFeel()) {
            aplicarCorrecoesNimbus();
        } else if (isWindowsLookAndFeel()) {
            aplicarCorrecoesWindows();
        } else if (isMetalLookAndFeel()) {
            aplicarCorrecoesMetal();
        }
    }
    
    public static void aplicarCorrecoesNimbus() {
        try {
            // Configurações básicas do Nimbus
            UIManager.put("Button.contentMargins", new Insets(6, 14, 6, 14));
            UIManager.put("Button.forceOpaque", Boolean.TRUE);
            UIManager.put("Button.gradient", null);
            
            // Desabilitar recursos específicos do Nimbus que afetam as cores
            UIManager.put("Nimbus.Button.painters", null);
            UIManager.put("Nimbus.Button.contentMargins", new Insets(6, 14, 6, 14));
            
            // Forçar opacidade
            UIManager.put("Button.opaque", true);
            UIManager.put("Button[Enabled].backgroundPainter", null);
            UIManager.put("Button[Disabled].backgroundPainter", null);
            UIManager.put("Button[Focused].backgroundPainter", null);
            UIManager.put("Button[MouseOver].backgroundPainter", null);
            UIManager.put("Button[Pressed].backgroundPainter", null);
            UIManager.put("Button[Default].backgroundPainter", null);
            
            // Substituir ButtonUI padrão por uma versão mais simples
            UIManager.put("ButtonUI", "javax.swing.plaf.basic.BasicButtonUI");
            
            System.out.println("Aplicadas correções específicas para Nimbus Look and Feel");
        } catch (Exception e) {
            System.err.println("Erro ao aplicar correções para Nimbus: " + e.getMessage());
        }
    }
    
    private static void aplicarCorrecoesWindows() {
        try {
            // Desativar efeitos visuais do Windows que podem afetar as cores
            UIManager.put("Button.background", null);
            UIManager.put("Button.rollover", Boolean.FALSE);
            UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
        } catch (Exception e) {
            System.err.println("Erro ao aplicar correções para Windows: " + e.getMessage());
        }
    }
    
    private static void aplicarCorrecoesMetal() {
        try {
            // O Metal geralmente aplica cores corretamente, mas vamos garantir
            UIManager.put("Button.select", null);
            UIManager.put("Button.gradient", null);
        } catch (Exception e) {
            System.err.println("Erro ao aplicar correções para Metal: " + e.getMessage());
        }
    }
    
    /**
     * Cria uma classe de UI personalizada para botões que força o uso da cor de fundo
     */
    public static void instalarButtonUI() {
        try {
            // Esta é uma técnica avançada que substitui o ButtonUI padrão
            // Não é sempre necessária, mas pode ajudar em casos extremos
            UIManager.put("ButtonUI", "javax.swing.plaf.basic.BasicButtonUI");
        } catch (Exception e) {
            System.err.println("Erro ao instalar ButtonUI personalizado: " + e.getMessage());
        }
    }
    
    /**
     * Aplica uma solução extrema para o problema dos botões no Nimbus Look and Feel
     * Essa solução cria um botão com aparência totalmente personalizada
     */
    public static void aplicarCorrecaoExtremaNimbus(JButton botao, Color corFundo, Color corTexto) {
        // Substituir completamente o UI do botão
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                // Salvar configurações originais
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Definir antialiasing para texto e formas
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Desenhar fundo com cantos arredondados
                g2d.setColor(corFundo);
                g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
                
                // Desenhar texto centralizado
                FontMetrics fm = g2d.getFontMetrics();
                String text = botao.getText();
                
                // Se tiver ícone, ajustar posição do texto
                int textX;
                if (botao.getIcon() != null) {
                    int iconWidth = botao.getIcon().getIconWidth();
                    textX = (c.getWidth() - fm.stringWidth(text) - iconWidth - 5) / 2 + iconWidth + 5;
                } else {
                    textX = (c.getWidth() - fm.stringWidth(text)) / 2;
                }
                
                int textY = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                g2d.setColor(corTexto);
                g2d.drawString(text, textX, textY);
                
                // Desenhar ícone se existir
                if (botao.getIcon() != null) {
                    int iconWidth = botao.getIcon().getIconWidth();
                    int iconHeight = botao.getIcon().getIconHeight();
                    int iconX;
                    
                    if (botao.getHorizontalAlignment() == SwingConstants.LEFT) {
                        iconX = 5; // Para botões alinhados à esquerda
                    } else {
                        iconX = textX - iconWidth - 5; // Para botões centralizados
                        if (iconX < 5) iconX = 5;
                    }
                    
                    int iconY = (c.getHeight() - iconHeight) / 2;
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
        botao.setBackground(corFundo); // para listeners que verificam a cor
        botao.setForeground(corTexto);
    }
}
