package com.sistemabar.controller;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.sistemabar.model.Produto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sistemabar.model.Produto;
import com.sistemabar.repository.EstoqueRepository;
import com.sistemabar.repository.ProdutoRepository;
import com.sistemabar.service.ProdutoService;
import com.sun.net.httpserver.*;

public class ProdutoController implements HttpHandler {
    ProdutoRepository produtoRepository = new ProdutoRepository();
    EstoqueRepository estoqueRepository = new EstoqueRepository();
    ProdutoService produtoService = new ProdutoService(estoqueRepository, produtoRepository);
    Gson json = new GsonBuilder().setPrettyPrinting().create();

    public void handle(HttpExchange exchange) throws IOException{
        String method = exchange.getRequestMethod();
        String response = "";

        switch (method) {
            case "GET":
                response = json.toJson(produtoService.listarProdutos());
                byte[] respostaBytes = response.getBytes();
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, respostaBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(respostaBytes);
                    System.out.println(respostaBytes);
                }
                break;
            
            case "POST":
                Produto produto = json.fromJson(new InputStreamReader(exchange.getRequestBody()), Produto.class);
                if (produto != null) {
                    produtoService.criarProduto(produto.getNome(),produto.getPreco());
                    String respostaPositiva = "produto criado com sucesso";
                    byte[] respostaPositivaBytes = respostaPositiva.getBytes();
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, respostaPositivaBytes.length);                
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(respostaPositivaBytes);
                        System.out.println(respostaPositivaBytes);
                    }
                } else {
                    String negativa = "Erro ao criar o produto";
                    byte[] negativaByte = negativa.getBytes();
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, negativaByte.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(negativaByte);
                        System.out.println(negativaByte);
                    }
                }
                break;
            
            case "DELETE":
                JsonObject jsonObject2 = json.fromJson(new InputStreamReader(exchange.getRequestBody()), JsonObject.class);
                String nome = jsonObject2.get("nome").getAsString();
                if (produtoService.removerProduto(nome) == true) {
                    String removido = "produto removido";
                    byte[] removidoByte = removido.getBytes();
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, removidoByte.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(removidoByte);
                    }
                }
                break;

            case "PUT":
                Produto produto2 = json.fromJson(new InputStreamReader(exchange.getRequestBody()), Produto.class);
                if (produtoService.atualizarPreço(produto2.getNome(), produto2.getPreco())) {
                    String nomeAtualizar = "preço atualizado";
                    byte[] atualizarPreço = nomeAtualizar.getBytes();
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, atualizarPreço.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(atualizarPreço);
                    }
                } else { 
                    String negativa = "erro ao atualizar";
                    byte[] negativaByte = negativa.getBytes();
                    exchange.getRequestHeaders().add("COntent-Type", "application/json");
                    exchange.sendResponseHeaders(400, negativaByte.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(negativaByte);
                    }
                }
                    }
                
                



            
        }

        
    }

