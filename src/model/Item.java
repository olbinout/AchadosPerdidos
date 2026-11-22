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
    private String dataRegistro; // texto ISO (yyyy-MM-dd), compatível com SQLite
    private List <Foto> fotos = new ArrayList<>(); // composição: Item 1 possui 0..* Foto

    /** Novo item (o id é gerado pelo banco ao cadastrar; data de registro = hoje). */
    public Item(int idAluno, int idFuncionario, int idLocal, String titulo, String descricao, StatusItem status) {
        this.idAluno = idAluno;
        this.idFuncionario = idFuncionario;
        this.idLocal = idLocal;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataRegistro = LocalDate.now().toString();
    }

    /** Novo item (data de registro = hoje). */
    public Item(int id, int idAluno, int idFuncionario, int idLocal, String titulo, String descricao, StatusItem status) {
        this(idAluno, idFuncionario, idLocal, titulo, descricao, status);
        this.id = id;
    }

    /** Item que já existe (ex.: lido do banco), com a data de registro original. */
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

    /** Na composição, a Foto é criada e controlada pelo próprio Item. */
    public Foto adicionarFoto(String caminhoArquivo) {
        int idFoto = fotos.size() + 1; // provisório (só em memória)
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

    /** Preenchido quando um funcionário atende o item (0 = ainda sem funcionário). */
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

    // ---------- Banco de dados ----------

    // idFuncionario = 0 significa "sem funcionário": no banco isso é NULL
    // (um 0 quebraria a chave estrangeira, pois não existe funcionário com id 0)
    private String idFuncionarioSql() {
        return idFuncionario == 0 ? "NULL" : String.valueOf(idFuncionario);
    }

    public boolean cadastrarItem(Banco db) {
        String query = String.format(
                "INSERT INTO item (idAluno, idFuncionario, idLocal, titulo, descricao, status, dataRegistro) "
                + "VALUES (%d, %s, %d, %s, %s, %s, %s)",
                idAluno, idFuncionarioSql(), idLocal, Banco.texto(titulo), Banco.texto(descricao),
                Banco.texto(status.name()), Banco.texto(dataRegistro));
        if (db.queryUpdate(query) > 0) {
            this.id = db.ultimoId();
            return true;
        }
        return false;
    }

    public boolean editarItem(Banco db) {
        String query = String.format(
                "UPDATE item SET idFuncionario = %s, idLocal = %d, titulo = %s, descricao = %s, status = %s WHERE id = %d",
                idFuncionarioSql(), idLocal, Banco.texto(titulo), Banco.texto(descricao),
                Banco.texto(status.name()), id);
        return db.queryUpdate(query) > 0;
    }

    /** Muda o status na memória e já salva no banco. */
    public boolean atualizarStatus(Banco db, StatusItem novoStatus) {
        atualizarStatus(novoStatus);
        return editarItem(db);
    }

    /** Cria a foto, salva no banco e guarda na lista do item. Retorna null se falhar. */
    public Foto adicionarFoto(Banco db, String caminhoArquivo) {
        Foto foto = new Foto(0, this.id, caminhoArquivo);
        if (foto.cadastrarFoto(db)) {
            fotos.add(foto);
            return foto;
        }
        return null;
    }

    // As fotos do item precisam ser apagadas antes (chave estrangeira)
    public static boolean removerItem(Banco db, int id) {
        db.queryUpdate(String.format("DELETE FROM foto WHERE idItem = %d", id));
        return db.queryUpdate(String.format("DELETE FROM item WHERE id = %d", id)) > 0;
    }

    // Retorna o item (com suas fotos) ou null se não existir
    public static Item buscarPorId(Banco db, int id) throws SQLException {
        ResultSet rs = db.querySearch(String.format("SELECT * FROM item WHERE id = %d", id));
        if (rs == null || !rs.next()) {
            return null;
        }
        // Lê tudo do ResultSet antes de fazer outra consulta (a conexão reaproveita o mesmo Statement)
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
            System.out.println(rs.getInt("id") + " | " + rs.getString("titulo") + " | "
                    + rs.getString("status") + " | local " + rs.getInt("idLocal")
                    + " | " + rs.getString("dataRegistro"));
        }
    }
}