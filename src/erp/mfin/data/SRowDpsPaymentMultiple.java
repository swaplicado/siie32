package erp.mfin.data;

import java.util.Date;
import sa.lib.SLibUtils;

/**
 * Renglón del grid del diálogo de pagos múltiples a documentos de clientes y proveedores.
 * @author Sergio Flores
 */
public class SRowDpsPaymentMultiple extends erp.lib.table.STableRow {
    
    public static final int COL_IS_PAYED = 9;
    public static final int COL_PAYMENT_CY = 10;
    public static final int COL_PAYMENT_XRT = 11;
    
    public int RecYear;
    public int[] DpsKey;
    public String DpsNumber;
    public Date DpsDate;
    public Date DpsDateStartCredit;
    public int DpsCreditDays;
    public Date DpsDateDue;
    public double Total;
    public double TotalXrt;
    public double TotalCy;
    public double Balance;
    public double BalanceCy;
    public boolean IsPayed;
    public double Payment;
    public double PaymentXrt;
    public double PaymentCy;
    public int[] DpsTypeKey;
    public String DpsTypeName;
    public String DpsTypeCode;
    public int CurrencyId;
    public String CurrencyName;
    public String CurrencyCode;
    public int BizPartnerId;
    public String BizPartnerName;
    public String BizPartnerFiscalId;
    public int ComBranchId;
    public String ComBranchCode;
    public int FuncAreaId;
    public String FuncAreaName;
    public String FuncAreaCode;
    
    public SRowDpsPaymentMultiple() {
        RecYear = 0;
        DpsKey = null;
        DpsNumber = "";
        DpsDate = null;
        DpsDateStartCredit = null;
        DpsCreditDays = 0;
        DpsDateDue = null;
        Total = 0.0;
        TotalXrt = 0.0;
        TotalCy = 0.0;
        Balance = 0.0;
        BalanceCy = 0.0;
        IsPayed = false;
        Payment = 0.0;
        PaymentXrt = 0.0;
        PaymentCy = 0.0;
        DpsTypeKey = null;
        DpsTypeName = "";
        DpsTypeCode = "";
        CurrencyId = 0;
        CurrencyName = "";
        CurrencyCode = "";
        BizPartnerId = 0;
        BizPartnerName = "";
        BizPartnerFiscalId = "";
        ComBranchId = 0;
        ComBranchCode = "";
        FuncAreaId = 0;
        FuncAreaName = "";
        FuncAreaCode = "";
    }
    
    private void evaluatePayment() {
        IsPayed = PaymentCy != 0;
        Payment = SLibUtils.roundAmount(PaymentCy * PaymentXrt);
    }
    
    @Override
    public void prepareTableRow() {
        evaluatePayment();
        
        mvValues.clear();
        mvValues.add(DpsTypeCode); // 0
        mvValues.add(DpsNumber); // 1
        mvValues.add(DpsDate); // 2
        mvValues.add(DpsDateStartCredit); // 3
        mvValues.add(DpsCreditDays); // 4
        mvValues.add(DpsDateDue); // 5
        mvValues.add(FuncAreaCode); // 6
        mvValues.add(TotalCy); // 7
        mvValues.add(BalanceCy); // 8
        mvValues.add(IsPayed); // 9
        mvValues.add(PaymentCy); // 10
        mvValues.add(PaymentXrt); // 11
        mvValues.add(Payment); // 12
    }
    
    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case COL_IS_PAYED:
                value = IsPayed;
                break;
            case COL_PAYMENT_CY:
                value = PaymentCy;
                break;
            case COL_PAYMENT_XRT:
                value = PaymentXrt;
                break;
            default:
                // do nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case COL_IS_PAYED:
                IsPayed = value == null ? false : (Boolean) value;
                break;
            case COL_PAYMENT_CY:
                PaymentCy = value == null ? 0 : SLibUtils.roundAmount((Double) value);
                evaluatePayment();
                break;
            case COL_PAYMENT_XRT:
                PaymentXrt = value == null ? 0 : SLibUtils.roundAmount((Double) value);
                evaluatePayment();
                break;
            default:
                // do nothing
        }
    }
}
