/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbAccountingPayrollEmployee extends SDbRegistryUser {

    protected int mnPkPayrollId;
    protected int mnPkAccountingId;
    protected int mnPkEmployeeId;
    protected int mnFkRecordYearId;
    protected int mnFkRecordPeriodId;
    protected int mnFkRecordBookkeepingCenterId;
    protected String msFkRecordRecordTypeId;
    protected int mnFkRecordNumberId;


    public SDbAccountingPayrollEmployee() {
        super(SModConsts.HRS_ACC_PAY_RCP);
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkAccountingId(int n) { mnPkAccountingId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setFkRecordYearId(int n) { mnFkRecordYearId = n; }
    public void setFkRecordPeriodId(int n) { mnFkRecordPeriodId = n; }
    public void setFkRecordBookkeepingCenterId(int n) { mnFkRecordBookkeepingCenterId = n; }
    public void setFkRecordRecordTypeId(String s) { msFkRecordRecordTypeId = s; }
    public void setFkRecordNumberId(int n) { mnFkRecordNumberId = n; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkAccountingId() { return mnPkAccountingId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getFkRecordYearId() { return mnFkRecordYearId; }
    public int getFkRecordPeriodId() { return mnFkRecordPeriodId; }
    public int getFkRecordBookkeepingCenterId() { return mnFkRecordBookkeepingCenterId; }
    public String getFkRecordRecordTypeId() { return msFkRecordRecordTypeId; }
    public int getFkRecordNumberId() { return mnFkRecordNumberId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPayrollId = pk[0];
        mnPkAccountingId = pk[1];
        mnPkEmployeeId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkAccountingId, mnPkEmployeeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPayrollId = 0;
        mnPkAccountingId = 0;
        mnPkEmployeeId = 0;
        mnFkRecordYearId = 0;
        mnFkRecordPeriodId = 0;
        mnFkRecordBookkeepingCenterId = 0;
        msFkRecordRecordTypeId = "";
        mnFkRecordNumberId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPayrollId + " AND "
                + "id_acc = " + mnPkAccountingId + " AND "
                + "id_emp = " + mnPkAccountingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " AND "
                + "id_acc = " + pk[1] + " "
                + "id_emp = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        
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
            mnPkPayrollId = resultSet.getInt("id_pay");
            mnPkAccountingId = resultSet.getInt("id_acc");
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mnFkRecordYearId = resultSet.getInt("fid_rec_year");
            mnFkRecordPeriodId = resultSet.getInt("fid_rec_per");
            mnFkRecordBookkeepingCenterId = resultSet.getInt("fid_rec_bkc");
            msFkRecordRecordTypeId = resultSet.getString("fid_rec_tp_rec");
            mnFkRecordNumberId = resultSet.getInt("fid_rec_num");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPayrollId + ", " + 
                    mnPkAccountingId + ", " + 
                    mnPkEmployeeId + ", " + 
                    mnFkRecordYearId + ", " + 
                    mnFkRecordPeriodId + ", " + 
                    mnFkRecordBookkeepingCenterId + ", " + 
                    "'" + msFkRecordRecordTypeId + "', " + 
                    mnFkRecordNumberId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_pay = " + mnPkPayrollId + ", " +
                    "id_acc = " + mnPkAccountingId + ", " +
                    "id_emp = " + mnPkEmployeeId + ", " +
                    */
                    "fid_rec_year = " + mnFkRecordYearId + ", " +
                    "fid_rec_per = " + mnFkRecordPeriodId + ", " +
                    "fid_rec_bkc = " + mnFkRecordBookkeepingCenterId + ", " +
                    "fid_rec_tp_rec = '" + msFkRecordRecordTypeId + "', " +
                    "fid_rec_num = " + mnFkRecordNumberId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAccountingPayrollEmployee clone() throws CloneNotSupportedException {
        SDbAccountingPayrollEmployee registry = new SDbAccountingPayrollEmployee();

        registry.setPkPayrollId(this.getPkPayrollId());
        registry.setPkAccountingId(this.getPkAccountingId());
        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setFkRecordYearId(this.getFkRecordYearId());
        registry.setFkRecordPeriodId(this.getFkRecordPeriodId());
        registry.setFkRecordBookkeepingCenterId(this.getFkRecordBookkeepingCenterId());
        registry.setFkRecordRecordTypeId(this.getFkRecordRecordTypeId());
        registry.setFkRecordNumberId(this.getFkRecordNumberId());

        return registry;
    }
}
