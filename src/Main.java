import database.Banco;
import menu.Menu;

public class Main {

    public static void main(String[] args) {
        Banco db = new Banco();
        Menu menu = new Menu(db);

        if (menu.login()) {
            menu.principal();
        }

        db.desconectar();
    }
}