package com.sistemabar.repository;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sistemabar.model.Carrinho;
import com.sistemabar.model.Estoque;
import com.sistemabar.model.ProdutoCarrinho;


public class EstoqueRepository {
    Gson json = new GsonBuilder().setPrettyPrinting().create();
    List<Estoque> estoques = new ArrayList<>();

    
    public void salvarEstoque(Estoque estoque) {
        try (FileReader reader = new FileReader("estoque.json")) {
            estoques = json.fromJson(reader, new TypeToken<List<Estoque>>(){}.getType());
            if(estoques == null)
                estoques = new ArrayList<>();
        } catch (IOException e) {
            estoques = new ArrayList<>();
        }

        boolean encontrado = false;
        for (Estoque e : estoques) {
            if (e.getProduto().getNome().equalsIgnoreCase(estoque.getProduto().getNome())) {
                int quantidadeAtualizada = e.getQuantidade() + estoque.getQuantidade();
                e.setQuantidade(quantidadeAtualizada);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            estoques.add(estoque);
        }

        try (FileWriter writer = new FileWriter("estoque.json")) {
            json.toJson(estoques, writer);
            System.out.println("Produto adicionado ao estoque com sucesso.");
        } catch (java.io.IOException e) {
            System.err.println("Erro ao salvar estoque: " + e.getMessage());
        }
    }
    public List<Estoque> carregarEstoque() {
        String estoqueJson;
        try {
            estoqueJson = String.join("\n", Files.readAllLines(Paths.get("estoque.json"), StandardCharsets.UTF_8));
             List<Estoque> estoques = json.fromJson(estoqueJson, new TypeToken<List<Estoque>>(){}.getType());
            return estoques;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void removerEstoque(Carrinho carrinho) {
        try (FileReader reader = new FileReader("estoque.json")) {
            estoques = json.fromJson(reader, new TypeToken<List<Estoque>>(){}.getType());            
        } catch (IOException e) {
            System.out.println("Erro na comunicação com o arquivo de estoque.");
        }
        for (ProdutoCarrinho produtoCarrinho : carrinho.getProdutos()) {
            Iterator<Estoque> iterator = estoques.iterator();
            while (iterator.hasNext()) {
                Estoque estoque = iterator.next();
                if (estoque.getProduto().getNome().equalsIgnoreCase(produtoCarrinho.getProduto().getNome())) {
                        int novaQuantidade = estoque.getQuantidade() - produtoCarrinho.getQuantidade();
                        if (novaQuantidade > 0) {
                            estoque.setQuantidade(novaQuantidade);
                            System.out.println("Estoque atualizado para o produto: " + estoque.getProduto().getNome() + " - Nova quantidade: " + novaQuantidade);
                            break;}
                        else {
                            iterator.remove();
                            System.out.println("Produto esgotado e removido do estoque: " + estoque.getProduto().getNome());
                            break;
                    }
                }
            }
        }
        try (FileWriter writer = new FileWriter("estoque.json")) {
            json.toJson(estoques, writer);
            System.out.println("Estoque atualizado com sucesso.");  
        } catch (java.io.IOException e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
        }
        }
    
       

        public Estoque buscarProdutoPorNome(String nome) {
            List<Estoque> estoques = carregarEstoque();
            for (Estoque estoque : estoques) {
                if (estoque.getProduto().getNome().equalsIgnoreCase(nome)) {
                    System.out.println("Produto encontrado no estoque: " + estoque.getProduto().getNome() + " - Quantidade: " + estoque.getQuantidade());
                    return estoque;
                }
        }   
        return null;}
    }

    

