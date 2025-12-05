package com.sistemabar.service;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.sistemabar.model.Carrinho;
import com.sistemabar.model.Estoque;
import com.sistemabar.model.Produto;
import com.sistemabar.model.ProdutoCarrinho;
import com.sistemabar.repository.EstoqueRepository;
import com.sistemabar.repository.ProdutoRepository;

public class ProdutoService {
    EstoqueRepository estoqueRepository;
    ProdutoRepository produtoRepository;
    private Carrinho carrinho = new Carrinho();

    public ProdutoService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    public Carrinho getCarrinho() {
        return carrinho;
        
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
        
    }

    public void criarProduto(String nome, double preco) {
        Produto produto = new Produto(nome, preco);
        produtoRepository.salvarProduto(produto);
        System.out.println("Produto criado: " + produto.getNome() + " - Preço: " + produto.getPreco());
    }

    public boolean removerProduto(String nome) {
        if(produtoRepository.removerProduto(produtoRepository.buscarProdutoPorNome(nome)) == true) {
            return true;
        }else {
            return false;
        }
        }
        
    public boolean atualizarPreço(String nome, double preço) {
        if (produtoRepository.atualizarProduto(nome, preço)) {
            return true;
        }else {
            return false;
        }
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.carregarProdutos();
        
    }

    public void adicionarProdutoAoCarrinho(Produto produto, int quantidade) {
        ProdutoCarrinho produtoCarrinho = new ProdutoCarrinho(produto, quantidade);
        getCarrinho().adicionarProduto(produtoCarrinho);
        System.out.println("Produto adicionado ao carrinho: " + produto.getNome());
    }   

    public void adicionarProdutoAoEstoque(Produto produto, int quantidade) {
        System.out.println(produtoRepository.carregarProdutos());
        Scanner scanner = new Scanner(System.in);
        Estoque estoque = new Estoque(produto, quantidade);
        estoqueRepository.salvarEstoque(estoque);
        System.out.println("Adicionado ao estoque: " + produto.getNome() + " - Quantidade: " + quantidade       );
    }

    public void mostrarCarrinho() {
        System.out.println("Produtos no carrinho:");
        for (ProdutoCarrinho produto : getCarrinho().getProdutos()) {
            System.out.println("- " + produto.getProduto().getNome() + " - Preço: " + produto.getProduto().getPreco());
        }
    }

    public void comprarCarrinho(EstoqueRepository estoque, Carrinho carrinho, boolean pagamento) {
    double total = carrinho.calcularTotal();
    System.out.println("Total da compra: " + total);
    if (pagamento = false) {
        System.out.println("Crédito insuficiente para realizar a compra.");}
        else {
        estoque.removerEstoque(carrinho);
        carrinho.getProdutos().clear();
        }
        System.out.println("Compra realizada com sucesso.");
        
    }

}

