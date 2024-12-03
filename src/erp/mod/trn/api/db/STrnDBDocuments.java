/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.db;

import com.swaplicado.cloudstoragemanager.CloudStorageManager;
import com.swaplicado.data.StorageManagerException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import erp.mod.SModConsts;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.trn.api.data.SWebDpsFile;
import erp.mod.trn.api.data.SWebFile;

/**
 *
 * @author Edwin Carmona
 */
public class STrnDBDocuments {
    
    /**
     * Obtiene los archivos de un documento
     * 
     * @param idYear
     * @param idDoc
     * 
     * 
     * @return ArrayList<SWebDpsFile> 
     */
    public static ArrayList<SWebDpsFile> getDpsFiles(final int idYear, final int idDoc) {
        try {
            SMySqlClass mdb = new SMySqlClass();
            String dbName = mdb.getMainDatabaseName();
            Connection conn = mdb.connect("", "", dbName, "", "");

            if (conn == null) {
                return null;
            }

            String query = "SELECT  " +
                    "    fdps.*, " +
                    "    f.num, " +
                    "    f.tot_cur_quot, " +
                    "    f.ext_bp_name, " +
                    "    f.filevault_id, " +
                    "    f.file_name, " +
                    "    f.file_storage_name, " +
                    "    f.file_type, " +
                    "    f.fk_cur_quot, " +
                    "    COALESCE(bp.bp, f.ext_bp_name) AS bp_name " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_SUP_FILE_DPS) + " AS fdps " +
                    "        INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_SUP_FILE) + " AS f ON fdps.id_sup_file = f.id_sup_file " +
                    "        LEFT JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON f.fid_bp_n = bp.id_bp " +
                    "WHERE " +
                    "    NOT f.b_del " +
                    "   AND fdps.id_year = " + idYear + " " +
                    "   AND fdps.id_doc = " + idDoc + ";";

            Statement st = conn.createStatement();
            System.out.println(query);
            ResultSet res = st.executeQuery(query);
            ArrayList<SWebDpsFile> lWebDpsFiles = new ArrayList<>();
            while (res.next()) {
                SWebDpsFile oWebDpsFile = new SWebDpsFile();
                oWebDpsFile.setIdSupFile(res.getInt("id_sup_file"));
                oWebDpsFile.setIdYear(res.getInt("id_year"));
                oWebDpsFile.setIdDoc(res.getInt("id_doc"));
                oWebDpsFile.setSort(res.getInt("sort"));
                oWebDpsFile.setFileType(res.getString("fdps.sup_file_dps_type"));
                oWebDpsFile.setTotalLocal(res.getDouble("fdps.tot_loc_r"));
                oWebDpsFile.setExchangeRateLocal(res.getDouble("fdps.exc_rate_loc"));
                oWebDpsFile.setTotalDps(res.getDouble("fdps.tot_dps_r"));
                oWebDpsFile.setExchangeRateDps(res.getDouble("fdps.exc_rate_dps"));
                oWebDpsFile.setTotalCurQuot(res.getDouble("fdps.tot_cur_quot"));
                oWebDpsFile.setNotes(res.getString("fdps.nts"));
                oWebDpsFile.setExtTemp(res.getBoolean("fdps.b_extemp"));
                oWebDpsFile.setFkDpsCurrency(res.getInt("fdps.fk_cur_dps"));
                oWebDpsFile.setFkQuotCurrency(res.getInt("fdps.fk_cur_quot"));

                SWebFile oFile = new SWebFile();
                oFile.setIdSupFile(res.getInt("id_sup_file"));
                oFile.setTotalCurrencyQuot(res.getDouble("f.tot_cur_quot"));
                oFile.setFileNumber(res.getString("f.num"));
                oFile.setUserFileName(res.getString("f.file_name"));
                oFile.setMongoId(res.getString("f.filevault_id"));
                oFile.setCloudStorageName(res.getString("f.file_storage_name"));
                oFile.setFileExtension(res.getString("f.file_type"));
                oFile.setExternalBpName(res.getString("bp_name"));
                oFile.setFkCurQuot(res.getInt("f.fk_cur_quot"));
                if (oFile.getCloudStorageName() != null && !oFile.getCloudStorageName().isEmpty()) {
                    System.out.println("Storage name: " + oFile.getCloudStorageName());
                    try {
                        oFile.setCloudFileUrl(CloudStorageManager.generatePresignedUrl(oFile.getCloudStorageName()));
                    } catch (StorageManagerException ex) {
                        Logger.getLogger(STrnDBDocuments.class.getName()).log(Level.SEVERE, null, ex);
                        oFile.setCloudFileUrl("");
                    }
                }

                oWebDpsFile.setoWebFile(oFile);

                lWebDpsFiles.add(oWebDpsFile);
            }

            return lWebDpsFiles;
        } catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SConfigException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }
}
