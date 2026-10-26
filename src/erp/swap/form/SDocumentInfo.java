/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import erp.swap.utils.SImportUtils;
import java.util.Date;

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
    protected String msPdfFileName;
    
    public SDocumentInfo(final String numberSeries, final String number, final String uuid, final Date date, final String issuer, final String pdfFileName) {
        msNumberSeries = numberSeries;
        msNumber = number;
        msUuid = uuid;
        mtDate = date;
        msIssuer = issuer;
        msPdfFileName = pdfFileName;
    }

    public SDocumentInfo(final SImportedDocument document, final String fileName) {
        this(document.NumberSeries, document.Number, document.ExternalDocumentUuid, document.Date, document.BizPartner, !fileName.isEmpty() ? fileName : document.getAuxFileName(SImportUtils.CFDI_PDF_IDX));
    }

    public SDocumentInfo(final SImportedDocument document) {
        this(document.NumberSeries, document.Number, document.ExternalDocumentUuid, document.Date, document.BizPartner, document.getAuxFileName(SImportUtils.CFDI_PDF_IDX));
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
    
    public String getPdfFileName() {
        return msPdfFileName;
    }

    @Override
    public String getFolio() {
        return SDocumentUtils.composeFolio(msNumberSeries, msNumber, msUuid);
    }
    
    @Override
    public String getIssuer() {
        return msIssuer;
    }

    @Override
    public String getFileName() {
        return msPdfFileName;
    }
}
