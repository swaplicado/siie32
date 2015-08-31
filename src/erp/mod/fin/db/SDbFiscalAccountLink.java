/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbFiscalAccountLink extends SDbRegistryUser {

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected ArrayList<SDbFiscalAccountLinkDetail> maChildDetails;

    public SDbFiscalAccountLink() {
        super(SModConsts.FIN_FISCAL_ACC_LINK);
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbFiscalAccountLinkDetail> getChildDetails() { return maChildDetails; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkPeriodId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkPeriodId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        maChildDetails = new ArrayList<SDbFiscalAccountLinkDetail>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " AND " +
                "id_per = " + mnPkPeriodId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND " +
                "id_per = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        SDbFiscalAccountLinkDetail detail = null;
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
            mnPkYearId = resultSet.getInt("id_year");
            mnPkPeriodId = resultSet.getInt("id_per");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_tp_fiscal_acc_link, id_ref_1, id_ref_2 " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " " + getSqlWhere() +
                    "ORDER BY id_tp_fiscal_acc_link, id_ref_1, id_ref_2 ";

            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                detail = new SDbFiscalAccountLinkDetail();
                detail.read(session, new int[] { mnPkYearId, mnPkPeriodId, resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                if (detail.getQueryResultId() != SDbConsts.READ_OK) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        verifyRegistryNew(session);

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkYearId + ", " +
                    mnPkPeriodId + ", " +
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
                    //"id_year = " + mnPkYearId + ", " +
                    //"id_per = " + mnPkPeriodId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.FIN_FISCAL_ACC_LINK_DET) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (SDbFiscalAccountLinkDetail child : maChildDetails) {
            child.setPkYearId(mnPkYearId);
            child.setPkPeriodId(mnPkPeriodId);
            child.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbFiscalAccountLink clone() throws CloneNotSupportedException {
        SDbFiscalAccountLink registry = new SDbFiscalAccountLink();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkPeriodId(this.getPkPeriodId());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbFiscalAccountLinkDetail child : maChildDetails) {
            registry.getChildDetails().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
