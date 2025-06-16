/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SParamsApp;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.musr.data.SDataUser;
import erp.siieapp.SAppLinkResponse;
import erp.siieapp.SAuthorizationsAPI;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;

/**
 * Clase para simular una autorización de DPS por la vía web.
 * @author Isabel Servín
 */
public class SCliAuthornDps {
    
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
            SLibUtils.printException(SCliAuthornDps.class.getName(), e);
            System.exit(-1);
        }
    }

    private static void run(SDbDatabase dbCompany) throws Exception {
        SGuiSession session = new SGuiSession(null);
        session.setDatabase(dbCompany);
        
        SDataUser user = new SDataUser();
        user.read(new int[] { 2 }, session.getStatement());
        session.setUser(user);
        
        SAuthorizationsAPI api = new SAuthorizationsAPI(session);
        SAppLinkResponse resp = api.approbeResource(SAuthorizationUtils.AUTH_TYPE_DPS, new int[] { 2025, 6057 }, 2, "");
        //String resp = api.rejectResource(SAuthorizationUtils.AUTH_TYPE_DPS, new int[] { 2025, 6057 }, 2, "");
    
        System.out.println("proceso terminado!");
    }
}
