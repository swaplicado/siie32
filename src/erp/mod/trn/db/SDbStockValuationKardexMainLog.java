package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 * Registro de respaldo de fk_dps_*_in_main_n antes de que updateKardexDpsMainIn
 * los sobreescriba. Permite revertir el cambio al eliminar la valuación.
 *
 * @author Edwin Carmona
 */
public class SDbStockValuationKardexMainLog extends SDbRegistryUser implements Serializable {

    protected int mnPkLog;
    protected int mnFkStockValuationId;
    protected int mnFkStockValuationKardexId;
    protected int mnFkOldDpsYear;
    protected int mnFkOldDpsDoc;
    protected int mnFkOldDpsEty;

    public SDbStockValuationKardexMainLog() {
        super(SModConsts.TRN_STK_VAL_KARDEX_MAIN_LOG);
    }

    public void setPkLog(int n) { mnPkLog = n; }
    public void setFkStockValuationId(int n) { mnFkStockValuationId = n; }
    public void setFkStockValuationKardexId(int n) { mnFkStockValuationKardexId = n; }
    public void setFkOldDpsYear(int n) { mnFkOldDpsYear = n; }
    public void setFkOldDpsDoc(int n) { mnFkOldDpsDoc = n; }
    public void setFkOldDpsEty(int n) { mnFkOldDpsEty = n; }

    public int getPkLog() { return mnPkLog; }
    public int getFkStockValuationId() { return mnFkStockValuationId; }
    public int getFkStockValuationKardexId() { return mnFkStockValuationKardexId; }
    public int getFkOldDpsYear() { return mnFkOldDpsYear; }
    public int getFkOldDpsDoc() { return mnFkOldDpsDoc; }
    public int getFkOldDpsEty() { return mnFkOldDpsEty; }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkLog = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[]{ mnPkLog };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        mnPkLog = 0;
        mbSystem = false;
        mbDeleted = false;
        mnFkStockValuationId = 0;
        mnFkStockValuationKardexId = 0;
        mnFkOldDpsYear = 0;
        mnFkOldDpsDoc = 0;
        mnFkOldDpsEty = 0;
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
        return "WHERE id_log = " + mnPkLog;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_log = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        mnPkLog = 0;
        msSql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + getSqlTable();
        ResultSet rs = session.getStatement().executeQuery(msSql);
        if (rs.next()) {
            mnPkLog = rs.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        ResultSet rs = session.getStatement().executeQuery(msSql);
        if (!rs.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        mnPkLog = rs.getInt("id_log");
        mbSystem = rs.getBoolean("b_sys");
        mbDeleted = rs.getBoolean("b_del");
        mnFkStockValuationId = rs.getInt("fk_stk_val");
        mnFkStockValuationKardexId = rs.getInt("fk_stk_val_kardex");
        mnFkOldDpsYear = rs.getInt("fk_old_dps_year");
        mnFkOldDpsDoc = rs.getInt("fk_old_dps_doc");
        mnFkOldDpsEty = rs.getInt("fk_old_dps_ety");
        mnFkUserInsertId = rs.getInt("fk_usr_ins");
        mnFkUserUpdateId = rs.getInt("fk_usr_upd");
        mtTsUserInsert = rs.getTimestamp("ts_usr_ins");
        mtTsUserUpdate = rs.getTimestamp("ts_usr_upd");
        mbRegistryNew = false;
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

            msSql = "INSERT INTO " + getSqlTable() + " VALUES ("
                    + mnPkLog + ", "
                    + (mbSystem ? 1 : 0) + ", "
                    + (mbDeleted ? 1 : 0) + ", "
                    + mnFkStockValuationId + ", "
                    + mnFkStockValuationKardexId + ", "
                    + mnFkOldDpsYear + ", "
                    + mnFkOldDpsDoc + ", "
                    + mnFkOldDpsEty + ", "
                    + mnFkUserInsertId + ", "
                    + mnFkUserUpdateId + ", "
                    + "NOW(), NOW())";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET "
                    + "b_sys = " + (mbSystem ? 1 : 0) + ", "
                    + "b_del = " + (mbDeleted ? 1 : 0) + ", "
                    + "fk_stk_val = " + mnFkStockValuationId + ", "
                    + "fk_stk_val_kardex = " + mnFkStockValuationKardexId + ", "
                    + "fk_old_dps_year = " + mnFkOldDpsYear + ", "
                    + "fk_old_dps_doc = " + mnFkOldDpsDoc + ", "
                    + "fk_old_dps_ety = " + mnFkOldDpsEty + ", "
                    + "fk_usr_upd = " + mnFkUserUpdateId + ", "
                    + "ts_usr_upd = NOW() "
                    + getSqlWhere();
        }

        session.getStatement().getConnection().createStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuationKardexMainLog o = new SDbStockValuationKardexMainLog();
        o.setPkLog(mnPkLog);
        o.setSystem(mbSystem);
        o.setDeleted(mbDeleted);
        o.setFkStockValuationId(mnFkStockValuationId);
        o.setFkStockValuationKardexId(mnFkStockValuationKardexId);
        o.setFkOldDpsYear(mnFkOldDpsYear);
        o.setFkOldDpsDoc(mnFkOldDpsDoc);
        o.setFkOldDpsEty(mnFkOldDpsEty);
        o.setFkUserInsertId(mnFkUserInsertId);
        o.setFkUserUpdateId(mnFkUserUpdateId);
        o.setTsUserInsert(mtTsUserInsert);
        o.setTsUserUpdate(mtTsUserUpdate);
        o.setRegistryNew(mbRegistryNew);
        return o;
    }
}
