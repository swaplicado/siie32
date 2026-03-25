/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbSwapDataProcessing extends SDbRegistryUser {
    
    public static final String DATA_TYPE_INV = "INV"; // invoice
    public static final String DATA_TYPE_DN = "DN"; // debit note
    public static final String DATA_TYPE_CN = "CN"; // credit note
    public static final String DATA_TYPE_P = "P"; // payment
    public static final String DATA_TYPE_PR = "PR"; // payment receipt
    public static final String DATA_TYPE_RP = "RP"; // receipt of payment
    public static final String DATA_TYPE_PRF = "PRF"; // proforma
    
    public static final int PROC_TYPE_STANDARD = 0;
    public static final int PROC_TYPE_RAW_MAT_FREIGHT = 11;
    public static final int PROC_TYPE_RAW_MAT_PURCHASE = 12;
    
    public static final int ACC_METHOD_UNDEFINED = 0;
    public static final int ACC_METHOD_MANUAL = 1;
    public static final int ACC_METHOD_ASSISTED = 2;
    public static final int ACC_METHOD_AUTOMATIC = 3;
    
    /** Processing types (in SWAP Services). */
    public static final HashMap<Integer, String> ProcTypes = new HashMap<>();
    
    private static final int LEN_REFS = 100;
    
    static {
        ProcTypes.put(PROC_TYPE_STANDARD, "STD");
        ProcTypes.put(PROC_TYPE_RAW_MAT_FREIGHT, "FLT-MP");
        ProcTypes.put(PROC_TYPE_RAW_MAT_PURCHASE, "CPA-MP");
    }
    
    protected int mnPkSwapDataProcessingId;
    protected String msDataType;
    protected int mnTransactionCategory;
    protected int mnExternalDataId;
    protected String msExternalDataUuid;
    protected String msExternalDataAuthorizationHistory;
    protected String msDpsReferences;
    protected String msDpsDescription;
    protected boolean mbDpsPaymentLocal;
    protected int mnProcessingType;
    protected boolean mbPaymentRequired;
    protected double mdPaymentApplicationCy;
    protected Date mtPaymentDateRequired_n;
    protected int mnAccMethod;
    protected double mdAccUserUnits;
    protected double mdAccSystemUnits;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkPaymentId_n;
    protected int mnFkPayCurrencyId_n;
    protected int mnFkAccUserAccountId_n;
    protected int mnFkAccUserCostCenterId_n;
    protected int mnFkAccUserItemId_n;
    protected int mnFkAccUserItemAuxId_n;
    protected int mnFkAccUserUnitId_n;
    protected int mnFkAccSystemAccountId_n;
    protected int mnFkAccSystemCostCenterId_n;
    protected int mnFkAccSystemItemId_n;
    protected int mnFkAccSystemItemAuxId_n;
    protected int mnFkAccSystemUnitId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbSwapDataProcessing() {
        super(SModConsts.TRN_SWAP_DATA_PRC);
    }
    
    public void setPkSwapDataProcessingId(int n) { mnPkSwapDataProcessingId = n; }
    public void setDataType(String s) { msDataType = s; }
    public void setTransactionCategory(int n) { mnTransactionCategory = n; }
    public void setExternalDataId(int n) { mnExternalDataId = n; }
    public void setExternalDataUuid(String s) { msExternalDataUuid = s; }
    public void setExternalDataAuthorizationHistory(String s) { msExternalDataAuthorizationHistory = s; }
    public void setDpsReferences(String s) { msDpsReferences = s.length() <= LEN_REFS ? s : s.substring(0, LEN_REFS); }
    public void setDpsDescription(String s) { msDpsDescription = s; }
    public void setDpsPaymentLocal(boolean b) { mbDpsPaymentLocal = b; }
    public void setProcessingType(int n) { mnProcessingType = n; }
    public void setPaymentRequired(boolean b) { mbPaymentRequired = b; }
    public void setPaymentApplicationCy(double d) { mdPaymentApplicationCy = d; }
    public void setPaymentDateRequired_n(Date t) { mtPaymentDateRequired_n = t; }
    public void setAccMethod(int n) { mnAccMethod = n; }
    public void setAccUserUnits(double d) { mdAccUserUnits = d; }
    public void setAccSystemUnits(double d) { mdAccSystemUnits = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkPaymentId_n(int n) { mnFkPaymentId_n = n; }
    public void setFkPayCurrencyId_n(int n) { mnFkPayCurrencyId_n = n; }
    public void setFkAccUserAccountId_n(int n) { mnFkAccUserAccountId_n = n; }
    public void setFkAccUserCostCenterId_n(int n) { mnFkAccUserCostCenterId_n = n; }
    public void setFkAccUserItemId_n(int n) { mnFkAccUserItemId_n = n; }
    public void setFkAccUserItemAuxId_n(int n) { mnFkAccUserItemAuxId_n = n; }
    public void setFkAccUserUnitId_n(int n) { mnFkAccUserUnitId_n = n; }
    public void setFkAccSystemAccountId_n(int n) { mnFkAccSystemAccountId_n = n; }
    public void setFkAccSystemCostCenterId_n(int n) { mnFkAccSystemCostCenterId_n = n; }
    public void setFkAccSystemItemId_n(int n) { mnFkAccSystemItemId_n = n; }
    public void setFkAccSystemItemAuxId_n(int n) { mnFkAccSystemItemAuxId_n = n; }
    public void setFkAccSystemUnitId_n(int n) { mnFkAccSystemUnitId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSwapDataProcessingId() { return mnPkSwapDataProcessingId; }
    public String getDataType() { return msDataType; }
    public int getTransactionCategory() { return mnTransactionCategory; }
    public int getExternalDataId() { return mnExternalDataId; }
    public String getExternalDataUuid() { return msExternalDataUuid; }
    public String getExternalDataAuthorizationHistory() { return msExternalDataAuthorizationHistory; }
    public String getDpsReferences() { return msDpsReferences; }
    public String getDpsDescription() { return msDpsDescription; }
    public boolean isDpsPaymentLocal() { return mbDpsPaymentLocal; }
    public int getProcessingType() { return mnProcessingType; }
    public boolean isPaymentRequired() { return mbPaymentRequired; }
    public double getPaymentApplicationCy() { return mdPaymentApplicationCy; }
    public Date getPaymentDateRequired_n() { return mtPaymentDateRequired_n; }
    public int getAccMethod() { return mnAccMethod; }
    public double getAccUserUnits() { return mdAccUserUnits; }
    public double getAccSystemUnits() { return mdAccSystemUnits; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkPaymentId_n() { return mnFkPaymentId_n; }
    public int getFkPayCurrencyId_n() { return mnFkPayCurrencyId_n; }
    public int getFkAccUserAccountId_n() { return mnFkAccUserAccountId_n; }
    public int getFkAccUserCostCenterId_n() { return mnFkAccUserCostCenterId_n; }
    public int getFkAccUserItemId_n() { return mnFkAccUserItemId_n; }
    public int getFkAccUserItemAuxId_n() { return mnFkAccUserItemAuxId_n; }
    public int getFkAccUserUnitId_n() { return mnFkAccUserUnitId_n; }
    public int getFkAccSystemAccountId_n() { return mnFkAccSystemAccountId_n; }
    public int getFkAccSystemCostCenterId_n() { return mnFkAccSystemCostCenterId_n; }
    public int getFkAccSystemItemId_n() { return mnFkAccSystemItemId_n; }
    public int getFkAccSystemItemAuxId_n() { return mnFkAccSystemItemAuxId_n; }
    public int getFkAccSystemUnitId_n() { return mnFkAccSystemUnitId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSwapDataProcessingId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSwapDataProcessingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkSwapDataProcessingId = 0;
        msDataType = "";
        mnTransactionCategory = 0;
        mnExternalDataId = 0;
        msExternalDataUuid = "";
        msExternalDataAuthorizationHistory = "";
        msDpsReferences = "";
        msDpsDescription = "";
        mbDpsPaymentLocal = false;
        mnProcessingType = 0;
        mbPaymentRequired = false;
        mdPaymentApplicationCy = 0;
        mtPaymentDateRequired_n = null;
        mnAccMethod = 0;
        mdAccUserUnits = 0;
        mdAccSystemUnits = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkPaymentId_n = 0;
        mnFkPayCurrencyId_n = 0;
        mnFkAccUserAccountId_n = 0;
        mnFkAccUserCostCenterId_n = 0;
        mnFkAccUserItemId_n = 0;
        mnFkAccUserItemAuxId_n = 0;
        mnFkAccUserUnitId_n = 0;
        mnFkAccSystemAccountId_n = 0;
        mnFkAccSystemCostCenterId_n = 0;
        mnFkAccSystemItemId_n = 0;
        mnFkAccSystemItemAuxId_n = 0;
        mnFkAccSystemUnitId_n = 0;
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
        return "WHERE id_swap_data_prc = " + mnPkSwapDataProcessingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_swap_data_prc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkSwapDataProcessingId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_swap_data_prc), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSwapDataProcessingId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SWAP_DATA_PRC) + " " +
                getSqlWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkSwapDataProcessingId = resultSet.getInt("id_swap_data_prc");
            msDataType = resultSet.getString("data_type");
            mnTransactionCategory = resultSet.getInt("txn_cat");
            mnExternalDataId = resultSet.getInt("ext_data_id");
            msExternalDataUuid = resultSet.getString("ext_data_uuid");
            msExternalDataAuthorizationHistory = resultSet.getString("ext_authorn_hist");
            msDpsReferences = resultSet.getString("dps_refs");
            msDpsDescription = resultSet.getString("dps_descrip");
            mbDpsPaymentLocal = resultSet.getBoolean("b_dps_pay_loc");
//            mnProcessingType = resultSet.getInt("proc_type");
//            mbPaymentRequired = resultSet.getBoolean("b_pay_req");
//            mdPaymentApplicationCy = resultSet.getDouble("pay_app_cur");
//            mtPaymentDateRequired_n = resultSet.getDate("pay_dt_req_n");
//            mnAccMethod = resultSet.getInt("acc_method");
//            mdAccUserUnits = resultSet.getDouble("acc_usr_units");
//            mdAccSystemUnits = resultSet.getDouble("acc_sys_units");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkDpsYearId_n = resultSet.getInt("fk_dps_year_n");
            mnFkDpsDocId_n = resultSet.getInt("fk_dps_doc_n");
            mnFkPaymentId_n = resultSet.getInt("fk_pay_n");
//            mnFkPayCurrencyId_n = resultSet.getInt("fk_pay_cur_n");
//            mnFkAccUserAccountId_n = resultSet.getInt("fk_acc_usr_acc_n");
//            mnFkAccUserCostCenterId_n = resultSet.getInt("fk_acc_usr_cc_n");
//            mnFkAccUserItemId_n = resultSet.getInt("fk_acc_usr_item_n");
//            mnFkAccUserItemAuxId_n = resultSet.getInt("fk_acc_usr_item_aux_n");
//            mnFkAccUserUnitId_n = resultSet.getInt("fk_acc_usr_unit_n");
//            mnFkAccSystemAccountId_n = resultSet.getInt("fk_acc_sys_acc_n");
//            mnFkAccSystemCostCenterId_n = resultSet.getInt("fk_acc_sys_cc_n");
//            mnFkAccSystemItemId_n = resultSet.getInt("fk_acc_sys_item_n");
//            mnFkAccSystemItemAuxId_n = resultSet.getInt("fk_acc_sys_item_aux_n");
//            mnFkAccSystemUnitId_n = resultSet.getInt("fk_acc_sys_unit_n");
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
                    mnPkSwapDataProcessingId + ", " + 
                    "'" + msDataType + "', " + 
                    mnTransactionCategory + ", " + 
                    mnExternalDataId + ", " + 
                    "'" + msExternalDataUuid + "', " + 
                    "'" + msExternalDataAuthorizationHistory + "', " + 
                    "'" + msDpsReferences + "', " + 
                    "'" + msDpsDescription + "', " + 
                    (mbDpsPaymentLocal ? 1 : 0) + ", " + 
//                    mnProcessingType + ", " + 
//                    (mbPaymentRequired ? 1 : 0) + ", " + 
//                    mdPaymentApplicationCy + ", " + 
//                    (mtPaymentDateRequired_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtPaymentDateRequired_n) + "'") + ", " + 
//                    mnAccMethod + ", " + 
//                    mdAccUserUnits + ", " + 
//                    mdAccSystemUnits + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mnFkDpsYearId_n == 0 ? "NULL" : mnFkDpsYearId_n) + ", " + 
                    (mnFkDpsDocId_n == 0 ? "NULL" : mnFkDpsDocId_n) + ", " + 
                    (mnFkPaymentId_n == 0 ? "NULL" : mnFkPaymentId_n) + ", " + 
//                    (mnFkPayCurrencyId_n == 0 ? "NULL" : mnFkPayCurrencyId_n) + ", " + 
//                    (mnFkAccUserAccountId_n == 0 ? "NULL" : mnFkAccUserAccountId_n) + ", " + 
//                    (mnFkAccUserCostCenterId_n == 0 ? "NULL" : mnFkAccUserCostCenterId_n) + ", " + 
//                    (mnFkAccUserItemId_n == 0 ? "NULL" : mnFkAccUserItemId_n) + ", " + 
//                    (mnFkAccUserItemAuxId_n == 0 ? "NULL" : mnFkAccUserItemAuxId_n) + ", " + 
//                    (mnFkAccUserUnitId_n == 0 ? "NULL" : mnFkAccUserUnitId_n) + ", " + 
//                    (mnFkAccSystemAccountId_n == 0 ? "NULL" : mnFkAccSystemAccountId_n) + ", " + 
//                    (mnFkAccSystemCostCenterId_n == 0 ? "NULL" : mnFkAccSystemCostCenterId_n) + ", " + 
//                    (mnFkAccSystemItemId_n == 0 ? "NULL" : mnFkAccSystemItemId_n) + ", " + 
//                    (mnFkAccSystemItemAuxId_n == 0 ? "NULL" : mnFkAccSystemItemAuxId_n) + ", " + 
//                    (mnFkAccSystemUnitId_n == 0 ? "NULL" : mnFkAccSystemUnitId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_swap_data_prc = " + mnPkSwapDataProcessingId + ", " +
                    "data_type = '" + msDataType + "', " +
                    "txn_cat = " + mnTransactionCategory + ", " +
                    "ext_data_id = " + mnExternalDataId + ", " +
                    "ext_data_uuid = '" + msExternalDataUuid + "', " +
                    "ext_authorn_hist = '" + msExternalDataAuthorizationHistory + "', " +
                    "dps_refs = '" + msDpsReferences + "', " +
                    "dps_descrip = '" + msDpsDescription + "', " +
                    "b_dps_pay_loc = " + (mbDpsPaymentLocal ? 1 : 0) + ", " +
//                    "proc_type = " + mnProcessingType + ", " +
//                    "b_pay_req = " + (mbPaymentRequired ? 1 : 0) + ", " +
//                    "pay_app_cur = " + mdPaymentApplicationCy + ", " +
//                    "pay_dt_req_n = " + (mtPaymentDateRequired_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtPaymentDateRequired_n) + "'") + ", " +
//                    "acc_method = " + mnAccMethod + ", " +
//                    "acc_usr_units = " + mdAccUserUnits + ", " +
//                    "acc_sys_units = " + mdAccSystemUnits + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_dps_year_n = " + (mnFkDpsYearId_n == 0 ? "NULL" : mnFkDpsYearId_n) + ", " +
                    "fk_dps_doc_n = " + (mnFkDpsDocId_n == 0 ? "NULL" : mnFkDpsDocId_n) + ", " +
                    "fk_pay_n = " + (mnFkPaymentId_n == 0 ? "NULL" : mnFkPaymentId_n) + ", " +
//                    "fk_pay_cur_n = " + (mnFkPayCurrencyId_n == 0 ? "NULL" : mnFkPayCurrencyId_n) + ", " +
//                    "fk_acc_usr_acc_n = " + (mnFkAccUserAccountId_n == 0 ? "NULL" : mnFkAccUserAccountId_n) + ", " +
//                    "fk_acc_usr_cc_n = " + (mnFkAccUserCostCenterId_n == 0 ? "NULL" : mnFkAccUserCostCenterId_n) + ", " +
//                    "fk_acc_usr_item_n = " + (mnFkAccUserItemId_n == 0 ? "NULL" : mnFkAccUserItemId_n) + ", " +
//                    "fk_acc_usr_item_aux_n = " + (mnFkAccUserItemAuxId_n == 0 ? "NULL" : mnFkAccUserItemAuxId_n) + ", " +
//                    "fk_acc_usr_unit_n = " + (mnFkAccUserUnitId_n == 0 ? "NULL" : mnFkAccUserUnitId_n) + ", " +
//                    "fk_acc_sys_acc_n = " + (mnFkAccSystemAccountId_n == 0 ? "NULL" : mnFkAccSystemAccountId_n) + ", " +
//                    "fk_acc_sys_cc_n = " + (mnFkAccSystemCostCenterId_n == 0 ? "NULL" : mnFkAccSystemCostCenterId_n) + ", " +
//                    "fk_acc_sys_item_n = " + (mnFkAccSystemItemId_n == 0 ? "NULL" : mnFkAccSystemItemId_n) + ", " +
//                    "fk_acc_sys_item_aux_n = " + (mnFkAccSystemItemAuxId_n == 0 ? "NULL" : mnFkAccSystemItemAuxId_n) + ", " +
//                    "fk_acc_sys_unit_n = " + (mnFkAccSystemUnitId_n == 0 ? "NULL" : mnFkAccSystemUnitId_n) + ", " +
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
    public SDbSwapDataProcessing clone() throws CloneNotSupportedException {
        SDbSwapDataProcessing registry = new SDbSwapDataProcessing();
        
        registry.setPkSwapDataProcessingId(this.getPkSwapDataProcessingId());
        registry.setDataType(this.getDataType());
        registry.setTransactionCategory(this.getTransactionCategory());
        registry.setExternalDataId(this.getExternalDataId());
        registry.setExternalDataUuid(this.getExternalDataUuid());
        registry.setExternalDataAuthorizationHistory(this.getExternalDataAuthorizationHistory());
        registry.setDpsReferences(this.getDpsReferences());
        registry.setDpsDescription(this.getDpsDescription());
        registry.setDpsPaymentLocal(this.isDpsPaymentLocal());
        registry.setProcessingType(this.getProcessingType());
        registry.setPaymentRequired(this.isPaymentRequired());
        registry.setPaymentApplicationCy(this.getPaymentApplicationCy());
        registry.setPaymentDateRequired_n(this.getPaymentDateRequired_n());
        registry.setAccMethod(this.getAccMethod());
        registry.setAccUserUnits(this.getAccUserUnits());
        registry.setAccSystemUnits(this.getAccSystemUnits());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkDpsYearId_n(this.getFkDpsYearId_n());
        registry.setFkDpsDocId_n(this.getFkDpsDocId_n());
        registry.setFkPaymentId_n(this.getFkPaymentId_n());
        registry.setFkPayCurrencyId_n(this.getFkPayCurrencyId_n());
        registry.setFkAccUserAccountId_n(this.getFkAccUserAccountId_n());
        registry.setFkAccUserCostCenterId_n(this.getFkAccUserCostCenterId_n());
        registry.setFkAccUserItemId_n(this.getFkAccUserItemId_n());
        registry.setFkAccUserItemAuxId_n(this.getFkAccUserItemAuxId_n());
        registry.setFkAccUserUnitId_n(this.getFkAccUserUnitId_n());
        registry.setFkAccSystemAccountId_n(this.getFkAccSystemAccountId_n());
        registry.setFkAccSystemCostCenterId_n(this.getFkAccSystemCostCenterId_n());
        registry.setFkAccSystemItemId_n(this.getFkAccSystemItemId_n());
        registry.setFkAccSystemItemAuxId_n(this.getFkAccSystemItemAuxId_n());
        registry.setFkAccSystemUnitId_n(this.getFkAccSystemUnitId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
