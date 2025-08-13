/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.db;

import erp.mod.SModConsts;

/**
 * Clase para registrar el detalle de la sincronización de datos con servicios externos.
 * 
 * Esta clase representa una entrada de registro de sincronización, que almacena
 * información sobre cada sincronización realizada, incluyendo el tipo de
 * sincronización, cuerpo de la solicitud, código de respuesta, cuerpo de la
 * respuesta, y marcas de tiempo.
 *
 * IMPORTANTE:
 * La estructura de esta clase es idéntica a SDbSyncLogEntry, salvo que se almacena en las BD de empresas.
 *
 * @author Sergio Flores
 */
public class SDbComSyncLogEntry extends SDbSyncLogEntry {
    
    public SDbComSyncLogEntry() {
        mnRegistryType = SModConsts.CFG_COM_SYNC_LOG_ETY;
        
        moClass = getClass();
    }
}
