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
    protected int mnPkDiogYearId;
    protected int mnPkDiogDocId;

    public SDbStockValuationDiogAdjust() {
        super(SModConsts.TRN_STK_VAL_DIOG_ADJ);
    }

    public SDbStockValuationDiogAdjust(int mnPkStockValuationId, int mnPkDiogYearId, int mnPkDiogDocId) {
        super(SModConsts.TRN_STK_VAL_DIOG_ADJ);
        this.mnPkStockValuationId = mnPkStockValuationId;
        this.mnPkDiogYearId = mnPkDiogYearId;
        this.mnPkDiogDocId = mnPkDiogDocId;
    }
    
    public void setPkStockValuationId(int n) { mnPkStockValuationId = n; }
    public void setPkDiogYearId(int n) { mnPkDiogYearId = n; }
    public void setPkDiogDocId(int n) { mnPkDiogDocId = n; }
    
    public int getPkStockValuationtId() { return mnPkStockValuationId; }
    public int getPkDiogYearId() { return mnPkDiogYearId; }
    public int getPkDiogDocId() { return mnPkDiogDocId; }

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
        mnPkDiogYearId = 0;
        mnPkDiogDocId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val = " + mnPkStockValuationId 
                + " AND id_year = " + mnPkDiogYearId
                + " AND id_doc = " + mnPkDiogDocId;
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
                mnPkDiogYearId = resultSet.getInt("id_year");
                mnPkDiogDocId = resultSet.getInt("id_doc");
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
                    mnPkDiogYearId + ", " +
                    mnPkDiogDocId + " " +
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
        registry.setPkDiogYearId(this.getPkDiogYearId());
        registry.setPkDiogDocId(this.getPkDiogDocId());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
