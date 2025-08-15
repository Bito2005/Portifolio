package auth;

import domain.Cliente;
import domain.Funcionario;
import persistence.GerenciadorArquivos;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Gerenciador de autenticação do sistema
 * Controla login de funcionários e clientes
 */
public class GerenciadorAutenticacao {
    
    private static GerenciadorAutenticacao instancia;
    private GerenciadorArquivos<Funcionario> gerenciadorFuncionarios;
    private GerenciadorArquivos<Cliente> gerenciadorClientes;
    
    private Object usuarioLogado;
    private TipoUsuario tipoUsuarioLogado;
    private int tentativasLogin = 0;
    private static final int MAX_TENTATIVAS = 3;
    
    public enum TipoUsuario {
        ADMIN,
        FUNCIONARIO,
        CLIENTE
    }
    
    private GerenciadorAutenticacao() {
        gerenciadorFuncionarios = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class);
        gerenciadorClientes = new GerenciadorArquivos<>("clientes.json", Cliente.class);
        criarUsuarioAdminPadrao();
    }
    
    public static GerenciadorAutenticacao getInstance() {
        if (instancia == null) {
            instancia = new GerenciadorAutenticacao();
        }
        return instancia;
    }
    
    /**
     * Cria o usuário admin padrão se não existir
     */
    private void criarUsuarioAdminPadrao() {
        List<Funcionario> funcionarios = gerenciadorFuncionarios.carregar();
        
        // Verificar se já existe um admin
        boolean adminExiste = funcionarios.stream()
            .anyMatch(f -> "admin".equals(f.getUsuario()));
        
        if (!adminExiste) {
            Funcionario admin = new Funcionario();
            admin.setId(util.GeradorID.gerarIDFuncionario());
            admin.setNome("Administrador do Sistema");
            admin.setCpf("000.000.000-00");
            admin.setEmail("admin@autofacil.com");
            admin.setTelefone("(00) 00000-0000");
            admin.setUsuario("admin");
            admin.setSenha("admin");
            admin.setTipo(Funcionario.TipoFuncionario.ADMIN);
            
            funcionarios.add(admin);
            gerenciadorFuncionarios.salvar(funcionarios);
            
            System.out.println("Usuário admin padrão criado: admin/admin");
        }
    }
    
    /**
     * Realiza login no sistema
     */
    public ResultadoLogin login(String usuario, String senha) {
        if (tentativasLogin >= MAX_TENTATIVAS) {
            return new ResultadoLogin(false, "Número máximo de tentativas excedido. Sistema será encerrado.", null, null);
        }
        
        if (usuario == null || usuario.trim().isEmpty() || 
            senha == null || senha.trim().isEmpty()) {
            tentativasLogin++;
            return new ResultadoLogin(false, "Usuário e senha são obrigatórios.", null, null);
        }
        
        // Tentar login como funcionário primeiro
        List<Funcionario> funcionarios = gerenciadorFuncionarios.carregar();
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getUsuario().equals(usuario) && 
                funcionario.getSenha().equals(senha) && 
                funcionario.isAtivo()) {
                
                funcionario.setUltimoLogin(LocalDateTime.now());
                gerenciadorFuncionarios.salvar(funcionarios);
                
                usuarioLogado = funcionario;
                tipoUsuarioLogado = funcionario.isAdmin() ? TipoUsuario.ADMIN : TipoUsuario.FUNCIONARIO;
                tentativasLogin = 0;
                
                return new ResultadoLogin(true, "Login realizado com sucesso!", 
                                        funcionario, tipoUsuarioLogado);
            }
        }
        
        // Tentar login como cliente
        List<Cliente> clientes = gerenciadorClientes.carregar();
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().replaceAll("[^0-9]", "").equals(usuario.replaceAll("[^0-9]", "")) && 
                cliente.getSenha().equals(senha) && 
                cliente.isAtivo()) {
                
                cliente.setUltimoLogin(LocalDateTime.now());
                gerenciadorClientes.salvar(clientes);
                
                usuarioLogado = cliente;
                tipoUsuarioLogado = TipoUsuario.CLIENTE;
                tentativasLogin = 0;
                
                return new ResultadoLogin(true, "Login realizado com sucesso!", 
                                        cliente, tipoUsuarioLogado);
            }
        }
        
        tentativasLogin++;
        String mensagem = "Senha incorreta";
        if (tentativasLogin >= MAX_TENTATIVAS) {
            mensagem = "Número máximo de tentativas excedido. Sistema será encerrado.";
        }
        
        return new ResultadoLogin(false, mensagem, null, null);
    }
    
    /**
     * Realiza logout do sistema
     */
    public void logout() {
        usuarioLogado = null;
        tipoUsuarioLogado = null;
        tentativasLogin = 0;
    }
    
    /**
     * Verifica se há um usuário logado
     */
    public boolean isLogado() {
        return usuarioLogado != null;
    }
    
    /**
     * Retorna o usuário logado
     */
    public Object getUsuarioLogado() {
        return usuarioLogado;
    }
    
    /**
     * Retorna o tipo do usuário logado
     */
    public TipoUsuario getTipoUsuarioLogado() {
        return tipoUsuarioLogado;
    }
    
    /**
     * Verifica se o usuário logado é admin
     */
    public boolean isAdmin() {
        return tipoUsuarioLogado == TipoUsuario.ADMIN;
    }
    
    /**
     * Verifica se o usuário logado é funcionário
     */
    public boolean isFuncionario() {
        return tipoUsuarioLogado == TipoUsuario.FUNCIONARIO || 
               tipoUsuarioLogado == TipoUsuario.ADMIN;
    }
    
    /**
     * Verifica se o usuário logado é cliente
     */
    public boolean isCliente() {
        return tipoUsuarioLogado == TipoUsuario.CLIENTE;
    }
    
    /**
     * Retorna o nome do usuário logado
     */
    public String getNomeUsuarioLogado() {
        if (usuarioLogado instanceof Funcionario) {
            return ((Funcionario) usuarioLogado).getNome();
        } else if (usuarioLogado instanceof Cliente) {
            return ((Cliente) usuarioLogado).getNome();
        }
        return "Usuário";
    }
    
    /**
     * Retorna o ID do usuário logado
     */
    public String getIdUsuarioLogado() {
        if (usuarioLogado instanceof Funcionario) {
            return ((Funcionario) usuarioLogado).getId();
        } else if (usuarioLogado instanceof Cliente) {
            return ((Cliente) usuarioLogado).getId();
        }
        return null;
    }
    
    /**
     * Retorna o ID do cliente logado
     */
    public String getClienteLogadoId() {
        if (isCliente() && usuarioLogado instanceof Cliente) {
            return ((Cliente) usuarioLogado).getId();
        }
        return null;
    }
    
    /**
     * Verifica se o usuário tem permissão para acessar uma funcionalidade
     */
    public boolean temPermissao(String funcionalidade) {
        if (!isLogado()) {
            return false;
        }
        
        switch (funcionalidade.toLowerCase()) {
            case "funcionarios":
            case "cadastrar_funcionario":
            case "editar_funcionario":
            case "excluir_funcionario":
                return isAdmin();
                
            case "clientes":
            case "veiculos":
            case "alugueis":
            case "relatorios":
                return isFuncionario();
                
            case "meus_alugueis":
                return isCliente();
                
            case "configuracoes":
            case "sobre":
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Retorna o número de tentativas de login restantes
     */
    public int getTentativasRestantes() {
        return Math.max(0, MAX_TENTATIVAS - tentativasLogin);
    }
    
    /**
     * Verifica se as tentativas de login foram esgotadas
     */
    public boolean tentativasEsgotadas() {
        return tentativasLogin >= MAX_TENTATIVAS;
    }
    
    /**
     * Retorna o gerenciador de clientes
     */
    public GerenciadorArquivos<Cliente> getGerenciadorClientes() {
        return gerenciadorClientes;
    }
    
    /**
     * Retorna o gerenciador de funcionários
     */
    public GerenciadorArquivos<Funcionario> getGerenciadorFuncionarios() {
        return gerenciadorFuncionarios;
    }
    
    /**
     * Classe para retorno do resultado do login
     */
    public static class ResultadoLogin {
        private final boolean sucesso;
        private final String mensagem;
        private final Object usuario;
        private final TipoUsuario tipo;
        
        public ResultadoLogin(boolean sucesso, String mensagem, Object usuario, TipoUsuario tipo) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.usuario = usuario;
            this.tipo = tipo;
        }
        
        public boolean isSucesso() {
            return sucesso;
        }
        
        public String getMensagem() {
            return mensagem;
        }
        
        public Object getUsuario() {
            return usuario;
        }
        
        public TipoUsuario getTipo() {
            return tipo;
        }
    }
}
