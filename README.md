# Template de Plugin para Minecraft 1.20.1

Este é um template completo para desenvolvimento rápido de plugins para servidores Minecraft 1.20.1 usando Spigot/Paper API.

## Características

- Estrutura Maven pronta para uso
- Configuração Java 17
- Exemplos didáticos para desenvolvedores iniciantes:
  - Comandos simples e avançados com autocompletar
  - Interfaces gráficas (GUIs) e menus interativos
  - Sistema de eventos completo
  - Utilitários para tarefas comuns
  - Gerenciamento de configurações e dados de jogadores
- Documentação detalhada com comentários
- Compatível com Minecraft 1.20.1

## Requisitos

- Java 17 ou superior
- Maven
- IDE compatível com Java (recomendado: IntelliJ IDEA ou Eclipse)

## Como Usar

### 1. Clone o repositório

```bash
git clone https://github.com/SEU_USUARIO/minecraft-plugin-template.git
cd minecraft-plugin-template
```

### 2. Personalize o template

1. Edite o arquivo `pom.xml` para definir:
   - `groupId`: Seu domínio invertido (ex: com.seudominio)
   - `artifactId`: Nome do seu plugin em minúsculo (ex: meuplugin)
   - `version`: Versão inicial (ex: 1.0-SNAPSHOT)
   - `plugin.name`: Nome do plugin
   - `plugin.description`: Descrição do plugin

2. Renomeie o pacote e as classes:
   - Renomeie o pacote `com.example.minecraft` para seu pacote personalizado
   - Renomeie a classe `ExamplePlugin` para o nome do seu plugin
   - Atualize as referências em todas as classes

3. Edite o arquivo `plugin.yml` para definir:
   - `name`: Nome do plugin
   - `main`: Caminho completo para sua classe principal
   - `commands`: Comandos personalizados do seu plugin

### 3. Compile o plugin

```bash
mvn clean package
```

O arquivo JAR compilado estará disponível na pasta `target/`.

### 4. Instale o plugin

Coloque o arquivo JAR na pasta `plugins` do seu servidor Minecraft e reinicie o servidor.

## Estrutura do Projeto

```
src/main/java/com/example/minecraft/
├── ExamplePlugin.java       # Classe principal do plugin
├── ExampleCommand.java      # Exemplo de comando simples
├── commands/
│   └── AdvancedCommand.java # Exemplo de comando avançado com subcomandos
├── events/
│   └── EventsManager.java   # Gerenciador de eventos do plugin
├── gui/
│   └── MenuManager.java     # Sistema de menus e interfaces gráficas
└── utils/
    └── PluginUtils.java     # Utilitários para tarefas comuns

src/main/resources/
├── plugin.yml               # Configuração do plugin
└── config.yml               # Arquivo de configuração padrão
```

## Exemplos Incluídos

### Comandos
O template inclui exemplos de:
- Comando simples (`/example`) - Demonstra comandos básicos
- Comando avançado (`/advanced`) - Demonstra subcomandos, autocompletar e armazenamento de dados

### Interfaces Gráficas
O template inclui um sistema completo de menus com:
- Menu principal com navegação
- Submenus interativos
- Sistema de registro de cliques
- Criação de itens personalizados

### Eventos
O template inclui exemplos de manipulação de eventos:
- Eventos de jogador (entrada, saída)
- Eventos de interação (cliques, quebra de blocos)
- Eventos de combate
- Armazenamento e carregamento de dados de jogadores

### Utilitários
O template inclui métodos utilitários para:
- Criação de itens personalizados
- Efeitos de partículas e sons
- Verificação de permissões
- Formatação de mensagens
- Manipulação de localização

## Personalização Avançada

### Adicionando Dependências

Para adicionar dependências ao seu plugin, edite a seção `dependencies` no arquivo `pom.xml`:

```xml
<dependencies>
    <!-- Dependência do Spigot API -->
    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot-api</artifactId>
        <version>1.20.1-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- Adicione suas dependências aqui -->
    <dependency>
        <groupId>com.exemplo</groupId>
        <artifactId>biblioteca</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

### Configurando GitHub Actions

O template inclui um workflow do GitHub Actions para compilação automática. Para ativá-lo:

1. Crie uma pasta `.github/workflows` no seu repositório
2. Adicione um arquivo `maven.yml` com o seguinte conteúdo:

```yaml
name: Java CI with Maven

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main, master ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
        
    - name: Build with Maven
      run: mvn -B package --file pom.xml
      
    - name: Upload artifact
      uses: actions/upload-artifact@v4
      with:
        name: Plugin
        path: target/*.jar
```

## Licença

Este template é distribuído sob a licença MIT. Sinta-se livre para usar, modificar e distribuir conforme necessário.

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests com melhorias para este template.
