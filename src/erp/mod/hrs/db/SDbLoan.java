/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbLoan extends SDbRegistryUser {
    
    public static final String ONLY_PLAIN_LOANS_HAVE_BALANCE = "Solamente los préstamos tienen saldo.";

    protected int mnPkEmployeeId;
    protected int mnPkLoanId;
    protected String msNumber;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    protected double mdCapital;
    protected double mdTotalAmount;
    protected double mdPaymentAmount;
    protected double mdPaymentFixedFees;
    protected double mdPaymentUmas;
    protected double mdPaymentUmis;
    protected int mnPaymentPercentageReference;
    protected double mdPaymentPercentage;
    protected double mdPaymentPercentageAmount;
    protected boolean mbClosed;
    //protected boolean mbDeleted;
    protected int mnFkLoanTypeId;
    protected int mnFkLoanPaymentTypeId;
    protected int mnFkUserClosedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserClosed;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    private String msXtaLoanType;
    
    public SDbLoan() {
        super(SModConsts.HRS_LOAN);
    }
    
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkLoanId(int n) { mnPkLoanId = n; }
    public void setNumber(String s) { msNumber = s; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setCapital(double d) { mdCapital = d; }
    public void setTotalAmount(double d) { mdTotalAmount = d; }
    public void setPaymentAmount(double d) { mdPaymentAmount = d; }
    public void setPaymentFixedFees(double d) { mdPaymentFixedFees = d; }
    public void setPaymentUmas(double d) { mdPaymentUmas = d; }
    public void setPaymentUmis(double d) { mdPaymentUmis = d; }
    public void setPaymentPercentageReference(int n) { mnPaymentPercentageReference = n; }
    public void setPaymentPercentage(double d) { mdPaymentPercentage = d; }
    public void setPaymentPercentageAmount(double d) { mdPaymentPercentageAmount = d; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkLoanTypeId(int n) { mnFkLoanTypeId = n; }
    public void setFkLoanPaymentTypeId(int n) { mnFkLoanPaymentTypeId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkLoanId() { return mnPkLoanId; }
    public String getNumber() { return msNumber; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public double getCapital() { return mdCapital; }
    public double getTotalAmount() { return mdTotalAmount; }
    public double getPaymentAmount() { return mdPaymentAmount; }
    public double getPaymentFixedFees() { return mdPaymentFixedFees; }
    public double getPaymentUmas() { return mdPaymentUmas; }
    public double getPaymentUmis() { return mdPaymentUmis; }
    public int getPaymentPercentageReference() { return mnPaymentPercentageReference; }
    public double getPaymentPercentage() { return mdPaymentPercentage; }
    public double getPaymentPercentageAmount() { return mdPaymentPercentageAmount; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkLoanTypeId() { return mnFkLoanTypeId; }
    public int getFkLoanPaymentTypeId() { return mnFkLoanPaymentTypeId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosed() { return mtTsUserClosed; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public String composeLoanDescription() {
        return msXtaLoanType + ", " + msNumber;
    }
    
    public boolean isPlainLoan() {
        return SLibUtils.belongsTo(mnFkLoanTypeId, new int[] { SModSysConsts.HRSS_TP_LOAN_LOAN_COM, SModSysConsts.HRSS_TP_LOAN_LOAN_UNI, SModSysConsts.HRSS_TP_LOAN_LOAN_3RD, SModSysConsts.HRSS_TP_LOAN_LOAN_GLASS, SModSysConsts.HRSS_TP_LOAN_LOAN_SCHOOL });
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkLoanId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkLoanId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkEmployeeId = 0;
        mnPkLoanId = 0;
        msNumber = "";
        mtDateStart = null;
        mtDateEnd_n = null;
        mdCapital = 0;
        mdTotalAmount = 0;
        mdPaymentAmount = 0;
        mdPaymentFixedFees = 0;
        mdPaymentUmas = 0;
        mdPaymentUmis = 0;
        mnPaymentPercentageReference = 0;
        mdPaymentPercentage = 0;
        mdPaymentPercentageAmount = 0;
        mbClosed = false;
        mbDeleted = false;
        mnFkLoanTypeId = 0;
        mnFkLoanPaymentTypeId = 0;
        mnFkUserClosedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosed = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msXtaLoanType = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_emp = " + mnPkEmployeeId + " AND id_loan = " + mnPkLoanId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " AND " +
                "id_loan = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLoanId = 0;

        msSql = "SELECT COALESCE(MAX(id_loan), 0) + 1 FROM " + getSqlTable() + " WHERE id_emp = " + mnPkEmployeeId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLoanId = resultSet.getInt(1);
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
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mnPkLoanId = resultSet.getInt("id_loan");
            msNumber = resultSet.getString("num");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            mdCapital = resultSet.getDouble("cap");
            mdTotalAmount = resultSet.getDouble("tot_amt");
            mdPaymentAmount = resultSet.getDouble("pay_amt");
            mdPaymentFixedFees = resultSet.getDouble("pay_fix");
            mdPaymentUmas = resultSet.getDouble("pay_uma");
            mdPaymentUmis = resultSet.getDouble("pay_umi");
            mnPaymentPercentageReference = resultSet.getInt("pay_per_ref");
            mdPaymentPercentage = resultSet.getDouble("pay_per");
            mdPaymentPercentageAmount = resultSet.getDouble("pay_per_amt");
            mbClosed = resultSet.getBoolean("b_clo");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkLoanTypeId = resultSet.getInt("fk_tp_loan");
            mnFkLoanPaymentTypeId = resultSet.getInt("fk_tp_loan_pay");
            mnFkUserClosedId = resultSet.getInt("fk_usr_clo");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserClosed = resultSet.getTimestamp("ts_usr_clo");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            msXtaLoanType = (String) session.readField(SModConsts.HRSS_TP_LOAN, new int[] { mnFkLoanTypeId }, SDbRegistry.FIELD_NAME);
            
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
            mbClosed = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " + 
                    mnPkLoanId + ", " + 
                    "'" + msNumber + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    (mtDateEnd_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n)+ "' ") + ", " +
                    mdCapital + ", " + 
                    mdTotalAmount + ", " + 
                    mdPaymentAmount + ", " + 
                    mdPaymentFixedFees + ", " + 
                    mdPaymentUmas + ", " + 
                    mdPaymentUmis + ", " + 
                    mnPaymentPercentageReference + ", " + 
                    mdPaymentPercentage + ", " + 
                    mdPaymentPercentageAmount + ", " + 
                    (mbClosed ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkLoanTypeId + ", " + 
                    mnFkLoanPaymentTypeId + ", " + 
                    (mnFkUserClosedId == SLibConsts.UNDEFINED ? mnFkUserInsertId : mnFkUserClosedId) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_emp = " + mnPkEmployeeId + ", " +
                    "id_loan = " + mnPkLoanId + ", " +
                    */
                    "num = '" + msNumber + "', " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = " + (mtDateEnd_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n)+ "' ") + ", " +
                    "cap = " + mdCapital + ", " +
                    "tot_amt = " + mdTotalAmount + ", " +
                    "pay_amt = " + mdPaymentAmount + ", " +
                    "pay_fix = " + mdPaymentFixedFees + ", " +
                    "pay_uma = " + mdPaymentUmas + ", " +
                    "pay_umi = " + mdPaymentUmis + ", " +
                    "pay_per_ref = " + mnPaymentPercentageReference + ", " +
                    "pay_per = " + mdPaymentPercentage + ", " +
                    "pay_per_amt = " + mdPaymentPercentageAmount + ", " +
                    "b_clo = " + (mbClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_loan = " + mnFkLoanTypeId + ", " +
                    "fk_tp_loan_pay = " + mnFkLoanPaymentTypeId + ", " +
                    "fk_usr_clo = " + mnFkUserClosedId  + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_clo = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbLoan clone() throws CloneNotSupportedException {
        SDbLoan registry = new SDbLoan();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkLoanId(this.getPkLoanId());
        registry.setNumber(this.getNumber());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setCapital(this.getCapital());
        registry.setTotalAmount(this.getTotalAmount());
        registry.setPaymentAmount(this.getPaymentAmount());
        registry.setPaymentFixedFees(this.getPaymentFixedFees());
        registry.setPaymentUmas(this.getPaymentUmas());
        registry.setPaymentUmis(this.getPaymentUmis());
        registry.setPaymentPercentageReference(this.getPaymentPercentageReference());
        registry.setPaymentPercentage(this.getPaymentPercentage());
        registry.setPaymentPercentageAmount(this.getPaymentPercentageAmount());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setFkLoanTypeId(this.getFkLoanTypeId());
        registry.setFkLoanPaymentTypeId(this.getFkLoanPaymentTypeId());
        registry.setFkUserClosedId(this.getFkUserClosedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosed(this.getTsUserClosed());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
