/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockValuationMvt extends SDbRegistryUser {
    
    protected int mnPkStockValuationMvtId;
    protected Date mtDateMove;
    protected double mdQuantityEntry;
    protected double mdQuantityConsumption_r;
    protected double mdCostUnitary;
    protected double mdCost_r;
    //protected boolean mbDeleted;
    protected int mnFkDiogCategoryId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFktemReferenceId_n;
    protected int mnFkDiogYear;
    protected int mnFkDiogDocId;
    protected int mnFkDiogEntryId;
    protected int mnFkStockValuationId;
    protected int mnFkStockValuationMvtId_n;
    //protected int mnFkUserInsertId;
    //protected int mnFkUserUpdateId;
    //protected Date mtTsUserInsert;
    //protected Date mtTsUserUpdate;

    protected boolean mbAuxConsumed;
    protected double mdAuxConsumption;
    protected int[] maAuxWarehousePk;

    public SDbStockValuationMvt() {
        super(SModConsts.TRN_STK_VAL_MVT);
    }
    
    public void setPkStockValuationMvtId(int n) { mnPkStockValuationMvtId = n; }
    public void setDateMove(Date t) { mtDateMove = t; }
    public void setQuantityEntry(double d) { mdQuantityEntry = d; }
    public void setQuantityConsumption_r(double d) { mdQuantityConsumption_r = d; }
    public void setCostUnitary(double d) { mdCostUnitary = d; }
    public void setCost_r(double d) { mdCost_r = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDiogCategoryId(int n) { mnFkDiogCategoryId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkItemReference_n(int n) { mnFktemReferenceId_n = n; }
    public void setFkDiogYear(int n) { mnFkDiogYear = n; }
    public void setFkDiogDocId(int n) { mnFkDiogDocId = n; }
    public void setFkDiogEntryId(int n) { mnFkDiogEntryId = n; }
    public void setFkStockValuationId(int n) { mnFkStockValuationId = n; }
    public void setFkStockValuationMvtId_n(int n) { mnFkStockValuationMvtId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setAuxConsumed(boolean b) { mbAuxConsumed = b; }
    public void setAuxConsumption(double d) { mdAuxConsumption = d; }
    public void setAuxWarehousePk(int[] a) { maAuxWarehousePk = a; }
    
    public int getPkStockValuationMvtId() { return mnPkStockValuationMvtId; }
    public Date getDateMove() { return mtDateMove; }
    public double getQuantityEntry() { return mdQuantityEntry; }
    public double getQuantityConsumption() { return mdQuantityConsumption_r; }
    public double getCostUnitary() { return mdCostUnitary; }
    public double getCost_r() { return mdCost_r; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDiogCategoryId() { return mnFkDiogCategoryId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkItemReference_n() { return mnFktemReferenceId_n; }
    public int getFkDiogYear() { return mnFkDiogYear; }
    public int getFkDiogDocId() { return mnFkDiogDocId; }
    public int getFkDiogEntryId() { return mnFkDiogEntryId; }
    public int getFkStockValuationId() { return mnFkStockValuationId; }
    public int getFkStockValuationMvtId_n() { return mnFkStockValuationMvtId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public boolean isAuxConsumed() { return mbAuxConsumed; }
    public double getAuxConsumption() { return mdAuxConsumption; }
    public int[] getAuxWarehousePk() { return maAuxWarehousePk; }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValuationMvtId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValuationMvtId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValuationMvtId = 0;
        mtDateMove = null;
        mdQuantityEntry = 0d;
        mdQuantityConsumption_r = 0d;
        mdCostUnitary = 0d;
        mdCost_r = 0d;
        mbDeleted = false;
        mnFkDiogCategoryId = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFktemReferenceId_n = 0;
        mnFkDiogYear = 0;
        mnFkDiogDocId = 0;
        mnFkDiogEntryId = 0;
        mnFkStockValuationId = 0;
        mnFkStockValuationMvtId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mbAuxConsumed = false;
        mdAuxConsumption = 0d;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val_mvt = " + mnPkStockValuationMvtId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val_mvt = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValuationMvtId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_mvt), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValuationMvtId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStockValuationMvtId = resultSet.getInt("id_stk_val_mvt");
            mtDateMove = resultSet.getDate("dt_mov");
            mdQuantityEntry = resultSet.getDouble("qty_entry");
            mdQuantityConsumption_r = resultSet.getDouble("qty_cons_r");
            mdCostUnitary = resultSet.getDouble("cost_u");
            mdCost_r = resultSet.getDouble("cost_r");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkDiogCategoryId = resultSet.getInt("fk_ct_iog");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFktemReferenceId_n = resultSet.getInt("fk_item_ref_n");
            mnFkDiogYear = resultSet.getInt("fk_diog_year");
            mnFkDiogDocId = resultSet.getInt("fk_diog_doc");
            mnFkDiogEntryId = resultSet.getInt("fk_diog_ety");
            mnFkStockValuationId = resultSet.getInt("fk_stk_val");
            mnFkStockValuationMvtId_n = resultSet.getInt("fk_stk_val_mvt_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkStockValuationMvtId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateMove) + "', " + 
                    mdQuantityEntry + ", " +
                    mdQuantityConsumption_r + ", " +
                    mdCostUnitary + ", " +
                    mdCost_r + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkDiogCategoryId + ", " +
                    mnFkItemId + ", " +
                    mnFkUnitId + ", " +
                    (mnFktemReferenceId_n > 0 ? mnFktemReferenceId_n : "null") + ", " +
                    mnFkDiogYear + ", " +
                    mnFkDiogDocId + ", " +
                    mnFkDiogEntryId + ", " +
                    mnFkStockValuationId + ", " +
                    (mnFkStockValuationMvtId_n > 0 ? mnFkStockValuationMvtId_n : "null") + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")" ;
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_stk_val_mvt = " + mnPkValuationEntryId + ", " +
                    "dt_mov = '" + SLibUtils.DbmsDateFormatDate.format(mtDateMove) + "', " +
                    "qty_entry = " + mdQuantityEntry + ", " +
                    "qty_cons_r = " + mdQuantityConsumption_r + ", " +
                    "cost_u = " + mdCostUnitary + ", " +
                    "cost_r = " + mdCost_r + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_ct_iog = " + mnFkDiogCategoryId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_item_ref_n = " + (mnFktemReferenceId_n > 0 ? mnFktemReferenceId_n : "null") + ", " +
                    "fk_diog_year = " + mnFkDiogYear + ", " +
                    "fk_diog_doc = " + mnFkDiogDocId + ", " +
                    "fk_diog_ety = " + mnFkDiogEntryId + ", " +
                    "fk_stk_val = " + mnFkStockValuationId + ", " +
                    "fk_stk_val_mvt_n = " + (mnFkStockValuationMvtId_n > 0 ? mnFkStockValuationMvtId_n : "null") + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " "  + 
                    getSqlWhere();
        }
        
        session.getStatement().getConnection().createStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuationMvt registry = new SDbStockValuationMvt();
        
        registry.setPkStockValuationMvtId(this.getPkStockValuationMvtId());
        registry.setDateMove(this.getDateMove());
        registry.setQuantityEntry(this.getQuantityEntry());
        registry.setQuantityConsumption_r(this.getQuantityConsumption());
        registry.setCostUnitary(this.getCostUnitary());
        registry.setCost_r(this.getCost_r());
        registry.setAuxConsumed(this.isAuxConsumed());
        registry.setDeleted(this.isDeleted());
        registry.setFkDiogCategoryId(this.getFkDiogCategoryId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkItemReference_n(this.getFkItemReference_n());
        registry.setFkDiogYear(this.getFkDiogYear());
        registry.setFkDiogDocId(this.getFkDiogDocId());
        registry.setFkDiogEntryId(this.getFkDiogEntryId());
        registry.setFkStockValuationId(this.getFkStockValuationId());
        registry.setFkStockValuationMvtId_n(this.getFkStockValuationMvtId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
