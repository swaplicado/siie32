/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DCfdUtils;
import erp.SClientUtils;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import sa.lib.SLibConsts;

/**
 *
 * @author Isabel Servín
 */
public final class SDataPdf extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.lang.String msDocPdfName;
    
    protected File moAuxDocPdfFile;
    protected java.lang.String msAuxXmlBaseDirectory;
    protected boolean mbAuxToBeDeleted;
    
    public SDataPdf() {
        super(SDataConstants.TRN_PDF);
        reset();
    }
    
    public static String composePdfName(final int yearId, final int docId) {
        return yearId + "_" + DCfdUtils.CfdNumberFormat.format(docId) + ".pdf";
    }
    
    public static String composePdfDirectory(final String xmlBaseDirectory, final int yearId) {
        return xmlBaseDirectory + (xmlBaseDirectory.charAt(xmlBaseDirectory.length()-1) == '/' ? "" : "/") + "ext/" + yearId;
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDocPdfName(java.lang.String s) { msDocPdfName = s; }
    
    public void setAuxDocPdfFile(File f) { moAuxDocPdfFile = f; }
    public void setAuxXmlBaseDirectory(java.lang.String s) { msAuxXmlBaseDirectory = s; }
    public void setAuxToBeDeleted(boolean b) { mbAuxToBeDeleted = b; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.lang.String getDocPdfName() { return msDocPdfName; }
    
    public File getAuxDocPdfFile() { return moAuxDocPdfFile; }
    public java.lang.String getAuxXmlBaseDirectory() { return msAuxXmlBaseDirectory; }
    public boolean getAuxToBeDeleted() { return mbAuxToBeDeleted; }
    
    /*
     * Implementation of erp.lib.data.SDataRegistry
     */
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        msDocPdfName = "";
        
        moAuxDocPdfFile = null;
        msAuxXmlBaseDirectory = "";
        mbAuxToBeDeleted = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM " + SClientUtils.getComplementaryDbName(statement.getConnection()) + ".trn_pdf WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                msDocPdfName = resultSet.getString("doc_pdf_name");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_READ;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int index = 1;
        
        String sql;
        PreparedStatement preparedStatement;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (!mbAuxToBeDeleted) {
                if (mbIsRegistryNew) {
                    sql = "INSERT INTO " + SClientUtils.getComplementaryDbName(connection) + ".trn_pdf "
                            + "(id_year, id_doc, doc_pdf_name) "
                            + "VALUES (" + mnPkYearId + ", " + mnPkDocId + ", ?)";
                }
                else {
                    sql = "UPDATE " + SClientUtils.getComplementaryDbName(connection) + ".trn_pdf "
                            + "SET doc_pdf_name = ? "
                            + "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + ";"; 
                }
                msDocPdfName = composePdfName(mnPkYearId, mnPkDocId);

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(index++, msDocPdfName);
                preparedStatement.execute();

                savePdfFile();
            }
            else {
                sql = "DELETE FROM " + SClientUtils.getComplementaryDbName(connection) + ".trn_pdf "
                        + "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + ";";
                connection.createStatement().execute(sql);
                
                deletePdfFile();
            }
            
            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_SAVE;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    
    private void savePdfFile() throws Exception {
        File destiny = new File(composePdfDirectory(msAuxXmlBaseDirectory, mnPkYearId));
        if (!destiny.exists()){
            destiny.mkdirs();
        }
        Files.copy(Paths.get(moAuxDocPdfFile.getAbsolutePath()), Paths.get(destiny.getAbsolutePath() + "/" + msDocPdfName), StandardCopyOption.REPLACE_EXISTING);
    }
    
    private void deletePdfFile() throws Exception {
        File delete = new File (composePdfDirectory(msAuxXmlBaseDirectory, mnPkYearId) + "/" + msDocPdfName);
        if (delete.exists()) {
            delete.delete();
        }
    }
    
    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
    
    @Override
    public int canSave(java.sql.Connection connection) {
        return mnLastDbActionResult;
    }

    @Override
    public int canAnnul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;
        return mnLastDbActionResult;
    }
    
    @Override
    public int annul(java.sql.Connection connection) {
        return mnLastDbActionResult;
    }
}
