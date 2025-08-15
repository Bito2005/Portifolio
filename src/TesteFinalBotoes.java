import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import util.CorrecaoTema;
import javax.swing.border.Border;

/**
 * Teste final da solução para o problema de cores de botões no Nimbus Look and Feel
 * Esta classe demonstra a solução completa e eficaz para o problema
 */
public class TesteFinalBotoes {
    
    // Cores de teste (mesmas do projeto)
    private static final Color AZUL_ESCURO = new Color(31, 78, 121);   // #1F4E79
    private static final Color VERDE = new Color(76, 175, 80);         // #4CAF50
    private static final Color VERMELHO = new Color(244, 67, 54);      // #F44336
    
    public static void main(String[] args) {
        try {
            // Definir Nimbus Look and Feel diretamente
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            
            // Aplicar correções específicas para o Nimbus
            CorrecaoTema.aplicarCorrecoesNimbus();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        // Criar janela de teste
        JFrame frame = new JFrame("Solução Final - Botões Nimbus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Painel de botões de teste
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Botão Verde (como botão de confirmação)
        JButton btnVerde = new JButton("Botão Verde (Confirmar)");
        btnVerde.setIcon(new ImageIcon("icons/aluguel.png"));
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnVerde, VERDE, Color.WHITE);
        buttonsPanel.add(btnVerde);
        
        // Botão Vermelho (como botão de cancelamento)
        JButton btnVermelho = new JButton("Botão Vermelho (Cancelar)");
        btnVermelho.setIcon(new ImageIcon("icons/sair.png"));
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnVermelho, VERMELHO, Color.WHITE);
        buttonsPanel.add(btnVermelho);
        
        // Botão Azul (como botão da sidebar)
        JButton btnAzul = new JButton("Botão Azul (Sidebar)");
        btnAzul.setIcon(new ImageIcon("icons/cliente.png"));
        btnAzul.setHorizontalAlignment(SwingConstants.LEFT);
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnAzul, AZUL_ESCURO, Color.WHITE);
        buttonsPanel.add(btnAzul);
        
        // Botão com estado hover
        JButton btnHover = new JButton("Hover (Passar Mouse)");
        CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnHover, AZUL_ESCURO, Color.WHITE);
        btnHover.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnHover, Color.CYAN, Color.BLACK);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                CorrecaoTema.aplicarCorrecaoExtremaNimbus(btnHover, AZUL_ESCURO, Color.WHITE);
            }
        });
        buttonsPanel.add(btnHover);
        
        // Adicionar painel de botões ao painel principal
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Painel de instruções
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        JTextArea instructions = new JTextArea(
            "Teste Final - Solução para o problema de cores em botões com Nimbus Look and Feel\n\n" +
            "Esta tela demonstra a solução final implementada através da classe CorrecaoTema.\n" +
            "A abordagem usa um ButtonUI customizado que desenha completamente o botão,\n" +
            "garantindo que a cor de fundo seja exibida corretamente em qualquer Look and Feel.\n\n" +
            "Observe que os botões mantêm suas cores e estão funcionando corretamente com o Nimbus."
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(240, 240, 240));
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        instructionsPanel.add(instructions, BorderLayout.CENTER);
        mainPanel.add(instructionsPanel, BorderLayout.NORTH);
        
        // Adicionar painel principal à janela
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
