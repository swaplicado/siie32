/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

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
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbSsContributionTable extends SDbRegistryUser {

    protected int mnPkSsContributionTableId;
    protected Date mtDateStart;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected ArrayList<SDbSsContributionTableRow> maChildRows;

    public SDbSsContributionTable() {
        super(SModConsts.HRS_SSC);
    }

    public void setPkSsContributionTableId(int n) { mnPkSsContributionTableId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSsContributionTableId() { return mnPkSsContributionTableId; }
    public Date getDateStart() { return mtDateStart; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbSsContributionTableRow> getChildRows() { return maChildRows; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSsContributionTableId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSsContributionTableId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSsContributionTableId = 0;
        mtDateStart = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        maChildRows = new ArrayList<SDbSsContributionTableRow>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ssc = " + mnPkSsContributionTableId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ssc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSsContributionTableId = 0;

        msSql = "SELECT COALESCE(MAX(id_ssc), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSsContributionTableId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnPkSsContributionTableId = resultSet.getInt("id_ssc");
            mtDateStart = resultSet.getDate("dt_sta");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            maChildRows.clear();

            msSql = "SELECT id_row FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SSC_ROW) + " "
                    + "WHERE id_ssc = " + mnPkSsContributionTableId + " "
                    + "ORDER BY id_row ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                maChildRows.add((SDbSsContributionTableRow) session.readRegistry(SModConsts.HRS_SSC_ROW, new int[] { mnPkSsContributionTableId, resultSet.getInt(1) }));
            }

            // Finish registry reading:

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
                    mnPkSsContributionTableId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
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
                    //"id_ssc = " + mnPkSsContributionTableId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SSC_ROW) + " "
                + "WHERE id_ssc = " + mnPkSsContributionTableId + " ";

        session.getStatement().execute(msSql);

        for (SDbSsContributionTableRow child : maChildRows) {
            child.setPkSsContributionTableId(mnPkSsContributionTableId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry saving:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSsContributionTable clone() throws CloneNotSupportedException {
        SDbSsContributionTable registry = new SDbSsContributionTable();

        registry.setPkSsContributionTableId(this.getPkSsContributionTableId());
        registry.setDateStart(this.getDateStart());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
