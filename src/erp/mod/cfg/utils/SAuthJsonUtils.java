/*
 * Utilidades para el manejo de estructuras JSON relacionadas con autenticación.
 */
package erp.mod.cfg.utils;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de utilidades para procesar objetos JSON en el contexto de autenticación.
 * Utiliza la librería Jackson para trabajar con nodos JSON.
 * 
 * @author Edwin Carmona
 */
public class SAuthJsonUtils {

    /**
     * Verifica si un arreglo dentro de un objeto JSON contiene un valor específico.
     *
     * @param root Nodo raíz del JSON.
     * @param parentKey Clave del nodo padre donde se encuentra el arreglo.
     * @param arrayKey Clave del arreglo a analizar.
     * @param valueToFind Valor entero que se desea encontrar dentro del arreglo.
     * @return true si el arreglo contiene el valor; false en caso contrario.
     */
    public static boolean containsValue(JsonNode root, String parentKey, String arrayKey, int valueToFind) {
        JsonNode arrayNode = root.path(parentKey).path(arrayKey);
        if (arrayNode.isArray()) {
            for (JsonNode elem : arrayNode) {
                if (elem.asInt() == valueToFind) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Obtiene una lista de enteros desde un arreglo JSON si contiene un valor específico.
     * Si el valor no se encuentra, retorna una lista vacía.
     *
     * @param root Nodo raíz del JSON.
     * @param parentKey Clave del nodo padre donde se encuentra el arreglo.
     * @param arrayKey Clave del arreglo a analizar.
     * @param valueToFind Valor entero a buscar dentro del arreglo.
     * @return Lista de enteros si el valor existe en el arreglo; lista vacía en caso contrario.
     */
    public static List<Integer> getArrayIfContains(JsonNode root, String parentKey, String arrayKey, int valueToFind) {
        JsonNode arrayNode = root.path(parentKey).path(arrayKey);
        List<Integer> result = new ArrayList<>();
        boolean found = false;

        if (arrayNode.isArray()) {
            for (JsonNode elem : arrayNode) {
                int val = elem.asInt();
                result.add(val);
                if (val == valueToFind) {
                    found = true;
                }
            }
        }

        return found ? result : new ArrayList<>();
    }

    /**
     * Convierte una lista de enteros en una cadena de texto separada por comas.
     * 
     * @param toUsers Lista de enteros a convertir.
     * @return Cadena de texto con los valores separados por comas.
     */
    public static String getListAsString(List<Integer> toUsers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < toUsers.size(); i++) {
            sb.append(toUsers.get(i));
            if (i < toUsers.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    /**
     * Obtiene el valor de un elemento específico dentro de un nodo JSON.
     * Puede recuperar como <code>String</code>: textos, enteros y arreglos de enteros.
     *
     * @param root Nodo raíz del JSON.
     * @param parentKey Clave del nodo padre donde se encuentra el elemento.
     * @param elementKey Clave del elemento cuyo valor se desea obtener.
     * @return Valor del elemento como cadena de texto; si no es texto, entero o arreglo de enteros, devuelve una cadena vacía.
     */
    public static String getValueOfElementAsText(JsonNode root, String parentKey, String elementKey) {
        JsonNode parentNode = parentKey.isEmpty() ? root : root.path(parentKey);
        JsonNode elementNode = parentNode.path(elementKey);
        String value = "";
        
        if (elementNode.isTextual()) {
            value = elementNode.asText();
        }
        else if (elementNode.isInt()) {
            value = "" + elementNode.asInt();
        }
        else if (elementNode.isArray()) {
            value = elementNode.toString().replaceAll("[\\[\\]]", "");
        }
        
        return value;
    }

    /**
     * Verifica si un nodo JSON contiene un elemento específico.
     *
     * @param root Nodo raíz del JSON.
     * @param parentKey Clave del nodo padre donde se encuentra el elemento.
     * @param elementKey Clave del elemento a verificar.
     * @return true si el elemento existe; false en caso contrario.
     */
    public static boolean containsElement(JsonNode root, String parentKey, String elementKey) {
        JsonNode parentNode = parentKey.isEmpty() ? root : root.path(parentKey);
        return parentNode.has(elementKey);
    }
}
