/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.utils.SAnniversary;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.joda.time.LocalDate;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mbps.data.SDataEmployee
 * All of them also make raw SQL queries, insertions or updates.
 */

/**
 * Used mainly in re-hiring and dismissal operations. The base class used in CRUD operations is SDataEmployee.
 * @author Juan Barajas, Edwin Carmona, Sergio Flores, Claudio Peña, Sergio Flores
 */
public class SDbEmployee extends SDbRegistryUser {
    
    public static final int FIELD_ACTIVE = FIELD_BASE + 1;
    public static final int FIELD_DATE_LAST_HIRE = FIELD_BASE + 2;
    public static final int FIELD_DATE_LAST_DISMISSAL = FIELD_BASE + 3;
    public static final int FIELD_ZIP_CODE = FIELD_BASE + 11;
    public static final int FIELD_BRANCH_HQ = FIELD_BASE + 12;

    protected int mnPkEmployeeId;
    protected String msNumber;
    protected String msLastname1;
    protected String msLastname2;
    protected String msZipCode;
    protected String msSocialSecurityNumber;
    protected Date mtDateBirth;
    protected Date mtDateBenefits;
    protected Date mtDateLastHire;
    protected Date mtDateLastDismissal_n;
    protected double mdSalary;
    protected double mdWage;
    protected double mdSalarySscBase;
    protected Date mtDateSalary;
    protected Date mtDateWage;
    protected Date mtDateSalarySscBase;
    protected int mnWorkingHoursDay;
    protected Date mtContractExpiration_n;
    protected int mnOvertimePolicy;
    protected int mnCheckerPolicy;
    protected String msBankAccount;
    protected String msGroceryServiceAccount;
    protected String msPlaceOfBirth;
    protected String msUmf;
    protected java.sql.Blob moImagePhoto_n;
    protected java.sql.Blob moImageSignature_n;
    protected boolean mbUnionized;
    protected boolean mbMfgOperator;
    protected boolean mbActive;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkPaymentTypeId;
    protected int mnFkSalaryTypeId;
    protected int mnFkEmployeeTypeId;
    protected int mnFkWorkerTypeId;
    protected int mnFkMwzTypeId;
    protected int mnFkDepartmentId;
    protected int mnFkPositionId;
    protected int mnFkShiftId;
    protected int mnFkContractTypeId;
    protected int mnFkRecruitmentSchemaTypeId;
    protected int mnFkPositionRiskTypeId;
    protected int mnFkWorkingDayTypeId;
    protected int mnFkCatalogueSexClassId;
    protected int mnFkCatalogueSexTypeId;
    protected int mnFkCatalogueBloodTypeClassId;
    protected int mnFkCatalogueBloodTypeTypeId;
    protected int mnFkCatalogueMaritalStatusClassId;
    protected int mnFkCatalogueMaritalStatusTypeId;
    protected int mnFkCatalogueEducationClassId;
    protected int mnFkCatalogueEducationTypeId;
    protected int mnFkSourceCompanyId;
    protected int mnFkBankId_n;
    protected int mnFkGroceryServiceId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected boolean mbActiveOld; // immutable member
    
    protected String msXtaEmployeeName;
    protected String msXtaEmployeeProperName;
    protected String msXtaEmployeeRfc;
    protected String msXtaEmployeeCurp;
    protected int mnXtaMembershipRecruitmentSchemaTypeId;
    /** Effective recruitment-schema category. Used in computing taxable earnings and withheld taxes in fiscal year. */
    protected int mnXtaEffectiveRecruitmentSchemaCat;
    
    protected Date mtAuxHireLogDate;
    protected String msAuxHireLogNotes;
    protected int mnAuxEmployeeDismissalTypeId;
    protected SHrsEmployeeHireLog moAuxHrsEmployeeHireLog;
    
    public SDbEmployee() {
        super(SModConsts.HRSU_EMP);
    }
    
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setNumber(String s) { msNumber = s; }
    public void setLastname1(String s) { msLastname1 = s; }
    public void setLastname2(String s) { msLastname2 = s; }
    public void setZipCode(String s) { msZipCode = s; }
    public void setSocialSecurityNumber(String s) { msSocialSecurityNumber = s; }
    public void setDateBirth(Date t) { mtDateBirth = t; }
    public void setDateBenefits(Date t) { mtDateBenefits = t; }
    public void setDateLastHire(Date t) { mtDateLastHire = t; }
    public void setDateLastDismissal_n(Date t) { mtDateLastDismissal_n = t; }
    public void setSalary(double d) { mdSalary = d; }
    public void setWage(double d) { mdWage = d; }
    public void setSalarySscBase(double d) { mdSalarySscBase = d; }
    public void setDateSalary(Date t) { mtDateSalary = t; }
    public void setDateWage(Date t) { mtDateWage = t; }
    public void setDateSalarySscBase(Date t) { mtDateSalarySscBase = t; }
    public void setWorkingHoursDay(int n) { mnWorkingHoursDay = n; }
    public void setContractExpiration_n(Date t) { mtContractExpiration_n = t; }
    public void setOvertimePolicy(int n) { mnOvertimePolicy = n; }
    public void setCheckerPolicy(int n) { mnCheckerPolicy = n; }
    public void setBankAccount(String s) { msBankAccount = s; }
    public void setGroceryServiceAccount(String s) { msGroceryServiceAccount = s; }
    public void setPlaceOfBirth (String s) { msPlaceOfBirth = s; }
    public void setUmf(String s) { msUmf= s; }
    public void setImagePhoto_n(java.sql.Blob o) { moImagePhoto_n = o; }
    public void setImageSignature_n(java.sql.Blob o) { moImageSignature_n = o; }
    public void setUnionized(boolean b) { mbUnionized = b; }
    public void setMfgOperator(boolean b) { mbMfgOperator = b; }
    public void setActive(boolean b) { mbActive = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkSalaryTypeId(int n) { mnFkSalaryTypeId = n; }
    public void setFkEmployeeTypeId(int n) { mnFkEmployeeTypeId = n; }
    public void setFkWorkerTypeId(int n) { mnFkWorkerTypeId = n; }
    public void setFkMwzTypeId(int n) { mnFkMwzTypeId = n; }
    public void setFkDepartmentId(int n) { mnFkDepartmentId = n; }
    public void setFkPositionId(int n) { mnFkPositionId = n; }
    public void setFkShiftId(int n) { mnFkShiftId = n; }
    public void setFkContractTypeId(int n) { mnFkContractTypeId = n; }
    public void setFkRecruitmentSchemaTypeId(int n) { mnFkRecruitmentSchemaTypeId = n; }
    public void setFkPositionRiskTypeId(int n) { mnFkPositionRiskTypeId = n; }
    public void setFkWorkingDayTypeId(int n) { mnFkWorkingDayTypeId = n; }
    public void setFkCatalogueSexClassId(int n) { mnFkCatalogueSexClassId = n; }
    public void setFkCatalogueSexTypeId(int n) { mnFkCatalogueSexTypeId = n; }
    public void setFkCatalogueBloodTypeClassId(int n) { mnFkCatalogueBloodTypeClassId = n; }
    public void setFkCatalogueBloodTypeTypeId(int n) { mnFkCatalogueBloodTypeTypeId = n; }
    public void setFkCatalogueMaritalStatusClassId(int n) { mnFkCatalogueMaritalStatusClassId = n; }
    public void setFkCatalogueMaritalStatusTypeId(int n) { mnFkCatalogueMaritalStatusTypeId = n; }
    public void setFkCatalogueEducationClassId(int n) { mnFkCatalogueEducationClassId = n; }
    public void setFkCatalogueEducationTypeId(int n) { mnFkCatalogueEducationTypeId = n; }
    public void setFkSourceCompanyId(int n) { mnFkSourceCompanyId = n; }
    public void setFkBankId_n(int n) { mnFkBankId_n = n; }
    public void setFkGroceryServiceId(int n) { mnFkGroceryServiceId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public String getNumber() { return msNumber; }
    public String getLastname1() { return msLastname1; }
    public String getLastname2() { return msLastname2; }
    public String getZipCode() { return msZipCode; }
    public String getSocialSecurityNumber() { return msSocialSecurityNumber; }
    public Date getDateBirth() { return mtDateBirth; }
    public Date getDateBenefits() { return mtDateBenefits; }
    public Date getDateLastHire() { return mtDateLastHire; }
    public Date getDateLastDismissal_n() { return mtDateLastDismissal_n; }
    public double getSalary() { return mdSalary; }
    public double getWage() { return mdWage; }
    public double getSalarySscBase() { return mdSalarySscBase; }
    public Date getDateSalary() { return mtDateSalary; }
    public Date getDateWage() { return mtDateWage; }
    public Date getDateSalarySscBase() { return mtDateSalarySscBase; }
    public int getWorkingHoursDay() { return mnWorkingHoursDay; }
    public Date getContractExpiration_n() { return mtContractExpiration_n; }
    public int getOvertimePolicy() { return mnOvertimePolicy; }
    public int getCheckerPolicy() { return mnCheckerPolicy; }
    public String getBankAccount() { return msBankAccount; }
    public String getGroceryServiceAccount() { return msGroceryServiceAccount; }
    public String getPlaceOfBirth() { return msPlaceOfBirth; }
    public String getUmf() { return msUmf; }
    public java.sql.Blob getImagePhoto_n() { return moImagePhoto_n; }
    public java.sql.Blob getImageSignature_n() { return moImageSignature_n; }
    public boolean isUnionized() { return mbUnionized; }
    public boolean isMfgOperator() { return mbMfgOperator; }
    public boolean isActive() { return mbActive; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkSalaryTypeId() { return mnFkSalaryTypeId; }
    public int getFkEmployeeTypeId() { return mnFkEmployeeTypeId; }
    public int getFkWorkerTypeId() { return mnFkWorkerTypeId; }
    public int getFkMwzTypeId() { return mnFkMwzTypeId; }
    public int getFkDepartmentId() { return mnFkDepartmentId; }
    public int getFkPositionId() { return mnFkPositionId; }
    public int getFkShiftId() { return mnFkShiftId; }
    public int getFkContractTypeId() { return mnFkContractTypeId; }
    public int getFkRecruitmentSchemaTypeId() { return mnFkRecruitmentSchemaTypeId; }
    public int getFkPositionRiskTypeId() { return mnFkPositionRiskTypeId; }
    public int getFkWorkingDayTypeId() { return mnFkWorkingDayTypeId; }
    public int getFkCatalogueSexClassId() { return mnFkCatalogueSexClassId; }
    public int getFkCatalogueSexTypeId() { return mnFkCatalogueSexTypeId; }
    public int getFkCatalogueBloodTypeClassId() { return mnFkCatalogueBloodTypeClassId; }
    public int getFkCatalogueBloodTypeTypeId() { return mnFkCatalogueBloodTypeTypeId; }
    public int getFkCatalogueMaritalStatusClassId() { return mnFkCatalogueMaritalStatusClassId; }
    public int getFkCatalogueMaritalStatusTypeId() { return mnFkCatalogueMaritalStatusTypeId; }
    public int getFkCatalogueEducationClassId() { return mnFkCatalogueEducationClassId; }
    public int getFkCatalogueEducationTypeId() { return mnFkCatalogueEducationTypeId; }
    public int getFkSourceCompanyId() { return mnFkSourceCompanyId; }
    public int getFkBankId_n() { return mnFkBankId_n; }
    public int getFkGroceryServiceId() { return mnFkGroceryServiceId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setXtaEmployeeName(String s) { msXtaEmployeeName = s; }
    public void setXtaEmployeePropername(String s) { msXtaEmployeeProperName = s; }
    public void setXtaEmployeeRfc(String s) { msXtaEmployeeRfc = s; }
    public void setXtaEmployeeCurp(String s) { msXtaEmployeeCurp = s; }
    public void setXtaMembershipRecruitmentSchemaTypeId(int n) { mnXtaMembershipRecruitmentSchemaTypeId = n; }
    /** Set effective recruitment-schema category. Used in computing taxable earnings and withheld taxes in fiscal year. */
    public void setXtaEffectiveRecruitmentSchemaCat(int n) { mnXtaEffectiveRecruitmentSchemaCat = n; }
    
    public String getXtaEmployeeName() { return msXtaEmployeeName; }
    public String getXtaEmployeeProperName() { return msXtaEmployeeProperName; }
    public String getXtaEmployeeRfc() { return msXtaEmployeeRfc; }
    public String getXtaEmployeeCurp() { return msXtaEmployeeCurp; }
    public int getXtaMembershipRecruitmentSchemaTypeId() { return mnXtaMembershipRecruitmentSchemaTypeId; }
    /** Get effective recruitment-schema category. Used in computing taxable earnings and withheld taxes in fiscal year. */
    public int getXtaEffectiveRecruitmentSchemaCat() { return mnXtaEffectiveRecruitmentSchemaCat; }
    
    public void setAuxHireLogDate(Date t) { mtAuxHireLogDate = t; }
    public void setAuxHireLogNotes(String s) { msAuxHireLogNotes = s; }
    public void setAuxEmployeeDismissalTypeId(int n) { mnAuxEmployeeDismissalTypeId = n; }
    public void setAuxHrsEmployeeHireLog(SHrsEmployeeHireLog o) { moAuxHrsEmployeeHireLog = o; }
    
    public Date getAuxHireLogDate() { return mtAuxHireLogDate; }
    public String getAuxHireLogNotes() { return msAuxHireLogNotes; }
    public int getAuxEmployeeDismissalTypeId() { return mnAuxEmployeeDismissalTypeId; }
    public SHrsEmployeeHireLog getAuxHrsEmployeeHireLog() { return moAuxHrsEmployeeHireLog; }
    
    /**
     * Get base calendar year of benefits.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @return 
     */
    public int getBenefitsYear() {
        return SLibTimeUtils.digestYear(mtDateBenefits)[0];
    }
    
    /**
     * Get calendar year for given anniversary.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @param anniversary Anniversary.
     * @return Calendar year for given anniversary.
     */
    public int getAnniversaryYear(final int anniversary) {
        return getBenefitsYear() + (anniversary - 1);
    }
    
    /**
     * Get anniversary date for given anniversary.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @param anniversary Anniversary.
     * @return Anniversary date for given anniversary.
     */
    public Date getAnniversaryDate(final int anniversary) {
        return new LocalDate(mtDateBenefits).plusYears(anniversary - 1).toDate();
    }
    
    /**
     * Create <code>SAnniversary</code> from date of benefits.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @param cutoff Cutoff date (e.g., today).
     * @return Composed lastname.
     */    
    public SAnniversary createAnniversary(final Date cutoff) {
        return new SAnniversary(mtDateBenefits, cutoff);
    }
    
    /**
     * Get effective salary.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @param isFortnightStandard Flag that indicates if fortnights are allways fixed to 15 days.
     * @return Effective salary.
     */
    public double getEffectiveSalary(boolean isFortnightStandard) {
        double effectiveSalary;
        
        if (mnFkPaymentTypeId == SModSysConsts.HRSS_TP_PAY_WEE) {
            effectiveSalary = mdSalary;
        }
        else {
            int yearDays = isFortnightStandard ? SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED : SHrsConsts.YEAR_DAYS;
            effectiveSalary = SLibUtils.roundAmount((mdWage * SHrsConsts.YEAR_MONTHS) / yearDays);
        }
        
        return effectiveSalary;
    }
    
    /**
     * Get settlement salary.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @return Settlement salary.
     */
    public double getSettlementSalary() {
        double settlementSalary;
        
        if (mnFkPaymentTypeId == SModSysConsts.HRSS_TP_PAY_WEE) {
            settlementSalary = mdSalary;
        }
        else {
            settlementSalary = SLibUtils.roundAmount(mdWage / SHrsConsts.MONTH_DAYS_FIXED);
        }
        
        return settlementSalary;
    }
    
    /**
     * Compose lastname.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @return Composed lastname.
     */    
    public String composeLastname() {
        return SLibUtils.textTrim(msLastname1 + (msLastname1.isEmpty() ? "" : " ") + msLastname2);
    }
    
    /**
     * Get effective type of recruitment schema.
     * If available, that one set in employee's membership, otherwise actual employee's type of recruitment schema.
     * Mirrored in erp.mbps.data.SDataEmployee.
     * @return Effective type of recruitment schema.
     */
    public int getEffectiveRecruitmentSchemaTypeId() {
        return mnXtaMembershipRecruitmentSchemaTypeId != 0 ? mnXtaMembershipRecruitmentSchemaTypeId : mnFkRecruitmentSchemaTypeId;
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkEmployeeId = 0;
        msNumber = "";
        msLastname1 = "";
        msLastname2 = "";
        msZipCode = "";
        msSocialSecurityNumber = "";
        mtDateBirth = null;
        mtDateBenefits = null;
        mtDateLastHire = null;
        mtDateLastDismissal_n = null;
        mdSalary = 0;
        mdWage = 0;
        mdSalarySscBase = 0;
        mtDateSalary = null;
        mtDateWage = null;
        mtDateSalarySscBase = null;
        mnWorkingHoursDay = 0;
        mtContractExpiration_n = null;
        mnOvertimePolicy = 0;
        mnCheckerPolicy = 0;
        msBankAccount = "";
        msGroceryServiceAccount = "";
        msPlaceOfBirth = "";
        msUmf = "";
        moImagePhoto_n = null;
        moImageSignature_n = null;
        mbUnionized = false;
        mbMfgOperator = false;
        mbActive = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkPaymentTypeId = 0;
        mnFkSalaryTypeId = 0;
        mnFkEmployeeTypeId = 0;
        mnFkWorkerTypeId = 0;
        mnFkMwzTypeId = 0;
        mnFkDepartmentId = 0;
        mnFkPositionId = 0;
        mnFkShiftId = 0;
        mnFkContractTypeId = 0;
        mnFkRecruitmentSchemaTypeId = 0;
        mnFkPositionRiskTypeId = 0;
        mnFkWorkingDayTypeId = 0;
        mnFkCatalogueSexClassId = 0;
        mnFkCatalogueSexTypeId = 0;
        mnFkCatalogueBloodTypeClassId = 0;
        mnFkCatalogueBloodTypeTypeId = 0;
        mnFkCatalogueMaritalStatusClassId = 0;
        mnFkCatalogueMaritalStatusTypeId = 0;
        mnFkCatalogueEducationClassId = 0;
        mnFkCatalogueEducationTypeId = 0;
        mnFkSourceCompanyId = 0;
        mnFkBankId_n = 0;
        mnFkGroceryServiceId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbActiveOld = false;
        
        msXtaEmployeeName = "";
        msXtaEmployeeProperName = "";
        msXtaEmployeeRfc = "";
        msXtaEmployeeCurp = "";
        mnXtaMembershipRecruitmentSchemaTypeId = 0;
        mnXtaEffectiveRecruitmentSchemaCat = 0;
        
        mtAuxHireLogDate = null;
        msAuxHireLogNotes = "";
        mnAuxEmployeeDismissalTypeId = 0;
        moAuxHrsEmployeeHireLog = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_emp = " + mnPkEmployeeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        Blob oPhoto_n;      // although not used, preserve variable
        Blob oSignature_n;  // although not used, preserve variable

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkEmployeeId = resultSet.getInt("id_emp");
            msNumber = resultSet.getString("num");
            msLastname1 = resultSet.getString("lastname1");
            msLastname2 = resultSet.getString("lastname2");
            msZipCode = resultSet.getString("zip_code");
            msSocialSecurityNumber = resultSet.getString("ssn");
            mtDateBirth = resultSet.getDate("dt_bir");
            mtDateBenefits = resultSet.getDate("dt_ben");
            mtDateLastHire = resultSet.getDate("dt_hire");
            mtDateLastDismissal_n = resultSet.getDate("dt_dis_n");
            mdSalary = resultSet.getDouble("sal");
            mdWage = resultSet.getDouble("wage");
            mdSalarySscBase = resultSet.getDouble("sal_ssc");
            mtDateSalary = resultSet.getDate("dt_sal");
            mtDateWage = resultSet.getDate("dt_wage");
            mtDateSalarySscBase = resultSet.getDate("dt_sal_ssc");
            mnWorkingHoursDay = resultSet.getInt("wrk_hrs_day");
            mtContractExpiration_n = resultSet.getDate("con_exp_n");
            mnOvertimePolicy = resultSet.getInt("overtime");
            mnCheckerPolicy = resultSet.getInt("checker_policy");
            msBankAccount = resultSet.getString("bank_acc");
            msGroceryServiceAccount = resultSet.getString("grocery_srv_acc");
            msPlaceOfBirth = resultSet.getString("place_bir");
            msUmf = resultSet.getString("umf");
            /*
            moImagePhoto_n = resultSet.getBlob("img_pho_n");
            moImageSignature_n = resultSet.getBlob("img_sig_n");
            */
            oPhoto_n = resultSet.getBlob("img_pho_n");
            oSignature_n = resultSet.getBlob("img_sig_n");
            mbUnionized = resultSet.getBoolean("b_uni");
            mbMfgOperator = resultSet.getBoolean("b_mfg_ope");
            mbActive = resultSet.getBoolean("b_act");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkPaymentTypeId = resultSet.getInt("fk_tp_pay");
            mnFkSalaryTypeId = resultSet.getInt("fk_tp_sal");
            mnFkEmployeeTypeId = resultSet.getInt("fk_tp_emp");
            mnFkWorkerTypeId = resultSet.getInt("fk_tp_wrk");
            mnFkMwzTypeId = resultSet.getInt("fk_tp_mwz");
            mnFkDepartmentId = resultSet.getInt("fk_dep");
            mnFkPositionId = resultSet.getInt("fk_pos");
            mnFkShiftId = resultSet.getInt("fk_sht");
            mnFkContractTypeId = resultSet.getInt("fk_tp_con");
            mnFkRecruitmentSchemaTypeId = resultSet.getInt("fk_tp_rec_sche");
            mnFkPositionRiskTypeId = resultSet.getInt("fk_tp_pos_risk");
            mnFkWorkingDayTypeId = resultSet.getInt("fk_tp_work_day");
            mnFkCatalogueSexClassId = resultSet.getInt("fk_cl_cat_sex");
            mnFkCatalogueSexTypeId = resultSet.getInt("fk_tp_cat_sex");
            mnFkCatalogueBloodTypeClassId = resultSet.getInt("fk_cl_cat_blo");
            mnFkCatalogueBloodTypeTypeId = resultSet.getInt("fk_tp_cat_blo");
            mnFkCatalogueMaritalStatusClassId = resultSet.getInt("fk_cl_cat_mar");
            mnFkCatalogueMaritalStatusTypeId = resultSet.getInt("fk_tp_cat_mar");
            mnFkCatalogueEducationClassId = resultSet.getInt("fk_cl_cat_edu");
            mnFkCatalogueEducationTypeId = resultSet.getInt("fk_tp_cat_edu");
            mnFkSourceCompanyId = resultSet.getInt("fk_src_com");
            mnFkBankId_n = resultSet.getInt("fk_bank_n");
            mnFkGroceryServiceId = resultSet.getInt("fk_grocery_srv");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbActiveOld = mbActive;
            
            // employee's names, fiscal and legal ID:
            msSql = "SELECT bp, lastname, firstname, fiscal_id, alt_id "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " "
                    + "WHERE id_bp = " + mnPkEmployeeId + ";";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n"
                        + "Nombre, RFC y CURP del empleado.");
            }
            else {
                msXtaEmployeeName = resultSet.getString("bp");
                msXtaEmployeeProperName = resultSet.getString("firstname") + " " + resultSet.getString("lastname");
                msXtaEmployeeRfc = resultSet.getString("fiscal_id");
                msXtaEmployeeCurp = resultSet.getString("alt_id");
            }
            
            // employee's membership type of recruitment schema:
            msSql = "SELECT fk_tp_rec_sche_n "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " "
                    + "WHERE id_emp = " + mnPkEmployeeId + ";";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                // do nothing; some long-ago dismissed employees do not have membership registry
            }
            else {
                mnXtaMembershipRecruitmentSchemaTypeId = resultSet.getInt("fk_tp_rec_sche_n");
            }
            
            // employee's effective category of recruitment schema:
            msSql = "SELECT rec_sche_cat "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " "
                    + "WHERE id_tp_rec_sche = " + getEffectiveRecruitmentSchemaTypeId() + ";";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n"
                        + "Tipo de régimen de contratación del empleado.");
            }
            else {
                mnXtaEffectiveRecruitmentSchemaCat = resultSet.getInt("rec_sche_cat");
            }
            
            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        PreparedStatement preparedStatementmagePhoto_n = null;      // although not used, preserve variable
        PreparedStatement preparedStatementmageSignature_n = null;  // although not used, preserve variable

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " + 
                    "'" + msNumber + "', " + 
                    "'" + msLastname1 + "', " + 
                    "'" + msLastname2 + "', " + 
                    "'" + msZipCode + "', " + 
                    "'" + msSocialSecurityNumber + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateBirth) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateBenefits) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " + 
                    (mtDateLastDismissal_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismissal_n) + "'") + ", " + 
                    mdSalary + ", " + 
                    mdWage + ", " + 
                    mdSalarySscBase + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSalary) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateWage) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSalarySscBase) + "', " + 
                    mnWorkingHoursDay + ", " + 
                    (mtContractExpiration_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtContractExpiration_n) + "'") + ", " + 
                    mnOvertimePolicy + ", " + 
                    mnCheckerPolicy + ", " + 
                    "'" + msBankAccount + "', " + 
                    "'" + msGroceryServiceAccount + "', " + 
                    "'" + msPlaceOfBirth + "', " + 
                    "'" + msUmf + "', " + 
                    "NULL, " +
                    "NULL, " +
                    (mbUnionized ? 1 : 0) + ", " + 
                    (mbMfgOperator ? 1 : 0) + ", " + 
                    (mbActive ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkPaymentTypeId + ", " + 
                    mnFkSalaryTypeId + ", " + 
                    mnFkEmployeeTypeId + ", " + 
                    mnFkWorkerTypeId + ", " + 
                    mnFkMwzTypeId + ", " + 
                    mnFkDepartmentId + ", " + 
                    mnFkPositionId + ", " + 
                    mnFkShiftId + ", " + 
                    mnFkContractTypeId + ", " + 
                    mnFkRecruitmentSchemaTypeId + ", " + 
                    mnFkPositionRiskTypeId + ", " + 
                    mnFkWorkingDayTypeId + ", " + 
                    mnFkCatalogueSexClassId + ", " + 
                    mnFkCatalogueSexTypeId + ", " + 
                    mnFkCatalogueBloodTypeClassId + ", " + 
                    mnFkCatalogueBloodTypeTypeId + ", " + 
                    mnFkCatalogueMaritalStatusClassId + ", " + 
                    mnFkCatalogueMaritalStatusTypeId + ", " + 
                    mnFkCatalogueEducationClassId + ", " + 
                    mnFkCatalogueEducationTypeId + ", " + 
                    mnFkSourceCompanyId + ", " + 
                    (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
                    mnFkGroceryServiceId + ", " + 
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
                    "id_emp = " + mnPkEmployeeId + ", " +
                    */                    
                    "num = '" + msNumber + "', " +
                    "lastname1 = '" + msLastname1 + "', " +
                    "lastname2 = '" + msLastname2 + "', " +
                    "zip_code = '" + msZipCode + "', " +
                    "ssn = '" + msSocialSecurityNumber + "', " +
                    "dt_bir = '" + SLibUtils.DbmsDateFormatDate.format(mtDateBirth) + "', " +
                    "dt_ben = '" + SLibUtils.DbmsDateFormatDate.format(mtDateBenefits) + "', " +
                    "dt_hire = '" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " +
                    "dt_dis_n = " + (mtDateLastDismissal_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismissal_n) + "'") + ", " +
                    "sal = " + mdSalary + ", " +
                    "wage = " + mdWage + ", " +
                    "sal_ssc = " + mdSalarySscBase + ", " +
                    "dt_sal = '" + SLibUtils.DbmsDateFormatDate.format(mtDateSalary) + "', " +
                    "dt_wage = '" + SLibUtils.DbmsDateFormatDate.format(mtDateWage) + "', " +
                    "dt_sal_ssc = '" + SLibUtils.DbmsDateFormatDate.format(mtDateSalarySscBase) + "', " +
                    "wrk_hrs_day = " + mnWorkingHoursDay + ", " +
                    "con_exp_n = " + (mtContractExpiration_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtContractExpiration_n) + "'") + ", " +
                    "overtime = " + mnOvertimePolicy + ", " +
                    "checker_policy = " + mnCheckerPolicy + ", " +
                    "bank_acc = '" + msBankAccount + "', " +
                    "grocery_srv_acc = '" + msGroceryServiceAccount + "', " +
                    "place_bir = '" + msPlaceOfBirth + "', " +
                    "umf = '" + msUmf + "', " +
                    /*
                    "img_pho_n = " + moImagePhoto_n + ", " +
                    "img_sig_n = " + moImageSignature_n + ", " +
                    */
                    "b_uni = " + (mbUnionized ? 1 : 0) + ", " +
                    "b_mfg_ope = " + (mbMfgOperator ? 1 : 0) + ", " +
                    "b_act = " + (mbActive ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_tp_pay = " + mnFkPaymentTypeId + ", " +
                    "fk_tp_sal = " + mnFkSalaryTypeId + ", " +
                    "fk_tp_emp = " + mnFkEmployeeTypeId + ", " +
                    "fk_tp_wrk = " + mnFkWorkerTypeId + ", " +
                    "fk_tp_mwz = " + mnFkMwzTypeId + ", " +
                    "fk_dep = " + mnFkDepartmentId + ", " +
                    "fk_pos = " + mnFkPositionId + ", " +
                    "fk_sht = " + mnFkShiftId + ", " +
                    "fk_tp_con = " + mnFkContractTypeId + ", " +
                    "fk_tp_rec_sche = " + mnFkRecruitmentSchemaTypeId + ", " +
                    "fk_tp_pos_risk = " + mnFkPositionRiskTypeId + ", " +
                    "fk_tp_work_day = " + mnFkWorkingDayTypeId + ", " +
                    "fk_cl_cat_sex = " + mnFkCatalogueSexClassId + ", " +
                    "fk_tp_cat_sex = " + mnFkCatalogueSexTypeId + ", " +
                    "fk_cl_cat_blo = " + mnFkCatalogueBloodTypeClassId + ", " +
                    "fk_tp_cat_blo = " + mnFkCatalogueBloodTypeTypeId + ", " +
                    "fk_cl_cat_mar = " + mnFkCatalogueMaritalStatusClassId + ", " +
                    "fk_tp_cat_mar = " + mnFkCatalogueMaritalStatusTypeId + ", " +
                    "fk_cl_cat_edu = " + mnFkCatalogueEducationClassId + ", " +
                    "fk_tp_cat_edu = " + mnFkCatalogueEducationTypeId + ", " +
                    "fk_src_com = " + mnFkSourceCompanyId + ", " +
                    "fk_bank_n = " + (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
                    "fk_grocery_srv = " + mnFkGroceryServiceId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // process hiring or dismissing:

        if (mbRegistryNew || mbActive != mbActiveOld) {
            SHrsEmployeeHireLog hrsEmployeeHireLog = moAuxHrsEmployeeHireLog != null ? moAuxHrsEmployeeHireLog : new SHrsEmployeeHireLog(session); // spreads log entries to all sibling companies
                
            hrsEmployeeHireLog.setPkEmployeeId(mnPkEmployeeId);
            
            if (mbActive) {
                hrsEmployeeHireLog.setIsHire(true);
                hrsEmployeeHireLog.setLastHireDate(mtAuxHireLogDate);
                hrsEmployeeHireLog.setLastHireNotes(msAuxHireLogNotes);
                hrsEmployeeHireLog.setFkEmployeeDismissalTypeId(SModSysConsts.HRSU_TP_EMP_DIS_NA);
                hrsEmployeeHireLog.setFkRecruitmentSchemaTypeId(mnFkRecruitmentSchemaTypeId); // recruitment schema type set only when hiring
            }
            else {
                hrsEmployeeHireLog.setIsHire(false);
                hrsEmployeeHireLog.setLastDismissalDate_n(mtAuxHireLogDate);
                hrsEmployeeHireLog.setLastDismissalNotes(msAuxHireLogNotes);
                hrsEmployeeHireLog.setFkEmployeeDismissalTypeId(mnAuxEmployeeDismissalTypeId);
                //hrsEmployeeHireLog.setFkRecruitmentSchemaTypeId(...); // recruitment schema type set only when hiring
            }
            
            hrsEmployeeHireLog.setDeleted(mbDeleted);
            
            if (mbRegistryNew) {
                hrsEmployeeHireLog.setFkUserInsertId(mnFkUserInsertId);
                hrsEmployeeHireLog.setFkUserUpdateId(SUtilConsts.USR_NA_ID);
            }
            else {
                if (hrsEmployeeHireLog.getFkUserInsertId() == 0) {
                    hrsEmployeeHireLog.setFkUserInsertId(mnFkUserUpdateId);
                }
                hrsEmployeeHireLog.setFkUserUpdateId(mnFkUserUpdateId);
            }
            
            hrsEmployeeHireLog.setRequestSettings(mbRegistryNew);
            hrsEmployeeHireLog.processRequest();
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEmployee clone() throws CloneNotSupportedException {
        SDbEmployee registry = new SDbEmployee();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setNumber(this.getNumber());
        registry.setLastname1(this.getLastname1());
        registry.setLastname2(this.getLastname2());
        registry.setZipCode(this.getZipCode());
        registry.setSocialSecurityNumber(this.getSocialSecurityNumber());
        registry.setDateBirth(this.getDateBirth());
        registry.setDateBenefits(this.getDateBenefits());
        registry.setDateLastHire(this.getDateLastHire());
        registry.setDateLastDismissal_n(this.getDateLastDismissal_n());
        registry.setSalary(this.getSalary());
        registry.setWage(this.getWage());
        registry.setSalarySscBase(this.getSalarySscBase());
        registry.setDateSalary(this.getDateSalary());
        registry.setDateWage(this.getDateWage());
        registry.setDateSalarySscBase(this.getDateSalarySscBase());
        registry.setWorkingHoursDay(this.getWorkingHoursDay());
        registry.setContractExpiration_n(this.getContractExpiration_n());
        registry.setOvertimePolicy(this.getOvertimePolicy());
        registry.setCheckerPolicy(this.getCheckerPolicy());
        registry.setBankAccount(this.getBankAccount());
        registry.setGroceryServiceAccount(this.getGroceryServiceAccount());
        registry.setPlaceOfBirth(this.getPlaceOfBirth());
        registry.setUmf(this.getUmf());
        registry.setImagePhoto_n(this.getImagePhoto_n());
        registry.setImageSignature_n(this.getImageSignature_n());
        registry.setUnionized(this.isUnionized());
        registry.setMfgOperator(this.isMfgOperator());
        registry.setActive(this.isActive());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkPaymentTypeId(this.getFkPaymentTypeId());
        registry.setFkSalaryTypeId(this.getFkSalaryTypeId());
        registry.setFkEmployeeTypeId(this.getFkEmployeeTypeId());
        registry.setFkWorkerTypeId(this.getFkWorkerTypeId());
        registry.setFkMwzTypeId(this.getFkMwzTypeId());
        registry.setFkDepartmentId(this.getFkDepartmentId());
        registry.setFkPositionId(this.getFkPositionId());
        registry.setFkShiftId(this.getFkShiftId());
        registry.setFkContractTypeId(this.getFkContractTypeId());
        registry.setFkRecruitmentSchemaTypeId(this.getFkRecruitmentSchemaTypeId());
        registry.setFkPositionRiskTypeId(this.getFkPositionRiskTypeId());
        registry.setFkWorkingDayTypeId(this.getFkWorkingDayTypeId());
        registry.setFkCatalogueSexClassId(this.getFkCatalogueSexClassId());
        registry.setFkCatalogueSexTypeId(this.getFkCatalogueSexTypeId());
        registry.setFkCatalogueBloodTypeClassId(this.getFkCatalogueBloodTypeClassId());
        registry.setFkCatalogueBloodTypeTypeId(this.getFkCatalogueBloodTypeTypeId());
        registry.setFkCatalogueMaritalStatusClassId(this.getFkCatalogueMaritalStatusClassId());
        registry.setFkCatalogueMaritalStatusTypeId(this.getFkCatalogueMaritalStatusTypeId());
        registry.setFkCatalogueEducationClassId(this.getFkCatalogueEducationClassId());
        registry.setFkCatalogueEducationTypeId(this.getFkCatalogueEducationTypeId());
        registry.setFkSourceCompanyId(this.getFkSourceCompanyId());
        registry.setFkBankId_n(this.getFkBankId_n());
        registry.setFkGroceryServiceId(this.getFkGroceryServiceId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.mbActiveOld = this.mbActiveOld;
        
        registry.setXtaEmployeeName(this.getXtaEmployeeName());
        registry.setXtaEmployeePropername(this.getXtaEmployeeProperName());
        registry.setXtaEmployeeRfc(this.getXtaEmployeeRfc());
        registry.setXtaEmployeeCurp(this.getXtaEmployeeCurp());
        registry.setXtaMembershipRecruitmentSchemaTypeId(this.getXtaMembershipRecruitmentSchemaTypeId());
        registry.setXtaEffectiveRecruitmentSchemaCat(this.getXtaEffectiveRecruitmentSchemaCat());

        registry.setAuxHireLogDate(this.getAuxHireLogDate());
        registry.setAuxHireLogNotes(this.getAuxHireLogNotes());
        registry.setAuxEmployeeDismissalTypeId(this.getAuxEmployeeDismissalTypeId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case FIELD_NAME:
                msSql += "bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " WHERE id_bp = " + pk[0] + ";";
                break;
            case FIELD_CODE:
                msSql += "num FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " WHERE id_emp = " + pk[0] + ";";
                break;
            case FIELD_ZIP_CODE:
                msSql += "zip_code FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " WHERE id_emp = " + pk[0] + ";";
                break;
            case FIELD_BRANCH_HQ:
                msSql += "id_bpb FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " WHERE fid_bp = " + pk[0] + " AND fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + ";";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }


        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case FIELD_NAME:
                case FIELD_CODE:
                case FIELD_ZIP_CODE:
                    value = resultSet.getString(1);
                    break;
                case FIELD_BRANCH_HQ:
                    value = resultSet.getInt(1);
                    break;
                default:
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
        return value;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_ACTIVE:
                msSql += "b_act = " + (boolean) value + " ";
                break;
            case FIELD_DATE_LAST_HIRE:
                msSql += "dt_hire = '" + SLibUtils.DbmsDateFormatDate.format((Date) value) + "' ";
                break;
            case FIELD_DATE_LAST_DISMISSAL:
                msSql += "dt_dis_n = " + (value == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format((Date) value) + "'") + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);

        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
