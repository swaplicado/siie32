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
 * @author Juan Barajas, Sergio Flores
 */
public class SDbAbpItem extends SDbRegistryUser implements SAbp {

    private static final int ACC_IN_PUR = 111;
    private static final int ACC_IN_PUR_CHA = 112;
    private static final int ACC_IN_PUR_WAR = 113;
    private static final int ACC_IN_PUR_CSG = 114;
    private static final int ACC_IN_SAL = 121;
    private static final int ACC_IN_SAL_CHA = 122;
    private static final int ACC_IN_SAL_WAR = 123;
    private static final int ACC_IN_SAL_CSG = 124;
    private static final int ACC_IN_ADJ = 131;
    private static final int ACC_IN_INV = 132;
    private static final int ACC_OUT_PUR = 211;
    private static final int ACC_OUT_PUR_CHA = 212;
    private static final int ACC_OUT_PUR_WAR = 213;
    private static final int ACC_OUT_PUR_CSG = 214;
    private static final int ACC_OUT_SAL = 221;
    private static final int ACC_OUT_SAL_CHA = 222;
    private static final int ACC_OUT_SAL_WAR = 223;
    private static final int ACC_OUT_SAL_CSG = 224;
    private static final int ACC_OUT_ADJ = 231;
    private static final int ACC_OUT_INV = 232;
    private static final int ACC_PUR = 301;
    private static final int ACC_PUR_INC_INC = 311;
    private static final int ACC_PUR_INC_ADD = 312;
    private static final int ACC_PUR_DEC_DIS = 313;
    private static final int ACC_PUR_DEC_RET = 314;
    private static final int ACC_SAL = 401;
    private static final int ACC_SAL_INC_INC = 411;
    private static final int ACC_SAL_INC_ADD = 412;
    private static final int ACC_SAL_DEC_DIS = 412;
    private static final int ACC_SAL_DEC_RET = 414;

    private static final String TXT_ACC_IN_PUR = "ENTRADA X COMPRAS";
    private static final String TXT_ACC_IN_PUR_CHA = "ENTRADA X CAMBIOS COMPRAS";
    private static final String TXT_ACC_IN_PUR_WAR = "ENTRADA X GARANTÍAS COMPRAS";
    private static final String TXT_ACC_IN_PUR_CSG = "ENTRADA X CONSIGNACIONES COMPRAS";
    private static final String TXT_ACC_IN_SAL = "ENTRADA X VENTAS";
    private static final String TXT_ACC_IN_SAL_CHA = "ENTRADA X CAMBIOS VENTAS";
    private static final String TXT_ACC_IN_SAL_WAR = "ENTRADA X GARANTÍAS VENTAS";
    private static final String TXT_ACC_IN_SAL_CSG = "ENTRADA X CONSIGNACIONES VENTAS";
    private static final String TXT_ACC_IN_ADJ = "ENTRADA X AJUSTES";
    private static final String TXT_ACC_IN_INV = "ENTRADA X INVENTARIOS";
    private static final String TXT_ACC_OUT_PUR = "SALIDA X COMPRAS";
    private static final String TXT_ACC_OUT_PUR_CHA = "SALIDA X CAMBIOS COMPRAS";
    private static final String TXT_ACC_OUT_PUR_WAR = "SALIDA X GARANTÍAS COMPRAS";
    private static final String TXT_ACC_OUT_PUR_CSG = "SALIDA X CONSIGNACIONES COMPRAS";
    private static final String TXT_ACC_OUT_SAL = "SALIDA X VENTAS";
    private static final String TXT_ACC_OUT_SAL_CHA = "SALIDA X CAMBIOS VENTAS";
    private static final String TXT_ACC_OUT_SAL_WAR = "SALIDA X GARANTÍAS VENTAS";
    private static final String TXT_ACC_OUT_SAL_CSG = "SALIDA X CONSIGNACIONES VENTAS";
    private static final String TXT_ACC_OUT_ADJ = "SALIDA X AJUSTES";
    private static final String TXT_ACC_OUT_INV = "SALIDA X INVENTARIOS";
    private static final String TXT_ACC_PUR = "COMPRAS";
    private static final String TXT_ACC_PUR_INC_INC = "INCREMENTOS COMPRAS";
    private static final String TXT_ACC_PUR_INC_ADD = "ADICIONES COMPRAS";
    private static final String TXT_ACC_PUR_DEC_DIS = "DESCUENTOS COMPRAS";
    private static final String TXT_ACC_PUR_DEC_RET = "DEVOLUCIONES COMPRAS";
    private static final String TXT_ACC_SAL = "VENTAS";
    private static final String TXT_ACC_SAL_INC_INC = "INCREMENTOS VENTAS";
    private static final String TXT_ACC_SAL_INC_ADD = "ADICIONES VENTAS";
    private static final String TXT_ACC_SAL_DEC_DIS = "DESCUENTOS VENTAS";
    private static final String TXT_ACC_SAL_DEC_RET = "DEVOLUCIONES VENTAS";

    protected int mnPkAbpItemId;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkAccountInPurchasesId;
    protected int mnFkAccountInPurchasesChangeId;
    protected int mnFkAccountInPurchasesWarrantyId;
    protected int mnFkAccountInPurchasesConsignationId;
    protected int mnFkAccountInSalesId;
    protected int mnFkAccountInSalesChangeId;
    protected int mnFkAccountInSalesWarrantyId;
    protected int mnFkAccountInSalesConsignationId;
    protected int mnFkAccountInAdjustmentId;
    protected int mnFkAccountInInventoryId;
    protected int mnFkAccountOutPurchasesId;
    protected int mnFkAccountOutPurchasesChangeId;
    protected int mnFkAccountOutPurchasesWarrantyId;
    protected int mnFkAccountOutPurchasesConsignationId;
    protected int mnFkAccountOutSalesId;
    protected int mnFkAccountOutSalesChangeId;
    protected int mnFkAccountOutSalesWarrantyId;
    protected int mnFkAccountOutSalesConsignationId;
    protected int mnFkAccountOutAdjustmentId;
    protected int mnFkAccountOutInventoryId;
    protected int mnFkAccountPurchasesId;
    protected int mnFkAccountPurchasesIncIncrementId;
    protected int mnFkAccountPurchasesIncAdditionId;
    protected int mnFkAccountPurchasesDecDiscountId;
    protected int mnFkAccountPurchasesDecReturnId;
    protected int mnFkAccountSalesId;
    protected int mnFkAccountSalesIncIncrementId;
    protected int mnFkAccountSalesIncAdditionId;
    protected int mnFkAccountSalesDecDiscountId;
    protected int mnFkAccountSalesDecReturnId;
    protected int mnFkCostCenterInAdjustmentId;
    protected int mnFkCostCenterInInventoryId;
    protected int mnFkCostCenterPurchasesId;
    protected int mnFkCostCenterPurchasesIncIncrementId;
    protected int mnFkCostCenterPurchasesIncAdditionId;
    protected int mnFkCostCenterPurchasesDecDiscountId;
    protected int mnFkCostCenterPurchasesDecReturnId;
    protected int mnFkCostCenterOutAdjustmentId;
    protected int mnFkCostCenterOutInventoryId;
    protected int mnFkCostCenterSalesId;
    protected int mnFkCostCenterSalesIncIncrementId;
    protected int mnFkCostCenterSalesIncAdditionId;
    protected int mnFkCostCenterSalesDecDiscountId;
    protected int mnFkCostCenterSalesDecReturnId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbAbpItem() {
        super(SModConsts.FIN_ABP_ITEM);
    }

    public void setPkAbpItemId(int n) { mnPkAbpItemId = n; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAccountInPurchasesId(int n) { mnFkAccountInPurchasesId = n; }
    public void setFkAccountInPurchasesChangeId(int n) { mnFkAccountInPurchasesChangeId = n; }
    public void setFkAccountInPurchasesWarrantyId(int n) { mnFkAccountInPurchasesWarrantyId = n; }
    public void setFkAccountInPurchasesConsignationId(int n) { mnFkAccountInPurchasesConsignationId = n; }
    public void setFkAccountInSalesId(int n) { mnFkAccountInSalesId = n; }
    public void setFkAccountInSalesChangeId(int n) { mnFkAccountInSalesChangeId = n; }
    public void setFkAccountInSalesWarrantyId(int n) { mnFkAccountInSalesWarrantyId = n; }
    public void setFkAccountInSalesConsignationId(int n) { mnFkAccountInSalesConsignationId = n; }
    public void setFkAccountInAdjustmentId(int n) { mnFkAccountInAdjustmentId = n; }
    public void setFkAccountInInventoryId(int n) { mnFkAccountInInventoryId = n; }
    public void setFkAccountOutPurchasesId(int n) { mnFkAccountOutPurchasesId = n; }
    public void setFkAccountOutPurchasesChangeId(int n) { mnFkAccountOutPurchasesChangeId = n; }
    public void setFkAccountOutPurchasesWarrantyId(int n) { mnFkAccountOutPurchasesWarrantyId = n; }
    public void setFkAccountOutPurchasesConsignationId(int n) { mnFkAccountOutPurchasesConsignationId = n; }
    public void setFkAccountOutSalesId(int n) { mnFkAccountOutSalesId = n; }
    public void setFkAccountOutSalesChangeId(int n) { mnFkAccountOutSalesChangeId = n; }
    public void setFkAccountOutSalesWarrantyId(int n) { mnFkAccountOutSalesWarrantyId = n; }
    public void setFkAccountOutSalesConsignationId(int n) { mnFkAccountOutSalesConsignationId = n; }
    public void setFkAccountOutAdjustmentId(int n) { mnFkAccountOutAdjustmentId = n; }
    public void setFkAccountOutInventoryId(int n) { mnFkAccountOutInventoryId = n; }
    public void setFkAccountPurchasesId(int n) { mnFkAccountPurchasesId = n; }
    public void setFkAccountPurchasesIncIncrementId(int n) { mnFkAccountPurchasesIncIncrementId = n; }
    public void setFkAccountPurchasesIncAdditionId(int n) { mnFkAccountPurchasesIncAdditionId = n; }
    public void setFkAccountPurchasesDecDiscountId(int n) { mnFkAccountPurchasesDecDiscountId = n; }
    public void setFkAccountPurchasesDecReturnId(int n) { mnFkAccountPurchasesDecReturnId = n; }
    public void setFkAccountSalesId(int n) { mnFkAccountSalesId = n; }
    public void setFkAccountSalesIncIncrementId(int n) { mnFkAccountSalesIncIncrementId = n; }
    public void setFkAccountSalesIncAdditionId(int n) { mnFkAccountSalesIncAdditionId = n; }
    public void setFkAccountSalesDecDiscountId(int n) { mnFkAccountSalesDecDiscountId = n; }
    public void setFkAccountSalesDecReturnId(int n) { mnFkAccountSalesDecReturnId = n; }
    public void setFkCostCenterInAdjustmentId(int n) { mnFkCostCenterInAdjustmentId = n; }
    public void setFkCostCenterInInventoryId(int n) { mnFkCostCenterInInventoryId = n; }
    public void setFkCostCenterPurchasesId(int n) { mnFkCostCenterPurchasesId = n; }
    public void setFkCostCenterPurchasesIncIncrementId(int n) { mnFkCostCenterPurchasesIncIncrementId = n; }
    public void setFkCostCenterPurchasesIncAdditionId(int n) { mnFkCostCenterPurchasesIncAdditionId = n; }
    public void setFkCostCenterPurchasesDecDiscountId(int n) { mnFkCostCenterPurchasesDecDiscountId = n; }
    public void setFkCostCenterPurchasesDecReturnId(int n) { mnFkCostCenterPurchasesDecReturnId = n; }
    public void setFkCostCenterOutAdjustmentId(int n) { mnFkCostCenterOutAdjustmentId = n; }
    public void setFkCostCenterOutInventoryId(int n) { mnFkCostCenterOutInventoryId = n; }
    public void setFkCostCenterSalesId(int n) { mnFkCostCenterSalesId = n; }
    public void setFkCostCenterSalesIncIncrementId(int n) { mnFkCostCenterSalesIncIncrementId = n; }
    public void setFkCostCenterSalesIncAdditionId(int n) { mnFkCostCenterSalesIncAdditionId = n; }
    public void setFkCostCenterSalesDecDiscountId(int n) { mnFkCostCenterSalesDecDiscountId = n; }
    public void setFkCostCenterSalesDecReturnId(int n) { mnFkCostCenterSalesDecReturnId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpItemId() { return mnPkAbpItemId; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAccountInPurchasesId() { return mnFkAccountInPurchasesId; }
    public int getFkAccountInPurchasesChangeId() { return mnFkAccountInPurchasesChangeId; }
    public int getFkAccountInPurchasesWarrantyId() { return mnFkAccountInPurchasesWarrantyId; }
    public int getFkAccountInPurchasesConsignationId() { return mnFkAccountInPurchasesConsignationId; }
    public int getFkAccountInSalesId() { return mnFkAccountInSalesId; }
    public int getFkAccountInSalesChangeId() { return mnFkAccountInSalesChangeId; }
    public int getFkAccountInSalesWarrantyId() { return mnFkAccountInSalesWarrantyId; }
    public int getFkAccountInSalesConsignationId() { return mnFkAccountInSalesConsignationId; }
    public int getFkAccountInAdjustmentId() { return mnFkAccountInAdjustmentId; }
    public int getFkAccountInInventoryId() { return mnFkAccountInInventoryId; }
    public int getFkAccountOutPurchasesId() { return mnFkAccountOutPurchasesId; }
    public int getFkAccountOutPurchasesChangeId() { return mnFkAccountOutPurchasesChangeId; }
    public int getFkAccountOutPurchasesWarrantyId() { return mnFkAccountOutPurchasesWarrantyId; }
    public int getFkAccountOutPurchasesConsignationId() { return mnFkAccountOutPurchasesConsignationId; }
    public int getFkAccountOutSalesId() { return mnFkAccountOutSalesId; }
    public int getFkAccountOutSalesChangeId() { return mnFkAccountOutSalesChangeId; }
    public int getFkAccountOutSalesWarrantyId() { return mnFkAccountOutSalesWarrantyId; }
    public int getFkAccountOutSalesConsignationId() { return mnFkAccountOutSalesConsignationId; }
    public int getFkAccountOutAdjustmentId() { return mnFkAccountOutAdjustmentId; }
    public int getFkAccountOutInventoryId() { return mnFkAccountOutInventoryId; }
    public int getFkAccountPurchasesId() { return mnFkAccountPurchasesId; }
    public int getFkAccountPurchasesIncIncrementId() { return mnFkAccountPurchasesIncIncrementId; }
    public int getFkAccountPurchasesIncAdditionId() { return mnFkAccountPurchasesIncAdditionId; }
    public int getFkAccountPurchasesDecDiscountId() { return mnFkAccountPurchasesDecDiscountId; }
    public int getFkAccountPurchasesDecReturnId() { return mnFkAccountPurchasesDecReturnId; }
    public int getFkAccountSalesId() { return mnFkAccountSalesId; }
    public int getFkAccountSalesIncIncrementId() { return mnFkAccountSalesIncIncrementId; }
    public int getFkAccountSalesIncAdditionId() { return mnFkAccountSalesIncAdditionId; }
    public int getFkAccountSalesDecDiscountId() { return mnFkAccountSalesDecDiscountId; }
    public int getFkAccountSalesDecReturnId() { return mnFkAccountSalesDecReturnId; }
    public int getFkCostCenterInAdjustmentId() { return mnFkCostCenterInAdjustmentId; }
    public int getFkCostCenterInInventoryId() { return mnFkCostCenterInInventoryId; }
    public int getFkCostCenterPurchasesId() { return mnFkCostCenterPurchasesId; }
    public int getFkCostCenterPurchasesIncIncrementId() { return mnFkCostCenterPurchasesIncIncrementId; }
    public int getFkCostCenterPurchasesIncAdditionId() { return mnFkCostCenterPurchasesIncAdditionId; }
    public int getFkCostCenterPurchasesDecDiscountId() { return mnFkCostCenterPurchasesDecDiscountId; }
    public int getFkCostCenterPurchasesDecReturnId() { return mnFkCostCenterPurchasesDecReturnId; }
    public int getFkCostCenterOutAdjustmentId() { return mnFkCostCenterOutAdjustmentId; }
    public int getFkCostCenterOutInventoryId() { return mnFkCostCenterOutInventoryId; }
    public int getFkCostCenterSalesId() { return mnFkCostCenterSalesId; }
    public int getFkCostCenterSalesIncIncrementId() { return mnFkCostCenterSalesIncIncrementId; }
    public int getFkCostCenterSalesIncAdditionId() { return mnFkCostCenterSalesIncAdditionId; }
    public int getFkCostCenterSalesDecDiscountId() { return mnFkCostCenterSalesDecDiscountId; }
    public int getFkCostCenterSalesDecReturnId() { return mnFkCostCenterSalesDecReturnId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpItemId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpItemId = 0;
        msName = "";
        mbDeleted = false;
        mnFkAccountInPurchasesId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInPurchasesChangeId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInPurchasesWarrantyId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInPurchasesConsignationId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInSalesId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInSalesChangeId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInSalesWarrantyId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInSalesConsignationId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountInInventoryId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutPurchasesId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutPurchasesChangeId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutPurchasesWarrantyId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutPurchasesConsignationId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutSalesId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutSalesChangeId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutSalesWarrantyId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutSalesConsignationId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountOutInventoryId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPurchasesId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPurchasesIncIncrementId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPurchasesIncAdditionId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPurchasesDecDiscountId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPurchasesDecReturnId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesIncIncrementId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesIncAdditionId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesDecDiscountId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountSalesDecReturnId = SModSysConsts.FIN_ACC_NA;
        mnFkCostCenterInAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterInInventoryId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterPurchasesId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterPurchasesIncIncrementId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterPurchasesIncAdditionId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterPurchasesDecDiscountId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterPurchasesDecReturnId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterOutAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterOutInventoryId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterSalesId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterSalesIncIncrementId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterSalesIncAdditionId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterSalesDecDiscountId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterSalesDecReturnId = SModSysConsts.FIN_CC_NA;
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
        return "WHERE id_abp_item = " + mnPkAbpItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_item = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbpItemId = 0;

        msSql = "SELECT COALESCE(MAX(id_abp_item), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbpItemId = resultSet.getInt(1);
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
            mnPkAbpItemId = resultSet.getInt("id_abp_item");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAccountInPurchasesId = resultSet.getInt("fk_acc_in_pur");
            mnFkAccountInPurchasesChangeId = resultSet.getInt("fk_acc_in_pur_chg");
            mnFkAccountInPurchasesWarrantyId = resultSet.getInt("fk_acc_in_pur_war");
            mnFkAccountInPurchasesConsignationId = resultSet.getInt("fk_acc_in_pur_csg");
            mnFkAccountInSalesId = resultSet.getInt("fk_acc_in_sal");
            mnFkAccountInSalesChangeId = resultSet.getInt("fk_acc_in_sal_chg");
            mnFkAccountInSalesWarrantyId = resultSet.getInt("fk_acc_in_sal_war");
            mnFkAccountInSalesConsignationId = resultSet.getInt("fk_acc_in_sal_csg");
            mnFkAccountInAdjustmentId = resultSet.getInt("fk_acc_in_adj");
            mnFkAccountInInventoryId = resultSet.getInt("fk_acc_in_inv");
            mnFkAccountOutPurchasesId = resultSet.getInt("fk_acc_out_pur");
            mnFkAccountOutPurchasesChangeId = resultSet.getInt("fk_acc_out_pur_chg");
            mnFkAccountOutPurchasesWarrantyId = resultSet.getInt("fk_acc_out_pur_war");
            mnFkAccountOutPurchasesConsignationId = resultSet.getInt("fk_acc_out_pur_csg");
            mnFkAccountOutSalesId = resultSet.getInt("fk_acc_out_sal");
            mnFkAccountOutSalesChangeId = resultSet.getInt("fk_acc_out_sal_chg");
            mnFkAccountOutSalesWarrantyId = resultSet.getInt("fk_acc_out_sal_war");
            mnFkAccountOutSalesConsignationId = resultSet.getInt("fk_acc_out_sal_csg");
            mnFkAccountOutAdjustmentId = resultSet.getInt("fk_acc_out_adj");
            mnFkAccountOutInventoryId = resultSet.getInt("fk_acc_out_inv");
            mnFkAccountPurchasesId = resultSet.getInt("fk_acc_pur");
            mnFkAccountPurchasesIncIncrementId = resultSet.getInt("fk_acc_pur_inc_inc");
            mnFkAccountPurchasesIncAdditionId = resultSet.getInt("fk_acc_pur_inc_add");
            mnFkAccountPurchasesDecDiscountId = resultSet.getInt("fk_acc_pur_dec_dis");
            mnFkAccountPurchasesDecReturnId = resultSet.getInt("fk_acc_pur_dec_ret");
            mnFkAccountSalesId = resultSet.getInt("fk_acc_sal");
            mnFkAccountSalesIncIncrementId = resultSet.getInt("fk_acc_sal_inc_inc");
            mnFkAccountSalesIncAdditionId = resultSet.getInt("fk_acc_sal_inc_add");
            mnFkAccountSalesDecDiscountId = resultSet.getInt("fk_acc_sal_dec_dis");
            mnFkAccountSalesDecReturnId = resultSet.getInt("fk_acc_sal_dec_ret");
            mnFkCostCenterInAdjustmentId = resultSet.getInt("fk_cc_in_adj");
            mnFkCostCenterInInventoryId = resultSet.getInt("fk_cc_in_inv");
            mnFkCostCenterPurchasesId = resultSet.getInt("fk_cc_pur");
            mnFkCostCenterPurchasesIncIncrementId = resultSet.getInt("fk_cc_pur_inc_inc");
            mnFkCostCenterPurchasesIncAdditionId = resultSet.getInt("fk_cc_pur_inc_add");
            mnFkCostCenterPurchasesDecDiscountId = resultSet.getInt("fk_cc_pur_dec_dis");
            mnFkCostCenterPurchasesDecReturnId = resultSet.getInt("fk_cc_pur_dec_ret");
            mnFkCostCenterOutAdjustmentId = resultSet.getInt("fk_cc_out_adj");
            mnFkCostCenterOutInventoryId = resultSet.getInt("fk_cc_out_inv");
            mnFkCostCenterSalesId = resultSet.getInt("fk_cc_sal");
            mnFkCostCenterSalesIncIncrementId = resultSet.getInt("fk_cc_sal_inc_inc");
            mnFkCostCenterSalesIncAdditionId = resultSet.getInt("fk_cc_sal_inc_add");
            mnFkCostCenterSalesDecDiscountId = resultSet.getInt("fk_cc_sal_dec_dis");
            mnFkCostCenterSalesDecReturnId = resultSet.getInt("fk_cc_sal_dec_ret");
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
                    mnPkAbpItemId + ", " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkAccountInPurchasesId + ", " +
                    mnFkAccountInPurchasesChangeId + ", " +
                    mnFkAccountInPurchasesWarrantyId + ", " +
                    mnFkAccountInPurchasesConsignationId + ", " +
                    mnFkAccountInSalesId + ", " +
                    mnFkAccountInSalesChangeId + ", " +
                    mnFkAccountInSalesWarrantyId + ", " +
                    mnFkAccountInSalesConsignationId + ", " +
                    mnFkAccountInAdjustmentId + ", " +
                    mnFkAccountInInventoryId + ", " +
                    mnFkAccountOutPurchasesId + ", " +
                    mnFkAccountOutPurchasesChangeId + ", " +
                    mnFkAccountOutPurchasesWarrantyId + ", " +
                    mnFkAccountOutPurchasesConsignationId + ", " +
                    mnFkAccountOutSalesId + ", " +
                    mnFkAccountOutSalesChangeId + ", " +
                    mnFkAccountOutSalesWarrantyId + ", " +
                    mnFkAccountOutSalesConsignationId + ", " +
                    mnFkAccountOutAdjustmentId + ", " +
                    mnFkAccountOutInventoryId + ", " +
                    mnFkAccountPurchasesId + ", " +
                    mnFkAccountPurchasesIncIncrementId + ", " +
                    mnFkAccountPurchasesIncAdditionId + ", " +
                    mnFkAccountPurchasesDecDiscountId + ", " +
                    mnFkAccountPurchasesDecReturnId + ", " +
                    mnFkAccountSalesId + ", " +
                    mnFkAccountSalesIncIncrementId + ", " +
                    mnFkAccountSalesIncAdditionId + ", " +
                    mnFkAccountSalesDecDiscountId + ", " +
                    mnFkAccountSalesDecReturnId + ", " +
                    mnFkCostCenterInAdjustmentId + ", " +
                    mnFkCostCenterInInventoryId + ", " +
                    mnFkCostCenterPurchasesId + ", " +
                    mnFkCostCenterPurchasesIncIncrementId + ", " +
                    mnFkCostCenterPurchasesIncAdditionId + ", " +
                    mnFkCostCenterPurchasesDecDiscountId + ", " +
                    mnFkCostCenterPurchasesDecReturnId + ", " +
                    mnFkCostCenterOutAdjustmentId + ", " +
                    mnFkCostCenterOutInventoryId + ", " +
                    mnFkCostCenterSalesId + ", " +
                    mnFkCostCenterSalesIncIncrementId + ", " +
                    mnFkCostCenterSalesIncAdditionId + ", " +
                    mnFkCostCenterSalesDecDiscountId + ", " +
                    mnFkCostCenterSalesDecReturnId + ", " +
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
                    "id_abp_item = " + mnPkAbpItemId + ", " +
                    */
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_acc_in_pur = " + mnFkAccountInPurchasesId + ", " +
                    "fk_acc_in_pur_chg = " + mnFkAccountInPurchasesChangeId + ", " +
                    "fk_acc_in_pur_war = " + mnFkAccountInPurchasesWarrantyId + ", " +
                    "fk_acc_in_pur_csg = " + mnFkAccountInPurchasesConsignationId + ", " +
                    "fk_acc_in_sal = " + mnFkAccountInSalesId + ", " +
                    "fk_acc_in_sal_chg = " + mnFkAccountInSalesChangeId + ", " +
                    "fk_acc_in_sal_war = " + mnFkAccountInSalesWarrantyId + ", " +
                    "fk_acc_in_sal_csg = " + mnFkAccountInSalesConsignationId + ", " +
                    "fk_acc_in_adj = " + mnFkAccountInAdjustmentId + ", " +
                    "fk_acc_in_inv = " + mnFkAccountInInventoryId + ", " +
                    "fk_acc_out_pur = " + mnFkAccountOutPurchasesId + ", " +
                    "fk_acc_out_pur_chg = " + mnFkAccountOutPurchasesChangeId + ", " +
                    "fk_acc_out_pur_war = " + mnFkAccountOutPurchasesWarrantyId + ", " +
                    "fk_acc_out_pur_csg = " + mnFkAccountOutPurchasesConsignationId + ", " +
                    "fk_acc_out_sal = " + mnFkAccountOutSalesId + ", " +
                    "fk_acc_out_sal_chg = " + mnFkAccountOutSalesChangeId + ", " +
                    "fk_acc_out_sal_war = " + mnFkAccountOutSalesWarrantyId + ", " +
                    "fk_acc_out_sal_csg = " + mnFkAccountOutSalesConsignationId + ", " +
                    "fk_acc_out_adj = " + mnFkAccountOutAdjustmentId + ", " +
                    "fk_acc_out_inv = " + mnFkAccountOutInventoryId + ", " +
                    "fk_acc_pur = " + mnFkAccountPurchasesId + ", " +
                    "fk_acc_pur_inc_inc = " + mnFkAccountPurchasesIncIncrementId + ", " +
                    "fk_acc_pur_inc_add = " + mnFkAccountPurchasesIncAdditionId + ", " +
                    "fk_acc_pur_dec_dis = " + mnFkAccountPurchasesDecDiscountId + ", " +
                    "fk_acc_pur_dec_ret = " + mnFkAccountPurchasesDecReturnId + ", " +
                    "fk_acc_sal = " + mnFkAccountSalesId + ", " +
                    "fk_acc_sal_inc_inc = " + mnFkAccountSalesIncIncrementId + ", " +
                    "fk_acc_sal_inc_add = " + mnFkAccountSalesIncAdditionId + ", " +
                    "fk_acc_sal_dec_dis = " + mnFkAccountSalesDecDiscountId + ", " +
                    "fk_acc_sal_dec_ret = " + mnFkAccountSalesDecReturnId + ", " +
                    "fk_cc_in_adj = " + mnFkCostCenterInAdjustmentId + ", " +
                    "fk_cc_in_inv = " + mnFkCostCenterInInventoryId + ", " +
                    "fk_cc_pur = " + mnFkCostCenterPurchasesId + ", " +
                    "fk_cc_pur_inc_inc = " + mnFkCostCenterPurchasesIncIncrementId + ", " +
                    "fk_cc_pur_inc_add = " + mnFkCostCenterPurchasesIncAdditionId + ", " +
                    "fk_cc_pur_dec_dis = " + mnFkCostCenterPurchasesDecDiscountId + ", " +
                    "fk_cc_pur_dec_ret = " + mnFkCostCenterPurchasesDecReturnId + ", " +
                    "fk_cc_out_adj = " + mnFkCostCenterOutAdjustmentId + ", " +
                    "fk_cc_out_inv = " + mnFkCostCenterOutInventoryId + ", " +
                    "fk_cc_sal = " + mnFkCostCenterSalesId + ", " +
                    "fk_cc_sal_inc_inc = " + mnFkCostCenterSalesIncIncrementId + ", " +
                    "fk_cc_sal_inc_add = " + mnFkCostCenterSalesIncAdditionId + ", " +
                    "fk_cc_sal_dec_dis = " + mnFkCostCenterSalesDecDiscountId + ", " +
                    "fk_cc_sal_dec_ret = " + mnFkCostCenterSalesDecReturnId + ", " +
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
    public SDbAbpItem clone() throws CloneNotSupportedException {
        SDbAbpItem registry = new SDbAbpItem();

        registry.setPkAbpItemId(this.getPkAbpItemId());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkAccountInPurchasesId(this.getFkAccountInPurchasesId());
        registry.setFkAccountInPurchasesChangeId(this.getFkAccountInPurchasesChangeId());
        registry.setFkAccountInPurchasesWarrantyId(this.getFkAccountInPurchasesWarrantyId());
        registry.setFkAccountInPurchasesConsignationId(this.getFkAccountInPurchasesConsignationId());
        registry.setFkAccountInSalesId(this.getFkAccountInSalesId());
        registry.setFkAccountInSalesChangeId(this.getFkAccountInSalesChangeId());
        registry.setFkAccountInSalesWarrantyId(this.getFkAccountInSalesWarrantyId());
        registry.setFkAccountInSalesConsignationId(this.getFkAccountInSalesConsignationId());
        registry.setFkAccountInAdjustmentId(this.getFkAccountInAdjustmentId());
        registry.setFkAccountInInventoryId(this.getFkAccountInInventoryId());
        registry.setFkAccountOutPurchasesId(this.getFkAccountOutPurchasesId());
        registry.setFkAccountOutPurchasesChangeId(this.getFkAccountOutPurchasesChangeId());
        registry.setFkAccountOutPurchasesWarrantyId(this.getFkAccountOutPurchasesWarrantyId());
        registry.setFkAccountOutPurchasesConsignationId(this.getFkAccountOutPurchasesConsignationId());
        registry.setFkAccountOutSalesId(this.getFkAccountOutSalesId());
        registry.setFkAccountOutSalesChangeId(this.getFkAccountOutSalesChangeId());
        registry.setFkAccountOutSalesWarrantyId(this.getFkAccountOutSalesWarrantyId());
        registry.setFkAccountOutSalesConsignationId(this.getFkAccountOutSalesConsignationId());
        registry.setFkAccountOutAdjustmentId(this.getFkAccountOutAdjustmentId());
        registry.setFkAccountOutInventoryId(this.getFkAccountOutInventoryId());
        registry.setFkAccountPurchasesId(this.getFkAccountPurchasesId());
        registry.setFkAccountPurchasesIncIncrementId(this.getFkAccountPurchasesIncIncrementId());
        registry.setFkAccountPurchasesIncAdditionId(this.getFkAccountPurchasesIncAdditionId());
        registry.setFkAccountPurchasesDecDiscountId(this.getFkAccountPurchasesDecDiscountId());
        registry.setFkAccountPurchasesDecReturnId(this.getFkAccountPurchasesDecReturnId());
        registry.setFkAccountSalesId(this.getFkAccountSalesId());
        registry.setFkAccountSalesIncIncrementId(this.getFkAccountSalesIncIncrementId());
        registry.setFkAccountSalesIncAdditionId(this.getFkAccountSalesIncAdditionId());
        registry.setFkAccountSalesDecDiscountId(this.getFkAccountSalesDecDiscountId());
        registry.setFkAccountSalesDecReturnId(this.getFkAccountSalesDecReturnId());
        registry.setFkCostCenterInAdjustmentId(this.getFkCostCenterInAdjustmentId());
        registry.setFkCostCenterInInventoryId(this.getFkCostCenterInInventoryId());
        registry.setFkCostCenterPurchasesId(this.getFkCostCenterPurchasesId());
        registry.setFkCostCenterPurchasesIncIncrementId(this.getFkCostCenterPurchasesIncIncrementId());
        registry.setFkCostCenterPurchasesIncAdditionId(this.getFkCostCenterPurchasesIncAdditionId());
        registry.setFkCostCenterPurchasesDecDiscountId(this.getFkCostCenterPurchasesDecDiscountId());
        registry.setFkCostCenterPurchasesDecReturnId(this.getFkCostCenterPurchasesDecReturnId());
        registry.setFkCostCenterOutAdjustmentId(this.getFkCostCenterOutAdjustmentId());
        registry.setFkCostCenterOutInventoryId(this.getFkCostCenterOutInventoryId());
        registry.setFkCostCenterSalesId(this.getFkCostCenterSalesId());
        registry.setFkCostCenterSalesIncIncrementId(this.getFkCostCenterSalesIncIncrementId());
        registry.setFkCostCenterSalesIncAdditionId(this.getFkCostCenterSalesIncAdditionId());
        registry.setFkCostCenterSalesDecDiscountId(this.getFkCostCenterSalesDecDiscountId());
        registry.setFkCostCenterSalesDecReturnId(this.getFkCostCenterSalesDecReturnId());
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

        rowsMap.put(ACC_IN_PUR, new SAbpRegistry(ACC_IN_PUR, TXT_ACC_IN_PUR, new int[] { },
                mnFkAccountInPurchasesId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_PUR_CHA, new SAbpRegistry(ACC_IN_PUR_CHA, TXT_ACC_IN_PUR_CHA, new int[] { },
                mnFkAccountInPurchasesChangeId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesChangeId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesChangeId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_PUR_WAR, new SAbpRegistry(ACC_IN_PUR_WAR, TXT_ACC_IN_PUR_WAR, new int[] { },
                mnFkAccountInPurchasesWarrantyId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesWarrantyId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesWarrantyId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_PUR_CSG, new SAbpRegistry(ACC_IN_PUR_CSG, TXT_ACC_IN_PUR_CSG, new int[] { },
                mnFkAccountInPurchasesConsignationId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesConsignationId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInPurchasesConsignationId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_SAL, new SAbpRegistry(ACC_IN_SAL, TXT_ACC_IN_SAL, new int[] { },
                mnFkAccountInSalesId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_SAL_CHA, new SAbpRegistry(ACC_IN_SAL_CHA, TXT_ACC_IN_SAL_CHA, new int[] { },
                mnFkAccountInSalesChangeId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesChangeId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesChangeId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_SAL_WAR, new SAbpRegistry(ACC_IN_SAL_WAR, TXT_ACC_IN_SAL_WAR, new int[] { },
                mnFkAccountInSalesWarrantyId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesWarrantyId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesWarrantyId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_SAL_CSG, new SAbpRegistry(ACC_IN_SAL_CSG, TXT_ACC_IN_SAL_CSG, new int[] { },
                mnFkAccountInSalesConsignationId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesConsignationId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInSalesConsignationId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_ADJ, new SAbpRegistry(ACC_IN_ADJ, TXT_ACC_IN_ADJ, new int[] { },
                mnFkAccountInAdjustmentId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInAdjustmentId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterInAdjustmentId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterInAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterInAdjustmentId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_IN_INV, new SAbpRegistry(ACC_IN_INV, TXT_ACC_IN_INV, new int[] { },
                mnFkAccountInInventoryId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInInventoryId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountInInventoryId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterInInventoryId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterInInventoryId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterInInventoryId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_PUR, new SAbpRegistry(ACC_OUT_PUR, TXT_ACC_OUT_PUR, new int[] { },
                mnFkAccountOutPurchasesId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_PUR_CHA, new SAbpRegistry(ACC_OUT_PUR_CHA, TXT_ACC_OUT_PUR_CHA, new int[] { },
                mnFkAccountOutPurchasesChangeId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesChangeId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesChangeId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_PUR_WAR, new SAbpRegistry(ACC_OUT_PUR_WAR, TXT_ACC_OUT_PUR_WAR, new int[] { },
                mnFkAccountOutPurchasesWarrantyId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesWarrantyId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesWarrantyId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_PUR_CSG, new SAbpRegistry(ACC_OUT_PUR_CSG, TXT_ACC_OUT_PUR_CSG, new int[] { },
                mnFkAccountOutPurchasesConsignationId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesConsignationId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutPurchasesConsignationId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_SAL, new SAbpRegistry(ACC_OUT_SAL, TXT_ACC_OUT_SAL, new int[] { },
                mnFkAccountOutSalesId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_SAL_CHA, new SAbpRegistry(ACC_OUT_SAL_CHA, TXT_ACC_OUT_SAL_CHA, new int[] { },
                mnFkAccountOutSalesChangeId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesChangeId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesChangeId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_SAL_WAR, new SAbpRegistry(ACC_OUT_SAL_WAR, TXT_ACC_OUT_SAL_WAR, new int[] { },
                mnFkAccountOutSalesWarrantyId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesWarrantyId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesWarrantyId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_SAL_CSG, new SAbpRegistry(ACC_OUT_SAL_CSG, TXT_ACC_OUT_SAL_CSG, new int[] { },
                mnFkAccountOutSalesConsignationId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesConsignationId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutSalesConsignationId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_ADJ, new SAbpRegistry(ACC_OUT_ADJ, TXT_ACC_OUT_ADJ, new int[] { },
                mnFkAccountOutAdjustmentId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutAdjustmentId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterOutAdjustmentId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterOutAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterOutAdjustmentId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_OUT_INV, new SAbpRegistry(ACC_OUT_INV, TXT_ACC_OUT_INV, new int[] { },
                mnFkAccountOutInventoryId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutInventoryId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountOutInventoryId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterOutInventoryId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterOutInventoryId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterOutInventoryId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_PUR, new SAbpRegistry(ACC_PUR, TXT_ACC_PUR, new int[] { },
                mnFkAccountPurchasesId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterPurchasesId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_PUR_INC_INC, new SAbpRegistry(ACC_PUR_INC_INC, TXT_ACC_PUR_INC_INC, new int[] { },
                mnFkAccountPurchasesIncIncrementId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesIncIncrementId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesIncIncrementId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterPurchasesIncIncrementId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesIncIncrementId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesIncIncrementId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_PUR_INC_ADD, new SAbpRegistry(ACC_PUR_INC_ADD, TXT_ACC_PUR_INC_ADD, new int[] { },
                mnFkAccountPurchasesIncAdditionId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesIncAdditionId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesIncAdditionId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterPurchasesIncAdditionId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesIncAdditionId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesIncAdditionId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_PUR_DEC_DIS, new SAbpRegistry(ACC_PUR_DEC_DIS, TXT_ACC_PUR_DEC_DIS, new int[] { },
                mnFkAccountPurchasesDecDiscountId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesDecDiscountId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesDecDiscountId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterPurchasesDecDiscountId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesDecDiscountId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesDecDiscountId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_PUR_DEC_RET, new SAbpRegistry(ACC_PUR_DEC_RET, TXT_ACC_PUR_DEC_RET, new int[] { },
                mnFkAccountPurchasesDecReturnId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesDecReturnId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPurchasesDecReturnId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterPurchasesDecReturnId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesDecReturnId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterPurchasesDecReturnId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL, new SAbpRegistry(ACC_SAL, TXT_ACC_SAL, new int[] { },
                mnFkAccountSalesId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterSalesId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL_INC_INC, new SAbpRegistry(ACC_SAL_INC_INC, TXT_ACC_SAL_INC_INC, new int[] { },
                mnFkAccountSalesIncIncrementId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesIncIncrementId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesIncIncrementId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterSalesIncIncrementId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesIncIncrementId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesIncIncrementId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL_INC_ADD, new SAbpRegistry(ACC_SAL_INC_ADD, TXT_ACC_SAL_INC_ADD, new int[] { },
                mnFkAccountSalesIncAdditionId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesIncAdditionId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesIncAdditionId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterSalesIncAdditionId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesIncAdditionId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesIncAdditionId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL_DEC_DIS, new SAbpRegistry(ACC_SAL_DEC_DIS, TXT_ACC_SAL_DEC_DIS, new int[] { },
                mnFkAccountSalesDecDiscountId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesDecDiscountId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesDecDiscountId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterSalesDecDiscountId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesDecDiscountId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesDecDiscountId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_SAL_DEC_RET, new SAbpRegistry(ACC_SAL_DEC_RET, TXT_ACC_SAL_DEC_RET, new int[] { },
                mnFkAccountSalesDecReturnId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesDecReturnId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountSalesDecReturnId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterSalesDecReturnId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesDecReturnId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterSalesDecReturnId }, SDbRegistry.FIELD_NAME)));

        return rowsMap;
    }

    @Override
    public void setAbpRows(HashMap<Integer, SAbpRegistry> rowsMap) {
        SAbpRegistry row = null;

        row = rowsMap.get(ACC_IN_PUR);
        mnFkAccountInPurchasesId = row.getAccountId();

        row = rowsMap.get(ACC_IN_PUR_CHA);
        mnFkAccountInPurchasesChangeId = row.getAccountId();

        row = rowsMap.get(ACC_IN_PUR_WAR);
        mnFkAccountInPurchasesWarrantyId = row.getAccountId();

        row = rowsMap.get(ACC_IN_PUR_CSG);
        mnFkAccountInPurchasesConsignationId = row.getAccountId();

        row = rowsMap.get(ACC_IN_SAL);
        mnFkAccountInSalesId = row.getAccountId();

        row = rowsMap.get(ACC_IN_SAL_CHA);
        mnFkAccountInSalesChangeId = row.getAccountId();

        row = rowsMap.get(ACC_IN_SAL_WAR);
        mnFkAccountInSalesWarrantyId = row.getAccountId();

        row = rowsMap.get(ACC_IN_SAL_CSG);
        mnFkAccountInSalesConsignationId = row.getAccountId();

        row = rowsMap.get(ACC_IN_ADJ);
        mnFkAccountInAdjustmentId = row.getAccountId();
        mnFkCostCenterInAdjustmentId = row.getCostCenterId();

        row = rowsMap.get(ACC_IN_INV);
        mnFkAccountInInventoryId = row.getAccountId();
        mnFkCostCenterInInventoryId = row.getCostCenterId();

        row = rowsMap.get(ACC_OUT_PUR);
        mnFkAccountOutPurchasesId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_PUR_CHA);
        mnFkAccountOutPurchasesChangeId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_PUR_WAR);
        mnFkAccountOutPurchasesWarrantyId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_PUR_CSG);
        mnFkAccountOutPurchasesConsignationId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_SAL);
        mnFkAccountOutSalesId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_SAL_CHA);
        mnFkAccountOutSalesChangeId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_SAL_WAR);
        mnFkAccountOutSalesWarrantyId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_SAL_CSG);
        mnFkAccountOutSalesConsignationId = row.getAccountId();

        row = rowsMap.get(ACC_OUT_ADJ);
        mnFkAccountOutAdjustmentId = row.getAccountId();
        mnFkCostCenterOutAdjustmentId = row.getCostCenterId();

        row = rowsMap.get(ACC_OUT_INV);
        mnFkAccountOutInventoryId = row.getAccountId();
        mnFkCostCenterOutInventoryId = row.getCostCenterId();

        row = rowsMap.get(ACC_PUR);
        mnFkAccountPurchasesId = row.getAccountId();
        mnFkCostCenterPurchasesId = row.getCostCenterId();

        row = rowsMap.get(ACC_PUR_INC_INC);
        mnFkAccountPurchasesIncIncrementId = row.getAccountId();
        mnFkCostCenterPurchasesIncIncrementId = row.getCostCenterId();

        row = rowsMap.get(ACC_PUR_INC_ADD);
        mnFkAccountPurchasesIncAdditionId = row.getAccountId();
        mnFkCostCenterPurchasesIncAdditionId = row.getCostCenterId();

        row = rowsMap.get(ACC_PUR_DEC_DIS);
        mnFkAccountPurchasesDecDiscountId = row.getAccountId();
        mnFkCostCenterPurchasesDecDiscountId = row.getCostCenterId();

        row = rowsMap.get(ACC_PUR_DEC_RET);
        mnFkAccountPurchasesDecReturnId = row.getAccountId();
        mnFkCostCenterPurchasesDecReturnId = row.getCostCenterId();

        row = rowsMap.get(ACC_SAL);
        mnFkAccountSalesId = row.getAccountId();
        mnFkCostCenterSalesId = row.getCostCenterId();

        row = rowsMap.get(ACC_SAL_INC_INC);
        mnFkAccountSalesIncIncrementId = row.getAccountId();
        mnFkCostCenterSalesIncIncrementId = row.getCostCenterId();

        row = rowsMap.get(ACC_SAL_INC_ADD);
        mnFkAccountSalesIncAdditionId = row.getAccountId();
        mnFkCostCenterSalesIncAdditionId = row.getCostCenterId();

        row = rowsMap.get(ACC_SAL_DEC_DIS);
        mnFkAccountSalesDecDiscountId = row.getAccountId();
        mnFkCostCenterSalesDecDiscountId = row.getCostCenterId();

        row = rowsMap.get(ACC_SAL_DEC_RET);
        mnFkAccountSalesDecReturnId = row.getAccountId();
        mnFkCostCenterSalesDecReturnId = row.getCostCenterId();
    }
}
