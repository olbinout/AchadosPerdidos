# Sistema de Achados e Perdidos

Sistema de gerenciamento de **Achados e Perdidos** voltado para o ambiente universitário, desenvolvido em **Java** com interface de linha de comando (CLI) e banco de dados em **SQLite** (via JDBC). O projeto foi construído como atividade prática da disciplina de Programação Orientada a Objetos (POO), ministrada pelo docente Ruan Pabllo Barbosa Claudino.

# Integrantes

* Breno Alves de Almeida

* Francisco Jonathan Aquino Souza

* Luan Jefferson Lopes

* Maria Raquel Nunes Silva

* Otávio Linhares Batista

## Sobre o Projeto

O sistema permite o controle centralizado de objetos perdidos, encontrados e devolvidos dentro da UFERSA. Por meio de menus interativos no terminal, é possível gerenciar alunos, funcionários, locais de armazenamento, itens registrados e fotos associadas a cada ocorrência, mantendo integridade referencial e validações de entrada.

## Conceitos de POO Aplicados

A modelagem do sistema foi estruturada aplicando os principais pilares e relacionamentos da Orientação a Objetos:

* **Abstração e Herança:** A classe abstrata `Usuario` concentra os atributos e comportamentos comuns (`id`, `nome`, `email`, `senha`, `telefone`), sendo especializada pelas subclasses `Aluno` (`matricula`, `curso`) e `Funcionario` (`cargo`, `setor`).

* **Encapsulamento:** Atributos privados acessados e manipulados de forma controlada por getters, setters e métodos específicos de regra de negócio.

* **Enumeração (`enum`):** Uso do `StatusItem` para restringir o ciclo de vida de um item aos estados válidos: `PERDIDO`, `ACHADO` e `DEVOLVIDO`.

* **Associação:** Relacionamento entre `Aluno` (quem registra o item) e `Item`, bem como entre `Funcionario` (quem atende a ocorrência) e `Item`.

* **Agregação:** `Local` guarda múltiplos objetos do tipo `Item` (`1` para `0..*`), permitindo consultar e contabilizar itens armazenados em cada setor.

* **Composição:** `Item` possui múltiplas instâncias de `Foto` (`1` para `0..*`), vinculando o ciclo de vida das imagens ao registro do objeto.

## Funcionalidades

### Gestão de Alunos

* Cadastrar, listar, editar e remover alunos.

* Validação de nome, formato e unicidade de e-mail, senha e telefone com DDD.

### Gestão de Funcionários

* Cadastrar, listar, editar e remover funcionários (com cargo e setor).

* Validação de dados e verificação de e-mail único entre todos os usuários.

### Gestão de Locais

* Cadastrar, listar, editar e remover pontos de guarda/setores (ex.: Biblioteca, Administrativo, Cantina).

* Listar todos os itens vinculados a um local específico e exibir a contagem de itens com status `ACHADO` guardados naquele local.

### Gestão de Itens e Fotos

* Cadastrar, listar, editar e remover itens.

* Controle de status (`PERDIDO`, `ACHADO`, `DEVOLVIDO`).

* Regra de vínculo obrigatório de um **Local** cadastrado quando o item está com status `ACHADO`.

* Associação opcional de um **Funcionário** responsável pelo atendimento.

* Visualização detalhada de um item (incluindo IDs relacionados e lista de fotos).

* Adição de múltiplas fotos a um item no momento do cadastro ou posteriormente.

## Diagrama de Classes

A arquitetura das entidades do pacote `model` e seus relacionamentos está representada abaixo:

DiagramaClasseAchadosPerdidos.drawio.png

(visualização pelo site <https://app.diagrams.net>)

## Banco de Dados

A persistência é realizada localmente com **SQLite**, no arquivo `achados_perdidos.db`, criado automaticamente na primeira execução pela classe `database.Banco` com suporte a chaves estrangeiras habilitado (`PRAGMA foreign_keys = ON`).

### Tabelas criadas:

* **`usuario`**: Armazena os dados base de usuários (`id`, `nome`, `email`, `senha`, `telefone`).

* **`aluno`**: Especialização de usuário (`idAluno` com `ON DELETE CASCADE`, `matricula`, `curso`).

* **`funcionario`**: Especialização de usuário (`idFuncionario` com `ON DELETE CASCADE`, `cargo`, `setor`).

* **`local`**: Locais físicos de armazenamento (`id`, `nome`).

* **`item`**: Registros de achados e perdidos (`id`, `idAluno`, `idFuncionario`, `idLocal`, `titulo`, `descricao`, `status`, `dataRegistro`).

* **`foto`**: Imagens vinculadas aos itens (`id`, `idItem`, `caminhoArquivo`, `dataUpload`).

## Estrutura do Projeto

```
├── src/
│   ├── Main.java               # Ponto de entrada da aplicação
│   ├── database/
│   │   └── Banco.java          # Conexão SQLite, criação de tabelas e execução de queries
│   ├── menu/
│   │   └── Menu.java           # Interface de terminal (CLI) e validações de entrada
│   └── model/
│       ├── Usuario.java        # Classe abstrata base
│       ├── Aluno.java          # Subclasse de Usuario
│       ├── Funcionario.java    # Subclasse de Usuario
│       ├── Local.java          # Entidade de local de armazenamento
│       ├── Item.java           # Entidade principal de achados e perdidos
│       ├── Foto.java           # Entidade de fotos do item
│       └── StatusItem.java     # Enumeração de status (PERDIDO, ACHADO, DEVOLVIDO)
├── lib/
│   └── sqlite-jdbc-*.jar       # Driver JDBC do SQLite (necessário para execução)
├── DiagramaClasseAchadosPerdidos.drawio
├── DiagramaClasseAchadosPerdidos.drawio.png
├── .gitignore
└── README.md

```

## Ferramentas Utilizadas

* **Java (JDK 17+)**

* **SQLite 3**

* **SQLite JDBC Driver** (`org.xerial:sqlite-jdbc`)

* **Draw.io** (Modelagem UML)

## Como Executar o Projeto

### Pré-requisitos

* **Java JDK** instalado e configurado no `PATH` (verifique com `java -version` e `javac -version`).

* Driver **SQLite JDBC** (`.jar`) disponível na pasta `lib/` (você pode baixá-lo no repositório oficial do [xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc/releases)).

### Passo a passo via Terminal

1. **Clone o repositório:**

   ```
   git clone https://github.com/'SEU-USUARIO'/'SEU-REPOSITORIO'.git
   cd 'SEU-REPOSITORIO'
   
   ```

2. **Compile os arquivos Java:**

   ```
   mkdir -p bin
   javac -d bin src/*.java src/database/*.java src/menu/*.java src/model/*.java
   
   ```

3. **Execute a aplicação incluindo o driver JDBC no classpath:**

   * **No Linux / macOS:**

     ```
     java -cp "bin:lib/*" Main
     
     ```

   * **No Windows (PowerShell / CMD):**

     ```
     java -cp "bin;lib/*" Main
     
     ```

> Ao iniciar o programa, o arquivo `achados_perdidos.db` será criado automaticamente na raiz do projeto caso ainda não exista.
