/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli.swap;

import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SResponses;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SSwapServices {
    
    private static final String DEF_HOST = "192.168.1.233";
    private static final String DEF_PORT = "3306";
    private static final String DEF_DB = "erp_aeth";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String host = DEF_HOST;
            String port = DEF_PORT;
            String db = DEF_DB;
            
            if (args.length == 3) {
                host = args[0];
                port = args[1];
                db = args[2];
            }
            
            SResponses responses;
            SSwapClient client = new SSwapClient(host, SLibUtils.parseInt(port), db);
            
            responses = SExportUtils.exportData(client.getSession(), SSyncType.USER);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
            
            responses = SExportUtils.exportData(client.getSession(), SSyncType.PARTNER_SUPPLIER);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
            
            /* Not implemented yet!
            responses = SExportUtils.exportData(client.getSession(), SSyncType.PARTNER_CUSTOMER);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
            */
            
            responses = SExportUtils.exportData(client.getSession(), SSyncType.PUR_REF_ORDER);
            SExportUtils.processResponses(client.getSession(), responses, 0, 0);
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
