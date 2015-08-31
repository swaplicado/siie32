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
public class SDbBizPartnerBranch extends SDbRegistryUser {

    protected int mnPkBizPartnerBranchId;
    protected String msBizPartnerBranch;
    protected String msCode;
    protected boolean mbIsAddressPrintable;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerId;
    protected int mnFkBizPartnerBranchTypeId;
    protected int mnFkTaxRegionId_n;
    protected int mnFkAddressFormatTypeId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;

    public SDbBizPartnerBranch() {
        super(SModConsts.BPSU_BPB);
    }

    /*
     * Public methods
     */

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setBizPartnerBranch(String s) { msBizPartnerBranch = s; }
    public void setCode(String s) { msCode = s; }
    public void setIsAddressPrintable(boolean b) { mbIsAddressPrintable = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkBizPartnerBranchTypeId(int n) { mnFkBizPartnerBranchTypeId = n; }
    public void setFkTaxRegionId_n(int n) { mnFkTaxRegionId_n = n; }
    public void setFkAddressFormatTypeId_n(int n) { mnFkAddressFormatTypeId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public String getBizPartnerBranch() { return msBizPartnerBranch; }
    public String getCode() { return msCode; }
    public boolean getIsAddressPrintable() { return mbIsAddressPrintable; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkBizPartnerBranchTypeId() { return mnFkBizPartnerBranchTypeId; }
    public int getFkTaxRegionId_n() { return mnFkTaxRegionId_n; }
    public int getFkAddressFormatTypeId_n() { return mnFkAddressFormatTypeId_n; }
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
        mnPkBizPartnerBranchId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerBranchId = 0;
        msBizPartnerBranch = "";
        msCode = "";
        mbIsAddressPrintable = false;
        mbIsDeleted = false;
        mnFkBizPartnerId = 0;
        mnFkBizPartnerBranchTypeId = 0;
        mnFkTaxRegionId_n = 0;
        mnFkAddressFormatTypeId_n = 0;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpb = " + pk[0] + " ";
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
            mnPkBizPartnerBranchId = resultSet.getInt("id_bpb");
            msBizPartnerBranch = resultSet.getString("bpb");
            msCode = resultSet.getString("code");
            mbIsAddressPrintable = resultSet.getBoolean("b_add_prt");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkBizPartnerId = resultSet.getInt("fid_bp");
            mnFkBizPartnerBranchTypeId = resultSet.getInt("fid_tp_bpb");
            mnFkTaxRegionId_n = resultSet.getInt("fid_tax_reg_n");
            mnFkAddressFormatTypeId_n = resultSet.getInt("fid_tp_add_fmt_n");
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
                msSql += "code ";
                break;
            case SDbRegistry.FIELD_NAME:
                msSql += "bpb ";
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
                case SDbRegistry.FIELD_NAME:
                    value = resultSet.getString(1);
                    break;
                default:
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
        return value;
    }
}
