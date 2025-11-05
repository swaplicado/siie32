/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mod.SModConsts;
import erp.mod.hrs.utils.SDocUtils;
import java.io.File;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SDbPaymentFile extends SDbRegistryUser implements SGridRow, Serializable {
    
    public static final String FILE_TP_EF = "EF"; // evidencia fotográfica
    public static final String FILE_TP_RA = "RA"; // reporte de avance o bitácora de trabajo
    public static final String FILE_TP_PF = "PF"; // soporte de parcialidad o finiquito
    public static final String FILE_TP_OS = "OS"; // otro soporte
    
    public static final String DESC_FILE_TP_EF = "Evidencia fotográfica";
    public static final String DESC_FILE_TP_RA = "Reporte de avance o bitácora de trabajo";
    public static final String DESC_FILE_TP_PF = "Soporte de parcialidad o finiquito";
    public static final String DESC_FILE_TP_OS = "Otro soporte";
    
    protected int mnPkPaymentId;
    protected int mnPkFileId;
    protected String msPaymentFileType;
    protected String msFileDescription;
    protected int mnSortingPos;
    protected String msFilevaultId;
    protected Date mtFilevaultTs_n;
    protected String msFileName;
    protected String msFileType;
    protected String msFileStorageName;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected File msAuxFile;

    public SDbPaymentFile() {
        super(SModConsts.FIN_PAY_FILE);
    }
    
    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setPkFileId(int n) { mnPkFileId = n; }
    public void setPaymentFileType(String s) { msPaymentFileType = s; }
    public void setFileDescription(String s) { msFileDescription = s; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setFilevaultId(String s) { msFilevaultId = s; }
    public void setFilevaultTs_n(Date t) { mtFilevaultTs_n = t; }
    public void setFileName(String s) { msFileName = s; }
    public void setFileType(String s) { msFileType = s; }
    public void setFileStorageName(String s) { msFileStorageName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxFile(File o) { msAuxFile = o; }

    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getPkFileId() { return mnPkFileId; }
    public String getPaymentFileType() { return msPaymentFileType; }
    public String getFileDescription() { return msFileDescription; }
    public int getSortingPos() { return mnSortingPos; }
    public String getFilevaultId() { return msFilevaultId; }
    public Date getFilevaultTs_n() { return mtFilevaultTs_n; }
    public String getFileName() { return msFileName; }
    public String getFileType() { return msFileType; }
    public String getFileStorageName() { return msFileStorageName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public File getAuxFile() { return msAuxFile; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPaymentId = pk[0];
        mnPkFileId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPaymentId, mnPkFileId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkPaymentId = 0;
        mnPkFileId = 0;
        msPaymentFileType = "";
        msFileDescription = "";
        mnSortingPos = 0;
        msFilevaultId = "";
        mtFilevaultTs_n = null;
        msFileName = "";
        msFileType = "";
        msFileStorageName = "";
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msAuxFile = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPaymentId + " "
                + "AND id_file = " + mnPkFileId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " "
                + "AND id_file = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkFileId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_file), 0) + 1 FROM " + getSqlTable() + " WHERE id_pay = " + mnPkPaymentId + "";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkFileId = resultSet.getInt(1);
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
            mnPkPaymentId = resultSet.getInt("id_pay");
            mnPkFileId = resultSet.getInt("id_file");
            msPaymentFileType = resultSet.getString("pay_file_type");
            msFileDescription = resultSet.getString("file_descrip");
            mnSortingPos = resultSet.getInt("sort");
            msFilevaultId = resultSet.getString("filevault_id");
            mtFilevaultTs_n = resultSet.getTimestamp("filevault_ts_n");
            msFileName = resultSet.getString("file_name");
            msFileType = resultSet.getString("file_type");
            msFileStorageName = resultSet.getString("file_storage_name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }
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
                    mnPkPaymentId + ", " + 
                    mnPkFileId + ", " + 
                    "'" + msPaymentFileType + "', " + 
                    "'" + msFileDescription + "', " + 
                    mnSortingPos + ", " + 
                    "'" + msFilevaultId + "', " + 
                    "NOW()" + ", " + 
                    "'" + msFileName + "', " + 
                    "'" + msFileType + "', " + 
                    "'" + msFileStorageName + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pay = " + mnPkPaymentId + ", " +
                    //"id_file = " + mnPkFileId + ", " +
                    "pay_file_type = '" + msPaymentFileType + "', " +
                    "file_descrip = '" + msFileDescription + "', " +
                    "sort = " + mnSortingPos + ", " +
                    "filevault_id = '" + msFilevaultId + "', " +
                    "filevault_ts_n = " + "NOW()" + ", " +
                    "file_name = '" + msFileName + "', " +
                    "file_type = '" + msFileType + "', " +
                    "file_storage_name = '" + msFileStorageName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
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
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPaymentFile clone() throws CloneNotSupportedException {
        SDbPaymentFile registry = new SDbPaymentFile();
        
        registry.setPkPaymentId(this.getPkPaymentId());
        registry.setPkFileId(this.getPkFileId());
        registry.setPaymentFileType(this.getPaymentFileType());
        registry.setFileDescription(this.getFileDescription());
        registry.setSortingPos(this.getSortingPos());
        registry.setFilevaultId(this.getFilevaultId());
        registry.setFilevaultTs_n(this.getFilevaultTs_n());
        registry.setFileName(this.getFileName());
        registry.setFileType(this.getFileType());
        registry.setFileStorageName(this.getFileStorageName());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        msSql = "DELETE " + getSqlFromWhere();
        session.getStatement().execute(msSql);
        if (!msFilevaultId.isEmpty()) {
            SDocUtils.deleteFile(session, SDocUtils.BUCKET_DOC_DPS_SUPPLIER, msFilevaultId);
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = mnSortingPos;
                break;
            case 1:
                value = getFileTypeDescription(msPaymentFileType);
                break;
            case 2:
                value = msFileName;
                break;
            case 3:
                value = msFileDescription;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static String getFileTypeDescription(final String fileType) {
        String description;
        
        switch (fileType) {
            case FILE_TP_EF:
                description = DESC_FILE_TP_EF;
                break;
            case FILE_TP_RA:
                description = DESC_FILE_TP_RA;
                break;
            case FILE_TP_PF:
                description = DESC_FILE_TP_PF;
                break;
            case FILE_TP_OS:
                description = DESC_FILE_TP_OS;
                break;
            default:
                description = "?";
        }
        
        return description;
    }
}
