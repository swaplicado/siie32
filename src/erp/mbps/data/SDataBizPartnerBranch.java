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
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchContact> mvDbmsBizPartnerBranchContacts;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchBankAccount> mvDbmsBizPartnerBranchBankAccounts;
    protected java.util.Vector<erp.mbps.data.SDataBizPartnerBranchNote> mvDbmsBizPartnerBranchNotes;
    protected erp.mmkt.data.SDataCustomerBranchConfig moDbmsDataCustomerBranchConfig;
    protected java.util.Vector<erp.lib.form.SFormComponentItem> mvDnsForDps;
    protected java.util.Vector<erp.lib.form.SFormComponentItem> mvDnsForDiog;

    public SDataBizPartnerBranch() {
        super(SDataConstants.BPSU_BPB);
        mvDbmsBizPartnerBranchAddresses = new Vector<>();
        mvDbmsBizPartnerBranchBankAccounts = new Vector<>();
        mvDbmsBizPartnerBranchContacts = new Vector<>();
        mvDbmsBizPartnerBranchNotes = new Vector<>();
        mvDnsForDps = new Vector<>();
        mvDnsForDiog = new Vector<>();
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

    public void setAuxSaveBkc(boolean b) { mbAuxSaveBkc = b; }
    public void setDbmsDataCompanyBranchBkc(erp.mbps.data.SDataCompanyBranchBkc o) { moDbmsDataCompanyBranchBkc = o; }
    public void setDbmsDataCustomerBranchConfig(erp.mmkt.data.SDataCustomerBranchConfig o) { moDbmsDataCustomerBranchConfig = o; }
    
    public boolean getAuxSaveBkc() { return mbAuxSaveBkc; }
    public erp.mbps.data.SDataCompanyBranchBkc getDbmsDataCompanyBranchBkc() { return moDbmsDataCompanyBranchBkc; }
    public erp.mmkt.data.SDataCustomerBranchConfig getDbmsDataCustomerBranchConfig() { return moDbmsDataCustomerBranchConfig; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchAddress> getDbmsBizPartnerBranchAddresses() { return mvDbmsBizPartnerBranchAddresses; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchBankAccount> getDbmsBizPartnerBranchBankAccounts() { return mvDbmsBizPartnerBranchBankAccounts; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchContact> getDbmsBizPartnerBranchContacts() { return mvDbmsBizPartnerBranchContacts; }
    public java.util.Vector<erp.mbps.data.SDataBizPartnerBranchNote> getDbmsBizPartnerBranchNotes() { return mvDbmsBizPartnerBranchNotes; }
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

        msDbmsBizPartner = "";
        msDbmsBizPartnerBranchType = "";
        msDbmsTaxRegion = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    
        mbAuxSaveBkc = false;
        moDbmsDataCompanyBranchBkc = null;
        mvDbmsBizPartnerBranchAddresses.clear();
        mvDbmsBizPartnerBranchContacts.clear();
        mvDbmsBizPartnerBranchBankAccounts.clear();
        mvDbmsBizPartnerBranchNotes.clear();
        moDbmsDataCustomerBranchConfig = null;
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

                statementAux = statement.getConnection().createStatement();
                
                // Read aswell company branch bookkeeping center:

                sql = "SELECT id_bkc FROM fin_cob_bkc WHERE id_cob = " + key[0] + ";";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsDataCompanyBranchBkc = new SDataCompanyBranchBkc();
                    if (moDbmsDataCompanyBranchBkc.read(new int[] { mnPkBizPartnerBranchId, resultSet.getInt("id_bkc") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }
                
                // Read aswell business partner branch addresses:

                sql = "SELECT id_bpb, id_add FROM erp.bpsu_bpb_add WHERE id_bpb = " + key[0] + " ORDER BY fid_tp_add;";
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

                // Read aswell the business partner branch contacts:

                sql = "SELECT id_bpb, id_con FROM erp.bpsu_bpb_con WHERE id_bpb = " + key[0] + " ORDER BY id_con;";
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

                // Read aswell the business partner branch bank accounts:

                sql = "SELECT id_bpb, id_bank_acc FROM erp.bpsu_bank_acc WHERE id_bpb = " + key[0] + " ORDER BY id_bank_acc;";
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

                // Read aswell business partner branch notes:

                sql = "SELECT id_bpb, id_nts FROM erp.bpsu_bpb_nts WHERE id_bpb = " + key[0] + " ORDER BY id_nts;";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataBizPartnerBranchNote bizPartnerBranchNote = new SDataBizPartnerBranchNote();
                    if (bizPartnerBranchNote.read(new int[] { resultSet.getInt("id_bpb"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerBranchNotes.add(bizPartnerBranchNote);
                    }
                }

                // Read configuration of customer braches:

                sql = "SELECT id_cusb FROM mkt_cfg_cusb WHERE id_cusb = " + key[0] + ";";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsDataCustomerBranchConfig = new SDataCustomerBranchConfig();
                    if (moDbmsDataCustomerBranchConfig.read(new int[] { resultSet.getInt("id_cusb") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
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
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            int userId = mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId;
            
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
            callableStatement.setInt(nParam++, userId);
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
                 if (mbAuxSaveBkc) {
                     moDbmsDataCompanyBranchBkc = new SDataCompanyBranchBkc();
                     moDbmsDataCompanyBranchBkc.setPkCompanyBranchId(mnPkBizPartnerBranchId);
                     moDbmsDataCompanyBranchBkc.setPkBookkepingCenterId(1);
                     moDbmsDataCompanyBranchBkc.setFkUserNewId(userId);
                     
                    if (moDbmsDataCompanyBranchBkc.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell adddresses:

                if (!mvDbmsBizPartnerBranchAddresses.isEmpty() && mvDbmsBizPartnerBranchAddresses.get(0).getIsDeleted() != mbIsDeleted) { // homogenize deletion status of official address
                    mvDbmsBizPartnerBranchAddresses.get(0).setIsDeleted(mbIsDeleted);
                    mvDbmsBizPartnerBranchAddresses.get(0).setIsRegistryEdited(true);
                }

                for (SDataBizPartnerBranchAddress address : mvDbmsBizPartnerBranchAddresses) {
                    // save only new or edited addresses:
                    
                    if (address.getIsRegistryNew() || address.getIsRegistryEdited()) {
                        address.setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                        
                        if (address.getIsRegistryNew()) {
                            address.setFkUserNewId(userId);
                        }
                        else {
                            address.setFkUserEditId(userId);
                        }
                        
                        if (address.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save aswell contacts:

                if (!mvDbmsBizPartnerBranchContacts.isEmpty() && mvDbmsBizPartnerBranchContacts.get(0).getIsDeleted() != mbIsDeleted) { // homogenize deletion status of official contact
                    mvDbmsBizPartnerBranchContacts.get(0).setIsDeleted(mbIsDeleted);
                    mvDbmsBizPartnerBranchContacts.get(0).setIsRegistryEdited(true);
                }

                for (SDataBizPartnerBranchContact contact : mvDbmsBizPartnerBranchContacts) {
                    // save only new or edited contacts:
                    
                    if (contact.getIsRegistryNew() || contact.getIsRegistryEdited()) {
                        contact.setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                        
                        if (contact.getIsRegistryNew()) {
                            contact.setFkUserNewId(userId);
                        }
                        else {
                            contact.setFkUserEditId(userId);
                        }
                        
                        if (contact.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save aswell bank accounts:

                for (SDataBizPartnerBranchBankAccount bankAccount : mvDbmsBizPartnerBranchBankAccounts) {
                    // save only new or edited bank accounts:
                    
                    if (bankAccount.getIsRegistryNew() || bankAccount.getIsRegistryEdited()) {
                        bankAccount.setPkBizPartnerBranchId(mnPkBizPartnerBranchId);

                        if (bankAccount.getIsRegistryNew()) {
                            bankAccount.setFkUserNewId(userId);
                        }
                        else {
                            bankAccount.setFkUserEditId(userId);
                        }

                        if (bankAccount.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save aswell notes:

                String sql = "DELETE FROM erp.bpsu_bpb_nts "
                        + "WHERE id_bpb = " + mnPkBizPartnerBranchId + ";"; // first delete all existing notes
                connection.createStatement().execute(sql);

                for (SDataBizPartnerBranchNote note : mvDbmsBizPartnerBranchNotes) {
                    // save all notes:
                    
                    note.setPkBizPartnerBranchId(mnPkBizPartnerBranchId);
                    note.setPkNotesId(0); // assure all notes are treated as new
                    
                    if (note.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell customer-branch configurations, if applies:
                
                if (moDbmsDataCustomerBranchConfig != null) {
                    // save only new or edited registry:
                    
                    if (moDbmsDataCustomerBranchConfig.getIsRegistryNew() || moDbmsDataCustomerBranchConfig.getIsRegistryEdited() || moDbmsDataCustomerBranchConfig.getIsDeleted() != mbIsDeleted) {
                        moDbmsDataCustomerBranchConfig.setPkCustomerBranchId(mnPkBizPartnerBranchId);
                        moDbmsDataCustomerBranchConfig.setIsDeleted(mbIsDeleted); // homogenize deletion status
                        
                        if (moDbmsDataCustomerBranchConfig.getIsRegistryNew()) {
                            moDbmsDataCustomerBranchConfig.setFkUserNewId(userId);
                        }
                        else {
                            moDbmsDataCustomerBranchConfig.setFkUserEditId(userId);
                        }
                        
                        if (moDbmsDataCustomerBranchConfig.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
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
     * Get official contact.
     * @return <code>SDataBizPartnerBranchContact</code>
     */
    public erp.mbps.data.SDataBizPartnerBranchContact getDbmsBizPartnerBranchContactOfficial() {
        SDataBizPartnerBranchContact contact = null;

        if (mvDbmsBizPartnerBranchContacts.size() > 0) {
            contact = mvDbmsBizPartnerBranchContacts.get(0);
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
}
