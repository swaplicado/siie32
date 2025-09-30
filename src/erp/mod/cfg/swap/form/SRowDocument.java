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
import erp.mfin.data.SDataRecord;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.trn.db.SDbSwapDataProcessing;
import erp.mtrn.data.SThinDps;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 * In memory document received from SWAP Services.
 * @author Sergio Flores
 */
public class SRowDocument implements SGridRow, Comparable<SRowDocument> {
    
    public static final int PAYMENT_DEFN_NOT_REQ = 0; // pago no requerido
    public static final int PAYMENT_DEFN_BY_AMT = 1; // pago definido por monto
    public static final int PAYMENT_DEFN_BY_PCT = 2; // pago definido por porcentaje
    
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
    public boolean IsRequiredPaymentLoc;
    public String RequiredPaymentNotes;
    public int StatusId;
    public String Status;
    public boolean Download;
    public boolean AlreadyDownloaded;
    
    public SDbSwapDataProcessing.ProcessedDps ProcessedDps;
    public SDbSwapDataProcessing SwapDataProcessing;
    public SDbPayment Payment;
    
    public Reference[] References;
    
    public SRowDocument() {
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
     * Get folio of documents in format series-number.
     * @return 
     */
    public String getFolio() {
        return NumberSeries + (NumberSeries.isEmpty() ? "" : "-") + Number;
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
     * Compose all references in a single semicolon separated string.
     * @return 
     */
    public String composeReferences() {
        String references = "";
        
        if (References != null) {
            for (Reference reference : References) {
                references += (references.isEmpty() ? "" : ";") + reference;
            }
        }
        
        return references;
    }
    
    /**
     * Check if document is recorded.
     * @return 
     */
    public boolean isRecorded() {
        return ProcessedDps != null;
    }
    
    /**
     * Check if document is processed.
     * @return 
     */
    public boolean isProcessed() {
        return isRecorded() && ProcessedDps.SwapDataProcessingId != 0 && SwapDataProcessing != null;
    }
    
    /**
     * Check if processing of document is with payment request.
     * @return 
     */
    public boolean isProcessedWithPaymentRequest() {
        return isProcessed() && SwapDataProcessing.getFkPaymentId_n() != 0;
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
    public boolean isPaymentRequestable() {
        return RequiredPaymentDate != null && (getRequiredPaymentPct() > 0 && getRequiredPaymentPct() <= 1) || getRequiredPaymentAmount() > 0;
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
     * @return
     * @throws Exception 
     */
    private SDbPayment createAndSavePaymentRequest(final SGuiSession session, final SThinDps dps) throws Exception {
        SDbPayment payment = null;
        
        if (RequiredPaymentDefinition == PAYMENT_DEFN_NOT_REQ) {
            throw new Exception("Este documento no requiere pago.");
        }
        else if (getRequiredPaymentAmount() == 0) {
            throw new Exception("Este documento no tiene monto requerido de pago.");
        }
        else if (RequiredPaymentPct == 0) {
            throw new Exception("Este documento no tiene porcentaje requerido de pago.");
        }
        else if (RequiredPaymentPct > 100) {
            throw new Exception("Este documento tiene un porcentaje requerido de pago mayor a 100%.");
        }
        else if (RequiredPaymentDate == null) {
            throw new Exception("Este documento no tiene fecha requerida de pago.");
        }
        else if (!isRecorded()) {
            throw new Exception("Este documento no tiene una factura vinculada.");
        }
        else if (dps == null) {
            throw new Exception("No se proporcionó ninguna factura para generar el pago.");
        }
        else if (ProcessedDps.getDpsKey() == null) {
            throw new Exception("La factura vinculada a este documento no está registrada.");
        }
        else if (!SLibUtils.compareKeys(ProcessedDps.getDpsKey(), (int[]) dps.getPrimaryKey())) {
            throw new Exception("La factura vinculada a este documento (PK = " + SLibUtils.textKey(ProcessedDps.getDpsKey()) + ") es distinta a la factura proporcionada para generar el pago (PK = " + SLibUtils.textKey((int[]) dps.getPrimaryKey()) + ").");
        }
        else {
            // check if first payment request already exists:
            
            payment = getPaymentRequestByDpsKey(session, ProcessedDps.getDpsKey());
            
            // create and save payment request:
            
            if (payment == null) {
                boolean isLocalCurrency = session.getSessionCustom().isLocalCurrency(new int[] { CurrencyId });
                Date date = session.getCurrentDate();
                double exchangeRate = 0;

                if (!isLocalCurrency) {
                    exchangeRate = SDataUtilities.obtainExchangeRate((SClientInterface) session.getClient(), CurrencyId, date);

                    if (exchangeRate == 0) {
                        throw new Exception("No se ha capturado el tipo de cambio " + session.getSessionCustom().getLocalCurrencyCode() + "/" + CurrencyCode + " para el día " + SLibUtils.DateFormatDate.format(date) + ".");
                    }
                }

                // create payment:

                payment = new SDbPayment();

                //payment.setPkPaymentId(...);
                payment.setSeries("");
                payment.setNumber(0);
                payment.setDateApplication(date);
                payment.setDateRequired(RequiredPaymentDate);
                payment.setDateSchedule_n(null);
                payment.setDateExecution_n(null);

                double paymentAmount;
                double paymentExchangeRate;
                int paymentCurrencyId;

                double entryConversionRate;

                /*
                Payment cases:

                1. currency == local + is_payment_local == true | false
                    payment data:
                        currency: local
                        computation: required payment
                        exchange rate: 1

                2. currency <> local + is_payment_local == true
                    payment data:
                        currency: local
                        computation: required payment * applicable exchange rate
                        exchange rate: 1

                3. currency <> local + is_payment_local == false
                    payment data:
                        currency: local
                        computation: required payment
                        exchange rate: applicable exchange rate
                */

                if (isLocalCurrency) {
                    // payment case 1:
                    paymentAmount = getRequiredPaymentAmount();
                    paymentExchangeRate = 1;
                    paymentCurrencyId = CurrencyId;

                    entryConversionRate = 1; // currencies conversion: from local to local
                }
                else {
                    if (IsRequiredPaymentLoc) {
                        // payment case 2:
                        paymentAmount = SLibUtils.roundAmount(getRequiredPaymentAmount() * exchangeRate);
                        paymentExchangeRate = 1;
                        paymentCurrencyId = session.getSessionCustom().getLocalCurrencyKey()[0];

                        entryConversionRate = 1 / exchangeRate; // currencies conversion: from local to foreign
                    }
                    else {
                        // payment case 3:
                        paymentAmount = getRequiredPaymentAmount();
                        paymentExchangeRate = exchangeRate;
                        paymentCurrencyId = CurrencyId;

                        entryConversionRate = 1; // currencies conversion: from foreign to the same foreign
                    }
                }

                payment.setPaymentCy(paymentAmount);
                payment.setPaymentExchangeRateApplication(paymentExchangeRate);
                payment.setPaymentApplication(SLibUtils.roundAmount(paymentAmount * paymentExchangeRate));
                payment.setPaymentExchangeRate(payment.getPaymentExchangeRateApplication()); // same value "at application"!
                payment.setPayment(payment.getPaymentApplication()); // same value "at application"!
                payment.setPaymentWay(DCfdi40Catalogs.FDP_POR_DEF);
                payment.setPriority(SDbPayment.PRIORITY_NORMAL);
                payment.setNotes(!RequiredPaymentNotes.isEmpty() ? RequiredPaymentNotes : "(Solicitud de pago generada en base a lo indicado por el comprador.)");
                payment.setNotesAuthorization("");
                payment.setReceiptPaymentRequired(dps.getFkPaymentTypeId() == SDataConstantsSys.TRNS_TP_PAY_CREDIT);
                payment.setDeleted(false);
                payment.setSystem(true);
                payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_NEW);
                payment.setFkCurrencyId(paymentCurrencyId);
                payment.setFkBeneficiaryId(BizPartnerId);
                payment.setFkFunctionalAreaId(dps.getFkFunctionalAreaId());
                payment.setFkFunctionalSubareaId(dps.getFkFunctionalSubAreaId());
                payment.setFkPayerCashBizPartnerBranchId_n(0);
                payment.setFkPayerCashAccountingCashId_n(0);
                payment.setFkBeneficiaryBankBizParterBranchId_n(0);
                payment.setFkBeneficiaryBankAccountCashId_n(0);
                payment.setFkUserScheduledId(SUtilConsts.USR_NA_ID);
                payment.setFkUserExecutedId(SUtilConsts.USR_NA_ID);
                //payment.setFkUserInsertId(...);
                //payment.setFkUserUpdateId(...);
                //payment.setTsUserScheduledId(...);
                //payment.setTsUserExecuted(...);
                //payment.setTsUserInsert(...);
                //payment.setTsUserUpdate(...);

                payment.setDbmsStatus((String) session.readField(SModConsts.FINS_ST_PAY, new int[] { payment.getFkStatusPaymentId() }, SDbRegistry.FIELD_NAME));

                // create payment entry:

                SDbPaymentEntry paymentEntry = new SDbPaymentEntry();

                //paymentEntry.setPkPaymentId(...);
                //paymentEntry.setPkEntryId(...);
                paymentEntry.setEntryType(SDbPaymentEntry.ENTRY_TYPE_PAYMENT);
                paymentEntry.setEntryPaymentCy(payment.getPaymentCy());
                paymentEntry.setEntryPaymentApplication(payment.getPaymentApplication());
                paymentEntry.setConversionRateApplication(entryConversionRate);
                paymentEntry.setDestinyPaymentApplicationEntryCy(getRequiredPaymentAmount());
                paymentEntry.setEntryPayment(paymentEntry.getEntryPaymentApplication()); // same value "at application"!
                paymentEntry.setConversionRate(paymentEntry.getConversionRateApplication()); // same value "at application"!
                paymentEntry.setDestinyPaymentEntryCy(paymentEntry.getDestinyPaymentApplicationEntryCy()); // same value "at application"!
                paymentEntry.setInstallment(1);
                paymentEntry.setDocBalancePreviousApplicationCy(Total);
                paymentEntry.setDocBalanceUnpaidApplicationCy_r(SLibUtils.roundAmount(Total - paymentEntry.getEntryPaymentCy()));
                paymentEntry.setDocBalancePreviousCy(paymentEntry.getDocBalancePreviousApplicationCy()); // same value "at application"!
                paymentEntry.setDocBalanceUnpaidCy_r(paymentEntry.getDocBalanceUnpaidApplicationCy_r()); // same value "at application"!
                paymentEntry.setFkDocYearId_n(dps.getPkYearId());
                paymentEntry.setFkDocDocId_n(dps.getPkDocId());
                paymentEntry.setFkEntryCurrencyId(CurrencyId);

                payment.getChildEntries().add(paymentEntry);

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
     * @param dps DPS to link.
     * @param createPaymentRequest Create-payment-request flag.
     * @return
     * @throws Exception 
     */
    public boolean link(final SGuiSession session, final SThinDps dps, final boolean createPaymentRequest) throws Exception {
        boolean linked = false;
        
        if (isRecorded()) {
            throw new Exception("Este documento ya tiene una factura vinculada (" + ProcessedDps.composeRecord() + ").");
        }
        else if (isProcessed()) {
            throw new Exception("Este documento ya está procesado (importado o capturado).");
        }
        else if (dps == null) {
            throw new Exception("No se proporcionó ninguna factura para vincular a este documento.");
        }
        else {
            if (dps.getDbmsRecordKey() == null) {
                throw new Exception("La factura a vincular a este documento (" + dps.getDpsNumber() + ") no está contabilizada.");
            }
            else if (BizPartnerId != dps.getFkBizPartnerId_r()) {
                throw new Exception("El asociado de negocios de este documento "
                        + "(" + BizPartner + ", ID = " + BizPartnerId + ") "
                        + "es distinto al de la factura a vincular "
                        + "(" + (String) session.readField(SModConsts.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SDbRegistry.FIELD_NAME) + ", ID = " + dps.getFkBizPartnerId_r() + " ).");
            }
            else if (!SLibTimeUtils.isSameDate(Date, dps.getDate())) {
                throw new Exception("El fecha de este documento "
                        + "(" + SLibUtils.DateFormatDate.format(Date) + ") "
                        + "es distinta a la de la factura a vincular "
                        + "(" + SLibUtils.DateFormatDate.format(dps.getDate()) + ").");
            }
            else if (NumberSeries.isEmpty() && !dps.getNumberSeries().isEmpty()) {
                throw new Exception("Este documento no tiene serie del folio, "
                        + "pero la factura a vincular sí lo tiene "
                        + "('" + dps.getNumberSeries() + "').");
            }
            else if (!NumberSeries.isEmpty() && dps.getNumberSeries().isEmpty()) {
                throw new Exception("Este documento sí tiene serie del folio "
                        + "('" + NumberSeries + "'), "
                        + "pero la factura a vincular no lo tiene.");
            }
            else if (!NumberSeries.equals(dps.getNumberSeries())) {
                throw new Exception("El serie del folio de este documento "
                        + "('" + NumberSeries + "') "
                        + "es distinto al de la factura a vincular "
                        + "('" + dps.getNumberSeries() + "').");
            }
            else if (!Number.equals(dps.getNumber())) {
                throw new Exception("El número del folio de este documento "
                        + "('" + Number + "') "
                        + "es distinto al de la factura a vincular "
                        + "('" + dps.getNumber() + "').");
            }
            else if (!SLibUtils.compareAmount(Total, dps.getTotalCy_r())) {
                throw new Exception("El total de este documento "
                        + "($ " + SLibUtils.getDecimalFormatAmount().format(Total) + ") "
                        + "es distinto al de la factura a vincular "
                        + "($ " + SLibUtils.getDecimalFormatAmount().format(dps.getTotalCy_r()) + ").");
            }
            else if (CurrencyId != dps.getFkCurrencyId()) {
                throw new Exception("El moneda de este documento "
                        + "('" + CurrencyCode + "', ID = " + CurrencyId + ") "
                        + "es distinto a la de la factura a vincular "
                        + "('" + dps.getDbmsCurrencyKey() + "', ID = " + dps.getFkCurrencyId() + ").");
            }
            else {
                Object[] recKey = (Object[]) dps.getDbmsRecordKey();
                
                int recYearId = (Integer) recKey[0];
                int recPeriodId = (Integer) recKey[1];
                int recBokkeepingCenterId = (Integer) recKey[2];
                String recRecordTypeId = (String) recKey[3];
                int recNumberId = (Integer) recKey[4];
                String recCompanyBranchCode = SDataRecord.getCompanyBranchCode(dps.getDbmsRecordKey(), session.getStatement());
                
                ProcessedDps = new SDbSwapDataProcessing.ProcessedDps(0, dps.getPkYearId(), dps.getPkDocId(), 
                        recYearId, recPeriodId, recBokkeepingCenterId, recRecordTypeId, recNumberId, recCompanyBranchCode);
                
                // check if first payment request already exists:

                SDbPayment payment = getPaymentRequestByDpsKey(session, ProcessedDps.getDpsKey());
                
                if (createPaymentRequest && isPaymentRequestable() && payment == null) {
                    payment = createAndSavePaymentRequest(session, dps);
                }
                
                // create DPS processing:
                
                SDbSwapDataProcessing swapDataProcessing = new SDbSwapDataProcessing();
                
                //swapDataProcessing.setPkSwapDataProcessingId(...);
                swapDataProcessing.setDataType(SDbSwapDataProcessing.DATA_TYPE_INV);
                swapDataProcessing.setTransactionCategory(SDataConstantsSys.TRNS_CT_DPS_PUR);
                swapDataProcessing.setExternalDataId(ExternalDocumentId);
                swapDataProcessing.setExternalDataUuid(ExternalDocumentUuid);
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
            throw new Exception("Este documento no tiene una factura vinculada.");
        }
        else if (!isProcessed()) {
            throw new Exception("Este documento no ha sido procesado (importado o capturado).");
        }
        else {
            if (!SwapDataProcessing.isDeleted()) {
                SwapDataProcessing.setDeleted(true);
                SwapDataProcessing.save(session);
            }

            ProcessedDps = null;
            SwapDataProcessing = null;
            Payment = null;
            
            unlinked = true;
        }
        
        return unlinked;
    }
    
    /**
     * Validate if a new payment request can be created.
     * @return
     * @throws Exception 
     */
    private boolean validatePaymentRequestCreation() throws Exception {
        if (!isRecorded()) {
            throw new Exception("Este documento no tiene una factura vinculada.");
        }
        else if (!isProcessed()) {
            throw new Exception("Este documento no ha sido procesado (importado o capturado).");
        }
        else if (isProcessedWithPaymentRequest() && !isPaymentRequested()) {
            throw new Exception("El procesamiento de este documento ya tiene una solicitud de pago (ID = " + SwapDataProcessing.getFkPaymentId_n() + ").");
        }
        else if (isPaymentRequested()) {
            throw new Exception("Este documento ya tiene una solicitud de pago ('" + Payment.getFolio() + "').");
        }
        else if (!isPaymentRequestable()) {
            throw new Exception("Este documento no tiene información para solicitar su pago.");
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
            
            SDbPayment payment = createAndSavePaymentRequest(session, dps);

            SwapDataProcessing.setFkPaymentId_n(payment.getPkPaymentId());
            SwapDataProcessing.save(session);

            Payment = payment;

            requested = true;
        }
        
        return requested;
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
                value = isRecorded();
                break;
            case 10:
                value = !isRecorded() ? "" : ProcessedDps.composeRecord();
                break;
            case 11:
                value = Status;
                break;
            case 12:
                value = FunctionalSubArea;
                break;
            case 13:
                value = FiscalUseCode;
                break;
            case 14:
                value = getRequiredPaymentAmount();
                break;
            case 15:
                value = CurrencyCode;
                break;
            case 16:
                value = getRequiredPaymentPct();
                break;
            case 17:
                value = RequiredPaymentDate;
                break;
            case 18:
                value = IsRequiredPaymentLoc;
                break;
            case 19:
                value = RequiredPaymentNotes;
                break;
            case 20:
                value = isPaymentRequested() ? Payment.getFolio() : null;
                break;
            case 21:
                value = isPaymentRequested() ? Payment.getDateApplication() : null;
                break;
            case 22:
                value = ExternalDocumentId;
                break;
            case 23:
                value = ExternalDocumentUuid;
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
        return BizPartner + "; " + getFolio() + "; $" + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode + "; ID " + ExternalDocumentId;
    }

    @Override
    public int compareTo(SRowDocument o) {
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
    
    public static class Reference {
        
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
