/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mhrs.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Sergio Flores
 */
public class SDataFormerPayrollEmp extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPayrollId;
    protected int mnPkEmployeeId;
    protected java.lang.String msEmployee;
    protected java.lang.String msDepartment;
    protected java.lang.String msDepartmentKey;
    protected java.lang.String msEmployeeCategory;
    protected java.lang.String msEmployeeType;
    protected java.lang.String msSalaryType;
    protected double mdDebit;
    protected double mdCredit;
    protected double mdSalary;
    protected double mdDaysNotWorked;
    protected double mdDaysWorked;
    protected double mdDaysPayed;
    protected java.lang.String msNumberSeries;
    protected int mnNumber;
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerId_n;
    protected int mnFkPaymentSystemTypeId;
    protected int mnFkYearId;
    protected int mnFkPeriodId;
    protected int mnFkBookkeepingCenterId;
    protected java.lang.String msFkRecordTypeId;
    protected int mnFkNumberId;

    private void computeNumber(java.sql.Connection connection) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;

        mnNumber = 0;

        sql = "SELECT COALESCE(MAX(num), 0) + 1 AS f_num FROM hrs_sie_pay_emp WHERE num_ser = '" + msNumberSeries + "' AND b_del = 0 ";

        resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnNumber = resultSet.getInt("f_num");
        }
    }

    public SDataFormerPayrollEmp() {
        super(SDataConstants.HRS_SIE_PAY_EMP);
        reset();
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setEmployee(java.lang.String s) { msEmployee = s; }
    public void setDepartment(java.lang.String s) { msDepartment = s; }
    public void setDepartmentKey(java.lang.String s) { msDepartmentKey = s; }
    public void setEmployeeCategory(java.lang.String s) { msEmployeeCategory = s; }
    public void setEmployeeType(java.lang.String s) { msEmployeeType = s; }
    public void setSalaryType(java.lang.String s) { msSalaryType = s; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setSalary(double d) { mdSalary = d; }
    public void setDaysNotWorked(double d) { mdDaysNotWorked = d; }
    public void setDaysWorked(double d) { mdDaysWorked = d; }
    public void setDaysPayed(double d) { mdDaysPayed = d; }
    public void setNumberSeries(java.lang.String s) { msNumberSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkPaymentSystemTypeId(int n) { mnFkPaymentSystemTypeId = n; }
    public void setFkYearId(int n) { mnFkYearId = n; }
    public void setFkPeriodId(int n) { mnFkPeriodId = n; }
    public void setFkBookkeepingCenterId(int n) { mnFkBookkeepingCenterId = n; }
    public void setFkRecordTypeId(java.lang.String s) { msFkRecordTypeId = s; }
    public void setFkNumberId(int n) { mnFkNumberId = n; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public java.lang.String getEmployee() { return msEmployee; }
    public java.lang.String getDepartment() { return msDepartment; }
    public java.lang.String getDepartmentKey() { return msDepartmentKey; }
    public java.lang.String getEmployeeCategory() { return msEmployeeCategory; }
    public java.lang.String getEmployeeType() { return msEmployeeType; }
    public java.lang.String getSalaryType() { return msSalaryType; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public double getSalary() { return mdSalary; }
    public double getDaysNotWorked() { return mdDaysNotWorked; }
    public double getDaysWorked() { return mdDaysWorked; }
    public double getDaysPayed() { return mdDaysPayed; }
    public java.lang.String getNumberSeries() { return msNumberSeries; }
    public int getNumber() { return mnNumber; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkPaymentSystemTypeId() { return mnFkPaymentSystemTypeId; }
    public int getFkYearId() { return mnFkYearId; }
    public int getFkPeriodId() { return mnFkPeriodId; }
    public int getFkBookkeepingCenterId() { return mnFkBookkeepingCenterId; }
    public java.lang.String getFkRecordTypeId() { return msFkRecordTypeId; }
    public int getFkNumberId() { return mnFkNumberId; }

    public java.lang.String getFormerPayrollEmpNumber() {
        return (msNumberSeries.isEmpty() ? "" : msNumberSeries + "-") + mnNumber;
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPayrollId = ((int[]) pk)[0];
        mnPkEmployeeId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPayrollId = 0;
        mnPkEmployeeId = 0;
        msEmployee = "";
        msDepartment = "";
        msDepartmentKey = "";
        msEmployeeCategory = "";
        msEmployeeType = "";
        msSalaryType = "";
        mdDebit = 0;
        mdCredit = 0;
        mdSalary = 0;
        mdDaysNotWorked = 0;
        mdDaysWorked = 0;
        mdDaysPayed = 0;
        msNumberSeries = "";
        mnNumber = 0;
        mbIsDeleted = false;
        mnFkBizPartnerId_n = 0;
        mnFkPaymentSystemTypeId = 0;
        mnFkYearId = 0;
        mnFkPeriodId = 0;
        mnFkBookkeepingCenterId = 0;
        msFkRecordTypeId = "";
        mnFkNumberId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM hrs_sie_pay_emp WHERE id_pay = " + key[0] + " AND id_emp = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkPayrollId = resultSet.getInt("id_pay");
                mnPkEmployeeId = resultSet.getInt("id_emp");
                msEmployee = resultSet.getString("emp");
                msDepartment = resultSet.getString("dep");
                msDepartmentKey = resultSet.getString("dep_key");
                msEmployeeCategory = resultSet.getString("emp_cat");
                msEmployeeType = resultSet.getString("emp_tp");
                msSalaryType = resultSet.getString("sal_tp");
                mdDebit = resultSet.getDouble("debit");
                mdCredit = resultSet.getDouble("credit");
                mdSalary = resultSet.getDouble("sal");
                mdDaysNotWorked = resultSet.getDouble("day_not_wkd");
                mdDaysWorked = resultSet.getDouble("day_wkd");
                mdDaysPayed = resultSet.getDouble("day_pay");
                msNumberSeries = resultSet.getString("num_ser");
                mnNumber = resultSet.getInt("num");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkBizPartnerId_n = resultSet.getInt("fid_bpr_n");
                mnFkPaymentSystemTypeId = resultSet.getInt("fid_tp_pay_sys");
                mnFkYearId = resultSet.getInt("fid_year");
                mnFkPeriodId = resultSet.getInt("fid_per");
                mnFkBookkeepingCenterId = resultSet.getInt("fid_bkc");
                msFkRecordTypeId = resultSet.getString("fid_tp_rec");
                mnFkNumberId = resultSet.getInt("fid_num");

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
            computeNumber(connection);

            callableStatement = connection.prepareCall(
                    "{ CALL hrs_sie_pay_emp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPayrollId);
            callableStatement.setInt(nParam++, mnPkEmployeeId);
            callableStatement.setString(nParam++, msEmployee);
            callableStatement.setString(nParam++, msDepartment);
            callableStatement.setString(nParam++, msDepartmentKey);
            callableStatement.setString(nParam++, msEmployeeCategory);
            callableStatement.setString(nParam++, msEmployeeType);
            callableStatement.setString(nParam++, msSalaryType);
            callableStatement.setDouble(nParam++, mdDebit);
            callableStatement.setDouble(nParam++, mdCredit);
            callableStatement.setDouble(nParam++, mdSalary);
            callableStatement.setDouble(nParam++, mdDaysNotWorked);
            callableStatement.setDouble(nParam++, mdDaysWorked);
            callableStatement.setDouble(nParam++, mdDaysPayed);
            callableStatement.setString(nParam++, msNumberSeries);
            callableStatement.setInt(nParam++, mnNumber);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerId_n);
            callableStatement.setInt(nParam++, mnFkPaymentSystemTypeId);
            callableStatement.setInt(nParam++, mnFkYearId);
            callableStatement.setInt(nParam++, mnFkPeriodId);
            callableStatement.setInt(nParam++, mnFkBookkeepingCenterId);
            callableStatement.setString(nParam++, msFkRecordTypeId);
            callableStatement.setInt(nParam++, mnFkNumberId);
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
}
