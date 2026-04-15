/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import java.util.HashMap;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SExportDataSomUtils {
    
    public static HashMap<String, Object> createReportParamsMap(final SGuiSession session) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        // Parameters that need to be declared in reports:

        map.put("sCompanyName", session.getConfigCompany().getCompanyId());
        map.put("sUserName", session.getUser().getName());
        map.put("sAppName", "SOM");
        map.put("sAppCopyright", "COPYRIGTH");
        map.put("sAppProvider", "SWAPLICADO");
        map.put("sVendorCopyright", "AETH");
        map.put("sVendorWebsite", "SWAPLICADO.COM");

        // Optional parameters:

        map.put("oFormatDate", SLibUtils.DateFormatDate);
        map.put("oFormatDateShort", SLibUtils.DateFormatDateShort);
        map.put("oFormatDatetime", SLibUtils.DateFormatDatetime);
        map.put("oFormatDatetimeTic", SLibUtils.DateFormatDatetime);
        map.put("oFormatTime", SLibUtils.DateFormatTime);
        map.put("oFormatValue", SLibUtils.DecimalFormatValue0D);
        map.put("oFormatValue0D", SLibUtils.DecimalFormatValue0D);
        map.put("oFormatValue2D", SLibUtils.DecimalFormatValue2D);
        map.put("oFormatValue4D", SLibUtils.DecimalFormatValue4D);
        map.put("oFormatValue8D", SLibUtils.DecimalFormatValue8D);

        return map;
    }
}
