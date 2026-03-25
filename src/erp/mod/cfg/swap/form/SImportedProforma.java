/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver40.DCfdi40Catalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.fin.db.SDbPaymentFile;
import erp.mod.trn.db.SDbSwapDataProcessing;
import java.io.File;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDatePicker;
import sa.lib.gui.SGuiSession;

/**
 * In memory document received from SWAP Services.
 * @author Cesar Orozco
 */
public class SImportedProforma implements SGridRow, Serializable, Comparable<SImportedProforma> {
    
    public static final int DOC_TYPE_ALL = 0;
    public static final int DOC_TYPE_ASSETS = 1;
    public static final int DOC_TYPE_EXPENSES = 2;
    
    public static final int DOC_CASE_ALL = 0;
    public static final int DOC_CASE_STANDARD = 1;
    public static final int DOC_CASE_FRUIT_FREIGHT = 2;
    public static final int DOC_CASE_FRUIT_PURCHASE = 3;
    
    public static final int PROC_TYPE_STANDARD = 0; // NA
    public static final int PROC_TYPE_FRUIT_FREIGHT = 11;
    public static final int PROC_TYPE_FRUIT_PURCHASE = 12;
    
    public static final int PAY_NOT_REQ = 0; // pago no requerido
    public static final int PAY_DEF_BY_AMT = 1; // pago definido por monto
    public static final int PAY_DEF_BY_PCT = 2; // pago definido por porcentaje
    
    public static final int MATCH_PAY_TP_OMIT = 0; // coincidencia de tipo de pago: omitir
    public static final int MATCH_PAY_TP_REQUIRED = 1; // coincidencia de tipo de pago: requerida
    public static final int MATCH_PAY_TP_CONFIRM_ON_FAIL = 2; // coincidencia de tipo de pago: confirmar cuando no corresponda
    
    /** Document types. */
    public static final HashMap<Integer, String> DocTypes = new HashMap<>();
    /** Document cases. */
    public static final HashMap<Integer, String> DocCases = new HashMap<>();
    /** Processing types (in SWAP Services). */
    public static final HashMap<Integer, String> ProcTypes = new HashMap<>();
    /** Payment definition options. */
    public static final HashMap<Integer, String> PayDefinitions = new HashMap<>();
    
    static {
        PayDefinitions.put(PAY_NOT_REQ, "No requerido");
        PayDefinitions.put(PAY_DEF_BY_AMT, "Por monto ($)");
        PayDefinitions.put(PAY_DEF_BY_PCT, "Por porcentaje (%)");
    }
    
    // Exception messages:
    
    public static final String EXC_DOC_NOT_RECORDED = "Este documento no ha sido procesado, no tiene factura " + SSwapConsts.SIIE + ".";

    public static final String EXC_PAY_NOT_REQUESTABLE = "Este documento no tiene información para solicitar su pago.";
    public static final String EXC_PAY_NOT_REQUESTED = "Este documento no tiene solicitud de pago.";
    public static final String EXC_PAY_ALREADY_REQUESTED_IN_ = "Este documento ya tiene solicitud de pago: ";
    
    public int ExternalDocumentId;
    public String ExternalDocumentUuid;
    public int BizPartnerId;
    public String BizPartner;
    public String NumberSeries;
    public String Number;
    public Date Date;
    public Date DueDate;
    public int ReferencesType;
    public String ReferencesAsText;
    public String Description;
    public int FunctionalSubAreaId;
    public String FunctionalSubArea;
    public String FiscalUseCode;
    public double Total;
    public int CurrencyId;
    public String CurrencyCode;
    public int RequiredPaymentDefinition;
    public double RequiredPaymentAmount;
    public double RequiredPaymentPct;
    public Date RequiredPaymentDate;
    public Date RequiredPaymentDateNew;
    public boolean IsRequiredPaymentLoc;
    public String RequiredPaymentNotes;
    public Date RevisionDatetime;
    public int ProcessingTypeId;
    public String ProcessingTypeCode;
    public int StatusId;
    public String Status;
    public boolean Download;
    public boolean AlreadyDownloaded;
    
    public ProcessedProforma ProcessedProforma;
    public SDbSwapDataProcessing SwapDataProcessing;
    public SDbPayment Payment;
    
    public Reference[] References;
    
    public SImportedProforma() {
        ExternalDocumentId = 0;
        ExternalDocumentUuid = "";
        BizPartnerId = 0;
        BizPartner = "";
        NumberSeries = "";
        Number = "";
        Date = null;
        DueDate = null;
        ReferencesType = 0;
        ReferencesAsText = "";
        Description = "";
        FunctionalSubAreaId = 0;
        FunctionalSubArea = "";
        FiscalUseCode = "";
        Total = 0;
        CurrencyId = 0;
        CurrencyCode = "";
        RequiredPaymentDefinition = PAY_NOT_REQ;
        RequiredPaymentAmount = 0;
        RequiredPaymentPct = 0;
        RequiredPaymentDate = null;
        RequiredPaymentDateNew = null;
        IsRequiredPaymentLoc = false;
        RequiredPaymentNotes = "";
        RevisionDatetime = null;
        ProcessingTypeId = 0;
        ProcessingTypeCode = "";
        StatusId = 0;
        Status = "";
        Download = false;
        AlreadyDownloaded = false;
        
        ProcessedProforma = null;
        SwapDataProcessing = null;
        Payment = null;

        References = null;
    }
    
    /**
     * Get folio of document in format series-number.
     * @return 
     */
    public String getFolio() {
        return SDocumentUtils.composeFolio(NumberSeries, Number, ExternalDocumentUuid);
    }

    /**
     * Pick a date.
     * @param session GUI session.
     * @return 
     */
    private Date pickDate(final SGuiSession session) {
        Date date = null;
        
        SGuiDatePicker picker = session.getClient().getDatePicker();
        picker.resetPicker();
        picker.setOption(getRequiredPaymentDateEffective());
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            date = picker.getOption();
        }
        
        return date;
    }
    
    /**
     * Get effective total, from this imported document.
     * @return 
     */
    private double getTotalEffective() {
        return Total;
    }
    
    /**
     * Ger required payment percentage of document as a double between 0 and 1.
     * @return 
     */
    private double getRequiredPaymentPct() {
        return RequiredPaymentPct / 100;
    }
    
    /**
     * Ger required payment amount of document, directly defined or indirectly from required payment percentage.
     * @return 
     */
    private double getRequiredPaymentAmount() {
        double amount = 0;
        
        switch (RequiredPaymentDefinition) {
            case PAY_NOT_REQ:
                break;
            case PAY_DEF_BY_AMT:
                amount = RequiredPaymentAmount;
                break;
            case PAY_DEF_BY_PCT:
                amount = getTotalEffective() * getRequiredPaymentPct();
                break;
            default:
                // nothing
        }
        
        return amount;
    }

    /**
     * Get effective required payment date.
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
        return ProcessedProforma != null && ProcessedProforma.getDpsKey() != 0 && ProcessedProforma.SwapDataProcessingId != 0 && SwapDataProcessing != null;
    }
    
    /**
     * Check if payment is already requested.
     * @return 
     */
    public boolean isPaymentRequested() {
        return Payment != null;
    }
    
    /**
     * Check if payment is requestable.
     * @return 
     */
    public boolean isPaymentRequestDataAvailable() {
        return getRequiredPaymentDateEffective() != null && (getRequiredPaymentAmount() > 0 || (getRequiredPaymentPct() > 0 && getRequiredPaymentPct() <= 1));
    }
    
    /**
     * Check if document has references and if they are of the given reference document type.
     * @param refDocType Reference document type (SSwapConsts.TXN_DOC_TYPE_...).
     * @return 
     */
    public boolean hasReferences(final int refDocType) {
        return References != null && References.length > 0 && ReferencesType == refDocType;
    }
    
    /**
     * Get key of first reference if it matches the given document type.
     * @param client GUI client.
     * @param refDocType Reference document type (SSwapConsts.TXN_DOC_TYPE_...).
     * @return 
     * @throws java.lang.Exception 
     */
    public int[] getFirstReferenceKey(final SGuiClient client, final int refDocType) throws Exception {
        int[] orderKey = null;
        String refPrefix = "";
        
        switch (refDocType) {
            case SSwapConsts.TXN_REF_TYPE_ORDER:
                refPrefix = SSwapConsts.TXN_REF_TYPE_ORDER_CODE;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Tipo no soportado de documento de la referencia: " + refDocType + ".)");
        }
        
        if (hasReferences(refDocType)) {
            SImportUtils.DpsKey orderDpsKey = References[0].createDpsKey();

            if (orderDpsKey != null) {
                orderKey = orderDpsKey.asKey();
            }
            else {
                SImportUtils.DpsFolio orderDpsFolio = SImportUtils.createDpsFolio(References[0].Reference, refPrefix);

                if (orderDpsFolio != null) {
                    orderKey = SDataUtilities.obtainDpsKey((SClientInterface) client, orderDpsFolio.Series, orderDpsFolio.Number, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD);
                }
            }
        }
        
        return orderKey;
    }
    
    /**
     * Create and save payment request.
     * @param miClient GUI Client.
     * @param urlDownload url to download files of payment.
     * @return
     * @throws Exception 
     */
    private SDbPayment createAndSavePaymentRequest(final SGuiClient miClient, final String urlDownload) throws Exception {
        SDbPayment payment = null;
        SGuiSession session = miClient.getSession();
        SDbFunctionalSubArea functionalSubArea = (SDbFunctionalSubArea) session.readRegistry(SModConsts.CFGU_FUNC_SUB, new int[] { FunctionalSubAreaId });
        if (RequiredPaymentDefinition == PAY_NOT_REQ) {
            throw new Exception("Este documento carece de la indicación de requerir un pago.");
        }
        else if (getRequiredPaymentDateEffective() == null) {
            throw new Exception("Este documento no tiene fecha requerida de pago.");
        }
        else if (getRequiredPaymentAmount() == 0) {
            throw new Exception("Este documento no tiene monto requerido de pago.");
        }
        else if (getRequiredPaymentPct() == 0) {
            throw new Exception("Este documento no tiene porcentaje requerido de pago.");
        }
        else if (getRequiredPaymentPct() > 1) {
            throw new Exception("Este documento tiene un porcentaje requerido de pago mayor a 100%.");
        }
        
        // create and save payment request:
         File[] externalFiles = null;

        if (ExternalDocumentId != 0) {
            externalFiles = SImportUtils.downloadDocumentAllFilesInTempDir(session, urlDownload, ExternalDocumentId, SSwapConsts.TXN_DOC_TYPE_PROFORMA);

            System.out.println("Files downloaded: " + (externalFiles != null ? externalFiles.length : 0));

            if (externalFiles != null) {
                for (File f : externalFiles) {
                    System.out.println("Downloaded file: " + f.getAbsolutePath());
                }
            }

            AlreadyDownloaded = true;
        }

        Date date = session.getCurrentDate();

        // throw exception if exchange rate is not available:
        double exchangeRate = SDocumentUtils.getExchangeRate(session, CurrencyId, date);

        // create & prepare payment and its single one payment entry:
        
        payment = new SDbPayment();

        SDbPaymentEntry singleEntry = new SDbPaymentEntry();
        payment.getChildEntries().add(singleEntry);

        payment.processPaymentAtApplication(session, getRequiredPaymentAmount(), CurrencyId, exchangeRate, IsRequiredPaymentLoc, 1, getTotalEffective());

        //payment.setPkPaymentId(...);
        payment.setPaymentType(SDbPayment.TYPE_REQUEST);
        payment.setSeries("");
        payment.setNumber(0);
        payment.setDateApplication(date);
        payment.setDateRequired(getRequiredPaymentDateEffective());
        payment.setDateSchedule_n(null);
        payment.setDateExecution_n(null);
        // ...
        payment.setPaymentWay(DCfdi40Catalogs.FDP_POR_DEF);
        payment.setPriority(SDbPayment.PRIORITY_NORMAL);
        payment.setNotes(!RequiredPaymentNotes.isEmpty() ? RequiredPaymentNotes : "-"); // "-" means no comments
        payment.setNotesAuthorization(!Description.isEmpty() ? Description : "-"); // "-" means no comments
        payment.setNotesAuthorizationFlow("");
        payment.setReceiptPaymentRequired(false);
        payment.setRescheduled(false);
        payment.setExecutedManually(false);
        payment.setDeleted(false);
        payment.setSystem(true);
        payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_NEW);
        //payment.setFkCurrencyId(...); // set in SDbPayment.processPaymentCy()
        payment.setFkBeneficiaryId(BizPartnerId);
        payment.setFkFunctionalAreaId(functionalSubArea.getFkFunctionalAreaId());
        payment.setFkFunctionalSubareaId(FunctionalSubAreaId);
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
        singleEntry.setEntryType(SDbPaymentEntry.TYPE_ADVANCE);
        //...
        //paymentEntry.setFkEntryCurrencyId(...); // set in SDbPayment.processPaymentCy()
        singleEntry.setFkPaymentRequestId_n(0);

        Exception exception = null;

        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            try {
                statement.execute("START TRANSACTION");
                payment.save(session);
                
                if (externalFiles != null && externalFiles.length > 0) {

                    int sorting = 1;

                    for (File file : externalFiles) {

                        SDbPaymentFile paymentFile = new SDbPaymentFile();

                        paymentFile.setRegistryNew(true);
                        paymentFile.setPkPaymentId(payment.getPkPaymentId());
                        paymentFile.setPaymentFileType(SDbPaymentFile.FILE_TP_OS);
                        paymentFile.setFileDescription("Archivo importado automáticamente");
                        paymentFile.setSortingPos(sorting++);
                        paymentFile.setAuxFile(file);

                        paymentFile.save(session);
                        payment.getFiles().add(paymentFile);
                    }
                    
                }
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
        
        Object[] sendRequest = SAuthorizationUtils.sendPaymentFilesToCloud(miClient, payment);
                    boolean sendPaymentFilesOk = (boolean) sendRequest[0];
                    if (sendPaymentFilesOk != true){
                        throw exception;
                    }
        return payment;
    }
    
    private SDbPayment createAndSavePaymentRequest(final SGuiSession session) throws Exception {
        SDbPayment payment = null;
        
        if (RequiredPaymentDefinition == PAY_NOT_REQ) {
            throw new Exception("Este documento carece de la indicación de requerir un pago.");
        }
        else if (getRequiredPaymentDateEffective() == null) {
            throw new Exception("Este documento no tiene fecha requerida de pago.");
        }
        else if (getRequiredPaymentAmount() == 0) {
            throw new Exception("Este documento no tiene monto requerido de pago.");
        }
        else if (getRequiredPaymentPct() == 0) {
            throw new Exception("Este documento no tiene porcentaje requerido de pago.");
        }
        else if (getRequiredPaymentPct() > 1) {
            throw new Exception("Este documento tiene un porcentaje requerido de pago mayor a 100%.");
        }
        

        Date date = session.getCurrentDate();

        // throw exception if exchange rate is not available:
        double exchangeRate = SDocumentUtils.getExchangeRate(session, CurrencyId, date);

        // create & prepare payment and its single one payment entry:

        payment = new SDbPayment();

        SDbPaymentEntry singleEntry = new SDbPaymentEntry();
        payment.getChildEntries().add(singleEntry);

        payment.processPaymentAtApplication(session, getRequiredPaymentAmount(), CurrencyId, exchangeRate, IsRequiredPaymentLoc, 1, getTotalEffective());

        //payment.setPkPaymentId(...);
        payment.setPaymentType(SDbPayment.TYPE_REQUEST);
        payment.setSeries("");
        payment.setNumber(0);
        payment.setDateApplication(date);
        payment.setDateRequired(getRequiredPaymentDateEffective());
        payment.setDateSchedule_n(null);
        payment.setDateExecution_n(null);
        // ...
        payment.setPaymentWay(DCfdi40Catalogs.FDP_POR_DEF);
        payment.setPriority(SDbPayment.PRIORITY_NORMAL);
        payment.setNotes(!RequiredPaymentNotes.isEmpty() ? RequiredPaymentNotes : "-"); // "-" means no comments
        payment.setNotesAuthorization(!Description.isEmpty() ? Description : "-"); // "-" means no comments
        payment.setNotesAuthorizationFlow("");
        payment.setReceiptPaymentRequired(false);
        payment.setRescheduled(false);
        payment.setExecutedManually(false);
        payment.setDeleted(false);
        payment.setSystem(true);
        payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_NEW);
        //payment.setFkCurrencyId(...); // set in SDbPayment.processPaymentCy()
        payment.setFkBeneficiaryId(BizPartnerId);
        payment.setFkFunctionalAreaId(1);
        payment.setFkFunctionalSubareaId(FunctionalSubAreaId);
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
        singleEntry.setEntryType(SDbPaymentEntry.TYPE_ADVANCE);
        //...
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
        
        
        return payment;
    }
    
    /**
     * Retrieve document existing provessiog.
     * @param session GUI session.
     * @param preparedStatement Prepared statement.
     * @param dataType Constants DATA_TYPE...: INV = invoice; DB = debit note; CN = credit note.
     * @param txnCategory Transaction category: 1 = purchase; 2 = sales.
     * @param externalId External ID.
     * @throws Exception 
     */
    public void retrieveProcessing(final SGuiSession session, final PreparedStatement preparedStatement, final String dataType, final int txnCategory, final int externalId) throws Exception {
        ProcessedProforma = SImportedProforma.getProcessedProformaByExternalId(preparedStatement, dataType, txnCategory, externalId);

        if (ProcessedProforma != null) {
            SwapDataProcessing = (SDbSwapDataProcessing) session.readRegistry(SModConsts.TRN_SWAP_DATA_PRC, new int[] { ProcessedProforma.SwapDataProcessingId });

            if (SwapDataProcessing.getFkPaymentId_n() != 0) {
                Payment = (SDbPayment) session.readRegistry(SModConsts.FIN_PAY, new int[] { SwapDataProcessing.getFkPaymentId_n() });
            }
        }
    }
    
    /**
     * Validate if a new payment request can be created.
     * @return
     * @throws Exception 
     */
    private boolean validatePaymentRequestCreation() throws Exception {
        if (isPaymentRequested()) {
            throw new Exception(EXC_PAY_ALREADY_REQUESTED_IN_ + Payment.getFolio() + ", " + SLibUtils.DateFormatDate.format(Payment.getDateApplication()) + ".");
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
        if (validatePaymentRequestCreation()) {
            
            SDbPayment payment = createAndSavePaymentRequest(session);
            SDbSwapDataProcessing swapDataProcessing = new SDbSwapDataProcessing();

                //swapDataProcessing.setPkSwapDataProcessingId(...);
            swapDataProcessing.setDataType(SDbSwapDataProcessing.DATA_TYPE_PRF);
            swapDataProcessing.setTransactionCategory(SDataConstantsSys.TRNS_CT_DPS_PUR);
            swapDataProcessing.setExternalDataId(ExternalDocumentId);
            swapDataProcessing.setExternalDataUuid(ExternalDocumentUuid);
            swapDataProcessing.setExternalDataAuthorizationHistory("");
            //swapDataProcessing.setDpsReferences(composeReferences());
            swapDataProcessing.setDpsDescription(Description);
            swapDataProcessing.setDpsPaymentLocal(IsRequiredPaymentLoc);
            swapDataProcessing.setProcessingType(SDbSwapDataProcessing.PROC_TYPE_STANDARD);
            swapDataProcessing.setPaymentRequired(false);
            swapDataProcessing.setPaymentApplicationCy(0);
            swapDataProcessing.setPaymentDateRequired_n(null);
            swapDataProcessing.setAccMethod(SDbSwapDataProcessing.ACC_METHOD_MANUAL);
            swapDataProcessing.setAccUserUnits(0);
            swapDataProcessing.setAccSystemUnits(0);
            swapDataProcessing.setDeleted(false);
            swapDataProcessing.setSystem(false);
            swapDataProcessing.setFkPaymentId_n(payment.getPkPaymentId());
            //swapDataProcessing.setFkPayCurrencyId_n(...);
            //swapDataProcessing.setFkAccUserAccountId_n(...);
            //swapDataProcessing.setFkAccUserCostCenterId_n(...);
            //swapDataProcessing.setFkAccUserItemId_n(...);
            //swapDataProcessing.setFkAccUserItemAuxId_n(...);
            //swapDataProcessing.setFkAccUserUnitId_n(...);
            //swapDataProcessing.setFkAccSystemAccountId_n(...);
            //swapDataProcessing.setFkAccSystemCostCenterId_n(...);
            //swapDataProcessing.setFkAccSystemItemId_n(...);
            //swapDataProcessing.setFkAccSystemItemAuxId_n(...);
            //swapDataProcessing.setFkAccSystemUnitId_n(...);
            //swapDataProcessing.setFkUserInsertId(...);
            //swapDataProcessing.setFkUserUpdateId(...);
            //swapDataProcessing.setTsUserInsert(...);
            //swapDataProcessing.setTsUserUpdate(...);

            swapDataProcessing.save(session);
            SwapDataProcessing = swapDataProcessing;
            Payment = payment;
            
            
            requested = true;
        }
        
        return requested;
    }
    
    public boolean requestPayment(final SGuiClient miClient, final String urlDownload) throws Exception {
        boolean requested = false;
        if (validatePaymentRequestCreation()) {
            
            SDbPayment payment = createAndSavePaymentRequest(miClient, urlDownload);
            SDbSwapDataProcessing swapDataProcessing = new SDbSwapDataProcessing();

                //swapDataProcessing.setPkSwapDataProcessingId(...);
            swapDataProcessing.setDataType(SDbSwapDataProcessing.DATA_TYPE_PRF);
            swapDataProcessing.setTransactionCategory(SDataConstantsSys.TRNS_CT_DPS_PUR);
            swapDataProcessing.setExternalDataId(ExternalDocumentId);
            swapDataProcessing.setExternalDataUuid(ExternalDocumentUuid);
            swapDataProcessing.setExternalDataAuthorizationHistory("");
            //swapDataProcessing.setDpsReferences(composeReferences());
            swapDataProcessing.setDpsDescription(Description);
            swapDataProcessing.setDpsPaymentLocal(IsRequiredPaymentLoc);
            swapDataProcessing.setProcessingType(SDbSwapDataProcessing.PROC_TYPE_STANDARD);
            swapDataProcessing.setPaymentRequired(false);
            swapDataProcessing.setPaymentApplicationCy(0);
            swapDataProcessing.setPaymentDateRequired_n(null);
            swapDataProcessing.setAccMethod(SDbSwapDataProcessing.ACC_METHOD_MANUAL);
            swapDataProcessing.setAccUserUnits(0);
            swapDataProcessing.setAccSystemUnits(0);
            swapDataProcessing.setDeleted(false);
            swapDataProcessing.setSystem(false);
            swapDataProcessing.setFkPaymentId_n(payment.getPkPaymentId());
            //swapDataProcessing.setFkPayCurrencyId_n(...);
            //swapDataProcessing.setFkAccUserAccountId_n(...);
            //swapDataProcessing.setFkAccUserCostCenterId_n(...);
            //swapDataProcessing.setFkAccUserItemId_n(...);
            //swapDataProcessing.setFkAccUserItemAuxId_n(...);
            //swapDataProcessing.setFkAccUserUnitId_n(...);
            //swapDataProcessing.setFkAccSystemAccountId_n(...);
            //swapDataProcessing.setFkAccSystemCostCenterId_n(...);
            //swapDataProcessing.setFkAccSystemItemId_n(...);
            //swapDataProcessing.setFkAccSystemItemAuxId_n(...);
            //swapDataProcessing.setFkAccSystemUnitId_n(...);
            //swapDataProcessing.setFkUserInsertId(...);
            //swapDataProcessing.setFkUserUpdateId(...);
            //swapDataProcessing.setTsUserInsert(...);
            //swapDataProcessing.setTsUserUpdate(...);

            swapDataProcessing.save(miClient.getSession());
            SwapDataProcessing = swapDataProcessing;
            Payment = payment;
            
            
            requested = true;
        }
        
        return requested;
    }
    
    /**
     * Validate if a new payment request can be created.
     * @return
     * @throws Exception 
     */
    private boolean validateRequiredPaymentDateChanging() throws Exception {
        if (!isRecorded()) {
            throw new Exception(EXC_DOC_NOT_RECORDED);
        }
        else if (!isPaymentRequested()) {
            throw new Exception(EXC_PAY_NOT_REQUESTED);
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
        Date dateNew = null;
        
        if (!isPaymentRequested()) {
            // payment request not yet created:
            if (!isPaymentRequestDataAvailable()) {
                throw new Exception(EXC_PAY_NOT_REQUESTABLE);
            }
        }
        else {
            // payment request already created:
            validateRequiredPaymentDateChanging(); // thorws exception on validation failure
        }

        dateNew = pickDate(session);

        if (dateNew != null) {
            dateNew = SLibTimeUtils.convertToDateOnly(dateNew);
            
            String message = "La nueva fecha requerida de pago, " + SLibUtils.DateFormatDate.format(dateNew) + ", no puede ser anterior ";
            
            if (dateNew.before(SLibTimeUtils.convertToDateOnly(session.getSystemDate()))) {
                throw new Exception(message + "al día de hoy, " + SLibUtils.DateFormatDate.format(session.getSystemDate()) + ".");
            }
            else if (dateNew.before(SLibTimeUtils.convertToDateOnly(Date))) {
                throw new Exception(message + "a la fecha del documento '" + getFolio() + "', " + SLibUtils.DateFormatDate.format(Date) + ".");
            }
            
            if (isPaymentRequested()) {
                // update the new required date according to current status of payment:
                
                switch (Payment.getFkStatusPaymentId()) {
                    case SModSysConsts.FINS_ST_PAY_NEW:
                        Payment.setDateRequired(dateNew);
                        break;
                        
                    case SModSysConsts.FINS_ST_PAY_SCHED:
                        Payment.setDateSchedule_n(dateNew);
                        Payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_SCHED_P);
                        Payment.setFkUserScheduleId(session.getUser().getPkUserId());
                        Payment.setTsUserSchedule(new Date());
                        break;
                        
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(ID no soportado de estatus de pago: " + Payment.getFkStatusPaymentId() + ".)");
                }
                
                Payment.save(session);
            }
        
            RequiredPaymentDateNew = SLibTimeUtils.isSameDate(RequiredPaymentDate, dateNew) ? null : dateNew;
            
            changed = true;
        }
        
        return changed;
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
            case 7:
                Download = (Boolean) value;
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
                value = Download;
                break;
            case 8:
                value = AlreadyDownloaded;
                break;
            case 9:
                value = FunctionalSubArea;
                break;
            case 10:
                value = FiscalUseCode;
                break;
            case 11:
                value = RevisionDatetime;
                break;
            case 12:
                value = getRequiredPaymentAmount();
                break;
            case 13:
                value = CurrencyCode;
                break;
            case 14:
                value = getRequiredPaymentPct();
                break;
            case 15:
                value = RequiredPaymentDate;
                break;
            case 16:
                value = RequiredPaymentDateNew;
                break;
            case 17:
                value = IsRequiredPaymentLoc;
                break;
            case 18:
                value = RequiredPaymentNotes;
                break;
            case 19:
                value = !isPaymentRequested() ? null : Payment.getFolio();
                break;
            case 20:
                value = !isPaymentRequested() ? null : Payment.getDateApplication();
                break;
            case 21:
                value = PayDefinitions.get(RequiredPaymentDefinition);
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public int compareTo(SImportedProforma o) {
        return this.toString().compareTo(o.toString());
    }
    
    @Override
    public String toString() {
        return "Emisor: " + BizPartner + "; " // allways available
                + "Folio: " + getFolio() + "; " // allways available
                + "Fecha: " + SLibUtils.DateFormatDate.format(Date) + "; " // allways available
                + "Total: $" + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode // allways available
                + (!FunctionalSubArea.isEmpty() ? "; Subárea funcional: " + FunctionalSubArea : "") // may not be available
                + (ExternalDocumentId != 0 ? "; ID documento: " + ExternalDocumentId : "") // may not be available
                + ".";
    }
    
    public static PreparedStatement createPrepStatementToGetProcessedProformaByExternalId(final Statement statement) throws Exception {
        String sql = "SELECT sdp.id_swap_data_prc AS id_swap_data_prc, sdp.fk_pay_n AS fk_pay_n, sdp.data_type AS data_type, "
                + "sdp.ext_data_id AS ext_data_id, un.id_usr AS id_usr, un.usr AS usr "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SWAP_DATA_PRC) + " AS sdp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON "
                + "un.id_usr = sdp.fk_usr_ins "
                + "WHERE NOT sdp.b_del AND sdp.data_type = ? AND sdp.txn_cat = ? AND sdp.ext_data_id = ?;";
        
        return statement.getConnection().prepareStatement(sql);
    }
    
    /**
     * Get Processed Proforma from SWAP processed data, if any, by its external ID.
     * @param preparedStatement Prepared statement.
     * @param dataType Constants DATA_TYPE...: INV = invoice; DB = debit note; CN = credit note; PRF = proforma.
     * @param txnCategory Transaction category: 1 = purchase; 2 = sales.
     * @param externalId External ID.
     * @return A Processed Proforma if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static SImportedProforma.ProcessedProforma getProcessedProformaByExternalId(final PreparedStatement preparedStatement, final String dataType, final int txnCategory, final int externalId) throws Exception{
        SImportedProforma.ProcessedProforma processedProforma = null;
        
        preparedStatement.setString(1, dataType);
        preparedStatement.setInt(2, txnCategory);
        preparedStatement.setInt(3, externalId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                processedProforma = new SImportedProforma.ProcessedProforma(resultSet.getInt("id_swap_data_prc"), resultSet.getString("data_type"),resultSet.getInt("ext_data_id"),resultSet.getInt("fk_pay_n"),resultSet.getInt("id_usr"), resultSet.getString("usr"));
            }
        }
        return processedProforma;
    }
    
    /**
     * Create prepared statement to get Processed DPS from Payable or Receivable Accounts by its own document data.
     * @param statement DB statement.
     * @param dpsTypeKey Key of DPS type: (category, class & type).
     * @return A prepared statment with these columns: dps_id_year, dps_id_doc, rec_id_year, rec_id_per, rec_id_bkc, rec_id_tp_rec, rec_id_num, rec_cob_code.
     * @throws Exception 
     */
    public static PreparedStatement createPrepStatementToGetDpsKeyByDocData(final Statement statement, final int[] dpsTypeKey) throws Exception {
        String sql = "SELECT d.id_year AS dps_id_year, d.id_doc AS dps_id_doc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "WHERE NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                + "AND d.fid_ct_dps = " + dpsTypeKey[0] + " AND d.fid_cl_dps = " + dpsTypeKey[1] + " AND d.fid_tp_dps = " + dpsTypeKey[2] + " "
                + "AND d.fid_bp_r = ? AND d.dt = ? AND d.num_ser = ? AND d.num = ? AND d.tot_cur_r = ? AND d.fid_cur = ?;";
        
        return statement.getConnection().prepareStatement(sql);
    }
    
    /**
     * Get DPS primary key from Payable or Receivable Accounts, if any, by its own document data.
     * @param preparedStatement Prepared statement.
     * @param bizPartnerId Document's ID of business partner.
     * @param date Document's date.
     * @param numberSeries Document's folio series.
     * @param number Document's folio number.
     * @param total Document's net total.
     * @param currencyId Document's ID of currency.
     * @return A DPS primary key if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static int[] getDpsKeyByDocData(final PreparedStatement preparedStatement, final int bizPartnerId, final Date date, final String numberSeries, final String number, final double total, final int currencyId) throws Exception {
        int[] dpsKey = null;
        
        preparedStatement.setInt(1, bizPartnerId);
        preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
        preparedStatement.setString(3, numberSeries);
        preparedStatement.setString(4, number);
        preparedStatement.setDouble(5, total);
        preparedStatement.setInt(6, currencyId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                dpsKey = new int[] { resultSet.getInt("dps_id_year"), resultSet.getInt("dps_id_doc") };
            }
        }
        
        return dpsKey;
    }
    
    /**
     * In memory Processed Proforma.
     */
    public static class ProcessedProforma implements Serializable{
        public int SwapDataProcessingId;
        public String DataType;
        public int ExtDataId;
        public int FkPay;
        public int UsrId;
        public String Usr;
        
        public ProcessedProforma(final int swapDataProcessingId, final String dataType, final int extDataId, final int fkPay, final int usrId, final String usr) {
            SwapDataProcessingId = swapDataProcessingId;
            DataType = dataType;
            ExtDataId = extDataId;
            FkPay = fkPay;
            UsrId = usrId;
            Usr = usr;
        }
        
        public int getDpsKey() {
            int key = 0;
            
            key = ExtDataId;
            
            return key;
        }
        
        public int getPayKey() {
            int key = 0;
            key = FkPay;
            
            return key;
        }
        
    }
    
    public static class Reference implements Serializable {
        
        public int ReferenceType;
        public String Reference;
        public int DpsYearId;
        public int DpsDocId;
        
        public Reference(final int referenceType, final String reference, final SImportUtils.DpsKey dpsKey) {
            this(referenceType, reference, dpsKey != null ? dpsKey.YearId : 0, dpsKey != null ? dpsKey.DocId : 0);
        }
        
        public Reference(final int referenceType, final String reference, final int dpsYearId, final int dpsDocId) {
            ReferenceType = referenceType;
            Reference = reference;
            DpsYearId = dpsYearId;
            DpsDocId = dpsDocId;
        }
        
        public SImportUtils.DpsKey createDpsKey() {
            return DpsYearId != 0 && DpsDocId != 0 ? new SImportUtils.DpsKey(DpsYearId, DpsDocId) : null;
        }
    }
}