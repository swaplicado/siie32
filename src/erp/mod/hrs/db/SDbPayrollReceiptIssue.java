/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.ver3.DCfdVer3Consts;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataCfd;
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

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbPayrollReceiptIssue extends SDbRegistryUser {

    public static final int FIELD_NUMBER_SERIES = FIELD_BASE + 1;
    public static final int FIELD_NUMBER = FIELD_BASE + 2;
    public static final int FIELD_DATE_ISSUE = FIELD_BASE + 3;
    public static final int FIELD_DATE_PAYMENT = FIELD_BASE + 4;
    public static final int FIELD_TYPE_PAYMENT_SYS = FIELD_BASE + 5;
    public static final int FIELD_TYPE_UUID_RELATED = FIELD_BASE + 6;
    
    protected int mnPkPayrollId;
    protected int mnPkEmployeeId;
    protected int mnPkIssueId;
    protected String msNumberSeries;
    protected int mnNumber;
    protected Date mtDateIssue;
    protected Date mtDatePayment;
    protected String msBankAccount;
    protected String msUuidRelated;
    protected double mdEarnings_r;
    protected double mdDeductions_r;
    protected double mdPayment_r;
    //protected boolean mbDeleted;
    protected int mnFkReceiptStatusId;
    protected int mnFkBankId_n;
    protected int mnFkPaymentSystemTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected erp.mtrn.data.SDataCfd moDbmsDataCfd;

    public SDbPayrollReceiptIssue() {
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
    public void setUuidRelated(String s) { msUuidRelated = s; }
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
    public String getUuidRelated() { return msUuidRelated; }
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
    
    public java.lang.String getPayrollReceiptIssueNumber() {
        return (msNumberSeries.isEmpty() ? "" : msNumberSeries + "-") + mnNumber;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPayrollId = pk[0];
        mnPkEmployeeId = pk[1];
        mnPkIssueId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId, mnPkIssueId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPayrollId = 0;
        mnPkEmployeeId = 0;
        mnPkIssueId = 0;
        msNumberSeries = "";
        mnNumber = 0;
        mtDateIssue = null;
        mtDatePayment = null;
        msBankAccount = "";
        msUuidRelated = "";
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
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " AND id_iss = " + mnPkIssueId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " AND id_emp = " + pk[1] + " AND id_iss = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkIssueId = 0;

        msSql = "SELECT COALESCE(MAX(id_iss), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkIssueId = resultSet.getInt(1);
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
            mnPkIssueId = resultSet.getInt("id_iss");
            msNumberSeries = resultSet.getString("num_ser");
            mnNumber = resultSet.getInt("num");
            mtDateIssue = resultSet.getDate("dt_iss");
            mtDatePayment = resultSet.getDate("dt_pay");
            msBankAccount = resultSet.getString("bank_acc");
            msUuidRelated = resultSet.getString("uuid_rel");
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

            msSql = "SELECT id_cfd FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " "
                    + "WHERE fid_pay_rcp_pay_n = " + mnPkPayrollId + " AND fid_pay_rcp_emp_n = " + mnPkEmployeeId + " AND fid_pay_rcp_iss_n = " + mnPkIssueId + " ";
            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moDbmsDataCfd = new SDataCfd();
                if (moDbmsDataCfd.read(new int[] { resultSet.getInt("id_cfd") }, statement)!= SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
            }

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
                    mnPkPayrollId + ", " + 
                    mnPkEmployeeId + ", " + 
                    mnPkIssueId + ", " + 
                    "'" + msNumberSeries + "', " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateIssue) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDatePayment) + "', " + 
                    "'" + msBankAccount + "', " + 
                    "'" + msUuidRelated + "', " + 
                    mdEarnings_r + ", " + 
                    mdDeductions_r + ", " + 
                    mdPayment_r + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkReceiptStatusId + ", " + 
                    (mnFkBankId_n > 0 ? mnFkBankId_n : "NULL") + ", " +
                    mnFkPaymentSystemTypeId + ", " + 
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
                    "id_iss = " + mnPkIssueId + ", " +
                    */
                    "num_ser = '" + msNumberSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "dt_iss = '" + SLibUtils.DbmsDateFormatDate.format(mtDateIssue) + "', " +
                    "dt_pay = '" + SLibUtils.DbmsDateFormatDate.format(mtDatePayment) + "', " +
                    "bank_acc = '" + msBankAccount + "', " +
                    "uuid_rel = '" + msUuidRelated + "', " +
                    "ear_r = " + mdEarnings_r + ", " +
                    "ded_r = " + mdDeductions_r + ", " +
                    "pay_r = " + mdPayment_r + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_st_rcp = " + mnFkReceiptStatusId + ", " +
                    "fk_bank_n = " + (mnFkBankId_n > 0 ? mnFkBankId_n : "NULL") + ", " +
                    "fk_tp_pay_sys = " + mnFkPaymentSystemTypeId + ", " +
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
    public SDbPayrollReceiptIssue clone() throws CloneNotSupportedException {
        SDbPayrollReceiptIssue registry = new SDbPayrollReceiptIssue();

        registry.setPkPayrollId(this.getPkPayrollId());
        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkIssueId(this.getPkIssueId());
        registry.setNumberSeries(this.getNumberSeries());
        registry.setNumber(this.getNumber());
        registry.setDateIssue(this.getDateIssue());
        registry.setDatePayment(this.getDatePayment());
        registry.setBankAccount(this.getBankAccount());
        registry.setUuidRelated(this.getUuidRelated());
        registry.setEarnings_r(this.getEarnings_r());
        registry.setDeductions_r(this.getDeductions_r());
        registry.setPayment_r(this.getPayment_r());
        registry.setDeleted(this.isDeleted());
        registry.setFkReceiptStatusId(this.getFkReceiptStatusId());
        registry.setFkBankId_n(this.getFkBankId_n());
        registry.setFkPaymentSystemTypeId(this.getFkPaymentSystemTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case FIELD_NUMBER_SERIES:
                msSql += "num_ser ";
                break;
            case FIELD_NUMBER:
                msSql += "num ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlFromWhere(pk);

        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case FIELD_NUMBER_SERIES:
                    value = resultSet.getString(1);
                    break;
                case FIELD_NUMBER:
                    value = resultSet.getInt(1);
                    break;
                default:
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
        return value;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_NUMBER_SERIES:
                msSql += "num_ser = '" + (String) value + "' ";
                break;
            case FIELD_NUMBER:
                msSql += "num = " + (int) value + " ";
                break;
            case FIELD_DATE_ISSUE:
                msSql += "dt_iss = '" + SLibUtils.DbmsDateFormatDate.format((Date) value) + "' ";
                break;
            case FIELD_DATE_PAYMENT:
                msSql += "dt_pay = '" + SLibUtils.DbmsDateFormatDate.format((Date) value) + "' ";
                break;
            case FIELD_TYPE_PAYMENT_SYS:
                msSql += "fk_tp_pay_sys = " + (int) value + " ";
                break;
            case FIELD_TYPE_UUID_RELATED:
                String uuid = (String) value;
                if (!uuid.isEmpty() && uuid.length() != DCfdVer3Consts.LEN_UUID) {
                    throw new Exception("El UUID '" + uuid + "' debe ser de 36 caracteres.");
                }
                msSql += "uuid_rel = '" + uuid + "' ";
                break;

            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);

        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
