package util;

import domain.*;
import persistence.GerenciadorArquivos;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe para gerar dados de exemplo para o sistema
 */
public class GeradorDadosExemplo {
    
    public static void gerarDadosSeNecessario() {
        gerarFuncionariosExemplo();
        gerarClientesExemplo();
        gerarVeiculosExemplo();
        gerarAlugueisExemplo();
    }
    
    private static void gerarFuncionariosExemplo() {
        GerenciadorArquivos<Funcionario> gerenciador = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class);
        List<Funcionario> funcionarios = gerenciador.carregar();
        
        if (funcionarios.size() <= 1) { // Só tem o admin padrão
            // Funcionário 1
            Funcionario func1 = new Funcionario();
            func1.setId(GeradorID.gerarIDFuncionario());
            func1.setNome("Maria Silva Santos");
            func1.setCpf("123.456.789-01");
            func1.setEmail("maria.silva@autofacil.com");
            func1.setTelefone("(11) 99876-5432");
            func1.setUsuario("maria.silva");
            func1.setSenha("123456");
            func1.setTipo(Funcionario.TipoFuncionario.FUNCIONARIO);
            funcionarios.add(func1);
            
            // Funcionário 2
            Funcionario func2 = new Funcionario();
            func2.setId(GeradorID.gerarIDFuncionario());
            func2.setNome("João Pedro Oliveira");
            func2.setCpf("987.654.321-09");
            func2.setEmail("joao.pedro@autofacil.com");
            func2.setTelefone("(11) 98765-4321");
            func2.setUsuario("joao.pedro");
            func2.setSenha("123456");
            func2.setTipo(Funcionario.TipoFuncionario.FUNCIONARIO);
            funcionarios.add(func2);
            
            gerenciador.salvar(funcionarios);
            System.out.println("Funcionários de exemplo criados.");
        }
    }
    
    private static void gerarClientesExemplo() {
        GerenciadorArquivos<Cliente> gerenciador = new GerenciadorArquivos<>("clientes.json", Cliente.class);
        List<Cliente> clientes = gerenciador.carregar();
        
        if (clientes.isEmpty()) {
            // Cliente 1
            Cliente cli1 = new Cliente();
            cli1.setId(GeradorID.gerarIDCliente());
            cli1.setNome("Ana Carolina Ferreira");
            cli1.setCpf("111.222.333-44");
            cli1.setEmail("ana.ferreira@email.com");
            cli1.setTelefone("(11) 91234-5678");
            cli1.setCep("01234-567");
            cli1.setEndereco("Rua das Flores, 123 - Centro");
            cli1.setSenha("123456");
            clientes.add(cli1);
            
            // Cliente 2
            Cliente cli2 = new Cliente();
            cli2.setId(GeradorID.gerarIDCliente());
            cli2.setNome("Carlos Eduardo Santos");
            cli2.setCpf("555.666.777-88");
            cli2.setEmail("carlos.santos@email.com");
            cli2.setTelefone("(11) 95678-9012");
            cli2.setCep("04567-890");
            cli2.setEndereco("Av. Paulista, 456 - Bela Vista");
            cli2.setSenha("123456");
            clientes.add(cli2);
            
            // Cliente 3
            Cliente cli3 = new Cliente();
            cli3.setId(GeradorID.gerarIDCliente());
            cli3.setNome("Fernanda Ribeiro Costa");
            cli3.setCpf("999.888.777-66");
            cli3.setEmail("fernanda.costa@email.com");
            cli3.setTelefone("(11) 97890-1234");
            cli3.setCep("02345-678");
            cli3.setEndereco("Rua Augusta, 789 - Consolação");
            cli3.setSenha("123456");
            clientes.add(cli3);
            
            gerenciador.salvar(clientes);
            System.out.println("Clientes de exemplo criados.");
        }
    }
    
    private static void gerarVeiculosExemplo() {
        GerenciadorArquivos<Veiculo> gerenciador = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
        List<Veiculo> veiculos = gerenciador.carregar();
        
        if (veiculos.isEmpty()) {
            // Veículo 1 - Econômico
            Veiculo vei1 = new Veiculo();
            vei1.setId(GeradorID.gerarIDVeiculo());
            vei1.setMarca("Chevrolet");
            vei1.setModelo("Onix");
            vei1.setPlaca("ABC-1234");
            vei1.setCor("Branco");
            vei1.setAno(2022);
            vei1.setCategoria(Veiculo.Categoria.ECONOMICO);
            vei1.setValorDiaria(new BigDecimal("80.00"));
            vei1.setQuilometragem(15000);
            vei1.setCombustivel("Flex");
            vei1.setStatus(Veiculo.Status.DISPONIVEL);
            veiculos.add(vei1);
            
            // Veículo 2 - Compacto
            Veiculo vei2 = new Veiculo();
            vei2.setId(GeradorID.gerarIDVeiculo());
            vei2.setMarca("Volkswagen");
            vei2.setModelo("Polo");
            vei2.setPlaca("DEF-5678");
            vei2.setCor("Prata");
            vei2.setAno(2021);
            vei2.setCategoria(Veiculo.Categoria.COMPACTO);
            vei2.setValorDiaria(new BigDecimal("95.00"));
            vei2.setQuilometragem(22000);
            vei2.setCombustivel("Flex");
            vei2.setStatus(Veiculo.Status.DISPONIVEL);
            veiculos.add(vei2);
            
            // Veículo 3 - Intermediário
            Veiculo vei3 = new Veiculo();
            vei3.setId(GeradorID.gerarIDVeiculo());
            vei3.setMarca("Honda");
            vei3.setModelo("Civic");
            vei3.setPlaca("GHI-9012");
            vei3.setCor("Preto");
            vei3.setAno(2023);
            vei3.setCategoria(Veiculo.Categoria.INTERMEDIARIO);
            vei3.setValorDiaria(new BigDecimal("120.00"));
            vei3.setQuilometragem(8000);
            vei3.setCombustivel("Flex");
            vei3.setStatus(Veiculo.Status.DISPONIVEL);
            veiculos.add(vei3);
            
            // Veículo 4 - Executivo
            Veiculo vei4 = new Veiculo();
            vei4.setId(GeradorID.gerarIDVeiculo());
            vei4.setMarca("Toyota");
            vei4.setModelo("Corolla");
            vei4.setPlaca("JKL-3456");
            vei4.setCor("Prata");
            vei4.setAno(2023);
            vei4.setCategoria(Veiculo.Categoria.EXECUTIVO);
            vei4.setValorDiaria(new BigDecimal("150.00"));
            vei4.setQuilometragem(5000);
            vei4.setCombustivel("Híbrido");
            vei4.setStatus(Veiculo.Status.DISPONIVEL);
            veiculos.add(vei4);
            
            // Veículo 5 - SUV
            Veiculo vei5 = new Veiculo();
            vei5.setId(GeradorID.gerarIDVeiculo());
            vei5.setMarca("Jeep");
            vei5.setModelo("Compass");
            vei5.setPlaca("MNO-7890");
            vei5.setCor("Azul");
            vei5.setAno(2022);
            vei5.setCategoria(Veiculo.Categoria.SUV);
            vei5.setValorDiaria(new BigDecimal("180.00"));
            vei5.setQuilometragem(18000);
            vei5.setCombustivel("Flex");
            vei5.setStatus(Veiculo.Status.DISPONIVEL);
            veiculos.add(vei5);
            
            // Veículo 6 - Luxo
            Veiculo vei6 = new Veiculo();
            vei6.setId(GeradorID.gerarIDVeiculo());
            vei6.setMarca("BMW");
            vei6.setModelo("X3");
            vei6.setPlaca("PQR-1357");
            vei6.setCor("Branco");
            vei6.setAno(2024);
            vei6.setCategoria(Veiculo.Categoria.LUXO);
            vei6.setValorDiaria(new BigDecimal("250.00"));
            vei6.setQuilometragem(2000);
            vei6.setCombustivel("Gasolina");
            vei6.setStatus(Veiculo.Status.DISPONIVEL);
            veiculos.add(vei6);
            
            // Veículo em manutenção
            Veiculo vei7 = new Veiculo();
            vei7.setId(GeradorID.gerarIDVeiculo());
            vei7.setMarca("Ford");
            vei7.setModelo("EcoSport");
            vei7.setPlaca("STU-2468");
            vei7.setCor("Vermelho");
            vei7.setAno(2020);
            vei7.setCategoria(Veiculo.Categoria.COMPACTO);
            vei7.setValorDiaria(new BigDecimal("90.00"));
            vei7.setQuilometragem(35000);
            vei7.setCombustivel("Flex");
            vei7.setStatus(Veiculo.Status.MANUTENCAO);
            vei7.setObservacoes("Revisão dos 30.000 km");
            veiculos.add(vei7);
            
            gerenciador.salvar(veiculos);
            System.out.println("Veículos de exemplo criados.");
        }
    }
    
    private static void gerarAlugueisExemplo() {
        GerenciadorArquivos<Aluguel> gerenciador = new GerenciadorArquivos<>("alugueis.json", Aluguel.class);
        List<Aluguel> alugueis = gerenciador.carregar();
        
        if (alugueis.isEmpty()) {
            // Carregar clientes e veículos para referência
            GerenciadorArquivos<Cliente> gerClientes = new GerenciadorArquivos<>("clientes.json", Cliente.class);
            GerenciadorArquivos<Veiculo> gerVeiculos = new GerenciadorArquivos<>("veiculos.json", Veiculo.class);
            GerenciadorArquivos<Funcionario> gerFuncionarios = new GerenciadorArquivos<>("funcionarios.json", Funcionario.class);
            
            List<Cliente> clientes = gerClientes.carregar();
            List<Veiculo> veiculos = gerVeiculos.carregar();
            List<Funcionario> funcionarios = gerFuncionarios.carregar();
            
            if (!clientes.isEmpty() && !veiculos.isEmpty() && !funcionarios.isEmpty()) {
                Cliente cliente1 = clientes.get(0);
                Cliente cliente2 = clientes.size() > 1 ? clientes.get(1) : cliente1;
                Veiculo veiculo1 = veiculos.get(0);
                Veiculo veiculo2 = veiculos.size() > 1 ? veiculos.get(1) : veiculo1;
                Funcionario funcionario = funcionarios.get(0);
                
                // Aluguel 1 - Ativo
                Aluguel alu1 = new Aluguel();
                alu1.setId(GeradorID.gerarIDAluguel());
                alu1.setClienteId(cliente1.getId());
                alu1.setVeiculoId(veiculo1.getId());
                alu1.setFuncionarioId(funcionario.getId());
                alu1.setDataInicio(LocalDate.now().minusDays(3));
                alu1.setDataFimPrevista(LocalDate.now().plusDays(4));
                alu1.setValorDiaria(veiculo1.getValorDiaria());
                alu1.setQuilometragemInicial(veiculo1.getQuilometragem());
                alu1.setStatus(Aluguel.StatusAluguel.ATIVO);
                alugueis.add(alu1);
                
                // Atualizar status do veículo
                veiculo1.setStatus(Veiculo.Status.ALUGADO);
                
                // Aluguel 2 - Finalizado
                Aluguel alu2 = new Aluguel();
                alu2.setId(GeradorID.gerarIDAluguel());
                alu2.setClienteId(cliente2.getId());
                alu2.setVeiculoId(veiculo2.getId());
                alu2.setFuncionarioId(funcionario.getId());
                alu2.setDataInicio(LocalDate.now().minusDays(10));
                alu2.setDataFimPrevista(LocalDate.now().minusDays(3));
                alu2.setDataFimReal(LocalDate.now().minusDays(3));
                alu2.setValorDiaria(veiculo2.getValorDiaria());
                alu2.setQuilometragemInicial(veiculo2.getQuilometragem() - 500);
                alu2.setQuilometragemFinal(veiculo2.getQuilometragem());
                alu2.setStatus(Aluguel.StatusAluguel.FINALIZADO);
                alugueis.add(alu2);
                
                gerenciador.salvar(alugueis);
                gerVeiculos.salvar(veiculos);
                System.out.println("Aluguéis de exemplo criados.");
            }
        }
    }
}
