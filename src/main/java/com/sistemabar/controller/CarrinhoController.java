package com.sistemabar.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sistemabar.CarrinhoAddDTO;
import com.sistemabar.CriaEstoqueDTO;
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

public class CarrinhoController implements HttpHandler {
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
                    response = json.toJson(produtoService.mostrarCarrinho());
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
                String path = exchange.getRequestURI().getPath();
                if(path.equals("/carrinho")) {
                    try {

                        CarrinhoAddDTO carrinhoDTO = json.fromJson(new InputStreamReader(exchange.getRequestBody()), CarrinhoAddDTO.class);
                        Produto produto = new Produto(carrinhoDTO.produto().nome(), carrinhoDTO.produto().preco());
                        if (produto != null) {
                            produtoService.adicionarProdutoAoCarrinho(produto, carrinhoDTO.quantidade());
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
                } else if (path.equals("/carrinho/compra")) {
                    if(produtoService.comprarCarrinho(true)){
                        String positiva = "Compra realizada com sucesso!";
                        byte[] positivaBytes = positiva.getBytes();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, positivaBytes.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(positivaBytes);
                            System.out.println(positivaBytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        String negativa = "Erro ao finalizar a compra";
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
                }


            case "DELETE":
                try {
                    CarrinhoAddDTO carrinhoDTO = json.fromJson(new InputStreamReader(exchange.getRequestBody()), CarrinhoAddDTO.class);
                    Produto produto = new Produto(carrinhoDTO.produto().nome(), carrinhoDTO.produto().preco());
                    if (produtoService.removerProdutoCar(carrinhoDTO)) {
                        String removido = "produto removido";
                        byte[] removidoByte = removido.getBytes();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, removidoByte.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(removidoByte);
                            System.out.println(removido);
                        }
                    } else {
                        String negativa = "Erro ao deletar o produto";
                        byte[] negativaByte = negativa.getBytes();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.sendResponseHeaders(400, negativaByte.length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(negativaByte);
                            System.out.println(negativa);
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
                    CarrinhoAddDTO carrinhoDTO = json.fromJson(new InputStreamReader(exchange.getRequestBody()), CarrinhoAddDTO.class);
                    Produto produto = new Produto(carrinhoDTO.produto().nome(), carrinhoDTO.produto().preco());
                    if (produtoService.atualizaCarrinho(carrinhoDTO)) {
                        String nomeAtualizar = "carrinho atualizado";
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
                            System.out.println(negativa);
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
