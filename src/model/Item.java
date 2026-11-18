package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /** Novo item (data de registro = hoje). */
    public Item(int id, int idAluno, int idFuncionario, int idLocal, String titulo, String descricao, StatusItem status) {
        this.id = id;
        this.idAluno = idAluno;
        this.idFuncionario = idFuncionario;
        this.idLocal = idLocal;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataRegistro = LocalDate.now().toString();
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
        int idFoto = fotos.size() + 1; // provisório, até existir banco de dados
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
}