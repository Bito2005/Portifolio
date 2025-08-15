package ui;

import domain.Aluguel;
import domain.Cliente;
import domain.Funcionario;
import domain.Veiculo;
import persistence.GerenciadorArquivos;
import util.GeradorID;
import util.Tema;
import auth.GerenciadorAutenticacao;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Diálogo para cadastro e edição de aluguéis
 */
public class DialogoAluguel extends JDialog {

    private JComboBox<Cliente> comboCliente;
    private JComboBox<Veiculo> comboVeiculo;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JTextField campoQuilometragemInicial;
    private JTextField campoObservacoes;
    private JLabel labelValorDiaria;
    private JLabel labelValorTotal;
    private JLabel labelDias;
    
    private Aluguel aluguel;
    private boolean confirmado = false;
    private GerenciadorArquivos<Aluguel> gerenciadorAlugueis;
    private GerenciadorArquivos<Cliente> gerenciadorClientes;
    private GerenciadorArquivos<Veiculo> gerenciadorVeiculos;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DialogoAluguel(JFrame parent, Aluguel aluguel) {
        super(parent, aluguel == null ? "Novo Aluguel" : "Editar Aluguel", true);
        this.aluguel = aluguel;
        this.gerenciadorAlugueis = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
        this.gerenciadorClientes = new GerenciadorArquivos<>("clientes.json", Cliente.class);
        this.gerenciadorVeiculos = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
        
        inicializarComponentes();
        configurarLayouts();
        configurarEventos();
        aplicarTema();
        preencherCampos();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        // ComboBox de clientes
        comboCliente = new JComboBox<>();
        List<Cliente> clientes = gerenciadorClientes.carregar();
        for (Cliente cliente : clientes) {
            if (cliente.isAtivo()) {
                comboCliente.addItem(cliente);
            }
        }
        
        // ComboBox de veículos
        comboVeiculo = new JComboBox<>();
        List<Veiculo> veiculos = gerenciadorVeiculos.carregar();
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getStatus() == Veiculo.Status.DISPONIVEL) {
                comboVeiculo.addItem(veiculo);
            }
        }
        
        // Campos de data
        campoDataInicio = new JTextField(10);
        campoDataFim = new JTextField(10);
        
        // Campo de quilometragem
        campoQuilometragemInicial = new JTextField(10);
        
        // Campo de observações
        campoObservacoes = new JTextField(30);
        
        // Labels informativos
        labelValorDiaria = new JLabel("R$ 0,00");
        labelValorTotal = new JLabel("R$ 0,00");
        labelDias = new JLabel("0 dias");
        
        // Configurar datas padrão
        if (aluguel == null) {
            campoDataInicio.setText(LocalDate.now().format(FORMATO_DATA));
            campoDataFim.setText(LocalDate.now().plusDays(1).format(FORMATO_DATA));
        }
    }

    private void configurarLayouts() {
        setLayout(new BorderLayout());

        // Painel principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        painelPrincipal.add(new JLabel("Cliente*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridwidth = 2;
        painelPrincipal.add(comboCliente, gbc);

        // Veículo
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Veículo*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridwidth = 2;
        painelPrincipal.add(comboVeiculo, gbc);

        // Valor da diária
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Valor da Diária:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        painelPrincipal.add(labelValorDiaria, gbc);

        // Data de início
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Data Início*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoDataInicio, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("(dd/MM/yyyy)"), gbc);

        // Data de fim
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Data Fim*:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoDataFim, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("(dd/MM/yyyy)"), gbc);

        // Quantidade de dias
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Quantidade de Dias:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        painelPrincipal.add(labelDias, gbc);

        // Valor total
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Valor Total:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        labelValorTotal.setFont(new Font("Arial", Font.BOLD, 14));
        painelPrincipal.add(labelValorTotal, gbc);

        // Quilometragem inicial
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Quilometragem Inicial:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(campoQuilometragemInicial, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("km"), gbc);

        // Observações
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 1;
        painelPrincipal.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridwidth = 2;
        painelPrincipal.add(campoObservacoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnCalcular = new JButton("Calcular");
        btnCalcular.setPreferredSize(new Dimension(100, 35));
        btnCalcular.addActionListener(e -> calcularValores());
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setPreferredSize(new Dimension(100, 35));
        btnSalvar.addActionListener(e -> salvarAluguel());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> dispose());

        Tema.configurarBotao(btnCalcular);
        Tema.configurarBotao(btnSalvar);
        Tema.configurarBotaoCancelamento(btnCancelar);
        
        painelBotoes.add(btnCalcular);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        // Atualizar valor da diária quando o veículo for selecionado
        comboVeiculo.addActionListener(e -> atualizarValorDiaria());
        
        // Calcular automaticamente quando as datas mudarem
        campoDataInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                calcularValores();
            }
        });
        
        campoDataFim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                calcularValores();
            }
        });
    }

    private void atualizarValorDiaria() {
        try {
            Veiculo veiculo = (Veiculo) comboVeiculo.getSelectedItem();
            if (veiculo != null) {
                labelValorDiaria.setText(String.format("R$ %.2f", veiculo.getValorDiaria()));
                calcularValores();
            } else {
                labelValorDiaria.setText("R$ 0,00");
                labelValorTotal.setText("R$ 0,00");
                labelDias.setText("0 dias");
            }
        } catch (Exception e) {
            labelValorDiaria.setText("Erro ao calcular");
            e.printStackTrace();
        }
    }

    private void calcularValores() {
        try {
            // Verificar se os campos de data não estão vazios
            if (campoDataInicio.getText().trim().isEmpty() || campoDataFim.getText().trim().isEmpty()) {
                labelDias.setText("0 dias");
                labelValorTotal.setText("R$ 0,00");
                return;
            }
            
            LocalDate dataInicio = LocalDate.parse(campoDataInicio.getText().trim(), FORMATO_DATA);
            LocalDate dataFim = LocalDate.parse(campoDataFim.getText().trim(), FORMATO_DATA);
            Veiculo veiculo = (Veiculo) comboVeiculo.getSelectedItem();
            
            if (veiculo != null && !dataFim.isBefore(dataInicio)) {
                long dias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
                BigDecimal valorTotal = veiculo.getValorDiaria().multiply(BigDecimal.valueOf(dias));
                
                labelDias.setText(dias + " dia" + (dias > 1 ? "s" : ""));
                labelValorTotal.setText(String.format("R$ %.2f", valorTotal));
            } else {
                if (veiculo == null) {
                    labelDias.setText("Selecione um veículo");
                } else {
                    labelDias.setText("Data fim antes da data início");
                }
                labelValorTotal.setText("R$ 0,00");
            }
        } catch (Exception e) {
            labelDias.setText("Data inválida");
            labelValorTotal.setText("R$ 0,00");
            // Não é necessário exibir erro ao usuário durante cálculos automáticos
        }
    }

    private void preencherCampos() {
        try {
            if (aluguel != null) {
                // Buscar e selecionar cliente
                List<Cliente> clientes = gerenciadorClientes.carregar();
                
                // Usar filter e findFirst com segurança
                Cliente cliente = clientes.stream()
                    .filter(c -> c.getId() != null && c.getId().equals(aluguel.getClienteId()))
                    .findFirst()
                    .orElse(null);
                    
                if (cliente != null) {
                    // Verificar se o cliente existe no combobox antes de selecioná-lo
                    for (int i = 0; i < comboCliente.getItemCount(); i++) {
                        Cliente c = comboCliente.getItemAt(i);
                        if (c.getId().equals(cliente.getId())) {
                            comboCliente.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                
                // Buscar e selecionar veículo
                List<Veiculo> veiculos = gerenciadorVeiculos.carregar();
                
                // Usar filter e findFirst com segurança
                Veiculo veiculo = veiculos.stream()
                    .filter(v -> v.getId() != null && v.getId().equals(aluguel.getVeiculoId()))
                    .findFirst()
                    .orElse(null);
                    
                if (veiculo != null) {
                    // Verificar se o veículo existe no combobox antes de selecioná-lo
                    for (int i = 0; i < comboVeiculo.getItemCount(); i++) {
                        Veiculo v = comboVeiculo.getItemAt(i);
                        if (v.getId().equals(veiculo.getId())) {
                            comboVeiculo.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                
                // Verificar datas nulas antes de formatar
                if (aluguel.getDataInicio() != null) {
                    campoDataInicio.setText(aluguel.getDataInicio().format(FORMATO_DATA));
                }
                
                if (aluguel.getDataFimPrevista() != null) {
                    campoDataFim.setText(aluguel.getDataFimPrevista().format(FORMATO_DATA));
                }
                
                campoQuilometragemInicial.setText(String.valueOf(aluguel.getQuilometragemInicial()));
                campoObservacoes.setText(aluguel.getObservacoes() != null ? aluguel.getObservacoes() : "");
                
                atualizarValorDiaria();
                calcularValores();
            } else {
                // Configurar datas padrão para novo aluguel
                campoDataInicio.setText(LocalDate.now().format(FORMATO_DATA));
                campoDataFim.setText(LocalDate.now().plusDays(1).format(FORMATO_DATA));
                campoQuilometragemInicial.setText("0");
                atualizarValorDiaria();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar dados do aluguel: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void salvarAluguel() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Cliente cliente = (Cliente) comboCliente.getSelectedItem();
            Veiculo veiculo = (Veiculo) comboVeiculo.getSelectedItem();
            
            // Verificação extra para garantir que cliente e veículo não sejam nulos
            if (cliente == null || veiculo == null) {
                JOptionPane.showMessageDialog(this,
                    "Erro: Cliente ou veículo não selecionado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDate dataInicio = LocalDate.parse(campoDataInicio.getText().trim(), FORMATO_DATA);
            LocalDate dataFim = LocalDate.parse(campoDataFim.getText().trim(), FORMATO_DATA);
            int quilometragemInicial = Integer.parseInt(campoQuilometragemInicial.getText().trim());
            String observacoes = campoObservacoes.getText().trim();

            List<Aluguel> alugueis = gerenciadorAlugueis.carregar();
            List<Veiculo> veiculos = gerenciadorVeiculos.carregar();

            if (aluguel == null) {
                // Novo aluguel
                String id = GeradorID.gerarIDAluguel();
                
                // Obter funcionário para o aluguel
                String funcionarioId;
                GerenciadorAutenticacao auth = GerenciadorAutenticacao.getInstance();
                
                if (auth.isCliente()) {
                    // Se for cliente logado, atribuir a um funcionário padrão
                    // Buscamos o primeiro funcionário ADMIN como responsável
                    List<Funcionario> funcionarios = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class).carregar();
                    funcionarioId = funcionarios.stream()
                        .filter(f -> f.getTipo() == Funcionario.TipoFuncionario.ADMIN && f.isAtivo())
                        .map(Funcionario::getId)
                        .findFirst()
                        .orElse(null);
                        
                    if (funcionarioId == null) {
                        // Se não encontrar admin, pegar qualquer funcionário ativo
                        funcionarioId = funcionarios.stream()
                            .filter(f -> f.isAtivo())
                            .map(Funcionario::getId)
                            .findFirst()
                            .orElse(null);
                    }
                } else {
                    // Caso seja um funcionário realizando o aluguel
                    funcionarioId = auth.getIdUsuarioLogado();
                }
                
                if (funcionarioId == null || funcionarioId.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Erro: Não foi possível atribuir um funcionário responsável pelo aluguel.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                aluguel = new Aluguel();
                aluguel.setId(id);
                aluguel.setClienteId(cliente.getId());
                aluguel.setVeiculoId(veiculo.getId());
                aluguel.setFuncionarioId(funcionarioId);
                aluguel.setDataInicio(dataInicio);
                aluguel.setDataFimPrevista(dataFim);
                aluguel.setDataHoraCriacao(LocalDateTime.now());
                aluguel.setQuilometragemInicial(quilometragemInicial);
                aluguel.setValorDiaria(veiculo.getValorDiaria());
                aluguel.setObservacoes(observacoes);
                aluguel.setStatus(Aluguel.StatusAluguel.ATIVO);
                
                // Calcular valor total
                long dias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
                aluguel.setValorTotal(veiculo.getValorDiaria().multiply(BigDecimal.valueOf(dias)));
                
                alugueis.add(aluguel);
                
                // Marcar veículo como alugado
                boolean veiculoAtualizado = false;
                for (Veiculo v : veiculos) {
                    if (v.getId() != null && v.getId().equals(veiculo.getId())) {
                        v.setStatus(Veiculo.Status.ALUGADO);
                        veiculoAtualizado = true;
                        break;
                    }
                }
                
                if (!veiculoAtualizado) {
                    JOptionPane.showMessageDialog(this,
                        "Aviso: Não foi possível atualizar o status do veículo.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                }
                    
            } else {
                // Editar aluguel existente (apenas se não estiver finalizado)
                if (aluguel.getStatus() != Aluguel.StatusAluguel.ATIVO) {
                    JOptionPane.showMessageDialog(this,
                        "Apenas aluguéis ativos podem ser editados.",
                        "Operação não permitida",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                aluguel.setDataInicio(dataInicio);
                aluguel.setDataFimPrevista(dataFim);
                aluguel.setQuilometragemInicial(quilometragemInicial);
                aluguel.setObservacoes(observacoes);
                
                // Recalcular valor total
                long dias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
                aluguel.setValorTotal(veiculo.getValorDiaria().multiply(BigDecimal.valueOf(dias)));
                
                // Verificar se o aluguel existe na lista antes de salvar
                boolean aluguelEncontrado = false;
                for (int i = 0; i < alugueis.size(); i++) {
                    if (alugueis.get(i).getId().equals(aluguel.getId())) {
                        alugueis.set(i, aluguel);
                        aluguelEncontrado = true;
                        break;
                    }
                }
                
                if (!aluguelEncontrado) {
                    JOptionPane.showMessageDialog(this,
                        "Aviso: O aluguel que está sendo editado não foi encontrado na base de dados.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                    alugueis.add(aluguel);
                }
            }

            // Salvar alterações
            boolean salvouAlugueis = gerenciadorAlugueis.salvar(alugueis);
            boolean salvouVeiculos = gerenciadorVeiculos.salvar(veiculos);
            
            if (salvouAlugueis && salvouVeiculos) {
                confirmado = true;
                
                JOptionPane.showMessageDialog(this,
                    "Aluguel salvo com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Aviso: Pode ter ocorrido um erro ao salvar os dados.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar aluguel: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();

        // Validar cliente
        if (comboCliente.getSelectedItem() == null) {
            erros.append("- Selecione um cliente\n");
        }

        // Validar veículo
        if (comboVeiculo.getSelectedItem() == null) {
            erros.append("- Selecione um veículo\n");
        }

        // Validar datas
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        boolean datasValidas = true;
        
        try {
            if (campoDataInicio.getText().trim().isEmpty()) {
                erros.append("- Data de início é obrigatória\n");
                datasValidas = false;
            } else {
                dataInicio = LocalDate.parse(campoDataInicio.getText().trim(), FORMATO_DATA);
            }
        } catch (Exception e) {
            erros.append("- Formato de data de início inválido (use dd/MM/yyyy)\n");
            datasValidas = false;
        }
        
        try {
            if (campoDataFim.getText().trim().isEmpty()) {
                erros.append("- Data de fim é obrigatória\n");
                datasValidas = false;
            } else {
                dataFim = LocalDate.parse(campoDataFim.getText().trim(), FORMATO_DATA);
            }
        } catch (Exception e) {
            erros.append("- Formato de data de fim inválido (use dd/MM/yyyy)\n");
            datasValidas = false;
        }
        
        if (datasValidas && dataInicio != null && dataFim != null) {
            if (dataFim.isBefore(dataInicio)) {
                erros.append("- Data de fim deve ser posterior à data de início\n");
            }
            
            if (dataInicio.isBefore(LocalDate.now())) {
                erros.append("- Data de início não pode ser no passado\n");
            }
        }

        // Validar quilometragem
        try {
            if (campoQuilometragemInicial.getText().trim().isEmpty()) {
                erros.append("- Quilometragem inicial é obrigatória\n");
            } else {
                int km = Integer.parseInt(campoQuilometragemInicial.getText().trim());
                if (km < 0) {
                    erros.append("- Quilometragem deve ser um número positivo\n");
                }
            }
        } catch (NumberFormatException e) {
            erros.append("- Quilometragem deve ser um número válido\n");
        }

        // Se houver erros, mostrar mensagem
        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Corrija os seguintes erros:\n\n" + erros.toString(),
                "Dados Inválidos",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void aplicarTema() {
        // Aplicar tema geral ao diálogo
        Tema.configurarDialog(this);
        
        // Personalizar componentes
        if (comboCliente != null) {
            comboCliente.setBackground(Tema.BRANCO);
            comboCliente.setForeground(Tema.PRETO);
        }
        
        if (comboVeiculo != null) {
            comboVeiculo.setBackground(Tema.BRANCO);
            comboVeiculo.setForeground(Tema.PRETO);
        }
        
        // Configurar campos de texto
        JTextField[] campos = {campoDataInicio, campoDataFim, campoQuilometragemInicial, campoObservacoes};
        for (JTextField campo : campos) {
            if (campo != null) {
                campo.setBackground(Tema.BRANCO);
                campo.setForeground(Tema.PRETO);
                campo.setBorder(BorderFactory.createCompoundBorder(
                    campo.getBorder(),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));
            }
        }
        
        // Configurar labels informativos
        JLabel[] labels = {labelValorDiaria, labelValorTotal, labelDias};
        for (JLabel label : labels) {
            if (label != null) {
                label.setForeground(Tema.AZUL_ESCURO);
            }
        }
        
        // Realçar valor total
        if (labelValorTotal != null) {
            labelValorTotal.setFont(new Font("Arial", Font.BOLD, 14));
            labelValorTotal.setForeground(Tema.VERDE);
        }
    }

    /**
     * Configura o diálogo para usar o cliente logado, desabilitando a seleção de cliente
     * 
     * @param clienteId ID do cliente logado
     */
    public void configurarClienteLogado(String clienteId) {
        if (clienteId == null || clienteId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Cliente não identificado. Não é possível criar o aluguel.",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        try {
            // Carrega a lista de clientes
            List<Cliente> clientes = gerenciadorClientes.carregar();
            
            // Encontra o cliente pelo ID
            Cliente clienteLogado = clientes.stream()
                .filter(c -> c.getId().equals(clienteId))
                .findFirst()
                .orElse(null);
                
            if (clienteLogado == null) {
                JOptionPane.showMessageDialog(this,
                    "Cliente não encontrado. Não é possível criar o aluguel.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            
            // Limpa e adiciona apenas o cliente logado
            comboCliente.removeAllItems();
            comboCliente.addItem(clienteLogado);
            comboCliente.setSelectedItem(clienteLogado);
            
            // Desabilita o combobox para evitar mudanças
            comboCliente.setEnabled(false);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao configurar cliente: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            dispose();
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
