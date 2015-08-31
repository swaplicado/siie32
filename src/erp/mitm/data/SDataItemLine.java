/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author TRON
 */
public class SDataItemLine extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemLineId;
    protected java.lang.String msItemLine;
    protected java.lang.String msItemLineShort;
    protected java.lang.String msCode;
    protected boolean mbIsFixedBrand;
    protected boolean mbIsFixedManufacturer;
    protected boolean mbIsFixedElement;
    protected boolean mbIsDeleted;
    protected int mnFkItemGenericId;
    protected int mnFkBrandId;
    protected int mnFkManufacturerId;
    protected int mnFkElementId;
    protected int mnFkVarietyType01Id;
    protected int mnFkVarietyType02Id;
    protected int mnFkVarietyType03Id;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsItemGeneric;
    protected java.lang.String msDbmsBrand;
    protected java.lang.String msDbmsManufacturer;
    protected java.lang.String msDbmsElement;
    protected java.lang.String msDbmsVarietyType01;
    protected java.lang.String msDbmsVarietyType02;
    protected java.lang.String msDbmsVarietyType03;

    public SDataItemLine() {
        super(SDataConstants.ITMU_LINE);
        reset();
    }

    public void setPkItemLineId(int n) { mnPkItemLineId = n; }
    public void setItemLine(java.lang.String s) { msItemLine = s; }
    public void setItemLineShort(java.lang.String s) { msItemLineShort = s; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setIsFixedBrand(boolean b) { mbIsFixedBrand = b; }
    public void setIsFixedManufacturer(boolean b) { mbIsFixedManufacturer = b; }
    public void setIsFixedElement(boolean b) { mbIsFixedElement = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemGenericId(int n) { mnFkItemGenericId = n; }
    public void setFkBrandId(int n) { mnFkBrandId = n; }
    public void setFkManufacturerId(int n) { mnFkManufacturerId = n; }
    public void setFkElementId(int n) { mnFkElementId = n; }
    public void setFkVarietyType01Id(int n) { mnFkVarietyType01Id = n; }
    public void setFkVarietyType02Id(int n) { mnFkVarietyType02Id = n; }
    public void setFkVarietyType03Id(int n) { mnFkVarietyType03Id = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemLineId() { return mnPkItemLineId; }
    public java.lang.String getItemLine() { return msItemLine; }
    public java.lang.String getItemLineShort() { return msItemLineShort; }
    public java.lang.String getCode() { return msCode; }
    public boolean getIsFixedBrand() { return mbIsFixedBrand; }
    public boolean getIsFixedManufacturer() { return mbIsFixedManufacturer; }
    public boolean getIsFixedElement() { return mbIsFixedElement; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemGenericId() { return mnFkItemGenericId; }
    public int getFkBrandId() { return mnFkBrandId; }
    public int getFkManufacturerId() { return mnFkManufacturerId; }
    public int getFkElementId() { return mnFkElementId; }
    public int getFkVarietyType01Id() { return mnFkVarietyType01Id; }
    public int getFkVarietyType02Id() { return mnFkVarietyType02Id; }
    public int getFkVarietyType03Id() { return mnFkVarietyType03Id; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsItemGeneric(java.lang.String s) { msDbmsItemGeneric = s; }
    public void setDbmsBrand(java.lang.String s) { msDbmsBrand = s; }
    public void setDbmsManufacturer(java.lang.String s) { msDbmsManufacturer = s; }
    public void setDbmsElement(java.lang.String s) { msDbmsElement = s; }
    public void setDbmsVarietyType01(java.lang.String s) { msDbmsVarietyType01 = s; }
    public void setDbmsVarietyType02(java.lang.String s) { msDbmsVarietyType02 = s; }
    public void setDbmsVarietyType03(java.lang.String s) { msDbmsVarietyType03 = s; }

    public java.lang.String getDbmsItemGeneric() { return msDbmsItemGeneric; }
    public java.lang.String getDbmsBrand() { return msDbmsBrand; }
    public java.lang.String getDbmsManufacturer() { return msDbmsManufacturer; }
    public java.lang.String getDbmsElement() { return msDbmsElement; }
    public java.lang.String getDbmsVarietyType01() { return msDbmsVarietyType01; }
    public java.lang.String getDbmsVarietyType02() { return msDbmsVarietyType02; }
    public java.lang.String getDbmsVarietyType03() { return msDbmsVarietyType03; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemLineId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemLineId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemLineId = 0;
        msItemLine = "";
        msItemLineShort = "";
        msCode = "";
        mbIsFixedBrand = false;
        mbIsFixedManufacturer = false;
        mbIsFixedElement = false;
        mbIsDeleted = false;
        mnFkItemGenericId = 0;
        mnFkBrandId = 0;
        mnFkManufacturerId = 0;
        mnFkElementId = 0;
        mnFkVarietyType01Id = 0;
        mnFkVarietyType02Id = 0;
        mnFkVarietyType03Id = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT l.*, ig.igen, b.brd, m.mfr, e.emt, t1.tp_var, t2.tp_var, t3.tp_var " +
                    "FROM erp.itmu_line AS l  " +
                    "INNER JOIN erp.itmu_igen AS ig ON " +
                    "l.fid_igen = ig.id_igen " +
                    "INNER JOIN erp.itmu_brd AS b ON " +
                    "l.fid_brd = b.id_brd " +
                    "INNER JOIN erp.itmu_mfr AS m ON " +
                    "l.fid_mfr = m.id_mfr " +
                    "INNER JOIN erp.itmu_emt AS e ON " +
                    "l.fid_emt = e.id_emt " +
                    "INNER JOIN erp.itmu_tp_var AS t1 ON " +
                    "l.fid_tp_var_01 = t1.id_tp_var " +
                    "INNER JOIN erp.itmu_tp_var AS t2 ON " +
                    "l.fid_tp_var_02 = t2.id_tp_var " +
                    "INNER JOIN erp.itmu_tp_var AS t3 ON " +
                    "l.fid_tp_var_03 = t3.id_tp_var " +
                    "WHERE l.id_line = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemLineId = resultSet.getInt("l.id_line");
                msItemLine = resultSet.getString("l.line");
                msItemLineShort = resultSet.getString("l.line_short");
                msCode = resultSet.getString("l.code");
                mbIsFixedBrand = resultSet.getBoolean("l.b_fix_brd");
                mbIsFixedManufacturer = resultSet.getBoolean("l.b_fix_mfr");
                mbIsFixedElement = resultSet.getBoolean("l.b_fix_emt");
                mbIsDeleted = resultSet.getBoolean("l.b_del");
                mnFkItemGenericId = resultSet.getInt("l.fid_igen");
                mnFkBrandId = resultSet.getInt("l.fid_brd");
                mnFkManufacturerId = resultSet.getInt("l.fid_mfr");
                mnFkElementId = resultSet.getInt("l.fid_emt");
                mnFkVarietyType01Id = resultSet.getInt("l.fid_tp_var_01");
                mnFkVarietyType02Id = resultSet.getInt("l.fid_tp_var_02");
                mnFkVarietyType03Id = resultSet.getInt("l.fid_tp_var_03");
                mnFkUserNewId = resultSet.getInt("l.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("l.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("l.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("l.ts_new");
                mtUserEditTs = resultSet.getTimestamp("l.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("l.ts_del");

                msDbmsItemGeneric = resultSet.getString("ig.igen");
                msDbmsBrand = resultSet.getString("b.brd");
                msDbmsManufacturer = resultSet.getString("m.mfr");
                msDbmsElement = resultSet.getString("e.emt");
                msDbmsVarietyType01 = resultSet.getString("t1.tp_var");
                msDbmsVarietyType02 = resultSet.getString("t2.tp_var");
                msDbmsVarietyType03 = resultSet.getString("t3.tp_var");

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
            callableStatement = connection.prepareCall(
                    "{ CALL erp.itmu_line_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemLineId);
            callableStatement.setString(nParam++, msItemLine);
            callableStatement.setString(nParam++, msItemLineShort);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setBoolean(nParam++, mbIsFixedBrand);
            callableStatement.setBoolean(nParam++, mbIsFixedManufacturer);
            callableStatement.setBoolean(nParam++, mbIsFixedElement);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemGenericId);
            callableStatement.setInt(nParam++, mnFkBrandId);
            callableStatement.setInt(nParam++, mnFkManufacturerId);
            callableStatement.setInt(nParam++, mnFkElementId);
            callableStatement.setInt(nParam++, mnFkVarietyType01Id);
            callableStatement.setInt(nParam++, mnFkVarietyType02Id);
            callableStatement.setInt(nParam++, mnFkVarietyType03Id);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkItemLineId = callableStatement.getInt(nParam - 3);
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
}
