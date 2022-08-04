/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbIdentifiedCostLot extends SDbRegistryUser {

    public static final String COST_UNIT_TP_NA = "NA"; // not available
    public static final String COST_UNIT_TP_STK = "STK"; // from stock
    public static final String COST_UNIT_TP_MFG = "MFG"; // from manufacturing
    public static final String CALC_ISSUE_ZERO = "ZERO"; // zero cost
    public static final String CALC_ISSUE_NULL = "NULL"; // null cost
    public static final String CALC_ISSUE_SUP = "SUP"; // non-existing supply
    public static final String CALC_ISSUE_LOT = "LOT"; // non-existing lot
    
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLotId;
    protected int mnPkCalculationId;
    protected double mdCostUnit;
    protected String msCostUnitType; // "STK" = from stock; "MFG" = from manufacturing
    protected String msCalculationIssue; // [BLANK] = no issues; "ZERO" = zero cost; "NULL" = null (undefined) cost
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkJointMfgItemId_n;
    protected int mnFkJointMfgUnitId_n;
    protected int mnFkJointMfgLotId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbIdentifiedCostLot() {
        super(SModConsts.TRN_COST_IDENT_LOT);
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
    public void setPkCalculationId(int n) { mnPkCalculationId = n; }
    public void setCostUnit(double d) { mdCostUnit = d; }
    /**
     * Set unit cost type.
     * @param s Available options: constants COST_UNIT_TP_..., i.e., "STK" = from stock; "MFG" = from manufacturing.
     */
    public void setCostUnitType(String s) { msCostUnitType = s; }
    /**
     * Set calculation issue.
     * @param s Available options: constants CALC_ISSUE_..., i.e., [BLANK] = no issues; "ZERO" = zero cost; "NULL" = null (undefined) cost.
     */
    public void setCalculationIssue(String s) { msCalculationIssue = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkJointMfgItemId_n(int n) { mnFkJointMfgItemId_n = n; }
    public void setFkJointMfgUnitId_n(int n) { mnFkJointMfgUnitId_n = n; }
    public void setFkJointMfgLotId_n(int n) { mnFkJointMfgLotId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLotId() { return mnPkLotId; }
    public int getPkCalculationId() { return mnPkCalculationId; }
    public double getCostUnit() { return mdCostUnit; }
    /**
     * Get unit cost type.
     * @return "STK" = from stock; "MFG" = from manufacturing.
     */
    public String getCostUnitType() { return msCostUnitType; }
    /**
     * Get calculation issue.
     * @return [BLANK] = no issues; "ZERO" = zero cost; "NULL" = null (undefined) cost.
     */
    public String getCalculationIssue() { return msCalculationIssue; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkJointMfgItemId_n() { return mnFkJointMfgItemId_n; }
    public int getFkJointMfgUnitId_n() { return mnFkJointMfgUnitId_n; }
    public int getFkJointMfgLotId_n() { return mnFkJointMfgLotId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setLotKey(int[] key) {
        mnPkItemId = key[0];
        mnPkUnitId = key[1];
        mnPkLotId = key[2];
    }

    public int[] getLotKey() {
        return new int[] { mnPkItemId, mnPkUnitId, mnPkLotId };
    }

    public String getLotKeyAsString() {
        return SLibUtils.textKey(getLotKey());
    }

    /**
     * Set joint manufacturing lot key. Can be <code>null</code>.
     * @param key Joint manufacturing lot key.
     */
    public void setJountMfgLotKey(int[] key) {
        if (key == null) {
            mnFkJointMfgItemId_n = 0;
            mnFkJointMfgUnitId_n = 0;
            mnFkJointMfgLotId_n = 0;
        }
        else {
            mnFkJointMfgItemId_n = key[0];
            mnFkJointMfgUnitId_n = key[1];
            mnFkJointMfgLotId_n = key[2];
        }
    }

    /**
     * Get joint manufacturing lot key. Can be <code>null</code>.
     * @return 
     */
    public int[] getJointMfgLotKey() {
        if (mnFkJointMfgItemId_n == 0 || mnFkJointMfgUnitId_n == 0 || mnFkJointMfgLotId_n == 0) {
            setJountMfgLotKey(null);
            return null;
        }
        else {
            return new int[] { mnFkJointMfgItemId_n, mnFkJointMfgUnitId_n, mnFkJointMfgLotId_n };
        }
    }

    /**
     * Get joint manufacturing lot key as <code>String</code>. Can be empty if key is <code>null</code>.
     * @return 
     */
    public String getJointMfgLotKeyAsString() {
        int[] key = getJointMfgLotKey();
        return key == null ? "" : SLibUtils.textKey(key);
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
        mnPkUnitId = pk[1];
        mnPkLotId = pk[2];
        mnPkCalculationId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkUnitId, mnPkLotId, mnPkCalculationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkLotId = 0;
        mnPkCalculationId = 0;
        mdCostUnit = 0;
        msCostUnitType = "";
        msCalculationIssue = "";
        mbDeleted = false;
        mnFkJointMfgItemId_n = 0;
        mnFkJointMfgUnitId_n = 0;
        mnFkJointMfgLotId_n = 0;
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
        return "WHERE "
                + "id_item = " + mnPkItemId + " AND "
                + "id_unit = " + mnPkUnitId + " AND "
                + "id_lot = " + mnPkLotId + " AND "
                + "id_calc = " + mnPkCalculationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE "
                + "id_item = " + pk[0] + " AND "
                + "id_unit = " + pk[1] + " AND "
                + "id_lot = " + pk[2] + " AND "
                + "id_calc = " + pk[3] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkItemId = resultSet.getInt("id_item");
            mnPkUnitId = resultSet.getInt("id_unit");
            mnPkLotId = resultSet.getInt("id_lot");
            mnPkCalculationId = resultSet.getInt("id_calc");
            mdCostUnit = resultSet.getDouble("cost_u");
            msCostUnitType = resultSet.getString("cost_u_tp");
            msCalculationIssue = resultSet.getString("calc_issue");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkJointMfgItemId_n = resultSet.getInt("fk_jm_item_n");
            mnFkJointMfgUnitId_n = resultSet.getInt("fk_jm_unit_n");
            mnFkJointMfgLotId_n = resultSet.getInt("fk_jm_lot_n");
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
                    mnPkItemId + ", " + 
                    mnPkUnitId + ", " + 
                    mnPkLotId + ", " + 
                    mnPkCalculationId + ", " + 
                    mdCostUnit + ", " + 
                    "'" + msCostUnitType + "', " + 
                    "'" + msCalculationIssue + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mnFkJointMfgItemId_n == 0 ? "NULL" : mnFkJointMfgItemId_n) + ", " + 
                    (mnFkJointMfgUnitId_n == 0 ? "NULL" : mnFkJointMfgUnitId_n) + ", " + 
                    (mnFkJointMfgLotId_n == 0 ? "NULL" : mnFkJointMfgLotId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_item = " + mnPkItemId + ", " +
                    //"id_unit = " + mnPkUnitId + ", " +
                    //"id_lot = " + mnPkLotId + ", " +
                    //"id_calc = " + mnPkCalculationId + ", " +
                    "cost_u = " + mdCostUnit + ", " +
                    "cost_u_tp = '" + msCostUnitType + "', " +
                    "calc_issue = '" + msCalculationIssue + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_jm_item_n = " + (mnFkJointMfgItemId_n == 0 ? "NULL" : mnFkJointMfgItemId_n) + ", " +
                    "fk_jm_unit_n = " + (mnFkJointMfgUnitId_n == 0 ? "NULL" : mnFkJointMfgUnitId_n) + ", " +
                    "fk_jm_lot_n = " + (mnFkJointMfgLotId_n == 0 ? "NULL" : mnFkJointMfgLotId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbIdentifiedCostLot clone() throws CloneNotSupportedException {
        SDbIdentifiedCostLot registry = new SDbIdentifiedCostLot();

        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkLotId(this.getPkLotId());
        registry.setPkCalculationId(this.getPkCalculationId());
        registry.setCostUnit(this.getCostUnit());
        registry.setCostUnitType(this.getCostUnitType());
        registry.setCalculationIssue(this.getCalculationIssue());
        registry.setDeleted(this.isDeleted());
        registry.setFkJointMfgItemId_n(this.getFkJointMfgItemId_n());
        registry.setFkJointMfgUnitId_n(this.getFkJointMfgUnitId_n());
        registry.setFkJointMfgLotId_n(this.getFkJointMfgLotId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    /**
     * Create a new instance of identified cost for lot.
     * @param lotKey Lot key: item ID, unit ID, lot ID.
     * @param costUnit Unit cost for lot.
     * @param costUnitType Unit cost type. Available options: constants COST_UNIT_TP_..., i.e., "STK" = from stock; "MFG" = from manufacturing.
     * @param jointMfgLotKey_n Joint manufacturing lot key: item ID, unit ID, lot ID. Can be <code>null</code>.
     * @return New instance of identified cost for lot.
     */
    public static SDbIdentifiedCostLot createIdentifiedCostLot(final int[] lotKey, final Double costUnit, final String costUnitType, final int[] jointMfgLotKey_n) {
        SDbIdentifiedCostLot identifiedCostLot = new SDbIdentifiedCostLot();
        
        identifiedCostLot.setLotKey(lotKey);
        identifiedCostLot.setCostUnitType(costUnitType);

        if (costUnit == null) {
            identifiedCostLot.setCostUnit(0.0);
            identifiedCostLot.setCalculationIssue(SDbIdentifiedCostLot.CALC_ISSUE_NULL);
        }
        else {
            identifiedCostLot.setCostUnit(costUnit);
            if (costUnit == 0.0) {
                identifiedCostLot.setCalculationIssue(SDbIdentifiedCostLot.CALC_ISSUE_ZERO);
            }
        }
        
        return identifiedCostLot;
    }
    
    /**
     * Get all existing active identified costs for lot.
     * @param session GUI session.
     * @param lotKey Lot key: item ID, unit ID, lot ID.
     * @return
     * @throws Exception 
     */
    public static ArrayList<SDbIdentifiedCostLot> getIdentifiedCostLots(final SGuiSession session, final int[] lotKey) throws Exception {
        ArrayList<SDbIdentifiedCostLot> identifiedCostLots = new ArrayList<>();
        int itemId = lotKey[0];
        int unitId = lotKey[1];
        int lotId = lotKey[2];
        
        String sql = "SELECT cic.id_calc "
                + "FROM trn_cost_ident_lot AS cil "
                + "INNER JOIN trn_cost_ident_calc AS cic ON cic.id_calc = cil.id_calc "
                + "WHERE NOT cil.b_del AND NOT cic.b_del "
                + "AND cil.id_item = " + itemId + " AND cil.id_unit = " + unitId + " AND cil.id_lot = " + lotId + " "
                + "ORDER BY cic.dt_end DESC, cic.id_calc DESC;";
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbIdentifiedCostLot identifiedCostLot = new SDbIdentifiedCostLot();
                identifiedCostLot.read(session, new int[] { itemId, unitId, lotId, resultSet.getInt("cic.id_calc") });
                identifiedCostLots.add(identifiedCostLot);
            }
        }
        
        return identifiedCostLots;
    }
}
