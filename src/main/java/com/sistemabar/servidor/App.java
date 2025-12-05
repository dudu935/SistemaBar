package com.sistemabar.servidor;
import java.io.IOException;
import java.util.Scanner;

import com.sistemabar.controller.MenuService;
import com.sistemabar.repository.EstoqueRepository;
import com.sistemabar.repository.ProdutoRepository;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )    {
        boolean menu = true;
        MenuService menuService = new MenuService(new EstoqueRepository(), new ProdutoRepository()); 
        while (menu == true) {
               
            menuService.exibirMenu();
            int escolha = menuService.obterEscolhaUsuario();
            switch (escolha) {
                case 1:
                    menuService.escolha1();
                    break;
                case 2:
                    menuService.escolha2();
                    break;
                case 3:
                    menuService.escolha3();
                    break;
                case 4:
                    menuService.escolha4();
                    break;
                case 5:
                    menuService.escolha5();
                    break;
                case 6:
                    menuService.escolha6();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
