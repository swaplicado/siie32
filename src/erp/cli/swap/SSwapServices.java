/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli.swap;

import erp.SClientApi;
import erp.SParamsApp;
import erp.cli.SCliSendAuthMails;
import erp.mcfg.data.SDataParamsCompany;
import erp.musr.data.SDataUser;
import erp.swap.SSyncType;
import erp.swap.utils.SExportUtils;
import erp.swap.utils.SResponses;
import java.util.Date;
import java.util.logging.Logger;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 * Versión 2.0.
 * Se crea un cliente que implementa las interfaces SClientInterface, SGuiClient
 */
public class SSwapServices {

    private static final String DEF_DB = "erp_aeth";
    private static final int DEF_COMPANY_ID = 2852;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String host = "";
            String port = "";
            String dbName = DEF_DB;

            SDbDatabase dbCompany = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            SParamsApp paramsApp = new SParamsApp();
            if (!paramsApp.read()) {
                throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
            }

            if (args.length >= 3) {
                host = args[0];
                port = args[1];
                dbName = args[2];
            }
            else {
                host = paramsApp.getDatabaseHostClt();
                port = paramsApp.getDatabasePortClt();
            }

            int result = dbCompany.connect(host, port, dbName, paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());
            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }

            SGuiSession session = new SGuiSession(null);
            session.setDatabase(dbCompany);
            session.setSystemDate(new Date());
            
            SDataParamsCompany params = new SDataParamsCompany();
            params.read(new int[] { DEF_COMPANY_ID }, session.getStatement());
            session.setConfigCompany(params);

            SDataUser user = new SDataUser();
            user.read(new int[]{1}, session.getStatement());
            session.setUser(user);
            SResponses responses;
            SClientApi client = SCliSendAuthMails.createClientApi(session, 1);
            session.setClient(client);

            responses = SExportUtils.exportData(client.getSession(), SSyncType.PUR_ORDER, true, SExportUtils.EXPORT_MODE_SILENT);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);

            responses = SExportUtils.exportData(client.getSession(), SSyncType.PUR_PAYMENT, false, SExportUtils.EXPORT_MODE_SILENT);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);

            responses = SExportUtils.exportData(client.getSession(), SSyncType.PUR_REF_SCALE_TICKET, false, SExportUtils.EXPORT_MODE_SILENT);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
        }
        catch (Exception e) {
            Logger.getLogger(SSwapServices.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }
}
