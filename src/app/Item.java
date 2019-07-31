package app;

/**
 * Classe Item
 * 
 * @author MarcosFranco
 */
public class Item extends HashMapImplementation<String, Integer> {

    /* PROPERTIES */

    private static final long serialVersionUID = 1L;

    /* CONSTRUCTOR */

    public Item(Integer altura, Integer largura, Integer comprimento) {
        this.put("A", altura);
        this.put("L", largura);
        this.put("C", comprimento);
    }
}