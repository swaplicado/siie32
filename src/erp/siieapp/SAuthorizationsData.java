/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

/**
 * Estructura de datos que representa un registro de autorización de un recurso.
 *
 * Contiene información completa sobre una solicitud de autorización de un
 * documento (DPS, solicitud de material, etc.), incluyendo estado de
 * autorización, datos del usuario, información del asociado de negocios y
 * fechas relevantes. Se utiliza principalmente para transportar información
 * desde servicios externos o APIs.
 *
 * @author Adrián Avilés
 * @version 1.0
 */
public class SAuthorizationsData {

    /**
     * Clave primaria compuesta del recurso (año, documento, etc.)
     */
    int[] idData;
    /**
     * Folio o número de referencia del recurso
     */
    String folio;
    /**
     * Descripción del estado de autorización actual
     */
    String authorizationStatusName;
    /**
     * Tipo de autorización requerida (DPS, Solicitud de Material, etc.)
     */
    String authorizationTypeName;
    /**
     * Nombre del tipo de dato/recurso
     */
    String dataTypeName;
    /**
     * Nombre del usuario que creó el recurso
     */
    String userCreator;
    /**
     * Nombre del usuario que actualizó el recurso
     */
    String userUpdator;
    /**
     * Nombre del usuario que autorizó el recurso
     */
    String authorizationUser;
    /**
     * Entidad de consumo (ubicación)
     */
    String consumeEntity;
    /**
     * Sub-entidad de consumo
     */
    String subConsumeEntity;
    /**
     * Entidad proveedora o asociado de negocios
     */
    String supplierEntity;
    /**
     * Estado de la requisición o solicitud
     */
    String requisitionStatus;
    /**
     * Fecha del recurso en formato string
     */
    String date;
    /**
     * Referencia al tipo de dato
     */
    int fkDataType;
    /**
     * Referencia al usuario creador
     */
    int fkUserCreator;
    /**
     * Referencia al usuario que realizó última actualización
     */
    int fkUserUpdator;
    /**
     * Estado actual de autorización
     */
    int authorizationStatus;
    /**
     * Referencia a la prioridad del documento
     */
    int fkPriority;
    /**
     * Descripción de la prioridad
     */
    String priority;
    /**
     * Fecha de inserción en formato string
     */
    String dateInsert;
    /**
     * Fecha de última actualización en formato string
     */
    String dateUpdate;

    /**
     * Establece la referencia de prioridad del documento.
     *
     * @param fkPriority identificador de la prioridad
     */
    public void setFkPriority(int fkPriority) {
        this.fkPriority = fkPriority;
    }

    /**
     * Establece la descripción de la prioridad.
     *
     * @param priority descripción de la prioridad
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Establece la clave primaria compuesta del recurso.
     *
     * @param idData arreglo con componentes de la clave primaria
     */
    public void setIdData(int[] idData) {
        this.idData = idData;
    }

    /**
     * Establece la fecha del recurso.
     *
     * @param date fecha en formato string
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Establece el folio del recurso.
     *
     * @param folio folio o número de referencia
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * Establece el tipo de dato del recurso.
     *
     * @param dataType identificador del tipo de dato
     */
    public void setDataType(int dataType) {
        this.fkDataType = dataType;
    }

    /**
     * Establece el usuario creador.
     *
     * @param fkUserCreator identificador del usuario creador
     */
    public void setFkUserCreator(int fkUserCreator) {
        this.fkUserCreator = fkUserCreator;
    }

    /**
     * Establece el usuario que realizó la última actualización.
     *
     * @param fkUserUpdator identificador del usuario que actualizó
     */
    public void setFkUserUpdator(int fkUserUpdator) {
        this.fkUserUpdator = fkUserUpdator;
    }

    /**
     * Establece la fecha de inserción.
     *
     * @param dateInsert fecha de inserción en formato string
     */
    public void setDateInsert(String dateInsert) {
        this.dateInsert = dateInsert;
    }

    /**
     * Establece la fecha de última actualización.
     *
     * @param dateUpdate fecha de actualización en formato string
     */
    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    /**
     * Establece el estado de autorización.
     *
     * @param authorizationStatus estado de autorización
     */
    public void setAuthorizationStatus(int authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    /**
     * Obtiene la clave primaria compuesta del recurso.
     *
     * @return arreglo con componentes de la clave primaria
     */
    public int[] getIdData() {
        return idData;
    }

    /**
     * Obtiene la fecha del recurso.
     *
     * @return fecha en formato string
     */
    public String getDate() {
        return date;
    }

    /**
     * Obtiene el folio del recurso.
     *
     * @return folio o número de referencia
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Obtiene el tipo de dato del recurso.
     *
     * @return identificador del tipo de dato
     */
    public int getDataType() {
        return fkDataType;
    }

    /**
     * Obtiene el identificador del usuario creador.
     *
     * @return identificador del usuario creador
     */
    public int getFkUserCreator() {
        return fkUserCreator;
    }

    /**
     * Obtiene el identificador del usuario que realizó la última actualización.
     *
     * @return identificador del usuario que actualizó
     */
    public int getFkUserUpdator() {
        return fkUserUpdator;
    }

    /**
     * Obtiene la fecha de inserción.
     *
     * @return fecha de inserción en formato string
     */
    public String getDateInsert() {
        return dateInsert;
    }

    /**
     * Obtiene la fecha de última actualización.
     *
     * @return fecha de actualización en formato string
     */
    public String getDateUpdate() {
        return dateUpdate;
    }

    /**
     * Obtiene el estado de autorización.
     *
     * @return estado de autorización
     */
    public int getAuthorizationStatus() {
        return authorizationStatus;
    }

    /**
     * Obtiene la descripción del estado de autorización.
     *
     * @return nombre del estado de autorización
     */
    public String getAuthorizationStatusName() {
        return authorizationStatusName;
    }

    /**
     * Establece la descripción del estado de autorización.
     *
     * @param authorizationStatusName nombre del estado de autorización
     */
    public void setAuthorizationStatusName(String authorizationStatusName) {
        this.authorizationStatusName = authorizationStatusName;
    }

    /**
     * Obtiene el tipo de autorización requerida.
     *
     * @return nombre del tipo de autorización
     */
    public String getAuthorizationTypeName() {
        return authorizationTypeName;
    }

    /**
     * Establece el tipo de autorización requerida.
     *
     * @param authorizationTypeName nombre del tipo de autorización
     */
    public void setAuthorizationTypeName(String authorizationTypeName) {
        this.authorizationTypeName = authorizationTypeName;
    }

    /**
     * Obtiene el nombre del tipo de dato/recurso.
     *
     * @return nombre del tipo de dato
     */
    public String getDataTypeName() {
        return dataTypeName;
    }

    /**
     * Establece el nombre del tipo de dato/recurso.
     *
     * @param dataTypeName nombre del tipo de dato
     */
    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    /**
     * Obtiene el nombre del usuario creador.
     *
     * @return nombre del usuario creador
     */
    public String getUserCreator() {
        return userCreator;
    }

    /**
     * Establece el nombre del usuario creador.
     *
     * @param userCreator nombre del usuario creador
     */
    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    /**
     * Obtiene el nombre del usuario que realizó la última actualización.
     *
     * @return nombre del usuario que actualizó
     */
    public String getUserUpdator() {
        return userUpdator;
    }

    /**
     * Establece el nombre del usuario que realizó la última actualización.
     *
     * @param userUpdator nombre del usuario que actualizó
     */
    public void setUserUpdator(String userUpdator) {
        this.userUpdator = userUpdator;
    }

    /**
     * Obtiene el nombre del usuario que autorizó el recurso.
     *
     * @return nombre del usuario autorizador
     */
    public String getAuthorizationUser() {
        return authorizationUser;
    }

    /**
     * Establece el nombre del usuario que autorizó el recurso.
     *
     * @param authorizationUser nombre del usuario autorizador
     */
    public void setAuthorizationUser(String authorizationUser) {
        this.authorizationUser = authorizationUser;
    }

    /**
     * Obtiene la entidad de consumo (ubicación).
     *
     * @return entidad de consumo
     */
    public String getConsumeEntity() {
        return consumeEntity;
    }

    /**
     * Obtiene la sub-entidad de consumo (ubicación secundaria).
     *
     * @return sub-entidad de consumo
     */
    public String getSubConsumeEntity() {
        return subConsumeEntity;
    }

    /**
     * Establece la entidad de consumo (ubicación).
     *
     * @param consumeEntity entidad de consumo
     */
    public void setConsumeEntity(String consumeEntity) {
        this.consumeEntity = consumeEntity;
    }

    /**
     * Establece la sub-entidad de consumo (ubicación secundaria).
     *
     * @param subConsumeEntity sub-entidad de consumo
     */
    public void setSubConsumeEntity(String subConsumeEntity) {
        this.subConsumeEntity = subConsumeEntity;
    }

    /**
     * Obtiene la entidad proveedora o asociado de negocios.
     *
     * @return entidad proveedora
     */
    public String getSupplierEntity() {
        return supplierEntity;
    }

    /**
     * Establece la entidad proveedora o asociado de negocios.
     *
     * @param supplierEntity entidad proveedora
     */
    public void setSupplierEntity(String supplierEntity) {
        this.supplierEntity = supplierEntity;
    }

    /**
     * Obtiene el estado de la requisición.
     *
     * @return estado de requisición
     */
    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    /**
     * Establece el estado de la requisición.
     *
     * @param requisitionStatus estado de requisición
     */
    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    /**
     * Obtiene el tipo de dato del recurso.
     *
     * @return identificador del tipo de dato
     */
    public int getFkDataType() {
        return fkDataType;
    }

    /**
     * Establece el tipo de dato del recurso.
     *
     * @param fkDataType identificador del tipo de dato
     */
    public void setFkDataType(int fkDataType) {
        this.fkDataType = fkDataType;
    }

    /**
     * Obtiene la referencia de prioridad.
     *
     * @return identificador de la prioridad
     */
    public int getFkPriority() {
        return fkPriority;
    }

    /**
     * Obtiene la descripción de la prioridad.
     *
     * @return descripción de la prioridad
     */
    public String getPriority() {
        return priority;
    }
}
