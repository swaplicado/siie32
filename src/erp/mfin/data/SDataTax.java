/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataTax extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    public static final String ERR_MSG_VAT_TYPE = "No se ha configurado el tipo de IVA del impuesto ";

    protected int mnPkTaxBasicId;
    protected int mnPkTaxId;
    protected java.lang.String msTax;
    protected double mdPercentage;
    protected double mdValueUnitary;
    protected double mdValue;
    protected java.lang.String msVatType;
    protected boolean mbIsDeleted;
    protected int mnFkTaxTypeId;
    protected int mnFkTaxCalculationTypeId;
    protected int mnFkTaxApplicationTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsTaxType;
    protected java.lang.String msDbmsTaxCalculationType;
    protected java.lang.String msDbmsTaxApplicationType;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataTax() {
        super(SDataConstants.FINU_TAX);
        reset();
    }

    public void setPkTaxBasicId(int n) { mnPkTaxBasicId = n; }
    public void setPkTaxId(int n) { mnPkTaxId = n; }
    public void setTax(java.lang.String s) { msTax = s; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setValueUnitary(double d) { mdValueUnitary = d; }
    public void setValue(double d) { mdValue = d; }
    public void setVatType(java.lang.String s) { msVatType = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkTaxTypeId(int n) { mnFkTaxTypeId = n; }
    public void setFkTaxCalculationTypeId(int n) { mnFkTaxCalculationTypeId = n; }
    public void setFkTaxApplicationTypeId(int n) { mnFkTaxApplicationTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkTaxBasicId() { return mnPkTaxBasicId; }
    public int getPkTaxId() { return mnPkTaxId; }
    public java.lang.String getTax() { return msTax; }
    public double getPercentage() { return mdPercentage; }
    public double getValueUnitary() { return mdValueUnitary; }
    public double getValue() { return mdValue; }
    public java.lang.String getVatType() { return msVatType; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkTaxTypeId() { return mnFkTaxTypeId; }
    public int getFkTaxCalculationTypeId() { return mnFkTaxCalculationTypeId; }
    public int getFkTaxApplicationTypeId() { return mnFkTaxApplicationTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsTaxType(java.lang.String s) { msDbmsTaxType = s; }
    public void setDbmsTaxCalculationType(java.lang.String s) { msDbmsTaxCalculationType = s; }
    public void setDbmsTaxApplicationType(java.lang.String s) { msDbmsTaxApplicationType = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsTaxType() { return msDbmsTaxType; }
    public java.lang.String getDbmsTaxCalculationType() { return msDbmsTaxCalculationType; }
    public java.lang.String getDbmsTaxApplicationType() { return msDbmsTaxApplicationType; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkTaxBasicId = ((int[]) pk)[0];
        mnPkTaxId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkTaxBasicId, mnPkTaxId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkTaxBasicId = 0;
        mnPkTaxId = 0;
        msTax = "";
        mdPercentage = 0;
        mdValueUnitary = 0;
        mdValue = 0;
        msVatType = "";
        mbIsDeleted = false;
        mnFkTaxTypeId = 0;
        mnFkTaxCalculationTypeId = 0;
        mnFkTaxApplicationTypeId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsTaxType = "";
        msDbmsTaxCalculationType = "";
        msDbmsTaxApplicationType = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT tax.*, tp_tax.tp_tax, tp_tax_cal.tp_tax_cal, tp_tax_app.tp_tax_app, un.usr, ue.usr, ud.usr " +
                    "FROM erp.finu_tax AS tax " +
                    "INNER JOIN erp.fins_tp_tax AS tp_tax ON " +
                    "tax.fid_tp_tax = tp_tax.id_tp_tax " +
                    "INNER JOIN erp.fins_tp_tax_cal AS tp_tax_cal ON " +
                    "tax.fid_tp_tax_cal = tp_tax_cal.id_tp_tax_cal " +
                    "INNER JOIN erp.fins_tp_tax_app AS tp_tax_app ON " +
                    "tax.fid_tp_tax_app = tp_tax_app.id_tp_tax_app " +
                    "INNER JOIN erp.usru_usr AS un ON " +
                    "tax.fid_usr_new=un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON " +
                    "tax.fid_usr_edit=ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON " +
                    "tax.fid_usr_del=ud.id_usr " +
                    "WHERE id_tax_bas = " + key[0]  + " AND id_tax = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkTaxBasicId = resultSet.getInt("tax.id_tax_bas");
                mnPkTaxId = resultSet.getInt("tax.id_tax");
                msTax = resultSet.getString("tax.tax");
                mdPercentage = resultSet.getDouble("tax.per");
                mdValueUnitary = resultSet.getDouble("tax.val_u");
                mdValue = resultSet.getDouble("tax.val");
                msVatType = resultSet.getString("tax.vat_type");
                mbIsDeleted = resultSet.getBoolean("tax.b_del");
                mnFkTaxTypeId = resultSet.getInt("tax.fid_tp_tax");
                mnFkTaxCalculationTypeId = resultSet.getInt("tax.fid_tp_tax_cal");
                mnFkTaxApplicationTypeId = resultSet.getInt("tax.fid_tp_tax_app");
                mnFkUserNewId = resultSet.getInt("tax.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("tax.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("tax.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("tax.ts_new");
                mtUserEditTs = resultSet.getTimestamp("tax.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("tax.ts_del");

                msDbmsTaxType = resultSet.getString("tp_tax.tp_tax");
                msDbmsTaxCalculationType = resultSet.getString("tp_tax_cal.tp_tax_cal");
                msDbmsTaxApplicationType = resultSet.getString("tp_tax_app.tp_tax_app");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

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
                    "{ CALL erp.finu_tax_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkTaxBasicId);
            callableStatement.setInt(nParam++, mnPkTaxId);
            callableStatement.setString(nParam++, msTax);
            callableStatement.setDouble(nParam++, mdPercentage);
            callableStatement.setDouble(nParam++, mdValueUnitary);
            callableStatement.setDouble(nParam++, mdValue);
            callableStatement.setString(nParam++, msVatType);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkTaxTypeId);
            callableStatement.setInt(nParam++, mnFkTaxCalculationTypeId);
            callableStatement.setInt(nParam++, mnFkTaxApplicationTypeId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkTaxId = callableStatement.getInt(nParam - 3);
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
}
