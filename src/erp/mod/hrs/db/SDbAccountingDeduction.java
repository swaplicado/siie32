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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbAccountingDeduction extends SDbRegistryUser {

    protected int mnPkDeductionId;
    protected int mnPkAccountingTypeId;
    protected int mnPkReferenceId;
    //protected boolean mbDeleted;
    protected int mnFkAccountId;
    protected int mnFkCostCenterId_n;
    protected int mnFkItemId_n;
    protected int mnFkBizPartnerId_n;
    protected int mnFkTaxBasicId_n;
    protected int mnFkTaxTaxId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msAuxDeduction;
    protected String msAuxReference;
    protected String msAuxTable;

    public SDbAccountingDeduction() {
        super(SModConsts.HRS_ACC_DED);
    }

    public void setPkDeductionId(int n) { mnPkDeductionId = n; }
    public void setPkAccountingTypeId(int n) { mnPkAccountingTypeId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAccountId(int n) { mnFkAccountId = n; }
    public void setFkCostCenterId_n(int n) { mnFkCostCenterId_n = n; }
    public void setFkItemId_n(int n) { mnFkItemId_n = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkTaxBasicId_n(int n) { mnFkTaxBasicId_n = n; }
    public void setFkTaxTaxId_n(int n) { mnFkTaxTaxId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxDeduction(String s) { msAuxDeduction = s; }
    public void setAuxReference(String s) { msAuxReference = s; }
    public void setAuxTable(String s) { msAuxTable = s; }

    public int getPkDeductionId() { return mnPkDeductionId; }
    public int getPkAccountingTypeId() { return mnPkAccountingTypeId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAccountId() { return mnFkAccountId; }
    public int getFkCostCenterId_n() { return mnFkCostCenterId_n; }
    public int getFkItemId_n() { return mnFkItemId_n; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkTaxBasicId_n() { return mnFkTaxBasicId_n; }
    public int getFkTaxTaxId_n() { return mnFkTaxTaxId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public String getAuxDeduction() { return msAuxDeduction; }
    public String getAuxReference() { return msAuxReference; }
    public String getAuxTable() { return msAuxTable; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDeductionId = pk[0];
        mnPkAccountingTypeId = pk[1];
        mnPkReferenceId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDeductionId, mnPkAccountingTypeId, mnPkReferenceId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDeductionId = 0;
        mnPkAccountingTypeId = 0;
        mnPkReferenceId = 0;
        mbDeleted = false;
        mnFkAccountId = 0;
        mnFkCostCenterId_n = 0;
        mnFkItemId_n = 0;
        mnFkBizPartnerId_n = 0;
        mnFkTaxBasicId_n = 0;
        mnFkTaxTaxId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msAuxDeduction = "";
        msAuxReference = "";
        msAuxTable = "";
    }

    @Override
    public String getSqlTable() {
        return (msAuxTable.isEmpty() ? "" : (msAuxTable + ".")) + SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ded = " + mnPkDeductionId + " AND "
                + "id_tp_acc = " + mnPkAccountingTypeId + " AND "
                + "id_ref = " + mnPkReferenceId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ded = " + pk[0] + " AND "
                + "id_tp_acc = " + pk[1] + " AND "
                + "id_ref = " + pk[2] + " ";
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
            mnPkDeductionId = resultSet.getInt("id_ded");
            mnPkAccountingTypeId = resultSet.getInt("id_tp_acc");
            mnPkReferenceId = resultSet.getInt("id_ref");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAccountId = resultSet.getInt("fk_acc");
            mnFkCostCenterId_n = resultSet.getInt("fk_cc_n");
            mnFkItemId_n = resultSet.getInt("fk_item_n");
            mnFkBizPartnerId_n = resultSet.getInt("fk_bp_n");
            mnFkTaxBasicId_n = resultSet.getInt("fk_tax_bas_n");
            mnFkTaxTaxId_n = resultSet.getInt("fk_tax_tax_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            msSql = "SELECT name FROM hrs_ded WHERE id_ded = " + mnPkDeductionId;
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                msAuxDeduction = resultSet.getString("name");
            }
            
            switch (mnPkAccountingTypeId) {
                case SModSysConsts.HRSS_TP_ACC_GBL:
                    msSql = "";
                    break;
                case SModSysConsts.HRSS_TP_ACC_DEP:
                    msSql = "SELECT name AS f_ref FROM erp.hrsu_dep WHERE id_dep = " + mnPkReferenceId;
                    break;
                case SModSysConsts.HRSS_TP_ACC_EMP:
                    msSql = "SELECT bp AS f_ref FROM erp.bpsu_bp WHERE id_bp = " + mnPkReferenceId;
                    break;
                default:
                    break;
            }
            
            if (!msSql.isEmpty()) {
                resultSet = session.getStatement().executeQuery(msSql);
                if (!resultSet.next()) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    msAuxReference = resultSet.getString("f_ref");
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
                    mnPkDeductionId + ", " + 
                    mnPkAccountingTypeId + ", " + 
                    mnPkReferenceId + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkAccountId + ", " + 
                    (mnFkCostCenterId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCostCenterId_n) + ", " +
                    (mnFkItemId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemId_n) + ", " +
                    (mnFkBizPartnerId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerId_n) + ", " +
                    (mnFkTaxBasicId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxBasicId_n) + ", " +
                    (mnFkTaxTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxTaxId_n) + ", " +
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
                    "id_ded = " + mnPkDeductionId + ", " +
                    "id_tp_acc = " + mnPkAccountingTypeId + ", " +
                    "id_ref = " + mnPkReferenceId + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_acc = " + mnFkAccountId + ", " +
                    "fk_cc_n = " + (mnFkCostCenterId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCostCenterId_n) + ", " +
                    "fk_item_n = " + (mnFkItemId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemId_n) + ", " +
                    "fk_bp_n = " + (mnFkBizPartnerId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerId_n) + ", " +
                    "fk_tax_bas_n = " + (mnFkTaxBasicId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxBasicId_n) + ", " +
                    "fk_tax_tax_n = " + (mnFkTaxTaxId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxTaxId_n) + ", " +
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
    public SDbAccountingDeduction clone() throws CloneNotSupportedException {
        SDbAccountingDeduction registry = new SDbAccountingDeduction();

        registry.setPkDeductionId(this.getPkDeductionId());
        registry.setPkAccountingTypeId(this.getPkAccountingTypeId());
        registry.setPkReferenceId(this.getPkReferenceId());
        registry.setDeleted(this.isDeleted());
        registry.setFkAccountId(this.getFkAccountId());
        registry.setFkCostCenterId_n(this.getFkCostCenterId_n());
        registry.setFkItemId_n(this.getFkItemId_n());
        registry.setFkBizPartnerId_n(this.getFkBizPartnerId_n());
        registry.setFkTaxBasicId_n(this.getFkTaxBasicId_n());
        registry.setFkTaxTaxId_n(this.getFkTaxTaxId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(true);

        return registry;
    }
}
