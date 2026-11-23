package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Banco;

public class Local {

    private int id;
    private String nome;
    private List <Item> itens = new ArrayList<>();
    private int quantidadeAtual;

    public Local(String nome) {
        this.nome = nome;
    }

    public Local(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public boolean adicionarItem(Item novoItem) {
        if (novoItem == null || itens.contains(novoItem)) {
            return false;
        }

        itens.add(novoItem);
        quantidadeAtual = itens.size();

        return true;
    }

    public boolean removerItem(int idItem) {
        boolean removido = itens.removeIf(i -> i.getId() == idItem);
        quantidadeAtual = itens.size();

        return removido;
    }

    public List<Item> listarItens() {
        return new ArrayList<>(itens);
    }

    public List<Item> listarItens(Banco db) throws SQLException {
        List<Item> lista = new ArrayList<>();
        ResultSet rs = db.querySearch(String.format("SELECT * FROM item WHERE idLocal = %d", id));
        if (rs == null) {
            return lista;
        }
        while (rs.next()) {
            lista.add(new Item(rs.getInt("id"), rs.getInt("idAluno"), rs.getInt("idFuncionario"),
                    rs.getInt("idLocal"), rs.getString("titulo"), rs.getString("descricao"),
                    StatusItem.valueOf(rs.getString("status")), rs.getString("dataRegistro")));
        }
        return lista;
    }

    public int getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public int getQuantidadeAtual(){
        return quantidadeAtual;
    }


    public boolean cadastrarLocal(Banco db) {
        String query = String.format("INSERT INTO local (nome) VALUES (%s)", Banco.texto(nome));
        if (db.queryUpdate(query) > 0) {
            this.id = db.ultimoId();
            return true;
        }
        return false;
    }

    public boolean editarLocal(Banco db) {
        String query = String.format("UPDATE local SET nome = %s WHERE id = %d", Banco.texto(nome), id);
        return db.queryUpdate(query) > 0;
    }

    public static boolean removerLocal(Banco db, int id) {
        String query = String.format("DELETE FROM local WHERE id = %d", id);
        return db.queryUpdate(query) > 0;
    }

    public static Local buscarPorId(Banco db, int id) throws SQLException {
        ResultSet rs = db.querySearch(String.format("SELECT * FROM local WHERE id = %d", id));
        if (rs != null && rs.next()) {
            return new Local(rs.getInt("id"), rs.getString("nome"));
        }
        return null;
    }

    public static void listarLocais(Banco db) throws SQLException {
        ResultSet rs = db.querySearch("SELECT * FROM local");
        if (rs == null) {
            return;
        }
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | " + rs.getString("nome"));
        }
    }

    public static int contarItensAchados(Banco db, int idLocal) throws SQLException {
        ResultSet rs = db.querySearch(String.format(
                "SELECT COUNT(*) AS total FROM item WHERE idLocal = %d AND status = 'ACHADO'", idLocal));
        if (rs != null && rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }
}