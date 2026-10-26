/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli.swap;

import erp.data.SDataConstantsSys;
import erp.swap.SSyncType;
import erp.swap.utils.SExportUtils;
import erp.swap.utils.SResponses;
import java.util.logging.Logger;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 * Versión 1.0
 * dejada de utilizar para la exportación de registros para swapservices
 * 2026-04-30
 */
public class SSwapServices1 {
    
    private static final String DEF_HOST = "192.168.1.233";
    private static final String DEF_PORT = "3306";
    private static final String DEF_DB = "erp_aeth";
    private static final int DEF_COMPANY_ID = 2852;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String host = DEF_HOST;
            String port = DEF_PORT;
            String db = DEF_DB;
            
            if (args.length >= 3) {
                host = args[0];
                port = args[1];
                db = args[2];
            }
            
            SResponses responses;
            SSwapClient client = new SSwapClient(host, SLibUtils.parseInt(port), db, false, SDataConstantsSys.USRX_USER_ADMIN, DEF_COMPANY_ID);
            
            responses = SExportUtils.exportData(client.getSession(), SSyncType.PUR_ORDER, true, SExportUtils.EXPORT_MODE_SILENT);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
            
            responses = SExportUtils.exportData(client.getSession(), SSyncType.PUR_PAYMENT, false, SExportUtils.EXPORT_MODE_SILENT);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
            
            responses = SExportUtils.exportData(client.getSession(), SSyncType.PUR_REF_SCALE_TICKET, false, SExportUtils.EXPORT_MODE_SILENT);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
        }
        catch (Exception e) {
            Logger.getLogger(SSwapServices1.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }
}
