/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap;

import erp.mod.cfg.swap.utils.SExportDataUser;
import erp.mod.cfg.swap.utils.SExportDataUtils;
import erp.mod.cfg.swap.utils.SResourceStatusResponse;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.trn.api.db.STrnDBCore;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
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

    public SPublicInterface(String sjon) {
        try {
            SMySqlClass.setJsonConn(sjon);
            this.oDbObj = new SMySqlClass();
            this.msMainDatabase = this.oDbObj.getMainDatabaseName(0);
        }
        catch (SConfigException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public SResourceStatusResponse updateResourceStatus(final int companyId, 
                                                            final int resourceType, 
                                                            final String resourceId, 
                                                            final int authStatusId, 
                                                            final int userId,
                                                            final String notes) {
        try {
            SResourceStatusResponse oResponse;
            Connection conn = this.getConnection(companyId);

            if (conn == null) {
                oResponse = new SResourceStatusResponse();
                oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
                oResponse.message = "No se pudo establecer conexión con la base de datos de SIIE.";
                oResponse.error = "No se pudo establecer conexión con la base de datos de SIIE.";
                oResponse.data = null;
                
                return oResponse;
            }

            return SExportDataUtils.updateResourceStatus(conn.createStatement(), 
                                                        companyId, 
                                                        resourceType, 
                                                        resourceId, 
                                                        authStatusId,
                                                        userId,
                                                        notes);
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
