package com.sistemabar.controller;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sistemabar.AtualizaçãoProduto;
import com.sistemabar.ProcuraObjDTO;
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

    @Override
    public void handle(@org.jetbrains.annotations.NotNull HttpExchange exchange) throws IOException{
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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    String negativa = "Erro ao criar o produto";
                    byte[] negativaByte = negativa.getBytes();
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, negativaByte.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(negativaByte);
                        System.out.println(negativaByte);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            
            case "DELETE":
                ProcuraObjDTO objDTO = json.fromJson(new InputStreamReader(exchange.getRequestBody()), ProcuraObjDTO.class);
                if (produtoService.removerProduto(objDTO.nome()) == true) {
                    String removido = "produto removido";
                    byte[] removidoByte = removido.getBytes();
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, removidoByte.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(removidoByte);
                    }
                } else {
                    String negativa = "Erro ao deletar o produto";
                    byte[] negativaByte = negativa.getBytes();
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, negativaByte.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(negativaByte);
                    }
                }
                break;

            case "PUT":
                AtualizaçãoProduto produtoAtt = json.fromJson(new InputStreamReader(exchange.getRequestBody()), AtualizaçãoProduto.class);
                if (produtoService.atualizarPreço(produtoAtt)) {
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
                break;
        }
                
                



            
        }

        
    }

