
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbAutomaticDeduction extends SDbRegistryUser implements SGridRow {

    protected int mnPkDeductionId;
    protected int mnPkAutomaticId;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    protected double mdUnits;
    protected double mdAmountUnitary;
    protected double mdAmount_r;
    //protected boolean mbDeleted;
    protected int mnFkDeductionTypeId;
    protected int mnFkPaysheetTypeId;
    protected int mnFkEmployeeId_n;
    protected int mnFkLoanEmployeeId_n;
    protected int mnFkLoanLoanId_n;
    protected int mnFkLoanTypeId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msXtaDeductionCode;
    protected String msXtaDeduction;
    protected String msXtaUnit;
    protected String msXtaLoan;
    protected String msXtaPaysheetType;
    
    public SDbAutomaticDeduction() {
        super(SModConsts.HRS_AUT_DED);
    }

    public void setPkDeductionId(int n) { mnPkDeductionId = n; }
    public void setPkAutomaticId(int n) { mnPkAutomaticId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setUnits(double d) { mdUnits = d; }
    public void setAmountUnitary(double d) { mdAmountUnitary = d; }
    public void setAmount_r(double d) { mdAmount_r = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDeductionTypeId(int n) { mnFkDeductionTypeId = n; }
    public void setFkPaysheetTypeId(int n) { mnFkPaysheetTypeId = n; }
    public void setFkEmployeeId_n(int n) { mnFkEmployeeId_n = n; }
    public void setFkLoanEmployeeId_n(int n) { mnFkLoanEmployeeId_n = n; }
    public void setFkLoanLoanId_n(int n) { mnFkLoanLoanId_n = n; }
    public void setFkLoanTypeId_n(int n) { mnFkLoanTypeId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setXtaDeductionCode(String s) { msXtaDeductionCode = s; }
    public void setXtaDeduction(String s) { msXtaDeduction = s; }
    public void setXtaUnit(String s) { msXtaUnit = s; }
    public void setXtaLoan(String s) { msXtaLoan = s; }
    public void setXtaPaysheetType(String s) { msXtaPaysheetType = s; }

    public int getPkDeductionId() { return mnPkDeductionId; }
    public int getPkAutomaticId() { return mnPkAutomaticId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public double getUnits() { return mdUnits; }
    public double getAmountUnitary() { return mdAmountUnitary; }
    public double getAmount_r() { return mdAmount_r; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDeductionTypeId() { return mnFkDeductionTypeId; }
    public int getFkPaysheetTypeId() { return mnFkPaysheetTypeId; }
    public int getFkEmployeeId_n() { return mnFkEmployeeId_n; }
    public int getFkLoanEmployeeId_n() { return mnFkLoanEmployeeId_n; }
    public int getFkLoanLoanId_n() { return mnFkLoanLoanId_n; }
    public int getFkLoanTypeId_n() { return mnFkLoanTypeId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public String getXtaDeductionCode() { return msXtaDeductionCode; }
    public String getXtaDeduction() { return msXtaDeduction; }
    public String getXtaUnit() { return msXtaUnit; }
    public String getXtaLoan() { return msXtaLoan; }
    public String getXtaTypeSht() { return msXtaPaysheetType; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDeductionId = pk[0];
        mnPkAutomaticId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDeductionId, mnPkAutomaticId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkDeductionId = 0;
        mnPkAutomaticId = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mdUnits = 0;
        mdAmountUnitary = 0;
        mdAmount_r = 0;
        mbDeleted = false;
        mnFkDeductionTypeId = 0;
        mnFkPaysheetTypeId = 0;
        mnFkEmployeeId_n = 0;
        mnFkLoanEmployeeId_n = 0;
        mnFkLoanLoanId_n = 0;
        mnFkLoanTypeId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ded = " + mnPkDeductionId + " AND id_aut = " + mnPkAutomaticId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ded = " + pk[0] + " AND " +
                "id_aut = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAutomaticId = 0;

        msSql = "SELECT COALESCE(MAX(id_aut), 0) + 1 FROM " + getSqlTable() + " WHERE id_ded = " + mnPkDeductionId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAutomaticId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkDeductionId = resultSet.getInt("id_ded");
            mnPkAutomaticId = resultSet.getInt("id_aut");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            mdUnits = resultSet.getDouble("unt");
            mdAmountUnitary = resultSet.getDouble("amt_unt");
            mdAmount_r = resultSet.getDouble("amt_r");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkDeductionTypeId = resultSet.getInt("fk_tp_ded");
            mnFkPaysheetTypeId = resultSet.getInt("fk_tp_pay_sht");
            mnFkEmployeeId_n = resultSet.getInt("fk_emp_n");
            mnFkLoanEmployeeId_n = resultSet.getInt("fk_loan_emp_n");
            mnFkLoanLoanId_n = resultSet.getInt("fk_loan_loan_n");
            mnFkLoanTypeId_n = resultSet.getInt("fk_tp_loan_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDeductionId + ", " + 
                    mnPkAutomaticId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    (mtDateEnd_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " + 
                    mdUnits + ", " + 
                    mdAmountUnitary + ", " + 
                    mdAmount_r + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkDeductionTypeId + ", " +
                    mnFkPaysheetTypeId + ", " +
                    (mnFkEmployeeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkEmployeeId_n) + ", " +
                    (mnFkLoanEmployeeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLoanEmployeeId_n) + ", " +
                    (mnFkLoanLoanId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLoanLoanId_n) + ", " +
                    (mnFkLoanTypeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLoanTypeId_n) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_ded = " + mnPkDeductionId + ", " +
                    "id_aut = " + mnPkAutomaticId + ", " +
                    */
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = " + (mtDateEnd_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " +
                    "unt = " + mdUnits + ", " +
                    "amt_unt = " + mdAmountUnitary + ", " +
                    "amt_r = " + mdAmount_r + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_ded = " + mnFkDeductionTypeId + ", " +
                    "fk_tp_pay_sht = " + mnFkPaysheetTypeId + ", " +
                    "fk_emp_n = " + (mnFkEmployeeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkEmployeeId_n) + ", " +
                    "fk_loan_emp_n = " + (mnFkLoanEmployeeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLoanEmployeeId_n) + ", " +
                    "fk_loan_loan_n = " + (mnFkLoanLoanId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLoanLoanId_n) + ", " +
                    "fk_tp_loan_n = " + (mnFkLoanTypeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkLoanTypeId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAutomaticDeduction clone() throws CloneNotSupportedException {
        SDbAutomaticDeduction registry = new SDbAutomaticDeduction();

        registry.setPkDeductionId(this.getPkDeductionId());
        registry.setPkAutomaticId(this.getPkAutomaticId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setUnits(this.getUnits());
        registry.setAmountUnitary(this.getAmountUnitary());
        registry.setAmount_r(this.getAmount_r());
        registry.setDeleted(this.isDeleted());
        registry.setFkDeductionTypeId(this.getFkDeductionTypeId());
        registry.setFkPaysheetTypeId(this.getFkPaysheetTypeId());
        registry.setFkEmployeeId_n(this.getFkEmployeeId_n());
        registry.setFkLoanEmployeeId_n(this.getFkLoanEmployeeId_n());
        registry.setFkLoanLoanId_n(this.getFkLoanLoanId_n());
        registry.setFkLoanTypeId_n(this.getFkLoanTypeId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getXtaDeductionCode();
    }

    @Override
    public String getRowName() {
        return getXtaDeduction();
    }

    @Override
    public boolean isRowSystem() {
        return isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch (row) {
            case 0:
                value = msXtaDeductionCode;
                break;
            case 1:
                value = msXtaDeduction;
                break;
            case 2:
                value = mdAmount_r;
                break;
            case 3:
                value = msXtaUnit;
                break;
            case 4:
                value = mtDateStart;
                break;
            case 5:
                value = mtDateEnd_n;
                break;
            case 6:
                value = msXtaLoan;
                break;
            case 7:
                value = msXtaPaysheetType;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
                msXtaDeductionCode = (String) value;
                break;
            case 1:
                msXtaDeduction = (String) value;
                break;
            case 2:
                mdAmount_r = (double) value;
                break;
            case 3:
                msXtaUnit = (String) value;
                break;
            case 4:
                mtDateStart = (Date) value;
                break;
            case 5:
                mtDateEnd_n = (Date) value;
                break;
            case 6:
                msXtaLoan = (String) value;
                break;
            case 7:
                msXtaPaysheetType = (String) value;
                break;
            default:
        }
    }
}
