/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver4.DCfdVer4Consts;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDocumentInfo implements SDocument {
    
    public static final String NON_FOLIO = "SIN FOLIO";
    
    protected String msNumberSeries;
    protected String msNumber;
    protected String msUuid;
    protected Date mtDate;
    protected String msIssuer;
    
    public SDocumentInfo(final String numberSeries, final String number, final String uuid, final Date date, final String issuer) {
        msNumberSeries = numberSeries;
        msNumber = number;
        msUuid = uuid;
        mtDate = date;
        msIssuer = issuer;
    }

    public SDocumentInfo(final SImportedDocument document) {
        this(document.NumberSeries, document.Number, document.ExternalDocumentUuid, document.Date, document.BizPartner);
    }

    public String getNumberSeries() {
        return msNumberSeries;
    }
    
    public String getNumber() {
        return msNumber;
    }
    
    public String getUuid() {
        return msUuid;
    }
    
    public Date gettDate() {
        return mtDate;
    }

    @Override
    public String getFolio() {
        return SDocumentInfo.composeFolio(msNumberSeries, msNumber, msUuid);
    }
    
    @Override
    public String getIssuer() {
        return msIssuer;
    }
    
    public static String composeFolio(final String numberSeries, final String number, final String uuid) {
        String folio = numberSeries + (numberSeries.isEmpty() ? "" : "-") + number;
        return !folio.isEmpty() ? folio : "[" + (!uuid.isEmpty() ? SLibUtils.textLeft(uuid.toUpperCase(), DCfdVer4Consts.LEN_UUID_1ST_SEGMENT) + "...": SDocumentInfo.NON_FOLIO) + "]";
    }
}
