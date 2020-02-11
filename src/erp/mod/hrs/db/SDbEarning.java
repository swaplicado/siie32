/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.cfd.SCfdConsts;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbEarning extends SDbRegistryUser {
    
    public static final HashMap<Integer, String> AuxAmountLabels = new HashMap<>();
    public static final HashMap<Integer, String> AuxAmountHints = new HashMap<>();
    
    static {
        AuxAmountLabels.put(SModSysConsts.HRSS_TP_EAR_TAX_SUB, "Subsidio efectivo");
        AuxAmountHints.put(SModSysConsts.HRSS_TP_EAR_TAX_SUB, SCfdConsts.CFDI_OTHER_PAY_TAX_SUBSIDY_EFF);
    }

    protected int mnPkEarningId;
    protected String msCode;
    protected String msName;
    protected String msNameAbbreviated;
    protected double mdExemptionMwz;
    protected double mdExemptionSalaryEqualsMwzPercentage;
    protected double mdExemptionSalaryEqualsMwzLimit;
    protected double mdExemptionSalaryGreaterMwzPercentage;
    protected double mdExemptionSalaryGreaterMwzLimit;
    protected double mdExemptionMwzYear;
    protected double mdPayPercentage;
    protected double mdUnitsMaximumWeek;
    protected double mdUnitsFactor;
    protected boolean mbWelfare;
    protected boolean mbDaysAdjustment;
    protected boolean mbDaysAbsence;
    protected boolean mbDaysWorked;
    @Deprecated
    protected boolean mbDaysWorkedBasedOn;
    protected boolean mbWithholding;
    protected boolean mbAlternativeTaxCalculation;
    protected boolean mbPayrollTax;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkEarningTypeId;
    protected int mnFkEarningComputationTypeId;
    protected int mnFkEarningExemptionTypeId;
    protected int mnFkEarningExemptionTypeYearId;
    protected int mnFkOtherPaymentTypeId;
    protected int mnFkLoanTypeId;
    protected int mnFkBenefitTypeId;
    protected int mnFkAccountingConfigurationTypeId;
    protected int mnFkAccountingRecordTypeId;
    protected int mnFkAbsenceClassId_n;
    protected int mnFkAbsenceTypeId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected int mnOldAccountingConfigurationTypeId;
    
    public SDbEarning() {
        super(SModConsts.HRS_EAR);
    }

    public void setPkEarningId(int n) { mnPkEarningId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setNameAbbreviated(String s) { msNameAbbreviated = s; }
    public void setExemptionMwz(double d) { mdExemptionMwz = d; }
    public void setExemptionSalaryEqualsMwzPercentage(double d) { mdExemptionSalaryEqualsMwzPercentage = d; }
    public void setExemptionSalaryEqualsMwzLimit(double d) { mdExemptionSalaryEqualsMwzLimit = d; }
    public void setExemptionSalaryGreaterMwzPercentage(double d) { mdExemptionSalaryGreaterMwzPercentage = d; }
    public void setExemptionSalaryGreaterMwzLimit(double d) { mdExemptionSalaryGreaterMwzLimit = d; }
    public void setExemptionMwzYear(double d) { mdExemptionMwzYear = d; }
    public void setPayPercentage(double d) { mdPayPercentage = d; }
    public void setUnitsMaximumWeek(double d) { mdUnitsMaximumWeek = d; }
    public void setUnitsFactor(double d) { mdUnitsFactor = d; }
    public void setWelfare(boolean b) { mbWelfare = b; }
    public void setDaysAdjustment(boolean b) { mbDaysAdjustment = b; }
    public void setDaysAbsence(boolean b) { mbDaysAbsence = b; }
    public void setDaysWorked(boolean b) { mbDaysWorked = b; }
    @Deprecated
    public void setDaysWorkedBasedOn(boolean b) { mbDaysWorkedBasedOn = b; }
    public void setWithholding(boolean b) { mbWithholding = b; }
    public void setAlternativeTaxCalculation(boolean b) { mbAlternativeTaxCalculation = b; }
    public void setPayrollTax(boolean b) { mbPayrollTax = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkEarningTypeId(int n) { mnFkEarningTypeId = n; }
    public void setFkEarningComputationTypeId(int n) { mnFkEarningComputationTypeId = n; }
    public void setFkEarningExemptionTypeId(int n) { mnFkEarningExemptionTypeId = n; }
    public void setFkEarningExemptionTypeYearId(int n) { mnFkEarningExemptionTypeYearId = n; }
    public void setFkOtherPaymentTypeId(int n) { mnFkOtherPaymentTypeId = n; }
    public void setFkLoanTypeId(int n) { mnFkLoanTypeId = n; }
    public void setFkBenefitTypeId(int n) { mnFkBenefitTypeId = n; }
    public void setFkAccountingConfigurationTypeId(int n) { mnFkAccountingConfigurationTypeId = n; }
    public void setFkAccountingRecordTypeId(int n) { mnFkAccountingRecordTypeId = n; }
    public void setFkAbsenceClassId_n(int n) { mnFkAbsenceClassId_n = n; }
    public void setFkAbsenceTypeId_n(int n) { mnFkAbsenceTypeId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEarningId() { return mnPkEarningId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getNameAbbreviated() { return msNameAbbreviated; }
    public double getExemptionMwz() { return mdExemptionMwz; }
    public double getExemptionSalaryEqualsMwzPercentage() { return mdExemptionSalaryEqualsMwzPercentage; }
    public double getExemptionSalaryEqualsMwzLimit() { return mdExemptionSalaryEqualsMwzLimit; }
    public double getExemptionSalaryGreaterMwzPercentage() { return mdExemptionSalaryGreaterMwzPercentage; }
    public double getExemptionSalaryGreaterMwzLimit() { return mdExemptionSalaryGreaterMwzLimit; }
    public double getExemptionMwzYear() { return mdExemptionMwzYear; }
    public double getPayPercentage() { return mdPayPercentage; }
    public double getUnitsMaximumWeek() { return mdUnitsMaximumWeek; }
    public double getUnitsFactor() { return mdUnitsFactor; }
    public boolean isWelfare() { return mbWelfare; }
    public boolean isDaysAdjustment() { return mbDaysAdjustment; }
    public boolean isDaysAbsence() { return mbDaysAbsence; }
    public boolean isDaysWorked() { return mbDaysWorked; }
    @Deprecated
    public boolean isDaysWorkedBasedOn() { return mbDaysWorkedBasedOn; }
    public boolean isWithholding() { return mbWithholding; }
    public boolean isAlternativeTaxCalculation() { return mbAlternativeTaxCalculation; }
    public boolean isPayrollTax() { return mbPayrollTax; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkEarningTypeId() { return mnFkEarningTypeId; }
    public int getFkEarningComputationTypeId() { return mnFkEarningComputationTypeId; }
    public int getFkEarningExemptionTypeId() { return mnFkEarningExemptionTypeId; }
    public int getFkEarningExemptionTypeYearId() { return mnFkEarningExemptionTypeYearId; }
    public int getFkOtherPaymentTypeId() { return mnFkOtherPaymentTypeId; }
    public int getFkLoanTypeId() { return mnFkLoanTypeId; }
    public int getFkBenefitTypeId() { return mnFkBenefitTypeId; }
    public int getFkAccountingConfigurationTypeId() { return mnFkAccountingConfigurationTypeId; }
    public int getFkAccountingRecordTypeId() { return mnFkAccountingRecordTypeId; }
    public int getFkAbsenceClassId_n() { return mnFkAbsenceClassId_n; }
    public int getFkAbsenceTypeId_n() { return mnFkAbsenceTypeId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setOldAccountingConfigurationTypeId(int n) { mnOldAccountingConfigurationTypeId = n; }

    public int getOldAccountingConfigurationTypeId() { return mnOldAccountingConfigurationTypeId; }
    
    public boolean isComputedByPercentage() {
        return SLibUtils.belongsTo(mnFkEarningComputationTypeId, new int[] { SModSysConsts.HRSS_TP_EAR_COMP_PCT_DAY, SModSysConsts.HRSS_TP_EAR_COMP_PCT_HR, SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME } );
    }
    
    public boolean isBasedOnDaysWorked() {
        return mnFkEarningComputationTypeId == SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME;
    }
    
    public boolean isBasedOnUnits() {
        return mnFkEarningComputationTypeId != SModSysConsts.HRSS_TP_EAR_COMP_AMT;
    }
    
    public boolean areUnitsModifiable() {
        return isBasedOnUnits() && !isAbsence();
    }
    
    public int[] getAbsenceClassKey() { return new int[] { mnFkAbsenceClassId_n }; }
    public int[] getAbsenceTypeKey() { return new int[] { mnFkAbsenceClassId_n, mnFkAbsenceTypeId_n }; }
    
    public boolean isLoan() { return mnFkLoanTypeId != 0 && mnFkLoanTypeId != SModSysConsts.HRSS_TP_LOAN_NON; }
    public boolean isBenefit() { return mnFkBenefitTypeId != 0 && mnFkBenefitTypeId != SModSysConsts.HRSS_TP_BEN_NON; }
    public boolean isAbsence() { return mnFkAbsenceClassId_n != 0 && mnFkAbsenceTypeId_n != 0; }
    
    public boolean isMassAsignable() { return !isLoan() && !isBenefit() && !isAbsence(); }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEarningId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEarningId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEarningId = 0;
        msCode = "";
        msName = "";
        msNameAbbreviated = "";
        mdExemptionMwz = 0;
        mdExemptionSalaryEqualsMwzPercentage = 0;
        mdExemptionSalaryEqualsMwzLimit = 0;
        mdExemptionSalaryGreaterMwzPercentage = 0;
        mdExemptionSalaryGreaterMwzLimit = 0;
        mdExemptionMwzYear = 0;
        mdPayPercentage = 0;
        mdUnitsMaximumWeek = 0;
        mdUnitsFactor = 0;
        mbWelfare = false;
        mbDaysAdjustment = false;
        mbDaysAbsence = false;
        mbDaysWorked = false;
        mbDaysWorkedBasedOn = false;
        mbWithholding = false;
        mbAlternativeTaxCalculation = false;
        mbPayrollTax = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkEarningTypeId = 0;
        mnFkEarningComputationTypeId = 0;
        mnFkEarningExemptionTypeId = 0;
        mnFkEarningExemptionTypeYearId = 0;
        mnFkOtherPaymentTypeId = 0;
        mnFkLoanTypeId = 0;
        mnFkBenefitTypeId = 0;
        mnFkAccountingConfigurationTypeId = 0;
        mnFkAccountingRecordTypeId = 0;
        mnFkAbsenceClassId_n = 0;
        mnFkAbsenceTypeId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mnOldAccountingConfigurationTypeId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ear = " + mnPkEarningId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ear = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEarningId = 0;

        msSql = "SELECT COALESCE(MAX(id_ear), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEarningId = resultSet.getInt(1);
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
            mnPkEarningId = resultSet.getInt("id_ear");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msNameAbbreviated = resultSet.getString("name_abbr");
            mdExemptionMwz = resultSet.getDouble("exem_mwz");
            mdExemptionSalaryEqualsMwzPercentage = resultSet.getDouble("exem_sal_equ_mwz_per");
            mdExemptionSalaryEqualsMwzLimit = resultSet.getDouble("exem_sal_equ_mwz_lim");
            mdExemptionSalaryGreaterMwzPercentage = resultSet.getDouble("exem_sal_grt_mwz_per");
            mdExemptionSalaryGreaterMwzLimit = resultSet.getDouble("exem_sal_grt_mwz_lim");
            mdExemptionMwzYear = resultSet.getDouble("exem_mwz_year");
            mdPayPercentage = resultSet.getDouble("pay_per");
            mdUnitsMaximumWeek = resultSet.getDouble("unt_max_wee");
            mdUnitsFactor = resultSet.getDouble("unt_fac");
            mbWelfare = resultSet.getBoolean("b_wel");
            mbDaysAdjustment = resultSet.getBoolean("b_day_adj");
            mbDaysAbsence = resultSet.getBoolean("b_day_abs");
            mbDaysWorked = resultSet.getBoolean("b_day_wrk");
            mbDaysWorkedBasedOn = resultSet.getBoolean("b_day_wrk_bas");
            mbWithholding = resultSet.getBoolean("b_who");
            mbAlternativeTaxCalculation = resultSet.getBoolean("b_alt_tax");
            mbPayrollTax = resultSet.getBoolean("b_pay_tax");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkEarningTypeId = resultSet.getInt("fk_tp_ear");
            mnFkEarningComputationTypeId = resultSet.getInt("fk_tp_ear_comp");
            mnFkEarningExemptionTypeId = resultSet.getInt("fk_tp_ear_exem");
            mnFkEarningExemptionTypeYearId = resultSet.getInt("fk_tp_ear_exem_year");
            mnFkOtherPaymentTypeId = resultSet.getInt("fk_tp_oth_pay");
            mnFkLoanTypeId = resultSet.getInt("fk_tp_loan");
            mnFkBenefitTypeId = resultSet.getInt("fk_tp_ben");
            mnFkAccountingConfigurationTypeId = resultSet.getInt("fk_tp_acc_cfg");
            mnFkAccountingRecordTypeId = resultSet.getInt("fk_tp_acc_rec");
            mnFkAbsenceClassId_n = resultSet.getInt("fk_cl_abs_n");
            mnFkAbsenceTypeId_n = resultSet.getInt("fk_tp_abs_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mnOldAccountingConfigurationTypeId = mnFkAccountingConfigurationTypeId;

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
                    mnPkEarningId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msNameAbbreviated + "', " + 
                    mdExemptionMwz + ", " + 
                    mdExemptionSalaryEqualsMwzPercentage + ", " + 
                    mdExemptionSalaryEqualsMwzLimit + ", " + 
                    mdExemptionSalaryGreaterMwzPercentage + ", " + 
                    mdExemptionSalaryGreaterMwzLimit + ", " + 
                    mdExemptionMwzYear + ", " + 
                    mdPayPercentage + ", " + 
                    mdUnitsMaximumWeek + ", " + 
                    mdUnitsFactor + ", " + 
                    (mbWelfare ? 1 : 0) + ", " + 
                    (mbDaysAdjustment ? 1 : 0) + ", " + 
                    (mbDaysAbsence ? 1 : 0) + ", " + 
                    (mbDaysWorked ? 1 : 0) + ", " + 
                    (mbDaysWorkedBasedOn ? 1 : 0) + ", " + 
                    (mbWithholding ? 1 : 0) + ", " + 
                    (mbAlternativeTaxCalculation ? 1 : 0) + ", " + 
                    (mbPayrollTax ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkEarningTypeId + ", " + 
                    mnFkEarningComputationTypeId + ", " + 
                    mnFkEarningExemptionTypeId + ", " + 
                    mnFkEarningExemptionTypeYearId + ", " + 
                    mnFkOtherPaymentTypeId + ", " + 
                    mnFkLoanTypeId + ", " + 
                    mnFkBenefitTypeId + ", " + 
                    mnFkAccountingConfigurationTypeId + ", " + 
                    mnFkAccountingRecordTypeId + ", " + 
                    (mnFkAbsenceClassId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbsenceClassId_n) + ", " +
                    (mnFkAbsenceTypeId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbsenceTypeId_n) + ", " +
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
                    "id_ear = " + mnPkEarningId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "name_abbr = '" + msNameAbbreviated + "', " +
                    "exem_mwz = " + mdExemptionMwz + ", " +
                    "exem_sal_equ_mwz_per = " + mdExemptionSalaryEqualsMwzPercentage + ", " +
                    "exem_sal_equ_mwz_lim = " + mdExemptionSalaryEqualsMwzLimit + ", " +
                    "exem_sal_grt_mwz_per = " + mdExemptionSalaryGreaterMwzPercentage + ", " +
                    "exem_sal_grt_mwz_lim = " + mdExemptionSalaryGreaterMwzLimit + ", " +
                    "exem_mwz_year = " + mdExemptionMwzYear + ", " +
                    "pay_per = " + mdPayPercentage + ", " +
                    "unt_max_wee = " + mdUnitsMaximumWeek + ", " +
                    "unt_fac = " + mdUnitsFactor + ", " +
                    "b_wel = " + (mbWelfare ? 1 : 0) + ", " +
                    "b_day_adj = " + (mbDaysAdjustment ? 1 : 0) + ", " +
                    "b_day_abs = " + (mbDaysAbsence ? 1 : 0) + ", " +
                    "b_day_wrk = " + (mbDaysWorked ? 1 : 0) + ", " +
                    "b_day_wrk_bas = " + (mbDaysWorkedBasedOn ? 1 : 0) + ", " +
                    "b_who = " + (mbWithholding ? 1 : 0) + ", " +
                    "b_alt_tax = " + (mbAlternativeTaxCalculation ? 1 : 0) + ", " +
                    "b_pay_tax = " + (mbPayrollTax ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_tp_ear = " + mnFkEarningTypeId + ", " +
                    "fk_tp_ear_comp = " + mnFkEarningComputationTypeId + ", " +
                    "fk_tp_ear_exem = " + mnFkEarningExemptionTypeId + ", " +
                    "fk_tp_ear_exem_year = " + mnFkEarningExemptionTypeYearId + ", " +
                    "fk_tp_oth_pay = " + mnFkOtherPaymentTypeId + ", " +
                    "fk_tp_loan = " + mnFkLoanTypeId + ", " +
                    "fk_tp_ben = " + mnFkBenefitTypeId + ", " +
                    "fk_tp_acc_cfg = " + mnFkAccountingConfigurationTypeId + ", " +
                    "fk_tp_acc_rec = " + mnFkAccountingRecordTypeId + ", " +
                    "fk_cl_abs_n = " + (mnFkAbsenceClassId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbsenceClassId_n) + ", " +
                    "fk_tp_abs_n = " + (mnFkAbsenceTypeId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbsenceTypeId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        SHrsUtils.createAccountingEarningConfiguration(session, mnPkEarningId, mnFkAccountingConfigurationTypeId, mnOldAccountingConfigurationTypeId);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEarning clone() throws CloneNotSupportedException {
        SDbEarning registry = new SDbEarning();

        registry.setPkEarningId(this.getPkEarningId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setNameAbbreviated(this.getNameAbbreviated());
        registry.setExemptionMwz(this.getExemptionMwz());
        registry.setExemptionSalaryEqualsMwzPercentage(this.getExemptionSalaryEqualsMwzPercentage());
        registry.setExemptionSalaryEqualsMwzLimit(this.getExemptionSalaryEqualsMwzLimit());
        registry.setExemptionSalaryGreaterMwzPercentage(this.getExemptionSalaryGreaterMwzPercentage());
        registry.setExemptionSalaryGreaterMwzLimit(this.getExemptionSalaryGreaterMwzLimit());
        registry.setExemptionMwzYear(this.getExemptionMwzYear());
        registry.setPayPercentage(this.getPayPercentage());
        registry.setUnitsMaximumWeek(this.getUnitsMaximumWeek());
        registry.setUnitsFactor(this.getUnitsFactor());
        registry.setWelfare(this.isWelfare());
        registry.setDaysAdjustment(this.isDaysAdjustment());
        registry.setDaysAbsence(this.isDaysAbsence());
        registry.setDaysWorked(this.isDaysWorked());
        registry.setDaysWorkedBasedOn(this.isDaysWorkedBasedOn());
        registry.setWithholding(this.isWithholding());
        registry.setAlternativeTaxCalculation(this.isAlternativeTaxCalculation());
        registry.setPayrollTax(this.isPayrollTax());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkEarningTypeId(this.getFkEarningTypeId());
        registry.setFkEarningComputationTypeId(this.getFkEarningComputationTypeId());
        registry.setFkEarningExemptionTypeId(this.getFkEarningExemptionTypeId());
        registry.setFkEarningExemptionTypeYearId(this.getFkEarningExemptionTypeYearId());
        registry.setFkOtherPaymentTypeId(this.getFkOtherPaymentTypeId());
        registry.setFkLoanTypeId(this.getFkLoanTypeId());
        registry.setFkBenefitTypeId(this.getFkBenefitTypeId());
        registry.setFkAccountingConfigurationTypeId(this.getFkAccountingConfigurationTypeId());
        registry.setFkAccountingRecordTypeId(this.getFkAccountingRecordTypeId());
        registry.setFkAbsenceClassId_n(this.getFkAbsenceClassId_n());
        registry.setFkAbsenceTypeId_n(this.getFkAbsenceTypeId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setOldAccountingConfigurationTypeId(this.getOldAccountingConfigurationTypeId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
