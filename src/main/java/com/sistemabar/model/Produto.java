package com.sistemabar.model;

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

    
}
