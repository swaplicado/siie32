/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.mkt.db;

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
 * @author Néstor Ávalos
 */
public class SDbCommissionPayment extends SDbRegistryUser {

    protected int mnPkPaymentId;
    protected int mnNumber;
    protected Date mtDate;
    protected double mdPayment_r;
    protected double mdRefund_r;
    protected double mdTotal_r;
    protected boolean mbAnnulled;
    //protected boolean mbDeleted;
    protected int mnFkSalesAgentId;
    protected int mnFkUserAnnulledId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserAnnulled;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected ArrayList<SDbCommissionPaymentEntry> maChildCommisionPayment;

    public SDbCommissionPayment() {
        super(SModConsts.MKT_COMMS_PAY);
    }

    /*
     * Private methods
     */

    public void computeNumber(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnNumber = 0;

        msSql = "SELECT COALESCE(MAX(num), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnNumber = resultSet.getInt(1);
        }
    }

    /*
     * Public methods
     */

    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setPayment_r(double d) { mdPayment_r = d; }
    public void setRefund_r(double d) { mdRefund_r = d; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setAnnulled(boolean b) { mbAnnulled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkSalesAgentId(int n) { mnFkSalesAgentId = n; }
    public void setFkUserAnnulledId(int n) { mnFkUserAnnulledId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdatetId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserAnnulled(Date t) { mtTsUserAnnulled = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public double getPayment_r() { return mdPayment_r; }
    public double getRefund_r() { return mdRefund_r; }
    public double getTotal_r() { return mdTotal_r; }
    public boolean isAnnulled() { return mbAnnulled; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkSalesAgentId() { return mnFkSalesAgentId; }
    public int getFkUserAnnulledId() { return mnFkUserAnnulledId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserAnnulled() { return mtTsUserAnnulled; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbCommissionPaymentEntry> getChildCommisionPayment() { return maChildCommisionPayment; }

    public void getRows(final SGuiSession session, final int[] key) throws SQLException {
        String sql = "";

        SMktCommissionPaymentRow row = null;

        ResultSet resultSet = null;
        Statement statement = null;

        // maCommissionPaymentRows.clear();

        sql = "SELECT p.*, b.bp " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY_ETY) + " AS pe ON " +
            "p.id_pay = pe.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS) + " AS c ON " +
            "pe.fk_year = c.id_year AND pe.fk_doc = c.id_doc AND pe.fk_ety = c.id_ety AND pe.fk_sal_agt = c.id_sal_agt " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON " +
            "p.fk_sal_agt = b.id_bp " +
            "WHERE p.b_del = 0 AND p.b_ann = 0 AND " +
                "c.id_year = " + key[0]  + " AND " +
                "c.id_doc = " + key[1] +  " AND " +
                "c.id_ety = " + key[2] +  " AND " +
                "c.id_sal_agt = " + key[3] +  " " +
            "ORDER BY p.num ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {

            row = new SMktCommissionPaymentRow();

            row.setPkPaymentId(resultSet.getInt("p.id_pay"));
            row.setNumber(resultSet.getInt("p.num"));
            row.setDate(resultSet.getDate("p.dt"));
            row.setPayment_r(resultSet.getDouble("p.pay_r"));
            row.setRefund_r(resultSet.getDouble("p.rfd_r"));
            row.setTotal_r(resultSet.getDouble("p.tot_r"));
            row.setSalesAgent(resultSet.getString("b.bp"));
            row.setSalesAgent(resultSet.getString("b.bp"));
            row.setCurrencyKey("MXN");

            // maCommissionPaymentRows.add(row);
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPaymentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPaymentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPaymentId = 0;
        mnNumber = 0;
        mtDate = null;
        mdPayment_r = 0;
        mdRefund_r = 0;
        mdTotal_r = 0;
        mbAnnulled = false;
        mbDeleted = false;
        mnFkSalesAgentId = 0;
        mnFkUserAnnulledId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserAnnulled = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        maChildCommisionPayment = new ArrayList<SDbCommissionPaymentEntry>();
        // maCommissionPaymentRows = new ArrayList<SMktCommissionPaymentRow>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPaymentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPaymentId = 0;

        msSql = "SELECT COALESCE(MAX(id_pay), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPaymentId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;

        SDbCommissionPaymentEntry child = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkPaymentId = resultSet.getInt("id_pay");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            mdPayment_r = resultSet.getDouble("pay_r");
            mdRefund_r = resultSet.getDouble("rfd_r");
            mdTotal_r = resultSet.getDouble("tot_r");
            mbAnnulled = resultSet.getBoolean("b_ann");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkSalesAgentId = resultSet.getInt("fk_sal_agt");
            mnFkUserAnnulledId = resultSet.getInt("fk_usr_ann");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserAnnulled = resultSet.getTimestamp("ts_usr_ann");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        statement = session.getStatement().getConnection().createStatement();
        msSql = "SELECT pe.id_pay, pe.id_ety " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY) + " AS p " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY_ETY) + " AS pe ON " +
                "p.id_pay = pe.id_pay " +
                "WHERE pe.id_pay = " + mnPkPaymentId + "; ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            child = new SDbCommissionPaymentEntry();
            child.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            maChildCommisionPayment.add(child);
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        Statement statement = null;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            computeNumber(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserAnnulledId = SUtilConsts.USR_NA_ID;
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPaymentId + ", " +
                    mnNumber + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdPayment_r + ", " +
                    mdRefund_r + ", " +
                    mdTotal_r + ", " +
                    (mbAnnulled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkSalesAgentId + ", " +
                    mnFkUserAnnulledId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pay = " + mnPkPaymentId + ", " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "pay_r = " + mdPayment_r + ", " +
                    "rfd_r = " + mdRefund_r + ", " +
                    "tot_r = " + mdTotal_r + ", " +
                    "b_ann = " + (mbAnnulled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_sal_agt = " + mnFkSalesAgentId + ", " +
                    "fk_usr_ann = " + mnFkUserAnnulledId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ann = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        statement = session.getDatabase().getConnection().createStatement();
        // Delete entries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY_ETY) + " WHERE id_pay = " + mnPkPaymentId + " ";
        statement.executeUpdate(msSql);

        // Save destinies:

        for (SDbCommissionPaymentEntry entry : maChildCommisionPayment) {

            entry.setPkPaymentId(mnPkPaymentId);
            entry.setPkEntryId(0);
            entry.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCommissionPayment clone() throws CloneNotSupportedException {
        SDbCommissionPayment registry = new SDbCommissionPayment();

        registry.setPkPaymentId(this.getPkPaymentId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setPayment_r(this.getPayment_r());
        registry.setRefund_r(this.getRefund_r());
        registry.setTotal_r(this.getTotal_r());
        registry.setAnnulled(this.isAnnulled());
        registry.setDeleted(this.isDeleted());
        registry.setFkSalesAgentId(this.getFkSalesAgentId());
        registry.setFkUserAnnulledId(this.getFkUserAnnulledId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserAnnulled(this.getTsUserAnnulled());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbCommissionPaymentEntry child : this.getChildCommisionPayment()) {
            registry.getChildCommisionPayment().add(child.clone());
        }

        return registry;
    }
}
