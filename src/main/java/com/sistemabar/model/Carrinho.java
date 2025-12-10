package com.sistemabar.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private List<ProdutoCarrinho> produtos = new ArrayList<>();
    Gson gson = new Gson();
    File file;

    public Carrinho() {
        this.file = new File("carrinho.json");
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




    public void adicionarProduto(ProdutoCarrinho produtoCarrinho) {
        try (FileReader reader = new FileReader("carrinho.json")) {
            produtos = gson.fromJson(reader, new TypeToken<List<ProdutoCarrinho>>(){}.getType());
            if(produtos == null)
                produtos = new ArrayList<>();
        } catch (IOException e) {
            produtos = new ArrayList<>();
        }

        boolean encontrado = false;
        for (ProdutoCarrinho e : produtos) {
            if (e.getProduto().getNome().equalsIgnoreCase(produtoCarrinho.getProduto().getNome())) {
                int quantidadeAtualizada = e.getQuantidade() + produtoCarrinho.getQuantidade();
                e.setQuantidade(quantidadeAtualizada);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            produtos.add(produtoCarrinho);
        }

        try (FileWriter writer = new FileWriter("carrinho.json")) {
            gson.toJson(produtos, writer);
            System.out.println("Produto adicionado ao estoque com sucesso.");
        } catch (java.io.IOException e) {
            System.err.println("Erro ao salvar estoque: " + e.getMessage());
        }

    }

    public void limparCarrinho() {
        try (FileWriter writer = new FileWriter("carrinho.json")) {
            produtos.clear();
            writer.write("[]");
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removerProduto(ProdutoCarrinho produtoCarrinho) {
        try (FileReader reader = new FileReader("carrinho.json")) {
            produtos = gson.fromJson(reader, new TypeToken<List<Produto>>(){}.getType());
            if(produtos == null) {
                produtos = new ArrayList<>();
            }
        } catch (IOException e) {
            produtos = new ArrayList<>();

        }
        try (FileWriter writer = new FileWriter("carrinho.json")) {
            produtos.remove(produtoCarrinho);
            gson.toJson(produtos, writer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean atualizarCarrinho (ProdutoCarrinho produtoCarrinho) {
        try (FileReader reader = new FileReader("carrinho.json")) {
            produtos = gson.fromJson(reader, new TypeToken<List<ProdutoCarrinho>>(){}.getType());
            if(produtos == null) {
                produtos = new ArrayList<>();
            }
        } catch (IOException e) {
            produtos = new ArrayList<>();
        }
        boolean atualizado = false;
        for (ProdutoCarrinho produto : produtos) {
            if (produto.getProduto().getNome().equals(produtoCarrinho.getProduto().getNome())) {
                produto.getProduto().setPreco(produtoCarrinho.getProduto().getPreco());
                atualizado = true;
                break;
            }
        }
        try (FileWriter writer = new FileWriter("produto.json")) {
            if(atualizado) {
                gson.toJson(produtos, writer);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return atualizado;
    }



    public double calcularTotal() {
        try (FileReader reader = new FileReader("carrinho.json")) {
            produtos = gson.fromJson(reader, new TypeToken<List<ProdutoCarrinho>>(){}.getType());
            if (produtos == null) {
                produtos = new ArrayList<>();
            }
        } catch (IOException e) {
            produtos = new ArrayList<>();
        }
        double total = 0;
        for (ProdutoCarrinho produtoCarrinho : produtos) {
            total += produtoCarrinho.getProduto().getPreco() * produtoCarrinho.getQuantidade();
        }
        return total;
    }

    public List<ProdutoCarrinho> getProdutos() {
        try (FileReader reader = new FileReader("carrinho.json")) {
            produtos = gson.fromJson(reader, new TypeToken<List<ProdutoCarrinho>>(){}.getType());
            if (produtos == null) {
                produtos = new ArrayList<>();
            }
        } catch (IOException e) {
            produtos = new ArrayList<>();
        }
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
