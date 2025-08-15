========================================
    AutoFácil Java Edition v1.0
========================================

SISTEMA DE GERENCIAMENTO DE LOCADORA DE VEÍCULOS

📋 REQUISITOS:
- JDK 8 ou superior
- Windows (executável .bat incluído)

📁 ESTRUTURA:
AutoFacil/
├── src/          ← Código fonte Java
├── lib/          ← Bibliotecas (gson, itextpdf, jfreechart)
├── data/         ← Arquivos JSON (gerados automaticamente)
├── export/       ← PDFs exportados
├── icons/        ← Ícones PNG
├── bin/          ← Classes compiladas
└── executar.bat  ← Script de execução

🚀 EXECUÇÃO:
1. Execute: executar.bat
2. Login padrão: admin / admin

🔧 COMPILAÇÃO MANUAL:
javac -cp "lib/*" -d bin src/**/*.java src/*.java
java -cp "bin;lib/*" Main

👤 USUÁRIOS:
- Admin: admin / admin (acesso total)
- Funcionários: cadastrados pelo admin
- Clientes: acesso limitado aos próprios dados

📊 FUNCIONALIDADES:
✓ CRUD completo (Clientes, Veículos, Funcionários, Aluguéis)
✓ Relatórios com gráficos usando JFreeChart
✓ Exportação PDF com iText
✓ Validações de CPF, CEP, telefone, placa
✓ Tema claro/escuro
✓ Interface responsiva
✓ Sidebar deslizante animada
✓ Ícones profissionais em PNG

🎨 DESIGN:
- Cores padronizadas em todo sistema:
  • Azul escuro (#1F4E79) - Cabeçalhos, menus, botões principais
  • Azul claro (#DCE6F1) - Fundos, painéis secundários
  • Verde (#4CAF50) - Botões de confirmação
  • Vermelho - Botões de cancelamento/exclusão
- Layout homogêneo e responsivo
- Botões totalmente coloridos com texto branco
- Temas claro/escuro aplicados a todos os componentes

📊 GRÁFICOS:
O sistema inclui gráficos profissionais gerados com JFreeChart:
- Gráfico de pizza para status dos veículos
- Gráfico de barras para categorias de veículos
- Gráfico de barras para aluguéis por mês
- Gráfico de linha para receita por período
Todos os gráficos são interativos e se adaptam ao tema do sistema.

📄 RELATÓRIOS:
Relatórios disponíveis:
- Relatório de Clientes
- Relatório de Veículos
- Relatório de Aluguéis
- Relatório de Funcionários
Todos os relatórios podem ser exportados em PDF profissional.

💾 DADOS:
Os dados são salvos automaticamente em JSON na pasta /data/:
- data/clientes.json
- data/veiculos.json
- data/funcionarios.json
- data/alugueis.json
Dados de exemplo são gerados na primeira execução.

🔐 AUTENTICAÇÃO:
- Sistema seguro com controle de acesso por perfil
- 3 tentativas de login (após isso o sistema é encerrado)
- Senhas criptografadas

📱 RESPONSIVIDADE:
- Interface adaptável a diferentes resoluções
- Suporte a maximização e redimensionamento
- Componentes com tamanho proporcional
- GridBagLayout e BorderLayout usados corretamente

========================================
Desenvolvido em Java Swing
Persistência com Gson
Relatórios com iText
Gráficos com JFreeChart
========================================
