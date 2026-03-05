/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import java.util.Date;

/**
 *
 * @author Cesar Orozco
 */
public class SProformaInfo implements SDocument {
    
    public static final String NON_FOLIO = "SIN FOLIO";
    
    protected String msNumberSeries;
    protected String msNumber;
    protected String msUuid;
    protected Date mtDate;
    protected String msIssuer;
    
    public SProformaInfo(final String numberSeries, final String number, final String uuid, final Date date, final String issuer) {
        msNumberSeries = numberSeries;
        msNumber = number;
        msUuid = uuid;
        mtDate = date;
        msIssuer = issuer;
    }

    public SProformaInfo(final SImportedProforma document) {
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
        return SDocumentUtils.composeFolio(msNumberSeries, msNumber, msUuid);
    }
    
    @Override
    public String getIssuer() {
        return msIssuer;
    }
}
