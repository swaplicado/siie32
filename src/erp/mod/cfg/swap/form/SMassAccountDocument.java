/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.ver40.DCfdi40Catalogs;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.account.Case;
import erp.mod.cfg.swap.account.Group;
import erp.mod.cfg.swap.account.Partner;
import erp.mod.cfg.swap.account.Unit;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.trn.db.SDbSwapDataProcessing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    
    private static final int COL_COMPUTE = 8;
    
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
        
        Comprobante = DCfdUtils.getCfdi40(SXmlUtils.readXml(ImportedDocument.AuxFiles[SImportUtils.CFDI_XML].getAbsolutePath()));
        
        if (isCfdiInvoice()) {
            IsEmisorPerson = Comprobante.getEltEmisor().getAttRfc().getString().length() == DCfdConsts.LEN_RFC_PER;
            EmisorFiscalId = Comprobante.getEltEmisor().getAttRfc().getString();
            EmisorDescripByName = ImportedDocument.BizPartner + " - " + ImportedDocument.BizPartnerId;
            
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
            }
            
            // determine invoice main configuration elements for accounting:
            
            ArrayList<String> descriptions = new ArrayList<>();

            for (cfd.ver40.DElementConcepto concepto : Comprobante.getEltConceptos().getEltConceptos()) {
                descriptions.add(concepto.getAttDescripcion().getString());
                
                if (!isFreight) {
                    Units += concepto.getAttCantidad().getDouble();
                }
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
                    + "No existe una configuración aplicable a unidades de medida de clave SAT '" + ComprobanteUnidadCode + "'.";
        }
        else if (InvoiceCase == null) {
            error = "No se pudo determinar el caso de contabilización del comprobante.\n"
                    + "No existe una configuración aplicable a productos o servicios de clave SAT '" + ComprobanteProdServCode + "'.";
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
                        + "No existe una configuración aplicable a productos o servicios de clave SAT '" + CartaPorteBienesTranspsCode + "'.";
            }
        }
        
        return error;
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
            case COL_COMPUTE:
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
            case COL_COMPUTE:
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
                value = Comprobante.getEltEmisor().getAttRegimenFiscal().getString();
                break;
            case 14:
                value = ImportedDocument.FiscalUseCode;
                break;
            case 15:
                value = ImportedDocument.FunctionalSubArea;
                break;
            case 16:
                value = ImportedDocument.getRevisionYearWeek();
                break;
            case 17:
                value = ImportedDocument.RevisionDatetime;
                break;
            case 18:
                value = ImportedDocument.RequirePayment;
                break;
            case 19:
                value = ImportedDocument.getRequiredPaymentPct();
                break;
            case 20:
                value = ImportedDocument.getRequiredPaymentAmount(null);
                break;
            case 21:
                value = ImportedDocument.RequiredPaymentAmountNew == 0 ? null : ImportedDocument.RequiredPaymentAmountNew;
                break;
            case 22:
                value = ImportedDocument.CurrencyCode;
                break;
            case 23:
                value = ImportedDocument.RequiredPaymentDate;
                break;
            case 24:
                value = ImportedDocument.RequiredPaymentDateNew;
                break;
            case 25:
                value = ImportedDocument.IsRequiredPaymentLoc;
                break;
            case 26:
                value = ImportedDocument.RequiredPaymentNotes;
                break;
            case 27:
                value = SSwapConsts.PayDefinitions.get(ImportedDocument.RequiredPaymentDefinition);
                break;
            case 28:
                value = ImportedDocument.DueDate;
                break;
            case 29:
                value = ImportedDocument.AccountingTag;
                break;
            case 30:
                value = ImportedDocument.ExternalDocumentUuid;
                break;
            case 31:
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
