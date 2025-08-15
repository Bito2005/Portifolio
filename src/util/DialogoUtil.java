package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Utilitário para padronizar e corrigir os diálogos do sistema
 */
public class DialogoUtil {

    /**
     * Configura os diálogos do sistema para usar o estilo personalizado
     * Deve ser chamado na inicialização do sistema
     */
    public static void inicializar() {
        // Substituir o UIManager padrão para personalizar JOptionPane
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        
        // Personalizar botões
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        
        // Substituir renderização dos botões de JOptionPane
        UIManager.put("OptionPane.buttonUI", "javax.swing.plaf.basic.BasicButtonUI");
        
        // Remover bordas padrão
        UIManager.put("OptionPane.border", BorderFactory.createEmptyBorder(10, 10, 10, 10));
        UIManager.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(10, 0, 0, 0));
    }

    /**
     * Mostra uma mensagem de aviso estilizada
     */
    public static void mostrarAviso(Component parentComponent, String mensagem, String titulo) {
        mostrarDialogoEstilizado(parentComponent, mensagem, titulo, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Mostra uma mensagem de erro estilizada
     */
    public static void mostrarErro(Component parentComponent, String mensagem, String titulo) {
        mostrarDialogoEstilizado(parentComponent, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mostra uma mensagem de informação estilizada
     */
    public static void mostrarInformacao(Component parentComponent, String mensagem, String titulo) {
        mostrarDialogoEstilizado(parentComponent, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra um diálogo de confirmação estilizado
     * @return true se confirmado, false caso contrário
     */
    public static boolean confirmar(Component parentComponent, String mensagem, String titulo) {
        JOptionPane optionPane = criarOptionPaneEstilizado(mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
        
        // Personalizar os botões
        JButton btnSim = new JButton("Sim");
        JButton btnNao = new JButton("Não");
        
        // Aplicar estilo aos botões
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnSim, Tema.VERDE, Color.WHITE);
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnNao, Tema.VERMELHO, Color.WHITE);
        
        // Configurar dimensões
        Dimension tamanhoBotao = new Dimension(80, 30);
        btnSim.setPreferredSize(tamanhoBotao);
        btnNao.setPreferredSize(tamanhoBotao);
        
        // Adicionar atalhos de teclado
        btnSim.setMnemonic(KeyEvent.VK_S);
        btnNao.setMnemonic(KeyEvent.VK_N);
        
        optionPane.setOptions(new Object[] { btnSim, btnNao });
        
        // Criar diálogo
        JDialog dialog = criarDialog(parentComponent, optionPane, titulo);
        
        // Ação para o botão Sim
        btnSim.addActionListener((ActionEvent e) -> {
            optionPane.setValue(0); // 0 = Sim
            dialog.dispose();
        });
        
        // Ação para o botão Não
        btnNao.addActionListener((ActionEvent e) -> {
            optionPane.setValue(1); // 1 = Não
            dialog.dispose();
        });
        
        // Exibir diálogo e aguardar resposta
        dialog.setVisible(true);
        
        // Retornar resultado (true para Sim, false para Não ou fechou)
        Object valor = optionPane.getValue();
        return valor instanceof Integer && (Integer)valor == 0;
    }

    /**
     * Mostra um diálogo estilizado com um componente personalizado
     */
    public static void mostrarComponente(Component parentComponent, Component conteudo, String titulo) {
        JOptionPane optionPane = new JOptionPane(
            conteudo,
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            null,
            null
        );
        
        JButton btnOK = new JButton("OK");
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnOK, Tema.AZUL_ESCURO, Color.WHITE);
        btnOK.setPreferredSize(new Dimension(80, 30));
        
        optionPane.setOptions(new Object[] { btnOK });
        
        JDialog dialog = criarDialog(parentComponent, optionPane, titulo);
        
        btnOK.addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });
        
        dialog.setVisible(true);
    }

    /**
     * Método privado para criar e exibir diálogos estilizados
     */
    private static void mostrarDialogoEstilizado(Component parentComponent, String mensagem, String titulo, int messageType) {
        JOptionPane optionPane = criarOptionPaneEstilizado(mensagem, titulo, messageType);
        
        // Personalizar botão OK
        JButton btnOK = new JButton("OK");
        
        // Aplicar estilo personalizado ao botão
        Color corBotao;
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                corBotao = Tema.VERMELHO;
                break;
            case JOptionPane.WARNING_MESSAGE:
                corBotao = Tema.LARANJA;
                break;
            case JOptionPane.INFORMATION_MESSAGE:
            default:
                corBotao = Tema.AZUL_ESCURO;
                break;
        }
        
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnOK, corBotao, Color.WHITE);
        btnOK.setPreferredSize(new Dimension(80, 30));
        
        optionPane.setOptions(new Object[] { btnOK });
        
        // Criar e exibir diálogo
        JDialog dialog = criarDialog(parentComponent, optionPane, titulo);
        
        btnOK.addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });
        
        dialog.setVisible(true);
    }

    /**
     * Cria um JOptionPane estilizado
     */
    private static JOptionPane criarOptionPaneEstilizado(String mensagem, String titulo, int messageType) {
        // Usar uma fonte maior e mais visível para a mensagem
        JLabel labelMensagem = new JLabel(mensagem);
        labelMensagem.setFont(new Font("Arial", Font.PLAIN, 14));
        
        return new JOptionPane(
            labelMensagem, 
            messageType,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[0],
            null
        );
    }

    /**
     * Cria um JDialog personalizado para o JOptionPane
     */
    private static JDialog criarDialog(Component parentComponent, JOptionPane optionPane, String titulo) {
        JDialog dialog = optionPane.createDialog(parentComponent, titulo);
        dialog.setModal(true);
        dialog.setResizable(false);
        
        // Configurar tamanho mínimo
        dialog.pack();
        Dimension tamanho = dialog.getSize();
        dialog.setMinimumSize(new Dimension(
            Math.max(300, tamanho.width),
            Math.max(150, tamanho.height)
        ));
        
        dialog.setLocationRelativeTo(parentComponent);
        return dialog;
    }
}
