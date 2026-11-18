package model;

import java.time.LocalDateTime;

public class Foto {

    private int id;
    private int idItem;
    private String caminhoArquivo;
    private LocalDateTime dataUpload;

    /** Nova foto (data de upload = agora). */
    public Foto(int id, int idItem, String caminhoArquivo) {
        this.id = id;
        this.idItem = idItem;
        this.caminhoArquivo = caminhoArquivo;
        this.dataUpload = LocalDateTime.now();
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
}
