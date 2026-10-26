/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import cfd.ver4.DCfdVer4Consts;
import cfd.ver40.DCfdi40Catalogs;
import erp.SClientUtils;
import erp.SFileUtilities;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SFinUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.trn.db.SDbSwapDataProcessing;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataPdf;
import erp.mtrn.data.SThinDps;
import erp.swap.SSwapConsts;
import erp.swap.SSwapUtils;
import erp.swap.utils.SImportUtils;
import erp.swap.utils.SServicesUtils;
import java.io.File;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;

/**
 * In-memory document (purchase invoice or credit note) received from SWAP Services.
 * @author Sergio Flores
 */
public class SImportedDocument implements SGridRow, Serializable, Comparable<SImportedDocument> {
    
    public static final int DOC_TYPE_ALL = 0;
    public static final int DOC_TYPE_ASSETS = 1;
    public static final int DOC_TYPE_EXPENSES = 2;
    
    public static final int DOC_CASE_ALL = 0;
    public static final int DOC_CASE_STANDARD = 1;
    public static final int DOC_CASE_RAW_MAT_FREIGHT = 2;
    public static final int DOC_CASE_RAW_MAT_PURCHASE = 3;
    
    /** Coincidencia de tipo de pago: obligatoria. */
    public static final int MATCH_PAY_TP_MAND = 1;
    /** Coincidencia de tipo de pago: confirmar cuando es diferente. */
    public static final int MATCH_PAY_TP_CONF_DIFF = 2;
    
    /** Document types. */
    public static final HashMap<Integer, String> DocTypes = new HashMap<>();
    /** Document cases. */
    public static final HashMap<Integer, String> DocCases = new HashMap<>();
    
    private static final int COL_DOWNLOAD = 8;
    
    static {
        DocTypes.put(DOC_TYPE_ALL, "Todas");
        DocTypes.put(DOC_TYPE_ASSETS, "Activo fijo");
        DocTypes.put(DOC_TYPE_EXPENSES, "Compras y gastos");
        
        DocCases.put(DOC_CASE_ALL, "Todas");
        DocCases.put(DOC_CASE_STANDARD, "Estándar");
        DocCases.put(DOC_CASE_RAW_MAT_FREIGHT, "Fletes MP");
        DocCases.put(DOC_CASE_RAW_MAT_PURCHASE, "Compras MP");
    }
    
    // Exception messages:
    
    public static final String EXC_DOC_NOT_RECORDED = "Este documento no ha sido procesado, no tiene registro en " + SSwapConsts.SIIE + ".";
    public static final String EXC_DOC_ALREADY_RECORDED_IN_ = "Este documento ya fue procesado, tiene registro en " + SSwapConsts.SIIE + " en la póliza contable: ";

    public static final String EXC_PAY_NOT_REQUIRED = "Este documento no requiere pago.";
    public static final String EXC_PAY_NOT_REQUESTABLE = "Este documento no tiene información para solicitar su pago.";
    public static final String EXC_PAY_NOT_REQUESTED_YET = "Este documento no tiene aún solicitud de pago.";
    public static final String EXC_PAY_ALREADY_REQUESTED_IN_ = "Este documento ya tiene solicitud de pago: ";
    
    public static final String EXC_ADV_NO_ADVANCES = "Este documento no tiene anticipos.";
    
    private static final DecimalFormat RecPeriodFormat = new DecimalFormat("00");
    private static final DecimalFormat RecNumberFormat = new DecimalFormat(SLibUtils.textRepeat("0", SDataConstantsSys.NUM_LEN_FIN_REC));

    private final SServicesUtils.ConfigSettings ConfigSettings;
    
    final public int DocumentType;
    final public String DocumentName;
    public int ExternalDocumentId;
    public String ExternalDocumentUuid;
    public int BizPartnerId;
    public String BizPartner;
    public Boolean IsBizPartnerDomestic;
    public String NumberSeries;
    public String Number;
    /** Date of document. */
    public Date Date;
    /** Due date of document. Actually seldom set. */
    public Date DueDate;
    public int ReferencesType;
    public String ReferencesAsText;
    public String Description;
    public String AccountingTag;
    public int FunctionalSubAreaId;
    public String FunctionalSubArea;
    public String FiscalUseCode;
    public double Total;
    public int CurrencyId;
    public String CurrencyCode;
    public boolean RequirePayment;
    public int RequiredPaymentDefinition;
    public double RequiredPaymentPct;
    public double RequiredPaymentAmount;
    public double RequiredPaymentAmountNew;
    public Date RequiredPaymentDate;
    public Date RequiredPaymentDateNew;
    public boolean IsRequiredPaymentLoc;
    public String RequiredPaymentNotes;
    public int RevisionYear;
    public int RevisionWeek;
    public Date RevisionDatetime;
    public int Priority;
    /** Processing type ID defined in by constants SDbSwapDataProcessing.PRC_TYPE_... */
    public int ProcessingTypeId;
    public String ProcessingTypeCode;
    public int StatusId;
    public String Status;
    public boolean Download;
    public boolean AlreadyDownloaded;
    
    public String DocumentUploadedBy;
    public Date DocumentUploadedAt;
    public String DocumentReviewedBy;
    public Date DocumentReviewedAt;
    public String DocumentAuthorizedBy;
    public Date DocumentAuthorizedAt;
    
    public ProcessedDps ProcessedDps;
    public SDbSwapDataProcessing SwapDataProcessing;
    public SDbPayment Payment;
    
    public Reference[] References;
    
    /** Payment type of document: credit (default option) or cash. */
    public int AuxPaymentType;
    /** Files of document: XML & PDF. */
    public File[] AuxFiles;
    /** Available payment advances. */
    public SFinUtilities.Balance[] AuxAdvances;
    
    public int AccMethod;
    public int AccAccountId;
    public int AccCostCenterId;
    public int AccItemId;
    public int AccItemAuxId;
    public double AccUnits;
    public int AccUnitId;
    
    /**
     * Creates a new imported document.
     * @param configSettings SWAP-Services configuration settings, can be <code>null</code> when there is no need to process and record this document.
     * @param documentType GUI document type. Either SDataConstantsSys.TRNX_TP_DPS_DOC (invoice) or SDataConstantsSys.TRNX_TP_DPS_ADJ (credit note)
     */
    public SImportedDocument(final SServicesUtils.ConfigSettings configSettings, final int documentType) {
        ConfigSettings = configSettings;
        
        DocumentType = documentType;
        DocumentName = isInvoice() ? "factura" : "nota de crédito";
        ExternalDocumentId = 0;
        ExternalDocumentUuid = "";
        BizPartnerId = 0;
        BizPartner = "";
        IsBizPartnerDomestic = null;
        NumberSeries = "";
        Number = "";
        Date = null;
        DueDate = null;
        ReferencesType = 0;
        ReferencesAsText = "";
        Description = "";
        AccountingTag = "";
        FunctionalSubAreaId = 0;
        FunctionalSubArea = "";
        FiscalUseCode = "";
        Total = 0;
        CurrencyId = 0;
        CurrencyCode = "";
        RequirePayment = false;
        RequiredPaymentDefinition = SSwapConsts.PAY_NOT_REQ;
        RequiredPaymentPct = 0;
        RequiredPaymentAmount = 0;
        RequiredPaymentAmountNew = 0;
        RequiredPaymentDate = null;
        RequiredPaymentDateNew = null;
        IsRequiredPaymentLoc = false;
        RequiredPaymentNotes = "";
        RevisionYear = 0;
        RevisionWeek = 0;
        RevisionDatetime = null;
        Priority = 0;
        ProcessingTypeId = 0;
        ProcessingTypeCode = "";
        StatusId = 0;
        Status = "";
        Download = false;
        AlreadyDownloaded = false;
        
        DocumentUploadedBy = "";
        DocumentUploadedAt = null;
        DocumentReviewedBy = "";
        DocumentReviewedAt = null;
        DocumentAuthorizedBy = "";
        DocumentAuthorizedAt = null;
    
        ProcessedDps = null;
        SwapDataProcessing = null;
        Payment = null;

        References = null;
        
        AuxPaymentType = SDataConstantsSys.TRNS_TP_PAY_CREDIT;
        AuxFiles = null;
        AuxAdvances = null;
        
        setMassAccountSettings(SDbSwapDataProcessing.ACC_METHOD_MANUAL, 0, 0, 0, 0, 0, 0);
    }
    
    /**
     * Inform whether this document is an invoice.
     * @return 
     */
    private boolean isInvoice() {
        return DocumentType == SDataConstantsSys.TRNX_TP_DPS_DOC;
    }
    
    private int getSwapTxnDocType() {
        return SSwapUtils.getSwapTxnDocumentType(DocumentType);
    }
    
    private String getSwapPrcDataType() {
        return SSwapUtils.getSwapPrcDataType(DocumentType);
    }
    
    /**
     * Set mass account settings.
     * @param method
     * @param accountId
     * @param costCenterId
     * @param itemId
     * @param itemAuxId
     * @param units
     * @param unitId 
     */
    public final void setMassAccountSettings(final int method, final int accountId, final int costCenterId, final int itemId, final int itemAuxId, double units, final int unitId) {
        AccMethod = method;
        AccAccountId = accountId;
        AccCostCenterId = costCenterId;
        AccItemId = itemId;
        AccItemAuxId = itemAuxId;
        AccUnits = units;
        AccUnitId = unitId;
    }
    
    /**
     * Check whether business partner is domestic.
     * @param client GUI client.
     * @return
     * @throws Exception 
     */
    public boolean isBizPartnerDomestic(final SGuiClient client) throws Exception {
        if (IsBizPartnerDomestic == null) {
            IsBizPartnerDomestic = SDataBizPartner.checkIsDomestic(BizPartnerId, (SClientInterface) client);
        }
        return IsBizPartnerDomestic;
    }
    
    /**
     * Get folio of document in format series-number.
     * @return 
     */
    public String getFolio() {
        return SDocumentUtils.composeFolio(NumberSeries, Number, ExternalDocumentUuid);
    }
    
    /**
     * Get revision year and week in format yyyy-ww.
     * @return 
     */
    public String getRevisionYearWeek() {
        return SLibUtils.DecimalFormatCalendarYear.format(RevisionYear) + "-" + SLibUtils.DecimalFormatCalendarWeek.format(RevisionWeek);
    }
    
    /**
     * Get effective total, either from given DPS, if available, otherwise from this imported document.
     * @param dps Document, can be <code>null</code>.
     * @return 
     */
    private double getTotalEffective(final SThinDps dps) {
        return dps != null ? dps.getTotalCy_r() : Total;
    }
    
    /**
     * Ger required payment percentage of document as a double between 0 and 1.
     * @return 
     */
    public double getRequiredPaymentPct() {
        return RequiredPaymentPct / 100;
    }
    
    /**
     * Ger required payment amount of document, directly defined or indirectly from required payment percentage.
     * @param dps Document.
     * @return 
     */
    public double getRequiredPaymentAmount(final SThinDps dps) {
        double amount = 0;
        
        switch (RequiredPaymentDefinition) {
            case SSwapConsts.PAY_NOT_REQ:
                break;
            case SSwapConsts.PAY_DEF_BY_AMT:
            case SSwapConsts.PAY_DEF_BY_AMT_MAN:
                amount = RequiredPaymentAmount;
                break;
            case SSwapConsts.PAY_DEF_BY_PCT:
                amount = getTotalEffective(dps) * getRequiredPaymentPct();
                break;
            default:
                // nothing
        }
        
        return amount;
    }
    
    /**
     * Ger effective required payment amount of document, just already defined or originally defined, either directly or indirectly defined from required payment percentage.
     * @param dps Document.
     * @return 
     */
    public double getRequiredPaymentAmountEffective(final SThinDps dps) {
        return RequiredPaymentAmountNew != 0 ? RequiredPaymentAmountNew : getRequiredPaymentAmount(dps);
    }
    
    /**
     * Get effective required payment date, just already defined or originally defined.
     * @return 
     */
    public Date getRequiredPaymentDateEffective() {
        return RequiredPaymentDateNew != null ? RequiredPaymentDateNew : RequiredPaymentDate;
    }
    
    /**
     * Get effective required payment date.
     * @return 
     */
    public Date getDueDateEffective() {
        return DueDate != null ? DueDate : getRequiredPaymentDateEffective();
    }
    
    /**
     * Compose all references in a single semicolon separated string.
     * @return 
     */
    public String composeReferences() {
        String references = "";
        
        if (References != null) {
            for (Reference reference : References) {
                references += (references.isEmpty() ? "" : ";") + reference.Reference;
            }
        }
        
        return references;
    }
    
    /**
     * Check if document is registered.
     * @return 
     */
    public boolean isRecorded() {
        return ProcessedDps != null && ProcessedDps.getDpsKey() != null && ProcessedDps.SwapDataProcessingId != 0 && SwapDataProcessing != null;
    }
    
    /**
     * Check if payment is already requested.
     * @return 
     */
    public boolean isPaymentRequested() {
        return Payment != null;
    }
    
    /**
     * Check if payment is indeed required and if the required payment definition is different from "not required".
     * @return 
     */
    private boolean isPaymentRequired() {
        return RequirePayment && RequiredPaymentDefinition != SSwapConsts.PAY_NOT_REQ;
    }
    
    /**
     * Check if payment is requestable.
     * @return 
     */
    public boolean isPaymentRequestDataAvailable() {
        return getRequiredPaymentDateEffective() != null && (getRequiredPaymentAmountEffective(null) > 0 || (getRequiredPaymentPct() > 0 && getRequiredPaymentPct() <= 1));
    }
    
    /**
     * Check if document has references and if they are of the given reference type.
     * @param referenceType Reference type (SSwapConsts.TXN_DOC_TYPE_...).
     * @return 
     */
    public boolean hasReferences(final int referenceType) {
        return References != null && References.length > 0 && ReferencesType == referenceType;
    }
    
    /**
     * Check if supplier of document has advances.
     * @return 
     */
    public boolean hasAdvances() {
        return AuxAdvances != null && AuxAdvances.length > 0;
    }
    
    /**
     * Get advances data as a string.
     * @param client GUI client.
     * @return 
     */
    public String getAdvancesAsString(final SGuiClient client) {
        String string = "";
        
        if (hasAdvances()) {
            string = "El proveedor " + BizPartner + " tiene "
                    + (AuxAdvances.length == 1 ? "el siguiente anticipo" : "los siguientes " + SLibUtils.DecimalFormatInteger.format(AuxAdvances.length) + " anticiipos") + ", "
                    + "al corte del " + SLibUtils.DateFormatDate.format(client.getSession().getSystemDate()) + ":";

            for (int i = 0; i < AuxAdvances.length; i++) {
                string += "\n" + (i + 1) + ") " + AuxAdvances[i].getAdvanceAsString();
            }
        }
        
        return string;
    }
    
    /**
     * Check suitability of business partner's advances on an upcomign payment request.
     * Regarding business partner's advances, when these do not exist, go ahead, otherwise, ask for confirmation.
     * @param client GUI client.
     * @param validateDocumentIsRecorded Validate if document is already recorded.
     * @return <code>true</code> only when payment is not requestable or there are not advances or user confirmed that these advances do not matter.
     */
    public boolean checkAdvancesOnUpcommingPaymentRequest(final SGuiClient client, final boolean validateDocumentIsRecorded) {
        boolean check = true;
        
        if (isPaymentRequestable(validateDocumentIsRecorded)) {
            if (hasAdvances()) {
                String confirm = "Considere que:\n"
                        + getAdvancesAsString(client) + "\n"
                        + SGuiConsts.MSG_CNF_CONT;
                check = client.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION;
            }
        }
        
        return check;
    }
    
    /**
     * Get key of first reference DPS key if it matches the given reference type.
     * @param client GUI client.
     * @param referenceType Reference type (SSwapConsts.TXN_REF_TYPE_...).
     * @return 
     * @throws java.lang.Exception 
     */
    public int[] getFirstReferenceDpsKey(final SGuiClient client, final int referenceType) throws Exception {
        int[] referenceDpsKey = null;
        int[] dpsClassKey = null;
        String refPrefix = "";
        
        switch (referenceType) {
            case SSwapConsts.TXN_REF_TYPE_ORDER:
                dpsClassKey = SDataConstantsSys.TRNS_CL_DPS_PUR_ORD;
                refPrefix = SSwapConsts.TXN_REF_TYPE_ORDER_CODE;
                break;
            case SSwapConsts.TXN_REF_TYPE_INVOICE:
                dpsClassKey = SDataConstantsSys.TRNS_CL_DPS_PUR_DOC;
                refPrefix = "";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Tipo no soportado de documento de la referencia: " + referenceType + ".)");
        }
        
        if (hasReferences(referenceType)) {
            SImportUtils.DpsKey dpsKey = References[0].createDpsKey();

            if (dpsKey != null) {
                referenceDpsKey = dpsKey.asKey();
            }
            else if (referenceType == SSwapConsts.TXN_REF_TYPE_ORDER || referenceType == SSwapConsts.TXN_REF_TYPE_INVOICE) {
                SImportUtils.DpsFolio dpsFolio = SImportUtils.createDpsFolio(References[0].Reference, refPrefix);

                if (dpsFolio != null) {
                    referenceDpsKey = SDataUtilities.obtainDpsKey((SClientInterface) client, dpsFolio.Series, dpsFolio.Number, dpsClassKey);
                }
            }
        }
        
        return referenceDpsKey;
    }
    
    /**
     * Get payment request data as a string.
     * @return 
     */
    public String getPaymentRequestDataAsString() {
        String string = "";
        
        if (!isPaymentRequired()) {
            string += "¡No se solicitó pago!";
        }
        
        if (!isPaymentRequestDataAvailable()) {
            string += (!string.isEmpty() ? "; " : "");
            
            string += "¡No hay información disponible para solicitar pago!";
        }
        else {
            string += (!string.isEmpty() ? "; " : "");
            
            double amountEffective = getRequiredPaymentAmountEffective(null);
            
            if (amountEffective > 0) {
                string += "monto solicitado de pago: $" + SLibUtils.getDecimalFormatAmount().format(amountEffective) + " " + CurrencyCode + "; ";
            }
            else {
                string += "porcentaje solicitado de pago: $" + SLibUtils.DecimalFormatPercentage1D.format(getRequiredPaymentPct()) + "; ";
            }
            
            string += "fecha requerida de pago: " + SLibUtils.GuiDateFormat.format(getRequiredPaymentDateEffective()) + ".";
        }
        
        return string;
    }
    
    /**
     * Get payment request by DPS key.
     * @param session GUI session.
     * @param dpsKey DPS key.
     * @return 
     * @throws java.lang.Exception 
     */
    private SDbPayment getPaymentRequestByDpsKey(final SGuiSession session, final int[] dpsKey) throws Exception {
        SDbPayment payment = null;
        
        String sql = "SELECT p.id_pay "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " AS pe ON pe.id_pay = p.id_pay "
                + "WHERE NOT p.b_del AND pe.fk_doc_year_n = " + dpsKey[0] + " AND pe.fk_doc_doc_n = " + dpsKey[1] + " AND pe.install = 1;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                payment = (SDbPayment) session.readRegistry(SModConsts.FIN_PAY, new int[] { resultSet.getInt("p.id_pay")});
            }
        }
        
        return payment;
    }
    
    /**
     * Create and save payment request.
     * @param session GUI session.
     * @param dps Linked DPS.
     * @param validateDocumentIsRecorded Validate if document is already recorded.
     * @return
     * @throws Exception 
     */
    private SDbPayment createAndSavePaymentRequest(final SGuiSession session, final SThinDps dps, final boolean validateDocumentIsRecorded) throws Exception {
        SDbPayment payment = null;
        
        if (!isPaymentRequired()) {
            throw new Exception(EXC_PAY_NOT_REQUIRED);
        }
        else if (validateDocumentIsRecorded && !isRecorded()) {
            throw new Exception(EXC_DOC_NOT_RECORDED);
        }
        else if (dps == null) {
            throw new Exception("No se proporcionó ninguna factura para generar el pago.");
        }
        else if (!SLibUtils.compareKeys(ProcessedDps.getDpsKey(), (int[]) dps.getPrimaryKey())) {
            throw new Exception("La factura vinculada a este documento (PK = " + SLibUtils.textKey(ProcessedDps.getDpsKey()) + ") es distinta a la factura proporcionada para generar el pago (PK = " + SLibUtils.textKey((int[]) dps.getPrimaryKey()) + ").");
        }
        else if (getRequiredPaymentDateEffective() == null) {
            throw new Exception("Este documento no tiene fecha requerida de pago.");
        }
        else if (getRequiredPaymentAmountEffective(dps) == 0) {
            throw new Exception("Este documento no tiene monto requerido de pago.");
        }
        else if (getRequiredPaymentPct() == 0) {
            throw new Exception("Este documento no tiene porcentaje requerido de pago.");
        }
        else if (getRequiredPaymentPct() > 1) {
            throw new Exception("Este documento tiene un porcentaje requerido de pago mayor a 100%.");
        }
        else {
            // check if first payment request already exists:
            
            payment = getPaymentRequestByDpsKey(session, ProcessedDps.getDpsKey());
            
            // create and save payment request:
            
            if (payment == null) {
                boolean isAutoAuthReqPayReq = ConfigSettings.isAutoAuthReqPayReq(session.getUser().getPkUserId()); // check if payment request needs to be send to be authorized automatically
                boolean isAutoAuthPayReq = ConfigSettings.isAutoAuthPayReq(ProcessingTypeId); // check if payment request will be authorized automatically
                double exchangeRate = SDocumentUtils.getExchangeRate(session, CurrencyId, session.getCurrentDate()); // throws exception if exchange rate is unavailable
                Date dateRequired = getRequiredPaymentDateEffective();

                // create & prepare payment and its single one payment entry:

                payment = new SDbPayment();
                
                SDbPaymentEntry singleEntry = new SDbPaymentEntry();
                payment.getChildEntries().add(singleEntry);
                
                payment.processPaymentAtApplication(session, getRequiredPaymentAmountEffective(dps), CurrencyId, exchangeRate, IsRequiredPaymentLoc, 1, getTotalEffective(dps));

                //payment.setPkPaymentId(...);
                payment.setPaymentType(SDbPayment.TYPE_REQUEST);
                payment.setSeries("");
                payment.setNumber(0);
                payment.setDateApplication(session.getCurrentDate());
                payment.setDateRequired(dateRequired);
                payment.setDateSchedule_n(isAutoAuthPayReq ? dateRequired : null);
                payment.setDateExecution_n(null);
                // ...
                payment.setPaymentWay(DCfdi40Catalogs.FDP_POR_DEF);
                payment.setPriority(Priority == SDbPayment.PRIORITY_URGENT ? SDbPayment.PRIORITY_URGENT : SDbPayment.PRIORITY_NORMAL);
                payment.setNotes(!RequiredPaymentNotes.isEmpty() ? RequiredPaymentNotes : "-"); // "-" means no comments
                payment.setNotesAuthorization(!Description.isEmpty() ? Description : "-"); // "-" means no comments
                payment.setNotesAuthorizationFlow("");
                payment.setReceiptPaymentRequired(dps.getFkPaymentTypeId() == SDataConstantsSys.TRNS_TP_PAY_CREDIT);
                payment.setRescheduled(false);
                payment.setExecutedManually(false);
                payment.setDeleted(false);
                payment.setSystem(isAutoAuthReqPayReq); // system's payment requests are send automatically to be authorized, otherwise a user intervention is required
                payment.setFkStatusPaymentId(isAutoAuthPayReq ? SModSysConsts.FINS_ST_PAY_SCHED : SModSysConsts.FINS_ST_PAY_NEW);
                //payment.setFkCurrencyId(...); // set in SDbPayment.processPaymentCy()
                payment.setFkBeneficiaryId(BizPartnerId);
                payment.setFkFunctionalAreaId(dps.getFkFunctionalAreaId());
                payment.setFkFunctionalSubareaId(dps.getFkFunctionalSubAreaId());
                payment.setFkPayerCashBizPartnerBranchId_n(0);
                payment.setFkPayerCashAccountingCashId_n(0);
                payment.setFkBeneficiaryBankBizParterBranchId_n(0);
                payment.setFkBeneficiaryBankAccountCashId_n(0);
                payment.setFkUserScheduleId(SUtilConsts.USR_NA_ID);
                payment.setFkUserRescheduleId(SUtilConsts.USR_NA_ID);
                payment.setFkUserExecutiondId(SUtilConsts.USR_NA_ID);
                //payment.setFkUserInsertId(...);
                //payment.setFkUserUpdateId(...);
                //payment.setTsUserScheduled(...);
                //payment.setTsUserRescheduled(...);
                //payment.setTsUserExecuted(...);
                //payment.setTsUserInsert(...);
                //payment.setTsUserUpdate(...);

                payment.setDbmsStatus((String) session.readField(SModConsts.FINS_ST_PAY, new int[] { payment.getFkStatusPaymentId() }, SDbRegistry.FIELD_NAME));
                //payment.setDbmsBeneficiary(...);
                //payment.setDbmsDataCurrency(...);

                // prepare payment entry:

                //paymentEntry.setPkPaymentId(...);
                //paymentEntry.setPkEntryId(...);
                singleEntry.setEntryType(SDbPaymentEntry.TYPE_PAYMENT);
                //...
                singleEntry.setFkDocYearId_n(dps.getPkYearId());
                singleEntry.setFkDocDocId_n(dps.getPkDocId());
                //paymentEntry.setFkEntryCurrencyId(...); // set in SDbPayment.processPaymentCy()
                singleEntry.setFkPaymentRequestId_n(0);

                Exception exception = null;
                
                try (Statement statement = session.getStatement().getConnection().createStatement()) {
                    try {
                        statement.execute("START TRANSACTION");
                        payment.save(session);
                    }
                    catch (Exception e) {
                        exception = e;
                        statement.execute("ROLLBACK");
                    }
                    finally {
                        statement.execute("COMMIT");
                    }
                }
                
                if (exception != null) {
                    throw exception;
                }
            }
        }
        
        return payment;
    }
    
    /**
     * Link document to its given matching DPS, and optionally and only when it is an invoice, create its payment request.
     * Intenged to be used in GUI context.
     * @param session GUI session.
     * @param filesDownloadServiceUrl URL of document files download service.
     * @param dpsKey DPS primary key of invoice or credit note to be linked to.
     * @param paymentTypeMatchigPolicy Payment type matching policy: MATCH_PAY_TP...
     * @param allowGreaterDocToLink Allow linking an invoice or credit note whose total is greater.
     * @param allowLaterDocToLink Allow linking an invoice or credit note wich is issued later.
     * @param ommitNumberValidation Ommit number validation of invoice or credit note to be linked to.
     * @param createPaymentRequest Create-payment-request flag.
     * @return
     * @throws Exception 
     */
    public boolean link(final SGuiSession session, final String filesDownloadServiceUrl, final int[] dpsKey, final int paymentTypeMatchigPolicy, final boolean allowGreaterDocToLink, final boolean allowLaterDocToLink, final boolean ommitNumberValidation, final boolean createPaymentRequest) throws Exception {
        boolean linked = false;
        String prefix = "No se pudo realizar la vinculación:\n";
        
        // Validate linkage:
        
        if (isRecorded()) {
            throw new Exception(prefix + EXC_DOC_ALREADY_RECORDED_IN_ + ProcessedDps.composeRecord() + ".");
        }
        else {
            // Read DPS in its "thin" version:
            
            SThinDps dps = new SThinDps();
            dps.read(dpsKey, session.getStatement());
            
            // Validate DPS:
            
            if (dps.getDbmsRecordKey() == null) {
                throw new Exception(prefix + "La " + DocumentName + " a vincular a este documento, '" + dps.getDpsNumber() + "', no está contabilizada.");
            }
            else if (BizPartnerId != dps.getFkBizPartnerId_r()) {
                // business partner does not match:
                throw new Exception(prefix + "El asociado de negocios de este documento, '" + BizPartner + "' (ID = " + BizPartnerId + "), "
                        + "es distinto al de la " + DocumentName + " a vincular '" + (String) session.readField(SModConsts.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SDbRegistry.FIELD_NAME) + "' (ID = " + dps.getFkBizPartnerId_r() + ").");
            }
            else if (CurrencyId != dps.getFkCurrencyId()) {
                // currency does not match:
                throw new Exception(prefix + "La moneda de este documento, " + CurrencyCode + ", "
                        + "es distinta a la de la " + DocumentName + " a vincular, " + dps.getDbmsCurrencyCode() + ".");
            }
            else if ((AuxPaymentType == SDataConstantsSys.TRNS_TP_PAY_CASH && AuxPaymentType != dps.getFkPaymentTypeId()) &&
                    (paymentTypeMatchigPolicy == MATCH_PAY_TP_MAND || (paymentTypeMatchigPolicy == MATCH_PAY_TP_CONF_DIFF &&
                    session.getClient().showMsgBoxConfirm("Este documento es de contado, pero la " + DocumentName + " a vincular '" + dps.getDpsNumber() + "' no lo es.\n"
                            + "¿Está seguro que desea hacer caso omiso y continuar?") != JOptionPane.YES_OPTION))) {
                throw new Exception(prefix + "Tanto este documento como la " + DocumentName + " a vincular, '" + dps.getDpsNumber() + "', deben ser de contado.");
            }
            else if ((AuxPaymentType == SDataConstantsSys.TRNS_TP_PAY_CREDIT && AuxPaymentType != dps.getFkPaymentTypeId()) &&
                    (paymentTypeMatchigPolicy == MATCH_PAY_TP_MAND || (paymentTypeMatchigPolicy == MATCH_PAY_TP_CONF_DIFF &&
                    session.getClient().showMsgBoxConfirm("Este documento es de crédito, pero la " + DocumentName + " a vincular '" + dps.getDpsNumber() + "' no lo es.\n"
                            + "¿Está seguro que desea hacer caso omiso y continuar?") != JOptionPane.YES_OPTION))) {
                throw new Exception(prefix + "Tanto este documento como la " + DocumentName + " a vincular, '" + dps.getDpsNumber() + "', deben ser de crédito.");
            }
            else if (!SLibUtils.compareAmount(Total, dps.getTotalCy_r()) && (
                    (Math.abs(Total - dps.getTotalCy_r()) < 1d && session.getClient().showMsgBoxConfirm(
                            "Hay una diferencia entre el total de este documento y el de la " + DocumentName + " a vincular de $" + SLibUtils.getDecimalFormatAmount().format(Total - dps.getTotalCy_r()) + " " + CurrencyCode + ".\n"
                            + "¿Está seguro que desea hacer caso omiso y continuar?") != JOptionPane.YES_OPTION) ||
                    (Math.abs(Total - dps.getTotalCy_r()) >= 1d && Total > dps.getTotalCy_r() && (!allowGreaterDocToLink || session.getClient().showMsgBoxConfirm(
                            "El total de la " + DocumentName + " a vincular, $" + SLibUtils.getDecimalFormatAmount().format(dps.getTotalCy_r()) + " " + CurrencyCode + ", es mayor al de este documento, $" + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode + ", por $" + SLibUtils.getDecimalFormatAmount().format(dps.getTotalCy_r() - Total) + " " + CurrencyCode + ".\n"
                            + "¿Está seguro que desea hacer caso omiso y continuar?") != JOptionPane.YES_OPTION)))) {
                /*
                total does not match AND
                (absolute difference is < $1.00 AND user doesn't accept) OR
                (absolute difference is >= $1.00 AND this total is > document's AND (no greater invoices or credit notes allowed OR user user doesn't accept))
                */
                throw new Exception(prefix + "El total de este documento, $" + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode + ", "
                        + "es distinto al de la " + DocumentName + " a vincular, $" + SLibUtils.getDecimalFormatAmount().format(dps.getTotalCy_r()) + " " + dps.getDbmsCurrencyCode() + ".");
            }
            else if ((!allowLaterDocToLink && !SLibTimeUtils.isSameDate(Date, dps.getDate())) ||
                    (allowLaterDocToLink && (dps.getDate().before(Date) || (dps.getDate().after(Date) && session.getClient().showMsgBoxConfirm("La fecha de la " + DocumentName + " a vincular, "
                            + SLibUtils.DateFormatDate.format(dps.getDate()) + ", es posterior a la de este documento, " + SLibUtils.DateFormatDate.format(Date) + ".\n"
                            + "¿Está seguro que desea hacer caso omiso y continuar?") != JOptionPane.YES_OPTION)))) {
                // match required:
                throw new Exception(prefix + "La fecha de este documento, " + SLibUtils.DateFormatDate.format(Date) + ", "
                        + "es distinta a la de la " + DocumentName + " a vincular, " + SLibUtils.DateFormatDate.format(dps.getDate()) + ".");
            }
            else {
                if (!ommitNumberValidation) {
                    // validate folio of document:

                    String msgChooseOtherDocToLink = "Favor de elegir una " + DocumentName + " distinta a la '" + dps.getDpsNumber() + "' para vincularla a este documento.";

                    // check folio number: it must match its counterpart in document, in DPS it is allways available:

                    String msgMatter = "";
                    String msgError = "";
                    String msgConfirm = "";

                    if (!Number.isEmpty()) {
                        // document has folio number:

                        msgMatter = "El número del folio de este documento, '" + Number + "', ";

                        if (!Number.toUpperCase().equals(dps.getNumber().toUpperCase())) {
                            // match required:
                            msgError = msgMatter + "es distinto al de la " + DocumentName + " a vincular, '" + dps.getNumber() + "'.";
                        }
                    }
                    else {
                        // document does not have folio number:

                        msgMatter = "Este documento no tiene número de folio";

                        if (ExternalDocumentUuid.isEmpty()) {
                            // no UUID available to attempt to find similitudes:

                            // match required:
                            msgError = " ni UUID,\n"
                                    + "mientras que el número de folio de la " + DocumentName + " a vincular es '" + dps.getNumber() + "'.";
                        }
                        else {
                            // UUID available, attempt to find similitudes:

                            if (ExternalDocumentUuid.toUpperCase().equals(dps.getNumber().toUpperCase())) {
                                msgConfirm = msgMatter + ", pero su UUID, '" + ExternalDocumentUuid + "',\n"
                                        + "es igual al número del folio de la " + DocumentName + " a vincular, '" + dps.getNumber() + "'.";
                            }
                            else if (dps.getNumber().length() >= DCfdVer4Consts.LEN_UUID_1ST_SEGMENT && dps.getNumber().length() < ExternalDocumentUuid.length() && ExternalDocumentUuid.toUpperCase().startsWith(dps.getNumber().toUpperCase())) {
                                msgConfirm = msgMatter + ", pero su UUID, '" + ExternalDocumentUuid + "',\n"
                                        + "inicia como el número del folio de la " + DocumentName + " a vincular, '" + dps.getNumber() + "'.";
                            }
                            else {
                                // match required:
                                msgError = msgMatter + ", y su UUID, '" + ExternalDocumentUuid + "',\n"
                                        + "no tiene similitud con el número del folio de la " + DocumentName + " a vincular, '" + dps.getNumber() + "'.";
                            }
                        }
                    }

                    // processs folio number validation:

                    if (!msgError.isEmpty()) {
                        throw new Exception(prefix + msgError);
                    }
                    else if (!msgConfirm.isEmpty()) {
                        if (session.getClient().showMsgBoxConfirm(msgConfirm + "\n"
                                + "Sin embargo, es posible vincular la " + DocumentName + " '" + dps.getDpsNumber() + "' a este documento.\n"
                                + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                            throw new Exception(msgChooseOtherDocToLink);
                        }
                    }

                    // check folio series: it must match its counterpart in document, in DPS it is not allways available:

                    msgMatter = "";
                    msgError = "";
                    msgConfirm = "";

                    if (!NumberSeries.isEmpty()) {
                        // document has folio series:

                        msgMatter = "La serie del folio de este documento, '" + NumberSeries + "', ";

                        if (!NumberSeries.toUpperCase().equals(dps.getNumberSeries().toUpperCase())) {
                            // match required:
                            msgError = msgMatter + "es distinta a la de la " + DocumentName + " a vincular, '" + dps.getNumberSeries()+ "'.";
                        }
                        else if (dps.getNumberSeries().isEmpty()) {
                            // match required:
                            msgConfirm = msgMatter + "no corresponde a la de la " + DocumentName + " a vincular porque esta carece de serie.";
                        }
                    }
                    else {
                        // document does not have folio series:

                        msgMatter = "Este documento no tiene serie de folio";

                        if (!dps.getNumberSeries().isEmpty()) {
                            msgConfirm = msgMatter + ", y no corresponde a la de la " + DocumentName + " a vincular porque su serie es '" + dps.getNumberSeries() + "'.";
                        }
                    }

                    // processs folio series validation:

                    if (!msgError.isEmpty()) {
                        throw new Exception(prefix + msgError);
                    }
                    else if (!msgConfirm.isEmpty()) {
                        if (session.getClient().showMsgBoxConfirm(msgConfirm + "\n"
                                + "Sin embargo, es posible vincular la " + DocumentName + " '" + dps.getDpsNumber() + "' a este documento.\n"
                                + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                            throw new Exception(msgChooseOtherDocToLink);
                        }
                    }
                }

                // link document to its matching invoice or credit note:

                Object[] recKey = (Object[]) dps.getDbmsRecordKey();

                int recYearId = (Integer) recKey[0];
                int recPeriodId = (Integer) recKey[1];
                int recBokkeepingCenterId = (Integer) recKey[2];
                String recRecordTypeId = (String) recKey[3];
                int recNumberId = (Integer) recKey[4];
                String recCompanyBranchCode = SDataRecord.getCompanyBranchCode(dps.getDbmsRecordKey(), session.getStatement());

                ProcessedDps = new SImportedDocument.ProcessedDps(0, dps.getPkYearId(), dps.getPkDocId(), dps.getDpsNumber(), dps.getDate(), dps.getTotalCy_r(), dps.getDbmsCurrencyCode(), 
                        recYearId, recPeriodId, recBokkeepingCenterId, recRecordTypeId, recNumberId, recCompanyBranchCode, 
                        dps.getFkUserNewId(), dps.getDbmsUserNew(), dps.getThinCfd() != null, dps.getThinPdf() != null);

                // check if first payment request already exists (applying only to invoices):

                SDbPayment payment = null;
                
                if (isInvoice()) {
                    payment = getPaymentRequestByDpsKey(session, ProcessedDps.getDpsKey());
                    
                    if (createPaymentRequest && payment == null && isPaymentRequired() && isPaymentRequestDataAvailable()) {
                        payment = createAndSavePaymentRequest(session, dps, false);
                    }
                }


                // create DPS processing:

                SDbSwapDataProcessing swapDataProcessing = new SDbSwapDataProcessing();

                //swapDataProcessing.setPkSwapDataProcessingId(...);
                swapDataProcessing.setDataType(getSwapPrcDataType());
                swapDataProcessing.setTransactionCategory(SDataConstantsSys.TRNS_CT_DPS_PUR);
                swapDataProcessing.setExternalDataId(ExternalDocumentId);
                swapDataProcessing.setExternalDataUuid(ExternalDocumentUuid);
                swapDataProcessing.setExternalDataAuthorizationHistory("");
                swapDataProcessing.setDpsReferences(composeReferences());
                swapDataProcessing.setDpsDescription(Description);
                swapDataProcessing.setDpsPaymentLocal(IsRequiredPaymentLoc);
                swapDataProcessing.setProcessingType(ProcessingTypeId);
                swapDataProcessing.setProcessingUploadedBy(DocumentUploadedBy);
                swapDataProcessing.setProcessingUploadedAt(DocumentUploadedAt);
                swapDataProcessing.setProcessingReviewedBy(DocumentReviewedBy);
                swapDataProcessing.setProcessingReviewedAt(DocumentReviewedAt);
                swapDataProcessing.setProcessingAuthorizedBy(DocumentAuthorizedBy);
                swapDataProcessing.setProcessingAuthorizedAt(DocumentAuthorizedAt);
                
                boolean isPaymentRequired = isInvoice() && isPaymentRequired();
                
                swapDataProcessing.setPaymentRequired(isPaymentRequired);
                if (isPaymentRequired && isPaymentRequestDataAvailable()) {
                    swapDataProcessing.setPaymentApplicationCy(getRequiredPaymentAmountEffective(dps));
                    swapDataProcessing.setPaymentDateRequired_n(getRequiredPaymentDateEffective());
                }
                else {
                    swapDataProcessing.setPaymentApplicationCy(0);
                    swapDataProcessing.setPaymentDateRequired_n(null);
                }
                
                swapDataProcessing.setDeleted(false);
                swapDataProcessing.setSystem(false);
                
                swapDataProcessing.setFkDpsYearId_n(dps.getPkYearId());
                swapDataProcessing.setFkDpsDocId_n(dps.getPkDocId());
                
                swapDataProcessing.setFkPaymentId_n(payment == null ? 0 : payment.getPkPaymentId());
                swapDataProcessing.setFkPayCurrencyId_n(payment == null ? 0 : payment.getFkCurrencyId());
                
                swapDataProcessing.setAccMethod(AccMethod);
                swapDataProcessing.setAccUserUnits(AccUnits);
                swapDataProcessing.setAccSystemUnits(AccUnits);
                swapDataProcessing.setFkAccUserAccountId_n(AccAccountId);
                swapDataProcessing.setFkAccUserCostCenterId_n(AccCostCenterId);
                swapDataProcessing.setFkAccUserItemId_n(AccItemId);
                swapDataProcessing.setFkAccUserItemAuxId_n(AccItemAuxId);
                swapDataProcessing.setFkAccUserUnitId_n(AccUnitId);
                swapDataProcessing.setFkAccSystemAccountId_n(AccAccountId);
                swapDataProcessing.setFkAccSystemCostCenterId_n(AccCostCenterId);
                swapDataProcessing.setFkAccSystemItemId_n(AccItemId);
                swapDataProcessing.setFkAccSystemItemAuxId_n(AccItemAuxId);
                swapDataProcessing.setFkAccSystemUnitId_n(AccUnitId);
                
                //swapDataProcessing.setFkUserInsertId(...);
                //swapDataProcessing.setFkUserUpdateId(...);
                //swapDataProcessing.setTsUserInsert(...);
                //swapDataProcessing.setTsUserUpdate(...);

                swapDataProcessing.save(session);

                ProcessedDps.SwapDataProcessingId = swapDataProcessing.getPkSwapDataProcessingId();
                SwapDataProcessing = swapDataProcessing;
                Payment = payment;

                linked = true;
                
                // Attach XML and/or PDF, if needed:
                
                boolean attachXml = isBizPartnerDomestic(session.getClient()) && dps.getThinCfd() == null;
                boolean attachPdf = dps.getThinPdf() == null;
                
                if (attachXml || attachPdf) {
                    File[] files = AuxFiles; // re-use existing files, if available
                    
                    if (files == null || files.length != SImportUtils.CFDI_FILES) {
                        files = SImportUtils.downloadDocumentFilesInTempDir(session, filesDownloadServiceUrl, SImportUtils.DWNLD_FILES_TYPE_CFDI, ExternalDocumentId, getSwapTxnDocType());
                    }
                    
                    if (files != null && files.length == SImportUtils.CFDI_FILES) {
                        if (attachXml && files[SImportUtils.CFDI_XML_IDX] != null) {
                            // attach CFD:
                            
                            SDataCfd cfd = SDataCfd.prepareCfd(
                                    null, 
                                    files[SImportUtils.CFDI_XML_IDX], 
                                    session.getUser().getPkUserId());
                            
                            cfd.setFkDpsYearId_n(dps.getPkYearId());
                            cfd.setFkDpsDocId_n(dps.getPkDocId());
                            cfd.setTimestamp(dps.getDate());

                            if (cfd.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(prefix + SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + "\n(Registro CFD.)");
                            }
                            
                            ProcessedDps.HasCfd = true;
                        }
                        
                        if (attachPdf && files[SImportUtils.CFDI_PDF_IDX] != null) {
                            // attach PDF:
                            
                            SDataPdf pdf = SDataPdf.preparePdf(
                                    null, 
                                    files[SImportUtils.CFDI_PDF_IDX], 
                                    dps.getPkYearId(), 
                                    ((SDataParamsCompany) session.getConfigCompany()).getXmlBaseDirectory());
                            
                            pdf.setPkYearId(dps.getPkYearId());
                            pdf.setPkDocId(dps.getPkDocId());

                            if (pdf.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(prefix + SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + "\n(Registro PDF.)");
                            }
                            
                            ProcessedDps.HasPdf = true;
                        }
                    }
                }
            }
        }
        
        return linked;
    }
    
    /**
     * Unlink document from asigned DPS.
     * @param session GUI session.
     * @return
     * @throws Exception 
     */
    public boolean unlink(final SGuiSession session) throws Exception {
        boolean unlinked = false;
        
        if (!isRecorded()) {
            throw new Exception(EXC_DOC_NOT_RECORDED);
        }
        else {
            // check if payment can be deleted:
            
            if (isPaymentRequested()) {
                // treat payment as a non-system registry to check if it can be deleted!:
                
                boolean isSystem = Payment.isSystem(); // preserve original system condition
                
                Payment.setSystem(false); // reset system condition
                
                boolean canDelete = Payment.canDelete(session);
                
                Payment.setSystem(isSystem); // restore original system condition
                
                if (!canDelete) {
                    throw new Exception(Payment.getQueryResult());
                }
            }
            
            // unlink document:
            
            if (!SwapDataProcessing.isDeleted()) {
                SwapDataProcessing.setDeleted(true);
                SwapDataProcessing.save(session);
            }
            
            if (isPaymentRequested()) {
                Payment.setDeleted(true);
                Payment.save(session);
            }

            ProcessedDps = null;
            SwapDataProcessing = null;
            Payment = null;
            
            unlinked = true;
        }
        
        return unlinked;
    }
    
    /**
     * Retrieve document's existing processiog data.
     * @param session GUI session.
     * @param prepStatement Prepared statement.
     * @param dataType Supported options: SDbSwapDataProcessing.DATA_TYPE_INV and SDbSwapDataProcessing.DATA_TYPE_CN.
     * @param txnCategory Transaction category: 1 = purchase; 2 = sales.
     * @param externalId External ID.
     * @throws Exception 
     */
    public void retrieveProcessing(final SGuiSession session, final PreparedStatement prepStatement, final String dataType, final int txnCategory, final int externalId) throws Exception {
        ProcessedDps = SImportedDocument.getProcessedDpsByExternalId(prepStatement, dataType, txnCategory, externalId);

        if (ProcessedDps != null) {
            SwapDataProcessing = (SDbSwapDataProcessing) session.readRegistry(SModConsts.TRN_SWAP_DATA_PRC, new int[] { ProcessedDps.SwapDataProcessingId });
            
            DocumentUploadedBy = SwapDataProcessing.getProcessingUploadedBy();
            DocumentUploadedAt = SwapDataProcessing.getProcessingUploadedAt();
            DocumentReviewedBy = SwapDataProcessing.getProcessingReviewedBy();
            DocumentReviewedAt = SwapDataProcessing.getProcessingReviewedAt();
            DocumentAuthorizedBy = SwapDataProcessing.getProcessingAuthorizedBy();
            DocumentAuthorizedAt = SwapDataProcessing.getProcessingAuthorizedAt();

            if (SwapDataProcessing.getFkPaymentId_n() != 0) {
                Payment = (SDbPayment) session.readRegistry(SModConsts.FIN_PAY, new int[] { SwapDataProcessing.getFkPaymentId_n() });
            }
        }
    }
    
    /**
     * Check if payment is requestable.
     * If needed, document needs to be recorded, then payment must not be already requested and payment is required and its data available.
     * @param validateDocumentIsRecorded Validate if document is already recorded.
     * @return 
     */
    public boolean isPaymentRequestable(final boolean validateDocumentIsRecorded) {
        return (!validateDocumentIsRecorded || isRecorded()) &&
                !isPaymentRequested() &&
                isPaymentRequired() &&
                isPaymentRequestDataAvailable();
    }
    
    /**
     * Validate if payment is requestable.
     * If needed, document needs to be recorded, then payment must not be already requested and payment is required and its data available.
     * @return
     * @throws Exception 
     */
    private boolean validatePaymentIsRequestable(final boolean validateDocumentIsRecorded) throws Exception {
        if (validateDocumentIsRecorded && !isRecorded()) {
            throw new Exception(EXC_DOC_NOT_RECORDED);
        }
        else if (isPaymentRequested()) {
            throw new Exception(EXC_PAY_ALREADY_REQUESTED_IN_ + Payment.getFolio() + ", " + SLibUtils.DateFormatDate.format(Payment.getDateApplication()) + ".");
        }
        else if (!isPaymentRequired()) {
            throw new Exception(EXC_PAY_NOT_REQUIRED);
        }
        else if (!isPaymentRequestDataAvailable()) {
            throw new Exception(EXC_PAY_NOT_REQUESTABLE);
        }
        
        return true;
    }
    
    /**
     * Request payment of document.
     * @param session GUI session.
     * @return
     * @throws Exception 
     */
    public boolean requestPayment(final SGuiSession session) throws Exception {
        boolean requested = false;
        
        if (validatePaymentIsRequestable(true)) {
            SThinDps dps = new SThinDps();
            dps.read(ProcessedDps.getDpsKey(), session.getStatement());
            
            SDbPayment payment = createAndSavePaymentRequest(session, dps, true);

            SwapDataProcessing.setFkPaymentId_n(payment.getPkPaymentId());
            SwapDataProcessing.save(session);

            Payment = payment;

            requested = true;
        }
        
        return requested;
    }
    
    /**
     * Validate if the required payment date can be changed.
     * @return
     * @throws Exception 
     */
    private boolean validateRequiredPaymentDateIsChangeable() throws Exception {
        if (!isRecorded()) {
            throw new Exception(EXC_DOC_NOT_RECORDED);
        }
        else if (!isPaymentRequested()) {
            throw new Exception(EXC_PAY_NOT_REQUESTED_YET);
        }
        else if (!Payment.isExportable()) {
            throw new Exception("No se puede cambiar la fecha requerida de pago, el estatus de la solicitud de pago debe ser '" + SDbPayment.ST_NEW + "' o '" + SDbPayment.ST_SCHED + "'.");
        }
        
        return true;
    }
    
    /**
     * Request payment of document.
     * @param session GUI session.
     * @return
     * @throws Exception 
     */
    public boolean changeRequiredPaymentDate(final SGuiSession session) throws Exception {
        boolean changed = false;
        Date newDate = null;
        
        if (!isPaymentRequested()) {
            // payment request not yet created:
            if (!isPaymentRequired()) {
                throw new Exception(EXC_PAY_NOT_REQUIRED);
            }
            else if (!isPaymentRequestDataAvailable()) {
                throw new Exception(EXC_PAY_NOT_REQUESTABLE);
            }
        }
        else {
            // payment request already created:
            validateRequiredPaymentDateIsChangeable(); // thorws exception on validation failure
        }

        newDate = SDocumentUtils.pickDate(session, getRequiredPaymentDateEffective());

        if (newDate != null) {
            newDate = SLibTimeUtils.convertToDateOnly(newDate);
            
            String message = "La nueva fecha requerida de pago, " + SLibUtils.DateFormatDate.format(newDate) + ", no puede ser anterior ";
            
            if (newDate.before(SLibTimeUtils.convertToDateOnly(session.getSystemDate()))) {
                throw new Exception(message + "al día de hoy, " + SLibUtils.DateFormatDate.format(session.getSystemDate()) + ".");
            }
            else if (newDate.before(SLibTimeUtils.convertToDateOnly(Date))) {
                throw new Exception(message + "a la fecha del documento '" + getFolio() + "', " + SLibUtils.DateFormatDate.format(Date) + ".");
            }
            
            if (isPaymentRequested()) {
                // make due date of document match the new required date:
                SImportUtils.updateDpsDaysOfCreditByDueDate(session, ProcessedDps.getDpsKey(), newDate);

                // update the new required date according to current status of payment:
                
                switch (Payment.getFkStatusPaymentId()) {
                    case SModSysConsts.FINS_ST_PAY_NEW:
                        Payment.setDateRequired(newDate);
                        break;
                        
                    case SModSysConsts.FINS_ST_PAY_SCHED:
                        Payment.setDateSchedule_n(newDate);
                        Payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_SCHED_P);
                        Payment.setFkUserScheduleId(session.getUser().getPkUserId());
                        Payment.setTsUserSchedule(new Date());
                        break;
                        
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(ID no soportado de estatus de pago: " + Payment.getFkStatusPaymentId() + ".)");
                }
                
                Payment.save(session);
            }
        
            RequiredPaymentDateNew = SLibTimeUtils.isSameDate(RequiredPaymentDate, newDate) ? null : newDate;
            
            changed = true;
        }
        
        return changed;
    }
    
    /**
     * Create a new DPS header, without entries, from this document.
     * @param session GUI session.
     * @param order Order, can be <code>null</code>.
     * @return 
     * @throws java.lang.Exception 
     */
    public SDataDps createDps(final SGuiSession session, final SDataDps order) throws Exception {
        int year = SLibTimeUtils.digestYear(Date)[0];
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.BPSU_BP, new int[] { BizPartnerId }, SLibConstants.EXEC_MODE_STEALTH);
        SDbFunctionalSubArea functionalSubArea = (SDbFunctionalSubArea) session.readRegistry(SModConsts.CFGU_FUNC_SUB, new int[] { FunctionalSubAreaId });
        
        SDataDps dps = new SDataDps();
        
        dps.setPkYearId(year);
        //dps.setPkDocId(...
        dps.setDate(Date);
        dps.setDateDoc(Date);
        dps.setDateStartCredit(Date);
        //dps.setDateShipment_n(
        //dps.setDateDelivery_n(
        //dps.setDateDocLapsing_n(
        //dps.setDateDocDelivery_n(
        dps.setNumberSeries(NumberSeries);
        dps.setNumber(!Number.isEmpty() ? Number : SDocumentUtils.getUuidFirstSegment(ExternalDocumentUuid));
        //dps.setNumberReference(
        //dps.setCommissionsReference(
        //dps.setConditionsPayment(
        //dps.setApprovalYear(
        //dps.setApprovalNumber(
        //dps.setDaysOfCredit(
        //dps.setIsDiscountDocApplying(
        //dps.setIsDiscountDocPercentage(
        //dps.setDiscountDocPercentage(
        //dps.setSubtotalProvisional_r(
        //dps.setDiscountDoc_r(
        //dps.setSubtotal_r(
        //dps.setTaxCharged_r(
        //dps.setTaxRetained_r(
        //dps.setTotal_r(
        //dps.setCommissions_r(
        //dps.setExchangeRate(
        //dps.setExchangeRateSystem(
        //dps.setSubtotalProvisionalCy_r(
        //dps.setDiscountDocCy_r(
        //dps.setSubtotalCy_r(
        //dps.setTaxChargedCy_r(
        //dps.setTaxRetainedCy_r(
        //dps.setTotalCy_r(
        //dps.setCommissionsCy_r(
        //dps.setDriver(
        //dps.setPlate(
        //dps.setTicket(
        //dps.setShipments(
        //dps.setPayments(
        //dps.setPaymentMethod(
        //dps.setPaymentAccount(
        //dps.setAccountingTag(
        //dps.setAutomaticAuthorizationRejection(
        //dps.setIsPublic(
        //dps.setIsLinked(
        //dps.setIsClosed(
        //dps.setIsClosedCommissions(
        //dps.setIsShipped(
        //dps.setIsDpsDeliveryAck(
        //dps.setIsRebill(
        //dps.setIsAudited(
        //dps.setIsAuthorized(
        dps.setIsRecordAutomatic(true);
        //dps.setIsCopy(
        //dps.setIsCopied(
        //dps.setIsSystem(
        //dps.setIsDeleted(
        dps.setFkDpsCategoryId(SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0]);
        dps.setFkDpsClassId(SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1]);
        dps.setFkDpsTypeId(SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2]);
        dps.setFkPaymentTypeId(AuxPaymentType);
        dps.setFkPaymentSystemTypeId(SDataConstantsSys.TRNU_TP_PAY_SYS_NA);
        dps.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
        dps.setFkDpsValidityStatusId(SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
        dps.setFkDpsAuthorizationStatusId(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA);
        dps.setFkDpsAnnulationTypeId(SModSysConsts.TRNU_TP_DPS_ANN_NA);
        dps.setFkDpsNatureId(order != null ? order.getFkDpsNatureId() : SDataConstantsSys.TRNU_DPS_NAT_DEF);
        dps.setFkCompanyBranchId(((SClientInterface) session.getClient()).getSessionXXX().getCurrentCompanyBranchId());
        dps.setFkFunctionalAreaId(functionalSubArea.getFkFunctionalAreaId());
        dps.setFkFunctionalSubAreaId(functionalSubArea.getPkFunctionalSubAreaId());
        if (order != null) {
            dps.setFkBizPartnerId_r(order.getFkBizPartnerId_r());
            dps.setFkBizPartnerBranchId(order.getFkBizPartnerBranchId());
            dps.setFkBizPartnerBranchAddressId(order.getFkBizPartnerBranchAddressId());
            dps.setFkBizPartnerAltId_r(order.getFkBizPartnerAltId_r());
            dps.setFkBizPartnerBranchAltId(order.getFkBizPartnerBranchAltId());
            dps.setFkBizPartnerBranchAddressAltId(order.getFkBizPartnerBranchAddressAltId());
            dps.setFkBizPartnerAddresseeId_n(order.getFkBizPartnerAddresseeId_n());
            dps.setFkAddresseeBizPartnerId_nr(order.getFkAddresseeBizPartnerId_nr());
            dps.setFkAddresseeBizPartnerBranchId_n(order.getFkAddresseeBizPartnerBranchId_n());
            dps.setFkAddresseeBizPartnerBranchAddressId_n(order.getFkAddresseeBizPartnerBranchAddressId_n());
        }
        else {
            dps.setFkBizPartnerId_r(bizPartner.getPkBizPartnerId());
            dps.setFkBizPartnerBranchId(bizPartner.getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId());
            dps.setFkBizPartnerBranchAddressId(bizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getPkAddressId());
            //dps.setFkBizPartnerAltId_r(
            //dps.setFkBizPartnerBranchAltId(
            //dps.setFkBizPartnerBranchAddressAltId(
            //dps.setFkBizPartnerAddresseeId_n(
            //dps.setFkAddresseeBizPartnerId_nr(
            //dps.setFkAddresseeBizPartnerBranchId_n(
            //dps.setFkAddresseeBizPartnerBranchAddressId_n(
        }
        //dps.setFkContactBizPartnerBranchId_n(
        //dps.setFkContactContactId_n(
        //dps.setFkTaxIdentityEmisorTypeId(
        //dps.setFkTaxIdentityReceptorTypeId(
        dps.setFkLanguajeId(order != null ? order.getFkLanguajeId() : bizPartner.getDbmsCategorySettingsSup().getFkLanguageId_n());
        dps.setFkCurrencyId(CurrencyId);
        //dps.setFkSalesAgentId_n(
        //dps.setFkSalesAgentBizPartnerId_n(
        //dps.setFkSalesSupervisorId_n(
        //dps.setFkSalesSupervisorBizPartnerId_n(
        dps.setFkIncotermId(SModSysConsts.LOGS_INC_NA);
        //dps.setFkSpotSourceId_n(
        //dps.setFkSpotDestinyId_n(
        dps.setFkModeOfTransportationTypeId(SModSysConsts.LOGS_TP_MOT_NA);
        dps.setFkCarrierTypeId(SModSysConsts.LOGS_TP_CAR_NA);
        //dps.setFkCarrierId_n(
        //dps.setFkVehicleTypeId_n(
        //dps.setFkVehicleId_n(
        //dps.setFkBillOfLading_n(
        //dps.setFkSourceYearId_n(
        //dps.setFkSourceDocId_n(
        //dps.setFkMfgYearId_n(
        //dps.setFkMfgOrderId_n(
        dps.setFkUserLinkedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserClosedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserClosedCommissionsId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserShippedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserDpsDeliveryAckId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserAuditedId(SDataConstantsSys.USRX_USER_NA);
        dps.setFkUserAuthorizedId(SDataConstantsSys.USRX_USER_NA);
        //dps.setFkUserAuditedId(
        //dps.setFkUserAuthorizedId(
        //dps.setFkUserNewId(
        //dps.setFkUserEditId(
        //dps.setFkUserDeleteId(
        
        // set dayos of credit and accounting tag:
        
        if (AuxPaymentType == SDataConstantsSys.TRNS_TP_PAY_CREDIT && getDueDateEffective() != null) {
            dps.setDaysOfCreditByDueDate(getDueDateEffective());
        }
        
        String tag = AccountingTag;
        
        if (tag.isEmpty() && order != null && !order.getAccountingTag().isEmpty()) {
            tag = order.getAccountingTag();
        }
        
        dps.setAccountingTag(tag);
        
        // complete DPS creation:
        
        dps.setAuxKeepDpsData(true);
        dps.setXtaImportedDocument(this);
        
        return dps;
    }
    
    /**
     * Get auxiliar file by index.
     * @param fileIndex Either SImportUtils.CFDI_XML_IDX or SImportUtils.CFDI_PDF_IDX.
     * @return If available, auxiliar file, otherwise <code>null</code>.
     */
    public File getAuxFile(final int fileIndex) {
        File auxFile = null;
        
        if (AuxFiles != null && AuxFiles.length == SImportUtils.CFDI_FILES && fileIndex >= 0 && fileIndex < AuxFiles.length) {
            auxFile = AuxFiles[fileIndex];
        }
        
        return auxFile;
    }
    
    /**
     * Get auxiliar file name by index.
     * @param fileIndex Either SImportUtils.CFDI_XML_IDX or SImportUtils.CFDI_PDF_IDX.
     * @return If available, auxiliar file name, otherwise an empty <code>String</code>.
     */
    public String getAuxFileName(final int fileIndex) {
        File auxFile = getAuxFile(fileIndex);
        
        return auxFile == null ? "" : auxFile.getName();
    }
    
    /**
     * Retrieve XML and PDF files of document.
     * @param session GUI session.
     * @param filesDownloadServiceUrl URL of document files download service.
     * @return XML & PDF files, if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public File[] retrieveFiles(final SGuiSession session, final String filesDownloadServiceUrl) throws Exception {
        File[] files = null;
        
        if (AuxFiles != null && AuxFiles.length == SImportUtils.CFDI_FILES) {
            files = AuxFiles; // re-use existing files, if available
        }
        
        if (files == null) {
            boolean isBizPartnerDomestic = isBizPartnerDomestic(session.getClient());
            
            files = SImportUtils.downloadDocumentFilesInTempDir(session, filesDownloadServiceUrl, SImportUtils.DWNLD_FILES_TYPE_CFDI, ExternalDocumentId, getSwapTxnDocType());

            if (files == null || files.length != SImportUtils.CFDI_FILES) {
                throw new Exception("No se pudieron descargar o no existen los archivos XML y/o PDF del CFDI de esta " + DocumentName + " autorizada.");
            }
            else if (isBizPartnerDomestic && files[SImportUtils.CFDI_XML_IDX] == null) {
                throw new Exception("No se pudo descargar o no existe el archivo XML del CFDI de esta " + DocumentName + " autorizada.");
            }
            else if (files[SImportUtils.CFDI_PDF_IDX] == null) {
                throw new Exception("No se pudo descargar o no existe el archivo PDF de esta " + DocumentName + " autorizada.");
            }

            // copy files to local temporal directory:

            if (isBizPartnerDomestic) {
                SImportUtils.copyDocumentFileToTempDir(ExternalDocumentId, SFileUtilities.xml, files[SImportUtils.CFDI_XML_IDX], BizPartnerId);
            }

            SImportUtils.copyDocumentFileToTempDir(ExternalDocumentId, SFileUtilities.pdf, files[SImportUtils.CFDI_PDF_IDX], BizPartnerId);

            // preserve files:

            AuxFiles = files;
        }
        
        return files;
    }
    
    /**
     * Retrieve XML file of document.
     * @param session GUI session.
     * @param filesDownloadServiceUrl URL of document files download service.
     * @return XML file, if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public File retrieveXml(final SGuiSession session, final String filesDownloadServiceUrl) throws Exception {
        File xml = SImportUtils.getDocumentFileFromTempDirIfExists(ExternalDocumentId, SFileUtilities.xml, BizPartnerId);
        
        if (xml == null) {
            xml = getAuxFile(SImportUtils.CFDI_XML_IDX); // re-use existing file, if available
        }
        
        if (xml == null) {
            retrieveFiles(session, filesDownloadServiceUrl);
            xml = SImportUtils.getDocumentFileFromTempDirIfExists(ExternalDocumentId, SFileUtilities.xml, BizPartnerId);
        }
        
        return xml;
    }
    
    /**
     * Retrieve PDF file of document.
     * @param session GUI session.
     * @param filesDownloadServiceUrl URL of document files download service.
     * @return PDF file, if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public File retrievePdf(final SGuiSession session, final String filesDownloadServiceUrl) throws Exception {
        File pdf = SImportUtils.getDocumentFileFromTempDirIfExists(ExternalDocumentId, SFileUtilities.pdf, BizPartnerId);
        
        if (pdf == null) {
            pdf = getAuxFile(SImportUtils.CFDI_PDF_IDX); // re-use existing file, if available
        }
        
        if (pdf == null) {
            retrieveFiles(session, filesDownloadServiceUrl);
            pdf = SImportUtils.getDocumentFileFromTempDirIfExists(ExternalDocumentId, SFileUtilities.pdf, BizPartnerId);
        }
        
        return pdf;
    }
    
    /**
     * Get text of comparison of this document's total vs. total posted in accounting, if document is recorded.
     * @return 
     */
    private String getTotalComparison() {
        String string = "";
        
        if (isRecorded()) {
            boolean isTotalOk = SLibUtils.compareAmount(Total, ProcessedDps.DpsTotalCy);
            boolean isCurrencyOk = CurrencyCode.equals(ProcessedDps.DpsCurrencyCode);

            if (isTotalOk && isCurrencyOk) {
                string = "OK";
            }
            else {
                if (!isTotalOk) {
                    string = "Dif. total: $" + SLibUtils.getDecimalFormatAmount().format(ProcessedDps.DpsTotalCy - Total);
                }
                if (!isCurrencyOk) {
                    string = (string == null ? "" : string + "; ") + "Dif. moneda: " + ProcessedDps.DpsCurrencyCode;
                }
            }
        }
        
        return string;
    }

    /**
     * Format date.
     * @param oDate Date to format.
     * @return Formatted date, or "ND" if date is <code>null</code>.
     */
    private String formatDate(final Date oDate) {
        if (oDate == null) {
            return "ND";
        }

        String sDateAt = SLibUtils.DateFormatDatetime.format(oDate);
        return sDateAt;
    }
    
    /**
     * Get document information: upload, review, authorization.
     * @return 
     */
    public String getDocumentInfo() {
        String info = "Información del documento " + getFolio() + ", del " + SLibUtils.DateFormatDate.format(Date) + " (ID externo " + ExternalDocumentId + "):"
                + "\nNombre del proveedor: " + BizPartner + " (ID " + BizPartnerId + ").\n";
        
        info += "\nCarga:";
        info += "\n+ realizada por: " + (DocumentUploadedBy.isEmpty() ? "ND" : DocumentUploadedBy) + ";";
        info += "\n+ realizada el: " + (DocumentUploadedAt == null ? "ND" : SLibUtils.DateFormatDatetimeTimeZone.format(DocumentUploadedAt)) + ".";
        info += "\nRevisión:";
        info += "\n+ realizada por: " + (DocumentReviewedBy.isEmpty() ? "ND" : DocumentReviewedBy) + ";";
        info += "\n+ realizada el: " + (DocumentReviewedAt == null ? "ND" : SLibUtils.DateFormatDatetimeTimeZone.format(DocumentReviewedAt)) + ".";
        info += "\nAutorización:";
        info += "\n+ realizada por: " + (DocumentAuthorizedBy.isEmpty() ? "ND" : DocumentAuthorizedBy) + ";";
        info += "\n+ realizada el: " + (DocumentAuthorizedAt == null ? "ND" : SLibUtils.DateFormatDatetimeTimeZone.format(DocumentAuthorizedAt)) + ".";
        
        return info;
    }
    
    /**
     * Get document information: upload, review, authorization.
     * @param referenceType Reference type (SSwapConsts.TXN_DOC_TYPE_...).
     * @return 
     */
    public String getReferenceInfo(final int referenceType) {
        String info = "Información de la referencia del documento " + getFolio() + ", del " + SLibUtils.DateFormatDate.format(Date) + " (ID externo " + ExternalDocumentId + "):"
                + "\nNombre del proveedor: " + BizPartner + " (ID " + BizPartnerId + ").\n";
        
        if (!hasReferences(referenceType)) {
            String name = SSwapConsts.ReferenceTypes.get(referenceType);
            info += "\n+ ¡El documento no tiene referencias de tipo " + (name == null || name.isEmpty() ? "desconocido (" + referenceType + ")" : name) + "!";
        }
        else {
            String name = SSwapConsts.ReferenceTypes.get(referenceType);
            info += "\nReferencia " + References[0].Reference + ", de tipo " + (name == null || name.isEmpty() ? "desconocido (" + referenceType + ")" : name) + ":";
            info += "\nCreación:";
            info += "\n+ realizada por: " + (References[0].ReferenceCreatedBy.isEmpty() ? "ND" : References[0].ReferenceCreatedBy) + ";";
            info += "\n+ realizada el: " + (References[0].ReferenceCreatedAt == null ? "ND" : SLibUtils.DateFormatDatetimeTimeZone.format(References[0].ReferenceCreatedAt)) + ".";
            info += "\nAutorización:";
            info += "\n+ realizada por: " + (References[0].ReferenceAuthorizedBy.isEmpty() ? "ND" : References[0].ReferenceAuthorizedBy) + ";";
            info += "\n+ realizada el: " + (References[0].ReferenceAuthorizedAt == null ? "ND" : SLibUtils.DateFormatDatetimeTimeZone.format(References[0].ReferenceAuthorizedAt)) + ".";
        }
        
        return info;
    }
    
    /*
     * Implemented and overriden inherited methods
     */

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
            case COL_DOWNLOAD:
                Download = (boolean) value;
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
                value = BizPartner;
                break;
            case 1:
                value = getFolio();
                break;
            case 2:
                value = Date;
                break;
            case 3:
                value = ReferencesAsText;
                break;
            case 4:
                value = Description;
                break;
            case 5:
                value = Total;
                break;
            case 6:
                value = CurrencyCode;
                break;
            case 7:
                value = Priority == SDbPayment.PRIORITY_URGENT ? SGridConsts.ICON_EXCL : SGridConsts.ICON_NULL;
                break;
            case COL_DOWNLOAD:
                value = Download;
                break;
            case 9:
                value = AlreadyDownloaded;
                break;
            case 10:
                value = isRecorded();
                break;
            case 11:
                value = !isRecorded() ? null : ProcessedDps.composeRecord();
                break;
            case 12:
                value = !isRecorded() ? false : ProcessedDps.HasCfd;
                break;
            case 13:
                value = !isRecorded() ? false : ProcessedDps.HasPdf;
                break;
            case 14:
                value = FunctionalSubArea;
                break;
            case 15:
                value = FiscalUseCode;
                break;
            case 16:
                value = ProcessingTypeCode;
                break;
            case 17:
                value = getRevisionYearWeek();
                break;
            case 18:
                value = RevisionDatetime;
                break;
            case 19:
                value = RequirePayment;
                break;
            case 20:
                value = getRequiredPaymentPct();
                break;
            case 21:
                value = getRequiredPaymentAmount(null);
                break;
            case 22:
                value = RequiredPaymentAmountNew == 0 ? null : RequiredPaymentAmountNew;
                break;
            case 23:
                value = CurrencyCode;
                break;
            case 24:
                value = RequiredPaymentDate;
                break;
            case 25:
                value = RequiredPaymentDateNew;
                break;
            case 26:
                value = IsRequiredPaymentLoc;
                break;
            case 27:
                value = RequiredPaymentNotes;
                break;
            case 28:
                value = SSwapConsts.PayDefinitions.get(RequiredPaymentDefinition);
                break;
            case 29:
                value = !isPaymentRequested() ? null : Payment.getFolio();
                break;
            case 30:
                value = !isPaymentRequested() ? null : Payment.getDateApplication();
                break;
            case 31:
                value = !isRecorded() ? null : ProcessedDps.DpsFolio;
                break;
            case 32:
                value = !isRecorded() ? null : ProcessedDps.DpsDate;
                break;
            case 33:
                value = !isRecorded() ? null : ProcessedDps.DpsTotalCy;
                break;
            case 34:
                value = !isRecorded() ? null : ProcessedDps.DpsCurrencyCode;
                break;
            case 35:
                value = !isRecorded() ? null : getTotalComparison();
                break;
            case 36:
                value = DueDate;
                break;
            case 37:
                value = AccountingTag;
                break;
            case 38:
                value = ExternalDocumentUuid;
                break;
            case 39:
                value = ExternalDocumentId;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public int compareTo(SImportedDocument o) {
        return this.toString().compareTo(o.toString());
    }
    
    @Override
    public String toString() {
        return "emisor: " + BizPartner + "; " // allways available
                + "folio: " + getFolio() + "; " // allways available
                + "fecha: " + SLibUtils.DateFormatDate.format(Date) + "; " // allways available
                + "total: $" + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode // allways available
                + (!FunctionalSubArea.isEmpty() ? "; subárea funcional: " + FunctionalSubArea : "") // may not be available
                + (ExternalDocumentId != 0 ? "; ID documento: " + ExternalDocumentId : "") // may not be available
                + "; cargado por: " + (DocumentUploadedBy != null && !DocumentUploadedBy.isEmpty() ? " " + DocumentUploadedBy : "ND") // may not be available
                + "; fecha carga: " + formatDate(DocumentUploadedAt)
                + "; revisado por: " + (DocumentReviewedBy != null && !DocumentReviewedBy.isEmpty() ? " " + DocumentReviewedBy : "ND")
                + "; fecha revisión: " + formatDate(DocumentReviewedAt)
                + "; autorizado por: " + (DocumentAuthorizedBy != null && !DocumentAuthorizedBy.isEmpty() ? " " + DocumentAuthorizedBy : "ND")
                + "; fecha autorización: " + formatDate(DocumentAuthorizedAt)
                + ".";
    }
    
    /*
     * Static methods and classes
     */
    
    /**
     * Get GUI document type for the given DPS type.
     * @param dpsTypeKey DPS type key.
     * @return When DPS type key is supported, either SDataConstantsSys.TRNX_TP_DPS_DOC (invoices) or SDataConstantsSys.TRNX_TP_DPS_ADJ (credit notes), otherwise SLibConstants.UNDEFINED.
     */
    public static int getGuiDocumentType(final int[] dpsTypeKey) {
        int type = SLibConstants.UNDEFINED;
        
        if (SLibUtils.compareKeys(dpsTypeKey, SDataConstantsSys.TRNU_TP_DPS_PUR_INV)) {
            type = SDataConstantsSys.TRNX_TP_DPS_DOC;
        }
        else if (SLibUtils.compareKeys(dpsTypeKey, SDataConstantsSys.TRNU_TP_DPS_PUR_CN)) {
            type = SDataConstantsSys.TRNX_TP_DPS_ADJ;
        }
            
        return type;
    }
    
    /**
     * Create prepared statement to get Processed DPS, by its external ID, from SWAP processed data.
     * @param statement DB statement.
     * @return A prepared statment with these columns:
     * id_swap_data_prc, dps_id_year, dps_id_doc, dps_folio, dps_date, dps_tot_cur, dps_cur_code,
     * rec_id_year, rec_id_per, rec_id_bkc, rec_id_tp_rec, rec_id_num, rec_cob_code,
     * id_usr, usr, id_cfd, and pdf.doc_pdf_name.
     * @throws Exception 
     */
    public static PreparedStatement createPrepStatementToGetProcessedDpsByExternalId(final Statement statement) throws Exception {
        String sql = "SELECT sdp.id_swap_data_prc AS id_swap_data_prc, "
                + "sdp.fk_dps_year_n AS dps_id_year, sdp.fk_dps_doc_n AS dps_id_doc, CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS dps_folio, d.dt AS dps_date, d.tot_cur_r AS dps_tot_cur, c.cur_key AS dps_cur_code, "
                + "r.id_year AS rec_id_year, r.id_per AS rec_id_per, r.id_bkc AS rec_id_bkc, r.id_tp_rec AS rec_id_tp_rec, r.id_num AS rec_id_num, cob.code AS rec_cob_code, "
                + "un.id_usr, un.usr, cfd.id_cfd, pdf.doc_pdf_name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SWAP_DATA_PRC) + " AS sdp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "d.id_year = sdp.fk_dps_year_n AND d.id_doc = sdp.fk_dps_doc_n "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON "
                + "c.id_cur = d.fid_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_REC) + " AS dr ON "
                + "dr.id_dps_year = d.id_year AND dr.id_dps_doc = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON "
                + "r.id_year = dr.fid_rec_year AND r.id_per = dr.fid_rec_per AND r.id_bkc = dr.fid_rec_bkc AND r.id_tp_rec = dr.fid_rec_tp_rec AND r.id_num = dr.fid_rec_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON "
                + "cob.id_bpb = r.fid_cob "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON "
                + "un.id_usr = d.fid_usr_new "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS cfd ON "
                + "cfd.fid_dps_year_n = d.id_year AND cfd.fid_dps_doc_n = d.id_doc "
                + "LEFT OUTER JOIN " + SClientUtils.getComplementaryDbName(statement.getConnection()) + "." + SModConsts.TablesMap.get(SModConsts.TRN_PDF) + " AS pdf ON "
                + "pdf.id_year = d.id_year AND pdf.id_doc = d.id_doc "
                + "WHERE NOT sdp.b_del AND sdp.data_type = ? AND sdp.txn_cat = ? AND sdp.ext_data_id = ?;";
        
        return statement.getConnection().prepareStatement(sql);
    }
    
    /**
     * Create prepared statement to get DPS primary key, by its own document data, from Payable or Receivable Accounts.
     * @param statement DB statement.
     * @param dpsTypeKey Key of DPS type: (category, class & type).
     * @return A prepared statment with these columns:
     * dps_id_year and dps_id_doc.
     * @throws Exception 
     */
    public static PreparedStatement createPrepStatementToGetDpsKeyByDocumentData(final Statement statement, final int[] dpsTypeKey) throws Exception {
        String sql = "SELECT d.id_year AS dps_id_year, d.id_doc AS dps_id_doc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "WHERE NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                + "AND d.fid_ct_dps = " + dpsTypeKey[0] + " AND d.fid_cl_dps = " + dpsTypeKey[1] + " AND d.fid_tp_dps = " + dpsTypeKey[2] + " "
                + "AND d.fid_bp_r = ? AND d.dt = ? AND d.num_ser = ? AND d.num = ? AND d.tot_cur_r = ? AND d.fid_cur = ?;";
        
        return statement.getConnection().prepareStatement(sql);
    }
    
    /**
     * Create prepared statement to get DPS handling data: creation and authorization.
     * @param statement DB statement.
     * @return A prepared statment with these columns:
     * created_by (java.lang.String), created_at (java.sql.Date), authorized_by (java.lang.String) and authorized_at (java.sql.Date).
     * @throws Exception 
     */
    public static PreparedStatement createPrepStatementToGetDpsHandlingData(final Statement statement) throws Exception {
        String sql = "SELECT un.usr AS created_by, d.ts_new AS created_at, ua.usr AS authorized_by, d.ts_authorn AS authorized_at "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON un.id_usr = d.fid_usr_new "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON ua.id_usr = d.fid_usr_authorn "
                + "WHERE NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                + "AND d.id_year = ? AND d.id_doc = ?;";
        
        return statement.getConnection().prepareStatement(sql);
    }
    
    /**
     * Get Processed DPS, by its external ID, from SWAP processed data.
     * @param prepStatement Prepared statement.
     * @param dataType Supported options: SDbSwapDataProcessing.DATA_TYPE_INV and SDbSwapDataProcessing.DATA_TYPE_CN.
     * @param txnCategory Transaction category: 1 = purchase; 2 = sales.
     * @param externalId External ID.
     * @return A Processed DPS if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static SImportedDocument.ProcessedDps getProcessedDpsByExternalId(final PreparedStatement prepStatement, final String dataType, final int txnCategory, final int externalId) throws Exception {
        SImportedDocument.ProcessedDps processedDps = null;
        
        prepStatement.setString(1, dataType);
        prepStatement.setInt(2, txnCategory);
        prepStatement.setInt(3, externalId);
        
        try (ResultSet resultSet = prepStatement.executeQuery()) {
            if (resultSet.next()) {
                processedDps = new SImportedDocument.ProcessedDps(resultSet.getInt("id_swap_data_prc"), resultSet.getInt("dps_id_year"), resultSet.getInt("dps_id_doc"), resultSet.getString("dps_folio"), resultSet.getDate("dps_date"), resultSet.getDouble("dps_tot_cur"), resultSet.getString("dps_cur_code"), 
                        resultSet.getInt("rec_id_year"), resultSet.getInt("rec_id_per"), resultSet.getInt("rec_id_bkc"), resultSet.getString("rec_id_tp_rec"), resultSet.getInt("rec_id_num"), resultSet.getString("rec_cob_code"), 
                        resultSet.getInt("un.id_usr"), resultSet.getString("un.usr"), resultSet.getInt("id_cfd") != 0, resultSet.getString("doc_pdf_name") != null);
            }
        }
        
        return processedDps;
    }
    
    /**
     * Get DPS primary key, by its own document data, from Payable or Receivable Accounts.
     * @param prepStatement Prepared statement.
     * @param bizPartnerId Document's ID of business partner.
     * @param date Document's date.
     * @param numberSeries Document's folio series.
     * @param number Document's folio number.
     * @param total Document's net total.
     * @param currencyId Document's ID of currency.
     * @return A DPS primary key if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static int[] getDpsKeyByDocumentData(final PreparedStatement prepStatement, final int bizPartnerId, final Date date, final String numberSeries, final String number, final double total, final int currencyId) throws Exception {
        int[] dpsKey = null;
        
        prepStatement.setInt(1, bizPartnerId);
        prepStatement.setDate(2, new java.sql.Date(date.getTime()));
        prepStatement.setString(3, numberSeries);
        prepStatement.setString(4, number);
        prepStatement.setDouble(5, total);
        prepStatement.setInt(6, currencyId);
        
        try (ResultSet resultSet = prepStatement.executeQuery()) {
            if (resultSet.next()) {
                dpsKey = new int[] { resultSet.getInt("dps_id_year"), resultSet.getInt("dps_id_doc") };
            }
        }
        
        return dpsKey;
    }
    
    /**
     * Create a minimal version of an imported document from SWAP data processed DPS to be used in SFormDps.
     * @param statement DB statement.
     * @param dataType Data type, supported options: SDbSwapDataProcessing.DATA_TYPE_...
     * @param dpsKey DPS key.
     * @return 
     * @throws java.lang.Exception 
     */
    public static SImportedDocument createImportedDocumentFromProcessedDps(final Statement statement, final String dataType, final int[] dpsKey) throws Exception {
        SImportedDocument importedDocument = null;
        
        String sql = "SELECT sdp.ext_data_id, sdp.ext_data_uuid, sdp.dps_refs, sdp.dps_descrip, "
                + "d.id_year, d.id_doc, d.num_ser, d.num, d.dt, d.tot_cur_r, d.acc_tag, d.fid_func, d.fid_func_sub, d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, "
                + "CONCAT(f.code, '" + SDbFunctionalSubArea.SEPARATOR + "', fs.name) AS _func_sub, "
                + "b.id_bp, b.bp, c.id_cur, c.cur_key, dc.cfd_use, "
                + "sdp.prc_upl_by, sdp.prc_upl_at_n, sdp.prc_rev_by, sdp.prc_rev_at_n, sdp.prc_aut_by, sdp.prc_aut_at_n "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SWAP_DATA_PRC) + " AS sdp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON d.id_year = sdp.fk_dps_year_n AND d.id_doc = sdp.fk_dps_doc_n "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = d.fid_bp_r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = d.fid_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON f.id_func = d.fid_func "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = d.fid_func_sub "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_CFD) + " AS dc ON dc.id_year = d.id_year AND dc.id_doc = d.id_doc "
                + "WHERE NOT sdp.b_del AND sdp.data_type = '" + dataType + "' "
                + "AND sdp.fk_dps_year_n = " + dpsKey[0] + " AND sdp.fk_dps_doc_n = " + dpsKey[1] + ";";

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int[] dpsTypeKey = new int[] { resultSet.getInt("d.fid_ct_dps"), resultSet.getInt("d.fid_cl_dps"), resultSet.getInt("d.fid_tp_dps") };
                importedDocument = new SImportedDocument(null, SImportedDocument.getGuiDocumentType(dpsTypeKey));
                importedDocument.ExternalDocumentId = resultSet.getInt("sdp.ext_data_id");
                importedDocument.ExternalDocumentUuid = resultSet.getString("sdp.ext_data_uuid");
                importedDocument.BizPartnerId = resultSet.getInt("b.id_bp");
                importedDocument.BizPartner = resultSet.getString("b.bp");
                importedDocument.NumberSeries = resultSet.getString("d.num_ser");
                importedDocument.Number = resultSet.getString("d.num");
                importedDocument.Date = resultSet.getDate("d.dt");
                String referencesAsText = resultSet.getString("sdp.dps_refs");
                importedDocument.ReferencesType = referencesAsText.isEmpty() ? 0 : SSwapConsts.TXN_REF_TYPE_ORDER;
                importedDocument.ReferencesAsText = referencesAsText;
                importedDocument.Description = resultSet.getString("sdp.dps_descrip");
                importedDocument.AccountingTag = resultSet.getString("d.acc_tag");
                importedDocument.FunctionalSubAreaId = resultSet.getInt("d.fid_func_sub");
                importedDocument.FunctionalSubArea = resultSet.getString("_func_sub");
                String fiscalUseCode = resultSet.getString("dc.cfd_use"); // can be null
                importedDocument.FiscalUseCode = resultSet.wasNull() ? "" : fiscalUseCode;
                importedDocument.Total = resultSet.getDouble("d.tot_cur_r");
                importedDocument.CurrencyId = resultSet.getInt("c.id_cur");
                importedDocument.CurrencyCode = resultSet.getString("c.cur_key");
                importedDocument.RequiredPaymentDefinition = SSwapConsts.PAY_NOT_REQ;
                importedDocument.RequiredPaymentPct = 0;
                importedDocument.RequiredPaymentAmount = 0;
                importedDocument.RequiredPaymentAmountNew = 0;
                importedDocument.RequiredPaymentDate = null;
                importedDocument.RequiredPaymentDateNew = null;
                importedDocument.IsRequiredPaymentLoc = false;
                importedDocument.RequiredPaymentNotes = "";
                importedDocument.RevisionYear = 0;
                importedDocument.RevisionWeek = 0;
                importedDocument.RevisionDatetime = null;
                importedDocument.ProcessingTypeId = 0;
                importedDocument.ProcessingTypeCode = "";
                importedDocument.StatusId = 0;
                importedDocument.Status = "";
                importedDocument.Download = false;
                importedDocument.AlreadyDownloaded = false;

                importedDocument.DocumentUploadedBy = resultSet.getString("sdp.prc_upl_by");
                importedDocument.DocumentUploadedAt = resultSet.getTimestamp("sdp.prc_upl_at_n");
                importedDocument.DocumentReviewedBy = resultSet.getString("sdp.prc_rev_by");
                importedDocument.DocumentReviewedAt = resultSet.getTimestamp("sdp.prc_rev_at_n");
                importedDocument.DocumentAuthorizedBy = resultSet.getString("sdp.prc_aut_by");
                importedDocument.DocumentAuthorizedAt = resultSet.getTimestamp("sdp.prc_aut_at_n");

                importedDocument.ProcessedDps = null;
                importedDocument.SwapDataProcessing = null;
                importedDocument.Payment = null;

                importedDocument.References = null;
            }
        }
        
        return importedDocument;
    }
    
    /**
     * In-memory minimal DPS.
     */
    public static class MinimalDps {
        
        public int BizPartnerId;
        public Date Date;
        public String NumberSeries;
        public String Number;
        public double Total;
        public int CurrencyId;
        
        public MinimalDps(final int bizPartnerId, final Date date, final String numberSeries, final String number, final double total, final int currencyId) {
            BizPartnerId = bizPartnerId;
            Date = date;
            NumberSeries = numberSeries;
            Number = number;
            Total = total;
            CurrencyId = currencyId;
        }
    }
    
    /**
     * In-memory Processed DPS.
     */
    public static class ProcessedDps implements Serializable {
        
        public int SwapDataProcessingId;
        public int DpsYearId;
        public int DpsDocId;
        public String DpsFolio;
        public Date DpsDate;
        public double DpsTotalCy;
        public String DpsCurrencyCode;
        public int RecYearId;
        public int RecPeriodId;
        public int RecBookkeepingCenterId;
        public String RecRecordTypeId;
        public int RecNumberId;
        public String RecCompanyBranchCode;
        public int UserNewId;
        public String UserNew;
        public boolean HasCfd;
        public boolean HasPdf;
        
        public ProcessedDps(final int swapDataProcessingId, final int dpsYearId, final int dpsDocId, final String dpsFolio, final Date dpsDate, final double dpsTotalCy, final String dpsCurrencyCode, 
                final int recYearId, final int recPeriodId, final int recBookkeepingCenterId, final String recRecordTypeId, final int recNumberId, final String recCompanyBranchCode, 
                final int userNewId, final String userNew, final boolean hasCfd, final boolean hasPdf) {
            SwapDataProcessingId = swapDataProcessingId;
            DpsYearId = dpsYearId;
            DpsDocId = dpsDocId;
            DpsFolio = dpsFolio;
            DpsDate = dpsDate;
            DpsTotalCy = dpsTotalCy;
            DpsCurrencyCode = dpsCurrencyCode;
            RecYearId = recYearId;
            RecPeriodId = recPeriodId;
            RecBookkeepingCenterId = recBookkeepingCenterId;
            RecRecordTypeId = recRecordTypeId;
            RecNumberId = recNumberId;
            RecCompanyBranchCode = recCompanyBranchCode;
            UserNewId = userNewId;
            UserNew = userNew;
            HasCfd = hasCfd;
            HasPdf = hasPdf;
        }
        
        public int[] getDpsKey() {
            int[] key = null;
            
            if (DpsYearId != 0 && DpsDocId != 0) {
                key = new int[] { DpsYearId, DpsDocId };
            }
            
            return key;
        }
        
        public Object[] getRecordKey() {
            Object[] key = null;
            
            if (RecYearId != 0 && RecPeriodId != 0 && RecBookkeepingCenterId != 0 && !RecRecordTypeId.isEmpty() && RecNumberId != 0) {
                key = new Object[] { RecYearId, RecPeriodId, RecBookkeepingCenterId, RecRecordTypeId, RecNumberId };
            }
            
            return key;
        }
        
        public String composeRecord() {
            return RecYearId + "-" +
                    RecPeriodFormat.format(RecPeriodId) + " " +
                    RecCompanyBranchCode + " " +
                    RecRecordTypeId + "-" +
                    RecNumberFormat.format(RecNumberId);
        }
    }
    
    /**
     * In-memory reference.
     */
    public static class Reference implements Serializable {
        
        public int ReferenceType;
        public String Reference;
        public int DpsYearId;
        public int DpsDocId;
        public int DocExternalId;
        
        public String ReferenceCreatedBy;
        public Date ReferenceCreatedAt;
        public String ReferenceAuthorizedBy;
        public Date ReferenceAuthorizedAt;
        
        /**
         * Create a new reference.
         * @param referenceType Reference type, supported options in SSwapConsts.TXN_REF_TYPE_...
         * @param reference Reference.
         * @param dpsKey DPS primary key of the reference.
         * @param prepStatementToGetDpsHandlingData Prepared statement to get DPS handling data.
         */
        public Reference(final int referenceType, final String reference, final int[] dpsKey,
                final PreparedStatement prepStatementToGetDpsHandlingData) {
            this(referenceType, reference, dpsKey, 0, null, null, null, prepStatementToGetDpsHandlingData);
        }
        
        /**
         * Create a new reference.
         * @param referenceType Reference type, supported options in SSwapConsts.TXN_REF_TYPE_...
         * @param reference Reference.
         * @param docExternalId Document external ID (that of SWAP Services).
         * @param minimalDps Minimal DPS data, as a second option to search for DPS.
         * @param prepStatementToGetProcessedDpsByExternalId Prepared statement to get Processed DPS, by its external ID, from SWAP processed data.
         * @param prepStatementToGetDpsKeyByDocumentData Prepared statement to get DPS primary key, by its own document data, from Payable or Receivable Accounts.
         * @param prepStatementToGetDpsHandlingData Prepared statement to get DPS handling data: creation and authorization.
         */
        public Reference(final int referenceType, final String reference, final int docExternalId, SImportedDocument.MinimalDps minimalDps,
                final PreparedStatement prepStatementToGetProcessedDpsByExternalId, final PreparedStatement prepStatementToGetDpsKeyByDocumentData, final PreparedStatement prepStatementToGetDpsHandlingData) {
            this(referenceType, reference, null, docExternalId, minimalDps, prepStatementToGetProcessedDpsByExternalId, prepStatementToGetDpsKeyByDocumentData, prepStatementToGetDpsHandlingData);
        }
        
        /**
         * Create a new reference.
         * @param referenceType Reference type, supported options in SSwapConsts.TXN_REF_TYPE_...
         * @param reference Reference.
         * @param dpsKey DPS primary key of the reference.
         * @param docExternalId Document external ID (that of SWAP Services).
         * @param minimalDps Minimal DPS data, as a second option to search for DPS.
         * @param prepStatementToGetProcessedDpsByExternalId Prepared statement to get Processed DPS, by its external ID, from SWAP processed data.
         * @param prepStatementToGetDpsKeyByDocumentData Prepared statement to get DPS primary key, by its own document data, from Payable or Receivable Accounts.
         * @param prepStatementToGetDpsHandlingData Prepared statement to get DPS handling data: creation and authorization.
         */
        private Reference(final int referenceType, final String reference, final int[] dpsKey, final int docExternalId, SImportedDocument.MinimalDps minimalDps,
                final PreparedStatement prepStatementToGetProcessedDpsByExternalId, final PreparedStatement prepStatementToGetDpsKeyByDocumentData, final PreparedStatement prepStatementToGetDpsHandlingData) {
            ReferenceType = referenceType;
            Reference = reference;
            
            if (dpsKey != null) {
                DpsYearId = dpsKey[0];
                DpsDocId = dpsKey[1];
                DocExternalId = 0;
            }
            else {
                DpsYearId = 0;
                DpsDocId = 0;
                DocExternalId = docExternalId;
            }
            
            ReferenceCreatedBy = "";
            ReferenceCreatedAt = null;
            ReferenceAuthorizedBy = "";
            ReferenceAuthorizedAt = null;
            
            boolean retrieveHandlingData = false;
            
            if (ReferenceType == SSwapConsts.TXN_REF_TYPE_INVOICE && DocExternalId != 0 && prepStatementToGetProcessedDpsByExternalId != null && prepStatementToGetDpsKeyByDocumentData != null && prepStatementToGetDpsHandlingData != null) {
                try {
                    // first attemt to get DPS key: from its external ID:
                    
                    SImportedDocument.ProcessedDps processedDps = SImportedDocument.getProcessedDpsByExternalId(prepStatementToGetProcessedDpsByExternalId, SDbSwapDataProcessing.DATA_TYPE_INV, SDataConstantsSys.TRNS_CT_DPS_PUR, DocExternalId);
                    
                    if (processedDps != null) {
                        DpsYearId = processedDps.DpsYearId;
                        DpsDocId = processedDps.DpsDocId;
                        retrieveHandlingData = true;
                    }
                    else if (minimalDps != null) {
                        // second attemt to get DPS key: from its external ID:
                    
                        int[] key = SImportedDocument.getDpsKeyByDocumentData(prepStatementToGetDpsKeyByDocumentData, minimalDps.BizPartnerId, minimalDps.Date, minimalDps.NumberSeries, minimalDps.Number, minimalDps.Total, minimalDps.CurrencyId);
                        
                        if (key != null) {
                            DpsYearId = key[0];
                            DpsDocId = key[1];
                            retrieveHandlingData = true;
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
            }
            
            if ((retrieveHandlingData || ReferenceType == SSwapConsts.TXN_REF_TYPE_ORDER) && DpsYearId != 0 && DpsDocId != 0 && prepStatementToGetDpsHandlingData != null) {
                try {
                    prepStatementToGetDpsHandlingData.setInt(1, DpsYearId);
                    prepStatementToGetDpsHandlingData.setInt(2, DpsDocId);

                    try (ResultSet resultSet = prepStatementToGetDpsHandlingData.executeQuery()) {
                        if (resultSet.next()) {
                            ReferenceCreatedBy = resultSet.getString("created_by");
                            ReferenceCreatedAt = resultSet.getTimestamp("created_at");
                            ReferenceAuthorizedBy = resultSet.getString("authorized_by");
                            ReferenceAuthorizedAt = resultSet.getTimestamp("authorized_at");
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
            }
        }
        
        public SImportUtils.DpsKey createDpsKey() {
            return DpsYearId != 0 && DpsDocId != 0 ? new SImportUtils.DpsKey(DpsYearId, DpsDocId) : null;
        }
    }
}
