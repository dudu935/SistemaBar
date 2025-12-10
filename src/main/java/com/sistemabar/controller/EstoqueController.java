package com.sistemabar.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sistemabar.AtualizaçãoProduto;
import com.sistemabar.CriaEstoqueDTO;
import com.sistemabar.ProcuraObjDTO;
import com.sistemabar.model.Estoque;
import com.sistemabar.model.Produto;
import com.sistemabar.repository.EstoqueRepository;
import com.sistemabar.repository.ProdutoRepository;
import com.sistemabar.service.ProdutoService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EstoqueController implements HttpHandler {
    ProdutoRepository produtoRepository = new ProdutoRepository();
    EstoqueRepository estoqueRepository = new EstoqueRepository();
    ProdutoService produtoService = new ProdutoService(estoqueRepository, produtoRepository);
    Gson json = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(@org.jetbrains.annotations.NotNull HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        switch (method) {
            case "GET":
                try {
                    response = json.toJson(produtoService.listarEstoques());
                    byte[] respostaBytes = response.getBytes(StandardCharsets.UTF_8);

                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, respostaBytes.length);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(respostaBytes);
                    }

                    System.out.println("Resposta enviada: " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                    String erro = "{\"mensagem\":\"erro interno\"}";
                    byte[] erroBytes = erro.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(500, erroBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(erroBytes);
                    }
                }
                break;

            case "POST":
                try {

                    CriaEstoqueDTO dto = json.fromJson(new InputStreamReader(exchange.getRequestBody()), CriaEstoqueDTO.class);
                    Produto produto = new Produto(dto.produto().nome(), dto.produto().preco());
                    Estoque estoque = new Estoque(produto, dto.quantidade());
                    if (estoque != null) {
                        produtoService.adicionarProdutoAoEstoque(produto, dto.quantidade());
                        String respostaPositiva = "Estoque adicionado com sucesso!";
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
                        String negativa = "Erro ao adicionar estoque!";
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "DELETE":
                try {
                    CriaEstoqueDTO estoqueDTO = json.fromJson(new InputStreamReader(exchange.getRequestBody()), CriaEstoqueDTO.class);
                    Produto produto = new Produto(estoqueDTO.produto().nome(), estoqueDTO.produto().preco());
                    Estoque estoque = new Estoque(produto, estoqueDTO.quantidade());
                    if (produtoService.removerEstoque(estoque)) {
                        String removido = "produto removido";
                        byte[] removidoByte = removido.getBytes();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, removidoByte.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(removidoByte);
                            System.out.println(removidoByte);
                        }
                    } else {
                        String negativa = "Erro ao deletar o produto";
                        byte[] negativaByte = negativa.getBytes();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.sendResponseHeaders(400, negativaByte.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(negativaByte);
                            System.out.println(negativaByte);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    throw new RuntimeException(e);
                } catch (JsonIOException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "PUT":
                try {
                    CriaEstoqueDTO estoqueDTO = json.fromJson(new InputStreamReader(exchange.getRequestBody()), CriaEstoqueDTO.class);
                    Produto produto = new Produto(estoqueDTO.produto().nome(), estoqueDTO.produto().preco());
                    Estoque estoque = new Estoque(produto, estoqueDTO.quantidade());
                    if (produtoService.atualizarProduto(estoque)) {
                        String nomeAtualizar = "preço atualizado";
                        byte[] atualizarPreço = nomeAtualizar.getBytes();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, atualizarPreço.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(atualizarPreço);
                            System.out.println(nomeAtualizar);
                        }
                    } else {
                        String negativa = "erro ao atualizar";
                        byte[] negativaByte = negativa.getBytes();
                        exchange.getRequestHeaders().add("COntent-Type", "application/json");
                        exchange.sendResponseHeaders(400, negativaByte.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(negativaByte);
                            System.out.println(negativaByte);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    throw new RuntimeException(e);
                } catch (JsonIOException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }
}