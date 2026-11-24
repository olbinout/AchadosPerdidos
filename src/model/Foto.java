package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import database.Banco;

public class Foto {

    private int id;
    private int idItem;
    private String caminhoArquivo;
    private LocalDateTime dataUpload;

    
    public Foto(int id, int idItem, String caminhoArquivo) {
        this.id = id;
        this.idItem = idItem;
        this.caminhoArquivo = caminhoArquivo;
        this.dataUpload = LocalDateTime.now();
    }

    public Foto(int id, int idItem, String caminhoArquivo, LocalDateTime dataUpload) {
        this.id = id;
        this.idItem = idItem;
        this.caminhoArquivo = caminhoArquivo;
        this.dataUpload = dataUpload;
    }

    public String getUrl(){
        return caminhoArquivo;
    }

    public int getId(){
        return id;
    }

    public int getIdItem() {
        return idItem;
    }

    public String getCaminhoArquivo(){
        return caminhoArquivo;
    }

    public LocalDateTime getDataUpload(){
        return dataUpload;
    }


    public boolean cadastrarFoto(Banco db) {
        String query = String.format(
                "INSERT INTO foto (idItem, caminhoArquivo, dataUpload) VALUES (%d, %s, %s)",
                idItem, Banco.texto(caminhoArquivo), Banco.texto(dataUpload.toString()));
        if (db.queryUpdate(query) > 0) {
            this.id = db.ultimoId();
            return true;
        }
        return false;
    }

    public static boolean removerFoto(Banco db, int id) {
        String query = String.format("DELETE FROM foto WHERE id = %d", id);
        return db.queryUpdate(query) > 0;
    }

    
    public static List<Foto> listarPorItem(Banco db, int idItem) throws SQLException {
        List<Foto> fotos = new ArrayList<>();
        ResultSet rs = db.querySearch(String.format("SELECT * FROM foto WHERE idItem = %d", idItem));
        if (rs == null) {
            return fotos;
        }
        while (rs.next()) {
            fotos.add(new Foto(rs.getInt("id"), rs.getInt("idItem"), rs.getString("caminhoArquivo"),
                    LocalDateTime.parse(rs.getString("dataUpload"))));
        }
        return fotos;
    }
}