/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mhrs.data;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataCfd;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Juan Barajas
 */
public class SDataPayrollReceiptIssue extends SDataRegistry implements Serializable {
    
    protected int mnPkPayrollId;
    protected int mnPkEmployeeId;
    protected int mnPkIssueId;
    protected String msNumberSeries;
    protected int mnNumber;
    protected Date mtDateIssue;
    protected Date mtDatePayment;
    protected String msBankAccount;
    protected double mdEarnings_r;
    protected double mdDeductions_r;
    protected double mdPayment_r;
    protected boolean mbDeleted;
    protected int mnFkReceiptStatusId;
    protected int mnFkBankId_n;
    protected int mnFkPaymentSystemTypeId;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    
    protected erp.mtrn.data.SDataCfd moDbmsDataCfd;

    public boolean isStamped() {
        if (moDbmsDataCfd != null) {
            return moDbmsDataCfd.isStamped() && moDbmsDataCfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED;
        }
        return false;
    }
    
    public boolean isAnnul() {
        if (moDbmsDataCfd != null) {
            return moDbmsDataCfd.isStamped() && moDbmsDataCfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED;
        }
        return false;
    }
    
    public java.lang.String getIssueNumber() {
        return (msNumberSeries.isEmpty() ? "" : msNumberSeries + "-") + mnNumber;
    }
    
    @Override
    public int annul(java.sql.Connection connection) {
        String sSql = "";
        Statement oStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            oStatement = connection.createStatement();

            sSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " SET " +
                    "fk_st_rcp = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_upd = NOW() " +
                    "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " AND id_iss = " + mnPkIssueId + " ";

            oStatement.execute(sSql);

            if (moDbmsDataCfd != null) {
                moDbmsDataCfd.annul(connection);
            }
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_OK;
        
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }
    
    public SDataPayrollReceiptIssue() {
        super(SModConsts.HRS_PAY_RCP_ISS);
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkIssueId(int n) { mnPkIssueId = n; }
    public void setNumberSeries(String s) { msNumberSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDateIssue(Date t) { mtDateIssue = t; }
    public void setDatePayment(Date t) { mtDatePayment = t; }
    public void setBankAccount(String s) { msBankAccount = s; }
    public void setEarnings_r(double d) { mdEarnings_r = d; }
    public void setDeductions_r(double d) { mdDeductions_r = d; }
    public void setPayment_r(double d) { mdPayment_r = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkReceiptStatusId(int n) { mnFkReceiptStatusId = n; }
    public void setFkBankId_n(int n) { mnFkBankId_n = n; }
    public void setFkPaymentSystemTypeId(int n) { mnFkPaymentSystemTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setDbmsDataCfd(erp.mtrn.data.SDataCfd o) { moDbmsDataCfd = o; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkIssueId() { return mnPkIssueId; }
    public String getNumberSeries() { return msNumberSeries; }
    public int getNumber() { return mnNumber; }
    public Date getDateIssue() { return mtDateIssue; }
    public Date getDatePayment() { return mtDatePayment; }
    public String getBankAccount() { return msBankAccount; }
    public double getEarnings_r() { return mdEarnings_r; }
    public double getDeductions_r() { return mdDeductions_r; }
    public double getPayment_r() { return mdPayment_r; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkReceiptStatusId() { return mnFkReceiptStatusId; }
    public int getFkBankId_n() { return mnFkBankId_n; }
    public int getFkPaymentSystemTypeId() { return mnFkPaymentSystemTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public erp.mtrn.data.SDataCfd getDbmsDataCfd() { return moDbmsDataCfd; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkPayrollId = ((int[]) pk)[0];
        mnPkEmployeeId = ((int[]) pk)[1];
        mnPkIssueId = ((int[]) pk)[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId, mnPkIssueId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPayrollId = 0;
        mnPkEmployeeId = 0;
        mnPkIssueId = 0;
        msNumberSeries = "";
        mnNumber = 0;
        mtDateIssue = null;
        mtDatePayment = null;
        msBankAccount = "";
        mdEarnings_r = 0;
        mdDeductions_r = 0;
        mdPayment_r = 0;
        mbDeleted = false;
        mnFkReceiptStatusId = 0;
        mnFkBankId_n = 0;
        mnFkPaymentSystemTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " WHERE id_pay = " + key[0] + " AND id_emp = " + key[1] + " AND id_iss = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkPayrollId = resultSet.getInt("id_pay");
                mnPkEmployeeId = resultSet.getInt("id_emp");
                mnPkIssueId = resultSet.getInt("id_iss");
                msNumberSeries = resultSet.getString("num_ser");
                mnNumber = resultSet.getInt("num");
                mtDateIssue = resultSet.getDate("dt_iss");
                mtDatePayment = resultSet.getDate("dt_pay");
                msBankAccount = resultSet.getString("bank_acc");
                mdEarnings_r = resultSet.getDouble("ear_r");
                mdDeductions_r = resultSet.getDouble("ded_r");
                mdPayment_r = resultSet.getDouble("pay_r");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkReceiptStatusId = resultSet.getInt("fk_st_rcp");
                mnFkBankId_n = resultSet.getInt("fk_bank_n");
                mnFkPaymentSystemTypeId = resultSet.getInt("fk_tp_pay_sys");
                mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
                mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
                mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
                mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

                // Read XML:
                statementAux = statement.getConnection().createStatement();

                sql = "SELECT id_cfd FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " "
                        + "WHERE fid_pay_rcp_pay_n = " + mnPkPayrollId + " AND fid_pay_rcp_emp_n = " + mnPkEmployeeId + " AND fid_pay_rcp_iss_n = " + mnPkIssueId + " ";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsDataCfd = new SDataCfd();
                    if (moDbmsDataCfd.read(new int[] { resultSet.getInt("id_cfd") }, statementAux)!= SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
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
    public int save(Connection connection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
