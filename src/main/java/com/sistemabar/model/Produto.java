package com.sistemabar.model;

import java.util.Objects;
import java.util.UUID;

public class Produto {
    private String nome;
    private double preco;
    private UUID id;

    public Produto(String nome, double preco) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.preco = preco;
    }   

    public String getNome() {
        return nome;
    }   

    public double getPreco() {
        return preco;
    }   

    public void setNome(String nome) {
        this.nome = nome;
    }       

    public void setPreco(double preco) {
        this.preco = preco;
    }       

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Double.compare(preco, produto.preco) == 0 && Objects.equals(nome, produto.nome) && Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, preco, id);
    }
}
