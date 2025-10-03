/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DCfdUtils;
import erp.SClientUtils;
import erp.SFileUtilities;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.io.File;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Objects of this class must be read and saved throw SIIE Server to work properly. Otherwise phisical files can be read or saved in the wrong host.
 * 
 * @author Isabel Servín, Sergio Flores
 */
public final class SDataPdf extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    /** Original name of PDF file. */
    protected java.lang.String msDocPdfName;
    
    protected java.lang.String msPdfDirectory;
    protected java.lang.String msPdfAsBase64;
    
    protected boolean mbAuxSkipSave;
    protected boolean mbAuxDelete;
    
    public SDataPdf() {
        super(SDataConstants.TRN_PDF);
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDocPdfName(java.lang.String s) { msDocPdfName = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.lang.String getDocPdfName() { return msDocPdfName; }
    
    public void setAuxSkipSave(boolean b) { mbAuxSkipSave = b; }
    public void setAuxDeleted(boolean b) { mbAuxDelete = b; }
    
    public boolean getAuxSkipSave() { return mbAuxSkipSave; }
    public boolean getAuxDeleted() { return mbAuxDelete; }
    
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
        
        msPdfDirectory = "";
        msPdfAsBase64 = "";
        
        mbAuxSkipSave = false;
        mbAuxDelete = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM " + SClientUtils.getComplementaryDbName(statement.getConnection()) + ".trn_pdf WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + ";";
            resultSet = statement.executeQuery(sql);
            
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Registro " + SFileUtilities.PDF.toUpperCase() + ")");
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                msDocPdfName = resultSet.getString("doc_pdf_name");
                
                sql = "SELECT xml_base_dir FROM cfg_param_co LIMIT 1;";
                resultSet = statement.executeQuery(sql);
                
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Configuración empresa.)");
                }
                else {
                    msPdfDirectory = resultSet.getString("xml_base_dir");
                }
                
                readPdfFile();

                mbAuxSkipSave = true; // prevent from saving, unless it is explicitly commanded
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (Exception e) {
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
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (mbAuxSkipSave) {
                return mnLastDbActionResult;
            }
            
            String sql;
            
            if (!mbAuxDelete) {
                if (msDocPdfName.isEmpty()) {
                    throw new Exception("No se ha proporcionado el archivo " + SFileUtilities.PDF.toUpperCase() + ".");
                }
                
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
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                
                preparedStatement.setString(1, msDocPdfName);
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
    
    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
    
    /*
     * PDF file management:
     */
    
    private String composePdfDirectory() throws Exception {
        if (msPdfDirectory.isEmpty()) {
            throw new Exception("No se ha proporcionado el directorio " + SFileUtilities.PDF.toUpperCase() + ".");
        }
        else if (mnPkYearId == 0) {
            throw new Exception("No se ha proporcionado la clave primaria del registro " + SFileUtilities.PDF.toUpperCase() + ".");
        }
        
        return msPdfDirectory + (msPdfDirectory.endsWith("/") ? "" : "/") + "ext/" + mnPkYearId;
    }
    
    private String composePdfFileNameStd() throws Exception {
        if (mnPkYearId == 0 || mnPkDocId == 0) {
            throw new Exception("No se ha proporcionado la clave primaria del registro " + SFileUtilities.PDF.toUpperCase() + ".");
        }
        
        return mnPkYearId + "_" + DCfdUtils.CfdNumberFormat.format(mnPkDocId) + "." + SFileUtilities.PDF;
    }
    
    private File createPdfFile() throws Exception {
        return new File(composePdfDirectory() + "/" + composePdfFileNameStd());
    }
    
    /**
     * Upoad original PDF file from user's file system.
     * @param pdfFile PDF file.
     * @param pdfDirectory PDF base directory for storage.
     * @throws Exception 
     */
    public void uploadPdfFile(final File pdfFile, final String pdfDirectory) throws Exception {
        if (pdfFile.getName().toLowerCase().endsWith("." + SFileUtilities.PDF)) {
            msPdfDirectory = pdfDirectory;
            
            byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
            msPdfAsBase64 = Base64.getEncoder().encodeToString(pdfBytes);
            
            msDocPdfName = pdfFile.getName();
        }
        else {
            throw new Exception("El archivo '" + pdfFile.getName() + "' debe ser " + SFileUtilities.PDF.toUpperCase() + ".");
        }
    }
    
    /**
     * Download original PDF file to user's file system.
     * @param client GUI client.
     * @throws Exception 
     */
    public void downloadPdfFile(SClientInterface client) throws Exception {
        if (msPdfAsBase64.isEmpty()) {
            throw new Exception("No se ha codificado el archivo " + SFileUtilities.PDF.toUpperCase() + ".");
        }
        
        JFileChooser fileChooser = null;
        Exception exception = null;
        
        try {
            File pdfFile = null;
            FileFilter filter = SFileUtilities.createFileNameExtensionFilter(SFileUtilities.PDF);
            
            fileChooser = client.getFileChooser();
            fileChooser.repaint();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(filter);

            fileChooser.setSelectedFile(new File(msDocPdfName));

            if (fileChooser.showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                pdfFile = fileChooser.getSelectedFile();

                // Ensure file ends with ".pdf"
                if (!pdfFile.getName().toLowerCase().endsWith("." + SFileUtilities.PDF)) {
                    pdfFile = new File(pdfFile.getAbsolutePath() + "." + SFileUtilities.PDF);
                }
            }

            if (pdfFile != null) {
                byte[] pdfBytes = Base64.getDecoder().decode(msPdfAsBase64);
                Files.write(pdfFile.toPath(), pdfBytes);

                client.showMsgBoxInformation("El archivo " + SFileUtilities.PDF.toUpperCase() + " ha sido descargado en:\n" + pdfFile.getAbsolutePath());
            }
        }
        catch (Exception e) {
            exception = e;
        }
        finally {
            if (fileChooser != null) {
                fileChooser.resetChoosableFileFilters();
                fileChooser.setAcceptAllFileFilterUsed(true);
            }
        }
        
        if (exception != null) {
            throw exception;
        }
    }
    
    private void readPdfFile() throws Exception {
        File file = createPdfFile();
        
        if (file.exists()) {
            byte[] pdfBytes = Files.readAllBytes(file.toPath());
            msPdfAsBase64 = Base64.getEncoder().encodeToString(pdfBytes);
        }
    }
    
    private void savePdfFile() throws Exception {
        if (msPdfAsBase64.isEmpty()) {
            throw new Exception("No se ha codificado el archivo " + SFileUtilities.PDF.toUpperCase() + ".");
        }
        
        File directory = new File(composePdfDirectory());
        
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        byte[] pdfBytes = Base64.getDecoder().decode(msPdfAsBase64);
        Files.write(createPdfFile().toPath(), pdfBytes);
        
        /* Preserve as reference former code to save PDF file:
        Files.copy(Paths.get(moPdfFile.getAbsolutePath()), Paths.get(directory.getAbsolutePath() + "/" + msDocPdfName), StandardCopyOption.REPLACE_EXISTING);
        */
    }
    
    private void deletePdfFile() throws Exception {
        File file = createPdfFile();
        
        if (file.exists()) {
            file.delete();
        }
    }
    
    /*
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
    */
}
