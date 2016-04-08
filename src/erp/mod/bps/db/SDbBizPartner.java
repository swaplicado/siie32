/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.bps.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
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
public class SDbBizPartner extends SDbRegistryUser {

    public static final int FIELD_FISCAL_ID = SDbRegistry.FIELD_BASE + 1;
    public static final int FIELD_NAME_COMM = SDbRegistry.FIELD_BASE + 2;

    protected int mnPkBizPartnerId;
    protected String msBizPartner;
    protected String msBizPartnerCommercial;
    protected String msLastname;
    protected String msFirstname;
    protected String msFiscalId;
    protected String msFiscalForeignId;
    protected String msAlternativeId;
    protected String msExternalId;
    protected String msWeb;
    protected boolean mbIsCompany;
    protected boolean mbIsSupplier;
    protected boolean mbIsCustomer;
    protected boolean mbIsCreditor;
    protected boolean mbIsDebtor;
    protected boolean mbIsAttributeBank;
    protected boolean mbIsAttributeCarrier;
    protected boolean mbIsAttributeEmployee;
    protected boolean mbIsAttributeSalesAgent;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerIdentityTypeId;
    protected int mnFkTaxIdentityTypeId;
    protected int mnFkBizAreaId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;
    
    protected SDbBizPartnerBranch moRegHeadquarters;

    public SDbBizPartner() {
        super(SModConsts.BPSU_BP);
    }

    /*
     * Public methods
     */

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setBizPartnerCommercial(String s) { msBizPartnerCommercial = s; }
    public void setLastname(String s) { msLastname = s; }
    public void setFirstname(String s) { msFirstname = s; }
    public void setFiscalId(String s) { msFiscalId = s; }
    public void setFiscalForeignId(String s) { msFiscalForeignId = s; }
    public void setAlternativeId(String s) { msAlternativeId = s; }
    public void setExternalId(String s) { msExternalId = s; }
    public void setWeb(String s) { msWeb = s; }
    public void setIsCompany(boolean b) { mbIsCompany = b; }
    public void setIsSupplier(boolean b) { mbIsSupplier = b; }
    public void setIsCustomer(boolean b) { mbIsCustomer = b; }
    public void setIsCreditor(boolean b) { mbIsCreditor = b; }
    public void setIsDebtor(boolean b) { mbIsDebtor = b; }
    public void setIsAttributeBank(boolean b) { mbIsAttributeBank = b; }
    public void setIsAttributeCarrier(boolean b) { mbIsAttributeCarrier = b; }
    public void setIsAttributeEmployee(boolean b) { mbIsAttributeEmployee = b; }
    public void setIsAttributeSalesAgent(boolean b) { mbIsAttributeSalesAgent = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerIdentityTypeId(int n) { mnFkBizPartnerIdentityTypeId = n; }
    public void setFkTaxIdentityTypeId(int n) { mnFkTaxIdentityTypeId = n; }
    public void setFkBizAreaId(int n) { mnFkBizAreaId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public String getBizPartner() { return msBizPartner; }
    public String getBizPartnerCommercial() { return msBizPartnerCommercial; }
    public String getLastname() { return msLastname; }
    public String getFirstname() { return msFirstname; }
    public String getFiscalId() { return msFiscalId; }
    public String getFiscalForeignId() { return msFiscalForeignId; }
    public String getAlternativeId() { return msAlternativeId; }
    public String getExternalId() { return msExternalId; }
    public String getWeb() { return msWeb; }
    public boolean getIsCompany() { return mbIsCompany; }
    public boolean getIsSupplier() { return mbIsSupplier; }
    public boolean getIsCustomer() { return mbIsCustomer; }
    public boolean getIsCreditor() { return mbIsCreditor; }
    public boolean getIsDebtor() { return mbIsDebtor; }
    public boolean getIsAttributeBank() { return mbIsAttributeBank; }
    public boolean getIsAttributeCarrier() { return mbIsAttributeCarrier; }
    public boolean getIsAttributeEmployee() { return mbIsAttributeEmployee; }
    public boolean getIsAttributeSalesAgent() { return mbIsAttributeSalesAgent; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerIdentityTypeId() { return mnFkBizPartnerIdentityTypeId; }
    public int getFkTaxIdentityTypeId() { return mnFkTaxIdentityTypeId; }
    public int getFkBizAreaId() { return mnFkBizAreaId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserNewTs() { return mtUserNewTs; }
    public Date getUserEditTs() { return mtUserEditTs; }
    public Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setRegHeadquarters(SDbBizPartnerBranch o) { moRegHeadquarters = o; }
    
    public SDbBizPartnerBranch getRegHeadquarters() { return moRegHeadquarters; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        msBizPartner = "";
        msBizPartnerCommercial = "";
        msLastname = "";
        msFirstname = "";
        msFiscalId = "";
        msFiscalForeignId = "";
        msAlternativeId = "";
        msExternalId = "";
        msWeb = "";
        mbIsCompany = false;
        mbIsSupplier = false;
        mbIsCustomer = false;
        mbIsCreditor = false;
        mbIsDebtor = false;
        mbIsAttributeBank = false;
        mbIsAttributeCarrier = false;
        mbIsAttributeEmployee = false;
        mbIsAttributeSalesAgent = false;
        mbIsDeleted = false;
        mnFkBizPartnerIdentityTypeId = 0;
        mnFkTaxIdentityTypeId = 0;
        mnFkBizAreaId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        moRegHeadquarters = null;
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
            msBizPartner = resultSet.getString("bp");
            msBizPartnerCommercial = resultSet.getString("bp_comm");
            msLastname = resultSet.getString("lastname");
            msFirstname = resultSet.getString("firstname");
            msFiscalId = resultSet.getString("fiscal_id");
            msFiscalForeignId = resultSet.getString("fiscal_frg_id");
            msAlternativeId = resultSet.getString("alt_id");
            msExternalId = resultSet.getString("ext_id");
            msWeb = resultSet.getString("web");
            mbIsCompany = resultSet.getBoolean("b_co");
            mbIsSupplier = resultSet.getBoolean("b_sup");
            mbIsCustomer = resultSet.getBoolean("b_cus");
            mbIsCreditor = resultSet.getBoolean("b_cdr");
            mbIsDebtor = resultSet.getBoolean("b_dbr");
            mbIsAttributeBank = resultSet.getBoolean("b_att_bank");
            mbIsAttributeCarrier = resultSet.getBoolean("b_att_car");
            mbIsAttributeEmployee = resultSet.getBoolean("b_att_emp");
            mbIsAttributeSalesAgent = resultSet.getBoolean("b_att_sal_agt");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkBizPartnerIdentityTypeId = resultSet.getInt("fid_tp_bp_idy");
            mnFkTaxIdentityTypeId = resultSet.getInt("fid_tax_idy");
            mnFkBizAreaId = resultSet.getInt("fid_ba");
            mnFkUserNewId = resultSet.getInt("fid_usr_new");
            mnFkUserEditId = resultSet.getInt("fid_usr_edit");
            mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
            mtUserNewTs = resultSet.getTimestamp("ts_new");
            mtUserEditTs = resultSet.getTimestamp("ts_edit");
            mtUserDeleteTs = resultSet.getTimestamp("ts_del");
            
            // Read aswell registry members:
            
            msSql = "SELECT id_bpb "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " "
                    + "WHERE fid_bp = " + mnPkBizPartnerId + " AND fid_tp_bpb = " + SModSysConsts.BPSS_TP_BPB_HQ + " AND b_del = 0; ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                moRegHeadquarters = new SDbBizPartnerBranch();
                moRegHeadquarters.read(session, new int[] { resultSet.getInt(1) });
            }
            
            // Finish registry reading:

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
            case SDbRegistry.FIELD_NAME:
                msSql += "bp ";
                break;
            case SDbBizPartner.FIELD_FISCAL_ID:
                msSql += "fiscal_id ";
                break;
            case FIELD_NAME_COMM:
                msSql += "bp_comm ";
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
                case SDbRegistry.FIELD_NAME:
                case SDbBizPartner.FIELD_FISCAL_ID:
                case FIELD_NAME_COMM:
                    value = resultSet.getString(1);
                    break;
                default:
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
        return value;
    }
}
