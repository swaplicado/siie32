/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbAbpTax extends SDbRegistryUser implements SAbp {

    private static final int ACC_PUR_PEND = 101;
    private static final int ACC_PUR_PAY = 102;
    private static final int ACC_PUR_ADV = 103;
    private static final int ACC_SAL_PEND = 201;
    private static final int ACC_SAL_PAY = 202;
    private static final int ACC_SAL_ADV = 203;

    private static final String TXT_ACC_PUR_PEND = "COMPRAS PAGOS PENDIENTES";
    private static final String TXT_ACC_PUR_PAY = "COMPRAS PAGOS EFECTIVOS";
    private static final String TXT_ACC_PUR_ADV = "COMPRAS ANTICIPOS";
    private static final String TXT_ACC_SAL_PEND = "VENTAS PAGOS PENDIENTES";
    private static final String TXT_ACC_SAL_PAY = "VENTAS PAGOS EFECTIVOS";
    private static final String TXT_ACC_SAL_ADV = "VENTAS ANTICIPOS";

    protected int mnPkAbpTaxId;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkTaxBasicId;
    protected int mnFkTaxId;
    protected int mnFkAccountPurchasesPendingId;
    protected int mnFkAccountPurchasesPaymentId;
    protected int mnFkAccountPurchasesPaymentAdvanceId;
    protected int mnFkAccountSalesPendingId;
    protected int mnFkAccountSalesPaymentId;
    protected int mnFkAccountSalesPaymentAdvanceId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbAbpTax() {
        super(SModConsts.FIN_ABP_TAX);
    }

    public void setPkAbpTaxId(int n) { mnPkAbpTaxId = n; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkTaxBasicId(int n) { mnFkTaxBasicId = n; }
    public void setFkTaxId(int n) { mnFkTaxId = n; }
    public void setFkAccountPurchasesPendingId(int n) { mnFkAccountPurchasesPendingId = n; }
    public void setFkAccountPurchasesPaymentId(int n) { mnFkAccountPurchasesPaymentId = n; }
    public void setFkAccountPurchasesPaymentAdvanceId(int n) { mnFkAccountPurchasesPaymentAdvanceId = n; }
    public void setFkAccountSalesPendingId(int n) { mnFkAccountSalesPendingId = n; }
    public void setFkAccountSalesPaymentId(int n) { mnFkAccountSalesPaymentId = n; }
    public void setFkAccountSalesPaymentAdvanceId(int n) { mnFkAccountSalesPaymentAdvanceId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpTaxId() { return mnPkAbpTaxId; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkTaxBasicId() { return mnFkTaxBasicId; }
    public int getFkTaxId() { return mnFkTaxId; }
    public int getFkAccountPurchasesPendingId() { return mnFkAccountPurchasesPendingId; }
    public int getFkAccountPurchasesPaymentId() { return mnFkAccountPurchasesPaymentId; }
    public int getFkAccountPurchasesPaymentAdvanceId() { return mnFkAccountPurchasesPaymentAdvanceId; }
    public int getFkAccountSalesPendingId() { return mnFkAccountSalesPendingId; }
    public int getFkAccountSalesPaymentId() { return mnFkAccountSalesPaymentId; }
    public int getFkAccountSalesPaymentAdvanceId() { return mnFkAccountSalesPaymentAdvanceId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpTaxId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpTaxId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpTaxId = 0;
        msName = "";
        mbDeleted = false;
        mnFkTaxBasicId = 0;
        mnFkTaxId = 0;
        mnFkAccountPurchasesPendingId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPurchasesPaymentId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPurchasesPaymentAdvanceId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesPendingId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesPaymentId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesPaymentAdvanceId = SModSysConsts.FIN_ACC_NA;
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
        return "WHERE id_abp_tax = " + mnPkAbpTaxId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_tax = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbpTaxId = 0;

        msSql = "SELECT COALESCE(MAX(id_abp_tax), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbpTaxId = resultSet.getInt(1);
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
            mnPkAbpTaxId = resultSet.getInt("id_abp_tax");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkTaxBasicId = resultSet.getInt("fk_tax_bas");
            mnFkTaxId = resultSet.getInt("fk_tax");
            mnFkAccountPurchasesPendingId = resultSet.getInt("fk_acc_pur_pend");
            mnFkAccountPurchasesPaymentId = resultSet.getInt("fk_acc_pur_pay");
            mnFkAccountPurchasesPaymentAdvanceId = resultSet.getInt("fk_acc_pur_adv");
            mnFkAccountSalesPendingId = resultSet.getInt("fk_acc_sal_pend");
            mnFkAccountSalesPaymentId = resultSet.getInt("fk_acc_sal_pay");
            mnFkAccountSalesPaymentAdvanceId = resultSet.getInt("fk_acc_sal_adv");
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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpTaxId + ", " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkTaxBasicId + ", " +
                    mnFkTaxId + ", " +
                    mnFkAccountPurchasesPendingId + ", " +
                    mnFkAccountPurchasesPaymentId + ", " +
                    mnFkAccountPurchasesPaymentAdvanceId + ", " +
                    mnFkAccountSalesPendingId + ", " +
                    mnFkAccountSalesPaymentId + ", " +
                    mnFkAccountSalesPaymentAdvanceId + ", " +
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
                    "id_abp_tax = " + mnPkAbpTaxId + ", " +
                    */
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tax_bas = " + mnFkTaxBasicId + ", " +
                    "fk_tax = " + mnFkTaxId + ", " +
                    "fk_acc_pur_pend = " + mnFkAccountPurchasesPendingId + ", " +
                    "fk_acc_pur_pay = " + mnFkAccountPurchasesPaymentId + ", " +
                    "fk_acc_pur_adv = " + mnFkAccountPurchasesPaymentAdvanceId + ", " +
                    "fk_acc_sal_pend = " + mnFkAccountSalesPendingId + ", " +
                    "fk_acc_sal_pay = " + mnFkAccountSalesPaymentId + ", " +
                    "fk_acc_sal_adv = " + mnFkAccountSalesPaymentAdvanceId + ", " +
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
    public SDbAbpTax clone() throws CloneNotSupportedException {
        SDbAbpTax registry = new SDbAbpTax();

        registry.setPkAbpTaxId(this.getPkAbpTaxId());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkTaxBasicId(this.getFkTaxBasicId());
        registry.setFkTaxId(this.getFkTaxId());
        registry.setFkAccountPurchasesPendingId(this.getFkAccountPurchasesPendingId());
        registry.setFkAccountPurchasesPaymentId(this.getFkAccountPurchasesPaymentId());
        registry.setFkAccountPurchasesPaymentAdvanceId(this.getFkAccountPurchasesPaymentAdvanceId());
        registry.setFkAccountSalesPendingId(this.getFkAccountSalesPendingId());
        registry.setFkAccountSalesPaymentId(this.getFkAccountSalesPaymentId());
        registry.setFkAccountSalesPaymentAdvanceId(this.getFkAccountSalesPaymentAdvanceId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public HashMap<Integer, SAbpRegistry> getAbpRows(SGuiSession session) {
        HashMap<Integer, SAbpRegistry> rowsMap = new HashMap<Integer, SAbpRegistry>();

        rowsMap.put(ACC_PUR_PEND, new SAbpRegistry(ACC_PUR_PEND, TXT_ACC_PUR_PEND, new int[] { },
                mnFkAccountPurchasesPendingId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesPendingId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesPendingId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_PUR_PAY, new SAbpRegistry(ACC_PUR_PAY, TXT_ACC_PUR_PAY, new int[] { },
                mnFkAccountPurchasesPaymentId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesPaymentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesPaymentId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_PUR_ADV, new SAbpRegistry(ACC_PUR_ADV, TXT_ACC_PUR_ADV, new int[] { },
                mnFkAccountPurchasesPaymentAdvanceId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesPaymentAdvanceId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesPaymentAdvanceId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL_PEND, new SAbpRegistry(ACC_SAL_PEND, TXT_ACC_SAL_PEND, new int[] { },
                mnFkAccountSalesPendingId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesPendingId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesPendingId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL_PAY, new SAbpRegistry(ACC_SAL_PAY, TXT_ACC_SAL_PAY, new int[] { },
                mnFkAccountSalesPaymentId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesPaymentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesPaymentId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL_ADV, new SAbpRegistry(ACC_SAL_ADV, TXT_ACC_SAL_ADV, new int[] { },
                mnFkAccountSalesPaymentAdvanceId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesPaymentAdvanceId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesPaymentAdvanceId }, SDbRegistry.FIELD_NAME)));

        return rowsMap;
    }

    @Override
    public void setAbpRows(HashMap<Integer, SAbpRegistry> rowsMap) {
        SAbpRegistry row = null;

        row = rowsMap.get(ACC_PUR_PEND);
        mnFkAccountPurchasesPendingId = row.getAccountId();

        row = rowsMap.get(ACC_PUR_PAY);
        mnFkAccountPurchasesPaymentId = row.getAccountId();

        row = rowsMap.get(ACC_PUR_ADV);
        mnFkAccountPurchasesPaymentAdvanceId = row.getAccountId();

        row = rowsMap.get(ACC_SAL_PEND);
        mnFkAccountSalesPendingId = row.getAccountId();

        row = rowsMap.get(ACC_SAL_PAY);
        mnFkAccountSalesPaymentId = row.getAccountId();

        row = rowsMap.get(ACC_SAL_ADV);
        mnFkAccountSalesPaymentAdvanceId = row.getAccountId();
    }
}
