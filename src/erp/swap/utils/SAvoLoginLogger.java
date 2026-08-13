/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import java.util.Date;

/**
 *
 * @author Rodrigo Ayala
 */
public class SAvoLoginLogger {
    
    public static void logFailure(String errorMessage) {
        try {
            Date now = new Date();
            
            String timestamp = SExportUtils.FormatSyncLogDatetime.format(now);
            String FileName = "error_login_AVO_" + timestamp;
            
            SExportLogsUtils.safeWriteToLogFile(FileName, errorMessage);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
