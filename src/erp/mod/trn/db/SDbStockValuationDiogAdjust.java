/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockValuationDiogAdjust extends SDbRegistryUser {
    
    protected int mnPkStockValuationId;
    protected int mnPkDiogIdYear;
    protected int mnPkDiogIdDoc;

    public SDbStockValuationDiogAdjust() {
        super(SModConsts.TRN_STK_VAL_DIOG_ADJ);
    }

    public SDbStockValuationDiogAdjust(int mnPkStockValuationId, int mnPkDiogIdYear, int mnPkDiogIdDoc) {
        super(SModConsts.TRN_STK_VAL_DIOG_ADJ);
        this.mnPkStockValuationId = mnPkStockValuationId;
        this.mnPkDiogIdYear = mnPkDiogIdYear;
        this.mnPkDiogIdDoc = mnPkDiogIdDoc;
    }
    
    public void setPkStockValuationId(int n) { mnPkStockValuationId = n; }
    public void setPkDiogIdYear(int n) { mnPkDiogIdYear = n; }
    public void setPkDiogIdDoc(int n) { mnPkDiogIdDoc = n; }
    
    public int getPkStockValuationtId() { return mnPkStockValuationId; }
    public int getPkDiogIdYear() { return mnPkDiogIdYear; }
    public int getPkDiogIdDoc() { return mnPkDiogIdDoc; }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValuationId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValuationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValuationId = 0;
        mnPkDiogIdYear = 0;
        mnPkDiogIdDoc = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val = " + mnPkStockValuationId 
                + " AND id_year = " + mnPkDiogIdYear
                + " AND id_doc = " + mnPkDiogIdDoc;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val = " + pk[0] 
                + " AND id_year = " + pk[1]
                + " AND id_doc = " + pk[2];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkStockValuationId = resultSet.getInt("id_stk_val");
                mnPkDiogIdYear = resultSet.getInt("id_year");
                mnPkDiogIdDoc = resultSet.getInt("id_doc");
            }

            mnQueryResultId = SDbConsts.READ_OK;
            resultSet.close();
        }
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkStockValuationId + ", " + 
                    mnPkDiogIdYear + ", " +
                    mnPkDiogIdDoc + " " +
                    ")" ;
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "";

        }
        
        try {
            session.getStatement().getConnection().createStatement().execute(msSql);
        }
        catch (SQLException ex) {
            session.getStatement().getConnection().rollback();
            Logger.getLogger(SDbStockValuationDiogAdjust.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex);
        }
        catch (Exception ex) {
            session.getStatement().getConnection().rollback();
            Logger.getLogger(SDbStockValuationDiogAdjust.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException {
        
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuationDiogAdjust registry = new SDbStockValuationDiogAdjust();
        
        registry.setPkStockValuationId(this.getPkStockValuationtId());
        registry.setPkDiogIdYear(this.getPkDiogIdYear());
        registry.setPkDiogIdDoc(this.getPkDiogIdDoc());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
