import java.sql.SQLException;

import database.Banco;
import model.*;

public class Main {

    public static void main(String[] args) {
        Banco db = new Banco();
        criarTabelas(db);

        try {
            Aluno aluno = new Aluno("Otávio", "otavio@alunos.ufersa.edu.br", "123456", "84999999999", "2025010001", "Engenharia de Computação");

            Funcionario seguranca = new Funcionario("Ruan", "ruan@ufersa.edu.br", "654321", "84988888888", "Professor", "Segurança");

            Local portaria = new Local("Biblioteca");

            aluno.cadastrarAluno(db);
            seguranca.cadastrarFuncionario(db);
            portaria.cadastrarLocal(db);

            Item item = new Item(aluno.getId(), 0, portaria.getId(), "Garrafa térmica", "Azul, com adesivo da UFERSA", StatusItem.PERDIDO);
            item.cadastrarItem(db);
            item.adicionarFoto(db, "fotos/garrafa1.jpg");

            item.setIdFuncionario(seguranca.getId());
            item.atualizarStatus(db, StatusItem.ACHADO);

            System.out.println("--- Alunos ---");
            Aluno.listarAlunos(db);
            System.out.println();

            System.out.println("--- Funcionarios ---");
            Funcionario.listarFuncionarios(db);
            System.out.println();

            System.out.println("--- Locais ---");
            Local.listarLocais(db);
            System.out.println();

            System.out.println("--- Itens ---");
            Item.listarItens(db);

            Item doBanco = Item.buscarPorId(db, item.getId());
            System.out.println("\n" + doBanco);
            System.out.println("Itens achados em " + portaria.getNome() + ": " + Local.contarItensAchados(db, portaria.getId()));

            Aluno alunoDoBanco = Aluno.buscarPorId(db, aluno.getId());
            System.out.println("Login aluno: " + alunoDoBanco.login("maria@alunos.ufersa.edu.br", "123456"));

            item.atualizarStatus(db, StatusItem.DEVOLVIDO);
            System.out.println("Itens achados em " + portaria.getNome() + " apos a devolucao: " + Local.contarItensAchados(db, portaria.getId()));

        } catch (SQLException e) {
            System.out.println("Erro no banco: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Dados invalidos: " + e.getMessage());
        }

        db.desconectar();
    }

    static void criarTabelas(Banco db) {
        db.queryUpdate("CREATE TABLE IF NOT EXISTS usuario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "email TEXT NOT NULL, "
                + "senha TEXT NOT NULL, "
                + "telefone TEXT)");

        db.queryUpdate("CREATE TABLE IF NOT EXISTS aluno ("
                + "idAluno INTEGER PRIMARY KEY, "
                + "matricula TEXT, "
                + "curso TEXT, "
                + "FOREIGN KEY (idAluno) REFERENCES usuario(id) ON DELETE CASCADE)");

        db.queryUpdate("CREATE TABLE IF NOT EXISTS funcionario ("
                + "idFuncionario INTEGER PRIMARY KEY, "
                + "cargo TEXT, "
                + "setor TEXT, "
                + "FOREIGN KEY (idFuncionario) REFERENCES usuario(id) ON DELETE CASCADE)");

        db.queryUpdate("CREATE TABLE IF NOT EXISTS local ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL)");

        db.queryUpdate("CREATE TABLE IF NOT EXISTS item ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "idAluno INTEGER NOT NULL, "
                + "idFuncionario INTEGER, "
                + "idLocal INTEGER NOT NULL, "
                + "titulo TEXT NOT NULL, "
                + "descricao TEXT, "
                + "status TEXT NOT NULL, "
                + "dataRegistro TEXT NOT NULL, "
                + "FOREIGN KEY (idAluno) REFERENCES aluno(idAluno), "
                + "FOREIGN KEY (idFuncionario) REFERENCES funcionario(idFuncionario), "
                + "FOREIGN KEY (idLocal) REFERENCES local(id))");

        db.queryUpdate("CREATE TABLE IF NOT EXISTS foto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "idItem INTEGER NOT NULL, "
                + "caminhoArquivo TEXT NOT NULL, "
                + "dataUpload TEXT NOT NULL, "
                + "FOREIGN KEY (idItem) REFERENCES item(id))");
    }
}