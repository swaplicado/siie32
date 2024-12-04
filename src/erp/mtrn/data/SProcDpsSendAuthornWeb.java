/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SDbDps;
import erp.mod.trn.db.SDbSupplierFileProcess;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Isabel Servín
 */
public class SProcDpsSendAuthornWeb extends Thread {

    private final SClientInterface miClient;
    private final SDbSupplierFileProcess moSuppFileProc;
    private final ArrayList<SDbAuthorizationPath> maAuthPaths;
    
    public SProcDpsSendAuthornWeb(SClientInterface client, SDbSupplierFileProcess proc, ArrayList<SDbAuthorizationPath> paths) {
        miClient = client;
        moSuppFileProc = proc;
        maAuthPaths = paths;
        setDaemon(true);
    }
    
    @Override
    public void run() {
        try {
            Connection connection = miClient.getSession().getDatabase().getConnection();
            SDbDps dps = moSuppFileProc.getDps();
            SDataDpsAuthorn auth = new SDataDpsAuthorn();
            auth.setPrimaryKey(dps.getPrimaryKey());
            auth.setFkAuthorizationStatusId(SDataConstantsSys.CFGS_ST_AUTHORN_SND);
            auth.setFkUserId(miClient.getSession().getUser().getPkUserId());
            auth.save(connection);

            if (sendAuthorn()) {
                auth.setFkAuthorizationStatusId(SDataConstantsSys.CFGS_ST_AUTHORN_PEND);
                auth.save(connection);
                moSuppFileProc.updateDpsStatus(miClient.getSession(), SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING);
            }
            else {
                auth.delete(connection);
            }
            
            //miClient.getGuiModule(SDataConstants.MOD_PUR).refreshCatalogues(SDataConstants.TRN_DPS);
            miClient.getGuiModule(SDataConstants.MOD_PUR).refreshCatalogues(SDataConstants.TRNX_DPS_AUTH_APP);
        }
        catch(Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }
    
    private boolean sendAuthorn() throws Exception {
        boolean send = true;
        
        Thread.sleep(60000);
        
        SAuthorizationUtils.processAuthorizationsDps(miClient.getSession(), maAuthPaths, 
                SAuthorizationUtils.AUTH_TYPE_DPS, moSuppFileProc.getPrimaryKey(), true);
        
        System.out.println("Documento enviado con éxito.");
        return send;
    }
}
