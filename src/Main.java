import database.Banco;
import menu.Menu;

public class Main {

    public static void main(String[] args) {
        Banco db = new Banco();
        criarTabelas(db);

        new Menu(db).principal();

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
                + "idLocal INTEGER, "
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