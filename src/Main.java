import model.*;

/** Demonstração das classes em memória (ainda sem banco de dados). */
public class Main {
    public static void main(String[] args) {
        Aluno aluno = new Aluno(1, "Maria", "maria@alunos.ufersa.edu.br", "123456", "84999999999", "2024010001", "Tecnologia da Informação");

        Funcionario seguranca = new Funcionario(2, "João", "joao@ufersa.edu.br", "654321", "84988888888", "Vigilante", "Segurança");

        aluno.cadastrar();
        seguranca.cadastrar();
        System.out.println("Login aluno: " + aluno.login("maria@alunos.ufersa.edu.br", "123456"));

        Local portaria = new Local(1, "Portaria do Bloco Central");
        // idFuncionario = 0: nenhum funcionário atendeu o item ainda
        Item item = new Item(10, aluno.getId(), 0, portaria.getId(), "Garrafa térmica", "Azul, com adesivo da UFERSA", StatusItem.PERDIDO);
        item.adicionarFoto("fotos/garrafa1.jpg");

        aluno.registrarItem(item);
        seguranca.atenderItem(item);
        item.setIdFuncionario(seguranca.getId());
        item.atualizarStatus(StatusItem.ACHADO);
        portaria.adicionarItem(item);

        System.out.println(item);
        System.out.println("Itens em " + portaria.getNome() + ": " + portaria.getQuantidadeAtual());
        item.atualizarStatus(StatusItem.DEVOLVIDO);
        portaria.removerItem(item.getId());
        System.out.println("Quantidade de itens na " + portaria.getNome() + " após a devolução: " + portaria.getQuantidadeAtual());
    }
}