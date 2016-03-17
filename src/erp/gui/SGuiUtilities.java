package erp.gui;

import sa.lib.SLibTimeUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SGuiUtilities {
    
    public static String validateDateRange(final java.util.Date dateStart, final java.util.Date dateEnd) {
        String msg = "";
        
        if (dateEnd.before(dateStart)) {
            msg = "La fecha final no puede ser anterior a la fecha inicial.";
        }
        else if (SLibTimeUtils.digestYear(dateStart)[0] != SLibTimeUtils.digestYear(dateEnd)[0]) {
            msg = "La fecha inicial y final deben pertenecer al mismo ejercicio.";
        }
        
        return msg;
    }
}
