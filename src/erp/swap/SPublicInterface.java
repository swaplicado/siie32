/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap;

import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.trn.api.db.STrnDBCore;
import erp.swap.utils.SExportDataUser;
import erp.swap.utils.SExportDataUtils;
import erp.swap.utils.SResourceResponse;
import erp.swap.utils.SSwapHttpUtils;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interfaz pública para la clase de sincronización de datos.
 * Esta clase proporciona métodos para interactuar con la base de datos y
 * realizar operaciones de sincronización de datos.
 * @author Edwin Carmona, Sergio Flores
 */
public class SPublicInterface {
    
    SMySqlClass oDbObj;
    String msMainDatabase;

    public SPublicInterface(String sjon) throws Exception {
        try {
            this.oDbObj = new SMySqlClass(sjon);
            this.msMainDatabase = this.oDbObj.getMainDatabaseName(0);
        }
        catch (SConfigException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return Objeto {@code Connection} si la conexión es exitosa, de lo contrario {@code null}.
     */
    private Connection getConnection(final int idCo) {
        try {
            String db;
            if (idCo > 0) {
                db = this.oDbObj.getMainDatabaseName(idCo);
            }
            else {
                db = this.msMainDatabase;
            }
            return this.oDbObj.connect("", "", db, "", "");
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    /**
     * Obtiene un proveedor por su ID fiscal.
     *
     * @param fiscalId ID fiscal del proveedor.
     * @return Objeto {@code SUserExport} que representa al proveedor, o {@code null} si no se encuentra.
     */
    public SExportDataUser getSupplierByFiscalId(final String fiscalId) {
        try {
            Connection conn = this.getConnection(0);

            if (conn == null) {
                return null;
            }

            return SExportDataUtils.getSupplierByFiscalId(conn.createStatement(), fiscalId);
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public SResourceResponse updateResourceStatus(final int companyId, 
                                                            final int resourceType, 
                                                            final String resourceId, 
                                                            final int authStatusId, 
                                                            final int userId,
                                                            final String notes,
                                                            final double newAmount,
                                                            final String newDate) {
        try {
            SResourceResponse oResponse;
            Connection conn = this.getConnection(companyId);

            if (conn == null) {
                oResponse = new SResourceResponse();
                oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
                oResponse.message = "No se pudo establecer conexión con la base de datos de SIIE.";
                oResponse.error = "No se pudo establecer conexión con la base de datos de SIIE.";
                oResponse.data = null;
                
                return oResponse;
            }
            
            return SSwapHttpUtils.updateResourceStatus(conn.createStatement(), 
                                                        this.oDbObj.getMainDatabaseName(companyId), 
                                                        resourceType, 
                                                        resourceId, 
                                                        authStatusId,
                                                        userId,
                                                        notes,
                                                        newAmount,
                                                        newDate);
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SPublicInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Actualiza un recurso específico en la base de datos.
     *

     * @param companyId El ID de la compañía.
     * @param resourceType El tipo de recurso a actualizar.
     * @param resourceId El ID del recurso a actualizar.
     * @param userId El ID del usuario que realiza la actualización.
     * @param lAttributes Un HashMap que contiene los atributos a actualizar.
     * 
     * @return Un objeto SResourceResponse que contiene la respuesta de la operación.
     */
    public SResourceResponse updateResource(final int companyId, 
                                            final int resourceType, 
                                            final String resourceId,
                                            final int userId,
                                            final HashMap<String, Object> lAttributes) {
        try {
            SResourceResponse oResponse;
            if (lAttributes.isEmpty()) {
                oResponse = new SResourceResponse();
                oResponse.status_code = HttpURLConnection.HTTP_BAD_REQUEST;
                oResponse.message = "El cuerpo de la solicitud está vacío.";
                oResponse.error = "El cuerpo de la solicitud está vacío.";
                oResponse.data = null;

                return oResponse;
            }
            Connection conn = this.getConnection(companyId);

            if (conn == null) {
                oResponse = new SResourceResponse();
                oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
                oResponse.message = "No se pudo establecer conexión con la base de datos de SIIE.";
                oResponse.error = "No se pudo establecer conexión con la base de datos de SIIE.";
                oResponse.data = null;
                
                return oResponse;
            }
            
            return SSwapHttpUtils.updateResource(conn.createStatement(), 
                                            this.oDbObj.getMainDatabaseName(companyId), 
                                            resourceType, 
                                            resourceId,
                                            userId,
                                            lAttributes);
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(SPublicInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
