package app;

import java.util.*;
import org.json.JSONObject;

/**
 * Classe de cálculo das dimensões
 * @author MarcosFranco
 */
public class CalculaBox extends Constants {

    /**
     * Função responsável por retornar o maior valor do array
     * @param numbers
     * @return
     */
    private static int getMaxValue(int[] numbers) {
        int maxValue = numbers[0];
        for (int i = 0; i < numbers.length; i++){
            if (numbers[i] > maxValue) {
                maxValue = numbers[i];
            }
        }
        return maxValue;
    }

    /**
     * Função responsável por retornar o menor valor do array
     * @param numbers
     * @return
     */
    private static int getMinValue(int[] numbers) {
        int minValue = numbers[0];
        for (int i = 0; i < numbers.length; i++){
            if (numbers[i] < minValue) {
                minValue = numbers[i];
            }
        }
        return minValue;
    }

    /**
     * Ordena os itens dentro do carrinho de forma que os maiores estejam no topo do List, e:
     *  Maior dimensão seja comprimento
     *  Menor dimensao seja altura
     *  Dimensão intermediária seja largura
     *
     * @param cart
     * @return
     */
    public static List<Item> orderCart(List<Item> cart) {
        // Percorre os itens reatribuindo suas dimensões
        for (int i = 0; i < cart.size(); i++) {
            int[] numbers = new int[3];
            numbers[0] = (int) cart.get(i).getKey("A");
            numbers[1] = (int) cart.get(i).getKey("L");
            numbers[2] = (int) cart.get(i).getKey("C");

            // P 1.0
            // A menor dimensão do nosso produto será nossa altura
            Integer new_alt  = getMinValue(numbers);

            // P 1.1
            // O maior valor será o comprimento
            Integer new_comp = getMaxValue(numbers);
            
            // P 1.2
            // O valor intermediário será a largura
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add((int) cart.get(i).getKey("A"));
            temp.add((int) cart.get(i).getKey("L"));
            temp.add((int) cart.get(i).getKey("C"));

            temp.sort(Comparator.naturalOrder());
            temp.remove(0);
            temp.remove(temp.get(temp.size()-1));

            cart.get(i).put("L", temp.size() != 0 ? temp.get(0) : new_alt);
            cart.get(i).put("A", new_alt);
            cart.get(i).put("C", new_comp);
            cart.get(i).put("LC", (int) cart.get(i).getKey("L") * (int) cart.get(i).getKey("C"));
            
            cart.set(i, cart.get(i));
        }

        // Ordena a lista
        cart.sort((a, b) -> {
            if (a.getKey("LC") < b.getKey("LC"))
                return 1;
            else 
                return 0;
        });

        return cart;
    }
    

    /**
     * Faz o cálculo dos itens dispostos nas dimensões da caixa
     * @param carrinho 
     * @return
     */
    public static JSONObject calcBox(List<Item> carrinho) {
        carrinho = orderCart(carrinho);

        // Guarda informações acerca da caixa a utilizar
        Map<String, Integer> box = new HashMap<>();
        box.put("altura", 0);
        box.put("largura", 0);
        box.put("comprimento", 0);
        box.put("qtd_itens", 0);
        box.put("volume", 0);
        box.put("volume_itens", 0);
        box.put("volume_vazio", 0);
        box.put("comprimento_remanescente", 0);
        box.put("largura_remanescente", 0);
        box.put("altura_remanescente", 0);

        // Checa se carrinho está vazio
        if (carrinho.isEmpty()) {
            System.out.println("Carrinho encontra-se vazio!");
            System.exit(0);
        }

        // Percorrendo lista de produtos realizando cálculos devidos
        for (Item item : carrinho) {
            int a  = (Integer) item.getKey("A");
            int l  = (Integer) item.getKey("L");
            int c  = (Integer) item.getKey("C");

            // Incrementa quantidade de itens dentro da caixa
            box.put("qtd_itens", box.get("qtd_itens") + 1);

            // Calculando volume de itens dentro da caixa
            int volume_itens = (box.get("volume_itens") > 0 ? box.get("volume_itens") : 0) + (a * l * c);
            box.put("volume_itens", volume_itens);

            // Verifica se produto cabe no espaço remanescente
            if (box.get("comprimento_remanescente") >= c && box.get("largura_remanescente") >= l) {
                // Se altura do novo produto for maior que altura disponivel
                // Incrementa altura da caixa
                if (a > box.get("altura_remanescente")) {
                    int altura = (box.get("altura") > 0 ? box.get("altura") : 0);
                    altura += a - ((Integer) box.get("altura_remanescente"));
                    box.put("altura", altura);
                }

                if (c > box.get("comprimento")) {
                    box.put("comprimento", c);
                }

                // Calculando volume remanescente do valor remanescente
                box.put("comprimento_remanescente", box.get("comprimento") - c);

                // Largura restante
                box.put("largura_remanescente", box.get("largura_remanescente") - l);
                box.put("altura_remanescente", a > box.get("altura_remanescente") ? a : box.get("altura_remanescente"));

                // Pula para próxima iteração...
                continue;
            }

            // P 2.0
            // Altura é a variável que sempre incrementa 
            // Independente de condicao
            int altura = (box.get("altura") > 0 ? box.get("altura") : 0) + a;
            box.put("altura", altura);

            // P 2.1 
            // Verificando se o item tem dimensões maiores que a caixa
            if (l > box.get("largura")) {
                box.put("largura", l);
            }

            if (c > box.get("comprimento")) {
                box.put("comprimento", c);
            }

            // Calculando volume remanescente
            box.put("comprimento_remanescente", box.get("comprimento"));
            box.put("largura_remanescente", box.get("largura") - l);
            box.put("altura_remanescente", a);
        }

        // Calculando volume da caixa
        box.put("volume", box.get("altura") * box.get("largura") * box.get("comprimento"));

        // Calculando volume vazio (ar dentro da caixa)
        box.put("volume_vazio", box.get("volume") - box.get("volume_itens"));

        // Checa se temos produtos e se conseguimos alcançar a dimensão mínima
        if (!carrinho.isEmpty()) {
            // Verificando se as dimensões mínimas são alcançadas
            if (box.get("altura") > 0 && box.get("altura") < MIN_ALTURA) box.put("altura", MIN_ALTURA);
            if (box.get("largura") > 0 && box.get("largura") < MIN_LARGURA) box.put("largura", MIN_LARGURA);
            if (box.get("comprimento") > 0 && box.get("comprimento") < MIN_COMPRIMENTO) box.put("comprimento", MIN_COMPRIMENTO);
        }

        // Verifica se as dimensões não ultrapassam valor máximo
        if (box.get("altura") > MAX_ALTURA) System.out.println("Erro: Altura maior que o permitido.\n");
        if (box.get("largura") > MAX_LARGURA) System.out.println("Erro: Largura maior que o permitido.\n");
        if (box.get("comprimento") > MAX_COMPRIMENTO) System.out.println("Erro: Comprimento maior que o permitido.\n");

        /**
         * @note
         * Não sei se é regra
         * Soma (C+L+A) MIN 29cm e MAX 200cm
         */
        int cla = box.get("comprimento") + box.get("largura") + box.get("altura");
        if (cla < MIN_SOMA_CLA) System.out.println("Erro: Soma dos valores C+L+A menor que o permitido.\n");
        if (cla > MAX_SOMA_CLA) System.out.println("Erro: Soma dos valores C+L+A maior que o permitido.\n");

        return new JSONObject(box);
    }
}