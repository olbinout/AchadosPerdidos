package menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import database.Banco;
import model.*;

public class Menu {

    private final Banco db;
    private final Scanner entrada = new Scanner(System.in);

    public Menu(Banco db) {
        this.db = db;
    }

    public void principal() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===== ACHADOS E PERDIDOS =====");
            System.out.println("1 - Alunos");
            System.out.println("2 - Funcionarios");
            System.out.println("3 - Locais");
            System.out.println("4 - Itens");
            System.out.println("0 - Sair");
            opcao = lerInt("Opcao: ");

            if (opcao == 1) {
                menuAlunos();
            } else if (opcao == 2) {
                menuFuncionarios();
            } else if (opcao == 3) {
                menuLocais();
            } else if (opcao == 4) {
                menuItens();
            } else if (opcao != 0) {
                System.out.println("Opcao invalida.");
            }
        }
        entrada.close();
    }



    private void menuAlunos() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- ALUNOS ---");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Listar alunos");
            System.out.println("3 - Editar aluno");
            System.out.println("4 - Remover aluno");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao: ");

            try {
                if (opcao == 1) {
                    String nome = lerNome();
                    String email = lerEmail(0);
                    String senha = lerSenha();
                    String telefone = lerTelefone();
                    String matricula = lerTexto("Matricula");
                    String curso = lerTexto("Curso");
                    if (new Aluno(nome, email, senha, telefone, matricula, curso).cadastrarAluno(db)) {
                        System.out.println("Aluno cadastrado.");
                    }
                } else if (opcao == 2) {
                    Aluno.listarAlunos(db);
                } else if (opcao == 3) {
                    int id = lerInt("ID do aluno: ");
                    Aluno aluno = Aluno.buscarPorId(db, id);
                    if (aluno == null) {
                        System.out.println("Aluno nao encontrado.");
                    } else {
                        System.out.println("Editando: " + aluno.getNome() + " | " + aluno.getEmail());
                        String nome = lerNome();
                        String email = lerEmail(id);
                        String senha = lerSenha();
                        String telefone = lerTelefone();
                        String matricula = lerTexto("Matricula");
                        String curso = lerTexto("Curso");
                        if (new Aluno(id, nome, email, senha, telefone, matricula, curso).editarAluno(db)) {
                            System.out.println("Aluno atualizado.");
                        }
                    }
                } else if (opcao == 4) {
                    int id = lerInt("ID do aluno: ");
                    if (Aluno.buscarPorId(db, id) == null) {
                        System.out.println("Aluno nao encontrado.");
                    } else if (Aluno.removerAluno(db, id)) {
                        System.out.println("Aluno removido.");
                    } else {
                        System.out.println("Nao foi possivel remover. O aluno tem itens cadastrados?");
                    }
                } else if (opcao != 0) {
                    System.out.println("Opcao invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Dados invalidos: " + e.getMessage());
            }
        }
    }



    private void menuFuncionarios() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- FUNCIONARIOS ---");
            System.out.println("1 - Cadastrar funcionario");
            System.out.println("2 - Listar funcionarios");
            System.out.println("3 - Editar funcionario");
            System.out.println("4 - Remover funcionario");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao: ");

            try {
                if (opcao == 1) {
                    String nome = lerNome();
                    String email = lerEmail(0);
                    String senha = lerSenha();
                    String telefone = lerTelefone();
                    String cargo = lerTexto("Cargo");
                    String setor = lerTexto("Setor");
                    if (new Funcionario(nome, email, senha, telefone, cargo, setor).cadastrarFuncionario(db)) {
                        System.out.println("Funcionario cadastrado.");
                    }
                } else if (opcao == 2) {
                    Funcionario.listarFuncionarios(db);
                } else if (opcao == 3) {
                    int id = lerInt("ID do funcionario: ");
                    Funcionario funcionario = Funcionario.buscarPorId(db, id);
                    if (funcionario == null) {
                        System.out.println("Funcionario nao encontrado.");
                    } else {
                        System.out.println("Editando: " + funcionario.getNome() + " | " + funcionario.getEmail());
                        String nome = lerNome();
                        String email = lerEmail(id);
                        String senha = lerSenha();
                        String telefone = lerTelefone();
                        String cargo = lerTexto("Cargo");
                        String setor = lerTexto("Setor");
                        if (new Funcionario(id, nome, email, senha, telefone, cargo, setor).editarFuncionario(db)) {
                            System.out.println("Funcionario atualizado.");
                        }
                    }
                } else if (opcao == 4) {
                    int id = lerInt("ID do funcionario: ");
                    if (Funcionario.buscarPorId(db, id) == null) {
                        System.out.println("Funcionario nao encontrado.");
                    } else if (Funcionario.removerFuncionario(db, id)) {
                        System.out.println("Funcionario removido.");
                    } else {
                        System.out.println("Nao foi possivel remover. O funcionario atendeu itens?");
                    }
                } else if (opcao != 0) {
                    System.out.println("Opcao invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Dados invalidos: " + e.getMessage());
            }
        }
    }



    private void menuLocais() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- LOCAIS ---");
            System.out.println("1 - Cadastrar local");
            System.out.println("2 - Listar locais");
            System.out.println("3 - Editar local");
            System.out.println("4 - Remover local");
            System.out.println("5 - Ver itens de um local");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao: ");

            try {
                if (opcao == 1) {
                    String nome = lerTexto("Nome do local");
                    if (new Local(nome).cadastrarLocal(db)) {
                        System.out.println("Local cadastrado.");
                    }
                } else if (opcao == 2) {
                    Local.listarLocais(db);
                } else if (opcao == 3) {
                    int id = lerInt("ID do local: ");
                    Local local = Local.buscarPorId(db, id);
                    if (local == null) {
                        System.out.println("Local nao encontrado.");
                    } else {
                        System.out.println("Editando: " + local.getNome());
                        String nome = lerTexto("Novo nome do local");
                        if (new Local(id, nome).editarLocal(db)) {
                            System.out.println("Local atualizado.");
                        }
                    }
                } else if (opcao == 4) {
                    int id = lerInt("ID do local: ");
                    if (Local.buscarPorId(db, id) == null) {
                        System.out.println("Local nao encontrado.");
                    } else if (Local.removerLocal(db, id)) {
                        System.out.println("Local removido.");
                    } else {
                        System.out.println("Nao foi possivel remover. Ha itens neste local?");
                    }
                } else if (opcao == 5) {
                    int id = lerInt("ID do local: ");
                    Local local = Local.buscarPorId(db, id);
                    if (local == null) {
                        System.out.println("Local nao encontrado.");
                    } else {
                        List<Item> itens = local.listarItens(db);
                        System.out.println("Itens em " + local.getNome() + ":");
                        if (itens.isEmpty()) {
                            System.out.println("  Nenhum item neste local.");
                        }
                        for (Item item : itens) {
                            System.out.println("  " + item.getId() + " | " + item.getTitulo() + " | "
                                    + item.getStatus() + " | " + item.getDataRegistro());
                        }
                        System.out.println("Quantidade de itens ACHADOS que estão no " + local.getNome() + ": " + Local.contarItensAchados(db, id));
                    }
                } else if (opcao != 0) {
                    System.out.println("Opcao invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco: " + e.getMessage());
            }
        }
    }


    private void menuItens() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- ITENS ---");
            System.out.println("1 - Cadastrar item");
            System.out.println("2 - Listar itens");
            System.out.println("3 - Editar item");
            System.out.println("4 - Remover item");
            System.out.println("5 - Ver detalhes do item (com fotos)");
            System.out.println("6 - Adicionar foto a um item");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao: ");

            try {
                if (opcao == 1) {
                    int idAluno = lerIdAluno();
                    if (idAluno == 0) {
                        continue;
                    }
                    String titulo = lerTexto("Titulo");
                    String descricao = lerTexto("Descricao");
                    StatusItem status = lerStatus();
                    int idLocal = 0;
                    if (status == StatusItem.ACHADO) {
                        idLocal = lerIdLocal();
                        if (idLocal == 0) {
                            continue;
                        }
                    }
                    int idFuncionario = lerIdFuncionario();

                    Item item = new Item(idAluno, idFuncionario, idLocal, titulo, descricao, status);
                    if (item.cadastrarItem(db)) {
                        System.out.println("Item cadastrado (ID " + item.getId() + ").");
                        System.out.print("Caminho da foto (Enter para pular): ");
                        String caminho = entrada.nextLine().trim();
                        if (!caminho.isEmpty() && item.adicionarFoto(db, caminho) != null) {
                            System.out.println("Foto adicionada.");
                        }
                    }
                } else if (opcao == 2) {
                    Item.listarItens(db);
                } else if (opcao == 3) {
                    int id = lerInt("ID do item: ");
                    Item item = Item.buscarPorId(db, id);
                    if (item == null) {
                        System.out.println("Item nao encontrado.");
                    } else {
                        System.out.println("Editando: " + item.getTitulo() + " | " + item.getStatus());
                        String titulo = lerTexto("Novo titulo");
                        String descricao = lerTexto("Nova descricao");
                        StatusItem status = lerStatus();
                        int idLocal = 0;
                        if (status == StatusItem.ACHADO) {
                            idLocal = lerIdLocal();
                            if (idLocal == 0) {
                                continue;
                            }
                        }
                        int idFuncionario = lerIdFuncionario();

                        Item atualizado = new Item(id, item.getIdAluno(), idFuncionario, idLocal, titulo, descricao, status);
                        if (atualizado.editarItem(db)) {
                            System.out.println("Item atualizado.");
                        }
                    }
                } else if (opcao == 4) {
                    int id = lerInt("ID do item: ");
                    if (Item.buscarPorId(db, id) == null) {
                        System.out.println("Item nao encontrado.");
                    } else if (Item.removerItem(db, id)) {
                        System.out.println("Item removido.");
                    } else {
                        System.out.println("Nao foi possivel remover o item.");
                    }
                } else if (opcao == 5) {
                    int id = lerInt("ID do item: ");
                    Item item = Item.buscarPorId(db, id);
                    if (item == null) {
                        System.out.println("Item nao encontrado.");
                    } else {
                        System.out.println(item);
                        System.out.println(" * Aluno (ID) = " + item.getIdAluno()
                                + " | Local (ID) = " + (item.getIdLocal() == 0 ? "nenhum" : item.getIdLocal())
                                + " | Funcionario (ID) = " + (item.getIdFuncionario() == 0 ? "nenhum" : item.getIdFuncionario()));
                        for (Foto foto : item.getFotos()) {
                            System.out.println("   foto " + foto.getId() + ": " + foto.getCaminhoArquivo());
                        }
                    }
                } else if (opcao == 6) {
                    int id = lerInt("ID do item: ");
                    Item item = Item.buscarPorId(db, id);
                    if (item == null) {
                        System.out.println("Item nao encontrado.");
                    } else {
                        String caminho = lerTexto("Caminho da foto");
                        if (item.adicionarFoto(db, caminho) != null) {
                            System.out.println("Foto adicionada.");
                        }
                    }
                } else if (opcao != 0) {
                    System.out.println("Opcao invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Dados invalidos: " + e.getMessage());
            }
        }
    }


    private int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
            }
        }
    }

    private String lerTexto(String campo) {
        System.out.print(campo + ": ");
        String texto = entrada.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.println(campo + " e obrigatorio.");
            System.out.print(campo + ": ");
            texto = entrada.nextLine().trim();
        }
        return texto;
    }

    private static boolean nomeValido(String nome) {
        return nome.matches("[\\p{L} ]{3,}");
    }

    private static boolean emailValido(String email) {
        return email.matches("[\\w.-]+@[\\w-]+(\\.[\\w-]+)+");
    }

    private static boolean telefoneValido(String telefone) {
        return telefone.matches("\\d{10,11}");
    }

    private String lerNome() {
        System.out.print("Nome: ");
        String nome = entrada.nextLine().trim();
        while (!nomeValido(nome)) {
            System.out.println("Nome invalido. Use apenas letras (minimo 3).");
            System.out.print("Nome: ");
            nome = entrada.nextLine().trim();
        }
        return nome;
    }

    private String lerEmail(int idProprio) throws SQLException {
        while (true) {
            System.out.print("Email: ");
            String email = entrada.nextLine().trim();
            if (!emailValido(email)) {
                System.out.println("Email invalido. Exemplo: nome@email.com");
            } else if (emailEmUso(email, idProprio)) {
                System.out.println("Este email ja esta cadastrado.");
            } else {
                return email;
            }
        }
    }

    private boolean emailEmUso(String email, int idProprio) throws SQLException {
        Aluno aluno = Aluno.buscarPorEmail(db, email);
        if (aluno != null && aluno.getId() != idProprio) {
            return true;
        }
        Funcionario funcionario = Funcionario.buscarPorEmail(db, email);
        return funcionario != null && funcionario.getId() != idProprio;
    }

    private String lerSenha() {
        System.out.print("Senha: ");
        String senha = entrada.nextLine();
        while (senha.length() < 4) {
            System.out.println("Senha invalida. Minimo de 4 caracteres.");
            System.out.print("Senha: ");
            senha = entrada.nextLine();
        }
        return senha;
    }

    private String lerTelefone() {
        System.out.print("Telefone (so numeros, com DDD): ");
        String telefone = entrada.nextLine().trim();
        while (!telefoneValido(telefone)) {
            System.out.println("Telefone invalido. Exemplo: 84999999999");
            System.out.print("Telefone (so numeros, com DDD): ");
            telefone = entrada.nextLine().trim();
        }
        return telefone;
    }

    private StatusItem lerStatus() {
        StatusItem[] opcoes = StatusItem.values();
        while (true) {
            System.out.println("Status:");
            for (int i = 0; i < opcoes.length; i++) {
                System.out.println("  " + (i + 1) + " - " + opcoes[i]);
            }
            int escolha = lerInt("Opcao: ");
            if (escolha >= 1 && escolha <= opcoes.length) {
                return opcoes[escolha - 1];
            }
            System.out.println("Opcao invalida.");
        }
    }

    private int lerIdAluno() throws SQLException {
        while (true) {
            int id = lerInt("ID do aluno que registrou o item (0 para cancelar): ");
            if (id == 0 || Aluno.buscarPorId(db, id) != null) {
                return id;
            }
            System.out.println("Aluno nao encontrado.");
        }
    }

    private int lerIdLocal() throws SQLException {
        while (true) {
            int id = lerInt("ID do local onde o item esta guardado (0 para cancelar): ");
            if (id == 0 || Local.buscarPorId(db, id) != null) {
                return id;
            }
            System.out.println("Local nao encontrado.");
        }
    }

    private int lerIdFuncionario() throws SQLException {
        while (true) {
            int id = lerInt("ID do funcionario que atendeu (0 se nenhum): ");
            if (id == 0 || Funcionario.buscarPorId(db, id) != null) {
                return id;
            }
            System.out.println("Funcionario nao encontrado.");
        }
    }
}