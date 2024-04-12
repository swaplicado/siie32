/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.mitm.data.SDataItemConfigBizPartnerItems;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.data.diot.SDiotConsts;
import erp.mitm.data.SDataItemConfigBizPartner;
import erp.mmkt.data.SDataConfigurationSalesAgent;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mmkt.data.SDataCustomerConfig;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import sa.lib.gui.SGuiSession;

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Sergio Flores
 */
public class SDataBizPartner extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    public static final int CFD_ORG_NAMES_FULL_NAME = 1;
    public static final int CFD_ORG_NAMES_FISCAL_NAME = 2;
    
    protected int mnPkBizPartnerId;
    protected java.lang.String msBizPartner;
    protected java.lang.String msBizPartnerCommercial;
    protected java.lang.String msBizPartnerFiscalPolicy;
    protected java.lang.String msBizPartnerFiscal;
    protected java.lang.String msBizPartnerCapitalRegime;
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

    protected erp.mbps.data.SDataBizPartnerCategory[] maoDbmsCategorySettings;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranch> mvDbmsBizPartnerBranches;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerNote> mvDbmsBizPartnerNotes;
    protected java.util.Vector<erp.mitm.data.SDataItemConfigBizPartner> mvDbmsItemConfigs;
    protected erp.mmkt.data.SDataCustomerConfig moDbmsDataCustomerConfig;
    protected erp.mmkt.data.SDataConfigurationSalesAgent moDbmsDataConfigurationSalesAgent;
    protected erp.mbps.data.SDataEmployee moDbmsDataEmployee;

    /** Save benefit tables of employee in SBA-Lib style, in a post-save request. */
    protected erp.mod.hrs.db.SDbEmployeeBenefitTables moAuxDbEmployeeBenefitTables;

    public SDataBizPartner() {
        super(SDataConstants.BPSU_BP);
        maoDbmsCategorySettings = null;
        mvDbmsBizPartnerBranches = new Vector<>();
        mvDbmsBizPartnerNotes = new Vector<>();
        mvDbmsItemConfigs = new Vector<>();
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
    public void setBizPartnerFiscalPolicy(java.lang.String s) { msBizPartnerFiscalPolicy = s; }
    public void setBizPartnerFiscal(java.lang.String s) { msBizPartnerFiscal = s; }
    public void setBizPartnerCapitalRegime(java.lang.String s) { msBizPartnerCapitalRegime = s; }
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
    public java.lang.String getBizPartnerFiscalPolicy() { return msBizPartnerFiscalPolicy; }
    public java.lang.String getBizPartnerFiscal() { return msBizPartnerFiscal; }
    public java.lang.String getBizPartnerCapitalRegime() { return msBizPartnerCapitalRegime; }
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

    public java.util.Vector<SDataBizPartnerBranch> getDbmsBizPartnerBranches() { return mvDbmsBizPartnerBranches; }
    public java.util.Vector<SDataBizPartnerNote> getDbmsBizPartnerNotes() { return mvDbmsBizPartnerNotes; }
    public java.util.Vector<SDataItemConfigBizPartner> getDbmsItemConfigs() { return mvDbmsItemConfigs; }
    public erp.mmkt.data.SDataCustomerConfig getDbmsDataCustomerConfig() { return moDbmsDataCustomerConfig; }
    public erp.mmkt.data.SDataConfigurationSalesAgent getDbmsDataConfigurationSalesAgent() { return moDbmsDataConfigurationSalesAgent; }
    public erp.mbps.data.SDataEmployee getDbmsDataEmployee() { return moDbmsDataEmployee; }
    
    /** Set benefit tables of employee to be saved in SBA-Lib style, in a post-save request. */
    public void setAuxDbEmployeeBenefitTables(erp.mod.hrs.db.SDbEmployeeBenefitTables o) { moAuxDbEmployeeBenefitTables = o; }
    
    /** Get benefit tables of employee to be saved in SBA-Lib style, in a post-save request. */
    public erp.mod.hrs.db.SDbEmployeeBenefitTables getAuxDbEmployeeBenefitTables() { return moAuxDbEmployeeBenefitTables; }

    public boolean isPerson() {
        return mnFkBizPartnerIdentityTypeId == SDataConstantsSys.BPSS_TP_BP_IDY_PER;
    }

    public java.lang.String getProperName() {
        return SLibUtilities.textTrim(isPerson() ? msFirstname + " " + msLastname : msBizPartner);
    }
    
    public boolean isDomestic(SClientInterface client) {
        int country = getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n();
        
        return country == SLibConstants.UNDEFINED || client.getSession().getSessionCustom().isLocalCountry(new int[] { country });
    }
    
    public String getDiotTerceroClave() {
        return mnPkBizPartnerId + "-" + getDbmsCategorySettingsSup().msDiotOperation;
    }

    public String getDiotTipoTercero(SClientInterface client) {
        return isDomestic(client) ? SDiotConsts.THIRD_DOMESTIC : SDiotConsts.THIRD_INTERNATIONAL;
    }
    
    public String getDiotTipoOperación() {
        return getDbmsCategorySettingsSup().msDiotOperation;
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
        msBizPartnerFiscalPolicy = "";
        msBizPartnerFiscal = "";
        msBizPartnerCapitalRegime = "";
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

        maoDbmsCategorySettings = new SDataBizPartnerCategory[SDataConstantsSys.BPSX_CT_BP_QTY];
        mvDbmsBizPartnerBranches.clear();
        mvDbmsBizPartnerNotes.clear();
        mvDbmsItemConfigs.clear();
        moDbmsDataCustomerConfig = null;
        moDbmsDataConfigurationSalesAgent = null;
        moDbmsDataEmployee = null;
        
        moAuxDbEmployeeBenefitTables = null;
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
            sql = "SELECT * " +
                    "FROM erp.bpsu_bp " +
                    "WHERE id_bp = " + key[0] + ";";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerId = resultSet.getInt("id_bp");
                msBizPartner = resultSet.getString("bp");
                msBizPartnerCommercial = resultSet.getString("bp_comm");
                msBizPartnerFiscalPolicy = resultSet.getString("bp_fiscal_pol");
                msBizPartnerFiscal = resultSet.getString("bp_fiscal");
                msBizPartnerCapitalRegime = resultSet.getString("bp_cap_reg");
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

                // Read as well business partner category settings:

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
                        sql = "SELECT COUNT(*) FROM erp.bpsu_bp_ct WHERE id_bp = " + mnPkBizPartnerId + " AND id_ct_bp = " + category + ";";
                        resultSet = statement.executeQuery(sql);
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            maoDbmsCategorySettings[i] = new SDataBizPartnerCategory();
                            if (maoDbmsCategorySettings[i].read(new int[] { mnPkBizPartnerId, category }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                        }
                    }
                }

                // Read as well business partner branches:

                sql = "SELECT id_bpb, bpb, fid_tp_bpb FROM erp.bpsu_bpb WHERE fid_bp = " + mnPkBizPartnerId + " ORDER BY fid_tp_bpb, bpb, id_bpb;";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerBranch branch = new SDataBizPartnerBranch();
                    if (branch.read(new int[] { resultSet.getInt("id_bpb") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerBranches.add(branch);
                    }
                }

                // Read as well business partner notes:

                sql = "SELECT id_nts FROM erp.bpsu_bp_nts WHERE id_bp = " + mnPkBizPartnerId + " ORDER BY id_nts;";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerNote note = new SDataBizPartnerNote();
                    if (note.read(new int[] { mnPkBizPartnerId, resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerNotes.add(note);
                    }
                }

                // Read as well business partner ítems descriptions:

                SDataItemConfigBizPartnerItems itemConfigs = new SDataItemConfigBizPartnerItems();

                if (itemConfigs.read(new int[] { mnPkBizPartnerId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mvDbmsItemConfigs = itemConfigs.getDbmsItemConfigs();
                }

                // Read as well customer configuration, if exists:

                if (mbIsCustomer) {
                    sql = "SELECT COUNT(*) FROM mkt_cfg_cus WHERE id_cus = " + mnPkBizPartnerId + ";";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        moDbmsDataCustomerConfig = new SDataCustomerConfig();
                        if (moDbmsDataCustomerConfig.read(new int[] { mnPkBizPartnerId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                    }
                }

                // Read as well sales agent configuration, if exists:

                if (mbIsAttributeSalesAgent) {
                    sql = "SELECT COUNT(*) FROM mkt_cfg_sal_agt WHERE id_sal_agt = " + mnPkBizPartnerId + ";";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        moDbmsDataConfigurationSalesAgent = new SDataConfigurationSalesAgent();
                        if (moDbmsDataConfigurationSalesAgent.read(new int[] { mnPkBizPartnerId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                    }
                }

                // Read information of the employee, of the human resources module, if exists:

                if (mbIsAttributeEmployee) {
                    sql = "SELECT COUNT(*) FROM erp.hrsu_emp WHERE id_emp = " + mnPkBizPartnerId + ";";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        moDbmsDataEmployee = new SDataEmployee();
                        if (moDbmsDataEmployee.read(new int[] { mnPkBizPartnerId }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
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
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        if (moDbmsDataEmployee != null) {
            // for employees, last name is splitted into father and mother surenames:
            msLastname = moDbmsDataEmployee.composeLastname();
        }
        
        try {
            int userId = mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId;
            
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerId);
            callableStatement.setString(nParam++, msBizPartner);
            callableStatement.setString(nParam++, msBizPartnerCommercial);
            callableStatement.setString(nParam++, msBizPartnerFiscalPolicy);
            callableStatement.setString(nParam++, msBizPartnerFiscal);
            callableStatement.setString(nParam++, msBizPartnerCapitalRegime);
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
            callableStatement.setInt(nParam++, userId);
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
                // Save as well business partner category settings:

                for (int i = 0; i < maoDbmsCategorySettings.length; i++) { // note that category settings can have null elements!
                    // save only existing and new or edited branches:
                    
                    SDataBizPartnerCategory category = maoDbmsCategorySettings[i]; // convenience variable
                    
                    if (category != null) {
                        if (category.getIsRegistryNew() || category.getIsRegistryEdited()) {
                            category.setPkBizPartnerId(mnPkBizPartnerId);

                            if (category.getIsRegistryNew()) {
                                category.setFkUserNewId(userId);
                            }
                            else {
                                category.setFkUserEditId(userId);
                            }

                            if (category.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                            }
                        }
                    }
                }

                // Save as well branches:
                
                if (!mvDbmsBizPartnerBranches.isEmpty() && mvDbmsBizPartnerBranches.get(0).getIsDeleted() != mbIsDeleted) { // homogenize deletion status of headquarters
                    mvDbmsBizPartnerBranches.get(0).setIsDeleted(mbIsDeleted);
                    mvDbmsBizPartnerBranches.get(0).setIsRegistryEdited(true);
                }

                for (SDataBizPartnerBranch bpb : mvDbmsBizPartnerBranches) {
                    // save only new or edited branches:
                    
                    if (bpb.getIsRegistryNew() || bpb.getIsRegistryEdited()) {
                        bpb.setFkBizPartnerId(mnPkBizPartnerId);
                        
                        if (bpb.getIsRegistryNew()) {
                            bpb.setFkUserNewId(userId);
                        }
                        else {
                            bpb.setFkUserEditId(userId);
                        }
                        
                        if (bpb.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
                
                // Save as well notes:

                String sql = "DELETE FROM erp.bpsu_bp_nts "
                        + "WHERE id_bp = " + mnPkBizPartnerId + ";"; // first delete all existing notes
                connection.createStatement().execute(sql);

                for (SDataBizPartnerNote note : mvDbmsBizPartnerNotes) {
                    // save all notes:
                    
                    note.setPkBizPartnerId(mnPkBizPartnerId);
                    note.setPkNotesId(0); // assure all notes are treated as new
                    
                    if (note.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save as well customer configuration, if applies:

                if (moDbmsDataCustomerConfig != null) {
                    // save only new or edited registry:
                    
                    if (moDbmsDataCustomerConfig.getIsRegistryNew() || moDbmsDataCustomerConfig.getIsRegistryEdited() || moDbmsDataCustomerConfig.getIsDeleted() != mbIsDeleted) {
                        moDbmsDataCustomerConfig.setPkCustomerId(mnPkBizPartnerId);
                        moDbmsDataCustomerConfig.setIsDeleted(mbIsDeleted); // homogenize deletion status

                        if (moDbmsDataCustomerConfig.getIsRegistryNew()) {
                            moDbmsDataCustomerConfig.setFkUserNewId(userId);
                        }
                        else {
                            moDbmsDataCustomerConfig.setFkUserEditId(userId);
                        }

                        if (moDbmsDataCustomerConfig.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save as well sales-agent configuration, if applies:

                if (moDbmsDataConfigurationSalesAgent != null) {
                    // save only new or edited registry:
                    
                    if (moDbmsDataConfigurationSalesAgent.getIsRegistryNew() || moDbmsDataConfigurationSalesAgent.getIsRegistryEdited()) {
                        moDbmsDataConfigurationSalesAgent.setPkSalesAgentId(mnPkBizPartnerId);
                        moDbmsDataConfigurationSalesAgent.setIsDeleted(mbIsDeleted); // homogenize deletion status

                        if (moDbmsDataConfigurationSalesAgent.getIsRegistryNew()) {
                            moDbmsDataConfigurationSalesAgent.setFkUserNewId(userId);
                        }
                        else {
                            moDbmsDataConfigurationSalesAgent.setFkUserEditId(userId);
                        }

                        if (moDbmsDataConfigurationSalesAgent.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save as well employee data, if applies:

                if (moDbmsDataEmployee != null) {
                    // save only new or edited registry:
                    
                    if (moDbmsDataEmployee.getIsRegistryNew() || moDbmsDataEmployee.getIsRegistryEdited()) {
                        moDbmsDataEmployee.setPkEmployeeId(mnPkBizPartnerId);

                        if (moDbmsDataEmployee.getIsRegistryNew()) {
                            //moDbmsDataEmployee.setFkUserNewId(userId); // not implemented, because class is aligned to framework SA 1.0
                            moDbmsDataEmployee.setFkUserInsertId(userId); // aligned to framework SA 1.0
                        }
                        else {
                            //moDbmsDataEmployee.setFkUserEditId(userId); // not implemented, because class is aligned to framework SA 1.0
                            moDbmsDataEmployee.setFkUserUpdateId(userId); // aligned to framework SA 1.0
                        }

                        if (moDbmsDataEmployee.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
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

    /**
     * Get headquarters branch.
     * @return <code>SDataBizPartnerBranch</code>.
     */
    public erp.mbps.data.SDataBizPartnerBranch getDbmsBizPartnerBranchHq() {
        SDataBizPartnerBranch branchHq = null;

        for (SDataBizPartnerBranch bpb : mvDbmsBizPartnerBranches) {
            if (bpb.getFkBizPartnerBranchTypeId() == SDataConstantsSys.BPSS_TP_BPB_HQ) {
                branchHq = bpb;
                break;
            }
        }

        return branchHq;
    }
    
    public erp.mbps.data.SDataBizPartnerBranch getDbmsBizPartnerBranch(int[] branchKey) {
        SDataBizPartnerBranch branch = null;

        for (SDataBizPartnerBranch bpb : mvDbmsBizPartnerBranches) {
            if (SLibUtilities.compareKeys(branchKey, bpb.getPrimaryKey())) {
                branch = bpb;
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
            branch = getDbmsBizPartnerBranchHq();
        }
        else {
            branch = getDbmsBizPartnerBranch(keyBranch_n);
        }

        if (branch != null) {
            contact = getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(0);

            mails = contact.getEmail01();

            if (mails.isEmpty()) {
                for (int i = 0; i < anTypesContact.length; i++) {
                    for (int j = 0; j < branch.getDbmsBizPartnerBranchContacts().size(); j++) {
                        contact = branch.getDbmsBizPartnerBranchContacts().get(j);

                        if (contactType != SLibConstants.UNDEFINED && contact.getFkContactTypeId() == contactType) {
                            mails = contact.getEmail01();

                            if (mails.isEmpty()) {
                                mails = contact.getEmail02();
                            }
                            break;
                        }
                        else if (contact.getFkContactTypeId() == anTypesContact[i] && !contact.getIsDeleted()) {
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
        int[] key = null;
    
        if (moDbmsDataCustomerConfig != null) {
            SDataBizPartnerBranch branch = getDbmsBizPartnerBranch(branchKey);
            SDataCustomerBranchConfig branchConfig = null;

            if (branch != null) {
                branchConfig =  branch.getDbmsDataCustomerBranchConfig();
            }
            else {
                branchConfig =  mvDbmsBizPartnerBranches.get(0).getDbmsDataCustomerBranchConfig();
            }

            if (branchConfig != null && !branchConfig.getIsDeleted() && branchConfig.getFkSalesAgentId_n() != 0) {
                key = new int[] { branchConfig.getFkSalesAgentId_n() };
            }
            else if (moDbmsDataCustomerConfig.getFkSalesAgentId_n() != 0) {
                key = new int[] { moDbmsDataCustomerConfig.getFkSalesAgentId_n() };
            }
        }

        return key;
    }

    public int[] getSalesSupervisorKey(int[] branchKey) {
        int[] key = null;

        if (moDbmsDataCustomerConfig != null) {
            SDataBizPartnerBranch branch = getDbmsBizPartnerBranch(branchKey);
            SDataCustomerBranchConfig branchConfig = null;

            if (branch != null) {
                branchConfig =  branch.getDbmsDataCustomerBranchConfig();
            }
            else {
                branchConfig =  mvDbmsBizPartnerBranches.get(0).getDbmsDataCustomerBranchConfig();
            }

            if (branchConfig != null && !branchConfig.getIsDeleted() && branchConfig.getFkSalesSupervisorId_n() != 0) {
                key = new int[] { branchConfig.getFkSalesSupervisorId_n() };
            }
            else if (moDbmsDataCustomerConfig.getFkSalesSupervisorId_n() != 0) {
                key = new int[] { moDbmsDataCustomerConfig.getFkSalesSupervisorId_n() };
            }
        }

        return key;
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
    
    /**
     * Save benefit tables of employee in SBA-Lib style, in a post-save request.
     * Note that this registry's primary key must be already set before invoking this method.
     * @param session
     * @throws Exception 
     */
    public void saveEmployeeBenefitTables(final SGuiSession session) throws Exception {
        if (moAuxDbEmployeeBenefitTables != null) {
            moAuxDbEmployeeBenefitTables.setPkEmployeeId(mnPkBizPartnerId);
            moAuxDbEmployeeBenefitTables.save(session);
        }
    }
}
