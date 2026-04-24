/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.ver40.DCfdi40Catalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mfin.data.SDataTax;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mloc.data.SDataBolCounty;
import erp.mloc.data.SDataBolLocality;
import erp.mloc.data.SDataBolZipCode;
import erp.mloc.data.SDataCountry;
import erp.mloc.data.SDataState;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.model.account.Case;
import erp.mod.cfg.swap.model.account.Group;
import erp.mod.cfg.swap.model.account.Partner;
import erp.mod.cfg.swap.model.account.SAccountSettings;
import erp.mod.cfg.swap.model.account.Tax;
import erp.mod.cfg.swap.model.account.Unit;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.trn.db.SDbSwapDataProcessing;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryTax;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridRow;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Sergio Flores
 */
public class SMassAccountDocument implements SGridRow, Comparable<SMassAccountDocument> {
    
    public static final int WARN_CFDI_MULT_PROD_SERV = 11;
    public static final int WARN_CFDI_MULT_UNIT = 12;
    public static final int WARN_CCP_MULT_MERC_TRAN = 21;
    
    public static final HashMap<Integer, String> WarningsMap = new HashMap<>();
    
    static {
        WarningsMap.put(WARN_CFDI_MULT_PROD_SERV, "El CFDI tiene distintos productos o servicios.");
        WarningsMap.put(WARN_CFDI_MULT_UNIT, "El CFDI tiene distintas unidades de medida.");
        WarningsMap.put(WARN_CCP_MULT_MERC_TRAN, "El Complemento Carta Porte tiene distintos bienes transportados.");
    }
    
    private static final int COL_RECORD = 8;
    
    public SImportedDocument ImportedDocument;
    public SDialogMassAccountDocuments DialogMassAccountDocuments;
    
    public boolean Record;
    public int IconRecordable;
    public int IconRecorded;
    
    // CFDI data:
    
    public int ParsingWarningType;
    public boolean ParsingError;
    
    public cfd.ver40.DElementComprobante Comprobante;
    public cfd.ver4.ccp31.DElementCartaPorte CartaPorte;
    public ArrayList<SCfdiConcepto> Conceptos;
    public boolean IsEmisorPerson;
    public String EmisorFiscalId;
    public String EmisorDescripByName; // ID + " - " + name
    public String ComprobanteUnidadCode;
    public String ComprobanteProdServCode;
    public String ComprobanteProdServDescripByCode; // code + " - " + name
    public String CartaPorteBienesTranspsCode;
    public String CartaPorteBienesTranspsDescripByCode; // code + " - " + name
    
    public String ScaleTicketBol;
    public String ScaleTicketRef;
    public double Units;
    
    // invoice main configuration elements for accounting:
    
    public Group InvoiceGroup;
    public Partner InvoicePartner;
    public Unit InvoiceUnit;
    public Case InvoiceCase;
    
    // BOL complementary configuration elements for accounting of transported goods:
    
    public Group GoodsGroup;
    public Partner GoodsPartner;
    public Case GoodsCase;
    
    // accounting settings:
    
    public AccountSettings AccountSettingsSystem;
    public AccountSettings AccountSettingsUser;
    
    public SMassAccountDocument(final SImportedDocument importedDocument, final SDialogMassAccountDocuments dialogMassAccountDocuments) throws Exception {
        ImportedDocument = importedDocument;
        DialogMassAccountDocuments = dialogMassAccountDocuments;
        
        Record = false;
        IconRecordable = SGridConsts.ICON_NULL;
        IconRecorded = SGridConsts.ICON_WAIT;
        
        parseComprobante();
    }
    
    private void parseComprobante() throws Exception {
        ParsingWarningType = 0;
        ParsingError = true; // by default, assume that there is a parsing error
        Comprobante = null;
        CartaPorte = null;
        Conceptos = null;
        IsEmisorPerson = true; // by default, assume that partner is a person
        EmisorFiscalId = "";
        EmisorDescripByName = "";
        ComprobanteUnidadCode = "";
        ComprobanteProdServCode = "";
        ComprobanteProdServDescripByCode = "";
        CartaPorteBienesTranspsCode = "";
        CartaPorteBienesTranspsDescripByCode = "";
        
        ScaleTicketBol = "";
        ScaleTicketRef = "";
        Units = 0;
        
        InvoiceGroup = null;
        InvoicePartner = null;
        InvoiceUnit = null;
        InvoiceCase = null;
        
        GoodsGroup = null;
        GoodsPartner = null;
        GoodsCase = null;

        AccountSettingsSystem = null;
        AccountSettingsUser = null;
        
        // parse CFDI data from its XML:
        
        Comprobante = DCfdUtils.getCfdi40(SXmlUtils.readXml(ImportedDocument.AuxFiles[SImportUtils.CFDI_XML_IDX].getAbsolutePath()));
        
        if (isCfdiInvoice()) {
            // issuer settings:
            IsEmisorPerson = Comprobante.getEltEmisor().getAttRfc().getString().length() == DCfdConsts.LEN_RFC_PER;
            EmisorFiscalId = Comprobante.getEltEmisor().getAttRfc().getString();
            EmisorDescripByName = ImportedDocument.BizPartner + " - " + ImportedDocument.BizPartnerId;
            
            // payment type setting:
            ImportedDocument.AuxPaymentType = Comprobante.getAttMetodoPago().getString().equals(DCfdi40Catalogs.MDP_PPD) ? SDataConstantsSys.TRNS_TP_PAY_CREDIT : SDataConstantsSys.TRNS_TP_PAY_CASH;
            
            // parse invoice entries:
            
            Conceptos = new ArrayList<>();
            
            HashSet<String> conceptoProdServClavesSet = new HashSet<>();
            HashSet<String> conceptoUnidadClavesSet = new HashSet<>();
            HashSet<String> cartaPorteBienesTranspsSet = null;
            
            for (cfd.ver40.DElementConcepto concepto : Comprobante.getEltConceptos().getEltConceptos()) {
                Conceptos.add(new SCfdiConcepto(concepto));
                conceptoProdServClavesSet.add(concepto.getAttClaveProdServ().getString());
                conceptoUnidadClavesSet.add(concepto.getAttClaveUnidad().getString());
            }
            
            if (conceptoProdServClavesSet.size() != 1) {
                ParsingWarningType = WARN_CFDI_MULT_PROD_SERV; // error: multiple ClaveProdServ
            }
            
            if (conceptoUnidadClavesSet.size() != 1) {
                ParsingWarningType = WARN_CFDI_MULT_UNIT; // error: multiple ClaveUnidad
            }
            
            boolean isFreight = isFreight();
            
            if (isFreight) {
                CartaPorte = (cfd.ver4.ccp31.DElementCartaPorte) Comprobante.getEltComplemento(cfd.ver4.ccp31.DElementCartaPorte.NAME);
            }
            
            if (isFreight && isCfdiInvoiceAndBol()) {
                // parse BOL entries:

                cartaPorteBienesTranspsSet = new HashSet<>();

                for (cfd.ver4.ccp31.DElementMercancia mercancía : CartaPorte.getEltMercancias().getEltMercancias()) {
                    cartaPorteBienesTranspsSet.add(mercancía.getAttBienesTransp().getString());
                }

                if (cartaPorteBienesTranspsSet.size() != 1) {
                    ParsingWarningType = WARN_CCP_MULT_MERC_TRAN; // error: multiple BienesTransp
                }
                
                ScaleTicketBol = SMassAccountUtils.extractScaleTicket(Comprobante.getEltConceptos().getEltConceptos().get(0).getAttDescripcion().getString(), DialogMassAccountDocuments.getPatternScaleTicketBol(), false);
                ScaleTicketRef = SMassAccountUtils.extractScaleTicket(ImportedDocument.ReferencesAsText, DialogMassAccountDocuments.getPatternScaleTicketRef(), false);
            }
            
            // determine invoice main configuration elements for accounting:
            
            ArrayList<String> descriptions = new ArrayList<>();

            for (cfd.ver40.DElementConcepto concepto : Comprobante.getEltConceptos().getEltConceptos()) {
                descriptions.add(concepto.getAttDescripcion().getString());
                
                Units += concepto.getAttCantidad().getDouble();
            }
            
            InvoiceGroup = DialogMassAccountDocuments.getConfig().getGroup(isFreight ? Group.DOC_TYPE_BOL : Group.DOC_TYPE_INVOICE);
            
            if (InvoiceGroup != null) {
                InvoicePartner = InvoiceGroup.getPartner(IsEmisorPerson);
                
                if (InvoicePartner != null) {
                    if (!conceptoProdServClavesSet.isEmpty()) {
                        ComprobanteUnidadCode = conceptoUnidadClavesSet.toArray()[0].toString();
                        InvoiceUnit = InvoicePartner.getUnit(ComprobanteUnidadCode);
                        
                        if (InvoiceUnit != null) {
                            if (!conceptoUnidadClavesSet.isEmpty()) {
                                ComprobanteProdServCode = conceptoProdServClavesSet.toArray()[0].toString();
                                InvoiceCase = InvoicePartner.getCase(ComprobanteProdServCode, descriptions);
                                
                                if (InvoiceCase != null) {
                                    ComprobanteProdServDescripByCode = ComprobanteProdServCode + " - " + InvoiceCase.getProdServ(ComprobanteProdServCode).getKeyDesc();
                                }
                            }
                        }
                    }
                }

                if (isFreight && isCfdiInvoiceAndBol()) {
                    GoodsGroup = DialogMassAccountDocuments.getConfig().getGroup(Group.DOC_TYPE_INVOICE);

                    if (GoodsGroup != null) {
                        GoodsPartner = GoodsGroup.getPartner(IsEmisorPerson);

                        if (GoodsPartner != null) {
                            if (!cartaPorteBienesTranspsSet.isEmpty()) {
                                CartaPorteBienesTranspsCode = cartaPorteBienesTranspsSet.toArray()[0].toString();
                                GoodsCase = GoodsPartner.getCase(CartaPorteBienesTranspsCode, descriptions);
                                
                                if (GoodsCase != null) {
                                    CartaPorteBienesTranspsDescripByCode = CartaPorteBienesTranspsCode + " - " + GoodsCase.getProdServ(CartaPorteBienesTranspsCode).getKeyDesc();
                                }
                            }
                        }
                    }
                }
            }
            
            if (isFreight && InvoiceCase != null && GoodsCase != null) {
                AccountSettingsSystem = new AccountSettings(
                        GoodsCase.getItem(), GoodsCase.getItemDesc(),
                        InvoiceCase.getItem(), InvoiceCase.getItemDesc(),
                        InvoiceCase.getUnit(), InvoiceCase.getUnitDesc(),
                        GoodsCase.getAccount(), GoodsCase.getCostCenter());
            }
            else if (!isFreight && InvoiceCase != null) {
                AccountSettingsSystem = new AccountSettings(
                        InvoiceCase.getItem(), InvoiceCase.getItemDesc(),
                        0, "",
                        InvoiceCase.getUnit(), InvoiceCase.getUnitDesc(),
                        InvoiceCase.getAccount(), InvoiceCase.getCostCenter());
            }
            
            if (AccountSettingsSystem != null) {
                AccountSettingsUser = AccountSettingsSystem.clone();
                IconRecordable = ParsingWarningType != 0 ? SGridConsts.ICON_WARN : SGridConsts.ICON_THUMBS_UP;
                ParsingError = false;
            }
            else {
                IconRecordable = SGridConsts.ICON_ANNUL;
            }
        }
    }
    
    public boolean isRecordable() {
        return !ParsingError;
    }
    
    /**
     * Check if processing type of imported document is freight of raw materials.
     * @return 
     */
    public boolean isFreight() {
        return ImportedDocument.ProcessingTypeId == SDbSwapDataProcessing.PROC_TYPE_RAW_MAT_FREIGHT;
    }
    
    /**
     * Check if document is an invoice. Consider that BOL's are invoices too.
     * @return 
     */
    public boolean isCfdiInvoice() {
        return Comprobante != null && Comprobante.getAttTipoDeComprobante().getString().equals(DCfdi40Catalogs.CFD_TP_I);
    }
    
    /**
     * Check if document is an invoice and as well bill of lading (BOL).
     * @return 
     */
    public boolean isCfdiInvoiceAndBol() {
        return isCfdiInvoice() && CartaPorte != null;
    }
    
    public String getAccountCase() {
        String accountCase = "";
        
        if (GoodsCase != null) {
            accountCase += GoodsCase.getCaseCode() + " + ";
        }
        
        if (InvoiceCase != null) {
            accountCase += InvoiceCase.getCaseName();
        }
        else {
            accountCase += "?";
        }
        
        return accountCase;
    }
    
    public String getParsingWarning() {
        String warning = ParsingWarningType == 0 ? "" : WarningsMap.get(ParsingWarningType);
        return warning != null ? warning : "?";
    }
    
    public String getParsingError() {
        String error = "";
        
        if (Comprobante == null) {
            error = "El documento no tiene CFDI.";
        }
        else if (!isCfdiInvoice()) {
            error = "El CFDI no es de tipo Ingreso.";
        }
        else if (isFreight() && CartaPorte == null) {
            error = "El CFDI no tiene Complemento Carta Porte.";
        }
        else if (InvoiceGroup == null) {
            error = "No se pudo determinar el grupo de contabilización del comprobante.\n"
                    + "No existe una configuración aplicable a documentos tipo " + (isCfdiInvoiceAndBol() ? "Carta Porte" : "Ingreso") + ".";
        }
        else if (InvoicePartner == null) {
            error = "No se pudo determinar el asociado de negocios de contabilización del comprobante.\n"
                    + "No existe una configuración aplicable a asociados de negocio tipo " + (IsEmisorPerson ? "Persona Física" : "Persona Moral") + ".";
        }
        else if (InvoiceUnit == null) {
            error = "No se pudo determinar la unidad de medida de contabilización del comprobante.\n"
                    + "No existe una configuración aplicable a unidades de medida de clave SAT (ClaveUnidad) '" + ComprobanteUnidadCode + "'.";
        }
        else if (InvoiceCase == null) {
            error = "No se pudo determinar el caso de contabilización del comprobante.\n"
                    + "No existe una configuración aplicable a productos o servicios de clave SAT (ClaveProdServ) '" + ComprobanteProdServCode + "'.";
        }
        else if (isFreight()) {
            if (GoodsGroup == null) {
                error = "No se pudo determinar el grupo de contabilización de los bienes transportados.\n"
                        + "No existe una configuración aplicable a documentos tipo Ingreso.";
            }
            else if (GoodsPartner == null) {
                error = "No se pudo determinar el asociado de negocios de contabilización de los bienes transportados.\n"
                        + "No existe una configuración aplicable a asociados de negocio tipo " + (IsEmisorPerson ? "Persona Física" : "Persona Moral") + ".";
            }
            else if (GoodsCase == null) {
                error = "No se pudo determinar el caso de contabilización de los bienes transportados.\n"
                        + "No existe una configuración aplicable a productos o servicios de clave SAT (ClaveProdServ) '" + CartaPorteBienesTranspsCode + "'.";
            }
        }
        
        return error;
    }
    
    private SAccountSettings createInvoiceSettings() {
        return new SAccountSettings(InvoiceGroup, InvoicePartner, InvoiceUnit, InvoiceCase);
    }

    private SAccountSettings createGoodsSettings() {
        SAccountSettings settings = null;
        
        if (isCfdiInvoiceAndBol()) {
            settings = new SAccountSettings(GoodsGroup, GoodsPartner, InvoiceUnit, GoodsCase);
        }
        
        return settings;
    }
    
    private int getScaleTicketInfoIcon() {
        int icon = SGridConsts.ICON_NULL;
        
        if (isCfdiInvoiceAndBol()) {
            if (ScaleTicketBol.isEmpty() || ScaleTicketRef.isEmpty()) {
                icon = SGridConsts.ICON_WARN;
            }
            else if (ScaleTicketBol.equals(ScaleTicketRef)) {
                icon = SGridConsts.ICON_OK;
            }
            else {
                icon = SGridConsts.ICON_CROSS;
            }
        }
        
        return icon;
    }
    
    /**
     * Compose scale ticket number for BOL.
     * @return 
     */
    private String composeScaleTicket() {
        String scaleTicket = ScaleTicketBol;
        
        if (scaleTicket.isEmpty()) {
            scaleTicket = ScaleTicketRef;
            
            if (scaleTicket.isEmpty()) {
                scaleTicket = "N/D";
            }
        }
        
        return scaleTicket;
    }
    
    /**
     * Compose concept for BOL.
     * Concept format: "B. " + scale-ticket-number + " DE " + item-name + " DE " + (({locality-name | county-name} + [", " + state-abbreviation]) | ("CP" + ZIP-code + [" " + state-name]) | {state-name | country-name})
     * @param client GUI client.
     * @param scaleTicket Scale ticket number for BOL.
     * @return 
     */
    private String composeBolConcept(final SClientInterface client, final SDataItem item, final String scaleTicket, final boolean isFromWarehouse) {
        String concept = "B. " + scaleTicket + " DE " + item.getItem();
        cfd.ver4.ccp31.DElementCartaPorte cartaPorte = (cfd.ver4.ccp31.DElementCartaPorte) Comprobante.getEltComplemento(cfd.ver4.ccp31.DElementCartaPorte.NAME);
        
        if (cartaPorte != null) {
            SDataBolLocality bolLocality = null;
            SDataBolCounty bolCounty = null;
            SDataBolZipCode bolZipCode = null;
            SDataState erpState = null;
            SDataCountry erpCountry = null;
            
            cfd.ver4.ccp31.DElementUbicacion origen = cartaPorte.getEltUbicaciones().getEltUbicaciones(DCfdi40Catalogs.CcpUbicaciónOrigen).get(0);
            String stateCode = origen.getEltDomicilio().getAttEstado().getString();
            
            if (!stateCode.isEmpty()) {
                String localityCode = origen.getEltDomicilio().getAttLocalidad().getString();
                String countyCode = origen.getEltDomicilio().getAttMunicipio().getString();
                String zipCode = origen.getEltDomicilio().getAttCodigoPostal().getString();
                
                if (!localityCode.isEmpty()) {
                    try {
                        bolLocality = SMassAccountUtils.getBolLocality(client, localityCode, stateCode);
                    }
                    catch (Exception e) { }
                }
                
                if (bolLocality == null && !countyCode.isEmpty()) {
                    try {
                        bolCounty = SMassAccountUtils.getBolCounty(client, countyCode, stateCode);
                    }
                    catch (Exception e) { }
                }
                
                if (bolCounty == null && !zipCode.isEmpty()) {
                    try {
                        bolZipCode = SMassAccountUtils.getBolZipCode(client, zipCode, stateCode);
                    }
                    catch (Exception e) { }
                    
                    if (bolZipCode != null) {
                        if (!bolZipCode.getLocalityCode().isEmpty()) {
                            try {
                                bolLocality = SMassAccountUtils.getBolLocality(client, bolZipCode.getLocalityCode(), stateCode);
                            }
                            catch (Exception e) { }
                        }
                        
                        if (bolLocality == null && !bolZipCode.getCountyCode().isEmpty()) {
                            try {
                                bolCounty = SMassAccountUtils.getBolCounty(client, bolZipCode.getCountyCode(), stateCode);
                            }
                            catch (Exception e) { }
                        }
                    }
                }
                
                try {
                    erpState = SMassAccountUtils.getErpState(client, stateCode);
                }
                catch (Exception e) { }
            }
            
            if (bolLocality == null && bolCounty == null && erpState == null) {
                try {
                    erpCountry = SMassAccountUtils.getErpCountry(client, origen.getEltDomicilio().getAttPais().getString());
                }
                catch (Exception e) { }
            }
            
            concept += " DE ";
            
            if (isFromWarehouse) {
                concept += "BODEGA DE ";
            }
            
            if (bolLocality != null) {
                concept += bolLocality.getDescription().toUpperCase();
                if (erpState != null) {
                    concept += ", " + erpState.getStateAbbr().toUpperCase();
                }
            }
            else if (bolCounty != null) {
                concept += bolCounty.getDescription().toUpperCase();
                if (erpState != null) {
                    concept += ", " + erpState.getStateAbbr().toUpperCase();
                }
            }
            else if (bolZipCode != null) {
                concept += "CP " + bolZipCode.getPkZipCode();
                if (erpState != null) {
                    concept += " " + erpState.getState().toUpperCase();
                }
            }
            else if (erpState != null) {
                concept += erpState.getState().toUpperCase();
            }
            else if (erpCountry != null) {
                concept += erpCountry.getCountry().toUpperCase();
            }
            else {
                concept += "ORIGEN DESCONOCIDO";
            }
        }
        
        return concept;
    }
    
    /**
     * Create DPS entries from a CFDI 4.0 and set account settings for imner imported document.
     * @param client GUI client.
     * @param taxRegionId Tax region ID.
     * @return 
     * @throws java.lang.Exception 
     */
    public ArrayList<SDataDpsEntry> createDpsEntriesAndSetAccountSettings(final SClientInterface client, final int taxRegionId) throws Exception {
        /*
         * 1. Create DPS entries from a CFDI 4.0.
         */
        
        ArrayList<SDataDpsEntry> dpsEntries = new ArrayList<>();
        
        SAccountSettings invoiceSettings = createInvoiceSettings();
        SAccountSettings goodsSettings = createGoodsSettings();

        SDataUnit unit = SMassAccountUtils.getErpUnit(client, invoiceSettings.getPartnerCase().getUnit());
        SDataItem item = SMassAccountUtils.getErpItem(client, invoiceSettings.getPartnerCase().getItem());
        SDataItem itemRef = null;

        String accountCode = "";
        String costCenterCode = "";
        
        String ticket = "";
        String concept = "";
        String conceptKey = item.getKey();

        double units = 0;
        
        if (isCfdiInvoiceAndBol()) {
            // invoice is BOL as well:

            Matcher matcher = DialogMassAccountDocuments.getPatternWarehouse().matcher(Comprobante.getEltConceptos().getEltConceptos().get(0).getAttDescripcion().getString());
            boolean isFromWarehouse = matcher.find();

            itemRef = SMassAccountUtils.getErpItem(client, goodsSettings.getPartnerCase().getItem());

            accountCode = goodsSettings.getPartnerCase().getAccount();
            costCenterCode = goodsSettings.getPartnerCase().getCostCenter();
            
            ticket = composeScaleTicket();
            concept = composeBolConcept(client, itemRef, ticket, isFromWarehouse);
        }
        else {
            // invoice is just invoice:

            itemRef = null;

            accountCode = invoiceSettings.getPartnerCase().getAccount();
            costCenterCode = invoiceSettings.getPartnerCase().getCostCenter();
            
            ticket = "";
            concept = item.getItem();
        }
        
        for (cfd.ver40.DElementConcepto concepto : Comprobante.getEltConceptos().getEltConceptos()) {
            // process concept taxes:
            
            ArrayList<SDataDpsEntryTax> entryTaxes = new ArrayList<>();
            
            if (concepto.getAttObjetoImp().getString().equals(DCfdi40Catalogs.ClaveObjetoImpSí) && concepto.getEltOpcConceptoImpuestos() != null) {
                // charged taxes:
                if (concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosTrasladados() != null) { // documents usually always have taxes
                    for (cfd.ver40.DElementConceptoImpuestoTraslado traslado : concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosTrasladados().getEltImpuestoTrasladados()) {
                        Tax tax = DialogMassAccountDocuments.getConfig().getTax(Tax.TYPE_ADDED, traslado.getAttImpuesto().getString(), traslado.getAttTipoFactor().getString(), traslado.getAttTasaOCuota().getFormattedDouble());

                        if (tax != null) {
                            SDataTax erpTax = SMassAccountUtils.getErpTax(client, tax.getTaxKey());

                            SDataDpsEntryTax entryTax = new SDataDpsEntryTax();

                            //entryTax.setPkYearId(...);
                            //entryTax.setPkDocId(...);
                            //entryTax.setPkEntryId(...);
                            entryTax.setPkTaxBasicId(erpTax.getPkTaxBasicId());
                            entryTax.setPkTaxId(erpTax.getPkTaxId());
                            entryTax.setPercentage(erpTax.getPercentage());
                            entryTax.setValueUnitary(0);
                            entryTax.setValue(0);
                            //entryTax.setTax(...);
                            entryTax.setTaxCy(SLibUtils.roundAmount(traslado.getAttImporte().getDouble())); // notice: sometimes amount has more than 2 decimals!
                            entryTax.setFkTaxTypeId(erpTax.getFkTaxTypeId());
                            entryTax.setFkTaxCalculationTypeId(erpTax.getFkTaxCalculationTypeId());
                            entryTax.setFkTaxApplicationTypeId(erpTax.getFkTaxApplicationTypeId());

                            entryTaxes.add(entryTax);
                        }
                        else {
                            throw new Exception("No se encontró el impuesto para: "
                                    + "tipo = '" + Tax.TYPE_ADDED + "'; "
                                    + traslado.getAttImpuesto().getName() + " = '" + traslado.getAttImpuesto().getString() + "'; "
                                    + traslado.getAttTipoFactor().getName() + " = '" + traslado.getAttTipoFactor().getString() + "'; "
                                    + traslado.getAttTasaOCuota().getName() + " = '" + traslado.getAttTasaOCuota().getFormattedDouble()+ "'.");
                        }
                    }
                }
                
                // withheld taxes:
                if (concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosRetenciones() != null) { // documents don't always have withheld taxes
                    for (cfd.ver40.DElementConceptoImpuestoRetencion retencion : concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones()) {
                        Tax tax = DialogMassAccountDocuments.getConfig().getTax(Tax.TYPE_WITHHELD, retencion.getAttImpuesto().getString(), retencion.getAttTipoFactor().getString(), retencion.getAttTasaOCuota().getFormattedDouble());

                        if (tax != null) {
                            SDataTax erpTax = SMassAccountUtils.getErpTax(client, tax.getTaxKey());

                            SDataDpsEntryTax entryTax = new SDataDpsEntryTax();

                            //entryTax.setPkYearId(...);
                            //entryTax.setPkDocId(...);
                            //entryTax.setPkEntryId(...);
                            entryTax.setPkTaxBasicId(erpTax.getPkTaxBasicId());
                            entryTax.setPkTaxId(erpTax.getPkTaxId());
                            entryTax.setPercentage(erpTax.getPercentage());
                            entryTax.setValueUnitary(0);
                            entryTax.setValue(0);
                            //entryTax.setTax(...);
                            entryTax.setTaxCy(SLibUtils.roundAmount(retencion.getAttImporte().getDouble())); // notice: sometimes amount has more than 2 decimals!
                            entryTax.setFkTaxTypeId(erpTax.getFkTaxTypeId());
                            entryTax.setFkTaxCalculationTypeId(erpTax.getFkTaxCalculationTypeId());
                            entryTax.setFkTaxApplicationTypeId(erpTax.getFkTaxApplicationTypeId());

                            entryTaxes.add(entryTax);
                        }
                        else {
                            throw new Exception("No se encontró el impuesto para: "
                                    + "tipo = '" + Tax.TYPE_WITHHELD + "'; "
                                    + retencion.getAttImpuesto().getName() + " = '" + retencion.getAttImpuesto().getString() + "'; "
                                    + retencion.getAttTipoFactor().getName() + " = '" + retencion.getAttTipoFactor().getString() + "'; "
                                    + retencion.getAttTasaOCuota().getName() + " = '" + retencion.getAttTasaOCuota().getFormattedDouble()+ "'.");
                        }
                    }
                }
            }
            
            // process concept:
            
            double quantity = 0;
            
            if (isCfdiInvoiceAndBol()) {
                // invoice is BOL as well:
                quantity = concepto.getAttCantidad().getDouble();
            }
            else {
                // invoice is just invoice:
                quantity = concepto.getAttCantidad().getDouble() * invoiceSettings.getPartnerUnit().getKg();
            }
            
            units += quantity; // summation of quantity
            
            SDataDpsEntry dpsEntry = new SDataDpsEntry();
            
            //dpsEntry.setPkYearId(...);
            //dpsEntry.setPkDocId(...);
            //dpsEntry.setPkEntryId(...);
            dpsEntry.setConceptKey(SLibUtils.textLeft(conceptKey, SDataDpsEntry.LEN_CONCEPT_KEY));
            dpsEntry.setConcept(SLibUtils.textLeft(concept, SDataDpsEntry.LEN_CONCEPT));
            dpsEntry.setReference(SLibUtils.textLeft(ticket, SDataDpsEntry.LEN_REFERENCE));
            dpsEntry.setQuantity(quantity);
            dpsEntry.setIsDiscountDocApplying(Comprobante.getAttDescuento().getDouble() > 0);
            //dpsEntry.setIsDiscountUnitaryPercentage(...);
            //dpsEntry.setIsDiscountUnitaryPercentageSystem(...);
            //dpsEntry.setIsDiscountEntryPercentage(...);
            //dpsEntry.setDiscountUnitaryPercentage(...);
            //dpsEntry.setDiscountUnitaryPercentageSystem(...);
            //dpsEntry.setDiscountEntryPercentage(...);
            //dpsEntry.setPriceUnitary(...);
            //dpsEntry.setPriceUnitarySystem(...);
            //dpsEntry.setDiscountUnitary(...);
            //dpsEntry.setDiscountUnitarySystem(...);
            //dpsEntry.setDiscountEntry(...);
            //dpsEntry.setSubtotalProvisional_r(...);
            //dpsEntry.setDiscountDoc(...);
            //dpsEntry.setSubtotal_r(...);
            //dpsEntry.setTaxCharged_r(...);
            //dpsEntry.setTaxRetained_r(...);
            //dpsEntry.setTotal_r(...);
            //dpsEntry.setPriceUnitaryReal_r(...);
            //dpsEntry.setCommissions_r(...);
            dpsEntry.setPriceUnitaryCy(concepto.getAttValorUnitario().getDouble());
            dpsEntry.setPriceUnitarySystemCy(concepto.getAttValorUnitario().getDouble());
            //dpsEntry.setDiscountUnitaryCy(...);
            //dpsEntry.setDiscountUnitarySystemCy(...);
            //dpsEntry.setDiscountEntryCy(...);
            dpsEntry.setSubtotalProvisionalCy_r(concepto.getAttImporte().getDouble());
            dpsEntry.setDiscountDocCy(concepto.getAttDescuento().getDouble());
            //dpsEntry.setSubtotalCy_r(...);
            //dpsEntry.setTaxChargedCy_r(...);
            //dpsEntry.setTaxRetainedCy_r(...);
            //dpsEntry.setTotalCy_r(...);
            //dpsEntry.setPriceUnitaryRealCy_r(...);
            //dpsEntry.setCommissionsCy_r(...);
            dpsEntry.setOriginalQuantity(quantity);
            dpsEntry.setOriginalPriceUnitaryCy(concepto.getAttValorUnitario().getDouble());
            dpsEntry.setOriginalPriceUnitarySystemCy(concepto.getAttValorUnitario().getDouble());
            //dpsEntry.setOriginalDiscountUnitaryCy(...);
            //dpsEntry.setOriginalDiscountUnitarySystemCy(...);
            //dpsEntry.setSalesPriceUnitaryCy(...);
            //dpsEntry.setSalesFreightUnitaryCy(...);
            //dpsEntry.setLength(...);
            //dpsEntry.setSurface(...);
            //dpsEntry.setVolume(...);
            dpsEntry.setMass(isCfdiInvoiceAndBol() ? 0 : quantity);
            //dpsEntry.setWeightPackagingExtra(...);
            dpsEntry.setWeightGross(isCfdiInvoiceAndBol() ? 0 : quantity);
            //dpsEntry.setWeightDelivery(...);
            //dpsEntry.setAcidityPercentage_n(...);
            //dpsEntry.setSurplusPercentage(...);
            //dpsEntry.setContractBase(...);
            //dpsEntry.setContractFuture(...);
            //dpsEntry.setContractFactor(...);
            //dpsEntry.setContractPriceYear(...);
            //dpsEntry.setContractPriceMonth(...);
            //dpsEntry.setSealQuality(...);
            //dpsEntry.setSealSecurity(...);
            //dpsEntry.setDriver(...);
            //dpsEntry.setPlate(...);
            dpsEntry.setTicket(ticket);
            //dpsEntry.setContainerTank(...);
            //dpsEntry.setTankCar(...);
            //dpsEntry.setVgm(...);
            dpsEntry.setOperationsType(SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS);
            //dpsEntry.setUserId(...);
            //dpsEntry.setSortingPosition(...);
            dpsEntry.setIsPrepayment(false);
            //dpsEntry.setIsDiscountRetailChain(...);
            dpsEntry.setIsTaxesAutomaticApplying(false); // prevent from recomputing taxes
            //dpsEntry.setIsPriceVariable(...);
            //dpsEntry.setIsPriceConfirm(...);
            //dpsEntry.setIsSalesFreightRequired(...);
            //dpsEntry.setIsSalesFreightConfirm(...);
            //dpsEntry.setIsSalesFreightAdd(...);
            dpsEntry.setIsInventoriable(item.getIsInventoriable());
            //dpsEntry.setIsDeleted(...);
            dpsEntry.setFkItemId(item.getPkItemId());
            dpsEntry.setFkUnitId(unit.getPkUnitId());
            dpsEntry.setFkOriginalUnitId(unit.getPkUnitId());
            dpsEntry.setFkTaxRegionId(taxRegionId);
            //dpsEntry.setFkThirdTaxpayerId_n(...);
            dpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
            dpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
            dpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
            //dpsEntry.setFkVehicleTypeId_n(...);
            //dpsEntry.setFkCashCompanyBranchId_n(...);
            //dpsEntry.setFkCashAccountId_n(...);
            dpsEntry.setFkCostCenterId_n(costCenterCode);
            dpsEntry.setFkItemRefId_n(itemRef != null ? itemRef.getPkItemId() : 0);
            dpsEntry.setFkUserNewId(client.getSession().getUser().getPkUserId());
            dpsEntry.setFkUserEditId(SUtilConsts.USR_NA_ID);
            dpsEntry.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
            //dpsEntry.setUserNewTs(...);
            //dpsEntry.setUserEditTs(...);
            //dpsEntry.setUserDeleteTs(...);
            
            dpsEntry.getDbmsEntryTaxes().addAll(entryTaxes);
            
            dpsEntries.add(dpsEntry);
        }
        
        /*
         * 2. Set account settings for imner imported document.
         */
        
        int accountId = SMassAccountUtils.getAccountId(client, accountCode);
        int costCenterId = SMassAccountUtils.getCostCenterId(client, costCenterCode);
        
        ImportedDocument.setMassAccountSettings(SDbSwapDataProcessing.ACC_METHOD_ASSISTED, accountId, costCenterId, itemRef != null ? itemRef.getPkItemId() : item.getPkItemId(), itemRef != null ? item.getPkItemId() : 0, units, unit.getPkUnitId());
        
        // processing is finished!
        
        return dpsEntries;
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case COL_RECORD:
                boolean record = (boolean) value;
                
                if (record) {
                    if (isRecordable() && Record != record) {
                        Record = record; // set to true!
                        DialogMassAccountDocuments.recountDocsToProcess();
                    }
                }
                else {
                    if (Record != record) {
                        Record = record; // set to falwse!
                        DialogMassAccountDocuments.recountDocsToProcess();
                    }
                }
                break;
                
            default:
                // nothing
        }
    }
    
    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = ImportedDocument.BizPartner;
                break;
            case 1:
                value = ImportedDocument.getFolio();
                break;
            case 2:
                value = ImportedDocument.Date;
                break;
            case 3:
                value = ImportedDocument.ReferencesAsText;
                break;
            case 4:
                value = ImportedDocument.Description;
                break;
            case 5:
                value = ImportedDocument.Total;
                break;
            case 6:
                value = ImportedDocument.CurrencyCode;
                break;
            case 7:
                value = ImportedDocument.Priority == SDbPayment.PRIORITY_URGENT ? SGridConsts.ICON_EXCL : SGridConsts.ICON_NULL;
                break;
            case COL_RECORD:
                value = Record;
                break;
            case 9:
                value = IconRecordable;
                break;
            case 10:
                value = IconRecorded;
                break;
            case 11:
                value = InvoiceCase == null ? "?" : InvoiceCase.getCaseCode();
                break;
            case 12:
                value = GoodsCase == null ? "" : GoodsCase.getCaseCode();
                break;
            case 13:
                value = ScaleTicketBol;
                break;
            case 14:
                value = getScaleTicketInfoIcon();
                break;
            case 15:
                value = Comprobante.getAttMetodoPago().getString();
                break;
            case 16:
                value = Comprobante.getEltEmisor().getAttRegimenFiscal().getString();
                break;
            case 17:
                value = ImportedDocument.FiscalUseCode;
                break;
            case 18:
                value = ImportedDocument.FunctionalSubArea;
                break;
            case 19:
                value = ImportedDocument.getRevisionYearWeek();
                break;
            case 20:
                value = ImportedDocument.RevisionDatetime;
                break;
            case 21:
                value = ImportedDocument.RequirePayment;
                break;
            case 22:
                value = ImportedDocument.getRequiredPaymentPct();
                break;
            case 23:
                value = ImportedDocument.getRequiredPaymentAmount(null);
                break;
            case 24:
                value = ImportedDocument.RequiredPaymentAmountNew == 0 ? null : ImportedDocument.RequiredPaymentAmountNew;
                break;
            case 25:
                value = ImportedDocument.CurrencyCode;
                break;
            case 26:
                value = ImportedDocument.RequiredPaymentDate;
                break;
            case 27:
                value = ImportedDocument.RequiredPaymentDateNew;
                break;
            case 28:
                value = ImportedDocument.IsRequiredPaymentLoc;
                break;
            case 29:
                value = ImportedDocument.RequiredPaymentNotes;
                break;
            case 30:
                value = SSwapConsts.PayDefinitions.get(ImportedDocument.RequiredPaymentDefinition);
                break;
            case 31:
                value = ImportedDocument.DueDate;
                break;
            case 32:
                value = ImportedDocument.AccountingTag;
                break;
            case 33:
                value = ImportedDocument.ExternalDocumentUuid;
                break;
            case 34:
                value = ImportedDocument.ExternalDocumentId;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public int compareTo(SMassAccountDocument o) {
        return this.toString().compareTo(o.toString());
    }
    
    @Override
    public String toString() {
        return "Emisor: " + ImportedDocument.BizPartner + "; " // allways available
                + "Folio: " + ImportedDocument.getFolio() + "; " // allways available
                + "Fecha: " + SLibUtils.DateFormatDate.format(ImportedDocument.Date) + "; " // allways available
                + "Total: $" + SLibUtils.getDecimalFormatAmount().format(ImportedDocument.Total) + " " + ImportedDocument.CurrencyCode // allways available
                + (!ImportedDocument.FunctionalSubArea.isEmpty() ? "; Subárea funcional: " + ImportedDocument.FunctionalSubArea : "") // may not be available
                + (ImportedDocument.ExternalDocumentId != 0 ? "; ID documento: " + ImportedDocument.ExternalDocumentId : "") // may not be available
                + ".";
    }
    
    public static class AccountSettings {
        
        public int ItemId;
        public String ItemDescrip;
        public int ItemAuxId;
        public String ItemAuxDescrip;
        public int UnitId;
        public String UnitDescrip;
        public String AccountCode;
        public String CostCenterCode;
        
        public AccountSettings(
                final int itemId, final String itemDescrip, 
                final int itemAuxId, final String itemAuxDescrip, 
                final int unitId, final String unitDescrip, 
                final String accountCode, final String costCenterCode) {
            ItemId = itemId;
            ItemDescrip = itemDescrip;
            ItemAuxId = itemAuxId;
            ItemAuxDescrip = itemAuxDescrip;
            UnitId = unitId;
            UnitDescrip = unitDescrip;
            AccountCode = accountCode;
            CostCenterCode = costCenterCode;
        }
        
        @Override
        public AccountSettings clone() throws CloneNotSupportedException {
            return new AccountSettings(ItemId, ItemDescrip, ItemAuxId, ItemAuxDescrip, UnitId, UnitDescrip, AccountCode, CostCenterCode);
        }
    }
}
