package ui;

import domain.Veiculo;
import persistence.GerenciadorArquivos;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Diálogo para cadastro e edição de veículos
 */
public class DialogoVeiculo extends JDialog {

    private JTextField campoMarca;
    private JTextField campoModelo;
    private JFormattedTextField campoPlaca;
    private JTextField campoCor;
    private JSpinner campoAno;
    private JComboBox<Veiculo.Categoria> comboCategoria;
    private JTextField campoValorDiaria;
    private JTextField campoQuilometragem;
    private JTextField campoCombustivel;
    private JComboBox<Veiculo.Status> comboStatus;
    private JTextArea campoObservacoes;

    private Veiculo veiculo;
    private boolean confirmado = false;
    private GerenciadorArquivos<Veiculo> gerenciador;

    public DialogoVeiculo(JFrame pai, Veiculo veiculo) {
        super(pai, veiculo == null ? "Novo Veículo" : "Editar Veículo", true);
        this.veiculo = veiculo;
        this.gerenciador = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
        
        inicializarComponentes();
        configurarJanela();
        
        if (veiculo != null) {
            preencherCampos();
        }
        
        aplicarTema();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Painel principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Marca
        gbc.gridx = 0; gbc.gridy = 0;
        painelPrincipal.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoMarca = new JTextField(20);
        painelPrincipal.add(campoMarca, gbc);

        // Modelo
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoModelo = new JTextField(20);
        painelPrincipal.add(campoModelo, gbc);

        // Placa
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        try {
            campoPlaca = new JFormattedTextField(Mascaras.criarMascaraPlaca());
            campoPlaca.setColumns(20);
        } catch (Exception e) {
            campoPlaca = new JFormattedTextField();
            campoPlaca.setColumns(20);
        }
        painelPrincipal.add(campoPlaca, gbc);

        // Cor
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Cor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoCor = new JTextField(20);
        painelPrincipal.add(campoCor, gbc);

        // Ano
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        int anoAtual = LocalDate.now().getYear();
        campoAno = new JSpinner(new SpinnerNumberModel(anoAtual, 1990, anoAtual + 2, 1));
        painelPrincipal.add(campoAno, gbc);

        // Categoria
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboCategoria = new JComboBox<>(Veiculo.Categoria.values());
        painelPrincipal.add(comboCategoria, gbc);

        // Valor Diária
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Valor Diária (R$):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoValorDiaria = new JTextField(20);
        painelPrincipal.add(campoValorDiaria, gbc);

        // Quilometragem
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Quilometragem:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoQuilometragem = new JTextField(20);
        painelPrincipal.add(campoQuilometragem, gbc);

        // Combustível
        gbc.gridx = 0; gbc.gridy = 8; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Combustível:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoCombustivel = new JTextField(20);
        painelPrincipal.add(campoCombustivel, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 9; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboStatus = new JComboBox<>(Veiculo.Status.values());
        painelPrincipal.add(comboStatus, gbc);

        // Observações
        gbc.gridx = 0; gbc.gridy = 10; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        campoObservacoes = new JTextArea(3, 20);
        campoObservacoes.setLineWrap(true);
        campoObservacoes.setWrapStyleWord(true);
        JScrollPane scrollObservacoes = new JScrollPane(campoObservacoes);
        painelPrincipal.add(scrollObservacoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setPreferredSize(new Dimension(100, 35));
        // Aplicar estilo personalizado ao botão Salvar
        Tema.configurarBotaoConfirmacao(btnSalvar);
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarVeiculo();
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        // Aplicar estilo personalizado ao botão Cancelar
        Tema.configurarBotaoCancelamento(btnCancelar);
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        painel.add(btnSalvar);
        painel.add(btnCancelar);
        
        // Adicionar um DocumentListener a cada campo para validar em tempo real
        adicionarValidacaoEmTempoReal(btnSalvar);

        return painel;
    }

    private void configurarJanela() {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void preencherCampos() {
        campoMarca.setText(veiculo.getMarca());
        campoModelo.setText(veiculo.getModelo());
        campoPlaca.setText(veiculo.getPlaca());
        campoCor.setText(veiculo.getCor());
        campoAno.setValue(veiculo.getAno());
        comboCategoria.setSelectedItem(veiculo.getCategoria());
        campoValorDiaria.setText(veiculo.getValorDiaria().toString());
        campoQuilometragem.setText(String.valueOf(veiculo.getQuilometragem()));
        campoCombustivel.setText(veiculo.getCombustivel());
        comboStatus.setSelectedItem(veiculo.getStatus());
        campoObservacoes.setText(veiculo.getObservacoes() != null ? veiculo.getObservacoes() : "");
    }

    private void salvarVeiculo() {
        try {
            // Validações
            if (!validarCampos()) {
                return;
            }

            List<Veiculo> veiculos = gerenciador.carregar();

            if (veiculo == null) {
                // Novo veículo
                veiculo = new Veiculo();
                veiculo.setId(GeradorID.gerarIDVeiculo());
                veiculo.setDataCadastro(LocalDate.now());
            }

            // Verificar se a placa já existe (exceto para o veículo atual)
            String placaDigitada = campoPlaca.getText().trim();
            boolean placaExiste = veiculos.stream()
                .anyMatch(v -> v.getPlaca().equals(placaDigitada) && !v.getId().equals(veiculo.getId()));

            if (placaExiste) {
                JOptionPane.showMessageDialog(this,
                    "Já existe um veículo cadastrado com esta placa.",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Preencher dados
            veiculo.setMarca(campoMarca.getText().trim());
            veiculo.setModelo(campoModelo.getText().trim());
            veiculo.setPlaca(placaDigitada);
            veiculo.setCor(campoCor.getText().trim());
            veiculo.setAno((Integer) campoAno.getValue());
            veiculo.setCategoria((Veiculo.Categoria) comboCategoria.getSelectedItem());
            veiculo.setValorDiaria(new BigDecimal(campoValorDiaria.getText().trim()));
            veiculo.setQuilometragem(Integer.parseInt(campoQuilometragem.getText().trim()));
            veiculo.setCombustivel(campoCombustivel.getText().trim());
            veiculo.setStatus((Veiculo.Status) comboStatus.getSelectedItem());
            veiculo.setObservacoes(campoObservacoes.getText().trim());

            // Salvar
            if (veiculos.stream().noneMatch(v -> v.getId().equals(veiculo.getId()))) {
                veiculos.add(veiculo);
            } else {
                // Atualizar veículo existente
                for (int i = 0; i < veiculos.size(); i++) {
                    if (veiculos.get(i).getId().equals(veiculo.getId())) {
                        veiculos.set(i, veiculo);
                        break;
                    }
                }
            }

            gerenciador.salvar(veiculos);

            JOptionPane.showMessageDialog(this,
                "Veículo salvo com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

            confirmado = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar veículo: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        // Marca
        if (campoMarca.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Marca é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoMarca.requestFocus();
            return false;
        }

        // Modelo
        if (campoModelo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Modelo é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoModelo.requestFocus();
            return false;
        }

        // Placa
        String placa = campoPlaca.getText().trim();
        if (placa.isEmpty() || !Validador.validarPlaca(placa)) {
            JOptionPane.showMessageDialog(this, "Digite uma placa válida (formato: AAA-0000).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoPlaca.requestFocus();
            return false;
        }

        // Cor
        if (campoCor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Cor é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoCor.requestFocus();
            return false;
        }

        // Valor Diária
        try {
            BigDecimal valor = new BigDecimal(campoValorDiaria.getText().trim());
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite um valor válido para a diária.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoValorDiaria.requestFocus();
            return false;
        }

        // Quilometragem
        try {
            int km = Integer.parseInt(campoQuilometragem.getText().trim());
            if (km < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite uma quilometragem válida.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoQuilometragem.requestFocus();
            return false;
        }

        // Combustível
        if (campoCombustivel.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Combustível é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoCombustivel.requestFocus();
            return false;
        }

        return true;
    }

    private void aplicarTema() {
        Tema.configurarPainel((JPanel) getContentPane());
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Adiciona validação em tempo real para ativar/desativar o botão Salvar
     */
    private void adicionarValidacaoEmTempoReal(JButton btnSalvar) {
        // Criar um DocumentListener para os campos de texto
        javax.swing.event.DocumentListener listener = new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validarCamposParaBotao(btnSalvar);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validarCamposParaBotao(btnSalvar);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validarCamposParaBotao(btnSalvar);
            }
        };

        // Adicionar o listener a todos os campos de texto
        campoMarca.getDocument().addDocumentListener(listener);
        campoModelo.getDocument().addDocumentListener(listener);
        campoPlaca.getDocument().addDocumentListener(listener);
        campoCor.getDocument().addDocumentListener(listener);
        campoValorDiaria.getDocument().addDocumentListener(listener);
        campoQuilometragem.getDocument().addDocumentListener(listener);
        campoCombustivel.getDocument().addDocumentListener(listener);
        campoObservacoes.getDocument().addDocumentListener(listener);

        // Adicionar ItemListener aos componentes de seleção
        comboCategoria.addItemListener(e -> validarCamposParaBotao(btnSalvar));
        comboStatus.addItemListener(e -> validarCamposParaBotao(btnSalvar));
        campoAno.addChangeListener(e -> validarCamposParaBotao(btnSalvar));

        // Validar inicialmente
        validarCamposParaBotao(btnSalvar);
    }

    /**
     * Valida os campos para ativar/desativar o botão Salvar
     */
    private void validarCamposParaBotao(JButton btnSalvar) {
        boolean camposValidos = 
            !campoMarca.getText().trim().isEmpty() &&
            !campoModelo.getText().trim().isEmpty() &&
            Validador.validarPlaca(campoPlaca.getText().trim()) &&
            !campoCor.getText().trim().isEmpty() &&
            !campoCombustivel.getText().trim().isEmpty();

        // Validar valor da diária
        try {
            BigDecimal valor = new BigDecimal(campoValorDiaria.getText().trim());
            camposValidos = camposValidos && (valor.compareTo(BigDecimal.ZERO) > 0);
        } catch (NumberFormatException e) {
            camposValidos = false;
        }

        // Validar quilometragem
        try {
            int km = Integer.parseInt(campoQuilometragem.getText().trim());
            camposValidos = camposValidos && (km >= 0);
        } catch (NumberFormatException e) {
            camposValidos = false;
        }

        btnSalvar.setEnabled(camposValidos);
        
        // Atualizar a aparência do botão conforme seu estado
        if (camposValidos) {
            Tema.configurarBotaoConfirmacao(btnSalvar);
        } else {
            CorrecaoTema.corrigirBotao(btnSalvar, new Color(180, 180, 180), Color.WHITE);
        }
    }
}
