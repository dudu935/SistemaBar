package com.sistemabar.repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sistemabar.AtualizaçãoProduto;
import com.sistemabar.model.Produto;

public class ProdutoRepository {
    Gson json = new GsonBuilder().setPrettyPrinting().create();
    List<Produto> produtos = new ArrayList<>();

    public ProdutoRepository() {
        File file = new File("produto.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo produto.json: " + e.getMessage());
            }
        }
        
    }
    public boolean atualizarProduto(AtualizaçãoProduto att) {
        try (FileReader reader = new FileReader("produto.json")) {
            produtos = json.fromJson(reader, new TypeToken<List<Produto>>(){}.getType());
            if(produtos == null) {
                produtos = new ArrayList<>();
            }
        } catch (IOException e) {
            produtos = new ArrayList<>();

        }
        try (FileWriter writer = new FileWriter("produto.json")) {
            Produto produto1 = buscarProdutoPorNome(att.nome());
            produto1.setPreco(att.preco());
            json.toJson(produtos, writer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removerProduto(Produto produto) {
        try (FileReader reader = new FileReader("produto.json")) {
            produtos = json.fromJson(reader, new TypeToken<List<Produto>>(){}.getType());
            if(produtos == null) {
                produtos = new ArrayList<>();
            }
        } catch (IOException e) {
            produtos = new ArrayList<>();
        
        }
        try (FileWriter writer = new FileWriter("produto.json")) {
            produtos.remove(produto);
            json.toJson(produtos, writer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }

    public void salvarProduto(Produto produto){
        try (FileReader reader = new FileReader("produto.json")) {
            produtos = json.fromJson(new FileReader("produto.json"), new TypeToken<List<Produto>>(){}.getType());
                if(produtos == null) {
                produtos = new ArrayList<>();}
            } 
             catch (IOException e) {
                produtos = new ArrayList<>();
            }
        try (FileWriter writer = new FileWriter("produto.json")) { 
            produtos.add(produto);
            json.toJson(produtos, writer);
            System.out.println("Produto salvo com sucesso.");       
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }  

    public List<Produto> carregarProdutos() {
        String produtoJson;
        try {
            produtoJson = String.join(" ", Files.readAllLines(Paths.get("produto.json"), StandardCharsets.UTF_8));
             List<Produto> produtos = json.fromJson(produtoJson, new TypeToken<List<Produto>>(){}.getType()    );
            return produtos;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } ;
        return null;
    }   
    
    public Produto buscarProdutoPorNome(String nome) {
        List<Produto> produtos = carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                return produto;
            }
        }
        return null;
    }



    

}
