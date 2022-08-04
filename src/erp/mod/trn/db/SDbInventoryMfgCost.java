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
public class SDbInventoryMfgCost extends SDbRegistryUser {
    
    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnOrders;
    protected int mnOrdersStarted;
    protected int mnOrdersFinished;
    protected double mdQuantityStarted;
    protected double mdQuantityWorkInProgress;
    protected double mdQuantityFinished;
    protected double mdQuantityFinishedPer;
    protected double mdQuantityFinishedEffectivePer;
    protected double mdRmCosts;
    protected double mdRmCostsWorkInProgress;
    protected double mdRmCostsFinishedGoods;
    protected double mdMohCosts;
    protected double mdMohCostsWorkInProgress;
    protected double mdMohCostsFinishedGoods;
    protected double mdCosts;
    protected double mdCostsWorkInProgress;
    protected double mdCostsFinishedGoods;
    protected double mdCostUnitWorkInProgress;
    protected double mdCostUnitFinishedGoods;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbInventoryMfgCost() {
        super(SModConsts.TRN_INV_MFG_CST);
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setOrders(int n) { mnOrders = n; }
    public void setOrdersStarted(int n) { mnOrdersStarted = n; }
    public void setOrdersFinished(int n) { mnOrdersFinished = n; }
    public void setQuantityStarted(double d) { mdQuantityStarted = d; }
    public void setQuantityWorkInProgress(double d) { mdQuantityWorkInProgress = d; }
    public void setQuantityFinished(double d) { mdQuantityFinished = d; }
    public void setQuantityFinishedPer(double d) { mdQuantityFinishedPer = d; }
    public void setQuantityFinishedEffectivePer(double d) { mdQuantityFinishedEffectivePer = d; }
    public void setRmCosts(double d) { mdRmCosts = d; }
    public void setRmCostsWorkInProgress(double d) { mdRmCostsWorkInProgress = d; }
    public void setRmCostsFinishedGoods(double d) { mdRmCostsFinishedGoods = d; }
    public void setMohCosts(double d) { mdMohCosts = d; }
    public void setMohCostsWorkInProgress(double d) { mdMohCostsWorkInProgress = d; }
    public void setMohCostsFinishedGoods(double d) { mdMohCostsFinishedGoods = d; }
    public void setCosts(double d) { mdCosts = d; }
    public void setCostsWorkInProgress(double d) { mdCostsWorkInProgress = d; }
    public void setCostsFinishedGoods(double d) { mdCostsFinishedGoods = d; }
    public void setCostUnitWorkInProgress(double d) { mdCostUnitWorkInProgress = d; }
    public void setCostUnitFinishedGoods(double d) { mdCostUnitFinishedGoods = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getOrders() { return mnOrders; }
    public int getOrdersStarted() { return mnOrdersStarted; }
    public int getOrdersFinished() { return mnOrdersFinished; }
    public double getQuantityStarted() { return mdQuantityStarted; }
    public double getQuantityWorkInProgress() { return mdQuantityWorkInProgress; }
    public double getQuantityFinished() { return mdQuantityFinished; }
    public double getQuantityFinishedPer() { return mdQuantityFinishedPer; }
    public double getQuantityFinishedEffectivePer() { return mdQuantityFinishedEffectivePer; }
    public double getRmCosts() { return mdRmCosts; }
    public double getRmCostsWorkInProgress() { return mdRmCostsWorkInProgress; }
    public double getRmCostsFinishedGoods() { return mdRmCostsFinishedGoods; }
    public double getMohCosts() { return mdMohCosts; }
    public double getMohCostsWorkInProgress() { return mdMohCostsWorkInProgress; }
    public double getMohCostsFinishedGoods() { return mdMohCostsFinishedGoods; }
    public double getCosts() { return mdCosts; }
    public double getCostsWorkInProgress() { return mdCostsWorkInProgress; }
    public double getCostsFinishedGoods() { return mdCostsFinishedGoods; }
    public double getCostUnitWorkInProgress() { return mdCostUnitWorkInProgress; }
    public double getCostUnitFinishedGoods() { return mdCostUnitFinishedGoods; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkPeriodId = pk[1];
        mnPkItemId = pk[2];
        mnPkUnitId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkPeriodId, mnPkItemId, mnPkUnitId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnOrders = 0;
        mnOrdersStarted = 0;
        mnOrdersFinished = 0;
        mdQuantityStarted = 0;
        mdQuantityWorkInProgress = 0;
        mdQuantityFinished = 0;
        mdQuantityFinishedPer = 0;
        mdQuantityFinishedEffectivePer = 0;
        mdRmCosts = 0;
        mdRmCostsWorkInProgress = 0;
        mdRmCostsFinishedGoods = 0;
        mdMohCosts = 0;
        mdMohCostsWorkInProgress = 0;
        mdMohCostsFinishedGoods = 0;
        mdCosts = 0;
        mdCostsWorkInProgress = 0;
        mdCostsFinishedGoods = 0;
        mdCostUnitWorkInProgress = 0;
        mdCostUnitFinishedGoods = 0;
        mbDeleted = false;
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
        return "WHERE id_year = " + mnPkYearId + " AND "
                + "id_per = " + mnPkPeriodId + " AND "
                + "id_item = " + mnPkItemId + " AND "
                + "id_unit = " + mnPkUnitId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND "
                + "id_per = " + pk[1] + " AND "
                + "id_item = " + pk[2] + " AND "
                + "id_unit = " + pk[3] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkYearId = resultSet.getInt("id_year");
            mnPkPeriodId = resultSet.getInt("id_per");
            mnPkItemId = resultSet.getInt("id_item");
            mnPkUnitId = resultSet.getInt("id_unit");
            mnOrders = resultSet.getInt("ord");
            mnOrdersStarted = resultSet.getInt("ord_sta");
            mnOrdersFinished = resultSet.getInt("ord_fin");
            mdQuantityStarted = resultSet.getDouble("qty_sta");
            mdQuantityWorkInProgress = resultSet.getDouble("qty_wip");
            mdQuantityFinished = resultSet.getDouble("qty_fin");
            mdQuantityFinishedPer = resultSet.getDouble("qty_fin_per");
            mdQuantityFinishedEffectivePer = resultSet.getDouble("qty_fin_eff_per");
            mdRmCosts = resultSet.getDouble("amc");
            mdRmCostsWorkInProgress = resultSet.getDouble("amc_wip");
            mdRmCostsFinishedGoods = resultSet.getDouble("amc_fin");
            mdMohCosts = resultSet.getDouble("moh");
            mdMohCostsWorkInProgress = resultSet.getDouble("moh_wip");
            mdMohCostsFinishedGoods = resultSet.getDouble("moh_fin");
            mdCosts = resultSet.getDouble("cst");
            mdCostsWorkInProgress = resultSet.getDouble("cst_wip");
            mdCostsFinishedGoods = resultSet.getDouble("cst_fin");
            mdCostUnitWorkInProgress = resultSet.getDouble("cst_u_wip");
            mdCostUnitFinishedGoods = resultSet.getDouble("cst_u_fin");
            mbDeleted = resultSet.getBoolean("b_del");
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
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkYearId + ", " + 
                    mnPkPeriodId + ", " + 
                    mnPkItemId + ", " + 
                    mnPkUnitId + ", " + 
                    mnOrders + ", " + 
                    mnOrdersStarted + ", " + 
                    mnOrdersFinished + ", " + 
                    mdQuantityStarted + ", " + 
                    mdQuantityWorkInProgress + ", " + 
                    mdQuantityFinished + ", " + 
                    mdQuantityFinishedPer + ", " + 
                    mdQuantityFinishedEffectivePer + ", " + 
                    mdRmCosts + ", " + 
                    mdRmCostsWorkInProgress + ", " + 
                    mdRmCostsFinishedGoods + ", " + 
                    mdMohCosts + ", " + 
                    mdMohCostsWorkInProgress + ", " + 
                    mdMohCostsFinishedGoods + ", " + 
                    mdCosts + ", " + 
                    mdCostsWorkInProgress + ", " + 
                    mdCostsFinishedGoods + ", " + 
                    mdCostUnitWorkInProgress + ", " + 
                    mdCostUnitFinishedGoods + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_year = " + mnPkYearId + ", " +
                    "id_per = " + mnPkPeriodId + ", " +
                    "id_item = " + mnPkItemId + ", " +
                    "id_unit = " + mnPkUnitId + ", " +
                    */
                    "ord = " + mnOrders + ", " +
                    "ord_sta = " + mnOrdersStarted + ", " +
                    "ord_fin = " + mnOrdersFinished + ", " +
                    "qty_sta = " + mdQuantityStarted + ", " +
                    "qty_wip = " + mdQuantityWorkInProgress + ", " +
                    "qty_fin = " + mdQuantityFinished + ", " +
                    "qty_fin_per = " + mdQuantityFinishedPer + ", " +
                    "qty_fin_eff_per = " + mdQuantityFinishedEffectivePer + ", " +
                    "amc = " + mdRmCosts + ", " +
                    "amc_wip = " + mdRmCostsWorkInProgress + ", " +
                    "amc_fin = " + mdRmCostsFinishedGoods + ", " +
                    "moh = " + mdMohCosts + ", " +
                    "moh_wip = " + mdMohCostsWorkInProgress + ", " +
                    "moh_fin = " + mdMohCostsFinishedGoods + ", " +
                    "cst = " + mdCosts + ", " +
                    "cst_wip = " + mdCostsWorkInProgress + ", " +
                    "cst_fin = " + mdCostsFinishedGoods + ", " +
                    "cst_u_wip = " + mdCostUnitWorkInProgress + ", " +
                    "cst_u_fin = " + mdCostUnitFinishedGoods + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbInventoryMfgCost  clone() throws CloneNotSupportedException {
        SDbInventoryMfgCost registry = new SDbInventoryMfgCost();
        
        registry.setPkYearId(this.getPkYearId());
        registry.setPkPeriodId(this.getPkPeriodId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setOrders(this.getOrders());
        registry.setOrdersStarted(this.getOrdersStarted());
        registry.setOrdersFinished(this.getOrdersFinished());
        registry.setQuantityStarted(this.getQuantityStarted());
        registry.setQuantityWorkInProgress(this.getQuantityWorkInProgress());
        registry.setQuantityFinished(this.getQuantityFinished());
        registry.setQuantityFinishedPer(this.getQuantityFinishedPer());
        registry.setQuantityFinishedEffectivePer(this.getQuantityFinishedEffectivePer());
        registry.setRmCosts(this.getRmCosts());
        registry.setRmCostsWorkInProgress(this.getRmCostsWorkInProgress());
        registry.setRmCostsFinishedGoods(this.getRmCostsFinishedGoods());
        registry.setMohCosts(this.getMohCosts());
        registry.setMohCostsWorkInProgress(this.getMohCostsWorkInProgress());
        registry.setMohCostsFinishedGoods(this.getMohCostsFinishedGoods());
        registry.setCosts(this.getCosts());
        registry.setCostsWorkInProgress(this.getCostsWorkInProgress());
        registry.setCostsFinishedGoods(this.getCostsFinishedGoods());
        registry.setCostUnitWorkInProgress(this.getCostUnitWorkInProgress());
        registry.setCostUnitFinishedGoods(this.getCostUnitFinishedGoods());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
