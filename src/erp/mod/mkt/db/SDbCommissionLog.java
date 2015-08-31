/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.mkt.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbCommissionLog extends SDbRegistryUser {

    protected int mnPkLogId;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDbCommissionLog() {
        super(SModConsts.MKT_COMMS_LOG);
    }

    /*
     * Public methods
     */

    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkLogId() { return mnPkLogId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLogId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLogId = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_log = " + mnPkLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_log = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLogId = 0;

        msSql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLogId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        ResultSet resultSet = null;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkLogId = resultSet.getInt("id_log");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLogId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    mnFkUserId + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    // "id_log = " + mnPkLogId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "fk_usr = " + mnFkUserId + ", " +
                    "ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCommissionLog clone() throws CloneNotSupportedException {
        SDbCommissionLog registry = new SDbCommissionLog();

        registry.setPkLogId(this.getPkLogId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        return registry;
    }
}
