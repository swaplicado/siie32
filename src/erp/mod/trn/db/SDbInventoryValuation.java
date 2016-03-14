package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbInventoryValuation extends SDbRegistryUser {
    
    protected int mnPkInventoryValuationId;
    protected boolean mbFinished;
    //protected boolean mbDeleted;
    protected int mnFkYearYearId;
    protected int mnFkYearPeriodId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbInventoryValuation() {
        super(SModConsts.TRN_INV_VAL);
    }

    public void setPkInventoryValuationId(int n) { mnPkInventoryValuationId = n; }
    public void setFinished(boolean b) { mbFinished = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkYearYearId(int n) { mnFkYearYearId = n; }
    public void setFkYearPeriodId(int n) { mnFkYearPeriodId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkInventoryValuationId() { return mnPkInventoryValuationId; }
    public boolean isFinished() { return mbFinished; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkYearYearId() { return mnFkYearYearId; }
    public int getFkYearPeriodId() { return mnFkYearPeriodId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkInventoryValuationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkInventoryValuationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkInventoryValuationId = 0;
        mbFinished = false;
        mbDeleted = false;
        mnFkYearYearId = 0;
        mnFkYearPeriodId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_inv_val = " + mnPkInventoryValuationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_inv_val = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkInventoryValuationId = 0;

        msSql = "SELECT COALESCE(MAX(id_inv_val), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkInventoryValuationId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkInventoryValuationId = resultSet.getInt("id_inv_val");
            mbFinished = resultSet.getBoolean("b_fin");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkYearYearId = resultSet.getInt("fk_year_year");
            mnFkYearPeriodId = resultSet.getInt("fk_year_per");
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
        STrnInventoryValuation inventoryValuation = new STrnInventoryValuation(session, mnFkYearYearId, mnFkYearPeriodId);
        
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        inventoryValuation.prepareValuation(); // must be called before saving inventory valuation registry

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkInventoryValuationId + ", " + 
                    (mbFinished ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkYearYearId + ", " + 
                    mnFkYearPeriodId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }
        
        session.getStatement().execute(msSql);
        
        inventoryValuation.computeValuation(mnPkInventoryValuationId); // must be called after saving inventory valuation registry
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbInventoryValuation  clone() throws CloneNotSupportedException {
        SDbInventoryValuation registry = new SDbInventoryValuation();
        
        registry.setPkInventoryValuationId(this.getPkInventoryValuationId());
        registry.setFinished(this.isFinished());
        registry.setDeleted(this.isDeleted());
        registry.setFkYearYearId(this.getFkYearYearId());
        registry.setFkYearPeriodId(this.getFkYearPeriodId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
