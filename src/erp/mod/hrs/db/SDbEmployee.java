
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mod.hrs.db.SDataEmployee
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbEmployee extends SDbRegistryUser {
    
    public static final int FIELD_ACTIVE = FIELD_BASE + 1;
    public static final int FIELD_DATE_LAST_HIRE = FIELD_BASE + 2;
    public static final int FIELD_DATE_LAST_DISMISS = FIELD_BASE + 3;

    protected int mnPkEmployeeId;
    protected String msNumber;
    protected String msLastname1;
    protected String msLastname2;
    protected String msSocialSecurityNumber;
    protected Date mtDateBirth;
    protected Date mtDateBenefits;
    protected Date mtDateLastHire;
    protected Date mtDateLastDismiss_n;
    protected double mdSalary;
    protected double mdWage;
    protected double mdSalarySscBase;
    protected Date mtDateSalary;
    protected Date mtDateWage;
    protected Date mtDateSalarySscBase;
    protected int mnWorkingHoursDay;
    protected String msBankAccount;
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
    protected int mnFkRecruitmentSchemeTypeId;
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
    protected int mnFkBankId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected boolean mbAuxActive;
    protected String msAuxEmployee;
    protected String msAuxFiscalId;
    protected String msAuxAlternativeId;
    protected SDbEmployeeHireLog moXtaEmployeeHireLog;
    protected Date mtXtaDate;
    protected int mnXtaEmployeeDismissTypeId;
    protected String msXtaNotes;
    
    protected byte[] moAuxImagePhoto_n;
    protected byte[] moAuxImageSignature_n;

    protected javax.swing.ImageIcon moXtaImageIconPhoto_n;
    protected javax.swing.ImageIcon moXtaImageIconSignature_n;
    
    public SDbEmployee() {
        super(SModConsts.HRSU_EMP);
    }
    
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setNumber(String s) { msNumber = s; }
    public void setLastname1(String s) { msLastname1 = s; }
    public void setLastname2(String s) { msLastname2 = s; }
    public void setSocialSecurityNumber(String s) { msSocialSecurityNumber = s; }
    public void setDateBirth(Date t) { mtDateBirth = t; }
    public void setDateBenefits(Date t) { mtDateBenefits = t; }
    public void setDateLastHire(Date t) { mtDateLastHire = t; }
    public void setDateLastDismiss_n(Date t) { mtDateLastDismiss_n = t; }
    public void setSalary(double d) { mdSalary = d; }
    public void setWage(double d) { mdWage = d; }
    public void setSalarySscBase(double d) { mdSalarySscBase = d; }
    public void setDateSalary(Date t) { mtDateSalary = t; }
    public void setDateWage(Date t) { mtDateWage = t; }
    public void setDateSalarySscBase(Date t) { mtDateSalarySscBase = t; }
    public void setWorkingHoursDay(int n) { mnWorkingHoursDay = n; }
    public void setBankAccount(String s) { msBankAccount = s; }
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
    public void setFkRecruitmentSchemeTypeId(int n) { mnFkRecruitmentSchemeTypeId = n; }
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
    public void setFkBankId_n(int n) { mnFkBankId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public String getNumber() { return msNumber; }
    public String getLastname1() { return msLastname1; }
    public String getLastname2() { return msLastname2; }
    public String getSocialSecurityNumber() { return msSocialSecurityNumber; }
    public Date getDateBirth() { return mtDateBirth; }
    public Date getDateBenefits() { return mtDateBenefits; }
    public Date getDateLastHire() { return mtDateLastHire; }
    public Date getDateLastDismiss_n() { return mtDateLastDismiss_n; }
    public double getSalary() { return mdSalary; }
    public double getWage() { return mdWage; }
    public double getSalarySscBase() { return mdSalarySscBase; }
    public Date getDateSalary() { return mtDateSalary; }
    public Date getDateWage() { return mtDateWage; }
    public Date getDateSalarySscBase() { return mtDateSalarySscBase; }
    public int getWorkingHoursDay() { return mnWorkingHoursDay; }
    public String getBankAccount() { return msBankAccount; }
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
    public int getFkRecruitmentSchemeTypeId() { return mnFkRecruitmentSchemeTypeId; }
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
    public int getFkBankId_n() { return mnFkBankId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setAuxActive(boolean b) { mbAuxActive = b; }
    public void setAuxEmployee(String s) { msAuxEmployee = s; }
    public void setAuxFiscalId(String s) { msAuxFiscalId = s; }
    public void setAuxAlternativeId(String s) { msAuxAlternativeId = s; }
    public void setXtaEmployeeHireLog(SDbEmployeeHireLog o) { moXtaEmployeeHireLog = o; }
    public void setXtaDate(Date t) { mtXtaDate = t; }
    public void setXtaEmployeeDismissTypeId(int n) { mnXtaEmployeeDismissTypeId = n; }
    public void setXtaNotes(String s) { msXtaNotes = s; }
    
    public void setAuxImagePhoto_n(byte[] o) { moAuxImagePhoto_n = o; }
    public void setAuxImageSignature_n(byte[] o) { moAuxImageSignature_n = o; }
    
    public void setXtaImageIconPhoto_n(javax.swing.ImageIcon o) { moXtaImageIconPhoto_n = o; }
    public void setXtaImageIconSignature_n(javax.swing.ImageIcon o) { moXtaImageIconSignature_n = o; }
    
    public boolean isAuxActive() { return mbAuxActive; }
    public String getAuxEmployee() { return msAuxEmployee; }
    public String getAuxFiscalId() { return msAuxFiscalId; }
    public String getAuxAlternativeId() { return msAuxAlternativeId; }
    public SDbEmployeeHireLog getXtaEmployeeHireLog() { return moXtaEmployeeHireLog; }
    public Date getXtaDate() { return mtXtaDate; }
    public int getXtaEmployeeDismissTypeId() { return mnXtaEmployeeDismissTypeId; }
    public String getXtaNotes() { return msXtaNotes; }
    
    public byte[] getAuxImagePhoto_n() { return moAuxImagePhoto_n; }
    public byte[] getAuxImageSignature_n() { return moAuxImageSignature_n; }
    
    public javax.swing.ImageIcon getXtaImageIconPhoto_n() { return moXtaImageIconPhoto_n; }
    public javax.swing.ImageIcon getXtaImageIconSignature_n() { return moXtaImageIconSignature_n; }
    
    public boolean isAssimilable() {
        return SLibUtils.belongsTo(mnFkRecruitmentSchemeTypeId, new int[] { 
            SModSysConsts.HRSS_TP_REC_SCHE_ASS_COO,
            SModSysConsts.HRSS_TP_REC_SCHE_ASS_CIV,
            SModSysConsts.HRSS_TP_REC_SCHE_ASS_BRD,
            SModSysConsts.HRSS_TP_REC_SCHE_ASS_SAL,
            SModSysConsts.HRSS_TP_REC_SCHE_ASS_PRO,
            SModSysConsts.HRSS_TP_REC_SCHE_ASS_SHA,
            SModSysConsts.HRSS_TP_REC_SCHE_ASS_OTH
        });
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
        msSocialSecurityNumber = "";
        mtDateBirth = null;
        mtDateBenefits = null;
        mtDateLastHire = null;
        mtDateLastDismiss_n = null;
        mdSalary = 0;
        mdWage = 0;
        mdSalarySscBase = 0;
        mtDateSalary = null;
        mtDateWage = null;
        mtDateSalarySscBase = null;
        mnWorkingHoursDay = 0;
        msBankAccount = "";
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
        mnFkRecruitmentSchemeTypeId = 0;
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
        mnFkBankId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbAuxActive = false;
        msAuxEmployee = "";
        msAuxFiscalId = "";
        msAuxAlternativeId = "";
        moXtaEmployeeHireLog = null;
        mtXtaDate = null;
        mnXtaEmployeeDismissTypeId = 0;
        msXtaNotes = "";
        
        moAuxImagePhoto_n = null;
        moAuxImageSignature_n = null;
        
        moXtaImageIconPhoto_n = null;
        moXtaImageIconSignature_n = null;
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
        Blob oPhoto_n;
        Blob oSignature_n;

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
            msSocialSecurityNumber = resultSet.getString("ssn");
            mtDateBirth = resultSet.getDate("dt_bir");
            mtDateBenefits = resultSet.getDate("dt_ben");
            mtDateLastHire = resultSet.getDate("dt_hire");
            mtDateLastDismiss_n = resultSet.getDate("dt_dis_n");
            mdSalary = resultSet.getDouble("sal");
            mdWage = resultSet.getDouble("wage");
            mdSalarySscBase = resultSet.getDouble("sal_ssc");
            mtDateSalary = resultSet.getDate("dt_sal");
            mtDateWage = resultSet.getDate("dt_wage");
            mtDateSalarySscBase = resultSet.getDate("dt_sal_ssc");
            mnWorkingHoursDay = resultSet.getInt("wrk_hrs_day");
            msBankAccount = resultSet.getString("bank_acc");
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
            mnFkRecruitmentSchemeTypeId = resultSet.getInt("fk_tp_rec_sche");
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
            mnFkBankId_n = resultSet.getInt("fk_bank_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbAuxActive = mbActive;
            
            if (oPhoto_n != null) {
                moXtaImageIconPhoto_n = SLibUtilities.convertBlobToImageIcon(oPhoto_n);
            }
            if (oSignature_n != null) {
                moXtaImageIconSignature_n = SLibUtilities.convertBlobToImageIcon(oSignature_n);
            }
            
            msSql = "SELECT bp, fiscal_id, alt_id FROM erp.bpsu_bp WHERE id_bp = " + mnPkEmployeeId;
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                msAuxEmployee = resultSet.getString("bp");
                msAuxFiscalId = resultSet.getString("fiscal_id");
                msAuxAlternativeId = resultSet.getString("alt_id");
            }
            
            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        PreparedStatement preparedStatementmagePhoto_n = null;
        PreparedStatement preparedStatementmageSignature_n = null;

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
                    "'" + msSocialSecurityNumber + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateBirth) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateBenefits) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " + 
                    (mtDateLastDismiss_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismiss_n) + "'") + ", " + 
                    mdSalary + ", " + 
                    mdWage + ", " + 
                    mdSalarySscBase + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSalary) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateWage) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSalarySscBase) + "', " + 
                    mnWorkingHoursDay + ", " + 
                    "'" + msBankAccount + "', " + 
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
                    mnFkRecruitmentSchemeTypeId + ", " + 
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
                    (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
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
                    "ssn = '" + msSocialSecurityNumber + "', " +
                    "dt_bir = '" + SLibUtils.DbmsDateFormatDate.format(mtDateBirth) + "', " +
                    "dt_ben = '" + SLibUtils.DbmsDateFormatDate.format(mtDateBenefits) + "', " +
                    "dt_hire = '" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " +
                    "dt_dis_n = " + (mtDateLastDismiss_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismiss_n) + "'") + ", " +
                    "sal = " + mdSalary + ", " +
                    "wage = " + mdWage + ", " +
                    "sal_ssc = " + mdSalarySscBase + ", " +
                    "dt_sal = '" + SLibUtils.DbmsDateFormatDate.format(mtDateSalary) + "', " +
                    "dt_wage = '" + SLibUtils.DbmsDateFormatDate.format(mtDateWage) + "', " +
                    "dt_sal_ssc = '" + SLibUtils.DbmsDateFormatDate.format(mtDateSalarySscBase) + "', " +
                    "wrk_hrs_day = " + mnWorkingHoursDay + ", " +
                    "bank_acc = '" + msBankAccount + "', " +
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
                    "fk_tp_rec_sche = " + mnFkRecruitmentSchemeTypeId + ", " +
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
                    "fk_bank_n = " + (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);

        if (moAuxImagePhoto_n != null) {
            msSql = "UPDATE erp.hrsu_emp SET img_pho_n = ? WHERE id_emp = " + mnPkEmployeeId + " ";

            preparedStatementmagePhoto_n = session.getStatement().getConnection().prepareStatement(msSql);
            preparedStatementmagePhoto_n.setBytes(1, moAuxImagePhoto_n);
            preparedStatementmagePhoto_n.execute();
        }

        if (moAuxImageSignature_n != null) {
            msSql = "UPDATE erp.hrsu_emp SET img_sig_n = ? WHERE id_emp = " + mnPkEmployeeId + " ";

            preparedStatementmageSignature_n = session.getStatement().getConnection().prepareStatement(msSql);
            preparedStatementmageSignature_n.setBytes(1, moAuxImageSignature_n);
            preparedStatementmageSignature_n.execute();
        }

        if (mbRegistryNew || mbActive != mbAuxActive) {
            //createHireLog(session);
            
            SHrsEmployeeHireLog employeeHireLog = new SHrsEmployeeHireLog(null, session);
                
            employeeHireLog.setPkEmployeeId(mnPkEmployeeId);
            employeeHireLog.setDateLastHire(mtDateLastHire);
            //employeeHireLog.setDateLastDismiss_n(null);
            employeeHireLog.setIsHire(mbActive);
            employeeHireLog.setDeleted(mbDeleted);
            employeeHireLog.setFkDismissedType(SModSysConsts.HRSU_TP_EMP_DIS_NON);
            employeeHireLog.setFkUserInsertId(mnFkUserInsertId);
            employeeHireLog.setFkUserUpdateId(mnFkUserUpdateId);
            
            employeeHireLog.setIsFirtsHire(false);
            if (mbActive) {
                employeeHireLog.setDateLastHire(mtXtaDate);
                employeeHireLog.setNotesHire(msXtaNotes);
            }
            else {
                employeeHireLog.setDateLastDismiss_n(mtXtaDate);
                employeeHireLog.setNotesDismissed(msXtaNotes);
            }
            employeeHireLog.setIsHire(mbActive);
            employeeHireLog.setFkDismissedType(mnXtaEmployeeDismissTypeId);
            employeeHireLog.save();
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
        registry.setSocialSecurityNumber(this.getSocialSecurityNumber());
        registry.setDateBirth(this.getDateBirth());
        registry.setDateBenefits(this.getDateBenefits());
        registry.setDateLastHire(this.getDateLastHire());
        registry.setDateLastDismiss_n(this.getDateLastDismiss_n());
        registry.setSalary(this.getSalary());
        registry.setWage(this.getWage());
        registry.setSalarySscBase(this.getSalarySscBase());
        registry.setDateSalary(this.getDateSalary());
        registry.setDateWage(this.getDateWage());
        registry.setDateSalarySscBase(this.getDateSalarySscBase());
        registry.setWorkingHoursDay(this.getWorkingHoursDay());
        registry.setBankAccount(this.getBankAccount());
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
        registry.setFkRecruitmentSchemeTypeId(this.getFkRecruitmentSchemeTypeId());
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
        registry.setFkBankId_n(this.getFkBankId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
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
            case FIELD_DATE_LAST_DISMISS:
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
