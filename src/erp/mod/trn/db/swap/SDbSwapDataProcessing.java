/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db.swap;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
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
    
    private static final int LEN_REFS = 100;
    private static final int LEN_DESCRIP = 100;
    
    private static final DecimalFormat RecPeriodFormat = new DecimalFormat("00");
    private static final DecimalFormat RecNumberFormat = new DecimalFormat(SLibUtils.textRepeat("0", SDataConstantsSys.NUM_LEN_FIN_REC));
    
    protected int mnPkSwapDataProcessingId;
    protected String msDataType;
    protected int mnTransactionCategory;
    protected int mnExternalDataId;
    protected String msExternalDataUuid;
    protected String msDpsReferences;
    protected String msDpsDescription;
    protected boolean mbDpsPaymentLocal;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkPaymentId_n;
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
    public void setDpsReferences(String s) { msDpsReferences = s.length() <= LEN_REFS ? s : s.substring(0, LEN_REFS); }
    public void setDpsDescription(String s) { msDpsDescription = s.length() <= LEN_DESCRIP ? s : s.substring(0, LEN_DESCRIP); }
    public void setDpsPaymentLocal(boolean b) { mbDpsPaymentLocal = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkPaymentId_n(int n) { mnFkPaymentId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSwapDataProcessingId() { return mnPkSwapDataProcessingId; }
    public String getDataType() { return msDataType; }
    public int getTransactionCategory() { return mnTransactionCategory; }
    public int getExternalDataId() { return mnExternalDataId; }
    public String getExternalDataUuid() { return msExternalDataUuid; }
    public String getDpsReferences() { return msDpsReferences; }
    public String getDpsDescription() { return msDpsDescription; }
    public boolean isDpsPaymentLocal() { return mbDpsPaymentLocal; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkPaymentId_n() { return mnFkPaymentId_n; }
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
        msDpsReferences = "";
        msDpsDescription = "";
        mbDpsPaymentLocal = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkPaymentId_n = 0;
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
            msDpsReferences = resultSet.getString("dps_refs");
            msDpsDescription = resultSet.getString("dps_descrip");
            mbDpsPaymentLocal = resultSet.getBoolean("b_dps_pay_loc");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkDpsYearId_n = resultSet.getInt("fid_dps_year_n");
            mnFkDpsDocId_n = resultSet.getInt("fid_dps_doc_n");
            mnFkPaymentId_n = resultSet.getInt("fk_pay_n");
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
                    "'" + msDpsReferences + "', " + 
                    "'" + msDpsDescription + "', " + 
                    (mbDpsPaymentLocal ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mnFkDpsYearId_n == 0 ? "NULL" : mnFkDpsYearId_n) + ", " + 
                    (mnFkDpsDocId_n == 0 ? "NULL" : mnFkDpsDocId_n) + ", " + 
                    (mnFkPaymentId_n == 0 ? "NULL" : mnFkPaymentId_n) + ", " + 
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
                    "dps_refs = '" + msDpsReferences + "', " +
                    "dps_descrip = '" + msDpsDescription + "', " +
                    "b_dps_pay_loc = " + (mbDpsPaymentLocal ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fid_dps_year_n = " + (mnFkDpsYearId_n == 0 ? "NULL" : mnFkDpsYearId_n) + ", " +
                    "fid_dps_doc_n = " + (mnFkDpsDocId_n == 0 ? "NULL" : mnFkDpsDocId_n) + ", " +
                    "fk_pay_n = " + (mnFkPaymentId_n == 0 ? "NULL" : mnFkPaymentId_n) + ", " +
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
        registry.setDpsReferences(this.getDpsReferences());
        registry.setDpsDescription(this.getDpsDescription());
        registry.setDpsPaymentLocal(this.isDpsPaymentLocal());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkDpsYearId_n(this.getFkDpsYearId_n());
        registry.setFkDpsDocId_n(this.getFkDpsDocId_n());
        registry.setFkPaymentId_n(this.getFkPaymentId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
    
    /**
     * Create prepared statement to get Processed DPS from SWAP processed data by its external ID.
     * @param session GUI session.
     * @return A prepared statment with these columns: id_swap_data_prc, dps_id_year, dps_id_doc, rec_id_year, rec_id_per, rec_id_bkc, rec_id_tp_rec, rec_id_num, rec_cob_code.
     * @throws Exception 
     */
    public static PreparedStatement createPrepStatementToGetProcessedDpsByExternalId(final SGuiSession session) throws Exception {
        String sql = "SELECT sdp.id_swap_data_prc AS id_swap_data_prc, "
                + "sdp.fid_dps_year_n AS dps_id_year, sdp.fid_dps_doc_n AS dps_id_doc, "
                + "r.id_year AS rec_id_year, r.id_per AS rec_id_per, r.id_bkc AS rec_id_bkc, r.id_tp_rec AS rec_id_tp_rec, r.id_num AS rec_id_num, cob.code AS rec_cob_code "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SWAP_DATA_PRC) + " AS sdp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "d.id_year = sdp.fid_dps_year_n AND d.id_doc = sdp.fid_dps_doc_n "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_REC) + " AS dr ON "
                + "dr.id_dps_year = d.id_year AND dr.id_dps_doc = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON "
                + "r.id_year = dr.fid_rec_year AND r.id_per = dr.fid_rec_per AND r.id_bkc = dr.fid_rec_bkc AND r.id_tp_rec = dr.fid_rec_tp_rec AND r.id_num = dr.fid_rec_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON "
                + "cob.id_bpb = r.fid_cob "
                + "WHERE NOT sdp.b_del AND sdp.data_type = ? AND sdp.txn_cat = ? AND sdp.ext_data_id = ?;";
        
        return session.getStatement().getConnection().prepareStatement(sql);
    }
    
    /**
     * Get Processed DPS from SWAP processed data, if any, by its external ID.
     * @param preparedStatement Prepared statement.
     * @param dataType Constants DATA_TYPE...: INV = invoice; DB = debit note; CN = credit note.
     * @param txnCategory Transaction category: 1 = purchase; 2 = sales.
     * @param externalId External ID.
     * @return A Processed DPS if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static ProcessedDps getProcessedDpsByByExternalId(final PreparedStatement preparedStatement, final String dataType, final int txnCategory, final int externalId) throws Exception {
        ProcessedDps processedDps = null;
        
        preparedStatement.setString(1, dataType);
        preparedStatement.setInt(2, txnCategory);
        preparedStatement.setInt(3, externalId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                processedDps = new ProcessedDps(resultSet.getInt("id_swap_data_prc"), resultSet.getInt("dps_id_year"), resultSet.getInt("dps_id_doc"), 
                        resultSet.getInt("rec_id_year"), resultSet.getInt("rec_id_per"), resultSet.getInt("rec_id_bkc"), resultSet.getString("rec_id_tp_rec"), resultSet.getInt("rec_id_num"), resultSet.getString("rec_cob_code"));
            }
        }
        
        return processedDps;
    }
    
    /**
     * Create prepared statement to get Processed DPS from Payable or Receivable Accounts by its own document data.
     * @param session GUI session.
     * @param dpsTypeKey Key of DPS type: (category, class & type).
     * @return A prepared statment with these columns: dps_id_year, dps_id_doc, rec_id_year, rec_id_per, rec_id_bkc, rec_id_tp_rec, rec_id_num, rec_cob_code.
     * @throws Exception 
     */
    public static PreparedStatement createPrepStatementToGetDpsKeyByDocData(final SGuiSession session, final int[] dpsTypeKey) throws Exception {
        String sql = "SELECT d.id_year AS dps_id_year, d.id_doc AS dps_id_doc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "WHERE NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                + "AND d.fid_ct_dps = " + dpsTypeKey[0] + " AND d.fid_cl_dps = " + dpsTypeKey[1] + " AND d.fid_tp_dps = " + dpsTypeKey[2] + " "
                + "AND d.fid_bp_r = ? AND d.dt = ? AND d.num_ser = ? AND d.num = ? AND d.tot_cur_r = ? AND d.fid_cur = ?;";
        
        return session.getStatement().getConnection().prepareStatement(sql);
    }
    
    /**
     * Get DPS primary key from Payable or Receivable Accounts, if any, by its own document data.
     * @param preparedStatement Prepared statement.
     * @param bizPartnerId Document's ID of business partner.
     * @param date Document's date.
     * @param numberSeries Document's folio series.
     * @param number Document's folio number.
     * @param total Document's net total.
     * @param currencyId Document's ID of currency.
     * @return A DPS primary key if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static int[] getDpsKeyByDocData(final PreparedStatement preparedStatement, final int bizPartnerId, final Date date, final String numberSeries, final String number, final double total, final int currencyId) throws Exception {
        int[] dpsKey = null;
        
        preparedStatement.setInt(1, bizPartnerId);
        preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
        preparedStatement.setString(3, numberSeries);
        preparedStatement.setString(4, number);
        preparedStatement.setDouble(5, total);
        preparedStatement.setInt(6, currencyId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                dpsKey = new int[] { resultSet.getInt("dps_id_year"), resultSet.getInt("dps_id_doc") };
            }
        }
        
        return dpsKey;
    }
    
    /**
     * In memory Processed DPS.
     */
    public static class ProcessedDps {
        
        public int SwapDataProcessingId;
        public int DpsYearId;
        public int DpsDocId;
        public int RecYearId;
        public int RecPeriodId;
        public int RecBookkeepingCenterId;
        public String RecRecordTypeId;
        public int RecNumberId;
        public String RecCompanyBranchCode;
        
        public ProcessedDps(final int swapDataProcessingId, final int dpsYearId, final int dpsDocId, 
                final int recYearId, final int recPeriodId, final int recBookkeepingCenterId, final String recRecordTypeId, final int recNumberId, final String recCompanyBranchCode) {
            SwapDataProcessingId = swapDataProcessingId;
            DpsYearId = dpsYearId;
            DpsDocId = dpsDocId;
            RecYearId = recYearId;
            RecPeriodId = recPeriodId;
            RecBookkeepingCenterId = recBookkeepingCenterId;
            RecRecordTypeId = recRecordTypeId;
            RecNumberId = recNumberId;
            RecCompanyBranchCode = recCompanyBranchCode;
        }
        
        public int[] getDpsKey() {
            return DpsYearId != 0 && DpsDocId != 0 ? new int[] { DpsYearId, DpsDocId } : null;
        }
        
        public String composeRecord() {
            return RecYearId + "-" +
                    RecPeriodFormat.format(RecPeriodId) + " " +
                    RecCompanyBranchCode + " " +
                    RecRecordTypeId + "-" +
                    RecNumberFormat.format(RecNumberId);
        }
    }
}
