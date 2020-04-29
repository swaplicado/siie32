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
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbConfig extends SDbRegistryUser {

    protected int mnPkConfigId;
    protected Date mtDateOperations;
    protected int mnFirstDayWeek;
    protected int mnLimitMwzReference;
    protected String msNumberSeries;
    protected String msSsSubbranch;
    protected String msBajioAffinityGroup;
    protected double mdPayrollTaxRate;
    protected int mnPrePayrollWeeklyCutoffDayWeek; //Pre payroll weekly cutoff day: 1=Su, 2=Mo, 3=Tu, ... Sa=7
    protected int mnPrePayrollWeeklyWeeksLag;
    protected int mnTimeClockPolicy;
    protected boolean mbFortnightStandard;
    protected boolean mbAutoVacationBonus;
    protected boolean mbTaxSubsidyEarning;
    protected boolean mbTaxNet;
    protected boolean mbBankAccountUse;
    //protected boolean mbDeleted;
    protected int mnFkMwzTypeId;
    protected int mnFkMwzReferenceTypeId;
    protected int mnFkTaxComputationTypeId;
    protected int mnFkBankId;
    protected int mnFkEarningEarningId_n;
    protected int mnFkEarningVacationId_n;
    protected int mnFkEarningTaxId_n;
    protected int mnFkEarningTaxSubsidyId_n;
    protected int mnFkEarningTaxSubsidyCompensatedId_n;
    protected int mnFkEarningSsContributionId_n;
    protected int mnFkEarningOvertime_2_Id_n;
    protected int mnFkEarningOvertime_3_Id_n;
    protected int mnFkEarningHolidayId_n;
    protected int mnFkEarningDayOffId_n;
    protected int mnFkEarningSunBonusId_n;
    protected int mnFkDeductionTaxId_n;
    protected int mnFkDeductionTaxSubsidyId_n;
    protected int mnFkDeductionSsContributionId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbConfig() {
        super(SModConsts.HRS_CFG);
    }

    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setDateOperations(Date t) { mtDateOperations = t; }
    public void setFirstDayWeek(int n) { mnFirstDayWeek = n; }
    public void setLimitMwzReference(int n) { mnLimitMwzReference = n; }
    public void setNumberSeries(String s) { msNumberSeries = s; }
    public void setSsSubbranch(String s) { msSsSubbranch = s; }
    public void setBajioAffinityGroup(String s) { msBajioAffinityGroup = s; }
    public void setPayrollTaxRate(double d) { mdPayrollTaxRate = d; }
    public void setPrePayWeekCutDay(int n) { mnPrePayrollWeeklyCutoffDayWeek = n; }
    public void setPrePayWeekLag(int n) { mnPrePayrollWeeklyWeeksLag = n; }
    public void setTimeClockPol(int n) { mnTimeClockPolicy = n; }
    public void setFortnightStandard(boolean b) { mbFortnightStandard = b; }
    public void setAutoVacationBonus(boolean b) { mbAutoVacationBonus = b; }
    public void setTaxSubsidyEarning(boolean b) { mbTaxSubsidyEarning = b; }
    public void setTaxNet(boolean b) { mbTaxNet = b; }
    public void setBankAccountUse(boolean b) { mbBankAccountUse = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkMwzTypeId(int n) { mnFkMwzTypeId = n; }
    public void setFkMwzReferenceTypeId(int n) { mnFkMwzReferenceTypeId = n; }
    public void setFkTaxComputationTypeId(int n) { mnFkTaxComputationTypeId = n; }
    public void setFkBankId(int n) { mnFkBankId = n; }
    public void setFkEarningEarningId_n(int n) { mnFkEarningEarningId_n = n; }
    public void setFkEarningVacationId_n(int n) { mnFkEarningVacationId_n = n; }
    public void setFkEarningTaxId_n(int n) { mnFkEarningTaxId_n = n; }
    public void setFkEarningTaxSubsidyId_n(int n) { mnFkEarningTaxSubsidyId_n = n; }
    public void setFkEarningTaxSubsidyCompensatedId_n(int n) { mnFkEarningTaxSubsidyCompensatedId_n = n; }
    public void setFkEarningSsContributionId_n(int n) { mnFkEarningSsContributionId_n = n; }
    public void setFkEarningOvertime2Id_n(int n) { mnFkEarningOvertime_2_Id_n = n; }
    public void setFkEarningOvertime3Id_n(int n) { mnFkEarningOvertime_3_Id_n = n; }
    public void setFkEarningHolidayId_n(int n) { mnFkEarningHolidayId_n = n; }
    public void setFkEarningDayOffId_n(int n) { mnFkEarningDayOffId_n = n; }
    public void setFkEarningSunBonusId_n(int n) { mnFkEarningSunBonusId_n = n; }
    public void setFkDeductionTaxId_n(int n) { mnFkDeductionTaxId_n = n; }
    public void setFkDeductionTaxSubsidyId_n(int n) { mnFkDeductionTaxSubsidyId_n = n; }
    public void setFkDeductionSsContributionId_n(int n) { mnFkDeductionSsContributionId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkConfigId() { return mnPkConfigId; }
    public Date getDateOperations() { return mtDateOperations; }
    public int getFirstDayWeek() { return mnFirstDayWeek; }
    public int getLimitMwzReference() { return mnLimitMwzReference; }
    public String getNumberSeries() { return msNumberSeries; }
    public String getSsSubbranch() { return msSsSubbranch; }
    public String getBajioAffinityGroup() { return msBajioAffinityGroup; }
    public double getPayrollTaxRate() { return mdPayrollTaxRate; }
    /**
     * Pre payroll weekly cutoff day: 1=Su, 2=Mo, 3=Tu, ... Sa=7
     * @return 
     */
    public int getPrePayWeekCutDay() { return mnPrePayrollWeeklyCutoffDayWeek; }
    public int getPrePayWeekLag() { return mnPrePayrollWeeklyWeeksLag; }
    public int getTimeClockPol() { return mnTimeClockPolicy; }
    public boolean isFortnightStandard() { return mbFortnightStandard; }
    public boolean isAutoVacationBonus() { return mbAutoVacationBonus; }
    public boolean isTaxSubsidyEarning() { return mbTaxSubsidyEarning; }
    public boolean isTaxNet() { return mbTaxNet; }
    public boolean isBankAccountUse() { return mbBankAccountUse; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkMwzTypeId() { return mnFkMwzTypeId; }
    public int getFkMwzReferenceTypeId() { return mnFkMwzReferenceTypeId; }
    public int getFkTaxComputationTypeId() { return mnFkTaxComputationTypeId; }
    public int getFkBankId() { return mnFkBankId; }
    public int getFkEarningEarningId_n() { return mnFkEarningEarningId_n; }
    public int getFkEarningVacationId_n() { return mnFkEarningVacationId_n; }
    public int getFkEarningTaxId_n() { return mnFkEarningTaxId_n; }
    public int getFkEarningTaxSubsidyId_n() { return mnFkEarningTaxSubsidyId_n; }
    public int getFkEarningTaxSubsidyCompensatedId_n() { return mnFkEarningTaxSubsidyCompensatedId_n; }
    public int getFkEarningSsContributionId_n() { return mnFkEarningSsContributionId_n; }
    public int getFkEarningOvertime2Id_n() { return mnFkEarningOvertime_2_Id_n; }
    public int getFkEarningOvertime3Id_n() { return mnFkEarningOvertime_3_Id_n; }
    public int getFkEarningHolidayId_n() { return mnFkEarningHolidayId_n; }
    public int getFkEarningDayOffId_n() { return mnFkEarningDayOffId_n; }
    public int getFkEarningSunBonusId_n() { return mnFkEarningSunBonusId_n; }
    public int getFkDeductionTaxId_n() { return mnFkDeductionTaxId_n; }
    public int getFkDeductionTaxSubsidyId_n() { return mnFkDeductionTaxSubsidyId_n; }
    public int getFkDeductionSsContributionId_n() { return mnFkDeductionSsContributionId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkConfigId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConfigId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkConfigId = 0;
        mtDateOperations = null;
        mnFirstDayWeek = 0;
        mnLimitMwzReference = 0;
        msNumberSeries = "";
        msSsSubbranch = "";
        msBajioAffinityGroup = "";
        mdPayrollTaxRate = 0;
        mnPrePayrollWeeklyCutoffDayWeek = 0;
        mnPrePayrollWeeklyWeeksLag = 0;
        mnTimeClockPolicy = 0;
        mbFortnightStandard = false;
        mbAutoVacationBonus = false;
        mbTaxSubsidyEarning = false;
        mbTaxNet = false;
        mbBankAccountUse = false;
        mbDeleted = false;
        mnFkMwzTypeId = 0;
        mnFkMwzReferenceTypeId = 0;
        mnFkTaxComputationTypeId = 0;
        mnFkBankId = 0;
        mnFkEarningEarningId_n = 0;
        mnFkEarningVacationId_n = 0;
        mnFkEarningTaxId_n = 0;
        mnFkEarningTaxSubsidyId_n = 0;
        mnFkEarningTaxSubsidyCompensatedId_n = 0;
        mnFkEarningSsContributionId_n = 0;
        mnFkEarningOvertime_2_Id_n = 0;
        mnFkEarningOvertime_3_Id_n = 0;
        mnFkEarningHolidayId_n = 0;
        mnFkEarningDayOffId_n = 0;
        mnFkEarningSunBonusId_n = 0;
        mnFkDeductionTaxId_n = 0;
        mnFkDeductionTaxSubsidyId_n = 0;
        mnFkDeductionSsContributionId_n = 0;
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
        return "WHERE id_cfg = " + mnPkConfigId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cfg = " + pk[0] + " ";
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

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkConfigId = resultSet.getInt("id_cfg");
            mtDateOperations = resultSet.getDate("dt_ops");
            mnFirstDayWeek = resultSet.getInt("fst_day_wee");
            mnLimitMwzReference = resultSet.getInt("lim_mwz_ref");
            msNumberSeries = resultSet.getString("num_ser");
            msSsSubbranch = resultSet.getString("ss_subbra");
            msBajioAffinityGroup = resultSet.getString("baj_aff_grp");
            mdPayrollTaxRate = resultSet.getDouble("pay_tax_rate");
            mnPrePayrollWeeklyCutoffDayWeek = resultSet.getInt("pre_pay_wee_cut_day_wee");
            mnPrePayrollWeeklyWeeksLag = resultSet.getInt("pre_pay_wee_wee_lag");
            mnTimeClockPolicy = resultSet.getInt("time_clock_pol");
            mbFortnightStandard = resultSet.getBoolean("b_for_std");
            mbAutoVacationBonus = resultSet.getBoolean("b_auto_vac_bon");
            mbTaxSubsidyEarning = resultSet.getBoolean("b_tax_sub_ear");
            mbTaxNet = resultSet.getBoolean("b_tax_net");
            mbBankAccountUse = resultSet.getBoolean("b_bank_acc_use");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkMwzTypeId = resultSet.getInt("fk_tp_mwz");
            mnFkMwzReferenceTypeId = resultSet.getInt("fk_tp_mwz_ref");
            mnFkTaxComputationTypeId = resultSet.getInt("fk_tp_tax_comp");
            mnFkBankId = resultSet.getInt("fk_bank");
            mnFkEarningEarningId_n = resultSet.getInt("fk_ear_ear_n");
            mnFkEarningVacationId_n = resultSet.getInt("fk_ear_vac_n");
            mnFkEarningTaxId_n = resultSet.getInt("fk_ear_tax_n");
            mnFkEarningTaxSubsidyId_n = resultSet.getInt("fk_ear_tax_sub_n");
            mnFkEarningTaxSubsidyCompensatedId_n = resultSet.getInt("fk_ear_tax_sub_comp_n");
            mnFkEarningSsContributionId_n = resultSet.getInt("fk_ear_ssc_n");
            mnFkEarningOvertime_2_Id_n = resultSet.getInt("fk_ear_overtime_2_n");
            mnFkEarningOvertime_3_Id_n = resultSet.getInt("fk_ear_overtime_3_n");
            mnFkEarningHolidayId_n = resultSet.getInt("fk_ear_holiday_n");
            mnFkEarningDayOffId_n = resultSet.getInt("fk_ear_day_off_n");
            mnFkEarningSunBonusId_n = resultSet.getInt("fk_ear_sun_bonus_n");
            mnFkDeductionTaxId_n = resultSet.getInt("fk_ded_tax_n");
            mnFkDeductionTaxSubsidyId_n = resultSet.getInt("fk_ded_tax_sub_n");
            mnFkDeductionSsContributionId_n = resultSet.getInt("fk_ded_ssc_n");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkConfigId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateOperations) + "', " +
                    mnFirstDayWeek + ", " +
                    mnLimitMwzReference + ", " +
                    "'" + msNumberSeries + "', " + 
                    "'" + msSsSubbranch + "', " + 
                    "'" + msBajioAffinityGroup + "', " + 
                    mdPayrollTaxRate + ", " + 
                    mnPrePayrollWeeklyCutoffDayWeek + ", " + 
                    mnPrePayrollWeeklyWeeksLag + ", " + 
                    mnTimeClockPolicy + ", " + 
                    (mbFortnightStandard ? 1 : 0) + ", " + 
                    (mbAutoVacationBonus ? 1 : 0) + ", " + 
                    (mbTaxSubsidyEarning ? 1 : 0) + ", " +
                    (mbTaxNet ? 1 : 0) + ", " +
                    (mbBankAccountUse ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkMwzTypeId + ", " +
                    mnFkMwzReferenceTypeId + ", " +
                    mnFkTaxComputationTypeId + ", " +
                    mnFkBankId + ", " +
                    (mnFkEarningEarningId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningEarningId_n) + ", " +
                    (mnFkEarningVacationId_n  == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningVacationId_n) + ", " +
                    (mnFkEarningTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningTaxId_n) + ", " +
                    (mnFkEarningTaxSubsidyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningTaxSubsidyId_n) + ", " +
                    (mnFkEarningTaxSubsidyCompensatedId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningTaxSubsidyCompensatedId_n) + ", " +
                    (mnFkEarningSsContributionId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningSsContributionId_n) + ", " +
                    (mnFkEarningOvertime_2_Id_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningOvertime_2_Id_n) + ", " +
                    (mnFkEarningOvertime_3_Id_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningOvertime_3_Id_n) + ", " +
                    (mnFkEarningHolidayId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningHolidayId_n) + ", " +
                    (mnFkEarningDayOffId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningDayOffId_n) + ", " +
                    (mnFkEarningSunBonusId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningSunBonusId_n) + ", " +
                    (mnFkDeductionTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionTaxId_n) + ", " +
                    (mnFkDeductionTaxSubsidyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionTaxSubsidyId_n) + ", " +
                    (mnFkDeductionSsContributionId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionSsContributionId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_cfg = " + mnPkConfigId + ", " +
                    "dt_ops = '" + SLibUtils.DbmsDateFormatDate.format(mtDateOperations) + "', " +
                    "fst_day_wee = " + mnFirstDayWeek + ", " +
                    "lim_mwz_ref = " + mnLimitMwzReference + ", " +
                    "num_ser = '" + msNumberSeries + "', " +
                    "ss_subbra = '" + msSsSubbranch + "', " +
                    "baj_aff_grp = '" + msBajioAffinityGroup + "', " +
                    "pay_tax_rate = " + mdPayrollTaxRate + ", " +
                    "pre_pay_wee_cut_day_wee = " + mnPrePayrollWeeklyCutoffDayWeek + ", " + 
                    "pre_pay_wee_wee_lag = " + mnPrePayrollWeeklyWeeksLag + ", " + 
                    "time_clock_pol = " + mnTimeClockPolicy + ", " + 
                    "b_for_std = " + (mbFortnightStandard ? 1 : 0) + ", " +
                    "b_auto_vac_bon = " + (mbAutoVacationBonus ? 1 : 0) + ", " +
                    "b_tax_sub_ear = " + (mbTaxSubsidyEarning ? 1 : 0) + ", " +
                    "b_tax_net = " + (mbTaxNet ? 1 : 0) + ", " +
                    "b_bank_acc_use = " + (mbBankAccountUse ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_mwz = " + mnFkMwzTypeId + ", " +
                    "fk_tp_mwz_ref = " + mnFkMwzReferenceTypeId + ", " +
                    "fk_tp_tax_comp = " + mnFkTaxComputationTypeId + ", " +
                    "fk_bank = " + mnFkBankId + ", " +
                    "fk_ear_ear_n = " + (mnFkEarningEarningId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningEarningId_n) + ", " +
                    "fk_ear_vac_n = " + (mnFkEarningVacationId_n  == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningVacationId_n) + ", " +
                    "fk_ear_tax_n = " + (mnFkEarningTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningTaxId_n) + ", " +
                    "fk_ear_tax_sub_n = " + (mnFkEarningTaxSubsidyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningTaxSubsidyId_n) + ", " +
                    "fk_ear_tax_sub_comp_n = " + (mnFkEarningTaxSubsidyCompensatedId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningTaxSubsidyCompensatedId_n) + ", " +
                    "fk_ear_ssc_n = " + (mnFkEarningSsContributionId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningSsContributionId_n) + ", " +
                    "fk_ear_overtime_2_n = " + (mnFkEarningOvertime_2_Id_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningOvertime_2_Id_n) + ", " +
                    "fk_ear_overtime_3_n = " + (mnFkEarningOvertime_3_Id_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningOvertime_3_Id_n) + ", " +
                    "fk_ear_holiday_n = " + (mnFkEarningHolidayId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningHolidayId_n) + ", " +
                    "fk_ear_day_off_n = " + (mnFkEarningDayOffId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningDayOffId_n) + ", " +
                    "fk_ear_sun_bonus_n = " + (mnFkEarningSunBonusId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkEarningSunBonusId_n) + ", " +
                    "fk_ded_tax_n = " + (mnFkDeductionTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionTaxId_n) + ", " +
                    "fk_ded_tax_sub_n = " + (mnFkDeductionTaxSubsidyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionTaxSubsidyId_n) + ", " +
                    "fk_ded_ssc_n = " + (mnFkDeductionSsContributionId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionSsContributionId_n) + ", " +
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
    public SDbConfig clone() throws CloneNotSupportedException {
        SDbConfig registry = new SDbConfig();

        registry.setPkConfigId(this.getPkConfigId());
        registry.setDateOperations(this.getDateOperations());
        registry.setFirstDayWeek(this.getFirstDayWeek());
        registry.setLimitMwzReference(this.getLimitMwzReference());
        registry.setNumberSeries(this.getNumberSeries());
        registry.setSsSubbranch(this.getSsSubbranch());
        registry.setBajioAffinityGroup(this.getBajioAffinityGroup());
        registry.setPayrollTaxRate(this.getPayrollTaxRate());
        registry.setPrePayWeekCutDay(this.getPrePayWeekCutDay());
        registry.setPrePayWeekLag(this.getPrePayWeekLag());
        registry.setTimeClockPol(this.getTimeClockPol());
        registry.setFortnightStandard(this.isFortnightStandard());
        registry.setAutoVacationBonus(this.isAutoVacationBonus());
        registry.setTaxSubsidyEarning(this.isTaxSubsidyEarning());
        registry.setTaxNet(this.isTaxNet());
        registry.setBankAccountUse(this.isBankAccountUse());
        registry.setDeleted(this.isDeleted());
        registry.setFkMwzTypeId(this.getFkMwzTypeId());
        registry.setFkMwzReferenceTypeId(this.getFkMwzReferenceTypeId());
        registry.setFkTaxComputationTypeId(this.getFkTaxComputationTypeId());
        registry.setFkBankId(this.getFkBankId());
        registry.setFkEarningEarningId_n(this.getFkEarningEarningId_n());
        registry.setFkEarningVacationId_n(this.getFkEarningVacationId_n());
        registry.setFkEarningTaxId_n(this.getFkEarningTaxId_n());
        registry.setFkEarningTaxSubsidyId_n(this.getFkEarningTaxSubsidyId_n());
        registry.setFkEarningTaxSubsidyCompensatedId_n(this.getFkEarningTaxSubsidyCompensatedId_n());
        registry.setFkEarningSsContributionId_n(this.getFkEarningSsContributionId_n());
        registry.setFkEarningOvertime2Id_n(this.getFkEarningOvertime2Id_n());
        registry.setFkEarningOvertime3Id_n(this.getFkEarningOvertime3Id_n());
        registry.setFkEarningHolidayId_n(this.getFkEarningHolidayId_n());
        registry.setFkEarningDayOffId_n(this.getFkEarningDayOffId_n());
        registry.setFkEarningSunBonusId_n(this.getFkEarningSunBonusId_n());
        registry.setFkDeductionTaxId_n(this.getFkDeductionTaxId_n());
        registry.setFkDeductionTaxSubsidyId_n(this.getFkDeductionTaxSubsidyId_n());
        registry.setFkDeductionSsContributionId_n(this.getFkDeductionSsContributionId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
