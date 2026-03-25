/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver4.DCfdVer4Consts;
import erp.client.SClientInterface;
import erp.data.SDataUtilities;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDatePicker;
import sa.lib.gui.SGuiSession;

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
    
    /**
     * Get exchange rate in desired date, if available.
     * @param session GUI session.
     * @param currencyId ID of currency.
     * @param date Desired date.
     * @return
     * @throws Exception
     */
    public static double getExchangeRate(final SGuiSession session, final int currencyId, final Date date) throws Exception {
        double exchangeRate = 0;
        int[] currencyKey = new int[] { currencyId };

        if (session.getSessionCustom().isLocalCurrency(currencyKey)) {
            exchangeRate = 1;
        }
        else {
            exchangeRate = SDataUtilities.obtainExchangeRate((SClientInterface) session.getClient(), currencyId, date);

            if (exchangeRate == 0) {
                throw new Exception("No se ha capturado el tipo de cambio " + session.getSessionCustom().getLocalCurrencyCode() + "/" + session.getSessionCustom().getCurrencyCode(currencyKey) + " para el día " + SLibUtils.DateFormatDate.format(date) + ".");
            }
        }
        
        return exchangeRate;
    }
    

    /**
     * Pick a date.
     * @param session GUI session.
     * @param currentDate Current date. Can be <code>null</code>.
     * @return 
     */
    public static Date pickDate(final SGuiSession session, final Date currentDate) {
        Date date = null;
        
        SGuiDatePicker picker = session.getClient().getDatePicker();
        picker.resetPicker();
        picker.setOption(currentDate);
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            date = picker.getOption();
        }
        
        return date;
    }
}
