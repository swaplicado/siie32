/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

/**
 * Representa un recurso (documento) asignado a un usuario para autorización.
 * 
 * Esta clase encapsula la información necesaria para identificar un recurso específico
 * que requiere autorización por parte de un usuario. Almacena el tipo de recurso,
 * su identificación compuesta (múltiples claves primarias), el folio para referencia
 * y un contador para estadísticas o control.
 * 
 * @author Edwin Carmona
 * @version 1.0
 */
public class SUserResource {

    /** Identificador único del usuario autorizado */
    private int idUser;
    /** Tipo de recurso que requiere autorización */
    private int resourceType;
    /** Primera clave primaria del recurso (ej: año) */
    private int pk1;
    /** Segunda clave primaria del recurso (ej: número de documento) */
    private int pk2;
    /** Tercera clave primaria del recurso (opcional) */
    private int pk3;
    /** Cuarta clave primaria del recurso (opcional) */
    private int pk4;
    /** Quinta clave primaria del recurso (opcional) */
    private int pk5;
    /** Folio o identificador legible del recurso */
    private String folio;
    /** Contador para control o estadísticas */
    private int counter;

    /**
     * Obtiene el identificador del usuario.
     * @return identificador \u00fanico del usuario
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * Establece el identificador del usuario.
     * @param idUser identificador \u00fanico del usuario
     */
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /**
     * Obtiene el tipo de recurso.
     * @return tipo de recurso (consultar constantes de autorizaci\u00f3n)
     */
    public int getResourceType() {
        return resourceType;
    }

    /**
     * Establece el tipo de recurso.
     * @param resourceType tipo de recurso
     */
    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * Obtiene la primera clave primaria del recurso.
     * @return primera componente de la clave primaria (ej: a\u00f1o del documento)
     */
    public int getPk1() {
        return pk1;
    }

    /**
     * Establece la primera clave primaria del recurso.
     * @param pk1 primera componente de la clave primaria
     */
    public void setPk1(int pk1) {
        this.pk1 = pk1;
    }

    /**
     * Obtiene la segunda clave primaria del recurso.
     * @return segunda componente de la clave primaria (ej: n\u00famero de documento)
     */
    public int getPk2() {
        return pk2;
    }

    /**
     * Establece la segunda clave primaria del recurso.
     * @param pk2 segunda componente de la clave primaria
     */
    public void setPk2(int pk2) {
        this.pk2 = pk2;
    }

    /**
     * Obtiene la tercera clave primaria del recurso.
     * @return tercera componente de la clave primaria (opcional)
     */
    public int getPk3() {
        return pk3;
    }

    /**
     * Establece la tercera clave primaria del recurso.
     * @param pk3 tercera componente de la clave primaria
     */
    public void setPk3(int pk3) {
        this.pk3 = pk3;
    }

    /**
     * Obtiene la cuarta clave primaria del recurso.
     * @return cuarta componente de la clave primaria (opcional)
     */
    public int getPk4() {
        return pk4;
    }

    /**
     * Establece la cuarta clave primaria del recurso.
     * @param pk4 cuarta componente de la clave primaria
     */
    public void setPk4(int pk4) {
        this.pk4 = pk4;
    }

    /**
     * Obtiene la quinta clave primaria del recurso.
     * @return quinta componente de la clave primaria (opcional)
     */
    public int getPk5() {
        return pk5;
    }

    /**
     * Establece la quinta clave primaria del recurso.
     * @param pk5 quinta componente de la clave primaria
     */
    public void setPk5(int pk5) {
        this.pk5 = pk5;
    }

    /**
     * Obtiene el folio del recurso.
     * @return folio o n\u00famero de referencia del recurso
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Establece el folio del recurso.
     * @param folio folio o n\u00famero de referencia del recurso
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * Obtiene el contador asociado al recurso.
     * @return contador (puede usarse para estad\u00edsticas o control)
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Establece el contador asociado al recurso.
     * @param counter contador para control o estad\u00edsticas
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
}
