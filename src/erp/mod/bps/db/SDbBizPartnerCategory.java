/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.bps.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbBizPartnerCategory extends SDbRegistryUser {

    protected int mnPkBizPartnerId;
    protected int mnPkBizPartnerCategoryId;
    protected String msKey;
    protected String msCompanyKey;
    protected double mdCreditLimit;
    protected int mnDaysOfCredit;
    protected int mnDaysOfGrace;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    protected boolean mbIsCreditByUser;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerCategoryId;
    protected int mnFkBizPartnerTypeId;
    protected int mnFkCreditTypeId_n;
    protected int mnFkCfdAddendaTypeId;
    protected int mnFkLanguageId_n;
    protected int mnFkCurrencyId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;

    public SDbBizPartnerCategory() {
        super(SModConsts.BPSU_BP_CT);
    }

    /*
     * Public methods
     */

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBizPartnerCategoryId(int n) { mnPkBizPartnerCategoryId = n; }
    public void setKey(String s) { msKey = s; }
    public void setCompanyKey(String s) { msCompanyKey = s; }
    public void setCreditLimit(double d) { mdCreditLimit = d; }
    public void setDaysOfCredit(int n) { mnDaysOfCredit = n; }
    public void setDaysOfGrace(int n) { mnDaysOfGrace = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setIsCreditByUser(boolean b) { mbIsCreditByUser = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerCategoryId(int n) { mnFkBizPartnerCategoryId = n; }
    public void setFkBizPartnerTypeId(int n) { mnFkBizPartnerTypeId = n; }
    public void setFkCreditTypeId_n(int n) { mnFkCreditTypeId_n = n; }
    public void setFkCfdAddendaTypeId(int n) { mnFkCfdAddendaTypeId = n; }
    public void setFkLanguageId_n(int n) { mnFkLanguageId_n = n; }
    public void setFkCurrencyId_n(int n) { mnFkCurrencyId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBizPartnerCategoryId() { return mnPkBizPartnerCategoryId; }
    public String getKey() { return msKey; }
    public String getCompanyKey() { return msCompanyKey; }
    public double getCreditLimit() { return mdCreditLimit; }
    public int getDaysOfCredit() { return mnDaysOfCredit; }
    public int getDaysOfGrace() { return mnDaysOfGrace; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean getIsCreditByUser() { return mbIsCreditByUser; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerCategoryId() { return mnFkBizPartnerCategoryId; }
    public int getFkBizPartnerTypeId() { return mnFkBizPartnerTypeId; }
    public int getFkCreditTypeId_n() { return mnFkCreditTypeId_n; }
    public int getFkCfdAddendaTypeId() { return mnFkCfdAddendaTypeId; }
    public int getFkLanguageId_n() { return mnFkLanguageId_n; }
    public int getFkCurrencyId_n() { return mnFkCurrencyId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserNewTs() { return mtUserNewTs; }
    public Date getUserEditTs() { return mtUserEditTs; }
    public Date getUserDeleteTs() { return mtUserDeleteTs; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
        mnPkBizPartnerCategoryId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBizPartnerCategoryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnPkBizPartnerCategoryId = 0;
        msKey = "";
        msCompanyKey = "";
        mdCreditLimit = 0;
        mnDaysOfCredit = 0;
        mnDaysOfGrace = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mbIsCreditByUser = false;
        mbIsDeleted = false;
        mnFkBizPartnerCategoryId = 0;
        mnFkBizPartnerTypeId = 0;
        mnFkCreditTypeId_n = 0;
        mnFkCfdAddendaTypeId = 0;
        mnFkLanguageId_n = 0;
        mnFkCurrencyId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bp = " + mnPkBizPartnerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bp = " + pk[0] + " ";
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
            mnPkBizPartnerId = resultSet.getInt("id_bp");
            mnPkBizPartnerCategoryId = resultSet.getInt("id_ct_bp");
            msKey = resultSet.getString("bp_key");
            msCompanyKey = resultSet.getString("co_key");
            mdCreditLimit = resultSet.getDouble("cred_lim");
            mnDaysOfCredit = resultSet.getInt("days_cred");
            mnDaysOfGrace = resultSet.getInt("days_grace");
            mtDateStart = resultSet.getDate("dt_start");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            mbIsCreditByUser = resultSet.getBoolean("b_cred_usr");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkBizPartnerCategoryId = resultSet.getInt("fid_ct_bp");
            mnFkBizPartnerTypeId = resultSet.getInt("fid_tp_bp");
            mnFkCreditTypeId_n = resultSet.getInt("fid_tp_cred_n");
            mnFkCfdAddendaTypeId = resultSet.getInt("fid_tp_cfd_add");
            mnFkLanguageId_n = resultSet.getInt("fid_lan_n");
            mnFkCurrencyId_n = resultSet.getInt("fid_cur_n");
            mnFkUserNewId = resultSet.getInt("fid_usr_new");
            mnFkUserEditId = resultSet.getInt("fid_usr_edit");
            mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
            mtUserNewTs = resultSet.getTimestamp("ts_new");
            mtUserEditTs = resultSet.getTimestamp("ts_edit");
            mtUserDeleteTs = resultSet.getTimestamp("ts_del");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case SDbRegistry.FIELD_CODE:
                msSql += "bp_key ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlFromWhere(pk);

        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case SDbRegistry.FIELD_CODE:
                    value = resultSet.getString(1);
                    break;
                default:
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
        return value;
    }
}
