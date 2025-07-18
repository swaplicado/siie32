/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swapms;

import erp.mod.cfg.swapms.utils.SExportUtils;
import erp.mod.cfg.swapms.utils.SUserExport;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.trn.api.db.STrnDBCore;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interfaz pública para la clase de sincronización de datos.
 * Esta clase proporciona métodos para interactuar con la base de datos y
 * realizar operaciones de sincronización de datos.
 * @author Edwin Carmona
 */
public class SPublicInterface {
    
    SMySqlClass oDbObj;
    String msMainDatabase;

    public SPublicInterface() {
        try {
            this.oDbObj = new SMySqlClass();
            this.msMainDatabase = this.oDbObj.getMainDatabaseName();
        } catch (SConfigException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return Objeto {@code Connection} si la conexión es exitosa, de lo contrario {@code null}.
     */
    private Connection getConnection() {
        try {
            return this.oDbObj.connect("", "", this.msMainDatabase, "", "");
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
    public SUserExport getSupplierByFiscalId(final String fiscalId) {
        try {
            Connection conn = this.getConnection();

            if (conn == null) {
                return null;
            }

            return SExportUtils.getSupplierByFiscalId(conn.createStatement(), fiscalId);
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
