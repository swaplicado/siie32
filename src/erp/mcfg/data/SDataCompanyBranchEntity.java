/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SDataCompanyBranchEntity extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCompanyBranchId;
    protected int mnPkEntityId;
    protected java.lang.String msEntity;
    protected java.lang.String msCode;
    protected boolean mbIsDefault;
    protected boolean mbIsActive;
    protected boolean mbIsActiveIn;
    protected boolean mbIsActiveOut;
    protected boolean mbIsDeleted;
    protected int mnFkCompanyId;
    protected int mnFkEntityCategoryId;
    protected int mnFkEntityTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.lib.form.SFormComponentItem> mvDnsForDps;
    protected java.util.Vector<erp.lib.form.SFormComponentItem> mvDnsForDiog;

    public SDataCompanyBranchEntity() {
        super(SDataConstants.CFGU_COB_ENT);
        mvDnsForDps = new Vector<SFormComponentItem>();
        mvDnsForDiog = new Vector<SFormComponentItem>();
        reset();
    }

    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkEntityId(int n) { mnPkEntityId = n; }
    public void setEntity(java.lang.String s) { msEntity = s; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsActiveIn(boolean b) { mbIsActiveIn = b; }
    public void setIsActiveOut(boolean b) { mbIsActiveOut = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCompanyId(int n) { mnFkCompanyId = n; }
    public void setFkEntityCategoryId(int n) { mnFkEntityCategoryId = n; }
    public void setFkEntityTypeId(int n) { mnFkEntityTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkEntityId() { return mnPkEntityId; }
    public java.lang.String getEntity() { return msEntity; }
    public java.lang.String getCode() { return msCode; }
    public boolean getIsDefault() { return mbIsDefault; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsActiveIn() { return mbIsActiveIn; }
    public boolean getIsActiveOut() { return mbIsActiveOut; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCompanyId() { return mnFkCompanyId; }
    public int getFkEntityCategoryId() { return mnFkEntityCategoryId; }
    public int getFkEntityTypeId() { return mnFkEntityTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public int[] getEntityTypeKey() { return new int[] { mnFkEntityCategoryId, mnFkEntityTypeId }; }

    public java.util.Vector<erp.lib.form.SFormComponentItem> getDnsForDps() { return mvDnsForDps; }
    public java.util.Vector<erp.lib.form.SFormComponentItem> getDnsForDiog() { return mvDnsForDiog; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCompanyBranchId = ((int[]) pk)[0];
        mnPkEntityId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCompanyBranchId, mnPkEntityId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCompanyBranchId = 0;
        mnPkEntityId = 0;
        msEntity = "";
        msCode = "";
        mbIsDefault = false;
        mbIsActive = false;
        mbIsActiveIn = false;
        mbIsActiveOut = false;
        mbIsDeleted = false;
        mnFkCompanyId = 0;
        mnFkEntityCategoryId = 0;
        mnFkEntityTypeId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

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
        String query = "";
        String[] fields = null;
        ResultSet resultSet = null;
        Vector<SFormComponentItem> vector = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.cfgu_cob_ent " +
                    "WHERE id_cob = " + key[0] + " AND id_ent = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCompanyBranchId = resultSet.getInt("id_cob");
                mnPkEntityId = resultSet.getInt("id_ent");
                msEntity = resultSet.getString("ent");
                msCode = resultSet.getString("code");
                mbIsDefault = resultSet.getBoolean("b_def");
                mbIsActive = resultSet.getBoolean("b_act");
                mbIsActiveIn = resultSet.getBoolean("b_act_in");
                mbIsActiveOut = resultSet.getBoolean("b_act_out");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCompanyId = resultSet.getInt("fid_co");
                mnFkEntityCategoryId = resultSet.getInt("fid_ct_ent");
                mnFkEntityTypeId = resultSet.getInt("fid_tp_ent");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read aswell document number series applying:

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
                                fields = new String[] { "fid_ct_" + field, "fid_cl_" + field, "fid_tp_" + field };
                                vector = mvDnsForDps;
                                break;
                            case 2:
                                table = "diog";
                                field = "iog";
                                fields = new String[] { "fid_ct_" + field, "fid_cl_" + field };
                                vector = mvDnsForDiog;
                                break;
                            default:
                        }

                        query = "";
                        for (int j = 0; j < fields.length; j++) {
                            query += "s." + fields[j] + ", ";
                        }

                        sql = "SELECT s.dns, " + query + "cs.num_start, cs.num_end_n " +
                                "FROM trn_dnc_" + table + "_cob AS d INNER JOIN trn_dnc_" + table + " AS c ON " +
                                "d.id_cob = " + key[0] + " AND d.id_dnc = c.id_dnc AND " +
                                "d.b_del = FALSE AND c.b_del = FALSE " +
                                "INNER JOIN trn_dnc_" + table + "_dns AS cs ON " +
                                "c.id_dnc = cs.id_dnc AND cs.b_del = FALSE " +
                                "INNER JOIN trn_dns_" + table + " AS s ON " +
                                "cs.id_dns = s.id_dns AND s.b_del = FALSE " +
                                "UNION " +
                                "SELECT s.dns, " + query + "cs.num_start, cs.num_end_n " +
                                "FROM trn_dnc_" + table + "_cob_ent AS d INNER JOIN trn_dnc_" + table + " AS c ON " +
                                "d.id_cob = " + key[0] + " AND d.id_ent = " + key[1] + " AND d.id_dnc = c.id_dnc AND " +
                                "d.b_del = FALSE AND c.b_del = FALSE " +
                                "INNER JOIN trn_dnc_" + table + "_dns AS cs ON " +
                                "c.id_dnc = cs.id_dnc AND cs.b_del = FALSE " +
                                "INNER JOIN trn_dns_" + table + " AS s ON " +
                                "cs.id_dns = s.id_dns AND s.b_del = FALSE " +
                                "ORDER BY dns, num_start ";
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

                            item = new SFormComponentItem(itemPk, resultSet.getString("dns"));
                            item.setComplement(new int[] { numStart, numEnd_n });
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL erp.cfgu_cob_ent_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCompanyBranchId);
            callableStatement.setInt(nParam++, mnPkEntityId);
            callableStatement.setString(nParam++, msEntity);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setBoolean(nParam++, mbIsDefault);
            callableStatement.setBoolean(nParam++, mbIsActive);
            callableStatement.setBoolean(nParam++, mbIsActiveIn);
            callableStatement.setBoolean(nParam++, mbIsActiveOut);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCompanyId);
            callableStatement.setInt(nParam++, mnFkEntityCategoryId);
            callableStatement.setInt(nParam++, mnFkEntityTypeId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkEntityId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
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

    public java.util.Vector<erp.lib.form.SFormComponentItem> getDnsForDps(int[] pkDpsType) {
        Vector<SFormComponentItem> vector = new Vector<SFormComponentItem>();

        for (int i = 0; i < mvDnsForDps.size(); i++) {
            if (SLibUtilities.compareKeys((Object) pkDpsType, mvDnsForDps.get(i).getPrimaryKey())) {
                vector.add(mvDnsForDps.get(i));
            }
        }

        return vector;
    }

    public java.util.Vector<erp.lib.form.SFormComponentItem> getDnsForDiog(int[] pkDiogClass) {
        Vector<SFormComponentItem> vector = new Vector<SFormComponentItem>();

        for (int i = 0; i < mvDnsForDiog.size(); i++) {
            if (SLibUtilities.compareKeys((Object) pkDiogClass, mvDnsForDiog.get(i).getPrimaryKey())) {
                vector.add(mvDnsForDiog.get(i));
            }
        }

        return vector;
    }
}
