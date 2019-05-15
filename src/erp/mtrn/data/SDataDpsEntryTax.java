/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Alfonso Flores, Sergio Flores, Juan Barajas
 */
public class SDataDpsEntryTax extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnPkTaxBasicId;
    protected int mnPkTaxId;
    protected double mdPercentage;
    protected double mdValueUnitary;
    protected double mdValue;
    protected double mdTax;
    protected double mdTaxCy;
    protected int mnFkTaxTypeId;
    protected int mnFkTaxCalculationTypeId;
    protected int mnFkTaxApplicationTypeId;

    protected java.lang.String msDbmsTax;
    protected java.lang.String msDbmsTaxType;
    protected java.lang.String msDbmsTaxCalculationType;
    protected java.lang.String msDbmsTaxApplicationType;
    protected java.lang.String msDbmsCfdTax;
    protected int mnDbmsCfdTaxId;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsEntryTax() {
        super(SDataConstants.TRN_DPS_ETY_TAX);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkTaxBasicId(int n) { mnPkTaxBasicId = n; }
    public void setPkTaxId(int n) { mnPkTaxId = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setValueUnitary(double d) { mdValueUnitary = d; }
    public void setValue(double d) { mdValue = d; }
    public void setTax(double d) { mdTax = d; }
    public void setTaxCy(double d) { mdTaxCy = d; }
    public void setFkTaxTypeId(int n) { mnFkTaxTypeId = n; }
    public void setFkTaxCalculationTypeId(int n) { mnFkTaxCalculationTypeId = n; }
    public void setFkTaxApplicationTypeId(int n) { mnFkTaxApplicationTypeId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkTaxBasicId() { return mnPkTaxBasicId; }
    public int getPkTaxId() { return mnPkTaxId; }
    public double getPercentage() { return mdPercentage; }
    public double getValueUnitary() { return mdValueUnitary; }
    public double getValue() { return mdValue; }
    public double getTax() { return mdTax; }
    public double getTaxCy() { return mdTaxCy; }
    public int getFkTaxTypeId() { return mnFkTaxTypeId; }
    public int getFkTaxCalculationTypeId() { return mnFkTaxCalculationTypeId; }
    public int getFkTaxApplicationTypeId() { return mnFkTaxApplicationTypeId; }

    public void setDbmsTax(java.lang.String s) { msDbmsTax = s; }
    public void setDbmsTaxType(java.lang.String s) { msDbmsTaxType = s; }
    public void setDbmsTaxCalculationType(java.lang.String s) { msDbmsTaxCalculationType = s; }
    public void setDbmsTaxApplicationType(java.lang.String s) { msDbmsTaxApplicationType = s; }
    public void setDbmsCfdTax(java.lang.String s) { msDbmsCfdTax = s; }
    public void setDbmsCfdTaxId(int n) { mnDbmsCfdTaxId = n; }

    public java.lang.String getDbmsTax() { return msDbmsTax; }
    public java.lang.String getDbmsTaxType() { return msDbmsTaxType; }
    public java.lang.String getDbmsTaxCalculationType() { return msDbmsTaxCalculationType; }
    public java.lang.String getDbmsTaxApplicationType() { return msDbmsTaxApplicationType; }
    public java.lang.String getDbmsCfdTax() { return msDbmsCfdTax; }
    public int getDbmsCfdTaxId() { return mnDbmsCfdTaxId; }
    
    public int[] getKeyDpsEntry() { return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId }; }
    public int[] getKeyTax() { return new int[] { mnPkTaxBasicId, mnPkTaxId }; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
        mnPkTaxBasicId = ((int[]) pk)[3];
        mnPkTaxId = ((int[]) pk)[4];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, mnPkTaxBasicId, mnPkTaxId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnPkTaxBasicId = 0;
        mnPkTaxId = 0;
        mdPercentage = 0;
        mdValueUnitary = 0;
        mdValue = 0;
        mdTax = 0;
        mdTaxCy = 0;
        mnFkTaxTypeId = 0;
        mnFkTaxCalculationTypeId = 0;
        mnFkTaxApplicationTypeId = 0;

        msDbmsTax = "";
        msDbmsTaxType = "";
        msDbmsTaxCalculationType = "";
        msDbmsTaxApplicationType = "";
        msDbmsCfdTax = "";
        mnDbmsCfdTaxId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT et.*, t.tax, tt.tp_tax, tc.tp_tax_cal, ta.tp_tax_app, ct.id_cfd_tax, ct.code " +
                    "FROM trn_dps_ety_tax AS et " +
                    "INNER JOIN erp.finu_tax AS t ON " +
                    "et.id_tax_bas = t.id_tax_bas AND et.id_tax = t.id_tax " +
                    "INNER JOIN erp.fins_tp_tax AS tt ON " +
                    "et.fid_tp_tax = tt.id_tp_tax " +
                    "INNER JOIN erp.fins_tp_tax_cal AS tc ON " +
                    "et.fid_tp_tax_cal = tc.id_tp_tax_cal " +
                    "INNER JOIN erp.fins_tp_tax_app AS ta ON " +
                    "et.fid_tp_tax_app = ta.id_tp_tax_app " +
                    "INNER JOIN erp.finu_tax_bas AS tb ON " +
                    "t.id_tax_bas = tb.id_tax_bas " +
                    "INNER JOIN erp.fins_cfd_tax AS ct ON " +
                    "tb.fid_cfd_tax = ct.id_cfd_tax " +
                    "WHERE et.id_year = " + key[0] + " AND et.id_doc = " + key[1] + " AND " +
                    "et.id_ety = " + key[2] + " AND et.id_tax_bas = " + key[3] + " AND et.id_tax = " + key[4] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("et.id_year");
                mnPkDocId = resultSet.getInt("et.id_doc");
                mnPkEntryId = resultSet.getInt("et.id_ety");
                mnPkTaxBasicId = resultSet.getInt("et.id_tax_bas");
                mnPkTaxId = resultSet.getInt("et.id_tax");
                mdPercentage = resultSet.getDouble("et.per");
                mdValueUnitary = resultSet.getDouble("et.val_u");
                mdValue = resultSet.getDouble("et.val");
                mdTax = resultSet.getDouble("et.tax");
                mdTaxCy = resultSet.getDouble("et.tax_cur");
                mnFkTaxTypeId = resultSet.getInt("et.fid_tp_tax");
                mnFkTaxCalculationTypeId = resultSet.getInt("et.fid_tp_tax_cal");
                mnFkTaxApplicationTypeId = resultSet.getInt("et.fid_tp_tax_app");

                msDbmsTax = resultSet.getString("t.tax");
                msDbmsTaxType = resultSet.getString("tt.tp_tax");
                msDbmsTaxCalculationType = resultSet.getString("tc.tp_tax_cal");
                msDbmsTaxApplicationType = resultSet.getString("ta.tp_tax_app");
                msDbmsCfdTax = resultSet.getString("ct.code");
                mnDbmsCfdTaxId = resultSet.getInt("ct.id_cfd_tax");

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
                    "{ CALL trn_dps_ety_tax_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setInt(nParam++, mnPkTaxBasicId);
            callableStatement.setInt(nParam++, mnPkTaxId);
            callableStatement.setDouble(nParam++, mdPercentage);
            callableStatement.setDouble(nParam++, mdValueUnitary);
            callableStatement.setDouble(nParam++, mdValue);
            callableStatement.setDouble(nParam++, mdTax);
            callableStatement.setDouble(nParam++, mdTaxCy);
            callableStatement.setInt(nParam++, mnFkTaxTypeId);
            callableStatement.setInt(nParam++, mnFkTaxCalculationTypeId);
            callableStatement.setInt(nParam++, mnFkTaxApplicationTypeId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);

            callableStatement.execute();

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
        return null;
    }

    @Override
    public erp.mtrn.data.SDataDpsEntryTax clone() {
        SDataDpsEntryTax clone = new SDataDpsEntryTax();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkYearId(mnPkYearId);
        clone.setPkDocId(mnPkDocId);
        clone.setPkEntryId(mnPkEntryId);
        clone.setPkTaxBasicId(mnPkTaxBasicId);
        clone.setPkTaxId(mnPkTaxId);
        clone.setPercentage(mdPercentage);
        clone.setValueUnitary(mdValueUnitary);
        clone.setValue(mdValue);
        clone.setTax(mdTax);
        clone.setTaxCy(mdTaxCy);
        clone.setFkTaxTypeId(mnFkTaxTypeId);
        clone.setFkTaxCalculationTypeId(mnFkTaxCalculationTypeId);
        clone.setFkTaxApplicationTypeId(mnFkTaxApplicationTypeId);

        clone.setDbmsTax(msDbmsTax);
        clone.setDbmsTaxType(msDbmsTaxType);
        clone.setDbmsTaxCalculationType(msDbmsTaxCalculationType);
        clone.setDbmsTaxApplicationType(msDbmsTaxApplicationType);
        clone.setDbmsCfdTax(msDbmsCfdTax);
        clone.setDbmsCfdTaxId(mnDbmsCfdTaxId);

        return clone;
    }
}
