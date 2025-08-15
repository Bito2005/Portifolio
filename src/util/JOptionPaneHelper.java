package util;

import javax.swing.*;
import java.awt.*;

/**
 * Utilitário para configurar diálogos JOptionPane com o estilo do sistema
 */
public class JOptionPaneHelper {
    
    /**
     * Configura um JOptionPane para usar o estilo padrão do sistema
     * @param optionPane O JOptionPane a ser configurado
     */
    public static void configurarEstilo(JOptionPane optionPane) {
        // Aplicar cores de fundo
        optionPane.setBackground(Tema.BRANCO);
        configurarComponentesRecursivamente(optionPane);
        
        // Configurar botões
        Component[] componentes = optionPane.getComponents();
        for (Component componente : componentes) {
            if (componente instanceof JPanel) {
                Component[] subComponentes = ((JPanel)componente).getComponents();
                for (Component subComp : subComponentes) {
                    if (subComp instanceof JButton) {
                        JButton botao = (JButton) subComp;
                        
                        // Aplicar estilo personalizado
                        String texto = botao.getText();
                        if (texto != null) {
                            if (texto.equals("OK") || texto.equals("Sim")) {
                                CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.AZUL_ESCURO, Color.WHITE);
                            } else if (texto.equals("Não") || texto.equals("Cancelar")) {
                                CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.VERMELHO, Color.WHITE);
                            } else {
                                CorrecaoTema.aplicarCorrecaoExtremaNimbus(botao, Tema.AZUL_ESCURO, Color.WHITE);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Substitui o diálogo padrão de mensagem por um personalizado
     * @param parentComponent Componente pai
     * @param message Mensagem a ser exibida
     * @param title Título da janela
     * @param messageType Tipo da mensagem (JOptionPane constants)
     */
    public static void showMessageDialog(Component parentComponent, Object message, 
                                        String title, int messageType) {
        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        
        configurarEstilo(optionPane);
        configurarBotaoOK(optionPane);
        
        dialog.setVisible(true);
    }
    
    /**
     * Exibe um diálogo de confirmação estilizado
     * @param parentComponent Componente pai
     * @param message Mensagem a ser exibida
     * @param title Título da janela
     * @param optionType Tipo de opção (YES_NO_OPTION, etc.)
     * @param messageType Tipo da mensagem (QUESTION_MESSAGE, etc.)
     * @return Opção selecionada pelo usuário (YES_OPTION, NO_OPTION, etc.)
     */
    public static int showConfirmDialog(Component parentComponent, Object message,
                                     String title, int optionType, int messageType) {
        JOptionPane optionPane = new JOptionPane(message, messageType, optionType);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        
        configurarEstilo(optionPane);
        
        dialog.setVisible(true);
        Object selectedValue = optionPane.getValue();
        
        if (selectedValue == null)
            return JOptionPane.CLOSED_OPTION;
        
        // Se o valor retornado for um Integer, é o código da opção
        if (selectedValue instanceof Integer)
            return ((Integer) selectedValue).intValue();
        
        return JOptionPane.CLOSED_OPTION;
    }
    
    /**
     * Configura os componentes do diálogo recursivamente
     */
    private static void configurarComponentesRecursivamente(Container container) {
        container.setBackground(Tema.BRANCO);
        
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                panel.setBackground(Tema.BRANCO);
                configurarComponentesRecursivamente(panel);
            }
            else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                label.setForeground(Color.BLACK);
            }
        }
    }
    
    /**
     * Configura o botão OK do diálogo
     */
    private static void configurarBotaoOK(JOptionPane optionPane) {
        // Encontrar e configurar botão OK
        for (Component comp : optionPane.getComponents()) {
            if (comp instanceof Container) {
                Container container = (Container) comp;
                for (Component button : container.getComponents()) {
                    if (button instanceof JButton) {
                        JButton btn = (JButton) button;
                        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btn, Tema.AZUL_ESCURO, Color.WHITE);
                        btn.setPreferredSize(new Dimension(80, 30));
                        break;
                    }
                }
            }
        }
    }
}
