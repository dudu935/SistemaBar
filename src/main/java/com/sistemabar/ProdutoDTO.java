package com.sistemabar;

import java.util.UUID;

public record ProdutoDTO(UUID id, String nome, Double preco) {
}
