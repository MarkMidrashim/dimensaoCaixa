package app;

import java.util.*;

/**
 * Classe HashMapImplementation
 * Implementação do HashMap
 * 
 * @author MarcosFranco
 */
public class HashMapImplementation<K, V> extends HashMap<K, V> {

    /* PROPERTIES */

    private static final long serialVersionUID = 1L;
    Map<K, V> map = new HashMap<>();

    /**
     * Método responsável por retornar o valor da chave especificada
     * @param key
     * @return
     */
    public V getKey(K key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        map.put(key, value);
        return super.put(key, value);
    }
}