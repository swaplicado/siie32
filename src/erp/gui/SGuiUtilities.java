package erp.gui;

import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SGuiUtilities {
    
    public static String validateDateRange(final java.util.Date start, final java.util.Date end) {
        String msg = "";
        
        if (start.after(end)) {
            msg = "La fecha inicial no puede ser posterior a la fecha final.";
        }
        else if (SLibTimeUtils.digestYear(start)[0] != SLibTimeUtils.digestYear(end)[0]) {
            msg = "La fecha inicial y final deben pertenecer al mismo ejercicio.";
        }
        
        return msg;
    }
    
    public static int getPeriodMonths(final java.util.Date start, final java.util.Date end) throws Exception {
        if (start == null) {
            throw new Exception("La fecha inicial no existe.");
        }
        if (end == null) {
            throw new Exception("La fecha final no existe.");
        }
        if (start.after(end)) {
            throw new Exception("La fecha inicial no puede ser posterior a la fecha final.");
        }
        
        int[] periodStart = SLibTimeUtils.digestMonth(start);
        int[] periodEnd = SLibTimeUtils.digestMonth(end);

        return (periodEnd[0] - periodStart[0]) * SLibTimeConsts.MONTH_MAX + periodEnd[1] - periodStart[1] + 1;
    }
}
