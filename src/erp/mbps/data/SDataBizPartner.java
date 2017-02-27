/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mitm.data.SDataBizPartnerItemDescription;
import erp.mitm.data.SDataItemBizPartnerDescription;
import erp.mmkt.data.SDataConfigurationSalesAgent;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mmkt.data.SDataCustomerConfig;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SDataBizPartner extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerId;
    protected java.lang.String msBizPartner;
    protected java.lang.String msBizPartnerCommercial;
    protected java.lang.String msLastname;
    protected java.lang.String msFirstname;
    protected java.lang.String msFiscalId;
    protected java.lang.String msFiscalFrgId;
    protected java.lang.String msAlternativeId;
    protected java.lang.String msExternalId;
    protected java.lang.String msCodeBankSantander;
    protected java.lang.String msCodeBankBanBajio;
    protected java.lang.String msWeb;
    protected boolean mbIsCompany;
    protected boolean mbIsSupplier;
    protected boolean mbIsCustomer;
    protected boolean mbIsCreditor;
    protected boolean mbIsDebtor;
    protected boolean mbIsAttributeBank;
    protected boolean mbIsAttributeCarrier;
    protected boolean mbIsAttributeEmployee;
    protected boolean mbIsAttributeSalesAgent;
    protected boolean mbIsAttributePartnerShareholder;
    protected boolean mbIsAttributeRelatedParty;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerIdentityTypeId;
    protected int mnFkTaxIdentityId;
    protected int mnFkFiscalBankId;
    protected int mnFkBizAreaId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected erp.mmkt.data.SDataCustomerConfig moDbmsDataCustomerConfig;
    protected erp.mmkt.data.SDataConfigurationSalesAgent moDbmsDataConfigurationSalesAgent;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranch> mvDbmsBizPartnerBranches;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerNote> mvDbmsBizPartnerNotes;
    protected java.util.Vector<erp.mitm.data.SDataItemBizPartnerDescription> mvDbmsItemBizPartnerDescription;
    protected erp.mbps.data.SDataBizPartnerCategory[] maoDbmsCategorySettings;

    protected erp.mbps.data.SDataEmployee moDbmsDataEmployee;

    public SDataBizPartner() {
        super(SDataConstants.BPSU_BP);
        mvDbmsBizPartnerBranches = new Vector<SDataBizPartnerBranch>();
        mvDbmsBizPartnerNotes = new Vector<SDataBizPartnerNote>();
        mvDbmsItemBizPartnerDescription = new Vector<SDataItemBizPartnerDescription>();
        maoDbmsCategorySettings = null;
        reset();
    }
    
    /*
     * Private methods:
     */

    /*
     * Public methods:
     */
    
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setBizPartner(java.lang.String s) { msBizPartner = s; }
    public void setBizPartnerCommercial(java.lang.String s) { msBizPartnerCommercial = s; }
    public void setLastname(java.lang.String s) { msLastname = s; }
    public void setFirstname(java.lang.String s) { msFirstname = s; }
    public void setFiscalId(java.lang.String s) { msFiscalId = s; }
    public void setFiscalFrgId(java.lang.String s) { msFiscalFrgId = s; }
    public void setAlternativeId(java.lang.String s) { msAlternativeId = s; }
    public void setExternalId(java.lang.String s) { msExternalId = s; }
    public void setCodeBankSantander(java.lang.String s) { msCodeBankSantander = s; }
    public void setCodeBankBanBajio(java.lang.String s) { msCodeBankBanBajio = s; }
    public void setWeb(java.lang.String s) { msWeb = s; }
    public void setIsCompany(boolean b) { mbIsCompany = b; }
    public void setIsSupplier(boolean b) { mbIsSupplier = b; }
    public void setIsCustomer(boolean b) { mbIsCustomer = b; }
    public void setIsCreditor(boolean b) { mbIsCreditor = b; }
    public void setIsDebtor(boolean b) { mbIsDebtor = b; }
    public void setIsAttributeBank(boolean b) { mbIsAttributeBank = b; }
    public void setIsAttributeCarrier(boolean b) { mbIsAttributeCarrier = b; }
    public void setIsAttributeEmployee(boolean b) { mbIsAttributeEmployee = b; }
    public void setIsAttributeSalesAgent(boolean b) { mbIsAttributeSalesAgent = b; }
    public void setIsAttributePartnerShareholder(boolean b) { mbIsAttributePartnerShareholder = b; }
    public void setIsAttributeRelatedParty(boolean b) { mbIsAttributeRelatedParty = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerIdentityTypeId(int n) { mnFkBizPartnerIdentityTypeId = n; }
    public void setFkTaxIdentityId(int n) { mnFkTaxIdentityId = n; }
    public void setFkFiscalBankId(int n) { mnFkFiscalBankId = n; }
    public void setFkBizAreaId(int n) { mnFkBizAreaId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public java.lang.String getBizPartner() { return msBizPartner; }
    public java.lang.String getBizPartnerCommercial() { return msBizPartnerCommercial; }
    public java.lang.String getLastname() { return msLastname; }
    public java.lang.String getFirstname() { return msFirstname; }
    public java.lang.String getFiscalId() { return msFiscalId; }
    public java.lang.String getFiscalFrgId() { return msFiscalFrgId; }
    public java.lang.String getAlternativeId() { return msAlternativeId; }
    public java.lang.String getExternalId() { return msExternalId; }
    public java.lang.String getCodeBankSantander() { return msCodeBankSantander; }
    public java.lang.String getCodeBankBanBajio() { return msCodeBankBanBajio; }
    public java.lang.String getWeb() { return msWeb; }
    public boolean getIsCompany() { return mbIsCompany; }
    public boolean getIsSupplier() { return mbIsSupplier; }
    public boolean getIsCustomer() { return mbIsCustomer; }
    public boolean getIsCreditor() { return mbIsCreditor; }
    public boolean getIsDebtor() { return mbIsDebtor; }
    public boolean getIsAttributeBank() { return mbIsAttributeBank; }
    public boolean getIsAttributeCarrier() { return mbIsAttributeCarrier; }
    public boolean getIsAttributeEmployee() { return mbIsAttributeEmployee; }
    public boolean getIsAttributeSalesAgent() { return mbIsAttributeSalesAgent; }
    public boolean getIsAttributePartnerShareholder() { return mbIsAttributePartnerShareholder; }
    public boolean getIsAttributeRelatedParty() { return mbIsAttributeRelatedParty; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerIdentityTypeId() { return mnFkBizPartnerIdentityTypeId; }
    public int getFkTaxIdentityId() { return mnFkTaxIdentityId; }
    public int getFkFiscalBankId() { return mnFkFiscalBankId; }
    public int getFkBizAreaId() { return mnFkBizAreaId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsCategorySettingsCo(erp.mbps.data.SDataBizPartnerCategory o) { maoDbmsCategorySettings[0] = o; }
    public void setDbmsCategorySettingsSup(erp.mbps.data.SDataBizPartnerCategory o) { maoDbmsCategorySettings[1] = o; }
    public void setDbmsCategorySettingsCus(erp.mbps.data.SDataBizPartnerCategory o) { maoDbmsCategorySettings[2] = o; }
    public void setDbmsCategorySettingsCdr(erp.mbps.data.SDataBizPartnerCategory o) { maoDbmsCategorySettings[3] = o; }
    public void setDbmsCategorySettingsDbr(erp.mbps.data.SDataBizPartnerCategory o) { maoDbmsCategorySettings[4] = o; }

    public erp.mbps.data.SDataBizPartnerCategory getDbmsCategorySettingsCo() { return maoDbmsCategorySettings[0]; }
    public erp.mbps.data.SDataBizPartnerCategory getDbmsCategorySettingsSup() { return maoDbmsCategorySettings[1]; }
    public erp.mbps.data.SDataBizPartnerCategory getDbmsCategorySettingsCus() { return maoDbmsCategorySettings[2]; }
    public erp.mbps.data.SDataBizPartnerCategory getDbmsCategorySettingsCdr() { return maoDbmsCategorySettings[3]; }
    public erp.mbps.data.SDataBizPartnerCategory getDbmsCategorySettingsDbr() { return maoDbmsCategorySettings[4]; }

    public void setDbmsDataCustomerConfig(erp.mmkt.data.SDataCustomerConfig o) { moDbmsDataCustomerConfig = o; }
    public void setDbmsDataConfigurationSalesAgent(erp.mmkt.data.SDataConfigurationSalesAgent o) { moDbmsDataConfigurationSalesAgent = o; }

    public void setDbmsDataEmployee(erp.mbps.data.SDataEmployee o) { moDbmsDataEmployee = o; }

    public erp.mmkt.data.SDataCustomerConfig getDbmsDataCustomerConfig() { return moDbmsDataCustomerConfig; }
    public erp.mmkt.data.SDataConfigurationSalesAgent getDbmsDataConfigurationSalesAgent() { return moDbmsDataConfigurationSalesAgent; }
    public java.util.Vector<SDataBizPartnerBranch> getDbmsBizPartnerBranches() { return mvDbmsBizPartnerBranches; }
    public java.util.Vector<SDataBizPartnerNote> getDbmsBizPartnerNotes() { return mvDbmsBizPartnerNotes; }
    public java.util.Vector<SDataItemBizPartnerDescription> getDbmsItemBizPartnerDescription() { return mvDbmsItemBizPartnerDescription; }

    public erp.mbps.data.SDataEmployee getDbmsDataEmployee() { return moDbmsDataEmployee; }

    public java.lang.String getProperName() {
        return SLibUtilities.textTrim(mnFkBizPartnerIdentityTypeId == SDataConstantsSys.BPSS_TP_BP_IDY_ORG ? msBizPartner : msFirstname + " " + msLastname);
    }

    public erp.mbps.data.SDataBizPartnerBranch getDbmsHqBranch() {
        SDataBizPartnerBranch hq = null;

        for (int i = 0; i < mvDbmsBizPartnerBranches.size(); i++) {
            if (mvDbmsBizPartnerBranches.get(i).getFkBizPartnerBranchTypeId() == SDataConstantsSys.BPSS_TP_BPB_HQ) {
                hq = mvDbmsBizPartnerBranches.get(i);
                break;
            }
        }

        return hq;
    }
    
    public boolean isDomestic(SClientInterface client) {
        int country = getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n();
        
        return country == SLibConstants.UNDEFINED || client.getSession().getSessionCustom().isLocalCountry(new int[] { country });
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerId = 0;
        msBizPartner = "";
        msBizPartnerCommercial = "";
        msLastname = "";
        msFirstname = "";
        msFiscalId = "";
        msFiscalFrgId = "";
        msAlternativeId = "";
        msExternalId = "";
        msCodeBankSantander = "";
        msCodeBankBanBajio = "";
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
        mbIsAttributePartnerShareholder = false;
        mbIsAttributeRelatedParty = false;
        mbIsDeleted = false;
        mnFkBizPartnerIdentityTypeId = 0;
        mnFkTaxIdentityId = 0;
        mnFkFiscalBankId = 0;
        mnFkBizAreaId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        moDbmsDataCustomerConfig = null;
        moDbmsDataConfigurationSalesAgent = null;
        mvDbmsBizPartnerBranches.clear();
        mvDbmsBizPartnerNotes.clear();
        mvDbmsItemBizPartnerDescription.clear();
        maoDbmsCategorySettings = new SDataBizPartnerCategory[SDataConstantsSys.BPSX_CT_BP_QTY];

        moDbmsDataEmployee = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int i = 0;
        int category = 0;
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.bpsu_bp WHERE id_bp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerId = resultSet.getInt("id_bp");
                msBizPartner = resultSet.getString("bp");
                msBizPartnerCommercial = resultSet.getString("bp_comm");
                msLastname = resultSet.getString("lastname");
                msFirstname = resultSet.getString("firstname");
                msFiscalId = resultSet.getString("fiscal_id");
                msFiscalFrgId = resultSet.getString("fiscal_frg_id");
                msAlternativeId = resultSet.getString("alt_id");
                msExternalId = resultSet.getString("ext_id");
                msCodeBankSantander = resultSet.getString("code_bank_san");
                msCodeBankBanBajio = resultSet.getString("code_bank_baj");
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
                mbIsAttributePartnerShareholder = resultSet.getBoolean("b_att_par_shh");
                mbIsAttributeRelatedParty = resultSet.getBoolean("b_att_rel_pty");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkBizPartnerIdentityTypeId = resultSet.getInt("fid_tp_bp_idy");
                mnFkTaxIdentityId = resultSet.getInt("fid_tax_idy");
                mnFkFiscalBankId = resultSet.getInt("fid_fiscal_bank");
                mnFkBizAreaId = resultSet.getInt("fid_ba");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                statementAux = statement.getConnection().createStatement();

                // Read aswell business partner category settings:

                for (i = 0; i < SDataConstantsSys.BPSX_CT_BP_QTY; i++) {
                    category = 0;
                    switch (i) {
                        case 0:
                            category = SDataConstantsSys.BPSS_CT_BP_CO;
                            break;
                        case 1:
                            category = SDataConstantsSys.BPSS_CT_BP_SUP;
                            break;
                        case 2:
                            category = SDataConstantsSys.BPSS_CT_BP_CUS;
                            break;
                        case 3:
                            category = SDataConstantsSys.BPSS_CT_BP_CDR;
                            break;
                        case 4:
                            category = SDataConstantsSys.BPSS_CT_BP_DBR;
                            break;
                        default:
                            break;
                    }

                    if (category != 0) {
                        sql = "SELECT count(*) f_count FROM erp.bpsu_bp_ct WHERE id_bp = " + key[0] + " AND id_ct_bp = " + category + " ";
                        resultSet = statement.executeQuery(sql);
                        if (!resultSet.next()) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            if (resultSet.getInt("f_count") > 0) {
                                maoDbmsCategorySettings[i] = new SDataBizPartnerCategory();
                                if (maoDbmsCategorySettings[i].read(new int[] { mnPkBizPartnerId, category }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                                }
                            }
                        }
                    }
                }

                // Read aswell business partner branches:

                sql = "SELECT id_bpb, bpb, fid_tp_bpb FROM erp.bpsu_bpb WHERE fid_bp = " + key[0] + " ORDER BY fid_tp_bpb, bpb, id_bpb ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    erp.mbps.data.SDataBizPartnerBranch branch = new SDataBizPartnerBranch();
                    if (branch.read(new int[] { resultSet.getInt("id_bpb") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerBranches.add(branch);
                    }
                }

                // Read aswell the notes:

                sql = "SELECT id_bp, id_nts FROM erp.bpsu_bp_nts WHERE id_bp = " + key[0] + " ORDER BY id_nts ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerNote note = new SDataBizPartnerNote();
                    if (note.read(new int[] { resultSet.getInt("id_bp"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerNotes.add(note);
                    }
                }

                // Read aswell business partner ítems descriptions:

                SDataBizPartnerItemDescription description = new SDataBizPartnerItemDescription();

                if (description.read(new int[] { mnPkBizPartnerId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mvDbmsItemBizPartnerDescription = description.getDbmsItemBizPartnerDescriptions();
                }


                // Read aswell customer configuration, if exists:

                if (mbIsCustomer) {
                    sql = "SELECT count(*) AS f_count FROM mkt_cfg_cus WHERE id_cus = " + mnPkBizPartnerId + " AND b_del = 0 ";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        if (resultSet.getInt("f_count") == 1) {
                            moDbmsDataCustomerConfig = new SDataCustomerConfig();
                            if (moDbmsDataCustomerConfig.read(new int[] { mnPkBizPartnerId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                        }
                    }
                }

                // Read aswell sales agent configuration, if exists:

                if (mbIsAttributeSalesAgent) {
                    sql = "SELECT count(*) AS f_count FROM mkt_cfg_sal_agt WHERE id_sal_agt = " + mnPkBizPartnerId + " AND b_del = 0 ";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        if (resultSet.getInt("f_count") == 1) {
                            moDbmsDataConfigurationSalesAgent = new SDataConfigurationSalesAgent();
                            if (moDbmsDataConfigurationSalesAgent.read(new int[] { mnPkBizPartnerId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                        }
                    }
                }

                // Read information of the employee, of the human resources module, if exists:

                if (mbIsAttributeEmployee) {
                    sql = "SELECT count(*) AS f_count FROM erp.hrsu_emp WHERE id_emp = " + mnPkBizPartnerId + " ";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        if (resultSet.getInt("f_count") == 1) {
                            moDbmsDataEmployee = new SDataEmployee();
                            if (moDbmsDataEmployee.read(new int[] { mnPkBizPartnerId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                        }
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i = 0;
        int nParam = 1;
        String sql = "";
        Statement statement = null;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerId);
            callableStatement.setString(nParam++, msBizPartner);
            callableStatement.setString(nParam++, msBizPartnerCommercial);
            callableStatement.setString(nParam++, msLastname);
            callableStatement.setString(nParam++, msFirstname);
            callableStatement.setString(nParam++, msFiscalId);
            callableStatement.setString(nParam++, msFiscalFrgId);
            callableStatement.setString(nParam++, msAlternativeId);
            callableStatement.setString(nParam++, msExternalId);
            callableStatement.setString(nParam++, msCodeBankSantander);
            callableStatement.setString(nParam++, msCodeBankBanBajio);
            callableStatement.setString(nParam++, msWeb);
            callableStatement.setBoolean(nParam++, mbIsCompany);
            callableStatement.setBoolean(nParam++, mbIsSupplier);
            callableStatement.setBoolean(nParam++, mbIsCustomer);
            callableStatement.setBoolean(nParam++, mbIsCreditor);
            callableStatement.setBoolean(nParam++, mbIsDebtor);
            callableStatement.setBoolean(nParam++, mbIsAttributeBank);
            callableStatement.setBoolean(nParam++, mbIsAttributeCarrier);
            callableStatement.setBoolean(nParam++, mbIsAttributeEmployee);
            callableStatement.setBoolean(nParam++, mbIsAttributeSalesAgent);
            callableStatement.setBoolean(nParam++, mbIsAttributePartnerShareholder);
            callableStatement.setBoolean(nParam++, mbIsAttributeRelatedParty);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerIdentityTypeId);
            callableStatement.setInt(nParam++, mnFkTaxIdentityId);
            callableStatement.setInt(nParam++, mnFkFiscalBankId);
            callableStatement.setInt(nParam++, mnFkBizAreaId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            if (mnPkBizPartnerId == SDataConstants.UNDEFINED) {
                mnPkBizPartnerId = callableStatement.getInt(nParam - 3);
            }
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                statement = connection.createStatement();

                // Save aswell business partner category settings:

                for (i = 0; i < maoDbmsCategorySettings.length; i++) {
                    if (maoDbmsCategorySettings[i] != null) {
                        maoDbmsCategorySettings[i].setPkBizPartnerId(mnPkBizPartnerId);
                        if (maoDbmsCategorySettings[i].save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save aswell business partner branches:

                for (i = 0; i < mvDbmsBizPartnerBranches.size(); i++) {
                    mvDbmsBizPartnerBranches.get(i).setFkBizPartnerId(mnPkBizPartnerId);
                    if (mvDbmsBizPartnerBranches.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell customer configuration, if applies:

                if (moDbmsDataCustomerConfig != null) {
                    moDbmsDataCustomerConfig.setPkCustomerId(mnPkBizPartnerId);
                    if (moDbmsDataCustomerConfig.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell sales agent configuration, if applies:

                if (moDbmsDataConfigurationSalesAgent != null) {
                    moDbmsDataConfigurationSalesAgent.setPkSalesAgentId(mnPkBizPartnerId);
                    if (moDbmsDataConfigurationSalesAgent.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save information of the employee, of the human resources module, if applies:

                if (moDbmsDataEmployee != null) {
                    moDbmsDataEmployee.setPkEmployeeId(mnPkBizPartnerId);
                    if (moDbmsDataEmployee.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell the notes:

                sql = "DELETE FROM erp.bpsu_bp_nts WHERE id_bp = " + mnPkBizPartnerId + " ";
                statement.execute(sql);

                for (i = 0; i < mvDbmsBizPartnerNotes.size(); i++) {
                    mvDbmsBizPartnerNotes.get(i).setPkBizPartnerId(mnPkBizPartnerId);
                    if (mvDbmsBizPartnerNotes.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    public erp.mbps.data.SDataBizPartnerBranch getDbmsBizPartnerBranch(int[] pk) {
        SDataBizPartnerBranch branch = null;

        for (int i = 0; i < mvDbmsBizPartnerBranches.size(); i++) {
            if (SLibUtilities.compareKeys(pk, mvDbmsBizPartnerBranches.get(i).getPrimaryKey())) {
                branch = mvDbmsBizPartnerBranches.get(i);
                break;
            }
        }

        return branch;
    }

    /**
     * Obtain mail for requested business partner's branch, if any, and requested contact type.
     * @param keyBranch_n Primary key of the business partner branch
     * @param contactType Contact of type (SDataConstantsSys.BPSS_TP_CON_)
     * @return Mail of contact
     */
    public String getBizPartnerBranchContactMail(int[] keyBranch_n, int contactType) {
        String mails = "";
        SDataBizPartnerBranch branch = null;
        SDataBizPartnerBranchContact contact = null;
        int[] anTypesContact = { SDataConstantsSys.BPSS_TP_CON_PAY, SDataConstantsSys.BPSS_TP_CON_PUR, SDataConstantsSys.BPSS_TP_CON_ADM };

        if (keyBranch_n == null) {
            branch = getDbmsHqBranch();
        }
        else {
            branch = getDbmsBizPartnerBranch(keyBranch_n);
        }

        if (branch != null) {
            contact = getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0);

            mails = contact.getEmail01();

            if (mails.isEmpty()) {
                for (int i = 0; i < anTypesContact.length; i++) {
                    for (int j = 0; j < branch.getDbmsBizPartnerBranchContacts().size(); j++) {
                        contact = branch.getDbmsBizPartnerBranchContacts().get(j);

                        if (contactType != SLibConstants.UNDEFINED && contact.getPkContactTypeId() == contactType) {
                            mails = contact.getEmail01();

                            if (mails.isEmpty()) {
                                mails = contact.getEmail02();
                            }
                            break;
                        }
                        else if (contact.getPkContactTypeId() == anTypesContact[i] && !contact.getIsDeleted()) {
                            mails = contact.getEmail01();

                            if (mails.isEmpty()) {
                                mails = contact.getEmail02();
                            }
                        }
                        if (!mails.isEmpty()) {
                            break;
                        }
                    }
                    if (!mails.isEmpty()) {
                        break;
                    }
                }
            }
        }

        if (mails.isEmpty() && keyBranch_n != null) {
            mails = getBizPartnerBranchContactMail(null, contactType);
        }

        return mails;
    }

    /**
     * Obtain mail for default contact of requested business partner's branch.
     * @param keyBranch Primary key of the business partner's branch.
     * @return Mail of contact.
     */
    public String getBizPartnerBranchContactMail(int[] keyBranch) {
        return getBizPartnerBranchContactMail(keyBranch, SLibConstants.UNDEFINED);
    }

    /**
     * Obtain mail for requested contact type of business partner's headquarters.
     * @param contactType Contact type (SDataConstantsSys.BPSS_TP_CON_) or SLibConstants.UNDEFINED for default contact.
     * @return Mail of contact.
     */
    public String getBizPartnerContactMail(int contactType) {
        return getBizPartnerBranchContactMail(null, contactType);
    }
    
    public int[] getSalesAgentKey(int[] branchKey) {
        int[] pk = null;
        SDataCustomerBranchConfig oConfig = null;
        SDataBizPartnerBranch oBizPartnerBranch = null;

        if (moDbmsDataCustomerConfig != null) {
            for (SDataBizPartnerBranch bizPartnerBranch : mvDbmsBizPartnerBranches) {
                if (SLibUtilities.compareKeys(bizPartnerBranch.getPrimaryKey(), branchKey)) {
                    oBizPartnerBranch = bizPartnerBranch;
                    break;
                }
            }

            if (oBizPartnerBranch != null) {
                oConfig =  oBizPartnerBranch.getDbmsCustomerBranchConfig(branchKey);
            }
            else {
                oConfig =  mvDbmsBizPartnerBranches.get(0).getDbmsCustomerBranchConfig(branchKey);
            }

            if (oConfig != null && oConfig.getFkSalesAgentId_n() > 0 && !oConfig.getIsDeleted()) {
                pk = new int[] { oConfig.getFkSalesAgentId_n() };
            }
            else if (moDbmsDataCustomerConfig.getFkSalesAgentId_n() > 0) {
                pk = new int[] { moDbmsDataCustomerConfig.getFkSalesAgentId_n() };
            }
        }

        return pk;
    }

    public int[] getSalesSupervisorKey(int[] branchKey) {
        int[] pk = null;
        SDataCustomerBranchConfig oConfig = null;
        SDataBizPartnerBranch oBizPartnerBranch = null;

        if (moDbmsDataCustomerConfig != null) {
            for (SDataBizPartnerBranch bizPartnerBranch : mvDbmsBizPartnerBranches) {
                if (SLibUtilities.compareKeys(bizPartnerBranch.getPrimaryKey(), branchKey)) {
                    oBizPartnerBranch = bizPartnerBranch;
                    break;
                }
            }

            if (oBizPartnerBranch != null) {
                oConfig =  oBizPartnerBranch.getDbmsCustomerBranchConfig(branchKey);
            }
            else {
                oConfig =  mvDbmsBizPartnerBranches.get(0).getDbmsCustomerBranchConfig(branchKey);
            }

            if (oConfig != null && oConfig.getFkSalesSupervisorId_n() > 0 && !oConfig.getIsDeleted()) {
                pk = new int[] { oConfig.getFkSalesSupervisorId_n() };
            }
            else if (moDbmsDataCustomerConfig.getFkSalesSupervisorId_n() > 0) {
                pk = new int[] { moDbmsDataCustomerConfig.getFkSalesSupervisorId_n() };
            }
        }

        return pk;
    }

    public void openCategory(SClientInterface client, int formType, Object pk) {
        boolean copy = false;

        switch (formType) {
            case SDataConstants.BPSX_BP_SUP:
                copy = !mbIsSupplier;
                break;
            case SDataConstants.BPSX_BP_CUS:
                copy = !mbIsCustomer;
                break;
            case SDataConstants.BPSX_BP_CDR:
                copy = !mbIsCreditor;
                break;
            case SDataConstants.BPSX_BP_DBR:
                copy = !mbIsDebtor;
                break;
            case SDataConstants.BPSX_BP_EMP:
                copy = !mbIsAttributeEmployee;
                break;
            default:
        }

        if (copy) {
            client.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showFormForCopy(formType, pk);
        }
        else {
            client.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(formType, pk);
        }
    }
}
