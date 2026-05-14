/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import java.util.ArrayList;

/**
 * Estructura de respuesta para operaciones de autorización de recursos desde
 * servicios externos.
 *
 * Encapsula la información de respuesta de las operaciones de aprobación o
 * rechazo de recursos (documentos, solicitudes de materiales, etc.) que se
 * pueden ejecutar desde portales o aplicaciones móviles. Contiene información
 * sobre el estado de la operación, mensajes descriptivos, el folio del recurso
 * procesado y los usuarios que tienen el recurso en su turno de autorización.
 *
 * @author Edwin Carmona
 * @version 1.0
 */
public class SAppLinkResponse {

    /**
     * Código de estado de la respuesta (0 = éxito, otros valores = error)
     */
    int code;
    /**
     * Mensaje descriptivo de la operación o error
     */
    String message;
    /**
     * Folio o identificador del recurso que fue autorizado/rechazado
     */
    String folio;
    /**
     * Lista de identificadores de usuarios que tienen el recurso en su turno de
     * autorización
     */
    ArrayList<Integer> nextUsers;

    /**
     * Constructor por defecto. Inicializa todos los atributos con valores
     * predeterminados: código en 0, mensajes vacíos y lista de usuarios vacía.
     */
    public SAppLinkResponse() {
        code = 0;
        message = "";
        folio = "";
        nextUsers = new ArrayList<>();
    }

    /**
     * Constructor parametrizado para inicializar la respuesta con datos
     * específicos.
     *
     * @param code código de estado de la respuesta
     * @param message mensaje descriptivo o de error
     * @param nextUsers lista de usuarios con turno de autorización pendiente
     */
    public SAppLinkResponse(int code, String message, ArrayList<Integer> nextUsers) {
        this.code = code;
        this.message = message;
        this.nextUsers = nextUsers;
    }

    /**
     * Obtiene el código de estado de la respuesta.
     *
     * @return código de estado
     */
    public int getCode() {
        return code;
    }

    /**
     * Establece el código de estado de la respuesta.
     *
     * @param code código de estado
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Obtiene el mensaje descriptivo de la operación.
     *
     * @return mensaje descriptivo o de error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje descriptivo de la operación.
     *
     * @param message mensaje descriptivo o de error
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene el folio del recurso procesado.
     *
     * @return folio o identificador del recurso
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Establece el folio del recurso procesado.
     *
     * @param folio folio o identificador del recurso
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * Obtiene la lista de usuarios con turno pendiente de autorización.
     *
     * @return lista de identificadores de usuarios
     */
    public ArrayList<Integer> getNextUsers() {
        return nextUsers;
    }

    /**
     * Establece la lista de usuarios con turno pendiente de autorización.
     *
     * @param nextUsers lista de identificadores de usuarios
     */
    public void setNextUsers(ArrayList<Integer> nextUsers) {
        this.nextUsers = nextUsers;
    }
}
