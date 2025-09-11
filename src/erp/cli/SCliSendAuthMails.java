/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SClientApi;
import erp.SParamsApp;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.utils.SDpsGoogleCloudUtils;
import erp.mod.cfg.swap.utils.SFileData;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.musr.data.SDataUser;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SCliSendAuthMails {
   
    public static void main(String[] args) {
        SParamsApp paramsApp = new SParamsApp();
        SDbDatabase dbCompany = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        
        try {
            if (!paramsApp.read()) {
                throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
            }
            
            int result = dbCompany.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), 
                    "erp_aeth", paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());
            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            run(dbCompany);
            
            dbCompany.disconnect();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
    
    private static void run(SDbDatabase dbCompany) throws SQLException {
        SGuiSession session = new SGuiSession(null);
        session.setDatabase(dbCompany);
        
        SDataUser user = new SDataUser();
        user.read(new int[] { 1 }, session.getStatement());
        session.setUser(user);
        
        SClientApi client = createClientApi(session , 1);
        
        String sql = "SELECT d.id_year, d.id_doc, d.ts_edit " +
                "FROM trn_dps AS d " +
                "LEFT JOIN trn_dps_snd_log AS l ON " +
                "d.id_year = l.id_year AND d.id_doc = l.id_doc AND l.b_snd AND l.id_snd = " +
                "  (SELECT MAX(aux.id_snd) FROM trn_dps_snd_log AS aux WHERE l.id_year = aux.id_year and l.id_doc = aux.id_doc) " +
                "WHERE d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " " +
                "AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " " +
                "AND d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + " " +
                "AND d.fid_st_dps_authorn IN (" + SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN + ") " +
                "AND d.ts_authorn >= NOW() - INTERVAL 24 HOUR " +
                "AND NOT d.b_del " +
                "AND (d.ts_authorn > l.ts OR l.ts IS NULL);";
        
        ResultSet resultSet = session.getDatabase().getConnection().createStatement().executeQuery(sql);
        HashMap<SFileData, File> mFiles = new HashMap<>();
        while (resultSet.next()) {
            try {
                int idYear = resultSet.getInt("d.id_year");
                int idDoc = resultSet.getInt("d.id_doc");
                File oPdf = SAuthorizationUtils.sendAutomaticProviderAuthornMails(client, new int[] { idYear, idDoc });
                SFileData oFileData = new SFileData(idYear, idDoc, dbCompany.getDbName(), resultSet.getTimestamp("d.ts_edit"));
                mFiles.put(oFileData, oPdf);
            }
            catch (Exception ex) {
                Logger.getLogger(SCliSendAuthMails.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Envío de PDF de OCs a Google Cloud Storage
        SDpsGoogleCloudUtils.uploadFiles(session, mFiles);
    }
    
    private static SClientApi createClientApi(SGuiSession session, int userId) {
        SClientApi clientApi = new SClientApi(session, userId);
        return clientApi;
    }
}
