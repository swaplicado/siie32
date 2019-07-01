/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbPayrollReceiptEarning extends SDbRegistryUser {

    protected int mnPkPayrollId;
    protected int mnPkEmployeeId;
    protected int mnPkMoveId;
    protected double mdUnitsAlleged;
    protected double mdUnits;
    protected double mdFactorAmount;
    protected double mdAmountUnitary;
    protected double mdAmountSystem_r;
    protected double mdAmount_r;
    protected double mdAmountExempt;
    protected double mdAmountTaxable;
    protected int mnBenefitYear;
    protected int mnBenefitAnniversary;
    protected boolean mbAlternativeTaxCalculation;
    protected boolean mbUserEdited;
    protected boolean mbAutomatic;
    //protected boolean mbDeleted;
    //protected boolean mbSystem;
    protected int mnFkEarningTypeId;
    protected int mnFkEarningId;
    protected int mnFkBenefitTypeId;
    protected int mnFkLoanEmployeeId_n;
    protected int mnFkLoanLoanId_n;
    protected int mnFkLoanTypeId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbPayrollReceiptEarningComplement moChildEarningComplement;

    public SDbPayrollReceiptEarning() {
        super(SModConsts.HRS_PAY_RCP_EAR);
    }
    
    private void calculateAmountSystem() {
        mdAmountSystem_r = (SLibUtils.round((mdUnits * mdAmountUnitary * mdFactorAmount), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setUnitsAlleged(double d) { mdUnitsAlleged = d; }
    public void setUnits(double d) { mdUnits = d; }
    public void setFactorAmount(double d) { mdFactorAmount = d; }
    public void setAmountUnitary(double d) { mdAmountUnitary = d; }
    public void setAmountSystem_r(double d) { mdAmountSystem_r = d; }
    public void setAmount_r(double d) { mdAmount_r = d; }
    public void setAmountExempt(double d) { mdAmountExempt = d; }
    public void setAmountTaxable(double d) { mdAmountTaxable = d; }
    public void setBenefitYear(int n) { mnBenefitYear = n; }
    public void setBenefitAnniversary(int n) { mnBenefitAnniversary = n; }
    public void setAlternativeTaxCalculation(boolean b) { mbAlternativeTaxCalculation = b; }
    public void setUserEdited(boolean b) { mbUserEdited = b; }
    public void setAutomatic(boolean b) { mbAutomatic = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkEarningTypeId(int n) { mnFkEarningTypeId = n; }
    public void setFkEarningId(int n) { mnFkEarningId = n; }
    public void setFkBenefitTypeId(int n) { mnFkBenefitTypeId = n; }
    public void setFkLoanEmployeeId_n(int n) { mnFkLoanEmployeeId_n = n; }
    public void setFkLoanLoanId_n(int n) { mnFkLoanLoanId_n = n; }
    public void setFkLoanTypeId_n(int n) { mnFkLoanTypeId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setChildEarningComplement(SDbPayrollReceiptEarningComplement o) { moChildEarningComplement = o; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public double getUnitsAlleged() { return mdUnitsAlleged; }
    public double getUnits() { return mdUnits; }
    public double getFactorAmount() { return mdFactorAmount; }
    public double getAmountUnitary() { return mdAmountUnitary; }
    public double getAmountSystem_r() { return mdAmountSystem_r; }
    public double getAmount_r() { return mdAmount_r; }
    public double getAmountExempt() { return mdAmountExempt; }
    public double getAmountTaxable() { return mdAmountTaxable; }
    public int getBenefitYear() { return mnBenefitYear; }
    public int getBenefitAnniversary() { return mnBenefitAnniversary; }
    public boolean isAlternativeTaxCalculation() { return mbAlternativeTaxCalculation; }
    public boolean isUserEdited() { return mbUserEdited; }
    public boolean isAutomatic() { return mbAutomatic; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkEarningTypeId() { return mnFkEarningTypeId; }
    public int getFkEarningId() { return mnFkEarningId; }
    public int getFkBenefitTypeId() { return mnFkBenefitTypeId; }
    public int getFkLoanEmployeeId_n() { return mnFkLoanEmployeeId_n; }
    public int getFkLoanLoanId_n() { return mnFkLoanLoanId_n; }
    public int getFkLoanTypeId_n() { return mnFkLoanTypeId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public SDbPayrollReceiptEarningComplement getChildEarningComplement() { return moChildEarningComplement; }
    
    public int[] getLoanKey() { return new int[] { mnFkLoanEmployeeId_n, mnFkLoanLoanId_n }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPayrollId = pk[0];
        mnPkEmployeeId = pk[1];
        mnPkMoveId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId, mnPkMoveId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPayrollId = 0;
        mnPkEmployeeId = 0;
        mnPkMoveId = 0;
        mdUnitsAlleged = 0;
        mdUnits = 0;
        mdFactorAmount = 0;
        mdAmountUnitary = 0;
        mdAmountSystem_r = 0;
        mdAmount_r = 0;
        mdAmountExempt = 0;
        mdAmountTaxable = 0;
        mnBenefitYear = 0;
        mnBenefitAnniversary = 0;
        mbAlternativeTaxCalculation = false;
        mbUserEdited = false;
        mbAutomatic = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkEarningTypeId = 0;
        mnFkEarningId = 0;
        mnFkBenefitTypeId = 0;
        mnFkLoanEmployeeId_n = 0;
        mnFkLoanLoanId_n = 0;
        mnFkLoanTypeId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moChildEarningComplement = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " AND id_mov = " + mnPkMoveId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " AND id_emp = " + pk[1] + " AND id_mov = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMoveId = 0;

        msSql = "SELECT COALESCE(MAX(id_mov), 0) + 1 FROM " + getSqlTable() + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMoveId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnPkPayrollId = resultSet.getInt("id_pay");
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mnPkMoveId = resultSet.getInt("id_mov");
            mdUnitsAlleged = resultSet.getDouble("unt_all");
            mdUnits = resultSet.getDouble("unt");
            mdFactorAmount = resultSet.getDouble("fac_amt");
            mdAmountUnitary = resultSet.getDouble("amt_unt");
            mdAmountSystem_r = resultSet.getDouble("amt_sys_r");
            mdAmount_r = resultSet.getDouble("amt_r");
            mdAmountExempt = resultSet.getDouble("amt_exem");
            mdAmountTaxable = resultSet.getDouble("amt_taxa");
            mnBenefitYear = resultSet.getInt("ben_year");
            mnBenefitAnniversary = resultSet.getInt("ben_ann");
            mbAlternativeTaxCalculation = resultSet.getBoolean("b_alt_tax");
            mbUserEdited = resultSet.getBoolean("b_usr");
            mbAutomatic = resultSet.getBoolean("b_aut");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkEarningTypeId = resultSet.getInt("fk_tp_ear");
            mnFkEarningId = resultSet.getInt("fk_ear");
            mnFkBenefitTypeId = resultSet.getInt("fk_tp_ben");
            mnFkLoanEmployeeId_n = resultSet.getInt("fk_loan_emp_n");
            mnFkLoanLoanId_n = resultSet.getInt("fk_loan_loan_n");
            mnFkLoanTypeId_n = resultSet.getInt("fk_tp_loan_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read payrollReceiptDeductionComplement:
            
            msSql = "SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR_CMP) + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " AND id_mov = " + mnPkMoveId + " ";
            
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                moChildEarningComplement = new SDbPayrollReceiptEarningComplement();
                moChildEarningComplement.read(session, new int[] { mnPkPayrollId, mnPkEmployeeId, mnPkMoveId });
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        SDbPayrollReceipt.checkDummyRegistry(session, mnPkEmployeeId);
        
        calculateAmountSystem();

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPayrollId + ", " +
                    mnPkEmployeeId + ", " +
                    mnPkMoveId + ", " +
                    mdUnitsAlleged + ", " + 
                    mdUnits + ", " + 
                    mdFactorAmount + ", " + 
                    mdAmountUnitary + ", " + 
                    mdAmountSystem_r + ", " + 
                    mdAmount_r + ", " + 
                    mdAmountExempt + ", " + 
                    mdAmountTaxable + ", " + 
                    mnBenefitYear + ", " + 
                    mnBenefitAnniversary + ", " + 
                    (mbAlternativeTaxCalculation ? 1 : 0) + ", " +
                    (mbUserEdited ? 1 : 0) + ", " + 
                    (mbAutomatic ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkEarningTypeId + ", " + 
                    mnFkEarningId + ", " + 
                    mnFkBenefitTypeId + ", " + 
                    (mnFkLoanEmployeeId_n > 0 ? mnFkLoanEmployeeId_n : "NULL") + ", " +
                    (mnFkLoanLoanId_n > 0 ? mnFkLoanLoanId_n : "NULL") + ", " +
                    (mnFkLoanTypeId_n > 0 ? mnFkLoanTypeId_n : "NULL") + ", " +
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
                    "id_pay = " + mnPkPayrollId + ", " +
                    "id_emp = " + mnPkEmployeeId + ", " +
                    "id_mov = " + mnPkMoveId + ", " +
                    */
                    "unt_all = " + mdUnitsAlleged + ", " +
                    "unt = " + mdUnits + ", " +
                    "fac_amt = " + mdFactorAmount + ", " +
                    "amt_unt = " + mdAmountUnitary + ", " +
                    "amt_sys_r = " + mdAmountSystem_r + ", " +
                    "amt_r = " + mdAmount_r + ", " +
                    "amt_exem = " + mdAmountExempt + ", " +
                    "amt_taxa = " + mdAmountTaxable + ", " +
                    "ben_year = " + mnBenefitYear + ", " +
                    "ben_ann = " + mnBenefitAnniversary + ", " +
                    "b_alt_tax = " + (mbAlternativeTaxCalculation ? 1 : 0) + ", " +
                    "b_usr = " + (mbUserEdited ? 1 : 0) + ", " +
                    "b_aut = " + (mbAutomatic ? 1 : 0) + ", " +
                    //"b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_tp_ear = " + mnFkEarningTypeId + ", " +
                    "fk_ear = " + mnFkEarningId + ", " +
                    "fk_tp_ben = " + mnFkBenefitTypeId + ", " +
                    "fk_loan_emp_n = " + (mnFkLoanEmployeeId_n > 0 ? mnFkLoanEmployeeId_n : "NULL") + ", " +
                    "fk_loan_loan_n = " + (mnFkLoanLoanId_n > 0 ? mnFkLoanLoanId_n : "NULL") + ", " +
                    "fk_tp_loan_n = " + (mnFkLoanTypeId_n > 0 ? mnFkLoanTypeId_n : "NULL") + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // Save payrollEarningComplement:

        if (moChildEarningComplement != null) {
            moChildEarningComplement.setDeleted(mbDeleted);
            moChildEarningComplement.setPkPayrollId(mnPkPayrollId);
            moChildEarningComplement.setPkEmployeeId(mnPkEmployeeId);
            moChildEarningComplement.setPkMoveId(mnPkMoveId);
            moChildEarningComplement.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPayrollReceiptEarning clone() throws CloneNotSupportedException {
        SDbPayrollReceiptEarning registry = new SDbPayrollReceiptEarning();

        registry.setPkPayrollId(this.getPkPayrollId());
        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkMoveId(this.getPkMoveId());
        registry.setUnitsAlleged(this.getUnitsAlleged());
        registry.setUnits(this.getUnits());
        registry.setFactorAmount(this.getFactorAmount());
        registry.setAmountUnitary(this.getAmountUnitary());
        registry.setAmountSystem_r(this.getAmountSystem_r());
        registry.setAmount_r(this.getAmount_r());
        registry.setAmountExempt(this.getAmountExempt());
        registry.setAmountTaxable(this.getAmountTaxable());
        registry.setBenefitYear(this.getBenefitYear());
        registry.setBenefitAnniversary(this.getBenefitAnniversary());
        registry.setAlternativeTaxCalculation(this.isAlternativeTaxCalculation());
        registry.setUserEdited(this.isUserEdited());
        registry.setAutomatic(this.isAutomatic());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkEarningTypeId(this.getFkEarningTypeId());
        registry.setFkEarningId(this.getFkEarningId());
        registry.setFkBenefitTypeId(this.getFkBenefitTypeId());
        registry.setFkLoanEmployeeId_n(this.getFkLoanEmployeeId_n());
        registry.setFkLoanLoanId_n(this.getFkLoanLoanId_n());
        registry.setFkLoanTypeId_n(this.getFkLoanTypeId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setChildEarningComplement(this.getChildEarningComplement());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
