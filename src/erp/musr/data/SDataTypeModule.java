/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import java.sql.Connection;
import java.sql.Statement;

import erp.data.SDataConstants;
import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public class SDataTypeModule extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    public SDataTypeModule() {
        super(SDataConstants.CFGS_TP_MOD);
    }

    @Override
    public void setPrimaryKey(Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int read(Object pk, Statement statement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int save(Connection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }

}
