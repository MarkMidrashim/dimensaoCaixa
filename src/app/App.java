package app;

import java.util.*;
import org.json.JSONObject;

/**
 * Classe App
 * @author MarcosFranco
 */
public class App {
    /**
     * Main
     * Responsável por definir as dimensões e chamar a função de cálculo
     * @param args
     */
    public static void main (String[] args) throws Exception {
        System.out.println("# ----------------- #");
        System.out.println("#     Correios      #");
        System.out.println("# Cálculo de volume #");
        System.out.println("# ------------------#\n");

        List<Item> carrinho = new ArrayList<>();
        
        // Itens
        Item item1 = new Item(18, 13, 4);
        Item item2 = new Item(21, 14, 5);
        Item item3 = new Item(21, 14, 5);
        Item item4 = new Item(23, 8, 8);
        Item item5 = new Item(18, 4, 9);

        // Adiciona os itens na lista
        carrinho.add(item1);
        carrinho.add(item2);
        carrinho.add(item3);
        carrinho.add(item4);
        carrinho.add(item5);

        // Calculo de dimensão
        JSONObject objBox = CalculaBox.calcBox(carrinho);

        // Exibe resultado
        System.out.println("Altura          : " + objBox.get("altura") + " cm");
        System.out.println("Largura         : " + objBox.get("largura") + " cm");
        System.out.println("Comprimento     : " + objBox.get("comprimento") + " cm");
        System.out.println("Itens           : " + objBox.get("qtd_itens") + " un");
        System.out.println("Volume          : " + objBox.get("volume") + " cm²");
        System.out.println("Volume Produtos : " + objBox.get("volume_itens") + " cm²");
        System.out.println("Volume Vazio    : " + objBox.get("volume_vazio") + " cm²");
    }
}