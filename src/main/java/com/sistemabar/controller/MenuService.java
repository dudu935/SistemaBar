package com.sistemabar.controller;

import java.io.IOException;
import java.util.Scanner;

import com.sistemabar.repository.EstoqueRepository;
import com.sistemabar.repository.ProdutoRepository;
import com.sistemabar.service.ProdutoService;
import com.sistemabar.model.*;

public class MenuService {
    private ProdutoService produtoService;
    private EstoqueRepository estoqueRepository;
    private ProdutoRepository produtoRepository;
    public MenuService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        produtoService = new ProdutoService(estoqueRepository, produtoRepository);
        this.produtoRepository = produtoRepository;
    }



    public void exibirMenu() {
        System.out.println(
            "----------menu---------\n" +
            "1.adicionar produto ao estoque\n" +
            "2.adicionar produto ao carrinho\n" +
            "3.excluir produto do carrinho\n" +
            "4.finalizar compra\n" +
            "5.Adicionar novo produto ao sistema\n" +
            "6.sair\n");
    }

    public int obterEscolhaUsuario() {
        Scanner scan = new Scanner(System.in);
        int escolha = scan.nextInt();
        return escolha;
    }

    public void escolha1() {
        produtoService.listarProdutos();
        Scanner scanner = new Scanner(System.in);  
        System.out.println("Digite o nome do produto que deseja adicionar ao estoque:");
        String nome = scanner.nextLine();
        System.out.println("Digite a quantidade do produto:");
        int quantidade = scanner.nextInt();
        produtoService.adicionarProdutoAoEstoque(produtoRepository.buscarProdutoPorNome(nome), quantidade);
    }

    public void escolha2() {
        produtoService.listarProdutos();
        Scanner scanner = new Scanner(System.in);  
        System.out.println("Digite o nome do produto que deseja adicionar ao carrinho:");
        String nome = scanner.nextLine();
        System.out.println("Digite a quantidade do produto:");
        int quantidade = scanner.nextInt();
        produtoService.adicionarProdutoAoCarrinho(produtoRepository.buscarProdutoPorNome(nome), quantidade);
        System.out.println(produtoService.getCarrinho().getProdutos());
        System.out.println(produtoService.getCarrinho().calcularTotal());
    }

    public void escolha3() {
        produtoService.mostrarCarrinho();
        System.out.println("Digite o nome do produto que deseja remover do carrinho:");
        Scanner scanner = new Scanner(System.in);
        String nome = scanner.nextLine();
        produtoService.getCarrinho().removerProduto(produtoService.getCarrinho().buscarProdutoPorNome(nome));
    }

    public void escolha4() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Valor total da compra: " 
        + produtoService.getCarrinho().calcularTotal());
        boolean pagamento = true;
        produtoService.comprarCarrinho(estoqueRepository, produtoService.getCarrinho(), pagamento);
    }

    public void escolha5() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do novo produto:");
        String nome = scanner.nextLine();
        System.out.println("Digite o preço do novo produto:");
        double preco = scanner.nextDouble();
        produtoService.criarProduto(nome, preco);
    }

    public void escolha6() {
        produtoService.mostrarCarrinho();
    }

}

