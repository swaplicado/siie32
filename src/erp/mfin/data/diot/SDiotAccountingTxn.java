/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data.diot;

import erp.data.SDataConstantsSys;
import erp.mtrn.data.SDataDps;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDiotAccountingTxn {
    
    private Statement miStatement;
    private int mnEntryUserId;
    private Object[] maoRecordKey;
    private int[] manBkkNumberKey;
    private SDataDps moDps; 
    private int[] manDpsKey;
    private ArrayList<Entry> maEntries;
    private ArrayList<ThirdTaxpayerTax> maThirdTaxpayerTaxes;
    
    /**
     * Create the representation of an accounting transaction, at the document level, if applicable.
     * @param statement
     * @param entryUserId
     * @param recordKey
     * @param bkkNumberKey
     * @param dps
     * @throws Exception 
     */
    public SDiotAccountingTxn(final Statement statement, final int entryUserId, final Object[] recordKey, final int[] bkkNumberKey, final SDataDps dps) throws Exception {
        miStatement = statement;
        mnEntryUserId = entryUserId;
        maoRecordKey = recordKey;
        manBkkNumberKey = bkkNumberKey;
        moDps = dps;
        manDpsKey = moDps == null ? new int[2] : (int[]) moDps.getPrimaryKey();
        
        createChildren();
    }
    
    private void createChildren() throws Exception {
        createEntries();
        createThirdTaxpayerTaxes();
    }
    
    /**
     * Create individual sumarized entries of system-movement types.
     * Should be called only by private method <code>createChildren()</code>.
     * @throws Exception 
     */
    private void createEntries() throws Exception {
        maEntries = new ArrayList<>();
        
        String sql = "SELECT re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_tax_bas_n, re.fid_tax_n, "
                + "SUM(re.debit) AS _debit, SUM(re.credit) AS _credit, "
                + "(SELECT SUM(det.tax) "
                + " FROM trn_dps_ety_tax AS det "
                + " WHERE det.id_year = " + manDpsKey[0] + " AND det.id_doc = " + manDpsKey[1] + " AND det.id_tax_bas = re.fid_tax_bas_n AND det.id_tax = re.fid_tax_n) AS _tax, "
                + "(SELECT SUM(de.stot_r) "
                + " FROM trn_dps_ety AS de "
                + " INNER JOIN trn_dps_ety_tax AS det ON de.id_year = det.id_year AND de.id_doc = det.id_doc AND de.id_ety = det.id_ety "
                + " WHERE NOT de.b_del AND de.id_year = " + manDpsKey[0] + " AND de.id_doc = " + manDpsKey[1] + " AND det.id_tax_bas = re.fid_tax_bas_n AND det.id_tax = re.fid_tax_n) AS _stot "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del AND "
                + "re.usr_id = " + mnEntryUserId + " AND "
                + "r.id_year = " + maoRecordKey[0] + " AND r.id_per = " + maoRecordKey[1] + " AND r.id_bkc = " + maoRecordKey[2] + " AND r.id_tp_rec = '" + maoRecordKey[3] + "' AND r.id_num = " + maoRecordKey[4] + " AND "
                + "re.fid_bkk_year_n " + (manBkkNumberKey[0] == 0 ? "IS NULL" : "= " + manBkkNumberKey[0]) + " AND re.fid_bkk_num_n " + (manBkkNumberKey[1] == 0 ? "IS NULL" : "= " + manBkkNumberKey[1]) + " AND "
                + "re.fid_dps_year_n " + (manDpsKey[0] == 0 ? "IS NULL" : "= " + manDpsKey[0]) + " AND re.fid_dps_doc_n " + (manDpsKey[1] == 0 ? "IS NULL" : "= " + manDpsKey[1]) + " "
                + "GROUP BY re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_tax_bas_n, re.fid_tax_n "
                + "ORDER BY re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_tax_bas_n, re.fid_tax_n;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                maEntries.add(new Entry(
                        new int[] { resultSet.getInt("re.fid_ct_sys_mov_xxx"), resultSet.getInt("re.fid_tp_sys_mov_xxx") },
                        new int[] { resultSet.getInt("re.fid_tax_bas_n"), resultSet.getInt("re.fid_tax_n") },
                        resultSet.getDouble("_debit"),
                        resultSet.getDouble("_credit"),
                        resultSet.getDouble("_tax"),
                        resultSet.getDouble("_stot")
                ));
            }
        }
    }
    
    /**
     * Create individual sumarized taxes of third taxpayers by tax type.
     * Should be called only by private method <code>createChildren()</code>.
     * @throws Exception 
     */
    private void createThirdTaxpayerTaxes() throws Exception {
        maThirdTaxpayerTaxes = new ArrayList<>();
        
        if (manDpsKey[0] != 0 && manDpsKey[1] != 0) {
            String sql = "SELECT de.fid_third_tax_n, de.stot_r, det.id_tax_bas, det.id_tax, SUM(det.tax) AS _tax "
                    + "FROM trn_dps AS d "
                    + "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                    + "INNER JOIN trn_dps_ety_tax AS det ON de.id_year = det.id_year AND de.id_doc = det.id_doc AND de.id_ety = det.id_ety "
                    + "WHERE NOT d.b_del AND NOT de.b_del AND de.fid_third_tax_n IS NOT NULL AND "
                    + "d.id_year = " + manDpsKey[0] + " and d.id_doc = " + manDpsKey[1] + " "
                    + "GROUP BY de.fid_third_tax_n, de.stot_r, det.id_tax_bas, det.id_tax "
                    + "ORDER BY de.fid_third_tax_n, de.stot_r, det.id_tax_bas, det.id_tax;";
            
            try (ResultSet resultSet = miStatement.executeQuery(sql)) {
                while (resultSet.next()) {
                    maThirdTaxpayerTaxes.add(new ThirdTaxpayerTax(
                            resultSet.getInt("de.fid_third_tax_n"), 
                            resultSet.getDouble("de.stot_r"), 
                            new int[] { resultSet.getInt("det.id_tax_bas"), resultSet.getInt("det.id_tax") },
                            resultSet.getDouble("_tax")
                    ));
                }
            }
        }
    }
    
    /**
     * Get Entry according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return Found entry, otherwise <code>null</code>.
     */
    private Entry getEntry(int[] sysMovTypeXxxKey, final int[] vatKey) {
        Entry entry = null;
        
        for (Entry e : maEntries) {
            if (SLibUtils.compareKeys(e.SysMovTypeXxxKey, sysMovTypeXxxKey) && SLibUtils.compareKeys(e.VatKey, vatKey)) {
                entry = e;
                break;
            }
        }
        
        return entry;
    }
    
    /**
     * Get sum of debit or credit of entries, according to type of system movement.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @return Debit or credit of entry.
     */
    private double getEntriesAmountSum(int[] sysMovTypeXxxKey) {
        double amount = 0;
        
        for (Entry entry : maEntries) {
            if (SLibUtils.compareKeys(entry.SysMovTypeXxxKey, sysMovTypeXxxKey)) {
                if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP)) {
                    amount = SLibUtils.roundAmount(amount + entry.getAmountAsDebit());
                }
                else if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT)) {
                    amount = SLibUtils.roundAmount(amount + entry.getAmountAsDebit());
                }
                else if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT)) {
                    amount = SLibUtils.roundAmount(amount + entry.getAmountAsCredit());
                }
            }
        }
        
        return amount;
    }
    
    /**
     * Get debit or credit of entry according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return Debit or credit of entry.
     */
    private double getEntryAmount(int[] sysMovTypeXxxKey, final int[] vatKey) {
        double amount = 0;
        Entry entry = getEntry(sysMovTypeXxxKey, vatKey);
        
        if (entry != null) {
            if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP)) {
                amount = entry.getAmountAsDebit();
            }
            else if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT)) {
                amount = entry.getAmountAsDebit();
            }
            else if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT)) {
                amount = entry.getAmountAsCredit();
            }
        }
        
        return amount;
    }
    
    /**
     * Get payment amount.
     * @return Payment amount.
     */
    public double getPaymentAmount() {
        return getEntryAmount(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP, null);
    }
    
    /**
     * Get VAT sum of all debits.
     * @return VAT sum of all debits.
     */
    public double getVatSumDebits() {
        return getEntriesAmountSum(SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT);
    }
    
    /**
     * Get VAT sum of all credits.
     * @return VAT sum of all credits.
     */
    public double getVatSumCredits() {
        return getEntriesAmountSum(SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT);
    }
    
    /**
     * Get DPS original tax according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return DPS original tax.
     */
    public double getEntryDpsTax(int[] sysMovTypeXxxKey, final int[] vatKey) {
        double dpsTax = 0;
        Entry entry = getEntry(sysMovTypeXxxKey, vatKey);
        
        if (entry != null) {
            dpsTax = entry.DpsTax;
        }
        
        return dpsTax;
    }
    
    /**
     * Get DPS original subtotal according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return DPS original tax.
     */
    public double getEntryDpsSubtotal(int[] sysMovTypeXxxKey, final int[] vatKey) {
        double dpsSubtotal = 0;
        Entry entry = getEntry(sysMovTypeXxxKey, vatKey);
        
        if (entry != null) {
            dpsSubtotal = entry.DpsSubtotal;
        }
        
        return dpsSubtotal;
    }
    
    /**
     * Get DPS original subtotal according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return DPS original tax.
     */
    public double getEntryDpsSubtotalRatio(int[] sysMovTypeXxxKey, final int[] vatKey) {
        double dpsSubtotalRatio = 0;
        Entry entry = getEntry(sysMovTypeXxxKey, vatKey);
        
        if (entry != null) {
            dpsSubtotalRatio = moDps == null || moDps.getTotal_r() == 0 ? 0 : entry.DpsSubtotal / moDps.getTotal_r();
        }
        
        return dpsSubtotalRatio;
    }
    
    /**
     * Get set of IDs of business partners of third taxpayers for supplied VAT.
     * @param vatKey Primary key of VAT. Can be null.
     * @return 
     */
    public HashSet<Integer> getThirdTaxpayerIds(final int[] vatKey) {
        HashSet<Integer> causings = new HashSet<>();
        
        for (ThirdTaxpayerTax thirdTax : maThirdTaxpayerTaxes) {
            if (vatKey == null || SLibUtils.compareKeys(vatKey, thirdTax.VatKey)) {
                causings.add(thirdTax.BizPartnerId);
            }
        }
        
        return causings;
    }
    
    /**
     * Get tax caused only by requested third taxpayer.
     * @param thirdTaxpayerId ID of third taxpayer.
     * @param vatKey PK of tax.
     * @return 
     */
    public double getThirdTaxpayerTax(final int thirdTaxpayerId, final int[] vatKey) {
        double tax = 0;
        
        for (ThirdTaxpayerTax thirdTax : maThirdTaxpayerTaxes) {
            if (thirdTax.BizPartnerId == thirdTaxpayerId && SLibUtils.compareKeys(thirdTax.VatKey, vatKey)) {
                tax = SLibUtils.roundAmount(tax + thirdTax.Tax);
                break;
            }
        }
        
        return tax;
    }
    
    /**
     * Get subtotal of tax caused only by requested third taxpayer.
     * @param thirdTaxpayerId ID of third taxpayer.
     * @param vatKey PK of tax.
     * @return 
     */
    public double getThirdTaxpayerTaxSubtotal(final int thirdTaxpayerId, final int[] vatKey) {
        double taxSubtotal = 0;
        
        for (ThirdTaxpayerTax thirdTax : maThirdTaxpayerTaxes) {
            if (thirdTax.BizPartnerId == thirdTaxpayerId && SLibUtils.compareKeys(thirdTax.VatKey, vatKey)) {
                taxSubtotal = SLibUtils.roundAmount(taxSubtotal + thirdTax.Subtotal);
                break;
            }
        }
        
        return taxSubtotal;
    }
    
    private class Entry {
        public int[] SysMovTypeXxxKey;
        public int[] VatKey;
        public double Debit;
        public double Credit;
        public double DpsTax;
        public double DpsSubtotal;
        
        public Entry(final int[] sysMovTypeXxxKey, final int[] vatKey, final double debit, final double credit, final double dpsTax, final double dpsSubtotal) {
            SysMovTypeXxxKey = sysMovTypeXxxKey;
            VatKey = vatKey == null ? null : (vatKey[0] == 0 && vatKey[1] == 0 ? null : vatKey);
            Debit = debit;
            Credit = credit;
            DpsTax = dpsTax;
            DpsSubtotal = dpsSubtotal;
        }
        
        public double getAmountAsDebit() {
            double amount = 0;
            
            if (Debit != 0) {
                amount = Debit;
            }
            else if (Credit < 0) {
                amount = -Credit;
            }
            
            return amount;
        }
        
        public double getAmountAsCredit() {
            double amount = 0;
            
            if (Credit != 0) {
                amount = Credit;
            }
            else if (Debit < 0) {
                amount = -Debit;
            }
            
            return amount;
        }
    }
    
    private class ThirdTaxpayerTax {
        public int BizPartnerId;
        public double Subtotal;
        public int[] VatKey;
        public double Tax;
        
        public ThirdTaxpayerTax(final int bizPartnerId, final double subtotal, final int[] vatKey, final double tax) {
            BizPartnerId = bizPartnerId;
            Subtotal = subtotal;
            VatKey = vatKey;
            Tax = tax;
        }
    }
}
