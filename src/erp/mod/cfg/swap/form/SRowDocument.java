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
import erp.mod.fin.db.SDbPayment;
import erp.mod.trn.db.swap.SDbSwapDataProcessing;
import erp.mtrn.data.SDataDps;
import java.util.Date;
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
    
    public int ExternalDocumentId;
    public String ExternalDocumentUuid;
    public int BizPartnerId;
    public String BizPartner;
    public String NumberSeries;
    public String Number;
    public Date Date;
    public int ReferenceType;
    public String Reference;
    public String Description;
    public int FunctionalSubAreaId;
    public String FunctionalSubArea;
    public String FiscalUseCode;
    public double Total;
    public int CurrencyId;
    public String CurrencyCode;
    public Date RequiredPaymentDate;
    public double RequiredPaymentPct;
    public int StatusId;
    public String Status;
    public boolean Download;
    public boolean AlreadyDownloaded;
    public SDbSwapDataProcessing.ProcessedDps ProcessedDps;
    
    public Reference[] References;
    
    public SRowDocument() {
        
    }
    
    public String getFolio() {
        return NumberSeries + (NumberSeries.isEmpty() ? "" : "-") + Number;
    }
    
    public double getRequiredPaymentPct() {
        return RequiredPaymentPct / 100;
    }
    
    public double getRequiredPayment() {
        return Total * getRequiredPaymentPct();
    }
    
    public SDbPayment createAndSavePayment(final SGuiSession session, final SRowDocument document, final SDataDps dps) throws Exception {
        SDbPayment payment = null;
        
        if (RequiredPaymentDate == null) {
            throw new Exception("Este documento no tiene fecha requerida de pago.");
        }
        else if (RequiredPaymentPct == 0) {
            throw new Exception("Este documento no tiene porcentaje requerido de pago.");
        }
        else if (RequiredPaymentPct > 100) {
            throw new Exception("Este documento tiene un porcentaje requerido de pago mayor a 100%.");
        }
        else if (ProcessedDps == null) {
            throw new Exception("Este documento no tiene un comprobante vinculado.");
        }
        else if (dps == null) {
            throw new Exception("No se proporcionó el comprobante para generar el pago.");
        }
        else if (ProcessedDps.DpsYearId == 0 || ProcessedDps.DpsDocId == 0) {
            throw new Exception("El comprobante vinculado de este documento carece de su registro.");
        }
        else if (ProcessedDps.DpsYearId != dps.getPkYearId() || ProcessedDps.DpsDocId != dps.getPkDocId()) {
            throw new Exception("El comprobante vinculado de este documento es distinto al comprobante proporcionado para generar el pago.");
        }
        else {
            Date date = session.getCurrentDate();
            double exchangeRate = 1;
            
            if (!session.getSessionCustom().isLocalCurrency(new int[] { CurrencyId })) {
                exchangeRate = SDataUtilities.obtainExchangeRate((SClientInterface) session.getClient(), CurrencyId, date);
                if (exchangeRate == 0) {
                    throw new Exception("No se ha definido el tipo de cambio " + session.getSessionCustom().getLocalCurrencyCode() + "/" + CurrencyCode + " para el día '" + SLibUtils.DateFormatDate.format(date) + "'.");
                }
            }
            
            payment = new SDbPayment();
            
            //payment.setPkPaymentId(...);
            payment.setSeries("");
            payment.setNumber(0);
            payment.setDateApplication(date);
            payment.setDateRequired(RequiredPaymentDate);
            payment.setDateScheduler_n(null);
            payment.setDateExecute_n(null);
            payment.setPaymentCurrency(getRequiredPayment());
            payment.setPaymentExchangeRateApplication(exchangeRate);
            payment.setPaymentApplication(SLibUtils.roundAmount(getRequiredPayment() * exchangeRate));
            payment.setPaymentExchangeRate(0);
            payment.setPayment(0);
            payment.setPaymentWay(DCfdi40Catalogs.FDP_POR_DEF);
            payment.setPriority(0);
            payment.setNotes(this.getNotes());
            payment.setDeleted(this.isDeleted());
            payment.setFkStatusPaymentId(this.getFkStatusPaymentId());
            payment.setFkCurrencyId(this.getFkCurrencyId());
            payment.setFkBeneficiaryId(this.getFkBeneficiaryId());
            payment.setFkFunctionalAreaId(this.getFkFunctionalAreaId());
            payment.setFkFunctionalSubareaId(this.getFkFunctionalSubareaId());
            payment.setFkPayerCashBizPartnerBranchId_n(this.getFkPayerCashBizPartnerBranchId_n());
            payment.setFkPayerCashAccountingCashId_n(this.getFkPayerCashAccountingCashId_n());
            payment.setFkBeneficiaryBankBizParterBranchId_n(this.getFkBeneficiaryBankBizParterBranchId_n());
            payment.setFkBeneficiaryBankAccountCashId_n(this.getFkBeneficiaryBankAccountCashId_n());
            payment.setFkUserScheduledId(this.getFkUserScheduledId());
            payment.setFkUserExecutedId(this.getFkUserExecutedId());
            payment.setFkUserInsertId(this.getFkUserInsertId());
            payment.setFkUserUpdateId(this.getFkUserUpdateId());
            payment.setTsUserScheduledId(this.getTsUserScheduledId());
            payment.setTsUserExecuted(this.getTsUserExecuted());
            payment.setTsUserInsert(this.getTsUserInsert());
            payment.setTsUserUpdate(this.getTsUserUpdate());
        }
        
        return payment;
    }
    
    public boolean link(final SGuiSession session, final SDataDps dps) throws Exception {
        boolean linked = false;
        
        if (dps == null) {
            throw new Exception("No se proporcionón ningún comprobante para vincular a este documento.");
        }
        else if (ProcessedDps != null) {
            throw new Exception("Este documento ya tiene un comprobante vinculado (" + ProcessedDps.composeRecord() + ").");
        }
        else {
            if (dps.getDbmsRecordKey() == null) {
                throw new Exception("El comprobante a vincular ('" + dps.getDpsNumber() + "') no está contabilizado.");
            }
            else if (BizPartnerId != dps.getFkBizPartnerId_r()) {
                throw new Exception("El asociado de negocios de este documento "
                        + "('" + BizPartner + "', ID = " + BizPartnerId + ") "
                        + "es distinto al del comprobante a vincular "
                        + "('" + (String) session.readField(SModConsts.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SDbRegistry.FIELD_NAME) + "', ID = " + dps.getFkBizPartnerId_r() + " ).");
            }
            else if (!SLibTimeUtils.isSameDate(Date, dps.getDate())) {
                throw new Exception("El fecha de este documento "
                        + "('" + SLibUtils.DateFormatDate.format(Date) + "') "
                        + "es distinta a la del comprobante a vincular "
                        + "('" + SLibUtils.DateFormatDate.format(dps.getDate()) + "').");
            }
            else if (!NumberSeries.equals(dps.getNumberSeries())) {
                throw new Exception("El serie del folio de este documento "
                        + "('" + NumberSeries + "') "
                        + "es distinto al del comprobante a vincular "
                        + "('" + dps.getNumberSeries() + "').");
            }
            else if (!Number.equals(dps.getNumber())) {
                throw new Exception("El número del folio de este documento "
                        + "('" + Number + "') "
                        + "es distinto al del comprobante a vincular "
                        + "('" + dps.getNumber() + "').");
            }
            else if (!SLibUtils.compareAmount(Total, dps.getTotalCy_r())) {
                throw new Exception("El total de este documento "
                        + "($ " + SLibUtils.getDecimalFormatAmount().format(Total) + ") "
                        + "es distinto al del comprobante a vincular "
                        + "($ " + SLibUtils.getDecimalFormatAmount().format(dps.getTotalCy_r()) + ").");
            }
            else if (CurrencyId != dps.getFkCurrencyId()) {
                throw new Exception("El moneda de este documento "
                        + "('" + CurrencyCode + "', ID = " + CurrencyId + ") "
                        + "es distinto a la del comprobante a vincular "
                        + "('" + dps.getDbmsCurrencyKey() + "', ID = " + dps.getFkCurrencyId() + ").");
            }
            else {
                SDbSwapDataProcessing swapDataProcessing = new SDbSwapDataProcessing();
                
                //swapDataProcessing.setPkSwapDataProcessingId(...);
                swapDataProcessing.setDataType(SDbSwapDataProcessing.DATA_TYPE_INV);
                swapDataProcessing.setTransactionCategory(SDataConstantsSys.TRNS_CT_DPS_PUR);
                swapDataProcessing.setExternalDataId(ExternalDocumentId);
                swapDataProcessing.setExternalDataUuid(ExternalDocumentUuid);
                swapDataProcessing.setDeleted(false);
                swapDataProcessing.setSystem(false);
                swapDataProcessing.setFkDpsYearId_n(dps.getPkYearId());
                swapDataProcessing.setFkDpsDocId_n(dps.getPkDocId());
                swapDataProcessing.setFkPaymentId_n();
                //swapDataProcessing.setFkUserInsertId(...);
                //swapDataProcessing.setFkUserUpdateId(...);
                //swapDataProcessing.setTsUserInsert(...);
                //swapDataProcessing.setTsUserUpdate(...);
                
                Object[] recKey = (Object[]) dps.getDbmsRecordKey();
                
                int recYearId = (Integer) recKey[0];
                int recPeriodId = (Integer) recKey[1];
                int recBokkeepingCenterId = (Integer) recKey[2];
                String recRecordTypeId = (String) recKey[3];
                int recNumberId = (Integer) recKey[4];
                String recCompanyBranchCode = SDataRecord.getCompanyBranchCode(dps.getDbmsRecordKey(), session.getStatement());
                
                ProcessedDps = new SDbSwapDataProcessing.ProcessedDps(0, dps.getPkYearId(), dps.getPkDocId(), recYearId, recPeriodId, recBokkeepingCenterId, recRecordTypeId, recNumberId, recCompanyBranchCode);
                linked = true;
            }
        }
        
        return linked;
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
                value = Reference;
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
                value = ProcessedDps != null;
                break;
            case 10:
                value = Status;
                break;
            case 11:
                value = FunctionalSubArea;
                break;
            case 12:
                value = FiscalUseCode;
                break;
            case 13:
                value = getRequiredPayment();
                break;
            case 14:
                value = CurrencyCode;
                break;
            case 15:
                value = getRequiredPaymentPct();
                break;
            case 16:
                value = RequiredPaymentDate;
                break;
            case 17:
                value = ExternalDocumentId;
                break;
            case 18:
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
        return BizPartner + "; " + getFolio() + "; $" + SLibUtils.getDecimalFormatAmount().format(Total) + " " + CurrencyCode + "; ID" + ExternalDocumentId;
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
    
    public class Reference {
        
        public int ReferenceType;
        public String Reference;
    }
}
