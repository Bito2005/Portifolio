import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Aplicativo de teste para identificar o problema de cores de botões
 * Este teste verifica como diferentes LookAndFeel afetam as cores dos botões
 */
public class TesteBotoes {
    
    // Cores do teste (mesmas do projeto)
    private static final Color AZUL_ESCURO = new Color(31, 78, 121);   // #1F4E79
    private static final Color VERDE = new Color(76, 175, 80);         // #4CAF50
    private static final Color VERMELHO = new Color(244, 67, 54);      // #F44336
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        // Criar janela de teste
        JFrame frame = new JFrame("Teste de Cores de Botões");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        // Criar painel principal com GridLayout
        JPanel painelPrincipal = new JPanel(new GridLayout(3, 1, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel para botões com LookAndFeel padrão
        JPanel painelPadrao = criarPainelComLAF("LookAndFeel Padrão", null);
        painelPrincipal.add(painelPadrao);
        
        // Painel para botões com Metal LookAndFeel
        JPanel painelMetal = criarPainelComLAF("Metal LookAndFeel", new MetalLookAndFeel());
        painelPrincipal.add(painelMetal);
        
        // Painel para botões com Nimbus LookAndFeel
        JPanel painelNimbus = criarPainelComLAF("Nimbus LookAndFeel", new NimbusLookAndFeel());
        painelPrincipal.add(painelNimbus);
        
        frame.add(painelPrincipal);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static JPanel criarPainelComLAF(String titulo, LookAndFeel laf) {
        // Tentar aplicar o LookAndFeel a este painel
        if (laf != null) {
            try {
                UIManager.setLookAndFeel(laf);
            } catch (UnsupportedLookAndFeelException e) {
                System.err.println("LookAndFeel não suportado: " + e.getMessage());
            }
        }
        
        // Criar painel com título
        JPanel painel = new JPanel(new GridLayout(3, 3, 10, 10));
        painel.setBorder(BorderFactory.createTitledBorder(titulo));
        
        // Adicionar botões padrão, opaque e client properties
        adicionarGrupoBotoes(painel, "Padrão", false, false);
        adicionarGrupoBotoes(painel, "Opaque", true, false);
        adicionarGrupoBotoes(painel, "Client Property", true, true);
        
        return painel;
    }
    
    private static void adicionarGrupoBotoes(JPanel painel, String tipo, boolean opaque, boolean clientProperty) {
        // Criar botão azul
        JButton botaoAzul = new JButton("Botão Azul (" + tipo + ")");
        configurarBotao(botaoAzul, AZUL_ESCURO, Color.WHITE, opaque, clientProperty);
        painel.add(botaoAzul);
        
        // Criar botão verde
        JButton botaoVerde = new JButton("Botão Verde (" + tipo + ")");
        configurarBotao(botaoVerde, VERDE, Color.WHITE, opaque, clientProperty);
        painel.add(botaoVerde);
        
        // Criar botão vermelho
        JButton botaoVermelho = new JButton("Botão Vermelho (" + tipo + ")");
        configurarBotao(botaoVermelho, VERMELHO, Color.WHITE, opaque, clientProperty);
        painel.add(botaoVermelho);
    }
    
    private static void configurarBotao(JButton botao, Color corFundo, Color corTexto, boolean opaque, boolean clientProperty) {
        // Configuração básica
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        
        // Opaque (controla se o botão usa a cor de fundo)
        botao.setOpaque(opaque);
        
        // BorderPainted (controla se a borda é desenhada)
        botao.setBorderPainted(false);
        
        // Cliente property (específico para Nimbus LookAndFeel)
        if (clientProperty) {
            botao.putClientProperty("Nimbus.Overrides", new javax.swing.plaf.ColorUIResource(corFundo));
            botao.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
        }
    }
}
