/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver4.DCfdVer4Consts;
import cfd.ver40.DCfdi40Catalogs;
import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataRecord;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.trn.db.SDbSwapDataProcessing;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataPdf;
import erp.mtrn.data.SThinDps;
import java.io.File;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.JOptionPane;
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
 * @author Sergio Flores
 */
public class SImportedDocument implements SGridRow, Serializable, Comparable<SImportedDocument> {
    
    public static final int PAYMENT_DEFN_NOT_REQ = 0; // pago no requerido
    public static final int PAYMENT_DEFN_BY_AMT = 1; // pago definido por monto
    public static final int PAYMENT_DEFN_BY_PCT = 2; // pago definido por porcentaje
    
    public static final String EXC_DOC_NOT_PROCESSED = "Este documento no ha sido procesado, no tiene factura " + SSwapConsts.SIIE + ".";
    public static final String EXC_DOC_ALREADY_RECORDED_ = "Este documento ya fue procesado, tiene factura " + SSwapConsts.SIIE + " en la póliza contable: ";

    public static final String EXC_PAY_NOT_REQUESTABLE = "Este documento no tiene información para solicitar su pago.";
    public static final String EXC_PAY_NOT_REGISTERED = "Este documento no tiene solicitud de pago.";
    public static final String EXC_PAY_ALREADY_REGISTERED_ = "Este documento ya tiene solicitud de pago: ";
    
    private static final DecimalFormat RecPeriodFormat = new DecimalFormat("00");
    private static final DecimalFormat RecNumberFormat = new DecimalFormat(SLibUtils.textRepeat("0", SDataConstantsSys.NUM_LEN_FIN_REC));
    
    public int ExternalDocumentId;
    public String ExternalDocumentUuid;
    public int BizPartnerId;
    public String BizPartner;
    public String NumberSeries;
    public String Number;
    public Date Date;
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
    public int StatusId;
    public String Status;
    public boolean Download;
    public boolean AlreadyDownloaded;
    
    public ProcessedDps ProcessedDps;
    public SDbSwapDataProcessing SwapDataProcessing;
    public SDbPayment Payment;
    
    public Reference[] References;
    
    public SImportedDocument() {
        ExternalDocumentId = 0;
        ExternalDocumentUuid = "";
        BizPartnerId = 0;
        BizPartner = "";
        NumberSeries = "";
        Number = "";
        Date = null;
        ReferencesType = 0;
        ReferencesAsText = "";
        Description = "";
        FunctionalSubAreaId = 0;
        FunctionalSubArea = "";
        FiscalUseCode = "";
        Total = 0;
        CurrencyId = 0;
        CurrencyCode = "";
        RequiredPaymentDefinition = PAYMENT_DEFN_NOT_REQ;
        RequiredPaymentAmount = 0;
        RequiredPaymentPct = 0;
        RequiredPaymentDate = null;
        RequiredPaymentDateNew = null;
        IsRequiredPaymentLoc = false;
        RequiredPaymentNotes = "";
        StatusId = 0;
        Status = "";
        Download = false;
        AlreadyDownloaded = false;
        
        ProcessedDps = null;
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
     * Ger required payment percentage of document as a double between 0 and 1.
     * @return 
     */
    public double getRequiredPaymentPct() {
        return RequiredPaymentPct / 100;
    }
    
    /**
     * Ger required payment amount of document, directly defined or indirectly from required payment percentage.
     * @return 
     */
    public double getRequiredPaymentAmount() {
        return RequiredPaymentDefinition == PAYMENT_DEFN_BY_AMT ? RequiredPaymentAmount : (Total * getRequiredPaymentPct());
    }
    
    /**
     * Get effective required payment date.
     * @return 
     */
    public Date getRequiredPaymentDateEffective() {
        return RequiredPaymentDateNew != null ? RequiredPaymentDateNew : RequiredPaymentDate;
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
     * Check if document is processed.
     * @return 
     */
    public boolean isProcessed() {
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
            case SSwapConsts.TXN_DOC_TYPE_ORDER:
                refPrefix = SSwapConsts.TXN_DOC_REF_TYPE_ORDER_CODE;
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
     * @param validateIsProcessed Validate if document is already processed.
     * @return
     * @throws Exception 
     */
    private SDbPayment createAndSavePaymentRequest(final SGuiSession session, final SThinDps dps, final boolean validateIsProcessed) throws Exception {
        SDbPayment payment = null;
        
        if (RequiredPaymentDefinition == PAYMENT_DEFN_NOT_REQ) {
            throw new Exception("Este documento carece de la indicación de requerir un pago.");
        }
        else if (validateIsProcessed && !isProcessed()) {
            throw new Exception(EXC_DOC_NOT_PROCESSED);
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
        else if (dps == null) {
            throw new Exception("No se proporcionó ninguna factura para generar el pago.");
        }
        else if (!SLibUtils.compareKeys(ProcessedDps.getDpsKey(), (int[]) dps.getPrimaryKey())) {
            throw new Exception("La factura vinculada a este documento (PK = " + SLibUtils.textKey(ProcessedDps.getDpsKey()) + ") es distinta a la factura proporcionada para generar el pago (PK = " + SLibUtils.textKey((int[]) dps.getPrimaryKey()) + ").");
        }
        else {
            // check if first payment request already exists:
            
            payment = getPaymentRequestByDpsKey(session, ProcessedDps.getDpsKey());
            
            // create and save payment request:
            
            if (payment == null) {
                Date date = session.getCurrentDate();
                
                // throw exception if exchange rate is not available:
                double exchangeRate = SDocumentUtils.getExchangeRate(session, CurrencyId, date);

                // create & prepare payment and its single one payment entry:

                payment = new SDbPayment();
                
                SDbPaymentEntry singleEntry = new SDbPaymentEntry();
                payment.getChildEntries().add(singleEntry);
                
                payment.processPaymentAtApplication(session, getRequiredPaymentAmount(), CurrencyId, exchangeRate, IsRequiredPaymentLoc, 1, Total);

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
                payment.setReceiptPaymentRequired(dps.getFkPaymentTypeId() == SDataConstantsSys.TRNS_TP_PAY_CREDIT);
                payment.setRescheduled(false);
                payment.setExecutedManually(false);
                payment.setDeleted(false);
                payment.setSystem(true);
                payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_NEW);
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
     * Link document to given DPS, and optionally create its payment request.
     * @param session GUI session.
     * @param dpsKey DPS primary key.
     * @param allowLaterInvoice Allow link to a later issued invoice.
     * @param createPaymentRequest Create-payment-request flag.
     * @param docFilesDownloadSrvUrl URL of document files download service.
     * @return
     * @throws Exception 
     */
    public boolean link(final SGuiSession session, final int[] dpsKey, final boolean allowLaterInvoice, final boolean createPaymentRequest, final String docFilesDownloadSrvUrl) throws Exception {
        boolean linked = false;
        
        // Validate linkage:
        
        if (isProcessed()) {
            throw new Exception(EXC_DOC_ALREADY_RECORDED_ + ProcessedDps.composeRecord() + ".");
        }
        else {
            // Read DPS in its "thin" version:
            
            SThinDps dps = new SThinDps();
            dps.read(dpsKey, session.getStatement());
            
            // Validate DPS:
            
            if (dps.getDbmsRecordKey() == null) {
                throw new Exception("La factura a vincular a este documento, '" + dps.getDpsNumber() + "', no está contabilizada.");
            }
            else if (BizPartnerId != dps.getFkBizPartnerId_r()) {
                // match required:
                throw new Exception("El asociado de negocios de este documento, "
                        + "'" + BizPartner + "' (ID = " + BizPartnerId + "), "
                        + "es distinto al de la factura a vincular "
                        + "'" + (String) session.readField(SModConsts.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SDbRegistry.FIELD_NAME) + "' (ID = " + dps.getFkBizPartnerId_r() + ").");
            }
            else if (CurrencyId != dps.getFkCurrencyId()) {
                // match required:
                throw new Exception("La moneda de este documento, "
                        + CurrencyCode + ", "
                        + "es distinta a la de la factura a vincular, "
                        + dps.getDbmsCurrencyCode() + ".");
            }
            else if (!SLibUtils.compareAmount(Total, dps.getTotalCy_r()) && (
                    Math.abs(Total - dps.getTotalCy_r()) >= 1 ||
                    Math.abs(Total - dps.getTotalCy_r()) < 1 && session.getClient().showMsgBoxConfirm("Hay una diferencia entre el total de este documento y el de la factura a vincular de $" + SLibUtils.getDecimalFormatAmount().format(Total - dps.getTotalCy_r()) + " " + CurrencyCode + "\n"
                            + "¿Está seguro que desea hacer caso omiso y continuar?") != JOptionPane.YES_OPTION)) {
                // match required:
                throw new Exception("El total de este documento, "
                        + "$ " + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode + ", "
                        + "es distinto al de la factura a vincular, "
                        + "$ " + SLibUtils.getDecimalFormatAmount().format(dps.getTotalCy_r()) + " " + dps.getDbmsCurrencyCode() + ".");
            }
            else if ((!allowLaterInvoice && !SLibTimeUtils.isSameDate(Date, dps.getDate())) ||
                    (allowLaterInvoice && (dps.getDate().before(Date) || (dps.getDate().after(Date) && session.getClient().showMsgBoxConfirm("La fecha de la factura a vincular, "
                            + SLibUtils.DateFormatDate.format(dps.getDate()) + ", es posterior a la de este documento, " + SLibUtils.DateFormatDate.format(Date) + ".\n"
                            + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION)))) {
                // match required:
                throw new Exception("La fecha de este documento, "
                        + SLibUtils.DateFormatDate.format(Date) + ", "
                        + "es distinta a la de la factura a vincular, "
                        + SLibUtils.DateFormatDate.format(dps.getDate()) + ".");
            }
            else {
                String msgChooseOtherInvoice = "Favor de elegir una factura distinta a la '" + dps.getDpsNumber() + "' para vincularla a este documento.";

                // check folio number: it must match its counterpart in document, in DPS it is allways available:

                String msgTopic = "";
                String msgError = "";
                String msgConfirm = "";

                if (!Number.isEmpty()) {
                    // document has folio number:

                    msgTopic = "El número del folio de este documento, '" + Number + "', ";

                    if (!Number.toUpperCase().equals(dps.getNumber().toUpperCase())) {
                        // match required:
                        msgError = msgTopic + "es distinto al de la factura a vincular, '" + dps.getNumber() + "'.";
                    }
                }
                else {
                    // document does not have folio number:

                    msgTopic = "Este documento no tiene número de folio";

                    if (ExternalDocumentUuid.isEmpty()) {
                        // no UUID available to attempt to find similitudes:

                        // match required:
                        msgError = " ni UUID,\n"
                                + "mientras que el número de folio de la factura a vincular es '" + dps.getNumber() + "'.";
                    }
                    else {
                        // UUID available, attempt to find similitudes:

                        if (ExternalDocumentUuid.toUpperCase().equals(dps.getNumber().toUpperCase())) {
                            msgConfirm = msgTopic + ", pero su UUID, '" + ExternalDocumentUuid + "',\n"
                                    + "es igual al número del folio de la factura a vincular, '" + dps.getNumber() + "'.";
                        }
                        else if (dps.getNumber().length() >= DCfdVer4Consts.LEN_UUID_1ST_SEGMENT && dps.getNumber().length() < ExternalDocumentUuid.length() && ExternalDocumentUuid.toUpperCase().startsWith(dps.getNumber().toUpperCase())) {
                            msgConfirm = msgTopic + ", pero su UUID, '" + ExternalDocumentUuid + "',\n"
                                    + "inicia como el número del folio de la factura a vincular, '" + dps.getNumber() + "'.";
                        }
                        else {
                            // match required:
                            msgError = msgTopic + ", y su UUID, '" + ExternalDocumentUuid + "',\n"
                                    + "no tiene similitud con el número del folio de la factura a vincular, '" + dps.getNumber() + "'.";
                        }
                    }
                }

                // processs folio number validation:

                if (!msgError.isEmpty()) {
                    throw new Exception(msgError);
                }
                else if (!msgConfirm.isEmpty()) {
                    if (session.getClient().showMsgBoxConfirm(msgConfirm + "\n"
                            + "Sin embargo, es posible vincular la factura '" + dps.getDpsNumber() + "' a este documento.\n"
                            + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        throw new Exception(msgChooseOtherInvoice);
                    }
                }

                // check folio series: it must match its counterpart in document, in DPS it is not allways available:

                msgTopic = "";
                msgError = "";
                msgConfirm = "";

                if (!NumberSeries.isEmpty()) {
                    // document has folio series:

                    msgTopic = "La serie del folio de este documento, '" + NumberSeries + "', ";

                    if (!NumberSeries.toUpperCase().equals(dps.getNumberSeries().toUpperCase())) {
                        // match required:
                        msgError = msgTopic + "es distinta a la de la factura a vincular, '" + dps.getNumberSeries()+ "'.";
                    }
                    else if (dps.getNumberSeries().isEmpty()) {
                        // match required:
                        msgConfirm = msgTopic + "no corresponde a la de la factura a vincular porque esta carece de serie.";
                    }
                }
                else {
                    // document does not have folio series:

                    msgTopic = "Este documento no tiene serie de folio";

                    if (!dps.getNumberSeries().isEmpty()) {
                        msgConfirm = msgTopic + ", y no corresponde a la de la factura a vincular porque su serie es '" + dps.getNumberSeries() + "'.";
                    }
                }

                // processs folio series validation:

                if (!msgError.isEmpty()) {
                    throw new Exception(msgError);
                }
                else if (!msgConfirm.isEmpty()) {
                    if (session.getClient().showMsgBoxConfirm(msgConfirm + "\n"
                            + "Sin embargo, es posible vincular la factura '" + dps.getDpsNumber() + "' a este documento.\n"
                            + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        throw new Exception(msgChooseOtherInvoice);
                    }
                }

                // link document to invoice:

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

                // check if first payment request already exists:

                SDbPayment payment = getPaymentRequestByDpsKey(session, ProcessedDps.getDpsKey());

                if (createPaymentRequest && isPaymentRequestDataAvailable() && payment == null) {
                    payment = createAndSavePaymentRequest(session, dps, false);
                }

                // create DPS processing:

                SDbSwapDataProcessing swapDataProcessing = new SDbSwapDataProcessing();

                //swapDataProcessing.setPkSwapDataProcessingId(...);
                swapDataProcessing.setDataType(SDbSwapDataProcessing.DATA_TYPE_INV);
                swapDataProcessing.setTransactionCategory(SDataConstantsSys.TRNS_CT_DPS_PUR);
                swapDataProcessing.setExternalDataId(ExternalDocumentId);
                swapDataProcessing.setExternalDataUuid(ExternalDocumentUuid);
                swapDataProcessing.setExternalDataAuthorizationHistory("");
                swapDataProcessing.setDpsReferences(composeReferences());
                swapDataProcessing.setDpsDescription(Description);
                swapDataProcessing.setDpsPaymentLocal(IsRequiredPaymentLoc);
                swapDataProcessing.setDeleted(false);
                swapDataProcessing.setSystem(false);
                swapDataProcessing.setFkDpsYearId_n(dps.getPkYearId());
                swapDataProcessing.setFkDpsDocId_n(dps.getPkDocId());
                swapDataProcessing.setFkPaymentId_n(payment == null ? 0 : payment.getPkPaymentId());
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
                
                boolean isBizPartnerDomestic = SDataBizPartner.checkIsDomestic(BizPartnerId, (SClientInterface) session.getClient());
                boolean attachXml = isBizPartnerDomestic && dps.getThinCfd() == null;
                boolean attachPdf = dps.getThinPdf() == null;
                
                if (attachXml || attachPdf) {
                    File[] files = SImportUtils.downloadDocumentCfdiFilesInTempDir(session, docFilesDownloadSrvUrl, ExternalDocumentId);
                    
                    if (files != null && files.length == 2) {
                        if (attachXml && files[SImportUtils.CFDI_XML] != null) {
                            // attach CFD:
                            
                            SDataCfd cfd = SDataCfd.prepareCfd(
                                    null, 
                                    files[SImportUtils.CFDI_XML], 
                                    session.getUser().getPkUserId());
                            
                            cfd.setFkDpsYearId_n(dps.getPkYearId());
                            cfd.setFkDpsDocId_n(dps.getPkDocId());
                            cfd.setTimestamp(dps.getDate());

                            if (cfd.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + "\n(Registro CFD.)");
                            }
                            
                            ProcessedDps.HasCfd = true;
                        }
                        
                        if (attachPdf && files[SImportUtils.CFDI_PDF] != null) {
                            // attach PDF:
                            
                            SDataPdf pdf = SDataPdf.preparePdf(
                                    null, 
                                    files[SImportUtils.CFDI_PDF], 
                                    dps.getPkYearId(), 
                                    ((SDataParamsCompany) session.getConfigCompany()).getXmlBaseDirectory());
                            
                            pdf.setPkYearId(dps.getPkYearId());
                            pdf.setPkDocId(dps.getPkDocId());

                            if (pdf.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + "\n(Registro PDF.)");
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
        
        if (!isProcessed()) {
            throw new Exception(EXC_DOC_NOT_PROCESSED);
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
     * Retrieve document existing provessiog.
     * @param session GUI session.
     * @param preparedStatement Prepared statement.
     * @param dataType Constants DATA_TYPE...: INV = invoice; DB = debit note; CN = credit note.
     * @param txnCategory Transaction category: 1 = purchase; 2 = sales.
     * @param externalId External ID.
     * @throws Exception 
     */
    public void retrieveProcessing(final SGuiSession session, final PreparedStatement preparedStatement, final String dataType, final int txnCategory, final int externalId) throws Exception {
        ProcessedDps = SImportedDocument.getProcessedDpsByExternalId(preparedStatement, dataType, txnCategory, externalId);

        if (ProcessedDps != null) {
            SwapDataProcessing = (SDbSwapDataProcessing) session.readRegistry(SModConsts.TRN_SWAP_DATA_PRC, new int[] { ProcessedDps.SwapDataProcessingId });

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
        if (!isProcessed()) {
            throw new Exception(EXC_DOC_NOT_PROCESSED);
        }
        else if (isPaymentRequested()) {
            throw new Exception(EXC_PAY_ALREADY_REGISTERED_ + Payment.getFolio() + ", " + SLibUtils.DateFormatDate.format(Payment.getDateApplication()) + ".");
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
     * Validate if a new payment request can be created.
     * @return
     * @throws Exception 
     */
    private boolean validateRequiredPaymentDateChanging() throws Exception {
        if (!isProcessed()) {
            throw new Exception(EXC_DOC_NOT_PROCESSED);
        }
        else if (!isPaymentRequested()) {
            throw new Exception(EXC_PAY_NOT_REGISTERED);
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
                // make due date of document match the new required date:
                SImportUtils.updateDpsDaysOfCreditByDueDate(session, ProcessedDps.getDpsKey(), dateNew);

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
    
    /**
     * Create a new DPS from this document.
     * @param session GUI session.
     * @param order Order, can be <code>null</code>.
     * @return 
     */
    public SDataDps createDps(final SGuiSession session, final SDataDps order) throws Exception {
        int year = SLibTimeUtils.digestYear(Date)[0];
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.BPSU_BP, new int[] { BizPartnerId }, SLibConstants.EXEC_MODE_STEALTH);
        SDbFunctionalSubArea functionalSubArea = (SDbFunctionalSubArea) session.readRegistry(SModConsts.CFGU_FUNC_SUB, new int[] { FunctionalSubAreaId });
        Date dueDate = getRequiredPaymentDateEffective();
        
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
        if (order != null) {
            dps.setAccountingTag(order.getAccountingTag());
        }
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
        dps.setFkPaymentTypeId(dueDate != null ? SDataConstantsSys.TRNS_TP_PAY_CREDIT : SDataConstantsSys.TRNS_TP_PAY_CASH);
        dps.setFkPaymentSystemTypeId(SDataConstantsSys.TRNU_TP_PAY_SYS_NA);
        dps.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
        dps.setFkDpsValidityStatusId(SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
        dps.setFkDpsAuthorizationStatusId(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA);
        dps.setFkDpsAnnulationTypeId(SModSysConsts.TRNU_TP_DPS_ANN_NA);
        if (order != null) {
            dps.setFkDpsNatureId(order.getFkDpsNatureId());
        }
        dps.setFkCompanyBranchId(((SClientInterface) session.getClient()).getSessionXXX().getCurrentCompanyBranchId());
        dps.setFkFunctionalAreaId(functionalSubArea.getFkFunctionalAreaId());
        dps.setFkFunctionalSubAreaId(functionalSubArea.getPkFunctionalSubAreaId());
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
        //dps.setFkContactBizPartnerBranchId_n(
        //dps.setFkContactContactId_n(
        //dps.setFkTaxIdentityEmisorTypeId(
        //dps.setFkTaxIdentityReceptorTypeId(
        dps.setFkLanguajeId(bizPartner.getDbmsCategorySettingsSup().getFkLanguageId_n());
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
        
        if (dueDate != null) {
            dps.setDaysOfCreditByDueDate(dueDate);
        }
        
        dps.setAuxKeepDpsData(true);
        dps.setXtaImportedDocument(this);
        
        return dps;
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
                value = isProcessed();
                break;
            case 10:
                value = !isProcessed() ? null : ProcessedDps.composeRecord();
                break;
            case 11:
                value = !isProcessed() ? false : ProcessedDps.HasCfd;
                break;
            case 12:
                value = !isProcessed() ? false : ProcessedDps.HasPdf;
                break;
            case 13:
                value = Status;
                break;
            case 14:
                value = FunctionalSubArea;
                break;
            case 15:
                value = FiscalUseCode;
                break;
            case 16:
                value = getRequiredPaymentAmount();
                break;
            case 17:
                value = CurrencyCode;
                break;
            case 18:
                value = getRequiredPaymentPct();
                break;
            case 19:
                value = RequiredPaymentDate;
                break;
            case 20:
                value = RequiredPaymentDateNew;
                break;
            case 21:
                value = IsRequiredPaymentLoc;
                break;
            case 22:
                value = RequiredPaymentNotes;
                break;
            case 23:
                value = !isPaymentRequested() ? null : Payment.getFolio();
                break;
            case 24:
                value = !isPaymentRequested() ? null : Payment.getDateApplication();
                break;
            case 25:
                value = ExternalDocumentId;
                break;
            case 26:
                value = ExternalDocumentUuid;
                break;
            case 27:
                value = !isProcessed() ? null : ProcessedDps.DpsFolio;
                break;
            case 28:
                value = !isProcessed() ? null : ProcessedDps.DpsDate;
                break;
            case 29:
                value = !isProcessed() ? null : ProcessedDps.DpsTotalCy;
                break;
            case 30:
                value = !isProcessed() ? null : ProcessedDps.DpsCurrencyCode;
                break;
            case 31:
                String string = null;
                if (isProcessed()) {
                    boolean isTotalOk = SLibUtils.compareAmount(Total, ProcessedDps.DpsTotalCy);
                    boolean isCurrencyOk = CurrencyCode.equals(ProcessedDps.DpsCurrencyCode);
                    
                    if (isTotalOk && isCurrencyOk) {
                        string = "OK";
                    }
                    else {
                        if (!isTotalOk) {
                            string = "Dif. total: " + SLibUtils.getDecimalFormatAmount().format(ProcessedDps.DpsTotalCy - Total);
                        }
                        if (!isCurrencyOk) {
                            string = (string == null ? "" : string + "; ") + "Dif. moneda: " + ProcessedDps.DpsCurrencyCode;
                        }
                    }
                }
                value = string;
                break;
            default:
                // nothing
        }
        
        return value;
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
    public String toString() {
        return "Emisor: " + BizPartner + "; " // allways available
                + "Folio: " + getFolio() + "; " // allways available
                + "Fecha: " + SLibUtils.DateFormatDate.format(Date) + "; " // allways available
                + "Total: $ " + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode // allways available
                + (!FunctionalSubArea.isEmpty() ? "; Subárea funcional: " + FunctionalSubArea : "") // may not be available
                + (ExternalDocumentId != 0 ? "; ID documento: " + ExternalDocumentId : "") // may not be available
                + ".";
    }

    @Override
    public int compareTo(SImportedDocument o) {
        return this.toString().compareTo(o.toString());
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
    
    /**
     * Create prepared statement to get Processed DPS from SWAP processed data by its external ID.
     * @param statement DB statement.
     * @return A prepared statment with these columns: id_swap_data_prc, dps_id_year, dps_id_doc, rec_id_year, rec_id_per, rec_id_bkc, rec_id_tp_rec, rec_id_num, rec_cob_code.
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
     * Get Processed DPS from SWAP processed data, if any, by its external ID.
     * @param preparedStatement Prepared statement.
     * @param dataType Constants DATA_TYPE...: INV = invoice; DB = debit note; CN = credit note.
     * @param txnCategory Transaction category: 1 = purchase; 2 = sales.
     * @param externalId External ID.
     * @return A Processed DPS if found, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static SImportedDocument.ProcessedDps getProcessedDpsByExternalId(final PreparedStatement preparedStatement, final String dataType, final int txnCategory, final int externalId) throws Exception {
        SImportedDocument.ProcessedDps processedDps = null;
        
        preparedStatement.setString(1, dataType);
        preparedStatement.setInt(2, txnCategory);
        preparedStatement.setInt(3, externalId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                processedDps = new SImportedDocument.ProcessedDps(resultSet.getInt("id_swap_data_prc"), resultSet.getInt("dps_id_year"), resultSet.getInt("dps_id_doc"), resultSet.getString("dps_folio"), resultSet.getDate("dps_date"), resultSet.getDouble("dps_tot_cur"), resultSet.getString("dps_cur_code"), 
                        resultSet.getInt("rec_id_year"), resultSet.getInt("rec_id_per"), resultSet.getInt("rec_id_bkc"), resultSet.getString("rec_id_tp_rec"), resultSet.getInt("rec_id_num"), resultSet.getString("rec_cob_code"), 
                        resultSet.getInt("un.id_usr"), resultSet.getString("un.usr"), resultSet.getInt("id_cfd") != 0, resultSet.getString("doc_pdf_name") != null);
            }
        }
        
        return processedDps;
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
     * Create a basic and elemental version of an imported document from SWAP data processed DPS to be used in SFormDps.
     * @param statement DB statement.
     * @param dpsKey DPS key.
     * @return 
     */
    public static SImportedDocument createBasicImportedDocumentFromProcessedDps(final Statement statement, final int[] dpsKey) throws Exception {
        SImportedDocument importedDocument = null;
        
        String sql = "SELECT sdp.ext_data_id, sdp.ext_data_uuid, sdp.dps_refs, sdp.dps_descrip, "
                + "d.id_year, d.id_doc, d.num_ser, d.num, d.dt, d.tot_cur_r, d.fid_func, d.fid_func_sub, "
                + "CONCAT(f.code, '" + SDbFunctionalSubArea.SEPARATOR + "', fs.name) AS _func_sub, "
                + "b.id_bp, b.bp, c.id_cur, c.cur_key, dc.cfd_use "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SWAP_DATA_PRC) + " AS sdp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON d.id_year = sdp.fk_dps_year_n AND d.id_doc = sdp.fk_dps_doc_n "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = d.fid_bp_r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = d.fid_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON f.id_func = d.fid_func "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = d.fid_func_sub "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_CFD) + " AS dc ON dc.id_year = d.id_year AND dc.id_doc = d.id_doc "
                + "WHERE NOT sdp.b_del AND sdp.data_type = '" + SDbSwapDataProcessing.DATA_TYPE_INV + "' "
                + "AND sdp.fk_dps_year_n = " + dpsKey[0] + " AND sdp.fk_dps_doc_n = " + dpsKey[1] + ";";

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                importedDocument = new SImportedDocument();
                importedDocument.ExternalDocumentId = resultSet.getInt("sdp.ext_data_id");
                importedDocument.ExternalDocumentUuid = resultSet.getString("sdp.ext_data_uuid");
                importedDocument.BizPartnerId = resultSet.getInt("b.id_bp");
                importedDocument.BizPartner = resultSet.getString("b.bp");
                importedDocument.NumberSeries = resultSet.getString("d.num_ser");
                importedDocument.Number = resultSet.getString("d.num");
                importedDocument.Date = resultSet.getDate("d.dt");
                String referencesAsText = resultSet.getString("sdp.dps_refs");
                importedDocument.ReferencesType = referencesAsText.isEmpty() ? 0 : SSwapConsts.TXN_DOC_TYPE_ORDER;
                importedDocument.ReferencesAsText = referencesAsText;
                importedDocument.Description = resultSet.getString("sdp.dps_descrip");
                importedDocument.FunctionalSubAreaId = resultSet.getInt("d.fid_func_sub");
                importedDocument.FunctionalSubArea = resultSet.getString("_func_sub");
                String fiscalUseCode = resultSet.getString("dc.cfd_use"); // can be null
                importedDocument.FiscalUseCode = resultSet.wasNull() ? "" : fiscalUseCode;
                importedDocument.Total = resultSet.getDouble("d.tot_cur_r");
                importedDocument.CurrencyId = resultSet.getInt("c.id_cur");
                importedDocument.CurrencyCode = resultSet.getString("c.cur_key");
                importedDocument.RequiredPaymentDefinition = PAYMENT_DEFN_NOT_REQ;
                importedDocument.RequiredPaymentAmount = 0;
                importedDocument.RequiredPaymentPct = 0;
                importedDocument.RequiredPaymentDate = null;
                importedDocument.RequiredPaymentDateNew = null;
                importedDocument.IsRequiredPaymentLoc = false;
                importedDocument.RequiredPaymentNotes = "";
                importedDocument.StatusId = 0;
                importedDocument.Status = "";
                importedDocument.Download = false;
                importedDocument.AlreadyDownloaded = false;

                importedDocument.ProcessedDps = null;
                importedDocument.SwapDataProcessing = null;
                importedDocument.Payment = null;

                importedDocument.References = null;
            }
        }
        
        return importedDocument;
    }
    
    /**
     * In memory Processed DPS.
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
