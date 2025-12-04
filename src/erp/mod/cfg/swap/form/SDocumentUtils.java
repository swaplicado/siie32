/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver4.DCfdVer4Consts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDocumentUtils {
    
    /**
     * Get UUID first segment.
     * @param uuid
     * @return 
     */
    public static String getUuidFirstSegment(final String uuid) {
        String firstSegment = "";
        
        if (uuid.length() == DCfdVer4Consts.LEN_UUID) {
            firstSegment = SLibUtils.textLeft(uuid.toUpperCase(), DCfdVer4Consts.LEN_UUID_1ST_SEGMENT);
        }
        
        return firstSegment;
    }
    
    /**
     * Compose folio in format series-number, if available, otherwise get first segment of UUID, if available, otherwise return non folio alusive text.
     * @param numberSeries
     * @param number
     * @param uuid
     * @return 
     */
    public static String composeFolio(final String numberSeries, final String number, final String uuid) {
        String folio = numberSeries + (numberSeries.isEmpty() ? "" : "-") + number;
        
        return !folio.isEmpty() ? folio : "[" + (!uuid.isEmpty() ? SDocumentUtils.getUuidFirstSegment(uuid) + "..." : SDocumentInfo.NON_FOLIO) + "]";
    }
}
