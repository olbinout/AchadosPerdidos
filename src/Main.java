import database.Banco;
import menu.Menu;

public class Main {

    public static void main(String[] args) {
        Banco db = new Banco();

        new Menu(db).principal();

        db.desconectar();
    }
}