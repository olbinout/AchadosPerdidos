package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.Banco;

public class Item {

    private int id;
    private int idAluno;
    private int idFuncionario;
    private int idLocal;
    private String titulo;
    private String descricao;
    private StatusItem status;
    private String dataRegistro; 
    private List <Foto> fotos = new ArrayList<>(); 

    public Item(int idAluno, int idFuncionario, int idLocal, String titulo, String descricao, StatusItem status) {
        this.idAluno = idAluno;
        this.idFuncionario = idFuncionario;
        this.idLocal = idLocal;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataRegistro = LocalDate.now().toString();
    }

    public Item(int id, int idAluno, int idFuncionario, int idLocal, String titulo, String descricao, StatusItem status) {
        this(idAluno, idFuncionario, idLocal, titulo, descricao, status);
        this.id = id;
    }

    public Item(int id, int idAluno, int idFuncionario, int idLocal, String titulo, String descricao, StatusItem status, String dataRegistro) {
        this(id, idAluno, idFuncionario, idLocal, titulo, descricao, status);
        this.dataRegistro = dataRegistro;
    }

    public void atualizarStatus(StatusItem novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
        this.status = novoStatus;
    }

    public void editarDetalhes(String novoTitulo, String novaDescricao) {
        this.titulo = novoTitulo;
        this.descricao = novaDescricao;
    }

    
    public Foto adicionarFoto(String caminhoArquivo) {
        int idFoto = fotos.size() + 1; 
        Foto foto = new Foto(idFoto, this.id, caminhoArquivo);
        fotos.add(foto);
        return foto;
    }

    public List <Foto> getFotos(){
        return Collections.unmodifiableList(fotos);
    }

    public int getId(){
        return id;
    }

    public int getIdAluno(){
        return idAluno;
    }

    public int getIdFuncionario(){
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario){
        this.idFuncionario = idFuncionario;
    }

    public int getIdLocal(){
        return idLocal;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getDescricao(){
        return descricao;
    }

    public StatusItem getStatus(){
        return status;
    }

    public String getDataRegistro(){
        return dataRegistro;
    }

    @Override
    public String toString() {
        return "Informações do Item: \n * ID = " + id + " \n * Titulo = " + titulo + "\n * Status = " + status + "\n * Data de Registro = " + dataRegistro + "\n * Fotos = " + fotos.size() + "\n * Descrição: " + getDescricao();
    }


    private String idFuncionarioSql() {
        return idFuncionario == 0 ? "NULL" : String.valueOf(idFuncionario);
    }

    // idLocal = 0 significa "sem local" (item perdido ou ja devolvido): no banco isso e NULL
    private String idLocalSql() {
        return idLocal == 0 ? "NULL" : String.valueOf(idLocal);
    }

    public boolean cadastrarItem(Banco db) {
        String query = String.format(
                "INSERT INTO item (idAluno, idFuncionario, idLocal, titulo, descricao, status, dataRegistro) "
                + "VALUES (%d, %s, %s, %s, %s, %s, %s)",
                idAluno, idFuncionarioSql(), idLocalSql(), Banco.texto(titulo), Banco.texto(descricao),
                Banco.texto(status.name()), Banco.texto(dataRegistro));
        if (db.queryUpdate(query) > 0) {
            this.id = db.ultimoId();
            return true;
        }
        return false;
    }

    public boolean editarItem(Banco db) {
        String query = String.format(
                "UPDATE item SET idFuncionario = %s, idLocal = %s, titulo = %s, descricao = %s, status = %s WHERE id = %d",
                idFuncionarioSql(), idLocalSql(), Banco.texto(titulo), Banco.texto(descricao),
                Banco.texto(status.name()), id);
        return db.queryUpdate(query) > 0;
    }

    public boolean atualizarStatus(Banco db, StatusItem novoStatus) {
        atualizarStatus(novoStatus);
        return editarItem(db);
    }

    public Foto adicionarFoto(Banco db, String caminhoArquivo) {
        Foto foto = new Foto(0, this.id, caminhoArquivo);
        if (foto.cadastrarFoto(db)) {
            fotos.add(foto);
            return foto;
        }
        return null;
    }

    public static boolean removerItem(Banco db, int id) {
        db.queryUpdate(String.format("DELETE FROM foto WHERE idItem = %d", id));
        return db.queryUpdate(String.format("DELETE FROM item WHERE id = %d", id)) > 0;
    }

    public static Item buscarPorId(Banco db, int id) throws SQLException {
        ResultSet rs = db.querySearch(String.format("SELECT * FROM item WHERE id = %d", id));
        if (rs == null || !rs.next()) {
            return null;
        }

        Item item = new Item(rs.getInt("id"), rs.getInt("idAluno"), rs.getInt("idFuncionario"),
                rs.getInt("idLocal"), rs.getString("titulo"), rs.getString("descricao"),
                StatusItem.valueOf(rs.getString("status")), rs.getString("dataRegistro"));
        item.fotos.addAll(Foto.listarPorItem(db, item.id));
        return item;
    }

    public static void listarItens(Banco db) throws SQLException {
        ResultSet rs = db.querySearch("SELECT * FROM item");
        if (rs == null) {
            return;
        }
        while (rs.next()) {
            int idLocal = rs.getInt("idLocal"); // NULL no banco vira 0
            System.out.println(rs.getInt("id") + " | " + rs.getString("titulo") + " | "
                    + rs.getString("status") + " | " + (idLocal == 0 ? "sem local" : "local " + idLocal)
                    + " | " + rs.getString("dataRegistro"));
        }
    }
}