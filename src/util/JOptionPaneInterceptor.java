package util;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;

/**
 * Interceptor para JOptionPane que aplica automaticamente o estilo do sistema
 * para todos os diálogos criados através do JOptionPane.
 */
public class JOptionPaneInterceptor {
    
    // Flag para controlar se o interceptor já foi instalado
    private static boolean instalado = false;
    
    /**
     * Instala o interceptor JOptionPane para estilizar automaticamente todos os diálogos
     * Deve ser chamado na inicialização da aplicação
     */
    public static void instalar() {
        if (instalado) {
            return; // Evitar instalação duplicada
        }
        
        try {
            // Substituir o UI do OptionPane
            UIManager.put("OptionPaneUI", EstiloOptionPaneUI.class.getName());
            
            // Configurar botões padrão
            configurarBotoesOptionPane();
            
            instalado = true;
            System.out.println("JOptionPaneInterceptor instalado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("Erro ao instalar JOptionPaneInterceptor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Configura o estilo dos botões padrão do JOptionPane
     */
    private static void configurarBotoesOptionPane() {
        // Configurar textos de botões
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "OK");
        
        // Configurar cores e fontes
        UIManager.put("OptionPane.background", Tema.BRANCO);
        UIManager.put("OptionPane.foreground", Tema.PRETO);
        UIManager.put("Panel.background", Tema.BRANCO);
        UIManager.put("OptionPane.messageForeground", Tema.PRETO);
        
        // Configurar bordas e espaçamentos
        UIManager.put("OptionPane.messageAreaBorder", BorderFactory.createEmptyBorder(15, 15, 15, 15));
        UIManager.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Configurar fontes
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
        
        // Configurar tamanhos
        UIManager.put("OptionPane.minimumSize", new Dimension(300, 150));
        
        // Configurar botões
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
        UIManager.put("Button.showMnemonics", Boolean.TRUE);
    }
    
    /**
     * UI personalizado para JOptionPane que aplica o estilo do sistema
     */
    public static class EstiloOptionPaneUI extends BasicOptionPaneUI {
        
        public static ComponentUI createUI(JComponent c) {
            return new EstiloOptionPaneUI();
        }
        
        @Override
        protected void installComponents() {
            super.installComponents();
            
            // Aplicar estilos após os componentes serem instalados
            SwingUtilities.invokeLater(() -> {
                estilizarBotoes(optionPane);
            });
        }
        
        /**
         * Estiliza os botões no JOptionPane
         */
        private void estilizarBotoes(JOptionPane optionPane) {
            Component[] componentes = optionPane.getComponents();
            for (Component c : componentes) {
                if (c instanceof Container) {
                    estilizarComponentes((Container)c);
                }
            }
        }
        
        /**
         * Aplica estilo recursivamente a todos os componentes do container
         */
        private void estilizarComponentes(Container container) {
            for (Component c : container.getComponents()) {
                if (c instanceof JButton) {
                    JButton botao = (JButton)c;
                    String texto = botao.getText();
                    
                    if (texto != null) {
                        // Configuração padrão de dimensão para todos os botões
                        Dimension tamanhoBotao = new Dimension(100, 35);
                        botao.setFont(new Font("Arial", Font.BOLD, 12));
                        
                        if (texto.equals(UIManager.getString("OptionPane.okButtonText"))) {
                            // Botão OK em azul
                            CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.AZUL_ESCURO, Color.WHITE);
                            botao.setPreferredSize(tamanhoBotao);
                        } 
                        else if (texto.equals(UIManager.getString("OptionPane.yesButtonText"))) {
                            // Botão Sim em verde
                            CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.VERDE, Color.WHITE);
                            botao.setPreferredSize(tamanhoBotao);
                        } 
                        else if (texto.equals(UIManager.getString("OptionPane.noButtonText"))) {
                            // Botão Não em vermelho
                            CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.VERMELHO, Color.WHITE);
                            botao.setPreferredSize(tamanhoBotao);
                        }
                        else if (texto.equals(UIManager.getString("OptionPane.cancelButtonText"))) {
                            // Botão Cancelar em azul claro
                            CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.AZUL_CLARO, Color.WHITE);
                            botao.setPreferredSize(tamanhoBotao);
                        }
                        else {
                            // Outros botões em azul escuro
                            CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.AZUL_ESCURO, Color.WHITE);
                            botao.setPreferredSize(tamanhoBotao);
                        }
                        
                        // Garantir que o botão seja bem visível
                        botao.setFocusPainted(false);
                        botao.setBorderPainted(false);
                        botao.setOpaque(true);
                        botao.setContentAreaFilled(true);
                    }
                }
                else if (c instanceof Container) {
                    estilizarComponentes((Container)c);
                }
            }
        }
    }
}
