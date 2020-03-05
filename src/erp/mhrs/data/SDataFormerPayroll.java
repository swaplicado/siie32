/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mhrs.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataRecord;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDataFormerPayroll extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPayrollId;
    protected int mnYear;
    protected int mnPeriod;
    protected int mnNumber;
    protected java.lang.String msType;
    protected java.lang.String msNote;
    protected java.util.Date mtDateBegin;
    protected java.util.Date mtDateEnd;
    protected java.util.Date mtDatePayment;
    protected double mdDebit_r;
    protected double mdCredit_r;
    protected boolean mbIsRegular;
    protected boolean mbIsClosed;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.mhrs.data.SDataFormerPayrollEmp> mvDbmsDataFormerPayrollEmps;
    protected java.util.Vector<erp.mhrs.data.SDataFormerPayrollMove> mvDbmsDataFormerPayrollMoves;

    protected java.util.Vector<erp.mfin.data.SDataRecord> mvAuxDataRecords;

    public SDataFormerPayroll() {
        super(SDataConstants.HRS_SIE_PAY);

        mvDbmsDataFormerPayrollEmps = new Vector<>();
        mvDbmsDataFormerPayrollMoves = new Vector<>();
        mvAuxDataRecords = new Vector<>();

        reset();
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setYear(int n) { mnYear = n; }
    public void setPeriod(int n) { mnPeriod = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setType(java.lang.String s) { msType = s; }
    public void setNote(java.lang.String s) { msNote = s; }
    public void setDateBegin(java.util.Date t) { mtDateBegin = t; }
    public void setDateEnd(java.util.Date t) { mtDateEnd = t; }
    public void setDatePayment(java.util.Date t) { mtDatePayment = t; }
    public void setDebit_r(double d) { mdDebit_r = d; }
    public void setCredit_r(double d) { mdCredit_r = d; }
    public void setIsRegular(boolean b) { mbIsRegular = b; }
    public void setIsClosed(boolean b) { mbIsClosed = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    public int getNumber() { return mnNumber; }
    public java.lang.String getType() { return msType; }
    public java.lang.String getNote() { return msNote; }
    public java.util.Date getDateBegin() { return mtDateBegin; }
    public java.util.Date getDateEnd() { return mtDateEnd; }
    public java.util.Date getDatePayment() { return mtDatePayment; }
    public double getDebit_r() { return mdDebit_r; }
    public double getCredit_r() { return mdCredit_r; }
    public boolean getIsRegular() { return mbIsRegular; }
    public boolean getIsClosed() { return mbIsClosed; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.util.Vector<erp.mhrs.data.SDataFormerPayrollEmp> getDbmsDataFormerPayrollEmps() { return mvDbmsDataFormerPayrollEmps; }
    public java.util.Vector<erp.mhrs.data.SDataFormerPayrollMove> getDbmsDataFormerPayrollMoves() { return mvDbmsDataFormerPayrollMoves; }
    public java.util.Vector<erp.mfin.data.SDataRecord> getAuxDataRecords() { return mvAuxDataRecords; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPayrollId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPayrollId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPayrollId = 0;
        mnYear = 0;
        mnPeriod = 0;
        mnNumber = 0;
        msType = "";
        msNote = "";
        mtDateBegin = null;
        mtDateEnd = null;
        mtDatePayment = null;
        mdDebit_r = 0;
        mdCredit_r = 0;
        mbIsRegular = false;
        mbIsClosed = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mvDbmsDataFormerPayrollEmps.clear();
        mvDbmsDataFormerPayrollMoves.clear();
        mvAuxDataRecords.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        Statement statementAux = null;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM hrs_sie_pay WHERE id_pay = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkPayrollId = resultSet.getInt("id_pay");
                mnYear = resultSet.getInt("pay_year");
                mnPeriod = resultSet.getInt("pay_per");
                mnNumber = resultSet.getInt("pay_num");
                msType = resultSet.getString("pay_type");
                msNote = resultSet.getString("pay_note");
                mtDateBegin = resultSet.getDate("dt_beg");
                mtDateEnd = resultSet.getDate("dt_end");
                mtDatePayment = resultSet.getDate("dt_pay");
                mdDebit_r = resultSet.getDouble("debit_r");
                mdCredit_r = resultSet.getDouble("credit_r");
                mbIsRegular = resultSet.getBoolean("b_reg");
                mbIsClosed = resultSet.getBoolean("b_cls");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                statementAux = statement.getConnection().createStatement();

                // Read aswell employees:

                sql = "SELECT id_pay, id_emp FROM hrs_sie_pay_emp WHERE id_pay = " + mnPkPayrollId + " ";
                resultSet = statementAux.executeQuery(sql);
                while (resultSet.next()) {
                    SDataFormerPayrollEmp emp = new SDataFormerPayrollEmp();
                    if (emp.read(new int[] { resultSet.getInt("id_pay"), resultSet.getInt("id_emp") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDataFormerPayrollEmps.add(emp);
                    }
                }

                // Read aswell employee moves:

                sql = "SELECT id_pay, id_mov FROM hrs_sie_pay_mov WHERE id_pay = " + mnPkPayrollId + " ";
                resultSet = statementAux.executeQuery(sql);
                while (resultSet.next()) {
                    SDataFormerPayrollMove move = new SDataFormerPayrollMove();
                    if (move.read(new int[] { resultSet.getInt("id_pay"), resultSet.getInt("id_mov") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDataFormerPayrollMoves.add(move);
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
            callableStatement = connection.prepareCall(
                    "{ CALL hrs_sie_pay_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPayrollId);
            callableStatement.setInt(nParam++, mnYear);
            callableStatement.setInt(nParam++, mnPeriod);
            callableStatement.setInt(nParam++, mnNumber);
            callableStatement.setString(nParam++, msType);
            callableStatement.setString(nParam++, msNote);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateBegin.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtDatePayment.getTime()));
            callableStatement.setDouble(nParam++, mdDebit_r);
            callableStatement.setDouble(nParam++, mdCredit_r);
            callableStatement.setBoolean(nParam++, mbIsRegular);
            callableStatement.setBoolean(nParam++, mbIsClosed);
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
                // Save aswell accounting records:

                for (SDataRecord record : mvAuxDataRecords) {
                    if (record.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell employees:

                for (SDataFormerPayrollEmp emp : mvDbmsDataFormerPayrollEmps) {
                    emp.setPkPayrollId(mnPkPayrollId);
                    if (emp.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell employee moves:

                for (SDataFormerPayrollMove move : mvDbmsDataFormerPayrollMoves) {
                    move.setPkPayrollId(mnPkPayrollId);
                    if (move.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

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
    
    public int saveField(final java.sql.Connection connection, final int[] pk) throws SQLException, Exception {
        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
        String sql;

        sql = "UPDATE hrs_sie_pay SET dt_pay = '" + SLibUtils.DbmsDateFormatDate.format(mtDatePayment) + "', fid_usr_edit = " + mnFkUserEditId + ", ts_edit = NOW() " + " WHERE id_pay = " + pk[0] + " ";
        connection.createStatement().execute(sql);
        
        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;

        return mnLastDbActionResult;
    }
}
