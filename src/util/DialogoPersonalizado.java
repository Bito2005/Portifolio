package util;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitária para exibir diálogos padronizados com o estilo do sistema
 * Use esta classe em vez de JOptionPane diretamente
 */
public class DialogoPersonalizado {

    /**
     * Exibe uma mensagem informativa
     * @param componente Componente pai
     * @param mensagem Mensagem a exibir
     * @param titulo Título do diálogo
     */
    public static void mostrarInformacao(Component componente, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(
            componente, 
            mensagem, 
            titulo, 
            JOptionPane.INFORMATION_MESSAGE,
            null
        );
    }

    /**
     * Exibe uma mensagem de aviso
     * @param componente Componente pai
     * @param mensagem Mensagem a exibir
     * @param titulo Título do diálogo
     */
    public static void mostrarAviso(Component componente, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(
            componente, 
            mensagem, 
            titulo, 
            JOptionPane.WARNING_MESSAGE,
            null
        );
    }

    /**
     * Exibe uma mensagem de erro
     * @param componente Componente pai
     * @param mensagem Mensagem a exibir
     * @param titulo Título do diálogo
     */
    public static void mostrarErro(Component componente, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(
            componente, 
            mensagem, 
            titulo, 
            JOptionPane.ERROR_MESSAGE,
            null
        );
    }

    /**
     * Exibe um diálogo de confirmação (Sim/Não)
     * @param componente Componente pai
     * @param mensagem Mensagem a exibir
     * @param titulo Título do diálogo
     * @return true se o usuário selecionar "Sim", false caso contrário
     */
    public static boolean confirmar(Component componente, String mensagem, String titulo) {
        int resultado = JOptionPane.showConfirmDialog(
            componente,
            mensagem,
            titulo,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null
        );
        return resultado == JOptionPane.YES_OPTION;
    }

    /**
     * Exibe um diálogo de confirmação com três opções (Sim/Não/Cancelar)
     * @param componente Componente pai
     * @param mensagem Mensagem a exibir
     * @param titulo Título do diálogo
     * @return JOptionPane.YES_OPTION, JOptionPane.NO_OPTION ou JOptionPane.CANCEL_OPTION
     */
    public static int confirmarComCancelar(Component componente, String mensagem, String titulo) {
        return JOptionPane.showConfirmDialog(
            componente,
            mensagem,
            titulo,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null
        );
    }

    /**
     * Exibe um diálogo de entrada de texto
     * @param componente Componente pai
     * @param mensagem Mensagem a exibir
     * @param titulo Título do diálogo
     * @return Texto digitado ou null se cancelado
     */
    public static String solicitarTexto(Component componente, String mensagem, String titulo) {
        return JOptionPane.showInputDialog(
            componente,
            mensagem,
            titulo,
            JOptionPane.QUESTION_MESSAGE
        );
    }

    /**
     * Exibe um diálogo com um componente personalizado
     * @param componente Componente pai
     * @param componentePersonalizado Componente a exibir no diálogo
     * @param titulo Título do diálogo
     */
    public static void mostrarComponente(Component componente, Component componentePersonalizado, String titulo) {
        JOptionPane.showMessageDialog(
            componente,
            componentePersonalizado,
            titulo,
            JOptionPane.PLAIN_MESSAGE,
            null
        );
    }
}
