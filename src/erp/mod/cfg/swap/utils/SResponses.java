/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import erp.mod.cfg.db.SSyncType;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SResponses {
    
    protected SSyncType meSyncType;
    protected ArrayList<SResponseInfo> maInfos;
    
    public SResponses(final SSyncType syncType) {
        meSyncType = syncType;
        maInfos = new ArrayList<>();
    }
    
    public SSyncType getSyncType() { return meSyncType; }
    public ArrayList<SResponseInfo> getInfos() { return maInfos; }
    
    public boolean isResponsesOk() throws Exception {
        if (maInfos.isEmpty()) {
            throw new Exception("¡No hay respuestas de exportación!");
        }
        
        boolean ok = true;
        
        for (SResponseInfo info : maInfos) {
            if (!info.isResponseOk()) {
                ok = false;
                break;
            }
        }
        
        return ok;
    }
    
    @Override
    public String toString() {
        String string = "";
        
        for (SResponseInfo info : maInfos) {
            string += (string.isEmpty() ? "" : "\n") + info;
        }
        
        return string;
    }
}
