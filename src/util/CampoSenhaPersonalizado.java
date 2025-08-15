package util;

import javax.swing.*;
import java.awt.*;

/**
 * Campo de senha personalizado com estilo visual fixo
 * para evitar mudanças de aparência indesejadas durante o uso
 */
public class CampoSenhaPersonalizado extends JPasswordField {
    
    private final Dimension TAMANHO_FIXO = new Dimension(250, 35);
    
    /**
     * Cria um campo de senha personalizado com estilo consistente
     * @param colunas Número de colunas
     */
    public CampoSenhaPersonalizado(int colunas) {
        super(colunas);
        configurarAparencia();
    }
    
    /**
     * Configura a aparência visual do campo
     */
    private void configurarAparencia() {
        // Configurações visuais fixas
        setBackground(Tema.BRANCO);
        setForeground(Tema.PRETO);
        setFont(new Font("Arial", Font.BOLD, 14));
        setBorder(BorderFactory.createLineBorder(Tema.AZUL_ESCURO, 1));
        
        // Dimensões fixas absolutas
        setPreferredSize(TAMANHO_FIXO);
        setMinimumSize(TAMANHO_FIXO);
        setMaximumSize(TAMANHO_FIXO);
        setSize(TAMANHO_FIXO);
    }
    
    /**
     * Sobrescrever métodos de UI para garantir estilo consistente
     */
    @Override
    public void updateUI() {
        super.updateUI();
        // Reaplicar estilo sempre que a UI for atualizada
        SwingUtilities.invokeLater(() -> configurarAparencia());
    }
    
    /**
     * Sobrescrever para garantir tamanho fixo
     */
    @Override
    public Dimension getPreferredSize() {
        return TAMANHO_FIXO;
    }
    
    /**
     * Sobrescrever para garantir tamanho fixo
     */
    @Override
    public Dimension getMinimumSize() {
        return TAMANHO_FIXO;
    }
    
    /**
     * Sobrescrever para garantir tamanho fixo
     */
    @Override
    public Dimension getMaximumSize() {
        return TAMANHO_FIXO;
    }
}