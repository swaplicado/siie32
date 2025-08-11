/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.db;

import erp.mod.SModConsts;

/**
 * Clase para registrar la sincronización de datos con servicios externos.
 * 
 * IMPORTANTE:
 * La estructura de esta clase es idéntica a SDbSyncLog, salvo que se almacena en las BD de empresas.
 *
 * @author Sergio Flores
 */
public class SDbComSyncLog extends SDbSyncLog {
    
    public SDbComSyncLog() {
        mnRegistryType = SModConsts.CFG_COM_SYNC_LOG;
        
        moClass = getClass();
        moChildClass = SDbSyncLogEntry.class;
        mnChildType = SModConsts.CFG_SYNC_LOG_ETY;
    }
}
