package com.sistemabar.model;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private List<ProdutoCarrinho> produtos = new ArrayList<>();

    public void adicionarProduto(ProdutoCarrinho produtoCarrinho) {
        produtos.add(produtoCarrinho);
    }

    public void removerProduto(ProdutoCarrinho produtoCarrinho) {
        produtos.remove(produtoCarrinho);
    }

    public double calcularTotal() {
        double total = 0;
        for (ProdutoCarrinho produtoCarrinho : produtos) {
            total += produtoCarrinho.getProduto().getPreco() * produtoCarrinho.getQuantidade();
        }
        return total;
    }

    public List<ProdutoCarrinho> getProdutos() {
        return produtos;
    }   

    public ProdutoCarrinho buscarProdutoPorNome(String nome) {
        for (ProdutoCarrinho produtoCarrinho : produtos) {
            if (produtoCarrinho.getProduto().getNome().equalsIgnoreCase(nome)) {
                return produtoCarrinho;
            }
        }
        return null;
    }       



}
