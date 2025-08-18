/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import erp.mod.cfg.db.SSyncType;
import erp.mod.cfg.swap.SSwapUtils;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SResponseInfo {

    protected SSyncType meSyncType;
    protected String msResponseMessage;
    protected boolean mbResponseOk;

    protected int mnRegistriesRetrieved;
    protected boolean mbRegistriesRetrievedSet;

    protected int mnIterations;
    protected int mnRegistriesProcessed;
    protected int mnRegistriesSynced;

    public SResponseInfo(final SSyncType syncType) {
        this(syncType, "", true);
    }

    public SResponseInfo(final SSyncType syncType, final String responseMessage, final boolean responseOk) {
        meSyncType = syncType;
        msResponseMessage = responseMessage;
        mbResponseOk = responseOk;

        mnRegistriesRetrieved = 0;
        mbRegistriesRetrievedSet = false;

        mnIterations = 0;
        mnRegistriesProcessed = 0;
        mnRegistriesSynced = 0;
    }

    public SSyncType getSyncType() {
        return meSyncType;
    }

    public String getResponseMessage() {
        return msResponseMessage;
    }

    public boolean isResponseOk() {
        return mbResponseOk;
    }

    public void setRegistriesRetrieved(int registriesRetrieved) {
        if (!mbRegistriesRetrievedSet) {
            mnRegistriesRetrieved = registriesRetrieved;
            mbRegistriesRetrievedSet = true;
        }
    }

    public int getRegistriesRetrieved() {
        return mnRegistriesRetrieved;
    }
    
    public boolean isRegistriesRetrievedSet() {
        return mbRegistriesRetrievedSet;
    }
    
    public void updateIteration(final double newRegistriesProcessed, final double newRegistriesSynced) {
        mnIterations++;
        mnRegistriesProcessed += newRegistriesProcessed;
        mnRegistriesSynced += newRegistriesSynced;
    }

    public int getIterations() {
        return mnIterations;
    }

    public int getRegistriesProcessed() {
        return mnRegistriesProcessed;
    }

    public int getRegistriesSynced() {
        return mnRegistriesSynced;
    }
    
    public int getRegistriesToProcess() {
        return mnRegistriesProcessed >= mnRegistriesRetrieved ? 0 : mnRegistriesRetrieved - mnRegistriesProcessed;
    }

    public boolean isProcessingComplete() {
        return mbRegistriesRetrievedSet && (mnRegistriesRetrieved == 0 || mnRegistriesProcessed >= mnRegistriesRetrieved);
    }

    public boolean isSyncComplete() {
        return mbRegistriesRetrievedSet && mnRegistriesProcessed >= mnRegistriesRetrieved && mnRegistriesSynced >= mnRegistriesProcessed;
    }

    public String getProgress() {
        return "Tipo de sincronización: '" + SSwapUtils.translateSyncType(meSyncType, SLibConsts.LAN_ISO639_ES) + "'.\n"
                + "Iteración = " + mnIterations + "; "
                + "registros obtenidos = " + mnRegistriesRetrieved + "; "
                + "registros procesados = " + mnRegistriesProcessed + "; "
                + "progreso = " + SLibUtils.DecimalFormatPercentage1D.format(mnRegistriesRetrieved == 0 ? 0d : (mnRegistriesProcessed / (double) mnRegistriesRetrieved)) + ".";
    }

    @Override
    public String toString() {
        String string = "Tipo de sincronización: '" + SSwapUtils.translateSyncType(meSyncType, SLibConsts.LAN_ISO639_ES) + "'.\n"
                + "- Registros: obtenidos = " + mnRegistriesRetrieved + "; procesados = " + mnRegistriesProcessed + "; sincronizados = " + mnRegistriesSynced + "; iteraciones = " + mnIterations + ".\n"
                + (isSyncComplete() ? "" : "- IMPORTANTE: ¡Aún quedan registros por sincronizar!\n")
                + (mbResponseOk && msResponseMessage.isEmpty() ? "- ¡Esta sincronización fue exitosa!" : "- Mensaje de respuesta: '" + msResponseMessage + "'.");

        return string;
    }
}
