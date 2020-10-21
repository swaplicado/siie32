/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.mmkt.data.SDataCustomerBranchConfig;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Sergio Flores
 */
public class SDataBizPartnerBranch extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerBranchId;
    protected java.lang.String msBizPartnerBranch;
    protected java.lang.String msCode;
    protected boolean mbIsAddressPrintable;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerId;
    protected int mnFkBizPartnerBranchTypeId;
    protected int mnFkTaxRegionId_n;
    protected int mnFkAddressFormatTypeId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsBizPartner;
    protected java.lang.String msDbmsBizPartnerBranchType;
    protected java.lang.String msDbmsTaxRegion;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    
    protected boolean mbAuxSaveBkc;

    protected erp.mbps.data.SDataCompanyBranchBkc moDbmsDataCompanyBranchBkc;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchAddress> mvDbmsBizPartnerBranchAddresses;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchBankAccount> mvDbmsBizPartnerBranchBankAccounts;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchContact> mvDbmsBizPartnerBranchContacts;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchNote> mvDbmsBizPartnerBranchNotes;
    protected java.util.Vector<erp.mmkt.data.SDataCustomerBranchConfig> mvDbmsCustomerBranchConfig; // it should be only one configuration registry per branch
    protected java.util.Vector<erp.lib.form.SFormComponentItem> mvDnsForDps;
    protected java.util.Vector<erp.lib.form.SFormComponentItem> mvDnsForDiog;

    public SDataBizPartnerBranch() {
        super(SDataConstants.BPSU_BPB);
        mvDbmsBizPartnerBranchAddresses = new Vector<SDataBizPartnerBranchAddress>();
        mvDbmsBizPartnerBranchBankAccounts = new Vector<SDataBizPartnerBranchBankAccount>();
        mvDbmsBizPartnerBranchContacts = new Vector<SDataBizPartnerBranchContact>();
        mvDbmsBizPartnerBranchNotes = new Vector<SDataBizPartnerBranchNote>();
        mvDbmsCustomerBranchConfig = new Vector<SDataCustomerBranchConfig>();
        mvDnsForDps = new Vector<SFormComponentItem>();
        mvDnsForDiog = new Vector<SFormComponentItem>();
        reset();
    }

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setBizPartnerBranch(java.lang.String s) { msBizPartnerBranch = s; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setIsAddressPrintable(boolean b) { mbIsAddressPrintable = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkBizPartnerBranchTypeId(int n) { mnFkBizPartnerBranchTypeId = n; }
    public void setFkTaxRegionId_n(int n) { mnFkTaxRegionId_n = n; }
    public void setFkAddressFormatTypeId_n(int n) { mnFkAddressFormatTypeId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public void setAuxSaveBkc(boolean b) { mbAuxSaveBkc = b; }
    
    public void setDbmsDataCustomerBranchConfig(java.util.Vector<erp.mmkt.data.SDataCustomerBranchConfig> o) { mvDbmsCustomerBranchConfig = o; }

    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public java.lang.String getBizPartnerBranch() { return msBizPartnerBranch; }
    public java.lang.String getCode() { return msCode; }
    public boolean getIsAddressPrintable() { return mbIsAddressPrintable; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkBizPartnerBranchTypeId() { return mnFkBizPartnerBranchTypeId; }
    public int getFkTaxRegionId_n() { return mnFkTaxRegionId_n; }
    public int getFkAddressFormatTypeId_n() { return mnFkAddressFormatTypeId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public boolean getAuxSaveBkc() { return mbAuxSaveBkc; }

    public void setDbmsBizPartner(java.lang.String s) { msDbmsBizPartner = s; }
    public void setDbmsBizPartnerBranchType(java.lang.String s) { msDbmsBizPartnerBranchType = s; }
    public void setDbmsTaxRegion(java.lang.String s) { msDbmsTaxRegion = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsBizPartner() { return msDbmsBizPartner; }
    public java.lang.String getDbmsBizPartnerBranchType() { return msDbmsBizPartnerBranchType; }
    public java.lang.String getDbmsTaxRegion() { return msDbmsTaxRegion; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public erp.mbps.data.SDataCompanyBranchBkc getDbmsDataCompanyBranchBkc() { return moDbmsDataCompanyBranchBkc; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchAddress> getDbmsBizPartnerBranchAddresses() { return mvDbmsBizPartnerBranchAddresses; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchBankAccount> getDbmsBizPartnerBranchBankAccounts() { return mvDbmsBizPartnerBranchBankAccounts; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchContact> getDbmsBizPartnerBranchContacts() { return mvDbmsBizPartnerBranchContacts; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchNote> getDbmsBizPartnerBranchNotes() { return mvDbmsBizPartnerBranchNotes; }
    public java.util.Vector<erp.mmkt.data.SDataCustomerBranchConfig> getDbmsDataCustomerBranchConfig() { return mvDbmsCustomerBranchConfig; }

    public java.util.Vector<erp.lib.form.SFormComponentItem> getDnsForDps() { return mvDnsForDps; }
    public java.util.Vector<erp.lib.form.SFormComponentItem> getDnsForDiog() { return mvDnsForDiog; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerBranchId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

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

        mbAuxSaveBkc = false;
        
        moDbmsDataCompanyBranchBkc = null;
        mvDbmsBizPartnerBranchAddresses.clear();
        mvDbmsBizPartnerBranchBankAccounts.clear();
        mvDbmsBizPartnerBranchNotes.clear();
        mvDbmsCustomerBranchConfig.clear();
        mvDnsForDps.clear();
        mvDnsForDiog.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int numStart = 0;
        int numEnd_n = 0;
        int[] key = (int[]) pk;
        String sql = "";
        String table = "";
        String field = "";
        String fieldsApprove = "";
        String query = "";
        String[] fields = null;
        ResultSet resultSet = null;
        Statement statementAux = null;
        Vector<SFormComponentItem> vector = null;
        SDataCustomerBranchConfig dataCustomerConfigBranch = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT bpb.*, bp.bp, tp_bpb.tp_bpb, tax_reg.tax_reg, un.usr, ue.usr, ud.usr " +
                    "FROM erp.bpsu_bpb AS bpb " +
                    "INNER JOIN erp.bpsu_bp AS bp ON " +
                    "bpb.fid_bp = bp.id_bp " +
                    "INNER JOIN erp.bpss_tp_bpb AS tp_bpb ON " +
                    "bpb.fid_tp_bpb = tp_bpb.id_tp_bpb " +
                    "INNER JOIN erp.usru_usr AS un ON " +
                    "bpb.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON " +
                    "bpb.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON " +
                    "bpb.fid_usr_del = ud.id_usr " +
                    "LEFT OUTER JOIN erp.finu_tax_reg AS tax_reg ON " +
                    "bpb.fid_tax_reg_n = tax_reg.id_tax_reg " +
                    "WHERE bpb.id_bpb = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerBranchId = resultSet.getInt("bpb.id_bpb");
                msBizPartnerBranch = resultSet.getString("bpb.bpb");
                msCode = resultSet.getString("bpb.code");
                mbIsAddressPrintable = resultSet.getBoolean("bpb.b_add_prt");
                mbIsDeleted = resultSet.getBoolean("bpb.b_del");
                mnFkBizPartnerId = resultSet.getInt("bpb.fid_bp");
                mnFkBizPartnerBranchTypeId = resultSet.getInt("bpb.fid_tp_bpb");
                mnFkTaxRegionId_n = resultSet.getInt("fid_tax_reg_n");
                mnFkAddressFormatTypeId_n = resultSet.getInt("fid_tp_add_fmt_n");
                mnFkUserNewId = resultSet.getInt("bpb.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("bpb.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("bpb.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("bpb.ts_new");
                mtUserEditTs = resultSet.getTimestamp("bpb.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("bpb.ts_del");

                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");
                msDbmsBizPartner = resultSet.getString("bp.bp");
                msDbmsBizPartnerBranchType = resultSet.getString("tp_bpb.tp_bpb");
                msDbmsTaxRegion = resultSet.getString("tax_reg.tax_reg");

                // Read aswell company branch bookkeeping center:

                sql = "SELECT id_bkc FROM fin_cob_bkc WHERE id_cob = " + key[0] + " ";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsDataCompanyBranchBkc = new SDataCompanyBranchBkc();
                    if (moDbmsDataCompanyBranchBkc.read(new int[] { mnPkBizPartnerBranchId, resultSet.getInt("id_bkc") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell business partner branch addresses:

                statementAux = statement.getConnection().createStatement();

                mvDbmsBizPartnerBranchAddresses.removeAllElements();
                sql = "SELECT id_bpb, id_add FROM erp.bpsu_bpb_add WHERE id_bpb = " + key[0] + " ORDER BY fid_tp_add";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerBranchAddress BizPartnerBranchAddress = new SDataBizPartnerBranchAddress();
                    if (BizPartnerBranchAddress.read(new int[] { resultSet.getInt("id_bpb"), resultSet.getInt("id_add") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerBranchAddresses.add(BizPartnerBranchAddress);
                    }
                }

                // Read aswell the business partner branch bank accounts:

                statementAux = statement.getConnection().createStatement();

                mvDbmsBizPartnerBranchBankAccounts.removeAllElements();
                sql = "SELECT id_bpb, id_bank_acc FROM erp.bpsu_bank_acc WHERE id_bpb = " + key[0] + " ORDER BY id_bank_acc";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerBranchBankAccount bizPartnerBranchBankAccount = new SDataBizPartnerBranchBankAccount();
                    if (bizPartnerBranchBankAccount.read(new int[] { resultSet.getInt("id_bpb"), resultSet.getInt("id_bank_acc") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerBranchBankAccounts.add(bizPartnerBranchBankAccount);
                    }
                }

                // Read aswell the business partner branch contacts:

                statementAux = statement.getConnection().createStatement();

                mvDbmsBizPartnerBranchContacts.removeAllElements();
                sql = "SELECT id_bpb, id_con FROM erp.bpsu_bpb_con WHERE id_bpb = " + key[0] + " ORDER BY id_con";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerBranchContact bizPartnerBranchContact = new SDataBizPartnerBranchContact();
                    if (bizPartnerBranchContact.read(new int[] { resultSet.getInt("id_bpb"), resultSet.getInt("id_con") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerBranchContacts.add(bizPartnerBranchContact);
                    }
                }

                // Read aswell business partner branch notes:

                statementAux = statement.getConnection().createStatement();

                mvDbmsBizPartnerBranchNotes.removeAllElements();
                sql = "SELECT id_bpb, id_nts FROM erp.bpsu_bpb_nts WHERE id_bpb = " + key[0] + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerBranchNote BizPartnerBranchNote = new SDataBizPartnerBranchNote();
                    if (BizPartnerBranchNote.read(new int[] { resultSet.getInt("id_bpb"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerBranchNotes.add(BizPartnerBranchNote);
                    }
                }

                // Read configuration of customer braches:

                statementAux = statement.getConnection().createStatement();
                mvDbmsCustomerBranchConfig.removeAllElements();
                sql = "SELECT bpb.bpb, bp.bp, cusb.* " +
                        "FROM mkt_cfg_cusb AS cusb " +
                            "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                                "cusb.id_cusb = bpb.id_bpb " +
                            "INNER JOIN mkt_cfg_cus AS cus ON " +
                                "bpb.fid_bp = cus.id_cus " +
                            "INNER JOIN erp.bpsu_bp AS bp ON " +
                                "bpb.fid_bp = bp.id_bp " +
                        "WHERE cusb.id_cusb = " + key[0] + " " +
                        "ORDER BY bpb.bpb, bp.bp ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    dataCustomerConfigBranch = new SDataCustomerBranchConfig();
                    if (dataCustomerConfigBranch.read(new int[] { resultSet.getInt("cusb.id_cusb") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsCustomerBranchConfig.add(dataCustomerConfigBranch);
                    }
                }

                if (statement.getConnection().getCatalog().compareTo("erp") != 0) {
                    /*
                     * When run in ERP Server, default connection is stablished to 'erp' database,
                     * so this must be read only from a company database connection.
                     */

                    for (int i = 1; i <= 2; i++) {
                        switch (i) {
                            case 1:
                                table = "dps";
                                field = "dps";
                                fieldsApprove = "cs.approve_year_n, cs.approve_num_n, ";
                                fields = new String[] { "fid_ct_" + field, "fid_cl_" + field, "fid_tp_" + field };
                                vector = mvDnsForDps;
                                break;
                            case 2:
                                table = "diog";
                                field = "iog";
                                fieldsApprove = "";
                                fields = new String[] { "fid_ct_" + field, "fid_cl_" + field };
                                vector = mvDnsForDiog;
                                break;
                            default:
                        }

                        query = "";
                        for (int j = 0; j < fields.length; j++) {
                            query += "s." + fields[j] + ", ";
                        }

                        sql = "SELECT s.dns, " + query + fieldsApprove + "cs.num_start, cs.num_end_n " +
                                "FROM trn_dnc_" + table + "_cob AS d INNER JOIN trn_dnc_" + table + " AS c ON " +
                                "d.id_cob = " + key[0] + " AND d.id_dnc = c.id_dnc AND " +
                                "d.b_del = FALSE AND c.b_del = FALSE " +
                                "INNER JOIN trn_dnc_" + table + "_dns AS cs ON " +
                                "c.id_dnc = cs.id_dnc AND cs.b_del = FALSE " +
                                "INNER JOIN trn_dns_" + table + " AS s ON " +
                                "cs.id_dns = s.id_dns AND s.b_del = FALSE " +
                                "ORDER BY s.dns, cs.num_start ";
                        resultSet = statement.executeQuery(sql);

                        while (resultSet.next()) {
                            int[] itemPk = new int[fields.length];
                            SFormComponentItem item = null;

                            for (int j = 0; j < itemPk.length; j++) {
                                itemPk[j] = resultSet.getInt(fields[j]);
                            }

                            numStart = resultSet.getInt("num_start");
                            numEnd_n = resultSet.getInt("num_end_n");
                            if (resultSet.wasNull()) {
                                numEnd_n = -1;
                            }

                            item = new SFormComponentItem(itemPk, resultSet.getString("s.dns"));
                            if (i == 1) {
                                // DPS:
                                item.setComplement(new int[] { numStart, numEnd_n, resultSet.getInt("cs.approve_year_n"), resultSet.getInt("cs.approve_num_n") });
                            }
                            else {
                                // DIOG:
                                item.setComplement(new int[] { numStart, numEnd_n });
                            }
                            vector.add(item);
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
        String sql = "";
        Statement statement = null;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL erp.bpsu_bpb_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerBranchId);
            callableStatement.setString(nParam++, msBizPartnerBranch);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setBoolean(nParam++, mbIsAddressPrintable);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            callableStatement.setInt(nParam++, mnFkBizPartnerBranchTypeId);
            if (mnFkTaxRegionId_n > 0) callableStatement.setInt(nParam++, mnFkTaxRegionId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkAddressFormatTypeId_n > 0) callableStatement.setInt(nParam++, mnFkAddressFormatTypeId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkBizPartnerBranchId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                statement = connection.createStatement();

                 if (mbAuxSaveBkc) {
                     moDbmsDataCompanyBranchBkc = new SDataCompanyBranchBkc();
                     
                     moDbmsDataCompanyBranchBkc.setPkCompanyBranchId(mnPkBizPartnerBranchId);
                     moDbmsDataCompanyBranchBkc.setPkBookkepingCenterId(1);
                     moDbmsDataCompanyBranchBkc.setFkUserNewId(mnFkUserNewId);
                    if (moDbmsDataCompanyBranchBkc.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell business partner branch adddress:

                for (int i = 0; i < mvDbmsBizPartnerBranchAddresses.size(); i++) {
                    mvDbmsBizPartnerBranchAddresses.get(i).setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                    if (mvDbmsBizPartnerBranchAddresses.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell business partner branch bank accounts:

                for (int i = 0; i < mvDbmsBizPartnerBranchBankAccounts.size(); i++) {
                    mvDbmsBizPartnerBranchBankAccounts.get(i).setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                    if (mvDbmsBizPartnerBranchBankAccounts.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell business partner branch contacts:

                for (int i = 0; i < mvDbmsBizPartnerBranchContacts.size(); i++) {
                    mvDbmsBizPartnerBranchContacts.get(i).setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                    if (mvDbmsBizPartnerBranchContacts.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell business partner branch notes:

                sql = "DELETE FROM erp.bpsu_bpb_nts WHERE id_bpb = " + mnPkBizPartnerBranchId + " ";
                statement.execute(sql);

                for (int i = 0; i < mvDbmsBizPartnerBranchNotes.size(); i++) {
                    mvDbmsBizPartnerBranchNotes.get(i).setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                    if (mvDbmsBizPartnerBranchNotes.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell customer branch configuration:
                
                for (int i = 0; i < mvDbmsCustomerBranchConfig.size(); i++) {
                    mvDbmsCustomerBranchConfig.get(i).setPkCustomerBranchId(mnPkBizPartnerBranchId);
                    if (mvDbmsCustomerBranchConfig.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
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

    /**
     * Get requested address.
     * @param addressKey Address key.
     * @return <code>SDataBizPartnerBranchAddress</code>
     */
    public erp.mbps.data.SDataBizPartnerBranchAddress getDbmsBizPartnerBranchAddress(final int[] addressKey) {
        SDataBizPartnerBranchAddress address = null;
        
        for (SDataBizPartnerBranchAddress bpba : mvDbmsBizPartnerBranchAddresses) {
            if (bpba.getPkBizPartnerBranchId() == addressKey[0] && bpba.getPkAddressId() == addressKey[1]) {
                address = bpba;
                break;
            }
        }
        
        return address;
    }

    /**
     * Get official address.
     * @return <code>SDataBizPartnerBranchAddress</code>
     */
    public erp.mbps.data.SDataBizPartnerBranchAddress getDbmsBizPartnerBranchAddressOfficial() {
        SDataBizPartnerBranchAddress address = null;

        for (SDataBizPartnerBranchAddress bpba : mvDbmsBizPartnerBranchAddresses) {
            if (bpba.getFkAddressTypeId() == SDataConstantsSys.BPSS_TP_ADD_OFF) {
                address = bpba;
                break;
            }
        }

        return address;
    }

    /**
     * Get requested contact.
     * @param contactKey Contact key.
     * @return <code>SDataBizPartnerBranchContact</code>
     */
    public erp.mbps.data.SDataBizPartnerBranchContact getDbmsBizPartnerBranchContact(int[] contactKey) {
        SDataBizPartnerBranchContact contact = null;

        for (SDataBizPartnerBranchContact bpbc : mvDbmsBizPartnerBranchContacts) {
            if (SLibUtilities.compareKeys(contactKey, bpbc.getPrimaryKey())) {
                contact = bpbc;
                break;
            }
        }

        return contact;
    }

    /**
     * Get contact by contact type.
     * @param contactType Contact type (constants defined in SDataConstantsSys.BPSS_TP_CON_...)
     * @param includeDefaultContact Include the default contact when searching a matching contact.
     * @return First contact found of given contact type as <code>SDataBizPartnerBranchContact</code>.
     */
    public erp.mbps.data.SDataBizPartnerBranchContact getDbmsBizPartnerBranchContactByType(final int contactType, final boolean includeDefaultContact) {
        SDataBizPartnerBranchContact contact = null;
        
        for (SDataBizPartnerBranchContact bpbc : mvDbmsBizPartnerBranchContacts) {
            if (bpbc.getFkContactTypeId() == contactType && ((includeDefaultContact && bpbc.getPkContactId() == 1) || (!includeDefaultContact && bpbc.getPkContactId() != 1))) {
                contact = bpbc;
                break;
            }
        }
        
        return contact;
    }
    
    /**
     * Get requested notes.
     * @param notesKey Notes key.
     * @return <code>SDataBizPartnerBranchNote</code>
     */
    public erp.mbps.data.SDataBizPartnerBranchNote getDbmsBizPartnerBranchNotes(int[] notesKey) {
        SDataBizPartnerBranchNote notes = null;

        for (SDataBizPartnerBranchNote bpbn : mvDbmsBizPartnerBranchNotes) {
            if (SLibUtilities.compareKeys(notesKey, bpbn.getPrimaryKey())) {
                notes = bpbn;
                break;
            }
        }

        return notes;
    }

    /**
     * Get requested configuration.
     * @param configKey Configuration key.
     * @return <code>SDataCustomerBranchConfig</code>
     */
    public erp.mmkt.data.SDataCustomerBranchConfig getDbmsCustomerBranchConfig(int[] configKey) {
        SDataCustomerBranchConfig config = null;

        for (SDataCustomerBranchConfig cbc : mvDbmsCustomerBranchConfig) {
            if (SLibUtilities.compareKeys(configKey, cbc.getPrimaryKey())) {
                config = cbc;
                break;
            }
        }

        return config;
    }
}
