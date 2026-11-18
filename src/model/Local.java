package model;

import java.util.ArrayList;
import java.util.List;

public class Local {

    private int id; // acrescentado para a futura chave primária no banco
    private String nome;
    private List <Item> itens = new ArrayList<>(); // agregação: Local 1 guarda 0..* Item
    private int quantidadeAtual;

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

    public int getId(){
        return id; 
    }

    public String getNome(){
        return nome; 
    }

    public int getQuantidadeAtual(){ 
        return quantidadeAtual; 
    }
}
