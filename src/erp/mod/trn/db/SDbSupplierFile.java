/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.hrs.utils.SDocUtils;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbSupplierFile extends SDbRegistryUser {
    
    protected int mnPkSupplierFileId;
    protected String msNumber;
    protected String msSupplierFileType;
    protected double mdTotalCyQuotation;
    protected String msExternalBizPartnerName;
    protected String msFilevaultId;
    protected Date mtFilevaultTs_n;
    protected String msFileName;
    protected String msFileType;
    protected String msFileStorageName;
    //protected boolean mbDeleted;
    protected int mnFkCurrencyQuotationId;
    protected int mnFkBizPartnerId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbSupplierFileDps moSuppFileDps;
    protected File msAuxFile;
    
    public SDbSupplierFile() {
        super(SModConsts.TRN_SUP_FILE);
    }
    
    public void setPkSupplierFileId(int n) { mnPkSupplierFileId = n; }
    public void setNumber(String s) { msNumber = s; }
    public void setSupplierFileType(String s) { msSupplierFileType = s; }
    public void setTotalCyQuotation(double d) { mdTotalCyQuotation = d; }
    public void setExternalBizPartnerName(String s) { msExternalBizPartnerName = s; }
    public void setFilevaultId(String s) { msFilevaultId = s; }
    public void setFilevaultTs_n(Date t) { mtFilevaultTs_n = t; }
    public void setFileName(String s) { msFileName = s; }
    public void setFileType(String s) { msFileType = s; }
    public void setFileStorageName(String s) { msFileStorageName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkCurrencyQuotationId(int n) { mnFkCurrencyQuotationId = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setSuppFileDps(SDbSupplierFileDps o) { moSuppFileDps = o; }
    public void setAuxFile(File o) { msAuxFile = o; }
    
    public int getPkSupplierFileId() { return mnPkSupplierFileId; }
    public String getNumber() { return msNumber; }
    public String getSupplierFileType() { return msSupplierFileType; }
    public double getTotalCyQuotation() { return mdTotalCyQuotation; }
    public String getExternalBizPartnerName() { return msExternalBizPartnerName; }
    public String getFilevaultId() { return msFilevaultId; }
    public Date getFilevaultTs_n() { return mtFilevaultTs_n; }
    public String getFileName () { return msAuxFile == null ? msFileName : msAuxFile.getName(); }
    public String getFileType() { return msFileType; }
    public String getFileStorageName() { return msFileStorageName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkCurrencyQuotationId() { return mnFkCurrencyQuotationId; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public SDbSupplierFileDps getSuppFileDps() { return moSuppFileDps; }
    public File getAuxFile() { return msAuxFile; }
    
    /**
     * Un objeto SupplierFile puede tener varios SupplierFileDps, sin embargo sólo nos interesa leer uno a la vez,
     * por eso se especifica cual se necesita a traves del PK del DPS.
     * @param session
     * @param idYear
     * @param idDoc
     * @throws java.sql.SQLException
     */
    public void readSuppFileDps(SGuiSession session, int idYear, int idDoc) throws SQLException, Exception {
        moSuppFileDps = new SDbSupplierFileDps();
        moSuppFileDps.read(session, new int[] { mnPkSupplierFileId, idYear, idDoc });
    }
    
    public String getBizPartnerName(SGuiSession session) throws Exception {
        if (msExternalBizPartnerName.isEmpty()) {
            msSql = "SELECT bp FROM erp.bpsu_bp WHERE id_bp = " + mnFkBizPartnerId_n;
            ResultSet resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        }
        else {
            return msExternalBizPartnerName;
        }
        return "";
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSupplierFileId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSupplierFileId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkSupplierFileId = 0;
        msNumber = "";
        msSupplierFileType = "";
        mdTotalCyQuotation = 0;
        msExternalBizPartnerName = "";
        msFilevaultId = "";
        mtFilevaultTs_n = null;
        msFileName = "";
        msFileType = "";
        msFileStorageName = "";
        mbDeleted = false;
        mnFkCurrencyQuotationId = 0;
        mnFkBizPartnerId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moSuppFileDps = null;
        msAuxFile = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_sup_file = " + mnPkSupplierFileId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_sup_file = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkSupplierFileId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_sup_file), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSupplierFileId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkSupplierFileId = resultSet.getInt("id_sup_file");
            msNumber = resultSet.getString("num");
            msSupplierFileType = resultSet.getString("sup_file_type");
            mdTotalCyQuotation = resultSet.getDouble("tot_cur_quot");
            msExternalBizPartnerName = resultSet.getString("ext_bp_name");
            msFilevaultId = resultSet.getString("filevault_id");
            mtFilevaultTs_n = resultSet.getTimestamp("filevault_ts_n");
            msFileName = resultSet.getString("file_name");
            msFileType = resultSet.getString("file_type");
            msFileStorageName = resultSet.getString("file_storage_name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkCurrencyQuotationId = resultSet.getInt("fk_cur_quot");
            mnFkBizPartnerId_n = resultSet.getInt("fid_bp_n");
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
                    mnPkSupplierFileId + ", " + 
                    "'" + msNumber + "', " + 
                    "'" + msSupplierFileType + "', " + 
                    mdTotalCyQuotation + ", " + 
                    "'" + msExternalBizPartnerName + "', " + 
                    "'" + msFilevaultId + "', " + 
                    "NULL, " + 
                    "'" + msFileName + "', " + 
                    "'" + msFileType + "', " + 
                    "'" + msFileStorageName + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkCurrencyQuotationId + ", " + 
                    (mnFkBizPartnerId_n == 0 ? "NULL" : mnFkBizPartnerId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_sup_file = " + mnPkSupplierFileId + ", " +
                    "num = '" + msNumber + "', " +
                    "sup_file_type = '" + msSupplierFileType + "', " +
                    "tot_cur_quot = " + mdTotalCyQuotation + ", " +
                    "ext_bp_name = '" + msExternalBizPartnerName + "', " +
                    "filevault_id = '" + msFilevaultId + "', " +
                    "filevault_ts_n = NULL, " +
                    "file_name = '" + msFileName + "', " +
                    "file_type = '" + msFileType + "', " +
                    "file_storage_name = '" + msFileStorageName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_cur_quot = " + mnFkCurrencyQuotationId + ", " +
                    "fid_bp_n = " + (mnFkBizPartnerId_n == 0 ? "NULL" : mnFkBizPartnerId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save file
        
        if (msAuxFile != null) {
            msFileType = SDocUtils.getExtensionFile(msAuxFile);
            msFilevaultId = SDocUtils.uploadFile(session, SDocUtils.BUCKET_DOC_DPS_SUPPLIER, msFileType, msAuxFile);
            msFileName = msAuxFile.getName();
        }
        
        msSql = "UPDATE " + getSqlTable() + " SET " +
                "filevault_id = '" + msFilevaultId + "', " +
                "filevault_ts_n = NULL, " +
                "file_type = '" + msFileType + "', " +
                "file_name = '" + msFileName + "' " +
                getSqlWhere();
        session.getStatement().execute(msSql);
        
        // Save file vs DPS
        moSuppFileDps.setPkSupplierFileId(mnPkSupplierFileId);
        moSuppFileDps.save(session);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbSupplierFile clone() throws CloneNotSupportedException {
        SDbSupplierFile registry = new SDbSupplierFile();
        
        registry.setPkSupplierFileId(this.getPkSupplierFileId());
        registry.setNumber(this.getNumber());
        registry.setSupplierFileType(this.getSupplierFileType());
        registry.setTotalCyQuotation(this.getTotalCyQuotation());
        registry.setExternalBizPartnerName(this.getExternalBizPartnerName());
        registry.setFilevaultId(this.getFilevaultId());
        registry.setFilevaultTs_n(this.getFilevaultTs_n());
        registry.setFileName(this.getFileName());
        registry.setFileType(this.getFileType());
        registry.setFileStorageName(this.getFileStorageName());
        registry.setDeleted(this.isDeleted());
        registry.setFkCurrencyQuotationId(this.getFkCurrencyQuotationId());
        registry.setFkBizPartnerId_n(this.getFkBizPartnerId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setSuppFileDps(this.getSuppFileDps());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        moSuppFileDps.delete(session);
        
        msSql = "SELECT * FROM trn_sup_file_dps WHERE id_sup_file = " + mnPkSupplierFileId;
        ResultSet resultSet = session.getDatabase().getConnection().createStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            msSql = "DELETE " + getSqlFromWhere();
            session.getStatement().execute(msSql);
            SDocUtils.deleteFile(session, SDocUtils.BUCKET_DOC_DPS_SUPPLIER, msFilevaultId);
        }
    }
}
