package com.sistemabar.service;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.sistemabar.AtualizaçãoProduto;
import com.sistemabar.CarrinhoAddDTO;
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
        
    public boolean atualizarPreço(AtualizaçãoProduto produtoAtt) {
        if (produtoRepository.atualizarProduto(produtoAtt)) {
            return true;
        }else {
            return false;
        }
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.carregarProdutos();
        
    }

    public boolean adicionarProdutoAoCarrinho(Produto produto, int quantidade) {
        ProdutoCarrinho produtoCarrinho = new ProdutoCarrinho(produto, quantidade);
        Estoque estoqueEncontrado = null;
        for (Estoque e : estoqueRepository.carregarEstoque()) {
            e.getProduto().equals(produtoCarrinho.getProduto());
            estoqueEncontrado = e;
        }
        if (produtoCarrinho.getQuantidade() <= estoqueEncontrado.getQuantidade()) {
            carrinho.adicionarProduto(produtoCarrinho);
            System.out.println("Produto adicionado ao carrinho: " + produto.getNome());
            return true;
        } else {
            return false;
        }

    }   

    public void adicionarProdutoAoEstoque(Produto produto, int quantidade) {
        System.out.println(produtoRepository.carregarProdutos());
        Scanner scanner = new Scanner(System.in);
        Estoque estoque = new Estoque(produto, quantidade);
        estoqueRepository.salvarEstoque(estoque);
        System.out.println("Adicionado ao estoque: " + produto.getNome() + " - Quantidade: " + quantidade       );
    }

    public List<ProdutoCarrinho> mostrarCarrinho() {
        return carrinho.getProdutos();
        }

    public boolean comprarCarrinho(boolean pagamento) {
    double total = carrinho.calcularTotal();
    System.out.println("Total da compra: " + total);
    if (pagamento = false) {
        System.out.println("Crédito insuficiente para realizar a compra.");
        return false;
    } else {
        estoqueRepository.removerEstoque(carrinho);
        carrinho.limparCarrinho();
        }
        System.out.println("Compra realizada com sucesso.");
        return true;
        
    }

    public List<Estoque> listarEstoques() {
        return estoqueRepository.carregarEstoque();
    }

    public boolean removerEstoque(Estoque estoque) {
        adicionarProdutoAoCarrinho(estoque.getProduto(), estoque.getQuantidade());
        estoqueRepository.removerUmPRoduto(estoque);
        return true;
    }

    public boolean atualizarProduto(Estoque estoque){
        estoqueRepository.atualizarEstoque(estoque);
        return true;
    }

    public boolean atualizaCarrinho(CarrinhoAddDTO carrinhoAddDTO) {
        Produto produto = new Produto(carrinhoAddDTO.produto().nome(), carrinhoAddDTO.produto().preco());
        ProdutoCarrinho proCarrinho = new ProdutoCarrinho(produto, carrinhoAddDTO.quantidade());
        carrinho.atualizarCarrinho(proCarrinho);
        return true;
    }

    public boolean removerProdutoCar (CarrinhoAddDTO carrinhoAddDTO) {
        Produto produto = new Produto(carrinhoAddDTO.produto().nome(), carrinhoAddDTO.produto().preco());
        ProdutoCarrinho proCarrinho = new ProdutoCarrinho(produto, carrinhoAddDTO.quantidade());
        carrinho.removerProduto(proCarrinho);
        carrinho.getProdutos().remove(proCarrinho);

        return true;
    }

}

