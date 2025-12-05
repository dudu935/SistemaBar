package com.sistemabar.servidor;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sistemabar.controller.ProdutoController;
import com.sun.net.httpserver.*;

public class ServidorSistema {
    public static void main (String args[]) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

    server.createContext("/produtos", new ProdutoController());
    server.start();
    System.out.println("servidor rodando");
    try {
        Thread.currentThread().join();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    

    }
}
