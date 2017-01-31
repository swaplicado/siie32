/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Sergio Flores, Uriel Castañeda
 */
public class SDataCompany extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCompanyId;
    protected java.lang.String msKey;
    protected java.lang.String msCompany;
    protected java.lang.String msDatabase;
    protected boolean mbIsModuleCfg;
    protected boolean mbIsModuleFin;
    protected boolean mbIsModulePur;
    protected boolean mbIsModuleSal;
    protected boolean mbIsModuleInv;
    protected boolean mbIsModuleMkt;
    protected boolean mbIsModuleLog;
    protected boolean mbIsModuleMfg;
    protected boolean mbIsModuleHrs;
    protected boolean mbIsModuleQlt;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected erp.mbps.data.SDataBizPartner moDbmsDataCompany;
    protected java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> mvDbmsCompanyBranchEntities;

    public SDataCompany() {
        super(SDataConstants.CFGU_CO);
        mvDbmsCompanyBranchEntities = new Vector<SDataCompanyBranchEntity>();
        reset();
    }

    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setKey(java.lang.String s) { msKey = s; }
    public void setCompany(java.lang.String s) { msCompany = s; }
    public void setDatabase(java.lang.String s) { msDatabase = s; }
    public void setIsModuleCfg(boolean b) { mbIsModuleCfg = b; }
    public void setIsModuleFin(boolean b) { mbIsModuleFin = b; }
    public void setIsModulePur(boolean b) { mbIsModulePur = b; }
    public void setIsModuleSal(boolean b) { mbIsModuleSal = b; }
    public void setIsModuleInv(boolean b) { mbIsModuleInv = b; }
    public void setIsModuleMkt(boolean b) { mbIsModuleMkt = b; }
    public void setIsModuleLog(boolean b) { mbIsModuleLog = b; }
    public void setIsModuleMfg(boolean b) { mbIsModuleMfg = b; }
    public void setIsModuleHrs(boolean b) { mbIsModuleHrs = b; }
    public void setIsModuleQty(boolean b) { mbIsModuleQlt = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCompanyId() { return mnPkCompanyId; }
    public java.lang.String getKey() { return msKey; }
    public java.lang.String getCompany() { return msCompany; }
    public java.lang.String getDatabase() { return msDatabase; }
    public boolean getIsModuleCfg() { return mbIsModuleCfg; }
    public boolean getIsModuleFin() { return mbIsModuleFin; }
    public boolean getIsModulePur() { return mbIsModulePur; }
    public boolean getIsModuleSal() { return mbIsModuleSal; }
    public boolean getIsModuleInv() { return mbIsModuleInv; }
    public boolean getIsModuleMkt() { return mbIsModuleMkt; }
    public boolean getIsModuleLog() { return mbIsModuleLog; }
    public boolean getIsModuleMfg() { return mbIsModuleMfg; }
    public boolean getIsModuleHrs() { return mbIsModuleHrs; }
    public boolean getIsModuleQty() { return mbIsModuleQlt; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsDataCompany(erp.mbps.data.SDataBizPartner o) { moDbmsDataCompany = o; }

    public erp.mbps.data.SDataBizPartner getDbmsDataCompany() { return moDbmsDataCompany; }
    public java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> getDbmsCompanyBranchEntities() { return mvDbmsCompanyBranchEntities; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCompanyId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCompanyId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCompanyId = 0;
        msKey = "";
        msCompany = "";
        msDatabase = "";
        mbIsModuleCfg = false;
        mbIsModuleFin = false;
        mbIsModulePur = false;
        mbIsModuleSal = false;
        mbIsModuleInv = false;
        mbIsModuleMkt = false;
        mbIsModuleLog = false;
        mbIsModuleMfg = false;
        mbIsModuleHrs = false;
        mbIsModuleQlt = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        moDbmsDataCompany = null;
        mvDbmsCompanyBranchEntities.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        Statement statementAux = null;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.cfgu_co WHERE id_co = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCompanyId = resultSet.getInt("id_co");
                msKey = resultSet.getString("co_key");
                msCompany = resultSet.getString("co");
                msDatabase = resultSet.getString("bd");
                mbIsModuleCfg = resultSet.getBoolean("b_mod_cfg");
                mbIsModuleFin = resultSet.getBoolean("b_mod_fin");
                mbIsModulePur = resultSet.getBoolean("b_mod_pur");
                mbIsModuleSal = resultSet.getBoolean("b_mod_sal");
                mbIsModuleInv = resultSet.getBoolean("b_mod_inv");
                mbIsModuleMkt = resultSet.getBoolean("b_mod_mkt");
                mbIsModuleLog = resultSet.getBoolean("b_mod_log");
                mbIsModuleMfg = resultSet.getBoolean("b_mod_mfg");
                mbIsModuleHrs = resultSet.getBoolean("b_mod_hrs");
                mbIsModuleQlt = resultSet.getBoolean("b_mod_qlt");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read aswell company as business partner:

                if (statement.getConnection().getCatalog().compareTo("erp") != 0) {
                    /*
                     * When run in ERP Server, default connection is stablished to 'erp' database,
                     * so this must be read only from a company database connection.
                     */

                    moDbmsDataCompany = new SDataBizPartner();
                    if (moDbmsDataCompany.read(new int[] { mnPkCompanyId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell company branch entities:

                statementAux = statement.getConnection().createStatement();

                sql = "SELECT id_cob, id_ent, ent, fid_ct_ent FROM erp.cfgu_cob_ent WHERE fid_co = " + mnPkCompanyId + " ORDER BY id_cob, fid_ct_ent, ent, id_ent ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataCompanyBranchEntity entity = new SDataCompanyBranchEntity();

                    if (entity.read(new int[] { resultSet.getInt("id_cob"), resultSet.getInt("id_ent") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsCompanyBranchEntities.add(entity);
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL erp.cfgu_co_save("
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCompanyId);
            callableStatement.setString(nParam++, msKey);
            callableStatement.setString(nParam++, msCompany);
            callableStatement.setString(nParam++, msDatabase);
            callableStatement.setBoolean(nParam++, mbIsModuleCfg);
            callableStatement.setBoolean(nParam++, mbIsModuleFin);
            callableStatement.setBoolean(nParam++, mbIsModulePur);
            callableStatement.setBoolean(nParam++, mbIsModuleSal);
            callableStatement.setBoolean(nParam++, mbIsModuleInv);
            callableStatement.setBoolean(nParam++, mbIsModuleMkt);
            callableStatement.setBoolean(nParam++, mbIsModuleLog);
            callableStatement.setBoolean(nParam++, mbIsModuleMfg);
            callableStatement.setBoolean(nParam++, mbIsModuleHrs);
            callableStatement.setBoolean(nParam++, mbIsModuleQlt);            
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell company as business partner:

                if (moDbmsDataCompany.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
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

    public erp.mcfg.data.SDataCompanyBranchEntity getDbmsCompanyBranchEntity(int[] pk) {
        SDataCompanyBranchEntity entity = null;

        for (int i = 0; i < mvDbmsCompanyBranchEntities.size(); i++) {
            if (SLibUtilities.compareKeys(pk, mvDbmsCompanyBranchEntities.get(i).getPrimaryKey())) {
                entity = mvDbmsCompanyBranchEntities.get(i);
                break;
            }
        }

        return entity;
    }
}
