/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

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
public class SDbFiscalAccountLinkDetail extends SDbRegistryUser {

    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkAccountId;
    protected int mnPkFiscalAccountLinkTypeId;
    protected int mnPkReference1Id;
    protected int mnPkReference2Id;
    protected String msFiscalAccount;
    protected String msAccountCode;
    protected String msAccountCodeParent;
    protected String msAccountName;
    protected String msNature;
    protected int mnLevel;

    public SDbFiscalAccountLinkDetail() {
        super(SModConsts.FIN_FISCAL_ACC_LINK_DET);
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setPkAccountId(int n) { mnPkAccountId = n; }
    public void setPkFiscalAccountLinkTypeId(int n) { mnPkFiscalAccountLinkTypeId = n; }
    public void setPkReference1Id(int n) { mnPkReference1Id = n; }
    public void setPkReference2Id(int n) { mnPkReference2Id = n; }
    public void setFiscalAccount(String s) { msFiscalAccount = s; }
    public void setAccountCode(String s) { msAccountCode = s; }
    public void setAccountCodeParent(String s) { msAccountCodeParent = s; }
    public void setAccountName(String s) { msAccountName = s; }
    public void setNature(String s) { msNature = s; }
    public void setLevel(int n) { mnLevel = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkAccountId() { return mnPkAccountId; }
    public int getPkFiscalAccountLinkTypeId() { return mnPkFiscalAccountLinkTypeId; }
    public int getPkReference1Id() { return mnPkReference1Id; }
    public int getPkReference2Id() { return mnPkReference2Id; }
    public String getFiscalAccount() { return msFiscalAccount; }
    public String getAccountCode() { return msAccountCode; }
    public String getAccountCodeParent() { return msAccountCodeParent; }
    public String getAccountName() { return msAccountName; }
    public String getNature() { return msNature; }
    public int getLevel() { return mnLevel; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkPeriodId = pk[1];
        mnPkAccountId = pk[2];
        mnPkFiscalAccountLinkTypeId = pk[3];
        mnPkReference1Id = pk[4];
        mnPkReference2Id = pk[5];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkPeriodId, mnPkAccountId, mnPkFiscalAccountLinkTypeId, mnPkReference1Id, mnPkReference2Id };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mnPkAccountId = 0;
        mnPkFiscalAccountLinkTypeId = 0;
        mnPkReference1Id = 0;
        mnPkReference2Id = 0;
        msFiscalAccount = "";
        msAccountCode = "";
        msAccountCodeParent = "";
        msAccountName = "";
        msNature = "";
        mnLevel = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " AND " +
                "id_per = " + mnPkPeriodId + " AND " +
                "id_acc = " + mnPkAccountId + " AND " +
                "id_tp_fiscal_acc_link = " + mnPkFiscalAccountLinkTypeId + " AND " +
                "id_ref_1 = " + mnPkReference1Id + " AND " +
                "id_ref_2 = " + mnPkReference2Id + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND " +
                "id_per = " + pk[1] + " AND " +
                "id_acc = " + pk[2] + " AND " +
                "id_tp_fiscal_acc_link = " + pk[3] + " AND " +
                "id_ref_1 = " + pk[4] + " AND " +
                "id_ref_2 = " + pk[5] + " ";
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
            mnPkYearId = resultSet.getInt("id_year");
            mnPkPeriodId = resultSet.getInt("id_per");
            mnPkAccountId = resultSet.getInt("id_acc");
            mnPkFiscalAccountLinkTypeId = resultSet.getInt("id_tp_fiscal_acc_link");
            mnPkReference1Id = resultSet.getInt("id_ref_1");
            mnPkReference2Id = resultSet.getInt("id_ref_2");
            msFiscalAccount = resultSet.getString("fiscal_acc");
            msAccountCode = resultSet.getString("acc_code");
            msAccountCodeParent = resultSet.getString("acc_code_par");
            msAccountName = resultSet.getString("acc_name");
            msNature = resultSet.getString("nat");
            mnLevel = resultSet.getInt("lev");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        verifyRegistryNew(session);

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkYearId + ", " +
                    mnPkPeriodId + ", " +
                    mnPkAccountId + ", " +
                    mnPkFiscalAccountLinkTypeId + ", " +
                    mnPkReference1Id + ", " +
                    mnPkReference2Id + ", " +
                    "'" + msFiscalAccount + "', " +
                    "'" + msAccountCode + "', " +
                    "'" + msAccountCodeParent + "', " +
                    "'" + msAccountName + "', " +
                    "'" + msNature + "', " +
                    mnLevel + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_year = " + mnPkYearId + ", " +
                    //"id_per = " + mnPkPeriodId + ", " +
                    //"id_acc = " + mnPkAccountId + ", " +
                    //"id_tp_fiscal_acc_link = " + mnPkFiscalAccountLinkTypeId + ", " +
                    //"id_ref_1 = " + mnPkReference1Id + ", " +
                    //"id_ref_2 = " + mnPkReference2Id + ", " +
                    "fiscal_acc = '" + msFiscalAccount + "', " +
                    "acc_code = '" + msAccountCode + "', " +
                    "acc_code_par = '" + msAccountCodeParent + "', " +
                    "acc_name = '" + msAccountName + "', " +
                    "nat = '" + msNature + "', " +
                    "lev = " + mnLevel + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbFiscalAccountLinkDetail clone() throws CloneNotSupportedException {
        SDbFiscalAccountLinkDetail registry = new SDbFiscalAccountLinkDetail();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkPeriodId(this.getPkPeriodId());
        registry.setPkAccountId(this.getPkAccountId());
        registry.setPkFiscalAccountLinkTypeId(this.getPkFiscalAccountLinkTypeId());
        registry.setPkReference1Id(this.getPkReference1Id());
        registry.setPkReference2Id(this.getPkReference2Id());
        registry.setFiscalAccount(this.getFiscalAccount());
        registry.setAccountCode(this.getAccountCode());
        registry.setAccountCodeParent(this.getAccountCodeParent());
        registry.setAccountName(this.getAccountName());
        registry.setNature(this.getNature());
        registry.setLevel(this.getLevel());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
