import ui.TelaLogin;
import util.GeradorDadosExemplo;
import util.Tema;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principal do sistema AutoFácil Java Edition
 * Ponto de entrada da aplicação
 */
public class Main {
    
    public static void main(String[] args) {
        // Configurar propriedades do sistema
        configurarSistema();
        
        // Configurar look and feel
        configurarLookAndFeel();
        
        // Gerar dados de exemplo se necessário
        System.out.println("Inicializando AutoFácil Java Edition...");
        GeradorDadosExemplo.gerarDadosSeNecessario();
        
        // Aplicar tema padrão
        Tema.inicializar();
        
        // Inicializar utilitários de diálogos
        util.DialogoUtil.inicializar();
        util.JOptionPaneInterceptor.instalar();
        
        // Iniciar interface gráfica
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Exibir splash screen
                    mostrarSplashScreen();
                    
                    // Iniciar tela de login
                    new TelaLogin().setVisible(true);
                    
                    System.out.println("Sistema iniciado com sucesso!");
                    
                } catch (Exception e) {
                    System.err.println("Erro ao iniciar o sistema: " + e.getMessage());
                    e.printStackTrace();
                    
                    JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar o sistema:\n" + e.getMessage(),
                        "Erro Fatal",
                        JOptionPane.ERROR_MESSAGE);
                    
                    System.exit(1);
                }
            }
        });
    }
    
    /**
     * Configura propriedades do sistema
     */
    private static void configurarSistema() {
        // Configurar propriedades do sistema
        System.setProperty("java.awt.headless", "false");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        System.setProperty("swing.plaf.metal.controlFont", "Arial-12");
        System.setProperty("swing.plaf.metal.userFont", "Arial-12");
        
        // Configurar codificação
        System.setProperty("file.encoding", "UTF-8");
        
        // Configurar fuso horário
        System.setProperty("user.timezone", "America/Sao_Paulo");
        
        // Configurar diretório de trabalho
        System.setProperty("user.dir", System.getProperty("user.dir"));
        
        // Desabilitar som de erro do sistema
        System.setProperty("sun.awt.exception.handler", "");
        
        // Configurar renderização
        System.setProperty("sun.java2d.d3d", "false");
        System.setProperty("sun.java2d.opengl", "false");
        System.setProperty("sun.java2d.xrender", "true");
        
        // Configurar thread para EDT
        System.setProperty("swing.invokeAndWaitFromAnyThread", "true");
    }
    
    /**
     * Configura o Look and Feel do sistema
     */
    private static void configurarLookAndFeel() {
        try {
            // Tentar usar Nimbus primeiro
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    // Configurar propriedades do Nimbus antes de aplicá-lo
                    UIManager.put("Button.contentMargins", new Insets(6, 14, 6, 14));
                    UIManager.put("Button.forceOpaque", Boolean.TRUE);
                    UIManager.put("Button.gradient", null);
                    
                    UIManager.setLookAndFeel(info.getClassName());
                    System.out.println("Look and Feel configurado: Nimbus");
                    
                    // Aplicar correções específicas para o Nimbus
                    util.CorrecaoTema.aplicarCorrecoesNimbus();
                    
                    return;
                }
            }
            
            // Se não encontrar Nimbus, usar padrão cruzado
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            System.out.println("Look and Feel configurado: Metal Default");
            
        } catch (Exception e) {
            System.err.println("Erro ao configurar Look and Feel: " + e.getMessage());
            
            // Fallback para Metal
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                System.out.println("Look and Feel configurado: Metal (fallback)");
            } catch (Exception e2) {
                System.err.println("Erro ao configurar Look and Feel fallback: " + e2.getMessage());
            }
        }
        
        // Configurações adicionais do UIManager
        configurarUIManager();
    }
    
    /**
     * Configura propriedades específicas do UIManager
     */
    private static void configurarUIManager() {
        // Configurar fontes
        Font fonteDefault = new Font("Arial", Font.PLAIN, 12);
        Font fonteBold = new Font("Arial", Font.BOLD, 12);
        Font fonteMaior = new Font("Arial", Font.BOLD, 14);
        
        UIManager.put("Button.font", fonteBold);
        UIManager.put("Label.font", fonteDefault);
        UIManager.put("TextField.font", fonteDefault);
        UIManager.put("TextArea.font", fonteDefault);
        UIManager.put("Table.font", fonteDefault);
        UIManager.put("Tree.font", fonteDefault);
        UIManager.put("Menu.font", fonteDefault);
        UIManager.put("MenuItem.font", fonteDefault);
        
        // Configurar cores padrão
        UIManager.put("Button.background", Tema.AZUL_ESCURO);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", Tema.AZUL_CLARO);
        
        // Configurar bordas
        UIManager.put("TextField.border", BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
        UIManager.put("PasswordField.border", BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
        
        // Configurar tooltips
        UIManager.put("ToolTip.background", Tema.AZUL_CLARO);
        UIManager.put("ToolTip.foreground", Tema.AZUL_ESCURO);
        UIManager.put("ToolTip.font", fonteDefault);
        
        // Configurar diálogos
        UIManager.put("OptionPane.background", Tema.BRANCO);
        UIManager.put("OptionPane.messageFont", fonteMaior);
        UIManager.put("OptionPane.buttonFont", fonteBold);
        UIManager.put("OptionPane.messageForeground", Tema.AZUL_ESCURO);
        
        // Configurar botões do OptionPane
        UIManager.put("OptionPane.buttonBackground", Tema.AZUL_ESCURO);
        UIManager.put("OptionPane.buttonForeground", Color.WHITE);
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "OK");
        
        // Definir padrões de botões
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
        UIManager.put("Button.showMnemonics", Boolean.TRUE);
        UIManager.put("Button.contentAreaFilled", Boolean.TRUE);
        UIManager.put("Button.opaque", Boolean.TRUE);
    }
    
    /**
     * Mostra uma tela de splash durante a inicialização
     */
    private static void mostrarSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(400, 300);
        splash.setLocationRelativeTo(null);
        
        JPanel painelSplash = new JPanel(new BorderLayout());
        painelSplash.setBackground(Tema.AZUL_ESCURO);
        painelSplash.setBorder(BorderFactory.createLineBorder(Tema.AZUL_CLARO, 3));
        
        // Título
        JLabel titulo = new JLabel("AutoFácil", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        painelSplash.add(titulo, BorderLayout.CENTER);
        
        // Subtítulo
        JLabel subtitulo = new JLabel("Java Edition", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitulo.setForeground(Tema.AZUL_CLARO);
        painelSplash.add(subtitulo, BorderLayout.SOUTH);
        
        // Versão
        JLabel versao = new JLabel("Sistema de Locadora de Veículos", SwingConstants.CENTER);
        versao.setFont(new Font("Arial", Font.PLAIN, 12));
        versao.setForeground(Tema.AZUL_CLARO);
        versao.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        painelSplash.add(versao, BorderLayout.NORTH);
        
        splash.add(painelSplash);
        splash.setVisible(true);
        
        // Aguardar 2 segundos
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        splash.dispose();
    }
    
    /**
     * Verifica se todas as dependências estão disponíveis
     */
    private static boolean verificarDependencias() {
        System.out.println("Verificando dependências...");
        
        // Verificar Gson
        try {
            Class.forName("com.google.gson.Gson");
            System.out.println("✓ Gson encontrado");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Gson não encontrado. Verifique se gson.jar está na pasta lib/");
            return false;
        }
        
        // Verificar iText
        try {
            Class.forName("com.itextpdf.text.Document");
            System.out.println("✓ iText encontrado");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ iText não encontrado. Verifique se itextpdf.jar está na pasta lib/");
            return false;
        }
        
        // Verificar JFreeChart
        try {
            Class.forName("org.jfree.chart.JFreeChart");
            System.out.println("✓ JFreeChart encontrado");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ JFreeChart não encontrado. Verifique se jfreechart.jar está na pasta lib/");
            return false;
        }
        
        System.out.println("Todas as dependências foram verificadas com sucesso!");
        return true;
    }
    
    /**
     * Exibe informações sobre o sistema
     */
    private static void exibirInfoSistema() {
        String separador = "============================================================";
        System.out.println(separador);
        System.out.println("           AUTOFACIL JAVA EDITION");
        System.out.println("        Sistema de Locadora de Veiculos");
        System.out.println(separador);
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("User Directory: " + System.getProperty("user.dir"));
        System.out.println("File Encoding: " + System.getProperty("file.encoding"));
        System.out.println(separador);
    }
    
    // Hook para encerramento seguro
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando AutoFácil Java Edition...");
            System.out.println("Sistema encerrado com sucesso!");
        }));
    }
}
