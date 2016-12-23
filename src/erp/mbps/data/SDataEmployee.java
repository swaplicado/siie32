
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsAccounting;
import erp.mod.hrs.db.SHrsEmployeeHireLog;
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
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Juan Barajas
 */
public class SDataEmployee extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkEmployeeId;
    protected String msNumber;
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
    protected int mnFkRecruitmentSchemeTypeId;
    protected int mnFkPositionRiskTypeId;
    protected int mnFkCatalogueSexCategoryId;
    protected int mnFkCatalogueSexTypeId;
    protected int mnFkCatalogueBloodTypeCategoryId;
    protected int mnFkCatalogueBloodTypeTypeId;
    protected int mnFkCatalogueMaritalStatusCategoryId;
    protected int mnFkCatalogueMaritalStatusTypeId;
    protected int mnFkCatalogueEducationCategoryId;
    protected int mnFkCatalogueEducationTypeId;
    protected int mnFkBankId_n;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;

    protected double mdAuxSalary;
    protected double mdAuxWage;
    protected double mdAuxSalarySscBase;
    protected Date mtAuxDateSalary;
    protected Date mtAuxDateWage;
    protected Date mtAuxDateSalarySscBase;

    protected javax.swing.ImageIcon moXtaImageIconPhoto_n;
    protected javax.swing.ImageIcon moXtaImageIconSignature_n;
    
    private void createHireLog(Connection connection) {
        String sql = "";

        try {
            sql = "INSERT INTO hrs_emp_log_hire VALUES (" +
                    mnPkEmployeeId + ", " +
                    "1, " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " +
                    "'', " +
                    "NULL, " +
                    "'', " +
                    (mbActive ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    SModSysConsts.HRSU_TP_EMP_DIS_NON + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";

            connection.createStatement().execute(sql);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
    }

    private void createWageLog(Connection connection, Date date) {
        String sql = "";
        ResultSet resultSet = null;
        int nLogId = 0;

        try {
            nLogId = 0;

            sql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM hrs_emp_log_wage WHERE id_emp = " + mnPkEmployeeId + " ";

            resultSet = connection.createStatement().executeQuery(sql);
            if (resultSet.next()) {
                nLogId = resultSet.getInt(1);
            }

            connection.createStatement().execute(sql);

            sql = "INSERT INTO hrs_emp_log_wage VALUES (" + mnPkEmployeeId + ", " +
                    nLogId + ", " +
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
                    mnFkRecruitmentSchemeTypeId + ", " +
                    mnFkPositionRiskTypeId + ", " +
                    (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";

            connection.createStatement().execute(sql);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
    }

    private void createSalarySscBaseLog(Connection connection, Date date) {
        String sql = "";
        ResultSet resultSet = null;
        int nLogId = 0;

        try {
            nLogId = 0;

            sql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " WHERE id_emp = " + mnPkEmployeeId + " ";
            resultSet = connection.createStatement().executeQuery(sql);
            if (resultSet.next()) {
                nLogId = resultSet.getInt(1);
            }

            connection.createStatement().execute(sql);

            sql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " VALUES (" + mnPkEmployeeId + ", " +
                    nLogId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(date) + "', " +
                    mdSalarySscBase + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";

            connection.createStatement().execute(sql);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
    }

    private void createAccountingEarningConfiguration(final Connection connection) {
        ResultSet resultSet = null;
        String sql = "";
        ArrayList<String> aSql = new ArrayList<String>();
        
        try {
            sql = "SELECT DISTINCT id_ear, b_del FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR) + " " +
                    "WHERE id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_EMP + " ";

            resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                sql = "INSERT INTO hrs_acc_ear VALUES (" +
                        resultSet.getInt("id_ear") + ", " + 
                        SModSysConsts.HRSS_TP_ACC_EMP + ", " + 
                        mnPkEmployeeId + ", " + 
                        resultSet.getInt("b_del") + ", " + 
                        SModSysConsts.FIN_ACC_NA + ", " + 
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " +
                        "NOW()" + " " +
                        ")";

                aSql.add(sql);
            }

            for (String s : aSql) {
                connection.createStatement().execute(s);
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
    }
    
    private void createAccountingDeductionConfiguration(final Connection connection) {
        ResultSet resultSet = null;
        String sql = "";
        ArrayList<String> aSql = new ArrayList<String>();
        
        try {
            sql = "SELECT DISTINCT id_ded, b_del FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED) + " " +
                    "WHERE id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_EMP + " ";

            resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                sql = "INSERT INTO hrs_acc_ded VALUES (" +
                        resultSet.getInt("id_ded") + ", " + 
                        SModSysConsts.HRSS_TP_ACC_EMP + ", " + 
                        mnPkEmployeeId + ", " + 
                        resultSet.getInt("b_del") + ", " + 
                        SModSysConsts.FIN_ACC_NA + ", " + 
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        "NULL, " +
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " +
                        "NOW()" + " " +
                        ")";

                aSql.add(sql);
            }

            for (String s : aSql) {
                connection.createStatement().execute(s);
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
    }

    public SDataEmployee() {
        super(SModConsts.HRSU_EMP);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setNumber(String s) { msNumber = s; }
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
    public void setFkCatalogueSexCategoryId(int n) { mnFkCatalogueSexCategoryId = n; }
    public void setFkCatalogueSexTypeId(int n) { mnFkCatalogueSexTypeId = n; }
    public void setFkCatalogueBloodTypeCategoryId(int n) { mnFkCatalogueBloodTypeCategoryId = n; }
    public void setFkCatalogueBloodTypeTypeId(int n) { mnFkCatalogueBloodTypeTypeId = n; }
    public void setFkCatalogueMaritalStatusCategoryId(int n) { mnFkCatalogueMaritalStatusCategoryId = n; }
    public void setFkCatalogueMaritalStatusTypeId(int n) { mnFkCatalogueMaritalStatusTypeId = n; }
    public void setFkCatalogueEducationCategoryId(int n) { mnFkCatalogueEducationCategoryId = n; }
    public void setFkCatalogueEducationTypeId(int n) { mnFkCatalogueEducationTypeId = n; }
    public void setFkBankId_n(int n) { mnFkBankId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setAuxSalary(double d) { mdAuxSalary = d; }
    public void setAuxWage(double d) { mdAuxWage = d; }
    public void setAuxSalarySscBase(double d) { mdAuxSalarySscBase = d; }
    public void setAuxDateSalary(Date t) { mtAuxDateSalary = t; }
    public void setAuxDateWage(Date t) { mtAuxDateWage = t; }
    public void setAuxDateSalarySscBase(Date t) { mtAuxDateSalarySscBase = t; }
    
    public void setXtaImageIconPhoto_n(javax.swing.ImageIcon o) { moXtaImageIconPhoto_n = o; }
    public void setXtaImageIconSignature_n(javax.swing.ImageIcon o) { moXtaImageIconSignature_n = o; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public String getNumber() { return msNumber; }
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
    public int getFkCatalogueSexCategoryId() { return mnFkCatalogueSexCategoryId; }
    public int getFkCatalogueSexTypeId() { return mnFkCatalogueSexTypeId; }
    public int getFkCatalogueBloodTypeCategoryId() { return mnFkCatalogueBloodTypeCategoryId; }
    public int getFkCatalogueBloodTypeTypeId() { return mnFkCatalogueBloodTypeTypeId; }
    public int getFkCatalogueMaritalStatusCategoryId() { return mnFkCatalogueMaritalStatusCategoryId; }
    public int getFkCatalogueMaritalStatusTypeId() { return mnFkCatalogueMaritalStatusTypeId; }
    public int getFkCatalogueEducationCategoryId() { return mnFkCatalogueEducationCategoryId; }
    public int getFkCatalogueEducationTypeId() { return mnFkCatalogueEducationTypeId; }
    public int getFkBankId_n() { return mnFkBankId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public double getAuxSalary() { return mdAuxSalary; }
    public double getAuxWage() { return mdAuxWage; }
    public double getAuxSalarySscBase() { return mdAuxSalarySscBase; }
    public Date getAuxDateSalary() { return mtAuxDateSalary; }
    public Date getAuxDateWage() { return mtAuxDateWage; }
    public Date getAuxDateSalarySscBase() { return mtAuxDateSalarySscBase; }
    
    public javax.swing.ImageIcon getXtaImageIconPhoto_n() { return moXtaImageIconPhoto_n; }
    public javax.swing.ImageIcon getXtaImageIconSignature_n() { return moXtaImageIconSignature_n; }

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
        mnFkCatalogueSexCategoryId = 0;
        mnFkCatalogueSexTypeId = 0;
        mnFkCatalogueBloodTypeCategoryId = 0;
        mnFkCatalogueBloodTypeTypeId = 0;
        mnFkCatalogueMaritalStatusCategoryId = 0;
        mnFkCatalogueMaritalStatusTypeId = 0;
        mnFkCatalogueEducationCategoryId = 0;
        mnFkCatalogueEducationTypeId = 0;
        mnFkBankId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mdAuxSalary = 0;
        mdAuxWage = 0;
        mdAuxSalarySscBase = 0;
        mtAuxDateSalary = null;
        mtAuxDateWage = null;
        mtAuxDateSalarySscBase = null;
        
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
                mnFkCatalogueSexCategoryId = resultSet.getInt("fk_cl_cat_sex");
                mnFkCatalogueSexTypeId = resultSet.getInt("fk_tp_cat_sex");
                mnFkCatalogueBloodTypeCategoryId = resultSet.getInt("fk_cl_cat_blo");
                mnFkCatalogueBloodTypeTypeId = resultSet.getInt("fk_tp_cat_blo");
                mnFkCatalogueMaritalStatusCategoryId = resultSet.getInt("fk_cl_cat_mar");
                mnFkCatalogueMaritalStatusTypeId = resultSet.getInt("fk_tp_cat_mar");
                mnFkCatalogueEducationCategoryId = resultSet.getInt("fk_cl_cat_edu");
                mnFkCatalogueEducationTypeId = resultSet.getInt("fk_tp_cat_edu");
                mnFkBankId_n = resultSet.getInt("fk_bank_n");
                mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
                mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
                mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
                mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

                if (oPhoto_n != null) {
                    moXtaImageIconPhoto_n = SLibUtilities.convertBlobToImageIcon(oPhoto_n);
                }
                if (oSignature_n != null) {
                    moXtaImageIconSignature_n = SLibUtilities.convertBlobToImageIcon(oSignature_n);
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
                        mnFkCatalogueSexCategoryId + ", " + 
                        mnFkCatalogueSexTypeId + ", " + 
                        mnFkCatalogueBloodTypeCategoryId + ", " + 
                        mnFkCatalogueBloodTypeTypeId + ", " + 
                        mnFkCatalogueMaritalStatusCategoryId + ", " + 
                        mnFkCatalogueMaritalStatusTypeId + ", " + 
                        mnFkCatalogueEducationCategoryId + ", " + 
                        mnFkCatalogueEducationTypeId + ", " + 
                        (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
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
                        "fk_tp_rec_sche = " + mnFkRecruitmentSchemeTypeId + ", " +
                        "fk_tp_pos_risk = " + mnFkPositionRiskTypeId + ", " +
                        "fk_cl_cat_sex = " + mnFkCatalogueSexCategoryId + ", " +
                        "fk_tp_cat_sex = " + mnFkCatalogueSexTypeId + ", " +
                        "fk_cl_cat_blo = " + mnFkCatalogueBloodTypeCategoryId + ", " +
                        "fk_tp_cat_blo = " + mnFkCatalogueBloodTypeTypeId + ", " +
                        "fk_cl_cat_mar = " + mnFkCatalogueMaritalStatusCategoryId + ", " +
                        "fk_tp_cat_mar = " + mnFkCatalogueMaritalStatusTypeId + ", " +
                        "fk_cl_cat_edu = " + mnFkCatalogueEducationCategoryId + ", " +
                        "fk_tp_cat_edu = " + mnFkCatalogueEducationTypeId + ", " +
                        "fk_bank_n = " + (mnFkBankId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkBankId_n) + ", " +
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
            
            if (mbIsRegistryNew) {
                //createHireLog(connection); // XXX jbarajas 09/02/2016 create hire log in all companies with enable payroll module 
                SHrsEmployeeHireLog employeeHireLog = new SHrsEmployeeHireLog(connection, null);
                
                employeeHireLog.setPkEmployeeId(mnPkEmployeeId);
                employeeHireLog.setDateLastHire(mtDateLastHire);
                //employeeHireLog.setDateLastDismiss_n(null);
                employeeHireLog.setIsHire(mbActive);
                employeeHireLog.setDeleted(mbDeleted);
                employeeHireLog.setFkDismissedType(SModSysConsts.HRSU_TP_EMP_DIS_NON);
                employeeHireLog.setFkUserInsertId(mnFkUserInsertId);
                employeeHireLog.setFkUserUpdateId(mnFkUserUpdateId);
                employeeHireLog.setIsFirtsHire(true);
                
                employeeHireLog.save();
            }

            if (mdAuxSalary != 0) {
                if (mtAuxDateSalary == null) {
                    throw new Exception("Fecha no disponible");
                }
                createWageLog(connection, mtAuxDateSalary);
            }

            if (mdAuxWage != 0) {
                if (mtAuxDateWage == null) {
                    throw new Exception("Fecha no disponible");
                }
                createWageLog(connection, mtAuxDateWage);
            }

            if (mdAuxSalarySscBase != 0) {
                if (mtAuxDateSalarySscBase == null) {
                    throw new Exception("Fecha no disponible");
                }
                createSalarySscBaseLog(connection, mtAuxDateSalarySscBase);
            }
        
            if (mbIsRegistryNew) {
                //createAccountingEarningConfiguration(connection);
                //createAccountingDeductionConfiguration(connection); // XXX jbarajas 09/02/2016 create accounting earning and deducction configuration in all companies with enable payroll module
                SHrsAccounting accounting = new SHrsAccounting(connection, null);
                
                accounting.setAccountingType(SModSysConsts.HRSS_TP_ACC_EMP);
                accounting.setPkReferenceId(mnPkEmployeeId);
                accounting.setFkUserInsertId(mnFkUserInsertId);
                accounting.setFkUserUpdateId(mnFkUserUpdateId);
            
                accounting.save();
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
