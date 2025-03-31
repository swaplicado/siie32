/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mhrs.data.SDataEmployeeRelatives;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsEmployeeHireLog;
import erp.mod.hrs.utils.SAnniversary;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.imageio.ImageIO;
import org.joda.time.LocalDate;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mod.hrs.db.SDbEmployee
 * All of them also make raw SQL queries, insertions or updates.
 */

/**
 * Used mainly in CRUD operations on employees.
 * @author Juan Barajas, Edwin Carmona, Sergio Flores, Sergio Flores, Claudio Peña
 */
public class SDataEmployee extends erp.lib.data.SDataRegistry implements java.io.Serializable {

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
    protected Date mtDatePosition;
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
    protected boolean mbDeleted;
    protected boolean mbSystem;
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
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;

    protected SDataEmployeeRelatives moChildRelatives;
    
    protected boolean mbActiveOld; // immutable member
    
    protected double mdAuxNewSalary;
    protected double mdAuxNewWage;
    protected double mdAuxNewSalarySscBase;
    protected double mdAuxNewDepartamenId;
    protected double mdAuxNewPositionId;
    protected Date mtAuxNewDateSalary;
    protected Date mtAuxNewDateWage;
    protected Date mtAuxNewDateSalarySscBase;
    protected Date mtAuxNewDatePosition;

    protected javax.swing.ImageIcon moXtaImageIconPhoto_n;
    protected javax.swing.ImageIcon moXtaImageIconSignature_n;
    
    private void createWageLog(Connection connection, Date date) throws Exception {
        try (Statement statement = connection.createStatement()) {
            int logId = 0;
            
            String sql = "SELECT COALESCE(MAX(id_log), 0) + 1 "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_WAGE) + " "
                    + "WHERE id_emp = " + mnPkEmployeeId + ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                logId = resultSet.getInt(1);
            }
            
            sql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_WAGE) + " VALUES (" + 
                    mnPkEmployeeId + ", " +
                    logId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(date) + "', " +
                    mdSalary + ", " +
                    mdWage + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkPaymentTypeId + ", " +
                    mnFkSalaryTypeId + ", " +
                    mnFkEmployeeTypeId + ", " +
                    mnFkWorkerTypeId + ", " +
                    mnFkMwzTypeId + ", " +
                    mnFkDepartmentId + ", " +
                    mnFkPositionId + ", " +
                    mnFkShiftId + ", " +
                    mnFkContractTypeId+ ", " +
                    mnFkRecruitmentSchemaTypeId + ", " +
                    mnFkPositionRiskTypeId + ", " +
                    mnFkWorkingDayTypeId + ", " +
                    (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ");";
            
            statement.execute(sql);
        }
    }

    private void createSalarySscBaseLog(Connection connection, Date date) throws Exception {
        try (Statement statement = connection.createStatement()) {
            int logId = 0;
            
            String sql = "SELECT COALESCE(MAX(id_log), 0) + 1 "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " "
                    + "WHERE id_emp = " + mnPkEmployeeId + ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                logId = resultSet.getInt(1);
            }
            
            sql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " VALUES (" + 
                    mnPkEmployeeId + ", " +
                    logId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(date) + "', " +
                    mdSalarySscBase + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ");";
            
            statement.execute(sql);
        }
    }
                                                         
    private void createChangePositionLog(Connection connection, Date date) throws Exception {
        try (Statement statement = connection.createStatement()) {
            int logId = 0;
            
            String sql = "SELECT COALESCE(MAX(id_log), 0) + 1 "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_DEP_POS) + " "
                    + "WHERE id_emp = " + mnPkEmployeeId + ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                logId = resultSet.getInt(1);
            }
            
            sql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_DEP_POS) + " VALUES (" + 
                    mnPkEmployeeId + ", " +
                    logId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(date) + "', " +                    
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkDepartmentId + ", " +
                    mnFkPositionId + ", " +
                    (mbIsRegistryNew ? mnFkUserInsertId : mnFkUserUpdateId) + ", " +
                    SUtilConsts.USR_NA_ID + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ");";
            
            statement.execute(sql);
        }
    }

    public SDataEmployee() {
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
    public void setDatePosition(Date t) { mtDatePosition = t; }
    public void setWorkingHoursDay(int n) { mnWorkingHoursDay = n; }
    public void setContractExpiration_n(Date t) { mtContractExpiration_n = t; }
    public void setOvertimePolicy(int n) { mnOvertimePolicy = n; }
    public void setCheckerPolicy(int n) { mnCheckerPolicy = n; }
    public void setBankAccount(String s) { msBankAccount = s; }
    public void setGroceryServiceAccount(String s) { msGroceryServiceAccount = s; }
    public void setPlaceOfBirth(String s) { msPlaceOfBirth = s; }
    public void setUmf(String s) { msUmf = s; }
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

    public void setChildRelatives(SDataEmployeeRelatives o) { moChildRelatives = o; }
    
    public void setAuxSalary(double d) { mdAuxNewSalary = d; }
    public void setAuxWage(double d) { mdAuxNewWage = d; }
    public void setAuxSalarySscBase(double d) { mdAuxNewSalarySscBase = d; }
    public void setAuxNewDepartamentId(double d) { mdAuxNewDepartamenId = d; }
    public void setAuxNewPositionId(double d) { mdAuxNewPositionId = d; }
    public void setAuxDateSalary(Date t) { mtAuxNewDateSalary = t; }
    public void setAuxDateWage(Date t) { mtAuxNewDateWage = t; }
    public void setAuxDateSalarySscBase(Date t) { mtAuxNewDateSalarySscBase = t; }
    public void setAuxNewDatePosition(Date t) { mtAuxNewDatePosition = t; }
    
    public void setXtaImageIconPhoto_n(javax.swing.ImageIcon o) { moXtaImageIconPhoto_n = o; }
    public void setXtaImageIconSignature_n(javax.swing.ImageIcon o) { moXtaImageIconSignature_n = o; }

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
    public Date getDatePosition() { return mtDatePosition; }
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

    public SDataEmployeeRelatives getChildRelatives() { return moChildRelatives; }
    
    public double getAuxSalary() { return mdAuxNewSalary; }
    public double getAuxWage() { return mdAuxNewWage; }
    public double getAuxNewDepartamentId() { return mdAuxNewDepartamenId; }
    public double getAuxNewPositionId() { return mdAuxNewPositionId; }
    public Date getAuxDateSalary() { return mtAuxNewDateSalary; }
    public Date getAuxDateWage() { return mtAuxNewDateWage; }
    public Date getAuxDateSalarySscBase() { return mtAuxNewDateSalarySscBase; }
    public Date getAuxNewDatePosition() { return mtAuxNewDatePosition; }
    
    public javax.swing.ImageIcon getXtaImageIconPhoto_n() { return moXtaImageIconPhoto_n; }
    public javax.swing.ImageIcon getXtaImageIconSignature_n() { return moXtaImageIconSignature_n; }
    
    /**
     * Get base calendar year of benefits.
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
     * @return 
     */
    public int getBenefitsYear() {
        return SLibTimeUtils.digestYear(mtDateBenefits)[0];
    }
    
    /**
     * Get calendar year for given anniversary.
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
     * @param anniversary Anniversary.
     * @return Calendar year for given anniversary.
     */
    public int getAnniversaryYear(final int anniversary) {
        return getBenefitsYear() + (anniversary - 1);
    }
    
    /**
     * Get anniversary date for given anniversary.
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
     * @param anniversary Anniversary.
     * @return Anniversary date for given anniversary.
     */
    public Date getAnniversaryDate(final int anniversary) {
        return new LocalDate(mtDateBenefits).plusYears(anniversary - 1).toDate();
    }
    
    /**
     * Create <code>SAnniversary</code> from date of benefits.
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
     * @param cutoff Cutoff date (e.g., today).
     * @return Composed lastname.
     */    
    public SAnniversary createAnniversary(final Date cutoff) {
        return new SAnniversary(mtDateBenefits, cutoff);
    }
    
    /**
     * Get effective salary.
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
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
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
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
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
     * @return Composed lastname.
     */    
    public String composeLastname() {
        return SLibUtils.textTrim(msLastname1 + (msLastname1.isEmpty() ? "" : " ") + msLastname2);
    }

    /**
     * Get effective type of recruitment schema.
     * If available, that one set in employee's membership, otherwise actual employee's type of recruitment schema.
     * Mirrored in erp.mod.hrs.db.SDbEmployee.
     * @return Effective type of recruitment schema.
     */
    public int getEffectiveRecruitmentSchemaTypeId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkEmployeeId = ((int[]) pk)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

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
        mtDatePosition = null;
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
        
        moChildRelatives = null;
        
        mbActiveOld = false;

        mdAuxNewSalary = 0;
        mdAuxNewWage = 0;
        mdAuxNewSalarySscBase = 0;
        mdAuxNewDepartamenId = 0;
        mdAuxNewPositionId = 0;
        mtAuxNewDateSalary = null;
        mtAuxNewDateWage = null;
        mtAuxNewDateSalarySscBase = null;
        mtAuxNewDatePosition = null;
        
        moXtaImageIconPhoto_n = null;
        moXtaImageIconSignature_n = null;
    }

    @Override
    public int read(Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Blob oPhoto_n;
        Blob oSignature_n;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.hrsu_emp WHERE id_emp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
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
                mtDatePosition = resultSet.getDate("dt_pos");
                mnWorkingHoursDay = resultSet.getInt("wrk_hrs_day");
                mtContractExpiration_n = resultSet.getDate("con_exp_n");
                mnOvertimePolicy = resultSet.getInt("overtime");
                mnCheckerPolicy = resultSet.getInt("checker_policy");
                msBankAccount = resultSet.getString("bank_acc");
                msGroceryServiceAccount = resultSet.getString("grocery_srv_acc");
                msPlaceOfBirth= resultSet.getString("place_bir");
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

                if (oPhoto_n != null) {
                    moXtaImageIconPhoto_n = SLibUtilities.convertBlobToImageIcon(oPhoto_n);
                }
                if (oSignature_n != null) {
                    moXtaImageIconSignature_n = SLibUtilities.convertBlobToImageIcon(oSignature_n);
                }
                
                sql = "SELECT * FROM erp.hrsu_emp_rel WHERE id_emp = " + key[0] + ";";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moChildRelatives = new SDataEmployeeRelatives();
                    moChildRelatives.setIsRegistryNew(false);
                    moChildRelatives.setPkEmployeeId(resultSet.getInt("id_emp"));
                    moChildRelatives.setMate(resultSet.getString("mate"));
                    moChildRelatives.setMateDateBirth(resultSet.getDate("mate_dt_bir_n"));
                    moChildRelatives.setMateDeceased(resultSet.getBoolean("b_mate_dec"));
                    moChildRelatives.setSon1(resultSet.getString("son_1"));
                    moChildRelatives.setSonDateBirth1(resultSet.getDate("son_dt_bir_1_n"));
                    moChildRelatives.setSonDeceased1(resultSet.getBoolean("b_son_dec_1"));
                    moChildRelatives.setSon2(resultSet.getString("son_2"));
                    moChildRelatives.setSonDateBirth2(resultSet.getDate("son_dt_bir_2_n"));
                    moChildRelatives.setSonDeceased2(resultSet.getBoolean("b_son_dec_2"));
                    moChildRelatives.setSon3(resultSet.getString("son_3"));
                    moChildRelatives.setSonDateBirth3(resultSet.getDate("son_dt_bir_3_n"));
                    moChildRelatives.setSonDeceased3(resultSet.getBoolean("b_son_dec_3"));
                    moChildRelatives.setSon4(resultSet.getString("son_4"));
                    moChildRelatives.setSonDateBirth4(resultSet.getDate("son_dt_bir_4_n"));
                    moChildRelatives.setSonDeceased4(resultSet.getBoolean("b_son_dec_4"));
                    moChildRelatives.setSon5(resultSet.getString("son_5"));
                    moChildRelatives.setSonDateBirth5(resultSet.getDate("son_dt_bir_5_n"));
                    moChildRelatives.setSonDeceased5(resultSet.getBoolean("b_son_dec_5"));
                    moChildRelatives.setEmergenciesContact(resultSet.getString("emergs_con"));
                    moChildRelatives.setEmergenciesTelNumber(resultSet.getString("emergs_tel_num"));
                    moChildRelatives.setBeneficiaries(resultSet.getString("benefs"));
                    moChildRelatives.setFkCatSexMateClassId(resultSet.getInt("fk_cl_cat_sex_mate"));
                    moChildRelatives.setFkCatSexMateTypeId(resultSet.getInt("fk_tp_cat_sex_mate"));
                    moChildRelatives.setFkCatSexSon1ClassId(resultSet.getInt("fk_cl_cat_sex_son_1"));
                    moChildRelatives.setFkCatSexSon1TypeId(resultSet.getInt("fk_tp_cat_sex_son_1"));
                    moChildRelatives.setFkCatSexSon2ClassId(resultSet.getInt("fk_cl_cat_sex_son_2"));
                    moChildRelatives.setFkCatSexSon2TypeId(resultSet.getInt("fk_tp_cat_sex_son_2"));
                    moChildRelatives.setFkCatSexSon3ClassId(resultSet.getInt("fk_cl_cat_sex_son_3"));
                    moChildRelatives.setFkCatSexSon3TypeId(resultSet.getInt("fk_tp_cat_sex_son_3"));
                    moChildRelatives.setFkCatSexSon4ClassId(resultSet.getInt("fk_cl_cat_sex_son_4"));
                    moChildRelatives.setFkCatSexSon4TypeId(resultSet.getInt("fk_tp_cat_sex_son_4"));
                    moChildRelatives.setFkCatSexSon5ClassId(resultSet.getInt("fk_cl_cat_sex_son_5"));
                    moChildRelatives.setFkCatSexSon5TypeId(resultSet.getInt("fk_tp_cat_sex_son_5"));
                    moChildRelatives.setFkCatKinshipEmergenciesClassId(resultSet.getInt("fk_cl_cat_kin_emergs"));
                    moChildRelatives.setFkCatKinshipEmergenciesTypeId(resultSet.getInt("fk_tp_cat_kin_emergs"));
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
        String sql = "";
        ResultSet resultSet = null;
        PreparedStatement preparedStatementmagePhoto_n = null;
        PreparedStatement preparedStatementmageSignature_n = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM erp.hrsu_emp WHERE id_emp = " + mnPkEmployeeId + " ";
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                mbDeleted = false;
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
                
                sql = "INSERT INTO erp.hrsu_emp VALUES (" +
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
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " + 
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
                sql = "UPDATE erp.hrsu_emp SET " +
                        //"id_emp = " + mnPkEmployeeId + ", " +
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
                        "dt_pos = '" + SLibUtils.DbmsDateFormatDate.format(mtDatePosition) + "', " + 
                        "wrk_hrs_day = " + mnWorkingHoursDay + ", " +
                        "con_exp_n = " + (mtContractExpiration_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtContractExpiration_n) + "'") + ", " +
                        "overtime = " + mnOvertimePolicy + ", " +
                        "checker_policy = " + mnCheckerPolicy + ", " +
                        "bank_acc = '" + msBankAccount + "', " +
                        "grocery_srv_acc = '" + msGroceryServiceAccount + "', " +
                        "place_bir = '" + msPlaceOfBirth + "', " +
                        "umf = '" + msUmf + "', " +
                        /*
                        "img_pho_n = " + (moImagePhoto_n == null ? null : moImagePhoto_n) + ", " +
                        "img_sig_n = " + (moImageSignature_n == null ? null : moImageSignature_n) + ", " +
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
                        "WHERE id_emp = " + mnPkEmployeeId + " ";
            }

            connection.createStatement().execute(sql);

            if (moXtaImageIconPhoto_n == null) {
                sql = "UPDATE erp.hrsu_emp SET img_pho_n = NULL WHERE id_emp = " + mnPkEmployeeId + " ";
                connection.createStatement().execute(sql);
            }
            else {
                sql = "UPDATE erp.hrsu_emp SET img_pho_n = ? WHERE id_emp = " + mnPkEmployeeId + " ";

                preparedStatementmagePhoto_n = connection.prepareStatement(sql);
                ByteArrayOutputStream byteArrayOSImagePhoto = null;
                GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
                BufferedImage img = gc.createCompatibleImage(moXtaImageIconPhoto_n.getImage().getWidth(null), moXtaImageIconPhoto_n.getImage().getHeight(null), Transparency.TRANSLUCENT);

                Graphics g = img.createGraphics();
                g.drawImage(moXtaImageIconPhoto_n.getImage(), 0, 0, null);
                g.dispose();

                byteArrayOSImagePhoto = new ByteArrayOutputStream(1024 * 1024);
                ImageIO.write(img, "jpg", byteArrayOSImagePhoto);

                preparedStatementmagePhoto_n.setBytes(1, byteArrayOSImagePhoto.toByteArray());
                preparedStatementmagePhoto_n.execute();
            }
            
            if (moXtaImageIconSignature_n == null) {
                sql = "UPDATE erp.hrsu_emp SET img_sig_n = NULL WHERE id_emp = " + mnPkEmployeeId + " ";
                connection.createStatement().execute(sql);
            }
            else {
                sql = "UPDATE erp.hrsu_emp SET img_sig_n = ? WHERE id_emp = " + mnPkEmployeeId + " ";

                preparedStatementmageSignature_n = connection.prepareStatement(sql);
                ByteArrayOutputStream byteArrayOSImageSignature = null;
                GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
                BufferedImage img = gc.createCompatibleImage(moXtaImageIconSignature_n.getImage().getWidth(null), moXtaImageIconSignature_n.getImage().getHeight(null), Transparency.TRANSLUCENT);

                Graphics g = img.createGraphics();
                g.drawImage(moXtaImageIconSignature_n.getImage(), 0, 0, null);
                g.dispose();

                byteArrayOSImageSignature = new ByteArrayOutputStream(1024 * 1024);
                ImageIO.write(img, "jpg", byteArrayOSImageSignature);

                preparedStatementmageSignature_n.setBytes(1, byteArrayOSImageSignature.toByteArray());
                preparedStatementmageSignature_n.execute();
            }
            
            Statement statement = connection.createStatement();
            
            sql = "DELETE FROM erp.hrsu_emp_rel WHERE id_emp = " + mnPkEmployeeId + ";";
            statement.execute(sql);
            
            if (moChildRelatives != null) {
                moChildRelatives.setPkEmployeeId(mnPkEmployeeId);
                
                sql = "INSERT INTO erp.hrsu_emp_rel VALUES (" +
                        moChildRelatives.getPkEmployeeId() + ", " + 
                        "'" + moChildRelatives.getMate() + "', " + 
                        (moChildRelatives.getMateDateBirth() == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(moChildRelatives.getMateDateBirth()) + "'") + ", " + 
                        (moChildRelatives.isMateDeceased() ? 1 : 0) + ", " + 
                        "'" + moChildRelatives.getSon1() + "', " + 
                        (moChildRelatives.getSonDateBirth1() == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(moChildRelatives.getSonDateBirth1()) + "'") + ", " +
                        (moChildRelatives.isSonDeceased1() ? 1 : 0) + ", " + 
                        "'" + moChildRelatives.getSon2() + "', " + 
                        (moChildRelatives.getSonDateBirth2() == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(moChildRelatives.getSonDateBirth2()) + "'") + ", " +
                        (moChildRelatives.isSonDeceased2() ? 1 : 0) + ", " + 
                        "'" + moChildRelatives.getSon3() + "', " + 
                        (moChildRelatives.getSonDateBirth3() == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(moChildRelatives.getSonDateBirth3()) + "'") + ", " +
                        (moChildRelatives.isSonDeceased3() ? 1 : 0) + ", " + 
                        "'" + moChildRelatives.getSon4() + "', " + 
                        (moChildRelatives.getSonDateBirth4() == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(moChildRelatives.getSonDateBirth4()) + "'") + ", " +
                        (moChildRelatives.isSonDeceased4() ? 1 : 0) + ", " + 
                        "'" + moChildRelatives.getSon5() + "', " + 
                        (moChildRelatives.getSonDateBirth5() == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(moChildRelatives.getSonDateBirth5()) + "'") + ", " +
                        (moChildRelatives.isSonDeceased5() ? 1 : 0) + ", " + 
                        "'" + moChildRelatives.getEmergenciesContact() + "', " + 
                        "'" + moChildRelatives.getEmergenciesTelNumber()+ "', " + 
                        "'" + moChildRelatives.getBeneficiaries()+ "', " + 
                        moChildRelatives.getFkCatSexMateClassId() + ", " + 
                        moChildRelatives.getFkCatSexMateTypeId() + ", " + 
                        moChildRelatives.getFkCatSexSon1ClassId() + ", " + 
                        moChildRelatives.getFkCatSexSon1TypeId() + ", " + 
                        moChildRelatives.getFkCatSexSon2ClassId() + ", " + 
                        moChildRelatives.getFkCatSexSon2TypeId() + ", " + 
                        moChildRelatives.getFkCatSexSon3ClassId() + ", " + 
                        moChildRelatives.getFkCatSexSon3TypeId() + ", " + 
                        moChildRelatives.getFkCatSexSon4ClassId() + ", " + 
                        moChildRelatives.getFkCatSexSon4TypeId() + ", " + 
                        moChildRelatives.getFkCatSexSon5ClassId() + ", " + 
                        moChildRelatives.getFkCatSexSon5TypeId() + ", " + 
                        moChildRelatives.getFkCatKinshipEmergenciesClassId() + ", " + 
                        moChildRelatives.getFkCatKinshipEmergenciesTypeId() + " " + 
                        ")";
                statement.execute(sql);
            }
            
            if (mbIsRegistryNew || mbActive != mbActiveOld) {
                SHrsEmployeeHireLog hrsEmployeeHireLog = new SHrsEmployeeHireLog(connection); // spreads log entries to all sibling companies
                
                hrsEmployeeHireLog.setPkEmployeeId(mnPkEmployeeId);
                hrsEmployeeHireLog.setLastHireDate(mtDateLastHire);
                hrsEmployeeHireLog.setLastHireNotes("");
                hrsEmployeeHireLog.setLastDismissalDate_n(mtDateLastDismissal_n); // within an employee registration, it must be null
                hrsEmployeeHireLog.setLastDismissalNotes("");
                hrsEmployeeHireLog.setIsHire(mbActive);
                hrsEmployeeHireLog.setDeleted(mbDeleted);
                hrsEmployeeHireLog.setFkEmployeeDismissalTypeId(SModSysConsts.HRSU_TP_EMP_DIS_NA);
                
                if (mbActive) {
                    hrsEmployeeHireLog.setFkRecruitmentSchemaTypeId(mnFkRecruitmentSchemaTypeId); // recruitment schema type set only when hiring
                }
                
                hrsEmployeeHireLog.setFkUserInsertId(mnFkUserInsertId != 0 ? mnFkUserInsertId : SUtilConsts.USR_NA_ID);
                hrsEmployeeHireLog.setFkUserUpdateId(mnFkUserUpdateId != 0 ? mnFkUserUpdateId : SUtilConsts.USR_NA_ID);
                
                hrsEmployeeHireLog.setRequestSettings(mbIsRegistryNew);
                hrsEmployeeHireLog.processRequest();
            }

            if (mdAuxNewSalary != 0) {
                if (mtAuxNewDateSalary == null) {
                    throw new Exception("La fecha de última actualización de 'salario diario' no ha sido definida.");
                }
                createWageLog(connection, mtAuxNewDateSalary);
            }

            if (mdAuxNewWage != 0) {
                if (mtAuxNewDateWage == null) {
                    throw new Exception("La fecha de última actualización de 'sueldo mensual' no ha sido definida.");
                }
                createWageLog(connection, mtAuxNewDateWage);
            }

            if (mdAuxNewSalarySscBase != 0) {
                if (mtAuxNewDateSalarySscBase == null) {
                    throw new Exception("La fecha de última actualización de 'Salario Base de Cotización (SBC)' no ha sido definida.");
                }
                createSalarySscBaseLog(connection, mtAuxNewDateSalarySscBase);
            }
            
            if (mtAuxNewDatePosition != null) {
                if (mtAuxNewDatePosition == null) {
                    throw new Exception("La fecha del último cambio de departamento o puesto no ha sido definida.");
                }
                createChangePositionLog(connection, mtAuxNewDatePosition);
            }

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
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
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
