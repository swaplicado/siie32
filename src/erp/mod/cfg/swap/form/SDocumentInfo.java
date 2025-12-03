/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public class SDocumentInfo implements SDocument {
    
    protected String msFolio;
    protected String msUuid;
    protected Date mtDate;
    protected String msIssuer;
    
    public SDocumentInfo(final String folio, final String uuid, final Date date, final String issuer) {
        msFolio = folio;
        msUuid = uuid;
        mtDate = date;
        msIssuer = issuer;
    }

    public SDocumentInfo(final SImportedDocument document) {
        this(document.getFolio(), document.ExternalDocumentUuid, document.Date, document.BizPartner);
    }

    public String getFolio() {
        return msFolio;
    }
    
    public String getUuid() {
        return msUuid;
    }
    
    public Date gettDate() {
        return mtDate;
    }

    @Override
    public String getIssuer() {
        return msIssuer;
    }
    
    @Override
    public String getEffectiveFolio() {
        return !msFolio.isEmpty() ? msFolio : msUuid;
    }
}
