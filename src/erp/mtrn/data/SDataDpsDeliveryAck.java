/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mcfg.data.SDataParamsCompany;
import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Daniel López
 */
public class SDataDpsDeliveryAck extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    private static final int ID_LEN = 10;

    protected int mnPkDpsDeliveryAckId;
    protected String msNameUser;
    protected String msNameSystem;
    protected boolean mbIsDeleted;
    protected int mnFkYearId;
    protected int mnFkDocId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.io.File moAuxFile;
    protected java.lang.String msAuxPrefix;
    protected String msTempFileName;

    public SDataDpsDeliveryAck() {
        super(SDataConstants.TRN_DPS_ACK);
        reset();
    }

    /*
     * Private methods
     */
    
    private void renameFile() {
        DecimalFormat format = new DecimalFormat(SLibUtils.textRepeat("0", ID_LEN));
        File directory = new File(SDataParamsCompany.FILES_DIR);
        File[] docs = directory.listFiles();
        
        if (docs != null) {
            for (File file : docs) {
                if (msTempFileName.equalsIgnoreCase(file.getName())) {
                    msNameSystem = msAuxPrefix + "_" + format.format(mnPkDpsDeliveryAckId) + "_" + msNameUser;
                    
                    file.renameTo(new File(SDataParamsCompany.FILES_DIR + "\\" + msNameSystem));
                }
            }
        }
    }
    
    /*
     * Public methods
     */
    
    public void setPkDpsDeliveryAckId(int n) { mnPkDpsDeliveryAckId = n; }

    public void setNameUser(java.lang.String s) { msNameUser = s; }

    public void setNameSystem(java.lang.String s) { msNameSystem = s;}

    public void setIsDeleted(boolean b) { mbIsDeleted = b; }

    public void setFkYearId(int n) { mnFkYearId = n; }

    public void setFkDocId(int n) { mnFkDocId = n; }

    public void setFkUserNewId(int n) { mnFkUserNewId = n; }

    public void setFkUserEditId(int n) { mnFkUserEditId = n; }

    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }

    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }

    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }

    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkDpsDeliveryAckId() { return mnPkDpsDeliveryAckId; }

    public java.lang.String getNameUser() { return msNameUser; }

    public java.lang.String getNameSystem() { return msNameSystem; }

    public boolean getIsDeleted() { return mbIsDeleted; }

    public int getFkYearId() { return mnFkYearId; }

    public int getFkDocId() { return mnFkDocId; }

    public int getFkUserNewId() { return mnFkUserNewId; }

    public int getFkUserEditId() { return mnFkUserEditId; }

    public int getFkUserDeleteId() { return mnFkUserDeleteId; }

    public java.util.Date getUserNewTs() { return mtUserNewTs; }

    public java.util.Date getUserEditTs() { return mtUserEditTs; }

    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setAuxFile(File o) { moAuxFile = o; }

    public void setAuxPrefix(java.lang.String s) { msAuxPrefix = s; }

    public File getAuxFile() { return moAuxFile; }

    public java.lang.String getAuxPrefix() { return msAuxPrefix; }
    
    public void setTempFileName(String s) { msTempFileName = s; }
    
    public String getTempFileName() { return msTempFileName; }

    public void saveFileSystemPath() throws Exception {
        renameFile();
    }
    
    public void saveFileCustomPath(final String customPath) throws Exception {        
        CopyOption[] options = new CopyOption[] {       // options for new file
                    StandardCopyOption.REPLACE_EXISTING,    // REPLACE_EXISTING: replace the file if exists.
                    StandardCopyOption.COPY_ATTRIBUTES      // COPY_ATTRIBUTES like last_modified, etc.
                };

            Files.copy(Paths.get(moAuxFile.getAbsolutePath()), Paths.get(customPath + "\\" + msNameUser), options);
    }
    
    public File createFileFromSystemPath() {
        return new File(SDataParamsCompany.FILES_DIR, msNameSystem);
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDpsDeliveryAckId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsDeliveryAckId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDpsDeliveryAckId = 0;
        msNameUser = "";
        msNameSystem = "";
        mbIsDeleted = false;
        mnFkYearId = 0;
        mnFkDocId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        moAuxFile = null;
        msAuxPrefix = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * "
                    + "FROM trn_dps_ack "
                    + "WHERE id_dps_ack = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDpsDeliveryAckId = resultSet.getInt("id_dps_ack");
                mnFkYearId = resultSet.getInt("fid_year");
                mnFkDocId = resultSet.getInt("fid_doc");
                msNameUser = resultSet.getString("name_usr");
                msNameSystem = resultSet.getString("name_sys");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        } catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        } catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;

        mnLastDbActionResult = SDbConsts.SAVE_ERROR;

        try {
            statement = connection.createStatement();

                mnPkDpsDeliveryAckId = 0;
                mbIsDeleted = false;
                mnFkUserEditId = 1;
                mnFkUserDeleteId = 1;

                sql = "SELECT COALESCE(MAX(id_dps_ack), 0) + 1 FROM trn_dps_ack ";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    mnPkDpsDeliveryAckId = resultSet.getInt(1);
                }

                saveFileSystemPath();
                
                sql = "INSERT INTO trn_dps_ack VALUES ("
                        + mnPkDpsDeliveryAckId + ", "
                        + "'" + msNameUser + "', "
                        + "'" + msNameSystem + "', "
                        + mbIsDeleted + ", "
                        + mnFkYearId + ", "
                        + mnFkDocId + ", "
                        + mnFkUserNewId + ", "
                        + mnFkUserEditId + ", "
                        + mnFkUserDeleteId + ", "
                        + "NOW(), "
                        + "NOW(), "
                        + "NOW()"
                        + ")";

            statement.execute(sql);

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        } catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}